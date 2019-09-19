package com.je.core.dao;

import com.je.core.constants.ConstantVars;
import com.je.core.constants.StatusType;
import com.je.core.constants.table.ColumnType;
import com.je.core.entity.QueryInfo;
import com.je.core.entity.extjs.JSONTreeNode;
import com.je.core.exception.PlatformException;
import com.je.core.exception.PlatformExceptionEnum;
import com.je.core.sql.BuildingQuery;
import com.je.core.util.ArrayUtils;
import com.je.core.util.DBSqlUtils;
import com.je.core.util.SqlUtil;
import com.je.core.util.StringUtil;
import com.je.core.util.bean.BeanUtils;
import com.je.core.util.bean.DynaBean;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.exception.DataException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.Clob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
/**
 * 针对于动态BEAN的数据库操作
 * @author 张帅鹏
 * 2012-5-02 下午11:58:16
 */
@Component("PCDynaDAOTemplateORCL")
public class PCDynaDaoTemplateImpl implements PCDynaDaoTemplate {
	private HibernateTemplate hibernateTemplate;
	private SessionFactory sessionFactory;
	private static BuildingQuery buildingQuery;
	private static BeanUtils beanUtils;
	protected static Logger logger = LoggerFactory.getLogger(PCDynaDaoTemplateImpl.class);
	static{
		buildingQuery = BuildingQuery.getInstance();
		beanUtils = BeanUtils.getInstance();
	}
	/**
	 * 得到Session
	 * @return
	 */
	private Session getSession(){
		return hibernateTemplate.getSessionFactory().getCurrentSession();
	}
	/**
     * 插入一条带指定数据的数据库记录，如果对应字段没有值，则系统将字符串类型字段置为'', 将数字类型字段置为0
     * 
     * @param dynaBean                存放要插入的数据库记录信息
     * @return 插入是否成功：                                                 1 是， 0 否
     */
	@Override
    public DynaBean insert(DynaBean dynaBean){
    	try {
    		String tableCode=dynaBean.getStr(BeanUtils.KEY_TABLE_CODE);
        	SQLQuery sqlQuery = buildingQuery.getInsertSQLQuery(getSession(),dynaBean);
        	sqlQuery.executeUpdate();
        	dynaBean.set(BeanUtils.KEY_TABLE_CODE, tableCode);
		}catch(HibernateException e){
			String errors="执行数据插入操作失败了...";
			//如果是SQLServer数据库
			if(ConstantVars.STR_SQLSERVER.equals(PCDaoTemplateImpl.DBNAME) && e.getCause()!=null && e.getCause().getMessage().indexOf("将截断字符串或二进制数据")!=-1){
				//找出字符过长的字段
				errors=SqlUtil.getStringLong(dynaBean);
			}else if(ConstantVars.STR_MYSQL.equals(PCDaoTemplateImpl.DBNAME) && e.getMessage().indexOf("Data too long for column")!=-1){
				//找出字符过长的字段
				errors=SqlUtil.getStringLong(dynaBean);
			}else if(ConstantVars.STR_ORACLE.equals(PCDaoTemplateImpl.DBNAME) && e.getCause()!=null && e.getMessage().indexOf("ORA-12899")!=-1){
				errors=e.getMessage().replaceAll("\"", "");
			}
			throw new PlatformException(errors,PlatformExceptionEnum.JE_CORE_DYNABEAN_FIELDLONG_ERROR,new Object[]{dynaBean},e);
		} catch (Exception e) {
			throw new PlatformException("执行数据插入的时候报错...",PlatformExceptionEnum.JE_CORE_DYNABEAN_INSERT_ERROR,new Object[]{dynaBean},e);
		}
    	return dynaBean;
    }
    /**
     * 基于主键删除一条数据库记录
     * 
     * @param dynaBean      数据信息
     * @return              本类
     */
	@Override
    public DynaBean delete(DynaBean dynaBean){
    	try {
    		//当动态实体设置的主键以后才能继续操作
    		if(dynaBean.get(BeanUtils.KEY_PK_CODE) != null){
	    		SQLQuery sqlQuery = buildingQuery.getDeleteByIdSQLQuery(getSession(),dynaBean);
	    		sqlQuery.executeUpdate();
    		}else{
				throw new PlatformException("删除失败:动态Bean没有主键的描述,请为KEY_PK_CODE赋值",PlatformExceptionEnum.JE_CORE_DYNABEAN_PARAM_ERROR);
    		}
		} catch (Exception e) {
			throw new PlatformException("执行数据删除的时候报错...",PlatformExceptionEnum.JE_CORE_DYNABEAN_DELETE_ERROR,new Object[]{dynaBean},e);
		}
    	return dynaBean;
    }
    /**
     * 基于主键假删除一条数据库记录
     * @param dynaBean      数据信息
     * @return              本类
     */
	@Override
    public DynaBean deleteFake(DynaBean dynaBean){
    	try {
			/**
			 * 假删除操作一定要有Bean中有status字段
			 * 假删除操作其实就是更新操作
			 */
    		//1.变更状态
    		dynaBean.set("SY_STATUS", StatusType.DISABLED);
    		//更新操作
    		update(dynaBean);
		} catch (Exception e) {
			throw new PlatformException("执行数据假删除的时候报错...",PlatformExceptionEnum.JE_CORE_DYNABEAN_DISABLED_ERROR,new Object[]{dynaBean},e);
		}
    	return new DynaBean();
    }
	/**
	 * 基于主键启用一条数据库记录
	 * @param dynaBean      数据信息
	 * @return              本类
	 */
	@Override
	public DynaBean enableFake(DynaBean dynaBean){
		try {
			/**
			 * 假删除操作一定要有Bean中有status字段
			 * 假删除操作其实就是更新操作
			 */
			//1.变更状态
			dynaBean.set("SY_STATUS", StatusType.ENABLED);
			//更新操作
			update(dynaBean);
		} catch (Exception e) {
			throw new PlatformException("执行数据启用的时候报错...",PlatformExceptionEnum.JE_CORE_DYNABEAN_ENABLED_ERROR,new Object[]{dynaBean},e);
		}
		return new DynaBean();
	}
    /**
     * 根据动态类中whereSql项设定的条件删除多条数据库记录
     *
     * @param dynaBean          数据信息
     * @return                  删除行数
     */
	@Override
    public int deleteByCondition(DynaBean dynaBean){
		int count = 0;
		try {
	   		//当动态实体设置WHERE以后才能继续操作
    		if(dynaBean.get(BeanUtils.KEY_WHERE) != null){
	    		SQLQuery sqlQuery = buildingQuery.getDeleteByWhereSQLQuery(getSession(),dynaBean,"sql",null,null);
	    		count = sqlQuery.executeUpdate();
    		}else{
				throw new PlatformException("删除失败:动态Bean没有WHERE的描述,请为KEY_WHERE赋值",PlatformExceptionEnum.JE_CORE_DYNABEAN_PARAM_ERROR);
    		}
		} catch (Exception e) {
			throw new PlatformException("执行数据删除的时候报错...",PlatformExceptionEnum.JE_CORE_DYNABEAN_DELETE_ERROR,new Object[]{dynaBean},e);
		}
    	return count;
    }
	/**
	 * 根据动态类中whereSql项设定的条件删除多条数据库记录
	 *
	 * @param dynaBean          数据信息
	 * @return                  删除行数
	 */
	@Override
	public int deleteByCondition(DynaBean dynaBean,Object[] params){
		int count = 0;
		try {
			//当动态实体设置WHERE以后才能继续操作
			if(dynaBean.get(BeanUtils.KEY_WHERE) != null){
				SQLQuery sqlQuery = buildingQuery.getDeleteByWhereSQLQuery(getSession(),dynaBean,"index",params,null);
				count = sqlQuery.executeUpdate();
			}else{
				throw new PlatformException("删除失败:动态Bean没有WHERE的描述,请为KEY_WHERE赋值",PlatformExceptionEnum.JE_CORE_DYNABEAN_PARAM_ERROR);
			}
		} catch (Exception e) {
			throw new PlatformException("执行数据删除的时候报错...",PlatformExceptionEnum.JE_CORE_DYNABEAN_DELETE_ERROR,new Object[]{dynaBean},e);
		}
		return count;
	}
	/**
	 * 根据动态类中whereSql项设定的条件删除多条数据库记录
	 *
	 * @param dynaBean          数据信息
	 * @return                  删除行数
	 */
	@Override
	public int deleteByCondition(DynaBean dynaBean,Map<String,Object> params){
		int count = 0;
		try {
			//当动态实体设置WHERE以后才能继续操作
			if(dynaBean.get(BeanUtils.KEY_WHERE) != null){
				SQLQuery sqlQuery = buildingQuery.getDeleteByWhereSQLQuery(getSession(),dynaBean,"name",null,params);
				count = sqlQuery.executeUpdate();
			}else{
				throw new PlatformException("删除失败:动态Bean没有WHERE的描述,请为KEY_WHERE赋值",PlatformExceptionEnum.JE_CORE_DYNABEAN_PARAM_ERROR);
			}
		} catch (Exception e) {
			throw new PlatformException("执行数据删除的时候报错...",PlatformExceptionEnum.JE_CORE_DYNABEAN_DELETE_ERROR,new Object[]{dynaBean},e);
		}
		return count;
	}
    /**
     * 根据用逗号分开的主键id字符串批量删除表中的数据
     * @param ids 主键字符串
     * @param tableName 表名字
     * @param idName 主键名称
     * @return 删除的行数
     */
	@Override
    public int deleteByIds(String ids ,String tableName ,String idName) {
		int count = 0;
		try {
    		SQLQuery sqlQuery = buildingQuery.getDeleteByIdsSQLQuery(getSession(),ids,tableName,idName);
    		count = sqlQuery.executeUpdate();			
		} catch (Exception e) {
			throw new PlatformException("执行数据删除的时候报错...",PlatformExceptionEnum.JE_CORE_DYNABEAN_DELETE_ERROR,new Object[]{tableName,idName,ids},e);
		}
    	return count;
    }
    /**
     * 根据主键查询一条指定的记录。
     * 
     * @param dynaBean      数据信息
     * @return              数据库记录相信信息，不存在数据时返回空
     */
	@SuppressWarnings("unchecked")
	@Override
    public DynaBean selectByPk(DynaBean dynaBean){
		try {
			String tableCode=dynaBean.getStr(BeanUtils.KEY_TABLE_CODE);
    		if(dynaBean.get(BeanUtils.KEY_PK_CODE) != null){
    			//拼装类,并且得到列模型
    			List<DynaBean> columns = loadDynaBeanFieldNames4Select(dynaBean);
	    		SQLQuery sqlQuery = buildingQuery.getSelectByIdSQLQuery(getSession(),dynaBean);
	    		List<Map> list = sqlQuery.list();
	    		if(list.size() != 1){
	    			//没处查询出或者查询出很多来数据就赋值为NULL
	    			dynaBean = null;
	    		}else{
	    			Map bean = list.get(0);
	    			loadDynaBeanFieldValues(dynaBean, bean, columns);
	    		}
    		}else{
				throw new PlatformException("查询失败:动态Bean没有主键的描述,请为KEY_PK_CODE赋值",PlatformExceptionEnum.JE_CORE_DYNABEAN_PARAM_ERROR);
    		}
    		if(dynaBean!=null) {
				dynaBean.set(BeanUtils.KEY_TABLE_CODE, tableCode);
			}
		} catch (Exception e) {
			throw new PlatformException("执行数据查询的时候报错...",PlatformExceptionEnum.JE_CORE_DYNABEAN_QUERY_ERROR,new Object[]{dynaBean},e);
		}
    	return dynaBean;
    }
//    /**
//     * 根据主键或者过滤条件查询一条指定的记录：
//     * 如果是则使用主键字段，则自动根据动态类中主键的信息进行过滤
//     * 如果不是使用主键字段，则使用$WHERE$中的信息进行过滤
//     *
//     * @param dynaBean      数据信息
//     * @param bKeySelect    是否基于主键查找一条指定的记录
//     * @return              数据库记录相信信息，不存在数据时返回空
//     */
//	@Override
//    public DynaBean select(DynaBean dynaBean, boolean bKeySelect){
//		if(bKeySelect){
//	    	return select(dynaBean);
//		}else{
//			return selectOne(dynaBean);
//		}
//    }
    /**
     * 根据$WEHRE$条件查询符合条件的纪录数量
     *
     * @param dynaBean      数据信息
     * @return              符合条件的纪录数
     */
	@Override
    public long selectCount(DynaBean dynaBean){
		long count = 0L;
		try {
    		if(dynaBean.get(BeanUtils.KEY_WHERE) != null){
    			SQLQuery sqlQuery = buildingQuery.getSelectCountByWhereSQLQuery(getSession(),dynaBean,"sql",null,null);
    			Object obj = (Object)(sqlQuery.list().get(0));
    			//赋值
    			if(obj!=null){
    				count = Long.parseLong(obj.toString());
    			}
    		}else{
				throw new PlatformException("查询失败:动态Bean没有WHERE的描述,请为KEY_WHERE赋值",PlatformExceptionEnum.JE_CORE_DYNABEAN_PARAM_ERROR);
    		}			
		} catch (Exception e) {
			throw new PlatformException("执行数据查询数量的时候报错...",PlatformExceptionEnum.JE_CORE_DYNABEAN_COUNT_ERROR,new Object[]{dynaBean},e);
		}
    	return count;
    }

