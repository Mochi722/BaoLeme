package com.gooood.GooooCoin.util;

import com.alibaba.fastjson.JSON;
import org.neo4j.driver.v1.*;
import org.neo4j.driver.v1.types.Node;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.*;
import java.util.Map.Entry;

@Component
public class Neo4jUtil {

    private final Driver neo4jDriver;

    @Autowired
    public Neo4jUtil(Driver neo4jDriver) {
        this.neo4jDriver = neo4jDriver;
    }

    public boolean connect() {
        try (Session session = neo4jDriver.session()) {
            System.out.println("连接成功：" + session.isOpen());
            return session.isOpen();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    public StatementResult executeCypherSql(String cypherSql){
        StatementResult result = null;
        try (Session session = neo4jDriver.session()) {
            System.out.println(cypherSql);
            result = session.run(cypherSql);
        } catch (Exception e) {
            throw e;
        }
        return result;
    }


}
