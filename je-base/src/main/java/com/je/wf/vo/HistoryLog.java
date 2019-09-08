/**
 *
 */
package com.je.wf.vo;

import java.util.Date;

import net.sf.json.JSONArray;

/**
 * 工作流历史留痕VO
 * @author chenmeng
 * 2012-3-8 下午5:29:05
 */
public class HistoryLog {
	private String taskId; //如果是任务节点，则有taskId
	private String nodeName; //活动节点名称
	private String nodeType;//活动节点类型
	private Date startTime; //节点开始时间
	private Date endTime; //节点完成时间
	private String transName; //流转路径
	private String transCode; // 流转路径code
	private Long duration; //节点停留用时（毫秒）
	private String durationDesc; // 节点停留用时描述（动态）
	private String diyAssignee;
	private String assignee; //指定执行人
	private String assigneeCode; //指定执行人用户编码
	private String activeAssignee;//正在执行人的任务信息
	private String comments = ""; //审批意见
	private String roundComments = ""; //传阅信息
	private String typeDesc; //节点类型
	private String operate="no";//操作  open高亮    close取消高亮   no无操作
	private String targerTransition;
	private JSONArray offLines=new JSONArray();
	private Integer taskRowspan=1;
	private String commentDetailId="";//详细意见ID
	private String fileVals;//附件值
	private String status;
	private String statusName;
	private String statusStyle="";
	private String timeStyle="";
	public String getNodeName() {
		return nodeName;
	}
	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

	public String getNodeType() {
		return nodeType;
	}
	public void setNodeType(String nodeType) {
		this.nodeType = nodeType;
	}
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public String getTransName() {
		return transName;
	}
	public void setTransName(String transName) {
		this.transName = transName;
	}
	public Long getDuration() {
		return duration;
	}
	public void setDuration(Long duration) {
		this.duration = duration;
	}
	public String getAssignee() {
		return assignee;
	}
	public void setAssignee(String assignee) {
		this.assignee = assignee;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public String getDurationDesc() {
		return durationDesc;
	}
	public void setDurationDesc(String durationDesc) {
		this.durationDesc = durationDesc;
	}
	public String getTypeDesc() {
		return typeDesc;
	}
	public void setTypeDesc(String typeDesc) {
		this.typeDesc = typeDesc;
	}
	public String getAssigneeCode() {
		return assigneeCode;
	}
	public void setAssigneeCode(String assigneeCode) {
		this.assigneeCode = assigneeCode;
	}
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public String getTransCode() {
		return transCode;
	}
	public Integer getTaskRowspan() {
		return taskRowspan;
	}
	public void setTaskRowspan(Integer taskRowspan) {
		this.taskRowspan = taskRowspan;
	}
	public void setTransCode(String transCode) {
		this.transCode = transCode;
	}
	public String getRoundComments() {
		return roundComments;
	}
	public void setRoundComments(String roundComments) {
		this.roundComments = roundComments;
	}
	public String getOperate() {
		return operate;
	}
	public void setOperate(String operate) {
		this.operate = operate;
	}
	public String getTargerTransition() {
		return targerTransition;
	}
	public void setTargerTransition(String targerTransition) {
		this.targerTransition = targerTransition;
	}
	public String getDiyAssignee() {
		return diyAssignee;
	}
	public void setDiyAssignee(String diyAssignee) {
		this.diyAssignee = diyAssignee;
	}
	public String getCommentDetailId() {
		return commentDetailId;
	}
	public void setCommentDetailId(String commentDetailId) {
		this.commentDetailId = commentDetailId;
	}
	public JSONArray getOffLines() {
		return offLines;
	}
	public void setOffLines(JSONArray offLines) {
		this.offLines = offLines;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getStatusName() {
		return statusName;
	}
	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}
	public String getStatusStyle() {
		return statusStyle;
	}
	public void setStatusStyle(String statusStyle) {
		this.statusStyle = statusStyle;
	}
	public String getTimeStyle() {
		return timeStyle;
	}
	public void setTimeStyle(String timeStyle) {
		this.timeStyle = timeStyle;
	}
	public String getFileVals() {
		return fileVals;
	}
	public void setFileVals(String fileVals) {
		this.fileVals = fileVals;
	}

	public String getActiveAssignee() {
		return activeAssignee;
	}

	public void setActiveAssignee(String activeAssignee) {
		this.activeAssignee = activeAssignee;
	}
}
