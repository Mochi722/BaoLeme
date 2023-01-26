package com.gooood.GooooCoin.service.graphService.blImpl;

import com.google.inject.internal.util.Sets;
import com.gooood.GooooCoin.data.mysqlData.CollectionMapper;
import com.gooood.GooooCoin.data.neo4jData.Neo4jData;
import com.gooood.GooooCoin.PO.Entity;
import com.gooood.GooooCoin.PO.Relation;
import com.gooood.GooooCoin.VO.ResponseVO;
import com.gooood.GooooCoin.VO.TreeVO;
import com.gooood.GooooCoin.service.graphService.bl.GraphService;
import com.gooood.GooooCoin.util.TransformUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class GraphServiceImpl implements GraphService {

    private final Neo4jData neo4jData;
    private final CollectionMapper collectionMapper;

    @Autowired
    GraphServiceImpl(Neo4jData neo4jData, CollectionMapper collectionMapper) {
        this.neo4jData = neo4jData;
        this.collectionMapper = collectionMapper;
    }

    private String getMethodName() {
        StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
        StackTraceElement e = stacktrace[2];
        return e.getMethodName();
    }
//
//    /**
//     * 上传图谱，file流，json格式
//     */
//    @Override
//    public ResponseVO importJsonFile(File file) {
//        try {
//            neo4jData.clearAll();
//            FileReader reader = new FileReader(file);
//            BufferedReader bReader = new BufferedReader(reader);//new一个BufferedReader对象，将文件内容读取到缓存
//            StringBuilder sb = new StringBuilder();//定义一个字符串缓存，将字符串存放缓存中
//            String s = "";
//            while ((s = bReader.readLine()) != null) {//逐行读取文件内容，不读取换行符和末尾的空格
//                sb.append(s + "\n");//将读取的字符串添加换行符后累加存放在缓存中
//                System.out.println(s);
//            }
//            bReader.close();
//            String str = sb.toString();
//            return importJson(str);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return ResponseVO.builder().success(false).message("Failed in :" + getMethodName()).build();
//        }
//    }
//
//    /**
//     * 上传图谱
//     *
//     * @param data：json格式字符串
//     * @return 所有图谱getAll
//     */
//    @Override
//    public ResponseVO importJson(String data) {
//        try {
//            neo4jData.clearAll();
//            JSONObject jsonObject = JSON.parseObject(data);
//            Map<String, Long> name_id = new HashMap<>();
//            for (Object o : jsonObject.getObject("nodes", List.class)) {
//                System.out.println(o);
//                Map<String, Object> map = (Map<String, Object>) o;
//                List<String> labels = (List<String>) map.get("labels");
//                Map<String, Object> properties = (Map<String, Object>) map.get("properties");
//                long id = neo4jData.insertEntity(labels, properties);
//                name_id.put(properties.get("name").toString(), id);
//            }
//            for (Object o : jsonObject.getObject("links", List.class)) {
//                Map<String, Object> map = (Map<String, Object>) o;
//                long source = name_id.get(map.get("source").toString());
//                long target = name_id.get(map.get("target").toString());
//                neo4jData.insertRelation(map.get("name").toString(), source, target, (Map<String, Object>) map.get("properties"));
//            }
//            return getAll();
//        } catch (Exception e) {
//            e.printStackTrace();
//            return ResponseVO.builder().success(false).message("Failed in :" + getMethodName()).build();
//        }
//    }
//
//    /**
//     * 获得全部图谱
//     *
//     * @return EntityList+RelationList
//     */
//    @Override
//    public ResponseVO getAll() {
//        List<Map<String, Object>> relations;
//        List<Map<String, Object>> entities;
//        try {
//            relations = neo4jData.getAllRelations();
//            entities = neo4jData.getAllEntities();
//            Map<String, List<Map<String, Object>>> map = new HashMap<>();
//            map.put("relations", relations);
//            map.put("entities", entities);
//            return ResponseVO.builder().success(true).message("Success in :" + getMethodName()).content(map).build();
//        } catch (Exception e) {
//            e.printStackTrace();
//            return ResponseVO.builder().success(false).message("Failed in :" + getMethodName()).build();
//        }
//    }

    /**
     * 获得当前节点相关的关系
     *
     * @param id：id
     * @return RelationList
     */
    @Override
    public ResponseVO getRelativeRelation(long id) {
        Map<String ,Object> map=new HashMap<>();
        List<Map<String, Object>> relations;
        Set<Map<String,Object>> entities= Sets.newHashSet();
        try {
            relations = neo4jData.getRelativeRelation(id);
            for(Map<String,Object>r:relations){
                long source=Long.parseLong(r.get("source").toString());
                long target=Long.parseLong(r.get("target").toString());
                long entity_id=source==id?target:source;
                Map<String,Object> entity=neo4jData.searchEntityById(entity_id);
                entities.add(entity);
            }
            entities.add(neo4jData.searchEntityById(id));
            map.put("entity",entities);
            map.put("relation",relations);
            return ResponseVO.builder().success(true).message("Success in :" + getMethodName()).content(map).build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseVO.builder().success(false).message("Failed in :" + getMethodName()).build();
        }
    }

    /**
     * 添加实体
     *
     * @return Entity
     */
    @Override
    public ResponseVO addEntity(List<String> labels, Map<String, Object> properties) {
        try {
            long newId = neo4jData.insertEntity(labels, properties);
            return ResponseVO.builder().success(true).message("Success in :" + getMethodName()).content(newId).build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseVO.builder().success(false).message("Failed in :" + getMethodName()).build();
        }
    }

    /**
     * 添加关系
     *
     * @return Relation
     */
    @Override
    public ResponseVO addRelation(String name, long source, long target, Map<String, Object> properties) {
        try {
            long newId = neo4jData.insertRelation(name, source, target, properties);
            return ResponseVO.builder().success(true).message("Success in :" + getMethodName()).content(newId).build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseVO.builder().success(false).message("Failed in :" + getMethodName()).build();
        }
    }


    /**
     * 关系改名，修改type，更新关系id(*)
     *
     * @return Relation
     */
    @Override
    public ResponseVO updateRelationName(long id, String name) {
        try {
            long newId = neo4jData.updateRelationName(id, name);
            return ResponseVO.builder().success(true).message("Success in :" + getMethodName()).content(newId).build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseVO.builder().success(false).message("Failed in :" + getMethodName()).build();
        }
    }

    /**
     * 删除节点，以及节点相关的关系(*)
     *
     * @return null
     */
    @Override
    public ResponseVO deleteEntity(long id) {
        try {
            neo4jData.delEntity(id);
            collectionMapper.invalidCollection(id);
            return ResponseVO.builder().success(true).message("Success in :" + getMethodName()).build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseVO.builder().success(false).message("Failed in :" + getMethodName()).build();
        }
    }

    /**
     * 删除关系
     *
     * @return null
     */
    @Override
    public ResponseVO deleteRelation(long id) {
        try {
            neo4jData.delRelation(id);
            return ResponseVO.builder().success(true).message("Success in :" + getMethodName()).build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseVO.builder().success(false).message("Failed in :" + getMethodName()).build();
        }
    }

    /**
     * 根据label获得节点
     *
     * @param labels：labels(*),列表
     * @return EntityIdList
     */
    @Override
    public ResponseVO getByLabels(List<String> labels) {
        try {
            List<Map<String,Object>> entities = neo4jData.getByLabels(labels);
            return ResponseVO.builder().success(true).message("Success in :" + getMethodName()).content(entities).build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseVO.builder().success(false).message("Failed in :" + getMethodName()).build();
        }
    }

    /**
     * 更新节点属性
     *
     * @return Entity
     */
    @Override
    public ResponseVO updateEntityProperty(long id, Map<String, Object> properties) {
        try {
            Map<String, Object> entity = neo4jData.updateEntityProperty(id, properties);
            return ResponseVO.builder().success(true).message("Success in :" + getMethodName()).content(entity).build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseVO.builder().success(false).message("Failed in :" + getMethodName()).build();
        }
    }

    /**
     * 更新关系属性
     *
     * @return Relation
     */
    @Override
    public ResponseVO updateRelationProperty(long id, Map<String, Object> properties) {
        try {
            Map<String, Object> relation = neo4jData.updateRelationProperty(id, properties);
            return ResponseVO.builder().success(true).message("Success in :" + getMethodName()).content(relation).build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseVO.builder().success(false).message("Failed in :" + getMethodName()).build();
        }
    }

    /**
     * 获得指定类型节点、指定属性的所有值及统计数
     *
     * @param label
     * @param propName
     * @return Map(value, count)
     */
    @Override
    public ResponseVO getDistinctPropValues(String label, String propName) {
        try {
            List<Map<String, Object>> propValues = neo4jData.getDistinctPropValues(label, propName);
            return ResponseVO.builder().success(true).message("Success in :" + getMethodName()).content(propValues).build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseVO.builder().success(false).message("Failed in :" + getMethodName()).build();
        }
    }

    /**
     * 获得所有标签和统计数
     *
     * @return Map(label, count)
     */
    @Override
    public ResponseVO getDistinctLabels() {
        try {
            List<Map<String, Object>> labels = neo4jData.getDistinctLabels();
            return ResponseVO.builder().success(true).message("Success in :" + getMethodName()).content(labels).build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseVO.builder().success(false).message("Failed in :" + getMethodName()).build();
        }
    }

    /**
     * 获得所有关系类型和统计数
     *
     * @return Map(name, count)
     */
    @Override
    public ResponseVO getDistinctRelationNames() {
        try {
            List<Map<String, Object>> relationNames = neo4jData.getDistinctRelationNames();
            return ResponseVO.builder().success(true).message("Success in :" + getMethodName()).content(relationNames).build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseVO.builder().success(false).message("Failed in :" + getMethodName()).build();
        }
    }



    @Override
    public ResponseVO getTree(long id, int depth) {
        try {
            Entity root = TransformUtil.Map2Bean(neo4jData.searchEntityById(id), Entity.class);
            System.out.println(root);
            TreeVO treeVO = TreeVO.builder()
                    .id(id)
                    .name(root.getProperties().get("name").toString())
                    .relation("")
                    .children(new ArrayList<>())
                    .depth(0)
                    .build();

            Queue<TreeVO> queue = new LinkedList<>();
            queue.add(treeVO);
            TreeVO treePointer;
            while (!queue.isEmpty()) {
                treePointer = queue.poll();
                if (treePointer.getDepth() == depth) continue;
                List<Map<String, Object>> relationList = neo4jData.getRelativeRelation(treePointer.getId());
                for (Map<String, Object> relationMap : relationList) {
                    Relation relation = TransformUtil.Map2Bean(relationMap, Relation.class);
                    Entity target = TransformUtil.Map2Bean(neo4jData.searchEntityById(relation.getTarget()), Entity.class);
                    TreeVO t = TreeVO.builder()
                            .id(relation.getTarget())
                            .name(target.getProperties().get("name").toString())
                            .relation(relation.getName())
                            .children(new ArrayList<>())
                            .depth(treePointer.getDepth() + 1)
                            .build();
                    treePointer.addChild(t);
                    queue.add(t);
                }
            }
            return ResponseVO.builder().success(true).message("Success in :" + getMethodName()).content(treeVO).build();

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseVO.builder().success(false).message("Failed in :" + getMethodName()).build();
        }
    }
}
