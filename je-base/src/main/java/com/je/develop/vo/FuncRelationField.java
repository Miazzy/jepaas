package com.je.develop.vo;

import java.io.Serializable;

public class FuncRelationField implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = -1248650493234925057L;
    /**
     * 主功能字段编码
     */
    private String fieldCode;
    /**
     * 子功能字段编码
     */
    private String childFieldCode;
    /**
     * 自定义SQL
     */
    private String diySql;
    /**
     * 关联关系
     */
    private String whereType;
    /**
     * 传值
     */
    private Boolean doValue;
    /**
     * WHERE条件
     */
    private Boolean whereChild;
    /**
     * 级联删除子功能
     */
    private Boolean deleteChild;
    /**
     * 级联更新
     */
    private Boolean updateChild;

    public String getFieldCode() {
        return fieldCode;
    }

    public void setFieldCode(String fieldCode) {
        this.fieldCode = fieldCode;
    }

    public String getChildFieldCode() {
        return childFieldCode;
    }

    public void setChildFieldCode(String childFieldCode) {
        this.childFieldCode = childFieldCode;
    }

    public String getDiySql() {
        return diySql;
    }

    public void setDiySql(String diySql) {
        this.diySql = diySql;
    }

    public String getWhereType() {
        return whereType;
    }

    public void setWhereType(String whereType) {
        this.whereType = whereType;
    }

    public Boolean getDoValue() {
        return doValue;
    }

    public void setDoValue(Boolean doValue) {
        this.doValue = doValue;
    }

    public Boolean getWhereChild() {
        return whereChild;
    }

    public void setWhereChild(Boolean whereChild) {
        this.whereChild = whereChild;
    }

    public Boolean getDeleteChild() {
        return deleteChild;
    }

    public void setDeleteChild(Boolean deleteChild) {
        this.deleteChild = deleteChild;
    }

    public Boolean getUpdateChild() {
        return updateChild;
    }

    public void setUpdateChild(Boolean updateChild) {
        this.updateChild = updateChild;
    }
}
