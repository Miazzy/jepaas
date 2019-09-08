package com.je.rbac.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.je.core.util.bean.DynaBean;
/**
 * 岗位服务层接口
 * @author zhangshuaipeng
 *
 */
public interface SentryManager {
	/**
	 * 保存
	 * @param dynaBean 自定义动态类
	 * @return
	 */
	public DynaBean doSave(DynaBean dynaBean) ;
	/**
	 * 修改
	 * @param dynaBean 自定义动态类
	 * @return
	 */
	public void doUpdate(DynaBean dynaBean);

	/**
	 * 删除
	 * @param ids id
	 * @param deleteFlag 删除标记
	 * @return
	 */
	public Integer doRemove(String ids, Boolean deleteFlag);
	/**
	 * 启用数据
	 * @param ids id
	 * @return
	 */
	public Integer doEnabled(String ids);
	/**
	 * 导入人员
	 * @param userIds 用户id
	 * @param sentryId TODO 暂不明确
	 * @return
	 */
	public void implUsers(String userIds, String sentryId);
	/**
	 * 移除人员
	 * @param userIds 用户id
	 * @param sentryId TODO 暂不明确
	 * @return
	 */
	public void removeUsers(String userIds, String sentryId);
	/**
	 * 同步人员信息
	 * @param users 用户信息
	 */
	public void syncUserInfo(List<DynaBean> users);
	/**
	 * 岗位移动
	 * @param dynaBean 自定义动态类
	 * @param request 信息体
	 * @return
	 */
	public DynaBean sentryMove(DynaBean dynaBean, HttpServletRequest request);
}
