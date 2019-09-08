package com.je.rbac.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.google.common.collect.Lists;
import com.je.cache.service.config.BackCacheManager;
import com.je.cache.service.config.FrontCacheManager;
import com.je.message.util.PushMsgHttpUtil;
import com.je.saas.service.SaasManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import com.je.core.constants.rbac.PermExtendType;
import com.je.core.constants.tree.NodeType;
import com.je.core.entity.QueryInfo;
import com.je.core.entity.extjs.JSONTreeNode;
import com.je.core.util.SecurityUserHolder;
import com.je.core.service.PCDynaServiceTemplate;
import com.je.core.service.PCServiceTemplate;
import com.je.core.util.ArrayUtils;
import com.je.core.util.DBSqlUtils;
import com.je.core.util.DateUtils;
import com.je.core.util.JEUUID;
import com.je.core.util.PingYinUtil;
import com.je.core.util.StringUtil;
import com.je.core.util.TreeUtil;
import com.je.core.util.WebUtils;
import com.je.core.util.bean.BeanUtils;
import com.je.core.util.bean.DynaBean;
import com.je.dd.vo.DicInfoVo;
import com.je.rbac.model.EndUser;
import com.je.rbac.node.RolePermNode;
import com.je.staticize.service.StaticizeManager;

/**
 * TODO 暂不明确的实现类
 */
@Component("departmentManager")
public class DepartmentManagerImpl implements DepartmentManager {
	private PCDynaServiceTemplate serviceTemplate;
	private PermissionManager permissionManager;
	private PCServiceTemplate pcServiceTemplate;
	private RoleManager roleManager;
	private StaticizeManager staticizeManager;
	private AdminPermManager adminPermManager;
	@Autowired
	private SaasManager saasManager;
	@Autowired
	private UserManager userManager;

	/**
	 * TODO 暂不明确
	 * @param dynaBean
	 * @return
	 */
	@Override
	public DynaBean doSave(DynaBean dynaBean) {
		// TODO Auto-generated method stub
		//判断是否需要插入到公司表中
//		if(!WebUtils.isSaas()){
		if("GS".equals(dynaBean.getStr("RANKCODE"))){
			DynaBean company=new DynaBean("JE_CORE_JDGSINFO",false);
			company.set(BeanUtils.KEY_PK_CODE, "JE_CORE_JDGSINFO_ID");
			company.set("JDGSINFO_TYPE_CODE", "CASCADE");
			company.set("JDGSINFO_TYPE_NAME", "部门级联");
			company.set("JDGSINFO_YECJTYPE_CODE", "GS");
			company.set("JDGSINFO_YECJTYPE_NAME", "公司级");
			company.set("JDGSINFO_GSMC", dynaBean.getStr("DEPTNAME"));
			company.set("JDGSINFO_GSDM", dynaBean.getStr("DEPTCODE"));
			company.set("JDGSINFO_GSID", dynaBean.getStr("DEPTID"));
			serviceTemplate.buildModelCreateInfo(company);
			company.set("SY_ZHID", dynaBean.getStr("ZHID"));
			company.set("SY_ZHMC", dynaBean.getStr("ZHMC"));
			company=serviceTemplate.insert(company);
			dynaBean.set("JTGSID", company.getStr("JE_CORE_JDGSINFO_ID"));
			dynaBean.set("JTGSMC", company.getStr("JDGSINFO_GSMC"));
			dynaBean.set("GSBMID", company.getStr("JDGSINFO_GSID"));
		}
//		}
		//默认公司信息 TODO  新增到组织管理中
		DynaBean inserted = serviceTemplate.insert(dynaBean);
		//插入角色表中
		String parentId=inserted.getStr("PARENT");
		String typeSql="";
		if(!NodeType.ROOT.equalsIgnoreCase(parentId)){
			typeSql=" AND ROLETYPE='DEPT'";
		}
		DynaBean parentRole=serviceTemplate.selectOne("JE_CORE_ROLE"," AND DEPTID='"+parentId+"' "+typeSql);
		DynaBean role=new DynaBean("JE_CORE_ROLE",false);
		role.set(BeanUtils.KEY_PK_CODE, "ROLEID");
		String roleuuid = JEUUID.uuid();
		role.set("ROLEID", roleuuid);
		role.set("ROLENAME", inserted.getStr("DEPTNAME"));
		role.set("ROLECODE", inserted.getStr("DEPTCODE"));
		role.set("ORDERINDEX", inserted.get("ORDERINDEX"));
		role.set("DEPTID", inserted.getStr("DEPTID"));
		role.set("CREATEUSER", inserted.getStr("CREATEUSER"));
		role.set("CREATEUSERNAME", inserted.getStr("CREATEUSERNAME"));
		role.set("CREATEORG", inserted.getStr("CREATEORG"));
		role.set("CREATEORGNAME", inserted.getStr("CREATEORGNAME"));
		role.set("CREATETIME", inserted.getStr("CREATETIME"));
		role.set("LAYER", inserted.getStr("LAYER"));
		role.set("ROLERANK", "SYS");
		role.set("STATUS", inserted.getStr("STATUS"));
		role.set("NODETYPE", inserted.getStr("NODETYPE"));
		role.set("TREEORDERINDEX", inserted.getStr("TREEORDERINDEX"));
		role.set("ISSUPERADMIN", "0");
		role.set("MANAGER", "0");
		role.set("DEVELOP", "0");
		role.set("ROLETYPE", "DEPT");
		role.set("JTGSID", inserted.get("JTGSID"));
		role.set("JTGSMC", inserted.getStr("JTGSMC"));
		role.set("ZHID", inserted.get("ZHID"));
		role.set("ZHMC", inserted.getStr("ZHMC"));
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
		DynaBean deptRole=serviceTemplate.insert(role);
		//加入分级权限
		if("1".equals(WebUtils.getSysVar("JE_SYS_ADMINPERM"))){
			EndUser currentUser=SecurityUserHolder.getCurrentUser();
			if(!WebUtils.getSysVar("JE_SYS_ADMIN").equals(currentUser.getBackUserCode())){
				adminPermManager.addChildrenPerm(currentUser, deptRole.getStr("ROLEID"), role.getStr("PARENT"), "DEPT");
			}
		}
		RolePermNode rootNode=permissionManager.buildRolePermTree(deptRole.getStr("PARENT"));
		rootNode.updateGroup("", "", PermExtendType.PERM_SELF, false);
		if(StringUtil.isNotEmpty(dynaBean.getStr("CHARGEUSER"))){
			doDeptUser(dynaBean.getStr("CHARGEUSER"), dynaBean.getStr("DEPTID"), false);
		}
		pcServiceTemplate.executeSql("INSERT INTO JE_CORE_ROLE_PERM(ROLEID,PERID,TYPE,ENABLED) SELECT '"+deptRole.getStr("ROLEID")+"',PERID,'EXTEND',ENABLED from JE_CORE_ROLE_PERM WHERE ROLEID='"+deptRole.getStr("PARENT")+"'");
		return inserted;
	}

