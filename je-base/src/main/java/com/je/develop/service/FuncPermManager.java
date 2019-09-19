package com.je.develop.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.je.core.entity.extjs.JSONTreeNode;
import com.je.core.util.bean.DynaBean;
import com.je.develop.vo.FuncInfoSaas;
import com.je.develop.vo.FuncPerm;
import com.je.develop.vo.FuncPermVo;
import com.je.develop.vo.FuncUserPerm;
import com.je.rbac.model.EndUser;

/**
 * TODO未处理
 */
public interface FuncPermManager {
	/**
	 * 获取功能权限
	 * @param funcCode TODO未处理
	 * @param refresh TODO未处理
	 * @return
	 */
	public FuncInfoSaas getFuncInfoSaas(String funcCode, String refresh);
	/**
	 * 获取功能权限
	 * @param funcCode TODO未处理
	 * @param zhId TODO未处理
	 * @param refresh TODO未处理
	 * @return
	 */
	public FuncInfoSaas getFuncInfoSaas(String funcCode, String zhId, String refresh);
	/**
	 * 获取功能权限
	 * @param funcCode TODO未处理
	 * @param refresh TODO未处理
	 * @return
	 */
	public FuncUserPerm getFuncUserPerm(String funcCode, String refresh);
	/**
	 * 获取功能权限
	 * @param funcCode TODO未处理
	 * @param userId 用户ID
	 * @param zhId TODO未处理
	 * @param roles TODO未处理
	 * @param refresh TODO未处理
	 * @return
	 */
	public FuncUserPerm getFuncUserPerm(String funcCode, String userId, String zhId, List<DynaBean> roles, String refresh);

	/**
	 * 根据功能编码获取VO
	 * @param funcCode TODO未处理
	 * @return
	 */
	public FuncPermVo getFuncPerm(String funcCode);
	/**
	 * 根据功能权限   功能人员权限获取权限数据
	 * @param funcPerm TODO未处理
	 * @param funcUserPerm TODO未处理
	 * @return
	 */
	public FuncPermVo getFuncPerm(FuncPerm funcPerm, FuncUserPerm funcUserPerm);

	/**
	 * 构建功能权限SQL
	 * @param funcCode TODO未处理
	 * @return
	 */
	public String buildPermSql(String funcCode);
	/**
	 * 得到功能的可看角色
	 * @param funcCode TODO未处理
	 * @param type TODO未处理
	 * @param currentUser 创建用户
	 * @return
	 */
	public List<JSONTreeNode> getFuncRoleTree(String funcCode, String type, EndUser currentUser);
	/**
	 * 获取改功能的权限
	 * @param funcInfo TODO未处理
	 * @param type TODO未处理
	 * @param currentUser 创建用户
	 * @return
	 */
	public Map<String,List<HashMap>> getFuncRolePerm(DynaBean funcInfo, String type, EndUser currentUser);
	/**
	 * 根据角色找到功能的权限
	 * @param queryRoleIds TODO未处理
	 * @return
	 */
	public List<String> getFuncCodeByRole(String queryRoleIds);
	/**
	 * 根据功能获取 字段编码名称
	 * @param funcId TODO未处理
	 * @return
	 */
	public List<String> getFuncFieldDic(String funcId);
	/**
	 * 获取层级关系的功能
	 * @param cjId TODO未处理
	 * @param type 类型  公司级 部门级
	 */
	public DynaBean getYwcjPermSql(String cjId, String type);
}
