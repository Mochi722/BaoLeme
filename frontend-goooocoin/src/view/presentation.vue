<template>
  <div class="presentationPage" style="position: absolute; left: 0px; top: 0px; width: 100%; height: 100%;">
    <background-canvas style="position: absolute;top: 0;bottom: 0;left: 0;right: 0;pointer-events: none;"></background-canvas>
    <HeaderGroup></HeaderGroup>
<!--    <el-divider></el-divider>-->
    <div class="dividerFake"></div>
    <el-container>
      <el-aside width="auto">
        <SideBar @changeMode="changeMode" @changeSize="changeSize" @searchResultShow="searchResultShow" @searchRelationResultShow="searchRelationResultShow" @searchResultShowEnd="searchResultShowEnd" ref="menu"></SideBar>
      </el-aside>
      <el-main>
<!--        <el-image-->
<!--          v-show="imageShow"-->
<!--          style="width: 70%; height: 70%;padding: 80px 0 80px 0"-->
<!--          :src="defaultImageUrl"-->
<!--          :fit="fit"></el-image>-->
        <el-container style="height: 95%">
<!--          <background-canvas style="position: absolute;width: 100%;height: 85%;pointer-events: none;z-index: -1"></background-canvas>-->
          <el-main class="main" v-if="!graphMode">
            <div class="search" id="search">
              <el-input placeholder="搜索菜品/食材实体" v-model="searchInput" class="input-search">
                <el-select v-model="searchType" slot="prepend" placeholder="请选择" @change="changeTable">
                  <el-option label="搜美食" value="1"></el-option>
                  <el-option label="搜食材" value="2"></el-option>
                </el-select>
                <el-button slot="append" icon="el-icon-search" @click="adminGetSearchResult"></el-button>
              </el-input>
            </div>
            <adminSearchResultDialog ref="table"></adminSearchResultDialog>
          </el-main>
          <el-main class="main" v-if="graphMode">
            <el-page-header @back="goBack" content="图谱页面">
            </el-page-header>
            <svg id="svgg" ref="headerG" @dblclick="svgDBLclicked"></svg>
          </el-main>
        </el-container>
        <TreeSvg ref="treeSvg"></TreeSvg>
        <el-button v-show="!imageShow" style="float: right; margin-left: 20px" @click="zoomInit">还原</el-button>
        <el-slider
          id="slider"
          v-model="scaleSize"
          v-show="!imageShow"
          :min="-9"
          :max="9"
          :step="1"
          @input="sliderChange"
          style="width: 300px; float: right; margin-left: 20px"
        >
        </el-slider>
        <!--        height="450" width="1350"-->
      </el-main>
    </el-container>
    <OpDialog :active="active" :arrowDir="arrowDir" :currentEntityID="currentEntityID" :OpDialogFlag="OpDialogFlag"></OpDialog>
    <RelationOpDialog></RelationOpDialog>
    <sideOpDialog :arrowDir="arrowDir"></sideOpDialog>
    <save-confirm-dialog></save-confirm-dialog>
<!--    <button @click="changeMode">切换模式</button>-->
  </div>
</template>

