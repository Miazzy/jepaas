package com.je.rbac.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.annotation.Resource;

import com.je.cache.service.config.BackCacheManager;
import com.je.cache.service.config.FrontCacheManager;
import com.je.cache.service.config.JepfCacheManager;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.providers.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Component;

import com.je.cache.service.dic.DicCacheManager;
import com.je.cache.service.dic.DicInfoCacheManager;
import com.je.core.constants.ConstantVars;
import com.je.core.constants.dd.DDType;
import com.je.core.constants.tree.NodeType;
import com.je.core.entity.QueryInfo;
import com.je.core.entity.extjs.JSONTreeNode;
import com.je.core.util.SecurityUserHolder;
import com.je.core.service.PCDynaServiceTemplate;
import com.je.core.service.PCServiceTemplate;
import com.je.core.util.ArrayUtils;
import com.je.core.util.StringUtil;
import com.je.core.util.WebUtils;
import com.je.core.util.bean.BeanUtils;
import com.je.core.util.bean.DynaBean;
import com.je.rbac.model.EndUser;

import edu.emory.mathcs.backport.java.util.Arrays;

@Component("rbacServiceTemplate")
public class RbacServiceTemplateImpl implements RbacServiceTemplate {
	@Autowired
	private PCDynaServiceTemplate serviceTemplate;
	@Autowired
	private PCServiceTemplate pcServiceTemplate;

	/**
	 * 根据部门id查询该部门及子部门下所有人员
	 * @param deptIds 部门主键
	 * @param whereSql  人员查询条件
	 * @param cascade 是否级联
	 * @param queryFields 查询字段   按逗号隔开，如果传空则查询全部字段
	 * @return
	 */
	@Override
	public List<DynaBean> queryUserByDeptId(String deptIds, String whereSql,Boolean cascade, String queryFields) {
		// TODO Auto-generated method stub
		DynaBean dynaBean=new DynaBean("JE_CORE_ENDUSER",false);
		dynaBean.set(BeanUtils.KEY_PK_CODE, "USERID");
		StringBuffer whereSqlBuffer=new StringBuffer();
		if(StringUtil.isEmpty(deptIds)){
			whereSqlBuffer.append(" 1!=1");
		}else{
			if(cascade){
				for(String deptId:deptIds.split(",")){
					whereSqlBuffer.append(" PATH LIKE '%"+deptId+"%' OR");
				}
				whereSqlBuffer.delete(whereSqlBuffer.length()-2, whereSqlBuffer.length());
			}else{
				whereSqlBuffer.append(" DEPTID IN ("+StringUtil.buildArrayToString(deptIds.split(","))+")");
			}
		}
		dynaBean.set(BeanUtils.KEY_WHERE, whereSql+"  AND STATUS='1' AND ISSYSUSER='1' AND DEPTID IN ( SELECT DEPTID FROM JE_CORE_DEPARTMENT WHERE 1=1 AND ("+whereSqlBuffer.toString()+"))");
		if(StringUtil.isNotEmpty(queryFields)){
			dynaBean.set(BeanUtils.KEY_QUERY_FIELDS, queryFields);
		}
		List<DynaBean> lists=serviceTemplate.selectList(dynaBean);
		return lists;
	}

	/**
	 * 根据部门编码查询该部门及子部门下所有人员
	 * @param deptCodes 部门编码
	 * @param whereSql 人员查询条件
	 * @param cascade 是否级联
	 * @param queryFields 查询字段   按逗号隔开，如果传空则查询全部字段
	 * @return
	 */
	@Override
	public List<DynaBean> queryUserByDeptCode(String deptCodes,String whereSql,Boolean cascade, String queryFields) {
		// TODO Auto-generated method stub
		List<DynaBean> depts=serviceTemplate.selectList("JE_CORE_DEPARTMENT", " AND DEPTCODE IN ("+StringUtil.buildArrayToString(deptCodes.split(","))+")");
		DynaBean dynaBean=new DynaBean("JE_CORE_ENDUSER",false);
		dynaBean.set(BeanUtils.KEY_PK_CODE, "USERID");
		StringBuffer whereSqlBuffer=new StringBuffer();
		if(depts.size()>0){
			if(cascade){
				for(DynaBean dept:depts){
					whereSqlBuffer.append(" PATH LIKE '%"+dept.getStr("DEPTID")+"%' OR");
				}
				whereSqlBuffer.delete(whereSqlBuffer.length()-2, whereSqlBuffer.length());
			}else{
				String[] deptIds=new String[depts.size()];
				for(Integer i=0;i<depts.size();i++){
					deptIds[i]=depts.get(i).getStr("DEPTID");
				}
				whereSqlBuffer.append(" DEPTID IN ("+StringUtil.buildArrayToString(deptIds)+")");
			}
		}else{
			whereSqlBuffer.append(" 1!=1");
		}
		dynaBean.set(BeanUtils.KEY_WHERE, whereSql+" AND STATUS='1' AND ISSYSUSER='1' AND DEPTID IN ( SELECT DEPTID FROM JE_CORE_DEPARTMENT WHERE 1=1 AND ("+whereSqlBuffer.toString()+"))");
		if(StringUtil.isNotEmpty(queryFields)){
			dynaBean.set(BeanUtils.KEY_QUERY_FIELDS, queryFields);
		}
		List<DynaBean> lists=serviceTemplate.selectList(dynaBean);
		return lists;
	}

	/**
	 * 统计部门下人员数量
	 * @param deptIds
	 * @param whereSql 人员查询条件
	 * @param cascade 是否级联
	 * @return
	 */
	@Override
	public Long countUserByDeptId(String deptIds,String whereSql,Boolean cascade) {
		// TODO Auto-generated method stub
		DynaBean dynaBean=new DynaBean("JE_CORE_ENDUSER",false);
		dynaBean.set(BeanUtils.KEY_PK_CODE, "USERID");
		StringBuffer whereSqlBuffer=new StringBuffer();
		if(StringUtil.isEmpty(deptIds)){
			whereSqlBuffer.append(" 1!=1");
		}else{
			if(cascade){
				for(String deptId:deptIds.split(",")){
					whereSqlBuffer.append(" PATH LIKE '%"+deptId+"%' OR");
				}
				whereSqlBuffer.delete(whereSqlBuffer.length()-2, whereSqlBuffer.length());
			}else{
				whereSqlBuffer.append(" DEPTID IN ("+StringUtil.buildArrayToString(deptIds.split(","))+")");
			}
		}
		dynaBean.set(BeanUtils.KEY_WHERE, whereSql+" AND STATUS='1' AND ISSYSUSER='1' AND DEPTID IN ( SELECT DEPTID FROM JE_CORE_DEPARTMENT WHERE 1=1 AND ("+whereSqlBuffer.toString()+"))");
		Long count=serviceTemplate.selectCount(dynaBean);
		return count;
	}

