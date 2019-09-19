package com.je.rbac.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.je.core.entity.QueryInfo;
import com.je.core.entity.extjs.JSONTreeNode;
import com.je.core.util.bean.DynaBean;
import com.je.dd.vo.DicInfoVo;
import com.je.rbac.node.RoleGroupPermNode;
import com.je.rbac.node.RolePermNode;
import com.je.wf.processVo.WfEventSubmitInfo;

/**
 * 权限服务层
 * @author zhangshuaipeng
 *
 */
public interface PermissionManager {
	/**
	 * 得到首页模块信息
	 * @param userId 用户id
	 * @param refresh TODO 暂不明确
	 * @return
	 */
	public List<DynaBean> getUserHome(String userId, String refresh);
	/**
	 * 构建指定菜单权限Map
	 * @param targers  目标   角色 /权限组
	 * @param permType 目标类型
	 * @return
	 */
	public List<DynaBean> buildMenuPermList(List<DynaBean> targers, String permType, String menuIds, boolean havePublic);
	/**
	 * 构建权限Map
	 * @param targers  目标   角色 /权限组
	 * @param permType 目标类型
	 * @param haveButton 是否包含按钮
	 * @param haveSubFunc 是否包含子功能
	 * @return
	 */
	public Map<String,DynaBean> buildPermMap(List<DynaBean> targers, String permType, Boolean haveButton, Boolean haveSubFunc, String module, boolean havePublic);
	/**
	 * 构建权限Map
	 * @param targers  目标   角色 /权限组
	 * @param permType 目标类型
	 * @param haveButton 是否包含按钮
	 * @param haveSubFunc 是否包含子功能
	 * @return
	 */
	public Map<String,DynaBean> buildAppPermMap(List<DynaBean> targers, String permType, Boolean haveButton, Boolean haveSubFunc, String module, boolean havePublic);

	/**
	 * 构建权限Map
	 * @param targers  目标   角色 /权限组
	 * @param permType 目标类型
	 * @param permCodeIds TODO 暂不明确
	 * @param havePublic TODO 暂不明确
	 * @return
	 */
	public Map<String,DynaBean> buildFuncPermMap(List<DynaBean> targers, String permType, String permCodeIds, boolean havePublic);

	/**
	 * 得到功能权限Map  主要用于用户点击功能过滤权限
	 * @param funcId 功能id
	 * @param roles 角色
	 * @param havePublic 是否公开
	 * @return
	 */
	public Map<String,DynaBean> getFuncPermMap(String funcId, List<DynaBean> roles, boolean havePublic);
	/**
	 * 构建权限树
	 * @param rootId 根节点ID
	 * @param targers  目标   角色 /权限组
	 * @param permType 目标类型
	 * @param haveButton 是否包含按钮
	 * @param haveSubFunc 是否包含子功能
	 * @param haveDeny 是否含有未授权
	 * @param queryInfo 查询条件(主要用于菜单的过滤)
	 * @return
	 */
	public JSONTreeNode buildPermTree(String rootId, List<DynaBean> targers, String permType, Boolean haveButton, Boolean haveSubFunc, Boolean haveDeny, Boolean en, QueryInfo queryInfo, Set<String> includes, Set<String> nodeIds, String zhId, boolean havePublic, boolean onlyOneChild);
	/**
	 * 构建手机权限树形
	 * @param apkId TODO 暂不明确
	 * @param targers 目标 角色/权限组
	 * @param permType 目标类型
	 * @param haveButton 是否包含按钮
	 * @param haveSubFunc 是否包含子功能
	 * @param haveDeny 是否含有未授权
	 * @param includes TODO 暂不明确
	 * @return
	 */
	public JSONTreeNode buildAppPermTree(String apkId, List<DynaBean> targers, String permType, Boolean haveButton, Boolean haveSubFunc, Boolean haveDeny, Set<String> includes, String cpId);
	/**
	 * 得到授权树形
	 * @param rootId 根节点ID
	 * @param targers  目标   角色 /权限组
	 * @param permType 目标类型
	 * @param queryInfo 查询条件(主要用于菜单的过滤)
	 * @return
	 */
	public JSONTreeNode getAuthorPermTree(String rootId, List<DynaBean> targers, String permType, QueryInfo queryInfo);
	/**
	 * 查询用户所有权限   用户查看权限
	 * @param users 用户
	 * @return
	 */
	public JSONTreeNode getUserPermTree(DynaBean users);

	/**
	 * 初始化所有功能授权的功能
	 * @param whereSql 查询sql
	 * @param haveDeny 是否含有未授权
	 * @param cpId TODO 暂不明确
	 * @return
	 */
	public JSONTreeNode initFuncTreeNode(String whereSql, Boolean haveDeny, String cpId);
	/**
	 * 获取指定角色或权限组的子系统功能类型
	 * @param whereSql 查询的sql
	 * @return
	 */
	public JSONTreeNode getFuncTreeNode(String whereSql);

