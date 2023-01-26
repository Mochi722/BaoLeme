<template>
  <div>
    <el-form class="form2">
      <div class="mytitle"><span class="dashed">&nbsp;—— </span>编辑属性<span class="dashed"> ——————</span></div>
      <!--    <div class="dividerFake"></div>-->
      <el-form-item class="item" v-if="selectedType === 'entity'" style="padding-top: 30px; font-size: 50px">
        <div class="mylabel">实体id： {{currentID}}</div>
      </el-form-item>
      <el-form-item v-if="selectedType === 'entity'" >
        <div class="mylabel">实体名称：</div>
        <el-input v-model="newEntityName" placeholder="请输入实体名称" size="small" style="float: left;width: 150px"></el-input>
      </el-form-item>
      <el-form-item class="item" v-if="selectedType === 'entity'">
        <div class="mylabel">实体标签： {{currentLable}}</div>
      </el-form-item>
<!--      <el-form-item class="item" v-if="selectedType === 'entity'">-->
<!--        <div class="mylabel">实体属性：</div>-->
<!--        <el-button type="text" icon="el-icon-plus" @click="addData">添加属性</el-button>-->
<!--      </el-form-item>-->

<!--      <el-form-item class="item" v-if="selectedType === 'relation'" style="padding-top: 30px; font-size: 50px">-->
<!--        <div class="mylabel">关系id： {{currentID}}</div>-->
<!--      </el-form-item>-->
<!--      <el-form-item v-if="selectedType === 'relation'" >-->
<!--        <div class="mylabel">关系名称：</div>-->
<!--        <el-input v-model="newRelationName" placeholder="父亲" size="small" style="float: left;width: 150px"></el-input>-->
<!--      </el-form-item>-->
      <el-button type="primary" size="small" style="float:right;margin-top:-28px;margin-right: 20px;border-radius: 20px" @click="propEdited" v-if="selectedType === 'relation' || selectedType === 'entity'">管理属性</el-button>
<!--      <el-button plain size="small" style="float:right;margin-right: 10px;border-radius: 20px" v-if="selectedType === 'relation' || selectedType === 'entity'">还原</el-button>-->
<!--      <div class="myblank"></div>-->
<!--      <div class="myblank"></div>-->
<!--      &lt;!&ndash;    <div class="dividerFake"></div>&ndash;&gt;-->
    </el-form>
    <el-form class="form1">
      <div class="mytitle"><span class="dashed">&nbsp;—— </span>编辑图元<span class="dashed"> ——————</span></div>
      <!--    <div class="dividerFake"></div>-->
      <el-form-item class="item" v-if="selectedType === 'entity'" style="padding-top: 30px; font-size: 50px">
        <div class="mylabel">图元形状：</div>
        <el-select v-model="e_shape" placeholder="请选择图元形状" size="small" style="float: left;width: 150px">
          <el-option
            v-for="item in shapes"
            :key="item.value"
            :lable="item.value"
            :value="item.label">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item v-if="selectedType === 'entity'" >
        <div class="mylabel">图元颜色 ：</div>
        <el-color-picker v-model="e_color" style="float:left;"></el-color-picker>
      </el-form-item>
      <el-form-item v-if="selectedType === 'entity'" >
        <div class="mylabel" >图元尺寸：</div>
        <el-input-number v-model="e_size" :min="1" :max="100" :step="10" label="描述文字" style="float:left;width: 150px;" size="small"></el-input-number>
      </el-form-item>
      <el-form-item v-if="selectedType === 'entity'" >
        <div class="mylabel">文字大小：</div>
        <el-input-number v-model="e_text_size" :min="1" :max="100" :step="5" label="描述文字" size="small" style="float: left;width: 150px"></el-input-number>
      </el-form-item>

      <el-form-item v-if="selectedType === 'relation'" size="small" style="padding-top: 30px;">
        <div class="mylabel">线条样式：</div>
        <el-select v-model="lineStyle" placeholder="请选择图元形状" size="small" style="float: left;width: 150px">
          <el-option label="实线" value="1"></el-option>
          <el-option label="虚线" value="2"></el-option>
          <el-option label="点线" value="3"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item v-if="selectedType === 'relation'" >
        <div class="mylabel">线条颜色：</div>
        <el-color-picker v-model="r_color" style="float:left;"></el-color-picker>
      </el-form-item>
      <el-form-item v-if="selectedType === 'relation'" >
        <div class="mylabel">线条粗细：</div>
        <el-input-number v-model="r_weight" :min="1" :max="10" :step="1" label="描述文字" size="small" style="float: left;width: 150px"></el-input-number>
      </el-form-item>
      <el-form-item v-if="selectedType === 'relation'" >
        <div class="mylabel">文字大小：</div>
        <el-input-number v-model="r_text_size" :min="1" :max="100" :step="5" label="描述文字" size="small" style="float: left;width: 150px"></el-input-number>
      </el-form-item>
      <el-button type="primary" size="small" style="float:right;margin-right: 20px;border-radius: 20px" @click="entityPropsEdit"  v-if="selectedType === 'relation' || selectedType === 'entity'">应用</el-button>
      <el-tooltip class="item"  effect="dark" content="删除" placement="top"  v-if="selectedType === 'relation' || selectedType === 'entity'">
        <el-button type="danger" size="small" icon="el-icon-delete" style="float:right;margin-right: 15px;margin-left: 0" circle  @click="deleteEntityinForm"></el-button>
      </el-tooltip>
      <el-tooltip class="item" effect="dark" content="换向" placement="top"  v-if="selectedType === 'relation'">
        <el-button type="warning" size="small" icon="el-icon-refresh" style="float:right;margin-right: 15px;margin-left: 0" circle v-if="selectedType === 'relation'" @click="reverseRelation"></el-button>
      </el-tooltip>
      <el-tooltip class="item" effect="dark" content="添加关系到" placement="top"  v-if="selectedType === 'entity'">
        <el-button type="success" size="small" icon="el-icon-plus" style="float:right;margin-right: 15px;" circle v-if="selectedType === 'entity'" @click="addRelation"></el-button>
      </el-tooltip>
      <div class="myblank"></div>
      <div class="myblank"></div>
      <!--    <div class="dividerFake"></div>-->
    </el-form>

