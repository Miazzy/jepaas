package com.je.table.service;

import javax.servlet.http.HttpServletRequest;

import com.je.core.util.bean.DynaBean;

/**
 * 表信息修改  主要负责 键 列 索引的维护
 * @author zhangshuaipeng
 *
 */
public interface TableInfoManager {
	/**
	 * 添加列
	 * @param dynaBean
	 */
	public DynaBean addField(DynaBean dynaBean);

	/**
	 * 删除列
	 * @param dynaBean 自定义动态类
	 * @param ids TODO 暂不明确
	 * @param isDll TODO 咋不明确
	 * @return
	 */
	public Integer removeColumn(DynaBean dynaBean, String ids, Boolean isDll);
	/**
	 * 增量导入
	 * @param dynaBean
	 */
	public void impNewCols(DynaBean dynaBean);
	/**
	 * 字典辅助添加列
	 */
	public void addColumnByDD(HttpServletRequest request);
	/**
	 * 字典辅助添加列
	 */
	public void addColumnByDDList(HttpServletRequest request);
	/**
	 * 表辅助添加列
	 */
	public Integer addColumnByTable(HttpServletRequest request);

	/**
	 * 原子辅助添加列
	 * @param strData TODO 暂不明确
	 * @param tableCode 表编码
	 * @param pkValue 主键
	 * @return
	 */
	public Integer addColumnByAtom(String strData, String tableCode, String pkValue);

	/**
	 * 存为原子
	 * @param strData TODO 暂不明确
	 * @param pkValue 主键
	 * @return
	 */
	public Integer addAtomByColumn(String strData, String pkValue);
	/**
	 * 删除索引
	 * @param dynaBean 自定义动态类
	 * @param ids TODO 暂不明确
	 * @return
	 */
	public Integer removeIndex(DynaBean dynaBean, String ids);

	/**
	 * 删除键
	 * @param dynaBean 自定义动态类
	 * @param ids TODO 暂不明确
	 * @param ddl TODO 暂不明确
	 * @return
	 */
	public Integer removeKey(DynaBean dynaBean, String ids, String ddl);

	/**
	 * 更新树形表数据(路径 层次 顺序 排序字段)
	 * @param tableCode 表编码
	 * @param pkCode 主键code
	 * @param preFix TODO 暂不明确
	 */
	public void syncTreePath(String tableCode, String pkCode, String preFix);
}
