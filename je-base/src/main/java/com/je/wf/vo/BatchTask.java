package com.je.wf.vo;
/**
 * 批量审批的任务节点
 * @author zhangshuaipeng
 *
 */
public class BatchTask {
	//流程部署主键
	private String processId;
	//任务名称
	private String taskName;
	//任务名称
	private String processName;
	//展示名称
	private String showName;
	/** 任务委派方式 */
	private String taskAssignedType;
	/** 任务提交目标 */
	private String submitCandidate;
	public String getProcessId() {
		return processId;
	}
	public void setProcessId(String processId) {
		this.processId = processId;
	}
	public String getTaskName() {
		return taskName;
	}
	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
	public String getProcessName() {
		return processName;
	}
	public void setProcessName(String processName) {
		this.processName = processName;
	}
	public String getShowName() {
		return showName;
	}
	public void setShowName(String showName) {
		this.showName = showName;
	}
	public String getTaskAssignedType() {
		return taskAssignedType;
	}
	public void setTaskAssignedType(String taskAssignedType) {
		this.taskAssignedType = taskAssignedType;
	}
	public String getSubmitCandidate() {
		return submitCandidate;
	}
	public void setSubmitCandidate(String submitCandidate) {
		this.submitCandidate = submitCandidate;
	}
	
}
