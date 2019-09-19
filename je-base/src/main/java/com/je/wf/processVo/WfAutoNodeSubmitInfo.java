package com.je.wf.processVo;

import com.je.core.util.bean.DynaBean;
import com.je.rbac.model.EndUser;

/**
 * 工作流自动节点提交模版
 * @author zhangshuaipeng
 *
 */
public class WfAutoNodeSubmitInfo {
	/**流程部署信息*/
	private ProcessInfo processInfo;
	/**当前执行节点*/
	private TaskNodeInfo currentTask;
	/**业务Bean*/
	private DynaBean dynaBean;
	/** 提交类型（通过或退回） */
	private String submitType;
	/** 提交意见 */
	private String submitComments;
	/**流程PDID*/
	private String pdid;
	/**流程PIID*/
	private String piid;
	/**当前执行人*/
	private EndUser currentUser;
	public ProcessInfo getProcessInfo() {
		return processInfo;
	}
	public void setProcessInfo(ProcessInfo processInfo) {
		this.processInfo = processInfo;
	}
	public TaskNodeInfo getCurrentTask() {
		return currentTask;
	}
	public void setCurrentTask(TaskNodeInfo currentTask) {
		this.currentTask = currentTask;
	}
	public DynaBean getDynaBean() {
		return dynaBean;
	}
	public void setDynaBean(DynaBean dynaBean) {
		this.dynaBean = dynaBean;
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
	public EndUser getCurrentUser() {
		return currentUser;
	}
	public void setCurrentUser(EndUser currentUser) {
		this.currentUser = currentUser;
	}
	
}
