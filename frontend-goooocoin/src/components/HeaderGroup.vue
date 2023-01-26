<template>
  <div>
    <el-row :gutter="20" style="margin-top: 40px">
      <el-col :span="10">
        <img src="../assets/LOGO.jpg" width="63px" height="56px" style="float: left;padding:0 0 10px 40px">
        <b class="title"
           style="padding-top:10px;float: left;font-size: 30px;color: #488cd1;font-family: 'Comic Sans MS'">GooooCOIN -
          Knowledge Graph</b>
      </el-col>
<!--      <el-col :span="14">-->
<!--        <el-tooltip class="buttonGroupButton" effect="dark" content="保存" placement="top">-->
<!--          <el-button icon="el-icon-circle-check" style="float:right;" circle @click="SaveLocation"></el-button>-->
<!--        </el-tooltip>-->
<!--        <el-tooltip id="filterTip" class="buttonGroupButton" effect="dark" content="屏蔽文字" placement="top">-->
<!--          <el-button id="filterButton" icon="el-icon-remove-outline" style="float:right;" circle @click="Filter"></el-button>-->
<!--        </el-tooltip>-->
<!--        <el-tooltip class="buttonGroupButton" effect="dark" content="导出xml" placement="top">-->
<!--          <el-button icon="el-icon-document-copy" style="float:right;" circle @click="OutputXmlButtonClick"></el-button>-->
<!--        </el-tooltip>-->
<!--        <el-tooltip class="buttonGroupButton" effect="dark" content="导出图片" placement="top">-->
<!--          <el-button icon="el-icon-picture-outline-round" style="float:right;" circle @click="OutputButtonClick"></el-button>-->
<!--        </el-tooltip>-->
<!--        <el-tooltip class="buttonGroupButton" effect="dark" content="上传" placement="top">-->
<!--          <el-button icon="el-icon-upload" style="float:right;" circle @click="UploadButtonClick"></el-button>-->
<!--        </el-tooltip>-->
<!--        <el-tooltip class="buttonGroupButton" effect="dark" content="我的图谱" placement="top">-->
<!--          <el-button icon="el-icon-view" style="float:right;" circle @click="OpenExistGraph"></el-button>-->
<!--        </el-tooltip>-->

