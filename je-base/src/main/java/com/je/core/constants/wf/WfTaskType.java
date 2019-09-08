package com.je.core.constants.wf;

/**
 * TODO 暂不明确
 */
public class WfTaskType {
	/**
	 * 开始节点 
	 */
	public static final String NODE_START = "start";
	/**
	 * 任务节点
	 */
	public static final String NODE_TASK = "task";
	/**
	 * 判断节点
	 */
	public static final String NODE_DECISION = "decision";
	/**
	 * 分支节点
	 */
	public static final String NODE_FORK = "fork";
	/**
	 * 聚合节点
	 */
	public static final String NODE_JOIN = "join";
	/**
	 *  结束节点
	 */
	public static final String NODE_END = "end";
	/**
	 * 候选节点
	 */
	public static final String NODE_JOINT = "joint";
	/**
	 * 多人处理节点
	 */
	public static final String NODE_BATCHTASK = "batchtask";
	/**
	 * 会签节点
	 */
	public static final String NODE_COUNTERSIGN = "countersign";
	/**
	 * 固定执行人节点
	 */
	public static final String NODE_TO_ASSIGNEE = "to_assignee";
	/**
	 * 自动节点
	 */
	public static final String NODE_AUTO = "auto";
	/**
	 * 循环流
	 */
	public static final String NODE_CIRCULAR="circular";
	/**
	 * 中转节点， 来作为转办和委托的时候进行的任务节点名称
	 */
	public static final String NODE_ENTRUST_TRANSMIT_TASK="ENTRUST_TRANSMIT_TASK";
	/**
	 * 强制预定义节点
	 */
	public static final String USERDIY_TASK="USERDIY_TASK";
	/**
	 * 跳跃中转节点
	 */
	public static final String JUMP_TASK="JUMP_TASK";
	/**
	 * 调拨中转节点
	 */
	public static final String ALLOT_TASK="ALLOT_TASK";
	/**
	 * 传阅类型
	 */
	public static final String ROUND = "ROUND";
}
