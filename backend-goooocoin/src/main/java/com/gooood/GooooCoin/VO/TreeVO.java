package com.gooood.GooooCoin.VO;

import lombok.Builder;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Data
@Builder
public class TreeVO {
    /**
     * 本树节点id
     */
    private long id;
    /**
     * 本树节点名称
     */
    private String name;
    /**
     * 本树节点的子节点
     */
    private ArrayList<TreeVO> children;
    /**
     * 上级节点到本节点的关系名
     */
    private String relation;
    /**
     * 本节点距离root的深度
     */
    private int depth;

    public void addChild(TreeVO t){
        this.children.add(t);
    }
}
