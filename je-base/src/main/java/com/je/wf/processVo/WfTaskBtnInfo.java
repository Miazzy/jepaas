package com.je.wf.processVo;

import java.io.Serializable;

public class WfTaskBtnInfo implements Serializable{
    /**
     *
     */
    private static final long serialVersionUID = -2687924104241926564L;
    /**
     * 按钮类型  送交/退回/取回...
     */
    private String btnCode;
    /**
     * 按钮自定义名称
     */
    private String btnName;
    /**
     * 流转节点名称
     */
    private String taskName;
    /**
     * 流转路径
     */
    private String tranCode;
    public String getBtnCode() {
        return btnCode;
    }
    public void setBtnCode(String btnCode) {
        this.btnCode = btnCode;
    }
    public String getBtnName() {
        return btnName;
    }
    public void setBtnName(String btnName) {
        this.btnName = btnName;
    }
    public String getTaskName() {
        return taskName;
    }
    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }
    public String getTranCode() {
        return tranCode;
    }
    public void setTranCode(String tranCode) {
        this.tranCode = tranCode;
    }

}
