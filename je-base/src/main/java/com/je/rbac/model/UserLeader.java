package com.je.rbac.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 用户领导以及下属的ihe
 */
public class UserLeader implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 8657827743312520963L;
    /**
     * 直属领导关系
     */
    private List<String> zsUserIds=new ArrayList<>();
    /**
     * 主管领导关系
     */
    private List<String> zgUserIds=new ArrayList<>();
    /**
     * 监管领导关系
     */
    private List<String> jgUserIds=new ArrayList<>();
    /**
     * 部门负责人关系
     */
    private List<String> fzrUserIds=new ArrayList<>();

    public List<String> getZsUserIds() {
        return zsUserIds;
    }

    public void setZsUserIds(List<String> zsUserIds) {
        this.zsUserIds = zsUserIds;
    }

    public List<String> getZgUserIds() {
        return zgUserIds;
    }

    public void setZgUserIds(List<String> zgUserIds) {
        this.zgUserIds = zgUserIds;
    }

    public List<String> getJgUserIds() {
        return jgUserIds;
    }

    public void setJgUserIds(List<String> jgUserIds) {
        this.jgUserIds = jgUserIds;
    }

    public List<String> getFzrUserIds() {
        return fzrUserIds;
    }

    public void setFzrUserIds(List<String> fzrUserIds) {
        this.fzrUserIds = fzrUserIds;
    }
}
