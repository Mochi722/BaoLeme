package com.gooood.GooooCoin.data.mysqlData;

import com.gooood.GooooCoin.PO.User;
import lombok.Data;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface UserMapper {

    @Select("select * from goooocoin.user where id=#{id} ")
    User getUserById(@Param("id") long id);

    @Select("select * from goooocoin.user where phone_number=#{phone} ")
    User getUserByPhone(@Param("phone") String phone);

    @Insert("insert into goooocoin.user (username,password,phone_number) values (#{name},#{pass},#{phone}) ")
    void register(@Param("name") String username, @Param("pass") String password, @Param("phone") String phone);

    @Update("update goooocoin.user set password=#{pass} where id=#{id}")
    void modifyPassword(@Param("id") long id, @Param("pass") String password);

    @Update("update goooocoin.user set flavor=#{flavor},method=#{method},type=#{type} where id=#{id}")
    void modifyFavor(@Param("id") long id, @Param("flavor") String flavor, @Param("method") String method, @Param("type") String type);

    @Update("update goooocoin.user set avatar_address=#{avatarAddress} where id =#{id}")
    void modifyAvatar(@Param("id") long id, @Param("avatarAddress") String avatarAddress);
}
