<template>
    <el-dialog
      :visible.sync="editPropsDialogShow"
      width="60%"
      center
      :before-close="handleOpClose"
      title="实体属性管理"
    >
      <el-table
        v-if="refreshTBL"
        :data="tableData"
        style="width: 100%;">
        <el-table-column
          prop="propName"
          label="属性名"
          width="100"
        >
          <template slot-scope="scope">
<!--            <el-input v-if="scope.row.edit" v-model="scope.row.propName" placeholder="属性名"></el-input>-->
            <span>{{scope.row.propName}}</span>
          </template>
        </el-table-column>
        <el-table-column
          prop="propValue"
          label="属性值"
        >
          <template slot-scope="scope">
            <el-input v-if="scope.row.edit" v-model="scope.row.propValue" placeholder="属性值"></el-input>
            <span v-else>{{scope.row.propValue}}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="100">
          <template slot-scope="scope">
            <el-button v-if="scope.row.edit" type="text" size="medium" @click="confirmAdd(scope.row)">
              <i class="el-icon-check" aria-hidden="true"></i>
            </el-button>
            <div v-else>
              <el-button type="text" size="medium" @click="editData(scope.row)">
                <i class="el-icon-edit" aria-hidden="true"></i>
              </el-button>
<!--              <el-button type="text" size="medium" @click="deleteData(scope.row,scope.$index)">-->
<!--                <i class="el-icon-delete" aria-hidden="true"></i>-->
<!--              </el-button>-->
            </div>
          </template>
        </el-table-column>
      </el-table>
    </el-dialog>
</template>

<script>
import {mapActions, mapGetters, mapMutations} from 'vuex'
export default {
  name: 'editPropsDialog',
  data () {
    return {
      refreshTBL: true,
      tableData: [],
      edited: false
    }
  },
  mounted () {
    this.setForm()
  },
  computed: {
    ...mapGetters([
      'selectedType',
      'currentEntityID',
      'currentRelationID',
      'nodeData',
      'linkData',
      'editPropsDialogShow'
    ])
  },
  methods: {
    ...mapMutations([
      'set_SelectedType',
      'set_nodeData',
      'set_linkData',
      'set_show',
      'set_editPropsDialogShow',
      'set_saveConfirm'
    ]),
    ...mapActions([
      'updateEntityPropsById',
      'updateRelationPropsById',
      'updateRelationName',
      'deleteEntity',
      'removeRelation',
      'reverseRelationDir'
    ]),
    // 确认添加
    confirmAdd (row) {
      console.log(row)
      this.edited = true
      row.edit = false
      console.log(this.tableData)
      this.refreshTBL = false
      this.$nextTick(() => {
        this.refreshTBL = true
      })
    },
    // 修改
    editData (row) {
      console.log(row)
      row.edit = true
      console.log(row)
      this.refreshTBL = false
      this.$nextTick(() => {
        this.refreshTBL = true
      })
    },
    // 删除
    deleteData (row, index) {
      this.tableData.splice(index, 1)
      console.log(this.tableData)
    },
    submitData () {
      alert(JSON.stringify(this.tableData))
    },
    setForm () {
      console.log('~!~!~!~!~!~!')
      var propList = []
      if (this.selectedType === 'entity') {
        var nodeList = this.nodeData
        console.log('id:' + this.currentEntityID)
        for (var i = 0; i < nodeList.length; i++) {
          if (nodeList[i].id === this.currentEntityID) {
            propList = nodeList[i]
          }
        }
        this.e_shape = propList.shape
        this.e_size = propList.size
        this.e_color = propList.color
        this.e_text_size = propList.textsize
        this.currentID = this.currentEntityID
        this.currentLable = propList.labels[0]
        this.newEntityName = propList.name
        // 设置tableData
        console.log('tabledata')
        var tbdataList = propList.properties
        console.log(tbdataList)
        for (var it in tbdataList) {
          var item = {}
          console.log(it)
          item.propName = it
          item.propValue = tbdataList[it]
          console.log(item)
          this.tableData.push(item)
        }
      } else {
        var linkList = this.linkData
        console.log('id:' + this.currentRelationID)
        for (var k = 0; k < linkList.length; k++) {
          if (linkList[k].id === this.currentRelationID) {
            propList = linkList[k]
          }
        }
        console.log(propList)
        this.r_color = propList.d3_properties.__D3_PROPS__color
        this.lineStyle = propList.d3_properties.__D3_PROPS__lineStyle
        this.r_text_size = propList.d3_properties.__D3_PROPS__textsize
        this.r_weight = propList.d3_properties.__D3_PROPS__weight
        this.currentID = propList.id
        this.newRelationName = propList.name
        // this.r_text_size = propList.
      }
    },
    handleOpClose (done) {
      if (this.edited) {
        console.log('改了！保存了吗！')
        // this.set_saveConfirm(true)
        // this.$confirm('确认关闭？')
        //   .then(_ => {
        //     done()
        //   })
        //   .catch(_ => {})
      }
      this.set_editPropsDialogShow(false)
      this.edited = false
    }
  }
}
</script>

<style scoped>

</style>