	/**
	 * 统计部门下人员数量
	 * @param deptCodes
	 * @param whereSql 人员查询条件
	 * @param cascade 是否级联
	 * @return
	 */
	@Override
	public Long countUserByDeptCode(String deptCodes, String whereSql,Boolean cascade) {
		// TODO Auto-generated method stub
		List<DynaBean> depts=serviceTemplate.selectList("JE_CORE_DEPARTMENT", " AND DEPTCODE IN ("+StringUtil.buildArrayToString(deptCodes.split(","))+")");
		DynaBean dynaBean=new DynaBean("JE_CORE_ENDUSER",false);
		dynaBean.set(BeanUtils.KEY_PK_CODE, "USERID");
		StringBuffer whereSqlBuffer=new StringBuffer();
		if(depts.size()>0){
			if(cascade){
				for(DynaBean dept:depts){
					whereSqlBuffer.append(" PATH LIKE '%"+dept.getStr("DEPTID")+"%' OR");
				}
				whereSqlBuffer.delete(whereSqlBuffer.length()-2, whereSqlBuffer.length());
			}else{
				String[] deptIds=new String[depts.size()];
				for(Integer i=0;i<depts.size();i++){
					deptIds[i]=depts.get(i).getStr("DEPTID");
				}
				whereSqlBuffer.append(" DEPTID IN ("+StringUtil.buildArrayToString(deptIds)+")");
			}
		}else{
			whereSqlBuffer.append(" 1!=1");
		}
		dynaBean.set(BeanUtils.KEY_WHERE,whereSql+" AND STATUS='1' AND ISSYSUSER='1' AND DEPTID IN ( SELECT DEPTID FROM JE_CORE_DEPARTMENT WHERE 1=1 AND ("+whereSqlBuffer.toString()+"))");
		Long count=serviceTemplate.selectCount(dynaBean);
		return count;
	}

	/**
	 * 根据角色id查询该部门及子角色下所有人员
	 * @param roleIds 角色主键
	 * @param whereSql  人员查询条件
	 * @param cascade 是否级联
	 * @param queryFields 查询字段   按逗号隔开，如果传空则查询全部字段
	 * @return
	 */
	@Override
	public List<DynaBean> queryUserByRoleId(String roleIds, String whereSql,Boolean cascade,String queryFields) {
		// TODO Auto-generated method stub
		DynaBean dynaBean=new DynaBean("JE_CORE_ENDUSER",false);
		dynaBean.set(BeanUtils.KEY_PK_CODE, "USERID");
		StringBuffer whereSqlBuffer=new StringBuffer();
		if(StringUtil.isEmpty(roleIds)){
			whereSqlBuffer.append(" 1!=1");
		}else{
			if(cascade){
				for(String roleId:roleIds.split(",")){
					whereSqlBuffer.append(" PATH LIKE '%"+roleId+"%' OR");
				}
				whereSqlBuffer.delete(whereSqlBuffer.length()-2, whereSqlBuffer.length());
			}else{
				whereSqlBuffer.append(" ROLEID IN ("+StringUtil.buildArrayToString(roleIds.split(","))+")");
			}
		}
		dynaBean.set(BeanUtils.KEY_WHERE, whereSql+"  AND STATUS='1' AND ISSYSUSER='1' AND USERID IN (SELECT USERID FROM JE_CORE_ROLE_USER WHERE ROLEID IN ( SELECT ROLEID FROM JE_CORE_ROLE WHERE 1=1 AND ROLETYPE='ROLE' AND ("+whereSqlBuffer.toString()+")))");
		if(StringUtil.isNotEmpty(queryFields)){
			dynaBean.set(BeanUtils.KEY_QUERY_FIELDS, queryFields);
		}
		List<DynaBean> lists=serviceTemplate.selectList(dynaBean);
		return lists;
	}

	/**
	 * 根据角色编码查询该部门及子角色下所有人员
	 * @param roleCodes 角色编码
	 * @param whereSql 人员查询条件
	 * @param cascade 是否级联
	 * @param queryFields 查询字段   按逗号隔开，如果传空则查询全部字段
	 * @return
	 */
	@Override
	public List<DynaBean> queryUserByRoleCode(String roleCodes,String whereSql,Boolean cascade, String queryFields) {
		// TODO Auto-generated method stub
		List<DynaBean> roles=serviceTemplate.selectList("JE_CORE_ROLE", " AND ROLETYPE='ROLE' AND ROLECODE IN ("+StringUtil.buildArrayToString(roleCodes.split(","))+")");
		DynaBean dynaBean=new DynaBean("JE_CORE_ENDUSER",false);
		dynaBean.set(BeanUtils.KEY_PK_CODE, "USERID");
		StringBuffer whereSqlBuffer=new StringBuffer();
		if(roles.size()>0){
			if(cascade){
				for(DynaBean role:roles){
					whereSqlBuffer.append(" PATH LIKE '%"+role.getStr("ROLEID")+"%' OR");
				}
				whereSqlBuffer.delete(whereSqlBuffer.length()-2, whereSqlBuffer.length());
			}else{
				String[] roleIds=new String[roles.size()];
				for(Integer i=0;i<roles.size();i++){
					roleIds[i]=roles.get(i).getStr("ROLEID");
				}
				whereSqlBuffer.append(" ROLEID IN ("+StringUtil.buildArrayToString(roleIds)+")");
			}
		}else{
			whereSqlBuffer.append(" 1!=1");
		}
		dynaBean.set(BeanUtils.KEY_WHERE, whereSql+"  AND STATUS='1' AND ISSYSUSER='1' AND USERID IN (SELECT USERID FROM JE_CORE_ROLE_USER WHERE ROLEID IN ( SELECT ROLEID FROM JE_CORE_ROLE WHERE 1=1 AND ROLETYPE='ROLE' AND ("+whereSqlBuffer.toString()+")))");
		if(StringUtil.isNotEmpty(queryFields)){
			dynaBean.set(BeanUtils.KEY_QUERY_FIELDS, queryFields);
		}
		List<DynaBean> lists=serviceTemplate.selectList(dynaBean);
		return lists;
	}

	/**
	 * 统计角色下人员数量
	 * @param roleIds 角色id
	 * @param whereSql 人员查询条件
	 * @param cascade 是否级联
	 * @return
	 */
	@Override
	public Long countUserByRoleId(String roleIds, String whereSql,Boolean cascade) {
		// TODO Auto-generated method stub
		DynaBean dynaBean=new DynaBean("JE_CORE_ENDUSER",false);
		dynaBean.set(BeanUtils.KEY_PK_CODE, "USERID");
		StringBuffer whereSqlBuffer=new StringBuffer();
		if(StringUtil.isEmpty(roleIds)){
			whereSqlBuffer.append(" 1!=1");
		}else{
			if(cascade){
				for(String roleId:roleIds.split(",")){
					whereSqlBuffer.append(" PATH LIKE '%"+roleId+"%' OR");
				}
				whereSqlBuffer.delete(whereSqlBuffer.length()-2, whereSqlBuffer.length());
			}else{
				whereSqlBuffer.append(" ROLEID IN ("+StringUtil.buildArrayToString(roleIds.split(","))+")");
			}
		}
		dynaBean.set(BeanUtils.KEY_WHERE, whereSql+"  AND STATUS='1' AND ISSYSUSER='1' AND USERID IN (SELECT USERID FROM JE_CORE_ROLE_USER WHERE ROLEID IN ( SELECT ROLEID FROM JE_CORE_ROLE WHERE 1=1 AND ROLETYPE='ROLE' AND ("+whereSqlBuffer.toString()+")))");
		Long count=serviceTemplate.selectCount(dynaBean);
		return count;
	}

