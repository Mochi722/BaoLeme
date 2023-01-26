<template>
  <div class="all">
    <myheader :activeIndex="activeIndex"></myheader>
    <el-container style="height: 85%;float: top">
      <background-canvas style="position: absolute;width: 100%;height: 85%;pointer-events: none;z-index: 0"></background-canvas>
        <el-main class="main">
<!--          <el-scrollbar style="height: 100%;" class="el-scrollbar">-->
          <div class="search">
            <el-popover
              placement="bottom-start"
              title=""
              width="180"
              arrow-offset="5"
              trigger="manual"
              content="选择搜索类型后再试试吧~"
              v-model="visible">
              <el-input slot="reference" :placeholder="placeholder" v-model="searchInput" class="input-search">
                <el-select class="preSelect" v-model="searchType" slot="prepend" placeholder="请选择" @change="unfoldCheckPan">
                  <el-option label="搜美食" value="1"></el-option>
                  <el-option label="搜食材" value="2"></el-option>
                  <el-option label="做点啥" value="3"></el-option>
                </el-select>
                <el-button slot="append" icon="el-icon-search" @click="getSearchResult"></el-button>
              </el-input>
            </el-popover>
            <el-popover
              placement="bottom"
              width="575"
              trigger="manual"
              v-model="popVisible"
              @blur="popVisible = false"
            >
              <div class="checkPan">
                <div class="recipeCheck" v-if="searchType === '1'" style="width: 575px">
                  <el-divider>选择口味</el-divider>
                  <el-checkbox-group v-model="flavorCheckList" style="margin-top: 10px;">
                    <el-checkbox v-for="item in flavorList.slice(0,5)" :label="item.name" :key="item.length" style="width: 75px">
                      {{item.name}}({{item.count}})
                    </el-checkbox>
                    <el-checkbox v-for="item in flavorList.slice(5,flavorList.length)" :label="item" :key="item.length" v-if="moreFlavors" style="width: 75px" >
                      {{item.name}}({{item.count}})
                    </el-checkbox>
                    <el-link type="primary" style="margin: -10px 0 0 20px" :underline="false" @click="moreFlavors = true" v-if="!moreFlavors">>>更多</el-link>
                    <el-link type="primary" style="margin: -10px 0 0 20px" :underline="false" @click="moreFlavors = false" v-if="moreFlavors">收起</el-link>
                  </el-checkbox-group>
                  <div style="height: 10px;"></div>
                  <el-divider>选择烹饪工艺</el-divider>
                  <el-checkbox-group v-model="methodCheckList" style="margin-top: 10px;">
                    <el-checkbox v-for="item in methodList.slice(0,5)" :label="item.name" :key="item.length" style="width: 75px">
                      {{item.name}}({{item.count}})
                    </el-checkbox>
                    <el-checkbox v-for="item in methodList.slice(5,methodList.length)" :label="item.name" :key="item.length" v-if="moreMethods"  style="width: 75px">
                      {{item.name}}({{item.count}})
                    </el-checkbox>
                    <el-link type="primary" style="margin: -10px 0 0 20px" :underline="false" @click="moreMethods = true" v-if="!moreMethods">>>更多</el-link>
                    <el-link type="primary" style="margin: -10px 0 0 20px" :underline="false" @click="moreMethods = false" v-if="moreMethods">收起</el-link>
                  </el-checkbox-group>
                  <div style="height: 10px"></div>
                  <el-divider>选择菜肴类型</el-divider>
                  <el-checkbox-group v-model="typeCheckList" style="margin-top: 10px;margin-bottom: 10px">
                    <el-checkbox v-for="item in typeList.slice(0,5)" :label="item.name" :key="item.length" style="width: 75px">
                      {{item.name === 'null' ? '其他' : item.name}}({{item.count}})
                    </el-checkbox>
                    <el-checkbox v-for="item in typeList.slice(5,typeList.length)" :label="item.name" :key="item.length" v-if="moreTypes" style="width: 75px" >
                      {{item.name}}({{item.count}})
                    </el-checkbox>
                    <el-link type="primary" style="margin: -10px 0 0 20px" :underline="false"  @click="moreTypes = true" v-if="!moreTypes">>>更多</el-link>
                    <el-link type="primary" style="margin: -10px 0 0 20px" :underline="false"  @click="moreTypes = false" v-if="moreTypes">收起</el-link>
                  </el-checkbox-group>
                </div>
                <div class="ingredientsCheck" v-if="searchType === '2'">
                  <el-divider>选择食材类别</el-divider>
                  <el-checkbox-group v-model="ingreTypeCheckList" style="margin-top: 10px;margin-bottom: 10px">
                    <el-checkbox v-for="item in ingreTypeList.slice(0,5)" :label="item.name" :key="item.length" style="width: 100px">
                      {{item.name}}({{item.count}})
                    </el-checkbox>
                    <el-checkbox v-for="item in ingreTypeList.slice(5,typeList.length)" :label="item.name" :key="item.length" v-if="moreIngreTypes"  style="width: 100px">
                      {{item.name}}({{item.count}})
                    </el-checkbox>
                    <el-link type="primary" style="margin: -10px 0 0 20px" :underline="false" @click="moreIngreTypes = true" v-if="!moreIngreTypes">>>更多</el-link>
                    <el-link type="primary" style="margin: -10px 0 0 20px" :underline="false" @click="moreIngreTypes = false" v-if="moreIngreTypes">收起</el-link>
                  </el-checkbox-group>
                </div>
                <div class="ingrePanel" v-if="searchType === '3'">
                  <el-divider>选择已有食材</el-divider>
                  <el-tag
                    v-for="tag in ingrePanel"
                    :key="tag"
                    closable
                    style="float:left;"
                    @close="handleClose(tag)"
                  >
                    {{tag}}
                  </el-tag>
                  <el-autocomplete
                    class="input-new-tag"
                    v-if="inputVisible"
                    v-model="inputValue"
                    ref="saveTagInput"
                    size="small"
                    @keyup.enter.native="handleInputConfirm"
                    :fetch-suggestions="querySearchAsync"
                    placeholder="请输入内容"
                  ></el-autocomplete>
                  <el-button v-else class="button-new-tag" size="small" @click="inputVisible = true" style="float:left;">+</el-button>
                  <el-button v-if="inputVisible" icon="el-icon-check" circle type="success" size="small" style="" @click="handleInputConfirm"></el-button>
                  <div style="height: 20px"></div>
                  <el-divider>选择口味</el-divider>
                  <el-checkbox-group v-model="flavorCheckList" style="margin-top: 10px;">
                    <el-checkbox v-for="item in flavorList.slice(0,5)" :label="item.name" :key="item.length" style="width: 75px">
                      {{item.name}}({{item.count}})
                    </el-checkbox>
                    <el-checkbox v-for="item in flavorList.slice(5,flavorList.length)" :label="item" :key="item.length" v-if="moreFlavors" style="width: 75px" >
                      {{item.name}}({{item.count}})
                    </el-checkbox>
                    <el-link type="primary" style="margin: -10px 0 0 20px" :underline="false" @click="moreFlavors = true" v-if="!moreFlavors">>>更多</el-link>
                    <el-link type="primary" style="margin: -10px 0 0 20px" :underline="false" @click="moreFlavors = false" v-if="moreFlavors">收起</el-link>
                  </el-checkbox-group>
                </div>
              </div>
            </el-popover>
          </div>
