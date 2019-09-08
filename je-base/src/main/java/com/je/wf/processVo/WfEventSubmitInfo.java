package com.je.wf.processVo;

import com.je.core.util.bean.DynaBean;

/**
 * 事件执行VO
 * @author zhangshuaipeng
 *
 */
public class WfEventSubmitInfo {
	/** 任务ID */
	private String taskId;
	/**任务名称*/
	private String currentTaskName;
	/** 目标任务 */
	private String targerTaskName;
	/** 目标路线 */
	private String targerTransition;
	/** 提交类型（通过或退回） */
	private String submitType;
	/** 提交意见 */
	private String submitComments;
	/**目标名称*/
	private String assigeeName="";
	/**目标编码*/
	private String assigeeCode="";
	/**目标主键*/
	private String assigeeId;
	/**功能编码*/
	private String funcCode;
	/** 执行Bean */
	private DynaBean dynaBean;
	/**
	 * 获取执行任务ID
	 * @return
	 */
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	/**
	 * 获取执行目标节点名称
	 * @return
	 */
	public String getTargerTaskName() {
		return targerTaskName;
	}
	public void setTargerTaskName(String targerTaskName) {
		this.targerTaskName = targerTaskName;
	}
	/**
	 * 获取执行目标路径
	 * @return
	 */
	public String getTargerTransition() {
		return targerTransition;
	}
	public void setTargerTransition(String targerTransition) {
		this.targerTransition = targerTransition;
	}
	/**
	 * 获取执行的操作
	 * @return
	 */
	public String getSubmitType() {
		return submitType;
	}
	public void setSubmitType(String submitType) {
		this.submitType = submitType;
	}
	/**
	 * 获取审批意见
	 * @return
	 */
	public String getSubmitComments() {
		return submitComments;
	}
	public void setSubmitComments(String submitComments) {
		this.submitComments = submitComments;
	}
	/**
	 * 任务接受人员名称
	 * @return
	 */
	public String getAssigeeName() {
		return assigeeName;
	}
	public void setAssigeeName(String assigeeName) {
		this.assigeeName = assigeeName;
	}
	/**
	 * 任务接受人员编码
	 * @return
	 */
	public String getAssigeeCode() {
		return assigeeCode;
	}
	public void setAssigeeCode(String assigeeCode) {
		this.assigeeCode = assigeeCode;
	}
	/**
	 * 任务接受人员ID
	 * @return
	 */
	public String getAssigeeId() {
		return assigeeId;
	}
	public void setAssigeeId(String assigeeId) {
		this.assigeeId = assigeeId;
	}
	/**
	 * 业务Bean
	 * @return
	 */
	public DynaBean getDynaBean() {
		return dynaBean;
	}
	public void setDynaBean(DynaBean dynaBean) {
		this.dynaBean = dynaBean;
	}
	/**
	 * 当前执行节点名称
	 * @return
	 */
	public String getCurrentTaskName() {
		return currentTaskName;
	}
	public void setCurrentTaskName(String currentTaskName) {
		this.currentTaskName = currentTaskName;
	}
	public String getFuncCode() {
		return funcCode;
	}
	public void setFuncCode(String funcCode) {
		this.funcCode = funcCode;
	}
	
}
