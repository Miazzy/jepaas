package com.je.staticize.service;

/**
 * TODO 暂不明确
 */
public interface StaticizeManager {
	/**
	 * 菜单静态化
	 * @param userId 用户id
	 * @param menuStr TODO 暂不明确
	 * @param enMenuStr TODO 暂不明确
	 */
	public void doMenu(String userId, String menuStr, String enMenuStr);

	/**
	 * 清空功能的缓存
	 * @param funcCodes 功能编码
	 * @param allZh TODO 暂不明确
	 */
	public void clearFuncAllStatize(String funcCodes, Boolean allZh);
	/**
	 * 清空功能的缓存
	 * @param funcCodes 功能编码
	 */
	public void clearFuncPermStatize(String funcCodes);

	/**
	 * 清楚关于用户的静态化
	 * @param whereSql 查询sql
	 * @param userCache 用户缓存
	 */
	public void clearUserStatize(String whereSql, Boolean userCache);
    /**
     * 静态化功能配置
     * @param funcCode 功能编码
     * @param funcValue 配置值
     */
    public void doFuncInfo(String funcCode, String funcValue, String funcEnValue);
}
