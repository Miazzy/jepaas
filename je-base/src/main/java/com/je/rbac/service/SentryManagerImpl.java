package com.je.rbac.service;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.je.core.exception.PlatformException;
import com.je.core.exception.PlatformExceptionEnum;
import org.springframework.stereotype.Component;

import com.je.core.constants.rbac.PermExtendType;
import com.je.core.constants.table.TableType;
import com.je.core.constants.tree.NodeType;
import com.je.core.util.SecurityUserHolder;
import com.je.core.service.PCDynaServiceTemplate;
import com.je.core.service.PCServiceTemplate;
import com.je.core.util.DBSqlUtils;
import com.je.core.util.JEUUID;
import com.je.core.util.StringUtil;
import com.je.core.util.WebUtils;
import com.je.core.util.bean.BeanUtils;
import com.je.core.util.bean.DynaBean;
import com.je.develop.service.FuncPermManager;
import com.je.rbac.model.EndUser;
import com.je.rbac.node.RolePermNode;
import com.je.staticize.service.StaticizeManager;
import com.je.table.service.TableManager;

/**
 * 岗位服务层接口实现类
 */
@Component("sentryManager")
public class SentryManagerImpl implements SentryManager {
	private PCDynaServiceTemplate serviceTemplate;
	private TableManager tableManager;
	private PermissionManager permissionManager;
	private RoleManager roleManager;
	private PCServiceTemplate pcServiceTemplate;
	private FuncPermManager funcPermManager;
	private StaticizeManager staticizeManager;
	private AdminPermManager adminPermManager;

