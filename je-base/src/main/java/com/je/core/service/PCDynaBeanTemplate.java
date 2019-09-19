package com.je.core.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.je.core.base.MethodArgument;
import com.je.develop.vo.FuncInfo;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.je.core.util.bean.DynaBean;
import com.je.rbac.model.EndUser;
/**
 * 平台默认CRUD的方法
 * @author zhangshuaipeng
 *
 */
public interface PCDynaBeanTemplate {
	/**
	 * 保存
	 * @param dynaBean
	 * @return
	 */
	public DynaBean doSave(DynaBean dynaBean);

	/**
	 * 复制数据
	 * @param dynaBean
	 * @param funcCode 功能code
	 * @param codeGenFieldInfo TODO 暂不明确
	 * @param uploadableFields TODO 暂不明确
	 * @return
	 */
	public DynaBean doCopy(DynaBean dynaBean, String funcCode, String codeGenFieldInfo, String uploadableFields);
	/**
	 * 列表更新
	 * @param dynaBean TODO 暂不明确
	 * @param updateStr TODO 暂不明确
	 * @return
	 */
	public List<DynaBean> doUpdateList(DynaBean dynaBean, String updateStr);

	/**
	 * 列表更新
	 * @param dynaBean  TODO 暂不明确
	 * @param updateStr  TODO 暂不明确
	 * @param funcType 功能类型
	 * @param viewConfigInfo   TODO 暂不明确
	 * @return
	 */
	public List<DynaBean> doUpdateList(DynaBean dynaBean, String updateStr, String funcType, String viewConfigInfo);
	/**
	 * 树形节点拖动
	 * @param dynaBean  TODO 暂不明确
	 * @return
	 */
	public DynaBean treeMove(DynaBean dynaBean);

	/**
	 * 级联删除子单
	 * @param dynaBean
	 * @param funcInfo 功能信息
	 * @param ids TODO 暂不明确
	 */
	public void removeChild(DynaBean dynaBean, FuncInfo funcInfo, String ids);
	/**
	 * 级联删除子单  查询出来子去删除
	 * @param dynaBean TODO 暂不明确
	 * @param funcInfo 功能信息
	 */
	public void removeChild(DynaBean dynaBean, FuncInfo funcInfo);
	/**
	 * 判断是否有子功能需级联删除
	 * @param funcInfo 功能json对象
	 * @return
	 */
	public Boolean decideDeleteChildren(FuncInfo funcInfo);
	/**
	 * 验证字段唯一
	 * @param dynaBean
	 * @param fieldCode 验证字段
	 * @return
	 */
	public Boolean checkFieldUniquen(DynaBean dynaBean, String fieldCode);

	/**
	 * 构建排序条件
	 * @param sort
	 * @param orderSql 排序sql
	 * @param useOrderSql TODO 暂不明确
	 * @return
	 */
	public String buildOrderSql(String sort, String orderSql, String useOrderSql);

	/**
	 * 构建ExtJs点击列按指定列排序
	 * @param sort  TODO 暂不明确
	 * @param excludes TODO 暂不明确
	 * @return
	 */
	public String buildSortOrder(String sort, String excludes);
	/**
	 * 编号构建
	 * @param codeGenFieldInfo TODO 暂不明确
	 * @param dynaBean TODO 暂不明确
	 */
	public void buildCode(String codeGenFieldInfo, DynaBean dynaBean);
	/**
	 * 编号构建
	 * @param codeGenFieldInfo TODO 暂不明确
	 * @param dynaBean TODO 暂不明确
	 * @param zhId TODO 暂不明确
	 */
	public void buildCode(String codeGenFieldInfo, DynaBean dynaBean, String zhId);
	/**
	 * 子功能默认添加树形ROOT节点操作
	 */
	public void doChildrenTree(DynaBean dynaBean, String cascadeInfo);
	/**
	 * 保存document信息  主要用于功能上传和只上传附件
	 * @param documentInfo TODO 暂不明确
	 */
	public DynaBean doSaveDocumentInfo(DynaBean documentInfo);