	/**
	 * 统计角色下人员数量
	 * @param roleCodes 角色编码
	 * @param whereSql 人员查询条件
	 * @param cascade 是否级联
	 * @return
	 */
	@Override
	public Long countUserByRoleCode(String roleCodes, String whereSql,Boolean cascade) {
		// TODO Auto-generated method stub
		List<DynaBean> roles=serviceTemplate.selectList("JE_CORE_ROLE", " AND ROLETYPE='ROLE' AND ROLECODE IN ("+StringUtil.buildArrayToString(roleCodes.split(","))+")");
		DynaBean dynaBean=new DynaBean("JE_CORE_ENDUSER",false);
		dynaBean.set(BeanUtils.KEY_PK_CODE, "USERID");
		StringBuffer whereSqlBuffer=new StringBuffer();
		if(roles.size()>0){
			if(cascade){
				for(DynaBean role:roles){
					whereSqlBuffer.append(" PATH LIKE '%"+role.getStr("ROLEID")+"%' OR");
				}
				whereSqlBuffer.delete(whereSqlBuffer.length()-2, whereSqlBuffer.length());
			}else{
				String[] roleIds=new String[roles.size()];
				for(Integer i=0;i<roles.size();i++){
					roleIds[i]=roles.get(i).getStr("ROLEID");
				}
				whereSqlBuffer.append(" ROLEID IN ("+StringUtil.buildArrayToString(roleIds)+")");
			}
		}else{
			whereSqlBuffer.append(" 1!=1");
		}
		dynaBean.set(BeanUtils.KEY_WHERE, whereSql+"  AND STATUS='1' AND ISSYSUSER='1' AND USERID IN (SELECT USERID FROM JE_CORE_ROLE_USER WHERE ROLEID IN ( SELECT ROLEID FROM JE_CORE_ROLE WHERE 1=1 AND ROLETYPE='ROLE' AND ("+whereSqlBuffer.toString()+")))");
		Long count=serviceTemplate.selectCount(dynaBean);
		return count;
	}

	/**
	 * 根据岗位id查询该部门及子岗位下所有人员
	 * @param sentryIds 岗位主键
	 * @param whereSql  人员查询条件
	 * @param cascade 是否级联
	 * @param queryFields 查询字段   按逗号隔开，如果传空则查询全部字段
	 * @return
	 */
	@Override
	public List<DynaBean> queryUserBySentryId(String sentryIds,String whereSql,Boolean cascade, String queryFields) {
		// TODO Auto-generated method stub
		DynaBean dynaBean=new DynaBean("JE_CORE_ENDUSER",false);
		dynaBean.set(BeanUtils.KEY_PK_CODE, "USERID");
		StringBuffer whereSqlBuffer=new StringBuffer();
		if(StringUtil.isEmpty(sentryIds)){
			whereSqlBuffer.append(" 1!=1");
		}else{
			if(cascade){
				for(String sentryId:sentryIds.split(",")){
					whereSqlBuffer.append(" PATH LIKE '%"+sentryId+"%' OR");
				}
				whereSqlBuffer.delete(whereSqlBuffer.length()-2, whereSqlBuffer.length());
			}else{
				whereSqlBuffer.append(" SENTRYID IN ("+StringUtil.buildArrayToString(sentryIds.split(","))+")");
			}
		}
		dynaBean.set(BeanUtils.KEY_WHERE, whereSql+"  AND STATUS='1' AND ISSYSUSER='1' AND USERID IN (SELECT USERID FROM JE_CORE_SENTRY_USER WHERE SENTRYID IN ( SELECT SENTRYID FROM JE_CORE_SENTRY WHERE 1=1 AND ("+whereSqlBuffer.toString()+")))");
		if(StringUtil.isNotEmpty(queryFields)){
			dynaBean.set(BeanUtils.KEY_QUERY_FIELDS, queryFields);
		}
		List<DynaBean> lists=serviceTemplate.selectList(dynaBean);
		return lists;
	}

	/**
	 * 根据岗位编码查询该部门及子岗位下所有人员
	 * @param sentryCodes 岗位编码
	 * @param whereSql 人员查询条件
	 * @param cascade 是否级联
	 * @param queryFields 查询字段   按逗号隔开，如果传空则查询全部字段
	 * @return
	 */
	@Override
	public List<DynaBean> queryUserBySentryCode(String sentryCodes,String whereSql,Boolean cascade, String queryFields) {
		// TODO Auto-generated method stub
		List<DynaBean> sentrys=serviceTemplate.selectList("JE_CORE_SENTRY", " AND SENTRYCODE IN ("+StringUtil.buildArrayToString(sentryCodes.split(","))+")");
		DynaBean dynaBean=new DynaBean("JE_CORE_ENDUSER",false);
		dynaBean.set(BeanUtils.KEY_PK_CODE, "USERID");
		StringBuffer whereSqlBuffer=new StringBuffer();
		if(sentrys.size()>0){
			if(cascade){
				for(DynaBean sentry:sentrys){
					whereSqlBuffer.append(" PATH LIKE '%"+sentry.getStr("SENTRYID")+"%' OR");
				}
				whereSqlBuffer.delete(whereSqlBuffer.length()-2, whereSqlBuffer.length());
			}else{
				String[] sentryIds=new String[sentrys.size()];
				for(Integer i=0;i<sentrys.size();i++){
					sentryIds[i]=sentrys.get(i).getStr("SENTRYID");
				}
				whereSqlBuffer.append(" SENTRYID IN ("+StringUtil.buildArrayToString(sentryIds)+")");
			}
		}else{
			whereSqlBuffer.append(" 1!=1");
		}
		dynaBean.set(BeanUtils.KEY_WHERE, whereSql+"  AND STATUS='1' AND ISSYSUSER='1' AND USERID IN (SELECT USERID FROM JE_CORE_SENTRY_USER WHERE SENTRYID IN ( SELECT SENTRYID FROM JE_CORE_SENTRY WHERE 1=1 AND ("+whereSqlBuffer.toString()+")))");
		if(StringUtil.isNotEmpty(queryFields)){
			dynaBean.set(BeanUtils.KEY_QUERY_FIELDS, queryFields);
		}
		List<DynaBean> lists=serviceTemplate.selectList(dynaBean);
		return lists;
	}

