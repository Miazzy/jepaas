package com.je.postil.vo;

import java.util.HashMap;
import java.util.Map;

/**
 * 批注功能vo
 */
public class PostilVo {
    /**功能名称*/
    private String funcName;
    /**功能编码*/
    private String funcId;
    /**功能ID*/
    private String funcCode;
    /**功能图标样式*/
    private String funcIconCls;
    /**表名*/
    private String tableCode;
    /**主键值*/
    private String pkValue;
    /**批注数量*/
    private Integer postilCount;
    /**展示内容*/
    private String  showConfig;
    /**标题展示*/
    private String  titleConfig;
    /**发布时间*/
    private String createTime;
    /**功能编辑*/
    private String funcEdit;

    private Map<String,Object> bean=new HashMap<>();
    private Map<String,Object> lastPostil=new HashMap<>();

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

    public String getPkValue() {
        return pkValue;
    }

    public void setPkValue(String pkValue) {
        this.pkValue = pkValue;
    }

    public Integer getPostilCount() {
        return postilCount;
    }

    public void setPostilCount(Integer postilCount) {
        this.postilCount = postilCount;
    }

    public String getShowConfig() {
        return showConfig;
    }

    public void setShowConfig(String showConfig) {
        this.showConfig = showConfig;
    }

    public String getTitleConfig() {
        return titleConfig;
    }

    public void setTitleConfig(String titleConfig) {
        this.titleConfig = titleConfig;
    }

    public Map<String, Object> getBean() {
        return bean;
    }

    public void setBean(Map<String, Object> bean) {
        this.bean = bean;
    }

    public String getFuncId() {
        return funcId;
    }

    public void setFuncId(String funcId) {
        this.funcId = funcId;
    }

    public String getFuncIconCls() {
        return funcIconCls;
    }

    public void setFuncIconCls(String funcIconCls) {
        this.funcIconCls = funcIconCls;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getFuncEdit() {
        return funcEdit;
    }

    public void setFuncEdit(String funcEdit) {
        this.funcEdit = funcEdit;
    }

    public Map<String, Object> getLastPostil() {
        return lastPostil;
    }

    public void setLastPostil(Map<String, Object> lastPostil) {
        this.lastPostil = lastPostil;
    }
}