	/**
	 * 批量上传附件处理
	 * @param dynaBean TODO 暂不明确
	 * @param batchFilesFields TODO 暂不明确
	 * @param funcCode TODO 暂不明确
	 * @param doSave TODO 暂不明确
	 * @param request
	 */
	public void doSaveBatchFiles(DynaBean dynaBean, String batchFilesFields, String funcCode, Boolean doSave, HttpServletRequest request);

	/**
	 * 批量上传附件处理（旧方法，保存为了重写action的业务不出错，默认都不是平台）
	 * @param dynaBean TODO 暂不明确
	 * @param batchFilesFields TODO 暂不明确
	 * @param funcCode TODO 暂不明确
	 * @param uploadPath TODO 暂不明确
	 * @param doSave TODO 暂不明确
	 * @param jeFileType TODO 暂不明确
	 */
	public void doSaveBatchFiles(DynaBean dynaBean, String batchFilesFields, String funcCode, String uploadPath, Boolean doSave, String jeFileType);

	/**
	 * 删除批量附件处理
	 * @param tableCode 表code
	 * @param ids
	 */
	public void doRemoveBatchFiles(String tableCode, String ids);

	/**
	 * 删除树形批量附件处理
	 * @param tableCode 表code
	 * @param ids
	 */
	public void doRemoveTreeBatchFiles(String tableCode, String ids);
	/**
	 * 构建树形的排序序号
	 * @param dynaBean
	 */
	public void generateTreeOrderIndex(DynaBean dynaBean);
//	/**
//	 * 获取到查询列
//	 * @param funcCode
//	 * @return
//	 */
//	public String getQueryColumns(String funcCode);
	/**
	 * 构建mark信息
	 * @param lists 查询结果数据信息
	 * @param tableCode 查询参数  取内部   tableCode  pkCode  whereSql
	 */
	public void buildMarkInfo(List<DynaBean> lists, String tableCode, EndUser currentUser, String funcId);
	/**
	 * 构建批注信息
	 * @param lists TODO 暂不明确
	 * @param tableCode 表code
	 * @param funcId TODO 暂不明确
	 */
	public void buildPostilInfo(List<DynaBean> lists, String tableCode, String funcId);
	/**
	 * 构建mark信息
	 * @param lists 查询结果数据信息
	 * @param tableCode 查询参数  取内部   tableCode  pkCode  whereSql
	 * @param currentUser 当前登陆人信息
	 * @param funcId 功能id
	 */
	public void buildFuncEditInfo(List<DynaBean> lists, String tableCode, EndUser currentUser, String funcId);
	/**
	 * 对视图级联的处理
	 * @param viewConfigInfo 对视图级联的处理
	 * @param dynaBean TODO 暂不明确
	 */
	public void doViewData(String viewConfigInfo, DynaBean dynaBean);

	/**
	 * 对视图级联的处理
	 * @param viewConfigInfo  TODO 暂不明确
	 * @param viewTableCode TODO 暂不明确
	 * @param mainPkCode TODO 暂不明确
	 * @param ids TODO 暂不明确
	 */
	public void doViewDelData(String viewConfigInfo, String viewTableCode, String mainPkCode, String ids);

	/**
	 * 删除数据相关 批注， 标记   编辑标记
	 * @param tableCode 表code
	 * @param ids id
	 * @param mark TODO 暂不明确
	 * @param funcEdit TODO 暂不明确
	 * @param postil TODO 暂不明确
	 * @param doTree TODO 暂不明确
	 */
	public void doRemoveData(String tableCode, String pkCode, String ids, boolean mark, boolean funcEdit, boolean postil, boolean doTree);

	/**
	 * 系统默认load加载存储过程，SQL数据信息
	 * @param queryType
	 */
	public Map<String,Object> doLoadOtherData(String queryType, MethodArgument param,String order);
}
