package com.gooood.GooooCoin.controller.graphController;

import com.gooood.GooooCoin.VO.EntityVO;
import com.gooood.GooooCoin.VO.RelationVO;
import com.gooood.GooooCoin.VO.ResponseVO;
import com.gooood.GooooCoin.service.graphService.bl.GraphService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminGraphController {
    private final GraphService graphService;
    @Autowired
    AdminGraphController(GraphService service) {
        this.graphService = service;
    }

    /**
     * 添加实体
     *
     * @param entityVO：labels,properties
     * @return Entity
     */
    @PostMapping("/add_e")
    public ResponseVO addEntity(@RequestBody EntityVO entityVO) {
        return graphService.addEntity(entityVO.getLabels(), entityVO.getProperties());
    }

    /**
     * 添加关系
     *
     * @param relationVO：name(*),properties
     * @return Relation
     */
    @PostMapping("/add_r")
    public ResponseVO addRelation(@RequestBody RelationVO relationVO) {
        return graphService.addRelation(relationVO.getName(), relationVO.getSource(), relationVO.getTarget(), relationVO.getProperties());
    }


    /**
     * 关系改名，修改type，更新关系id(*)
     *
     * @param relationVO：id(*),newName(*)
     * @return Relation
     */
    @PostMapping("/upd_r_name")
    public ResponseVO updateRelationName(@RequestBody RelationVO relationVO) {
        return graphService.updateRelationName(relationVO.getId(), relationVO.getName());
    }

    /**
     * 删除节点，以及节点相关的关系(*)
     *
     * @param entityVO: id(*)
     * @return null
     */
    @PostMapping("/del_e")
    public ResponseVO deleteEntity(@RequestBody EntityVO entityVO) {
        return graphService.deleteEntity(entityVO.getId());
    }

    /**
     * 删除关系
     *
     * @param relationVO：id(*)
     * @return null
     */
    @PostMapping("/del_r")
    public ResponseVO deleteRelation(@RequestBody RelationVO relationVO) {
        return graphService.deleteRelation(relationVO.getId());
    }

    /**
     * 更新节点属性
     *
     * @param entityVO：id(*)，properties(*)
     * @return Entity
     */
    @PostMapping("/upd_en_props")
    public ResponseVO updateEntityProperty(@RequestBody EntityVO entityVO) {
        return graphService.updateEntityProperty(entityVO.getId(), entityVO.getProperties());
    }

    /**
     * 更新关系属性
     *
     * @param relationVO：id(*)，properties(*)
     * @return Relation
     */
    @PostMapping("/upd_re_props")
    public ResponseVO updateRelationProperty(@RequestBody RelationVO relationVO) {
        return graphService.updateRelationProperty(relationVO.getId(), relationVO.getProperties());
    }

    /**
     * 保存更改的页面布局（批量）
     *
     * @param entityVOList [id(*)，properties(*)]
     * @return null
     */
    @PostMapping("/upd_loc")
    public ResponseVO updateLocation(@RequestBody List<EntityVO> entityVOList) {
        ResponseVO res = null;
        for (EntityVO entityVO : entityVOList) {
            res = graphService.updateEntityProperty(entityVO.getId(), entityVO.getProperties());
        }
        return res;
    }

}
