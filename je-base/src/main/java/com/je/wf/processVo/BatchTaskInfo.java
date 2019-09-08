package com.je.wf.processVo;

import java.io.Serializable;

/**
 * 多人处理节点
 * @author zhangshuaipeng
 *
 */
public class BatchTaskInfo implements Serializable{

    /**
     *
     */
    private static final long serialVersionUID = 4293724724795812335L;
    private Boolean sort=false;
    private String[] userIds;
    private String[] userNames;
    public Boolean getSort() {
        return sort;
    }
    public void setSort(Boolean sort) {
        this.sort = sort;
    }
    public String[] getUserIds() {
        return userIds;
    }
    public void setUserIds(String[] userIds) {
        this.userIds = userIds;
    }
    public String[] getUserNames() {
        return userNames;
    }
    public void setUserNames(String[] userNames) {
        this.userNames = userNames;
    }


}
