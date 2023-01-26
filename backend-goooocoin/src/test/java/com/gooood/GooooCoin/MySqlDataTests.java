package com.gooood.GooooCoin;

import com.gooood.GooooCoin.PO.User;
import com.gooood.GooooCoin.data.mysqlData.CollectionMapper;
import com.gooood.GooooCoin.data.mysqlData.UserMapper;
import com.gooood.GooooCoin.util.TransformUtil;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;


@EnableAutoConfiguration
@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class MySqlDataTests {
    @Autowired
    UserMapper userMapper;
    @Autowired
    CollectionMapper collectionMapper;

    @Test
    public void getUserByPhone(){
        User user=userMapper.getUserByPhone("12345678900");
        User admin=userMapper.getUserByPhone("12345678901");
        Assert.assertEquals(user.getUsername(),"user");
        Assert.assertEquals(admin.getUsername(),"admin");
    }

    @Test
    public void register(){
        userMapper.register("test000","111","12345678909");
        User user=userMapper.getUserByPhone("12345678909");
        Assert.assertEquals(user.getUsername(),"test000");
    }

    @Test
    public void modifyFavor(){
        userMapper.register("test000","111","12345678909");
        User user=userMapper.getUserByPhone("12345678909");
        userMapper.modifyFavor(user.getId(),"f1,f2,f3","","");
        user=userMapper.getUserById(user.getId());
        Assert.assertEquals(TransformUtil.string2List(user.getFlavor()).size(),3);
        Assert.assertEquals(TransformUtil.string2List(user.getMethod()).size(),0);
    }

    @Test
    public void collect(){
        userMapper.register("test000","111","12345678909");
        User user=userMapper.getUserByPhone("12345678909");
        collectionMapper.collect(user.getId(),1,"recipe1");
        collectionMapper.collect(user.getId(),2,"recipe2");
        collectionMapper.collect(user.getId(),3,"recipe3");
        Assert.assertEquals(collectionMapper.getCollection(user.getId()).size(),3);
    }


}
