package com.je.core.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


import com.je.core.exception.PlatformException;
import com.je.core.exception.PlatformExceptionEnum;
import com.je.core.util.*;
import com.je.develop.service.FunInfoManager;
import com.je.develop.vo.FuncInfo;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.je.core.constants.tree.NodeType;
import com.je.core.constants.wf.AudFlagStatus;
import com.je.core.dao.PCDaoTemplate;
import com.je.core.dao.PCDynaDaoTemplate;
import com.je.core.entity.QueryInfo;
import com.je.core.entity.extjs.JSONTreeNode;
import com.je.core.facade.extjs.JsonAssist;
import com.je.core.util.SecurityUserHolder;
import com.je.core.util.bean.BeanUtils;
import com.je.core.util.bean.DynaBean;
import com.je.develop.service.CodeGenManager;
import com.je.document.service.DocumentManager;
import com.je.rbac.model.Department;
import com.je.rbac.model.EndUser;
/**
 * 动态Bean的服务类实现
 * @author YUNFENGCHENG
 *
 */
@Component("PCDynaServiceTemplate")
public class PCDynaServiceTemplateImpl implements PCDynaServiceTemplate {
	private static Logger logger = LoggerFactory.getLogger(PCDynaServiceTemplate.class);
	@Autowired
	private PCDynaDaoTemplate dynaDaoTemplate;
	@Autowired
	private PCDaoTemplate daoTemplate;
	@Autowired
	private CodeGenManager codeGenManager;
	@Autowired
	private DocumentManager documentManager;
	@Autowired
	private FunInfoManager funInfoManager;
	//JSON辅助转换器
	protected JsonAssist jsonAssist=JsonAssist.getInstance();
	/**
	 * 插入一条带指定数据的数据库记录，如果对应字段没有值，则系统将字符串类型字段置为'', 将数字类型字段置为0
	 *
	 * @param dynaBean          存放要插入的数据库记录信息
	 * @return 插入是否成功：      1 是， 0 否
	 */
	@SuppressWarnings("unchecked")
	@Override
	public DynaBean insert(DynaBean dynaBean){
		DynaBean dynaBeanPo = dynaDaoTemplate.insert(dynaBean);
		List<DynaBean> docInfo = (List<DynaBean>) dynaBean.get(BeanUtils.KEY_DOC_INFO);
		if(null != docInfo && 0 != docInfo.size()) {
			doSaveDocuments(docInfo, dynaBeanPo,false);
		}
		return dynaBeanPo;
	}

	/**
	 * 批量插入数据
	 * @param beans
	 */
	@Override
	public void insert(List<DynaBean> beans){
		// TODO Auto-generated method stub
		dynaDaoTemplate.insert(beans);
	}
	/**
	 * 基于主键删除一条数据库记录
	 *
	 * @param dynaBean      数据信息
	 * @return              本类
	 */
	@Override
	public DynaBean delete(DynaBean dynaBean){
		// TODO Auto-generated method stub
		return dynaDaoTemplate.delete(dynaBean);
	}
	/**
	 * 基于主键假删除一条数据库记录
	 *
	 * @param dynaBean      数据信息
	 * @return              删除行数，正常为1
	 */
	@Override
	public DynaBean deleteFake(DynaBean dynaBean){
		return dynaDaoTemplate.deleteFake(dynaBean);
	}
	/**
	 * 基于主键启用一条数据库记录
	 *
	 * @param dynaBean      数据信息
	 * @return              删除行数，正常为1
	 */
	@Override
	public DynaBean enableFake(DynaBean dynaBean){
		return dynaDaoTemplate.enableFake(dynaBean);
	}
	/*
	 * (non-Javadoc)
	 * @see com.je.core.service.PCDynaServiceTemplate#deleteFake(java.lang.String, java.lang.String[])
	 */

	/**基于主键假删除一条数据库记录
	 *
	 * @param tableName
	 * @param ids           主键数组
	 * @return
	 */
	@Override
	public Integer deleteFake(String tableName, String[] ids){
		for(String id : ids) {
			DynaBean dynaBean = selectOneByPk(tableName, id);
			deleteFake(dynaBean);
		}

		return ids.length;
	}

	/**
	 * 基于主键假删除一条数据库记录
	 * @param tableName
	 * @param ids           主键数组
	 * @return
	 */
	@Override
	public Integer enableFake(String tableName, String[] ids){
		for(String id : ids) {
			DynaBean dynaBean = selectOneByPk(tableName, id);
			enableFake(dynaBean);
		}

		return ids.length;
	}

	/**
	 * 根据动态类中$WHERE$项设定的条件删除多条数据库记录
	 *
	 * @param dynaBean          数据信息
	 * @return                  删除行数
	 */
	@Override
	public int deleteByWhereSql(DynaBean dynaBean){
		// TODO Auto-generated method stub
		return dynaDaoTemplate.deleteByCondition(dynaBean);
	}

	@Override
	public int deleteByWhereSql(DynaBean dynaBean, Object[] params) {
		return dynaDaoTemplate.deleteByCondition(dynaBean,params);
	}

	@Override
	public int deleteByWhereSql(DynaBean dynaBean, Map<String, Object> params) {
		return dynaDaoTemplate.deleteByCondition(dynaBean,params);
	}

	/**
	 * 根据SQL删除指定表数据
	 * @param tableCode
	 * @param whereSql
	 * @return
	 */
	public int deleteByWehreSql(String tableCode,String whereSql){
		DynaBean dynaBean=new DynaBean(tableCode,true);
		dynaBean.set(BeanUtils.KEY_WHERE, whereSql);
		return deleteByWhereSql(dynaBean);
	}

	@Override
	public int deleteByWehreSql(String tableCode, String whereSql, Object[] params) {
		DynaBean dynaBean=new DynaBean(tableCode,true);
		dynaBean.set(BeanUtils.KEY_WHERE, whereSql);
		return deleteByWhereSql(dynaBean,params);
	}

	@Override
	public int deleteByWehreSql(String tableCode, String whereSql, Map<String, Object> params) {
		DynaBean dynaBean=new DynaBean(tableCode,true);
		dynaBean.set(BeanUtils.KEY_WHERE, whereSql);
		return deleteByWhereSql(dynaBean,params);
	}

