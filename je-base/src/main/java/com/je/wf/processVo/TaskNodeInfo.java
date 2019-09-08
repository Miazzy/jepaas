package com.je.wf.processVo;

import com.je.core.util.StringUtil;
import org.apache.commons.lang3.ArrayUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 任务信息VO
 * @author zhangshuaipeng
 *
 */
public class TaskNodeInfo implements Serializable{
	/**
	 *
	 */
	private static final long serialVersionUID = 4754147179451186490L;
	/**展示节点名称*/
	private String label;
	/**任务节点名称*/
	private String name;
	/**节点类型*/
	private String type;
	/**会签类型*/
	private String countersignType;
	/**会签负责人*/
	private String countersignAssgineName;
	/**会签负责人ID*/
	private String countersignAssgineId;
	/**会签负责人ID*/
	private Double countersignProportion=0.0;
	/**会签是否按照顺序审批*/
	private Boolean countersignOrder=false;
	/**是否需全部参与人给予意见   (主要针对负责人制度)*/
	private Boolean countersignAll=true;
	/**是否自定义选择人员*/
	private Boolean selectUser=false;
	/**会签结果配置*/
	private Map<String, CountersignTranInfo> countersignConfigs=new HashMap<>();
	/**委托目标配置*/
	private List<AssgineInfo> assgineConfig=new ArrayList<>();
	/**可编辑字段*/
	private String editFields;
	/**不可编辑字段*/
	private String readOnlyFields;
	/**隐藏字段*/
	private String hideFields;
	/**显示字段*/
	private String showFields;
	/**必填字段*/
	private String notNullFields;
	/**可编辑子功能*/
	private String editChildren;
	/**隐藏子功能*/
	private String hideChildren;
	/**显示子功能*/
	private String showChildren;
	/**可用按钮*/
	private String enabledButtons;
	/**赋值字段配置*/
	private String fieldsConfig;
	/**是否可以结束流程*/
	private Boolean endProcess;
	/**是否可转办*/
	private Boolean transmitAble;
	/**是否可委托*/
	private Boolean entrustAble;
	/**是否可作废*/
	private Boolean suspendAble;
	/**是否可退回根节点*/
	private Boolean returnBackAble;
	/**不提醒**/
	private Boolean warnAble;
	/**是否可驳回*/
	private Boolean rejectAble;
	/**不可退回*/
	private Boolean rollbackDisable;
	/**禁用简易审批*/
	private Boolean noEasy;
	/**表单可编辑*/
	private Boolean formEditAble;
	/**不可取回*/
	private Boolean noWithdrawable;
	/**可直接提交驳回人*/
	private Boolean rejectSubmit;
	/**可直接提交退回人*/
	private Boolean reutrnSubmit;
	/**禁用领取任务*/
	private Boolean noTakeTask;
	/**人员预定义*/
	private Boolean userDiyAble;
	/**启用预定义*/
	private Boolean diyAble;
	/**转交下一步*/
	private Boolean nextDiyUser;
	/**异步树*/
	private Boolean asyncTree;
	/**启用子流程*/
	private Boolean doWfChild;
	/**候选节点人员全选*/
	private Boolean autoSelectAll;
	/**可跳跃*/
	private Boolean jumpAble;
	/**跳跃预定义人员为止*/
	private Boolean jumpDiyUserAble;
	/**跳跃节点定义*/
	private String jumpNodes;
	/**指定bean名称*/
	private String beanName;
	/**执行bean方法*/
	private String beanMethod;
	/**自动候选*/
	private Boolean autoJoint=false;
	/**自动候选来源*/
	private String jointSource;
	/**候选过滤Bean名称*/
	private String jointBeanName;
	/**候选过滤Bean方法*/
	private String jointBeanMethod;
	/**可处理人参考人员*/
	private String doAssgineType;
	/**使用组架构调拨*/
	private Boolean allotDeptUser;
	/**
	 * 自动传阅
	 */
	private Boolean autoRound;
	/**
	 * 可直接提交驳回退回节点后禁用送交按钮
	 */
	private Boolean submitAble;
	/**
	 * 强制预定义
	 */
	private Boolean mustDiyUser;
	/**是否顺序审批*/
	private Boolean doUserSort;
	/**只顺序审批*/
	private Boolean doUserSortOnly;
	private List<TransitionInfo> transitions=new ArrayList<>();
	/**传阅类型*/
	private List<RoundInfo> roundInfos=new ArrayList<>();
	/**预警与延期设置*/
	private List<WarnInfoVo> warnInfos=new ArrayList<>();
	/**驳回节点指定*/
	private String rejectNodes;
	/**下一节点名称    分支  判断可能有N个*/
	private String afterTaskNames;
	/**上一节点名称     聚合  可能有N个*/
	private String beforeTaskNames;
	/**表单标题信息*/
	private String formTitleTemplate;
	/**任务节点事件*/
	private Map<String,List<EventInfo>> eventInfos=new HashMap<>();
	/**任务节点自定义按钮*/
	private Map<String, ButtonInfo> buttonInfos=new HashMap<>();
	/**任务节点事件*/
	private Map<String,List<WfTaskBtnInfo>> taskBtnInfos=new HashMap<>();
	/**任务审批默认意见**/
	private Map<String,String> taskComments=new HashMap<>();
	/**子流程信息*/
	private List<ChildProcessInfo> childProcessInfos=new ArrayList<>();
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEditFields() {
		return editFields;
	}
	public void setEditFields(String editFields) {
		this.editFields = editFields;
	}
	public String getEnabledButtons() {
		return enabledButtons;
	}
	public void setEnabledButtons(String enabledButtons) {
		this.enabledButtons = enabledButtons;
	}
	public String getHideFields() {
		return hideFields;
	}
	public void setHideFields(String hideFields) {
		this.hideFields = hideFields;
	}
	public String getFieldsConfig() {
		return fieldsConfig;
	}
	public void setFieldsConfig(String fieldsConfig) {
		this.fieldsConfig = fieldsConfig;
	}

