package com.je.table.service;

import java.sql.SQLException;

/**
 * 转化数据库操作层次
 * @author zhangshuaipeng
 *
 */
public interface DataBaseManager {
	/**
	 * 生成数据库SQL
	 * @param dbName
	 */
	public void generateSql(String dbName);
	/**
	 * 同步数据
	 * @throws SQLException
	 */
	public void syncOracleData() throws Exception;
	/**
	 * 同步mySql数据
	 * @throws Exception
	 */
	public void syncMySqlData() throws Exception;
	/**
	 * 同步jbpm数据
	 */
	public void syncOracleJbpm();
	/**
	 * 转oracle
	 */
	public void toOracle();

	/**
	 * 转Myql
	 */
	public void toMysql();
}
