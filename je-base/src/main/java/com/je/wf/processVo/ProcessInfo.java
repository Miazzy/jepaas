package com.je.wf.processVo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.je.core.util.StringUtil;
/**
 * 流程信息VO
 * @author zhangshuaipeng
 *
 */
public class ProcessInfo implements Serializable{
	/**
	 *
	 */
	private static final long serialVersionUID = 5102890565876781832L;
	/**主键*/
	private String id;
	/**流程部署id*/
	private String deploymentId;
	/**流程定义id*/
	private String processDefinitionId;
	/**流程名称*/
	private String processName;
	/**流程英文名称*/
	private String processEnName;
	/**流程KEY*/
	private String processKey;
	/**流程分类*/
	private String processFlName;
	/**流程分类编码*/
	private String processFlCode;
	/**功能主键*/
	private String funcId;
	/**功能编码*/
	private String funcCode;
	/**功能名称*/
	private String funcName;
	/**附属功能主键*/
	private String fsFuncIds;
	/**附属功能编码*/
	private String fsFuncCodes;
	/**附属功能名称*/
	private String fsFuncNames;
	/**表编码*/
	private String tableCode;
	/**视图表编码*/
	private String viewTableCode;
	/**表主键编码*/
	private String pkCode;
	/**回执方式*/
	private String messageType="";
	/**是否回退*/
	private Boolean rollbackAble;
	/**是否取回*/
	private Boolean withdrawAble;
	/**是否可转办*/
	private Boolean transmitAble;
	/**是否可催办*/
	private Boolean warnAble;
	/**是否可撤销*/
	private Boolean callAble;
	/**可委托*/
	private Boolean entrustAble;
	/**是否作废*/
	private Boolean suspend=false;

