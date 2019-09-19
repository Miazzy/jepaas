package com.je.wf.processVo;
/**
 * 任务流转任务信息
 * @author zhangshuaipeng
 *
 */
public class TaskSubmitInfo {
	/**工作流实例任务id*/
	private String taskId;
	/**活动任务*/
	private String currentTaskName;
	/** 提交类型（通过或退回） */
	private String submitType;
	/** 提交意见 */
	private String submitComments;
	/**详细意见*/
	private String commentDetails;
	/** 目标任务 */
	private String targerTaskName;
	/**目标名称*/
	private String assigeeName="";
	/**目标编码*/
	private String assigeeCode="";
	/**目标主键*/
	private String assigeeId;
	/**目标路径*/
	private String targerTransition;
	/**流程PDID*/
	private String pdid;
	/**流程PIID*/
	private String piid;
	/**业务bean主键*/
	private String beanId;
	/**会签信息*/
	private String countersignInfos;
	/**分支信息*/
	private String forkInfos;
	/**多人处理节点信息*/
	private String batchInfos;
	/**阻塞节点，该节点不会进历史记录， 执行人为系统规定*/
	private String stopTaskNames;
	/**传阅人*/
	private String roundUserId;
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
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
	public String getAssigeeName() {
		return assigeeName;
	}
	public void setAssigeeName(String assigeeName) {
		this.assigeeName = assigeeName;
	}
	public String getAssigeeCode() {
		return assigeeCode;
	}
	public void setAssigeeCode(String assigeeCode) {
		this.assigeeCode = assigeeCode;
	}
	public String getAssigeeId() {
		return assigeeId;
	}
	public void setAssigeeId(String assigeeId) {
		this.assigeeId = assigeeId;
	}
	public String getPdid() {
		return pdid;
	}
	public void setPdid(String pdid) {
		this.pdid = pdid;
	}

	public String getBeanId() {
		return beanId;
	}
	public void setBeanId(String beanId) {
		this.beanId = beanId;
	}
	public String getTargerTaskName() {
		return targerTaskName;
	}
	public void setTargerTaskName(String targerTaskName) {
		this.targerTaskName = targerTaskName;
	}
	public String getTargerTransition() {
		return targerTransition;
	}
	public void setTargerTransition(String targerTransition) {
		this.targerTransition = targerTransition;
	}
	public String getPiid() {
		return piid;
	}
	public void setPiid(String piid) {
		this.piid = piid;
	}
	public String getCurrentTaskName() {
		return currentTaskName;
	}
	public void setCurrentTaskName(String currentTaskName) {
		this.currentTaskName = currentTaskName;
	}
	public String getCountersignInfos() {
		return countersignInfos;
	}
	public void setCountersignInfos(String countersignInfos) {
		this.countersignInfos = countersignInfos;
	}
	public String getForkInfos() {
		return forkInfos;
	}
	public void setForkInfos(String forkInfos) {
		this.forkInfos = forkInfos;
	}
	public String getStopTaskNames() {
		return stopTaskNames;
	}
	public void setStopTaskNames(String stopTaskNames) {
		this.stopTaskNames = stopTaskNames;
	}
	public String getCommentDetails() {
		return commentDetails;
	}
	public void setCommentDetails(String commentDetails) {
		this.commentDetails = commentDetails;
	}
	public String getBatchInfos() {
		return batchInfos;
	}
	public void setBatchInfos(String batchInfos) {
		this.batchInfos = batchInfos;
	}
	public String getRoundUserId() {
		return roundUserId;
	}
	public void setRoundUserId(String roundUserId) {
		this.roundUserId = roundUserId;
	}


}
