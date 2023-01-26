package com.gooood.GooooCoin.data.mysqlData;

import lombok.Data;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface CollectionMapper {

    @Insert("insert into goooocoin.collection (user_id,entity_id,entity_name) values (#{user},#{id},#{name})")
    void collect(long user, long id, String name );

    @Select("select * from goooocoin.collection where user_id=#{user}")
    List<CollectedEntity> getCollection(long user);

    @Update("update goooocoin.collection set entity_name='已失效' where entity_id=#{id}")
    void invalidCollection(long id);

    @Delete("delete from goooocoin.collection where user_id=#{user} and entity_id=#{id}")
    void deleteCollection(long user,long id);

    @Select("select * from goooocoin.collection where user_id=#{user} and entity_id=#{id}")
    CollectedEntity getCertainCollection(long user,long id);


    @Data
    class CollectedEntity {
        long userId;
        String entityName;
        long entityId;
    }

}
