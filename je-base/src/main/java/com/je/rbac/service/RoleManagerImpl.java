package com.je.rbac.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.je.core.constants.rbac.AdminPermType;
import com.je.core.constants.rbac.PermExtendType;
import com.je.core.constants.tree.NodeType;
import com.je.core.service.PCDynaServiceTemplate;
import com.je.core.service.PCServiceTemplate;
import com.je.core.util.ArrayUtils;
import com.je.core.util.StringUtil;
import com.je.core.util.WebUtils;
import com.je.core.util.bean.BeanUtils;
import com.je.core.util.bean.DynaBean;
import com.je.rbac.node.RolePermNode;
import com.je.saas.service.SaasManager;
import com.je.staticize.service.StaticizeManager;
@Component("roleManager")
public class RoleManagerImpl implements RoleManager {
	private PCDynaServiceTemplate serviceTemplate;
	private PermissionManager permissionManager;
	private PCServiceTemplate pcServiceTemplate;
	private StaticizeManager staticizeManager;
	private SaasManager saasManager;

	/**
	 * 修改
	 * @param dynaBean 自定义动态类
	 * @param isHard TODO 暂不明确
	 * @return
	 */
	@Override
	public DynaBean doUpdate(DynaBean dynaBean,Boolean isHard) {
		// TODO Auto-generated method stub
		DynaBean role=serviceTemplate.selectOneByPk("JE_CORE_ROLE", dynaBean.getStr("ROLEID"));
//		if("1".equals(WebUtils.sysVar.get("JE_SYS_STATICIZE"))){
//		}
		if("DEPT".equals(role.getStr("ROLETYPE"))){
			pcServiceTemplate.executeSql("UPDATE JE_CORE_DEPARTMENT SET JTGSID='"+role.getStr("JTGSID")+"',JTGSMC='"+role.getStr("JTGSMC")+"' WHERE DEPTID='"+role.getStr("DEPTID")+"'");
		}else if("SENTRY".equals(role.getStr("ROLETYPE"))){
			pcServiceTemplate.executeSql("UPDATE JE_CORE_SENTRY SET JTGSID='"+role.getStr("JTGSID")+"',JTGSMC='"+role.getStr("JTGSMC")+"' WHERE SENTRYID='"+role.getStr("SENTRYID")+"'");
		}
		serviceTemplate.buildModelModifyInfo(dynaBean);
		DynaBean updated = serviceTemplate.update(dynaBean);
		RolePermNode rootNode=permissionManager.buildRolePermTree(dynaBean.getStr("ROLEID"));
		rootNode.updateGroup("", "", PermExtendType.PERM_SELF, isHard);
		//重新静态化该角色下
		if(!dynaBean.getStr("GROUPCODE","").equals(role.getStr("GROUPCODE",""))){
			staticizeManager.clearUserStatize(" AND USERID IN (SELECT USERID FROM JE_CORE_ROLE_USER WHERE ROLEID='"+dynaBean.getStr("ROLEID")+"') ",false);
		}
		return updated;
	}

