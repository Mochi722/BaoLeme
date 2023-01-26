<template>
  <el-dialog
    :visible.sync="show"
    width="450px"
    center
    :before-close="handleOpClose"
    title="添加关系"
  >
    <div v-if="addShow === 'AddRelation'">
    节点类型&nbsp;&nbsp;
    <el-select v-model="arrowDir" style="width: 50%">
      <el-option value="from" label="父节点">
      </el-option>
      <el-option value="to" label="子节点">
      </el-option>
    </el-select>
    <br/><br/>
    <div v-show="arrowDir === 'from'">
      父节点&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
      <el-select v-model="UpEnTemp" style="width: 50%">
        <el-option
          v-for="item in EntityList"
          :key="item.value"
          :label="item.label"
          :value="item.value"
        >
        </el-option>
      </el-select>
      <br/><br/>
    </div>
    <div v-show="arrowDir === 'to'">
      子节点&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
      <el-select v-model="DownEnTemp" style="width: 50%">
        <el-option
          v-for="item in EntityList"
          :key="item.value"
          :label="item.label"
          :value="item.value"
        >
        </el-option>
      </el-select>
      <br/><br/>
    </div>
    关系名称&nbsp;&nbsp;
    <el-input
      style="width: 50%"
      placeholder="请输入内容"
      v-model="RelationNameTemp"
      clearable
    >
    </el-input>
  </div>
  <span slot="footer" class="dialog-footer">
    <el-button @click="cancel">取 消</el-button>
    <el-button type="primary" @click="submitAddRelation">确 定</el-button>
  </span>
  </el-dialog>
</template>

<script>
import {mapActions, mapGetters, mapMutations} from 'vuex'
export default {
  name: 'sideOpDialog',
  data () {
    return {
      addShow: 'AddRelation',
      RelationNameTemp: '',
      DownEnTemp: '',
      UpEnTemp: ''
    }
  },
  props: ['arrowDir'],
  computed: {
    ...mapGetters([
      'EntityList',
      'show'
    ])
  },
  methods: {
    ...mapMutations([
      'set_UpEn',
      'set_DownEn',
      'set_RelationName',
      'set_show'
    ]),
    ...mapActions([]),
    handleOpClose () {
      // this.$parent.InitInput()
      // this.Op_Init()
      // this.set_stepShow('')
      this.set_show(false)
    },
    cancel () {
      this.set_show(false)
    },
    submitAddRelation () {
      this.set_UpEn(this.UpEnTemp)
      this.set_DownEn(this.DownEnTemp)
      this.set_RelationName(this.RelationNameTemp)
      console.log(this.arrowDir)
      this.$parent.AddRelationFinish(this.arrowDir)
      this.set_show(false)
    }
  }
}
</script>

<style scoped>

</style>