	/**
	 * 保存
	 * @param dynaBean 自定义动态类
	 * @return
	 */
	@Override
	public DynaBean doSave(DynaBean dynaBean) {
		// TODO Auto-generated method stub
		DynaBean inserted = serviceTemplate.insert(dynaBean);
		//插入角色表中
		String parentId=inserted.getStr("PARENT");
		DynaBean parentRole=serviceTemplate.selectOne("JE_CORE_ROLE"," AND SENTRYID='"+parentId+"' AND ROLETYPE='SENTRY'");
		DynaBean role=new DynaBean("JE_CORE_ROLE",false);
		role.set(BeanUtils.KEY_PK_CODE, "ROLEID");
		String roleuuid = JEUUID.uuid();
		role.set("ROLEID", roleuuid);
		role.set("ROLENAME", inserted.getStr("SENTRYNAME"));
		role.set("ROLECODE", inserted.getStr("SENTRYCODE"));
		role.set("ORDERINDEX", inserted.get("ORDERINDEX"));
		role.set("SENTRYID", inserted.getStr("SENTRYID"));
		role.set("CREATEUSER", inserted.getStr("CREATEUSER"));
		role.set("CREATEUSERNAME", inserted.getStr("CREATEUSERNAME"));
		role.set("CREATEORG", inserted.getStr("CREATEORG"));
		role.set("CREATEORGNAME", inserted.getStr("CREATETIME"));
		role.set("CREATETIME", inserted.getStr("CREATETIME"));
		role.set("LAYER", inserted.getStr("LAYER"));
		role.set("ROLERANK", "SYS");
		role.set("STATUS", inserted.getStr("STATUS"));
		role.set("JTGSMC", inserted.getStr("JTGSMC"));
		role.set("JTGSID", inserted.getStr("JTGSID"));
		role.set("NODETYPE", inserted.getStr("NODETYPE"));
		role.set("ISSUPERADMIN", "0");
		role.set("MANAGER", "0");
		role.set("DEVELOP", "0");
		role.set("ROLETYPE", "SENTRY");
		if(parentRole!=null){
			role.set("PARENTNAME", parentRole.getStr("ROLENAME"));
			role.set("PARENTCODE", parentRole.getStr("ROLECODE"));
			role.set("PARENT", parentRole.getStr("ROLEID"));
			role.set("PATH", parentRole.getStr("PATH")+"/"+roleuuid);
		}else{
			role.set("PARENTNAME", "ROOT");
			role.set("PARENTCODE", "ROOT");
			role.set("PARENT", "ROOT");
			role.set("PATH", "/ROOT/"+roleuuid);
		}
		DynaBean insertRole=serviceTemplate.insert(role);
		if("1".equals(WebUtils.getSysVar("JE_SYS_ADMINPERM"))){
			EndUser currentUser=SecurityUserHolder.getCurrentUser();
			if(!WebUtils.getSysVar("JE_SYS_ADMIN").equals(currentUser.getBackUserCode())){
				adminPermManager.addChildrenPerm(currentUser, insertRole.getStr("ROLEID"), insertRole.getStr("PARENT"), "SENTRY");
			}
		}
		//处理角色的权限
		RolePermNode rootNode=permissionManager.buildRolePermTree(insertRole.getStr("PARENT"));
		pcServiceTemplate.executeSql("INSERT INTO JE_CORE_ROLE_PERM(ROLEID,PERID,TYPE,ENABLED) SELECT '"+insertRole.getStr("ROLEID")+"',PERID,'EXTEND',ENABLED from JE_CORE_ROLE_PERM WHERE ROLEID='"+insertRole.getStr("PARENT")+"'");
		rootNode.updateGroup("", "", PermExtendType.PERM_SELF, false);
		/**----------处理岗位参数值表--------------*/
		DynaBean module=serviceTemplate.selectOne("JE_CORE_RESOURCETABLE"," AND RESOURCETABLE_TYPE='"+TableType.MODULETABLE+"' AND RESOURCETABLE_TABLECODE='GWCSZB'");
		if(module==null){
//			toWrite(jsonBuilder.returnFailureJson("'模块未找到，无法创建表'"));
			throw new PlatformException("模块未找到，无法创建表", PlatformExceptionEnum.JE_CORE_TABLE_NOFIND_ERROR,new Object[]{dynaBean});
		}
		DynaBean table=new DynaBean("JE_CORE_RESOURCETABLE",false);
		table.set(BeanUtils.KEY_PK_CODE, "JE_CORE_RESOURCETABLE_ID");
		String tableId = JEUUID.uuid();
		table.set("JE_CORE_RESOURCETABLE_ID", tableId);
		table.set("RESOURCETABLE_TABLENAME", "岗位【"+inserted.getStr("SENTRYNAME")+"】参数值表");
		table.set("RESOURCETABLE_TABLECODE", "JE_CORE_SENTRY_"+inserted.getStr("SENTRYCODE").toUpperCase());
		table.set("RESOURCETABLE_ISCREATE", "0");
		table.set("RESOURCETABLE_ICONCLS", "table_pt");
		table.set("RESOURCETABLE_ISUSEFOREIGNKEY", "0");
		table.set("RESOURCETABLE_TYPE", TableType.PTTABLE);
		table.set("RESOURCETABLE_NODEINFOTYPE", "");
		table.set("RESOURCETABLE_NODEINFO", "");
		table.set("SY_NODETYPE", "LEAF");
		table.set("SY_LAYER", 4);
		table.set("SY_DISABLED", "0");
		table.set("SY_PATH", module.getStr("SY_PATH")+"/"+tableId);
		table.set("SY_PARENTPATH", module.getStr("SY_PATH"));
		table.set("SY_PARENT",module.getStr("JE_CORE_RESOURCETABLE_ID"));
		table.set("RESOURCETABLE_PKCODE", "JE_CORE_SENTRY_"+inserted.getStr("SENTRYCODE").toUpperCase()+"_ID");
		table.set("SY_JECORE", "1");
		table.set("SY_JESYS", "1");
		serviceTemplate.buildModelCreateInfo(table);
		DynaBean tabled=serviceTemplate.insert(table);
		tableManager.initColumns(tabled, false);
		//加入用户ID
		DynaBean userColumn=new DynaBean("JE_CORE_TABLECOLUMN",false);
		userColumn.set(BeanUtils.KEY_PK_CODE, "JE_CORE_TABLECOLUMN_ID");
		userColumn.set("TABLECOLUMN_NAME", "用户主键ID");
		userColumn.set("TABLECOLUMN_CODE", inserted.getStr("SENTRYCODE").toUpperCase()+"_ENDUSER_ID");
		userColumn.set("TABLECOLUMN_TYPE", "VARCHAR");
		userColumn.set("TABLECOLUMN_LENGTH", "50");
		userColumn.set("TABLECOLUMN_ISCREATE", "0");
		userColumn.set("TABLECOLUMN_ISNULL", "1");
		userColumn.set("TABLECOLUMN_UNIQUE", "0");
		userColumn.set("TABLECOLUMN_CLASSIFY", "SYS");
		userColumn.set("TABLECOLUMN_TREETYPE", "NORMAL");
		userColumn.set("TABLECOLUMN_RESOURCETABLE_ID", tabled.getStr("JE_CORE_RESOURCETABLE_ID"));
		userColumn.set("TABLECOLUMN_TABLECODE", tabled.getStr("RESOURCETABLE_TABLECODE"));
		serviceTemplate.buildModelCreateInfo(userColumn);
		serviceTemplate.insert(userColumn);
		tableManager.initIndexs(tabled, false);
		tableManager.initKeys(tabled, false);
		Boolean flag=tableManager.createTable(tabled.getStr("JE_CORE_RESOURCETABLE_ID"));
		if(!flag){
//			toWrite(jsonBuilder.returnFailureJson("'表创建失败！'"));
			throw new PlatformException("表创建失败", PlatformExceptionEnum.JE_CORE_TABLE_NOFIND_ERROR,new Object[]{dynaBean});
//			throw new DynaActionException("表创建失败");
		}
		return inserted;
	}

