package com.gooood.GooooCoin.controller.graphController;

import com.gooood.GooooCoin.service.graphService.bl.GraphService;
import com.gooood.GooooCoin.VO.ResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/graph")
public class GraphController {

    private final GraphService graphService;

    @Autowired
    GraphController(GraphService service) {
        this.graphService = service;
    }
//
//    /**
//     * 上传json格式的图谱
//     * <p>
//     * {
//     * "nodes":
//     * [
//     * {"labels":["标签1","标签2"], "properties":{"name": "张伟","job": "前端工程师","age": 66}}
//     * ],
//     * "links":
//     * [
//     * {"source": "牛哥","target": "任同为","name": "哥哥","properties": {}}
//     * ]
//     * }
//     *
//     * @param data json格式字符串
//     * @return null
//     */
//    @PostMapping("/import_json")
//    public ResponseVO importJson(String data) {
//        return graphService.importJson(data);
//    }
//
//    /**
//     * 上传file流格式图谱
//     *
//     * @param file 文件流
//     * @return null
//     */
//    @PostMapping("/import_json_file")
//    public ResponseVO importJsonFile(File file) {
//        return graphService.importJsonFile(file);
//    }

//    /**
//     * 获取所有图谱
//     *
//     * @return Map{"entities":[],   "relations":[]}
//     */
//    @GetMapping("/get_all")
//    public ResponseVO getAll() {
//        return graphService.getAll();
//    }

    /**
     * 获取实体为source的所有关系
     *
     * @param id id(*)
     * @return RelationList
     */
    @GetMapping("/get_relative_r")
    public ResponseVO getRelativeRelation(HttpServletRequest request, @RequestParam long id) {
        return graphService.getRelativeRelation(id);
    }

    /**
     * 根据label获得节点
     *
     * @param labels：labels(*)，列表
     * @return EntityList
     */
    @GetMapping("/getByLabel")
    public ResponseVO getByLabel(@RequestParam(value = "labels") List<String> labels) {
        return graphService.getByLabels(labels);
    }

    /**
     * 获得所有标签和统计数
     *
     * @return Map(label, count)
     */
    @GetMapping("/get_distinct_e_labels")
    public ResponseVO getDistinctLabels() {
        return graphService.getDistinctLabels();
    }

    /**
     * 获得所有关系类型和统计数
     *
     * @return Map(name, count)
     */
    @GetMapping("/get_distinct_r_names")
    public ResponseVO getDistinctRelationNames() {
        return graphService.getDistinctRelationNames();
    }

    /**
     * 获得指定label、指定property的所有属性值及其计数
     * @param label
     * @param propName
     * @return Map(propName, count)
     */
    @GetMapping("/get_distinct_props_val")
    public ResponseVO getDistinctPropertyValues(@RequestParam("label")String label,@RequestParam("propName")String propName){
        return graphService.getDistinctPropValues(label,propName);
    }

    /**
     * 由指定root开始，获得指定深度的树结构图谱
     *
     * @param id
     * @param depth
     * @return
     */
    @GetMapping("/get_trees")
    public ResponseVO getTree(@RequestParam long id, @RequestParam(required = false, defaultValue = "3") int depth) {
        return graphService.getTree(id, depth);
    }
}