	@Override
	public long selectCount(DynaBean dynaBean, Object[] params) {
		long count = 0L;
		try {
			if(dynaBean.get(BeanUtils.KEY_WHERE) != null){
				SQLQuery sqlQuery = buildingQuery.getSelectCountByWhereSQLQuery(getSession(),dynaBean,"index",params,null);
				Object obj = (Object)(sqlQuery.list().get(0));
				//赋值
				if(obj!=null){
					count = Long.parseLong(obj.toString());
				}
			}else{
				throw new PlatformException("查询失败:动态Bean没有WHERE的描述,请为KEY_WHERE赋值",PlatformExceptionEnum.JE_CORE_DYNABEAN_PARAM_ERROR);
			}
		} catch (Exception e) {
			throw new PlatformException("执行数据查询数量的时候报错...",PlatformExceptionEnum.JE_CORE_DYNABEAN_COUNT_ERROR,new Object[]{dynaBean},e);
		}
		return count;
	}

	@Override
	public long selectCount(DynaBean dynaBean, Map<String, Object> params) {
		long count = 0L;
		try {
			if(dynaBean.get(BeanUtils.KEY_WHERE) != null){
				SQLQuery sqlQuery = buildingQuery.getSelectCountByWhereSQLQuery(getSession(),dynaBean,"name",null,params);
				Object obj = (Object)(sqlQuery.list().get(0));
				//赋值
				if(obj!=null){
					count = Long.parseLong(obj.toString());
				}
			}else{
				throw new PlatformException("查询失败:动态Bean没有WHERE的描述,请为KEY_WHERE赋值",PlatformExceptionEnum.JE_CORE_DYNABEAN_PARAM_ERROR);
			}
		} catch (Exception e) {
			throw new PlatformException("执行数据查询数量的时候报错...",PlatformExceptionEnum.JE_CORE_DYNABEAN_COUNT_ERROR,new Object[]{dynaBean},e);
		}
		return count;
	}