	/**
	 * 修改
	 * @param dynaBean 自定义动态类
	 */
	@Override
	public void doUpdate(DynaBean dynaBean) {
		// TODO Auto-generated method stub
		DynaBean sentryRole=serviceTemplate.selectOne("JE_CORE_ROLE"," and SENTRYID='"+dynaBean.getStr("SENTRYID")+"' and ROLETYPE='SENTRY'");
		if(sentryRole!=null){
			if(dynaBean.getStr("SENTRYNAME")!=null){
				sentryRole.set("ROLENAME", dynaBean.getStr("SENTRYNAME"));
			}
			if(dynaBean.getStr("ROLECODE")!=null){
				sentryRole.set("ROLECODE", dynaBean.getStr("SENTRYCODE"));
			}
			if(dynaBean.getStr("NODETYPE")!=null){
				sentryRole.set("NODETYPE", dynaBean.getStr("NODETYPE"));
			}
			sentryRole.set("JTGSMC", dynaBean.getStr("JTGSMC"));
			sentryRole.set("JTGSID", dynaBean.getStr("JTGSID"));
			sentryRole.set("ORDERINDEX", dynaBean.getStr("ORDERINDEX"));
			sentryRole.set(BeanUtils.KEY_TABLE_CODE, "JE_CORE_ROLE");
			serviceTemplate.update(sentryRole);
		}
		DynaBean sentry=serviceTemplate.selectOneByPk("JE_CORE_SENTRY", dynaBean.getStr("SENTRYID"));
		if(dynaBean.getStr("SENTRYCODE")!=null && !sentry.getStr("SENTRYCODE").equalsIgnoreCase(dynaBean.getStr("SENTRYCODE"))){
			DynaBean table=serviceTemplate.selectOne("JE_CORE_RESOURCETABLE"," AND RESOURCETABLE_TYPE!='"+TableType.MODULETABLE+"' AND RESOURCETABLE_TABLECODE='"+sentry.getStr("TABLECODE")+"'");
			BeanUtils.getInstance().clearCache(table.getStr("RESOURCETABLE_TABLECODE"));
			table.set("RESOURCETABLE_TABLECODE", "JE_CORE_SENTRY_"+dynaBean.getStr("SENTRYCODE").toUpperCase());
			table.set("RESOURCETABLE_TABLENAME", "岗位【"+dynaBean.getStr("SENTRYNAME")+"】参数值表");
			DynaBean tabled=serviceTemplate.update(table);
			List<DynaBean> params=serviceTemplate.selectList("JE_CORE_SENTRYPARAMS", " AND SENTRYPARAMS_SENTRY_ID='"+dynaBean.getStr("SENTRYID")+"'");
			for(DynaBean param:params){
				pcServiceTemplate.executeSql("UPDATE JE_CORE_TABLECOLUMN SET TABLECOLUMN_CODE='"+dynaBean.getStr("SENTRYCODE")+"_"+param.getStr("SENTRYPARAMS_FIELDCODE")+"' WHERE TABLECOLUMN_CODE='"+sentry.getStr("SENTRYCODE")+"_"+param.getStr("SENTRYPARAMS_FIELDCODE")+"'");
			}
			pcServiceTemplate.executeSql("UPDATE JE_CORE_SENTRYPARAMS SET SENTRYPARAMS_SENTRYCODE='"+dynaBean.getStr("SENTRYCODE")+"',SENTRYPARAMS_TABLECODE='"+table.getStr("RESOURCETABLE_TABLECODE")+"' WHERE SENTRYPARAMS_SENTRY_ID='"+dynaBean.getStr("SENTRYID")+"'");
			pcServiceTemplate.executeSql("UPDATE JE_CORE_TABLECOLUMN SET TABLECOLUMN_CODE='"+dynaBean.getStr("SENTRYCODE")+"_ENDUSER_ID' WHERE TABLECOLUMN_CODE='"+sentry.getStr("SENTRYCODE")+"_ENDUSER_ID'");
			tableManager.updateTable(tabled.getStr("JE_CORE_RESOURCETABLE_ID"), false);
			dynaBean.set("TABLECODE", tabled.getStr("RESOURCETABLE_TABLECODE"));
		}
	}

