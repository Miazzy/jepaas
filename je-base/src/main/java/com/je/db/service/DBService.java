package com.je.db.service;

import com.je.core.util.JdbcUtil;
import com.je.core.util.bean.DynaBean;

import java.util.List;

/**
 * 平台迁移
 */
public interface DBService {
	/**
	 * 转库操作
	 * @param dynaBean
	 */
	public void doDb(DynaBean dynaBean);

    /**
     * 覆盖平台所有结构
     * @param dynaBean
     */
    public void doOverrideDB(DynaBean dynaBean);
    /**
     * 覆盖平台所有结构
     * @param dynaBean
     */
    public void doOverrideTableDB(DynaBean dynaBean);
    /**
     * 覆盖平台所有结构
     * @param dynaBean
     */
    public void doOverrideTableData(DynaBean dynaBean);

    /**
     * 表结构按照主子排序
     * @param tables
     * @return
     */
    public List<DynaBean> orderTable(List<DynaBean> tables);

    /**
     * 同步业务表数据
     * @param jdbcUtil
     * @param tableCode
     * @param whereSql
     * @param deleteData
     * @return
     */
    public List<DynaBean> syncTableData(JdbcUtil jdbcUtil,String tableCode,String whereSql,Boolean deleteData);

    /**
     * 检查未在资源表维护的表名
     * @param dynaBean
     * @return
     */
    public List<String> checkDbTableInfo(DynaBean dynaBean);
	/**
	 * 检测系统信息
	 * @param jdbcUtil jdbc工具类
	 * @param dynaBean TODO 暂不明确
	 * @param before TODO 暂不明确
	 */
	public void doCheckInfo(JdbcUtil jdbcUtil, DynaBean dynaBean, Boolean before);
	/**
	 * 同步所有表结构
	 * @param jdbcUtil jdbc工具类
	 * @param dynaBean  TODO 暂不明确
	 */
	public void doTableInfo(JdbcUtil jdbcUtil, DynaBean dynaBean);
	/**
	 * 同步所有功能信息
	 * @param jdbcUtil jdbc工具类
	 * @param dynaBean TODO 暂不明确
	 */
	public void doFuncInfo(JdbcUtil jdbcUtil, DynaBean dynaBean);
	/**
	 * 同步所有功能信息
	 * @param jdbcUtil jdbc工具类
	 * @param dynaBean TODO 暂不明确
	 */
	public void doPhoneInfo(JdbcUtil jdbcUtil, DynaBean dynaBean);
	/**
	 * 同步所有系統功能数据
	 * @param jdbcUtil jdbc工具类
	 * @param dynaBean TODO 暂不明确
	 */
	public void doSysInfo(JdbcUtil jdbcUtil, DynaBean dynaBean);
	/**
	 * 同步所有流程数据
	 * @param jdbcUtil jdbc工具类
	 * @param dynaBean TODO 暂不明确
	 */
	public void doWfData(JdbcUtil jdbcUtil, DynaBean dynaBean);
	/**
	 * 同步RBAC数据
	 * @param jdbcUtil jdbc工具类
	 * @param dynaBean TODO 暂不明确
	 */
	public void doRabcData(JdbcUtil jdbcUtil, DynaBean dynaBean);
	/**
	 * 同步所有业务数据
	 * @param jdbcUtil jdbc工具类
	 * @param dynaBean  TODO 暂不明确
	 */
	public void doYwData(JdbcUtil jdbcUtil, DynaBean dynaBean);
	/**
	 * 更新核心信息
	 * @param jdbcUtil jdbc工具类
	 * @param dynaBean  TODO 暂不明确
	 */
	public void doSyncCoreInfo(JdbcUtil jdbcUtil, DynaBean dynaBean);

	/**
	 * 导出表结构
	 * @param title 标题
	 * @param type 类型
	 * @param whereSql 查询sql
	 * @param filePath 文件路径
	 */
	public void doTableInfo(String title, String type, String whereSql, String filePath);
	/**
	 * 检查重复字典编码现象，删除jeplus中字段，以jepf的字典为标准
	 * @param jdbcUtil jdbc工具类
	 * @param dynaBean  TODO 暂不明确
	 */
	public void doCheckSameDicCode(JdbcUtil jdbcUtil, DynaBean dynaBean);
	/**
	 * 规划树形路径
	 * @param jdbcUtil jdbc工具类
	 * @param dynaBean  TODO 暂不明确
	 */
	public void doSyncTreePath(JdbcUtil jdbcUtil, DynaBean dynaBean);
}