	/**
	 * 根据用逗号分开的主键id字符串批量删除表中的数据
	 * @param ids 主键字符串
	 * @param tableName 表名字
	 * @param idName 主键名称
	 * @return 删除的行数
	 */
	@Override
	public int deleteByIds(String ids, String tableName, String idName){
		// TODO Auto-generated method stub
		return dynaDaoTemplate.deleteByIds(ids, tableName, idName);
	}
	/**
	 * 根据逗号分开的主键id字符批量删除树形的数据
	 * @param ids
	 * @param tableName
	 */
	public int deleteTreeByIds(String ids,String tableName){
		String[] idArray=ids.split(",");
		String whereSql="";
		for(String id:idArray){
			whereSql+=" OR SY_PATH like '%"+id+"%' ";
		}
		daoTemplate.executeSql("DELETE FROM "+tableName+" where 1!=1 "+whereSql);
		return idArray.length;
	}
	/**
	 * 根据主键查询一条指定的记录。
	 *
	 * @param dynaBean      数据信息
	 * @return              数据库记录相信信息，不存在数据时返回空
	 */
	@Override
	public DynaBean selectByPk(DynaBean dynaBean){
		// TODO Auto-generated method stub
		return dynaDaoTemplate.selectByPk(dynaBean);
	}
	/**
	 * 根据主键查询一条指定的记录。
	 *
	 * @param dynaBean      数据信息
	 * @param queryFields   查询字段   多个按逗号隔开
	 * @return              数据库记录相信信息，不存在数据时返回空
	 */
	@Override
	public DynaBean selectByPk(DynaBean dynaBean,String queryFields){
		// TODO Auto-generated method stub
		dynaBean.set(BeanUtils.KEY_QUERY_FIELDS, queryFields);
		return dynaDaoTemplate.selectByPk(dynaBean);
	}
//	/**
//	 * 根据主键或者过滤条件查询一条指定的记录：
//	 * 如果是则使用主键字段，则自动根据动态类中主键的信息进行过滤
//	 * 如果不是使用主键字段，则使用$WHERE$中的信息进行过滤
//	 *
//	 * @param dynaBean      数据信息
//	 * @param bKeySelect    是否基于主键查找一条指定的记录
//	 * @return              数据库记录相信信息，不存在数据时返回空
//	 */
//	@Override
//	public DynaBean select(DynaBean dynaBean, boolean bKeySelect){
//		// TODO Auto-generated method stub
//		if(bKeySelect){
//			return dynaDaoTemplate.selectByPk(dynaBean);
//		}else{
//			return dynaDaoTemplate.selectOne(dynaBean);
//		}
//	}
//	/**
//	 * 根据主键或者过滤条件查询一条指定的记录：
//	 * 如果是则使用主键字段，则自动根据动态类中主键的信息进行过滤
//	 * 如果不是使用主键字段，则使用$WHERE$中的信息进行过滤
//	 *
//	 * @param dynaBean      数据信息
//	 * @param queryFields 	查询字段，多个按逗号隔开
//	 * @param bKeySelect    是否基于主键查找一条指定的记录
//	 * @return              数据库记录相信信息，不存在数据时返回空
//	 */
//	@Override
//	public DynaBean select(DynaBean dynaBean,String queryFields, boolean bKeySelect){
//		// TODO Auto-generated method stub
//		dynaBean.set(BeanUtils.KEY_QUERY_FIELDS, queryFields);
//		if(bKeySelect){
//			return dynaDaoTemplate.selectByPk(dynaBean);
//		}else{
//			return dynaDaoTemplate.selectOne(dynaBean);
//		}
//	}
	/**
	 * 根据$WEHRE$条件查询符合条件的纪录数量
	 *
	 * @param dynaBean      数据信息
	 * @return              符合条件的纪录数
	 */
	@Override
	public long selectCount(DynaBean dynaBean){
		// TODO Auto-generated method stub
		return dynaDaoTemplate.selectCount(dynaBean);
	}

	@Override
	public long selectCount(DynaBean dynaBean, Object[] params) {
		return dynaDaoTemplate.selectCount(dynaBean,params);
	}

	@Override
	public long selectCount(DynaBean dynaBean, Map<String, Object> params) {
		return dynaDaoTemplate.selectCount(dynaBean,params);
	}

	/**
	 * 根据$WEHRE$条件查询符合条件的纪录数量
	 *
	 * @param tableCode      表名
	 * @param whereSql       查询条件
	 * @return              符合条件的纪录数
	 */
	@Override
	public long selectCount(String tableCode,String whereSql){
		// TODO Auto-generated method stub
		DynaBean bean=new DynaBean(tableCode,true);
		bean.set(BeanUtils.KEY_WHERE, whereSql);
		return dynaDaoTemplate.selectCount(bean);
	}

	@Override
	public long selectCount(String tableCode, String whereSql, Object[] params) {
		DynaBean bean=new DynaBean(tableCode,true);
		bean.set(BeanUtils.KEY_WHERE, whereSql);
		return dynaDaoTemplate.selectCount(bean,params);
	}

	@Override
	public long selectCount(String tableCode, String whereSql, Map<String, Object> params) {
		DynaBean bean=new DynaBean(tableCode,true);
		bean.set(BeanUtils.KEY_WHERE, whereSql);
		return dynaDaoTemplate.selectCount(bean,params);
	}

	/**
	 * 根据条件查询数据库中的一条记录，如果查询出多条记录，那么只取第一条记录
	 * @param dynaBean  查询条件bean
	 * @return  结果bean
	 */
	@Override
	public DynaBean selectOne(DynaBean dynaBean){
		// TODO Auto-generated method stub
		return dynaDaoTemplate.selectOne(dynaBean);
	}

	@Override
	public DynaBean selectOne(DynaBean dynaBean, Object[] params) {
		return dynaDaoTemplate.selectOne(dynaBean,params);
	}

	@Override
	public DynaBean selectOne(DynaBean dynaBean, Map<String, Object> params) {
		return dynaDaoTemplate.selectOne(dynaBean,params);
	}

	/**
	 * 根据条件查询数据库中的一条记录，如果查询出多条记录，那么只取第一条记录
	 * @param dynaBean  查询条件bean
	 * @param queryFields 	查询字段，多个按逗号隔开
	 * @return  结果bean
	 */
	@Override
	public DynaBean selectOne(DynaBean dynaBean,String queryFields){
		// TODO Auto-generated method stub
		dynaBean.set(BeanUtils.KEY_QUERY_FIELDS, queryFields);
		return dynaDaoTemplate.selectOne(dynaBean);
	}

	@Override
	public DynaBean selectOne(DynaBean dynaBean, Object[] params, String queryFields) {
		dynaBean.set(BeanUtils.KEY_QUERY_FIELDS, queryFields);
		return dynaDaoTemplate.selectOne(dynaBean,params);
	}

	@Override
	public DynaBean selectOne(DynaBean dynaBean, Map<String, Object> params, String queryFields) {
		dynaBean.set(BeanUtils.KEY_QUERY_FIELDS, queryFields);
		return dynaDaoTemplate.selectOne(dynaBean,params);
	}

	/**
	 * 根据条件查询数据库记录列表，不分页，取全部信息
	 *
	 * @param dynaBean          数据信息
	 * @return                  数据库记录列表信息
	 */
	@Override
	public ArrayList<DynaBean> selectList(DynaBean dynaBean){
		// TODO Auto-generated method stub
		return dynaDaoTemplate.selectList(dynaBean);
	}

	@Override
	public ArrayList<DynaBean> selectList(DynaBean dynaBean, Object[] params) {
		return dynaDaoTemplate.selectList(dynaBean,params);
	}

	@Override
	public ArrayList<DynaBean> selectList(DynaBean dynaBean, Map<String, Object> params) {
		return dynaDaoTemplate.selectList(dynaBean,params);
	}

	/**
	 * 根据条件查询数据库记录列表，不分页，取全部信息
	 *
	 * @param dynaBean          数据信息
	 * @param queryFields 	查询字段，多个按逗号隔开
	 * @return                  数据库记录列表信息
	 */
	@Override
	public ArrayList<DynaBean> selectList(DynaBean dynaBean,String queryFields){
		// TODO Auto-generated method stub
		dynaBean.set(BeanUtils.KEY_QUERY_FIELDS, queryFields);
		return dynaDaoTemplate.selectList(dynaBean);
	}

	@Override
	public ArrayList<DynaBean> selectList(DynaBean dynaBean, Object[] params, String queryFields) {
		dynaBean.set(BeanUtils.KEY_QUERY_FIELDS, queryFields);
		return dynaDaoTemplate.selectList(dynaBean,params);
	}