	/**
	 * TODO 暂不明确
	 * @param dynaBean
	 */
	@Override
	public void doUpdate(DynaBean dynaBean) {
		// TODO Auto-generated method stub
		DynaBean oldDept=serviceTemplate.selectOneByPk("JE_CORE_DEPARTMENT", dynaBean.getStr("DEPTID"));
		DynaBean deptTable=BeanUtils.getInstance().getResourceTable("JE_CORE_DEPARTMENT");
		List<DynaBean> tableColumns=(List<DynaBean>) deptTable.get(BeanUtils.KEY_TABLE_COLUMNS);
		for(DynaBean column:tableColumns){
			String columnCode=column.getStr("TABLECOLUMN_CODE");
			if(!dynaBean.containsKey(columnCode)){
				dynaBean.set(columnCode, oldDept.get(columnCode));
			}
		}
		DynaBean role=serviceTemplate.selectOne("JE_CORE_ROLE"," and DEPTID='"+dynaBean.getStr("DEPTID")+"' and ROLETYPE='DEPT'");
		if(role!=null){
			if(dynaBean.getStr("DEPTNAME")!=null){
				role.set("ROLENAME", dynaBean.getStr("DEPTNAME"));
			}
			if(dynaBean.getStr("DEPTCODE")!=null){
				role.set("ROLECODE", dynaBean.getStr("DEPTCODE"));
			}
			if(dynaBean.getStr("NODETYPE")!=null){
				role.set("NODETYPE", dynaBean.getStr("NODETYPE"));
			}
			if(dynaBean.getStr("JTGSID")!=null){
				role.set("JTGSID", dynaBean.getStr("JTGSID"));
			}
			if(dynaBean.getStr("JTGSMC")!=null){
				role.set("JTGSMC", dynaBean.getStr("JTGSMC"));
			}
			if(dynaBean.getStr("ZHMC")!=null){
				role.set("ZHMC", dynaBean.getStr("ZHMC"));
			}
			if(dynaBean.getStr("ZHID")!=null){
				role.set("ZHID", dynaBean.getStr("ZHID"));
			}
			if(dynaBean.getStr("TREEORDERINDEX")!=null){
				role.set("TREEORDERINDEX", dynaBean.getStr("TREEORDERINDEX"));
			}
			if(dynaBean.getStr("ORDERINDEX")!=null){
				role.set("ORDERINDEX", dynaBean.getStr("ORDERINDEX"));
			}
			role.set(BeanUtils.KEY_TABLE_CODE, "JE_CORE_ROLE");
			role=serviceTemplate.update(role);
		}
		//清除公司
//		if(!WebUtils.isSaas()){
		if(!oldDept.getStr("RANKCODE").equals(dynaBean.getStr("RANKCODE"))  && dynaBean.containsKey("RANKCODE") && "GS".equals(oldDept.getStr("RANKCODE")) && !"GS".equals(dynaBean.getStr("RANKCODE"))){
			//删除公司
			//删除关系中公司
			List<DynaBean> gses=serviceTemplate.selectList("JE_CORE_JDGSINFO", " AND JDGSINFO_GSID='"+dynaBean.getStr("DEPTID")+"' AND JDGSINFO_TYPE_CODE='SHOW'");
			for(DynaBean gs:gses){
				pcServiceTemplate.executeSql("DELETE FROM JE_CORE_JDGSINFO WHERE SY_PATH LIKE '%"+gs.getStr("JE_CORE_JDGSINFO_ID")+"%'  AND JDGSINFO_TYPE_CODE='SHOW'");
			}
			pcServiceTemplate.executeSql(" DELETE FROM JE_CORE_JDGSINFO WHERE JDGSINFO_GSID = '"+dynaBean.getStr("DEPTID")+"' AND JDGSINFO_TYPE_CODE='CASCADE'");
		}
		//处理公司
		DynaBean company=null;
		if("GS".equals(dynaBean.getStr("RANKCODE")) || (!dynaBean.containsKey("RANKCODE") && "GS".equals(oldDept.getStr("RANKCODE")))){
			company=serviceTemplate.selectOne("JE_CORE_JDGSINFO"," AND JDGSINFO_GSID='"+dynaBean.getStr("DEPTID")+"' AND JDGSINFO_TYPE_CODE='CASCADE'");
			if(company==null){
				company=new DynaBean("JE_CORE_JDGSINFO",false);
				company.set(BeanUtils.KEY_PK_CODE, "JE_CORE_JDGSINFO_ID");
				company.set("JE_CORE_JDGSINFO_ID", JEUUID.uuid());
				company.set("JDGSINFO_TYPE_CODE", "CASCADE");
				company.set("JDGSINFO_TYPE_NAME", "部门级联");
				company.set("JDGSINFO_YECJTYPE_CODE", "GS");
				company.set("JDGSINFO_YECJTYPE_NAME", "公司级");
				company.set("JDGSINFO_GSMC", dynaBean.getStr("DEPTNAME"));
				company.set("JDGSINFO_GSDM", dynaBean.getStr("DEPTCODE"));
				company.set("JDGSINFO_GSID", dynaBean.getStr("DEPTID"));
				company.set("SY_JTGSMC", dynaBean.getStr("DEPTNAME"));
				company.set("SY_JTGSID", company.getStr("JE_CORE_JDGSINFO_ID"));
				serviceTemplate.buildModelCreateInfo(company);
				company=serviceTemplate.insert(company);
				dynaBean.set("JTGSID", company.getStr("JE_CORE_JDGSINFO_ID"));
				dynaBean.set("JTGSMC", company.getStr("JDGSINFO_GSMC"));
				dynaBean.set("GSBMID", company.getStr("JDGSINFO_GSID"));
			}else{
				company.set("JDGSINFO_TYPE_CODE", "CASCADE");
				company.set("JDGSINFO_TYPE_NAME", "部门级联");
				company.set("JDGSINFO_YECJTYPE_CODE", "GS");
				company.set("JDGSINFO_YECJTYPE_NAME", "公司级");
				if(dynaBean.containsKey("DEPTNAME")){
					company.set("JDGSINFO_GSMC", dynaBean.getStr("DEPTNAME"));
					company.set("SY_JTGSMC", dynaBean.getStr("DEPTNAME"));
				}
				if(dynaBean.containsKey("DEPTCODE")){
					company.set("JDGSINFO_GSDM", dynaBean.getStr("DEPTCODE"));
				}
				company.set("JDGSINFO_GSID", dynaBean.getStr("DEPTID"));
				company.set("SY_JTGSID", company.getStr("JE_CORE_JDGSINFO_ID"));
				company=serviceTemplate.update(company);
				dynaBean.set("JTGSID", company.getStr("JE_CORE_JDGSINFO_ID"));
				dynaBean.set("JTGSMC", company.getStr("JDGSINFO_GSMC"));
				dynaBean.set("GSBMID", company.getStr("JDGSINFO_GSID"));
				//更改管理层级已选过的公司名称
				pcServiceTemplate.executeSql(" UPDATE JE_CORE_DEPARTMENT SET JTGSMC='"+company.getStr("JDGSINFO_GSMC","")+"' WHERE JTGSID='"+company.getStr("JE_CORE_JDGSINFO_ID","")+"'");
				pcServiceTemplate.executeSql(" UPDATE JE_CORE_ENDUSER SET JTGSMC='"+company.getStr("JDGSINFO_GSMC","")+"' WHERE JTGSID='"+company.getStr("JE_CORE_JDGSINFO_ID","")+"'");
				pcServiceTemplate.executeSql(" UPDATE JE_CORE_ROLE SET JTGSMC='"+company.getStr("JDGSINFO_GSMC","")+"' WHERE JTGSID='"+company.getStr("JE_CORE_JDGSINFO_ID","")+"'");
				pcServiceTemplate.executeSql(" UPDATE JE_CORE_JDGSINFO SET SY_JTGSMC='"+company.getStr("JDGSINFO_GSMC","")+"' WHERE SY_JTGSID='"+company.getStr("JE_CORE_JDGSINFO_ID","")+"'");
				//更新所有部门的名称
			}
		}
//		}
		//处理负责人
		if(!oldDept.getStr("CHARGEUSER","").equals(dynaBean.getStr("CHARGEUSER",""))){
			doDeptUser(dynaBean.getStr("CHARGEUSER",""), dynaBean.getStr("DEPTID"), true);
		}
		//如果父部门发生改变了
		if(StringUtil.isNotEmpty(dynaBean.getStr("PARENT")) && !dynaBean.getStr("PARENT","").equals(oldDept.getStr("PARENT"))){
//			pcServiceTemplate.executeSql(" UPDATE JE_CORE_DEPARTMENT SET PATH=replace(PATH,'"+oldDept.getStr("PARENT")+"','"+dynaBean.getStr("PARENT")+"'),PARENTPATH=replace(PARENTPATH,'"+oldDept.getStr("PARENT")+"','"+dynaBean.getStr("PARENT")+"') WHERE PATH LIKE '%"+dynaBean.getStr("DEPTID")+"%'");
			DynaBean newParent=serviceTemplate.selectOneByPk("JE_CORE_DEPARTMENT",dynaBean.getStr("PARENT"));
			dynaBean.set("PARENT",newParent.getStr("DEPTID"));
			dynaBean.set("PARENTCODE",newParent.getStr("DEPTCODE"));
			dynaBean.set("PARENTNAME",newParent.getStr("DEPTNAME"));
			dynaBean.set("TREEORDERINDEX", newParent.getStr("TREEORDERINDEX")+StringUtil.preFillUp("1", 6, '0'));
			dynaBean.set("LAYER", newParent.getInt("LAYER", 1)+1);
			dynaBean.set("PARENTPATH",newParent.getStr("PATH"));
			dynaBean.set("PATH",newParent.getStr("PATH")+"/"+dynaBean.getStr("DEPTID"));
			if(NodeType.LEAF.equals(newParent.getStr("NODETYPE"))){
				newParent.set("NODETYPE",NodeType.GENERAL);
				serviceTemplate.update(newParent);
			}
			DynaBean oldParent=serviceTemplate.selectOneByPk("JE_CORE_DEPARTMENT",oldDept.getStr("PARENT"));
			if(NodeType.GENERAL.equalsIgnoreCase(oldParent.getStr("NODETYPE"))){
				long childCount=serviceTemplate.selectCount("JE_CORE_DEPARTMENT"," AND DEPTID!='"+dynaBean.getStr("DEPTID")+"' AND PARENT='"+oldParent.getStr("DEPTID")+"'");
				if(childCount<=0){
					oldParent.set("NODETYPE",NodeType.LEAF);
					oldParent.set(BeanUtils.KEY_TABLE_CODE,"JE_CORE_DEPARTMENT");
					serviceTemplate.update(oldParent);
				}
			}
			cascadeChildDeptPath(dynaBean,"JE_CORE_DEPARTMENT","DEPTID");
			//部门角色处理
			DynaBean newDeptRole=serviceTemplate.selectOne("JE_CORE_ROLE"," AND ROLETYPE='DEPT' AND DEPTID='"+dynaBean.getStr("PARENT")+"'","ROLEID,NODETYPE");
			if(newDeptRole!=null){
				role.set("PARENT",newDeptRole.getStr("ROLEID"));
				role.set("TREEORDERINDEX", newDeptRole.getStr("TREEORDERINDEX")+StringUtil.preFillUp("1", 6, '0'));
				role.set("LAYER", newDeptRole.getInt("LAYER", 1)+1);
				role.set("PARENTPATH",newDeptRole.getStr("PATH"));
				role.set("PATH",newDeptRole.getStr("PATH")+"/"+role.getStr("ROLEID"));
				cascadeChildDeptPath(role,"JE_CORE_ROLE","ROLEID");
//				pcServiceTemplate.executeSql(" UPDATE JE_CORE_ROLE SET PATH=replace(PATH,'"+role.getStr("PARENT")+"','"+newDeptRole.getStr("ROLEID")+"'),PARENTPATH=replace(PARENTPATH,'"+role.getStr("PARENT")+"','"+newDeptRole.getStr("ROLEID")+"') WHERE PATH LIKE '%"+role.getStr("ROLEID")+"%'");
				role.set(BeanUtils.KEY_TABLE_CODE,"JE_CORE_ROLE");
				serviceTemplate.update(role);
				if(NodeType.LEAF.equals(newDeptRole.getStr("NODETYPE"))){
					newDeptRole.set("NODETYPE",NodeType.GENERAL);
					serviceTemplate.update(newDeptRole);
				}
			}
		}
		pcServiceTemplate.executeSql(" UPDATE JE_CORE_ENDUSER SET DEPTORDERINDEX='"+StringUtil.getDefaultValue(dynaBean.getStr("TREEORDERINDEX"),oldDept.getStr("TREEORDERINDEX"))+"',DEPTNAME='"+dynaBean.getStr("DEPTNAME")+"',DEPTCODE='"+dynaBean.getStr("DEPTCODE")+"',JTGSMC='"+dynaBean.getStr("JTGSMC")+"',JTGSID='"+dynaBean.getStr("JTGSID")+"' WHERE DEPTID='"+dynaBean.getStr("DEPTID")+"'");

	}
	private void cascadeChildDeptPath(DynaBean newParent,String tableCode,String pkCode){
		List<DynaBean> childs=serviceTemplate.selectList(tableCode," AND PARENT='"+newParent.getStr(pkCode)+"' ORDER BY ORDERINDEX ASC");
		for(DynaBean child:childs){
			child.set("PARENTCODE",newParent.getStr("DEPTCODE"));
			child.set("PARENTNAME",newParent.getStr("DEPTNAME"));
			child.set("TREEORDERINDEX", newParent.getStr("TREEORDERINDEX")+StringUtil.preFillUp("1", 6, '0'));
			child.set("LAYER", newParent.getInt("LAYER", 1)+1);
			child.set("PARENTPATH",newParent.getStr("PATH"));
			child.set("PATH",newParent.getStr("PATH")+"/"+child.getStr(pkCode));
			child=serviceTemplate.update(child);
			cascadeChildDeptPath(child,tableCode,pkCode);
		}
		if(childs.size()>0){
			newParent.set("NODETYPE",NodeType.GENERAL);
		}
	}