<!--&lt;!&ndash;        <el-button-group class="buttonGroup">&ndash;&gt;-->
<!--&lt;!&ndash;          <el-button class="buttonGroupButton" icon="el-icon-view" @click="OpenExistGraph">我的图谱</el-button>&ndash;&gt;-->
<!--&lt;!&ndash;          <el-button class="buttonGroupButton" v-bind:disabled="imageShow" icon="el-icon-plus" @click="AddButtonClick">添加</el-button>&ndash;&gt;-->
<!--&lt;!&ndash;          <el-button class="buttonGroupButton" icon="el-icon-upload" @click="UploadButtonClick">上传</el-button>&ndash;&gt;-->
<!--&lt;!&ndash;          <el-dropdown>&ndash;&gt;-->
<!--&lt;!&ndash;            <el-button class="buttonGroupButton" type="primary" icon="el-icon-picture-download">&ndash;&gt;-->
<!--&lt;!&ndash;              导出&ndash;&gt;-->
<!--&lt;!&ndash;            </el-button>&ndash;&gt;-->
<!--&lt;!&ndash;            <el-dropdown-menu slot="dropdown">&ndash;&gt;-->
<!--&lt;!&ndash;              <el-dropdown-item>黄金糕</el-dropdown-item>&ndash;&gt;-->
<!--&lt;!&ndash;              <el-dropdown-item>狮子头</el-dropdown-item>&ndash;&gt;-->
<!--&lt;!&ndash;            </el-dropdown-menu>&ndash;&gt;-->
<!--&lt;!&ndash;          </el-dropdown>&ndash;&gt;-->
<!--&lt;!&ndash;          <el-button class="buttonGroupButton" icon="el-icon-picture-outline-round" v-bind:disabled="imageShow" @click="OutputButtonClick">导出图片</el-button>&ndash;&gt;-->
<!--&lt;!&ndash;          <el-button class="buttonGroupButton" icon="el-icon-document-copy" v-bind:disabled="imageShow" @click="OutputXmlButtonClick">导出xml</el-button>&ndash;&gt;-->
<!--&lt;!&ndash;          <el-button class="buttonGroupButton" icon="el-icon-document-copy" v-bind:disabled="imageShow" @click="SaveLocation">保存</el-button>&ndash;&gt;-->
<!--&lt;!&ndash;          <el-dropdown type="primary" split-button @click="Filter">&ndash;&gt;-->
<!--&lt;!&ndash;            过滤&ndash;&gt;-->
<!--&lt;!&ndash;            <el-dropdown-menu slot="dropdown">&ndash;&gt;-->
<!--&lt;!&ndash;              <el-dropdown-item>&ndash;&gt;-->
<!--&lt;!&ndash;                <el-checkbox v-model="r_text_ckd">关系标签</el-checkbox>&ndash;&gt;-->
<!--&lt;!&ndash;              </el-dropdown-item>&ndash;&gt;-->
<!--&lt;!&ndash;              <el-dropdown-item>&ndash;&gt;-->
<!--&lt;!&ndash;                <el-checkbox v-model="checked2">label2</el-checkbox>&ndash;&gt;-->
<!--&lt;!&ndash;              </el-dropdown-item>&ndash;&gt;-->
<!--&lt;!&ndash;            </el-dropdown-menu>&ndash;&gt;-->
<!--&lt;!&ndash;          </el-dropdown>&ndash;&gt;-->
<!--&lt;!&ndash;        </el-button-group>&ndash;&gt;-->
<!--      </el-col>-->
    </el-row>
    <el-dialog
      title="请选择所需要的上传文件"
      :visible.sync="UploadVisible"
      width="50%"
      :before-close="handleUploadClose">
      <div style="width: 100%">
            <span class="file-box">
                <input type="file"
                       style="background-color: black;overflow: hidden;opacity: 0;position: absolute;padding: 8px 0;width: 98px"
                       accept=".csv,.xls,.xlsx"
                       class="upload-file"
                       @change="Excel($event)"/>
              <el-button style="position: center">上传文件</el-button>
            </span>
      </div>
    </el-dialog>
    <el-dialog
      title="请选择操作"
      :visible.sync="AddOpVisible"
      :before-close="handleAddClose">
      <el-container style="height: auto">
        <el-aside style="width: 120px">
          <el-menu default-active="1" style="height: auto">
            <el-menu-item index="1" @click="AddOpChoice = '1'">添加节点</el-menu-item>
            <el-menu-item index="2" @click="AddOpChoice = '2'">添加关系</el-menu-item>
          </el-menu>
        </el-aside>
        <el-main v-if="AddOpChoice === '1'">
          实体名称：
          <el-input
            placeholder="请输入内容"
            v-model="EntityName"
            style="width: 60%"
            clearable
          ></el-input>
          <br/><br/>
          <el-form :model="PropertiesForm" style="padding:0 15%;">
            <el-divider style="padding: 20px">
              添加实体属性
              <i class="el-icon-circle-plus" style="color: #488cd1;width: 10px" @click="addProperty(1)"></i>
            </el-divider>

            <el-form-item
              v-for="property in PropertiesForm.EntityProperties"
              :key="property.key"
            >
              <el-row style="width: 100%">
                <el-input v-model="property.name" style="width: 40%;float: left">
                  <template slot="prepend">属性名</template>
                </el-input>
                <el-input v-model="property.value" style="width: 50%;float: left">
                  <template slot="prepend">属性值</template>
                </el-input>
                <i class="el-icon-close" @click.prevent="removeProperty(1,property)"></i>
              </el-row>
            </el-form-item>
            <br/>
          <el-divider>
            添加实体标签
          </el-divider>
          <el-tag
            :key="tag"
            v-for="tag in EntityLabels"
            closable
            :disable-transitions="false"
            @close="handleClose(tag)">
            {{tag}}
          </el-tag>
          <el-input
            style="width: 60%"
            v-if="LabelInputVisible"
            v-model="LabelInputValue"
            ref="saveLabelInput"
            size="small"
            @keyup.enter.native="handleInputConfirm"
            @blur="handleInputConfirm"
          >
          </el-input>
          <el-button v-else class="button-new-tag" size="small" @click="showInput">+ New Tag</el-button>
          </el-form>
        </el-main>
        <el-main v-if="AddOpChoice === '2'">
          两端节点：
          <el-cascader
            v-model="cascaderChoice"
            style="width: 60%"
            :options="EntitySelect"></el-cascader>
          <br/><br/>
          关系名称：
          <el-input
            placeholder="请输入内容"
            v-model="RelationName"
            style="width: 60%"
            clearable
          >
          </el-input>
          <el-form :model="PropertiesForm" v-if="AddOpChoice === '2'" style="padding:0 20%;">
            <el-divider >
              添加关系属性
              <i class="el-icon-circle-plus" style="color: #488cd1;width: 10px" @click="addProperty(2)"></i>
            </el-divider>
            <el-form-item
              v-for="property in PropertiesForm.RelationProperties"
              :key="property.key"
            >
              <el-row style="width: 100%">
                <el-input v-model="property.name" style="width: 40%;float: left">
                  <template slot="prepend">属性名</template>
                </el-input>
                <el-input v-model="property.value" style="width: 50%;float: left">
                  <template slot="prepend">属性值</template>
                </el-input>
                <i class="el-icon-close" @click.prevent="removeProperty(2,property)"></i>
              </el-row>
            </el-form-item>
          </el-form>
        </el-main>
      </el-container>
      <el-button type="text" size="medium" icon="el-icon-check" round @click="AddFinish"
                 style="position: absolute; right: 5px; bottom: 5px">完成
      </el-button>
    </el-dialog>
  </div>
