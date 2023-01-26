import {mapActions, mapGetters, mapMutations} from 'vuex'
import backgroundCanvas from '../components/backgroundCanvas'
// import sr from 'element-ui/src/locale/lang/sr'

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
      searchOrNot: false,
      oDiv: null,

      radius: 180, // 词云球体面积大小
      dtr: Math.PI / 180, // 鼠标滑过球体转动速度
      d: 350, // 球体立体感程度

      mcList: [],
      active: false, // 默认加载是否开启旋转
      lasta: 0.8, // 上下转动
      lastb: 0, // 左右转动
      distr: true,
      tspeed: 8, // 球体转动速度
      size: 200,

      mouseX: 0,
      mouseY: 0,

      howElliptical: 1,

      aA: null,

      sa: 0,
      ca: 0,
      sb: 0,
      cb: 0,
      sc: 0,
      cc: 0,
      per: 0
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
        this.oDiv = document.getElementById('tagbox')
      } else if (this.sequance === 1) {
        this.oDiv = document.getElementById('tagbox1')
      } else {
        this.oDiv = document.getElementById('tagbox2')
      }

      this.aA = this.oDiv.getElementsByClassName('Aa')

      for (i = 0; i < this.aA.length; i++) {
        oTag = {}

        oTag.offsetWidth = this.aA[i].offsetWidth
        oTag.offsetHeight = this.aA[i].offsetHeight

        this.mcList.push(oTag)
      }

      this.sineCosine(0, 0, 0)

      this.positionAll()

      this.oDiv.onmouseover = function () {
        homepage.methods.set_active(true)
      }

      this.oDiv.onmouseout = function () {
        homepage.methods.set_active(false)
      }

      console.log('here1')
      console.log(this.oDiv)
      this.oDiv.onmousemove = function (ev) {
        var oEvent = window.event || ev
        this.oDiv = document.getElementById('tagbox1')
        var x = oEvent.clientX - (this.oDiv.offsetLeft + this.oDiv.offsetWidth / 2)
        var y = oEvent.clientY - (this.oDiv.offsetTop + this.oDiv.offsetHeight / 2)

        x /= 5
        y /= 5

        homepage.methods.mMove(x, y)
      }

      setInterval(this.update, 30)
    },
    mMove (x, y) {
      this.mouseX = x
      this.mouseY = y
    },
    set_active (bool) {
      this.active = bool
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
    },
    update () {
      var a
      var b

      if (this.active) {
        a = (-Math.min(Math.max(-this.mouseY, -this.size), this.size) / this.radius) * this.tspeed
        b = (Math.min(Math.max(-this.mouseX, -this.size), this.size) / this.radius) * this.tspeed
      } else {
        a = this.lasta * 0.98
        b = this.lastb * 0.98
      }

      // 默认转动后是否需要停下
      // lasta=a;
      // lastb=b;

      // if(Math.abs(a)<=0.01 && Math.abs(b)<=0.01)
      // {
      // return;
      // }

      var c = 0
      this.sineCosine(a, b, c)
      for (var j = 0; j < this.mcList.length; j++) {
        var rx1 = this.mcList[j].cx
        var ry1 = this.mcList[j].cy * this.ca + this.mcList[j].cz * (-this.sa)
        var rz1 = this.mcList[j].cy * this.sa + this.mcList[j].cz * this.ca

        var rx2 = rx1 * this.cb + rz1 * this.sb
        var ry2 = ry1
        var rz2 = rx1 * (-this.sb) + rz1 * this.cb

        var rx3 = rx2 * this.cc + ry2 * (-this.sc)
        var ry3 = rx2 * this.sc + ry2 * this.cc
        var rz3 = rz2

        this.mcList[j].cx = rx3
        this.mcList[j].cy = ry3
        this.mcList[j].cz = rz3

        this.per = this.d / (this.d + rz3)

        this.mcList[j].x = (this.howElliptical * rx3 * this.per) - (this.howElliptical * 2)
        this.mcList[j].y = ry3 * this.per
        this.mcList[j].scale = this.per
        this.mcList[j].alpha = this.per

        this.mcList[j].alpha = (this.mcList[j].alpha - 0.6) * (10 / 6)
      }

      this.doPosition()
      this.depthSort()
    },

    depthSort () {
      var i = 0
      var aTmp = []

      for (i = 0; i < this.aA.length; i++) {
        aTmp.push(this.aA[i])
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
    },

    positionAll () {
      var phi = 0
      var theta = 0
      var max = this.mcList.length
      var i = 0

      var aTmp = []
      var oFragment = document.createDocumentFragment()

      // 随机排序
      for (i = 0; i < this.aA.length; i++) {
        aTmp.push(this.aA[i])
      }

      aTmp.sort(
        function () {
          return Math.random() < 0.5 ? 1 : -1
        }
      )

      for (i = 0; i < aTmp.length; i++) {
        oFragment.appendChild(aTmp[i])
      }

      this.oDiv.appendChild(oFragment)

      for (var j = 1; j < max + 1; j++) {
        if (this.distr) {
          phi = Math.acos(-1 + (2 * j - 1) / max)
          theta = Math.sqrt(max * Math.PI) * phi
        } else {
          phi = Math.random() * (Math.PI)
          theta = Math.random() * (2 * Math.PI)
        }
        // 坐标变换
        this.mcList[j - 1].cx = this.radius * Math.cos(theta) * Math.sin(phi)
        this.mcList[j - 1].cy = this.radius * Math.sin(theta) * Math.sin(phi)
        this.mcList[j - 1].cz = this.radius * Math.cos(phi)

        this.aA[j - 1].style.left = this.mcList[j - 1].cx + this.oDiv.offsetWidth / 2 - this.mcList[j - 1].offsetWidth / 2 + 'px'
        this.aA[j - 1].style.top = this.mcList[j - 1].cy + this.oDiv.offsetHeight / 2 - this.mcList[j - 1].offsetHeight / 2 + 'px'
      }
    },

    doPosition () {
      var l = this.oDiv.offsetWidth / 2
      var t = this.oDiv.offsetHeight / 2
      for (var i = 0; i < this.mcList.length; i++) {
        this.aA[i].style.left = this.mcList[i].cx + l - this.mcList[i].offsetWidth / 2 + 'px'
        this.aA[i].style.top = this.mcList[i].cy + t - this.mcList[i].offsetHeight / 2 + 'px'

        this.aA[i].style.fontSize = Math.ceil(12 * this.mcList[i].scale / 2) + 8 + 'px'

        this.aA[i].style.filter = 'alpha(opacity=' + 100 * this.mcList[i].alpha + ')'
        this.aA[i].style.opacity = this.mcList[i].alpha
      }
    },

    sineCosine (a, b, c) {
      this.sa = Math.sin(a * this.dtr)
      this.ca = Math.cos(a * this.dtr)
      this.sb = Math.sin(b * this.dtr)
      this.cb = Math.cos(b * this.dtr)
      this.sc = Math.sin(c * this.dtr)
      this.cc = Math.cos(c * this.dtr)
    }
  }
}
export default homepage
