package com.je.core.service;
/**
 * 登录操作接口
 * @author zhangshuaipeng
 *
 */
public interface LoginInfoService {

	/**
	 * 保存登录操作
	 * @param type 类型
	 * @param yxTime TODO 暂不明确
	 * @param userName 用户名称
	 * @param userId 用户id
	 * @param funcCode 功能code
	 * @param showView 显示视图
	 * @param modelId TODO 暂不明确
	 * @return
	 */
	public String getLoginKey(String type, String yxTime, String userName, String userId, String funcCode, String showView, String modelId);

	/**
	 * 获取该任务的key
	 * @param type 类型
	 * @param yxTime TODO 暂不明确
	 * @param userName 用户名称
	 * @param userId 用户id
	 * @return
	 */
	public String getLoginKey(String type, String yxTime, String userName, String userId);
}
