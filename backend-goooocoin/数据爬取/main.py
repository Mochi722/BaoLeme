from foodCrawb import FoodCrawb
from recipeCrawb import recipeCrawb
from recipeUrl import getRecipeUrl

#直接运行即可，只需修改getRecipeUrl的参数，begin下限为1，end上限为1001
if __name__ == '__main__':
    getRecipeUrl(1,101) #一面10个菜谱，共1000个菜谱
    recipeCrawb() #获得菜谱的详细信息及与食材的关系
    FoodCrawb() #获得食材的详细信息
