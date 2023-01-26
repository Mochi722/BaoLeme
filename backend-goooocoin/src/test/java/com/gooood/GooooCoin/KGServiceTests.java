package com.gooood.GooooCoin;

import com.google.inject.internal.util.Lists;
import com.google.inject.internal.util.Maps;
import com.gooood.GooooCoin.VO.ResponseVO;
import com.gooood.GooooCoin.data.neo4jData.Neo4jData;
import com.gooood.GooooCoin.service.graphService.blImpl.GraphServiceImpl;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.*;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class KGServiceTests {

    @InjectMocks
    GraphServiceImpl graphService;
    @Mock
    Neo4jData neo4jData;


    @Test
    public void getRelativeRelation(){
        List<Map<String ,Object>> mapList = new ArrayList<>();
        when(neo4jData.getRelativeRelation(1)).thenReturn(mapList);
        ResponseVO result = graphService.getRelativeRelation(1);
        Assert.assertTrue(result.isSuccess());
    }

    @Test
    public void addEntity(){
        List<String> labels=new ArrayList<>();
        Map<String,Object> props=new HashMap<>();
        when(neo4jData.insertEntity(labels,props)).thenReturn((long)1);
        ResponseVO result = graphService.addEntity(labels,props);
        Assert.assertEquals((long)result.getContent(),1);
    }

    @Test
    public void addRelation(){
        Map<String,Object> props=new HashMap<>();
        when(neo4jData.insertRelation("name",1,2,props)).thenReturn((long)1);
        ResponseVO result = graphService.addRelation("name",1,2,props);
        Assert.assertEquals((long)result.getContent(),1);
    }



    @Test
    public void updateRelationName(){
        when(neo4jData.updateRelationName(1,"name")).thenReturn((long)2);
        ResponseVO result = graphService.updateRelationName(1,"name");
        Assert.assertEquals((long)result.getContent(),2);
    }

    @Test
    public void deleteEntity(){
        ResponseVO result = graphService.deleteEntity(1);
        verify(neo4jData, times(1)).delEntity(1);
        Assert.assertTrue(result.isSuccess());
    }

    @Test
    public void deleteRelation(){
        ResponseVO result = graphService.deleteRelation(1);
        verify(neo4jData, times(1)).delRelation(1);
        Assert.assertTrue(result.isSuccess());
    }

    @Test
    public void getByLabels(){
        List<String> labels=new LinkedList<>();
        when(neo4jData.getByLabels(labels)).thenReturn(Lists.newArrayList());
        ResponseVO result = graphService.getByLabels(labels);
        Assert.assertTrue(result.isSuccess());
    }



    @Test
    public void updateEntityProperty(){
        Map<String,Object> props=Maps.newHashMap();
        when(neo4jData.updateEntityProperty(1,props)).thenReturn(Maps.newHashMap());
        ResponseVO result = graphService.updateEntityProperty(1,props);
        Assert.assertTrue(result.isSuccess());
    }

    @Test
    public void updateRelationProperty(){
        Map<String,Object> properties=new HashMap<>();
        when(neo4jData.updateRelationProperty(1,properties)).thenReturn(Maps.newHashMap());
        ResponseVO result = graphService.updateRelationProperty(1,properties);
        Assert.assertTrue(result.isSuccess());
    }

    @Test
    public void getDistinctLabels(){
        List<Map<String, Object>> labels=new ArrayList<>();
        when(neo4jData.getDistinctLabels()).thenReturn(labels);
        ResponseVO result = graphService.getDistinctLabels();
        Assert.assertTrue(result.isSuccess());
    }

    @Test
    public void getDistinctRelationNames(){
        List<Map<String, Object>> relation=new ArrayList<>();
        when(neo4jData.getDistinctRelationNames()).thenReturn(relation);
        ResponseVO result = graphService.getDistinctRelationNames();
        Assert.assertTrue(result.isSuccess());
    }

    @Test
    public void getDistinctPropValues(){
        List<Map<String, Object>> res=new ArrayList<>();
        when(neo4jData.getDistinctPropValues("label","prop")).thenReturn(res);
        ResponseVO result = graphService.getDistinctPropValues("label","prop");
        Assert.assertTrue(result.isSuccess());
    }



}