	/**
	 * 删除
	 * @param ids id
	 * @param deleteFlag 删除标记
	 * @return
	 */
	@Override
	public Integer doRemove(String ids,Boolean deleteFlag) {
		// TODO Auto-generated method stub
		String[] idArray=ids.split(",");
		for(String id:idArray){
			staticizeManager.clearUserStatize(" AND USERID IN (SELECT USERID FROM JE_CORE_SENTRY_USER WHERE SENTRYID in (SELECT SENTRYID FROM JE_CORE_SENTRY WHERE PATH LIKE '%"+id+"%'))",false);
			if(deleteFlag){
				DynaBean sentry=serviceTemplate.selectOneByPk("JE_CORE_SENTRY",id);
				//如果把父亲删除，孩子会查询不到
				if(sentry==null){
					continue;
				}
				List<DynaBean> users=serviceTemplate.selectList("JE_CORE_ENDUSER", " AND USERID IN (SELECT USERID FROM JE_CORE_SENTRY_USER WHERE SENTRYID IN (SELECT SENTRYID FROM JE_CORE_SENTRY where PATH like '%"+id+"%'))");
				String sentryTableCode=sentry.getStr("TABLECODE");
				if(StringUtil.isNotEmpty(sentryTableCode)){
					//删除表的操作
					DynaBean table=serviceTemplate.selectOne("JE_CORE_RESOURCETABLE"," AND RESOURCETABLE_TYPE!='"+TableType.MODULETABLE+"' AND RESOURCETABLE_TABLECODE='"+sentryTableCode+"'");
					BeanUtils.getInstance().clearCache(table.getStr("RESOURCETABLE_TABLECODE"));
					tableManager.removeTable(table.getStr("JE_CORE_RESOURCETABLE_ID"), true);
				}
				pcServiceTemplate.executeSql("DELETE FROM JE_CORE_ROLE_USER WHERE ROLEID IN (SELECT ROLEID FROM JE_CORE_ROLE WHERE ROLETYPE='SENTRY' AND SENTRYID IN (SELECT SENTRYID FROM JE_CORE_SENTRY WHERE PATH LIKE '%"+id+"%'))");
				pcServiceTemplate.executeSql("DELETE FROM JE_CORE_ROLE_PERM WHERE ROLEID IN (SELECT ROLEID FROM JE_CORE_ROLE WHERE ROLETYPE='SENTRY' AND SENTRYID IN (SELECT SENTRYID FROM JE_CORE_SENTRY WHERE PATH LIKE '%"+id+"%'))");
				pcServiceTemplate.executeSql("DELETE FROM JE_CORE_ROLE WHERE ROLETYPE='SENTRY' AND SENTRYID IN (SELECT SENTRYID FROM JE_CORE_SENTRY WHERE PATH LIKE '%"+id+"%')");
				pcServiceTemplate.executeSql("DELETE FROM JE_CORE_SENTRY_USER WHERE SENTRYID in (SELECT SENTRYID FROM JE_CORE_SENTRY WHERE PATH LIKE '%"+id+"%')");
				//			pcServiceTemplate.executeSql("DELETE FROM JE_CORE_SENTRYVALUES WHERE SENTRYVALUES_SENTRY_ID in (SELECT SENTRYID FROM JE_CORE_SENTRY WHERE PATH LIKE '%"+id+"%')");
				pcServiceTemplate.executeSql("DELETE FROM JE_CORE_SENTRY where PATH like '%"+id+"%'");
				//由于人员对岗位是多选，是否级联更新该名称和编码，主键 会导致性能稍慢问题
				if(users.size()>0){
					syncUserInfo(users);
				}
			}else{
				pcServiceTemplate.executeSql("update JE_CORE_SENTRY SET STATUS='0' WHERE PATH LIKE '%"+id+"%'");
				pcServiceTemplate.executeSql("update JE_CORE_ROLE SET STATUS='0' WHERE SENTRYID IN (SELECT SENTRYID FROM JE_CORE_SENTRY WHERE PATH LIKE '%"+id+"%')");
			}
			//重新静态化该人员
//			if("1".equals(WebUtils.getSysVar("JE_SYS_STATICIZE")) && !JECoreMode.DEVELOP.equals(WebUtils.getSysVar("JE_CORE_MODE")) && staticizes.size()>0){
//				for(DynaBean staticize:staticizes){
//					staticizeManager.doStaticize(staticize);
//				}
//			}
		}
		Integer count=idArray.length;

		return count;
	}

