<template>
  <div class="form1">
  <el-form >
    <div class="mytitle"><span class="dashed">—— </span>搜索<span class="dashed"> ————————</span></div>
    <el-form-item class="item" style="padding-top: 30px">
      <div class="mylabel">搜索类型：</div>
      <el-select v-model="search_type" size="small" placeholder="请选择搜索类型" style="float: left;width: 150px">
        <el-option label="搜索实体" value="1"></el-option>
        <el-option label="搜索关系" value="2"></el-option>
        <el-option label="按label搜索" value="3"></el-option>
      </el-select>
    </el-form-item>
    <el-form-item v-if="search_type === '1'">
      <div class="mylabel">实体名称：</div>
<!--      <el-input v-model="entityName" size="small" placeholder="请输入实体名称" style="width: 150px;float:left;"></el-input>-->
            <el-autocomplete
              v-model="entityName"
              :fetch-suggestions="querySearchAsync"
              placeholder="请输入实体名称"
              style="width: 150px;float:left;"
              size="small"
            ></el-autocomplete>
<!--      <el-input v-model="propName" v-if="search_type === '1'" placeholder="请输入属性名称"-->
<!--                style="width: 130px;float:left;margin-left: 20px"></el-input>-->
<!--      <span class="dashed2" v-if="search_type === '1'">&nbsp;_&nbsp;</span>-->
<!--      <el-input v-model="propVal" v-if="search_type === '1'" placeholder="请输入属性值"-->
<!--                style="width: 130px;float:left;"></el-input>-->
    </el-form-item>
    <el-form-item v-if="search_type === '2'">
      <div class="mylabel">关系名称：</div>
<!--      <el-input v-model="relationName" size="small" placeholder="请输入关系名称" style="width: 150px;float:left;"></el-input>-->
      <el-autocomplete
        v-model="relationName"
        size="small"
        :fetch-suggestions="querySearchAsync"
        placeholder="请输入关系名称"
        style="width: 150px;float:left;"
      ></el-autocomplete>
    </el-form-item>
    <el-form-item v-if="search_type === '3'">
      <div class="mylabel">label名称：</div>
<!--      <el-input v-model="labelName" size="small" placeholder="请输入label" style="width: 150px;float:left;"></el-input>-->
      <el-autocomplete
        v-model="labelName"
        size="small"
        :fetch-suggestions="querySearchAsync"
        placeholder="请输入label"
        style="width: 150px;float:left;"
      ></el-autocomplete>
    </el-form-item>
  </el-form>
  <el-button type="" size="small" icon="el-icon-refresh" style="position: absolute; top: 220px;right:20px;border-radius: 20px" @click="searchEnd">清空</el-button>
  <el-button type="primary" size="small"  icon="el-icon-search" style="position: absolute; top: 220px;right:100px;float:right;border-radius: 20px" @click="search">搜索</el-button>
  </div>
</template>

<script>
import {mapActions, mapGetters, mapMutations} from 'vuex'
export default {
  name: 'searchForm',
  data () {
    return {
      timeout: null,
      search_type: '1',
      propName: '',
      propVal: '',
      labelName: '',
      entityName: '',
      relationName: '',
      search_target: '',
      enHistory: [],
      reHistory: [],
      laHistory: []
    }
  },
  mounted () {
    this.enHistory = this.searchEntityHistory
    this.reHistory = this.searchRelationHistory
    this.laHistory = this.searchLabelHistory
    console.log(this.reHistory)
  },
  computed: {
    ...mapGetters([
      'selectedType',
      'currentEntityID',
      'currentRelationID',
      'nodeData',
      'linkData',
      'searchEntityHistory',
      'searchRelationHistory',
      'searchLabelHistory'
    ])
  },
  methods: {
    ...mapMutations([
      'set_searchEntityHistory',
      'set_searchRelationHistory',
      'set_searchLabelHistory'
    ]),
    ...mapActions([
      'searchEntityByLabel',
      'searchEntityByName',
      'searchRelationByName'
    ]),
    querySearchAsync (queryString, cb) {
      console.log(queryString)
      var results = ''
      if (this.search_type === '1') {
        var enHistory = this.enHistory
        results = queryString ? enHistory.filter(this.createStateFilter(queryString)) : enHistory
      } else if (this.search_type === '2') {
        var reHistory = this.reHistory
        results = queryString ? reHistory.filter(this.createStateFilter(queryString)) : reHistory
      } else {
        var laHistory = this.laHistory
        results = queryString ? laHistory.filter(this.createStateFilter(queryString)) : laHistory
      }
      clearTimeout(this.timeout)
      this.timeout = setTimeout(() => {
        cb(results)
      }, 3000 * Math.random())
    },
    createStateFilter (queryString) {
      return (entityName) => {
        return (entityName.value.toLowerCase().indexOf(queryString.toLowerCase()) === 0)
      }
    },
    loadAll () {
    },
    async search () {
      var target = {}
      var data = {}
      if (this.search_type === '1') { // 按实体名称搜索
        // target[this.propName] = this.propVal
        target.name = this.entityName
        await this.searchEntityByName(this.entityName)
        data.value = this.entityName
        this.set_searchEntityHistory(data)
        this.$emit('searchShow')
      } else if (this.search_type === '2') { // 按关系
        await this.searchRelationByName(this.relationName)
        data.value = this.relationName
        this.set_searchRelationHistory(data)
        this.$emit('searchRelationShow')
      } else { // 按label名搜索
        var labelList = []
        labelList.push(this.labelName)
        await this.searchEntityByLabel(this.labelName)
        data.value = this.labelName
        this.set_searchLabelHistory(data)
        this.$emit('searchShow')
      }
      console.log(target)
      // 该方法位于SideBar中，同名
    },
    searchEnd () {
      this.$emit('searchShowEnd')
      // 该方法位于SideBar中，同名
    }
  }
}
</script>

<style scoped>
  .mylabel {
    text-align: left;
    padding-left: 20px;
    font-size: 14px;
    float: left;
  }

  .mytitle {
    text-align: left;
    padding-left: 10px;
    padding-top: 30px;
    padding-bottom: 10px;
    font-size: 18px;
    float: top;
  }

  .dashed {
    color: #D3D3D3;
  }
  .form1{
    background-color: #E4EFF9;
    height: 99%;
    width: 98%;
    border-radius: 10px;
    box-shadow: 0 2px 4px rgba(0, 0, 0, .12), 0 0 6px rgba(0, 0, 0, .04)
  }

</style>