	/**
	 * TODO 暂不明确
	 * @param ids 多人id
	 * @param deleteFlag 是否真删除
	 * @param delAll 删除所有
	 * @return
	 */
	@Override
	public Integer doRemove(String ids,Boolean deleteFlag,Boolean delAll) {
		// TODO Auto-generated method stub
		String[] idArray=ids.split(",");
		for(String id:idArray){
			if(deleteFlag){
				//清除用户所有静态化  这里不判断模式和状态，  以免会有脏数据产生
				staticizeManager.clearUserStatize(" AND USERID IN (SELECT USERID FROM JE_CORE_ENDUSER WHERE DEPTID in (SELECT DEPTID FROM JE_CORE_DEPARTMENT WHERE PATH LIKE '%"+id+"%'))",true);
				//删除部门对应的角色
				pcServiceTemplate.executeSql("DELETE FROM JE_CORE_ROLE_USER WHERE ROLEID IN (SELECT ROLEID FROM JE_CORE_ROLE WHERE ROLETYPE='DEPT' AND DEPTID IN (SELECT DEPTID FROM JE_CORE_DEPARTMENT WHERE PATH LIKE '%"+id+"%'))");
				pcServiceTemplate.executeSql("DELETE FROM JE_CORE_ROLE_PERM WHERE ROLEID IN (SELECT ROLEID FROM JE_CORE_ROLE WHERE ROLETYPE='DEPT' AND DEPTID IN (SELECT DEPTID FROM JE_CORE_DEPARTMENT WHERE PATH LIKE '%"+id+"%'))");
				pcServiceTemplate.executeSql("DELETE FROM JE_CORE_ROLE WHERE ROLETYPE='DEPT' AND DEPTID IN (SELECT DEPTID FROM JE_CORE_DEPARTMENT WHERE PATH LIKE '%"+id+"%')");
				pcServiceTemplate.executeSql("DELETE FROM JE_CORE_DEPTREG WHERE DEPTID IN (SELECT DEPTID FROM JE_CORE_DEPARTMENT WHERE PATH LIKE '%"+id+"%')");
				if(delAll){
					//删除用户的角色
					pcServiceTemplate.executeSql("DELETE FROM JE_CORE_ROLE_USER WHERE USERID IN (SELECT USERID FROM JE_CORE_ENDUSER WHERE DEPTID in (SELECT DEPTID FROM JE_CORE_DEPARTMENT WHERE PATH LIKE '%"+id+"%'))");
					pcServiceTemplate.executeSql("DELETE FROM JE_CORE_WORKGROUP_USER WHERE USERID IN (SELECT USERID FROM JE_CORE_ENDUSER WHERE DEPTID in (SELECT DEPTID FROM JE_CORE_DEPARTMENT WHERE PATH LIKE '%"+id+"%'))");
					pcServiceTemplate.executeSql("DELETE FROM JE_CORE_SENTRY_USER WHERE USERID IN (SELECT USERID FROM JE_CORE_ENDUSER WHERE DEPTID in (SELECT DEPTID FROM JE_CORE_DEPARTMENT WHERE PATH LIKE '%"+id+"%'))");
					pcServiceTemplate.executeSql("DELETE FROM JE_CORE_ADMIN_USER WHERE USERID IN (SELECT USERID FROM JE_CORE_ENDUSER WHERE DEPTID in (SELECT DEPTID FROM JE_CORE_DEPARTMENT WHERE PATH LIKE '%"+id+"%'))");
					pcServiceTemplate.executeSql("DELETE FROM JE_CORE_AT WHERE USERID IN (SELECT USERID FROM JE_CORE_ENDUSER WHERE DEPTID in (SELECT DEPTID FROM JE_CORE_DEPARTMENT WHERE PATH LIKE '%"+id+"%'))");

					//发送一些消息
					List<DynaBean> users = serviceTemplate.selectList("JE_CORE_ENDUSER"," and DEPTID in (SELECT DEPTID FROM JE_CORE_DEPARTMENT WHERE PATH LIKE '%"+id+"%')");
					List<String> userIds = Lists.newArrayList();
					for (DynaBean user : users) {
						userIds.add(user.getStr("USERID"));
					}

					try {
						PushMsgHttpUtil.delGroupDetail(SecurityUserHolder.getCurrentUser().getZhId(),StringUtil.buildSplitString(ArrayUtils.getArray(userIds),","));
					} catch (Exception e) {
						//log.error("请求删除方法出现错误"+e.getMessage(),e);
						e.printStackTrace();
					}


					pcServiceTemplate.executeSql("DELETE FROM JE_CORE_ENDUSER WHERE DEPTID in (SELECT DEPTID FROM JE_CORE_DEPARTMENT WHERE PATH LIKE '%"+id+"%')");
				}else{
					//TODO 这个SQL语句有问题   外键约束 DEPTID 不能设置为空DEPTID='',
					pcServiceTemplate.executeSql("UPDATE JE_CORE_ENDUSER SET DEPTID='',DEPTCODE='',DEPTNAME='',ISMANAGER='0' WHERE DEPTID IN (SELECT DEPTID FROM JE_CORE_DEPARTMENT where PATH like '%"+id+"%')");
				}
				if(!WebUtils.isSaas()){
					//删除公司信息
					pcServiceTemplate.executeSql("DELETE FROM JE_CORE_JDGSINFO WHERE JDGSINFO_TYPE_CODE='CASCADE' AND JDGSINFO_GSID IN (SELECT DEPTID FROM JE_CORE_DEPARTMENT where PATH like '%"+id+"%')");
				}
				//TODO 这有外键约束  无法删除
				pcServiceTemplate.executeSql("DELETE FROM JE_CORE_DEPARTMENT where PATH like '%"+id+"%'");
			}else{
				pcServiceTemplate.executeSql("UPDATE JE_CORE_DEPARTMENT SET STATUS='0' where PATH like '%"+id+"%'");
				pcServiceTemplate.executeSql("UPDATE JE_CORE_ENDUSER SET STATUS='0',VALID='0' WHERE DEPTID IN (SELECT DEPTID FROM JE_CORE_DEPARTMENT WHERE STATUS='0' AND PATH like '%"+id+"%')");
				pcServiceTemplate.executeSql("UPDATE JE_CORE_ROLE SET STATUS='0' where DEPTID IN (SELECT DEPTID FROM JE_CORE_DEPARTMENT WHERE PATH LIKE '%"+id+"%') AND ROLETYPE='DEPT'");
				if(!WebUtils.isSaas()){
					pcServiceTemplate.executeSql("UPDATE JE_CORE_JDGSINFO SET SY_STATUS='0' WHERE JDGSINFO_TYPE_CODE='CASCADE' AND JDGSINFO_GSID IN (SELECT DEPTID FROM JE_CORE_DEPARTMENT where PATH like '%"+id+"%')");
				}
			}
		}
		userManager.removeLeaderAndBranch(SecurityUserHolder.getCurrentUser().getZhId());
		//刷新SAAS缓存
		if(WebUtils.isSaas()){
			saasManager.getSaasPermInfo(SecurityUserHolder.getCurrentUser().getZhId(),"1");
		}
		Integer count=idArray.length;
		return count;
	}