	/**
     * 根据条件查询数据库中的一条记录，如果查询出多条记录，那么只取第一条记录
     * @param dynaBean  查询条件bean
     * @return  结果bean
     */
	@SuppressWarnings("unchecked")
	@Override
    public DynaBean selectOne(DynaBean dynaBean){
		try {
			String tableCode=dynaBean.getStr(BeanUtils.KEY_TABLE_CODE);
    		if(dynaBean.get(BeanUtils.KEY_WHERE) != null){
    			//拼装类,并且得到列模型
    			List<DynaBean> columns = loadDynaBeanFieldNames4Select(dynaBean);
	    		SQLQuery sqlQuery = buildingQuery.getSelectByWhereSQLQuery(getSession(),dynaBean,"sql",null,null);
	    		List<Map> list = sqlQuery.list();
	    		if(list.size() != 1){
	    			//没处查询出或者查询出很多来数据就赋值为NULL
	    			dynaBean = null;
	    		}else{
	    			Map bean = list.get(0);
	    			loadDynaBeanFieldValues(dynaBean, bean, columns);
	    		}
    		}else{
				throw new PlatformException("查询失败:动态Bean没有WHERE的描述,请为KEY_WHERE赋值",PlatformExceptionEnum.JE_CORE_DYNABEAN_PARAM_ERROR);
    		}
    		if(dynaBean!=null){
    			dynaBean.set(BeanUtils.KEY_TABLE_CODE, tableCode);
    		}
		} catch (Exception e) {
			throw new PlatformException("执行数据查询一条的时候报错...",PlatformExceptionEnum.JE_CORE_DYNABEAN_QUERYONE_ERROR,new Object[]{dynaBean},e);
		}
    	return dynaBean;		
    }

	@Override
	public DynaBean selectOne(DynaBean dynaBean, Object[] params) {
		try {
			String tableCode=dynaBean.getStr(BeanUtils.KEY_TABLE_CODE);
			if(dynaBean.get(BeanUtils.KEY_WHERE) != null){
				//拼装类,并且得到列模型
				List<DynaBean> columns = loadDynaBeanFieldNames4Select(dynaBean);
				SQLQuery sqlQuery = buildingQuery.getSelectByWhereSQLQuery(getSession(),dynaBean,"index",params,null);
				List<Map> list = sqlQuery.list();
				if(list.size() != 1){
					//没处查询出或者查询出很多来数据就赋值为NULL
					dynaBean = null;
				}else{
					Map bean = list.get(0);
					loadDynaBeanFieldValues(dynaBean, bean, columns);
				}
			}else{
				throw new PlatformException("查询失败:动态Bean没有WHERE的描述,请为KEY_WHERE赋值",PlatformExceptionEnum.JE_CORE_DYNABEAN_PARAM_ERROR);
			}
			if(dynaBean!=null){
				dynaBean.set(BeanUtils.KEY_TABLE_CODE, tableCode);
			}
		} catch (Exception e) {
			throw new PlatformException("执行数据查询一条的时候报错...",PlatformExceptionEnum.JE_CORE_DYNABEAN_QUERYONE_ERROR,new Object[]{dynaBean},e);
		}
		return dynaBean;
	}