	/**
	 * 统计岗位下人员数量
	 * @param sentryIds
	 * @param whereSql 人员查询条件
	 * @param cascade 是否级联
	 * @return
	 */
	@Override
	public Long countUserBySentryId(String sentryIds, String whereSql,Boolean cascade) {
		// TODO Auto-generated method stub
		DynaBean dynaBean=new DynaBean("JE_CORE_ENDUSER",false);
		dynaBean.set(BeanUtils.KEY_PK_CODE, "USERID");
		StringBuffer whereSqlBuffer=new StringBuffer();
		if(StringUtil.isEmpty(sentryIds)){
			whereSqlBuffer.append(" 1!=1");
		}else{
			if(cascade){
				for(String sentryId:sentryIds.split(",")){
					whereSqlBuffer.append(" PATH LIKE '%"+sentryId+"%' OR");
				}
				whereSqlBuffer.delete(whereSqlBuffer.length()-2, whereSqlBuffer.length());
			}else{
				whereSqlBuffer.append(" SENTRYID IN ("+StringUtil.buildArrayToString(sentryIds.split(","))+")");
			}
		}
		dynaBean.set(BeanUtils.KEY_WHERE, whereSql+"  AND STATUS='1' AND ISSYSUSER='1' AND USERID IN (SELECT USERID FROM JE_CORE_SENTRY_USER WHERE SENTRYID IN ( SELECT SENTRYID FROM JE_CORE_SENTRY WHERE 1=1 AND ("+whereSqlBuffer.toString()+")))");
		Long count=serviceTemplate.selectCount(dynaBean);
		return count;
	}

	/**
	 * 统计岗位下人员数量
	 * @param sentryCodes
	 * @param whereSql 人员查询条件
	 * @param cascade 是否级联
	 * @return
	 */
	@Override
	public Long countUserBySentryCode(String sentryCodes, String whereSql,Boolean cascade) {
		// TODO Auto-generated method stub
		List<DynaBean> sentrys=serviceTemplate.selectList("JE_CORE_SENTRY", " AND SENTRYCODE IN ("+StringUtil.buildArrayToString(sentryCodes.split(","))+")");
		DynaBean dynaBean=new DynaBean("JE_CORE_ENDUSER",false);
		dynaBean.set(BeanUtils.KEY_PK_CODE, "USERID");
		StringBuffer whereSqlBuffer=new StringBuffer();
		if(sentrys.size()>0){
			if(cascade){
				for(DynaBean sentry:sentrys){
					whereSqlBuffer.append(" PATH LIKE '%"+sentry.getStr("SENTRYID")+"%' OR");
				}
				whereSqlBuffer.delete(whereSqlBuffer.length()-2, whereSqlBuffer.length());
			}else{
				String[] sentryIds=new String[sentrys.size()];
				for(Integer i=0;i<sentrys.size();i++){
					sentryIds[i]=sentrys.get(i).getStr("SENTRYID");
				}
				whereSqlBuffer.append(" SENTRYID IN ("+StringUtil.buildArrayToString(sentryIds)+")");
			}
		}else{
			whereSqlBuffer.append(" 1!=1");
		}
		dynaBean.set(BeanUtils.KEY_WHERE, whereSql+"  AND STATUS='1' AND ISSYSUSER='1' AND USERID IN (SELECT USERID FROM JE_CORE_SENTRY_USER WHERE SENTRYID IN ( SELECT SENTRYID FROM JE_CORE_SENTRY WHERE 1=1 AND ("+whereSqlBuffer.toString()+")))");
		Long count=serviceTemplate.selectCount(dynaBean);
		return count;
	}

	/**
	 * 统计所有人员数量
	 * @param whereSql 人员查询条件
	 * @param queryFields 查询字段   按逗号隔开，如果传空则查询全部字段
	 * @return
	 */
	@Override
	public List<DynaBean> queryUser(String whereSql, String queryFields) {
		// TODO Auto-generated method stub
		DynaBean dynaBean=new DynaBean("JE_CORE_ENDUSER",false);
		dynaBean.set(BeanUtils.KEY_PK_CODE, "USERID");
		dynaBean.set(BeanUtils.KEY_WHERE, whereSql+"  AND STATUS='1' AND ISSYSUSER='1'");
		if(StringUtil.isNotEmpty(queryFields)){
			dynaBean.set(BeanUtils.KEY_QUERY_FIELDS, queryFields);
		}
		List<DynaBean> lists=serviceTemplate.selectList(dynaBean);
		return lists;
	}

	/**
	 * 统计所有人员数量
	 * @param whereSql 人员查询条件
	 * @return
	 */
	@Override
	public Long countUser(String whereSql) {
		// TODO Auto-generated method stub
		DynaBean dynaBean=new DynaBean("JE_CORE_ENDUSER",false);
		dynaBean.set(BeanUtils.KEY_PK_CODE, "USERID");
		dynaBean.set(BeanUtils.KEY_WHERE, whereSql+"  AND STATUS='1' AND ISSYSUSER='1'");
		Long count=serviceTemplate.selectCount(dynaBean);
		return count;
	}

	/**
	 * 根据部门id查询该部门及子部门
	 * @param deptIds 部门主键
	 * @param whereSql  查询条件
	 * @param cascade 是否级联
	 * @param queryFields 查询字段   按逗号隔开，如果传空则查询全部字段
	 * @return
	 */
	@Override
	public List<DynaBean> queryDeptById(String deptIds, String whereSql,
										Boolean cascade, String queryFields) {
		// TODO Auto-generated method stub
		DynaBean dynaBean=new DynaBean("JE_CORE_DEPARTMENT",false);
		dynaBean.set(BeanUtils.KEY_PK_CODE, "DEPTID");
		StringBuffer whereSqlBuffer=new StringBuffer();
		if(StringUtil.isEmpty(deptIds)){
			whereSqlBuffer.append(" 1!=1");
		}else{
			if(cascade){
				for(String deptId:deptIds.split(",")){
					whereSqlBuffer.append(" PATH LIKE '%"+deptId+"%' OR");
				}
				whereSqlBuffer.delete(whereSqlBuffer.length()-2, whereSqlBuffer.length());
			}else{
				whereSqlBuffer.append(" DEPTID IN ("+StringUtil.buildArrayToString(deptIds.split(","))+")");
			}
		}
		dynaBean.set(BeanUtils.KEY_WHERE, whereSql+" AND STATUS='1' AND ("+whereSqlBuffer.toString()+")");
		if(StringUtil.isNotEmpty(queryFields)){
			dynaBean.set(BeanUtils.KEY_QUERY_FIELDS, queryFields);
		}
		List<DynaBean> lists=serviceTemplate.selectList(dynaBean);
		return lists;
	}

	/**
	 * 根据部门编码查询该部门及子部门
	 * @param deptCodes 部门编码
	 * @param whereSql 查询条件
	 * @param cascade 是否级联
	 * @param queryFields 查询字段   按逗号隔开，如果传空则查询全部字段
	 * @return
	 */
	@Override
	public List<DynaBean> queryDeptByCode(String deptCodes, String whereSql,Boolean cascade, String queryFields) {
		// TODO Auto-generated method stub
		List<DynaBean> depts=serviceTemplate.selectList("JE_CORE_DEPARTMENT", " AND DEPTCODE IN ("+StringUtil.buildArrayToString(deptCodes.split(","))+")");
		DynaBean dynaBean=new DynaBean("JE_CORE_DEPARTMENT",false);
		dynaBean.set(BeanUtils.KEY_PK_CODE, "DEPTID");
		StringBuffer whereSqlBuffer=new StringBuffer();
		if(depts.size()>0){
			if(cascade){
				for(DynaBean dept:depts){
					whereSqlBuffer.append(" PATH LIKE '%"+dept.getStr("DEPTID")+"%' OR");
				}
				whereSqlBuffer.delete(whereSqlBuffer.length()-2, whereSqlBuffer.length());
			}else{
				String[] deptIds=new String[depts.size()];
				for(Integer i=0;i<depts.size();i++){
					deptIds[i]=depts.get(i).getStr("DEPTID");
				}
				whereSqlBuffer.append(" DEPTID IN ("+StringUtil.buildArrayToString(deptIds)+")");
			}
		}else{
			whereSqlBuffer.append(" 1!=1");
		}
		dynaBean.set(BeanUtils.KEY_WHERE, whereSql+" AND STATUS='1' AND ("+whereSqlBuffer.toString()+")");
		if(StringUtil.isNotEmpty(queryFields)){
			dynaBean.set(BeanUtils.KEY_QUERY_FIELDS, queryFields);
		}
		List<DynaBean> lists=serviceTemplate.selectList(dynaBean);
		return lists;
	}