	@Override
	public ArrayList<DynaBean> selectList(DynaBean dynaBean, Map<String, Object> params, String queryFields) {
		dynaBean.set(BeanUtils.KEY_QUERY_FIELDS, queryFields);
		return dynaDaoTemplate.selectList(dynaBean,params);
	}

	/**
	 * 根据条件查询数据库记录列表
	 *
	 * @param dynaBean      查询信息
	 * @param limit         返回纪录数
	 * @param start         开始的纪录条数
	 * @return              数据库记录以及结果信息
	 */
	@Override
	public ArrayList<DynaBean> selectList(DynaBean dynaBean, int start,int limit){
		// TODO Auto-generated method stub
		return dynaDaoTemplate.selectList(dynaBean, start, limit,false);
	}

	@Override
	public ArrayList<DynaBean> selectList(DynaBean dynaBean, Object[] params, int start, int limit) {
		return dynaDaoTemplate.selectList(dynaBean,params, start, limit,false);
	}

	@Override
	public ArrayList<DynaBean> selectList(DynaBean dynaBean, Map<String, Object> params, int start, int limit) {
		return dynaDaoTemplate.selectList(dynaBean,params, start, limit,false);
	}

	/**
	 * 根据条件查询数据库记录列表
	 *
	 * @param dynaBean      查询信息
	 * @param limit         返回纪录数
	 * @param start         开始的纪录条数
	 * @param queryFields 	查询字段，多个按逗号隔开
	 * @return              数据库记录以及结果信息
	 */
	@Override
	public ArrayList<DynaBean> selectList(DynaBean dynaBean, int start,int limit,String queryFields){
		// TODO Auto-generated method stub
		dynaBean.set(BeanUtils.KEY_QUERY_FIELDS, queryFields);
		return dynaDaoTemplate.selectList(dynaBean, start, limit,false);
	}

	@Override
	public ArrayList<DynaBean> selectList(DynaBean dynaBean, Object[] params, int start, int limit, String queryFields) {
		dynaBean.set(BeanUtils.KEY_QUERY_FIELDS, queryFields);
		return dynaDaoTemplate.selectList(dynaBean,params, start, limit,false);
	}

	@Override
	public ArrayList<DynaBean> selectList(DynaBean dynaBean, Map<String, Object> params, int start, int limit, String queryFields) {
		dynaBean.set(BeanUtils.KEY_QUERY_FIELDS, queryFields);
		return dynaDaoTemplate.selectList(dynaBean,params, start, limit,false);
	}

	/**
	 * 传入tableName和主键值，返回一个DynaBean，如果未找到则返回null
	 * @param tableName 表名
	 * @param pk 主键值
	 * @return
	 */
	@Override
	public DynaBean selectOneByPk(String tableName, String pk) {
		DynaBean result = null;
		if(StringUtil.isNotEmpty(tableName)) {
			DynaBean dynaBean = new DynaBean(tableName,true);
			dynaBean.set(dynaBean.getStr(BeanUtils.KEY_PK_CODE),pk);
			result = dynaDaoTemplate.selectByPk(dynaBean);
		}
		return result;
	}

	/**
	 * 传入tableName和主键值，返回一个DynaBean，如果未找到则返回null
	 * @param tableName 表名
	 * @param pk 主键值
	 * @param queryFields 查询字段，多个按逗号隔开
	 * @return
	 */
	@Override
	public DynaBean selectOneByPk(String tableName, String pk,String queryFields) {
		DynaBean result = null;
		if(StringUtil.isNotEmpty(tableName)) {
			DynaBean dynaBean = new DynaBean(tableName,true);
			dynaBean.set(dynaBean.getStr(BeanUtils.KEY_PK_CODE),pk);
			dynaBean.set(BeanUtils.KEY_QUERY_FIELDS,queryFields);
			result = dynaDaoTemplate.selectByPk(dynaBean);
		}
		return result;
	}
	/**
	 * 基于主键修改数据库记录信息
	 * @param dynaBean                要更新的数据库记录信息
	 * @return                        更新好的数据BEAN
	 */
	@SuppressWarnings("unchecked")
	@Override
	public DynaBean update(DynaBean dynaBean){
		DynaBean dynaBeanPo =  dynaDaoTemplate.update(dynaBean);
		List<DynaBean> docInfo = (List<DynaBean>) dynaBean.get(BeanUtils.KEY_DOC_INFO);
		if(null != docInfo && 0 != docInfo.size()) {
			dynaBeanPo=doSaveDocuments(docInfo, dynaBeanPo,true);
		}
		return dynaBeanPo;
	}
	/**
	 * 基于whereSql修改数据库的一条记录信息
	 * @param dynaBean
	 * @return
	 */
	@Override
	public int listUpdate(DynaBean dynaBean){
		return dynaDaoTemplate.listUpdate(dynaBean);
	}

	@Override
	public int listUpdate(DynaBean dynaBean, Object[] params) {
		return dynaDaoTemplate.listUpdate(dynaBean,params);
	}

	@Override
	public int listUpdate(DynaBean dynaBean, Map<String, Object> params) {
		return dynaDaoTemplate.listUpdate(dynaBean,params);
	}

	/**
	 * 构建dynaBean的树形
	 * @param lists
	 * @return
	 */
	public HashMap buildJSONTree4Dyna(List<HashMap> lists,String pkCode,String parentCode) {
		// TODO Auto-generated method stub
		HashMap root=new HashMap();
		for(HashMap node:lists) { //当前循环这个集合每一个元素
			if(node.get(parentCode)==null || node.get(parentCode).equals("")){
				root=node;
				lists.remove(node);
				break;
			}
		}
		createTreeChildren(lists, root,pkCode,parentCode);
		return root;
	}
	/**
	 * 递归方法
	 * @param childrens
	 * @param root
	 */
	public void createTreeChildren(List<HashMap> childrens,HashMap root,String pkCode,String parentCode){
		String parentId=(String) root.get(pkCode);
		for(int i=0;i<childrens.size();i++){
			HashMap node=childrens.get(i);
			if(node.get(parentCode)!=null){
				if(node.get(parentCode).toString().equalsIgnoreCase(parentId)){
					((List<HashMap>)root.get("children")).add(node);
					if(((List<HashMap>)root.get("children")).size()>0){
						root.put("leaf", false);
					}
					//当前不能删除节点，因为孩子引用与它， 递归回来，坐标失效
					createTreeChildren(childrens, node,pkCode,parentCode);
				}
				if(i==childrens.size()-1){
					return;
				}
			}
		}
	}
	/*
	 * (non-Javadoc)
	 * @see com.je.core.service.PCDynaServiceTemplate#listUpdate(java.lang.String[])
	 */

	/**
	 * 传入一个SQL数组，批量执行更新操作（列表更新保存操作）
	 * @param sqls
	 * @return
	 */
	@Override
	public Integer listUpdate(String[] sqls) {
		Integer count = 0;
		Connection connection = daoTemplate.getConnection();
		Statement stmt = null;
		try {
			stmt = connection.createStatement();
			for(String updateSql : sqls) {
				if(StringUtil.isNotEmpty(updateSql)){
					stmt.addBatch(updateSql);
				}
			}
			int[] batchCounts = stmt.executeBatch();
			for(int ct : batchCounts) {
				count += ct;
			}
			connection.commit();
			return count;
		} catch (SQLException e) {
//			logger.error("", e);
			throw new PlatformException("批量执行SQL异常!", PlatformExceptionEnum.JE_CORE_SQL_BATCHEXECUTE_ERROR,new Object[]{sqls},e);
		} finally {
			JdbcUtil.close(null, stmt, connection);
		}
	}

