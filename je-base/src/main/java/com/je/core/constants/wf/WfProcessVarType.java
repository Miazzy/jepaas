package com.je.core.constants.wf;

/**
 * TODO 暂不明确
 */
public class WfProcessVarType {
	/** 流程定义创建人 */
	public static final String PI_OWNER = "PI_OWNER";
	/** 流程定义业务主键 */
	public static final String PI_PDID = "PI_PDID";
	/** 流程定义业务主键 */
	public static final String PI_PKVALUE = "PI_PKVALUE";
	/** 历史信息上一执行节点 */
	public static final String PI_BEFORETASK="PI_BEFORETASK";
	/** 取回人 */
	public static final String PI_RETURNASSGINE="PI_RETURNASSGINE";
	/** 目标路径 */
	public static final String PI_TARGER_TRANSITION="PI_TARGER_TRANSITION";
	/**已审批人*/
	public static final String PI_APPROVED="PI_APPROVED";
	/**延迟任务*/
	public static final String PI_DELAY="PI_DELAY";
	/**待审批人*/
	public static final String PI_PREAPPROV="PI_PREAPPROV";
	/**审批会签信息*/
	public static final String PI_COUNTERSIGN="PI_COUNTERSIGN";
	/**历史信息*/
	public static final String PI_HISTORY="PI_HISTORY";
	/**审批会签信息*/
	public static final String PI_FORK="PI_FORK";
	/**多人审批信息*/
	public static final String PI_BATCH="PI_BATCH";
	/**系统自动通过节点指派人，历史记录不展示*/
	public static final String PI_SYS_AUTOUSER="PI_SYS_AUTOUSER";
	/**系统自动节点执行人，虚拟用户*/
	public static final String PI_SYS_AUTONODE_ASSGINE="PI_SYS_AUTONODE_ASSGINE";
	/**系统自动通过节点指派人，历史记录不展示*/
	public static final String PI_JOIN_SIZE="PI_JOIN_SIZE";
}