<!--    <el-form class="form2" v-if="selectedType === 'entity'">-->
<!--      <div class="mytitle"><span class="dashed">&nbsp;—— </span>模式切换<span class="dashed"> ——————</span></div>-->
<!--      <el-form-item class="item" style="padding-top: 30px; font-size: 50px">-->
<!--        <el-switch-->
<!--          v-model="pbmode"-->
<!--          active-text="排版模式"-->
<!--          inactive-text="力导图模式"-->
<!--          @change="modeChanged"-->
<!--          style="float:left; margin-left: 15px">-->
<!--        </el-switch>-->
<!--      </el-form-item>-->
<!--      <div class="myblank2"></div>-->
<!--    </el-form>-->
    <edit-props-dialog></edit-props-dialog>
  </div>
</template>

<script>
import {mapActions, mapGetters, mapMutations} from 'vuex'
import {MessageBox, Message} from 'element-ui'
import sideOpDialog from './sideOpDialog'
import editPropsDialog from './editPropsDialog'
export default {
  name: 'propertyForm',
  components: {sideOpDialog, editPropsDialog},
  data () {
    return {
      pbmode: false,
      refreshTBL: true,
      pname: 'haha',
      pvalue: '',
      customStyle: 'background: #E4EFF9;border-radius: 20px;border: 0;overflow: hidden',
      shapes: [
        {value: 1, label: '圆形'},
        {value: 2, label: '星形'},
        {value: 3, label: '正方形'},
        {value: 4, label: '菱形'},
        {value: 5, label: '三角形'},
        {value: 6, label: '十字形'}
      ],
      e_shape: '圆形',
      lineStyle: '实线',
      e_color: '',
      r_color: '',
      r_weight: 1,
      r_text_size: 15,
      e_size: 1,
      e_text_size: 18,
      currentID: 0,
      newEntityName: '',
      newRelationName: '',
      currentLable: '',
      tableData: []
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
      'set_editPropsDialogShow'
    ]),
    ...mapActions([
      'updateEntityPropsById',
      'updateRelationPropsById',
      'updateRelationName',
      'deleteEntity',
      'removeRelation',
      'reverseRelationDir'
    ]),
    modeChanged () {
      console.log(this.pbmode)
      if (this.pbmode) this.$emit('changeMode')
      else this.$parent.$parent.$parent.$parent.$parent.$parent.Init()
    },
    addRelation () {
      this.set_show(true)
    },
    async reverseRelation () {
      var res = {'id': this.currentRelationID}
      await this.reverseRelationDir(res)
      this.$parent.$parent.$parent.Init2()
    },
    // 添加
    addData () {
      this.tableData.push({
        edit: true
      })
    },
    async entityPropsEdit () {
      var data = {}
      var newProps = {}
      if (this.selectedType === 'entity') {
        data.id = this.currentEntityID
        newProps.__D3_PROPS__shape = this.e_shape
        newProps.__D3_PROPS__size = this.e_size
        newProps.__D3_PROPS__color = this.e_color
        newProps.__D3_PROPS__textsize = this.e_text_size
        data.properties = newProps
        this.updateEntityPropsById(data)
        console.log(this.$parent.$parent.$parent)
      } else {
        data.id = this.currentRelationID
        newProps.__D3_PROPS__lineStyle = this.lineStyle
        newProps.__D3_PROPS__color = this.r_color
        newProps.__D3_PROPS__weight = this.r_weight
        newProps.__D3_PROPS__textsize = this.r_text_size
        data.properties = newProps
        await this.updateRelationPropsById(data)
        console.log(data)
      }
      this.$parent.$parent.$parent.Init2()
      this.$emit('changeSize')
    },
    propEdited () {
      this.set_editPropsDialogShow(true)
      // var data = {}
      // var props = {}
      // if (this.selectedType === 'entity') {
      //   data.id = this.currentEntityID
      //   props.name = this.newEntityName
      //   // todo: 修改初name和图元属性外的属性
      //   var tableData = this.tableData
      //   console.log('tabledataaaaaaaa')
      //   console.log(tableData)
      //   for (var i = 0; i < tableData.length; i++) {
      //     props[tableData[i].propName] = tableData[i].propValue
      //   }
      //   data.properties = props
      //   data = JSON.stringify(data)
      //   this.updateEntityPropsById(data)
      //   console.log(data)
      // } else {
      //   data.id = this.currentRelationID
      //   data.newName = this.newRelationName
      //   data = JSON.stringify(data)
      //   console.log('data')
      //   console.log(data)
      //   this.updateRelationName(data)
      // }
      // this.$parent.$parent.$parent.Init2()
      // this.$emit('changeSize')
    },
    setForm () {
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
    deleteEntityinForm () {
      var that = this
      if (that.selectedType === 'entity') {
        MessageBox.confirm('此操作将永久删除该实体, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(async () => {
          Message.success('删除成功！')
          var res = {'id': that.currentEntityID}
          console.log(res)
          await that.deleteEntity(res)
          that.$parent.$parent.$parent.Init2()
        }).catch(() => {
          Message.info('已取消删除')
        })
      } else {
        MessageBox.confirm('此操作将永久删除该关系, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(async () => {
          Message.success('删除成功！')
          var res = {'id': that.currentRelationID}
          console.log(res)
          await that.removeRelation(res)
          that.$parent.$parent.$parent.Init2()
        }).catch(() => {
          Message.info('已取消删除')
        })
      }
    }
  }
}
</script>

