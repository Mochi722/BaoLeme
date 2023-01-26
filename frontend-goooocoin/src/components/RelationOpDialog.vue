<template>
  <div>
    <el-dialog
      title="请选择关系操作"
      :visible.sync="linkEditShow"
      width="30%"
      :before-close="handleLinkEditClose">
      <el-row style="margin-bottom: 20px">
        <el-button type="text" icon="el-icon-refresh" @click="editRelationName">
          更改关系属性
        </el-button>
        <el-button type="text" icon="el-icon-refresh" @click="editDir">
          更改关系方向
        </el-button>
        <el-button type="text" icon="el-icon-refresh" @click="editRelation">
          删除关系
        </el-button>
      </el-row>
    </el-dialog>
  </div>
</template>

<script>
import {mapActions, mapGetters, mapMutations} from 'vuex'
export default {
  name: 'RelationOpDialog',
  data () {
    return {
    }
  },
  computed: {
    ...mapGetters([
      'linkEditShow',
      'UpEn',
      'DownEn',
      'RelationName',
      'currentRelationID'
    ])
  },
  methods: {
    ...mapMutations([
      'set_LinkEditShow',
      'set_FormShow',
      'set_MenuChoice',
      'set_SelectedType'
    ]),
    ...mapActions([
      'updateRelationName',
      'reverseRelationDir',
      'removeRelation'
    ]),
    handleLinkEditClose () {
      this.set_LinkEditShow(!this.linkEditShow)
      this.$parent.InitInput()
    },
    editRelationName () {
      this.$parent.refresh()
      this.set_LinkEditShow(!this.linkEditShow)
      this.set_MenuChoice(1)
      this.set_FormShow(true)
      this.$parent.changeSize()
      this.set_SelectedType('relation')
      // this.$prompt('请输入新的关系名称', '提示', {
      //   confirmButtonText: '确定',
      //   cancelButtonText: '取消',
      //   inputPattern: /[\S]/,
      //   inputErrorMessage: '名称不能为空'
      // }).then(async ({ value }) => {
      //   var res = {'source': this.$parent.getID(this.UpEn), 'target': this.$parent.getID(this.DownEn), 'name': this.RelationName, 'newName': value}
      //   console.log(res)
      //   this.set_LinkEditShow(false)
      //   await this.updateRelationName(res)
      //   await this.$parent.getGraphAsync()
      //   this.$parent.Init()
      //   this.$message({
      //     type: 'success',
      //     message: '更改成功'
      //   })
      // }).catch(() => {
      //   this.$message({
      //     type: 'info',
      //     message: '取消输入'
      //   })
      // })
    },
    editDir () {
      this.$confirm('是否确认更改其方向?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        var res = {'id': this.currentRelationID}
        console.log('}}')
        console.log(this.currentRelationID)
        await this.reverseRelationDir(res)
        await this.$parent.getGraphAsync()
        this.$parent.Init()
        console.log(res)
        this.set_LinkEditShow(false)
        this.$message({
          type: 'success',
          message: '更改成功!'
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消更改'
        })
      })
    },
    editRelation () {
      this.$confirm('是否确认删除该关系?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        await this.$parent.getGraphAsync()
        var res = {'id': this.currentRelationID}
        await this.removeRelation(res)
        await this.$parent.getGraphAsync()
        this.$parent.Init()
        console.log(res)
        this.set_LinkEditShow(false)
        this.$message({
          type: 'success',
          message: '更改成功!'
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消更改'
        })
      })
    }
  }
}
</script>

<style scoped>

</style>
