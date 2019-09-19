package com.je.wf.vo;

import com.je.core.util.bean.DynaBean;

/**
 *  TODO 暂不明确
 */
public class WarnEventVo {
	/**
	 * 提醒时间
	 */
	private String warnTime;
	/**
	 * 业务表名
	 */
	private String tableCode;
	//业务主键
	private String pkValue;
	/**
	 * 任务ID
	 */
	private String taskId;
	/**
	 * （任务名称
	 */
	private String taskName;
	/**
	 * 送交意见
	 */
	private String comments;
	/**
	 * 流程任务执行人
	 */
	private String assigneeId;
	/**
	 * 类型
	 */
	private String type;
	/**
	 * 业务执行
	 */
	private String config;
	/**
	 * 流转路径
	 */
	private String transCode;
	/**
	 * 发送人
	 */
	private String sendUser;
	/**
	 * 当前活动任务
	 */
	private String currentTask;
	/**
	 * 流程PDID
	 */
	private String pdid;
	/**
	 * 流程PIID
	 */
	private String piid;
	/**
	 * 是否已执行
	 */
	private Boolean execute;
	public String getWarnTime() {
		return warnTime;
	}
	public void setWarnTime(String warnTime) {
		this.warnTime = warnTime;
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
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public String getTaskName() {
		return taskName;
	}
	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	
	public String getAssigneeId() {
		return assigneeId;
	}
	public void setAssigneeId(String assigneeId) {
		this.assigneeId = assigneeId;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getConfig() {
		return config;
	}
	public void setConfig(String config) {
		this.config = config;
	}
	public String getTransCode() {
		return transCode;
	}
	public void setTransCode(String transCode) {
		this.transCode = transCode;
	}
	public String getSendUser() {
		return sendUser;
	}
	public void setSendUser(String sendUser) {
		this.sendUser = sendUser;
	}
	public String getCurrentTask() {
		return currentTask;
	}
	public void setCurrentTask(String currentTask) {
		this.currentTask = currentTask;
	}
	public String getPdid() {
		return pdid;
	}
	public void setPdid(String pdid) {
		this.pdid = pdid;
	}
	public String getPiid() {
		return piid;
	}
	public void setPiid(String piid) {
		this.piid = piid;
	}
	public void initWarnBean(DynaBean warn){
		this.warnTime=warn.getStr("WFWARN_TIME");
		this.pdid=warn.getStr("WFWARN_SY_PDID");
		this.piid=warn.getStr("WFWARN_SY_PIID");
		this.tableCode=warn.getStr("WFWARN_TABLECODE");
		this.pkValue=warn.getStr("WFWARN_PKVALUE");
		this.taskId=warn.getStr("WFWARN_TASKID");
		this.assigneeId=warn.getStr("WFWARN_USERID");
		this.type=warn.getStr("WFWARN_TYPE");
		this.config=warn.getStr("WFWARN_CONFIG");
		this.transCode=warn.getStr("WFWARN_TRANSCODE");
		this.comments=warn.getStr("WFWARN_COMMENTS");
		this.sendUser=warn.getStr("WFWARN_SENDUSER");
		this.taskName=warn.getStr("WFWARN_TASKNAME");
		this.transCode=warn.getStr("WFWARN_TASKSTR");
		this.execute="1".equals(warn.getStr("WFWARN_ZX"));
	}
	public Boolean getExecute() {
		return execute;
	}
	public void setExecute(Boolean execute) {
		this.execute = execute;
	}
	
}
