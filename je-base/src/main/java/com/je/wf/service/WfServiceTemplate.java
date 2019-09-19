package com.je.wf.service;

import java.util.List;
import java.util.Map;

import com.je.core.util.bean.DynaBean;
import com.je.rbac.model.EndUser;
import com.je.wf.processVo.ProcessInfo;
import com.je.wf.processVo.TaskNodeInfo;
import com.je.wf.processVo.WfCurrentProcessInfo;
import com.je.wf.processVo.WfTaskInfo;
import com.je.wf.vo.HistoryLog;

/**
 * TODO 暂不明确
 */
public interface WfServiceTemplate {
	/**
	 * 启动流程
	 * @param dynaBean 实体bean
	 * @param processKey 启动流程的pdid
	 * @param currentUser 操作人 传null默认为当前登录人
	 * @param returnInfo 是否返回流程信息
	 */
	public WfCurrentProcessInfo doStartProcess(DynaBean dynaBean, String processKey, EndUser currentUser, Boolean returnInfo);
	/**
	 * 简单快捷的启动流程
	 * 前置条件: 功能配置一个工作流
	 * (1)流程就配置了一个工作流
	 * (2)不需要返回流程信息
	 * @param dynaBean   启动流程的实体Bean
	 * @param currentUserCode 启动人[用户编码|null] 如果写null则会使用Bean中信息,如果是"用户编码"则会使用传入用户信息,如果没有得到任何人员信息则抛出异常
	 */
	public void doSimpleStartProcess(DynaBean dynaBean, String funcCode, String currentUserCode);
	/**
	 *
	 * 简单快捷的启动流程(使用当前登录人为流程启动人)
	 * 前置条件:
	 * (1)流程就配置了一个工作流
	 * (2)启动人就是当前登录人
	 * (3)不需要返回流程信息
	 * @param dynaBean   启动流程的实体Bean
	 */
	public void doSimpleStartProcess(DynaBean dynaBean, String funcCode);
	/**
	 * 发起流程
	 * @param dynaBean
	 * @param processKey
	 * @param currentUser 执行人
	 * @param taskInfo 执行第一个节点送交信息
	 * @param returnInfo
	 * @return
	 */
	public WfCurrentProcessInfo doSponsorProcess(DynaBean dynaBean, String processKey, EndUser currentUser, WfTaskInfo taskInfo, Boolean returnInfo);
	/**
	 * @author virus
	 *
	 * 简单的流程发起的
	 * @param dynaBean  启动流程的实体Bean
	 * @param currentUserCode 启动人[用户编码|null] 如果写null则会使用Bean中信息,如果是"用户编码"则会使用传入用户信息,如果没有得到任何人员信息则抛出异常
	 * @param targerTaskName  启动后流转到的节点名称
	 * @param submitUsers 执行人员目标值
	 * @param executeType 执行类型  WfAssgineUserType
	 */
	public void doSimpleSponsorProcess(DynaBean dynaBean, String currentUserCode, String funcCode, String targerTaskName, String submitComments, String submitUsers, String executeType);
	/**
	 *
	 * 简单的流程发起的(使用当前登录人为流程启动人)
	 * @param dynaBean  启动流程的实体Bean
	 * @param submitUsers [String[]|String|String(',')]
	 * @param executeType 执行类型  WfAssgineUserType
	 * (1)可以是用户编码的数组[code01,code02]
	 * (2)可以是用户的字符串code
	 * (3)可以是用逗号分开的字符串code1,code2,code3
	 * (4)可以是角色编码
	 */
	public void doSimpleSponsorProcess(DynaBean dynaBean, String funcCode, String targerTaskName, String submitComments, String submitUsers, String executeType);
	/**
	 * 撤销流程
	 * @param dynaBean 业务数据
	 * @return
	 */
	public WfCurrentProcessInfo doCallProcess(DynaBean dynaBean, EndUser currentUser, Boolean returnInfo);
	/**
	 * 结束流程(作废)
	 * @param dynaBean 业务数据
	 * @return
	 */
	public void doEndProcess(DynaBean dynaBean, String endReason, EndUser currentUser);
	/**
	 * 挂起流程
	 * @param dynaBean 业务数据
	 * @return
	 */
	public void doHandUpProcess(DynaBean dynaBean);
	/**
	 * 唤醒流程
	 * @param dynaBean 业务数据
	 * @return
	 */
	public void doActivateProcess(DynaBean dynaBean);
	/**
	 * 送交
	 * @param dynaBean 业务数据
	 * @param currentUser 登录用户
	 * @param taskInfo 执行信息
	 * @param returnInfo 是否返回流程信息
	 * @return
	 */
	public WfCurrentProcessInfo doSubmit(DynaBean dynaBean, EndUser currentUser, WfTaskInfo taskInfo, Boolean returnInfo);
	/**
	 * 回退
	 * @param dynaBean 业务数据
	 * @param currentUser 登录用户
	 * @param taskInfo 执行信息
	 * @param returnInfo 是否返回流程信息
	 * @return
	 */
	public WfCurrentProcessInfo doRollback(DynaBean dynaBean, EndUser currentUser, WfTaskInfo taskInfo, Boolean returnInfo);
	/**
	 * 取回
	 * @param dynaBean 业务数据
	 * @param currentUser 登录用户
	 * @param taskInfo 送交信息
	 * @param returnInfo 是否返回流程信息
	 * @return
	 */
	public WfCurrentProcessInfo doWithdraw(DynaBean dynaBean, EndUser currentUser, WfTaskInfo taskInfo, Boolean returnInfo);
	/**
	 * 委托
	 * @param dynaBean 业务数据
	 * @param currentUser 登录用户
	 * @param taskInfo 送交信息
	 * @param returnInfo 是否返回流程信息
	 * @return
	 */
	public WfCurrentProcessInfo doEntrust(DynaBean dynaBean, EndUser currentUser, WfTaskInfo taskInfo, Boolean returnInfo);
	/**
	 * 取消委托
	 * @param dynaBean 业务数据
	 * @param currentUser 登录用户
	 * @param taskInfo 送交信息
	 * @param returnInfo 是否返回流程信息
	 * @return
	 */
	public WfCurrentProcessInfo doCallEntrust(DynaBean dynaBean, EndUser currentUser, WfTaskInfo taskInfo, Boolean returnInfo);
	/**
	 * 转办
	 * @param dynaBean 业务数据
	 * @param currentUser 登录用户
	 * @param taskInfo 送交信息
	 * @param returnInfo 是否返回流程信息
	 * @return
	 */
	public WfCurrentProcessInfo doTransmit(DynaBean dynaBean, EndUser currentUser, WfTaskInfo taskInfo, Boolean returnInfo);
	/**
	 * 传阅
	 * @param dynaBean 业务数据
	 * @param currentUser 登录用户
	 * @param taskInfo 送交信息
	 * @param returnInfo 是否返回流程信息
	 * @return
	 */
	public WfCurrentProcessInfo doRound(DynaBean dynaBean, EndUser currentUser, WfTaskInfo taskInfo, Boolean returnInfo);
	/**
	 * 审阅
	 * @param dynaBean 业务数据
	 * @param currentUser 登录用户
	 * @param taskInfo 送交信息
	 * @param returnInfo 是否返回流程信息
	 * @return
	 */
	public WfCurrentProcessInfo takeRound(DynaBean dynaBean, EndUser currentUser, WfTaskInfo taskInfo, Boolean returnInfo);
	/**
	 * 会签通过
	 * @param dynaBean 业务数据
	 * @param currentUser 登录用户
	 * @param taskInfo 送交信息
	 * @param returnInfo 是否返回流程信息
	 * @return
	 */
	public WfCurrentProcessInfo doPass(DynaBean dynaBean, EndUser currentUser, WfTaskInfo taskInfo, Boolean returnInfo);
	/**
	 * 会签否决
	 * @param dynaBean 业务数据
	 * @param currentUser 登录用户
	 * @param taskInfo 送交信息
	 * @param returnInfo 是否返回流程信息
	 * @return
	 */
	public WfCurrentProcessInfo doNoPass(DynaBean dynaBean, EndUser currentUser, WfTaskInfo taskInfo, Boolean returnInfo);
	/**
	 * 会签弃权
	 * @param dynaBean 业务数据
	 * @param currentUser 登录用户
	 * @param taskInfo 送交信息
	 * @param returnInfo 是否返回流程信息
	 * @return
	 */
	public WfCurrentProcessInfo doWaiverPass(DynaBean dynaBean, EndUser currentUser, WfTaskInfo taskInfo, Boolean returnInfo);
	/**
	 * 领取任务
	 * @param dynaBean 业务数据
	 * @param currentUser 登录用户
	 * @param taskInfo 送交信息
	 * @param returnInfo 是否返回流程信息
	 * @return
	 */
	public WfCurrentProcessInfo doTakeTask(DynaBean dynaBean, EndUser currentUser, WfTaskInfo taskInfo, Boolean returnInfo);
	/**
	 * 获取当前流程所在状态
	 * @param dynaBean 业务数据
	 * @param currentUser 登录用户
	 * @return
	 */
	public WfCurrentProcessInfo loadProcessCurrentInfo(DynaBean dynaBean, EndUser currentUser);
	/**
	 * 获取流程历史
	 * @param dynaBean 业务数据
	 * @param isAsc  是否按照创建时间升序
	 * @return
	 */
	public List<HistoryLog> loadProcessHistory(DynaBean dynaBean, Boolean isAsc);
	/**
	 * 获取流程以那个结束节点结束
	 * @param dynaBean 业务数据
	 * @return
	 */
	public TaskNodeInfo loadProcessEndTask(DynaBean dynaBean);
	/**
	 * 获取流程部署信息
	 * @param pdid 流程部署ID
	 * @return
	 */
	public ProcessInfo loadProcessInfo(String pdid);
	/**
	 * 获取指定流程任务所有可执行的人
	 * @param pkValue 业务主键ID(可为空)
	 * @param pdid 流程部署ID
	 * @param taskName  任务名称
	 * @param currentUser 当前登录人
	 * @return
	 */
	public List<DynaBean> loadTaskUsers(String pkValue, String pdid, String taskName, EndUser currentUser);
	/**
	 * 读取流程预警缓存
	 */
	public void doWfWarn();
	/**
	 * 读取流程预警数据库
	 */
	public void doWfDbWarn();
	/**
	 * 处理流程预警延期提醒
	 */
	public void doWfWarn(List<DynaBean> lists);
	/**
	 * 后台推送发起流程
	 * @param dynaBean
	 * @param processKey 启动流程的pdid
	 * @param funcCode
	 * @param startNode
	 * @param targerTaskName  启动后流转到的节点名称
	 * @param nodeUsers
	 * @param nodeComments
	 * @return
	 */
	public WfCurrentProcessInfo doSponsorProcess(DynaBean dynaBean, String processKey, String funcCode, String startNode, String targerTaskName, Map<String, String> nodeUsers, Map<String, String> nodeComments);
	/**
	 * 推送送交时间
	 * @param dynaBean
	 * @param thisTaskName
	 * @param targerTaskName
	 * @param nodeUsers
	 * @param nodeComments
	 */
	public void doSubmit(DynaBean dynaBean, String thisTaskName, String targerTaskName, Map<String, String> nodeUsers, Map<String, String> nodeComments);
	/**
	 * 获取流程最后一个版本部署ID
	 * @param processKey
	 * @return
	 */
	public String getLastVersionProcess(String processKey);
}