	/**是否发起*/
	private Boolean sponsorAble;
	/***是否任何人启动*/
	private Boolean anyoneStartAble;
	/**是否启用*/
	private Boolean enabled=true;
	/**多表单*/
	private Boolean moreForm=false;
	/**审批记录状态展示*/
	private Boolean historyStatus=true;
	/**审批记录耗时展示*/
	private Boolean historyTime=true;
	/**简易审批*/
	private Boolean easyApprove=false;
	/**简易审批*/
	private Boolean easySponsor=false;
	/**人员预定义*/
	private Boolean userDiy=false;
	/**显示模版*/
	private String displayConfigInfo;
	/**流程组*/
	private String processGroup;
	/**启动角色组*/
	private String startRoles;
	/**手机App信息*/
	private List<AppInfo> appInfos;
	/**是否是系统流程*/
	private Boolean sysProcess=true;
	/**系统流程名称*/
	private String sysProcessName;
	/**系统流程KEY*/
	private String sysProcessKey;
	/**定制客户公司名称*/
	private String saasJtgsMc;
	/**定制客户公司ID*/
	private String saasJtgsId;
	/**定制租户公司名称*/
	private String saasZhMc;
	/**定制租户公司ID*/
	private String saasZhId;
	/**
	 *  管理员
	 */
	private String adminName;
	/**
	 * 管理员ID
	 */
	private String adminId;
	/**
	 * 流程结束业务处理
	 */
	private List<DiyEventInfo> diyEventInfos=new ArrayList<DiyEventInfo>();
	private Map<String,String> messgeTemplates=new HashMap<String,String>();
	/**
	 * 启动表达式
	 */
	private String startExp;
	private String startExpFn;
//	/**mxgraphXML信息*/
//	private String xmlContent;
	/**结束节点集合*/
	private List<TaskNodeInfo> endTaskNodes=new ArrayList<TaskNodeInfo>();
	/**根据任务名称获取任务*/
	private Map<String,TaskNodeInfo> taskNodeInfos=new HashMap<String,TaskNodeInfo>();
	private Map<String,ButtonInfo> buttonInfos=new HashMap<String,ButtonInfo>();
	/**获取第一个任务节点*/
	private String firstTaskNode;
	/**流程流转中赋值字段，主要用于操作后赋值表单*/
	private Set<String> changeFields=new HashSet<String>();
	/**流程所有的任务节点名称  按顺序*/
	private List<String> taskNodeNames=new ArrayList<String>();
	private List<String> allTaskNodeNames=new ArrayList<String>();
	private Map<String,List<EventInfo>> eventInfos=new HashMap<String,List<EventInfo>>();
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getDeploymentId() {
		return deploymentId;
	}
	public void setDeploymentId(String deploymentId) {
		this.deploymentId = deploymentId;
	}
	public String getProcessDefinitionId() {
		return processDefinitionId;
	}
	public void setProcessDefinitionId(String processDefinitionId) {
		this.processDefinitionId = processDefinitionId;
	}
	public String getProcessName() {
		return processName;
	}
	public void setProcessName(String processName) {
		this.processName = processName;
	}
	public String getProcessKey() {
		return processKey;
	}
	public void setProcessKey(String processKey) {
		this.processKey = processKey;
	}
	public String getFuncId() {
		return funcId;
	}
	public void setFuncId(String funcId) {
		this.funcId = funcId;
	}
	public String getFuncCode() {
		return funcCode;
	}
	public void setFuncCode(String funcCode) {
		this.funcCode = funcCode;
	}
	public String getFuncName() {
		return funcName;
	}
	public void setFuncName(String funcName) {
		this.funcName = funcName;
	}
	public String getFsFuncIds() {
		return fsFuncIds;
	}
	public void setFsFuncIds(String fsFuncIds) {
		this.fsFuncIds = fsFuncIds;
	}
	public String getFsFuncCodes() {
		return fsFuncCodes;
	}
	public void setFsFuncCodes(String fsFuncCodes) {
		this.fsFuncCodes = fsFuncCodes;
	}
	public String getFsFuncNames() {
		return fsFuncNames;
	}
	public void setFsFuncNames(String fsFuncNames) {
		this.fsFuncNames = fsFuncNames;
	}
	public String getTableCode() {
		return tableCode;
	}
	public void setTableCode(String tableCode) {
		this.tableCode = tableCode;
	}
	public String getPkCode() {
		return pkCode;
	}
	public void setPkCode(String pkCode) {
		this.pkCode = pkCode;
	}
	public String getMessageType() {
		return messageType;
	}
	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}
	public Boolean getRollbackAble() {
		return rollbackAble;
	}
	public void setRollbackAble(Boolean rollbackAble) {
		this.rollbackAble = rollbackAble;
	}
	public Boolean getWithdrawAble() {
		return withdrawAble;
	}
	public void setWithdrawAble(Boolean withdrawAble) {
		this.withdrawAble = withdrawAble;
	}
	public Boolean getAnyoneStartAble() {
		return anyoneStartAble;
	}
	public void setAnyoneStartAble(Boolean anyoneStartAble) {
		this.anyoneStartAble = anyoneStartAble;
	}
	public Boolean getMoreForm() {
		return moreForm;
	}
	public void setMoreForm(Boolean moreForm) {
		this.moreForm = moreForm;
	}
	public String getProcessGroup() {
		return processGroup;
	}
	public void setProcessGroup(String processGroup) {
		this.processGroup = processGroup;
	}
	public String getStartRoles() {
		return startRoles;
	}
	public void setStartRoles(String startRoles) {
		this.startRoles = startRoles;
	}
	public String getFirstTaskNode() {
		return firstTaskNode;
	}
	public void setFirstTaskNode(String firstTaskNode) {
		this.firstTaskNode = firstTaskNode;
	}
	@JsonIgnore
	public List<String> getTaskNodeNames() {
		return taskNodeNames;
	}
	public void setTaskNodeNames(List<String> taskNodeNames) {
		this.taskNodeNames = taskNodeNames;
	}
	public void pushTaskNode(TaskNodeInfo taskNode){
		this.taskNodeNames.add(taskNode.getName());
		this.taskNodeInfos.put(taskNode.getName(), taskNode);
	}
	public void pushButton(ButtonInfo buttonInfo){
		this.buttonInfos.put(buttonInfo.getCode(), buttonInfo);
	}
	@JsonIgnore
	public TaskNodeInfo getTaskNode(String taskName){
		return taskNodeInfos.get(taskName);
	}
	public void pushEvents(EventInfo eventInfo){
		List<EventInfo> events=eventInfos.get(eventInfo.getEventType());
		if(events!=null){
			events.add(eventInfo);
		}else{
			events=new ArrayList<EventInfo>();
			events.add(eventInfo);
			eventInfos.put(eventInfo.getEventType(), events);
		}
	}
	@JsonIgnore
	public List<EventInfo> getEvents(String eventType){
		return eventInfos.get(eventType);
	}
	public Map<String, TaskNodeInfo> getTaskNodeInfos() {
		return taskNodeInfos;
	}
	public void setTaskNodeInfos(Map<String, TaskNodeInfo> taskNodeInfos) {
		this.taskNodeInfos = taskNodeInfos;
	}
	public Map<String, ButtonInfo> getButtonInfos() {
		return buttonInfos;
	}
	public void setButtonInfos(Map<String, ButtonInfo> buttonInfos) {
		this.buttonInfos = buttonInfos;
	}
