<template>
    <div>
      <el-tooltip
        id="tooltip"
        placement="top-start"
        width="200"
        trigger="hover"
        :content="toolTipContent">
      </el-tooltip>
    </div>
</template>

<script>
import * as d3 from 'd3'
import {mapGetters, mapActions} from 'vuex'
export default {
  name: 'TreeSvg',
  data () {
    return {
      svg: {},
      toolTipShow: false,
      toolTipContent: '默认'
    }
  },
  computed: {
    ...mapGetters([
      'treeData',
      'currentEntityID'
    ])
  },
  methods: {
    ...mapActions([
      'getTreeById'
    ]),
    async InitTree () {
      await this.getTreeById({id: this.currentEntityID, depth: 2})
      d3.selectAll('svg > *').remove()
      this.svg = d3.select('svg')
      let tree = d3.tree()
        .size([550, 800])
        .separation(function (a, b) {
          return (a.parent === b.parent ? 1 : 2) / a.depth
        })
      // 初始化json数据转成一棵树，这个步骤是非常必要的！！
      let hierarchyData = d3.hierarchy(this.treeData)
        .sum(function (d) {
          return d.value
        })
      // 初始化树状图
      let treeData = tree(hierarchyData)
      // 获取节点
      let nodes = treeData.descendants()
      // 获取边,也就是连线
      let links = treeData.links()
      // 绘制线
      let g = this.svg.append('g').attr('transform', 'translate(80,0)')
      g.selectAll('.link')
        .data(links)
        .enter().append('path')
        .attr('class', 'link')
        .style('fill', '#B0C4DE')
        .style('opacity', 0.6)
        .attr('d', d3.linkHorizontal()
          .x(function (d) { return d.y })
          .y(function (d) { return d.x }))
        .style('stroke', '#B0C4DE')
        .style('stroke-width', '6px')
        // .on('mouseover', div.show)
        // .on('mouseout', div.hide)
        .append('title') // 在后面添加title
        .text(function (d) {
          return d.target.data.relation
        })
      // 绘制文本和节点
      g.selectAll('.node')
        .data(nodes)
        .enter().append('g')
        .attr('class', function (d) { return 'node' + (d.children ? ' node--internal' : ' node--leaf') })
        .attr('transform', function (d) { return 'translate(' + d.y + ',' + d.x + ')' })
      g.selectAll('.node').append('circle')
        .attr('r', 15)
        .style('fill', 'white')
        .style('stroke', this.circleColor)
        .style('stroke-width', '3px')
      g.selectAll('.node').append('text')
        .attr('dy', 5)
        .attr('x', function (d) {
          return d.children ? -20 : 20
        })
        .style('text-anchor', function (d) { return d.children ? 'end' : 'start' })
        .text(function (d) {
          return d.data.name
        })
        .style('font-size', '20px')
        .style('fill', ' #4682B4')
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
    }
  }
}
</script>

<style scoped>
  .d3-tip{
    display: flex;
    flex-direction: column;
    position: absolute;
    line-height: 1;
    font-weight: bold;
    padding:12px;
    background-color: antiquewhite;
    color: #fff;
    border-radius: 2px;
  }

  .d3-tip:after{
    box-sizing: border-box;
    display: inline;;
    font-size: 10px;
    width:100%;
    line-height: 1;
    color:  antiquewhite;
    content:'\25BC';
    position: absolute;
    text-align: center;
    top: 38px;
    left: -1px;
  }

  .d3-tip.n:after{
    margin: -1px 0 0 0;
    top: 100%;
    left:0;
  }
</style>
