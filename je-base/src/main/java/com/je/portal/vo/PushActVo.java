package com.je.portal.vo;

import net.sf.json.JSONObject;

//todo:尚不明确
public class PushActVo {
    /**
     * 类型
     */
    private String type;
    /**
     * 编码
     */
    private String code;
    /**
     * 角标
     */
    private Boolean badge=false;
    /**
     * 更新到APP应用外的总角标
     */
    private Boolean apkBadge=false;
    /**
     * 角标数值
     */
    private JSONObject badgeObj=new JSONObject();

    /**
     * 刷新
     */
    private Boolean refresh=false;
    /**
     * 更新数字
     */
    private Boolean num=false;
    /**
     * 更新数字
     */
    private Boolean refreshNum=false;
    /**
     * 数字信息
     */
    private JSONObject numObj=new JSONObject();

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean getRefresh() {
        return refresh;
    }

    public void setRefresh(Boolean refresh) {
        this.refresh = refresh;
    }

    public Boolean getNum() {
        return num;
    }

    public void setNum(Boolean num) {
        this.num = num;
    }

    public JSONObject getNumObj() {
        return numObj;
    }

    public void setNumObj(JSONObject numObj) {
        this.numObj = numObj;
    }

    public Boolean getRefreshNum() {
        return refreshNum;
    }

    public void setRefreshNum(Boolean refreshNum) {
        this.refreshNum = refreshNum;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Boolean getBadge() {
        return badge;
    }

    public void setBadge(Boolean badge) {
        this.badge = badge;
    }

    public JSONObject getBadgeObj() {
        return badgeObj;
    }

    public void setBadgeObj(JSONObject badgeObj) {
        this.badgeObj = badgeObj;
    }

    public Boolean getApkBadge() {
        return apkBadge;
    }

    public void setApkBadge(Boolean apkBadge) {
        this.apkBadge = apkBadge;
    }
}
