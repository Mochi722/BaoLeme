package com.gooood.GooooCoin.service.graphService.blImpl;

import com.gooood.GooooCoin.PO.Entity;
import com.gooood.GooooCoin.VO.ResponseVO;
import com.gooood.GooooCoin.data.mysqlData.CollectionMapper;
import com.gooood.GooooCoin.data.neo4jData.Neo4jData;
import com.gooood.GooooCoin.service.graphService.bl.IntelligenceService;
import com.gooood.GooooCoin.util.TransformUtil;
import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.corpus.tag.Nature;
import com.hankcs.hanlp.seg.Segment;
import com.hankcs.hanlp.seg.common.Term;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class IntelligenceServiceImpl implements IntelligenceService {
    private final Neo4jData neo4jData;
    private final CollectionMapper collectionMapper;
    private static final int LIST_SIZE = 25;


    private String getMethodName() {
        StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
        StackTraceElement e = stacktrace[2];
        return e.getMethodName();
    }

    @Autowired
    public IntelligenceServiceImpl(Neo4jData neo4jData, CollectionMapper collectionMapper) {
        this.neo4jData = neo4jData;
        this.collectionMapper = collectionMapper;
    }


    @Override
    public ResponseVO search(String key, String label, Map<String, List<String>> props) {
        try {
            Map<Entity, Integer> rank = new LinkedHashMap<>();
            List<Map<String, Object>> list = new ArrayList<>();
            if (label == null) label = "";
            else label = ":" + label;
            if (key == null || key.equals("")) key = null;
            if (props != null) {
                for (String propName : props.keySet()) {
                    List<String> propValue = props.get(propName);
                    for (String val : propValue) {
                        String filterPart = String.format("(e%s{%s:'%s'})", label, propName, val);
                        list.addAll(neo4jData.intelligenceSearch(key, filterPart));
                    }
                }

            }
            if (list.size() < LIST_SIZE) {
                String filterPart = String.format("(e%s)", label);
                list.addAll(neo4jData.intelligenceSearch(key, filterPart));
            }
            for (Map<String, Object> m : list) {
                Entity e = TransformUtil.Map2Bean(m, Entity.class);
                rank.put(e, rank.getOrDefault(e, 0) + 1);
            }
            List<Map.Entry<Entity, Integer>> ranked = new ArrayList<>(rank.entrySet());
            ranked.sort((o1, o2) -> {
                Integer p1 = o1.getValue() * Integer.parseInt(o1.getKey().getProperties().get("heat").toString());
                ;
                Integer p2 = o2.getValue() * Integer.parseInt(o2.getKey().getProperties().get("heat").toString());
                return p2 - p1;
            });
            ArrayList<Entity> res = new ArrayList<>();
            for (int i = 0; i < LIST_SIZE; i++) {
                if (i == ranked.size()) break;
                res.add(ranked.get(i).getKey());
            }
            return ResponseVO.builder().success(true).message("Success in :" + getMethodName()).content(res).build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseVO.builder().success(false).message("Failed in :" + getMethodName()).build();
        }
    }


    @Override
    public ResponseVO answer(String question) throws Exception {
        Segment segment = HanLP.newSegment().enableCustomDictionary(true);
        List<Term> terms = segment.seg(question);
        List<String> stepsList=Arrays.asList("做法","步骤","方式","怎么做");
        String res="";
        for (Term term : terms) {
            String word = term.word;
            String termStr = term.toString();
            System.out.println(termStr);
            Entity entity;
            if(termStr.contains("nre")){//有菜谱名字
                String name=termStr.split("/")[0];
                System.out.println(name);
                List<Map<String,Object>> lst=neo4jData.intelligenceSearch(name,"(e:Recipe)");
                if(lst!=null) {
                    entity = TransformUtil.Map2Bean(lst.get(0), Entity.class);
                    for(String step:stepsList){
                        if(question.contains(step)) return ResponseVO.builder().success(true).content(entity.getProperties().get("做法").toString()).build();
                    }

                }
                else{
                    return ResponseVO.builder().success(true).message("你输的什么菜啊").build();
                }
            }
            else if(termStr.contains("nma")){
                List<String> yyjzList=Arrays.asList("营养","好处","益处");
                String name=termStr.split("/")[0];
                List<Map<String,Object>> lst=neo4jData.intelligenceSearch(name,"(e:Ingredients)");
                if(lst!=null) {
                    entity = TransformUtil.Map2Bean(lst.get(0), Entity.class);
                    for(String yyjz:yyjzList){
                        if(question.contains(yyjz)) return ResponseVO.builder().success(true).content(entity.getProperties().get("营养价值").toString()).build();
                    }
                }
                else{
                    return ResponseVO.builder().success(true).message("你输的什么原料啊").build();
                }
            }
            res+=(word+" ");
        }
        System.out.println("res-------------"+res);
        return null;
    }

    @Override
    public ResponseVO cookWhat(List<String> names, Map<String, List<String>> props) {
        try {
            String alia = "(e:Ingredients)";
            Set<Map<String, Object>> entitySet = new HashSet<>();
            for (String name : names) {
                if (name.equals("")) continue;
                entitySet.addAll(neo4jData.intelligenceSearch(name, alia));
            }

            Map<Entity, Integer> rank = new LinkedHashMap<>();
            for (Map<String, Object> e : entitySet) {
                Entity entity = TransformUtil.Map2Bean(e, Entity.class);
                List<Map<String, Object>> recipes;
                if (props == null) {
                    alia = "(e:Recipe)";
                    recipes = neo4jData.intelligenceSearchRelativeRecipe(entity.getId(), alia);
                    for (Map<String, Object> r : recipes) {
                        String type = r.get("type").toString();
                        r.remove("type");
                        Entity recipe = TransformUtil.Map2Bean(r, Entity.class);
                        int add = recipe.getProperties().get("name").toString().contains(entity.getProperties().get("name").toString()) ? 3 : 1;
                        if (type.equals("主食材")) add += 2;
                        rank.put(recipe, rank.getOrDefault(recipe, add) + 1);
                    }
                } else {
                    for (String propName : props.keySet()) {
                        List<String> propValue = props.get(propName);
                        for (String val : propValue) {
                            alia = String.format("(e:Recipe{%s:'%s'})", propName, val);
                            recipes = neo4jData.intelligenceSearchRelativeRecipe(entity.getId(), alia);
                            for (Map<String, Object> r : recipes) {
                                String type = r.get("type").toString();
                                r.remove("type");
                                Entity recipe = TransformUtil.Map2Bean(r, Entity.class);
                                int add = recipe.getProperties().get("name").toString().contains(entity.getProperties().get("name").toString()) ? 3 : 1;
                                if (type.equals("主食材")) add += 2;
                                rank.put(recipe, rank.getOrDefault(recipe, add) + 1);
                            }
                        }
                    }
                }
                recipes = neo4jData.intelligenceSearch(entity.getProperties().get("name").toString(), "(e:Recipe)");
                for (Map<String, Object> r : recipes) {
                    Entity recipe = TransformUtil.Map2Bean(r, Entity.class);
                    int add = 5;
                    rank.put(recipe, rank.getOrDefault(recipe, add) + 1);
                }


            }


            List<Map.Entry<Entity, Integer>> ranked = new ArrayList<>(rank.entrySet());
            ranked.sort((o1, o2) -> {
                Integer p1 = o1.getValue() * Integer.parseInt(o1.getKey().getProperties().get("heat").toString());
                Integer p2 = o2.getValue() * Integer.parseInt(o2.getKey().getProperties().get("heat").toString());
                return p2 - p1;
            });
            ArrayList<Entity> res = new ArrayList<>();
            for (int i = 0; i < LIST_SIZE; i++) {
                if (i == ranked.size()) break;
                res.add(ranked.get(i).getKey());
            }
            return ResponseVO.builder().success(true).message("Success in :" + getMethodName()).content(res).build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseVO.builder().success(false).message("Failed in :" + getMethodName()).build();
        }

    }
}