	/**
	 * 统计部门数量
	 * @param deptIds
	 * @param whereSql 人员查询条件
	 * @param cascade 是否级联
	 * @return
	 */
	@Override
	public Long countDeptById(String deptIds, String whereSql, Boolean cascade) {
		// TODO Auto-generated method stub
		DynaBean dynaBean=new DynaBean("JE_CORE_DEPARTMENT",false);
		dynaBean.set(BeanUtils.KEY_PK_CODE, "DEPTID");
		StringBuffer whereSqlBuffer=new StringBuffer();
		if(StringUtil.isEmpty(deptIds)){
			whereSqlBuffer.append(" 1!=1");
		}else{
			if(cascade){
				for(String deptId:deptIds.split(",")){
					whereSqlBuffer.append(" PATH LIKE '%"+deptId+"%' OR");
				}
				whereSqlBuffer.delete(whereSqlBuffer.length()-2, whereSqlBuffer.length());
			}else{
				whereSqlBuffer.append(" DEPTID IN ("+StringUtil.buildArrayToString(deptIds.split(","))+")");
			}
		}
		dynaBean.set(BeanUtils.KEY_WHERE, whereSql+" AND STATUS='1' AND ("+whereSqlBuffer.toString()+")");
		Long count=serviceTemplate.selectCount(dynaBean);
		return count;
	}

	/**
	 * 统计部门数量
	 * @param deptCodes
	 * @param whereSql 人员查询条件
	 * @param cascade 是否级联
	 * @return
	 */
	@Override
	public Long countDeptByCode(String deptCodes, String whereSql,Boolean cascade) {
		// TODO Auto-generated method stub
		List<DynaBean> depts=serviceTemplate.selectList("JE_CORE_DEPARTMENT", " AND DEPTCODE IN ("+StringUtil.buildArrayToString(deptCodes.split(","))+")");
		DynaBean dynaBean=new DynaBean("JE_CORE_DEPARTMENT",false);
		dynaBean.set(BeanUtils.KEY_PK_CODE, "DEPTID");
		StringBuffer whereSqlBuffer=new StringBuffer();
		if(depts.size()>0){
			if(cascade){
				for(DynaBean dept:depts){
					whereSqlBuffer.append(" PATH LIKE '%"+dept.getStr("DEPTID")+"%' OR");
				}
				whereSqlBuffer.delete(whereSqlBuffer.length()-2, whereSqlBuffer.length());
			}else{
				return new Long(depts.size());
			}
		}else{
			whereSqlBuffer.append(" 1!=1");
		}
		dynaBean.set(BeanUtils.KEY_WHERE, whereSql+" AND STATUS='1' AND ("+whereSqlBuffer.toString()+")");
		Long count=serviceTemplate.selectCount(dynaBean);
		return count;
	}

	/**
	 * 根据角色id查询该角色及子角色
	 * @param roleIds 角色主键
	 * @param whereSql  查询条件
	 * @param cascade 是否级联
	 * @param queryFields 查询字段   按逗号隔开，如果传空则查询全部字段
	 * @return
	 */
	@Override
	public List<DynaBean> queryRoleById(String roleIds, String whereSql,Boolean cascade, String queryFields) {
		// TODO Auto-generated method stub
		DynaBean dynaBean=new DynaBean("JE_CORE_ROLE",false);
		dynaBean.set(BeanUtils.KEY_PK_CODE, "ROLEID");
		StringBuffer whereSqlBuffer=new StringBuffer();
		if(StringUtil.isEmpty(roleIds)){
			whereSqlBuffer.append(" 1!=1");
		}else{
			if(cascade){
				for(String roleId:roleIds.split(",")){
					whereSqlBuffer.append(" PATH LIKE '%"+roleId+"%' OR");
				}
				whereSqlBuffer.delete(whereSqlBuffer.length()-2, whereSqlBuffer.length());
			}else{
				whereSqlBuffer.append(" ROLEID IN ("+StringUtil.buildArrayToString(roleIds.split(","))+")");
			}
		}
		dynaBean.set(BeanUtils.KEY_WHERE, whereSql+" AND ROLETYPE='ROLE' AND STATUS='1' AND ("+whereSqlBuffer.toString()+")");
		if(StringUtil.isNotEmpty(queryFields)){
			dynaBean.set(BeanUtils.KEY_QUERY_FIELDS, queryFields);
		}
		List<DynaBean> lists=serviceTemplate.selectList(dynaBean);
		return lists;
	}

	/**
	 * 根据角色编码查询该角色及子角色
	 * @param roleCodes 角色编码
	 * @param whereSql 查询条件
	 * @param cascade 是否级联
	 * @param queryFields 查询字段   按逗号隔开，如果传空则查询全部字段
	 * @return
	 */
	@Override
	public List<DynaBean> queryRoleByCode(String roleCodes, String whereSql,
										  Boolean cascade, String queryFields) {
		// TODO Auto-generated method stub
		List<DynaBean> roles=serviceTemplate.selectList("JE_CORE_ROLE", " AND ROLETYPE='ROLE' AND ROLECODE IN ("+StringUtil.buildArrayToString(roleCodes.split(","))+")");
		DynaBean dynaBean=new DynaBean("JE_CORE_ROLE",false);
		dynaBean.set(BeanUtils.KEY_PK_CODE, "ROLEID");
		StringBuffer whereSqlBuffer=new StringBuffer();
		if(roles.size()>0){
			if(cascade){
				for(DynaBean role:roles){
					whereSqlBuffer.append(" PATH LIKE '%"+role.getStr("ROLEID")+"%' OR");
				}
				whereSqlBuffer.delete(whereSqlBuffer.length()-2, whereSqlBuffer.length());
			}else{
				String[] roleIds=new String[roles.size()];
				for(Integer i=0;i<roles.size();i++){
					roleIds[i]=roles.get(i).getStr("ROLEID");
				}
				whereSqlBuffer.append(" ROLEID IN ("+StringUtil.buildArrayToString(roleIds)+")");
			}
		}else{
			whereSqlBuffer.append(" 1!=1");
		}
		dynaBean.set(BeanUtils.KEY_WHERE, whereSql+" AND ROLETYPE='ROLE' AND STATUS='1' AND ("+whereSqlBuffer.toString()+")");
		if(StringUtil.isNotEmpty(queryFields)){
			dynaBean.set(BeanUtils.KEY_QUERY_FIELDS, queryFields);
		}
		List<DynaBean> lists=serviceTemplate.selectList(dynaBean);
		return lists;
	}

