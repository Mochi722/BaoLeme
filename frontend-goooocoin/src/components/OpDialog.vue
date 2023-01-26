<template>
  <el-dialog
    :visible.sync="show"
    width="450px"
    center
    :before-close="handleOpClose"
  >
    <el-header>
      <el-steps :active="active" finish-status="success" simple align-center>
        <el-step title="步骤 1" ></el-step>
        <el-step title="步骤 2" ></el-step>
      </el-steps>
    </el-header>
    <!--      以下为步骤一:选择操作类型-->
    <el-form id="OpChoose" v-if="stepShow == 'Op'" ref="form" label-width="80px" style="height: 100px">
      <el-main>
        <el-select v-model="OpValue"  placeholder="请选择操作" :popper-append-to-body="false">
          <el-option
            v-for="item in options"
            :key="item.value"
            :lable="item.label"
            :value="item.value">
          </el-option>
        </el-select>
      </el-main>
      <el-foot style="position: absolute; right: 20px; bottom: 20px">
        <el-button type="text" size="medium" icon="el-icon-check" round @click="OpCh">下一步</el-button>
      </el-foot>
    </el-form>
    <!--      以下为分类的步骤二-->
    <!--      第一种情况为添加 1. 关系+实体（分为子节点/父节点） 2. 关系-->
    <el-form v-if="stepShow == 'Add'" ref="form" label-width="80px" style="height: 250px">
      <el-container>
        <el-aside width="120px">
          <!--            以下为侧边导航栏-->
          <el-menu default-active="1">
            <el-menu-item index="1" @click="addUpEn">新增上级实体</el-menu-item>
            <el-menu-item index="2" @click="addDownEn">新增下级实体</el-menu-item>
            <el-menu-item index="3" @click="addRelation">已有实体</el-menu-item>
          </el-menu>
        </el-aside>
        <el-main>
          <!--            以下为添加父节点-->
          <div v-if="addShow == 'AddUp'">
            请输入父实体名称：<br/><br/>
            <el-input
              placeholder=""
              v-model="UpEnTemp"
              clearable
            >
            </el-input><br/><br/>
            请输入关系名称：<br/><br/>
            <el-input
              placeholder="请输入内容"
              v-model="RelationNameTemp"
              clearable
            >
            </el-input>
          </div>
          <!--            以下为添加子节点-->
          <div v-if="addShow == 'AddDown'">
            请输入子实体名称：<br/><br/>
            <el-input
              placeholder="请输入内容"
              v-model="DownEnTemp"
              clearable
            >
            </el-input><br/><br/>
            请输入关系名称：<br/><br/>
            <el-input
              placeholder="请输入内容"
              v-model="RelationNameTemp"
              clearable
            >
            </el-input>
          </div>
          <!--            以下为编辑关系属性-->
          <div v-if="addShow == 'AddRelation'">
            <!--              请选择父节点和子节点<br/><br/>-->
            <!--              <el-cascader-->
            <!--                v-model="cascaderChoice"-->
            <!--                :options="EntitySelect"></el-cascader>-->
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
        </el-main>
        <el-foot style="position: absolute; right: 20px; bottom: 20px">
          <el-button type="text" size="medium" icon="el-icon-d-arrow-left" round @click="minusStep">上一步</el-button>
          <el-button type="text" size="medium" icon="el-icon-check" round @click="buttonFinish">完成</el-button>
        </el-foot>
      </el-container>
    </el-form>
    <!--      以下为实体属性的编辑-->
    <el-form v-if="stepShow == 'EditEntity'" ref="form" label-width="80px" style="height: 250px">
      <el-main>
        请输入实体新名称：<br/><br/>
        <el-input
          placeholder="请输入内容"
          v-model="EntityNameTemp"
          clearable
        >
        </el-input>
      </el-main>
      <el-foot style="position: absolute; right: 20px; bottom: 20px">
        <el-button type="text" size="medium" icon="el-icon-d-arrow-left" round @click="minusStep">上一步</el-button>
        <el-button type="text" size="medium" icon="el-icon-check" round @click="buttonFinish">完成</el-button>
      </el-foot>
    </el-form>
  </el-dialog>
</template>