//	@JsonIgnore
//	public String getXmlContent() {
//		return xmlContent;
//	}
//	public void setXmlContent(String xmlContent) {
//		this.xmlContent = xmlContent;
//	}
	public Boolean getSponsorAble() {
		return sponsorAble;
	}
	public void setSponsorAble(Boolean sponsorAble) {
		this.sponsorAble = sponsorAble;
	}
	public Map<String, List<EventInfo>> getEventInfos() {
		return eventInfos;
	}
	public void setEventInfos(Map<String, List<EventInfo>> eventInfos) {
		this.eventInfos = eventInfos;
	}
	public List<TaskNodeInfo> getEndTaskNodes() {
		return endTaskNodes;
	}
	public void setEndTaskNodes(List<TaskNodeInfo> endTaskNodes) {
		this.endTaskNodes = endTaskNodes;
	}
	/**
	 * 根据前任务节点获取结束节点
	 * @param deployTask
	 * @return
	 */
	public TaskNodeInfo getEndTask(TaskNodeInfo deployTask){
		String afterTaskNames=deployTask.getAfterTaskNames();
		if(StringUtil.isNotEmpty(afterTaskNames)){
			for(String taskName:afterTaskNames.split(",")){
				TaskNodeInfo endTask=getEndTask(taskName);
				if(endTask!=null){
					return endTask;
				}
			}
		}
		return null;
	}
	/**
	 * 根据节点名称获取结束节点
	 * @param taskName
	 * @return
	 */
	public TaskNodeInfo getEndTask(String taskName){
		for(TaskNodeInfo endTask:this.endTaskNodes){
			if(taskName.equals(endTask.getName())){
				return endTask;
			}
		}
		return null;
	}

	/**
	 * 获取指定的APK下的app信息
	 * @param apkId
	 * @return
	 */
	public AppInfo findAppInfo(String apkId){
		if(appInfos!=null && StringUtil.isNotEmpty(apkId)){
			AppInfo apkAppInfo=null;
			for(AppInfo appInfo:appInfos){
				if(apkId.equalsIgnoreCase(appInfo.getApkId())){
					apkAppInfo=appInfo;
					break;
				}
			}
			return apkAppInfo;
		}else{
			return null;
		}
	}
	public void putMessageTemplate(String key,String value){
		this.messgeTemplates.put(key, value);
	}
	public String getMessage(String key){
		return this.messgeTemplates.get(key);
	}
	public Set<String> getChangeFields() {
		return changeFields;
	}
	public void setChangeFields(Set<String> changeFields) {
		this.changeFields = changeFields;
	}
	public String getDisplayConfigInfo() {
		return displayConfigInfo;
	}
	public void setDisplayConfigInfo(String displayConfigInfo) {
		this.displayConfigInfo = displayConfigInfo;
	}
	public String getStartExp() {
		return startExp;
	}
	public void setStartExp(String startExp) {
		this.startExp = startExp;
	}
	public Boolean getEnabled() {
		return enabled;
	}
	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}
	public Boolean getSuspend() {
		return suspend;
	}
	public void setSuspend(Boolean suspend) {
		this.suspend = suspend;
	}
	public List<DiyEventInfo> getDiyEventInfos() {
		return diyEventInfos;
	}
	public void setDiyEventInfos(List<DiyEventInfo> diyEventInfos) {
		this.diyEventInfos = diyEventInfos;
	}
	public String getStartExpFn() {
		return startExpFn;
	}
	public void setStartExpFn(String startExpFn) {
		this.startExpFn = startExpFn;
	}
	public List<AppInfo> getAppInfos() {
		return appInfos;
	}
	public void setAppInfos(List<AppInfo> appInfos) {
		this.appInfos = appInfos;
	}
	public Boolean getHistoryStatus() {
		return historyStatus;
	}
	public void setHistoryStatus(Boolean historyStatus) {
		this.historyStatus = historyStatus;
	}
	public Boolean getHistoryTime() {
		return historyTime;
	}
	public void setHistoryTime(Boolean historyTime) {
		this.historyTime = historyTime;
	}
	public Boolean getEasyApprove() {
		return easyApprove;
	}
	public void setEasyApprove(Boolean easyApprove) {
		this.easyApprove = easyApprove;
	}
	public Boolean getSysProcess() {
		return sysProcess;
	}
	public void setSysProcess(Boolean sysProcess) {
		this.sysProcess = sysProcess;
	}
	public String getSysProcessName() {
		return sysProcessName;
	}
	public void setSysProcessName(String sysProcessName) {
		this.sysProcessName = sysProcessName;
	}
	public String getSysProcessKey() {
		return sysProcessKey;
	}
	public void setSysProcessKey(String sysProcessKey) {
		this.sysProcessKey = sysProcessKey;
	}
	public String getSaasJtgsMc() {
		return saasJtgsMc;
	}
	public void setSaasJtgsMc(String saasJtgsMc) {
		this.saasJtgsMc = saasJtgsMc;
	}
	public String getSaasJtgsId() {
		return saasJtgsId;
	}
	public void setSaasJtgsId(String saasJtgsId) {
		this.saasJtgsId = saasJtgsId;
	}
	public String getViewTableCode() {
		return viewTableCode;
	}
	public void setViewTableCode(String viewTableCode) {
		this.viewTableCode = viewTableCode;
	}
	public String getProcessFlName() {
		return processFlName;
	}
	public void setProcessFlName(String processFlName) {
		this.processFlName = processFlName;
	}
	public String getProcessFlCode() {
		return processFlCode;
	}
	public void setProcessFlCode(String processFlCode) {
		this.processFlCode = processFlCode;
	}
	public String getSaasZhMc() {
		return saasZhMc;
	}
	public void setSaasZhMc(String saasZhMc) {
		this.saasZhMc = saasZhMc;
	}
	public String getSaasZhId() {
		return saasZhId;
	}
	public void setSaasZhId(String saasZhId) {
		this.saasZhId = saasZhId;
	}
	public Boolean getTransmitAble() {
		return transmitAble;
	}
	public void setTransmitAble(Boolean transmitAble) {
		this.transmitAble = transmitAble;
	}
	public Boolean getWarnAble() {
		return warnAble;
	}
	public void setWarnAble(Boolean warnAble) {
		this.warnAble = warnAble;
	}
	public Boolean getCallAble() {
		return callAble;
	}
	public void setCallAble(Boolean callAble) {
		this.callAble = callAble;
	}
	public Boolean getEntrustAble() {
		return entrustAble;
	}
	public void setEntrustAble(Boolean entrustAble) {
		this.entrustAble = entrustAble;
	}
	public Boolean getUserDiy() {
		return userDiy;
	}
	public void setUserDiy(Boolean userDiy) {
		this.userDiy = userDiy;
	}
	public String getProcessEnName() {
		return processEnName;
	}
	public void setProcessEnName(String processEnName) {
		this.processEnName = processEnName;
	}
	public String getAdminId() {
		return adminId;
	}
	public void setAdminId(String adminId) {
		this.adminId = adminId;
	}
	public String getAdminName() {
		return adminName;
	}
	public void setAdminName(String adminName) {
		this.adminName = adminName;
	}
	public Boolean getEasySponsor() {
		return easySponsor;
	}
	public void setEasySponsor(Boolean easySponsor) {
		this.easySponsor = easySponsor;
	}

}
