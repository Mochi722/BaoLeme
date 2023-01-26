package com.gooood.GooooCoin.data.neo4jData;

import com.gooood.GooooCoin.util.Neo4jUtil;
import com.gooood.GooooCoin.util.TransformUtil;
import org.neo4j.driver.v1.Record;
import org.neo4j.driver.v1.StatementResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Repository
public class Neo4jDataImpl implements Neo4jData {

    private final Neo4jUtil neo4jUtil;

    @Autowired
    public Neo4jDataImpl(Neo4jUtil neo4jUtil) {
        this.neo4jUtil = neo4jUtil;
    }

    /**
     * 更新properties字段时动态构建查询语句
     *
     * @param alias 查询语句中实体或关系的别名，如e,r
     * @param map   需要更新的properties表单
     * @return 查询语句："SET alias.propertyName1=property1, alias.propertyName2=property2"
     */
    private String updateProperty(String alias, Map<String, Object> map) {
        if (null == map || map.isEmpty()) return "";
        StringBuilder setProperty = new StringBuilder(" SET ");
        for (String key : map.keySet()) {
            setProperty.append(alias).append('.');

            String value = map.get(key).toString();

            Pattern pattern = Pattern.compile("-?[0-9]+.?[0-9]+");
            Matcher isNum = pattern.matcher(value);

            if (isNum.matches()) setProperty.append(key).append("=").append(value).append(",");
            else setProperty.append(key).append("='").append(value).append("',");
        }
        setProperty.deleteCharAt(setProperty.length() - 1);
        return setProperty.toString();
    }


    /**
     * 设置实体多个label时,动态构建查询语句
     *
     * @param labels label列表
     * @return 查询语句：" :label1 :label2 :label3"
     */
    private String getLabel(List<String> labels) {
        if (null == labels) return "";
        StringBuilder labelPart = new StringBuilder(" :");
        for (String label : labels) {
            labelPart.append(label).append(":");
        }
        // 如果for为空，去掉：
        // 如果for不为空，去掉末尾的：
        return labelPart.deleteCharAt(labelPart.length() - 1).toString();
    }

    /**
     * 设置实体或关系多个用户自定义property时，动态构建查询语句
     *
     * @param properties 属性名-属性值map
     * @return 查询语句：" { propertyName1:property1, propertyName2:property2 } "
     */
    private String getProperty(Map<String, Object> properties) {
        if (null == properties) return "";
        StringBuilder propertyPart = new StringBuilder(" {");
        for (String key : properties.keySet()) {
            String value = properties.get(key).toString();

            //判断是否为数值，如果非数值需要加引号 "
            Pattern pattern = Pattern.compile("-?[0-9]+.?[0-9]+");
            Matcher isNum = pattern.matcher(value);

            if (isNum.matches()) propertyPart.append(key).append(":").append(value).append(",");
            else propertyPart.append(key).append(":'").append(value).append("',");
        }
        // 去掉末尾的，
        // 如果for为空则去掉 {
        propertyPart.deleteCharAt(propertyPart.length() - 1);
        // 如果for为空，length为 1
        // 如果for不为空，加上 }
        if (propertyPart.length() > 1) propertyPart.append("}");
        return propertyPart.toString();
    }

//    @Override
//    public void clearAll() {
//        try {
//            neo4jUtil.executeCypherSql("MATCH ()-[r]-() DELETE r");
//            neo4jUtil.executeCypherSql("MATCH (N) DELETE N");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

//    @Override
//    public List<Map<String, Object>> getAllRelations() {
//        List<Map<String, Object>> relationList = new ArrayList<>();
//        try {
//            String sql = "MATCH (e1)-[r]->(e2) RETURN id(e1) as source,id(e2) as target,type(r) as name,id(r) as id,properties(r) as properties";
//            StatementResult res = neo4jUtil.executeCypherSql(sql);
//            while (res.hasNext()) {
//                Record record = res.next();
//                relationList.add(TransformUtil.D3propsFilter(record.asMap()));
//            }
//            return relationList;
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw e;
//        }
//    }
//
//    @Override
//    public List<Map<String, Object>> getAllEntities() {
//        List<Map<String, Object>> entityList = new ArrayList<>();
//        try {
//            String sql = "MATCH (e) RETURN id(e) as id,properties(e) as properties,labels(e) as labels";
//            StatementResult res = neo4jUtil.executeCypherSql(sql);
//            while (res.hasNext()) {
//                Record record = res.next();
//                entityList.add(TransformUtil.D3propsFilter(record.asMap()));
//            }
//            return entityList;
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw e;
//        }
//    }

