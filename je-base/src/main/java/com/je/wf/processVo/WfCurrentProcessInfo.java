package com.je.wf.processVo;

import java.util.Set;

/**
 * 流程活动信息VO
 * @author zhangshuaipeng
 *
 */
public class WfCurrentProcessInfo {
	/**
	 * 当前活动节点
	 */
	private String[] currentTaskNames;
	/**
	 * 当前执行活动名称
	 */
	private String taskName;
	/**
	 * 当前执行活动Id
	 */
	private String taskId;
	/**
	 * 会签按钮
	 */
	private String[] countersignBtns;
	/**
	 * 流程状态
	 */
	private String audFlag;
	/**
	 * 可执行按钮
	 */
	private Set<String> currentBtns;
	public String getTaskName() {
		return taskName;
	}
	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public String[] getCurrentTaskNames() {
		return currentTaskNames;
	}
	public void setCurrentTaskNames(String[] currentTaskNames) {
		this.currentTaskNames = currentTaskNames;
	}
	public String[] getCountersignBtns() {
		return countersignBtns;
	}
	public void setCountersignBtns(String[] countersignBtns) {
		this.countersignBtns = countersignBtns;
	}
	public Set<String> getCurrentBtns() {
		return currentBtns;
	}
	public void setCurrentBtns(Set<String> currentBtns) {
		this.currentBtns = currentBtns;
	}
	public String getAudFlag() {
		return audFlag;
	}
	public void setAudFlag(String audFlag) {
		this.audFlag = audFlag;
	}
	
	
}
