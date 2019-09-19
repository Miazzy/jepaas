package com.je.rbac.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.je.saas.vo.UserStatisticVo;
import com.je.core.entity.extjs.JSONTreeNode;
import com.je.core.util.bean.DynaBean;
import com.je.rbac.model.EndUser;
import com.je.rbac.model.UserLeader;

/**
 * 人员服务层接口
 * @author zhangshuaipeng
 *
 */
public interface UserManager {
	/**
	 * 保存
	 * @param dynaBean 自定义动态类
	 * @param request 信息体
	 */
	public DynaBean doSave(DynaBean dynaBean, HttpServletRequest request);
	/**
	 * 修改
	 * @param dynaBean 自定义动态类
	 * @param request 信息体
	 */
	public void doUpdate(DynaBean dynaBean, HttpServletRequest request);
	/**
	 * 批量修改部门
	 * @param ids id
	 * @param deptId 部门id
	 */
	public void doUpdateDept(String ids, String deptId);

	/**
	 * 删除
	 * @param ids id
	 * @param deleteFlag 删除标记
	 */
	public void doRemove(String ids, Boolean deleteFlag);
	/**
	 * 启用
	 * @param ids
	 */
	public void doEnabled(String ids);
	/**
	 * 岗位参数获取
	 * @param sentryIds TODO 暂不明确
	 * @param userId 用户id
	 * @return
	 */
	public List<DynaBean> loadSentryParams(String sentryIds, String userId);
	/**
	 * 得到快速查询人员的树形
	 * @return
	 */
	public List<JSONTreeNode> loadQueryInfo();
	/**
	 * 加载部门查询树形
	 * @param whereSql 查询sql
	 * @return
	 */
	public JSONTreeNode loadDeptInfo(String whereSql);
	/**
	 * 获取角色
	 * @param userId 用户id
	 * @return
	 */
	public List<DynaBean> getRoles(String userId);
	/**
	 * 获取岗位
	 * @param userId 用户id
	 * @return
	 */
	public List<DynaBean> getSentrys(String userId);
	/**
	 * 获取岗位参数值
	 * @param userId 用户id
	 * @param sentryId TODO 暂不明确
	 * @return
	 */
	public DynaBean getSentryValues(String userId, String sentryId);
	/**
	 * 获取岗位参数名
	 * @param sentryId TODO 暂不明确
	 * @return
	 */
	public List<DynaBean> getSentryParams(String sentryId);
	/**
	 * 获取该岗位下所有参数值
	 * @param sentryId TODO 暂不明确
	 * @return
	 */
	public List<DynaBean> getSentryValues(String sentryId);
	/**
	 * 得到角色下所有人员
	 * @param roleId 角色id
	 * @return
	 */
	public List<DynaBean> getUsersByRole(String roleId);
	/**
	 * 得到岗位下所有人员
	 * @param sentryId TODO 暂不明确
	 * @return
	 */
	public List<DynaBean> getUserBySentry(String sentryId);
	/**
	 * 得到部门下所有人员
	 * @param deptId 部门id
	 * @return
	 */
	public List<DynaBean> getUserByDept(String deptId);
//	/**
//	 * 获取用户
//	 * @param userCode
//	 * @return
//	 */
//	public EndUser findUserByCode(String userCode);
	/**
	 * 导入人员信息自定义处理方法
	 * @param dynaBean 自定义动态类
	 * @return
	 */
	public DynaBean impUserData(DynaBean dynaBean);
	/**
	 * 根据人员获取人员信息
	 * @param userId 用户id
	 * @return
	 */
	public DynaBean getUserById(String userId);
	/**
	 * 根据人员ID获取人员信息
	 * @param userId 用户id
	 * @return
	 */
	public EndUser getUserInfoById(String userId);
//	/**
//	 * 根据人员CODE获取人员信息
//	 * @param userCode
//	 * @return
//	 */
//	public EndUser getUserInfoByCode(String userCode);
	/**
	 * 根据人员获取人员信息
	 * @param userIds 用户id
	 * @return
	 */
	public List<DynaBean> getUserByIds(String userIds);

	/**
	 * 获取未知用户
	 * @return
	 */
	public EndUser getUnknowUser();

	/**
	 * 同步部门主管领导信息
	 * @param userIds 用户id
	 * @param deleteFlag 删除标记
	 */
	public void syncDeptZgld(String userIds, Boolean deleteFlag);
	/**
	 * 获取用户领导ID
	 * @param userIds 用户id
	 * @param leaderTypes 部门负责人FZR 监管领导JG 主管领导ZG 直属领导ZSLD   多个用逗号隔开
	 * @return
	 */
	public String[] getUserLeaderIds(String userIds, String leaderTypes);
	/**
	 * 获取用户下属ID
	 * @param userIds 用户id
	 * @param leaderTypes 部门负责人FZR 监管领导JG 主管领导ZG 直属领导ZSLD   多个用逗号隔开
	 * @return
	 */
	public String[] getUserBranchIds(String userIds, String leaderTypes);

	/**
	 * 获取指定人员的领导信息
	 * @param userId 用户id
	 * @return
	 */
	public UserLeader getUserLeaderInfo(String userId);

	/**
	 * 获取指定人员的下属信息
	 * @param userId 用户id
	 * @return
	 */
	public UserLeader getUserBranchInfo(String userId);

	/**
	 * 清除用户登录token信息(用户被禁用或者失效)
	 * @param userId 用户id
	 */
	public void clearUserToken(String userId);

	/**
	 * 清理租户全部人员的领导下属信息
	 * @param zhId 租户id
	 * @return
	 */
	public void removeLeaderAndBranch(String zhId);

//	/**
//	 * 获取租户下人员数量等统计信息
//	 * @param tenantId 租户id
//	 * @return 当前租户下人员限额类产品购买的数量以及总的人员数量和有效人数
//	 */
//	public UserStatisticVo getUserStatisticByCache(String tenantId);
//	public void refreshUserStatistic();
//	public UserStatisticVo findUserStatistic(String tenantId);

	/**
	 * 设置用户是否有效，用户如果有效，代表可以正常使用其归属租户购买的产品，如无效，则代表其无法正常使用归属租户购买的产品
	 * @param userId 用户id
	 * @param isValid TODO 暂不明确
	 */
	public void setUserValidOrNotValid(String userId, boolean isValid);

	/**
	 * 根据当前的租户id，查找当前租户新增用户或者修改用户时，用户的默认角色
	 * @param tenantId 用户id
	 * @return
	 */
	public List<DynaBean> findUserDefaultRoles(String tenantId);

	/**
	 * 添加人员时，增加手机认证
	 * @param user 自定义动态类
	 */
	public void updateUserPhoneAt(DynaBean user);

	/**
	 * 同步所有用户认证消息
	 */
	public void syncUserPhoneAt();


}