    @Override
    public List<Map<String, Object>> getRelativeRelation(long id) {
        List<Map<String, Object>> relationList = new ArrayList<>();
        try {
            String sql = "MATCH (e1)-[r]->(e2) WHERE id(%s)=%d RETURN id(e1) as source,type(r) as name,id(e2) as target,id(r) as id,properties(r) as properties limit 25";
            String sql1 = String.format(sql,"e1",id);
            StatementResult res = neo4jUtil.executeCypherSql(sql1);
            while (res.hasNext()) {
                Record record = res.next();
                relationList.add(TransformUtil.D3propsFilter(record.asMap()));
            }
            String sql2 = String.format(sql,"e2",id);
            res = neo4jUtil.executeCypherSql(sql2);
            while (res.hasNext()) {
                Record record = res.next();
                relationList.add(TransformUtil.D3propsFilter(record.asMap()));
            }
            return relationList;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public long insertEntity(List<String> labels, Map<String, Object> properties) {
        try {
            String labelPart = getLabel(labels);
            String propertyPart = getProperty(properties);

            String sql = "CREATE (e" + labelPart + propertyPart + ") RETURN id(e)";
            System.out.println(sql);
            StatementResult res = neo4jUtil.executeCypherSql(sql);
            return res.single().get("id(e)").asLong();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }


    @Override
    public long insertRelation(String name, long source, long target, Map<String, Object> properties) {
        try {
            String propertyPart = getProperty(properties);
            String sql = "MATCH (e1),(e2) WHERE id(e1)=%d AND id(e2)=%d"
                    + " CREATE (e1)-[r:%s"
                    + propertyPart
                    + "]->(e2) RETURN id(r)";
            sql = String.format(sql, source, target, name);
            StatementResult res = neo4jUtil.executeCypherSql(sql);
            return res.single().get("id(r)").asLong();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public Map<String, Object> searchEntityById(long id) {
        try {
            String sql = "MATCH (e) WHERE id(e)=%d RETURN id(e) as id,properties(e) as properties, labels(e) as labels";
            sql = String.format(sql, id);

            StatementResult res = neo4jUtil.executeCypherSql(sql);
            Record record = res.single();
            return TransformUtil.D3propsFilter(record.asMap());
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }


    @Override
    public long updateRelationName(long id, String newName) {
        try {
            String sql = "MATCH (e1)-[r1]->(e2) WHERE id(r1)=%d"
                    + " CREATE (e1)-[r2:%s]->(e2) SET r2=r1 DELETE r1 RETURN id(r2)";
            sql = String.format(sql, id, newName);
            StatementResult res = neo4jUtil.executeCypherSql(sql);
            return res.single().get("id(r2)").asLong();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public void delEntity(long id) {
        try {
            String sql1 = String.format("MATCH (e)-[r]-() WHERE id(e)=%d DELETE r", id);
            String sql2 = String.format("MATCH (e) WHERE id(e)=%d DELETE e", id);
            neo4jUtil.executeCypherSql(sql1);
            neo4jUtil.executeCypherSql(sql2);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

    }

    @Override
    public void delRelation(long id) {
        try {
            String sql = String.format("MATCH ()-[r]->() WHERE id(r)=%d DELETE r", id);
            StatementResult res = neo4jUtil.executeCypherSql(sql);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

    }

    @Override
    public List<Map<String, Object>> getByLabels(List<String> labels) {
        try {
            String labelPart = getLabel(labels);
            List<Map<String, Object>> list = new ArrayList<>();
            String sql = ("MATCH (e" + labelPart + ") RETURN id(e) as id,properties(e) as properties, labels(e) as labels limit 50");
            StatementResult res = neo4jUtil.executeCypherSql(sql);
            while (res.hasNext()) {
                Record record = res.next();
                list.add(TransformUtil.D3propsFilter(record.asMap()));
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public List<Map<String, Object>> intelligenceSearch(String key, String alias) {
        List<Map<String, Object>> list = new ArrayList<>();
        try {
            if(key==null) key="";
            else key = "WHERE e.name contains '" + key + "'";
            String sql = "MATCH " +
                    alias + key +
                    " RETURN id(e) as id,properties(e) as properties, labels(e) as labels limit 10";
            StatementResult res = neo4jUtil.executeCypherSql(sql);
            while (res.hasNext()) {
                Record record = res.next();
                list.add(TransformUtil.D3propsFilter(record.asMap()));
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public List<Map<String, Object>> intelligenceSearchRelativeRecipe(long id, String alia) {
        List<Map<String, Object>> list = new ArrayList<>();
        try{
            String sql = "MATCH " +
                    alia +
                    "-[r]-(e1) WHERE id(e1)=%d" +
                    " RETURN id(e) as id,properties(e) as properties, labels(e) as labels, type(r) as type limit 10";
            sql=String.format(sql,id);
            StatementResult res = neo4jUtil.executeCypherSql(sql);
            while (res.hasNext()) {
                Record record = res.next();
                list.add(TransformUtil.D3propsFilter(record.asMap()));
            }
            return list;
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }

//    @Override
//    public List<Map<String, Object>> fuzzySearchRelation(String name) {
//        List<Map<String,Object>> list = new ArrayList<>();
//        try {
//            String sql = String.format("MATCH ()-[r]->() where type(r) contains '%s' return id(e) as id,properties(e) as properties, labels(e) as labels limit 50", name);
//            StatementResult res = neo4jUtil.executeCypherSql(sql);
//            while (res.hasNext()) {
//                Record record = res.next();
//                list.add(TransformUtil.D3propsFilter(record.asMap()));
//            }
//            return list;
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw e;
//        }
//    }

    @Override
    public Map<String, Object> updateEntityProperty(long id, Map<String, Object> properties) {
        try {
            String setPropertyPart = updateProperty("e", properties);
            String sql = String.format("MATCH (e) WHERE id(e)=%d ", id)
                    + setPropertyPart
                    + " RETURN id(e) as id, labels(e) as labels, properties(e) as properties";
            System.out.println("\n\n\n" + sql + "\n\n\n");
            StatementResult res = neo4jUtil.executeCypherSql(sql);
            Record record = res.single();
            return TransformUtil.D3propsFilter(record.asMap());
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public Map<String, Object> updateRelationProperty(long id, Map<String, Object> properties) {
        try {
            String setPropertyPart = updateProperty("r", properties);
            String sql = "MATCH (e1)-[r]->(e2) WHERE id(r)="
                    + id
                    + setPropertyPart
                    + " RETURN id(e1) as source,type(r) as name,id(e2) as target,id(r) as id,properties(r) as properties";
            StatementResult res = neo4jUtil.executeCypherSql(sql);
            Record record = res.single();
            return TransformUtil.D3propsFilter(record.asMap());
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public List<Map<String, Object>> getDistinctPropValues(String label, String propName) {
        List<Map<String, Object>> list=new ArrayList<>();
        try {
            String sql = String.format("match (e:%s)" +
                    " where e.%s<>'暂未收录'" +
                    " return distinct e.%s as name,count(e.%s) as count" +
                    " order by count(e.%s) desc", label, propName, propName, propName, propName);
            StatementResult res = neo4jUtil.executeCypherSql(sql);
            while (res.hasNext()) {
                Record record = res.next();
                Map<String,Object> map=new HashMap<>();
                map.put("name",record.get("name").asString());
                map.put("count", record.get("count").asInt());
                list.add(map);
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public List<Map<String, Object>> getDistinctLabels() {
        List<Map<String, Object>> list=new ArrayList<>();
        try {
            String sql = "MATCH (n)" +
                    " WITH labels(n) AS labels" +
                    " UNWIND labels AS label" +
                    " RETURN DISTINCT label,count(label) as count" +
                    " ORDER BY count desc";
            StatementResult res = neo4jUtil.executeCypherSql(sql);
            while (res.hasNext()) {
                Record record = res.next();
                Map<String,Object> map=new HashMap<>();
                map.put("label",record.get("label").asString());
                map.put("count", record.get("count").asInt());
                list.add(map);
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public List<Map<String, Object>> getDistinctRelationNames() {
        List<Map<String, Object>> list=new ArrayList<>();
        try {
            String sql = "match (n)-[r]->() return distinct type(r) as name,count(r) as count order by count desc";
            StatementResult res = neo4jUtil.executeCypherSql(sql);
            while (res.hasNext()) {
                Record record = res.next();
                Map<String,Object> map=new HashMap<>();
                map.put("name",record.get("name").asString());
                map.put("count", record.get("count").asInt());
                list.add(map);
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }


}
