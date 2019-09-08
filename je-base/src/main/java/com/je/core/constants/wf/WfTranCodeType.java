/**
 *
 */
package com.je.core.constants.wf;

/**
 * 工作流默认路径名称
 * @author chenmeng
 * 2012-4-26 下午4:04:34
 */
public class WfTranCodeType {
	/** 默认提交 */
	public static final String SUBMIT = "to_submit";
	/** 默认返回提交 */
	public static final String RETURNSUBMIT = "to_returnsubmit";
	/** 默认退回 */
	public static final String ROLLBACK = "to_rollback";
	/** 默认重新流转 */
	public static final String RETURNBACK="to_returnback";
	/** 默认取回 */
	public static final String WITHDRAW = "to_withdraw";
	/** 默认结束 */
	public static final String TOEND = "to_end";
	/** 默认委托 */
	public static final String ENTRUST="to_entrust";
	/** 默认撤销委托 */
	public static final String CALLENTRUST="to_callentrust";
	/** 默认传阅 */
	public static final String ROUND="to_round";
	/** 审阅 */
	public static final String TAKEROUND="to_takeround";
	/**取消审阅   暂时用于状态，不用于操作*/
	public static final String NOTAKE="to_notake";
	/** 默认转办 */
	public static final String TRANSMIT="to_transmit";
	/** 会签通过 */
	public static final String PASS="to_pass";
	/** 会签不通过 */
	public static final String NOPASS="to_nopass";
	/** 会签修弃权 */
	public static final String WAIVERPASS="to_waiverpass";
	/** 激活流程 */
	public static final String ACTIVE="to_active";
	/** 挂起流程 */
	public static final String HANDUPED="to_handuped";
	/** 作废流程 */
	public static final String TO_ENDED="to_ended";
	/** 预定义 */
	public static final String TO_USERDIY="to_userdiy";
	/** 返回处理节点 */
	public static final String TO_BACKUSERDIY="to_backuserdiy";
	/** 提交下一处理节点 */
	public static final String TO_NEXTUSERDIY="to_nextuserdiy";
	/** 撤销流程 */
	public static final String TO_CALL="to_call";
	/**循环流执行*/
	public static final String TO_CIRCULAR="to_circular";
	/**驳回*/
	public static final String TO_REJECT="to_reject";
	/**跳跃节点*/
	public static final String TO_JUMP="to_jump";
	/**调拨节点*/
	public static final String TO_ALLOT="to_allot";


}
