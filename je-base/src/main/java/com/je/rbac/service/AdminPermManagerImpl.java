package com.je.rbac.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.je.core.constants.ConstantVars;
import com.je.core.constants.rbac.AdminPermType;
import com.je.core.constants.rbac.AdminType;
import com.je.core.constants.rbac.AuthorPermType;
import com.je.core.constants.tree.NodeType;
import com.je.core.dao.PCDaoTemplateImpl;
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
import com.je.rbac.model.AdminPermInfo;
import com.je.rbac.model.EndUser;
@Component("adminPermManager")
public class AdminPermManagerImpl implements AdminPermManager {
	@Autowired
	private PermissionManager permissionManager;
	@Autowired
	private PCDynaServiceTemplate serviceTemplate;
	@Autowired
	private PCServiceTemplate pcServiceTemplate;

	/**
	 * 获取权限树
	 * @return
	 */
	@Override
	public JSONTreeNode getPermTree() {
		// TODO Auto-generated method stub
		QueryInfo queryInfo=new QueryInfo();
		String whereSql="";
		EndUser currentUser=SecurityUserHolder.getCurrentUser();
//		if(!"1".equals(WebUtils.sysVar.get("JE_SYS_APPPERM"))){
//			whereSql+=" AND JE_CORE_MENU_ID not in('1hyQLMxEmeBD5T1zl1D','SydioeslUCIjNFbO0Hn') ";
//		}
		if(StringUtil.getDefaultValue(WebUtils.getSysVar("JE_SYS_ADMIN"),"admin").indexOf(currentUser.getUserCode())==-1 && StringUtil.isNotEmpty(WebUtils.getSysVar("JE_CORE_ADMINMENU_ID"))){
			whereSql+=" AND JE_CORE_MENU_ID NOT IN ("+StringUtil.buildArrayToString(WebUtils.getSysVar("JE_CORE_ADMINMENU_ID").split(","))+")";
		}
		queryInfo.setWhereSql(whereSql);
		Set<String> nodeIds=null;
		if(WebUtils.isSaas()){
			if(currentUser.getSaas()){

			}else{
//				DynaBean yh=serviceTemplate.selectOne("JE_SAAS_YH", " AND SY_JTGSID='"+currentUser.getJtgsId()+"'");
				nodeIds=new HashSet<String>();
//				String cpSql=" AND CPYHZY_CPID IN (SELECT JE_SAAS_CP_ID FROM JE_SAAS_CPYH WHERE 1=1 "+WebUtils.getCpYhWhere()+" AND JE_SAAS_YH_ID='"+yh.getStr("JE_SAAS_YH_ID")+"') ";
				if(StringUtil.isNotEmpty(currentUser.getZhId())){
					List<DynaBean> perms=serviceTemplate.selectList("JE_SAAS_CPYHZY", " AND CPYHZY_TYPE='YH' AND CPYHZY_ZHID='"+currentUser.getZhId()+"' AND CPYHZY_CPID IN (SELECT JE_SAAS_CP_ID FROM JE_SAAS_CPYH WHERE 1=1 "+WebUtils.getCpYhWhere()+" AND JE_SAAS_YH_ID='"+currentUser.getZhId()+"')");
					for(DynaBean perm:perms){
						nodeIds.add(perm.getStr("CPYHZY_ZYID"));
					}
				}
			}
		}
		return permissionManager.buildPermTree(NodeType.ROOT, new ArrayList<DynaBean>(), AuthorPermType.PERM_ROLE, false, true, true,false, queryInfo,null,nodeIds,"",false,false);
	}

