package com.je.wf.processVo;

import java.util.List;
import java.util.Map;

import com.je.core.util.bean.DynaBean;

/**
 * 任务送交信息
 * @author zhangshuaipeng
 *
 */
public class WfTaskInfo {
	/**
	 * 当前执行任务Id
	 */
	private String taskId;
	/**
	 * 当前执行任务名称
	 */
	private String currentTaskName;
	/**
	 * 目标任务名称
	 */
	private String targerTaskName;
	/**
	 * 执行目标人员集合      键-->任务名    值：人员集合
	 */
	private Map<String,List<DynaBean>> submitUsers;
	/**
	 * 执行意见
	 */
	private String submitComments;
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public String getCurrentTaskName() {
		return currentTaskName;
	}
	public void setCurrentTaskName(String currentTaskName) {
		this.currentTaskName = currentTaskName;
	}
	public String getTargerTaskName() {
		return targerTaskName;
	}
	public void setTargerTaskName(String targerTaskName) {
		this.targerTaskName = targerTaskName;
	}
	public Map<String, List<DynaBean>> getSubmitUsers() {
		return submitUsers;
	}
	public void setSubmitUsers(Map<String, List<DynaBean>> submitUsers) {
		this.submitUsers = submitUsers;
	}
	public String getSubmitComments() {
		return submitComments;
	}
	public void setSubmitComments(String submitComments) {
		this.submitComments = submitComments;
	}
	
}
