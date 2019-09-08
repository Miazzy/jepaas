package com.je.develop.vo;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * 功能权限缓存
 */
public class FuncPerm implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 5800084403271565983L;
    /**
     * 租户权限
     */
    private Boolean zhPerm=false;
    /**
     * 使用关系ID
     */
    private Set<String> useCjglIds=new HashSet<>();
    /**
     * 可见人ID
     */
    private Set<String> seeUserIds=new HashSet<>();
    /**
     * 可见部门ID
     */
    private Set<String> seeDeptIds=new HashSet<>();
    /**
     * 可见角色ID
     */
    private Set<String> seeRoleIds=new HashSet<>();
    /**
     * 可见岗位ID
     */
    private Set<String> seeSentryIds=new HashSet<>();
    /**
     * 权限类型
     */
    private Set<String> permConfig=new HashSet<>();
    /**权限SQL*/
    private String permSql="";
    /**
     * 权限脚本
     */
    private String permJs="";
    /**
     * 用户控制字段  为空使用SY_CREATEUSERID
     */
    private String userField="";
    /***
     * 部门控制字段 为空使用SY_CREATEORGID
     */
    private String deptField="";

    public Set<String> getUseCjglIds() {
        return useCjglIds;
    }

    public void setUseCjglIds(Set<String> useCjglIds) {
        this.useCjglIds = useCjglIds;
    }

    public Set<String> getSeeUserIds() {
        return seeUserIds;
    }

    public void setSeeUserIds(Set<String> seeUserIds) {
        this.seeUserIds = seeUserIds;
    }

    public Set<String> getSeeDeptIds() {
        return seeDeptIds;
    }

    public void setSeeDeptIds(Set<String> seeDeptIds) {
        this.seeDeptIds = seeDeptIds;
    }

    public Set<String> getSeeRoleIds() {
        return seeRoleIds;
    }

    public void setSeeRoleIds(Set<String> seeRoleIds) {
        this.seeRoleIds = seeRoleIds;
    }

    public Set<String> getSeeSentryIds() {
        return seeSentryIds;
    }

    public void setSeeSentryIds(Set<String> seeSentryIds) {
        this.seeSentryIds = seeSentryIds;
    }

    public Set<String> getPermConfig() {
        return permConfig;
    }

    public void setPermConfig(Set<String> permConfig) {
        this.permConfig = permConfig;
    }

    public String getPermSql() {
        return permSql;
    }

    public void setPermSql(String permSql) {
        this.permSql = permSql;
    }

    public String getPermJs() {
        return permJs;
    }

    public void setPermJs(String permJs) {
        this.permJs = permJs;
    }

    public String getUserField() {
        return userField;
    }

    public void setUserField(String userField) {
        this.userField = userField;
    }

    public String getDeptField() {
        return deptField;
    }

    public void setDeptField(String deptField) {
        this.deptField = deptField;
    }

    public Boolean getZhPerm() {
        return zhPerm;
    }

    public void setZhPerm(Boolean zhPerm) {
        this.zhPerm = zhPerm;
    }
}
