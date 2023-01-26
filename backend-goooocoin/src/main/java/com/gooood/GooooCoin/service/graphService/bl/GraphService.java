package com.gooood.GooooCoin.service.graphService.bl;

import com.gooood.GooooCoin.VO.EntityVO;
import com.gooood.GooooCoin.VO.RelationVO;
import com.gooood.GooooCoin.VO.ResponseVO;

import java.io.File;
import java.util.List;
import java.util.Map;

public interface GraphService {

//    /**
//     * 上传图谱
//     *
//     * @param data：json格式字符串
//     * @return 所有图谱getAll
//     */
//    ResponseVO importJson(String data);
//
//    /**
//     * 上传图谱，file流，json格式
//     */
//    ResponseVO importJsonFile(File file);
//
//    /**
//     * 获得全部图谱
//     *
//     * @return EntityList+RelationList
//     */
//    ResponseVO getAll();

    /**
     * 获得当前节点相关的关系
     *
     * @param id：id
     * @return RelationList
     */
    ResponseVO getRelativeRelation(long id);

    /**
     * 添加实体
     *
     * @param labels
     * @param properties
     * @return
     */
    ResponseVO addEntity(List<String> labels, Map<String, Object> properties);

    /**
     * 添加关系
     *
     * @param ：name(*),properties
     * @return Relation
     */
    ResponseVO addRelation(String name, long source, long target, Map<String, Object> properties);

    /**
     * 关系改名，修改type，更新关系id(*)
     *
     * @return Relation
     */
    ResponseVO updateRelationName(long id, String name);

    /**
     * 删除节点，以及节点相关的关系(*)
     *
     * @return null
     */
    ResponseVO deleteEntity(long id);

    /**
     * 删除关系
     *
     * @return null
     */
    ResponseVO deleteRelation(long id);

    /**
     * 根据label获得节点
     *
     * @param labels：labels(*)，列表
     * @return EntityList
     */
    ResponseVO getByLabels(List<String> labels);

//    /**
//     * 节点name模糊搜索
//     *
//     * @param name：name(*)
//     * @return EntityList
//     */
//    ResponseVO searchEntityByName(String name);
//
//    /**
//     * 关系name模糊搜索
//     *
//     * @param name name(*)
//     * @return RelationList
//     */
//    ResponseVO searchRelationByName(String name);
//

    /**
     * 更新节点属性
     *
     * @return Entity
     */
    ResponseVO updateEntityProperty(long id, Map<String, Object> properties);

    /**
     * 更新关系属性
     *
     * @return Relation
     */
    ResponseVO updateRelationProperty(long id, Map<String, Object> properties);

    /**
     * 获得指定类型节点、指定属性的所有值及统计数
     *
     * @return Map(value, count)
     */
    ResponseVO getDistinctPropValues(String label, String propName);

    /**
     * 获得所有标签和统计数
     *
     * @return Map(label, count)
     */
    ResponseVO getDistinctLabels();

    /**
     * 获得所有关系类型和统计数
     *
     * @return Map(name, count)
     */
    ResponseVO getDistinctRelationNames();

    /**
     * 获得树结构
     *
     * @return TreeVO
     */
    ResponseVO getTree(long id, int depth);
}
