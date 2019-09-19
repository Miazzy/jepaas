package com.je.wf.processVo;

import java.io.Serializable;
/**
 * 子流程设定
 * @author zhangshuaipeng
 *
 */
public class ChildProcessInfo implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 3602072872960593988L;
    /**流程名称*/
    private String processName;
    /**流程key*/
    private String processKey;
    /***
     * 表名
     */
    private String tableCode;
    /**
     * 主键编码
     */
    private String pkCode;
    /**
     * 过滤条件
     */
    private String whereSql;
    public String getProcessName() {
        return processName;
    }
    public void setProcessName(String processName) {
        this.processName = processName;
    }
    public String getProcessKey() {
        return processKey;
    }
    public void setProcessKey(String processKey) {
        this.processKey = processKey;
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
    public String getWhereSql() {
        return whereSql;
    }
    public void setWhereSql(String whereSql) {
        this.whereSql = whereSql;
    }

}
