package com.je.develop.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FuncInfo implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = -179354760984120059L;
    /**
     * 功能ID
     */
    private String funcId;
    /**
     * 功能名称
     */
    private String funcName;
    /**
     * 功能编码
     */
    private String funcCode;
    /**
     * 功能类型
     */
    private String funcType;
    /**
     * 表编码
     */
    private String tableCode;
    /**
     * 主键编码
     */
    private String pkCode;
    /**
     * 视图操作表名
     */
    private String curdTableCode;
    /**
     * 过滤条件
     */
    private String whereSql;
    /**
     * 查询列
     */
    private String queryColumns;
    /**
     * 字段默认值
     */
    private Map<String,String> fieldDefaultValues=new HashMap<>();
    /**
     * 图标样式
     */
    private String iconCls;
    /**
     * 批注展示信息
     */
    private String postilShowConfig;
    /**
     * 标题展示信息
     */
    private String postilTitleConfig;
    /**
     * 标记标识
     */
    private Boolean mark;
    /**
     * 未读标识
     */
    private Boolean funcEdit;
    /**
     * 批注标识
     */
    private Boolean postil;
    /**
     * 子功能
     */
    private List<FuncRelation> children=new ArrayList<>();

    public String getFuncId() {
        return funcId;
    }

    public void setFuncId(String funcId) {
        this.funcId = funcId;
    }

    public String getFuncName() {
        return funcName;
    }

    public void setFuncName(String funcName) {
        this.funcName = funcName;
    }

    public String getFuncCode() {
        return funcCode;
    }

    public void setFuncCode(String funcCode) {
        this.funcCode = funcCode;
    }

    public String getTableCode() {
        return tableCode;
    }

    public void setTableCode(String tableCode) {
        this.tableCode = tableCode;
    }

    public String getPkCode() {
        return pkCode;
    }

    public void setPkCode(String pkCode) {
        this.pkCode = pkCode;
    }

    public String getCurdTableCode() {
        return curdTableCode;
    }

    public void setCurdTableCode(String curdTableCode) {
        this.curdTableCode = curdTableCode;
    }

    public String getQueryColumns() {
        return queryColumns;
    }

    public void setQueryColumns(String queryColumns) {
        this.queryColumns = queryColumns;
    }

    public Map<String, String> getFieldDefaultValues() {
        return fieldDefaultValues;
    }

    public void setFieldDefaultValues(Map<String, String> fieldDefaultValues) {
        this.fieldDefaultValues = fieldDefaultValues;
    }

    public String getIconCls() {
        return iconCls;
    }

    public void setIconCls(String iconCls) {
        this.iconCls = iconCls;
    }

    public String getPostilShowConfig() {
        return postilShowConfig;
    }

    public void setPostilShowConfig(String postilShowConfig) {
        this.postilShowConfig = postilShowConfig;
    }

    public String getPostilTitleConfig() {
        return postilTitleConfig;
    }

    public void setPostilTitleConfig(String postilTitleConfig) {
        this.postilTitleConfig = postilTitleConfig;
    }

    public List<FuncRelation> getChildren() {
        return children;
    }

    public void setChildren(List<FuncRelation> children) {
        this.children = children;
    }

    public Boolean getMark() {
        return mark;
    }

    public void setMark(Boolean mark) {
        this.mark = mark;
    }

    public Boolean getFuncEdit() {
        return funcEdit;
    }

    public void setFuncEdit(Boolean funcEdit) {
        this.funcEdit = funcEdit;
    }

    public Boolean getPostil() {
        return postil;
    }

    public void setPostil(Boolean postil) {
        this.postil = postil;
    }

    public String getFuncType() {
        return funcType;
    }

    public void setFuncType(String funcType) {
        this.funcType = funcType;
    }

    public String getWhereSql() {
        return whereSql;
    }

    public void setWhereSql(String whereSql) {
        this.whereSql = whereSql;
    }
}
