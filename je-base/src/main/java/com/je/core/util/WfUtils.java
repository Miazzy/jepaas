/**
 *
 */
package com.je.core.util;

import java.util.HashMap;
import java.util.Map;

/**
 * 工作流工具类
 * @author chenmeng
 * 2012-3-19 上午10:04:52
 */
public final class WfUtils {

	public static final String NODE_CONCAT = "_to_";

	private final static String __to_submit = "to_submit";
	private final static String __to_returnsubmit = "to_returnsubmit";
	private final static String __to_rollback = "to_rollback";
	private final static String __to_withdraw = "to_withdraw";
	private final static String __to_entrust= "to_entrust";
	private final static String __to_returnback= "to_returnback";
	private final static String __to_callentrust= "to_callentrust";
	private final static String __to_transmit= "to_transmit";
	private final static String __to_taketask= "to_taketask";
	private final static String __to_end= "to_end";
	private final static String __to_round= "to_round";
	private final static String __to_takeround= "to_takeround";
	private final static String __to_circular= "to_circular";
	private final static String __to_reject= "to_reject";
	private final static String __to_userdiy= "to_userdiy";
	private final static String __to_backuserdiy= "to_backuserdiy";
	private final static String __to_jump= "to_jump";
	private final static String __no= "no";
	/** 会签通过 */
	private static final String _to_pass="to_pass";
	/** 会签不通过 */
	private static final String _to_nopass="to_nopass";
	/** 会签修弃权 */
	private static final String _to_waiverpass="to_waiverpass";
	private static Map<String, String> map;

	static {
		map = new HashMap<String, String>();
		map.put(__to_submit, "已审批");
		map.put(__to_returnsubmit, "已审批");
		map.put(__to_circular, "已办理");
		map.put(__to_rollback, "审批退回");
		map.put(__to_reject, "审批驳回");
		map.put(__to_withdraw, "任务取回");
		map.put(__to_entrust, "任务委托");
		map.put(__to_returnback, "重新流转");
		map.put(__to_callentrust, "撤销委托");
		map.put(__to_transmit, "任务转办");
		map.put(__to_taketask, "领取任务");
		map.put(__to_end, "结束流程");
		map.put(__to_round, "传阅");
		map.put(__to_takeround, "审阅");
		map.put(_to_pass, "通过");
		map.put(_to_nopass, "不通过");
		map.put(_to_waiverpass, "弃权");
		map.put(__to_userdiy, "已审批");
		map.put(__to_backuserdiy, "已审批");
		map.put(__to_jump, "已审批");
		map.put(__no, "未处理");
		map.put(null, "正在处理");
	}

	/**
	 * 为新建退回路径拼接路径名称
	 * @param fromNode
	 * @param toNode
	 * @return
	 */
	public static final String buildRollbackName(String fromNode, String toNode) {
		return fromNode + NODE_CONCAT + toNode;
	}

	public static final String getDefaultRollbackName() {
		return __to_rollback;
	}

	public static String tranNameParser(String tranKey) {
		if(map.containsKey(tranKey)) {
			return map.get(tranKey);
		} else {
			return tranKey;
		}
	}
}
