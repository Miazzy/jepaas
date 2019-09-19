package com.je.core.constants.wf;
/**
 * 任务委托目标类型
 * @author zhangshuaipeng
 *
 */
public class WfTaskAssgineType {
	/**角色处理*/
	public static final String ROLE="ROLE";
	/**部门处理*/
	public static final String DEPT="DEPT";
	/**岗位处理*/
	public static final String SENTRY="SENTRY";
	/**人员处理*/
	public static final String USER="USER";
	/**特殊处理*/
	public static final String SPECIAL="SPECIAL";
	/**表单字段*/
	public static final String FIELD="FIELD";
	/**组织架构*/
	public static final String DEPTUSER="DEPTUSER";
	/**用户SQL*/
	public static final String USERSQL="USERSQL";
	/**自定义处理*/
	public static final String DIY="DIY";

	/**本部门*/
	public static final String DEPT_HEAD="DEPT_HEAD";
	/**本部门负责人*/
	public static final String DEPT_FZR="DEPT_FZR";
	/**本部门监管领导(监管)*/
	public static final String DEPT_ONLYJGHEAD="DEPT_ONLYJGHEAD";
	/**本部门领导(含监管)*/
	public static final String DEPT_JGHEAD="DEPT_JGHEAD";
	/**本部门领导(含监管 直属)*/
	public static final String DEPT_ALLHEAD="DEPT_ALLHEAD";
	/**直属领导*/
	public static final String USER_HEAD="USER_HEAD";
	/**流程启动人*/
	public static final String PROCESS_STARTED="PROCESS_STARTED";
	/**当前登录人*/
	public static final String PROCESS_CURRENTUSER="PROCESS_CURRENTUSER";
	/**任务指派人 也就是该节点的上上节点执行人*/
	public static final String TASK_ASSGINE="TASK_ASSGINE";
	/**前置任务指派人    也就是该节点的上上节点执行人*/
	public static final String BEFORETASK_ASSGINE="BEFORETASK_ASSGINE";
	/**任务指派人 也就是该节点的上上节点执行人 领导*/
	public static final String TASK_ASSGINE_HEAD="TASK_ASSGINE_HEAD";
	/**任务指派人 也就是该节点的上上节点执行人 领导*/
	public static final String BEFORETASK_ASSGINE_HEAD="BEFORETASK_ASSGINE_HEAD";
	/**本部门人员*/
	public static final String DEPT_USER="DEPT_USER";
	/**本部门内(含子)人员*/
	public static final String DEPT_ALLUSER="DEPT_ALLUSER";
	/**本部门(含子)+监管部门人员*/
	public static final String DEPT_JGALLUSER="DEPT_JGALLUSER";
}