	/*
	 * 为Model保存相关的上传文档信息
	 */

	/**
	 * 为Model保存相关的上传文档信息
	 * @param documentInfo
	 * @param entityPo
	 * @param isQuery
	 * @return
	 */
	@Override
	public DynaBean doSaveDocuments(List<DynaBean> documentInfo, DynaBean entityPo,Boolean isQuery) {
		// 获取实体的主键值
		String domainId = entityPo.getPkValue(); // 主键
		// 上传文档对象列表
		Iterator<DynaBean> itor = documentInfo.iterator();
		while(itor.hasNext()) {
			DynaBean doc = itor.next();
			doc.set("DOCUMENT_PKVALUE",domainId);
			documentManager.doRemoveDocuments(entityPo.getStr(BeanUtils.KEY_TABLE_CODE), doc.getStr("DOCUMENT_FIELDCODE"), domainId);
			if(StringUtil.isNotEmpty(doc.getStr("SY_CREATEUSER"))){
				String nowDateStr=DateUtils.formatDateTime(new Date());
				doc.set("SY_CREATETIME", nowDateStr);
				doc.setStr("SY_MODIFYUSERID", doc.getStr("SY_CREATEUSERID"));
				doc.setStr("SY_MODIFYUSER", doc.getStr("SY_CREATEUSER"));
				doc.setStr("SY_MODIFYUSERNAME",doc.getStr("SY_CREATEUSERNAME"));
				doc.setStr("SY_MODIFYORGID", doc.getStr("SY_CREATEORGID"));
				doc.setStr("SY_MODIFYORG", doc.getStr("SY_CREATEORG"));
				doc.setStr("SY_MODIFYORGNAME", doc.getStr("SY_CREATEORGNAME"));
				doc.setStr("SY_MODIFYTIME", nowDateStr);
			}else{
				buildModelCreateInfo(doc);
				buildModelModifyInfo(doc);
			}
			documentManager.insertDoc(doc);
		}
		if(isQuery){
			return selectOneByPk(entityPo.getStr(BeanUtils.KEY_TABLE_CODE), domainId);
		}else{
			return entityPo;
		}
	}

	/**
	 * 获取当前sessionFactory
	 * YUNFENGCHENG
	 * 2012-1-4 上午11:10:18
	 * @return
	 */
	@Override
	public SessionFactory getSessionFactory() {
		// TODO Auto-generated method stub
		return dynaDaoTemplate.getSessionFactory();
	}

	/**
	 * 保存功能更新的内容，内容格式为自定义的字符串
	 * @param funcCode
	 * @param pkValue
	 * @param updateContext
	 */
	@Override
	public void savePageDiyNicked(String funcCode, String pkValue, String updateContext) {
		DynaBean pageNicked=new DynaBean("JE_CORE_PAGENICKED",true);
		if(StringUtil.isNotEmpty(updateContext)){
			pageNicked.set("PAGENICKED_FUNCCODE",funcCode);
			pageNicked.set("PAGENICKED_BUSINESSID",pkValue);
			JSONObject updateObj=new JSONObject();
			updateObj.put("type","show");
			updateObj.put("value",updateContext);
			pageNicked.set("PAGENICKED_NICKED",updateObj.toString());
			buildModelCreateInfo(pageNicked);
			insert(pageNicked);
		}
	}

	/***
	 * 保存功能修改记录
	 * @param funcCode
	 * @param pkValue
	 * @param updateContext   内容格式: [text:"修改字段中文名",oldVal:"原值",newVal:"新值"]
	 */
	@Override
	public void savePageNicked(String funcCode, String pkValue, String updateContext) {
		DynaBean pageNicked=new DynaBean("JE_CORE_PAGENICKED",true);
		if(StringUtil.isNotEmpty(updateContext)){
			pageNicked.set("PAGENICKED_FUNCCODE",funcCode);
			pageNicked.set("PAGENICKED_BUSINESSID",pkValue);
			pageNicked.set("PAGENICKED_NICKED",updateContext);
			buildModelCreateInfo(pageNicked);
			insert(pageNicked);
		}
	}

	/**
	 * 保存功能修改记录
	 * @param funcCode 功能编码
	 * @param dynaBean 业务实体
	 * @param updateFields 修改字段
	 * @param updateFieldNames
	 */
	@Override
	public void savePageNicked(String funcCode, DynaBean dynaBean, String updateFields,String updateFieldNames) {
		String tableCode=dynaBean.getStr(BeanUtils.KEY_TABLE_CODE);
		String pkValue=dynaBean.getStr(dynaBean.getStr(BeanUtils.KEY_PK_CODE));
		DynaBean oldBean=selectOneByPk(tableCode,pkValue);
		String[] updateFieldArray=updateFields.split(",");
		String[] updateFieldNameArray=updateFieldNames.split(",");
		if(updateFieldArray.length>0) {
			for (int i=0;i<updateFieldArray.length;i++) {
				String fieldCode=updateFieldArray[i];
				if(StringUtil.isEmpty(fieldCode))continue;
				if (!oldBean.getStr(fieldCode, "").equals(dynaBean.getStr(fieldCode, ""))) {
					JSONObject obj=new JSONObject();
					obj.put("text",updateFieldNameArray[i]);
					obj.put("oldVal",oldBean.getStr(fieldCode, ""));
					obj.put("newVal",oldBean.getStr(fieldCode, ""));
					savePageNicked(funcCode,pkValue,obj.toString());
				}
			}
		}
	}

	/**
	 * 保存编辑标记信息
	 * @param funcCode 功能编码
	 * @param tableCode 业务实体
	 * @param pkValue  主键
	 * @param userId  标记的用户ID
	 * @param isNew   1位标记  0为不标记   2未读
	 */
	@Override
	public void doDataFuncEdit(String funcCode, String tableCode, String pkValue, String userId,String isNew) {
		DynaBean funcEdit=selectOne("JE_CORE_FUNCEDIT"," AND FUNCEDIT_FUNCCODE='"+funcCode+"' AND FUNCEDIT_PKVALUE='"+pkValue+"' AND FUNCEDIT_USERID='"+userId+"'");
		boolean insertFlag=false;
		if(funcEdit==null){
			funcEdit=new DynaBean("JE_CORE_FUNCEDIT",true);
			insertFlag=true;
		}
		funcEdit.set("FUNCEDIT_FUNCCODE",funcCode);
		funcEdit.set("FUNCEDIT_TABLECODE",tableCode);
		funcEdit.set("FUNCEDIT_PKVALUE",pkValue);
		funcEdit.set("FUNCEDIT_USERID",userId);
		funcEdit.set("FUNCEDIT_NEW",isNew);
		if(insertFlag){
			insert(funcEdit);
		}else{
			update(funcEdit);
		}
	}

	/**
	 * 根据SQL语句查询结果(一般查询多个字段返回List<Map>)
	 * @param sql
	 * @return
	 */
	@Override
	public List<Map> queryMapBySql(String sql) {
		return daoTemplate.queryMapBySql(sql);
	}

	@Override
	public List<Map> queryMapBySql(String sql, Object[] params) {
		return daoTemplate.queryMapBySql(sql,params);
	}

	@Override
	public List<Map> queryMapBySql(String sql, Map<String, Object> params) {
		return daoTemplate.queryMapBySql(sql,params);
	}

	/**
	 * 执行SQL
	 * @param sql
	 * @return
	 */
	@Override
	public Long executeSql(String sql) {
		return daoTemplate.executeSql(sql);
	}

	@Override
	public Long executeSql(String sql, Object[] params) {
		return daoTemplate.executeSql(sql,params);
	}

