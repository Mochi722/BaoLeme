<template>
  <el-dialog
    title="修改密码"
    :visible.sync="this.editpswdDialogVisible"
    width="30%"
    :before-close="handleClose"
    style="text-align: left"
  >
    <el-form ref="form" :model="form" label-width="100px">
      <el-form-item label="原密码：">
        <el-input type="password" v-model="password" style="width: 260px"></el-input>
      </el-form-item>
      <el-form-item label="新密码：">
        <el-input type="password" v-model="newPassword" style="width: 260px"></el-input>
      </el-form-item>
      <el-form-item label="确认密码：">
        <el-input type="password" v-model="confirmPassword" style="width: 260px"></el-input>
      </el-form-item>
    </el-form>
    <span slot="footer" class="dialog-footer">
    <el-button @click="dialogClose">取 消</el-button>
    <el-button type="primary" @click="submitEditPswd">确 定</el-button>
  </span>
  </el-dialog>
</template>

<script>
import {mapActions, mapGetters, mapMutations} from 'vuex'
export default {
  name: 'editPasswordDialog',
  data () {
    return {
      visible: false,
      password: '',
      newPassword: '',
      confirmPassword: ''
    }
  },
  mounted () {
    this.visible = this.editpswdDialogVisible
  },
  computed: {
    ...mapGetters([
      'editpswdDialogVisible',
      'currentUserID'
    ])
  },
  methods: {
    ...mapMutations([
      'set_editpswdDialogVisible'
    ]),
    ...mapActions([
      'modifyPassword'
    ]),
    handleClose (done) {
      this.dialogClose()
    },
    dialogClose () {
      this.visible = false
      this.set_editpswdDialogVisible(false)
    },
    async submitEditPswd () {
      if (this.newPassword !== this.confirmPassword) {
        this.$message.error('两次密码不一致！')
      } else {
        var res = await this.modifyPassword({
          id: this.currentUserID,
          password: this.password,
          newPassword: this.newPassword
        })
        console.log('............')
        console.log(res)
        if (res.success) {
          this.$message.success(res.message)
          this.dialogClose()
        } else this.$message.error(res.message)
      }
    }
  }
}
</script>

<style scoped>

</style>