	/**
	 * 获取管理员权限
	 * @param adminQueryInfo 去要查询管理员的信息
	 * @return
	 */
	@Override
	public List<DynaBean> getAdminPermRes(String adminQueryInfo) {
		// TODO Auto-generated method stub
		List<DynaBean> perms=serviceTemplate.selectList("JE_CORE_ADMINPERM", " AND JE_CORE_ADMINPERM_ID IN (SELECT PERID FROM JE_CORE_ADMIN_PERM WHERE ADMINID IN ("+adminQueryInfo+"))");
		return perms;
	}
	/**
	 * 更新权限
	 * @param adminInfo 管理员信息
	 * @param delPermIds 删除人员id
	 * @param addPermIds 添加人员id
	 */
	@Override
	public void doUpdatePerms(DynaBean adminInfo, String delPermIds,String addPermIds) {
		// TODO Auto-generated method stub
		String[] permIds=addPermIds.split(",");
		String adminInfoId=adminInfo.getStr("JE_CORE_ADMININFO_ID");
		if(StringUtil.isNotEmpty(addPermIds)){
			for(String permId:permIds){
				String[] permArray=permId.split("#");
				String resId=permArray[0];
				String resType=permArray[1];
				DynaBean permInfo=serviceTemplate.selectOne("JE_CORE_ADMINPERM", " and ADMINPERM_TYPE='"+resType+"' and ADMINPERM_RESID='"+resId+"'");
				if(permInfo==null){
					permInfo=new DynaBean("JE_CORE_ADMINPERM",false);
					permInfo.set(BeanUtils.KEY_PK_CODE, "JE_CORE_ADMINPERM_ID");
					permInfo.set("ADMINPERM_TYPE", resType);
					permInfo.set("ADMINPERM_RESID", resId);
					permInfo=serviceTemplate.insert(permInfo);
				}
				DynaBean adminPerm=new DynaBean("JE_CORE_ADMIN_PERM",false);
				adminPerm.set("ADMINID", adminInfoId);
				adminPerm.set("PERID", permInfo.getStr("JE_CORE_ADMINPERM_ID"));
				adminPerm.set("TYPE", "ADMIN");
				serviceTemplate.insert(adminPerm);
			}
		}
		if(StringUtil.isNotEmpty(delPermIds)){
			if(ConstantVars.STR_ORACLE.equals(PCDaoTemplateImpl.DBNAME)){
				pcServiceTemplate.executeSql("DELETE FROM JE_CORE_ADMIN_PERM WHERE ADMINID='"+adminInfoId+"' AND PERID IN (SELECT JE_CORE_ADMINPERM_ID FROM JE_CORE_ADMINPERM WHERE (ADMINPERM_RESID || '#' || ADMINPERM_TYPE) IN ("+StringUtil.buildArrayToString(delPermIds.split(","))+"))");
			}else if(ConstantVars.STR_MYSQL.equals(PCDaoTemplateImpl.DBNAME)){
				pcServiceTemplate.executeSql("DELETE FROM JE_CORE_ADMIN_PERM WHERE ADMINID='"+adminInfoId+"' AND PERID IN (SELECT JE_CORE_ADMINPERM_ID FROM JE_CORE_ADMINPERM WHERE CONCAT(ADMINPERM_RESID,'#',ADMINPERM_TYPE) IN ("+StringUtil.buildArrayToString(delPermIds.split(","))+"))");
			}else{
				pcServiceTemplate.executeSql("DELETE FROM JE_CORE_ADMIN_PERM WHERE ADMINID='"+adminInfoId+"' AND PERID IN (SELECT JE_CORE_ADMINPERM_ID FROM JE_CORE_ADMINPERM WHERE (ADMINPERM_RESID+'#'+ADMINPERM_TYPE) IN ("+StringUtil.buildArrayToString(delPermIds.split(","))+"))");
			}
		}
	}

