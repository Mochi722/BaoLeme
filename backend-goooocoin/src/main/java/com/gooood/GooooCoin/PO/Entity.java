package com.gooood.GooooCoin.PO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Data
@Repository
@NoArgsConstructor
@EqualsAndHashCode
public class Entity {
    private long id;
    private List<String> labels;
    private Map<String,Object> properties;
    /**
     * 实体固有属性-d3_properties属性
     */
    private Map<String,Object> d3_properties;
}
