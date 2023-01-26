import requests
from lxml import etree

#传入的为要爬取的页面首尾页面
def getRecipeUrl(begin,end):
    recipeUrlList = []

    headers = {
        'User-Agent':'Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36 QIHU 360SE'
    }
    for i in range(begin,end):
        try:
            url = "https://home.meishichina.com/recipe-list-page-"+str(i)+".html"
            r = requests.get(url,headers=headers)
            dom =  etree.HTML(r.text)
            for j in range(1,11):
                recipeUrl = dom.xpath("//div[@class='wrap']/div[@class='w clear']/div[@class='space_left']/div[@id='J_list']/ul/li["+str(j)+"]/div[@class='detail']/h2/a/@href")[0]
                recipeUrlList.append(recipeUrl)
            if(i % 10==0):
                print("好")
        except:
            print(url)
    with open("./recipeUrl.txt", mode='w', encoding='utf-8') as f:
        for url in recipeUrlList:
            f.write(url+"\n")