	/**
	 * 统计角色数量
	 * @param roleIds
	 * @param whereSql 人员查询条件
	 * @param cascade 是否级联
	 * @return
	 */
	@Override
	public Long countRoleById(String roleIds, String whereSql, Boolean cascade) {
		// TODO Auto-generated method stub
		DynaBean dynaBean=new DynaBean("JE_CORE_ROLE",false);
		dynaBean.set(BeanUtils.KEY_PK_CODE, "ROLEID");
		StringBuffer whereSqlBuffer=new StringBuffer();
		if(StringUtil.isEmpty(roleIds)){
			whereSqlBuffer.append(" 1!=1");
		}else{
			if(cascade){
				for(String roleId:roleIds.split(",")){
					whereSqlBuffer.append(" PATH LIKE '%"+roleId+"%' OR");
				}
				whereSqlBuffer.delete(whereSqlBuffer.length()-2, whereSqlBuffer.length());
			}else{
				whereSqlBuffer.append(" ROLEID IN ("+StringUtil.buildArrayToString(roleIds.split(","))+")");
			}
		}
		dynaBean.set(BeanUtils.KEY_WHERE, whereSql+" AND STATUS='1' AND ROLETYPE='ROLE' AND ("+whereSqlBuffer.toString()+")");
		Long count=serviceTemplate.selectCount(dynaBean);
		return count;
	}

	@Override
	public Long countRoleByCode(String roleCodes, String whereSql,Boolean cascade) {
		// TODO Auto-generated method stub
		List<DynaBean> roles=serviceTemplate.selectList("JE_CORE_ROLE", " AND ROLETYPE='ROLE' AND ROLECODE IN ("+StringUtil.buildArrayToString(roleCodes.split(","))+")");
		DynaBean dynaBean=new DynaBean("JE_CORE_ROLE",false);
		dynaBean.set(BeanUtils.KEY_PK_CODE, "ROLEID");
		StringBuffer whereSqlBuffer=new StringBuffer();
		if(roles.size()>0){
			if(cascade){
				for(DynaBean role:roles){
					whereSqlBuffer.append(" PATH LIKE '%"+role.getStr("ROLEID")+"%' OR");
				}
				whereSqlBuffer.delete(whereSqlBuffer.length()-2, whereSqlBuffer.length());
			}else{
				return new Long(roles.size());
			}
		}else{
			whereSqlBuffer.append(" 1!=1");
		}
		dynaBean.set(BeanUtils.KEY_WHERE, whereSql+" AND STATUS='1' AND ROLETYPE='ROLE' AND ("+whereSqlBuffer.toString()+")");
		Long count=serviceTemplate.selectCount(dynaBean);
		return count;
	}

	/**
	 * 根据岗位id查询该岗位及子岗位
	 * @param sentryIds 岗位主键
	 * @param whereSql  查询条件
	 * @param cascade 是否级联
	 * @param queryFields 查询字段   按逗号隔开，如果传空则查询全部字段
	 * @return
	 */
	@Override
	public List<DynaBean> querySentryById(String sentryIds, String whereSql,Boolean cascade, String queryFields) {
		// TODO Auto-generated method stub
		DynaBean dynaBean=new DynaBean("JE_CORE_SENTRY",false);
		dynaBean.set(BeanUtils.KEY_PK_CODE, "SENTRYID");
		StringBuffer whereSqlBuffer=new StringBuffer();
		if(StringUtil.isEmpty(sentryIds)){
			whereSqlBuffer.append(" 1!=1");
		}else{
			if(cascade){
				for(String sentryId:sentryIds.split(",")){
					whereSqlBuffer.append(" PATH LIKE '%"+sentryId+"%' OR");
				}
				whereSqlBuffer.delete(whereSqlBuffer.length()-2, whereSqlBuffer.length());
			}else{
				whereSqlBuffer.append(" SENTRYID IN ("+StringUtil.buildArrayToString(sentryIds.split(","))+")");
			}
		}
		dynaBean.set(BeanUtils.KEY_WHERE, whereSql+" AND STATUS='1' AND ("+whereSqlBuffer.toString()+")");
		if(StringUtil.isNotEmpty(queryFields)){
			dynaBean.set(BeanUtils.KEY_QUERY_FIELDS, queryFields);
		}
		List<DynaBean> lists=serviceTemplate.selectList(dynaBean);
		return lists;
	}

	/**
	 * 根据岗位编码查询该岗位及子岗位
	 * @param sentryCodes 岗位编码
	 * @param whereSql 查询条件
	 * @param cascade 是否级联
	 * @param queryFields 查询字段   按逗号隔开，如果传空则查询全部字段
	 * @return
	 */
	@Override
	public List<DynaBean> querySentryByCode(String sentryCodes, String whereSql,Boolean cascade, String queryFields) {
		// TODO Auto-generated method stub
		List<DynaBean> sentrys=serviceTemplate.selectList("JE_CORE_ROLE", " AND SENTRYCODE IN ("+StringUtil.buildArrayToString(sentryCodes.split(","))+")");
		DynaBean dynaBean=new DynaBean("JE_CORE_SENTRY",false);
		dynaBean.set(BeanUtils.KEY_PK_CODE, "SENTRYID");
		StringBuffer whereSqlBuffer=new StringBuffer();
		if(sentrys.size()>0){
			if(cascade){
				for(DynaBean sentry:sentrys){
					whereSqlBuffer.append(" PATH LIKE '%"+sentry.getStr("SENTRYID")+"%' OR");
				}
				whereSqlBuffer.delete(whereSqlBuffer.length()-2, whereSqlBuffer.length());
			}else{
				String[] sentryIds=new String[sentrys.size()];
				for(Integer i=0;i<sentrys.size();i++){
					sentryIds[i]=sentrys.get(i).getStr("SENTRYID");
				}
				whereSqlBuffer.append(" SENTRYID IN ("+StringUtil.buildArrayToString(sentryIds)+")");
			}
		}else{
			whereSqlBuffer.append(" 1!=1");
		}
		dynaBean.set(BeanUtils.KEY_WHERE, whereSql+" AND STATUS='1' AND ("+whereSqlBuffer.toString()+")");
		if(StringUtil.isNotEmpty(queryFields)){
			dynaBean.set(BeanUtils.KEY_QUERY_FIELDS, queryFields);
		}
		List<DynaBean> lists=serviceTemplate.selectList(dynaBean);
		return lists;
	}

	/**
	 * 统计岗位数量
	 * @param sentryIds
	 * @param whereSql 人员查询条件
	 * @param cascade 是否级联
	 * @return
	 */
	@Override
	public Long countSentryById(String sentryIds, String whereSql, Boolean cascade) {
		// TODO Auto-generated method stub
		DynaBean dynaBean=new DynaBean("JE_CORE_SENTRY",false);
		dynaBean.set(BeanUtils.KEY_PK_CODE, "SENTRYID");
		StringBuffer whereSqlBuffer=new StringBuffer();
		if(StringUtil.isEmpty(sentryIds)){
			whereSqlBuffer.append(" 1!=1");
		}else{
			if(cascade){
				for(String sentryId:sentryIds.split(",")){
					whereSqlBuffer.append(" PATH LIKE '%"+sentryId+"%' OR");
				}
				whereSqlBuffer.delete(whereSqlBuffer.length()-2, whereSqlBuffer.length());
			}else{
				whereSqlBuffer.append(" SENTRYID IN ("+StringUtil.buildArrayToString(sentryIds.split(","))+")");
			}
		}
		dynaBean.set(BeanUtils.KEY_WHERE, whereSql+" AND STATUS='1' AND ("+whereSqlBuffer.toString()+")");
		Long count=serviceTemplate.selectCount(dynaBean);
		return count;
	}