	/**
	 * 导入人员
	 * @param ids 多个id
	 * @param pkValue 主键
	 * @return
	 */
	@Override
	public Integer implUsers(String ids, String pkValue) {
		// TODO Auto-generated method stub
		pcServiceTemplate.executeSql(" DELETE FROM JE_CORE_ADMIN_USER WHERE ADMINID='"+pkValue+"'");
		String[] idArray=ids.split(",");
		for(String id:idArray){
			DynaBean adminUser=new DynaBean("JE_CORE_ADMIN_USER",false);
			adminUser.set("USERID", id);
			adminUser.set("ADMINID", pkValue);
			serviceTemplate.insert(adminUser);
		}
		return idArray.length;
	}

	/**
	 * 移除人员
	 * @param ids 多个id
	 * @param pkValue 主键
	 * @return
	 */
	@Override
	public Integer delUsers(String ids, String pkValue) {
		// TODO Auto-generated method stub
		String[] idArray=ids.split(",");
		pcServiceTemplate.executeSql(" DELETE FROM JE_CORE_ADMIN_USER WHERE ADMINID='"+pkValue+"' AND USERID IN ("+StringUtil.buildArrayToString(idArray)+")");
		return idArray.length;
	}

	/**
	 * 获取用户可授权权限
	 * @param userId 用户id
	 * @param whereSql 查询条件sql
	 * @return
	 */
	@Override
	public Set<String> getUserPerms(String userId, String whereSql) {
		// TODO Auto-generated method stub
		Set<String> includes=new HashSet<String>();
		List<DynaBean> permInfos=serviceTemplate.selectList("JE_CORE_ADMINPERM", whereSql+" AND JE_CORE_ADMINPERM_ID IN (SELECT PERID FROM JE_CORE_ADMIN_PERM WHERE ADMINID IN (SELECT ADMINID FROM JE_CORE_ADMIN_USER WHERE USERID='"+userId+"'))");
		for(DynaBean permInfo:permInfos){
			includes.add(permInfo.getStr("ADMINPERM_RESID")+"#"+permInfo.getStr("ADMINPERM_TYPE"));
		}
		return includes;
	}

	/**
	 * 获取角色权限
	 * @param userId 用户id
	 * @param whereSql 查询条件sql
	 * @return
	 */
	@Override
	public Set<String> getUserRolePerms(String userId, String whereSql) {
		// TODO Auto-generated method stub
		Set<String> includes=new HashSet<String>();
		List<DynaBean> roleInfos=serviceTemplate.selectList("JE_CORE_ROLE"," AND ROLEID IN (SELECT ADMINPERM_RESID FROM JE_CORE_ADMINPERM WHERE 1=1 "+whereSql+" AND JE_CORE_ADMINPERM_ID IN (SELECT PERID FROM JE_CORE_ADMIN_PERM WHERE ADMINID IN (SELECT ADMINID FROM JE_CORE_ADMIN_USER WHERE USERID='"+userId+"')))");
		for(DynaBean roleInfo:roleInfos){
			includes.add(roleInfo.getStr("ROLEID"));
		}
		List<DynaBean> roleUpdateInfos=serviceTemplate.selectList("JE_CORE_ROLE"," AND ROLEID IN (SELECT ADMINPERM_RESID FROM JE_CORE_ADMINPERM WHERE 1=1 "+whereSql+" AND JE_CORE_ADMINPERM_ID IN (SELECT PERID FROM JE_CORE_ADMIN_PERM WHERE TYPE='"+AdminType.SELF+"' AND ADMINID IN (SELECT ADMINID FROM JE_CORE_ADMIN_USER WHERE USERID='"+userId+"')))");
		for(DynaBean roleUpdateInfo:roleUpdateInfos){
			includes.add(roleUpdateInfo.getStr("ROLEID")+"#"+AdminType.SELF);
		}
		return includes;
	}