	/**
	 * 取消删除
	 * @param ids 多份id
	 * @return
	 */
	public Integer doEnabled(String ids) {
		// TODO Auto-generated method stub
		String[] idArray=ids.split(",");
		for(String id:idArray){
			pcServiceTemplate.executeSql("UPDATE JE_CORE_DEPARTMENT SET STATUS='1' where PATH like '%"+id+"%'");
			//启用这里有点问题，， 如果是本身就是人员禁用的。。 那么在这个会被启用
			pcServiceTemplate.executeSql("UPDATE JE_CORE_ENDUSER SET STATUS='1' WHERE DEPTID IN (SELECT DEPTID FROM JE_CORE_DEPARTMENT WHERE STATUS='1' AND PATH like '%"+id+"%')");
			pcServiceTemplate.executeSql("UPDATE JE_CORE_ROLE SET STATUS='1' where DEPTID IN (SELECT DEPTID FROM JE_CORE_DEPARTMENT WHERE PATH LIKE '%"+id+"%') AND ROLETYPE='DEPT'");
			if(!WebUtils.isSaas()){
				pcServiceTemplate.executeSql("UPDATE JE_CORE_JDGSINFO SET SY_STATUS='1' WHERE JDGSINFO_TYPE_CODE='CASCADE' AND JDGSINFO_GSID IN (SELECT DEPTID FROM JE_CORE_DEPARTMENT where PATH like '%"+id+"%')");
			}
		}
		userManager.removeLeaderAndBranch(SecurityUserHolder.getCurrentUser().getZhId());
		//刷新SAAS缓存
		if(WebUtils.isSaas()){
			saasManager.getSaasPermInfo(SecurityUserHolder.getCurrentUser().getZhId(),"1");
		}
		Integer count=idArray.length;
		return count;
	}

