package com.je.develop.service;

import java.util.List;

import com.je.core.util.bean.DynaBean;

/**
 * 功能依赖软连接处理
 * @author zhangshuaipeng
 *
 */
public interface FuncRelyonManager {
	/**
	 * 保存依赖关系
	 * @param funcInfo 目标功能对象
	 * @param type 	         类型     母体/字体
	 * @param funcId   功能外键
	 */
	public void saveRelyon(DynaBean funcInfo, String type, String funcId);
	/**
	 * 监控保存时候触发软连接操作
	 * @param tableCode 表编码
	 * @param funcId TODO未处理
	 * @param dynaBean TODO未处理
	 */
	public void doSave(String tableCode, String funcId, DynaBean dynaBean);
	/**
	 * 监控修改时触发软连接操作
	 * @param tableCode 表编码
	 * @param funcId TODO未处理
	 * @param dynaBean TODO未处理
	 */
	public void doUpdate(String tableCode, String funcId, DynaBean dynaBean);
	/**
	 * 得到修改的值，完成局部修改
	 * @param tableCode 表编码
	 * @param newBean 新数据
	 * @param oldBean 老数据
	 * @return
	 */
	public DynaBean getUpdateBean(String tableCode, DynaBean newBean, DynaBean oldBean);
	/**
	 * 监控列表更新触发软连接操作
	 * @param tableCode 表编码
	 * @param funcId  TODO未处理
	 * @param beans TODO未处理
	 */
	public void doUpdateList(String tableCode, String funcId, List<DynaBean> beans);
	/**
	 * 监控删除时触发软连接操作
	 * @param tableCode 变编码
	 * @param funcId TODO未处理
	 * @param ids ID集合
	 * @param beans TODO未处理
	 */
	public void doRemove(String tableCode, String funcId, String ids, List<DynaBean> beans);
	/**
	 * 监控字段导入时触发软连接操作
	 * @param funcId  TODO未处理
	 */
	public void doImpl(String funcId);
	/**
	 * 监控与列表同步时触发软连接操作
	 * @param funcId  TODO未处理
	 */
	public void doSyncColumn(String funcId);
	/**
	 * 监控与表单同步时触发软连接操作
	 * @param funcId  TODO未处理
	 */
	public void doSyncField(String funcId);
	/**
	 * 移除依赖目标
	 * @param tableCode 表编码
	 * @param softFunc TODO未处理
	 * @param beans TODO未处理
	 */
	public void removeTarget(String tableCode, DynaBean softFunc, List<DynaBean> beans);
	/**
	 * 更新依赖目标
	 * @param tableCode 变编码
	 * @param softFunc TODO未处理
	 * @param newBean TODO未处理
	 */
	public void updateTarget(String tableCode, DynaBean softFunc, DynaBean newBean);
	/**
	 * 添加依赖目标
	 * @param tableCode 变编码
	 * @param softFunc TODO未处理
	 * @param dynaBean TODO未处理
	 */
	public void insertTarget(String tableCode, DynaBean softFunc, DynaBean dynaBean);
	/**
	 * 获取依赖的子体功能对象
	 * @param funcId TODO未处理
	 * @return
	 */
	public List<DynaBean> getRelyons(String funcId);
}
