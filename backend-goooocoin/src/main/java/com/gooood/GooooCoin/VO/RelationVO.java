package com.gooood.GooooCoin.VO;
import lombok.Builder;
import lombok.Data;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Data
public class RelationVO {
    /**
     * 关系固有属性-id
     */
    private Long id;
    /**
     * 关系固有属性-name关系类型，neo4j中表示为type
     */
    private String name;
    /**
     * 用于修改关系name时传参
     */
    private String newName;
    /**
     * 关系固有属性-起始节点id
     */
    private Long source;
    /**
     * 关系固有属性-目的节点id
     */
    private Long target;
    /**
     * 关系固有属性-properties
     */
    private Map<String,Object> properties;

}