	/**
	 * 导入用户
	 * @param userIds 用户id
	 * @param dynaBean
	 * @return
	 */
	@Override
	public List<DynaBean> implUsers(String userIds, DynaBean dynaBean) {
		// TODO Auto-generated method stub
		String deptId=dynaBean.getStr("DEPTID");
		List<DynaBean> users=new ArrayList<DynaBean>();
		for(String id:userIds.split(",")){
			DynaBean user=serviceTemplate.selectOneByPk("JE_CORE_ENDUSER", id);
			String userCode=user.getStr("USERCODE");
			user.set("USERID", null);
			if("1".equals(user.getStr("SHADOW"))){
				userCode=userCode.replace(user.getStr("DEPTID"), deptId);
			}else{
				userCode=userCode+"_"+deptId;
			}
			user.set("USERCODE", userCode);
			user.set("SHADOW", "1");
			user.set("DEPTID", deptId);
			user.set("SENTRYNAMES", "");
			user.set("SENTRYCODES", "");
			user.set("SENTRYIDS", "");
			user.set("ROLENAMES", "");
			user.set("ROLECODES", "");
			//virus 改20131016
			user.set("ICONCLS", "jeicon jeicon-users-fs");
			user.set("DEPTNAME", dynaBean.getStr("DEPTNAME"));
			user.set("DEPTCODE", dynaBean.getStr("DEPTCODE"));
			DynaBean haveUser=serviceTemplate.selectOne("JE_CORE_ENDUSER"," and USERCODE='"+userCode+"'");
			if(haveUser==null){
				DynaBean inserted=serviceTemplate.insert(user);
				users.add(inserted);
				DynaBean role=serviceTemplate.selectOne("JE_CORE_ROLE"," and DEPTID='"+deptId+"' AND ROLETYPE='DEPT'");
				if(role!=null){
					pcServiceTemplate.executeSql("INSERT INTO JE_CORE_ROLE_USER(ROLEID,USERID) VALUES('"+role.getStr("ROLEID")+"','"+inserted.getStr("USERID")+"')");
				}
			}
		}
		return users;
	}

