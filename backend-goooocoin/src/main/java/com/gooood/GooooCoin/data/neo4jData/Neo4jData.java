package com.gooood.GooooCoin.data.neo4jData;

import java.util.List;
import java.util.Map;

public interface Neo4jData {

    List<Map<String, Object>> getRelativeRelation(long id);

    long insertEntity(List<String> labels, Map<String, Object> properties);

    long insertRelation(String name, long source, long target, Map<String, Object> properties);

    Map<String, Object> searchEntityById(long id);

    long updateRelationName(long id, String newName);

    void delEntity(long id);

    void delRelation(long id);

    List<Map<String, Object>> getByLabels(List<String> labels);

    Map<String, Object> updateEntityProperty(long id, Map<String, Object> properties);

    Map<String, Object> updateRelationProperty(long id, Map<String, Object> properties);

    List<Map<String, Object>> getDistinctPropValues(String label,String propName);

    List<Map<String, Object>> getDistinctLabels();

    List<Map<String, Object>> getDistinctRelationNames();

    List<Map<String, Object>> intelligenceSearch(String key, String alia);

    List<Map<String, Object>> intelligenceSearchRelativeRecipe(long id, String alia);

}