	@Override
	public Long executeSql(String sql, Map<String, Object> params) {
		return daoTemplate.executeSql(sql,params);
	}

	/**
	 *      * 加载dynaBean的动态表格树
	 * @param rootId 根节点ID
	 * @param tableCode 表名
	 * @ excludes 排除树形  按逗号隔开  一般排除的是checked 去掉得树节点前无多选框
	 * @param excludes
	 * @param checked 是否选中
	 * @param queryInfo 查询对象
	 * @return
	 */
	public List<HashMap> loadTree(String rootId,String tableCode,String excludes,Boolean checked,QueryInfo queryInfo){
		DynaBean table=BeanUtils.getInstance().getResourceTable(tableCode);
		JSONTreeNode template=BeanUtils.getInstance().buildJSONTreeNodeTemplate((List<DynaBean>)table.get(BeanUtils.KEY_TABLE_COLUMNS));
		List<HashMap> datas=dynaDaoTemplate.loadTree(rootId, template, table,excludes,checked,queryInfo);
		HashMap root=buildJSONTree4Dyna(datas, table.getStr(BeanUtils.KEY_PK_CODE),template.getParent());
		List<HashMap> childrens=(List<HashMap>) root.get("children");
		return childrens;
	}
	/**
	 * 查询动态Bean的一条记录
	 * @param tableCode 表名
	 * @param whereSql 查询条件
	 * @return
	 */
	public DynaBean selectOne(String tableCode,String whereSql){
		DynaBean bean=new DynaBean(tableCode,true);
		bean.set(BeanUtils.KEY_WHERE, whereSql);
		DynaBean dynaBean=dynaDaoTemplate.selectOne(bean);
		return dynaBean;
	}

	@Override
	public DynaBean selectOne(String tableCode, String whereSql, Object[] params) {
		DynaBean bean=new DynaBean(tableCode,true);
		bean.set(BeanUtils.KEY_WHERE, whereSql);
		DynaBean dynaBean=dynaDaoTemplate.selectOne(bean,params);
		return dynaBean;
	}

	@Override
	public DynaBean selectOne(String tableCode, String whereSql, Map<String, Object> params) {
		DynaBean bean=new DynaBean(tableCode,true);
		bean.set(BeanUtils.KEY_WHERE, whereSql);
		DynaBean dynaBean=dynaDaoTemplate.selectOne(bean,params);
		return dynaBean;
	}

	/**
	 * 查询动态Bean的一条记录
	 * @param tableCode 表名
	 * @param whereSql 查询条件
	 * @param queryFields 	查询字段，多个按逗号隔开
	 * @return
	 */
	public DynaBean selectOne(String tableCode,String whereSql,String queryFields){
		DynaBean bean=new DynaBean(tableCode,true);
		bean.set(BeanUtils.KEY_WHERE, whereSql);
		bean.set(BeanUtils.KEY_QUERY_FIELDS, queryFields);
		DynaBean dynaBean=dynaDaoTemplate.selectOne(bean);
		return dynaBean;
	}

	@Override
	public DynaBean selectOne(String tableCode, String whereSql, Object[] params, String queryFields) {
		DynaBean bean=new DynaBean(tableCode,true);
		bean.set(BeanUtils.KEY_WHERE, whereSql);
		bean.set(BeanUtils.KEY_QUERY_FIELDS, queryFields);
		DynaBean dynaBean=dynaDaoTemplate.selectOne(bean,params);
		return dynaBean;
	}

	@Override
	public DynaBean selectOne(String tableCode, String whereSql, Map<String, Object> params, String queryFields) {
		DynaBean bean=new DynaBean(tableCode,true);
		bean.set(BeanUtils.KEY_WHERE, whereSql);
		bean.set(BeanUtils.KEY_QUERY_FIELDS, queryFields);
		DynaBean dynaBean=dynaDaoTemplate.selectOne(bean,params);
		return dynaBean;
	}

	/**
	 * 查询动态Bean的集合
	 * @param tableCode 表名
	 * @param whereSql 查询条件
	 * @return
	 */
	public List<DynaBean> selectList(String tableCode,String whereSql){
		DynaBean bean=new DynaBean(tableCode,true);
		bean.set(BeanUtils.KEY_WHERE, whereSql);
		List<DynaBean> lists=dynaDaoTemplate.selectList(bean);
		return lists;
	}

	@Override
	public List<DynaBean> selectList(String tableCode, String whereSql, Object[] params) {
		DynaBean bean=new DynaBean(tableCode,true);
		bean.set(BeanUtils.KEY_WHERE, whereSql);
		List<DynaBean> lists=dynaDaoTemplate.selectList(bean,params);
		return lists;
	}

	@Override
	public List<DynaBean> selectList(String tableCode, String whereSql, Map<String, Object> params) {
		DynaBean bean=new DynaBean(tableCode,true);
		bean.set(BeanUtils.KEY_WHERE, whereSql);
		List<DynaBean> lists=dynaDaoTemplate.selectList(bean,params);
		return lists;
	}

	/**
	 * 查询动态Bean的集合
	 * @param tableCode 表名
	 * @param whereSql 查询条件
	 * @return
	 */
	public List<DynaBean> selectList(String tableCode,String whereSql,int start,int limit){
		DynaBean bean=new DynaBean(tableCode,true);
		bean.set(BeanUtils.KEY_WHERE, whereSql);
		List<DynaBean> lists=dynaDaoTemplate.selectList(bean,start,limit,true);
		return lists;
	}

	@Override
	public List<DynaBean> selectList(String tableCode, String whereSql, Object[] params, int start, int limit) {
		DynaBean bean=new DynaBean(tableCode,true);
		bean.set(BeanUtils.KEY_WHERE, whereSql);
		List<DynaBean> lists=dynaDaoTemplate.selectList(bean,params,start,limit,true);
		return lists;
	}

	@Override
	public List<DynaBean> selectList(String tableCode, String whereSql, Map<String, Object> params, int start, int limit) {
		DynaBean bean=new DynaBean(tableCode,true);
		bean.set(BeanUtils.KEY_WHERE, whereSql);
		List<DynaBean> lists=dynaDaoTemplate.selectList(bean,params,start,limit,true);
		return lists;
	}

	/**
	 * 查询动态Bean的集合
	 * @param tableCode 表名
	 * @param whereSql 查询条件
	 * @param queryFields 	查询字段，多个按逗号隔开
	 * @return
	 */
	public List<DynaBean> selectList(String tableCode,String whereSql,String queryFields){
		DynaBean bean=new DynaBean(tableCode,true);
		bean.set(BeanUtils.KEY_WHERE, whereSql);
		bean.set(BeanUtils.KEY_QUERY_FIELDS, queryFields);
		List<DynaBean> lists=dynaDaoTemplate.selectList(bean);
		return lists;
	}

	@Override
	public List<DynaBean> selectList(String tableCode, String whereSql, Object[] params, String queryFields) {
		DynaBean bean=new DynaBean(tableCode,true);
		bean.set(BeanUtils.KEY_WHERE, whereSql);
		bean.set(BeanUtils.KEY_QUERY_FIELDS, queryFields);
		List<DynaBean> lists=dynaDaoTemplate.selectList(bean,params);
		return lists;
	}

	@Override
	public List<DynaBean> selectList(String tableCode, String whereSql, Map<String, Object> params, String queryFields) {
		DynaBean bean=new DynaBean(tableCode,true);
		bean.set(BeanUtils.KEY_WHERE, whereSql);
		bean.set(BeanUtils.KEY_QUERY_FIELDS, queryFields);
		List<DynaBean> lists=dynaDaoTemplate.selectList(bean,params);
		return lists;
	}