	/**
	 * 启用数据
	 * @param ids id
	 * @return
	 */
	@Override
	public Integer doEnabled(String ids){
		String[] idArray=ids.split(",");
		for(String id:idArray){
			pcServiceTemplate.executeSql("update JE_CORE_SENTRY SET STATUS='1' WHERE PATH LIKE '%"+id+"%'");
			pcServiceTemplate.executeSql("update JE_CORE_ROLE SET STATUS='1' WHERE SENTRYID IN (SELECT SENTRYID FROM JE_CORE_SENTRY WHERE PATH LIKE '%"+id+"%')");
			//重新静态化该人员
			staticizeManager.clearUserStatize(" AND USERID IN (SELECT USERID FROM JE_CORE_SENTRY_USER WHERE SENTRYID in (SELECT SENTRYID FROM JE_CORE_SENTRY WHERE PATH LIKE '%"+id+"%'))",false);
		}
		Integer count=idArray.length;

		return count;
	}

	/**
	 * 导入人员
	 * @param userIds 用户id
	 * @param sentryId TODO 暂不明确
	 */
	@Override
	public void implUsers(String userIds, String sentryId)  {
		// TODO Auto-generated method stub
		for(String userId:userIds.split(",")){
			String insertSql=" INSERT INTO JE_CORE_SENTRY_USER(SENTRYID,USERID) VALUES('"+sentryId+"','"+userId+"')";
			pcServiceTemplate.executeSql(insertSql);
			DynaBean roleSentry=serviceTemplate.selectOne("JE_CORE_ROLE"," and ROLETYPE='SENTRY' AND SENTRYID='"+sentryId+"'");
			if(roleSentry!=null){
				pcServiceTemplate.executeSql("INSERT INTO JE_CORE_ROLE_USER(ROLEID,USERID) VALUES('"+roleSentry.getStr("ROLEID")+"','"+userId+"')");
			}
			//更新用户中的角色名称和角色编码
			List<DynaBean> sentrys=serviceTemplate.selectList("JE_CORE_SENTRY", " AND SENTRYID IN (SELECT SENTRYID FROM JE_CORE_SENTRY_USER WHERE USERID='"+userId+"')");
			String sentryNames="";
			String sentryIds="";
			String sentryCodes="";
			for(DynaBean sentry:sentrys){
				sentryNames+=sentry.getStr("SENTRYNAME")+",";
				sentryCodes+=sentry.getStr("SENTRYCODE")+",";
				sentryIds+=sentry.getStr("SENTRYID")+",";
			}
			pcServiceTemplate.executeSql("UPDATE JE_CORE_ENDUSER SET SENTRYNAMES='"+StringUtil.replaceSplit(sentryNames)+"',SENTRYCODES='"+StringUtil.replaceSplit(sentryCodes)+"',SENTRYIDS='"+StringUtil.replaceSplit(sentryIds)+"' where USERID='"+userId+"'");
		}
		//重新静态化该人员
		staticizeManager.clearUserStatize(" AND USERID IN ("+StringUtil.buildArrayToString(userIds.split(","))+")",false);
	}

