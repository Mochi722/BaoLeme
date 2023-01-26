<template>
  <el-form class="form1" v-if="refresh">
    <div class="mytitle">&nbsp;&nbsp;图谱统计</div>
    <el-form-item class="item" style="padding-top: 30px; font-size: 50px">
      <div class="mylabel"></div>
      <div id="labelsBox" class="labelsBox" style="float: left">
        <span class="dashed">——</span>&nbsp;Node Labels&nbsp;<span class="dashed">———————</span>
        <div>
        <el-button class="btnTag" style="margin-left: 0;margin-right: 10px;" type="primary" plain size="mini" @click="searchEnd">*({{labelSum}})</el-button>
        <el-button
          style="margin-left: 0;margin-right: 10px;"
          class="btnTag"
          type="primary"
          plain
          size="mini"
          :key="tag"
          v-for="tag in this.distinctLabels"
          :disable-transitions="false"
          @click="labelTagClicked(tag.label)">
          {{tag.label}}({{tag.count}})
        </el-button>
        </div>
      </div>
    </el-form-item>
    <el-form-item class="item" style="padding-top: 30px; font-size: 50px">
      <div class="mylabel"></div>
      <div id="relationBox" class="labelsBox" style="float: left">
        <span class="dashed">——</span>&nbsp;RelationTypes&nbsp;<span class="dashed">———————</span>
        <div>
        <el-button class="btnTag" style="margin-left: 0;margin-right: 10px;" type="primary" plain size="mini" @click="searchEnd">*({{relationSum}})</el-button>
        <el-button
          style="margin-left: 0;margin-right: 10px;"
          class="btnTag"
          type="primary"
          plain
          size="mini"
          :key="tag"
          v-for="tag in this.distinctRelations"
          :disable-transitions="false"
          @click="relationTagClicked(tag.name)">
          {{tag.name}}({{tag.count}})
        </el-button>
        </div>
      </div>
    </el-form-item>
  </el-form>
</template>

<script>
import {mapActions, mapGetters, mapMutations} from 'vuex'
export default {
  name: 'statisticsForm',
  data () {
    return {
      refresh: true,
      inputVisible: false,
      labelList: [],
      labelSum: 0,
      relationSum: 0
    }
  },
  mounted () {
    this.getLabelStatistics()
    this.initLabelsBox()
  },
  computed: {
    ...mapGetters([
      'LabelStatistics',
      'distinctLabels',
      'distinctRelations'
    ])
  },
  methods: {
    ...mapMutations([]),
    ...mapActions([
      'getLabelStatistics',
      'getDistinctLabels',
      'searchEntityByLabel',
      'searchRelationByName'
    ]),
    initLabelsBox () {
      console.log(this.distinctLabels)
      for (var i = 0; i < this.distinctLabels.length; i++) {
        this.labelSum += this.distinctLabels[i].count
      }
      for (var j = 0; j < this.distinctRelations.length; j++) {
        this.relationSum += this.distinctRelations[j].count
      }
      console.log(this.labelSum)
    },
    async labelTagClicked (d) {
      await this.searchEntityByLabel(d)
      this.$emit('searchShow')
    },
    async relationTagClicked (d) {
      await this.searchRelationByName(d)
      this.$emit('searchRelationShow')
    },
    searchEnd () {
      this.$emit('searchShowEnd')
    }
  }
}
</script>

<style>

  .dashed{
    color: #D3D3D3;
  }
  .mytitle {
    text-align: left;
    padding-left: 20px;
    padding-top: 20px;
    font-size: 18px;
    float: top;
  }
  .labelsBox {
    width: 95%;
    height: 50%;
    margin-left: 5%;
    float: top;
    text-align: left;
  }
  .form1{
    background-color: #E4EFF9;
    height: 100%;
    width: 98%;
    border-radius: 10px;
    box-shadow: 0 2px 4px rgba(0, 0, 0, .12), 0 0 6px rgba(0, 0, 0, .04)
  }
  .mylabel{
    text-align: left;
    padding-left: 20px;
    font-size: 15px;
    float: left;
  }
  .btnTag{
    background-color: #488cd1;
    border-radius: 15px;
  }
</style>