<script>
import {mapActions, mapGetters, mapMutations} from 'vuex'
export default {
  name: 'OpDialog',
  data () {
    return {
      OpValue: '',
      options: [
        // {label: 1, value: '展开'},
        {label: 2, value: '添加关系'},
        {label: 3, value: '编辑属性'},
        {label: 4, value: '删除实体'}
      ],
      addShow: '',
      UpEnTemp: '',
      DownEnTemp: '',
      RelationNameTemp: '',
      EntityListTemp: '',
      EntityNameTemp: ''
    }
  },
  props: ['active', 'arrowDir', 'currentEntityID'],
  computed: {
    ...mapGetters([
      'graphJSON',
      'show',
      'stepShow',
      'EntityList',
      'RelationName',
      'UpEn',
      'DownEn',
      'EntityName',
      'OpDialogFlag'
    ])
  },
  methods: {
    ...mapMutations([
      'set_show',
      'set_stepShow',
      'set_UpEn',
      'set_DownEn',
      'set_RelationName',
      'set_EntityName',
      'set_nodeData',
      'set_linkData',
      'set_FormShow',
      'set_MenuChoice',
      'set_SelectedType'
    ]),
    ...mapActions([
      'getGraphJSON',
      'deleteEntity'
    ]),
    Op_Init () {
      this.OpValue = ''
      this.addShow = ''
      this.UpEnTemp = ''
      this.DownEnTemp = ''
      this.RelationNameTemp = ''
      this.EntityListTemp = ''
      this.EntityNameTemp = ''
    },
    handleOpClose () {
      this.$parent.InitInput()
      this.Op_Init()
      this.set_stepShow('')
      this.set_show(false)
    },
    OpCh () {
      // todo 迭代二内容勿删
      // if (this.OpValue === '展开') {
      //   this.$alert('研究中...', '标题名称', {
      //     confirmButtonText: '确定',
      //     callback: action => {
      //       this.$message({
      //         type: 'info',
      //         message: `action: ${action}`
      //       })
      //       this.show = !this.show
      //       this.InitInput()
      //     }
      //   })
      // }
      if (this.OpValue === '') {
        this.$message({
          message: '请选择一种操作！',
          type: 'warning'
        })
      } else {
        if (this.OpValue === '添加关系') {
          this.set_stepShow('Add')
          this.addShow = 'AddUp'
        }
        if (this.OpValue === '编辑属性') {
          this.$parent.refresh()
          this.set_show(!this.show)
          this.set_MenuChoice(1)
          this.set_FormShow(true)
          this.set_SelectedType('entity')
          this.$parent.changeSize()
        }
        if (this.OpValue === '删除实体') {
          this.$confirm('此操作将永久删除该实体, 是否继续?', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }).then(async () => {
            this.$message({
              type: 'success',
              message: '删除成功!'
            })
            var res = {'id': this.currentEntityID}
            console.log('删除' + res + '实体')
            await this.deleteEntity(res)
            await this.getGraphJSON()
            this.set_nodeData(this.graphJSON.entities)
            this.set_linkData(this.graphJSON.relations)
            this.$parent.processLinkData()
            this.$parent.Init()
            this.set_show(!this.show)
            this.$parent.InitInput()
            this.Op_Init()
          }).catch(() => {
            this.$message({
              type: 'info',
              message: '已取消删除'
            })
            this.set_show(!this.show)
            this.$parent.InitInput()
            this.Op_Init()
          })
        }
        this.$parent.addStep()
      }
    },
    addUpEn () {
      this.addShow = 'AddUp'
    },
    addDownEn () {
      this.addShow = 'AddDown'
    },
    addRelation () {
      this.addShow = 'AddRelation'
    },
    buttonFinish () {
      this.set_UpEn(this.UpEnTemp)
      this.set_DownEn(this.DownEnTemp)
      this.set_RelationName(this.RelationNameTemp)
      this.set_EntityName(this.EntityNameTemp)
      if (this.stepShow === 'Add') {
        if (this.addShow === 'AddUp') {
          this.$parent.AddUpFinish()
        } else if (this.addShow === 'AddDown') {
          this.$parent.AddDownFinish()
        } else {
          this.$parent.AddRelationFinish(this.arrowDir)
        }
      }
      if (this.stepShow === 'EditEntity') {
        this.$parent.EditEntityFinish()
      }
      console.log(this.OpDialogFlag)
      if (this.OpDialogFlag) {
        this.handleOpClose()
      }
    },
    minusStep () {
      this.$parent.InitInput()
      this.Op_Init()
      this.set_stepShow('Op')
    }
  }
}
</script>

<style scoped>
</style>
