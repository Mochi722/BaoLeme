import Vue from 'vue'
import Vuex from 'vuex'
import {
  addEntityAPI,
  addRelationAPI,
  deleteEntityAPI,
  deleteRelationAPI,
  getGraphJSONAPI,
  getPropsByIdAPI,
  reverseRelationDirAPI,
  updateEntityNameAPI,
  updateRelationNameAPI,
  uploadGraphJsonAPI,
  uploadLocationAPI,
  updateEntityPropsByIdAPI,
  updateRelationPropsByIdAPI,
  searchEntityByLabelAPI,
  searchEntityByNameAPI,
  searchRelationByNameAPI,
  getLabelStatisticsAPI,
  getTreeByIdAPI,
  getDistinctLabelsAPI,
  getDistinctRelationsAPI,
  verifyInformationAPI,
  addAccountAPI,
  getRecommendAPI,
  getRelativeRelationAPI,
  modifyFavorAPI,
  modifyPasswordAPI,
  getDistinctPropsValueAPI,
  intelligenceSearchAPI,
  getCollectionAPI,
  collectAPI, cookWhatAPI,
  uncollectAPI,
  getAnswerAPI,
  modifyUserInfoAPI
} from '../api/user'
// import user from './modules/user'
// import getters from './getters'
Vue.use(Vuex)
const state = {
  graphJSON: [],
  idReturned: 0,
  EntityList: [],
  imageShow: true,
  nodeData: [],
  linkData: [],
  show: false,
  stepShow: '',
  UpEn: '',
  DownEn: '',
  RelationName: '',
  OpDialogFlag: true,
  EntityName: '',
  linkEditShow: false,
  formShow: false,
  menuChoice: 0,
  selectedType: '',
  curProps: [],
  currentEntityID: 0,
  currentRelationID: 0,
  relationTextVisible: true,
  mouseOnEntity: false,
  labelStatistics: [],
  resultEntityList: [],
  resultRelationList: [],
  distinctLabels: [],
  distinctRelations: [],
  searchType: 0,
  treeData: {},
  searchEntityHistory: [],
  searchRelationHistory: [],
  searchLabelHistory: [],
  verificationRes: '',
  accountAddFlag: false,
  recommendList: [],
  collectionList: [],
  currentUserID: -1,
  editpswdDialogVisible: false,
  flavorList: [],
  methodList: [],
  typeList: [],
  ingreTypeList: [],
  intelligenceSearchResult: [],
  adminSearchResultVisible: false,
  graphMode: false,
  recipeTable: false,
  editPropsDialogShow: false,
  drawer: false,
  currentEntityInfo: {},
  DialogueItemShow: false,
  saveConfirm: false,
  answer: '',
  currentCenter: -1,
  currentCenterInfo: {}
}
const mutations = {
  set_GraphJSON: function (state, data) {
    state.graphJSON = []
    Vue.set(state, 'graphJSON', data)
    console.log('////////')
    console.log(state.graphJSON)
  },
  set_curEntityName: function (state, data) {
    state.curEntityName = data
  },
  set_idReturned: function (state, data) {
    state.idReturned = data
    console.log('idReturned: ' + state.idReturned)
  },
  set_EntityList: function (state, data) {
    state.EntityList = data
  },
  update_EntityList: function (state, data) {
    state.EntityList.push(data)
  },
  set_imageShow: function (state, data) {
    state.imageShow = data
  },
  set_nodeData: function (state, data) {
    state.nodeData = data
  },
  set_linkData: function (state, data) {
    state.linkData = data
  },
  set_show: function (state, data) {
    state.show = data
  },
  set_stepShow: function (state, data) {
    state.stepShow = data
  },
  set_UpEn: function (state, data) {
    state.UpEn = data
  },
  set_DownEn: function (state, data) {
    state.DownEn = data
  },
  set_RelationName: function (state, data) {
    state.RelationName = data
  },
  set_EntityName: function (state, data) {
    state.EntityName = data
  },
  set_OpDialogFlag: function (state, data) {
    state.OpDialogFlag = data
  },
  set_LinkEditShow: function (state, data) {
    state.linkEditShow = data
  },
  set_FormShow: function (state, data) {
    state.formShow = data
  },
  set_MenuChoice: function (state, data) {
    state.menuChoice = data
  },
  set_SelectedType: function (state, data) {
    state.selectedType = data
  },
  set_CurProps: function (state, data) {
    state.curProps = data
  },
  set_CurrentEntityID: function (state, data) {
    state.currentEntityID = data
  },
  set_CurrentRelationID: function (state, data) {
    state.currentRelationID = data
  },
  set_RelationTextVisible: function (state, data) {
    state.relationTextVisible = data
  },
  set_MouseOnEntity: function (state, data) {
    state.mouseOnEntity = data
  },
  set_LabelStatistics: function (state, data) {
    state.labelStatistics = data
  },
  set_ResultEntityList: function (state, data) {
    state.resultEntityList = data
  },
  set_ResultRelationList: function (state, data) {
    state.resultRelationList = data
  },
  set_distinctLabels: function (state, data) {
    state.distinctLabels = data
  },
  set_distinctRelations: function (state, data) {
    state.distinctRelations = data
  },
  set_SearchType: function (state, data) {
    state.searchType = data
  },
  set_treeData: function (state, data) {
    state.treeData = data
  },
  set_searchEntityHistory: function (state, data) {
    var flag = true
    for (var i = 0; i < state.searchEntityHistory.length; i++) {
      if (state.searchEntityHistory[i].value === data.value) flag = false
    }
    if (flag) state.searchEntityHistory.push(data)
  },
  set_searchRelationHistory: function (state, data) {
    var flag = true
    for (var i = 0; i < state.searchRelationHistory.length; i++) {
      if (state.searchRelationHistory[i].value === data.value) flag = false
    }
    if (flag) state.searchRelationHistory.push(data)
    console.log(state.searchRelationHistory)
  },
  set_searchLabelHistory: function (state, data) {
    var flag = true
    for (var i = 0; i < state.searchLabelHistory.length; i++) {
      if (state.searchLabelHistory[i].value === data.value) flag = false
    }
    if (flag) state.searchLabelHistory.push(data)
  },
  set_verificationRes: function (state, data) {
    state.verificationRes = data
  },
  set_accountAddFlag: function (state, data) {
    state.accountAddFlag = data
  },
  set_recommendList: function (state, data) {
    state.recommendList = data
  },
  set_collectionList: function (state, data) {
    state.collectionList = data
  },
  set_currentUserID: function (state, data) {
    state.currentUserID = data
  },
  set_editpswdDialogVisible: function (state, data) {
    state.editpswdDialogVisible = data
  },
  set_flavorList: function (state, data) {
    state.flavorList = data
  },
  set_methodList: function (state, data) {
    state.methodList = data
  },
  set_typeList: function (state, data) {
    state.typeList = data
  },
  set_ingreTypeList: function (state, data) {
    state.ingreTypeList = data
  },
  set_intelligenceSearchResult: function (state, data) {
    state.intelligenceSearchResult = data
  },
  set_adminSearchResultVisible: function (state, data) {
    state.adminSearchResultVisible = data
  },
  set_graphMode: function (state, data) {
    state.graphMode = data
  },
  set_recipeTable: function (state, data) {
    state.recipeTable = data
  },
  set_editPropsDialogShow: function (state, data) {
    state.editPropsDialogShow = data
  },
  set_drawer: function (state, data) {
    state.drawer = data
  },
  set_currentEntityInfo: function (state, data) {
    state.currentEntityInfo = data
  },
  set_DialogueItemShow: function (state, data) {
    state.DialogueItemShow = data
  },
  set_saveConfirm: function (state, data) {
    state.saveConfirm = data
  },
  set_answer: function (state, data) {
    state.answer = data
  },
  set_currentCenter: function (state, data) {
    console.log('setcurrentcenter')
    state.currentCenter = data
  },
  set_currentCenterInfo: function (state, data) {
    state.currentCenterInfo = data
  }
}
const getters = {
  graphJSON: function (state) {
    return state.graphJSON
  },
  idReturned: function (state) {
    return state.idReturned
  },
  EntityList: function (state) {
    return state.EntityList
  },
  imageShow: function (state) {
    return state.imageShow
  },
  nodeData: function (state) {
    return state.nodeData
  },
  linkData: function (state) {
    return state.linkData
  },
  show: function (state) {
    return state.show
  },
  stepShow: function (state) {
    return state.stepShow
  },
  UpEn: function (state) {
    return state.UpEn
  },
  DownEn: function (state) {
    return state.DownEn
  },
  RelationName: function (state) {
    return state.RelationName
  },
  EntityName: function (state) {
    return state.EntityName
  },
  OpDialogFlag: function (state) {
    return state.OpDialogFlag
  },
  linkEditShow: function (state) {
    return state.linkEditShow
  },
  formShow: function (state) {
    return state.formShow
  },
  menuChoice: function (state) {
    return state.menuChoice
  },
  selectedType: function (state) {
    return state.selectedType
  },
  curProps: function (state) {
    return state.curProps
  },
  currentEntityID: function (state) {
    return state.currentEntityID
  },
  currentRelationID: function (state) {
    return state.currentRelationID
  },
  relationTextVisible: function (state) {
    return state.relationTextVisible
  },
  mouseOnEntity: function (state) {
    return state.mouseOnEntity
  },
  LabelStatistics: function (state) {
    return state.labelStatistics
  },
  resultEntityList: function (state) {
    return state.resultEntityList
  },
  resultRelationList: function (state) {
    return state.resultRelationList
  },
  searchType: function (state) {
    return state.search_type
  },
  treeData: function (state) {
    return state.treeData
  },
  distinctLabels: function (state) {
    return state.distinctLabels
  },
  distinctRelations: function (state) {
    return state.distinctRelations
  },
  searchEntityHistory: function (state) {
    return state.searchEntityHistory
  },
  searchRelationHistory: function (state) {
    return state.searchRelationHistory
  },
  searchLabelHistory: function (state) {
    return state.searchLabelHistory
  },
  verificationRes: function (state) {
    return state.verificationRes
  },
  accountAddFlag: function (state) {
    return state.accountAddFlag
  },
  recommendList: function (state) {
    return state.recommendList
  },
  collectionList: function (state) {
    return state.collectionList
  },
  currentUserID: function (state) {
    return state.currentUserID
  },
  editpswdDialogVisible: function (state) {
    return state.editpswdDialogVisible
  },
  flavorList: function (state) {
    return state.flavorList
  },
  methodList: function (state) {
    return state.methodList
  },
  typeList: function (state) {
    return state.typeList
  },
  ingreTypeList: function (state) {
    return state.ingreTypeList
  },
  intelligenceSearchResult: function (state) {
    return state.intelligenceSearchResult
  },
  adminSearchResultVisible: function (state) {
    return state.adminSearchResultVisible
  },
  graphMode: function (state) {
    return state.graphMode
  },
  recipeTable: function (state) {
    return state.recipeTable
  },
  editPropsDialogShow: function (state) {
    return state.editPropsDialogShow
  },
  drawer: function (state) {
    return state.drawer
  },
  currentEntityInfo: function (state) {
    return state.currentEntityInfo
  },
  DialogueItemShow: function (state) {
    return state.DialogueItemShow
  },
  saveConfirm: function (state) {
    return state.saveConfirm
  },
  answer: function (state) {
    return state.answer
  },
  currentCenter: function (state) {
    return state.currentCenter
  },
  currentCenterInfo: function (state) {
    return state.currentCenterInfo
  }
}
const actions = {
  getGraphJSON: async ({state, commit}) => {
    console.log('&&&')
    const res = await getGraphJSONAPI()
    if (res) {
      var graphJSON = {}
      var entity = []
      for (var i = 0; i < res.entity.length; i++) {
        var en = {}
        en.id = res.entity[i].id
        en.name = res.entity[i].properties.name
        en.fx = res.entity[i].d3_properties.__D3_PROPS__x
        en.fy = res.entity[i].d3_properties.__D3_PROPS__y
        en.fixed = res.entity[i].d3_properties.__D3_PROPS__fixed
        en.color = res.entity[i].d3_properties.__D3_PROPS__color
        en.shape = res.entity[i].d3_properties.__D3_PROPS__shape
        en.size = res.entity[i].d3_properties.__D3_PROPS__size
        en.textsize = res.entity[i].d3_properties.__D3_PROPS__textsize
        en.labels = res.entity[i].labels
        en.properties = res.entity[i].properties
        entity.push(en)
      }
      graphJSON.entity = entity
      graphJSON.relations = res.relations
      await commit('set_GraphJSON', graphJSON)
      console.log('-----获取图json成功------')
      console.log(res)
      console.log(graphJSON)
    }
  },
  uploadGraphJSON: async ({state, dispatch}, data) => {
    console.log('上传文件\n' + data)
    const res = await uploadGraphJsonAPI(data)
    if (res) {
      console.log('-----上传文件成功------')
      await dispatch('getGraphJSON')
    }
  },
  deleteEntity: async ({state, dispatch}, data) => {
    const res = await deleteEntityAPI(data)
    if (res) {
      await dispatch('getRelativeRelation', state.currentEntityID)
      console.log('-----删除节点成功------')
      console.log(res)
    }
  },
  addEntity: async ({state, dispatch, commit}, data) => {
    var newEn = {}
    var props = {}
    props.__D3_PROPS__shape = '圆形'
    props.__D3_PROPS__color = '#A4D7D6'
    props.__D3_PROPS__size = 40
    props.__D3_PROPS__textsize = 15
    props.name = data
    newEn.properties = props
    newEn.labels = []
    const res = await addEntityAPI(newEn)
    if (res) {
      console.log('-----添加节点成功-----')
      console.log(res)
      await commit('set_idReturned', res)
      await dispatch('getGraphJSON')
    } else {
      console.log('-----添加节点失败-----')
      console.log(res)
    }
  },
  addRelationapi: async ({state, dispatch, commit}, data) => { // 由于与presentation中方法重名，更名添加api后缀
    console.log('addRelation')
    var props = {}
    props.__D3_PROPS__color = '#ccc'
    props.__D3_PROPS__lineStyle = '1'
    props.__D3_PROPS__textsize = '15'
    props.__D3_PROPS__weight = '2.5'
    data.properties = props
    const res = await addRelationAPI(data)
    if (res) {
      await dispatch('getGraphJSON')
      await commit('set_idReturned', res)
      console.log('-----添加关系成功-----')
    }
  },
  updateEntityName: async ({state, dispatch}, data) => {
    const res = await updateEntityNameAPI(data)
    if (res) {
      await dispatch('getGraphJSON')
      console.log('-----更改实体名称成功-----')
    }
  },
  updateRelationName: async ({state, dispatch}, data) => {
    const res = await updateRelationNameAPI(data)
    if (res) {
      await dispatch('getGraphJSON')
      console.log('-----更改关系名称成功-----')
    }
  },
  reverseRelationDir: async ({state, dispatch}, data) => {
    const res = await reverseRelationDirAPI(data)
    console.log('更改关系方向')
    console.log(res)
    if (res) {
      await dispatch('getGraphJSON')
      console.log('-----更改关系方向成功-----')
    }
  },
  removeRelation: async ({state, dispatch}, data) => {
    const res = await deleteRelationAPI(data)
    if (res) {
      await dispatch('getGraphJSON')
      console.log('-----删除关系成功-----')
    }
  },
  uploadLocation: async ({state, dispatch}) => {
    var nodes = state.nodeData
    var location = []
    for (var i = 0; i < nodes.length; i++) {
      // 只上传位置信息+id
      var loc = {}
      loc['id'] = nodes[i].id
      var properties = {}
      properties['__D3_PROPS__x'] = Number((nodes[i].x).toFixed(0))
      properties['__D3_PROPS__y'] = Number(nodes[i].y.toFixed(0))
      properties['__D3_PROPS__fixed'] = true
      loc['properties'] = properties
      location.push(loc)
    }
    // location = JSON.stringify(location)
    console.log(location)
    const res = await uploadLocationAPI(location)
    if (res) {
      await dispatch('getGraphJSON')
      console.log('-----保存布局成功-----')
    }
  },
  getPropsById: async ({state, commit}, data) => {
    const res = await getPropsByIdAPI(data)
    if (res) {
      await commit('set_CurProps', res)
      console.log('-----获取实体属性s成功-----')
    }
  },
  updateEntityPropsById: async ({state, dispatch}, data) => {
    const res = await updateEntityPropsByIdAPI(data)
    if (res) {
      await dispatch('getGraphJSON')
      console.log('-----更新实体属性成功-----')
    }
  },
  updateRelationPropsById: async ({state, dispatch}, data) => {
    const res = await updateRelationPropsByIdAPI(data)
    if (res) {
      await dispatch('getGraphJSON')
      console.log('-----更新关系属性成功-----')
    }
  },
  getLabelStatistics: async ({state, commit}) => {
    console.log('in get--------------')
    const res = await getLabelStatisticsAPI()
    console.log(res)
    if (res) {
      console.log(res)
      await commit('set_LabelStatistics', res)
      console.log('-----获取label统计成功-----')
    }
  },
  searchEntityByLabel: async ({state, commit}, data) => {
    console.log('[[[[')
    console.log(data)
    console.log('[[[[')
    const res = await searchEntityByLabelAPI(data)
    if (res) {
      await commit('set_ResultEntityList', res)
      console.log('-----按label搜索实体成功-----')
    }
  },
  searchEntityByName: async ({state, commit}, data) => {
    const res = await searchEntityByNameAPI(data)
    if (res) {
      await commit('set_ResultEntityList', res)
      console.log('-----按name搜索实体成功-----')
    }
  },
  searchRelationByName: async ({state, commit}, data) => {
    console.log('[[[[')
    console.log(data)
    console.log('[[[[')
    const res = await searchRelationByNameAPI(data)
    if (res) {
      await commit('set_ResultRelationList', res)
      console.log('-----按name搜索关系成功-----')
    }
  },
  getTreeById: async ({state, commit}, data) => {
    console.log(data)
    const res = await getTreeByIdAPI(data.id, data.depth)
    console.log(res)
    if (res) {
      await commit('set_treeData', res)
      console.log('-----获取treeData成功-----')
    }
  },
  getDistinctLabels: async ({state, commit}) => {
    const res = await getDistinctLabelsAPI()
    if (res) {
      await commit('set_distinctLabels', res)
      console.log('-----获取所有labels成功-----')
    }
  },
  getDistinctRelations: async ({state, commit}) => {
    const res = await getDistinctRelationsAPI()
    if (res) {
      await commit('set_distinctRelations', res)
      console.log('-----获取所有labels成功-----')
    }
  },
  verifyInformation: async ({state, commit}, data) => {
    const res = await verifyInformationAPI(data)
    console.log(res)
    if (res.success) {
      await commit('set_verificationRes', res.content)
      console.log(res.message)
      await commit('set_currentUserID', res.content.id)
      window.localStorage.setItem('userID', res.content.id)
      window.localStorage.setItem('verificationRes', JSON.stringify(res.content))
      console.log('sessionStorage:  ' + window.localStorage.getItem('verificationRes'))
    } else {
      console.log(res.message)
    }
  },
  addAccount: async ({state, commit}, data) => {
    const res = await addAccountAPI(data)
    if (res) {
      await commit('set_accountAddFlag', res.content)
    }
    console.log(res.message)
    return res
  },
  getRecommend: async ({state, commit}, data) => {
    const res = await getRecommendAPI(data)
    if (res) {
      await commit('set_recommendList', res)
      console.log('-----获取推荐成功-----')
    }
  },
  getCollection: async ({state, commit}, data) => {
    const res = await getCollectionAPI(data)
    if (res) {
      await commit('set_collectionList', res.content)
      console.log('-----获取收藏成功-----')
      console.log(res)
    }
  },
  spreadEntity: async ({state, commit}, data) => {
    console.log('spread~~~~~~')
    const fullresult = await getRelativeRelationAPI(data)
    var res = fullresult.content
    console.log(fullresult)
    if (fullresult.success) {
      var result = graphDataProcedure(res)
      console.log(result)
      var originGraphJson = state.graphJSON
      console.log('origin')
      console.log(originGraphJson)
      for (var i = 0; i < result.entities.length; i++) {
        if (!originGraphJson.entities.includes(result.entities[i])) {
          originGraphJson.entities.push(result.entities[i])
        }
      }
      for (var j = 0; j < result.relations.length; j++) {
        if (!originGraphJson.relations.includes(result.relations[j])) {
          originGraphJson.relations.push(result.relations[j])
        }
        // originGraphJson.relations.push(result.relations[j])
      }
      await commit('set_GraphJSON', originGraphJson)
      console.log('-----展开节点成功-----')
    }
  },
  getRelativeRelation: async ({state, commit}, data) => {
    const fullresult = await getRelativeRelationAPI(data)
    console.log('relativeRelation:')
    console.log(fullresult.success)
    const res = fullresult.content
    console.log(res)
    if (fullresult.success) {
      var result = graphDataProcedure(res)
      await commit('set_GraphJSON', result)
      console.log('-----获取相关关系成功-----')
    }
  },
  modifyFavor: async ({state}, data) => {
    const res = await modifyFavorAPI(data)
    console.log(res.message)
  },
  modifyPassword: async ({state}, data) => {
    const res = await modifyPasswordAPI(data)
    console.log('---------*********')
    console.log(res)
    return res
  },
  modifyUserInfo: async ({state}, data) => {
    const res = await modifyUserInfoAPI(data)
    console.log('修改用户信息')
    console.log(res)
    return res
  },
  getDistinctPropsValue: async ({state, commit}, data) => {
    const res = await getDistinctPropsValueAPI(data)
    if (res.success) {
      console.log('coontent')
      console.log(res.content)
      if (data.label === 'Recipe') {
        if (data.propName === '口味') await commit('set_flavorList', res.content)
        else if (data.propName === '工艺') await commit('set_methodList', res.content)
        else await commit('set_typeList', res.content)
      } else await commit('set_ingreTypeList', res.content)
    }
    console.log(res.message)
  },
  intelligenceSearch: async ({state, commit}, data) => {
    const res = await intelligenceSearchAPI(data)
    console.log('res:')
    console.log(res)
    if (res.success) {
      console.log(res.content)
      await commit('set_intelligenceSearchResult', res.content)
    }
    console.log(res.message)
  },
  collectEntity: async ({state, dispatch}, data) => {
    const res = await collectAPI(data)
    await dispatch('getCollection', data.id)
    console.log(res.message)
  },
  uncollect: async ({state, dispatch}, data) => {
    const res = await uncollectAPI(data)
    console.log('__uncollect__')
    console.log(res)
    if (res.success) {
      await dispatch('getCollection', data.id)
    }
    console.log(res.message)
  },
  cookWhat: async ({state, commit}, data) => {
    const res = await cookWhatAPI(data)
    console.log(res)
    if (res.success) {
      await commit('set_intelligenceSearchResult', res.content)
    }
  },
  getAnswer: async ({state, commit}, data) => {
    const res = await getAnswerAPI(data)
    console.log(res)
    if (res.success) {
      await commit('set_answer', res.content)
    }
  }
}
function graphDataProcedure (res) {
  var graphJSON1 = {}
  var entity1 = []
  var relations1 = []
  for (var i = 0; i < res.entity.length; i++) {
    var en = {}
    en.id = res.entity[i].id
    en.name = res.entity[i].properties.name
    en.fx = res.entity[i].d3_properties.__D3_PROPS__x
    en.fy = res.entity[i].d3_properties.__D3_PROPS__y
    en.fixed = res.entity[i].d3_properties.__D3_PROPS__fixed
    if (Object.keys(res.entity[i].d3_properties).length === 0) {
      console.log('uuuuuuuuuuu')
      en.color = res.entity[i].labels[0] === 'Recipe' ? '#7EA9D3' : '#FFC0CB'
      en.shape = res.entity[i].labels[0] === 'Recipe' ? '圆形' : '正方形'
      en.size = 50
      en.textsize = 15
    } else {
      console.log('nnnnnn')
      en.color = res.entity[i].d3_properties.__D3_PROPS__color
      en.shape = res.entity[i].d3_properties.__D3_PROPS__shape
      en.size = res.entity[i].d3_properties.__D3_PROPS__size
      en.textsize = res.entity[i].d3_properties.__D3_PROPS__textsize
    }
    en.labels = res.entity[i].labels
    en.properties = res.entity[i].properties
    entity1.push(en)
  }
  for (var j = 0; j < res.relation.length; j++) {
    var re = {}
    var red3 = {}
    re.id = res.relation[j].id
    re.name = res.relation[j].name
    re.source = res.relation[j].source
    re.target = res.relation[j].target
    re.properties = res.relation[j].properties
    if (Object.keys(res.relation[j].d3_properties).length === 0) {
      red3.__D3_PROPS__color = '#ccc'
      red3.__D3_PROPS__lineStyle = '1'
      red3.__D3_PROPS__textsize = '15'
      red3.__D3_PROPS__weight = '2.5'
      re.d3_properties = red3
    } else re.d3_properties = res.relation[j].d3_properties
    relations1.push(re)
  }
  graphJSON1.entities = entity1
  graphJSON1.relations = relations1
  // let result = Object.assign([], this.graphJSON, graphJSON1)
  return graphJSON1
}
const store = new Vuex.Store({
  state,
  mutations,
  actions,
  getters
})
export default store
