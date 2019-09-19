package com.je.wf.processVo;

import java.util.ArrayList;
import java.util.List;

import com.je.core.util.bean.DynaBean;
import com.je.rbac.model.EndUser;

public class WfAutoJointInfo {
	/**业务数据*/
	private DynaBean dynaBean;
	/**流程信息*/
	private ProcessInfo processInfo;
	/**候选人员*/
	private List<DynaBean> users=new ArrayList<DynaBean>();
	/**当前活动节点*/
	private TaskNodeInfo currentTask;
	/**候选节点信息*/
	private TaskNodeInfo jointTask;
	/**当前执行人*/
	private EndUser currentUser;
	public DynaBean getDynaBean() {
		return dynaBean;
	}
	public void setDynaBean(DynaBean dynaBean) {
		this.dynaBean = dynaBean;
	}
	public ProcessInfo getProcessInfo() {
		return processInfo;
	}
	public void setProcessInfo(ProcessInfo processInfo) {
		this.processInfo = processInfo;
	}
	public List<DynaBean> getUsers() {
		return users;
	}
	public void setUsers(List<DynaBean> users) {
		this.users = users;
	}
	public TaskNodeInfo getCurrentTask() {
		return currentTask;
	}
	public void setCurrentTask(TaskNodeInfo currentTask) {
		this.currentTask = currentTask;
	}
	public TaskNodeInfo getJointTask() {
		return jointTask;
	}
	public void setJointTask(TaskNodeInfo jointTask) {
		this.jointTask = jointTask;
	}
	public EndUser getCurrentUser() {
		return currentUser;
	}
	public void setCurrentUser(EndUser currentUser) {
		this.currentUser = currentUser;
	}
	
}
