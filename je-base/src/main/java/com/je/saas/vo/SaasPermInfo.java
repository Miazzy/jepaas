package com.je.saas.vo;

import com.je.core.util.bean.DynaBean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * SAAS的权限控制 人员数量，产品安装信息
 */
public class SaasPermInfo implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = -2956556910179151149L;
    /**
     * 租户ID
     */
    private String zhId;
    /**
     * 租户名称
     */
    private String zhMc;
    /***
     * 租户数据源
     */
    private String zhDs;
    /***
     * 租户管理员ID
     */
    private String adminIds;
    /**
     * 租户管理员名称
     */
    private String adminNames;
    /**
     *  租户管理员部门ID
     */
    private String adminDeptIds;
    /**
     * 联系人
     */
    private String lxrName;
    /**
     * 联系电话
     */
    private String lxrPhone;
    /**
     * 联系人邮箱
     */
    private String lxrEmail;
    /**
     * 用户使用上限
     */
    private Integer allUserLimit;
    /**
     * 当前已维护数量
     */
    private Integer nowUserLimit;
    /**
     * 有效用户数量
     */
    private Integer validUserLimit;
    /**
     * 无效用户数量
     */
    private Integer noValidUserLimit;
    /**
     * 已安装产品信息
     */
    private List<DynaBean> setupCps=new ArrayList<>();

    public String getZhId() {
        return zhId;
    }

    public void setZhId(String zhId) {
        this.zhId = zhId;
    }

    public Integer getAllUserLimit() {
        return allUserLimit;
    }

    public void setAllUserLimit(Integer allUserLimit) {
        this.allUserLimit = allUserLimit;
    }

    public Integer getNowUserLimit() {
        return nowUserLimit;
    }

    public void setNowUserLimit(Integer nowUserLimit) {
        this.nowUserLimit = nowUserLimit;
    }

    public Integer getValidUserLimit() {
        return validUserLimit;
    }

    public void setValidUserLimit(Integer validUserLimit) {
        this.validUserLimit = validUserLimit;
    }

    public Integer getNoValidUserLimit() {
        return noValidUserLimit;
    }

    public void setNoValidUserLimit(Integer noValidUserLimit) {
        this.noValidUserLimit = noValidUserLimit;
    }

    public List<DynaBean> getSetupCps() {
        return setupCps;
    }

    public void setSetupCps(List<DynaBean> setupCps) {
        this.setupCps = setupCps;
    }

    public String getAdminIds() {
        return adminIds;
    }

    public void setAdminIds(String adminIds) {
        this.adminIds = adminIds;
    }

    public String getAdminNames() {
        return adminNames;
    }

    public void setAdminNames(String adminNames) {
        this.adminNames = adminNames;
    }

    public String getAdminDeptIds() {
        return adminDeptIds;
    }

    public void setAdminDeptIds(String adminDeptIds) {
        this.adminDeptIds = adminDeptIds;
    }

    public String getZhDs() {
        return zhDs;
    }

    public void setZhDs(String zhDs) {
        this.zhDs = zhDs;
    }

    public String getLxrName() {
        return lxrName;
    }

    public void setLxrName(String lxrName) {
        this.lxrName = lxrName;
    }

    public String getLxrPhone() {
        return lxrPhone;
    }

    public void setLxrPhone(String lxrPhone) {
        this.lxrPhone = lxrPhone;
    }

    public String getLxrEmail() {
        return lxrEmail;
    }

    public void setLxrEmail(String lxrEmail) {
        this.lxrEmail = lxrEmail;
    }

    public String getZhMc() {
        return zhMc;
    }

    public void setZhMc(String zhMc) {
        this.zhMc = zhMc;
    }
}
