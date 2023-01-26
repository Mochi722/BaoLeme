package com.gooood.GooooCoin;

import com.gooood.GooooCoin.PO.User;
import com.gooood.GooooCoin.data.mysqlData.UserMapper;
import com.gooood.GooooCoin.util.Neo4jUtil;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@EnableAutoConfiguration
@RunWith(SpringRunner.class)
@SpringBootTest
public class GooooCoinApplicationTests {

    @Autowired
    Neo4jUtil neo4jUtil;

    @Autowired
    UserMapper userMapper;



    @Test
    public void connectNeo4j() {
        Assert.assertTrue(neo4jUtil.connect());
    }

    @Test
    public void login(){
        User user=userMapper.getUserByPhone("12345678900");
        System.out.println(user.getUsername());
        System.out.println(user.getPhoneNumber());
    }
}
