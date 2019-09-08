package com.je.wf.service;

import java.util.List;

import org.jbpm.api.task.Task;

import com.je.core.entity.extjs.JSONTreeNode;
import com.je.core.util.bean.DynaBean;
import com.je.rbac.model.EndUser;
import com.je.wf.processVo.BatchTaskInfo;
import com.je.wf.processVo.CountersignInfo;
import com.je.wf.processVo.ProcessInfo;
import com.je.wf.processVo.TaskNodeInfo;

import net.sf.json.JSONObject;

/**
 * TODO 暂不明确
 */
public interface WfInfoManager {
	/**
	 * 加载表单信息
	 * @param tableName 表名称
	 * @param pkValue 主键值
	 * @param currentUser 登陆人信息
	 * @param returnObj 返回信息
	 */
	public void loadInfo(String tableName, String pkValue, EndUser currentUser, JSONObject returnObj);
	/**
	 * 加载流程结束后当前登录人拥有表单历史
	 * @param pdid
	 * @param piid 流程实例id
	 * @param currentUser 登陆人信息
	 * @param returnObj
	 */
	public void loadEndInfo(String pdid, String piid, EndUser currentUser, JSONObject returnObj);
	/**
	 * 加载分支节点任务按钮信息
	 * @param tableName 表名称
	 * @param pkValue 主键值
	 * @param currentUser 登陆人信息
	 * @param returnObj
	 */
	public void loadForkTaskInfo(String tableName, String pkValue, EndUser currentUser, JSONObject returnObj);
	/**
	 * 根据当前流程信息构建当前所需要返回的按钮信息
	 * @param tasks 任务
	 * @param currentUser 登陆人信息
	 * @param piOwner TODO 暂不明确
	 * @param countersignInfoStr 会签信息
	 * @param processInfo 过程信息
	 * @param dynaBean
	 * @param returnObj
	 */
	public void buildLoadInfo(List<Task> tasks, EndUser currentUser, String piOwner, String countersignInfoStr, ProcessInfo processInfo, DynaBean dynaBean, JSONObject returnObj);
	/**
	 * 获取任务角色树
	 * @param pdid
	 * @param piid 流程实例id
	 * @param taskNames 任务流名称
	 */
	public List<JSONTreeNode> getTaskAssgineTree(String pkValue, String pdid, String piid, String executeTaskName, String taskNames, String forkTargerTaskInfoStr, Boolean onlyUser, EndUser currentUser, JSONObject returnObj);
	/**
	 * 获取待审批人异步树之后的审批人员信息
	 * @param rootId 根节点id
	 * @param isRoot 是否根节点
	 * @param whereSql 查询sql
	 * @param orderSql 排序sql
	 * @return
	 */
	public List<JSONTreeNode> loadAsyncAssgine(String rootId, boolean isRoot, String whereSql, String orderSql);

	/**
	 * 获取驳回任务人员系你想
	 * @param pkValue
	 * @param pdid
	 * @param piid 流程实例id
	 * @param rejectTaskNames 拒绝任务名称
	 * @param doUser 用户实体
	 * @return
	 */
	public List<JSONTreeNode> getTaskRejectAssgineTree(String pkValue, String pdid, String piid, String rejectTaskNames, EndUser doUser);

	/**
	 * 获取任务传阅人员
	 * @param pdid
	 * @param taskName 表名称
	 * @param onlyUser TODO 暂不明确
	 * @param pkValue 主键值
	 * @return
	 */
	public List<JSONTreeNode> getRoundUserTree(String pdid, String taskName, Boolean onlyUser, String pkValue);
	/**
	 * 得到催办数据
	 * @param tableCode 表code
	 * @param pkValue 主键值
	 * @return
	 */
	public List<JSONTreeNode> getPromptUser(String tableCode, String pkValue);
	/**
	 * 获取活动任务
	 * @param piid 流程实例id
	 * @param taskName 任务名称
	 * @return
	 */
	public Task getCurrentTask(String piid, String taskName);

	/**
	 * 设置任务节点的是否新标识
	 * @param task 任务
	 * @param currentIds 当前人id
	 */
	public void setTaskNew(Task task, String currentIds);
	/**
	 * 构建会签信息
	 * @param taskNodeInfo 节点信息
	 * @param countersignInfos 送交会签信息  如果为空 则默认不加载会签人员(加载徐piid和pdid 会签处理角色....)
	 */
	public CountersignInfo buildCountersignInfo(TaskNodeInfo taskNodeInfo, String countersignInfos);
	/**
	 * 获取多人任务信息
	 * @param taskNodeInfo 节点信息
	 * @param batchTaskInfos 分批任务信息
	 * @return
	 */
	public BatchTaskInfo buildBatchInfo(TaskNodeInfo taskNodeInfo, String batchTaskInfos);

	/**
	 * 处理审批详细意见
	 * @param tableCode 表code
	 * @param pkValue 主键值
	 * @param piid 流程实例id
	 * @param taskId 任务id
	 * @param taskType 任务类型
	 * @param action TODO 暂不明确
	 * @param comments 评论
	 * @param childTaskId TODO 暂不明确
	 * @return
	 */
	public String doCommentDetail(String tableCode, String pkValue, String piid, String taskId, String taskType, String action, String comments, String childTaskId);

	/**
	 * 获取流程预定义的所有任务节点
	 * @param pkValue 主键值
	 * @param piid 流程实例id
	 * @param pdid TODO 暂不明确
	 * @param en TODO 暂不明确
	 * @return
	 */
	public List<TaskNodeInfo> getDiyWfTasks(String pkValue, String piid, String pdid, boolean en);

	/**
	 * 获取流程预定义的所有任务节点
	 * @param pkValue 主键值
	 * @param pdid
	 * @param taskName 任务节点名称
	 * @param userIds 用户id
	 * @param userNames 用户名称
	 */
	public void setDiyWfTasks(String pkValue, String pdid, String taskName, String userIds, String userNames);
}
