import requests
from lxml import etree
import csv

#根据recipeUrl.txt里的链接爬取菜谱数据
def recipeCrawb():
    #食材id列表
    shicaiIdx=1
    shicaiMap={}

    headers = {
        'User-Agent':'Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36 QIHU 360SE'
    }
    relations=[]
    URLList=[]
    with open("./recipeUrl.txt", mode='r', encoding='utf-8') as f:
        URLList = f.readlines()

    recipeIdx=1
    recipeList=[]

    time = 0
    for URL in URLList:
        # 输入在浏览器的网址
        if(time%100 == 0):
            print("好")
        time +=1
        res = requests.get(URL[:-1],headers=headers)
        res.encoding = 'utf-8'
        dom = etree.HTML(res.text)
        tmp={}
        tmp['id']=recipeIdx
        name = dom.xpath("//div[@class='userTop clear']/h1[@class='recipe_De_title']/a[@id='recipe_title']/text()")
        tmp['name'] = name[0] if len(name)>0 else ''
        tupian = dom.xpath("//div[@class='wrap']/div[@class='w clear']/div[@class='space_left']/div[@class='space_box_home']/div[@class='recipDetail']/div[@id='recipe_De_imgBox']/a[@class='J_photo']/img/@src")
        tmp['tupian'] = tupian[0] if len(tupian) > 0 else ''
        haoshi=dom.xpath("//div[@class='wrap']/div[@class='w clear']/div[@class='space_left']/div[@class='space_box_home']/div[@class='recipDetail']/div[@class='recipeCategory_sub_R mt30 clear']/ul/li[3]/span[@class='category_s1']/a/text()")
        tmp['haoshi'] = haoshi[0] if len(haoshi) > 0 else ''
        kouwei=dom.xpath("//div[@class='wrap']/div[@class='w clear']/div[@class='space_left']/div[@class='space_box_home']/div[@class='recipDetail']/div[@class='recipeCategory_sub_R mt30 clear']/ul/li[1]/span[@class='category_s1']/a/text()")
        tmp['kouwei'] = kouwei[0] if len(kouwei) > 0 else ''
        gongyi=dom.xpath("//div[@class='wrap']/div[@class='w clear']/div[@class='space_left']/div[@class='space_box_home']/div[@class='recipDetail']/div[@class='recipeCategory_sub_R mt30 clear']/ul/li[2]/span[@class='category_s1']/a/text()")
        tmp['gongyi'] = gongyi[0] if len(gongyi) > 0 else ''
        tmp['label']='recipe'
        #遍历得到食材
        relationDes=['主食材','辅料','辅料']
        for j in range(0,3):
            i = 1
            while(1):
                relation={}
                relation['source'] = recipeIdx
                url = dom.xpath("//div[@class='wrap']/div[@class='w clear']/div[@class='space_left']/div[@class='space_box_home']/div[@class='recipDetail']/fieldset[@class='particulars']["+str(j+1)+"]/div[@class='recipeCategory_sub_R clear']/ul/li["+str(i)+"]/span[@class='category_s1']/a/@href")
                yongliang= dom.xpath("//div[@class='wrap']/div[@class='w clear']/div[@class='space_left']/div[@class='space_box_home']/div[@class='recipDetail']/fieldset[@class='particulars']["+str(j+1)+"]/div[@class='recipeCategory_sub_R clear']/ul/li["+str(i)+"]/span[@class='category_s2']/text()")
                i+=1
                if(url==[]):
                    break
                if(shicaiMap.get(url[0])==None): #列表中还没有这个食材
                    shicaiMap[url[0]]=shicaiIdx
                    relation['target'] = shicaiIdx
                    shicaiIdx+=1
                else:
                    relation['target'] = shicaiMap[url[0]]
                relation['relation'] = relationDes[j]
                relation['用量']=yongliang[0]
                relations.append(relation)
        i=1
        zuofa=""
        while (1):
            tmp_zuofa=dom.xpath("//div[@class='wrap']/div[@class='w clear']/div[@class='space_left']/div[@class='space_box_home']/div[@class='recipDetail']/div[@class='recipeStep']/ul/li["+str(i)+"]/div[@class='recipeStep_word']/text()")
            if(tmp_zuofa ==[]):
                break
            zuofa=zuofa+str(i)+":"+tmp_zuofa[0]
            if(tmp_zuofa[0][-1]!='。'):
                zuofa=zuofa+"。"
            i+=1
        tmp['zuofa']=zuofa
        #类型可能有几个，暂时只取一个
        leixing = dom.xpath("//div[@class='wrap']/div[@class='w clear']/div[@class='space_left']/div[@class='space_box_home']/div[@class='recipDetail']/div[@class='recipeTip mt16'][3]/a/text()")
        tmp['leixing'] = leixing[0] if len(leixing) > 0 else ''
        nandu = dom.xpath("//div[@class='wrap']/div[@class='w clear']/div[@class='space_left']/div[@class='space_box_home']/div[@class='recipDetail']/div[@class='recipeCategory_sub_R mt30 clear']/ul/li[4]/span[@class='category_s1']/a/text()")
        tmp['nandu'] = nandu[0] if len(nandu) > 0 else ''
        recipeList.append(tmp)
        recipeIdx+=1

    with open('./recipeDetail.csv','w',encoding='utf-8',newline="") as f:
        writer = csv.writer(f)
        writer.writerow(["name", "耗时", "做法","口味","工艺","类型","label","id","难度","图片"])
        for recipe in recipeList:
            writer.writerow([recipe['name'],recipe["haoshi"],recipe["zuofa"],recipe["kouwei"],recipe["gongyi"],recipe["leixing"],recipe["label"],recipe["id"],recipe["nandu"],recipe["tupian"]])
    with open('./relation.csv','w',encoding='utf-8',newline="") as f:
        writer = csv.writer(f)
        writer.writerow(["source","target","用量","relation"])
        for relation in relations:
            writer.writerow([relation["source"],relation["target"],relation["用量"],relation["relation"]])
    with open('./shicaiUrl.txt',"w",encoding='utf-8') as f:
        for key in shicaiMap.keys():
            f.write(key+","+str(shicaiMap[key])+"\n")