	/**
	 * 部门移动
	 * @param dynaBean
	 * @param request
	 * @return
	 */
	@Override
	public DynaBean deptMove(DynaBean dynaBean,HttpServletRequest request) {
		// TODO Auto-generated method stub
		String tableCode=dynaBean.getStr(BeanUtils.KEY_TABLE_CODE);
		String pkName=BeanUtils.getInstance().getPKeyFieldNames(tableCode);
		DynaBean bean=serviceTemplate.selectOneByPk(tableCode, dynaBean.getStr(pkName));
		String newParentId=request.getParameter("SY_PARENT");
		String oldParentId=bean.getStr("PARENT");
		String oldTreeOrderIndex=bean.getStr("TREEORDERINDEX");
		dynaBean.set("PATH", request.getParameter("SY_PATH"));
		dynaBean.set("LAYER", request.getParameter("SY_LAYER"));
		DynaBean newRole=serviceTemplate.selectOne("JE_CORE_ROLE", " AND ((DEPTID='"+newParentId+"' AND ROLETYPE='DEPT') OR (ROLEID='"+newParentId+"' AND ROLEID='ROOT'))");
		DynaBean role=serviceTemplate.selectOne("JE_CORE_ROLE", " AND DEPTID='"+dynaBean.getStr(pkName)+"' AND ROLETYPE='DEPT'");
		role.set("PATH", newRole.getStr("PATH")+"/"+role.getStr("ROLEID"));
		role.set("PARENTPATH", newRole.getStr("PATH"));
		role.set("PARENT", newRole.getStr("ROLEID"));
		role.set("LAYER", dynaBean.get("LAYER"));
		roleManager.roleMove(role);
		Integer chaLayer=dynaBean.getInt("LAYER")-bean.getInt("LAYER");
		String oldPath=bean.getStr("PATH");
		String oldParentPath=bean.getStr("PARENTPATH","");
//		bean.set("PATH", dynaBean.getStr("PATH")+"/"+dynaBean.getStr(pkName));
//		bean.set("PARENTPATH", dynaBean.getStr("PATH"));
//		bean.set("PARENT", newParentId);
//		bean.set("LAYER", dynaBean.get("LAYER"));
		bean.set(BeanUtils.KEY_TABLE_CODE,tableCode);
		DynaBean parent=serviceTemplate.selectOneByPk(tableCode, newParentId);
		bean.setStr("PARENTNAME",parent.getStr("DEPTNAME"));
		bean.setStr("PARENTCODE",parent.getStr("DEPTCODE"));
		bean.set("PARENTTEXT",parent.getStr("ROLENAME"));
		bean.set("PARENTCODE",parent.getStr("ROLECODE"));
		bean.set("LAYER", parent.getInt("LAYER",0)+1);
		bean.set("PATH", parent.getStr("PATH")+"/"+dynaBean.getStr("ROLEID"));
		bean.set("PARENTPATH", parent.getStr("PATH"));
		if(NodeType.LEAF.equals(parent.getStr("NODETYPE"))){
			pcServiceTemplate.executeSql("UPDATE JE_CORE_DEPARTMENT SET NODETYPE='"+NodeType.GENERAL+"' WHERE DEPTID='"+ newParentId+"'");
		}
		bean.set("TREEORDERINDEX", parent.getStr("TREEORDERINDEX"));
		roleManager.generateTreeOrderIndex(bean);
		//更新当前节点下所有孩子的路径信息
		DynaBean updated=serviceTemplate.update(bean);
		pcServiceTemplate.executeSql(" UPDATE JE_CORE_ENDUSER SET DEPTORDERINDEX='"+updated.getStr("TREEORDERINDEX")+"' WHERE DEPTID='"+updated.getStr("DEPTID")+"'");
		List<DynaBean> childDepts=serviceTemplate.selectList("JE_CORE_DEPARTMENT"," AND PATH LIKE '%"+oldPath+"%' AND "+pkName+"!='"+ dynaBean.getStr(pkName)+"'");
		for(DynaBean childDept:childDepts){
			childDept.set("PATH",childDept.getStr("PATH","").replace(oldPath,bean.getStr("PATH")));
			childDept.set("PARENTPATH",childDept.getStr("PARENTPATH","").replace(oldParentPath,bean.getStr("PARENTPATH")));
			childDept.set("LAYER",childDept.getInt("LAYER",0)+chaLayer);
			childDept.set("TREEORDERINDEX",bean.getStr("TREEORDERINDEX")+(childDept.getStr("TREEORDERINDEX","").substring(oldTreeOrderIndex.length())));
			serviceTemplate.update(childDept);
			pcServiceTemplate.executeSql(" UPDATE JE_CORE_ENDUSER SET DEPTORDERINDEX='"+childDept.getStr("TREEORDERINDEX")+"' WHERE DEPTID='"+childDept.getStr("DEPTID")+"'");
//			TREEORDERINDEX=('"+bean.getStr("TREEORDERINDEX")+"'+"+subStringFunction+"(TREEORDERINDEX,"+(oldTreeOrderIndex.length()+1)+","+lengthFunction+"(TREEORDERINDEX)-"+oldTreeOrderIndex.length()+"))
		}
//		pcServiceTemplate.executeSql("UPDATE "+tableCode+" SET PATH=REPLACE(PATH,'"+oldPath+"','"+bean.getStr("PATH")+"'),PARENTPATH=REPLACE(PARENTPATH,'"+oldParentPath+"','"+bean.getStr("PARENTPATH")+"'),LAYER=(LAYER+"+chaLayer+"),TREEORDERINDEX=('"+bean.getStr("TREEORDERINDEX")+"'+"+subStringFunction+"(TREEORDERINDEX,"+(oldTreeOrderIndex.length()+1)+","+lengthFunction+"(TREEORDERINDEX)-"+oldTreeOrderIndex.length()+")) WHERE  PATH LIKE '%"+oldPath+"%' AND "+pkName+"!='"+ dynaBean.getStr(pkName)+"' AND "+lengthFunction+"(TREEORDERINDEX)>="+oldTreeOrderIndex.length());
		//维护树形的NODETYPE
		Long count=pcServiceTemplate.countBySql("select count(*) from JE_CORE_DEPARTMENT where PARENT='"+oldParentId+"'");
		if(count<=0){
			DynaBean one=serviceTemplate.selectOneByPk("JE_CORE_DEPARTMENT",oldParentId);
			if(NodeType.GENERAL.equals(one.getStr("NODETYPE"))){
				one.set(BeanUtils.KEY_TABLE_CODE, "JE_CORE_DEPARTMENT");
				one.set("NODETYPE", NodeType.LEAF);
				serviceTemplate.update(one);
			}
		}
		return updated;
	}

	/**
	 * 部门数据导入
	 * @param dynaBean
	 * @return
	 */
	@Override
	public DynaBean implDeptData(DynaBean dynaBean) {
		// TODO Auto-generated method stub
		String parentDeptName=dynaBean.getStr("PARENTNAME");
		String deptCode=dynaBean.getStr("DEPTCODE");
		String parentDeptCode=dynaBean.getStr("PARENTCODE");
		//处理名称和编码
		if(StringUtil.isEmpty(deptCode)){
			deptCode="DEPT_"+PingYinUtil.getInstance().getFirstSpell(deptCode).toUpperCase();
			Long count=serviceTemplate.selectCount("JE_CORE_DEPARTMENT"," AND DEPTCODE='"+deptCode+"'");
			if(count>0){
				deptCode=deptCode+(count+1);
			}
		}
		dynaBean.set("DEPTCODE", deptCode);
		DynaBean parent=null;
		//找到父节点
		if(StringUtil.isNotEmpty(parentDeptCode)){
			parent=serviceTemplate.selectOne("JE_CORE_DEPARTMENT", " AND DEPTCODE='"+parentDeptCode+"'");
		}
		if(StringUtil.isNotEmpty(parentDeptName) && parent==null){
			parent=serviceTemplate.selectOne("JE_CORE_DEPARTMENT", " AND DEPTNAME='"+parentDeptName+"'");
		}
		if(parent==null && StringUtil.isEmpty(parentDeptCode) && StringUtil.isEmpty(parentDeptName)){
			parent=serviceTemplate.selectOne("JE_CORE_DEPARTMENT", " AND DEPTID='ROOT'");
		}
		String pkValue=JEUUID.uuid();
		dynaBean.set("DEPTID", pkValue);
		//创建信息
		EndUser currentUser=SecurityUserHolder.getCurrentUser();
		dynaBean.set("RANKCODE", "BM");
		dynaBean.set("RANKNAME", "部门");
		dynaBean.set("CREATEUSERID", currentUser.getId());
		dynaBean.set("CREATEUSER", currentUser.getUserCode());
		dynaBean.set("CREATEUSERNAME", currentUser.getUsername());
		dynaBean.set("CREATEORG", currentUser.getDept().getDeptCode());
		dynaBean.set("CREATEORGNAME", currentUser.getDept().getDeptName());
		dynaBean.set("CREATEORGID", currentUser.getDept().getDeptId());
		dynaBean.set("CREATETIME", DateUtils.formatDateTime(new Date()));
		if(parent!=null){
			dynaBean.set("PARENTPATH", parent.getStr("PATH"));
			dynaBean.set("PARENTNAME", parent.getStr("DEPTNAME"));
			dynaBean.set("PARENTCODE", parent.getStr("DEPTCODE"));
			dynaBean.set("PARENT", parent.getStr("DEPTID"));
			dynaBean.set("LAYER", parent.getInt("LAYER")+1);
			dynaBean.set("PATH", parent.getStr("PATH")+"/"+pkValue);
			dynaBean.set("NODETYPE", "LEAF");
			dynaBean.set("STATUS", "1");
			dynaBean.set("TREEORDERINDEX", parent.getStr("TREEORDERINDEX"));
			roleManager.generateTreeOrderIndex(dynaBean);
			dynaBean=doSave(dynaBean);
			if("LEAF".equals(parent.getStr("NODETYPE"))){
				pcServiceTemplate.executeSql("UPDATE JE_CORE_DEPARTMENT SET NODETYPE='"+NodeType.GENERAL+"' WHERE DEPTID='"+parent.getStr("DEPTID")+"'");
			}
		}
		return dynaBean;
	}

