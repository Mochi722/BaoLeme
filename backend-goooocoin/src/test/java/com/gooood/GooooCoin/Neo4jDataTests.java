package com.gooood.GooooCoin;

import com.gooood.GooooCoin.PO.Entity;
import com.gooood.GooooCoin.PO.Relation;
import com.gooood.GooooCoin.data.neo4jData.Neo4jData;
import com.gooood.GooooCoin.util.Neo4jUtil;
import com.gooood.GooooCoin.util.TransformUtil;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;
import java.util.List;

@EnableAutoConfiguration
@RunWith(SpringRunner.class)
@SpringBootTest
public class Neo4jDataTests {
    @Autowired
    Neo4jUtil neo4jUtil;

    @Autowired
    Neo4jData neo4jData;

    @Test
    public void getRelativeRelation_updateRelationName(){
        long id1=neo4jData.insertEntity(Collections.singletonList("_TEST_"),null);
        long id2=neo4jData.insertEntity(Collections.singletonList("_TEST_"),null);
        long id3=neo4jData.insertRelation("_TEST_RELATION_",id1,id2,null);
        List<Map<String,Object>> relations= neo4jData.getRelativeRelation(id1);
        Assert.assertEquals(1, relations.size());
        Assert.assertEquals("_TEST_RELATION_",relations.get(0).get("name"));
        long id4=neo4jData.updateRelationName(id3,"_TEST_RELATION_1");
        relations=neo4jData.getRelativeRelation(id1);
        Assert.assertEquals("_TEST_RELATION_1",relations.get(0).get("name"));
        neo4jData.delRelation(id4);
        neo4jData.delEntity(id1);
        neo4jData.delEntity(id2);
    }


    @Test
    public void search_Entity(){
        long id1=neo4jData.insertEntity(Collections.singletonList("_TEST_"),null);
        Entity e = TransformUtil.Map2Bean(neo4jData.searchEntityById(id1),Entity.class);
        Assert.assertEquals("_TEST_", e.getLabels().get(0));
        neo4jData.delEntity(id1);
    }



    @Test
    public  void getByLabels(){
        long id1=neo4jData.insertEntity(Collections.singletonList("_TEST_"),null);
        long id2=neo4jData.insertEntity(Collections.singletonList("_TEST_"),null);
        Assert.assertEquals(neo4jData.getByLabels(Collections.singletonList("_TEST_")).size(),2);
        neo4jData.delEntity(id1);
        neo4jData.delEntity(id2);
    }

    @Test
    public void updateEntityProperty(){
        long id1=neo4jData.insertEntity(null,Collections.singletonMap("name","TEST1"));
        Map<String,Object> map=neo4jData.updateEntityProperty(id1,Collections.singletonMap("name","TEST2"));
        Assert.assertEquals(((Map<String,Object>) map.get("properties")).get("name").toString(),"TEST2");
        neo4jData.delEntity(id1);
    }

    @Test
    public void updateRelationProperty(){
        long id1=neo4jData.insertEntity(Collections.singletonList("_TEST_"),null);
        long id2=neo4jData.insertEntity(Collections.singletonList("_TEST_"),null);
        long id3=neo4jData.insertRelation("_TEST_RELATION_",id1,id2,Collections.singletonMap("prop","p1"));
        Map<String,Object> map=neo4jData.updateRelationProperty(id3,Collections.singletonMap("prop","p2"));
        Assert.assertEquals("p2", ((Map<String,Object>) map.get("properties")).get("prop").toString());
        neo4jData.delEntity(id1);
        neo4jData.delEntity(id2);
    }

    @Test
    public void getDistinctPropValues(){
        long id1=neo4jData.insertEntity(Collections.singletonList("_TEST_"),Collections.singletonMap("name","TEST1"));
        long id2=neo4jData.insertEntity(Collections.singletonList("_TEST_"),Collections.singletonMap("name","TEST2"));
        long id3=neo4jData.insertEntity(Collections.singletonList("_TEST_"),Collections.singletonMap("name","TEST3"));
        long id4=neo4jData.insertEntity(Collections.singletonList("_TEST_"),Collections.singletonMap("name","TEST1"));
        List<Map<String, Object>> res=neo4jData.getDistinctPropValues("_TEST_","name");
        Assert.assertEquals(res.size(),3);
        neo4jData.delEntity(id1);
        neo4jData.delEntity(id2);
        neo4jData.delEntity(id3);
        neo4jData.delEntity(id4);
    }

    @Test
    public void getDistinctLabels(){
        Assert.assertEquals(2,neo4jData.getDistinctLabels().size());
    }

    @Test
    public void getDistinctRelationNames(){
        Assert.assertEquals(2,neo4jData.getDistinctRelationNames().size());
    }


}