	public String getAfterTaskNames() {
		return afterTaskNames;
	}
	public void setAfterTaskNames(String afterTaskNames) {
		this.afterTaskNames = afterTaskNames;
	}
	public String getBeforeTaskNames() {
		return beforeTaskNames;
	}
	public void setBeforeTaskNames(String beforeTaskNames) {
		this.beforeTaskNames = beforeTaskNames;
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
	public void pushTaskBtns(WfTaskBtnInfo taskBtnInfo){
		List<WfTaskBtnInfo> btns=taskBtnInfos.get(taskBtnInfo.getBtnCode());
		if(btns!=null){
			btns.add(taskBtnInfo);
		}else{
			btns=new ArrayList<WfTaskBtnInfo>();
			btns.add(taskBtnInfo);
			taskBtnInfos.put(taskBtnInfo.getBtnCode(), btns);
		}
	}
	public List<EventInfo> getEvents(String eventType){
		return eventInfos.get(eventType);
	}

	public List<RoundInfo> getRoundInfos() {
		return roundInfos;
	}
	public void pushRoundInfo(RoundInfo roundInfo) {
		roundInfos.add(roundInfo);
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getFormTitleTemplate() {
		return formTitleTemplate;
	}
	public void setFormTitleTemplate(String formTitleTemplate) {
		this.formTitleTemplate = formTitleTemplate;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public List<TransitionInfo> getTransitions() {
		return transitions;
	}
	public void setTransitions(List<TransitionInfo> transitions) {
		this.transitions = transitions;
	}
	public String getTranistionName(String tranistion){
		String tranistionName="";
		for(TransitionInfo transitionInfo:this.transitions){
			if(transitionInfo.getName().equals(tranistion)){
				tranistionName=transitionInfo.getLabel();
			}
		}
		return tranistionName;
	}
	/**
	 * 根据任务节点获取线code
	 * @param taskName
	 * @return
	 */
	public String getTranistionByTo(String taskName){
		String tranistionName="";
		for(TransitionInfo transitionInfo:this.transitions){
			if(transitionInfo.getTo().equals(taskName)){
				tranistionName=transitionInfo.getName();
			}
		}
		return tranistionName;
	}
	public String getToNameByTranistion(String tranistion){
		String toName="";
		for(TransitionInfo transitionInfo:this.transitions){
			if(transitionInfo.getName().equals(tranistion)){
				toName=transitionInfo.getTo();
			}
		}
		return toName;
	}
	public Boolean getEndProcess() {
		return endProcess;
	}
	public void setEndProcess(Boolean endProcess) {
		this.endProcess = endProcess;
	}
	public String getCountersignType() {
		return countersignType;
	}
	public void setCountersignType(String countersignType) {
		this.countersignType = countersignType;
	}
	public void pushCountersignTranInfo(CountersignTranInfo countersignTranInfo){
		this.countersignConfigs.put(countersignTranInfo.getResult(), countersignTranInfo);
	}
	public CountersignTranInfo getCountersignTranInfo(String result){
		return this.countersignConfigs.get(result);
	}
	public String getCountersignAssgineName() {
		return countersignAssgineName;
	}
	public void setCountersignAssgineName(String countersignAssgineName) {
		this.countersignAssgineName = countersignAssgineName;
	}
	public String getCountersignAssgineId() {
		return countersignAssgineId;
	}
	public void setCountersignAssgineId(String countersignAssgineId) {
		this.countersignAssgineId = countersignAssgineId;
	}
	public Double getCountersignProportion() {
		return countersignProportion;
	}
	public void setCountersignProportion(Double countersignProportion) {
		this.countersignProportion = countersignProportion;
	}

	public Boolean getCountersignAll() {
		return countersignAll;
	}
	public void setCountersignAll(Boolean countersignAll) {
		this.countersignAll = countersignAll;
	}
	public List<AssgineInfo> getTaskAssgineConfig(String taskName, Boolean isCircular){
		List<AssgineInfo> emptyAssgineInfos=new ArrayList<>();
		List<AssgineInfo> haveAssgineInfos=new ArrayList<>();
		for(AssgineInfo assgineInfo:this.assgineConfig){
			if(StringUtil.isEmpty(assgineInfo.getSource())){
				emptyAssgineInfos.add(assgineInfo);
				if(isCircular){
					haveAssgineInfos.add(assgineInfo);
				}
			}else{
				if(StringUtil.isEmpty(taskName))continue;
				String[] sources=assgineInfo.getSource().split(",");
				if(ArrayUtils.contains(sources, taskName)){
					haveAssgineInfos.add(assgineInfo);
				}
			}
		}
		if(haveAssgineInfos.size()>0){
			return haveAssgineInfos;
		}else{
			return emptyAssgineInfos;
		}
	}
	public void setAssgineConfig(List<AssgineInfo> assgineConfig) {
		this.assgineConfig = assgineConfig;
	}
	public Boolean getEntrustAble() {
		return entrustAble;
	}
	public void setEntrustAble(Boolean entrustAble) {
		this.entrustAble = entrustAble;
	}
	public String getShowFields() {
		return showFields;
	}
	public void setShowFields(String showFields) {
		this.showFields = showFields;
	}
	public String getEditChildren() {
		return editChildren;
	}
	public void setEditChildren(String editChildren) {
		this.editChildren = editChildren;
	}
	public String getHideChildren() {
		return hideChildren;
	}
	public void setHideChildren(String hideChildren) {
		this.hideChildren = hideChildren;
	}
	public String getShowChildren() {
		return showChildren;
	}
	public void setShowChildren(String showChildren) {
		this.showChildren = showChildren;
	}
	public String getReadOnlyFields() {
		return readOnlyFields;
	}
	public void setReadOnlyFields(String readOnlyFields) {
		this.readOnlyFields = readOnlyFields;
	}
	public String getNotNullFields() {
		return notNullFields;
	}
	public void setNotNullFields(String notNullFields) {
		this.notNullFields = notNullFields;
	}
	public Boolean getSelectUser() {
		return selectUser;
	}
	public void setSelectUser(Boolean selectUser) {
		this.selectUser = selectUser;
	}
	public void pushButton(ButtonInfo buttonInfo){
		this.buttonInfos.put(buttonInfo.getCode(), buttonInfo);
	}
	public Map<String, ButtonInfo> getButtonInfos() {
		return buttonInfos;
	}
	public void setButtonInfos(Map<String, ButtonInfo> buttonInfos) {
		this.buttonInfos = buttonInfos;
	}
	public String getBeanName() {
		return beanName;
	}
	public void setBeanName(String beanName) {
		this.beanName = beanName;
	}
	public String getBeanMethod() {
		return beanMethod;
	}
	public void setBeanMethod(String beanMethod) {
		this.beanMethod = beanMethod;
	}
	public Boolean getAutoJoint() {
		return autoJoint;
	}
	public void setAutoJoint(Boolean autoJoint) {
		this.autoJoint = autoJoint;
	}
	public String getJointBeanName() {
		return jointBeanName;
	}
	public void setJointBeanName(String jointBeanName) {
		this.jointBeanName = jointBeanName;
	}
	public String getJointBeanMethod() {
		return jointBeanMethod;
	}
	public void setJointBeanMethod(String jointBeanMethod) {
		this.jointBeanMethod = jointBeanMethod;
	}
	public String getJointSource() {
		return jointSource;
	}
	public void setJointSource(String jointSource) {
		this.jointSource = jointSource;
	}
	public Boolean getTransmitAble() {
		return transmitAble;
	}
	public void setTransmitAble(Boolean transmitAble) {
		this.transmitAble = transmitAble;
	}
	public Boolean getCountersignOrder() {
		return countersignOrder;
	}
	public void setCountersignOrder(Boolean countersignOrder) {
		this.countersignOrder = countersignOrder;
	}
	public Boolean getSuspendAble() {
		return suspendAble;
	}
	public void setSuspendAble(Boolean suspendAble) {
		this.suspendAble = suspendAble;
	}
	public Boolean getReturnBackAble() {
		return returnBackAble;
	}
	public void setReturnBackAble(Boolean returnBackAble) {
		this.returnBackAble = returnBackAble;
	}
	public Map<String, String> getTaskComments() {
		return taskComments;
	}
	public void setTaskComments(Map<String, String> taskComments) {
		this.taskComments = taskComments;
	}
	public Boolean getWarnAble() {
		return warnAble;
	}
	public void setWarnAble(Boolean warnAble) {
		this.warnAble = warnAble;
	}
	public Boolean getRejectAble() {
		return rejectAble;
	}
	public void setRejectAble(Boolean rejectAble) {
		this.rejectAble = rejectAble;
	}
	public String getRejectNodes() {
		return rejectNodes;
	}
	public void setRejectNodes(String rejectNodes) {
		this.rejectNodes = rejectNodes;
	}
	public Boolean getRollbackDisable() {
		return rollbackDisable;
	}
	public void setRollbackDisable(Boolean rollbackDisable) {
		this.rollbackDisable = rollbackDisable;
	}
	public List<WarnInfoVo> getWarnInfos() {
		return warnInfos;
	}
	public void setWarnInfos(List<WarnInfoVo> warnInfos) {
		this.warnInfos = warnInfos;
	}
	public Boolean getNoEasy() {
		return noEasy;
	}
	public void setNoEasy(Boolean noEasy) {
		this.noEasy = noEasy;
	}
	public Boolean getFormEditAble() {
		return formEditAble;
	}
	public void setFormEditAble(Boolean formEditAble) {
		this.formEditAble = formEditAble;
	}
	public Boolean getNoWithdrawable() {
		return noWithdrawable;
	}
	public void setNoWithdrawable(Boolean noWithdrawable) {
		this.noWithdrawable = noWithdrawable;
	}
	public String getDoAssgineType() {
		return doAssgineType;
	}
	public void setDoAssgineType(String doAssgineType) {
		this.doAssgineType = doAssgineType;
	}
	public Boolean getRejectSubmit() {
		return rejectSubmit;
	}
	public void setRejectSubmit(Boolean rejectSubmit) {
		this.rejectSubmit = rejectSubmit;
	}
	public Boolean getReutrnSubmit() {
		return reutrnSubmit;
	}
	public void setReutrnSubmit(Boolean reutrnSubmit) {
		this.reutrnSubmit = reutrnSubmit;
	}
	public Boolean getSubmitAble() {
		return submitAble;
	}
	public void setSubmitAble(Boolean submitAble) {
		this.submitAble = submitAble;
	}
	public Boolean getNoTakeTask() {
		return noTakeTask;
	}
	public void setNoTakeTask(Boolean noTakeTask) {
		this.noTakeTask = noTakeTask;
	}
	public Map<String, List<WfTaskBtnInfo>> getTaskBtnInfos() {
		return taskBtnInfos;
	}
	public void setTaskBtnInfos(Map<String, List<WfTaskBtnInfo>> taskBtnInfos) {
		this.taskBtnInfos = taskBtnInfos;
	}
	public Boolean getAutoRound() {
		return autoRound;
	}
	public void setAutoRound(Boolean autoRound) {
		this.autoRound = autoRound;
	}
	public Boolean getDoUserSort() {
		return doUserSort;
	}
	public void setDoUserSort(Boolean doUserSort) {
		this.doUserSort = doUserSort;
	}
	public Boolean getUserDiyAble() {
		return userDiyAble;
	}
	public void setUserDiyAble(Boolean userDiyAble) {
		this.userDiyAble = userDiyAble;
	}
	public Boolean getDiyAble() {
		return diyAble;
	}
	public void setDiyAble(Boolean diyAble) {
		this.diyAble = diyAble;
	}
	public Boolean getAsyncTree() {
		return asyncTree;
	}
	public void setAsyncTree(Boolean asyncTree) {
		this.asyncTree = asyncTree;
	}
	public List<ChildProcessInfo> getChildProcessInfos() {
		return childProcessInfos;
	}
	public void setChildProcessInfos(List<ChildProcessInfo> childProcessInfos) {
		this.childProcessInfos = childProcessInfos;
	}
	public Boolean getDoWfChild() {
		return doWfChild;
	}
	public void setDoWfChild(Boolean doWfChild) {
		this.doWfChild = doWfChild;
	}
	public Boolean getMustDiyUser() {
		return mustDiyUser;
	}
	public void setMustDiyUser(Boolean mustDiyUser) {
		this.mustDiyUser = mustDiyUser;
	}
	public Boolean getNextDiyUser() {
		return nextDiyUser;
	}
	public void setNextDiyUser(Boolean nextDiyUser) {
		this.nextDiyUser = nextDiyUser;
	}
	public Boolean getJumpAble() {
		return jumpAble;
	}
	public void setJumpAble(Boolean jumpAble) {
		this.jumpAble = jumpAble;
	}
	public Boolean getJumpDiyUserAble() {
		return jumpDiyUserAble;
	}
	public void setJumpDiyUserAble(Boolean jumpDiyUserAble) {
		this.jumpDiyUserAble = jumpDiyUserAble;
	}
	public String getJumpNodes() {
		return jumpNodes;
	}
	public void setJumpNodes(String jumpNodes) {
		this.jumpNodes = jumpNodes;
	}
	public Boolean getDoUserSortOnly() {
		return doUserSortOnly;
	}
	public void setDoUserSortOnly(Boolean doUserSortOnly) {
		this.doUserSortOnly = doUserSortOnly;
	}

	public Boolean getAutoSelectAll() {
		return autoSelectAll;
	}

	public void setAutoSelectAll(Boolean autoSelectAll) {
		this.autoSelectAll = autoSelectAll;
	}

	public Boolean getAllotDeptUser() {
		return allotDeptUser;
	}

	public void setAllotDeptUser(Boolean allotDeptUser) {
		this.allotDeptUser = allotDeptUser;
	}
}