	/**
	 * 查询动态Bean的集合
	 * @param tableCode 表名
	 * @param whereSql 查询条件
	 * @param queryFields 	查询字段，多个按逗号隔开
	 * @return
	 */
	public List<DynaBean> selectList(String tableCode,String whereSql,String queryFields,int start,int limit){
		DynaBean bean=new DynaBean(tableCode,true);
		bean.set(BeanUtils.KEY_WHERE, whereSql);
		bean.set(BeanUtils.KEY_QUERY_FIELDS, queryFields);
		List<DynaBean> lists=dynaDaoTemplate.selectList(bean,start,limit,true);
		return lists;
	}

	@Override
	public List<DynaBean> selectList(String tableCode, String whereSql, Object[] params, String queryFields, int start, int limit) {
		DynaBean bean=new DynaBean(tableCode,true);
		bean.set(BeanUtils.KEY_WHERE, whereSql);
		bean.set(BeanUtils.KEY_QUERY_FIELDS, queryFields);
		List<DynaBean> lists=dynaDaoTemplate.selectList(bean,params,start,limit,true);
		return lists;
	}

	@Override
	public List<DynaBean> selectList(String tableCode, String whereSql, Map<String, Object> params, String queryFields, int start, int limit) {
		DynaBean bean=new DynaBean(tableCode,true);
		bean.set(BeanUtils.KEY_WHERE, whereSql);
		bean.set(BeanUtils.KEY_QUERY_FIELDS, queryFields);
		List<DynaBean> lists=dynaDaoTemplate.selectList(bean,params,start,limit,true);
		return lists;
	}

	/**
	 * 构建创建信息
	 */
	@Override
	public void buildModelCreateInfo(DynaBean model) {
		// TODO Auto-generated method stub
		//设置登记信息
		EndUser currentUser = SecurityUserHolder.getCurrentUser();
		Department currentDept = SecurityUserHolder.getCurrentUserDept();
		if(null != currentUser && null != currentDept) {
			Date now = new Date();
			String nowDateTime = DateUtils.formatDateTime(now);
			//如果前台传入登录人信息则不构建，以后去掉
			if(StringUtil.isEmpty(model.getStr("SY_CREATEUSER"))){
				model.setStr("SY_CREATEUSER", currentUser.getUserCode());
				model.setStr("SY_CREATEUSERID", currentUser.getId());
				model.setStr("SY_CREATEUSERNAME", currentUser.getUsername());
			}
			if(StringUtil.isEmpty(model.getStr("SY_CREATEORG"))){
				model.setStr("SY_CREATEORGID", currentDept.getDeptId());
				model.setStr("SY_CREATEORG", currentDept.getDeptCode());
				model.setStr("SY_CREATEORGNAME", currentDept.getDeptName());
			}
			if(StringUtil.isEmpty(model.getStr("SY_JTGSID"))){
				model.setStr("SY_JTGSMC", currentUser.getJtgsMc());
				model.setStr("SY_JTGSID", currentUser.getJtgsId());
			}
			model.setStr("SY_ZHMC", currentUser.getZhMc());
			model.setStr("SY_ZHID", currentUser.getZhId());
			model.setStr("SY_CREATETIME", nowDateTime);
		}
		model.setStr("SY_AUDFLAG", AudFlagStatus.NOSTATUS);
		model.setStr("SY_PDID", "");
		model.setStr("SY_PIID", "");
//		//设置默认的工作流审核标记
//		if(StringUtil.isEmpty(model.getStr("SY_AUDFLAG"))) {
//		}
	}
	/**
	 * 构建创建信息
	 */
	@Override
	public void buildModelCreateInfo(DynaBean model,EndUser currentUser) {
		// TODO Auto-generated method stub
		if(currentUser==null){
			currentUser=SecurityUserHolder.getCurrentUser();
		}
		//设置登记信息
		Department currentDept =currentUser.getDept();
		if(null != currentUser && null != currentDept) {
			Date now = new Date();
			String nowDateTime = DateUtils.formatDateTime(now);
			//如果前台传入登录人信息则不构建，以后去掉
			if(StringUtil.isEmpty(model.getStr("SY_CREATEUSER"))){
				model.setStr("SY_CREATEUSER", currentUser.getUserCode());
				model.setStr("SY_CREATEUSERID", currentUser.getId());
				model.setStr("SY_CREATEUSERNAME", currentUser.getUsername());
			}
			if(StringUtil.isEmpty(model.getStr("SY_CREATEORG"))){
				model.setStr("SY_CREATEORGID", currentDept.getDeptId());
				model.setStr("SY_CREATEORG", currentDept.getDeptCode());
				model.setStr("SY_CREATEORGNAME", currentDept.getDeptName());
			}
			if(StringUtil.isEmpty(model.getStr("SY_JTGSID"))){
				model.setStr("SY_JTGSMC", currentUser.getJtgsMc());
				model.setStr("SY_JTGSID", currentUser.getJtgsId());
			}
			model.setStr("SY_ZHMC", currentUser.getZhMc());
			model.setStr("SY_ZHID", currentUser.getZhId());
			model.setStr("SY_CREATETIME", nowDateTime);
		}
		model.setStr("SY_AUDFLAG", AudFlagStatus.NOSTATUS);
		model.setStr("SY_PDID", "");
		model.setStr("SY_PIID", "");
//		//设置默认的工作流审核标记
//		if(StringUtil.isEmpty(model.getStr("SY_AUDFLAG"))) {
//		}
	}
	/**
	 * 仅当特殊情下(更改人信息但是流程继续走)才用次方法
	 * 构建创建信息,但是不处理工作流信息
	 * @param model
	 */
	@Override
	public void buildModelCreateInfoNoWf(DynaBean model) {
		// TODO Auto-generated method stub
		//设置登记信息
		EndUser currentUser = SecurityUserHolder.getCurrentUser();
		Department currentDept = SecurityUserHolder.getCurrentUserDept();
		if(null != currentUser && null != currentDept) {
			Date now = new Date();
			String nowDateTime = DateUtils.formatDateTime(now);
			model.setStr("SY_CREATEUSER", currentUser.getUserCode());
			model.setStr("SY_CREATEUSERID", currentUser.getId());
			model.setStr("SY_CREATEUSERNAME", currentUser.getUsername());
			model.setStr("SY_CREATEORGID", currentDept.getDeptId());
			model.setStr("SY_CREATEORG", currentDept.getDeptCode());
			model.setStr("SY_CREATEORGNAME", currentDept.getDeptName());
			model.setStr("SY_CREATETIME", nowDateTime);
		}
		if(StringUtil.isEmpty(model.getStr("SY_JTGSID"))){
			model.setStr("SY_JTGSMC", currentUser.getJtgsMc());
			model.setStr("SY_JTGSID", currentUser.getJtgsId());
		}
//		//设置默认的工作流审核标记
//		if(StringUtil.isEmpty(model.getStr("SY_AUDFLAG"))) {
//		}
	}
	/**
	 * 构建修改信息
	 */
	@Override
	public void buildModelModifyInfo(DynaBean model) {
		// TODO Auto-generated method stub
		EndUser currentUser = SecurityUserHolder.getCurrentUser();
		buildModelModifyInfo(model, currentUser);
	}
	/**
	 * 构建修改信息
	 */
	@Override
	public void buildModelModifyInfo(DynaBean model,EndUser currentUser) {
		// TODO Auto-generated method stub
		Department currentDept = currentUser.getDept();
		if(null != currentUser && null != currentDept) {
			Date now = new Date();
			String nowDateTime = DateUtils.formatDateTime(now);
			model.setStr("SY_MODIFYUSERID", currentUser.getUserId());
			model.setStr("SY_MODIFYUSER", currentUser.getUserCode());
			model.setStr("SY_MODIFYUSERNAME", currentUser.getUsername());
			model.setStr("SY_MODIFYORGID", currentDept.getDeptId());
			model.setStr("SY_MODIFYORG", currentDept.getDeptCode());
			model.setStr("SY_MODIFYORGNAME", currentDept.getDeptName());
			model.setStr("SY_MODIFYTIME", nowDateTime);
		}
	}

