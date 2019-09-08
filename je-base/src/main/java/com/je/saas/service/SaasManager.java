package com.je.saas.service;

import com.je.core.util.bean.DynaBean;
import com.je.phone.vo.DsInfoVo;
import com.je.saas.vo.SaasPermInfo;
import net.sf.json.JSONObject;

import java.util.HashMap;
import java.util.List;

/**
 * TODO 暂不明确
 */
public interface SaasManager {
	/**
	 * TODO 暂不明确
	 * @param params
	 * @return
	 */
	public DynaBean register(JSONObject params);

	/**
	 * 初始化客户
	 * @param khInfo TODO 暂不明确
	 * @param params JSONObject
	 * @return
	 */
	public DynaBean initCompany(DynaBean khInfo, JSONObject params);
	/**
	 * 清理客户信息(取消初始化)
	 * @param khInfo TODO 暂不明确
	 */
	public void clearCompany(DynaBean khInfo);
	/**
	 * 安装指定的产品
	 * @param cpId TODO 暂不明确
	 * @param khId TODO 暂不明确
	 */
	@Deprecated
	public void setupCp(String cpId, String khId, JSONObject params);
	/**
	 * 卸载指定的产品
	 * @param cpId TODO 暂不明确
	 * @param khId TODO 暂不明确
	 */
	@Deprecated
	public void uninstallCp(String cpId, String khId);

	/**
	 * 加入产品用户关系
	 * @param yh TODO 暂不明确
	 * @param cp TODO 暂不明确
	 * @param status TODO 暂不明确
	 * @param params TODO 暂不明确
	 * @return
	 */
	public DynaBean syncCpYhInfo(DynaBean yh, DynaBean cp, String status, JSONObject params);

	/**
	 * 导入产品用户字典信息
	 * @param cpYh TODO 暂不明确
	 * @param addDicIds 添加字典id
	 * @param delDicIds 删除字典id
	 * @param deleteHaveDic TODO 暂不明确
	 */
	public void impCpyhDicInfo(DynaBean cpYh, String addDicIds, String delDicIds, Boolean deleteHaveDic);
	/**
	 * 为产品添加产品角色
	 * @param cpInfo TODO 暂不明确
	 * @param roleIds 角色id
	 */
	public void doAddCpRole(DynaBean cpInfo, String roleIds);
	/**
	 * 为产品删除产品角色
	 * @param cpInfo TODO 暂不明确
	 * @param roleIds 角色id
	 */
	public void doRemoveCpRole(DynaBean cpInfo, String roleIds);
	/**
	 * 删除指定的产品下角色
	 * @param role 角色
	 */
	public void doRemoveCpRole(DynaBean role);

	/**
	 * 清理该产品下用户的权限
	 * @param yhRole TODO 暂不明确
	 * @param cpRole TODO 暂不明确
	 */
	public void doClearCpPerm(DynaBean yhRole, DynaBean cpRole);
	/**
	 * 为指定的产品/用户 添加用户角色
	 * @param cp TODO 暂不明确
	 * @param kh TODO 暂不明确
	 * @param baseRoles TODO 暂不明确
	 */
	public void doAddCpYhRoles(DynaBean cp, DynaBean kh, List<DynaBean> baseRoles, List<DynaBean> cpRoles);
	/**
	 * 同步管理员的角色
	 * @param cp TODO 暂不明确
	 * @param addIds TODO 暂不明确
	 * @param delIds TODO 暂不明确
	 */
	public void doSyncGlyRole(DynaBean cp, String[] addIds, String[] delIds);
	/**
	 * 订单
	 * @param order TODO 暂不明确
	 */
	public void orderCallback(DynaBean order);
	/**
	 * 短信购买订单
	 * @param order TODO 暂不明确
	 */
	public void doNoteCallback(DynaBean order);
	/**
	 * 下订单
	 * @param cp TODO 暂不明确
	 * @param yh TODO 暂不明确
	 * @param params TODO 暂不明确
	 */
	public void doSaveOrder(DynaBean cp, DynaBean yh, JSONObject params);
	/**
	 * 修改用户自定义规划权限
	 * @param addPermIds  TODO 暂不明确
	 * @param delPermIds TODO 暂不明确
	 * @param cpId TODO 暂不明确
	 * @param yhId TODO 暂不明确
	 * @param permType TODO 暂不明确
	 */
	public void doUpdateCpyhPerm(String addPermIds, String delPermIds, String cpId, String yhId, String permType);

	/**
	 * 检测租户租赁是否到期
	 */
	public void checkCpYh();
	/**
	 * 发送验证码
	 * @param zh 帐号信息
	 * @param cz 操作  注册 ZC
	 * @param type 类型  EMAIL || PHONE
	 * @return
	 */
	public String sendSbm(String zh, String cz, String type);
	/**
	 * 每天执行一次,计算用户的使用系统情况
	 */
	public void usageStatistics();
	/**
	 * 获取第三方登录的
	 * @param dsfStateBean TODO 暂不明确
	 * @param dsfCode TODO 暂不明确
	 * @param userId  TODO 暂不明确
	 * @return
	 */
	public JSONObject getDsfLoginInfos(DynaBean dsfStateBean, String dsfCode, String userId);

	/**
	 * 优惠券
	 * @param yhq TODO 暂不明确
	 * @param cpId TODO 暂不明确
	 * @param yhId TODO 暂不明确
	 * @return
	 */
	public String chekYhq(DynaBean yhq, String cpId, String yhId);

	/**
	 * 对已有的用户角色增加改产品的权限
	 * @param cp TODO 暂不明确
	 * @param yh TODO 暂不明确
	 * @param cpRoles TODO 暂不明确
	 * @param yhRoles TODO 暂不明确
	 */
	public void doAddCpRolePerm(DynaBean cp, DynaBean yh, List<DynaBean> cpRoles, List<DynaBean> yhRoles);


	/**
	 * 获取钉钉认证信息
	 * @param tmpAuthCode TODO 暂不明确
	 * @return
	 */
	public HashMap getAuthDT(String tmpAuthCode);

	/**
	 * 同步租户无菜单功能
	 * @param cpId TODO 暂不明确
	 * @param yhIds TODO 暂不明确
	 */
	public void doSyncYhFunc(String cpId, String yhIds);

	/**
	 * 一天内租户注册的名额限制
	 * @return
	 */
	public boolean isLimitRegister();

	/**
	 * 获取SAAS的权限控制信息
	 * @param zhId TODO 暂不明确
	 * @param refresh  0不更新缓存 1 更新缓存
	 * @return
	 */
	public SaasPermInfo getSaasPermInfo(String zhId, String refresh);

	/**
	 * 级联更新租户名称信息
	 * @param zhId
	 * @param zhMc
	 */
	public void doCascadeZhMc(String zhId,String zhMc);

	/**
	 * 周天销售额展板
	 * @param dsInfoVo
	 * @return
	 */
	public String operationTwo(DsInfoVo dsInfoVo);
	/**
	 * 年月销售额展板
	 * @param dsInfoVo
	 * @return
	 */
	public String operationOne(DsInfoVo dsInfoVo);
}