	@Override
	public DynaBean selectOne(DynaBean dynaBean, Map<String, Object> params) {
		try {
			String tableCode=dynaBean.getStr(BeanUtils.KEY_TABLE_CODE);
			if(dynaBean.get(BeanUtils.KEY_WHERE) != null){
				//拼装类,并且得到列模型
				List<DynaBean> columns = loadDynaBeanFieldNames4Select(dynaBean);
				SQLQuery sqlQuery = buildingQuery.getSelectByWhereSQLQuery(getSession(),dynaBean,"name",null,params);
				List<Map> list = sqlQuery.list();
				if(list.size() != 1){
					//没处查询出或者查询出很多来数据就赋值为NULL
					dynaBean = null;
				}else{
					Map bean = list.get(0);
					loadDynaBeanFieldValues(dynaBean, bean, columns);
				}
			}else{
				throw new PlatformException("查询失败:动态Bean没有WHERE的描述,请为KEY_WHERE赋值",PlatformExceptionEnum.JE_CORE_DYNABEAN_PARAM_ERROR);
			}
			if(dynaBean!=null){
				dynaBean.set(BeanUtils.KEY_TABLE_CODE, tableCode);
			}
		} catch (Exception e) {
			throw new PlatformException("执行数据查询一条的时候报错...",PlatformExceptionEnum.JE_CORE_DYNABEAN_QUERYONE_ERROR,new Object[]{dynaBean},e);
		}
		return dynaBean;
	}

	/**
     * 根据条件查询数据库记录列表，不分页，取全部信息
     *
     * @param dynaBean          数据信息
     * @return                  数据库记录列表信息
     */
	@SuppressWarnings("unchecked")
	@Override
    public ArrayList<DynaBean> selectList(DynaBean dynaBean){
		ArrayList<DynaBean> dynaBeans = new ArrayList<DynaBean>();
		DynaBean bean;
		try {
			String tableCode=dynaBean.getStr(BeanUtils.KEY_TABLE_CODE);
			//拼装类,并且得到列模型
			List<DynaBean> columns = loadDynaBeanFieldNames4Select(dynaBean);
    		SQLQuery sqlQuery = buildingQuery.getSelectByWhereSQLQuery(getSession(),dynaBean,"sql",null,null);
    		List<Map> list = sqlQuery.list();
    		if(list.size() == 0){
    			//没处查询出或者查询出很多来数据就赋值为NULL
    			dynaBean = null;
    		}else{
    			for(Map objs : list){
    				bean = new DynaBean();
    				bean.set(BeanUtils.KEY_QUERY_FIELDS, dynaBean.get(BeanUtils.KEY_QUERY_FIELDS));
    				loadDynaBeanFieldValues(bean, objs, columns);
    				bean.set(BeanUtils.KEY_TABLE_CODE, tableCode);
    				dynaBeans.add(bean);
    			}
    		}
		} catch (Exception e) {
			throw new PlatformException("执行查询所有集合数据的时候报错...",PlatformExceptionEnum.JE_CORE_DYNABEAN_QUERY_ERROR,new Object[]{dynaBean},e);
		}
    	return dynaBeans;
    }

	@Override
	public ArrayList<DynaBean> selectList(DynaBean dynaBean, Object[] params) {
		ArrayList<DynaBean> dynaBeans = new ArrayList<DynaBean>();
		DynaBean bean;
		try {
			String tableCode=dynaBean.getStr(BeanUtils.KEY_TABLE_CODE);
			//拼装类,并且得到列模型
			List<DynaBean> columns = loadDynaBeanFieldNames4Select(dynaBean);
			SQLQuery sqlQuery = buildingQuery.getSelectByWhereSQLQuery(getSession(),dynaBean,"index",params,null);
			List<Map> list = sqlQuery.list();
			if(list.size() == 0){
				//没处查询出或者查询出很多来数据就赋值为NULL
				dynaBean = null;
			}else{
				for(Map objs : list){
					bean = new DynaBean();
					bean.set(BeanUtils.KEY_QUERY_FIELDS, dynaBean.get(BeanUtils.KEY_QUERY_FIELDS));
					loadDynaBeanFieldValues(bean, objs, columns);
					bean.set(BeanUtils.KEY_TABLE_CODE, tableCode);
					dynaBeans.add(bean);
				}
			}
		} catch (Exception e) {
			throw new PlatformException("执行查询所有集合数据的时候报错...",PlatformExceptionEnum.JE_CORE_DYNABEAN_QUERY_ERROR,new Object[]{dynaBean},e);
		}
		return dynaBeans;
	}