	/**
	 * 得到角色权限
	 * @param rootId 根节点id
	 * @param roles 角色
	 * @param en TODO 暂不明确
	 * @param includeStr TODO 暂不明确
	 * @param jtgsId TODO 暂不明确
	 * @return
	 */
	public JSONTreeNode getMenuTree(String rootId, List<DynaBean> roles, Boolean en, String includeStr, String jtgsId);

	/**
	 * 得到手机端菜单权限
	 * @param apkId TODO 暂不明确
	 * @param roles TODO 暂不明确
	 * @param permType 目标类型
	 * @param havePublic 是否公开
	 * @return
	 */
	public List<String> getAppMenuPerm(String apkId, List<DynaBean> roles, String permType, boolean havePublic);

	/**
	 * 得到手机端菜单权限
	 * @param funcIds 功能ids
	 * @param roles 角色
	 * @param permType 目标类型
	 * @param havePublic 是否公开
	 * @return
	 */
	public List<String> getAppFuncPerm(String funcIds, List<DynaBean> roles, String permType, boolean havePublic);
	/**
	 * 得到权限组
	 * @param rootId 根节点id
	 * @param roleGroups 角色权限组
	 * @return
	 */
	public JSONTreeNode getRoleGroupTree(String rootId, List<DynaBean> roleGroups);

	/**
	 * 得到所有权限树
	 * @param rootId 根节点id
	 * @param queryInfo 查询信息
	 * @param cpId TODO 暂不明确
	 * @return
	 */
	public JSONTreeNode getPermTree(String rootId, QueryInfo queryInfo, String cpId);
	/**
	 * 更新权限
	 * @param targer TODO 暂不明确
	 * @param permType 目标类型
	 * @param delPerm 删除目标
	 * @param addPerm 添加目标
	 */
	public void doUpdatePermData(DynaBean targer, String permType, String delPerm, String addPerm, Boolean isHard);
	/**
	 * 更新权限
	 * @param targer TODO 暂不明确
	 * @param permType 目标类型
	 * @param delPerm 删除类型
	 * @param addPerm 添加类型
	 */
	public void doUpdatePublicPermData(DynaBean targer, String permType, String delPerm, String addPerm);
	/**
	 * 更新权限
	 * @param targer TODO 暂不明确
	 * @param permType 目标类型
	 * @param delPerm 删除类型
	 * @param addPerm 添加类型
	 */
	public void doUpdateFuncPermData(DynaBean targer, String permType, String delPerm, String addPerm, Boolean isHard);
	/**
	 * 构建角色授权树
	 * @param rooId 根节点id
	 * @return
	 */
	public RolePermNode buildRolePermTree(String rooId);
	/**
	 * 构建权限组授权树
	 * @param rootId 根节点id
	 * @return
	 */
	public RoleGroupPermNode buildRoleGroupTree(String rootId);
	/**
	 * 得到当前权限角色
	 * @return
	 */
	public List<DynaBean> getCurrentPermRoles();
	/**
	 * 清空权限
	 * @param permType 类型
	 * @param ids 主键
	 * @param delMenu 是否清空按钮
	 */
	public void clearPermData(String permType, String ids, Boolean delMenu);
	/**
	 * 更新功能下权限的所属模块   在菜单拖动过程中需要更新该值
	 * @param funcInfo TODO 暂不明确
	 * @param mouldeId TODO 暂不明确
	 */
	public void updatePermModule(DynaBean funcInfo, String mouldeId);
	/**
	 * 得到当前登录用户所有可操作功能的功能功能编码集合
	 * @return
	 */
	public Set<String> getPermFunc();
	/**
	 * 导入功能授权功能
	 * @param funcIds 功能id
	 */
	public JSONTreeNode impFuncTreeNode(String funcIds);
	/**
	 * 删除功能授权功能
	 * @param funcIds 功能id
	 */
	public void  removeFuncTreeNode(String funcIds);
	/**
	 * 授权给开发人员
	 * @param menuId 菜单id
	 * @param targer TODO 暂不明确
	 */
	public void doDevelopPerm(String menuId, DynaBean targer);

	/**
	 * 授权按钮给开发人员
	 * @param btnId 按钮id
	 * @param funcId 功能id
	 * @param type 类型
	 * @param targer TODO 暂不明确
	 */
	public void doDevelopFuncPerm(String btnId, String funcId, String type, DynaBean targer);
	/**
	 * 获取用户申请权限
	 * @param dicInfoVo TODO 暂不明确
	 * @return
	 */
	public JSONTreeNode getRoleDiyPerm(DicInfoVo dicInfoVo);
}
