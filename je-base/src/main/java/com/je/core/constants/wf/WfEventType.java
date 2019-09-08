/**
 *
 */
package com.je.core.constants.wf;

/**
 * 工作流事件类型
 * @author chenmeng
 * 2012-5-9 下午4:11:35
 */
public final class WfEventType {
	/** 流程范围 */
	public static final String SCOPE_PROCESS = "PROCESS";
	/** 任务节点范围 */
	public static final String SCOPE_TASK = "TASK";

	/** 流程启动 */
	public static final String WF_STARTED = "WF_STARTED";
	/** 流程撤销 */
	public static final String WF_CANCELED = "WF_CANCELED";
	/** 流程结束 */
	public static final String WF_ENDED = "WF_ENDED";
	/** 流程作废*/
	public static final String WF_SUSPEND = "WF_SUSPEND";

	/** 提交之前 */
	public static final String SUBMIT_BEFORE = "SUBMIT_BEFORE";
	/** 提交之后 */
	public static final String SUBMIT_AFTER = "SUBMIT_AFTER";
	/** 退回之前 */
	public static final String ROLLBACK_BEFORE = "ROLLBACK_BEFORE";
	/** 退回之后 */
	public static final String ROLLBACK_AFTER = "ROLLBACK_AFTER";
	/** 驳回之前 */
	public static final String REJECT_BEFORE = "REJECT_BEFORE";
	/** 驳回之后 */
	public static final String REJECT_AFTER = "REJECT_AFTER";
	/** 收回之前 */
	public static final String WITHDRAW_BEFORE = "WITHDRAW_BEFORE";
	/** 收回之后 */
	public static final String WITHDRAW_AFTER = "WITHDRAW_AFTER";
	/** 委托之前 */
	public static final String ENTRUST_BEFORE = "ENTRUST_BEFORE";
	/** 委托之后 */
	public static final String ENTRUST_AFTER = "ENTRUST_AFTER";
	/** 转办之前 */
	public static final String TRANSMIT_BEFORE = "TRANSMIT_BEFORE";
	/** 转办之后 */
	public static final String TRANSMIT_AFTER = "TRANSMIT_AFTER";
	/** 提交之前 */
	public static final String RETURNSUBMIT_BEFORE = "RETURNSUBMIT_BEFORE";
	/** 提交之后 */
	public static final String RETURNSUBMIT_AFTER = "RETURNSUBMIT_AFTER";
	/** 任务领取 */
	public static final String TAKETASK = "TAKETASK";
	/** 提交之前 */
	public static final String ROUND_BEFORE = "ROUND_BEFORE";
	/** 提交之后 */
	public static final String ROUND_AFTER = "ROUND_AFTER";
	/** 提交之前 */
	public static final String TAKEROUND_BEFORE = "TAKEROUND_BEFORE";
	/** 提交之后 */
	public static final String TAKEROUND_AFTER = "TAKEROUND_AFTER";
	/** 传参为业务 Bean*/
	public static final String EVENT_PARAM = "EVENT_PARAM";
	/** 传参为业务主键和任务执行人 */
	public static final String ID_ASSIGNEE = "ID_ASSIGNEE";
	/** 传参为空参数 */
	public static final String NULL_PARAM = "NULL_PARAM";
	/** 执行人进卡片 */
	public static final String SET_ASSIGNEE = "SET_ASSIGNEE";
	/** 一对一卡片更新 */
	public static final String ONETOONE_UPDATE = "ONETOONE_UPDATE";
	/** 自定义方法 */
	public static final String DIY = "DIY";
	/** 发送RTX消息通知 */
	public static final String RTX_MESSAGE = "RTX_MESSAGE";
	/** 发送Email电子邮件通知 */
	public static final String EMAIL_MESSAGE = "EMAIL_MESSAGE";
}
