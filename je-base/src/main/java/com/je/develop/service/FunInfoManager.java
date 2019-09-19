package com.je.develop.service;

import java.util.List;

import com.je.develop.vo.FuncInfo;
import com.je.develop.vo.FuncRelationField;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.je.core.util.bean.DynaBean;
import com.je.develop.vo.FuncPermVo;

/**
 * TODO未处理
 */
public interface FunInfoManager {
	/**
	 * 导入按钮
	 * @author sunwanxiang
	 * @date 2012-3-16 下午01:17:35
	 * @param funInfo TODO未处理
	 * @param funId TODO未处理
	 */
	public void implButton(DynaBean funInfo, String funId);
	/**
	 * 获取json的功能对象
	 * @param funcCode TODO未处理
	 * @return
	 */
	public FuncInfo getFuncInfo(String funcCode);
//	/**
//	 * 获取功能权限
//	 * @param funcId
//	 * @return
//	 */
//	public FuncPermVo getFuncPerm(String funcId,List<DynaBean> roles,DynaBean user);
	/**
	 * 根据功能对象获取功能配置信息
	 * @param funInfo TODO未处理
	 * @return
	 */
	public String getStrByFunInfo(DynaBean funInfo);
	/**
	 * 构建功能默认信息
	 * @param funcInfo TODO未处理
	 */
	public void buildDefaultFuncInfo(DynaBean funcInfo);
	/**
	 * 更新功能信息(包括对按钮导入、软连接、资源表等做处理)
	 * @param funcInfo TODO未处理
	 */
	public DynaBean updateFunInfo(DynaBean funcInfo);
	/**
	 * 删除功能   (包括对功能权限 按钮权限清除。 级联更新父节点信息 、删除挂有该功能的子功能信息和软连接关系清除)
	 * @param funcId TODO未处理
	 */
	public void  removeFuncInfoById(String funcId);
	/**
	 * 功能复制
	 * @param newFunInfo 新业务数据
	 * @param oldFuncId 老数据ID
	 */
	public DynaBean copyFuncInfo(DynaBean newFunInfo, String oldFuncId);
	/**
	 * 软连接复制
	 * @return
	 */
	public DynaBean copySoftFuncInfo(DynaBean newFunInfo, DynaBean oldFunInfo);
	/**
	 * 构建主子功能关联字段的查询条件
	 * @param relatedFields TODO未处理
	 * @param dynaBean TODO未处理
	 */
	public String buildWhereSql4funcRelation(List<FuncRelationField> relatedFields, DynaBean dynaBean);
	/**
	 * 清空功能
	 * @param funcInfo TODO未处理
	 */
	public DynaBean clearFuncInfo(DynaBean funcInfo);
	/**
	 * 初始化功能
	 * @param funcInfo TODO未处理
	 */
	public void initFuncInfo(DynaBean funcInfo);
	/**
	 * 解除软连接关系
	 * @param funcRelyonId TODO未处理
	 */
	public void removeFuncRelyon(String funcRelyonId);
	/**
	 * 功能拖动
	 * @param dynaBean
	 */
	public DynaBean treeMove(DynaBean dynaBean);
	/**
	 * 同步所有功能的权限数据
	 */
	public void syncFuncPermData();

}
