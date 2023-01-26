import requests
from lxml import etree
import csv

#根据shicaiUrl.txt里的链接爬取食材数据
def FoodCrawb():
    headers = {
        'User-Agent':'Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36 QIHU 360SE'
    }

    with open ("./shicaiUrl.txt","r",encoding="utf-8") as f:
        UrlIdList = f.readlines()

    foodList = []
    idx=1
    for item in UrlIdList:
        item = item[:-1]
        url = item.split(",")[0]
        id = item.split(",")[1]

        tmp={}
        tmp['id']=id
        tmp['label']="material"

        #名字
        res= requests.get(url,headers=headers)
        res.encoding = 'utf-8'
        dom = etree.HTML(res.text)
        if(res.status_code==200):
            name = dom.xpath("//div[@class='w clear collect_d']/div[@class='collect_dp']/h1/a[@id='category_title']/text()")
            tmp['name'] = name[0] if len(name) > 0 else ''
            tmp['leibie'] = dom.xpath("string(//div[@class='w nav_wrap2']/ul//a[@class='on'])")
        else:
            tmp['name']=''
            tmp['leibie']=''
        #图片
        res= requests.get(url+"tiyan/",headers=headers)
        res.encoding = 'utf-8'
        dom = etree.HTML(res.text)
        if(res.status_code==200):
            touxiang = dom.xpath("//div[@class='wrap']/div[@class='w clear']/div[@class='space_left']/div[@class='tiyan_blog_message mt10 clear']/div[@class='blog_message']//img/@src")
            tmp['touxiang'] = touxiang[0] if len(touxiang) > 0 else ''
        else:
            tmp['touxiang']=''

        res= requests.get(url+"useful/",headers=headers)
        res.encoding = 'utf-8'
        dom = etree.HTML(res.text)
        if(res.status_code==200):
            tmp['jianjie'] = dom.xpath("string(//div[@class='wrap']/div[@class='w clear']/div[@class='space_left']/div[@class='category_usebox mt10 clear']/div/p[2])")
            tmp['gongxiao'] = dom.xpath("string(//div[@class='wrap']/div[@class='w clear']/div[@class='space_left']/div[@class='category_usebox mt10 clear']/div/p[6])")
            tmp['shiyong'] = dom.xpath("string(//div[@class='wrap']/div[@class='w clear']/div[@class='space_left']/div[@class='category_usebox mt10 clear']/div/p[8])")
            tmp['jinji'] = dom.xpath("string(//div[@class='wrap']/div[@class='w clear']/div[@class='space_left']/div[@class='category_usebox mt10 clear']/div/p[10])")
            tmp['yingyang'] = dom.xpath("string(//div[@class='wrap']/div[@class='w clear']/div[@class='space_left']/div[@class='category_usebox mt10 clear']/div/p[4])")
        else:
            tmp['jianjie'] = ''
            tmp['gongxiao'] =  ''
            tmp['shiyong'] =  ''
            tmp['jinji'] =  ''
            tmp['yingyang'] =  ''
        foodList.append(tmp)
        if(idx%100==0):
            print("好")
        idx+=1
    with open('./foodDetail.csv','w',encoding='utf-8',newline="") as f:
        writer = csv.writer(f)
        writer.writerow(["name", "食材简介", "食用功效", "适用人群", "禁忌人群", "营养价值", "类别","图片","label", "id"])
        for food in foodList:
            writer.writerow([food['name'],food['jianjie'],food['gongxiao'],food['shiyong'],food['jinji'],food['yingyang'],food['leibie'],food['touxiang'],food['label'],food['id']])