	/**
	 * 统计岗位数量
	 * @param sentryCodes TODO 暂不明确
	 * @param whereSql 人员查询条件
	 * @param cascade 是否级联
	 * @return
	 */
	@Override
	public Long countSentryByCode(String sentryCodes, String whereSql,Boolean cascade) {
		// TODO Auto-generated method stub
		List<DynaBean> sentrys=serviceTemplate.selectList("JE_CORE_SENTRY", " AND SENTRYCODE IN ("+StringUtil.buildArrayToString(sentryCodes.split(","))+")");
		DynaBean dynaBean=new DynaBean("JE_CORE_SENTRY",false);
		dynaBean.set(BeanUtils.KEY_PK_CODE, "SENTRYID");
		StringBuffer whereSqlBuffer=new StringBuffer();
		if(sentrys.size()>0){
			if(cascade){
				for(DynaBean sentry:sentrys){
					whereSqlBuffer.append(" PATH LIKE '%"+sentry.getStr("SENTRYID")+"%' OR");
				}
				whereSqlBuffer.delete(whereSqlBuffer.length()-2, whereSqlBuffer.length());
			}else{
				return new Long(sentrys.size());
			}
		}else{
			whereSqlBuffer.append(" 1!=1");
		}
		dynaBean.set(BeanUtils.KEY_WHERE, whereSql+" AND STATUS='1' AND ("+whereSqlBuffer.toString()+")");
		Long count=serviceTemplate.selectCount(dynaBean);
		return count;
	}

