package com.je.develop.vo;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 用户跟功能权限
 */
public class FuncUserPerm implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = -8475364844008156859L;

    /**
     * 是否全部
     */
    private Boolean all=true;
    /**
     * 是否取反
     */
    private Boolean versa=false;
    /**
     * 查询SQL
     */
    private String querySql="";
    /**
     * 是否覆盖
     */
    private Boolean sqlOverwrite=false;
    /**
     * 字段权限
     */
    private Set<String> fieldIds=new HashSet<String>();
    private Set<String> fieldReadOnlyIds=new HashSet<String>();
    /**
     * 功能按钮权限
     */
    private Set<String> buttonIds=new HashSet<String>();
    /**
     * 子功能权限
     */
    private Set<String> childrenIds=new HashSet<String>();
    private Set<String> dicCodes=new HashSet<String>();
    /**
     * 字典权限信息
     */
    private Map<String,FuncDicPermVo> dicInfos=new HashMap<String,FuncDicPermVo>();

    public Set<String> getButtonIds() {
        return buttonIds;
    }

    public void setButtonIds(Set<String> buttonIds) {
        this.buttonIds = buttonIds;
    }

    public Set<String> getChildrenIds() {
        return childrenIds;
    }

    public void setChildrenIds(Set<String> childrenIds) {
        this.childrenIds = childrenIds;
    }

    public Boolean getAll() {
        return all;
    }

    public void setAll(Boolean all) {
        this.all = all;
    }

    public Boolean getVersa() {
        return versa;
    }

    public void setVersa(Boolean versa) {
        this.versa = versa;
    }

    public String getQuerySql() {
        return querySql;
    }

    public void setQuerySql(String querySql) {
        this.querySql = querySql;
    }

    public Boolean getSqlOverwrite() {
        return sqlOverwrite;
    }

    public void setSqlOverwrite(Boolean sqlOverwrite) {
        this.sqlOverwrite = sqlOverwrite;
    }

    public Set<String> getFieldIds() {
        return fieldIds;
    }

    public void setFieldIds(Set<String> fieldIds) {
        this.fieldIds = fieldIds;
    }

    public Set<String> getFieldReadOnlyIds() {
        return fieldReadOnlyIds;
    }

    public void setFieldReadOnlyIds(Set<String> fieldReadOnlyIds) {
        this.fieldReadOnlyIds = fieldReadOnlyIds;
    }

    public Set<String> getDicCodes() {
        return dicCodes;
    }

    public void setDicCodes(Set<String> dicCodes) {
        this.dicCodes = dicCodes;
    }

    public Map<String, FuncDicPermVo> getDicInfos() {
        return dicInfos;
    }

    public void setDicInfos(Map<String, FuncDicPermVo> dicInfos) {
        this.dicInfos = dicInfos;
    }
}
