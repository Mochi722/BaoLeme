<template>
  <div class="sidebox">
    <el-scrollbar style="height: 40%;margin-top: 30px" class="el-scrollbar" ref="myScrollbar">
      <img :src="currentEntityInfo.properties.图片 === undefined ? 'https://gitee.com/MochiQ/req_imgs/raw/master/%E6%B2%A1%E6%89%BE%E5%88%B0%E5%9B%BE%E7%89%87.png' : currentEntityInfo.properties.图片" width="400px"/>
    </el-scrollbar>
    <div class="collectbox" style="margin-top: 10px">
      <el-button v-if="isCollected" type="warning" icon="el-icon-star-off" circle @click="collect"></el-button>
      <p v-if="isCollected" style="color: darkgoldenrod">已收藏</p>
      <el-button v-else type="warning" icon="el-icon-star-off" circle plain @click="collect"></el-button>
    </div>
    <el-scrollbar style="height: 40%;margin-top: 20px" class="el-scrollbar">
      <el-form v-if="currentEntityInfo!==null" label-position="left" inline class="demo-table-expand">
        <el-form-item label="实体名称" style="width: 85%;text-align: left">
          <span slot="label" style="width: 90px; color: #EC9390">名&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;称</span>
          <span>{{currentEntityInfo.properties.name}}</span>
        </el-form-item>
        <el-form-item label="所属 label" style="width: 85%;text-align: left;margin-top: -20px;">
          <span slot="label" style="width: 90px; color: #EC9390">所属标签</span>
          <span>{{currentEntityInfo.labels[0] === 'Recipe'? '菜谱':'食材'}}</span>
        </el-form-item>
<!--        <el-form-item label="实体名称" style="width: 85%;text-align: left;margin-top: -20px;">-->
<!--          <span slot="label" style="width: 90px; color: #EC9390">实体 ID</span>-->
<!--          <span>{{currentEntityInfo.id}}</span>-->
<!--        </el-form-item>-->
        <el-form-item label="实体名称" style="width: 85%;text-align: left;margin-top: -20px;" v-if="currentEntityInfo.labels[0] === 'Ingredients'">
          <span slot="label" style="width: 90px; color: #EC9390">收藏热度</span>
          <span>{{currentEntityInfo.properties.heat}}</span>
        </el-form-item>
        <el-form-item label="实体名称" style="width: 85%;text-align: left;margin-top: -20px;" v-if="currentEntityInfo.labels[0] === 'Ingredients'">
          <span slot="label" style="width: 90px; color: #EC9390">禁忌人群</span>
          <span>{{currentEntityInfo.properties.禁忌人群}}</span>
        </el-form-item>
        <el-form-item label="实体名称" style="width: 85%;text-align: left;margin-top: -20px;" v-if="currentEntityInfo.labels[0] === 'Ingredients'">
          <span slot="label" style="width: 90px; color: #EC9390">适用人群</span>
          <span>{{currentEntityInfo.properties.适用人群}}</span>
        </el-form-item>
        <el-form-item label="实体名称" style="width: 85%;text-align: left;margin-top: -20px;" v-if="currentEntityInfo.labels[0] === 'Ingredients'">
          <span slot="label" style="width: 90px; color: #EC9390">类&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;别</span>
          <span>{{currentEntityInfo.properties.类别}}</span>
        </el-form-item>
        <el-form-item label="实体名称" style="width: 85%;text-align: left;margin-top: -20px;" v-if="currentEntityInfo.labels[0] === 'Ingredients'">
          <span slot="label" style="width: 90px; color: #EC9390">营养价值</span>
          <span>{{currentEntityInfo.properties.营养价值}}</span>
        </el-form-item>
        <el-form-item label="实体名称" style="width: 85%;text-align: left;margin-top: -20px;" v-if="currentEntityInfo.labels[0] === 'Ingredients'">
          <span slot="label" style="width: 90px; color: #EC9390">食材简介</span>
          <span>{{currentEntityInfo.properties.食材简介}}</span>
        </el-form-item>
        <el-form-item label="实体名称" style="width: 85%;text-align: left;margin-top: -20px;" v-if="currentEntityInfo.labels[0] === 'Ingredients'">
          <span slot="label" style="width: 90px; color: #EC9390">食用功效</span>
          <span>{{currentEntityInfo.properties.食用功效}}</span>
        </el-form-item>
        <el-form-item label="实体名称" style="width: 85%;text-align: left;margin-top: -20px;" v-if="currentEntityInfo.labels[0] === 'Ingredients'">
          <span slot="label" style="width: 90px; color: #EC9390">选购技巧</span>
          <span>{{currentEntityInfo.properties.选购技巧}}</span>
        </el-form-item>
        <el-form-item label="实体名称" style="width: 85%;text-align: left;margin-top: -20px;" v-if="currentEntityInfo.labels[0] === 'Recipe'">
          <span slot="label" style="width: 90px; color: #EC9390">口&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;味</span>
          <span>{{currentEntityInfo.properties.口味}}</span>
        </el-form-item>
        <el-form-item label="实体名称" style="width: 85%;text-align: left;margin-top: -20px;" v-if="currentEntityInfo.labels[0] === 'Recipe'">
          <span slot="label" style="width: 90px; color: #EC9390">工&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;艺</span>
          <span>{{currentEntityInfo.properties.工艺}}</span>
        </el-form-item>
        <el-form-item label="实体名称" style="width: 85%;text-align: left;margin-top: -20px;" v-if="currentEntityInfo.labels[0] === 'Recipe'">
          <span slot="label" style="width: 90px; color: #EC9390">类&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;型</span>
          <span>{{currentEntityInfo.properties.类型 === 'null' ? '其他' : currentEntityInfo.properties.类型}}</span>
        </el-form-item>
        <el-form-item label="实体名称" style="width: 85%;text-align: left;margin-top: -20px;" v-if="currentEntityInfo.labels[0] === 'Recipe'">
          <span slot="label" style="width: 90px; color: #EC9390">耗&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;时</span>
          <span>{{currentEntityInfo.properties.耗时}}</span>
        </el-form-item>
        <el-form-item label="实体名称" style="width: 85%;text-align: left;margin-top: -20px;" v-if="currentEntityInfo.labels[0] === 'Recipe'">
          <span slot="label" style="width: 90px; color: #EC9390">做&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;法</span>
          <span>{{currentEntityInfo.properties.做法}}</span>
        </el-form-item>
      </el-form>
    </el-scrollbar>
  </div>
