<template>
  <div class="tableBox" v-if="adminSearchResultVisible">
    <el-scrollbar style="height: 100%;" class="el-scrollbar">
      <el-table
        :data="intelligenceSearchResult"
        style="width: 100%;" v-if="refreshTable">
        <el-table-column type="expand" v-if="recipeTable">
          <template slot-scope="props">
            <el-form label-position="left" inline class="demo-table-expand">
              <el-form-item label="实体名称">
                <span>{{ props.row.properties.name }}</span>
              </el-form-item>
              <el-form-item label="所属 label">
                <span>{{ props.row.labels[0] }}</span>
              </el-form-item>
              <el-form-item label="实体 ID">
                <span>{{ props.row.id }}</span>
              </el-form-item>
              <el-form-item label="mysql ID">
                <span>{{ props.row.properties.myId }}</span>
              </el-form-item>
              <el-form-item label="收藏热度">
                <span>{{ props.row.properties.heat }}</span>
              </el-form-item>
              <el-form-item label="口味">
                <span>{{ props.row.properties.口味 }}</span>
              </el-form-item>
              <el-form-item label="工艺">
                <span>{{ props.row.properties.工艺 }}</span>
              </el-form-item>
              <el-form-item label="类型">
                <span>{{ props.row.properties.类型 }}</span>
              </el-form-item>
              <el-form-item label="耗时">
                <span>{{ props.row.properties.耗时 }}</span>
              </el-form-item>
              <el-form-item label="做法" style="width: 100%">
                <span>{{ props.row.properties.做法 }}</span>
              </el-form-item>
            </el-form>
          </template>
        </el-table-column>
        <el-table-column type="expand" v-if="!recipeTable">
          <template slot-scope="props">
            <el-form label-position="left" inline class="demo-table-expand">
              <el-form-item label="实体名称">
                <span>{{ props.row.properties.name }}</span>
              </el-form-item>
              <el-form-item label="所属 label">
                <span>{{ props.row.labels[0] }}</span>
              </el-form-item>
              <el-form-item label="实体 ID">
                <span>{{ props.row.id }}</span>
              </el-form-item>
              <el-form-item label="mysql ID">
                <span>{{ props.row.properties.myId }}</span>
              </el-form-item>
              <el-form-item label="收藏热度">
                <span>{{ props.row.properties.heat }}</span>
              </el-form-item>
              <el-form-item label="类别">
                <span>{{ props.row.properties.类别 }}</span>
              </el-form-item>
              <el-form-item label="营养价值" style="width: 100%">
                <span>{{ props.row.properties.营养价值 }}</span>
              </el-form-item>
              <el-form-item label="禁忌人群" style="width: 100%">
                <span>{{ props.row.properties.禁忌人群 }}</span>
              </el-form-item>
              <el-form-item label="适用人群" style="width: 100%">
                <span>{{ props.row.properties.适用人群 }}</span>
              </el-form-item>
              <el-form-item label="选购技巧" style="width: 100%">
                <span>{{ props.row.properties.选购技巧 }}</span>
              </el-form-item>
              <el-form-item label="食材简介" style="width: 100%">
                <span>{{ props.row.properties.食材简介 }}</span>
              </el-form-item>
              <el-form-item label="食用功效" style="width: 100%">
                <span>{{ props.row.properties.食用功效 }}</span>
              </el-form-item>
            </el-form>
          </template>
        </el-table-column>
        <el-table-column
          label="实体 ID"
          prop="id">
        </el-table-column>
        <el-table-column
          label="实体名称"
          prop="properties.name">
        </el-table-column>
        <el-table-column
          label="热度"
          prop="properties.heat">
        </el-table-column>
        <el-table-column
          label="口味"
          prop="properties.口味" v-if="recipeTable">
        </el-table-column>
        <el-table-column
          label="工艺"
          prop="properties.工艺" v-if="recipeTable">
        </el-table-column>
        <el-table-column
          label="类型"
          prop="properties.类型" v-if="recipeTable">
        </el-table-column>
        <el-table-column
          label="类别"
          prop="properties.类别" v-if="!recipeTable">
        </el-table-column>
        <el-table-column
          fixed="right"
          label="操作"
          width="100">
          <template slot-scope="scope">
            <el-button @click="handleClick(scope.row)" type="text" size="small">查看图谱</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-scrollbar>
  </div>
</template>

<script>
import {mapActions, mapGetters, mapMutations} from 'vuex'
export default {
  name: 'adminSearchResultDialog',
  data () {
    return {
      visible: false,
      refreshTable: true
    }
  },
  computed: {
    ...mapGetters([
      'adminSearchResultVisible',
      'intelligenceSearchResult',
      'graphMode',
      'recipeTable',
      'graphJSON'
    ])
  },
  mounted () {
    console.log('********: ' + this.recipeTable)
  },
  methods: {
    ...mapMutations([
      'set_adminSearchResultVisible',
      'set_graphMode',
      'set_nodeData',
      'set_linkData'
    ]),
    ...mapActions([
      'getRelativeRelation'
    ]),
    dialogClose () {
      this.visible = false
      this.set_adminSearchResultVisible(false)
    },
    handleClose () {
      this.dialogClose()
    },
    async handleClick (item) {
      console.log(item)
      this.set_graphMode(true)
      console.log(this.graphMode)
      await this.getRelativeRelation(item.id)
      console.log('graphJSON:')
      console.log(this.graphJSON)
      console.log(this.$parent.$parent.$parent.$parent.$parent)
      this.set_nodeData(this.graphJSON.entities)
      this.set_linkData(this.graphJSON.relations)
      this.$parent.$parent.$parent.$parent.$parent.processLinkData()
      this.$parent.$parent.$parent.$parent.$parent.Init()
    },
    refresh () {
      this.refreshTable = false
      // 在组件移除后，重新渲染组件
      // this.$nextTick可实现在DOM 状态更新后，执行传入的方法。
      this.$nextTick(() => {
        this.refreshTable = true
      })
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
  .tableBox{
    width: 80%;
    height: 400px;
    /*background-color: #7EA9D3;*/
    position: relative;
    margin: 0px auto 0px;
    overflow-x: hidden;
  }
  .el-scrollbar >>>.el-scrollbar__wrap {
    overflow-x: hidden;
  }
</style>
