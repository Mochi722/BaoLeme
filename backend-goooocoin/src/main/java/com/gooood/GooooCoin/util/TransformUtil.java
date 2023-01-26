package com.gooood.GooooCoin.util;

import com.google.inject.internal.util.Lists;
import com.gooood.GooooCoin.PO.Entity;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

public class TransformUtil {
    public static String list2String(List<String> list){
        if(list==null || list.size()==0)return "";
        StringBuilder stringBuilder=new StringBuilder();
        for(String s:list){
            stringBuilder.append(s);
            stringBuilder.append(",");
        }
        stringBuilder.deleteCharAt(stringBuilder.length()-1);
        return stringBuilder.toString();
    }
    public static List<String> string2List(String s){
        if(s.isEmpty())return new ArrayList<>();
        return Lists.newArrayList(s.split(","));
    }

    public static Map<String,Object> D3propsFilter(Map<String, Object> m){
        Map<String, Object> map = new HashMap<>(m); //创建新的map
        for(String key:m.keySet()) {
            if(key.equals("properties")) {
                // 把properties中的d3_properties过滤出来------------------------
                Map<String, Object> props = new HashMap<>(); //创建新的map-properties
                Map<String, Object> d3_props = new HashMap<>(); //创建新的map-d3_properties
                Map<String, Object> original_properties = (Map<String, Object>) m.get(key);
                for (String k : original_properties.keySet()) {
                    if (k.startsWith("__D3_PROPS__"))
                        d3_props.put(k, original_properties.get(k));
                    else
                        props.put(k, original_properties.get(k));
                }
                map.put("d3_properties", d3_props);
                map.put("properties", props);
            }
            else map.put(key,m.get(key));
        }
        return map;
    }

    public static Map<String, Object> Bean2Map(Object obj) {
        try {
            Field[] fields = obj.getClass().getDeclaredFields();
            Map<String, Object> map = new HashMap<>();
            for (Field field : fields) {
                field.setAccessible(true);
                if (null != field.get(obj)) {
                    map.put(field.getName(), field.get(obj));
                }
            }
            return map;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <T> T Map2Bean(Map<String, Object> map, Class<T> clazz) {
        T t = null;
        try {
            t = clazz.newInstance();
            Field[] fields = clazz.getDeclaredFields();

            for (Field field : fields) {
                field.setAccessible(true);
//                if (Modifier.isFinal(field.getModifiers()) || Modifier.isStatic(field.getModifiers())
//                        || field.isSynthetic()) {
//                    continue;
//                }

                // boolean类型
                switch (field.getType().getName()) {
                    case "boolean":
                        field.setBoolean(t, Boolean.parseBoolean((String) map.get(field.getName())));
                        break;
                    case "java.lang.Integer":
                        if (null != map.get((field.getName()))) {
                            field.setInt(t, Integer.parseInt(String.valueOf(map.get(field.getName()))));
                        }
                        break;
                    case "java.lang.Long":
                        if (null != map.get((field.getName()))) {
                            field.setLong(t, Long.parseLong(String.valueOf(map.get(field.getName()))));
                        }
                        break;
                    default:  // 其他类型
                        if (null != map.get((field.getName()))) {
                            field.set(t, map.get(field.getName()));
                        }
                        break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return t;
    }
    public static File transferToFile(MultipartFile multipartFile) {
        //选择用缓冲区来实现这个转换即使用java 创建的临时文件 使用 MultipartFile.transferto()方法 。
        File file = null;
        try {
            String suf ="."+multipartFile.getContentType().substring(6);
            System.out.println(suf);
            file=File.createTempFile("user",suf );    //注意下面的 特别注意！！！
            multipartFile.transferTo(file);
            file.deleteOnExit();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }
}
