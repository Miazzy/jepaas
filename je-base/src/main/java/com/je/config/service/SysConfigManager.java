/**
 *
 */
package com.je.config.service;

import com.je.core.util.bean.DynaBean;

import java.util.Map;

/**
 * 系统/用户变量接口
 * @author marico
 * @bean 注入名称： sysConfigManager
 */
public interface SysConfigManager {

	/**
	 * 加载用户变量
	 */
	public void doLoadUserConfigVariables();
	/**
	 * 加载系统变量
	 */
	public void doLoadSysConfigVariables();
	/**
	 * 加载JE文档路径默认
	 */
	public void doLoadSysDocVariables();

	/**
	 *  更新系统变量
	 * @param dynaBean
	 * @param allFields  TODO 暂不明确
	 */
	public void doWriteSysConfigVariables(DynaBean dynaBean, String[] allFields);
	/**
	 * 更新开发变量
	 * @param dynaBean
	 */
	public void doWriteDevelopConfigVariables(DynaBean dynaBean);
	/**
	 * 修改系统模式
	 * @param sysMode TODO 暂不明确
	 */
	public void doWriteSysModeVariables(String sysMode);
	/**
	 * 加载过滤功能和资源表信息
	 */
	public void doLoadJepfConfig();
	/**
	 * 加载saas模式系统设置
	 * @param zhId TODO 暂不明确
	 */
	public Map<String,Object> doLoadSaasConfig(String zhId);
	/**
	 * 修改图标版本
	 * @param version
	 */
	public void doWriteIconVersion(String version);
	/**
	 * 修改平台版本
	 * @param version
	 */
	public void doWriteJepfVersion(String version);
	/**
	 * 获取平台最新版本
	 */
	public String getJepfVersion();

	/**
	 * 初始化版本
	 */
	public void initJepfVersion();
}