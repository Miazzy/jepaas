package com.je.wf.vo;

/**
 *  TODO 暂不明确
 * @author chenmeng
 * 2012-4-25 下午1:16:15
 */
public class RollbackInfo {
	private String rollbackAssignee;
	private String rollbackTaskName;
	private String rollbackTaskId;
	
	public String getRollbackAssignee() {
		return rollbackAssignee;
	}
	public void setRollbackAssignee(String rollbackAssignee) {
		this.rollbackAssignee = rollbackAssignee;
	}
	public String getRollbackTaskName() {
		return rollbackTaskName;
	}
	public void setRollbackTaskName(String rollbackTaskName) {
		this.rollbackTaskName = rollbackTaskName;
	}
	public String getRollbackTaskId() {
		return rollbackTaskId;
	}
	public void setRollbackTaskId(String rollbackTaskId) {
		this.rollbackTaskId = rollbackTaskId;
	}
}
