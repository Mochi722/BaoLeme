package com.gooood.GooooCoin.service.graphService.bl;

import com.gooood.GooooCoin.VO.ResponseVO;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface IntelligenceService {
    ResponseVO search(String key, String label,Map<String, List<String>> props);

    ResponseVO answer(String question) throws Exception;

    ResponseVO cookWhat(List<String> names,Map<String, List<String>> props);
}
