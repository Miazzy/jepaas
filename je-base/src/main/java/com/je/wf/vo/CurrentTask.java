/**
 * 
 */
package com.je.wf.vo;

import java.util.HashMap;
import java.util.Map;
/**
 * TODO 暂不明确
 * @author marico
 * 2012-5-8 下午1:08:41
 */
public class CurrentTask {
	private String taskId;
	private String taskName;
	private String proccessName;
	private String tableCode;
	private String pkValue;
	private String funcName;
	private String funcCode;
	private String appId; //手机端功能ID
	private String appCode;//手机端功能编码
	private String displayConfigInfo;
	private String createTime;
	//优先级
	private String priority;
	//延迟
	private String delay;
	private Boolean isNew = false;
	//流程部署ID
	private String pdid;
	//流程实例ID
	private String piid;
	/**状态*/
	private String status;
	private Map<String,Object> bean=new HashMap<String,Object>();
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public String getTaskName() {
		return taskName;
	}
	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
	public String getProccessName() {
		return proccessName;
	}
	public void setProccessName(String proccessName) {
		this.proccessName = proccessName;
	}
	public String getTableCode() {
		return tableCode;
	}
	public void setTableCode(String tableCode) {
		this.tableCode = tableCode;
	}
	public String getPkValue() {
		return pkValue;
	}
	public void setPkValue(String pkValue) {
		this.pkValue = pkValue;
	}
	public String getFuncName() {
		return funcName;
	}
	public void setFuncName(String funcName) {
		this.funcName = funcName;
	}
	public String getFuncCode() {
		return funcCode;
	}
	public void setFuncCode(String funcCode) {
		this.funcCode = funcCode;
	}
	public Map<String, Object> getBean() {
		return bean;
	}
	public void setBean(Map<String, Object> bean) {
		this.bean = bean;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getPriority() {
		return priority;
	}
	public void setPriority(String priority) {
		this.priority = priority;
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
	public String getDisplayConfigInfo() {
		return displayConfigInfo;
	}
	public void setDisplayConfigInfo(String displayConfigInfo) {
		this.displayConfigInfo = displayConfigInfo;
	}
	public Boolean getIsNew() {
		return isNew;
	}
	public void setIsNew(Boolean isNew) {
		this.isNew = isNew;
	}
	public String getDelay() {
		return delay;
	}
	public void setDelay(String delay) {
		this.delay = delay;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}

	public String getAppCode() {
		return appCode;
	}

	public void setAppCode(String appCode) {
		this.appCode = appCode;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}
}