</template>

<script>
import {mapActions, mapGetters, mapMutations} from 'vuex'
export default {
  name: 'recipeCard',
  data () {
    return {
      picurl: '../assets/404.jpg'
    }
  },
  async mounted () {
    console.log('recipeCard')
    console.log('888888888888888')
    console.log(this.$refs['myScrollbar'].wrap.scrollHeight)
    this.$emit('updtisCollected')
    this.$refs['myScrollbar'].wrap.scrollTop = this.$refs['myScrollbar'].wrap.scrollHeight
  },
  props: [
    'refresh',
    'isCollected'
  ],
  computed: {
    ...mapGetters([
      'currentEntityInfo',
      'currentUserID',
      'collectionList'
    ])
  },
  methods: {
    ...mapMutations([]),
    ...mapActions([
      'uncollect',
      'collectEntity'
    ]),
    collect () {
      if (this.currentUserID === -1) {
        this.$message.error('抱歉，请先登录再收藏哦')
      } else {
        if (this.isCollected) {
          this.uncollect({id: this.currentUserID, entity: this.currentEntityInfo.id})
          this.$message.success('取消收藏成功')
          this.isCollected = false
        } else {
          this.collectEntity({id: this.currentUserID, entity: this.currentEntityInfo.id, name: this.currentEntityInfo.properties.name})
          this.$message.success('收藏成功！')
          this.isCollected = true
        }
      }
      console.log({id: this.currentUserID, entity: this.currentEntityInfo.id, name: this.currentEntityInfo.properties.name})
    }
  }
}
</script>

<style scoped>
  .sidebox {
    width: 450px;
    height: 640px;
    margin-top: 30px;
    margin-left: 115px;
    background-image: linear-gradient(to right, #f1d2d6, #fae7db);
    float: left;
    border-radius: 10px 0 0 10px;
  }
  .el-scrollbar >>>.el-scrollbar__wrap {
    overflow-x: hidden;
  }
</style>
