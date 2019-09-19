package com.je.core.service;

import java.util.List;

import com.je.rbac.model.EndUser;
import net.sf.json.JSONArray;
import com.je.core.entity.extjs.Model;
import com.je.core.util.bean.DynaBean;

/**
 * 根据SQL查询数据
 */
public interface PcDbSQLManager {
	/**
	 * 查询数据
	 * @param sql sql语句
	 * @return
	 */
	public List<Model> selectModel(String sql);
	/**
	 * 查询出SQL的名称
	 * @param sql sql语句
	 * @return
	 */
	public List<DynaBean> select(String sql);

	/**
	 * 查询出SQL的名称
	 * @param sql sql语句
	 * @param limit 分页
	 * @return
	 */
	public List<DynaBean> select(String sql, int limit);

	/**
	 * 执行前台的事务操作
	 * @param arrays
	 * @param currentUser 当前登陆人信息
	 */
	public void doTransaction(JSONArray arrays, EndUser currentUser);
}
