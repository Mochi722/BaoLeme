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
public class Relation {
    private long id;
    private long source;
    private long target;
    private String name;
    private Map<String,Object> properties;
    private Map<String,Object> d3_properties;
}
