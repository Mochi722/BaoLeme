<template>
  <el-dialog
    :visible.sync="saveConfirm"
    width="30%"
    center
    :before-close="handleOpClose"
    title="确认保存"
  >
    hia
  </el-dialog>
</template>

import {mapActions, mapGetters, mapMutations} from 'vuex'
<script>
import {mapActions, mapGetters, mapMutations} from 'vuex'

export default {
  name: 'saveConfirmDialog',
  data () {
    return {
    }
  },
  computed: {
    ...mapGetters([
      'saveConfirm'
    ])
  },
  methods: {
    ...mapMutations([]),
    ...mapActions([])
  }

}
</script>

<style scoped>

</style>
