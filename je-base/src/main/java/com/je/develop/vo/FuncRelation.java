package com.je.develop.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class FuncRelation  implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = -6059221221040988668L;
    /**
     * 名称
     */
    private String name;
    /**
     * 编码
     */
    private String code;
    /**
     * 功能类型 详情查看数据字典  JE_CORE_FUNCTYPE
     */
    private String  type;
    /**
     * 子表编码
     */
    private String tableCode;
    /**
     * 是否级联复制子功能
     */
    private Boolean copyChild;
    /**
     * 主子功能关联字段
     */
    private List<FuncRelationField> childFields=new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTableCode() {
        return tableCode;
    }

    public void setTableCode(String tableCode) {
        this.tableCode = tableCode;
    }

    public Boolean getCopyChild() {
        return copyChild;
    }

    public void setCopyChild(Boolean copyChild) {
        this.copyChild = copyChild;
    }

    public List<FuncRelationField> getChildFields() {
        return childFields;
    }

    public void setChildFields(List<FuncRelationField> childFields) {
        this.childFields = childFields;
    }
}
