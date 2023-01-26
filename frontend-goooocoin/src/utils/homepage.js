import {mapActions, mapGetters, mapMutations} from 'vuex'
import backgroundCanvas from '../components/backgroundCanvas'
// import sr from 'element-ui/src/locale/lang/sr'

var radius = 180 // 词云球体面积大小
var dtr = Math.PI / 180 // 鼠标滑过球体转动速度
var d = 350 // 球体立体感程度

var mcList = []
var active = false // 默认加载是否开启旋转
var lasta = 0.8 // 上下转动
var lastb = 0 // 左右转动
var distr = true
var tspeed = 8 // 球体转动速度
var size = 200

var mouseX = 0
var mouseY = 0

var howElliptical = 1

var aA = null
var oDiv = null

var sa = 0
var ca = 0
var sb = 0
var cb = 0
var sc = 0
var cc = 0
var per = 0

// window.onload = function () {
//   console.log('here')
//   homepage.methods.getNodes().then(res => { console.log('获取节点成功！') })
//   console.log('onload')
//   var i = 0
//   var oTag = null
//
//   if (this.COrR) {
//     oDiv = document.getElementById('tagbox')
//   } else {
//     oDiv = document.getElementById('tagbox1')
//   }
//
//   aA = oDiv.getElementsByClassName('Aa')
//   console.log(aA.length)
//
//   for (i = 0; i < aA.length; i++) {
//     oTag = {}
//
//     oTag.offsetWidth = aA[i].offsetWidth
//     oTag.offsetHeight = aA[i].offsetHeight
//
//     mcList.push(oTag)
//   }
//
//   sineCosine(0, 0, 0)
//
//   positionAll()
//
//   oDiv.onmouseover = function () {
//     active = true
//   }
//
//   oDiv.onmouseout = function () {
//     active = false
//   }
//
//   oDiv.onmousemove = function (ev) {
//     var oEvent = window.event || ev
//
//     mouseX = oEvent.clientX - (oDiv.offsetLeft + oDiv.offsetWidth / 2)
//     mouseY = oEvent.clientY - (oDiv.offsetTop + oDiv.offsetHeight / 2)
//
//     mouseX /= 5
//     mouseY /= 5
//   }
//
//   setInterval(update, 30)
// }

function update () {
  var a
  var b

  if (active) {
    a = (-Math.min(Math.max(-mouseY, -size), size) / radius) * tspeed
    b = (Math.min(Math.max(-mouseX, -size), size) / radius) * tspeed
  } else {
    a = lasta * 0.98
    b = lastb * 0.98
  }

  // 默认转动后是否需要停下
  // lasta=a;
  // lastb=b;

  // if(Math.abs(a)<=0.01 && Math.abs(b)<=0.01)
  // {
  // return;
  // }

  var c = 0
  sineCosine(a, b, c)
  for (var j = 0; j < mcList.length; j++) {
    var rx1 = mcList[j].cx
    var ry1 = mcList[j].cy * ca + mcList[j].cz * (-sa)
    var rz1 = mcList[j].cy * sa + mcList[j].cz * ca

    var rx2 = rx1 * cb + rz1 * sb
    var ry2 = ry1
    var rz2 = rx1 * (-sb) + rz1 * cb

    var rx3 = rx2 * cc + ry2 * (-sc)
    var ry3 = rx2 * sc + ry2 * cc
    var rz3 = rz2

    mcList[j].cx = rx3
    mcList[j].cy = ry3
    mcList[j].cz = rz3

    per = d / (d + rz3)

    mcList[j].x = (howElliptical * rx3 * per) - (howElliptical * 2)
    mcList[j].y = ry3 * per
    mcList[j].scale = per
    mcList[j].alpha = per

    mcList[j].alpha = (mcList[j].alpha - 0.6) * (10 / 6)
  }

  doPosition()
  depthSort()
}

function depthSort () {
  var i = 0
  var aTmp = []

  for (i = 0; i < aA.length; i++) {
    aTmp.push(aA[i])
  }

  aTmp.sort(
    function (vItem1, vItem2) {
      if (vItem1.cz > vItem2.cz) {
        return -1
      } else if (vItem1.cz < vItem2.cz) {
        return 1
      } else {
        return 0
      }
    }
  )

  for (i = 0; i < aTmp.length; i++) {
    aTmp[i].style.zIndex = i
  }
}