	/**
	 * 获取用户可授权权限
	 * @param userId 用户id
	 * @param whereSql 查询条件id
	 * @return
	 */
	@Override
	public Set<String> getUserDeptPerms(String userId, String whereSql) {
		// TODO Auto-generated method stub
		Set<String> includes=new HashSet<String>();
		List<DynaBean> deptInfos=serviceTemplate.selectList("JE_CORE_DEPARTMENT"," AND DEPTID IN (SELECT DEPTID FROM JE_CORE_ROLE WHERE ROLETYPE='DEPT' AND ROLEID IN (SELECT ADMINPERM_RESID FROM JE_CORE_ADMINPERM WHERE 1=1 "+whereSql+" AND JE_CORE_ADMINPERM_ID IN (SELECT PERID FROM JE_CORE_ADMIN_PERM WHERE ADMINID IN (SELECT ADMINID FROM JE_CORE_ADMIN_USER WHERE USERID='"+userId+"'))))");
		for(DynaBean deptInfo:deptInfos){
			includes.add(deptInfo.getStr("DEPTID"));
		}
		List<DynaBean> deptUpdateInfos=serviceTemplate.selectList("JE_CORE_DEPARTMENT"," AND DEPTID IN (SELECT DEPTID FROM JE_CORE_ROLE WHERE ROLETYPE='DEPT' AND ROLEID IN (SELECT ADMINPERM_RESID FROM JE_CORE_ADMINPERM WHERE 1=1 "+whereSql+" AND JE_CORE_ADMINPERM_ID IN (SELECT PERID FROM JE_CORE_ADMIN_PERM WHERE  TYPE='"+AdminType.SELF+"' AND ADMINID IN (SELECT ADMINID FROM JE_CORE_ADMIN_USER WHERE USERID='"+userId+"'))))");
		for(DynaBean deptUpdateInfo:deptUpdateInfos){
			includes.add(deptUpdateInfo.getStr("DEPTID")+"#"+AdminType.SELF);
		}
		return includes;
	}

	/**
	 * 获取用户可授权权限
	 * @param userId 用户id
	 * @param whereSql 查询条件id
	 * @return
	 */
	@Override
	public Set<String> getUserSentryPerms(String userId, String whereSql) {
		// TODO Auto-generated method stub
		Set<String> includes=new HashSet<String>();
		List<DynaBean> sentryInfos=serviceTemplate.selectList("JE_CORE_SENTRY"," AND SENTRYID IN (SELECT SENTRYID FROM JE_CORE_ROLE WHERE ROLETYPE='SENTRY' AND ROLEID IN (SELECT ADMINPERM_RESID FROM JE_CORE_ADMINPERM WHERE 1=1 "+whereSql+" AND JE_CORE_ADMINPERM_ID IN (SELECT PERID FROM JE_CORE_ADMIN_PERM WHERE ADMINID IN (SELECT ADMINID FROM JE_CORE_ADMIN_USER WHERE USERID='"+userId+"'))))");
		for(DynaBean sentryInfo:sentryInfos){
			includes.add(sentryInfo.getStr("SENTRYID"));
		}
		List<DynaBean> sentryUpdateInfos=serviceTemplate.selectList("JE_CORE_SENTRY"," AND SENTRYID IN (SELECT SENTRYID FROM JE_CORE_ROLE WHERE ROLETYPE='SENTRY' AND ROLEID IN (SELECT ADMINPERM_RESID FROM JE_CORE_ADMINPERM WHERE 1=1 "+whereSql+" AND JE_CORE_ADMINPERM_ID IN (SELECT PERID FROM JE_CORE_ADMIN_PERM WHERE  TYPE='"+AdminType.SELF+"' AND ADMINID IN (SELECT ADMINID FROM JE_CORE_ADMIN_USER WHERE USERID='"+userId+"'))))");
		for(DynaBean sentryUpdateInfo:sentryUpdateInfos){
			includes.add(sentryUpdateInfo.getStr("SENTRYID")+"#"+AdminType.SELF);
		}
		return includes;
	}

