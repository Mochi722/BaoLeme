<template>
    <div>
      <el-row type="flex">
        <el-col style="width: 80px">
          <el-menu
            mode="vertical"
            class="sideMenu"
            style="height: 100%"
          >
            <el-menu-item class="sideMenuItem" index="1" @click="choiceOne">
              <i class="el-icon-edit"></i>
            </el-menu-item>
            <el-menu-item class="sideMenuItem" index="2" @click="choiceTwo">
              <i class="el-icon-s-data"></i>
            </el-menu-item>
            <el-menu-item class="sideMenuItem" index="3" @click="choiceThree">
              <i class="el-icon-search"></i>
            </el-menu-item>
          </el-menu>
        </el-col>
        <el-col style="width: 260px" v-if="menuChoice === 1 && formShow">
          <property-form ref="prop" v-if="refresh" @changeMode="changeMode" @changeSize="changeSize"></property-form>
        </el-col>
        <el-col style="width: 280px" v-if="menuChoice === 2 && formShow">
          <statistics-form @searchShow="searchShow" @searchRelationShow="searchRelationShow" @searchShowEnd="searchShowEnd"></statistics-form>
        </el-col>
        <el-col style="width: 260px" v-if="menuChoice === 3 && formShow">
          <search-form @searchShow="searchShow" @searchRelationShow="searchRelationShow" @searchShowEnd="searchShowEnd"></search-form>
        </el-col>
      </el-row>
    </div>
</template>

<script>

import PropertyForm from './propertyForm'
import {mapActions, mapGetters, mapMutations} from 'vuex'
import SearchForm from './searchForm'
import StatisticsForm from './statisticsForm'
export default {
  name: 'SideBar',
  components: {StatisticsForm, SearchForm, PropertyForm},
  data () {
    return {
      num: 1,
      selectedType: 'entity',
      shape: '',
      lineStyle: '',
      e_color: '',
      r_color: '',
      r_weight: 1,
      e_size: 1,
      refresh: true
    }
  },
  computed: {
    ...mapGetters([
      'formShow',
      'menuChoice',
      'graphJSON',
      'currentEntityID'
    ])
  },
  methods: {
    ...mapMutations([
      'set_FormShow',
      'set_MenuChoice',
      'set_nodeData',
      'set_linkData'
    ]),
    ...mapActions([
      'getGraphJSON',
      'getRelativeRelation'
    ]),
    changeSize () {
      console.log('changesize')
      this.$emit('changeSize')
    },
    changeMode () {
      this.$emit('changeMode')
    },
    searchShow () {
      this.$emit('searchResultShow')
      // 该方法位于presentation中，同名
    },
    searchRelationShow () {
      console.log('searchRelation')
      this.$emit('searchRelationResultShow')
      // 该方法位于presentation中，同名
    },
    searchShowEnd () {
      this.$emit('searchResultShowEnd')
      // 该方法位于presentation中，同名
    },
    choiceOne () {
      if (this.menuChoice === 1) {
        this.set_MenuChoice(0)
        this.set_FormShow(false)
      } else {
        this.set_MenuChoice(1)
        this.set_FormShow(true)
      }
      this.$emit('changeSize')
    },
    choiceTwo () {
      if (this.menuChoice === 2) {
        this.set_MenuChoice(0)
        this.set_FormShow(false)
      } else {
        this.set_MenuChoice(2)
        this.set_FormShow(true)
      }
      this.$emit('changeSize')
    },
    choiceThree () {
      if (this.menuChoice === 3) {
        this.set_MenuChoice(0)
        this.set_FormShow(false)
      } else {
        this.set_MenuChoice(3)
        this.set_FormShow(true)
      }
      this.$emit('changeSize')
    },
    handleSizeChange (value) {
      console.log(value)
    },
    refreshForm1 () {
      this.refresh = false
      // 在组件移除后，重新渲染组件
      // this.$nextTick可实现在DOM 状态更新后，执行传入的方法。
      this.$nextTick(() => {
        this.refresh = true
      })
    },
    async Init2 () {
      var that = this
      console.log('gggggg')
      await that.getRelativeRelation(this.currentEntityID)
      console.log('gggggg')
      that.set_nodeData(that.graphJSON.entities)
      that.set_linkData(that.graphJSON.relations)
      console.log('init2')
      console.log(that.graphJSON)
      that.$parent.$parent.$parent.processLinkData()
      that.$parent.$parent.$parent.Init()
    }
  }
}
</script>

<style scoped>
.item .el-form-item__label{
  font-size: 50px;
}
.sideMenu{
  min-height: 600px;
}
.sideMenuItem{
  height: 70px;
}
.el-icon-edit{
  font-size: 30px;
  text-align: center;
}
.el-icon-s-data{
  font-size: 30px;
}
.el-icon-search{
  font-size: 30px;
}

</style>