	@Override
	public ArrayList<DynaBean> selectList(DynaBean dynaBean, Map<String, Object> params) {
		ArrayList<DynaBean> dynaBeans = new ArrayList<DynaBean>();
		DynaBean bean;
		try {
			String tableCode=dynaBean.getStr(BeanUtils.KEY_TABLE_CODE);
			//拼装类,并且得到列模型
			List<DynaBean> columns = loadDynaBeanFieldNames4Select(dynaBean);
			SQLQuery sqlQuery = buildingQuery.getSelectByWhereSQLQuery(getSession(),dynaBean,"name",null,params);
			List<Map> list = sqlQuery.list();
			if(list.size() == 0){
				//没处查询出或者查询出很多来数据就赋值为NULL
				dynaBean = null;
			}else{
				for(Map objs : list){
					bean = new DynaBean();
					bean.set(BeanUtils.KEY_QUERY_FIELDS, dynaBean.get(BeanUtils.KEY_QUERY_FIELDS));
					loadDynaBeanFieldValues(bean, objs, columns);
					bean.set(BeanUtils.KEY_TABLE_CODE, tableCode);
					dynaBeans.add(bean);
				}
			}
		} catch (Exception e) {
			throw new PlatformException("执行查询所有集合数据的时候报错...",PlatformExceptionEnum.JE_CORE_DYNABEAN_QUERY_ERROR,new Object[]{dynaBean},e);
		}
		return dynaBeans;
	}
//    /**
//     * 根据条件查询数据库记录列表
//     *
//     * @param dynaBean      查询信息
//     * @param page          当前所在页数，第一页应该为1
//     * @param limit         返回纪录数
//     * @param start         开始的纪录条数
//     * @return              数据库记录以及结果信息
//     */
//	@SuppressWarnings("unchecked")
//	@Override
//    public ArrayList<DynaBean> selectList(DynaBean dynaBean,int start , int limit ,int page) {
//    	return selectList(dynaBean, start, limit, page, false);
//    }
	/**
     * 根据条件查询数据库记录列表
     *
     * @param dynaBean      查询信息
     * @param limit         返回纪录数
     * @param start         开始的纪录条数
     * @return              数据库记录以及结果信息
     */
	@SuppressWarnings("unchecked")
    public ArrayList<DynaBean> selectList(DynaBean dynaBean,int start , int limit ,boolean noCount) {
		ArrayList<DynaBean> dynaBeans = new ArrayList<DynaBean>();
		DynaBean bean;
		try {
			String tableCode=dynaBean.getStr(BeanUtils.KEY_TABLE_CODE);
			//拼装类,并且得到列模型
			List<DynaBean> columns = loadDynaBeanFieldNames4Select(dynaBean);
    		SQLQuery sqlQuery = buildingQuery.getSelectByWhereSQLQuery(getSession(),dynaBean,"sql",null,null);
    		if(!noCount){
	    		SQLQuery countQuery = buildingQuery.getSelectCountByWhereSQLQuery(getSession(), dynaBean,"sql",null,null);
	    		Object count = countQuery.uniqueResult();
	    		dynaBean.setLong(BeanUtils.KEY_ALL_COUNT, Long.parseLong(count.toString()));
    		}
    		//设置分页
    		sqlQuery.setFirstResult(start);
    		sqlQuery.setMaxResults(limit);
    		List<Map> list = sqlQuery.list();
			for(Map objs : list){
				bean = new DynaBean();
				bean.set(BeanUtils.KEY_QUERY_FIELDS, dynaBean.get(BeanUtils.KEY_QUERY_FIELDS));
				loadDynaBeanFieldValues(bean, objs, columns);
				bean.set(BeanUtils.KEY_TABLE_CODE, tableCode);
				dynaBeans.add(bean);
			}
		} catch (Exception e) {
			throw new PlatformException("执行数据分页查询的时候报错...",PlatformExceptionEnum.JE_CORE_DYNABEAN_QUERY_ERROR,new Object[]{dynaBean,start,limit,noCount},e);
		}
    	return dynaBeans;
    }

	@Override
	public ArrayList<DynaBean> selectList(DynaBean dynaBean, Object[] params, int start, int limit, boolean noCount) {
		ArrayList<DynaBean> dynaBeans = new ArrayList<DynaBean>();
		DynaBean bean;
		try {
			String tableCode=dynaBean.getStr(BeanUtils.KEY_TABLE_CODE);
			//拼装类,并且得到列模型
			List<DynaBean> columns = loadDynaBeanFieldNames4Select(dynaBean);
			SQLQuery sqlQuery = buildingQuery.getSelectByWhereSQLQuery(getSession(),dynaBean,"index",params,null);
			if(!noCount){
				SQLQuery countQuery = buildingQuery.getSelectCountByWhereSQLQuery(getSession(), dynaBean,"index",params,null);
				Object count = countQuery.uniqueResult();
				dynaBean.setLong(BeanUtils.KEY_ALL_COUNT, Long.parseLong(count.toString()));
			}
			//设置分页
			sqlQuery.setFirstResult(start);
			sqlQuery.setMaxResults(limit);
			List<Map> list = sqlQuery.list();
			for(Map objs : list){
				bean = new DynaBean();
				bean.set(BeanUtils.KEY_QUERY_FIELDS, dynaBean.get(BeanUtils.KEY_QUERY_FIELDS));
				loadDynaBeanFieldValues(bean, objs, columns);
				bean.set(BeanUtils.KEY_TABLE_CODE, tableCode);
				dynaBeans.add(bean);
			}
		} catch (Exception e) {
			throw new PlatformException("执行数据分页查询的时候报错...",PlatformExceptionEnum.JE_CORE_DYNABEAN_QUERY_ERROR,new Object[]{dynaBean,start,limit,noCount},e);
		}
		return dynaBeans;
	}

	@Override
	public ArrayList<DynaBean> selectList(DynaBean dynaBean, Map<String, Object> params, int start, int limit, boolean noCount) {
		ArrayList<DynaBean> dynaBeans = new ArrayList<DynaBean>();
		DynaBean bean;
		try {
			String tableCode=dynaBean.getStr(BeanUtils.KEY_TABLE_CODE);
			//拼装类,并且得到列模型
			List<DynaBean> columns = loadDynaBeanFieldNames4Select(dynaBean);
			SQLQuery sqlQuery = buildingQuery.getSelectByWhereSQLQuery(getSession(),dynaBean,"name",null,params);
			if(!noCount){
				SQLQuery countQuery = buildingQuery.getSelectCountByWhereSQLQuery(getSession(), dynaBean,"name",null,params);
				Object count = countQuery.uniqueResult();
				dynaBean.setLong(BeanUtils.KEY_ALL_COUNT, Long.parseLong(count.toString()));
			}
			//设置分页
			sqlQuery.setFirstResult(start);
			sqlQuery.setMaxResults(limit);
			List<Map> list = sqlQuery.list();
			for(Map objs : list){
				bean = new DynaBean();
				bean.set(BeanUtils.KEY_QUERY_FIELDS, dynaBean.get(BeanUtils.KEY_QUERY_FIELDS));
				loadDynaBeanFieldValues(bean, objs, columns);
				bean.set(BeanUtils.KEY_TABLE_CODE, tableCode);
				dynaBeans.add(bean);
			}
		} catch (Exception e) {
			throw new PlatformException("执行数据分页查询的时候报错...",PlatformExceptionEnum.JE_CORE_DYNABEAN_QUERY_ERROR,new Object[]{dynaBean,start,limit,noCount},e);
		}
		return dynaBeans;
	}