	/**
	 * 添加子的权限
	 * @param currentUser 当前登陆人信息
	 * @param id
	 * @param parentId TODO 暂不明确
	 * @param type 类型
	 */
	@Override
	public void addChildrenPerm(EndUser currentUser,String id,String parentId,String type){
		List<DynaBean> adminInfos=serviceTemplate.selectList("JE_CORE_ADMININFO", " AND JE_CORE_ADMININFO_ID IN (SELECT ADMINID FROM JE_CORE_ADMIN_USER WHERE USERID='"+currentUser.getUserId()+"')");
		List<DynaBean> adminPerms=serviceTemplate.selectList("JE_CORE_ADMINPERM", " AND ADMINPERM_RESID='"+parentId+"' AND ADMINPERM_TYPE IN ('"+type+"_PERM','"+type+"_SELECT','"+type+"_UPDATE')");
		Map<String,String> parentPerms=new HashMap<String,String>();
		for(DynaBean adminPerm:adminPerms){
			parentPerms.put(adminPerm.getStr("ADMINPERM_TYPE"), adminPerm.getStr("JE_CORE_ADMINPERM_ID"));
		}
		DynaBean selectPerm=new DynaBean("JE_CORE_ADMINPERM",false);
		selectPerm.set(BeanUtils.KEY_PK_CODE, "JE_CORE_ADMINPERM_ID");
		selectPerm.set("ADMINPERM_RESID", id);
		selectPerm.set("ADMINPERM_TYPE", type+"_SELECT");
		selectPerm=serviceTemplate.insert(selectPerm);

		DynaBean permPerm=new DynaBean("JE_CORE_ADMINPERM",false);
		permPerm.set(BeanUtils.KEY_PK_CODE, "JE_CORE_ADMINPERM_ID");
		permPerm.set("ADMINPERM_RESID", id);
		permPerm.set("ADMINPERM_TYPE", type+"_PERM");
		permPerm=serviceTemplate.insert(permPerm);

		DynaBean updatePerm=new DynaBean("JE_CORE_ADMINPERM",false);
		updatePerm.set(BeanUtils.KEY_PK_CODE, "JE_CORE_ADMINPERM_ID");
		updatePerm.set("ADMINPERM_RESID", id);
		updatePerm.set("ADMINPERM_TYPE", type+"_UPDATE");
		updatePerm=serviceTemplate.insert(updatePerm);
		for(DynaBean adminInfo:adminInfos){
			if(StringUtil.isNotEmpty(parentPerms.get(type+"_SELECT"))){
				long count=serviceTemplate.selectCount("JE_CORE_ADMIN_PERM", " AND ADMINID='"+adminInfo.getStr("JE_CORE_ADMININFO_ID")+"' AND PERID='"+parentPerms.get(type+"_SELECT")+"'");
				if(count>0){
					//添加权限
					DynaBean aps=new DynaBean("JE_CORE_ADMIN_PERM",false);
					aps.set("ADMINID", adminInfo.getStr("JE_CORE_ADMININFO_ID"));
					aps.set("PERID", selectPerm.getStr("JE_CORE_ADMINPERM_ID"));
					aps.set("TYPE", AdminType.SELF);
					serviceTemplate.insert(aps);
				}
			}
			if(StringUtil.isNotEmpty(parentPerms.get(type+"_PERM"))){
				long count=serviceTemplate.selectCount("JE_CORE_ADMIN_PERM", " AND ADMINID='"+adminInfo.getStr("JE_CORE_ADMININFO_ID")+"' AND PERID='"+parentPerms.get(type+"_PERM")+"'");
				if(count>0){
					//添加权限
					DynaBean aps=new DynaBean("JE_CORE_ADMIN_PERM",false);
					aps.set("ADMINID", adminInfo.getStr("JE_CORE_ADMININFO_ID"));
					aps.set("PERID", permPerm.getStr("JE_CORE_ADMINPERM_ID"));
					aps.set("TYPE", AdminType.SELF);
					serviceTemplate.insert(aps);
				}
			}
			if(StringUtil.isNotEmpty(parentPerms.get(type+"_UPDATE"))){
				long count=serviceTemplate.selectCount("JE_CORE_ADMIN_PERM", " AND ADMINID='"+adminInfo.getStr("JE_CORE_ADMININFO_ID")+"' AND PERID='"+parentPerms.get(type+"_UPDATE")+"'");
				if(count>0){
					//添加权限
					DynaBean aps=new DynaBean("JE_CORE_ADMIN_PERM",false);
					aps.set("ADMINID", adminInfo.getStr("JE_CORE_ADMININFO_ID"));
					aps.set("PERID", updatePerm.getStr("JE_CORE_ADMINPERM_ID"));
					aps.set("TYPE", AdminType.SELF);
					serviceTemplate.insert(aps);
				}
			}

		}
	}