	/**
	 * 删除
	 * @param ids 操作id
	 * @param noSaas TODO 暂不明确
	 * @return
	 */
	@Override
	public Integer doRemove(String ids,Boolean noSaas) {
		// TODO Auto-generated method stub
		String[] idArray=ids.split(",");
		for(String id:idArray){
			DynaBean role=serviceTemplate.selectOneByPk("JE_CORE_ROLE",id);
			if(role==null)continue;
			//处理权限静态化
			staticizeManager.clearUserStatize(" AND USERID IN (SELECT USERID FROM JE_CORE_ROLE_USER WHERE ROLEID='"+id+"')",false);
			if(WebUtils.isSaas() && !noSaas){
				//如果是基础角色的删除
				if("SYS".equals(role.getStr("ROLERANK"))){
					List<DynaBean> saasRoles=serviceTemplate.selectList("JE_CORE_ROLE", " AND BASEROLEID IN (SELECT ROLEID FROM JE_CORE_ROLE WHERE PATH LIKE '%"+id+"%') AND ROLERANK IN ('CP','YH')");
					for(DynaBean saasRole:saasRoles){
						saasManager.doRemoveCpRole(saasRole);
					}
				}else if("CP".equals(role.getStr("ROLERANK"))){
					List<DynaBean> yhRoles=serviceTemplate.selectList("JE_CORE_ROLE", " AND BASEROLEID='"+role.getStr("BASEROLEID")+"' AND ROLERANK='YH'");
					for(DynaBean yhRole:yhRoles){
						saasManager.doClearCpPerm(yhRole,role);
					}
				}
			}
			List<DynaBean> roleUsers=serviceTemplate.selectList("JE_CORE_ROLE_USER", " AND ROLEID IN (SELECT ROLEID FROM JE_CORE_ROLE where PATH like '%"+id+"%')");
			pcServiceTemplate.executeSql("DELETE FROM JE_CORE_ROLE_USER WHERE ROLEID IN (SELECT ROLEID FROM JE_CORE_ROLE where PATH like '%"+id+"%')");
			if(role==null)continue;
			//级联更新用户的角色名和角色编码
			for(DynaBean roleUser:roleUsers){
				List<DynaBean> roles=serviceTemplate.selectList("JE_CORE_ROLE", " AND ROLEID IN (SELECT ROLEID FROM JE_CORE_ROLE_USER WHERE USERID='"+roleUser.getStr("USERID")+"') AND ROLETYPE='ROLE'");
				String roleNames="";
				String roleCodes="";
				String roleIds="";
				for(DynaBean r:roles){
					roleNames+=r.getStr("ROLENAME")+",";
					roleCodes+=r.getStr("ROLECODE")+",";
					roleIds+=r.getStr("ROLEID")+",";
				}
				pcServiceTemplate.executeSql("UPDATE JE_CORE_ENDUSER SET ROLENAMES='"+StringUtil.replaceSplit(roleNames)+"',ROLECODES='"+StringUtil.replaceSplit(roleCodes)+"',ROLEIDS='"+StringUtil.replaceSplit(roleIds)+"' where USERID='"+roleUser.getStr("USERID")+"'");
			}
			pcServiceTemplate.executeSql("DELETE FROM JE_CORE_ROLE_PERM WHERE ROLEID IN (SELECT ROLEID FROM JE_CORE_ROLE where PATH like '%"+id+"%')");
			//删除分级权限管理
			pcServiceTemplate.executeSql("DELETE FROM JE_CORE_ADMIN_PERM WHERE PERID IN (SELECT JE_CORE_ADMINPERM_ID FROM JE_CORE_ADMINPERM WHERE ADMINPERM_TYPE IN ('"+AdminPermType.ROLE_PERM+"','"+AdminPermType.ROLE_SELECT+"','"+AdminPermType.ROLE_UPDATE+"') AND ADMINPERM_RESID IN (SELECT ROLEID FROM JE_CORE_ROLE where PATH like '%"+id+"%'))");
			pcServiceTemplate.executeSql("DELETE FROM JE_CORE_ADMINPERM WHERE ADMINPERM_TYPE IN ('"+AdminPermType.ROLE_PERM+"','"+AdminPermType.ROLE_SELECT+"','"+AdminPermType.ROLE_UPDATE+"') AND ADMINPERM_RESID IN (SELECT ROLEID FROM JE_CORE_ROLE where PATH like '%"+id+"%')");
			pcServiceTemplate.executeSql("DELETE FROM JE_CORE_ROLE where PATH like '%"+id+"%'");
			//维护树形的NODETYPE
			Long count=pcServiceTemplate.countBySql("select count(*) from JE_CORE_ROLE where PARENT='"+role.getStr("PARENT")+"'");
			if(count<=0){
				DynaBean one=serviceTemplate.selectOneByPk("JE_CORE_ROLE",role.getStr("PARENT"));
				if(NodeType.GENERAL.equals(one.getStr("NODETYPE"))){
					one.set(BeanUtils.KEY_TABLE_CODE, "JE_CORE_ROLE");
					one.set("NODETYPE", NodeType.LEAF);
					serviceTemplate.update(one);
				}
			}
		}
		return idArray.length;
	}

