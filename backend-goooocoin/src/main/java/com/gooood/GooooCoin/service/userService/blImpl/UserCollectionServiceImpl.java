package com.gooood.GooooCoin.service.userService.blImpl;

import com.gooood.GooooCoin.VO.ResponseVO;
import com.gooood.GooooCoin.data.mysqlData.CollectionMapper;
import com.gooood.GooooCoin.data.neo4jData.Neo4jData;
import com.gooood.GooooCoin.service.userService.bl.UserCollectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class UserCollectionServiceImpl implements UserCollectionService {

    private String getMethodName() {
        StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
        StackTraceElement e = stacktrace[2];
        return e.getMethodName();
    }

    private final CollectionMapper collectionMapper;
    private final Neo4jData neo4jData;

    @Autowired
    public UserCollectionServiceImpl(CollectionMapper collectionMapper, Neo4jData neo4jData) {
        this.collectionMapper = collectionMapper;
        this.neo4jData = neo4jData;
    }


    @Override
    public ResponseVO collect(long id, long entityId, String recipeName) {
        try {
            CollectionMapper.CollectedEntity e=collectionMapper.getCertainCollection(id,entityId);
            if(e==null) {
                collectionMapper.collect(id, entityId, recipeName);
                Map<String, Object> entity = neo4jData.searchEntityById(entityId);
                Map<String, Object> props = ((Map<String, Object>) entity.get("properties"));
                neo4jData.updateEntityProperty(entityId, Collections.singletonMap("heat", Integer.parseInt(props.get("heat").toString()) + 1));
                return ResponseVO.builder().success(true).message("Success in :" + getMethodName()).build();
            }
            else  return ResponseVO.builder().success(false).message("已存在收藏夹！").build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseVO.builder().success(false).message("Failed in :" + getMethodName()).build();
        }
    }


    @Override
    public ResponseVO getCollections(long id) {
        try {
            List<CollectionMapper.CollectedEntity> collectedEntity=collectionMapper.getCollection(id);
            List<Map<String,Object>> res=new ArrayList<>();
            for(CollectionMapper.CollectedEntity e:collectedEntity){
                Map<String,Object> entity=neo4jData.searchEntityById(e.getEntityId());
                res.add(entity);
            }
            return ResponseVO.builder().success(true).content(res).message("Success in :" + getMethodName()).build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseVO.builder().success(false).message("Failed in :" + getMethodName()).build();
        }
    }

    @Override
    public ResponseVO deleteCollection(long id, long entity) {
        try {
            if(id<0)id=-id;
            collectionMapper.deleteCollection(id,entity);
            return ResponseVO.builder().success(true).message("Success in :" + getMethodName()).build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseVO.builder().success(false).message("Failed in :" + getMethodName()).build();
        }
    }

}
