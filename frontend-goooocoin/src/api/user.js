import {axios} from '../utils/request'

const api = {
  userPre: '/goooocoin'
  // userPre: ''
}

// export function getChildEntityAPI (data) {
//   return axios({
//     url: `${api.userPre}/get_child_e`,
//     method: 'GET',
//     data
//   })
// }

// 传输测试
export function getHelloAPI () {
  return axios({
    url: `${api.userPre}/`,
    method: 'GET'
  })
}

// 上传实体与关系表json
export function uploadGraphJsonAPI (data) {
  return axios({
    url: `${api.userPre}/import_json`,
    method: 'POST',
    params: {data: data}
  })
}

// 返回实体和关系的json
export function getGraphJSONAPI (data) {
  return axios({
    url: `${api.userPre}/get_all`,
    method: `GET`
  })
}

// 添加实体
export function addEntityAPI (data) {
  return axios({
    url: `${api.userPre}/admin/add_e`,
    method: 'POST',
    headers: {'Content-Type': 'application/json'},
    data: data
  })
}

export function addRelationAPI (data) {
  return axios({
    url: `${api.userPre}/admin/add_r`,
    method: 'POST',
    headers: {'Content-Type': 'application/json'},
    data: data
  })
}

export function updateEntityNameAPI (data) {
  return axios({
    url: `${api.userPre}/admin/upd_e_name`,
    method: 'POST',
    headers: {'Content-Type': 'application/json'},
    data: data
  })
}

export function updateRelationNameAPI (data) {
  return axios({
    url: `${api.userPre}/admin/upd_r_name`,
    method: 'POST',
    headers: {'Content-Type': 'application/json'},
    data: data
  })
}

export function reverseRelationDirAPI (data) {
  return axios({
    url: `${api.userPre}/rev_r_dir`,
    method: 'POST',
    headers: {'Content-Type': 'application/json'},
    data: data
  })
}

export function deleteEntityAPI (data) {
  return axios({
    url: `${api.userPre}/admin/del_e`,
    method: 'POST',
    headers: {'Content-Type': 'application/json'},
    data: data
  })
}

export function deleteRelationAPI (data) {
  return axios({
    url: `${api.userPre}/admin/del_r`,
    method: 'POST',
    headers: {'Content-Type': 'application/json'},
    data: data
  })
}

export function uploadLocationAPI (data) {
  return axios({
    url: `${api.userPre}/admin/upd_loc`,
    method: 'POST',
    headers: {'Content-Type': 'application/json'},
    data: data
  })
}

export function getPropsByIdAPI (data) {
  return axios({
    url: `${api.userPre}/admin/get_props`,
    method: 'GET',
    params: {
      id: data
    }
  })
}

export function updateEntityPropsByIdAPI (data) {
  return axios({
    url: `${api.userPre}/admin/upd_en_props`,
    method: 'POST',
    headers: {'Content-Type': 'application/json'},
    data: data
  })
}

export function updateRelationPropsByIdAPI (data) {
  return axios({
    url: `${api.userPre}/admin/upd_re_props`,
    method: 'POST',
    headers: {'Content-Type': 'application/json'},
    data: data
  })
}

export function getLabelStatisticsAPI () {
  return axios({
    url: `${api.userPre}/get_distinct_e_labels`,
    method: 'GET'
  })
}

export function searchEntityByNameAPI (data) {
  return axios({
    url: `${api.userPre}/searchEntityByName`,
    method: `GET`,
    params: {name: data}
  })
}

export function searchEntityByLabelAPI (data) {
  return axios({
    url: `${api.userPre}/getByLabel`,
    method: `GET`,
    params: {labels: data}
  })
}

export function searchRelationByNameAPI (data) {
  return axios({
    url: `${api.userPre}/searchRelationByName`,
    method: `GET`,
    params: {name: data}
  })
}

export function getDistinctLabelsAPI (data) {
  return axios({
    url: `${api.userPre}/get_distinct_e_labels`,
    method: `GET`
  })
}

export function getDistinctRelationsAPI (data) {
  return axios({
    url: `${api.userPre}/get_distinct_r_names`,
    method: `GET`
  })
}

export function getTreeByIdAPI (id, depth) {
  return axios({
    url: `${api.userPre}/get_trees`,
    method: 'GET',
    params: {
      id: id,
      depth: depth
    }
  })
}

export function verifyInformationAPI (data) {
  return axios({
    url: `${api.userPre}/user/login`,
    method: 'POST',
    headers: {'Content-Type': 'application/json'},
    data: data
  })
}
// 登陆验证接口，返回一个json，分为type(user/administrator)和id,验证失败的话type返回一个空字符串

export function addAccountAPI (data) {
  return axios({
    url: `${api.userPre}/user/register`,
    method: 'POST',
    headers: {'Content-Type': 'application/json'},
    data: data
  })
}
// 注册接口，返回一个Boolean

export function getRecommendAPI (data) {
  return axios({
    url: `${api.userPre}/graph/intelligence_search`,
    method: 'POST',
    headers: {'Content-Type': 'application/json'},
    data: {
      label: 'Recipe',
      props: {
        '口味': data.taste,
        '工艺': data.tech,
        '类型': data.type
      }
    }
  })
}

export function getCollectionAPI (data) {
  return axios({
    url: `${api.userPre}/user/getCollection`,
    method: 'GET',
    params: {
      id: data
    }
  })
}

export function getRelativeRelationAPI (data) {
  return axios({
    url: `${api.userPre}/graph/get_relative_r`,
    method: 'GET',
    params: {
      id: data
    }
  })
  // 返回值为json，分为collection和tagRecommend,分别是：收藏和根据tag推荐
}

export function modifyFavorAPI (data) {
  return axios({
    url: `${api.userPre}/user/modifyFavor`,
    method: 'POST',
    headers: {'Content-Type': 'application/json'},
    data: data
  })
}

export function modifyPasswordAPI (data) {
  return axios({
    url: `${api.userPre}/user/modifyPassword`,
    method: 'POST',
    headers: {'Content-Type': 'application/json'},
    data: data
  })
}

export function modifyUserInfoAPI (data) {
  return axios({
    url: `${api.userPre}/user/modifyUserInfo`,
    method: 'POST',
    headers: {'Content-Type': 'application/json'},
    data: data
  })
}

export function getDistinctPropsValueAPI (data) {
  return axios({
    url: `${api.userPre}/graph/get_distinct_props_val`,
    method: 'GET',
    params: {
      label: data.label,
      propName: data.propName
    }
  })
}

export function intelligenceSearchAPI (data) {
  return axios({
    url: `${api.userPre}/graph/intelligence_search`,
    method: 'POST',
    headers: {'Content-Type': 'application/json'},
    data: data
  })
}

export function collectAPI (data) {
  return axios({
    url: `${api.userPre}/user/collect`,
    method: 'POST',
    headers: {'Content-Type': 'application/json'},
    params: {
      id: data.id,
      entity: data.entity,
      name: data.name
    }
  })
}

export function cookWhatAPI (data) {
  return axios({
    url: `${api.userPre}/graph/cook_what`,
    method: 'POST',
    headers: {'Content-Type': 'application/json'},
    data: data
  })
}

export function uncollectAPI (data) {
  return axios({
    url: `${api.userPre}/user/deleteCollection`,
    method: 'POST',
    headers: {'Content-Type': 'application/json'},
    params: {
      id: data.id,
      entity: data.entity
    }
  })
}

export function getAnswerAPI (data) {
  return axios({
    url: `${api.userPre}/graph/query`,
    method: 'GET',
    params: {
      question: data
    }
  })
}