	/**
	 * 导入人员
	 * @param userIds 用户id
	 * @param roleId 角色id
	 * @return
	 */
	@Override
	public Integer implUsers(String userIds, String roleId) {
		// TODO Auto-generated method stub
		Set<String> userSet=new HashSet<String>();
		List<DynaBean> roleUsers=serviceTemplate.selectList("JE_CORE_ROLE_USER", " AND ROLEID='"+roleId+"'");
		for(DynaBean roleUser:roleUsers){
			userSet.add(roleUser.getStr("USERID"));
		}
//		if("1".equals(WebUtils.sysVar.get("JE_SYS_STATICIZE")) && !JECoreMode.DEVELOP.equals(WebUtils.sysVar.get("JE_CORE_MODE"))){
//		}
		pcServiceTemplate.executeSql("DELETE FROM JE_CORE_ROLE_USER WHERE ROLEID='"+roleId+"'");
		String[] userIdArray=userIds.split(",");

		for(String userId:userIdArray){
			String insertSql=" INSERT INTO JE_CORE_ROLE_USER(ROLEID,USERID) VALUES('"+roleId+"','"+userId+"')";
			pcServiceTemplate.executeSql(insertSql);
			userSet.add(userId);
			//更新用户中的角色名称和角色编码
//			List<DynaBean> roles=serviceTemplate.selectList("JE_CORE_ROLE", " AND ROLEID IN (SELECT ROLEID FROM JE_CORE_ROLE_USER WHERE USERID='"+userId+"') AND ROLETYPE='ROLE'");
//			String roleNames="";
//			String roleCodes="";
//			String roleIds="";
//			for(DynaBean r:roles){
//				roleNames+=r.getStr("ROLENAME")+",";
//				roleCodes+=r.getStr("ROLECODE")+",";
//				roleIds+=r.getStr("ROLEID")+",";
//			}
//			//当前只重新静态化已删除或新加， 如果没删没加不做处理
//			if(userSet.contains(userId)){
//				userSet.remove(userId);
//			}else{
//				userSet.add(userId);
//			}
		}
//		pcServiceTemplate.executeSql("UPDATE JE_CORE_ENDUSER SET ROLENAMES='"+StringUtil.replaceSplit(roleNames)+"',ROLECODES='"+StringUtil.replaceSplit(roleCodes)+"',ROLEIDS='"+StringUtil.replaceSplit(roleIds)+"' where USERID='"+userId+"'");
		syncUserRoleInfo(StringUtil.buildSplitString(ArrayUtils.getArray(userSet), ","));
		//处理权限静态化
		staticizeManager.clearUserStatize(" AND USERID IN ("+StringUtil.buildArrayToString(ArrayUtils.getArray(userSet))+")",false);
		return userIdArray.length;
	}

	/**
	 * 删除用户
	 * @param userIds 用户id
	 * @param roleId 角色id
	 * @return
	 */
	@Override
	public Integer removeUsers(String userIds, String roleId) {
		// TODO Auto-generated method stub
		String[] idArray=userIds.split(",");
		pcServiceTemplate.executeSql("DELETE FROM JE_CORE_ROLE_USER WHERE ROLEID='"+roleId+"' and USERID in ("+StringUtil.buildArrayToString(idArray)+")");
//		for(String userId:idArray){
//			//更新用户中的角色名称和角色编码
//			List<DynaBean> roles=serviceTemplate.selectList("JE_CORE_ROLE", " AND ROLEID IN (SELECT ROLEID FROM JE_CORE_ROLE_USER WHERE USERID='"+userId+"') AND ROLETYPE='ROLE'");
//			String roleNames="";
//			String roleCodes="";
//			String roleIds="";
//			for(DynaBean r:roles){
//				roleNames+=r.getStr("ROLENAME")+",";
//				roleCodes+=r.getStr("ROLECODE")+",";
//				roleIds+=r.getStr("ROLEID")+",";
//			}
//			pcServiceTemplate.executeSql("UPDATE JE_CORE_ENDUSER SET ROLENAMES='"+StringUtil.replaceSplit(roleNames)+"',ROLECODES='"+StringUtil.replaceSplit(roleCodes)+"',ROLEIDS='"+StringUtil.replaceSplit(roleIds)+"' where USERID='"+userId+"'");
//		}
		syncUserRoleInfo(userIds);
		//重新静态化该人员
		staticizeManager.clearUserStatize(" AND USERID IN ("+StringUtil.buildArrayToString(userIds.split(","))+")",false);
		return idArray.length;
	}

