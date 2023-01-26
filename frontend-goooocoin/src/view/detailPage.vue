<template>
  <div>
    <background-canvas style="background: #f7f2e7;position: absolute;top: 0;bottom: 0;left: 0;right: 0;z-index: -1;pointer-events: none"></background-canvas>
    <el-container style="position: absolute;top: 0px;left: 0px;width: 100%;height: 100%;background: #f7f2e7;">
      <myheader :activeIndex="activeIndex"></myheader>
      <el-container>
        <el-main>
          <el-page-header @back="goBack" content="详情页面">
          </el-page-header>
          <recipeCard @updtisCollected="updtisCollected" :isCollected="isCollected"></recipeCard>
          <div class="kgbox">
            <knowledge-graph v-if="kgrefresh" @updtisCollected="updtisCollected" @sideboxRefresh="sideboxRefresh" :entityStack="entityStack" :entityInfoStack="entityInfoStack" ref="knowledgeGraph"></knowledge-graph>
          </div>
          <graphEditorBox @zoominit="zoominit" @zoomin="zoomin" @zoomout="zoomout" @kgrefresh="kgRefresh"></graphEditorBox>
        </el-main>
      </el-container>
      <el-footer></el-footer>
    </el-container>
    <div style="position: absolute;left: 20%;top: 20%;right: 20%;bottom: 20%" v-show="DialogueItemShow">
      <DialogueItem style="background: white;border: solid black 2px"></DialogueItem>
    </div>
  </div>
</template>

<script>
import knowledgeGraph from '../components/knowledgeGraph'
import {mapActions, mapGetters, mapMutations} from 'vuex'
import BackgroundCanvas from '../components/backgroundCanvas'
import DialogueItem from '../components/DialogueItem'
import Myheader from '../components/myheader'
import recipeCard from '../components/recipeCard'
import graphEditorBox from '../components/graphEditorBox'
export default {
  name: 'detailPage',
  components: {Myheader, BackgroundCanvas, knowledgeGraph, DialogueItem, recipeCard, graphEditorBox},
  computed: {
    ...mapGetters([
      'currentEntityInfo',
      'currentUserID',
      'DialogueItemShow',
      'collectionList',
      'graphJSON',
      'currentCenter',
      'verificationRes'
    ])
  },
  data () {
    return {
      direction: 'rtl',
      drawer: false,
      isCollected: false,
      entityStack: [],
      entityInfoStack: [],
      activeIndex: '1',
      pictureSrc: '',
      refresh: true,
      kgrefresh: true
    }
  },
  methods: {
    ...mapMutations([
      'set_DialogueItemShow',
      'set_nodeData',
      'set_linkData',
      'set_currentUserID',
      'set_currentEntityInfo',
      'set_currentCenterInfo'
    ]),
    ...mapActions([
      'collectEntity',
      'uncollect',
      'getRelativeRelation'
    ]),
    mounted () {
      this.set_DialogueItemShow(false)
    },
    async zoominit () {
      await this.$refs.knowledgeGraph.zoomInit()
    },
    zoomin () {
      this.$refs.knowledgeGraph.myzoomIn()
    },
    zoomout () {
      this.$refs.knowledgeGraph.myzoomOut()
    },
    kgRefresh () {
      this.kgrefresh = false
      this.$nextTick(() => {
        this.kgrefresh = true
      })
    },
    updtisCollected () {
      this.isCollected = false
      for (var i = 0; i < this.collectionList.length; i++) {
        if (this.collectionList[i].id === this.currentEntityInfo.id) {
          this.isCollected = true
        }
      }
    },
    sideboxRefresh (src) {
      this.pictureSrc = src
      this.refresh = false
      this.$nextTick(() => {
        console.log('ffffffffffffffffreshhhhhh')
        this.refresh = true
      })
    },
    handleCommand (command) {
      window.localStorage.setItem('userID', -1)
      window.localStorage.setItem('verificationRes', null)
      window.sessionStorage.setItem('currentCenter', null)
      this.$router.push('/login')
    },
    async goBack () {
      this.entityStack = JSON.parse(window.sessionStorage.getItem('entityStack')) === null ? [] : JSON.parse(window.sessionStorage.getItem('entityStack'))
      this.entityInfoStack = JSON.parse(window.sessionStorage.getItem('entityInfoStack')) === null ? [] : JSON.parse(window.sessionStorage.getItem('entityInfoStack'))
      if (this.entityStack.length !== 0) {
        var n = this.entityStack.pop()
        window.sessionStorage.setItem('entityStack', JSON.stringify(this.entityStack))
        await this.getRelativeRelation(n)
        var temp = this.entityInfoStack.pop()
        this.set_currentEntityInfo(temp)
        this.set_currentCenterInfo(temp)
        window.sessionStorage.setItem('entityInfoStack', JSON.stringify(this.entityInfoStack))
        this.set_nodeData(this.graphJSON.entities)
        this.set_linkData(this.graphJSON.relations)
        this.$refs.knowledgeGraph.processLinkData()
        this.$refs.knowledgeGraph.Init()
        this.updtisCollected()
      } else this.$router.go(-1)
      // 如果栈不为空，pop一个id然后getRelativeRelation，然后重画knowledgeGragh，如果为空则router go-1
    },
    avatarClick () {
      console.log('click')
      this.$router.push('/userpage')
    },
    GoToQ () {
      this.set_DialogueItemShow(true)
    },
    drawerChange () {
      if (this.drawer === false) {}
      this.drawer = !this.drawer
    },
    collect () {
      if (this.currentUserID === -1) {
        this.$message.error('抱歉，请先登录再收藏哦')
      } else {
        if (this.isCollected) {
          this.uncollect({id: this.currentUserID, entity: this.currentEntityInfo.id})
          this.$message.success('取消收藏成功')
          this.isCollected = false
        } else {
          this.collectEntity({id: this.currentUserID, entity: this.currentEntityInfo.id, name: this.currentEntityInfo.name})
          this.$message.success('收藏成功！')
          this.isCollected = true
        }
      }
      console.log({id: this.currentUserID, entity: this.currentEntityInfo.id, name: this.currentEntityInfo.name})
    },
    handleSelect (key, keyPath) {
      this.set_DialogueItemShow(false)
      if (key === '1') this.$router.push('/home')
      else if (key === '2') {
        if (this.currentUserID === -1) this.$router.push('/login')
        else this.$router.push('/userpage')
      }
    }
  }
}
</script>

<style scoped>
  .demo-table-expand {
    font-size: 0;
  }
  .demo-table-expand >>>label {
    width: 90px;
    color: #99a9bf;
  }
  .demo-table-expand >>>.el-form-item {
    margin-right: 0;
    margin-bottom: 0;
    width: 33%;
  }
  .el-menu-demo >>>.el-menu--horizontal>.el-menu-item{
    height: 70px;
  }
  .el-menu--horizontal>.el-menu-item{
    height: 68px;
    border-bottom: 3px solid #ffffff;
    width: 90px;
    font-size: 15px;
    text-align: center;
    background-color: transparent;
  }
  .kgbox {
    width: 1000px;
    height: 580px;
    margin-left: 30px;
    margin-top: 30px;
    /*background-color: #fcf9f4;*/
    background-color: #fffefb;
    /*border: 2px solid rgba(236,147,144,0.6);*/
    border-radius: 0 10px 0 0;
    float: left;
  }
</style>