function positionAll () {
  var phi = 0
  var theta = 0
  var max = mcList.length
  var i = 0

  var aTmp = []
  var oFragment = document.createDocumentFragment()

  // 随机排序
  for (i = 0; i < aA.length; i++) {
    aTmp.push(aA[i])
  }

  aTmp.sort(
    function () {
      return Math.random() < 0.5 ? 1 : -1
    }
  )

  for (i = 0; i < aTmp.length; i++) {
    oFragment.appendChild(aTmp[i])
  }

  oDiv.appendChild(oFragment)

  for (var j = 1; j < max + 1; j++) {
    if (distr) {
      phi = Math.acos(-1 + (2 * j - 1) / max)
      theta = Math.sqrt(max * Math.PI) * phi
    } else {
      phi = Math.random() * (Math.PI)
      theta = Math.random() * (2 * Math.PI)
    }
    // 坐标变换
    mcList[j - 1].cx = radius * Math.cos(theta) * Math.sin(phi)
    mcList[j - 1].cy = radius * Math.sin(theta) * Math.sin(phi)
    mcList[j - 1].cz = radius * Math.cos(phi)

    aA[j - 1].style.left = mcList[j - 1].cx + oDiv.offsetWidth / 2 - mcList[j - 1].offsetWidth / 2 + 'px'
    aA[j - 1].style.top = mcList[j - 1].cy + oDiv.offsetHeight / 2 - mcList[j - 1].offsetHeight / 2 + 'px'
  }
}

function doPosition () {
  var l = oDiv.offsetWidth / 2
  var t = oDiv.offsetHeight / 2
  for (var i = 0; i < mcList.length; i++) {
    aA[i].style.left = mcList[i].cx + l - mcList[i].offsetWidth / 2 + 'px'
    aA[i].style.top = mcList[i].cy + t - mcList[i].offsetHeight / 2 + 'px'

    aA[i].style.fontSize = Math.ceil(12 * mcList[i].scale / 2) + 8 + 'px'

    aA[i].style.filter = 'alpha(opacity=' + 100 * mcList[i].alpha + ')'
    aA[i].style.opacity = mcList[i].alpha
  }
}