	/**
	 * 同步人员角色信息
	 * @param userIds 用户id
	 */
	@Override
	public void syncUserRoleInfo(String userIds) {
		// TODO Auto-generated method stub
		for(String userId:userIds.split(",")){
			List<DynaBean> roles=serviceTemplate.selectList("JE_CORE_ROLE", " AND ROLETYPE='ROLE' AND ROLEID IN (SELECT ROLEID FROM JE_CORE_ROLE_USER WHERE USERID='"+userId+"')","ROLENAME,ROLECODE,ROLEID");
			DynaBean user=new DynaBean("JE_CORE_ENDUSER",true);
			user.set("USERID", userId);
			user.set("ROLENAMES", StringUtil.buildSplitString(ArrayUtils.getBeanFieldArray(roles, "ROLENAME"), ","));
			user.set("ROLECODES", StringUtil.buildSplitString(ArrayUtils.getBeanFieldArray(roles, "ROLECODE"), ","));
			user.set("ROLEIDS", StringUtil.buildSplitString(ArrayUtils.getBeanFieldArray(roles, "ROLEID"), ","));
			serviceTemplate.update(user);
		}
	}

	/**
	 * 角色移动
	 * @param dynaBean 自定义动态类
	 * @return
	 */
	@Override
	public DynaBean roleMove(DynaBean dynaBean) {
		// TODO Auto-generated method stub
		DynaBean role=serviceTemplate.selectOneByPk("JE_CORE_ROLE", dynaBean.getStr("ROLEID"));
		String newParentId=dynaBean.getStr("PARENT");
		String oldPath=role.getStr("PATH");
		String oldParentPath=role.getStr("PARENTPATH","");
		String oldParentId=role.getStr("PARENT");
		Integer chaLayer=dynaBean.getInt("LAYER",0)-role.getInt("LAYER",0);
		role.set("PARENT", newParentId);
		role.set(BeanUtils.KEY_TABLE_CODE, "JE_CORE_ROLE");
		DynaBean parent=serviceTemplate.selectOneByPk("JE_CORE_ROLE", newParentId);
		role.set("PARENTTEXT",parent.getStr("ROLENAME"));
		role.set("PARENTCODE",parent.getStr("ROLECODE"));
		role.set("LAYER", parent.getInt("LAYER",0)+1);
		role.set("PATH", parent.getStr("PATH")+"/"+dynaBean.getStr("ROLEID"));
		role.set("PARENTPATH", parent.getStr("PATH"));
		if(NodeType.LEAF.equals(parent.getStr("NODETYPE"))){
			pcServiceTemplate.executeSql("UPDATE JE_CORE_ROLE SET NODETYPE='"+NodeType.GENERAL+"' WHERE ROLEID='"+ newParentId+"'");
		}
		role.set("TREEORDERINDEX", parent.getStr("TREEORDERINDEX"));
		//更新当前节点下所有孩子的路径信息
		pcServiceTemplate.executeSql("UPDATE JE_CORE_ROLE SET PATH=REPLACE(PATH,'"+oldPath+"','"+role.getStr("PATH")+"'),PARENTPATH=REPLACE(PATH,'"+oldParentPath+"','"+role.getStr("PARENTPATH")+"'),LAYER=(LAYER+"+chaLayer+") WHERE PATH LIKE '%"+oldPath+"%' AND ROLEID!='"+ dynaBean.getStr("ROLEID")+"'");
		//角色先不维护序号,TREEORDERINDEX=('"+role.getStr("TREEORDERINDEX")+"'+"+subStringFunction+"(TREEORDERINDEX,"+(oldTreeOrderIndex.length()+1)+","+lengthFunction+"(TREEORDERINDEX)-"+oldTreeOrderIndex.length()+"))
		DynaBean updated=serviceTemplate.update(role);
		//删除该角色继承的权限 该处应该利用组合模式将权限全部删除 然后再添加父亲的权限
//		pcServiceTemplate.executeSql("DELETE FROM JE_CORE_ROLE_PERM WHERE ROLEID ='"+updated.getStr("ROLEID")+"' AND TYPE='EXTEND'");
		List<DynaBean> delPerms=serviceTemplate.selectList("JE_CORE_PERMISSION"," AND PERID IN (SELECT PERID FROM JE_CORE_ROLE_PERM WHERE ROLEID ='"+updated.getStr("ROLEID")+"' AND TYPE='EXTEND')");
		String[] delPermIds=new String[delPerms.size()];
		for(Integer i=0;i<delPerms.size();i++){
			delPermIds[i]=delPerms.get(i).getStr("PERID");
		}
		//清楚本身以及孩子所有继承的权限，直接删除操作，以防止对以后聚合造成排斥
		RolePermNode roleNode=permissionManager.buildRolePermTree(updated.getStr("ROLEID"));
		if(delPermIds.length>0){
			roleNode.deleteExtendPerms(StringUtil.buildArrayToString(delPermIds));
		}
		if(!NodeType.ROOT.equalsIgnoreCase(newParentId)){
			List<DynaBean> addPerms=serviceTemplate.selectList("JE_CORE_PERMISSION", " AND PERID IN (SELECT PERID FROM JE_CORE_ROLE_PERM WHERE ROLEID='"+newParentId+"' AND TYPE!='GROUP' AND ENABLED='1')");
			if(addPerms.size()>0){
				roleNode.updatePerm("", addPerms, false, PermExtendType.PERM_EXTEND);
			}
			//处理新父亲聚合组继承的关系
			RolePermNode rolePermNode=permissionManager.buildRolePermTree(newParentId);
//			pcServiceTemplate.executeSql("INSERT INTO JE_CORE_ROLE_PERM(ROLEID,PERID,TYPE,ENABLED) SELECT '"+updated.getStr("ROLEID")+"',PERID,'EXTEND',ENABLED from JE_CORE_ROLE_PERM WHERE ROLEID='"+newParentId+"'");
			rolePermNode.updateGroup("", "", PermExtendType.PERM_SELF, false);
		}
		//处理树形NODETYPE
		pcServiceTemplate.executeSql("UPDATE JE_CORE_ROLE SET NODETYPE='"+NodeType.GENERAL+"' WHERE ROLEID='"+ newParentId+"'");
		//维护树形的NODETYPE
		Long count=pcServiceTemplate.countBySql("select count(*) from JE_CORE_ROLE where PARENT='"+oldParentId+"'");
		if(count<=0){
			DynaBean one=serviceTemplate.selectOneByPk("JE_CORE_ROLE",oldParentId);
			if(NodeType.GENERAL.equals(one.getStr("NODETYPE"))){
				one.set(BeanUtils.KEY_TABLE_CODE, "JE_CORE_ROLE");
				one.set("NODETYPE", NodeType.LEAF);
				serviceTemplate.update(one);
			}
		}
		//重新静态化文件
		staticizeManager.clearUserStatize(" AND USERID IN (SELECT USERID FROM JE_CORE_ROLE_USER WHERE ROLEID IN (SELECT ROLEID FROM JE_CORE_ROLE WHERE PATH LIKE '%"+updated.getStr("ROLEID")+"%'))",false);
		return updated;
	}