	/**
	 * 查询出用户树形
	 * @param ddInfos TODO 暂不明确
	 * @param onlyItem TODO 暂不明确
	 * @param whereSql 查询sql
	 * @return
	 */
	@Override
	public List queryAppUser(JSONArray ddInfos,Boolean onlyItem,String whereSql) {
		// TODO Auto-generated method stub
		List array=new ArrayList();
		//查找所有用户信息构建程map
		List<DynaBean> users=serviceTemplate.selectList("JE_CORE_ENDUSER", whereSql);
		Map<String,HashMap> userInfos=new HashMap<String,HashMap>();
		for(DynaBean user:users){
			userInfos.put(user.getStr("USERID"), user.getValues());
		}
		for(int i = 0;i<ddInfos.size();i++){
			JSONObject obj=ddInfos.getJSONObject(i);
			String ddCode = obj.getString("ddCode");
			String ddName = obj.getString("ddName");
			String rootId = obj.getString("rootId");
			String nodeInfo = obj.getString("nodeInfo");
			String dataCache="0";
			String querySql = StringUtil.isEmpty(obj.getString("whereSql"))?"":obj.getString("whereSql");
			String orderSql = StringUtil.isEmpty(obj.getString("orderSql"))?"":obj.getString("orderSql");
			if(obj.containsKey("params")){
				JSONObject varObj=obj.getJSONObject("params");
				if(varObj.containsKey("dataCache")){
					dataCache=varObj.getString("dataCache");
				}
			}
			DynaBean dictionary=DicInfoCacheManager.getCacheValue(ddCode);
			if(dictionary==null){
				dictionary=serviceTemplate.selectOne("JE_CORE_DICTIONARY", " and DICTIONARY_DDCODE='"+ddCode+"'");
			}
			List<JSONTreeNode> jsonTreeNodeList;
			JSONTreeNode emptyRoot=new JSONTreeNode();
			emptyRoot.setText(ddName);
			emptyRoot.setParent("ROOT");
			emptyRoot.setId("ROOT_" + nodeInfo);// + rootNode.getId());
			emptyRoot.setIconCls("jeicon jeicon-tree-query");
			emptyRoot.setNodeInfo(nodeInfo);
			if(dictionary==null){
				array.add(emptyRoot);
				System.out.println("未找到数据字典："+ddCode+"!");
				continue;
			}
			String ddType=dictionary.getStr("DICTIONARY_DDTYPE");
			String ddWhereSql=dictionary.getStr("DICTIONARY_WHERESQL","");
			String ddOrderSql=dictionary.getStr("DICTIONARY_ORDERSQL","");
			String tableName=dictionary.getStr("DICTIONARY_CLASSNAME");
			if(StringUtil.isEmpty(rootId)){
				rootId=ConstantVars.TREE_ROOT;
			}
			//声明变量集合，用于解析whereSql的通配符
			Set<Entry> ddSet=new HashSet<Entry>();
			//加入登录信息
			EndUser currentUser=SecurityUserHolder.getCurrentUser();
			ddSet.addAll(SecurityUserHolder.getCurrentInfo(currentUser).entrySet());
			//加入用户变量
			ddSet.addAll(FrontCacheManager.getCacheValues().entrySet());
			ddSet.addAll(BackCacheManager.getCacheValues().entrySet());
			//加入系统设置
			ddSet.addAll(WebUtils.getAllSysVar().entrySet());
			String key="";//树形缓存键值
			if("1".equals(dataCache)){
				if(!DDType.LIST.equalsIgnoreCase(ddType)){
					String[] keys=new String[]{ddCode,querySql,ddWhereSql};
					Arrays.sort(keys);
					String keyStr=StringUtil.buildSplitString(keys, "_");
					Md5PasswordEncoder md5=new Md5PasswordEncoder();
					key=md5.encodePassword(keyStr, null);
					JSONTreeNode node=DicCacheManager.getTreeCacheValue(key);
					if(node!=null){
						if(onlyItem){
							array.addAll(node.getChildren());
						}else{
							array.add(node);
						}
						continue;
					}
				}
			}
			DynaBean table=BeanUtils.getInstance().getResourceTable(tableName);
			if(table==null){
				array.add(emptyRoot);
				continue;
			}
			List<DynaBean> columns=(List<DynaBean>) table.get(BeanUtils.KEY_TABLE_COLUMNS);
			JSONTreeNode template = BeanUtils.getInstance().buildJSONTreeNodeTemplate(columns);
			String[] rbacTables=new String[]{"JE_CORE_DEPARTMENT","JE_CORE_SENTRY","JE_CORE_ROLE","JE_CORE_VDEPTUSER","JE_CORE_VROLEUSER","JE_CORE_VSENTRYUSER"};
			if(ArrayUtils.contains(rbacTables, tableName) && "1".equals(WebUtils.getSysVar("JE_SYS_COMPANYWHERE"))){
				String pkCode="";
				String sql="";
				if("JE_CORE_DEPARTMENT".equals(tableName)){
					pkCode="DEPTID";
				}else if("JE_CORE_SENTRY".equals(tableName)){
					pkCode="SENTRYID";
				}else if("JE_CORE_ROLE".equals(tableName)){
					pkCode="ROLEID";
				}else if("JE_CORE_VDEPTUSER".equals(tableName)){
					pkCode="ID";
					sql=" AND NODEINFO!='0' ";
				}else if("JE_CORE_VROLEUSER".equals(tableName)){
					pkCode="ID";
					sql=" AND NODEINFO!='ENDUSER' ";
				}else if("JE_CORE_VSENTRYUSER".equals(tableName)){
					pkCode="ID";
					sql=" AND NODEINFO!='ENDUSER' ";
				}
				if(StringUtil.isNotEmpty(ddWhereSql)){
					sql+=ddWhereSql;
				}
				//Saas模式
				if(WebUtils.isSaas()){
					sql+=" AND ZHID='"+currentUser.getZhId()+"'";
				}
				List<DynaBean> lists=serviceTemplate.selectList(tableName, querySql+sql);
				Set<String> parentIds=new HashSet<String>();
				int layer=-1;
				Boolean flag=false;
				for(DynaBean bean:lists){
					String pkValue=bean.getStr(bean.getStr(BeanUtils.KEY_PK_CODE));
					String parent=bean.getStr("PARENT");
					if(NodeType.ROOT.equalsIgnoreCase(pkValue)){
						continue;
					}
					if(NodeType.ROOT.equalsIgnoreCase(parent)){
//						flag=true;
					}
					if(flag!=true){
						String path=bean.getStr("PATH");
						String[] ids=path.split("/");
						if(layer==-1){
							layer=ids.length;
							for(String id:ids){
								if(StringUtil.isEmpty(id) || pkValue.equals(id) || NodeType.ROOT.equalsIgnoreCase(id)){

								}else{
									parentIds.add(id);
								}
							}
						}
//						if(ids.length<layer){
						for(String id:ids){
							if(StringUtil.isEmpty(id) || pkValue.equals(id) || NodeType.ROOT.equalsIgnoreCase(id)){

							}else{
								parentIds.add(id);
							}
						}
//						}
					}
				}
				if(!flag && parentIds.size()>0){
					if(StringUtil.isNotEmpty(querySql)){
						querySql=" AND ((1=1 "+querySql+") OR "+pkCode+" IN ("+StringUtil.buildArrayToString(ArrayUtils.getArray(parentIds))+"))";
					}
				}
			}
			if(StringUtil.isNotEmpty(ddWhereSql)){
				querySql+=ddWhereSql;
			}
			if(StringUtil.isEmpty(orderSql) && StringUtil.isNotEmpty(ddOrderSql)){
				orderSql=ddOrderSql;
			}
			//过滤平台核心
			String[] tables= JepfCacheManager.getCacheValue("SY_JECORETABLES").split(",");
			if(ArrayUtils.contains(tables, tableName)){
				if("1".equals(WebUtils.getSysVar("SY_JECORE"))){
					if(querySql.indexOf("SY_JESYS='1'")!=-1){
						querySql=querySql.replace("SY_JESYS='1'", "(SY_JESYS='1' OR (SY_JECORE!='1' OR SY_JECORE IS NULL))");
					}else{
						querySql+=" AND (SY_JECORE!='1' OR SY_JECORE IS NULL)";
					}
				}else if(querySql.indexOf("SY_JESYS='1'")!=-1){
					querySql=querySql.replace("SY_JESYS='1'", "1=1");
				}
			}
			QueryInfo queryInfo = new QueryInfo();
			if(StringUtil.isNotEmpty(querySql)){
				querySql=StringUtil.parseKeyWord(querySql, ddSet);
			}
			queryInfo.setWhereSql(querySql);
			if(StringUtil.isNotEmpty(orderSql)){
				orderSql=StringUtil.parseKeyWord(orderSql, ddSet);
			}
			queryInfo.setOrderSql(orderSql);
			jsonTreeNodeList = pcServiceTemplate.getJsonTreeNodeList(rootId, tableName, template, queryInfo);
			List<JSONTreeNode> lists=new ArrayList<JSONTreeNode>();
			for(JSONTreeNode node : jsonTreeNodeList) {
				Boolean add=false;
				if(node.getId().equals(rootId)){
					node.setText(ddName);
					node.setIconCls("jeicon jeicon-tree-query");
					node.setParent("ROOT");
					node.setDisabled("1");
					node.setNodeInfo(tableName);
					if(StringUtil.isNotEmpty(nodeInfo)){
						node.setNodeInfo(nodeInfo);
						node.setId(node.getId()+"_"+nodeInfo);
					}
					add=true;
				}else{
					//构建用户bean
					if("JE_CORE_VDEPTUSER".equals(tableName) && "0".equals(node.getNodeInfo())){
						String userId=node.getId();
						HashMap u=userInfos.get(userId);
						if(u!=null){
							node.setBean(u);
							add=true;
						}
						node.setBean(userInfos.get(userId));
					}else if("JE_CORE_VROLEUSER".equals(tableName) && "ENDUSER".equals(node.getNodeInfo())){
						String userId=node.getBean().get("USER_ID")+"";
						if(StringUtil.isNotEmpty(userId)){
							HashMap u=userInfos.get(userId);
							if(u!=null){
								node.setBean(u);
								add=true;
							}
						}
					}else if("JE_CORE_VSENTRYUSER".equals(tableName)  && "ENDUSER".equals(node.getNodeInfo())){
						String userId=node.getBean().get("USER_ID")+"";
						if(StringUtil.isNotEmpty(userId)){
							HashMap u=userInfos.get(userId);
							if(u!=null){
								node.setBean(u);
								add=true;
							}
						}
					}else{
						add=true;
					}
					if(StringUtil.isNotEmpty(nodeInfo)){
						node.setNodeInfo(nodeInfo);
						node.setId(node.getId()+"_"+nodeInfo);
						node.setParent(node.getParent()+"_"+nodeInfo);
					}
					add=true;
				}
				if(add){
					lists.add(node);
				}
			}
			if(StringUtil.isNotEmpty(nodeInfo)){
				rootId=rootId+"_"+nodeInfo;
			}
			JSONTreeNode rootNode = pcServiceTemplate.buildJSONNewTree(lists,rootId);
			rootNode.setText(ddName);
			rootNode.setParent("ROOT");
//			rootNode.setId("ROOT_" + nodeInfo);// + rootNode.getId());
			rootNode.setIconCls("jeicon jeicon-tree-query");
			if("1".equals(dataCache) && !DDType.LIST.equalsIgnoreCase(ddType)){
				DicCacheManager.putTreeCache(key, rootNode);
			}
			if(onlyItem){
				array.addAll(rootNode.getChildren());
			}else{
				array.add(rootNode);
			}
		}
		return array;
	}
	@Resource(name="PCDynaServiceTemplate")
	public void setServiceTemplate(PCDynaServiceTemplate serviceTemplate) {
		this.serviceTemplate = serviceTemplate;
	}
	@Resource(name="PCServiceTemplateImpl")
	public void setPcServiceTemplate(PCServiceTemplate pcServiceTemplate) {
		this.pcServiceTemplate = pcServiceTemplate;
	}

}