	/**
     * 基于主键修改数据库记录信息
     * @param dynaBean                要更新的数据库记录信息
     * @return                        更新好的数据BEAN
     */
	@SuppressWarnings("rawtypes")
	@Override
    public DynaBean update(DynaBean dynaBean){
		//1.根据ID先去查询出这条数据
		String tableCode=dynaBean.getStr(BeanUtils.KEY_TABLE_CODE);
		DynaBean goalBean = new DynaBean(); // select(dynaBean);
		goalBean.set(BeanUtils.KEY_TABLE_CODE, tableCode);
		try {
    		if(dynaBean.get(BeanUtils.KEY_PK_CODE) != null){
    			//2.生成更新的SQLQuery
    			SQLQuery sqlQuery = buildingQuery.getUpdateOneSQLQuery(getSession(),dynaBean);
    			int i = sqlQuery.executeUpdate();
    			if(i == 1){
    				Iterator it = dynaBean.getValues().entrySet().iterator();
    				while(it.hasNext()) {
    				   Map.Entry entry = (Map.Entry) it.next();
    				   String key = (String)entry.getKey();
    				   Object value = entry.getValue();
    				   if(key.indexOf("@") == -1 && value != null){
    					   goalBean.set(key, value);
    				   }
    				}    				
    			}
    		}else{
				throw new PlatformException("更新失败:动态Bean没有主键的描述,请为KEY_PK_CODE赋值",PlatformExceptionEnum.JE_CORE_DYNABEAN_PARAM_ERROR);
    		}
    		goalBean.set(BeanUtils.KEY_TABLE_CODE, tableCode);
		}catch(DataException e){
			String errors="执行更新数据的操作失败...";
			SQLException sqle=e.getSQLException();
			//如果是SQLServer数据库
			if(ConstantVars.STR_SQLSERVER.equals(PCDaoTemplateImpl.DBNAME) && e.getCause()!=null && sqle.getMessage().indexOf("将截断字符串或二进制数据")!=-1){
				//找出字符过长的字段
				errors=SqlUtil.getStringLong(dynaBean);
			}else if(ConstantVars.STR_MYSQL.equals(PCDaoTemplateImpl.DBNAME) && sqle.getMessage().indexOf("Data too long for column")!=-1){
				//找出字符过长的字段
				errors=SqlUtil.getStringLong(dynaBean);
			}else if(ConstantVars.STR_ORACLE.equals(PCDaoTemplateImpl.DBNAME) && e.getCause()!=null && sqle.getMessage().indexOf("ORA-12899")!=-1){
				errors=e.getMessage().replaceAll("\"", "");
			}
			throw new PlatformException(errors,PlatformExceptionEnum.JE_CORE_DYNABEAN_FIELDLONG_ERROR,e);
		} catch (Exception e) {
			throw new PlatformException("执行更新数据的操作失败...",PlatformExceptionEnum.JE_CORE_DYNABEAN_UPDATE_ERROR,new Object[]{dynaBean},e);
		}
    	return goalBean;		
    }
	/**
	 * 基于whereSql修改数据库的一条记录信息
	 * @param dynaBean
	 * @return
	 */
	@Override
    public int listUpdate(DynaBean dynaBean){
		int i = 0;
		try {
    		if(dynaBean.get(BeanUtils.KEY_WHERE) != null){
    			SQLQuery sqlQuery = buildingQuery.getListUpdateSQLQuery(getSession(),dynaBean,"sql",null,null);
    			i = sqlQuery.executeUpdate();
    		}else{
				throw new PlatformException("批量更新失败:动态Bean没有WHERE的描述,请为KEY_WHERE赋值",PlatformExceptionEnum.JE_CORE_DYNABEAN_PARAM_ERROR);
    		}
		}catch(HibernateException e){
			String errors="执行批量更新数据的操作失败...";
			//如果是SQLServer数据库
			if(ConstantVars.STR_SQLSERVER.equals(PCDaoTemplateImpl.DBNAME) && e.getCause()!=null && e.getCause().getMessage().indexOf("将截断字符串或二进制数据")!=-1){
				//找出字符过长的字段
				errors=SqlUtil.getStringLong(dynaBean);
			}else if(ConstantVars.STR_MYSQL.equals(PCDaoTemplateImpl.DBNAME) && e.getMessage().indexOf("Data too long for column")!=-1){
				//找出字符过长的字段
				errors=SqlUtil.getStringLong(dynaBean);
			}else if(ConstantVars.STR_ORACLE.equals(PCDaoTemplateImpl.DBNAME) && e.getCause()!=null && e.getMessage().indexOf("ORA-12899")!=-1){
				errors=e.getMessage().replaceAll("\"", "");
			}
			throw new PlatformException(errors,PlatformExceptionEnum.JE_CORE_DYNABEAN_FIELDLONG_ERROR,e);
		} catch (Exception e) {
			throw new PlatformException("执行批量更新数据的操作失败...",PlatformExceptionEnum.JE_CORE_DYNABEAN_UPDATE_ERROR,new Object[]{dynaBean},e);
		}
    	return i;
    }

	@Override
	public int listUpdate(DynaBean dynaBean, Object[] params) {
		int i = 0;
		try {
			if(dynaBean.get(BeanUtils.KEY_WHERE) != null){
				SQLQuery sqlQuery = buildingQuery.getListUpdateSQLQuery(getSession(),dynaBean,"index",params,null);
				i = sqlQuery.executeUpdate();
			}else{
				throw new PlatformException("批量更新失败:动态Bean没有WHERE的描述,请为KEY_WHERE赋值",PlatformExceptionEnum.JE_CORE_DYNABEAN_PARAM_ERROR);
			}
		}catch(HibernateException e){
			String errors="执行批量更新数据的操作失败...";
			//如果是SQLServer数据库
			if(ConstantVars.STR_SQLSERVER.equals(PCDaoTemplateImpl.DBNAME) && e.getCause()!=null && e.getCause().getMessage().indexOf("将截断字符串或二进制数据")!=-1){
				//找出字符过长的字段
				errors=SqlUtil.getStringLong(dynaBean);
			}else if(ConstantVars.STR_MYSQL.equals(PCDaoTemplateImpl.DBNAME) && e.getMessage().indexOf("Data too long for column")!=-1){
				//找出字符过长的字段
				errors=SqlUtil.getStringLong(dynaBean);
			}else if(ConstantVars.STR_ORACLE.equals(PCDaoTemplateImpl.DBNAME) && e.getCause()!=null && e.getMessage().indexOf("ORA-12899")!=-1){
				errors=e.getMessage().replaceAll("\"", "");
			}
			throw new PlatformException(errors,PlatformExceptionEnum.JE_CORE_DYNABEAN_FIELDLONG_ERROR,e);
		} catch (Exception e) {
			throw new PlatformException("执行批量更新数据的操作失败...",PlatformExceptionEnum.JE_CORE_DYNABEAN_UPDATE_ERROR,new Object[]{dynaBean},e);
		}
		return i;
	}