</template>

<script>
import XLSX from 'xlsx'
import {mapActions, mapGetters, mapMutations} from 'vuex'
import {Message} from 'element-ui'

const xml2js = require('xml2js')

export default {
  name: 'HeaderGroup',
  data () {
    return {
      UploadVisible: false,
      AddOpVisible: false,
      AddOpChoice: '1',
      cascaderChoice: [],
      EntitySelect: [],
      RelationName: '',
      EntityName: '',
      EntityLabels: [],
      LabelInputVisible: false,
      LabelInputValue: '',
      PropertiesForm: {
        EntityProperties: [],
        RelationProperties: []
      },
      r_text_ckd: true,
      checked2: false
    }
  },
  computed: {
    ...mapGetters([
      'graphJSON',
      'imageShow',
      'nodeData',
      'linkData',
      'relationTextVisible'
    ])
  },
  mounted () {
  },
  methods: {
    ...mapMutations([
      'set_imageShow',
      'set_GraphJSON',
      'set_nodeData',
      'set_linkData',
      'set_RelationTextVisible'
    ]),
    ...mapActions([
      'uploadGraphJSON',
      'getGraphJSON',
      'addEntity',
      'addRelationapi',
      'uploadLocation'
    ]),
    Header_Init () {
      this.AddOpVisible = false
      this.cascaderChoice = []
    },
    AddButtonClick () {
      this.AddOpVisible = true
      this.InitEntitySelectList()
      this.AddOpChoice = '1'
    },
    UploadButtonClick () {
      this.UploadVisible = true
    },
    OutputButtonClick () {
      this.$parent.outputGraph()
    },
    OutputXmlButtonClick () {
      var builder = new xml2js.Builder()
      let that = this
      var AllData = Object.assign(that.graphJSON.entities, that.graphJSON.relations)
      console.log(that.graphJSON.entities)
      var xmlData = builder.buildObject(AllData)
      this.doSave(xmlData, 'text/latex', 'data.xml')
    },
    handleUploadClose () {
      this.UploadVisible = false
    },
    removeProperty (v, item) {
      let list = v === 1 ? this.PropertiesForm.EntityProperties : this.PropertiesForm.RelationProperties
      var index = list.indexOf(item)
      if (index !== -1) {
        list.splice(index, 1)
      }
    },
    addProperty (v) {
      let list = v === 1 ? this.PropertiesForm.EntityProperties : this.PropertiesForm.RelationProperties
      list.push({
        name: '',
        value: '',
        key: Date.now()
      })
      console.log(this.PropertiesForm)
    },
    handleClose (tag) {
      this.EntityLabels.splice(this.EntityLabels.indexOf(tag), 1)
    },
    showInput () {
      this.LabelInputVisible = true
      this.$nextTick(_ => {
        this.$refs.saveTagInput.$refs.input.focus()
      })
    },
    handleInputConfirm () {
      let v = this.LabelInputValue
      if (v) {
        this.EntityLabels.push(v)
      }
      this.LabelInputVisible = false
      this.LabelInputValue = ''
    },
    Excel (e) {
      let that = this
      // 错误情况判断
      const files = e.target.files
      if (files.length <= 0) {
        return false
      } else if (!/\.(xls|xlsx|csv)$/.test(files[0].name.toLowerCase())) {
        this.$message({
          message: '上传格式不正确，请上传csv或xls或xlsx格式',
          type: 'warning'
        })
        return false
      } else {
        that.upload_file = files[0].name
      }
      // 读取表格
      const fileReader = new FileReader()
      fileReader.onload = async ev => {
        try {
          const data = ev.target.result
          const workbook = XLSX.read(data, {
            type: 'binary'
          })
          // 读取sheet1
          const wsname1 = workbook.SheetNames[0]
          // 读取sheet2
          const wsname2 = workbook.SheetNames[1]
          var nodes = XLSX.utils.sheet_to_json(workbook.Sheets[wsname1])
          var relations = XLSX.utils.sheet_to_json(workbook.Sheets[wsname2])
          var newnodes = []
          for (var i = 0; i < nodes.length; i++) {
            var node = {}
            var props = {}
            var relaprops = {}
            relaprops.__D3_PROPS__textsize = 15
            relaprops.__D3_PROPS__lineStyle = '实线'
            relaprops.__D3_PROPS__weight = 2.5
            relaprops.__D3_PROPS__color = '#ccc'
            props.name = nodes[i].name
            props.__D3_PROPS__shape = '圆形'
            props.__D3_PROPS__size = 40
            props.__D3_PROPS__color = this.$parent.circleColor()
            props.__D3_PROPS__textsize = 15
            node.properties = props
            node.labels = []
            node.labels.push(nodes[i].label)
            newnodes.push(node)
            relations[i].properties = relaprops
          }
          // console.log('********relations********')
          // console.log(relations)
          // console.log(relations[0]['source'])
          const graphJSON = {}
          graphJSON.nodes = newnodes
          graphJSON.links = relations
          console.log('before import:')
          console.log(newnodes)
          console.log(relations)
          var jsonstring = JSON.stringify(graphJSON)
          console.log(jsonstring)
          await that.uploadGraphJSON(jsonstring)
          await that.getGraphJSON()
          console.log('get graph success')
          console.log('[[[[')
          console.log(that.graphJSON.entities)
          console.log('[[[[')
          that.set_nodeData(that.graphJSON.entities)
          that.set_linkData(that.graphJSON.relations)
          // 处理link根据id定向
          that.$parent.processLinkData()
          // await that.$parent.getGraphAsync()
          console.log('after import:')
          console.log(that.nodeData)
          console.log(that.linkData)
          this.$parent.Init()
          this.$parent.InitEntityList()
          that.UploadVisible = false
          this.set_imageShow(false)
          // const graph = Object.assign({}, nodes, relations)
          // console.log(graph)
        } catch (e) {
          return false
        }
      }
      fileReader.readAsBinaryString(files[0])
    },
    handleAddClose () {
      this.$parent.InitInput()
      this.Header_Init()
    },
    async AddFinish () {
      var res = ''
      if (this.AddOpChoice === '1') {
        if (this.EntityName === '') {
          this.$message({
            message: '请填写实体名称！',
            type: 'warning'
          })
          return
        }
        if (this.$parent.findEntity(this.EntityName)) {
          this.$message({
            message: '不能填写已有的实体名称！',
            type: 'warning'
          })
          return
        }
        res = {'name': this.EntityName}
        await this.addEntity(this.EntityName)
        console.log(res)
        console.log(this.nodeData)
        console.log(this.linkData)
        // this.nodeData.push({age: 0, id: this.idReturned, job: '', name: this.EntityName})
        await this.$parent.getGraphAsync()
        // this.nodeData = this.graphJSON.entities
        // this.linkData = this.graphJSON.relations
        // this.processLinkData()
        console.log('++++++++++')
        console.log(this.nodeData)
        console.log(this.linkData)
        this.$parent.Init()
        this.handleAddClose()
      } else {
        if (this.cascaderChoice.length < 2) {
          this.$message({
            message: '请选择两端节点！',
            type: 'warning'
          })
          return
        }
        if (this.RelationName === '') {
          this.$message({
            message: '请填写关系名称！',
            type: 'warning'
          })
          return
        }
        res = {'source': this.$parent.getID(this.cascaderChoice[0]), 'target': this.$parent.getID(this.cascaderChoice[1]), 'name': this.RelationName}
        console.log(res)
        await this.addRelationapi(res)
        await this.$parent.getGraphAsync()
        this.$parent.Init()
        this.handleAddClose()
      }
    },
    doSave (value, type, name) {
      var blob
      if (typeof window.Blob === 'function') {
        blob = new Blob([value], {type: type})
      } else {
        var BlobBuilder = window.BlobBuilder || window.MozBlobBuilder || window.WebKitBlobBuilder || window.MSBlobBuilder
        var bb = new BlobBuilder()
        bb.append(value)
        blob = bb.getBlob(type)
      }
      var URL = window.URL || window.webkitURL
      var bloburl = URL.createObjectURL(blob)
      var anchor = document.createElement('a')
      if ('download' in anchor) {
        anchor.style.visibility = 'hidden'
        anchor.href = bloburl
        anchor.download = name
        document.body.appendChild(anchor)
        var evt = document.createEvent('MouseEvents')
        evt.initEvent('click', true, true)
        anchor.dispatchEvent(evt)
        document.body.removeChild(anchor)
      } else if (navigator.msSaveBlob) {
        navigator.msSaveBlob(blob, name)
      } else {
        location.href = bloburl
      }
    },
    InitEntitySelectList () {
      var entityList = []
      for (var i = 0; i < this.nodeData.length; i++) {
        entityList.push({
          value: this.nodeData[i].name,
          label: this.nodeData[i].name
        })
      }
      var tempList
      for (i = 0; i < entityList.length; i++) {
        tempList = entityList.concat()
        tempList.splice(i, 1)
        this.EntitySelect.push({
          value: entityList[i].label,
          label: entityList[i].label,
          children: tempList
        })
      }
    },
    async OpenExistGraph () {
      await this.$parent.getGraphAsync()
      this.$parent.Init()
      this.$parent.InitEntityList()
      this.UploadVisible = false
      this.set_imageShow(false)
    },
    SaveLocation () {
      Message.success('保存成功！')
      this.uploadLocation()
    },
    Filter () {
      console.log(this.r_text_ckd)
      this.r_text_ckd = !this.r_text_ckd
      this.set_RelationTextVisible(this.r_text_ckd ? 'visible' : 'hidden')
      console.log(window.document)
      this.$parent.Init()
    }
  }
}
</script>

<style scoped>
.buttonGroup {
  float: left;
  margin-right: 20px;
}
.buttonGroupButton {
  float: left;
  margin-right: 20px;
  font-size: 18px;
  background-color: #E4EFF9;
}
</style>
