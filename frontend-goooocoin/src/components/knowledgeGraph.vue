<template>
  <svg ref="headerG" style="background: transparent;" @click="svgclicked"></svg>
</template>

<script>
// This is the presentation page
import * as d3 from 'd3'
import {mapActions, mapGetters, mapMutations} from 'vuex'
export default {
  name: 'presentation',
  data () {
    return {
      svg: {},
      width: 0,
      height: 0,
      node: {},
      link: {},
      nodeText: {},
      linkText: {},
      simulation: {},
      scaleSize: 0,
      zoom: {},
      container: {},
      g: {},
      scaleSizeBefore: 0,
      timer: null
    }
  },
  props: ['entityStack', 'entityInfoStack'],
  computed: {
    ...mapGetters([
      'graphJSON',
      'nodeData',
      'linkData',
      'drawer',
      'currentCenter',
      'currentEntityInfo',
      'currentCenterInfo'
    ])
  },
  async mounted () {
    // console.log('kgmounted')
    // console.log(this.currentCenterInfo)
    // console.log(this.currentCenterInfo)
    var currentCenter = window.sessionStorage.getItem('currentCenter')
    var userID = window.localStorage.getItem('userID')
    this.set_currentUserID(userID)
    if (userID === '-1') this.$router.push('/login')
    if (currentCenter !== null && currentCenter !== '') {
      await this.getRelativeRelation(currentCenter)
    } else this.$router.push('/home')
    this.set_nodeData(this.graphJSON.entities)
    this.set_linkData(this.graphJSON.relations)
    this.processLinkData()
    this.Init()
  },
  methods: {
    ...mapMutations([
      'set_GraphJSON',
      'set_nodeData',
      'set_linkData',
      'set_currentEntityInfo',
      'set_MouseOnEntity',
      'set_currentCenter',
      'set_currentUserID',
      'set_currentCenterInfo'
    ]),
    ...mapActions([
      'getGraphJSON',
      'getRelativeRelation',
      'spreadEntity'
    ]),
    svgclicked () {
      console.log('clid')
    },
    zoomed () {
      // translate变换矢量（使用二元组标识）scale当前尺度的数字
      // this.g.attr('transform', 'translate(' + d3.event.translate + ')scale(' + d3.event.scale + ')') // 画布缩放与移动
      // this.svg.attr('transform', 'scale(' + d3.event.scale + ')') // 画布缩放
      this.g.attr('transform', d3.event.transform)
      this.linkText.attr('transform', d3.event.transform)
    },
    myzoomIn () {
      this.zoom.scaleBy(this.svg, 2)
    },
    myzoomOut () {
      this.zoom.scaleBy(this.svg, 0.5)
    },
    zoomInit () {
      console.log('zoominit')
      console.log(d3)
      // var num = 0
      // var temp = (1 + 0.1 * num) / (1 + 0.1 * this.scaleSizeBefore)
      this.zoom.scaleBy(this.svg, 1)
      // this.scaleSizeBefore = num
      // this.scaleSize = 0
      console.log('zoominit')
    },
    async Init () {
      console.log(this.graphJSON)
      var that = this
      // await this.getGraphJSON()
      this.zoom = d3.zoom()
        .scaleExtent([-10, 10])
        .on('zoom', this.zoomed)
      d3.selectAll('svg > *').remove()
      if (this.width === 0) {
        this.svg = d3.select('svg')
          .attr('width', 1000)
          .attr('height', 580)
          .attr('viewBox', '0 0 1000 580')
          .call(this.zoom)
          .on('dblclick.zoom', null)
        this.width = +this.svg.attr('width')
        this.height = +this.svg.attr('height')
      } else {
        this.svg = d3.select('svg')
          .attr('width', this.width)
          .attr('height', this.height)
          .attr('viewBox', '0 0 1000 580')
          .call(this.zoom)
          .on('dblclick.zoom', null)
      }

      this.simulation = d3.forceSimulation().nodes(this.nodeData)
      this.simulation
        .force('charge_force', d3.forceManyBody().strength(500))
        .force('center_force', d3.forceCenter(this.width / 2, this.height / 2))
        .force('collide_force', d3.forceCollide().radius(60).iterations(2))

      let linkForce = d3.forceLink(this.linkData)
        .id((d) => {
          return d.name
        })
        .distance(300)

      this.simulation.force('links', linkForce)

      this.g = this.svg.append('g')

      // 以下为连线设置
      this.link = this.g
        .attr('class', 'links')
        .selectAll('line')
        .data(this.linkData)
        .enter()
        .append('line')
        .attr('stroke-width', function (d) {
          return d.d3_properties.__D3_PROPS__weight
        })
        .style('stroke', function (d) {
          return d.d3_properties.__D3_PROPS__color
        })
        .style('stroke-dasharray', function (d) {
          if (d.d3_properties.__D3_PROPS__lineStyle === '1' || d.d3_properties.__D3_PROPS__lineStyle === '实线') return '5,0'
          else if (d.d3_properties.__D3_PROPS__lineStyle === '3') return '1,5'
          else return '5,5'
        })
        .attr('marker-end', 'url(#resolved)') // 加箭头
        .on('click', this.linkClicked)
        .on('mouseover', function (d) { // 试试
          console.log(d)
          d3.select(this).attr('stroke-width', function (d) {
            return d.d3_properties.__D3_PROPS__weight + 2
          })
        })
        .on('mouseout', function (d) { // 试试
          d3.select(this).attr('stroke-width', function (d) {
            return d.d3_properties.__D3_PROPS__weight
          })
        })

      // 以下为连线字符设置
      this.linkText = this.svg.selectAll('text')
        .data(this.linkData).enter()
        .append('text')
        .style('fill', '#44608a')
        .attr('class', 'linkText')
        .attr('tx', 0)
        .attr('ty', 0)
        .attr('text-anchor', 'middle')
        .style('visibility', this.relationTextVisible)
        .attr('font-size', function (d) {
          return d.d3_properties.__D3_PROPS__textsize
        })
        .text(function (d) {
          return d.name
        })

      // 以下为节点设置
      this.node = this.g
        .attr('class', 'nodes')
        .selectAll('path')
        .data(this.nodeData)
        .enter()
        .append('path')
        .attr('r', 32)
        .attr('d', d3.symbol().type(function (d) {
          if (d.shape === '圆形') return d3.symbolCircle
          else if (d.shape === '正方形') return d3.symbolSquare
          else if (d.shape === '菱形') return d3.symbolDiamond
          else if (d.shape === '星形') return d3.symbolStar
          else if (d.shape === '三角形') return d3.symbolTriangle
          else return d3.symbolCross
        }).size(function (d) {
          return d.size * d.size
        }))
        .attr('fill', function (d) {
          return d.color
        })
        .call(
          d3
            .drag()
            .on('start', this.dragStarted)
            .on('drag', this.dragged)
            .on('end', this.dragEnded)
        )
        .on('click', this.EntityClicked)
        .on('dblclick', this.EntityDBLClicked)
        .on('mouseover', function (d) { // 试试
          that.set_MouseOnEntity(true)
          d3.select(this).attr('d', d3.symbol().type(function (d) {
            if (d.shape === '圆形') return d3.symbolCircle
            else if (d.shape === '正方形') return d3.symbolSquare
            else if (d.shape === '菱形') return d3.symbolDiamond
            else if (d.shape === '星形') return d3.symbolStar
            else if (d.shape === '三角形') return d3.symbolTriangle
            else return d3.symbolCross
          }).size(function (d) {
            return (d.size + 10) * (d.size + 10)
          }))
        })
        .on('mouseout', function (d) { // 试试
          that.set_MouseOnEntity(false)
          d3.select(this).attr('d', d3.symbol().type(function (d) {
            if (d.shape === '圆形') return d3.symbolCircle
            else if (d.shape === '正方形') return d3.symbolSquare
            else if (d.shape === '菱形') return d3.symbolDiamond
            else if (d.shape === '星形') return d3.symbolStar
            else if (d.shape === '三角形') return d3.symbolTriangle
            else return d3.symbolCross
          }).size(function (d) {
            return d.size * d.size
          }))
        })

      // 以下为节点文字设置
      var nodeTextTemp = this.g
        .attr('class', 'nodeText')
        .selectAll('text')
        .data(this.nodeData)
      nodeTextTemp.exit().remove()
      this.nodeText = nodeTextTemp.enter()
        .append('text')
        .style('pointer-events', 'none')
        .style('fill', '#44608a')
        .attr('text-anchor', 'middle')
        .attr('dx', 20)
        .attr('font-size', function (d) {
          return d.textsize
        })
        .text(function (d) {
          if (d.name.length > 4) return d.name.slice(0, 4) + '...'
          else return d.name
        })

      // 箭头设置
      // eslint-disable-next-line no-unused-vars
      var marker =
        this.svg.append('marker')
          // .attr("id", function(d) { return d; })
          .attr('id', 'resolved')
          .attr('markerUnits', 'strokeWidth')// 设置为strokeWidth箭头会随着线的粗细发生变化
          .attr('markerUnits', 'userSpaceOnUse')
          .attr('viewBox', '0 -5 10 10')// 坐标系的区域
          .attr('refX', 30)// 箭头坐标
          .attr('refY', 0)
          .attr('markerWidth', 12)// 标识的大小
          .attr('markerHeight', 12)
          .attr('orient', 'auto')// 绘制方向，可设定为：auto（自动确认方向）和 角度值
          .attr('stroke-width', 2)// 箭头宽度
          .append('path')
          .attr('d', 'M0,-5L10,0L0,5')// 箭头的路径
          .attr('fill', '#ccc')// 箭头颜色
      // 以下为圆形菜单设置 TODO：迭代二完成
      this.simulation.on('tick', this.tickAction)
    },
    tickAction () {
      // this.node
      //   .attr('cx', (d) => { return d.x })
      //   .attr('cy', (d) => { return d.y })

      this.node.attr('transform', function (d) { return 'translate(' + d.x + ',' + d.y + ')' })
      this.link
        .attr('x1', (d) => { return d.source.x })
        .attr('y1', (d) => { return d.source.y })
        .attr('x2', (d) => { return d.target.x })
        .attr('y2', (d) => { return d.target.y })

      this.nodeText
        .attr('x', (d) => { return d.x - 20 })
        .attr('y', (d) => { return d.y + 5 })

      this.linkText
        .attr('x', function (d) {
          if (
            typeof d.source.x === 'undefined' ||
            typeof d.target.x === 'undefined'
          ) { return 0 }
          var x = (parseFloat(d.source.x) + parseFloat(d.target.x)) / 2
          return x
        })
        .attr('y', function (d) {
          if (
            typeof d.source.y === 'undefined' ||
            typeof d.target.y === 'undefined'
          ) { return 0 }
          var y = (parseFloat(d.source.y) + parseFloat(d.target.y)) / 2
          return y
        })
      // console.log(document.querySelector('svg'))
      let serializer = new XMLSerializer()
      let source = '<?xml version="1.0" standalone="no"?>\r\n' + serializer.serializeToString(document.querySelector('svg'))
      let image = new Image()
      image.src = 'data:image/svg+xml;charset=utf-8,' + encodeURIComponent(source)
      // console.log(image.src)
    },
    circleColor () { // 随机生成颜色
      console.log('hexcode')
      function showRGB (str) {
        var hexcode = '#'
        var v = str.substring(4, str.length - 1)
        var s = v.split(',')
        for (var x = 0; x < 3; x++) {
          var n = s[x]
          if (n === '')n = '0'
          var c = '0123456789ABCDEF'
          var b = ''
          var a = n % 16
          b = c.substr(a, 1)
          a = (n - a) / 16
          hexcode += c.substr(a, 1) + b
        }
        console.log('hexcode' + hexcode)
        return hexcode
      }
      var blue = 200 + Math.floor(Math.random() * 55)
      var green = 150 + Math.floor(Math.random() * 105)
      if (green > blue) { green -= (green - blue) * 0.9 }
      var red = Math.min(blue, green) - 50 - Math.floor(Math.random()) * ((Math.min(blue, green) + 20))
      return showRGB('rgb(' + red + ',' + green + ',' + blue + ')')
    },
    dragStarted (d) {
      if (!d3.event.active) this.simulation.alphaTarget(0.8).restart()
      // this.clickDate = new Date()
      d.fx = d.x
      d.fy = d.y
    },
    dragged (d) {
      d.fx = d3.event.x
      d.fy = d3.event.y
    },
    dragEnded (d) {
      if (!d3.event.active) this.simulation.alphaTarget(0)
      d.fx = d3.event.x
      d.fy = d3.event.y
      // this.difference_ms = (new Date()).getTime() - this.clickDate.getTime()
      // this.clickDate = new Date()
      // if (this.difference_ms < 200) {
      //   console.log('entity double Clicked!!')
      // }
    },
    async EntityDBLClicked (d) {
      // await this.spreadEntity(d.id)
      clearTimeout(this.timer)
      this.entityStack.push(this.currentCenterInfo.id)
      this.entityInfoStack.push(this.currentCenterInfo)
      console.log('*************')
      console.log(this.currentCenterInfo.id)
      console.log('*************')
      this.set_currentCenter(d.id)
      window.sessionStorage.setItem('currentCenter', d.id)
      window.sessionStorage.setItem('entityStack', JSON.stringify(this.entityStack))
      window.sessionStorage.setItem('entityInfoStack', JSON.stringify(this.entityInfoStack))
      console.log(this.entityStack)
      await this.getRelativeRelation(d.id)
      this.set_nodeData(this.graphJSON.entities)
      this.set_linkData(this.graphJSON.relations)
      await this.processLinkData()
      await this.Init()
      this.set_currentEntityInfo(d)
      console.log('dblclicked')
      console.log(this.currentCenterInfo)
      this.set_currentCenterInfo(d)
      console.log(this.currentCenterInfo)
      this.$emit('updtisCollected')
    },
    async EntityClicked (d) {
      let that = this
      console.log('entityClickedddddddddddddddd')
      console.log(that.pictureSrc)
      clearTimeout(that.timer)
      that.timer = setTimeout(function () {
        that.set_currentEntityInfo(d)
        that.$emit('updtisCollected')
      }, 300)
    },
    processLinkData () {
      console.log('processLinkData')
      let that = this
      that.linkData.forEach(function (e) {
        console.log('in forEach')
        let sourceNode = that.nodeData.filter(function (n) {
          return n.id === e.source
        })[0]
        let targetNode = that.nodeData.filter(function (n) {
          return n.id === e.target
        })[0]
        e.source = sourceNode
        e.target = targetNode
      })
    }
  }
}
</script>

<style scoped>

</style>
