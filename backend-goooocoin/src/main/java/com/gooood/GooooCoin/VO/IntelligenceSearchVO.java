package com.gooood.GooooCoin.VO;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@Builder
public class IntelligenceSearchVO {
    private String label;
    private Map<String, List<String>> props;
    private String key;
    private List<String> names;
}
