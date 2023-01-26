package com.gooood.GooooCoin.VO;


import lombok.Builder;
import lombok.Data;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;


@Data
public class EntityVO {
    /**
     * 实体固有属性-id
     */
    private Long id;
    /**
     * 实体固有属性-labels标签
     */
    private List<String> labels;
    /**
     * 实体固有属性-properties属性
     */
    private Map<String,Object> properties;
    /**
     * 用于search方法，实体属性name，常用属性字段
     */
    private String name;
    /**
     * 用于update方法，修改实体属性的name字段时传参
     */
    private String newName;

}
