package com.je.wf.vo;

/**
 * 任务信息VO
 * @author chenmeng
 * 2012-4-16 上午9:46:03
 */
public class TaskInfo {
	/** 业务对象主键 */
	private String pkValue;
	/** 业务对象全名称 */
	private String tableCode;
	/**主键名称 */
	private String pkName;
	/** 任务ID */
	private String taskId;
	/** 下一任务指定人 */
	private String assignee;
	/** 下一任务目标角色 */
	private String assigneeRole;
	/** 下一任务指定类型 单选人，多选人，单选角色，多选角色 */
	private String taskAssignedType;
	/** 提交类型（通过或退回） */
	private String submitType;
	/** 提交意见 */
	private String submitComments;
	/** 功能ID */
	private String funcId;
	/**角色*/
	private String roleCodes;
	/**流程主键值*/
	private String processInfoId;
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public String getAssignee() {
		return assignee;
	}
	public void setAssignee(String assignee) {
		this.assignee = assignee;
	}
	public String getSubmitType() {
		return submitType;
	}
	public void setSubmitType(String submitType) {
		this.submitType = submitType;
	}
	public String getSubmitComments() {
		return submitComments;
	}
	public void setSubmitComments(String submitComments) {
		this.submitComments = submitComments;
	}
	public String getFuncId() {
		return funcId;
	}
	public void setFuncId(String funcId) {
		this.funcId = funcId;
	}
	public String getTaskAssignedType() {
		return taskAssignedType;
	}
	public void setTaskAssignedType(String taskAssignedType) {
		this.taskAssignedType = taskAssignedType;
	}
	public String getRoleCodes() {
		return roleCodes;
	}
	public void setRoleCodes(String roleCodes) {
		this.roleCodes = roleCodes;
	}
	public String getAssigneeRole() {
		return assigneeRole;
	}
	public void setAssigneeRole(String assigneeRole) {
		this.assigneeRole = assigneeRole;
	}
	public String getPkValue() {
		return pkValue;
	}
	public void setPkValue(String pkValue) {
		this.pkValue = pkValue;
	}
	public String getTableCode() {
		return tableCode;
	}
	public void setTableCode(String tableCode) {
		this.tableCode = tableCode;
	}
	public String getPkName() {
		return pkName;
	}
	public void setPkName(String pkName) {
		this.pkName = pkName;
	}
	public String getProcessInfoId() {
		return processInfoId;
	}
	public void setProcessInfoId(String processInfoId) {
		this.processInfoId = processInfoId;
	}
	
}