<!--          <div id="robot">-->
<!--            <el-button icon="el-icon-chat-dot-round" circle @click="turnToQ"></el-button>-->
<!--          </div>-->
            <BallCanvas v-if="updt" ref="BallCanvas" v-bind:resList="canvasList" :sequance="sequance" :illegalSearch="illegalSearch"></BallCanvas>
<!--            <BallCanvas ref="BallCanvas" v-bind:resList="recommend" v-if="sequance === 1"></BallCanvas>-->
<!--            <BallCanvas ref="BallCanvas" v-bind:resList="collection" v-if="sequance === 2 && collection.length !== 0"></BallCanvas>-->
            <!--            <BallCanvas ref="BallCanvas" v-bind:resList="resultList" v-if="sequance === 3"></BallCanvas>-->
            <img @mouseover="lefthover" @mouseout="leftunhover" :src="anglelefturl" style="position: absolute;left: 20%;top: 60%" @click="sequanceMinus"/>
            <img @mouseover="righthover" @mouseout="rightunhover" :src="anglerighturl" style="position: absolute;right: 20%;top: 60%" @click="sequancePlus"/>
<!--            <el-button icon="el-icon-caret-left" @click="sequanceMinus" style="position: absolute;left: 20%;top: 50%"></el-button>-->
<!--            <el-button icon="el-icon-caret-right" @click="sequancePlus" style="position: absolute;right: 20%;top: 50%"></el-button>-->
<!--          </el-scrollbar>-->
        </el-main>
      <div v-if="sequance === 1" style="position:absolute;top:30%;left: 47%;color: #e8989a;font-size: 25px;font-weight: bold">每日推荐</div>
