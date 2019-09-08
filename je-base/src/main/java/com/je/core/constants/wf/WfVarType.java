/**
 * 
 */
package com.je.core.constants.wf;

/**
 * 工作流变量
 * @author chenmeng
 * 2012-3-22 下午12:58:15
 */
public class WfVarType {

	/** 业务对象类型 */
	public static final String BO_TABLECODE = "BO_TABLECODE";
	/** 业务对象主键名称 */
	public static final String BO_ID_NAME = "BO_ID_NAME";
	/** 业务对象主键值（预定义变量） */
	public static final String BO_ID_VALUE = "BO_ID_VALUE";
	/** 结点是否可退回参数 */
	public static final String ROLLBACKABLE = "ROLLBACKABLE";
	/** 结点是否可收回参数 */
	public static final String WITHDRAWABLE = "WITHDRAWABLE";
	/** 流程定义创建人 */
	public static final String PI_OWNER = "PI_OWNER";
	/** 功能ID */
	public static final String FUNCID = "FUNCID";
	/** 流程是否任何人均可启动 */
	public static final String ANYONE_STARTABLE = "ANYONE_STARTABLE";
	/**启动角色组*/
	public static final String STARTROLES="STARTROLES";
	/**流程所属组*/
	public static final String PROCESSGROUP="PROCESSGROUP";
	/**目标角色*/	
	public static final String TARGET_ROLE="TARGET_ROLE";
	/**目标人*/	
	public static final String TARGET_USER="TARGET_USER";
	/**是否回执*/
	public static final String RECEIPTABLE="RECEIPTABLE";
	/**回执方式      NULL EMAIL PCMAIL*/
	public static final String RECEIPTTYPE="RECEIPTTYPE";
	
}