	/**
	 * 移除人员
	 * @param userIds 用户id
	 * @param sentryId TODO 暂不明确
	 */
	@Override
	public void removeUsers(String userIds, String sentryId) {
		// TODO Auto-generated method stub
		DynaBean roleSentry=serviceTemplate.selectOne("JE_CORE_ROLE"," and ROLETYPE='SENTRY' AND SENTRYID='"+sentryId+"'");
		if(roleSentry!=null){
			pcServiceTemplate.executeSql("DELETE FROM JE_CORE_ROLE_USER WHERE ROLEID='"+roleSentry.getStr("ROLEID")+"' and USERID in ("+StringUtil.buildArrayToString(userIds.split(","))+")");
		}
//		pcServiceTemplate.executeSql("DELETE FROM JE_CORE_SENTRYVALUES WHERE SENTRYVALUES_SENTRY_ID='"+sentryId+"' AND SENTRYVALUES_ENDUSER_ID in ("+StringUtil.buildArrayToString(userIds.split(","))+")");
		pcServiceTemplate.executeSql("DELETE FROM JE_CORE_SENTRY_USER WHERE SENTRYID='"+sentryId+"' AND USERID in ("+StringUtil.buildArrayToString(userIds.split(","))+")");
		DynaBean sentryBean=serviceTemplate.selectOneByPk("JE_CORE_SENTRY",sentryId);
		pcServiceTemplate.executeSql("DELETE FROM "+sentryBean.getStr("TABLECODE")+" WHERE "+sentryBean.getStr("SENTRYCODE").toUpperCase()+"_ENDUSER_ID in ("+StringUtil.buildArrayToString(userIds.split(","))+")");
		for(String userId:userIds.split(",")){
			//更新用户中的角色名称和角色编码
			List<DynaBean> sentrys=serviceTemplate.selectList("JE_CORE_SENTRY", " AND SENTRYID IN (SELECT SENTRYID FROM JE_CORE_SENTRY_USER WHERE USERID='"+userId+"')");
			String sentryNames="";
			String sentryIds="";
			String sentryCodes="";
			for(DynaBean sentry:sentrys){
				sentryNames+=sentry.getStr("SENTRYNAME")+",";
				sentryCodes+=sentry.getStr("SENTRYCODE")+",";
				sentryIds+=sentry.getStr("SENTRYID")+",";
			}
			pcServiceTemplate.executeSql("UPDATE JE_CORE_ENDUSER SET SENTRYNAMES='"+StringUtil.replaceSplit(sentryNames)+"',SENTRYCODES='"+StringUtil.replaceSplit(sentryCodes)+"',SENTRYIDS='"+StringUtil.replaceSplit(sentryIds)+"' where USERID='"+userId+"'");
		}
		//重新静态化该人员
		staticizeManager.clearUserStatize( " AND USERID IN ("+StringUtil.buildArrayToString(userIds.split(","))+")",false);
	}