	/**
	 * TODO 暂不明确
	 * @param currentUser
	 */
	@Override
	public void initAdminPerm(EndUser currentUser) {
		// TODO Auto-generated method stub
		//首先判断该用户是否是管理员
		long count=serviceTemplate.selectCount("JE_CORE_ADMIN_USER", " AND USERID='"+currentUser.getUserId()+"'");
		AdminPermInfo adminPermInfo=new AdminPermInfo();
		if(count>0){
			//权限
//			List<DynaBean> adminPerms=serviceTemplate.selectList("JE_CORE_ADMINPERM", "");
//			SELECT ADMINPERM_RESID FROM  WHERE ADMINPERM_TYPE='"+AdminPermType.DEPT_SELECT+"' AND JE_CORE_ADMINPERM_ID IN (SELECT PERID FROM JE_CORE_ADMIN_PERM WHERE ADMINID IN (SELECT ADMINID FROM JE_CORE_ADMIN_USER WHERE USERID='"+currentUser.getUserId()+"')))
			List<DynaBean> rolePerms=serviceTemplate.selectList("JE_CORE_ROLE", " AND ROLEID IN (SELECT ADMINPERM_RESID FROM JE_CORE_ADMINPERM WHERE ADMINPERM_TYPE IN ('"+AdminPermType.ROLE_PERM+"','"+AdminPermType.DEPT_PERM+"','"+AdminPermType.SENTRY_PERM+"') AND JE_CORE_ADMINPERM_ID IN (SELECT PERID FROM JE_CORE_ADMIN_PERM WHERE ADMINID IN (SELECT ADMINID FROM JE_CORE_ADMIN_USER WHERE USERID='"+currentUser.getUserId()+"')))");
			for(DynaBean role:rolePerms){
				if("DEPT".equals(role.getStr("ROLETYPE"))){
					adminPermInfo.getDeptPerms().add(role.getStr("ROLEID"));
				}else if("SENTRY".equals(role.getStr("ROLETYPE"))){
					adminPermInfo.getSentryPerms().add(role.getStr("ROLEID"));
				}else{
					adminPermInfo.getRolePerms().add(role.getStr("ROLEID"));
				}
			}
			List<DynaBean> roleSees=serviceTemplate.selectList("JE_CORE_ROLE", " AND ROLEID IN (SELECT ADMINPERM_RESID FROM JE_CORE_ADMINPERM WHERE ADMINPERM_TYPE IN ('"+AdminPermType.ROLE_SELECT+"','"+AdminPermType.DEPT_SELECT+"','"+AdminPermType.SENTRY_SELECT+"') AND JE_CORE_ADMINPERM_ID IN (SELECT PERID FROM JE_CORE_ADMIN_PERM WHERE ADMINID IN (SELECT ADMINID FROM JE_CORE_ADMIN_USER WHERE USERID='"+currentUser.getUserId()+"')))");
			for(DynaBean role:roleSees){
				if("DEPT".equals(role.getStr("ROLETYPE"))){
					adminPermInfo.getDeptSees().add(role.getStr("DEPTID"));
				}else if("SENTRY".equals(role.getStr("ROLETYPE"))){
					adminPermInfo.getSentrySees().add(role.getStr("SENTRYID"));
				}else{
					adminPermInfo.getRoleSees().add(role.getStr("ROLEID"));
				}
			}
			List<DynaBean> roleUpdates=serviceTemplate.selectList("JE_CORE_ROLE", " AND ROLEID IN (SELECT ADMINPERM_RESID FROM JE_CORE_ADMINPERM WHERE ADMINPERM_TYPE IN ('"+AdminPermType.ROLE_UPDATE+"','"+AdminPermType.DEPT_UPDATE+"','"+AdminPermType.SENTRY_UPDATE+"') AND JE_CORE_ADMINPERM_ID IN (SELECT PERID FROM JE_CORE_ADMIN_PERM WHERE ADMINID IN (SELECT ADMINID FROM JE_CORE_ADMIN_USER WHERE USERID='"+currentUser.getUserId()+"')))");
			for(DynaBean role:roleUpdates){
				if("DEPT".equals(role.getStr("ROLETYPE"))){
					adminPermInfo.getDeptUpdates().add(role.getStr("DEPTID"));
				}else if("SENTRY".equals(role.getStr("ROLETYPE"))){
					adminPermInfo.getSentryUpdates().add(role.getStr("SENTRYID"));
				}else{
					adminPermInfo.getRoleUpdates().add(role.getStr("ROLEID"));
				}
			}
		}
		currentUser.setAdminPermInfo(adminPermInfo);
	}