	/**
	 * 公司信息级联
	 * @param jtgsIds
	 */
	@Override
	public void syncCompanyInfo(String jtgsIds) {
		// TODO Auto-generated method stub
		String whereSql=" AND JDGSINFO_TYPE_CODE='CASCADE'";
		if(StringUtil.isNotEmpty(jtgsIds)){
			whereSql+=" AND JE_CORE_JDGSINFO_ID IN ("+StringUtil.buildArrayToString(jtgsIds.split(","))+")";
		}
		EndUser currentUser=SecurityUserHolder.getCurrentUser();
		if(WebUtils.isSaas()){
//			if(currentUser.getSaas()){
//				whereSql=" AND ZHID='"+currentUser.getZhId()+"'";
//			}else{
			whereSql+=" AND SY_ZHID='"+currentUser.getZhId()+"'";
//			}
		}
		List<DynaBean> jtgs=serviceTemplate.selectList("JE_CORE_JDGSINFO", whereSql);
		for(DynaBean jg:jtgs){
			DynaBean dept=serviceTemplate.selectOne("JE_CORE_DEPARTMENT", " AND JTGSID='"+jg.getStr("JE_CORE_JDGSINFO_ID")+"' AND RANKCODE='GS'");
			if(dept!=null){
				jg.set("JDGSINFO_GSMC", dept.getStr("DEPTNAME"));
				jg.set("JDGSINFO_GSDM", dept.getStr("DEPTCODE"));
				jg=serviceTemplate.update(jg);
				syncChildDept(dept, jg,dept.getStr("DEPTID"));
			}
		}
	}

	/**
	 * 同步用户的是否主管信息
	 * @param dept
	 */
	@Override
	public void syncUserZgld(DynaBean dept) {
		String deptId=dept.getStr("DEPTID");
		String zgldId=dept.getStr("ZGLDID");
		pcServiceTemplate.executeSql(" UPDATE JE_CORE_ENDUSER SET ISMANAGER='0' WHERE DEPTID='"+deptId+"'");
		if(StringUtil.isNotEmpty(zgldId)) {
			pcServiceTemplate.executeSql(" UPDATE JE_CORE_ENDUSER SET ISMANAGER='1' WHERE DEPTID='" + deptId + "' AND USERID IN ("+StringUtil.buildArrayToString(zgldId.split(","))+")");
		}
	}

	private void syncChildDept(DynaBean dept,DynaBean jg,String gsbmId){
		List<DynaBean> users=serviceTemplate.selectList("JE_CORE_ENDUSER", " AND DEPTID='"+dept.getStr("DEPTID")+"'");
		for(DynaBean u:users){
			u.set("DEPTNAME", dept.getStr("DEPTNAME"));
			u.set("DEPTCODE", dept.getStr("DEPTCODE"));
			u.set("DEPTORDERINDEX", dept.getStr("TREEORDERINDEX"));
			u.set("JTGSMC", jg.getStr("JDGSINFO_GSMC"));
			u.set("JTGSDM", jg.getStr("JDGSINFO_GSDM"));
			u.set("JTGSID", jg.getStr("JE_CORE_JDGSINFO_ID"));
			u.set("GSBMID", gsbmId);
			serviceTemplate.update(u);
		}
		dept.set("JTGSMC", jg.getStr("JDGSINFO_GSMC"));
		dept.set("JTGSID",  jg.getStr("JE_CORE_JDGSINFO_ID"));
		dept.set("JTGSDM", jg.getStr("JDGSINFO_GSDM"));
		dept.set("GSBMID", gsbmId);
		dept=serviceTemplate.update(dept);
		DynaBean role=serviceTemplate.selectOne("JE_CORE_ROLE", " AND ROLETYPE='DEPT' AND DEPTID='"+dept.getStr("DEPTID")+"'");
		if(role!=null){
			role.set("JTGSMC", jg.getStr("JDGSINFO_GSMC"));
			role.set("JTGSID", jg.getStr("JE_CORE_JDGSINFO_ID"));
			serviceTemplate.update(role);
		}
		List<DynaBean> childs=serviceTemplate.selectList("JE_CORE_DEPARTMENT", " AND PARENT='"+dept.getStr("DEPTID")+"'");
		for(DynaBean child:childs){
			String rankCode=child.getStr("RANKCODE");
			if(!"GS".equals(rankCode)){
				syncChildDept(child, jg, gsbmId);
			}
		}
	}

