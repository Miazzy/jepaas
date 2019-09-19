package com.je.saas.vo;

import com.je.core.util.bean.DynaBean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class UserStatisticVo implements Serializable{
    private static final long serialVersionUID = 7902961715850473526L;
    private int totalUsersBesideDisabled;
    private int totalAvailableUsers;
    private int totalUnavailableUsers;
    private int totalLimitNumbers;
    private String tenantId;
    private List<DynaBean> unavailabelUsers = new ArrayList<>();

    public int getTotalUsersBesideDisabled() {
        return totalUsersBesideDisabled;
    }

    public void setTotalUsersBesideDisabled(int totalUsersBesideDisabled) {
        this.totalUsersBesideDisabled = totalUsersBesideDisabled;
    }

    public int getTotalAvailableUsers() {
        return totalAvailableUsers;
    }

    public void setTotalAvailableUsers(int totalAvailableUsers) {
        this.totalAvailableUsers = totalAvailableUsers;
    }

    public int getTotalUnavailableUsers() {
        return totalUnavailableUsers;
    }

    public void setTotalUnavailableUsers(int totalUnavailableUsers) {
        this.totalUnavailableUsers = totalUnavailableUsers;
    }

    public int getTotalLimitNumbers() {
        return totalLimitNumbers;
    }

    public void setTotalLimitNumbers(int totalLimitNumbers) {
        this.totalLimitNumbers = totalLimitNumbers;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public List<DynaBean> getUnavailabelUsers() {
        return unavailabelUsers;
    }

    public void setUnavailabelUsers(List<DynaBean> unavailabelUsers) {
        this.unavailabelUsers = unavailabelUsers;
    }
}