	/**
	 * 生成树形排序编号
	 * @param dynaBean 自定义动态类
	 */
	@Override
	public void generateTreeOrderIndex(DynaBean dynaBean){
		String parent=dynaBean.getStr("PARENT");
		String parentNumber=dynaBean.getStr("TREEORDERINDEX","");
		List<Object> objs=(List<Object>) pcServiceTemplate.queryBySql("select MAX(TREEORDERINDEX) FROM "+dynaBean.getStr(BeanUtils.KEY_TABLE_CODE)+" WHERE PARENT='"+parent+"'");
		if(objs!=null && objs.size()>0 && StringUtil.isNotEmpty(objs.get(0)+"")){
			Integer value=Integer.parseInt(StringUtil.getDefaultValue((objs.get(0)+"").substring(parentNumber.length()),"0"))+1;
			dynaBean.setStr("TREEORDERINDEX",parentNumber+=StringUtil.preFillUp(value.toString(), 6, '0'));
			dynaBean.set("ORDERINDEX", value);
		}else{
			dynaBean.set("ORDERINDEX", 1);
			dynaBean.setStr("TREEORDERINDEX",parentNumber+=StringUtil.preFillUp("1", 6, '0'));
		}
	}
	@Resource(name="PCDynaServiceTemplate")
	public void setServiceTemplate(PCDynaServiceTemplate serviceTemplate) {
		this.serviceTemplate = serviceTemplate;
	}

	@Resource(name="permissionManager")
	public void setPermissionManager(PermissionManager permissionManager) {
		this.permissionManager = permissionManager;
	}

	@Resource(name="PCServiceTemplateImpl")
	public void setPcServiceTemplate(PCServiceTemplate pcServiceTemplate) {
		this.pcServiceTemplate = pcServiceTemplate;
	}
	@Resource(name="staticizeManager")
	public void setStaticizeManager(StaticizeManager staticizeManager) {
		this.staticizeManager = staticizeManager;
	}
	@Resource(name="saasManager")
	public void setSaasManager(SaasManager saasManager) {
		this.saasManager = saasManager;
	}

}