function sineCosine (a, b, c) {
  sa = Math.sin(a * dtr)
  ca = Math.cos(a * dtr)
  sb = Math.sin(b * dtr)
  cb = Math.cos(b * dtr)
  sc = Math.sin(c * dtr)
  cc = Math.cos(c * dtr)
}
const homepage = {
  name: 'homepage',
  components: {backgroundCanvas},
  data () {
    return {
      nameChosen: '',
      searchInput: '',
      searchType: '',
      popVisible: false,
      moreFlavors: false,
      moreTypes: false,
      moreMethods: false,
      moreIngreTypes: false,
      methodCheckList: [],
      typeCheckList: [],
      flavorCheckList: [],
      ingreTypeCheckList: [],
      collection: [],
      recommend: [],
      resultList: [],
      sequance: 1,
      searchOrNot: false
      // ingreTypeList: ['菜', '肉', '海鲜', '菌类'],
      // methodList: ['炖', '炒', '炸'],
      // typeList: ['热菜', '凉菜', '拌菜'],
      // flavorList: ['奶香', '咸', '甜辣', '超辣', '咸', '甜辣', '咸', '甜辣', '超辣', '咸', '甜辣', '咸', '甜辣', '超辣', '咸', '甜辣', '咸', '甜辣', '超辣', '咸', '甜辣']
    }
  },
  computed: {
    ...mapGetters([
      'recommendList',
      'currentUserID',
      'collectionList',
      'verificationRes',
      'flavorList',
      'methodList',
      'typeList',
      'ingreTypeList',
      'intelligenceSearchResult'
    ])
  },
  async mounted () {
    console.log('here')
    await this.getNodes()
    console.log('onload')
    this.draw()
  },
  methods: {
    ...mapMutations([]),
    ...mapActions([
      'getRecommend',
      'searchEntityByName',
      'getCollection',
      'getDistinctPropsValue',
      'intelligenceSearch'
    ]),
    draw () {
      var i = 0
      var oTag = null

      if (this.sequance === 2) {
        oDiv = document.getElementById('tagbox')
      } else if (this.sequance === 1) {
        oDiv = document.getElementById('tagbox1')
      } else {
        oDiv = document.getElementById('tagbox2')
      }

      aA = oDiv.getElementsByClassName('Aa')
      console.log(aA.length)

      for (i = 0; i < aA.length; i++) {
        oTag = {}

        oTag.offsetWidth = aA[i].offsetWidth
        oTag.offsetHeight = aA[i].offsetHeight

        mcList.push(oTag)
      }

      sineCosine(0, 0, 0)

      positionAll()

      oDiv.onmouseover = function () {
        active = true
      }

      oDiv.onmouseout = function () {
        active = false
      }

      oDiv.onmousemove = function (ev) {
        var oEvent = window.event || ev

        mouseX = oEvent.clientX - (oDiv.offsetLeft + oDiv.offsetWidth / 2)
        mouseY = oEvent.clientY - (oDiv.offsetTop + oDiv.offsetHeight / 2)

        mouseX /= 5
        mouseY /= 5
      }

      setInterval(update, 30)
    },
    async handleClick (item) {
      await this.updateRelationPropsById(item.key)
    },
    async getNodes () {
      // console.log(this.verificationRes)
      this.tasteTags = []
      this.techTags = []
      this.typeTags = []
      if (this.verificationRes.flavor) this.tasteTags = this.verificationRes.flavor
      if (this.verificationRes.method) this.techTags = this.verificationRes.method
      if (this.verificationRes.type) this.typeTags = this.verificationRes.type
      var res = {
        taste: this.tasteTags,
        tech: this.techTags,
        type: this.typeTags
      }
      await this.getRecommend(res)
      await this.getCollection(this.currentUserID)
      this.recommend = this.recommendList.content
      this.collection = this.collectionList.content
      console.log(this.recommend)
      console.log(this.collection)
      return true
    },
    async getSearchResult () {
      console.log(this.searchInput)
      console.log(this.typeCheckList)
      console.log(this.flavorCheckList)
      console.log(this.methodCheckList)
      console.log(this.searchType)
      this.popVisible = false
      var label = this.searchType === '1' ? 'Recipe' : 'Ingredients'
      var props = {}
      var hasProps = false
      if (this.searchType === '1') {
        if (this.flavorCheckList.length !== 0) {
          props['口味'] = this.flavorCheckList
          hasProps = true
        }
        if (this.methodCheckList.length !== 0) {
          props['工艺'] = this.methodCheckList
          hasProps = true
        }
        if (this.typeCheckList.length !== 0) {
          props['类型'] = this.typeCheckList
          hasProps = true
        }
      } else {
        if (this.ingreTypeCheckList.length !== 0) {
          props['类别'] = this.ingreTypeCheckList
          hasProps = true
        }
      }
      var searchString = {}
      if (hasProps) searchString.props = props
      console.log('props:')
      console.log(props)
      if (this.searchInput !== null) searchString.key = this.searchInput
      console.log(searchString)
      searchString.label = label
      await this.intelligenceSearch(searchString)
      console.log('--------')
      console.log(searchString)
      this.resultList = this.intelligenceSearchResult
      console.log('intelligence')
      console.log(this.intelligenceSearchResult)
      this.sequance = 3
      this.searchOrNot = true
      this.draw()
    },
    toDetail (obj) {
      var res = $(obj).text()
      alert(res)
      this.set_DialogueItemShow(false)
      this.$router.push('/home/detailPage')
    },
    async unfoldCheckPan () {
      if (this.searchType === '1') {
        await this.getDistinctPropsValue({label: 'Recipe', propName: '口味'})
        await this.getDistinctPropsValue({label: 'Recipe', propName: '工艺'})
        await this.getDistinctPropsValue({label: 'Recipe', propName: '类型'})
        console.log('00000000')
        console.log(this.flavorList)
      } else await this.getDistinctPropsValue({label: 'Ingredients', propName: '类别'})
      this.popVisible = true
    },
    sequancePlus () {
      this.sequance++
      if (!this.searchOrNot && this.sequance === 3) this.sequance = 1
      if (this.searchOrNot && this.sequance === 4) this.sequance = 1
      console.log(this.sequance)
    },
    sequanceMinus () {
      this.sequance--
      if (!this.searchOrNot && this.sequance === 0) this.sequance = 2
      if (this.searchOrNot && this.sequance === 0) this.sequance = 3
      console.log(this.sequance)
    }
  }
}
export default homepage
