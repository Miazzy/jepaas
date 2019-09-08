package com.je.wf.vo;

import java.util.Date;

/**
 *  TODO 暂不明确
 * @author chenmeng
 * 2012-3-19 上午10:58:47
 */
public class ProcessTask {
	private String id; // 任务ID
	private String nodeName; // 任务结点名称
	private String nodeDesc; // 任务结点描述
	private String assignee; // 任务指定人
	private Date createTime; // 任务创建时间
	private Date duedate; // 任务处理用时
	private String modelId; // 业务对象ID（流程变量）
	private String modelType; // 业务对象类型（流程变量）
	private String state; // 任务状态
	private String candidates; // 待选组
	
	/** 任务结点ID */
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	/** 任务结点名称 */
	public String getNodeName() {
		return nodeName;
	}
	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}
	/** 任务结点描述 */
	public String getNodeDesc() {
		return nodeDesc;
	}
	public void setNodeDesc(String nodeDesc) {
		this.nodeDesc = nodeDesc;
	}
	/** 任务指定人 */
	public String getAssignee() {
		return assignee;
	}
	public void setAssignee(String assignee) {
		this.assignee = assignee;
	}
	/** 任务创建时间 */
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	/** 任务处理用时 */
	public Date getDuedate() {
		return duedate;
	}
	public void setDuedate(Date duedate) {
		this.duedate = duedate;
	}
	/** 业务对象ID（流程变量） */
	public String getModelId() {
		return modelId;
	}
	public void setModelId(String modelId) {
		this.modelId = modelId;
	}
	/** 业务对象类型（流程变量） */
	public String getModelType() {
		return modelType;
	}
	public void setModelType(String modelType) {
		this.modelType = modelType;
	}
	/** 任务状态 */
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	/** 待选组 */
	public String getCandidates() {
		return candidates;
	}
	public void setCandidates(String candidates) {
		this.candidates = candidates;
	}

	
}