	/**
	 * 构建DynaBean指定功能上的默认值
	 * @param funcCode
	 * @param dynaBean
	 */
	@Override
	public void buildFuncDefaultValues(String funcCode, DynaBean dynaBean) {
		// TODO Auto-generated method stub
		FuncInfo funcInfo=funInfoManager.getFuncInfo(funcCode);
		Map<String,String> fieldInfos=funcInfo.getFieldDefaultValues();
		for(String key:fieldInfos.keySet()){
			String defaultVal=fieldInfos.get(key);
			defaultVal=StringUtil.codeToValue(defaultVal);
			dynaBean.set(key, defaultVal);
		}
	}
	/**
	 * 构建编号
	 */
	public String buildCode(String fieldCode,String funcCode,DynaBean dynaBean){
		return buildCode(fieldCode, funcCode, dynaBean,"");
	}
	/**
	 * 构建编号
	 */
	public String buildCode(String fieldCode,String funcCode,DynaBean dynaBean,String zhId){
		DynaBean funcInfo=selectOne("JE_CORE_FUNCINFO", " AND FUNCINFO_FUNCCODE='"+funcCode+"' AND FUNCINFO_NODEINFOTYPE IN ('FUNC','FUNCFIELD')","FUNCINFO_FUNCNAME,FUNCINFO_FUNCCODE,JE_CORE_FUNCINFO_ID,FUNCINFO_TABLENAME");
		if(funcInfo==null)return "";
		DynaBean field=selectOne("JE_CORE_RESOURCEFIELD", " AND RESOURCEFIELD_FUNCINFO_ID='"+funcInfo.getStr("JE_CORE_FUNCINFO_ID")+"' and RESOURCEFIELD_CODE='"+fieldCode+"'");
		if(field==null)return "";
		String configInfoStr=field.getStr("RESOURCEFIELD_CONFIGINFO");
		if(StringUtil.isEmpty(configInfoStr))return "";
		JSONObject infos=new JSONObject();
		String tableCode=funcInfo.getStr("FUNCINFO_TABLENAME","");
		DynaBean table=BeanUtils.getInstance().getResourceTable(tableCode);
		infos.put("TABLECODE", tableCode);
		if(table!=null){
			infos.put("TABLENAME", table.getStr("RESOURCETABLE_TABLENAME"));
		}
		infos.put("FUNCID", funcInfo.getStr("JE_CORE_FUNCINFO_ID"));
		infos.put("FUNCCODE", funcCode);
		infos.put("FUNCNAME", funcInfo.getStr("FUNCINFO_FUNCNAME"));
		if(StringUtil.isNotEmpty(zhId)){
			infos.put("ZHID", zhId);
		}
		return codeGenerator(JSONArray.fromObject(configInfoStr), dynaBean, fieldCode, infos);
	}

	/**
	 * 构建编号
	 * @param fieldCode 字段编码
	 * @param funcCode 功能编码
	 * @param dynaBean 数据对象
	 * @param zhId
	 * @param disableRepeat
	 * @param tableCode
	 * @return
	 */
	@Override
	public String buildCode(String fieldCode, String funcCode,DynaBean dynaBean, String zhId, Boolean disableRepeat,String tableCode) {
		// TODO Auto-generated method stub
		if(disableRepeat){
//			DynaBean funcInfo=selectOne("JE_CORE_FUNCINFO", " AND FUNCINFO_FUNCCODE='"+funcCode+"' AND FUNCINFO_NODEINFOTYPE IN ('FUNC','FUNCFIELD')","FUNCINFO_FUNCNAME,FUNCINFO_FUNCCODE,JE_CORE_FUNCINFO_ID,FUNCINFO_TABLENAME");
//			String tableCode=funcInfo.getStr("FUNCINFO_TABLENAME");
			String code=buildCode(fieldCode, funcCode, dynaBean, zhId);
			long count=selectCount(tableCode," AND "+fieldCode+"='"+code+"'");
			if(count>0){
				return buildCode(fieldCode, funcCode, dynaBean, zhId, disableRepeat, tableCode);
			}else{
				return code;
			}
		}else{
			return buildCode(fieldCode, funcCode, dynaBean, zhId);
		}
	}