<style scoped>
  .mylabel{
    text-align: left;
    padding-left: 20px;
    font-size: 14px;
    float: left;
  }
  .mytitle{
    text-align: left;
    padding-left: 10px;
    padding-top: 20px;
    margin-bottom: 20px;
    font-size: 18px;
    float: left;
  }
  .myblank{
    text-align: left;
    padding-left: 10px;
    padding-top: 30px;
    font-size: 20px;
    float: top;
  }
  .myblank2{
    text-align: left;
    padding-left: 10px;
    padding-top: 10px;
    font-size: 20px;
    float: top;
  }
  /*.dividerFake{*/
  /*  height: 1px;*/
  /*  border-top: 2px dashed #D3D3D3;*/
  /*  text-align: center;*/
  /*  margin-top: 15px;*/
  /*}*/
  /*.dividerFake2{*/
  /*  height: 1px;*/
  /*  border-top: 2px solid #D3D3D3;*/
  /*  text-align: center;*/
  /*  margin-top: 35px;*/
  /*}*/
  .dashed{
    color: #D3D3D3;
  }
  .form1{
    background-color: #E4EFF9;
    height: 100%;
    width: 98%;
    border-radius: 10px;
    box-shadow: 0 2px 4px rgba(0, 0, 0, .12), 0 0 6px rgba(0, 0, 0, .04)
  }
  .form2{
    background-color: #E4EFF9;
    height: 100%;
    width: 98%;
    border-radius: 10px;
    margin-top: 10px;
    padding-bottom: 20px;
    margin-bottom: 10px;
    box-shadow: 0 2px 4px rgba(0, 0, 0, .12), 0 0 6px rgba(0, 0, 0, .04);
  }
</style>
