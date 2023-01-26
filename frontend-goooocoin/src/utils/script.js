import {mapActions, mapGetters, mapMutations} from 'vuex'

var radius = 180 // 词云球体面积大小
var dtr = Math.PI / 180 // 鼠标滑过球体转动速度
var d = 350 // 球体立体感程度

var mcList = []
var active = false // 默认加载是否开启旋转
var lasta = 0.8 // 上下转动
var lastb = 0 // 左右转动
var distr = true
var tspeed = 3 // 球体转动速度
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

// eslint-disable-next-line camelcase
function init_all () {
  radius = 180 // 词云球体面积大小
  dtr = Math.PI / 180 // 鼠标滑过球体转动速度
  d = 350 // 球体立体感程度

  mcList = []
  active = false // 默认加载是否开启旋转
  lasta = 0.8 // 上下转动
  lastb = 0 // 左右转动
  distr = true
  tspeed = 3 // 球体转动速度
  size = 200

  mouseX = 0
  mouseY = 0

  howElliptical = 1

  aA = null
  oDiv = null

  sa = 0
  ca = 0
  sb = 0
  cb = 0
  sc = 0
  cc = 0
  per = 0

  setInterval(update, 30)
}

function InitCanvas () {
  // init_all()
  console.log('speed: ' + tspeed)
  var i = 0
  var oTag = null
  mcList = []

  oDiv = document.getElementById('tagbox')
  console.log(oDiv)

  aA = oDiv.getElementsByClassName('link')
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

  // setInterval(update, 30)
}

function update () {
  // console.log('In Update')
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
  // lasta = a
  // lastb = b
  //
  // if (Math.abs(a) <= 0.01 && Math.abs(b) <= 0.01) {
  //   return
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
  // console.log('In DepthSort')
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
  // console.log('In PositionAll')
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

  for (i = 1; i < max + 1; i++) {
    if (distr) {
      phi = Math.acos(-1 + (2 * i - 1) / max)
      theta = Math.sqrt(max * Math.PI) * phi
    } else {
      phi = Math.random() * (Math.PI)
      theta = Math.random() * (2 * Math.PI)
    }
    // 坐标变换
    mcList[i - 1].cx = radius * Math.cos(theta) * Math.sin(phi)
    mcList[i - 1].cy = radius * Math.sin(theta) * Math.sin(phi)
    mcList[i - 1].cz = radius * Math.cos(phi)

    aA[i - 1].style.left = mcList[i - 1].cx + oDiv.offsetWidth / 2 - mcList[i - 1].offsetWidth / 2 + 'px'
    aA[i - 1].style.top = mcList[i - 1].cy + oDiv.offsetHeight / 2 - mcList[i - 1].offsetHeight / 2 + 'px'
  }
}

function doPosition () {
  // console.log('In DoPosition')
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
  // console.log('In SineCosine')
  sa = Math.sin(a * dtr)
  ca = Math.cos(a * dtr)
  sb = Math.sin(b * dtr)
  cb = Math.cos(b * dtr)
  sc = Math.sin(c * dtr)
  cc = Math.cos(c * dtr)
}
export default {
  name: 'BallCanvas',
  props: [
    'resList',
    'illegalSearch',
    'sequance'
  ],
  computed: {
    ...mapGetters([
      'graphJSON'
    ])
  },
  data () {
    return {
      len: false
    }
  },
  methods: {
    ...mapMutations([
      'set_currentCenter',
      'set_currentEntityInfo',
      'set_currentCenterInfo'
    ]),
    ...mapActions([
      'getRelativeRelation'
    ]),
    async initcanvas () {
      await init_all()
      await InitCanvas()
    },
    async canvasInit () {
      console.log(this.resList)
      if (this.resList === undefined) this.len = true
      else this.len = (this.resList.length === 0)
      if (this.len || this.illegalSearch) {
        console.log('return')
      }
      if (!this.len && !this.illegalSearch) await InitCanvas()
    },
    async handleClick (item) {
      console.log('****************************************')
      console.log(item)
      this.set_currentEntityInfo(item)
      console.log(this.graphJSON)
      await this.set_currentCenter(item.id)
      await this.set_currentCenterInfo(item)
      window.sessionStorage.setItem('currentCenter', item.id)
      console.log('ssssession: ' + window.sessionStorage.getItem('currentCenter'))
      await this.getRelativeRelation(item.id)
      this.$router.push('/home/detailPage')
    }
  }
}