	@Override
	public int listUpdate(DynaBean dynaBean, Map<String, Object> params) {
		int i = 0;
		try {
			if(dynaBean.get(BeanUtils.KEY_WHERE) != null){
				SQLQuery sqlQuery = buildingQuery.getListUpdateSQLQuery(getSession(),dynaBean,"name",null,params);
				i = sqlQuery.executeUpdate();
			}else{
				throw new PlatformException("批量更新失败:动态Bean没有WHERE的描述,请为KEY_WHERE赋值",PlatformExceptionEnum.JE_CORE_DYNABEAN_PARAM_ERROR);
			}
		}catch(HibernateException e){
			String errors="执行批量更新数据的操作失败...";
			//如果是SQLServer数据库
			if(ConstantVars.STR_SQLSERVER.equals(PCDaoTemplateImpl.DBNAME) && e.getCause()!=null && e.getCause().getMessage().indexOf("将截断字符串或二进制数据")!=-1){
				//找出字符过长的字段
				errors=SqlUtil.getStringLong(dynaBean);
			}else if(ConstantVars.STR_MYSQL.equals(PCDaoTemplateImpl.DBNAME) && e.getMessage().indexOf("Data too long for column")!=-1){
				//找出字符过长的字段
				errors=SqlUtil.getStringLong(dynaBean);
			}else if(ConstantVars.STR_ORACLE.equals(PCDaoTemplateImpl.DBNAME) && e.getCause()!=null && e.getMessage().indexOf("ORA-12899")!=-1){
				errors=e.getMessage().replaceAll("\"", "");
			}
			throw new PlatformException(errors,PlatformExceptionEnum.JE_CORE_DYNABEAN_FIELDLONG_ERROR,e);
		} catch (Exception e) {
			throw new PlatformException("执行批量更新数据的操作失败...",PlatformExceptionEnum.JE_CORE_DYNABEAN_UPDATE_ERROR,new Object[]{dynaBean},e);
		}
		return i;
	}