<script>
// This is the presentation page
import * as d3 from 'd3'
import {mapActions, mapGetters, mapMutations} from 'vuex'
import HeaderGroup from '../components/HeaderGroup'
import OpDialog from '../components/OpDialog'
import RelationOpDialog from '../components/RelationOpDialog'
import SideBar from '../components/SideBar'
import sideOpDialog from '../components/sideOpDialog'
import {Message} from 'element-ui'
import TreeSvg from '../components/TreeSvg'
import backgroundCanvas from '../components/backgroundCanvas'
import adminSearchResultDialog from '../components/adminSearchResultDialog'
import editPropsDialog from '../components/editPropsDialog'
import saveConfirmDialog from '../components/saveConfirmDialog'
export default {
  name: 'presentation',
  components: {saveConfirmDialog, RelationOpDialog, OpDialog, HeaderGroup, SideBar, sideOpDialog, TreeSvg, backgroundCanvas, adminSearchResultDialog, editPropsDialog},
  data () {
    return {
      active: 0,
      defaultImageUrl: require('@/assets/COINview.jpg'),
      fit: 'contain',
      mouseClick: '',
      currentEntity: '',
      svg: {},
      width: 0,
      height: 0,
      node: {},
      link: {},
      nodeText: {},
      linkText: {},
      simulation: {},
      nodeButtonGroup: {},
      defaultR: 10,
      arrowDir: '',
      scaleSize: 0,
      zoom: {},
      container: {},
      g: {},
      scaleSizeBefore: 0,
      clickDate: 0,
      difference_ms: 0,
      refreshSVG: true,
      timer: null,
      searchInput: '',
      searchType: '',
      refreshTBL: true
    }
  },
  computed: {
    ...mapGetters([
      'graphJSON',
      'curEntityName',
      'idReturned',
      'imageShow',
      'nodeData',
      'linkData',
      'EntityList',
      'stepShow',
      'show',
      'RelationName',
      'UpEn',
      'DownEn',
      'EntityName',
      'OpDialogFlag',
      'linkEditShow',
      'relationTextVisible',
      'currentEntityID',
      'mouseOnEntity',
      'formShow',
      'resultEntityList',
      'resultRelationList',
      'intelligenceSearchResult',
      'adminSearchResultVisible',
      'graphMode',
      'recipeTable',
      'editPropsDialogShow'
    ])
  },
  mounted () {
  },
  methods: {
    ...mapMutations([
      'set_GraphJSON',
      'set_curEntityName',
      'set_nodeData',
      'set_linkData',
      'set_EntityList',
      'update_EntityList',
      'set_stepShow',
      'set_show',
      'set_UpEn',
      'set_DownEn',
      'set_RelationName',
      'set_LinkEditShow',
      'set_EntityName',
      'set_OpDialogFlag',
      'set_CurrentEntityID',
      'set_CurrentRelationID',
      'set_FormShow',
      'set_MenuChoice',
      'set_SelectedType',
      'set_MouseOnEntity',
      'set_adminSearchResultVisible',
      'set_graphMode',
      'set_recipeTable',
      'set_intelligenceSearchResult'
    ]),
    ...mapActions([
      'getHello',
      'getGraphJSON',
      'uploadGraphJSON',
      'deleteEntity',
      'addEntity',
      'addRelationapi',
      'updateEntityName',
      'updateRelationName',
      'reverseRelationDir',
      'removeRelation',
      'getDistinctLabels',
      'getDistinctRelations',
      'intelligenceSearch'
    ]),
    async changeTable () {
      if (this.searchType === '1') await this.set_recipeTable(true)
      else await this.set_recipeTable(false)
      console.log('recipeTable: ' + this.recipeTable)
      this.set_intelligenceSearchResult(null)
      this.$refs.table.refresh()
    },
    async adminGetSearchResult () {
      document.getElementById('search').style.marginTop = '0px'
      console.log(this.searchType + ':' + this.searchInput)
      var label = this.searchType === '1' ? 'Recipe' : 'Ingredients'
      await this.intelligenceSearch({label: label, key: this.searchInput})
      console.log({label: label, key: this.searchInput})
      console.log(this.intelligenceSearchResult)
      await this.set_adminSearchResultVisible(true)
    },
    goBack () {
      this.set_graphMode(false)
      this.set_FormShow(false)
    },
    changeMode () {
      this.getDistinctLabels()
      this.getDistinctRelations()
      this.$refs.treeSvg.InitTree()
    },
    listenClick () {
      console.log('iiiiiinit')
      // var svgDoc = event.target.ownerDocument
      // console.log(svgDoc)
      var rect = document.getElementById('rect2')
      console.log(rect)
      rect.addEventListener('dblclick', function () {
        console.log('double clicked!')
      })
    },
    // 初始化
    InitInput () {
      this.set_UpEn('')
      this.set_DownEn('')
      this.set_RelationName('')
      this.set_EntityName('')
      this.active = 0
      this.arrowDir = ''
    },
    InitEntityList () {
      this.set_EntityList([])
      for (var i = 0; i < this.nodeData.length; i++) {
        if (this.nodeData[i].name === this.currentEntity) {
          continue
        }
        this.update_EntityList({
          value: this.nodeData[i].name,
          label: this.nodeData[i].name
        })
      }
    },
    changeSize () {
      console.log(this.formShow)
      console.log(this.width)
      if (this.formShow && this.width === 1300) {
        this.svg = d3.select('svg').attr('width', 1040)
        this.width = 1040
      } else if (!this.formShow) {
        this.svg = d3.select('svg').attr('width', 1300)
        this.width = 1300
      }
    },
    zoomed () {
      // translate变换矢量（使用二元组标识）scale当前尺度的数字
      // this.g.attr('transform', 'translate(' + d3.event.translate + ')scale(' + d3.event.scale + ')') // 画布缩放与移动
      // this.svg.attr('transform', 'scale(' + d3.event.scale + ')') // 画布缩放
      d3.select('g').attr('transform', d3.event.transform)
      this.linkText.attr('transform', d3.event.transform)
    },
    sliderChange (num) {
      var temp = (1 + 0.1 * num) / (1 + 0.1 * this.scaleSizeBefore)
      this.zoom.scaleBy(this.svg, temp)
      this.scaleSizeBefore = num
    },
    zoomInit () {
      var num = 0
      var temp = (1 + 0.1 * num) / (1 + 0.1 * this.scaleSizeBefore)
      this.zoom.scaleBy(this.svg, temp)
      this.scaleSizeBefore = num
      this.scaleSize = 0
    },
    searchResultShow () {
      console.log('resultList:')
      console.log(this.resultEntityList)
      var tempList = this.resultEntityList
      this.link.style('opacity', 0.05)
      this.linkText.style('opacity', 0)
      this.node.style('opacity', function (d) {
        if (tempList.indexOf(d.id) !== -1) {
          return 1
        }
        return 0.3
      })
      this.nodeText.style('opacity', function (d) {
        if (tempList.indexOf(d.id) !== -1) {
          return 1
        }
        return 0.9
      })
    },
    searchRelationResultShow () {
      console.log('resultList:')
      console.log(this.resultRelationList)
      var tempList = this.resultRelationList
      var nodeList = []
      this.link.style('opacity', function (d) {
        if (tempList.indexOf(d.id) !== -1) {
          nodeList.push(d.source.id)
          nodeList.push(d.target.id)
          return 1
        }
        return 0.2
      })
      this.linkText.style('opacity', function (d) {
        if (tempList.indexOf(d.id) !== -1) {
          return 1
        }
        return 0.2
      })
      this.node.style('opacity', function (d) {
        if (nodeList.indexOf(d.id) !== -1) {
          return 1
        }
        return 0.3
      })
      this.nodeText.style('opacity', function (d) {
        if (nodeList.indexOf(d.id) !== -1) {
          return 1
        }
        return 0.9
      })
    },
    searchResultShowEnd () {
      this.link.style('opacity', 1)
      this.linkText.style('opacity', 1)
      this.node.style('opacity', 1)
      this.nodeText.style('opacity', 1)
    },
    async Init () {
      var that = this
      console.log('innnnnnnnnnnnnnnnniiiiiiiiiiiiiiiiiiiiitttttttttt')
      // await this.getGraphJSON()
      this.zoom = d3.zoom()
        .scaleExtent([-10, 10])
        .on('zoom', this.zoomed)
      d3.selectAll('svg > *').remove()
      if (this.width === 0) {
        this.svg = d3.select('svg')
          .attr('width', 1300)
          .attr('height', 550)
          .attr('viewBox', '0 0 1300 550')
          .call(this.zoom)
          .on('dblclick.zoom', null)
        this.width = +this.svg.attr('width')
        this.height = +this.svg.attr('height')
      } else {
        this.svg = d3.select('svg')
          .attr('width', this.width)
          .attr('height', this.height)
          .attr('viewBox', '0 0 1300 550')
          .call(this.zoom)
          .on('dblclick.zoom', null)
          .on('wheel.zoom', null)
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
        .style('visibility', this.relationTextVisible) // todo: 过滤时可选不显示关系文字
        .attr('font-size', function (d) {
          return d.d3_properties.__D3_PROPS__textsize
        })
        .text(function (d) {
          return d.name
        })
        .on('click', this.linkClicked)

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
        .on('click', this.entityClicked)
        .on('dblclick', this.entityDblClicked)
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
        .style('fill', 'white')
        .attr('text-anchor', 'middle')
        .attr('dx', 20)
        .attr('font-size', function (d) {
          return d.textsize
        })
        .text(function (d) {
          return d.name
        })
        .on('click', this.entityClicked)

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
      // this.getDistinctLabels()
      // this.getDistinctRelations()
      // this.$refs.treeSvg.InitTree()
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
    entityClicked (d) {
      var that = this
      clearTimeout(that.timer)
      that.timer = setTimeout(function () {
        console.log('点击节点' + that.getID(d.name))
        that.set_CurrentEntityID(that.getID(d.name))
        console.log('currentID:' + that.currentEntityID)
        that.currentEntity = d.name
        that.currentEntityID = d.id
        that.InitEntityList()
        that.refresh()
        that.set_FormShow(true)
        that.set_MenuChoice(1)
        that.set_SelectedType('entity')
        that.set_MouseOnEntity(true)
        that.changeSize()
      }, 150)
      // this.set_show(!this.show)
      // this.set_stepShow('Op')
    },
    async entityDblClicked (d) {
      clearTimeout(this.timer)
      console.log('双双双击节点' + this.getID(d.name))
      this.set_CurrentEntityID(this.getID(d.name))
      var target = ''
      var Name = '新关系'
      await this.addEntity(target)
      console.log('添加新实体：' + target)
      var res = {}
      res['source'] = this.getID(d.name)
      res['target'] = this.idReturned
      res['name'] = Name
      await this.addRelationapi(res)
      await this.getGraphJSON()
      this.set_nodeData(this.graphJSON.entities)
      this.set_linkData(this.graphJSON.relations)
      console.log('before init')
      console.log(this.linkData)
      await this.processLinkData()
      await this.Init()
    },
    linkClicked (d) {
      var that = this
      // this.set_LinkEditShow(!this.linkEditShow)
      console.log('点击连线')
      console.log(d)
      this.set_CurrentRelationID(d.id)
      this.set_UpEn(d.source.name)
      this.set_DownEn(d.target.name)
      this.set_RelationName(d.name)
      that.refresh()
      that.set_FormShow(true)
      that.set_MenuChoice(1)
      that.set_SelectedType('relation')
      that.set_MouseOnEntity(true)
      that.changeSize()
    },
    svgDBLclicked () {
      console.log('double clicked!!!!!!!!')
      if (!this.mouseOnEntity) {
        this.addEntity('')
        console.log('double clicked!!!!!!!!')
        this.refreshSVG1()
      }
    },
    async refreshSVG1 () {
      await this.getGraphJSON()
      this.set_nodeData(this.graphJSON.entities)
      this.set_linkData(this.graphJSON.relations)
      this.processLinkData()
      this.Init()
    },
    /**
     * @return {boolean}
     */
    async AddUpFinish () {
      if (this.UpEn === '') {
        this.set_OpDialogFlag(false)
        Message.warning('请填写父实体名称！')
        return false
      }
      if (this.findEntity(this.UpEn)) {
        this.set_OpDialogFlag(false)
        Message.warning('不能填写已有的实体名称！')
        return false
      }
      if (this.RelationName === '') {
        this.set_OpDialogFlag(false)
        Message.warning('请填写关系名称！')
        return false
      }
      this.set_OpDialogFlag(true)
      var Name = this.RelationName
      var source = this.UpEn
      var en = {}
      en.name = source
      await this.addEntity(source) // mark
      console.log('relationname: ' + this.RelationName)
      console.log('添加新实体：' + source)
      var res = {}
      res['source'] = this.idReturned
      res['target'] = this.getID(this.currentEntity)
      res['name'] = Name
      console.log(res)
      await this.addRelationapi(res)
      await this.getGraphJSON()
      this.set_nodeData(this.graphJSON.entities)
      this.set_linkData(this.graphJSON.relations)
      console.log('before init')
      console.log(this.linkData)
      this.processLinkData()
      this.Init()
      console.log('添加关系')
      console.log(res)
      return true
    },
    /**
     * @return {boolean}
     */
    async AddDownFinish () {
      if (this.DownEn === '') {
        this.set_OpDialogFlag(false)
        Message.warning('请填写子实体名称！')
        return false
      }
      if (this.findEntity(this.DownEn)) {
        this.set_OpDialogFlag(false)
        Message.warning('不能填写已有的实体名称！')
        return false
      }
      if (this.RelationName === '') {
        this.set_OpDialogFlag(false)
        Message.warning('请填写关系名称！')
        return false
      }
      this.set_OpDialogFlag(true)
      var target = this.DownEn
      var Name = this.RelationName
      await this.addEntity(target)
      console.log('添加新实体：' + target)
      var res = {}
      res['source'] = this.getID(this.currentEntity)
      res['target'] = this.idReturned
      res['name'] = Name
      await this.addRelationapi(res)
      await this.getGraphJSON()
      this.set_nodeData(this.graphJSON.entities)
      this.set_linkData(this.graphJSON.relations)
      console.log('before init')
      console.log(this.linkData)
      await this.processLinkData()
      await this.Init()
      console.log('添加关系：' + res['source'] + ' 到 ' + res['target'] + '，' + res['relationName'])
      console.log(res)
      return true
    },
    /**
     * @return {boolean}
     */
    async AddRelationFinish (arrowDir) {
      if (arrowDir === '') {
        this.set_OpDialogFlag(false)
        Message.warning('请选择节点类型！')
        return false
      }
      if (this.UpEn === '' && this.DownEn === '') {
        this.set_OpDialogFlag(false)
        Message.warning('请选择另一端节点！')
        return false
      }
      if (this.RelationName === '') {
        this.set_OpDialogFlag(false)
        Message.warning('请填写关系名称！')
        return false
      }
      var res = {}
      if (arrowDir === 'to') {
        res['source'] = this.currentEntityID
        res['target'] = this.getID(this.DownEn)
      } else {
        res['target'] = this.currentEntityID
        res['source'] = this.getID(this.UpEn)
      }
      this.set_OpDialogFlag(true)
      res['name'] = this.RelationName
      await this.addRelationapi(res)
      await this.getGraphJSON()
      this.set_nodeData(this.graphJSON.entities)
      this.set_linkData(this.graphJSON.relations)
      this.processLinkData()
      this.Init()
      console.log('添加关系：' + res['source'] + ' 到 ' + res['target'] + '，' + res['relationName'])
      console.log(res)
      return true
    },
    async getGraphAsync () {
      let that = this
      console.log('getGraphAsync')
      await that.getGraphJSON()
      that.set_nodeData(this.graphJSON.entities)
      that.set_linkData(this.graphJSON.relations)
      that.processLinkData()
    },
    /**
     * @return {boolean}
     */
    async EditEntityFinish () {
      if (this.EntityName === '') {
        this.set_OpDialogFlag(false)
        this.$message({
          message: '请填写新的实体名称！',
          type: 'warning'
        })
        return false
      }
      if (this.findEntity(this.EntityName)) {
        this.set_OpDialogFlag(false)
        this.$message({
          message: '该实体名称已存在！',
          type: 'warning'
        })
        return false
      }
      this.set_OpDialogFlag(true)
      var newName = this.EntityName
      var id = this.getID(this.currentEntity)
      var res = {}
      res['id'] = id
      res['newName'] = newName
      console.log('更改实体名')
      console.log(res)
      await this.updateEntityName(res)
      await this.getGraphAsync()
      this.Init()
      return true
    },
    addStep () {
      if (this.active++ > 1) this.active = 0
    },
    findEntity (enName) {
      for (var i = 0; i < this.nodeData.length; i++) {
        if (this.nodeData[i].name === enName) {
          return true
        }
      }
      return false
    },
    processLinkData () {
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
    },
    getID (name) {
      // this.getGraphAsync()
      console.log('this.nodeData')
      console.log(this.nodeData)
      for (var i = 0; i < this.nodeData.length; i++) {
        if (this.nodeData[i].name === name) {
          return this.nodeData[i].id
        }
      }
    },
    outputGraph () {
      let serializer = new XMLSerializer()
      let source = '<?xml version="1.0" standalone="no"?>\r\n' + serializer.serializeToString(document.querySelector('svg'))
      let image = new Image()
      image.src = 'data:image/svg+xml;charset=utf-8,' + encodeURIComponent(source)
      let canvas = document.createElement('canvas')
      canvas.width = this.width
      canvas.height = this.height
      let context = canvas.getContext('2d')
      console.log('+++++++++++++++++++++++++++++++++++++++++++++++++')
      console.log(image.src)
      console.log('+++++++++++++++++++++++++++++++++++++++++++++++++')
      context.fillStyle = '#fff'
      context.fillRect(0, 0, 10000, 10000)
      image.onload = function () {
        context.drawImage(image, 0, 0)
        let a = document.createElement('a')
        a.download = `output.png`
        a.href = canvas.toDataURL(`image/png`)
        a.click()
      }
    },
    refresh () {
      this.$refs.menu.refreshForm1()
    },
    isLinked (d, o) {
      for (var i = 0; i < this.linkData.length; i++) {
        if ((this.linkData[i].source === d.id && this.linkData[i].target === o.id) || (this.linkData[i].source === o.id && this.linkData[i].target === d.id)) {
          return true
        }
      }
      return false
    },
    getSearchResult () {
    }
    // cut_n_json (data) {
    //   var resjson = []
    //   var temp = {'name': '', 'age': '', 'job': ''}
    //   for (var i = 0; i < data.length; i++) {
    //     temp['name'] = data[i]['name']
    //     temp['age'] = data[i]['age']
    //     temp['job'] = data[i]['job']
    //     console.log(temp)
    //     resjson.push(temp)
    //   }
    //   return resjson
    // }
  }
}
</script>

<style scoped>
.dividerFake{
  height: 1px;
  border-top: 1px solid #ddd;
  text-align: center;
}
.search{
  width: 600px;
  position: relative;
  margin: 80px auto 0px;
  height: 80px;
  -webkit-animation: marginTop 3s; /* Chrome, Safari, Opera */
}
.input-search >>>.el-input__inner{
  border: 1px solid #7EA9D3;
}
.input-search >>>.el-input-group__append{
  border: 1px solid #7EA9D3;
  background-color: #7EA9D3;
  color: white;
}
.el-select .el-input {
  width: 130px;
}
.input-with-select .el-input-group__prepend {
  background-color: #fff;
}
.input-search >>>.el-input-group__prepend {
  width: 60px;
  border: 1px solid #7EA9D3;
  border-right: 0;
}

</style>