	/**
	 * 获取层级关系树形
	 * @param dicInfoVo
	 * @return
	 */
	@Override
	public JSONTreeNode getCjglTree(DicInfoVo dicInfoVo) {
		// TODO Auto-generated method stub
		String ddCode=dicInfoVo.getDdCode();
		EndUser currentUser=SecurityUserHolder.getCurrentUser();
		Map<String,String> params=dicInfoVo.getParams();
		DynaBean dicBean=serviceTemplate.selectOne("JE_CORE_DICTIONARY", " AND DICTIONARY_DDCODE='"+ddCode+"'");
		String ddWhereSql=dicBean.getStr("DICTIONARY_WHERESQL");
		String ddType=dicBean.getStr("DICTIONARY_ORDERSQL");
		//找到部门树
		//找到root节点
		DynaBean rootBean=serviceTemplate.selectOne("JE_CORE_VJTGSUSER"," AND SY_TYPE='DEPT' AND SY_NODETYPE='ROOT' "+ddWhereSql);
		String rootId=rootBean.getStr("ID");
		DynaBean table=BeanUtils.getInstance().getResourceTable("JE_CORE_VJTGSUSER");
		List<DynaBean> columns=(List<DynaBean>) table.get(BeanUtils.KEY_TABLE_COLUMNS);
		JSONTreeNode template = BeanUtils.getInstance().buildJSONTreeNodeTemplate(columns);
		String querySql=" AND SY_TYPE='DEPT' "+ddWhereSql+" "+dicInfoVo.getWhereSql();
		List<String> sqls=new ArrayList<String>();
		DynaBean funcInfo=null;
		String gxId="";
		if(params.containsKey("FUNCCODE")){
			String funcCode=params.get("FUNCCODE");
			funcInfo=serviceTemplate.selectOne("JE_CORE_FUNCINFO"," AND FUNCINFO_FUNCCODE='"+funcCode+"'");
			if(funcInfo!=null){
				gxId=funcInfo.getStr("FUNCINFO_USECJGLIDS");
			}
		}
		if(params.containsKey("SELF_COMPANY") && "1".equals(params.get("SELF_COMPANY"))){
			sqls.add(" SY_JTGSID = '"+currentUser.getJtgsId()+"' ");
		}
		if(params.containsKey("MONITOR_COMPANY") && "1".equals(params.get("MONITOR_COMPANY"))){
			DynaBean jtgs=serviceTemplate.selectOne("JE_CORE_JDGSINFO", " AND JDGSINFO_GSID='"+currentUser.getGsbmId()+"' AND JDGSINFO_TYPE_CODE='SHOW' AND JE_CORE_YWCJ_ID='"+gxId+"'");
			if(jtgs!=null){
				sqls.add(" SY_JTGSID IN ("+StringUtil.buildArrayToString(jtgs.getStr("JDGSINFO_JGGS_ID","").split(","))+") ");
			}
		}
		if(params.containsKey("MONITOR_COMPANY_DEPT") && "1".equals(params.get("MONITOR_COMPANY_DEPT"))){
			DynaBean jtgs=serviceTemplate.selectOne("JE_CORE_JDGSINFO", " AND JDGSINFO_GSID='"+currentUser.getDeptId()+"' AND JDGSINFO_TYPE_CODE='SHOW' AND JE_CORE_YWCJ_ID='"+gxId+"'");
			if(jtgs!=null){
				sqls.add(" SY_JTGSID IN ("+StringUtil.buildArrayToString(jtgs.getStr("JDGSINFO_JGGS_ID","").split(","))+") ");
			}
		}
		if(sqls.size()>0){
			querySql+= " AND ("+StringUtil.buildSplitString(ArrayUtils.getArray(sqls), " OR ")+" )";
		}
		String orderSql=" ORDER BY SY_PARENT ASC,SY_ORDERINDEX ASC";
		Set<Entry> ddSet=new HashSet<Entry>();
		//加入登录信息
		ddSet.addAll(SecurityUserHolder.getCurrentInfo(currentUser).entrySet());
		//加入用户变量
		ddSet.addAll(FrontCacheManager.getCacheValues().entrySet());
		ddSet.addAll(BackCacheManager.getCacheValues().entrySet());
		//加入系统设置
		ddSet.addAll(WebUtils.getAllSysVar().entrySet());
		QueryInfo queryInfo = new QueryInfo();
		if(StringUtil.isNotEmpty(querySql)){
			querySql=StringUtil.parseKeyWord(querySql, ddSet);
		}
		if(StringUtil.isNotEmpty(orderSql)){
			orderSql=StringUtil.parseKeyWord(orderSql, ddSet);
		}
		if("1".equals(WebUtils.getSysVar("JE_SYS_COMPANYWHERE"))){
			List<DynaBean> lists=serviceTemplate.selectList("JE_CORE_VJTGSUSER", querySql);
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
					String path=bean.getStr("SY_PATH");
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
					querySql=" AND ((1=1 "+querySql+") OR ID IN ("+StringUtil.buildArrayToString(ArrayUtils.getArray(parentIds))+"))";
				}
			}
		}
		queryInfo.setWhereSql(querySql);
		queryInfo.setOrderSql(orderSql);
		List<JSONTreeNode> jsonTreeNodeList = pcServiceTemplate.getJsonTreeNodeList(rootId, "JE_CORE_VJTGSUSER", template, queryInfo);
		List<JSONTreeNode> users=new ArrayList<JSONTreeNode>();
		if("DEPTUSER".equals(ddType)){
			List<DynaBean> userLists=serviceTemplate.selectList("JE_CORE_VJTGSUSER", " AND SY_TYPE='USER'"+" ORDER BY SY_TREEORDERINDEX asc");
			for(DynaBean user:userLists){
				JSONTreeNode node=TreeUtil.buildTreeNode(user.getStr("ID"), user.getStr("TEXT"), user.getStr("CODE"), user.getStr("ID"), "USER", user.getStr("ICONCLS"), user.getStr("SY_PARENT"));
				node.setNodeType(NodeType.LEAF);
				node.setLeaf(true);
				node.setDisabled("0");
				node.setBean(user.getValues());
				node.setNodeInfo(dicInfoVo.getFieldCode());
				users.add(node);
			}
		}
		Map<String,String> idInfos=new HashMap<String,String>();
		for(JSONTreeNode deptNode:jsonTreeNodeList){
			if(rootId.equals(deptNode.getId())){
				rootId=deptNode.getNodeInfo();
			}
			idInfos.put(deptNode.getId(), deptNode.getNodeInfo());
			deptNode.setId(deptNode.getNodeInfo());
			if("DEPTUSER".equals(ddType)){
				if(!NodeType.ROOT.equals(deptNode.getNodeType())){
					deptNode.setNodeType(NodeType.GENERAL);
				}
				deptNode.setLeaf(false);
				deptNode.setDisabled("1");
				for(JSONTreeNode user:users){
					if(user.getParent().equals(deptNode.getId())){
						user.setId(user.getId()+dicInfoVo.getIdSuffix());
						user.setParent(user.getParent()+dicInfoVo.getIdSuffix());
						deptNode.getChildren().add(user);
					}
				}
			}
		}
		for(JSONTreeNode node:jsonTreeNodeList){
			String parentId=node.getParent();
			if(idInfos.containsKey(parentId)){
				node.setParent(idInfos.get(parentId));
			}
			if(NodeType.ROOT.equals(node.getNodeType())){
				node.setId(node.getId()+dicInfoVo.getIdSuffix());
				node.setNodeInfo(dicInfoVo.getFieldCode());
			}else{
				node.setNodeInfo(dicInfoVo.getFieldCode());
				node.setId(node.getId()+dicInfoVo.getIdSuffix());
				node.setParent(node.getParent()+dicInfoVo.getIdSuffix());
			}
		}
		JSONTreeNode rootNode = pcServiceTemplate.buildJSONNewTree(jsonTreeNodeList,rootId);
		rootNode.setText(dicInfoVo.getFieldName());
		rootNode.setParent("ROOT");
//		rootNode.setId("ROOT_" + nodeInfo);// + rootNode.getId());
		rootNode.setIconCls("jeicon jeicon-tree-query");
		return rootNode;
	}
	private void doDeptUser(String fzrCodes,String deptId,Boolean delete){
		if(delete){
			pcServiceTemplate.executeSql(" DELETE FROM JE_CORE_DEPTREG WHERE TYPE='FZR' and DEPTID='"+deptId+"'");
		}
		if(StringUtil.isNotEmpty(fzrCodes)){
			List<DynaBean> users=serviceTemplate.selectList("JE_CORE_ENDUSER", " and USERCODE IN ("+StringUtil.buildArrayToString(fzrCodes.split(","))+")","USERID");
			for(DynaBean user:users){
				DynaBean userDept=new DynaBean("JE_CORE_DEPTREG",true);
				userDept.set("USERID", user.getStr("USERID"));
				userDept.set("DEPTID", deptId);
				userDept.set("TYPE", "FZR");
				serviceTemplate.insert(userDept);
			}
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
	@Resource(name="roleManager")
	public void setRoleManager(RoleManager roleManager) {
		this.roleManager = roleManager;
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