<!--      <div style="border-bottom: 1px solid #e8989a;width: 30%;position:absolute;top:35%;margin-left: 35%"></div>-->
      <div v-if="sequance === 2" style="position:absolute;top:30%;left: 47%;color: #e8989a;font-size: 25px;font-weight: bold">我的收藏</div>
      <div v-if="sequance === 3" style="position:absolute;top:30%;left: 48%;color: #e8989a;font-size: 25px;font-weight: bold">搜索</div>
      <div style="position: absolute;left: 20%;top: 20%;right: 20%;bottom: 20%;background-color: white;z-index: 999" v-show="DialogueItemShow">
        <DialogueItem style="background-color: white;border: solid black 2px;"></DialogueItem>
      </div>
    </el-container>
  </div>
</template>
<script>
import {mapActions, mapGetters, mapMutations} from 'vuex'
import backgroundCanvas from '../components/backgroundCanvas'
import BallCanvas from '../components/BallCanvas'
import DialogueItem from '../components/DialogueItem'
import myheader from '../components/myheader'

export default {
  name: 'homepage',
  components: {backgroundCanvas, BallCanvas, DialogueItem, myheader},
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
      placeholder: '搜美食/搜食材/搜口味……',
      ingrePanel: [],
      inputVisible: false,
      inputValue: '',
      canvasList: [],
      illegalSearch: false,
      visible: false,
      updt: true,
      activeIndex: '1',
      anglelefturl: 'https://gitee.com/MochiQ/req_imgs/raw/master/whiteleft%20(3).png',
      anglerighturl: 'https://gitee.com/MochiQ/req_imgs/raw/master/whiteright.png'
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
      'intelligenceSearchResult',
      'graphJSON',
      'DialogueItemShow'
    ])
  },
  async mounted () {
    await this.getDistinctPropsValue({label: 'Recipe', propName: '口味'})
    await this.getDistinctPropsValue({label: 'Recipe', propName: '工艺'})
    await this.getDistinctPropsValue({label: 'Recipe', propName: '类型'})
    var userID = window.localStorage.getItem('userID')
    console.log(window.localStorage.getItem('verificationRes'))
    if (userID !== '-1') {
      console.log('noooo ')
      this.set_currentUserID(userID)
      this.set_verificationRes(JSON.parse(window.localStorage.getItem('verificationRes')))
    }
    if (this.currentUserID === -1) this.$router.push('/login')
    else {
      this.set_DialogueItemShow(false)
      await this.getNodes()
      console.log(this.canvasList.length)
      // this.$refs.BallCanvas.canvasInit()
      // this.$refs.BallCanvas.init_all()
      this.$refs.BallCanvas.initcanvas()
    }
  },
  methods: {
    ...mapMutations([
      'set_DialogueItemShow',
      'set_currentCenter',
      'set_currentUserID',
      'set_verificationRes'
    ]),
    ...mapActions([
      'getRecommend',
      'searchEntityByName',
      'getCollection',
      'getDistinctPropsValue',
      'intelligenceSearch',
      'getRelativeRelation',
      'cookWhat'
    ]),
    lefthover () {
      this.anglelefturl = 'https://gitee.com/MochiQ/req_imgs/raw/master/pinkleft.png'
    },
    leftunhover () {
      this.anglelefturl = 'https://gitee.com/MochiQ/req_imgs/raw/master/whiteleft%20(3).png'
    },
    righthover () {
      this.anglerighturl = 'https://gitee.com/MochiQ/req_imgs/raw/master/pinkright.png'
    },
    rightunhover () {
      this.anglerighturl = 'https://gitee.com/MochiQ/req_imgs/raw/master/whiteright.png'
    },
    handleInputConfirm () {
      let inputValue = this.inputValue
      if (inputValue) {
        this.ingrePanel.push(inputValue)
      }
      console.log(this.ingrePanel)
      this.inputVisible = false
      this.inputValue = ''
      this.unsaved = true
    },
    async querySearchAsync (queryString, cb) {
      await this.intelligenceSearch({label: 'Ingredients'})
      var ingrelist = this.listProcessor(this.intelligenceSearchResult)
      var results = queryString ? ingrelist.filter(this.createStateFilter(queryString)) : ingrelist

      clearTimeout(this.timeout)
      this.timeout = setTimeout(() => {
        cb(results)
      }, 3000 * Math.random())
    },
    listProcessor (list) {
      var res = []
      for (var i = 0; i < list.length; i++) {
        res.push({value: list[i].properties.name})
      }
      return res
    },
    handleClose (tag) {
      this.ingrePanel.splice(this.ingrePanel.indexOf(tag), 1)
    },
    turnToQ () {
      this.set_DialogueItemShow(true)
    },
    async handleClick (item) {
      await this.set_currentCenter(item.id)
      window.sessionStorage.setItem('currentCenter', item.id)
      console.log('ssssession: ' + window.sessionStorage.getItem('currentCenter'))
      await this.getRelativeRelation(item.id)
      this.set_DialogueItemShow(false)
      this.$router.push('/home/detailPage')
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
      console.log('==============')
      console.log(res)
      await this.getRecommend(res)
      await this.getCollection(this.currentUserID)
      this.recommend = this.recommendList.content
      this.collection = this.collectionList
      console.log(this.recommend)
      console.log(this.collection)
      this.canvasList = this.recommend
      return true
    },
    async getSearchResult () {
      this.popVisible = false
      this.illegalSearch = false
      var label = this.searchType === '1' ? 'Recipe' : 'Ingredients'
      var props = {}
      var hasProps = false
      var searchString = {}
      if (this.searchType === '') {
        this.illegalSearch = true
        this.visible = true
      } else {
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
        } else if (this.searchType === '2') {
          if (this.ingreTypeCheckList.length !== 0) {
            props['类别'] = this.ingreTypeCheckList
            hasProps = true
          }
        } else {
          if (this.flavorCheckList.length !== 0) {
            props['口味'] = this.flavorCheckList
            hasProps = true
            searchString.props = props
          }
          searchString.names = this.ingrePanel
          console.log(searchString)
          await this.cookWhat(searchString)
        }
        if (hasProps) searchString.props = props
        if (this.searchInput !== null) searchString.key = this.searchInput
        searchString.label = label
        if (this.searchType === '1' || this.searchType === '2') await this.intelligenceSearch(searchString)
        this.resultList = this.intelligenceSearchResult
        this.searchOrNot = true
      }
      this.sequance = 3
      this.searchType = ''
      await this.set_all()
      await this.$refs.BallCanvas.canvasInit()
      // this.illegalSearch = false
      // this.updt = false
      // this.updt = true
    },
    toDetail (obj) {
      var res = $(obj).text()
      alert(res)
      this.set_DialogueItemShow(false)
      this.$router.push('/home/detailPage')
    },
    async unfoldCheckPan () {
      this.visible = false
      if (this.searchType === '1') {
        this.placeholder = '今天吃点啥……'
        console.log('00000000')
        console.log(this.flavorList)
      } else if (this.searchType === '2') {
        await this.getDistinctPropsValue({label: 'Ingredients', propName: '类别'})
        this.placeholder = '请输入名称食材……'
      } else {
        this.placeholder = '我有这些食材，我能做些什么呢……'
      }
      this.popVisible = true
    },
    async sequancePlus () {
      this.sequance++
      if (!this.searchOrNot && this.sequance === 3) this.sequance = 1
      if (this.sequance === 4) {
        this.sequance = 1
        this.illegalSearch = false
      }
      console.log(this.sequance)
      await this.set_all()
      console.log('canvasList')
      console.log(this.canvasList)
      await this.$refs.BallCanvas.canvasInit()
      this.illegalSearch = false
    },
    async sequanceMinus () {
      this.sequance--
      if (this.illegalSearch && this.sequance === 2) this.illegalSearch = false
      if (!this.searchOrNot && this.sequance === 0) this.sequance = 2
      if (this.searchOrNot && this.sequance === 0) this.sequance = 3
      console.log(this.sequance)
      await this.set_all()
      console.log('canvasList')
      console.log(this.canvasList)
      await this.$refs.BallCanvas.canvasInit()
      this.illegalSearch = false
    },
    set_all () {
      switch (this.sequance) {
        case 1:
          this.canvasList = this.recommend
          break
        case 2:
          this.canvasList = this.collection
          break
        case 3:
          this.canvasList = this.resultList
          break
      }
      return true
    }
  }
}
</script>

<style scoped>
  .all{
    position: absolute;
    width: 100%;
    height: 100%;
    background: #f7f2e7;
  }
  .main{
    /*overflow: hidden;*/
  }
  .search{
    width: 600px;
    position: relative;
    margin: 40px auto 0px;
    height: 80px;
  }
  .input-search >>>.el-input__inner{
    border: 1px solid #EC9390;
  }
  .input-search >>>.el-input-group__append{
    border: 1px solid #EC9390;
    background-color: #EC9390;
    color: white;
  }
  .input-search >>>.el-input-group__prepend {
    width: 60px;
    border: 1px solid #EC9390;
    border-right: 0;
    background-image: linear-gradient(to right, #d48c99, #EC9390);
    color: white;
  }
  .el-select .el-input {
    width: 130px;
  }
  .input-with-select .el-input-group__prepend {
    background-color: #fff;
  }
  .el-scrollbar >>>.el-scrollbar__wrap {
    overflow-x: hidden;
    height: 100%;
  }
</style>