	/**
	 * 构建编号
	 * @param codePatterns
	 * @param entity
	 * @param fieldName
	 * @param infos
	 * @return
	 */
	@Override
	public String codeGenerator(JSONArray codePatterns, DynaBean entity, String fieldName,JSONObject infos) {
		StringBuffer codeValue = new StringBuffer();
		String zhId="";
		if(infos.containsKey("ZHID")){
			zhId=infos.getString("ZHID");
		}
		for(int i=0; i<codePatterns.size(); i++) {
			JSONObject codePattern = codePatterns.getJSONObject(i);
			String qzfs=codePattern.getString("qzfs");//取值方式
			String dyz=codePattern.getString("dyz"); //对应值
			String jq=codePattern.getString("jq");//截取格式
			String gs=codePattern.getString("gs");//日期格式
			//常量
			if("CL".equals(qzfs)){
				if(StringUtil.isNotEmpty(dyz)){
					codeValue.append(dyz);
				}
				//全局变量
			}else if("QJBL".equals(qzfs)){
				if(StringUtil.isNotEmpty(dyz) && StringUtil.isNotEmpty(WebUtils.getBackVar(dyz))){
					codeValue.append(StringUtil.getSubValue(WebUtils.getBackVar(dyz), jq));
				}else if(StringUtil.isNotEmpty(dyz) && StringUtil.isNotEmpty(StringUtil.codeToValue(dyz))){
					if(StringUtil.isNotEmpty(gs)){
						Date date=DateUtils.getDate(StringUtil.codeToValue(dyz), DateUtils.DAFAULT_DATETIME_FORMAT);
						if(date==null){
							date=DateUtils.getDate(StringUtil.codeToValue(dyz), DateUtils.DAFAULT_DATE_FORMAT);
						}
						if(date!=null){
							codeValue.append(StringUtil.getSubValue(DateUtils.formatDate(date, gs), jq));
						}
					}else{
						codeValue.append(StringUtil.getSubValue(StringUtil.codeToValue(dyz), jq));
					}
				}
			}else if("BDZD".equals(qzfs)){
				if(StringUtil.isNotEmpty(dyz) && StringUtil.isNotEmpty(entity.getStr(dyz, ""))){
					if(StringUtil.isNotEmpty(gs)){
						Date date=DateUtils.getDate(entity.getStr(dyz, ""), DateUtils.DAFAULT_DATETIME_FORMAT);
						if(date==null){
							date=DateUtils.getDate(entity.getStr(dyz, ""), DateUtils.DAFAULT_DATE_FORMAT);
						}
						if(date!=null){
							codeValue.append(StringUtil.getSubValue(DateUtils.formatDate(date, gs), jq));
						}
					}else{
						codeValue.append(StringUtil.getSubValue(entity.getStr(dyz, ""), jq));
					}
				}
			}else if("LSH".equals(qzfs)){
				String cd=codePattern.getString("cd");//长度
				String lshDyz=StringUtil.getDefaultValue(codePattern.getString("dyz")+"","FUNC");//流水号基准
				String codeBase=codePattern.getString("qsh");//起始号
				String step=codePattern.getString("bc");//步长
				String cycle=codePattern.getString("zq");//周期
				if(!"TABLE".equals(lshDyz)){
					lshDyz="FUNC";
				}
				infos.put("TYPE", lshDyz);
				if(StringUtil.isNotEmpty(cd) && StringUtil.isNotEmpty(codeBase) && StringUtil.isNotEmpty(step)){ // && StringUtil.isNotEmpty(cycle)
					if(StringUtil.isNotEmpty(zhId)){
						codeValue.append(codeGenManager.getSeq(infos, fieldName, codeBase, step, cycle,Integer.parseInt(cd),zhId));
					}else{
						codeValue.append(codeGenManager.getSeq(infos, fieldName, codeBase, step, cycle,Integer.parseInt(cd)));
					}
				}
			}
		}
		return codeValue.toString();
	}
	/**
	 * 更新父节点的NodeType
	 */
	@Override
	public void updateTreePanent4NodeType(String tableCode,String parentId) {
		// TODO Auto-generated method stub
		String pkName=BeanUtils.getInstance().getPKeyFieldNames(tableCode);
		Long count=daoTemplate.countBySql("select count(*) from "+tableCode+" where SY_PARENT='"+parentId+"'");
		DynaBean one=selectOneByPk(tableCode,parentId,pkName+",SY_NODETYPE");
		if(count>0){
			if(NodeType.LEAF.equals(one.getStr("SY_NODETYPE"))){
				one.set(BeanUtils.KEY_TABLE_CODE, tableCode);
				one.set("SY_NODETYPE", NodeType.GENERAL);
				update(one);
			}
		}else{
			if(NodeType.GENERAL.equals(one.getStr("SY_NODETYPE"))){
				one.set(BeanUtils.KEY_TABLE_CODE, tableCode);
				one.set("SY_NODETYPE", NodeType.LEAF);
				if("0".equals(one.getStr("SY_DISABLED","0"))){
					update(one);
				}
			}
		}
	}
	/**
	 * 节点保存级联更新父节点名称
	 */
	public void saveTreeParentInfo(String tableCode,String pkCode,String parentId){
		daoTemplate.executeSql(" UPDATE "+tableCode+" SET SY_NODETYPE='"+NodeType.GENERAL+"' where "+pkCode+" = '"+parentId+"' AND SY_NODETYPE!='"+NodeType.ROOT+"'");
	}

	/**
	 * 级联删除树形附件
	 * @param tableCode 表名
	 * @param ids 主键值  多个按逗号隔开
	 */
	@Override
	public void removeTreeDocument(String tableCode,String ids){
		documentManager.doRemoveTreeDocuments(tableCode, ids);
	}

	/**
	 * 级联删除附件
	 * @param tableCode 表名
	 * @param ids 主键值  多个按逗号隔开
	 */
	@Override
	public void removeDocument(String tableCode,String ids) {
		// TODO Auto-generated method stub
		documentManager.doRemoveDocuments(tableCode, ids);
	}
	/**
	 * 设定表的主键值
	 * @param dynaBean
	 * @return
	 */
	@Override
	public String buildPkValue(DynaBean dynaBean){
		String tableCode=dynaBean.getStr(BeanUtils.KEY_TABLE_CODE);
		String pkCode=dynaBean.getStr(BeanUtils.KEY_PK_CODE);
		if(StringUtil.isEmpty(pkCode) && StringUtil.isNotEmpty(tableCode)){
			pkCode=BeanUtils.getInstance().getPKeyFieldNames(tableCode);
		}
		if(StringUtil.isNotEmpty(pkCode)){
			String pkValue= dynaBean.getStr(pkCode);
			if(StringUtil.isEmpty(pkValue)){
				pkValue= JEUUID.uuid();
				dynaBean.set(pkCode,pkValue);
			}
			return pkValue;
		}else{
			return null;
		}
	}
//	/**
//	 * 业务常用发放
//	 * 研发部:云凤程
//	 * 把原始表中的数据全部导入到目标表,条件是目标表的字段要大于等于原始表字段
//	 * 当然你可以增加目标表的特殊字段处理,例如他独有的外键
//	 * @param source 原始表
//	 * @param goal 目标表
//	 * @param add 例外比如原有表总没有字段A但是目标表中有那你就要想上{A:*}
//	 * @param replace 需要在目标中替换的字符串比如目标ID为A_ID本表ID为B_ID就可以使用该参数
//	 * @param whereSql 条件
//	 * @param newId 拷贝时候ID生成粗略,传输""代表使用源中的id作为新的id,sqlserver2005 可以使用Newid()函数
//	 * @return 是否成功
//	 * @throws PCExcuteException
//	 */
//	@SuppressWarnings({ "rawtypes"})
//	@Override
//	public boolean dynaFill(DynaBean source, DynaBean goal,
//							Map<String,Object> add, Set<String> replace ,String whereSql,String newId) throws PCExcuteException {
//		try {
//			//资源表的CODE
//			String sourceTableCode = (String)source.get(BeanUtils.KEY_TABLE_CODE);
//			String goalIDName = goal.getStr(BeanUtils.KEY_PK_CODE);
//			StringBuffer sql = new StringBuffer();
//			sql.append("insert into "+goal.get(BeanUtils.KEY_TABLE_CODE)+"(");
//			/**
//			 * 这块由于顺序很难定所以就写成goal而非source
//			 */
//			sql.append(BeanUtils.getInstance().getNames2DynaBean4Sql(goal));
//			//变量Map
//			if(add == null){add = new HashMap<String, Object>();}
//			Set<Entry<String, Object>> key = add.entrySet();
//			sql.append(") ");
//			if(whereSql.isEmpty()){
//				whereSql = "";
//			}
//			sql.append("select ");
//			//下半截SQL
//			StringBuffer selectSql = new StringBuffer();
//			if("".equals(newId) || newId == null){
//				selectSql.append(BeanUtils.getInstance().getNames2DynaBean4Sql(goal));
//			}else{
//				selectSql.append(BeanUtils.getInstance().getNames2DynaBean4Sql(goal).replace(goalIDName, newId));
//			}
//			String ss = selectSql.toString();
//			String k,v;
//			for(String text : replace){
//				k = text.split(",")[0];
//				v = text.split(",")[1];
//				ss = ss.replaceFirst(k, v);
//			}
//			for (Iterator it = key.iterator(); it.hasNext();) {
//				Map.Entry entry = (Map.Entry)it.next();
//				if("String".equalsIgnoreCase(entry.getValue().getClass().getSimpleName())){
//					ss = ss.replaceFirst(entry.getKey()+"", "'"+entry.getValue()+"'");
//				}else{
//					ss = ss.replaceFirst(entry.getKey()+"", entry.getValue()+"");
//				}
//			}
//			sql.append(ss).append(" from "+sourceTableCode +" where 1=1 and "+whereSql);
//			daoTemplate.executeSql(sql.toString());
//			return true;
//		} catch (Exception e) {
//			logger.error("", e);
//			return false;
//		}
//	}
}