	/**
	 * 暂不明确
	 * @param tableCode
	 * @return
	 */
	@Override
	public String getQuerySql(String tableCode) {
		// TODO Auto-generated method stub
		String whereSql="";
		EndUser currentUser=SecurityUserHolder.getCurrentUser();
		if("1".equals(WebUtils.getSysVar("JE_SYS_ADMINPERM")) && !WebUtils.getSysVar("JE_SYS_ADMIN").equals(currentUser.getBackUserCode())){
			if("JE_CORE_DEPARTMENT".equals(tableCode)){
				whereSql=" AND DEPTID IN ("+StringUtil.buildArrayToString(ArrayUtils.getArray(currentUser.getAdminPermInfo().getDeptSees()))+")";
			}else if("JE_CORE_SENTRY".equals(tableCode)){
				whereSql=" AND SENTRYID IN ("+StringUtil.buildArrayToString(ArrayUtils.getArray(currentUser.getAdminPermInfo().getSentrySees()))+")";
			}else if("JE_CORE_ROLE".equals(tableCode)){
				whereSql=" AND ROLEID IN ("+StringUtil.buildArrayToString(ArrayUtils.getArray(currentUser.getAdminPermInfo().getRoleSees()))+")";
			}else if("JE_CORE_VDEPTUSER".equals(tableCode)){
				whereSql=" AND (NODEINFO='0' OR (NODEINFO='1' AND ID IN ("+StringUtil.buildArrayToString(ArrayUtils.getArray(currentUser.getAdminPermInfo().getDeptSees()))+")))";
			}else if("JE_CORE_VROLEUSER".equals(tableCode)){
				whereSql=" AND (NODEINFO='ENDUSER' OR (NODEINFO='ROLE' AND ID IN ("+StringUtil.buildArrayToString(ArrayUtils.getArray(currentUser.getAdminPermInfo().getRoleSees()))+")))";
			}else if("JE_CORE_VSENTRYUSER".equals(tableCode)){
				whereSql=" AND (NODEINFO='ENDUSER' OR (NODEINFO='SENTRY' AND ID IN ("+StringUtil.buildArrayToString(ArrayUtils.getArray(currentUser.getAdminPermInfo().getSentrySees()))+")))";
			}
		}
		return whereSql;
	}
	@Resource(name="permissionManager")
	public void setPermissionManager(PermissionManager permissionManager) {
		this.permissionManager = permissionManager;
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