	/**
	 * 执行SQL进行更新
	 * @param sql
	 * @return
	 */
	@Override
	public int listUpdate(String sql) {
		return hibernateTemplate.bulkUpdate(sql);
	}
	
	
	
	
	public HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}
	@Resource(name="hibernateTemplate")
	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}
	
	@Override
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}
	@Resource(name="sf")
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}    
	/**
	 * 研发部:云凤程
	 * 辅助函数把表中的字段用","连接起来拼接成字符
	 * @param dynaBean
	 */
	private List<DynaBean> loadDynaBeanFieldNames4Select(DynaBean dynaBean){
		//1.得到表的模型类
		DynaBean table = beanUtils.getResourceTable(dynaBean.getStr(BeanUtils.KEY_TABLE_CODE));
		if(null != table) {
			//2.为动态类组装select目标字段
			List<DynaBean> columns = (List<DynaBean>) table.get(BeanUtils.KEY_TABLE_COLUMNS);
			StringBuilder fieldNames = new StringBuilder();
			for(DynaBean column : columns){
				fieldNames.append(column.getStr("TABLECOLUMN_CODE")+",");
			}
			if(fieldNames.length()>0){
				fieldNames.deleteCharAt(fieldNames.length()-1);
			}
			dynaBean.set(BeanUtils.DEF_ALL_FIELDS, fieldNames.toString());
			return columns;
		}
		return null;
	}
	/**
	 * 研发部:云凤程
	 * 辅助函数,根据表的CODE得到表的主键名字
	 * @param tableName
	 * @return
	 */
	@SuppressWarnings("unused")
	private String getTablePriKeyByCode(String tableName){
		//1.得到表的模型类
		DynaBean table = beanUtils.getResourceTable(tableName);	
		List<DynaBean> keys = (List<DynaBean>) table.get(BeanUtils.KEY_TABLE_KEYS);
		String priKeyName = null;
		for(DynaBean key : keys){
			if("Primary".equals(key.getStr("TABLEKEY_TYPE"))){
				priKeyName = key.getStr("TABLEKEY_COLUMNCODE");
				break;
			}
		}
		return priKeyName;
	}
	/**
	 * 研发部:云凤程
	 * 辅助函数:装载动态Bean的数值
	 * @param dynaBean
	 * @param bean
	 */
	private void loadDynaBeanFieldValues(DynaBean dynaBean ,Map bean ,List<DynaBean> columns){
//		int i = 0;
		String queryFields=dynaBean.getStr(BeanUtils.KEY_QUERY_FIELDS, "");
		Set<String> fieldSets=new HashSet<String>();
		if(StringUtil.isNotEmpty(queryFields)){
			for(String f:queryFields.split(",")){
				if(StringUtil.isNotEmpty(f)){
					fieldSets.add(f);
				}
			}
		}
		for(DynaBean column : columns){
			String columnCode=column.getStr("TABLECOLUMN_CODE");
			if(fieldSets.size()>0 && !fieldSets.contains(columnCode)){
				continue;
			}
			if(ColumnType.CLOB.equals(column.getStr("TABLECOLUMN_TYPE")) || ColumnType.BIGCLOB.equals(column.getStr("TABLECOLUMN_TYPE"))){
				if(bean.get(columnCode) == null){
					dynaBean.set(column.getStr("TABLECOLUMN_CODE"), "");
				}else{
					String value="";
					//如果是Oracle数据库需将clob转换成string
					if(PCDaoTemplateImpl.DBNAME.equals(ConstantVars.STR_ORACLE)){
						value=StringUtil.getClobValue(bean.get(columnCode));
					}else{
						value=bean.get(columnCode)+"";
					}
					dynaBean.set(column.getStr("TABLECOLUMN_CODE"), value);
				}
			}else{
				//如果是小数类型则查询出来类型为BigDecimal   需转成string处理(dynabean本身会将string转成double)
				//2013年2月22日 13:50:45  张帅鹏改动
				Object value=bean.get(columnCode);
				if(value!=null && value instanceof BigDecimal && ArrayUtils.contains(new String[]{"NUMBER","FLOAT"}, column.getStr("TABLECOLUMN_TYPE"))){
					if("NUMBER".equals(column.getStr("TABLECOLUMN_TYPE"))){
						dynaBean.set(column.getStr("TABLECOLUMN_CODE"), Integer.parseInt(StringUtil.getDefaultValue(value,"0")));
					}else{
						dynaBean.set(column.getStr("TABLECOLUMN_CODE"), Double.parseDouble(StringUtil.getDefaultValue(value,"0")));
					}
				}else{
					//Oracle会把空串返回为null 所以这里设置默认值
					if(PCDaoTemplateImpl.DBNAME.equals(ConstantVars.STR_ORACLE) && value==null){
						dynaBean.set(column.getStr("TABLECOLUMN_CODE"), StringUtil.getDefaultValue(value,""));
					}else{
						dynaBean.set(column.getStr("TABLECOLUMN_CODE"), value);
					}
				}
			}
//			i++;
		}		
	
	}
	@SuppressWarnings("unchecked")
	@Override
	public ArrayList<HashMap> loadTree(String rootId,JSONTreeNode template,DynaBean table,String excludes,Boolean checked,QueryInfo queryInfo){
//		String pkCode=template.getId();
		String tableName=table.getStr(BeanUtils.KEY_TABLE_CODE);
		List<DynaBean> columns=(List<DynaBean>) table.get(BeanUtils.KEY_TABLE_COLUMNS);
		String sql=DBSqlUtils.getPcDBMethodManager().getDynaTreeSql(columns, template, tableName, rootId,queryInfo);
		Query query = buildingQuery.getSqlQuery(sql,"sql",null,null, sessionFactory);
		List<Object[]> dynas=query.list();
		ArrayList<HashMap> datas=new ArrayList<HashMap>();
		for(Object[] obj:dynas){
			HashMap item=new HashMap();
			for(int i=0;i<columns.size();i++){
				DynaBean column=columns.get(i);
				item.put(column.getStr("TABLECOLUMN_CODE"), obj[i]);
			}
			//"id","text","cls","leaf","href","hrefTarget","expandable","description","code","icon","iconCls","bigIcon","bigIconCls","parent","nodeInfo","nodeInfoType","disabled","nodePath"
			item.put("children", new ArrayList<HashMap>());
			item.put("id", item.get(template.getId()));
			item.put("text", item.get(template.getText()));
			item.put("code", item.get(template.getCode()));
			item.put("parent", item.get(template.getParent()));
			if(StringUtil.isNotEmpty(template.getNodeInfo())){
				item.put("nodeInfo", item.get(template.getNodeInfo()));
			}
			if(StringUtil.isNotEmpty(template.getNodeInfoType())){
				item.put("nodeInfoType", item.get(template.getNodeInfoType()));
			}
			if(StringUtil.isNotEmpty(template.getNodeType())){
				item.put("nodeType", item.get(template.getNodeType()));
			}
			if(StringUtil.isNotEmpty(template.getIcon())){
				item.put("icon", item.get(template.getIcon()));
			}
			if(StringUtil.isNotEmpty(template.getIconCls())){
				item.put("iconCls", item.get(template.getIconCls()));
			}
			if(StringUtil.isNotEmpty(template.getLayer())){
				item.put("layer", item.get(template.getLayer()));
			}
			if(StringUtil.isNotEmpty(template.getDisabled())){
				item.put("disabled", item.get(template.getDisabled()));
			}
			if(StringUtil.isNotEmpty(template.getNodePath())){
				item.put("nodePath", item.get(template.getNodePath()));
			}
			if(StringUtil.isNotEmpty(template.getParentPath())){
				item.put("parentPath", item.get(template.getParentPath()));
			}
			if(StringUtil.isNotEmpty(template.getHref())){
				item.put("href", item.get(template.getHref()));
			}
			if(StringUtil.isNotEmpty(template.getHrefTarget())){
				item.put("hrefTarget", item.get(template.getHrefTarget()));
			}
			if(StringUtil.isNotEmpty(template.getDescription())){
				item.put("description", item.get(template.getDescription()));
			}
			if(StringUtil.isNotEmpty(template.getOrderIndex())){
				item.put("orderIndex", item.get(template.getOrderIndex()));
			}
			item.put("checked", checked);
			item.put("leaf", true);
			item.put("children", new ArrayList<HashMap>());
			for(String exclude:excludes.split(",")){
				item.remove(exclude);
			}
			//如果是oracle则单独处理
			if(PCDaoTemplateImpl.DBNAME.equals(ConstantVars.STR_ORACLE)){
				for(Object key:item.keySet()){
					if(item.get(key+"")!=null && item.get(key+"") instanceof Clob){
						item.put(key+"", StringUtil.getClobValue(item.get(key+"")));
					}
				}
			}
			datas.add(item);
		}
		return datas;
	}
	@Override
	public ArrayList<HashMap> loadTree(String rootId,JSONTreeNode template, DynaBean dynaBean,QueryInfo queryInfo) {
		// TODO Auto-generated method stub
//		String pkCode=dynaBean.getStr(BeanUtils.KEY_PK_CODE);
		String tableName=dynaBean.getStr(BeanUtils.KEY_TABLE_CODE);
		List<DynaBean> columns=loadDynaBeanFieldNames4Select(dynaBean);
		//构建递归查询语句
		String sql=DBSqlUtils.getPcDBMethodManager().getDynaTreeSql(columns, template, tableName, rootId,queryInfo);
		Query query = buildingQuery.getSqlQuery(sql,"sql",null,null, sessionFactory);
		List<Object[]> dynas=query.list();
		//递归查询出来List
		ArrayList<HashMap> datas=new ArrayList<HashMap>();
		for(Object[] obj:dynas){
			HashMap item=new HashMap();
			for(int i=0;i<columns.size();i++){
				DynaBean column=columns.get(i);
				item.put(column.getStr("TABLECOLUMN_CODE"), obj[i]);
			}
			item.put("children", new ArrayList<HashMap>());
			item.put("leaf", true);
			item.put("checked", false);
			datas.add(item);
		}
		//构建树形
		return datas;
	}
	@Override
	public void insert(List<DynaBean> beans) {
		// TODO Auto-generated method stub
		for(DynaBean bean:beans){
			insert(bean);
		}
	}
}