	/**
	 * 同步人员信息
	 * @param users 用户信息
	 */
	@Override
	public void syncUserInfo(List<DynaBean> users){
		for(DynaBean user:users){
			List<DynaBean> sentrys=serviceTemplate.selectList("JE_CORE_SENTRY", " AND SENTRYID IN (SELECT SENTRYID FROM JE_CORE_SENTRY_USER WHERE USERID='"+user.getStr("USERID")+"')");
			String sentryCodes="";
			String sentryNames="";
			String sentryIds="";
			for(DynaBean sentry:sentrys){
				sentryCodes+=sentry.getStr("SENTRYCODE")+",";
				sentryNames+=sentry.getStr("SENTRYNAME")+",";
				sentryIds+=sentry.getStr("SENTRYID")+",";
			}
			pcServiceTemplate.executeSql("UPDATE JE_CORE_ENDUSER SET SENTRYCODES='"+StringUtil.replaceSplit(sentryCodes)+"',SENTRYNAMES='"+StringUtil.replaceSplit(sentryNames)+"',SENTRYIDS='"+StringUtil.replaceSplit(sentryIds)+"' WHERE USERID='"+user.getStr("USERID")+"'");
		}
	}

	/**
	 * 岗位移动
	 * @param dynaBean 自定义动态类
	 * @param request 信息体
	 * @return
	 */
	@Override
	public DynaBean sentryMove(DynaBean dynaBean, HttpServletRequest request) {
		// TODO Auto-generated method stub
		String tableCode=dynaBean.getStr(BeanUtils.KEY_TABLE_CODE);
		String pkName=BeanUtils.getInstance().getPKeyFieldNames(tableCode);
		DynaBean bean=serviceTemplate.selectOneByPk(tableCode, dynaBean.getStr(pkName));
		String newParentId=request.getParameter("SY_PARENT");
		String oldParentId=bean.getStr("PARENT");
		String oldTreeOrderIndex=bean.getStr("TREEORDERINDEX");
		dynaBean.set("PATH", request.getParameter("SY_PATH"));
		dynaBean.set("LAYER", request.getParameter("SY_LAYER"));
		DynaBean newRole=serviceTemplate.selectOne("JE_CORE_ROLE", " AND ((SENTRYID='"+newParentId+"' AND ROLETYPE='SENTRY') OR (ROLEID='"+newParentId+"' AND ROLEID='ROOT'))");
		DynaBean role=serviceTemplate.selectOne("JE_CORE_ROLE", " AND SENTRYID='"+dynaBean.getStr(pkName)+"' AND ROLETYPE='SENTRY'");
		role.set("PATH", newRole.getStr("PATH")+"/"+role.getStr("ROLEID"));
		role.set("PARENTPATH", newRole.getStr("PATH"));
		role.set("PARENT", newRole.getStr("ROLEID"));
		role.set("LAYER", dynaBean.get("LAYER"));
		roleManager.roleMove(role);
		Integer chaLayer=dynaBean.getInt("LAYER")-bean.getInt("LAYER");
		String oldPath=bean.getStr("PATH");
		String oldParentPath=bean.getStr("PARENTPATH","");
		bean.set(BeanUtils.KEY_TABLE_CODE,tableCode);
		DynaBean parent=serviceTemplate.selectOneByPk(tableCode, newParentId);
		bean.set("TREEORDERINDEX", parent.getStr("TREEORDERINDEX"));
		bean.set("PATH", parent.getStr("PATH")+"/"+dynaBean.getStr(pkName));
		bean.set("PARENTPATH", parent.getStr("PATH"));
		bean.set("PARENT", newParentId);
		bean.set("LAYER", parent.getInt("LAYER",0)+1);
		if(NodeType.LEAF.equals(parent.getStr("NODETYPE"))){
			pcServiceTemplate.executeSql("UPDATE JE_CORE_SENTRY SET NODETYPE='"+NodeType.GENERAL+"' WHERE SENTRYID='"+ newParentId+"'");
		}
		roleManager.generateTreeOrderIndex(bean);
		String subStringFunction=DBSqlUtils.getPcDBMethodManager().getSubString();
		String lengthFunction=DBSqlUtils.getPcDBMethodManager().getLength();
		//更新当前节点下所有孩子的路径信息
		pcServiceTemplate.executeSql("UPDATE "+tableCode+" SET PATH=REPLACE(PATH,'"+oldPath+"','"+bean.getStr("PATH")+"'),PARENTPATH=REPLACE(PARENTPATH,'"+oldParentPath+"','"+bean.getStr("PARENTPATH")+"'),LAYER=(LAYER+"+chaLayer+"),TREEORDERINDEX=('"+bean.getStr("TREEORDERINDEX")+"'+"+subStringFunction+"(TREEORDERINDEX,"+(oldTreeOrderIndex.length()+1)+","+lengthFunction+"(TREEORDERINDEX)-"+oldTreeOrderIndex.length()+")) WHERE PATH LIKE '%"+oldPath+"%' AND "+pkName+"!='"+ dynaBean.getStr(pkName)+"' AND "+lengthFunction+"(TREEORDERINDEX)>="+oldTreeOrderIndex.length());
		DynaBean updated=serviceTemplate.update(bean);
		//处理树形NODETYPE
		//维护树形的NODETYPE
		Long count=pcServiceTemplate.countBySql("select count(*) from JE_CORE_SENTRY where PARENT='"+oldParentId+"'");
		if(count<=0){
			DynaBean one=serviceTemplate.selectOneByPk("JE_CORE_SENTRY",oldParentId);
			if(NodeType.GENERAL.equals(one.getStr("NODETYPE"))){
				one.set(BeanUtils.KEY_TABLE_CODE, "JE_CORE_SENTRY");
				one.set("NODETYPE", NodeType.LEAF);
				serviceTemplate.update(one);
			}
		}
		return updated;
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
	@Resource(name="tableManager")
	public void setTableManager(TableManager tableManager) {
		this.tableManager = tableManager;
	}
	@Resource(name="roleManager")
	public void setRoleManager(RoleManager roleManager) {
		this.roleManager = roleManager;
	}

	@Resource(name="funcPermManager")
	public void setFuncPermManager(FuncPermManager funcPermManager) {
		this.funcPermManager = funcPermManager;
	}

	@Resource(name="staticizeManager")
	public void setStaticizeManager(StaticizeManager staticizeManager) {
		this.staticizeManager = staticizeManager;
	}

	@Resource(name="adminPermManager")
	public void setAdminPermManager(AdminPermManager adminPermManager) {
		this.adminPermManager = adminPermManager;
	}

}
