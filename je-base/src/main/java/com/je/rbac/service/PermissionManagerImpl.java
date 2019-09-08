package com.je.rbac.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import com.je.cache.service.rbac.UserHomeCacheManager;
import com.je.saas.service.SaasManager;
import com.je.saas.vo.SaasPermInfo;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.slf4j.Logger; import org.slf4j.LoggerFactory;
import org.jbpm.api.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.je.cache.service.MenuStaticizeCacheManager;
import com.je.core.constants.ConstantVars;
import com.je.core.constants.JECoreMode;
import com.je.core.constants.rbac.AdminPermType;
import com.je.core.constants.rbac.AuthorPermType;
import com.je.core.constants.rbac.PermExtendType;
import com.je.core.constants.rbac.PermType;
import com.je.core.constants.tree.NodeType;
import com.je.core.dao.PCDaoTemplateImpl;
import com.je.core.entity.QueryInfo;
import com.je.core.entity.extjs.JSONTreeNode;
import com.je.core.util.SecurityUserHolder;
import com.je.core.service.PCDynaServiceTemplate;
import com.je.core.service.PCServiceTemplate;
import com.je.core.util.ArrayUtils;
import com.je.core.util.DBSqlUtils;
import com.je.core.util.StringUtil;
import com.je.core.util.TreeUtil;
import com.je.core.util.WebUtils;
import com.je.core.util.bean.BeanUtils;
import com.je.core.util.bean.DynaBean;
import com.je.dd.vo.DicInfoVo;
import com.je.develop.service.FuncPermManager;
import com.je.rbac.model.EndUser;
import com.je.rbac.model.Role;
import com.je.rbac.node.RoleGroupPermNode;
import com.je.rbac.node.RolePermNode;
import com.je.staticize.service.StaticizeManager;
import com.je.wf.service.WfInfoManager;
import com.je.wf.service.WfServiceTemplate;
/**
 * 权限服务层实现
 * @author zhangshuaipeng
 *
 */
@Component("permissionManager")
public class PermissionManagerImpl implements PermissionManager{
	private static Logger logger = LoggerFactory.getLogger(PermissionManagerImpl.class);
	private PCDynaServiceTemplate serviceTemplate;
	private PCServiceTemplate pcServiceTemplate;
	private FuncPermManager funcPermManager;
	private StaticizeManager staticizeManager;
	private AdminPermManager adminPermManager;
	private TaskService taskService;
	private WfInfoManager wfInfoManager;
	private UserManager userManager;
	private WfServiceTemplate wfServiceTemplate;
	@Autowired
	private SaasManager saasManager;

	/**
	 * 得到首页模块信息
	 * @param userId 用户id
	 * @param refresh TODO 暂不明确
	 * @return
	 */
	@Override
	public List<DynaBean> getUserHome(String userId, String refresh) {
		List<DynaBean> lists=null;
		if(!"1".equals(refresh)){
			lists= UserHomeCacheManager.getCacheValue(userId);
		}
		if(lists!=null && lists.size()>0){
			return lists;
		}else {
			String queryFields="JE_CORE_HOME_ID,HOME_BM,HOME_BT,HOME_LX,HOME_TB,HOME_PZXX,HOME_BZ,HOME_CSGN_ID";
			DynaBean user=userManager.getUserById(userId);
			SaasPermInfo saasPermInfo=saasManager.getSaasPermInfo(user.getStr("ZHID"),"0");
			List<DynaBean> roles=serviceTemplate.selectList("JE_CORE_ROLE"," AND ROLEID IN (SELECT ROLEID FROM JE_CORE_ROLE_USER WHERE USERID='"+userId+"')");
			Boolean saasUser=false;
			for(DynaBean role:roles){
				if("SYS".equals(role.getStr("ROLERANK")) && "ROLE".equals(role.getStr("ROLETYPE"))){
					saasUser=true;
				}
			}
			String whereSql=" AND SY_STATUS='1' ";
			//租户过滤
			if(WebUtils.isSaas() && !saasUser){
				if(saasPermInfo.getAdminIds()!=null && saasPermInfo.getAdminIds().indexOf(userId)!=-1){
					whereSql+=" AND HOME_BM NOT IN ('PM','PLATFORM')";
				}else{
					whereSql+=" AND HOME_BM NOT IN ('HOME_SC','PM','PLATFORM')";
				}
			}else {
				//管理员过滤
				if ((WebUtils.getSysVar("JE_SYS_ADMIN")).indexOf(user.getStr("USERCODE")) == -1) {
					whereSql += " AND ((HOME_BM IN ('PM','PLATFORM') AND HOME_SEEPHONE LIKE '%" + user.getStr("USERCODE") + "%') OR HOME_BM NOT IN ('PM','PLATFORM'))";
				}
			}
			lists=serviceTemplate.selectList("JE_CORE_HOME",whereSql+" ORDER BY SY_ORDERINDEX ASC",queryFields);
			List<DynaBean> results=new ArrayList<>();
			//检测 如果没有权限则不加载了
			Set<String> moduleIds=new HashSet<>();
			for(DynaBean home:lists){
				if("MENU".equals(home.getStr("HOME_LX"))){
					moduleIds.add(home.getStr("HOME_PZXX"));
				}
			}
			//找出有权限的模块信息
			List<DynaBean> permissions=buildMenuPermList(roles, AuthorPermType.PERM_ROLE,StringUtil.buildSplitString(ArrayUtils.getArray(moduleIds),","),true);
			Set<String> haveMenuIds=new HashSet<>();
			for(DynaBean permission:permissions){
				haveMenuIds.add(permission.getStr("PERMCODE"));
			}
			for(DynaBean home:lists){
				if("MENU".equals(home.getStr("HOME_LX"))){
					if(haveMenuIds.contains(home.getStr("HOME_PZXX"))){
						results.add(home);
					}
				}else{
					results.add(home);
				}
			}
			UserHomeCacheManager.putCache(userId,results);
			return results;
		}
	}

	/**
	 * 构建指定菜单权限Map
	 * @param targers  目标   角色 /权限组
	 * @param permType 目标类型
	 * @param menuIds  菜单的配置的信息
	 * @param havePublic
	 * @return
	 */
	@Override
	public List<DynaBean> buildMenuPermList(List<DynaBean> targers, String permType, String menuIds, boolean havePublic) {
		String whereSql="";
 		Set<String> targerIds=new HashSet<>();
		for(DynaBean targer:targers){
			if(AuthorPermType.PERM_ROLE.equalsIgnoreCase(permType)){
				targerIds.add(targer.getStr("ROLEID"));
			}else if(AuthorPermType.PERM_ROLEGROUP.equalsIgnoreCase(permType)){
				targerIds.add(targer.getStr("JE_CORE_ROLEGROUP_ID"));
			}
		}
		String publicSql=" OR PERID IN (SELECT PERMID FROM JE_CORE_PUBLICPERM)";
		if(AuthorPermType.PERM_ROLE.equalsIgnoreCase(permType)){
			whereSql=" AND PERMCODE IN ("+StringUtil.buildArrayToString(menuIds.split(","))+") AND (PERID IN (SELECT PERID FROM JE_CORE_ROLE_PERM WHERE ENABLED='1' AND ROLEID IN ("+StringUtil.buildArrayToString(ArrayUtils.getArray(targerIds))+")) "+(havePublic?publicSql:"")+")";
		}else if(AuthorPermType.PERM_ROLEGROUP.equalsIgnoreCase(permType)){
			whereSql=" AND PERMCODE IN ("+StringUtil.buildArrayToString(menuIds.split(","))+") AND (PERID IN (SELECT PERID FROM JE_CORE_ROLEGROUP_PERM WHERE ENABLED='1' AND ROLEGROUPID IN ("+StringUtil.buildArrayToString(ArrayUtils.getArray(targerIds))+")) "+(havePublic?publicSql:"")+")";
		}
		List<DynaBean> permissions=serviceTemplate.selectList("JE_CORE_PERMISSION", whereSql);
		return permissions;
	}

	/**
	 * 构建权限Map
	 * @param targers  目标   角色 /权限组
	 * @param permType 目标类型
	 * @param haveButton 是否包含按钮
	 * @param haveSubFunc 是否包含子功能
	 * @return
	 */
	@Override
	public Map<String, DynaBean> buildPermMap(List<DynaBean> targers,String permType, Boolean haveButton, Boolean haveSubFunc,String module,boolean havePublic) {
		// TODO Auto-generated method stub
//		for(DynaBean targer:targers){
//			String whereSql="";
//			String targerId="";
//			if(AuthorPermType.PERM_ROLE.equalsIgnoreCase(permType)){
//				targerId=targer.getStr("ROLEID");
//				//处理聚合权限组
////				String groupCode=targer.getStr("GROUPCODE", "");
////				if(haveGroup && StringUtil.isNotEmpty(groupCode)){
////					whereSql=" AND (PERID IN (SELECT PERID FROM JE_CORE_ROLE_PERM WHERE ENABLED='1' AND ROLEID='"+targerId+"') OR PERID IN (SELECT PERID FROM JE_CORE_ROLEGROUP_PERM WHERE ENABLED='1' AND ROLEGROUPID in ("+StringUtil.buildArrayToString(groupCode.split(","))+")))";
////				}else{
//				whereSql=" AND PERID IN (SELECT PERID FROM JE_CORE_ROLE_PERM WHERE ENABLED='1' AND ROLEID='"+targerId+"')";
////				}
//			}else if(AuthorPermType.PERM_ROLEGROUP.equalsIgnoreCase(permType)){
//				targerId=;
//				whereSql=" AND PERID IN (SELECT PERID FROM JE_CORE_ROLEGROUP_PERM WHERE ENABLED='1' AND ROLEGROUPID='"+targerId+"')";
//			}
//			if(!haveButton){
//				whereSql+=" and PERMTYPE!='"+PermType.BUTTON+"'";
//			}
//			if(!haveSubFunc){
//				whereSql+=" and PERMTYPE!='"+PermType.SUB_FUNC+"'";
//			}
//			if(StringUtil.isNotEmpty(module)){
//				whereSql+=" and MODULE in ("+module+")";
//			}
//			List<DynaBean> permissions=serviceTemplate.selectList("JE_CORE_PERMISSION", whereSql);
//			for(DynaBean perm:permissions){
//				String key=targerId+"_"+permType+"_"+perm.getStr("PERMCODE")+"_"+perm.getStr("PERMTYPE");
//				permMap.put(key, perm);
//			}
//		}
//		if(havePublic){
//			String whereSql="";
//			if(!haveButton){
//				whereSql+=" and PERMTYPE!='"+PermType.BUTTON+"'";
//			}
//			if(!haveSubFunc){
//				whereSql+=" and PERMTYPE!='"+PermType.SUB_FUNC+"'";
//			}
//			if(StringUtil.isNotEmpty(module)){
//				whereSql+=" and MODULE in ("+module+")";
//			}
//			List<DynaBean> permissions=serviceTemplate.selectList("JE_CORE_PERMISSION", " AND PERID IN (SELECT PERMID FROM JE_CORE_PUBLICPERM) "+whereSql);
//			for(DynaBean perm:permissions){
//				String key="PUBLIC_"+permType+"_"+perm.getStr("PERMCODE")+"_"+perm.getStr("PERMTYPE");
//				permMap.put(key, perm);
//			}
//		}
		//获取权限改动，去掉每个角色的获取
		Map<String,DynaBean> permMap=new HashMap<String,DynaBean>();
		StringBuffer whereSql=new StringBuffer();
		String publicPermSql="";
		Set<String> targerIds=new HashSet<>();
		if(!haveButton){
			whereSql.append(" and PERMTYPE!='"+PermType.BUTTON+"'");
		}
		if(!haveSubFunc){
			whereSql.append(" and PERMTYPE!='"+PermType.SUB_FUNC+"'");
		}
		if(StringUtil.isNotEmpty(module)){
			whereSql.append(" and MODULE in ("+module+")");
		}
		if(havePublic){
			publicPermSql=" OR PERID IN (SELECT PERMID FROM JE_CORE_PUBLICPERM)";
		}
		for(DynaBean targer:targers){
			if(AuthorPermType.PERM_ROLE.equalsIgnoreCase(permType)) {
				targerIds.add(targer.getStr("ROLEID"));
			}else{
				targerIds.add(targer.getStr("JE_CORE_ROLEGROUP_ID"));
			}
		}
		if(AuthorPermType.PERM_ROLE.equalsIgnoreCase(permType)) {
			whereSql.append(" AND (PERID IN (SELECT PERID FROM JE_CORE_ROLE_PERM WHERE ENABLED='1' AND ROLEID IN ("+StringUtil.buildArrayToString(ArrayUtils.getArray(targerIds))+"))"+publicPermSql+")");
		}else if(AuthorPermType.PERM_ROLEGROUP.equalsIgnoreCase(permType)){
			whereSql.append(" AND (PERID IN (SELECT PERID FROM JE_CORE_ROLEGROUP_PERM WHERE ENABLED='1' AND ROLEGROUPID IN ("+StringUtil.buildArrayToString(ArrayUtils.getArray(targerIds))+"))"+publicPermSql+")");
		}
		List<DynaBean> permissions=serviceTemplate.selectList("JE_CORE_PERMISSION", whereSql.toString());
		for(DynaBean perm:permissions){
			String key=permType+"_"+perm.getStr("PERMCODE")+"_"+perm.getStr("PERMTYPE");
			permMap.put(key, perm);
		}
		return permMap;
	}
	/**
	 * 构建权限Map
	 * @param targers  目标   角色 /权限组
	 * @param permType 目标类型
	 * @param haveButton 是否包含按钮
	 * @param haveSubFunc 是否包含子功能
	 * @return
	 */
	@Override
	public Map<String, DynaBean> buildAppPermMap(List<DynaBean> targers,String permType, Boolean haveButton, Boolean haveSubFunc,String apkId,boolean havePublic) {
		// TODO Auto-generated method stub
//		Map<String,DynaBean> permMap=new HashMap<String,DynaBean>();
//		for(DynaBean targer:targers){
//			String whereSql="";
//			String targerId="";
//			if(AuthorPermType.PERM_ROLE.equalsIgnoreCase(permType)){
//				targerId=targer.getStr("ROLEID");
//				//处理聚合权限组
////				String groupCode=targer.getStr("GROUPCODE", "");
////				if(haveGroup && StringUtil.isNotEmpty(groupCode)){
////					whereSql=" AND (PERID IN (SELECT PERID FROM JE_CORE_ROLE_PERM WHERE ENABLED='1' AND ROLEID='"+targerId+"') OR PERID IN (SELECT PERID FROM JE_CORE_ROLEGROUP_PERM WHERE ENABLED='1' AND ROLEGROUPID in ("+StringUtil.buildArrayToString(groupCode.split(","))+")))";
////				}else{
//				whereSql=" AND PERID IN (SELECT PERID FROM JE_CORE_ROLE_PERM WHERE ENABLED='1' AND ROLEID='"+targerId+"')";
////				}
//			}else if(AuthorPermType.PERM_ROLEGROUP.equalsIgnoreCase(permType)){
//				targerId=targer.getStr("JE_CORE_ROLEGROUP_ID");
//				whereSql=" AND PERID IN (SELECT PERID FROM JE_CORE_ROLEGROUP_PERM WHERE ENABLED='1' AND ROLEGROUPID='"+targerId+"')";
//			}
//			if(!haveButton){
//				whereSql+=" and PERMTYPE!='"+PermType.APPBUTTON+"'";
//			}
//			if(!haveSubFunc){
//				whereSql+=" and PERMTYPE!='"+PermType.APPCHILDFUNC+"'";
//			}
//			if(StringUtil.isNotEmpty(apkId)){
//				whereSql+=" and MODULE in ("+apkId+")";
//			}
//			List<DynaBean> permissions=serviceTemplate.selectList("JE_CORE_PERMISSION", whereSql);
//			for(DynaBean perm:permissions){
//				String key=targerId+"_"+permType+"_"+perm.getStr("PERMCODE")+"_"+perm.getStr("PERMTYPE");
//				permMap.put(key, perm);
//			}
//		}
//		if(havePublic){
//			String whereSql="";
//			if(!haveButton){
//				whereSql+=" and PERMTYPE!='"+PermType.APPBUTTON+"'";
//			}
//			if(!haveSubFunc){
//				whereSql+=" and PERMTYPE!='"+PermType.APPCHILDFUNC+"'";
//			}
//			if(StringUtil.isNotEmpty(apkId)){
//				whereSql+=" and MODULE in ("+apkId+")";
//			}
//			List<DynaBean> permissions=serviceTemplate.selectList("JE_CORE_PERMISSION", " AND PERID IN (SELECT PERMID FROM JE_CORE_PUBLICPERM) "+whereSql);
//			for(DynaBean perm:permissions){
//				String key="PUBLIC_"+permType+"_"+perm.getStr("PERMCODE")+"_"+perm.getStr("PERMTYPE");
//				permMap.put(key, perm);
//			}
//		}
		//获取权限改动，去掉每个角色的获取
		Map<String,DynaBean> permMap=new HashMap<String,DynaBean>();
		StringBuffer whereSql=new StringBuffer();
		String publicPermSql="";
		Set<String> targerIds=new HashSet<>();
		if(!haveButton){
			whereSql.append(" and PERMTYPE!='"+PermType.APPBUTTON+"'");
		}
		if(!haveSubFunc){
			whereSql.append(" and PERMTYPE!='"+PermType.APPCHILDFUNC+"'");
		}
		if(StringUtil.isNotEmpty(apkId)){
			whereSql.append(" and MODULE in ("+apkId+")");
		}
		if(havePublic){
			publicPermSql=" OR PERID IN (SELECT PERMID FROM JE_CORE_PUBLICPERM)";
		}
		for(DynaBean targer:targers){
			if(AuthorPermType.PERM_ROLE.equalsIgnoreCase(permType)) {
				targerIds.add(targer.getStr("ROLEID"));
			}else{
				targerIds.add(targer.getStr("JE_CORE_ROLEGROUP_ID"));
			}
		}
		if(AuthorPermType.PERM_ROLE.equalsIgnoreCase(permType)) {
			whereSql.append(" AND (PERID IN (SELECT PERID FROM JE_CORE_ROLE_PERM WHERE ENABLED='1' AND ROLEID IN ("+StringUtil.buildArrayToString(ArrayUtils.getArray(targerIds))+"))"+publicPermSql+")");
		}else if(AuthorPermType.PERM_ROLEGROUP.equalsIgnoreCase(permType)){
			whereSql.append(" AND (PERID IN (SELECT PERID FROM JE_CORE_ROLEGROUP_PERM WHERE ENABLED='1' AND ROLEGROUPID IN ("+StringUtil.buildArrayToString(ArrayUtils.getArray(targerIds))+"))"+publicPermSql+")");
		}
		List<DynaBean> permissions=serviceTemplate.selectList("JE_CORE_PERMISSION", whereSql.toString());
		for(DynaBean perm:permissions){
			String key=permType+"_"+perm.getStr("PERMCODE")+"_"+perm.getStr("PERMTYPE");
			permMap.put(key, perm);
		}
		//处理聚合MAP
		return permMap;
	}
	/**
	 * 构建权限Map
	 * @param targers  目标   角色 /权限组
	 * @param permType 目标类型
	 * @param permCodeIds 是否包含按钮
	 * @param havePublic 是否包含子功能
	 * @return
	 */
	@Override
	public Map<String, DynaBean> buildFuncPermMap(List<DynaBean> targers,String permType,String permCodeIds,boolean havePublic) {
		// TODO Auto-generated method stub
//		Map<String,DynaBean> permMap=new HashMap<String,DynaBean>();
//		for(DynaBean targer:targers){
//			String whereSql="";
//			String targerId="";
//			if(AuthorPermType.PERM_ROLE.equalsIgnoreCase(permType)){
//				targerId=targer.getStr("ROLEID");
//				//处理聚合权限组
////				String groupCode=targer.getStr("GROUPCODE", "");
////				if(haveGroup && StringUtil.isNotEmpty(groupCode)){
////					whereSql=" AND (PERID IN (SELECT PERID FROM JE_CORE_ROLE_PERM WHERE ENABLED='1' AND ROLEID='"+targerId+"') OR PERID IN (SELECT PERID FROM JE_CORE_ROLEGROUP_PERM WHERE ENABLED='1' AND ROLEGROUPID in ("+StringUtil.buildArrayToString(groupCode.split(","))+")))";
////				}else{
//				whereSql=" AND PERID IN (SELECT PERID FROM JE_CORE_ROLE_PERM WHERE ENABLED='1' AND ROLEID='"+targerId+"')";
////				}
//			}else if(AuthorPermType.PERM_ROLEGROUP.equalsIgnoreCase(permType)){
//				targerId=targer.getStr("JE_CORE_ROLEGROUP_ID");
//				whereSql=" AND PERID IN (SELECT PERID FROM JE_CORE_ROLEGROUP_PERM WHERE ENABLED='1' AND ROLEGROUPID='"+targerId+"')";
//			}
//			whereSql+=" and PERMTYPE IN ('"+PermType.BUTTON+"','"+PermType.SUB_FUNC+"','"+PermType.FUNC+"') ";
//			if(StringUtil.isNotEmpty(permCodeIds)){
//				whereSql+=" and PERMCODE in ("+StringUtil.buildArrayToString(permCodeIds.split(","))+")";
//			}
//			List<DynaBean> permissions=serviceTemplate.selectList("JE_CORE_PERMISSION", whereSql);
//			for(DynaBean perm:permissions){
//				String key=targerId+"_"+permType+"_"+perm.getStr("PERMCODE")+"_"+perm.getStr("PERMTYPE");
//				permMap.put(key, perm);
//			}
//		}
//		if(havePublic){
//			String whereSql="";
//			whereSql+=" and PERMTYPE IN ('"+PermType.BUTTON+"','"+PermType.SUB_FUNC+"','"+PermType.FUNC+"') ";
//			if(StringUtil.isNotEmpty(permCodeIds)){
//				whereSql+=" and PERMCODE in ("+StringUtil.buildArrayToString(permCodeIds.split(","))+")";
//			}
//			List<DynaBean> permissions=serviceTemplate.selectList("JE_CORE_PERMISSION", " AND PERID IN (SELECT PERMID FROM JE_CORE_PUBLICPERM) "+whereSql);
//			for(DynaBean perm:permissions){
//				String key="PUBLIC_"+permType+"_"+perm.getStr("PERMCODE")+"_"+perm.getStr("PERMTYPE");
//				permMap.put(key, perm);
//			}
//		}
		//获取权限改动，去掉每个角色的获取
		Map<String,DynaBean> permMap=new HashMap<String,DynaBean>();
		StringBuffer whereSql=new StringBuffer();
		String publicPermSql="";
		Set<String> targerIds=new HashSet<>();
		whereSql.append(" and PERMTYPE IN ('"+PermType.BUTTON+"','"+PermType.SUB_FUNC+"','"+PermType.FUNC+"') ");
		if(StringUtil.isNotEmpty(permCodeIds)){
			whereSql.append(" and PERMCODE in ("+StringUtil.buildArrayToString(permCodeIds.split(","))+")");
		}
		if(havePublic){
			publicPermSql=" OR PERID IN (SELECT PERMID FROM JE_CORE_PUBLICPERM)";
		}
		for(DynaBean targer:targers){
			if(AuthorPermType.PERM_ROLE.equalsIgnoreCase(permType)) {
				targerIds.add(targer.getStr("ROLEID"));
			}else{
				targerIds.add(targer.getStr("JE_CORE_ROLEGROUP_ID"));
			}
		}
		if(AuthorPermType.PERM_ROLE.equalsIgnoreCase(permType)) {
			whereSql.append(" AND (PERID IN (SELECT PERID FROM JE_CORE_ROLE_PERM WHERE ENABLED='1' AND ROLEID IN ("+StringUtil.buildArrayToString(ArrayUtils.getArray(targerIds))+"))"+publicPermSql+")");
		}else if(AuthorPermType.PERM_ROLEGROUP.equalsIgnoreCase(permType)){
			whereSql.append(" AND (PERID IN (SELECT PERID FROM JE_CORE_ROLEGROUP_PERM WHERE ENABLED='1' AND ROLEGROUPID IN ("+StringUtil.buildArrayToString(ArrayUtils.getArray(targerIds))+"))"+publicPermSql+")");
		}
		List<DynaBean> permissions=serviceTemplate.selectList("JE_CORE_PERMISSION", whereSql.toString());
		for(DynaBean perm:permissions){
			String key=permType+"_"+perm.getStr("PERMCODE")+"_"+perm.getStr("PERMTYPE");
			permMap.put(key, perm);
		}
		//处理聚合MAP
		return permMap;
	}

	/**
	 * 得到功能权限Map  主要用于用户点击功能过滤权限
	 * @param funcId 功能id
	 * @param roles 角色
	 * @param havePublic 是否公开
	 * @return
	 */
	@Override
	public Map<String, DynaBean> getFuncPermMap(String funcId,List<DynaBean> roles,boolean havePublic) {
		// TODO Auto-generated method stub
//		List<DynaBean> roles=getCurrentPermRoles();
//		EndUser currentUser=SecurityUserHolder.getCurrentUser();
//		String excludeStr=currentUser.getExcludePerms();
//		Map<String, DynaBean> funcMap=new HashMap<String,DynaBean>();
//		String[] roleIds=new String[roles.size()];
//		for(Integer i=0;i<roles.size();i++){
//			roleIds[i]=roles.get(i).getStr("ROLEID");
//		}
//		if(roleIds.length>0){
//			String whereSql=" AND (PERID IN (SELECT PERID FROM JE_CORE_ROLE_PERM WHERE ENABLED='1' AND ROLEID in ("+StringUtil.buildArrayToString(roleIds)+")))";
//			//过滤按钮
//			whereSql+=" and (PERMTYPE='"+PermType.BUTTON+"'";
//			//过滤子功能
//			whereSql+=" or PERMTYPE='"+PermType.SUB_FUNC+"')";
//			//过滤功能
//			whereSql+=" and FUNCID='"+funcId+"'";
////			if(StringUtil.isNotEmpty(excludeStr)){
////				whereSql+=" AND PERMCODE NOT IN ("+StringUtil.buildArrayToString(excludeStr.split(","))+")";
////			}
//			List<DynaBean> permissions=serviceTemplate.selectList("JE_CORE_PERMISSION", whereSql);
//			for(DynaBean perm:permissions){
//				funcMap.put(perm.getStr("PERMCODE")+"_"+perm.getStr("PERMTYPE"), perm);
//			}
//		}
//		if(havePublic){
//			String whereSql="";
//			whereSql+=" and (PERMTYPE='"+PermType.BUTTON+"'";
//			//过滤子功能
//			whereSql+=" or PERMTYPE='"+PermType.SUB_FUNC+"')";
//			//过滤功能
//			whereSql+=" and FUNCID='"+funcId+"'";
//			List<DynaBean> permissions=serviceTemplate.selectList("JE_CORE_PERMISSION", " AND PERID IN (SELECT PERMID FROM JE_CORE_PUBLICPERM) "+whereSql);
//			for(DynaBean perm:permissions){
//				funcMap.put(perm.getStr("PERMCODE")+"_"+perm.getStr("PERMTYPE"), perm);
//			}
//		}
//		for(DynaBean role:roles){
//			String roleId=role.getStr("ROLEID");
//			//处理聚合权限组
//			String groupCode=role.getStr("GROUPCODE", "");
//			//查询权限
//			String whereSql=" AND (PERID IN (SELECT PERID FROM JE_CORE_ROLE_PERM WHERE ENABLED='1' AND ROLEID='"+roleId+"') OR PERID IN (SELECT PERID FROM JE_CORE_ROLEGROUP_PERM WHERE ENABLED='1' AND ROLEGROUPID in ("+StringUtil.buildArrayToString(groupCode.split(","))+")))";
//			//过滤按钮
//			whereSql+=" and (PERMTYPE='"+PermType.BUTTON+"'";
//			//过滤子功能
//			whereSql+=" or PERMTYPE='"+PermType.SUB_FUNC+"')";
//			//过滤功能
//			whereSql+=" and FUNCID='"+funcId+"'";
//			List<DynaBean> permissions=serviceTemplate.selectList("JE_CORE_PERMISSION", whereSql);
//			for(DynaBean perm:permissions){
//				funcMap.put(perm.getStr("PERMCODE")+"_"+perm.getStr("PERMTYPE"), perm);
//			}
//		}
		//获取权限改动，去掉每个角色的获取
		Map<String,DynaBean> permMap=new HashMap<String,DynaBean>();
		StringBuffer whereSql=new StringBuffer();
		String publicPermSql="";
		Set<String> targerIds=new HashSet<>();
		whereSql.append(" and PERMTYPE IN ('"+PermType.BUTTON+"','"+PermType.SUB_FUNC+"')");
		//过滤功能
		whereSql.append(" and FUNCID='"+funcId+"'");
		if(havePublic){
			publicPermSql=" OR PERID IN (SELECT PERMID FROM JE_CORE_PUBLICPERM)";
		}
		for(DynaBean targer:roles){
			targerIds.add(targer.getStr("ROLEID"));
		}
		whereSql.append(" AND (PERID IN (SELECT PERID FROM JE_CORE_ROLE_PERM WHERE ENABLED='1' AND ROLEID IN ("+StringUtil.buildArrayToString(ArrayUtils.getArray(targerIds))+"))"+publicPermSql+")");
		List<DynaBean> permissions=serviceTemplate.selectList("JE_CORE_PERMISSION", whereSql.toString());
		for(DynaBean perm:permissions){
			String key=perm.getStr("PERMCODE")+"_"+perm.getStr("PERMTYPE");
			permMap.put(key, perm);
		}
		return permMap;
	}
	/**
	 * 构建权限树
	 * @param rootId
	 * @param targers  目标   角色 /权限组
	 * @param permType 目标类型
	 * @param haveButton 是否包含按钮
	 * @param haveSubFunc 是否包含子功能
	 * @param haveDeny 是否含有未授权
	 * @param haveDeny 是否为英文
	 * @param queryInfo 查询条件(主要用于菜单的过滤)
	 * @return
	 */
	@Override
	public JSONTreeNode buildPermTree(String rootId, List<DynaBean> targers,String permType, Boolean haveButton, Boolean haveSubFunc,Boolean haveDeny,Boolean en, QueryInfo queryInfo,Set<String> includes,Set<String> nodeIds,String zhId,boolean havePublic,boolean onlyOneChild) {
		// TODO Auto-generated method stub
		Map<String,DynaBean> permMap=buildPermMap(targers, permType, haveButton, haveSubFunc,null,havePublic);
		DynaBean resourceTable=BeanUtils.getInstance().getResourceTable("JE_CORE_MENU");
		List<DynaBean> tableColumns=(List<DynaBean>) resourceTable.get(BeanUtils.KEY_TABLE_COLUMNS);
		JSONTreeNode template=BeanUtils.getInstance().buildJSONTreeNodeTemplate(tableColumns);
//		if(en){
//			template.setText("MENU_MENUSUBNAME");
//			template.setEnField("MENU_MENUNAME");
//		}
		List<JSONTreeNode> permDatas=new ArrayList<JSONTreeNode>();
		List<JSONTreeNode> lists=pcServiceTemplate.getJsonTreeNodeList(rootId, "JE_CORE_MENU", template, queryInfo);
		String[] excludes=null;
		//如果是运行模式
		if(JECoreMode.RUNNING.equals(WebUtils.getSysVar("JE_CORE_MODE")) && haveDeny){
			String excludesStr=WebUtils.getSysVar("JE_DEVELOP_MENUIDS");
			if(StringUtil.isNotEmpty(excludesStr)){
				excludes=excludesStr.split(",");
			}
		}
		Map<String,String> saasCpInfos=null;
		EndUser currentUser=SecurityUserHolder.getCurrentUser();
		if(WebUtils.isSaas() && (!currentUser.getSaas() || StringUtil.isNotEmpty(zhId))){
			saasCpInfos=new HashMap<String,String>();
			if(StringUtil.isEmpty(zhId)){
				zhId=currentUser.getZhId();
			}
			List<DynaBean> zyghs=serviceTemplate.selectList("JE_SAAS_CPYHZY", " AND CPYHZY_ZHID='"+zhId+"' AND CPYHZY_TYPE='YH' AND CPYHZY_ZYID IN (SELECT JE_CORE_MENU_ID FROM JE_CORE_MENU WHERE 1=1 "+queryInfo.getWhereSql()+")");
			for(DynaBean zygh:zyghs){
				String zyId=zygh.getStr("CPYHZY_ZYID","");
				String cpId=zygh.getStr("CPYHZY_CPID","");
				if(saasCpInfos.containsKey(zyId)){
					saasCpInfos.put(zyId, saasCpInfos.get(zyId)+","+cpId);
				}else{
					saasCpInfos.put(zyId, cpId);
				}
			}
		}
		//菜单的所有功能编码
		List<String> funcCodes=new ArrayList<String>();
		//菜单所有设计到的功能Id
		List<String> funcIds=new ArrayList<String>();
		//存放所有功能数据   key是功能编码
		Map<String,DynaBean> funcMaps=new HashMap<String,DynaBean>();
		//存放所有子功能数据
		Map<String,List<DynaBean>> childMaps=new HashMap<String,List<DynaBean>>();
		//存放是所有功能按钮
		Map<String,List<DynaBean>> funcBtnMaps=new HashMap<String,List<DynaBean>>();

		for(JSONTreeNode node:lists){
			if(node.getId().equals(rootId)){
				//permDatas.add(node);
				continue;
			}

			Map<String,Object> beans=node.getBean();
			beans.put("CPID", "");
			if(saasCpInfos!=null){
				if(saasCpInfos.containsKey(node.getId())){
					if(beans!=null){
						beans.put("CPID", saasCpInfos.get(node.getId()));
					}
				}
			}
			node.setBean(beans);
			if(includes!=null){
				if(!includes.contains(node.getId()+"#"+AdminPermType.RES_MENU)){
					continue;
				}
			}
			if(haveSubFunc){
				node.setLeaf(false);
			}
//			if(!haveSubFunc && !haveButton && !haveDeny){
//
//			}
			//如果是运行模式。。 则加载未授权的信息排除菜单，主要控制运行模式不授权功能
			if(haveDeny && excludes!=null){
				if(ArrayUtils.contains(excludes, node.getId()))continue;
			}
			if("MT".equalsIgnoreCase(node.getNodeInfoType())){
				DynaBean permission=checkPerm(permMap, targers, permType, node);
				if(haveDeny || permission!=null){
					funcCodes.add(node.getNodeInfo());
				}
			}
		}
		if(haveSubFunc || haveButton){
			List<DynaBean> funcInfos=serviceTemplate.selectList("JE_CORE_FUNCINFO", " AND FUNCINFO_FUNCCODE IN ("+StringUtil.buildArrayToString(ArrayUtils.getArray(funcCodes))+") AND FUNCINFO_NODEINFOTYPE IN ('FUNC','FUNCFIELD') ","JE_CORE_FUNCINFO_ID,FUNCINFO_FUNCCODE,SY_PATH");
			for(DynaBean funcInfo:funcInfos){
				funcIds.add(funcInfo.getStr("JE_CORE_FUNCINFO_ID"));
				funcMaps.put(funcInfo.getStr("FUNCINFO_FUNCCODE"), funcInfo);
			}
			List<DynaBean> funcRelations=serviceTemplate.selectList("JE_CORE_FUNCRELATION", " AND FUNCRELATION_ENABLED='1' AND FUNCRELATION_FUNCINFO_ID IN ("+StringUtil.buildArrayToString(ArrayUtils.getArray(funcIds))+") ORDER BY SY_ORDERINDEX", "JE_CORE_FUNCRELATION_ID,FUNCRELATION_NAME,FUNCRELATION_CODE,FUNCRELATION_FUNCID,SY_STATUS,FUNCRELATION_FUNCINFO_ID,SY_ORDERINDEX");
			//查询所有涉及到的子功能
			if(haveSubFunc){
				buildFuncChilds(funcRelations, funcIds,childMaps,funcMaps);
			}
			//查询所有涉及到的按钮信息
			if(haveButton){
				List<DynaBean> buttons=serviceTemplate.selectList("JE_CORE_RESOURCEBUTTON", " AND RESOURCEBUTTON_DISABLED!='1' AND RESOURCEBUTTON_FUNCINFO_ID IN ("+StringUtil.buildArrayToString(ArrayUtils.getArray(funcIds))+") ORDER BY SY_ORDERINDEX","JE_CORE_RESOURCEBUTTON_ID,RESOURCEBUTTON_TYPE,RESOURCEBUTTON_NAME,RESOURCEBUTTON_CODE,RESOURCEBUTTON_FUNCINFO_ID,SY_ORDERINDEX");
				for(DynaBean button:buttons){
					List<DynaBean> btns=funcBtnMaps.get(button.getStr("RESOURCEBUTTON_FUNCINFO_ID"));
					if(btns==null){
						btns=new ArrayList<DynaBean>();
					}
					btns.add(button);
					funcBtnMaps.put(button.getStr("RESOURCEBUTTON_FUNCINFO_ID"), btns);
				}
			}
		}
		for(JSONTreeNode node:lists){
			if(node.getId().equalsIgnoreCase(rootId)){
				permDatas.add(node);
				continue;
			}
			if(includes!=null){
				if(!includes.contains(node.getId()+"#"+AdminPermType.RES_MENU)){
					continue;
				}
			}
			if(nodeIds!=null){
				if(!nodeIds.contains(node.getId())){
					continue;
				}
			}
			Map menuValues=new HashMap<String,Object>();
			if(node.getBean()!=null){
				menuValues.put("MENU_ICONCLS", node.getBean().get("MENU_ICONCLS"));
				menuValues.put("MENU_BIGICONCLS", node.getBean().get("MENU_BIGICONCLS"));
				menuValues.put("MENU_BIGBUTTON", node.getBean().get("MENU_BIGBUTTON"));
				menuValues.put("MENU_MENUNAME", node.getBean().get("MENU_MENUNAME"));
				menuValues.put("MENU_MENUSUBNAME", node.getBean().get("MENU_MENUSUBNAME"));
				menuValues.put("MENU_FONTICONCLS", node.getBean().get("MENU_FONTICONCLS"));
				menuValues.put("MENU_FONTICONCOLOR", node.getBean().get("MENU_FONTICONCOLOR"));
				menuValues.put("MENU_HIDELOCATION", node.getBean().get("MENU_HIDELOCATION"));
				menuValues.put("MENU_QUICKSTART", node.getBean().get("MENU_QUICKSTART"));
				menuValues.put("CPID", node.getBean().get("CPID"));
			}
			node.setBean(menuValues);
			//如果是运行模式。。 则加载未授权的信息排除菜单，主要控制运行模式不授权功能
			if(haveDeny && excludes!=null){
				if(ArrayUtils.contains(excludes, node.getId()))continue;
			}
			DynaBean permission=checkPerm(permMap, targers, permType, node);
			//功能类型
			if("MT".equalsIgnoreCase(node.getNodeInfoType())){
				if(haveDeny || permission!=null){
					if(haveDeny){
						buildMenuType(node);
					}
					if(permission!=null){
						node.setChecked(true);
					}else{
						node.setChecked(false);
					}
					//加载功能的子功能 /多表单 /按钮
					if(haveButton || haveSubFunc){
						buildFuncTree(permMap,funcMaps,childMaps,funcBtnMaps, targers, permType, node, haveDeny,haveButton,haveSubFunc,includes,nodeIds,onlyOneChild);
					}
					permDatas.add(node);
				}else{
					//不包含未授权且没有权限
				}
			}else{
				if(haveDeny){
					buildMenuType(node);
					if(permission!=null){
						node.setChecked(true);
					}else{
						node.setChecked(false);
					}
					permDatas.add(node);
				}else{
					if(permission!=null){
						if(haveDeny){
							buildMenuType(node);
						}
						node.setChecked(true);
						permDatas.add(node);
					}
				}
			}
		}
		return pcServiceTemplate.buildJSONNewTree(permDatas, rootId);
	}

	/**
	 * 构建手机权限树形
	 * @param apkId TODO 暂不明确
	 * @param targers 目标 角色/权限组
	 * @param permType 目标类型
	 * @param haveButton 是否包含按钮
	 * @param haveSubFunc 是否包含子功能
	 * @param haveDeny 是否含有未授权
	 * @param includes TODO 暂不明确
	 * @param cpId
	 * @return
	 */
	@Override
	public JSONTreeNode buildAppPermTree(String apkId, List<DynaBean> targers,String permType, Boolean haveButton, Boolean haveSubFunc,Boolean haveDeny, Set<String> includes,String cpId) {
		// TODO Auto-generated method stub
		String whereSql="";
		JSONTreeNode rootNode=TreeUtil.buildRootNode();
		if(StringUtil.isNotEmpty(apkId)){
			whereSql=" AND JE_PHONE_APK_ID='"+apkId+"'";
		}
		if(WebUtils.isSaas()){
			EndUser currentUser=SecurityUserHolder.getCurrentUser();
			if(currentUser.getSaas()){
				if(StringUtil.isNotEmpty(cpId)){
					DynaBean cp=serviceTemplate.selectOneByPk("JE_SAAS_CP", cpId);
					includes=new HashSet<String>();
					whereSql+=" AND JE_PHONE_APK_ID IN ("+StringUtil.buildArrayToString(cp.getStr("CP_APK_ID","").split(","))+")";
					List<DynaBean> perms=serviceTemplate.selectList("JE_SAAS_CPYHZY", " AND CPYHZY_CPID='"+cpId+"' AND CPYHZY_TYPE='APP'");
					for(DynaBean perm:perms){
						includes.add(perm.getStr("CPYHZY_APKID","")+"_"+perm.getStr("CPYHZY_ZYID",""));
					}
				}
			}else{
//				DynaBean yh=serviceTemplate.selectOne("JE_SAAS_YH", " AND JE_SAAS_YH_ID='"+currentUser.getZhId()+"'");
				includes=new HashSet<String>();
//				String cpSql=" AND CPYHZY_CPID IN (SELECT JE_SAAS_CP_ID FROM JE_SAAS_CPYH WHERE 1=1 "+WebUtils.getCpYhWhere()+" AND JE_SAAS_YH_ID='"+yh.getStr("JE_SAAS_YH_ID")+"') ";
				if(StringUtil.isNotEmpty(currentUser.getZhId())){
					List<DynaBean> perms=serviceTemplate.selectList("JE_SAAS_CPYHZY", " AND CPYHZY_TYPE='YHAPP' AND CPYHZY_ZHID='"+currentUser.getZhId()+"' AND CPYHZY_CPID IN (SELECT JE_SAAS_CP_ID FROM JE_SAAS_CPYH WHERE 1=1 "+WebUtils.getCpYhWhere()+" AND JE_SAAS_YH_ID='"+currentUser.getZhId()+"')");
					for(DynaBean perm:perms){
						includes.add(perm.getStr("CPYHZY_APKID","")+"_"+perm.getStr("CPYHZY_ZYID",""));
					}
				}
			}
		}
		List<DynaBean> apks=serviceTemplate.selectList("JE_PHONE_APK", whereSql+" order by SY_CREATETIME");
		for(DynaBean apk:apks){
			JSONTreeNode apkNode=TreeUtil.buildTreeNode(apk.getStr("JE_PHONE_APK_ID"), apk.getStr("APK_TEXT"), apk.getStr("APK_CODE"), "APK", "app", "jeicon jeicon-module-three-dimensional", rootNode.getId());
			Map<String,DynaBean> funcInfos=new HashMap<String,DynaBean>();
			//找到所有子功能
			List<DynaBean> apkFuncs=serviceTemplate.selectList("JE_PHONE_APP", " AND APP_TYPE IN ('func','plugin','report','chart','link','plugin_cloud') AND APP_APK_ID='"+apkNode.getId()+"'");
			List<DynaBean> childFields=serviceTemplate.selectList("JE_PHONE_APPFIELD", " AND APPFIELD_XTYPE IN ('child','childfuncfield') AND APPFIELD_APP_ID IN (SELECT JE_PHONE_APP_ID FROM JE_PHONE_APP WHERE APP_TYPE='func' AND APP_APK_ID='"+apkNode.getId()+"')");
			for(DynaBean apkFunc:apkFuncs){
				String funcId=apkFunc.getStr("JE_PHONE_APP_ID");
				List<DynaBean> childrens=new ArrayList<DynaBean>();
				for(DynaBean child:childFields){
					if(child.get("APPFIELD_APP_ID", "").equals(funcId)){
						childrens.add(child);
					}
				}
				if(childrens.size()>0){
					apkFunc.set("childrens", childrens);
				}
				funcInfos.put(apkFunc.getStr("APP_CODE"), apkFunc);
			}

			//找到菜单管理节点
			DynaBean xtBean=serviceTemplate.selectOne("JE_PHONE_APP", " AND APP_TYPE='home' AND APP_CODE='__HOME' AND APP_APK_ID='"+apkNode.getId()+"'");
			String moduleStr=xtBean.getStr("APP_HOME_CFG","");
			//声明在菜单中含有的功能
			Set<String> haveFuncIds=new HashSet<>();
			Set<String> haveAppFuncIds=new HashSet<>();
			if(StringUtil.isNotEmpty(moduleStr)){
				JSONArray modules=JSONArray.fromObject(moduleStr);
				JSONTreeNode homeNode=new JSONTreeNode();
				homeNode.setText("首页");
				homeNode.setCode("home");
				homeNode.setNodeType(NodeType.GENERAL);
				homeNode.setNodeInfoType("ROOT");
				homeNode.setIconCls("jeicon jeicon-module-three-dimensional");
				homeNode.setLeaf(false);
				homeNode.setParent(apkNode.getId());
				homeNode.setId(apkNode.getId()+"_HOME");

				JSONTreeNode allMenuNode=new JSONTreeNode();
				allMenuNode.setText("全部菜单");
				allMenuNode.setCode("menu");
				allMenuNode.setNodeType(NodeType.GENERAL);
				allMenuNode.setNodeInfoType("ROOT");
				allMenuNode.setIconCls("jeicon jeicon-module-three-dimensional");
				allMenuNode.setLeaf(false);
				allMenuNode.setParent(apkNode.getId());
				allMenuNode.setId(apkNode.getId()+"_MENU");

				for(int i=0;i<modules.size();i++){
					JSONObject jsObj=modules.getJSONObject(i);
					JSONArray dataArray = jsObj.getJSONArray("data");
					JSONTreeNode home=new JSONTreeNode();
					if(jsObj.containsKey("title")){
						home.setText(jsObj.getString("title"));
					}else{
						home.setText("未知首页");
					}
					if(jsObj.containsKey("code")){
						home.setCode(jsObj.getString("code"));
					}else{
						home.setCode("未知");
					}
					home.setNodeType(NodeType.LEAF);
					home.setNodeInfoType("home");
					home.setIconCls("jeicon jeicon-module-three-dimensional");
					home.setLeaf(true);
					home.setParent(homeNode.getId());
					if(jsObj.containsKey("code")){
						home.setId(jsObj.getString("code"));
					}else{
						home.setId("未知首页");
					}
					homeNode.getChildren().add(home);
                 	 Boolean relyOnFlag=false;
					if(jsObj.containsKey("relyOn")){
						String relyOn = jsObj.getString("relyOn");
						if(StringUtil.isNotEmpty(relyOn)){
							relyOnFlag=true;
						}
					}
					for (int z=0;z<dataArray.size();z++){
						JSONTreeNode module=new JSONTreeNode();
						JSONObject obj = dataArray.getJSONObject(z);
						if("basic".equals(obj.getString("type"))){
							continue;
						}
						module.setText(obj.getString("text"));
						module.setCode(obj.getString("code"));
						module.setNodeType(NodeType.GENERAL);
						module.setNodeInfoType("app"+obj.getString("type"));
						module.setIconCls("jeicon jeicon-module-three-dimensional");
						module.setLeaf(false);
						module.setParent(allMenuNode.getId());
						if(obj.containsKey("id")){
							module.setId(obj.getString("id"));
						}else{
							module.setId(apkNode.getId()+"_"+module.getCode());
						}
						if(includes!=null){
							if(!includes.contains(apkNode.getId()+"_"+module.getId())){
								continue;
							}
						}
						//查询出功能
						JSONObject cfgObj=obj.getJSONObject("cfg");
						if(!relyOnFlag && cfgObj.containsKey("funcs")){
							JSONArray menus=cfgObj.getJSONArray("funcs");
							for(int j=0;j<menus.size();j++){
								JSONObject menu=menus.getJSONObject(j);
								if("func".equals(menu.getString("type"))){
									JSONTreeNode node=buildAppFunc(apkNode.getId(),menu, funcInfos,includes,haveFuncIds,haveAppFuncIds);
									node.setParent(module.getId());
									if(includes!=null){
										if(!includes.contains(apkNode.getId()+"_"+node.getId())){
											continue;
										}
									}
									if(!haveFuncIds.contains(node.getId())) {
										module.getChildren().add(node);
										haveFuncIds.add(node.getId());
									}
								}else if("menu".equals(menu.getString("type"))){// || "footmenu".equals(menu.getString("type"))
									JSONTreeNode menuNode=TreeUtil.buildTreeNode("", menu.getString("text"), menu.getString("code"), "", "appmenu", "jeicon jeicon-folder", module.getId());
									if(menu.containsKey("id")){
										menuNode.setId(menu.getString("id"));
									}
									//判断是否加入
									if(includes!=null){
										if(!includes.contains(apkNode.getId()+"_"+menuNode.getId())){
											continue;
										}
									}
									JSONArray menuFuncs=JSONArray.fromObject(StringUtil.getDefaultValue(menu.getString("funcs"),"[]"));
									for(int k=0;k<menuFuncs.size();k++){
										JSONObject menuFunc=menuFuncs.getJSONObject(k);
										JSONTreeNode menuFuncNode=buildAppFunc(apkId,menuFunc, funcInfos,includes,haveFuncIds,haveAppFuncIds);
										menuFuncNode.setParent(menuNode.getId());
										if(includes!=null){
											if(!includes.contains(apkNode.getId()+"_"+menuFuncNode.getId())){
												continue;
											}
										}
										if(!haveFuncIds.contains(menuFuncNode.getId())) {
											menuNode.getChildren().add(menuFuncNode);
											haveFuncIds.add(menuFuncNode.getId());
										}
									}
									if(!haveFuncIds.contains(menuNode.getId())) {
										module.getChildren().add(menuNode);
										haveFuncIds.add(menuNode.getId());
									}
								}else{
									//其他组件
									JSONTreeNode menuNode=TreeUtil.buildTreeNode("", menu.getString("text"), menu.getString("code"), "", "app"+menu.getString("type"), "", module.getId());
									if(menu.containsKey("id")){
										menuNode.setId(menu.getString("id"));
									}
									if(includes!=null){
										if(!includes.contains(apkNode.getId()+"_"+menuNode.getId())){
											continue;
										}
									}
									if(!haveFuncIds.contains(menuNode.getId())) {
										module.getChildren().add(menuNode);
										haveFuncIds.add(menuNode.getId());
									}
								}
							}
						}
						if(!haveFuncIds.contains(module.getId())) {
							allMenuNode.getChildren().add(module);
							haveFuncIds.add(module.getId());
						}
					}

				}
				apkNode.getChildren().add(homeNode);
				apkNode.getChildren().add(allMenuNode);
			}
			JSONTreeNode funcNode=new JSONTreeNode();
			funcNode.setText("无菜单功能");
			funcNode.setCode("iditfunc");
			funcNode.setNodeType(NodeType.GENERAL);
			funcNode.setNodeInfoType("ROOT");
			funcNode.setIconCls("jeicon jeicon-module-three-dimensional");
			funcNode.setLeaf(false);
			funcNode.setParent(apkNode.getId());
			funcNode.setId(apkNode.getId()+"_IDITFUNC");
			for(DynaBean apkFunc:apkFuncs) {
				String funcId = apkFunc.getStr("JE_PHONE_APP_ID");
				if(!haveAppFuncIds.contains(funcId) && "1".equals(apkFunc.getStr("APP_NOMENU"))){
					JSONObject menuObj=new JSONObject();
					menuObj.put("text",apkFunc.getStr("APP_TEXT"));
					menuObj.put("code",apkFunc.getStr("APP_CODE"));
					menuObj.put("id",apkFunc.getStr("JE_PHONE_APP_ID"));
					JSONTreeNode node=buildAppFunc(apkNode.getId(),menuObj, funcInfos,includes,haveFuncIds,haveAppFuncIds);
					node.setParent(funcNode.getId());
					if(includes!=null){
						if(!includes.contains(apkNode.getId()+"_"+node.getId())){
							continue;
						}
					}
					if(!haveFuncIds.contains(node.getId())) {
						funcNode.getChildren().add(node);
						haveFuncIds.add(node.getId());
					}
				}
			}
			apkNode.getChildren().add(funcNode);
			rootNode.getChildren().add(apkNode);
		}
		return rootNode;
	}
	private JSONTreeNode buildAppFunc(String apkId,JSONObject menu,Map<String,DynaBean> funcInfos,Set<String> includes,Set<String> haveFuncIds,Set<String> haveAppFuncIds){
		JSONTreeNode node=new JSONTreeNode();
		String funcCode=menu.getString("code");
		node.setText(menu.getString("text"));
		node.setCode(menu.getString("code"));
		node.setIconCls("jeicon jeicon-work2-on");
		node.setNodeInfo("");
		node.setNodeInfoType("appfunc");
		if(menu.containsKey("id")){
			node.setId(menu.getString("id"));
		}
		DynaBean funcInfo=funcInfos.get(funcCode);
		if(funcInfo!=null){
			haveAppFuncIds.add(funcInfo.getStr("JE_PHONE_APP_ID"));
			if(!menu.containsKey("id")){
				node.setId(funcInfo.getStr("JE_PHONE_APP_ID"));
			}
			//解析功能信息
			node.setNodeInfo(funcInfo.getStr("JE_PHONE_APP_ID"));
			String funcType=funcInfo.getStr("APP_TYPE");
			if("plugin".equals(funcType)){
				node.setIconCls("jeicon jeicon-buildingblocks");
			}else if("plugin_cloud".equals(funcType)){
				node.setIconCls("jeicon jeicon-buildingblocks");
			}else if("report".equals(funcType)){
				node.setIconCls("jeicon jeicon-form");
			}else if("chart".equals(funcType)){
				node.setIconCls("jeicon jeicon-yxsjtj");
			}else if("link".equals(funcType)){
				node.setIconCls("jeicon jeicon-associated");
			}else{
				//找到按钮信息
				JSONArray buttons=JSONArray.fromObject(StringUtil.getDefaultValue(funcInfo.getStr("APP_FUNC_BUTTONS","[]"),"[]"));
				for(int i=0;i<buttons.size();i++){
					JSONObject btnObj=buttons.getJSONObject(i);
					JSONTreeNode btnNode=new JSONTreeNode();
					btnNode.setText(btnObj.getString("text"));
					btnNode.setCode(btnObj.getString("code"));
					btnNode.setIconCls("jeicon jeicon-button");
					btnNode.setNodeInfo(funcInfo.getStr("JE_PHONE_APP_ID"));
					btnNode.setNodeInfoType("appbutton");
					String type=btnObj.getString("type");
					if("GRID".equals(type)){
						btnNode.setText(btnNode.getText()+" -->列表");
					}else if("FORM".equals(type)){
						btnNode.setText(btnNode.getText()+" -->表单");
					}else if("FORMITEM".equals(type)){
						btnNode.setText(btnNode.getText()+" -->表单内部");
					}else if("GRIDITEM".equals(type)){
						btnNode.setText(btnNode.getText()+" -->列表内部");
					}
					if(btnObj.containsKey("id")){
						btnNode.setId(btnObj.getString("id"));
					}else{
						btnNode.setId(node.getId()+"_"+btnNode.getCode());
					}
					if(!"1".equals(btnObj.getString("disable"))){
						if(includes!=null){
							if(!includes.contains(apkId+"_"+btnNode.getId())){
								continue;
							}
						}
						if(!haveFuncIds.contains(btnNode.getId())){
							node.getChildren().add(btnNode);
							haveFuncIds.add(btnNode.getId());
						}
					}
				}
				//找到子功能,循环递归子功能
				if(funcInfo.containsKey("childrens")){
					List<DynaBean> childrens=(List<DynaBean>) funcInfo.get("childrens");
					for(DynaBean children:childrens){
						if(StringUtil.isNotEmpty(children.getStr("APPFIELD_CONFIGINFO"))){
							JSONObject cfgObj=JSONObject.fromObject(StringUtil.getDefaultValue(children.getStr("APPFIELD_CONFIGINFO","{}"),"{}"));
							JSONObject childObj=new JSONObject();
							childObj.put("code", cfgObj.getString("funcCode"));
							childObj.put("text", children.getStr("APPFIELD_NAME"));
							childObj.put("id", children.getStr("JE_PHONE_APPFIELD_ID"));
							JSONTreeNode childNode=buildAppFunc(apkId,childObj, funcInfos,includes,haveFuncIds,haveAppFuncIds);
							childNode.setId(children.getStr("JE_PHONE_APPFIELD_ID"));
							if(includes!=null){
								if(!includes.contains(apkId+"_"+childNode.getId())){
									continue;
								}
							}
							childNode.setParent(node.getId());
							childNode.setNodeInfoType("appchildfunc");
							childNode.setNodeInfo(funcInfo.getStr("JE_PHONE_APP_ID"));
							childNode.setIconCls("jeicon jeicon-set-up");
							if(!haveFuncIds.contains(childNode.getId())){
								node.getChildren().add(childNode);
								haveFuncIds.add(childNode.getId());
							}

						}
					}
				}
			}
		}else{
			node.setLeaf(false);
		}
		return node;
	}

	/**
	 * 获取指定角色或权限组的子系统功能类型
	 * @param whereSql 查询的sql
	 * @return
	 */
	@Override
	public JSONTreeNode getFuncTreeNode(String whereSql) {
		// TODO Auto-generated method stub
		List<DynaBean> funcs=serviceTemplate.selectList("JE_CORE_FUNCINFO", " AND JE_CORE_FUNCINFO_ID IN (SELECT PERMCODE FROM JE_CORE_PERMISSION WHERE PERMTYPE='"+PermType.FUNC+"') "+whereSql);
		JSONTreeNode rootNode=TreeUtil.buildRootNode();
		//功能按钮
		for(DynaBean func:funcs){
			JSONTreeNode node=TreeUtil.buildTreeNode(func.getStr("JE_CORE_FUNCINFO_ID"), func.getStr("FUNCINFO_FUNCNAME"), func.getStr("FUNCINFO_FUNCCODE"), func.getStr("FUNCINFO_NODEINFO"), func.getStr("FUNCINFO_NODEINFOTYPE"), func.getStr("FUNCINFO_ICONCLS") , rootNode.getId());
			rootNode.getChildren().add(node);
		}
		return rootNode;
	}

	/**
	 * 初始化所有功能授权的功能
	 * @param whereSql 查询sql
	 * @param haveDeny 是否含有未授权
	 * @param cpId TODO 暂不明确
	 * @return
	 */
	@Override
	public JSONTreeNode initFuncTreeNode(String whereSql,Boolean haveDeny,String cpId) {
		// TODO Auto-generated method stub
		String denySql="AND JE_CORE_FUNCINFO_ID IN (SELECT PERMCODE FROM JE_CORE_PERMISSION WHERE PERMTYPE='"+PermType.FUNC+"') ";
		if(haveDeny){
			denySql="";
		}
		String funcSql="";
		String btnSql="";
		String childSql="";
		if(WebUtils.isSaas()){
			EndUser currentUser=SecurityUserHolder.getCurrentUser();
			if(currentUser.getSaas()){
				if(StringUtil.isNotEmpty(cpId)){
					funcSql=" AND JE_CORE_FUNCINFO_ID IN (SELECT CPYHZY_ZYID FROM JE_SAAS_CPYHZY WHERE CPYHZY_TYPE='CPFUNC' AND CPYHZY_CPID='"+cpId+"')";
					btnSql=" AND JE_CORE_RESOURCEBUTTON_ID IN (SELECT CPYHZY_ZYID FROM JE_SAAS_CPYHZY WHERE CPYHZY_TYPE='CPFUNC' AND CPYHZY_CPID='"+cpId+"')";
					childSql=" AND JE_CORE_FUNCRELATION_ID IN (SELECT CPYHZY_ZYID FROM JE_SAAS_CPYHZY WHERE CPYHZY_TYPE='CPFUNC' AND CPYHZY_CPID='"+cpId+"')";
				}
			}else{
//				DynaBean yh=serviceTemplate.selectOne("JE_SAAS_YH", " AND SY_JTGSID='"+currentUser.getJtgsId()+"'");
				String cpSql=" AND CPYHZY_CPID IN (SELECT JE_SAAS_CP_ID FROM JE_SAAS_CPYH WHERE 1=1 "+WebUtils.getCpYhWhere()+" AND JE_SAAS_YH_ID='"+currentUser.getZhId()+"') ";
				funcSql=" AND 1!=1";
				btnSql=" AND 1!=1";
				childSql=" AND 1!=1";
				if(StringUtil.isNotEmpty(currentUser.getZhId())){
					funcSql=" AND JE_CORE_FUNCINFO_ID IN (SELECT CPYHZY_ZYID FROM JE_SAAS_CPYHZY WHERE CPYHZY_TYPE='YHFUNC' AND CPYHZY_ZHID='"+currentUser.getZhId()+"' "+cpSql+")";
					btnSql=" AND JE_CORE_RESOURCEBUTTON_ID IN (SELECT CPYHZY_ZYID FROM JE_SAAS_CPYHZY WHERE CPYHZY_TYPE='YHFUNC' AND CPYHZY_ZHID='"+currentUser.getZhId()+"' "+cpSql+")";
					childSql=" AND JE_CORE_FUNCRELATION_ID IN (SELECT CPYHZY_ZYID FROM JE_SAAS_CPYHZY WHERE CPYHZY_TYPE='YHFUNC' AND CPYHZY_ZHID='"+currentUser.getZhId()+"' "+cpSql+")";
				}
			}
		}
		List<DynaBean> funcs=serviceTemplate.selectList("JE_CORE_FUNCINFO",  denySql+whereSql+" AND SY_NODETYPE!='ROOT'"+funcSql,"JE_CORE_FUNCINFO_ID,FUNCINFO_FUNCNAME,FUNCINFO_NODEINFO,FUNCINFO_ICONCLS,FUNCINFO_FUNCCODE,SY_PATH");
		JSONTreeNode rootNode=TreeUtil.buildRootNode();
		//功能按钮
//		Map<String,List<DynaBean>> funcBtns=new HashMap<String,List<DynaBean>>();
//		//子功能
//		Map<String,List<DynaBean>> funcChilds=new HashMap<String,List<DynaBean>>();
		List<String> funcIds=new ArrayList<String>();
		for(DynaBean func:funcs){
			JSONTreeNode node=TreeUtil.buildTreeNode(func.getStr("JE_CORE_FUNCINFO_ID"), func.getStr("FUNCINFO_FUNCNAME"), func.getStr("FUNCINFO_FUNCCODE"), func.getStr("FUNCINFO_NODEINFO"), PermType.FUNC, func.getStr("FUNCINFO_ICONCLS","jeicon jeicon-set-up") , rootNode.getId());
			buildFuncFuncChilds(node, func, btnSql, childSql);
			rootNode.getChildren().add(node);
			funcIds.add(node.getId());
		}
//		List<DynaBean> btns=serviceTemplate.selectList("JE_CORE_RESOURCEBUTTON", "  AND RESOURCEBUTTON_DISABLED!='1' AND RESOURCEBUTTON_FUNCINFO_ID IN ("+StringUtil.buildArrayToString(ArrayUtils.getArray(funcIds))+")"+btnSql);
//		List<DynaBean> childs=serviceTemplate.selectList("JE_CORE_FUNCRELATION", " AND FUNCRELATION_FUNCINFO_ID IN ("+StringUtil.buildArrayToString(ArrayUtils.getArray(funcIds))+")"+childSql);
//		for(DynaBean btn:btns){
//			String funcId=btn.getStr("RESOURCEBUTTON_FUNCINFO_ID");
//			List<DynaBean> btnInfos=funcBtns.get(funcId);
//			if(btnInfos==null){
//				btnInfos=new ArrayList<DynaBean>();
//			}
//			btnInfos.add(btn);
//			funcBtns.put(funcId, btnInfos);
//		}
//		buildFuncFuncChilds(childs, funcChilds,funcBtns,btnSql,childSql);
////		for(DynaBean child:childs){
////			String funcId=child.getStr("FUNCRELATION_FUNCINFO_ID");
////			List<DynaBean> childInfos=funcChilds.get(funcId);
////			if(childInfos==null){
////				childInfos=new ArrayList<DynaBean>();
////			}
////			childInfos.add(child);
////			funcChilds.put(funcId, childInfos);
////		}
////		buildFuncChilds(childs, funcIds, funcChilds, funcMaps)
//		for(JSONTreeNode funcNode:rootNode.getChildren()){
//			buildFuncInfo(funcNode, funcBtns, funcChilds);
//		}
		return rootNode;
	}
	/**
	 * 构建功能授权子功能信息
	 * @param funcNode
	 * @param funcInfo
	 * @param btnSql
	 * @param childSql
	 */
	private void buildFuncFuncChilds(JSONTreeNode funcNode,DynaBean funcInfo,String btnSql,String childSql){
		List<DynaBean> btns=serviceTemplate.selectList("JE_CORE_RESOURCEBUTTON",btnSql+ "  AND RESOURCEBUTTON_DISABLED!='1' AND RESOURCEBUTTON_FUNCINFO_ID = '"+funcInfo.getStr("JE_CORE_FUNCINFO_ID")+"' "+btnSql);
		for(DynaBean btn:btns){
			String btnType="未知";
			if("GRID".equalsIgnoreCase(btn.getStr("RESOURCEBUTTON_TYPE"))){
				btnType="表格";
			}else if("FORM".equalsIgnoreCase(btn.getStr("RESOURCEBUTTON_TYPE"))){
				btnType="表单";
			}else if("ACTION".equalsIgnoreCase(btn.getStr("RESOURCEBUTTON_TYPE"))){
				btnType="ACTION";
			}
			JSONTreeNode btnNode=TreeUtil.buildTreeNode(btn.getStr("JE_CORE_RESOURCEBUTTON_ID"), btn.getStr("RESOURCEBUTTON_NAME")+"->"+btnType, btn.getStr("RESOURCEBUTTON_CODE"), btn.getStr("RESOURCEBUTTON_FUNCINFO_ID"), PermType.BUTTON, "jeicon jeicon-button", funcNode.getId());
			btnNode.setNodeInfo(funcInfo.getStr("JE_CORE_FUNCINFO_ID"));
			funcNode.getChildren().add(btnNode);
		}
		List<DynaBean> funcRelations=serviceTemplate.selectList("JE_CORE_FUNCRELATION", " AND FUNCRELATION_FUNCINFO_ID='"+funcInfo.getStr("JE_CORE_FUNCINFO_ID")+"'");

		for(DynaBean child:funcRelations){
			JSONTreeNode subNode=TreeUtil.buildTreeNode(child.getStr("JE_CORE_FUNCRELATION_ID"), child.getStr("FUNCRELATION_NAME")+"->"+"子功能", child.getStr("FUNCRELATION_CODE"), child.getStr("FUNCRELATION_FUNCINFO_ID"), PermType.SUB_FUNC, "jeicon jeicon-set-up", funcNode.getId());
			subNode.setNodeInfo(funcInfo.getStr("JE_CORE_FUNCINFO_ID"));
			if("func".equals(child.getStr("SY_STATUS")) || "tree".equals(child.getStr("SY_STATUS"))){
				DynaBean childInfo=serviceTemplate.selectOne("JE_CORE_FUNCINFO", " AND FUNCINFO_FUNCCODE='"+child.getStr("FUNCRELATION_CODE")+"'","JE_CORE_FUNCINFO_ID,FUNCINFO_FUNCNAME,FUNCINFO_NODEINFO,FUNCINFO_ICONCLS,FUNCINFO_FUNCCODE,SY_PATH");
				if(childInfo!=null){
					buildFuncFuncChilds(subNode, childInfo,btnSql,childSql);
				}
			}
			funcNode.getChildren().add(subNode);
		}


//			if(ArrayUtils.contains(new String[]{"tree","func"}, funcRelation.getStr("SY_STATUS"))){
//				childIds.add(funcRelation.getStr("FUNCRELATION_FUNCID"));
////				funcIds.add(funcRelation.getStr("FUNCRELATION_FUNCID"));
//				//将子功能加入功能项
//			DynaBean funcInfo=new DynaBean("JE_CORE_FUNCINFO",false);
//			funcInfo.set(BeanUtils.KEY_PK_CODE, "JE_CORE_FUNCINFO_ID");
//			funcInfo.set("JE_CORE_FUNCINFO_ID", funcRelation.getStr("FUNCRELATION_FUNCID"));
//			funcInfo.set("FUNCINFO_FUNCCODE", funcRelation.getStr("FUNCRELATION_CODE"));
//			funcInfo.set("FUNCINFO_FUNCNAME", funcRelation.getStr("FUNCRELATION_NAME"));
////				funcMaps.put(funcRelation.getStr("FUNCRELATION_CODE"), funcInfo);
////			}
//			List<DynaBean> childrens=childMaps.get(funcRelation.getStr("FUNCRELATION_FUNCINFO_ID"));
//			if(childrens==null){
//				childrens=new ArrayList<DynaBean>();
//			}
//			childrens.add(funcRelation);
//			childMaps.put(funcRelation.getStr("FUNCRELATION_FUNCINFO_ID"), childrens);
//
//		}
//		if(childIds.size()>0){
//			List<DynaBean> relations=serviceTemplate.selectList("JE_CORE_FUNCRELATION", "AND FUNCRELATION_FUNCINFO_ID IN ("+StringUtil.buildArrayToString(ArrayUtils.getArray(childIds))+") "+childSql+" ORDER BY SY_ORDERINDEX","JE_CORE_FUNCRELATION_ID,FUNCRELATION_NAME,FUNCRELATION_CODE,FUNCRELATION_FUNCID,SY_STATUS,FUNCRELATION_FUNCINFO_ID,SY_ORDERINDEX");
//			buildFuncFuncChilds(relations, childMaps,funcBtns,btnSql,childSql);
//		}
	}

	/**
	 * 导入功能授权功能
	 * @param funcIds 功能id
	 * @return
	 */
	@Override
	public JSONTreeNode impFuncTreeNode(String funcIds) {
		// TODO Auto-generated method stub
		List<DynaBean> funcInfos=serviceTemplate.selectList("JE_CORE_FUNCINFO", " AND JE_CORE_FUNCINFO_ID IN ("+StringUtil.buildArrayToString(funcIds.split(","))+")");
		for(DynaBean funcInfo:funcInfos){
			DynaBean permission=new DynaBean("JE_CORE_PERMISSION",false);
			permission.set(BeanUtils.KEY_PK_CODE, "PERID");
			permission.set("PERMCODE", funcInfo.getStr("JE_CORE_FUNCINFO_ID"));
			permission.set("PERMTYPE", PermType.FUNC);
			permission.set("MODULE", "");
			permission.set("FUNCID", funcInfo.getStr("JE_CORE_FUNCINFO_ID"));
			serviceTemplate.insert(permission);
		}
		JSONTreeNode rootNode=initFuncTreeNode(" AND JE_CORE_FUNCINFO_ID IN ("+StringUtil.buildArrayToString(funcIds.split(","))+")",false,"");
		return rootNode;
	}

	/**
	 * 删除功能授权功能
	 * @param funcIds 功能id
	 */
	@Override
	public void removeFuncTreeNode(String funcIds) {
		// TODO Auto-generated method stub
		//删除已授权角色权限
		List<DynaBean> funcInfos=serviceTemplate.selectList("JE_CORE_FUNCINFO"," AND JE_CORE_FUNCINFO_ID IN ("+StringUtil.buildArrayToString(funcIds.split(","))+")","JE_CORE_FUNCINFO_ID,FUNCINFO_FUNCCODE");
		String[] funcIdArrays=ArrayUtils.getBeanFieldArray(funcInfos,"JE_CORE_FUNCINFO_ID");
		String[] funcCodeArrays=ArrayUtils.getBeanFieldArray(funcInfos,"FUNCINFO_FUNCCODE");
		String funcInSql=" ("+StringUtil.buildArrayToString(funcIdArrays)+")";
		pcServiceTemplate.executeSql(" DELETE FROM JE_CORE_ROLE_PERM WHERE PERID IN (SELECT PERID FROM JE_CORE_PERMISSION WHERE PERMTYPE='"+PermType.FUNC+"' AND PERMCODE IN "+funcInSql+")");
		pcServiceTemplate.executeSql(" DELETE FROM JE_CORE_ROLEGROUP_PERM WHERE PERID IN (SELECT PERID FROM JE_CORE_PERMISSION WHERE PERMTYPE='"+PermType.FUNC+"' AND PERMCODE IN "+funcInSql+")");
		//删除权限
		pcServiceTemplate.executeSql(" DELETE FROM JE_CORE_PERMISSION WHERE PERMTYPE='"+PermType.FUNC+"' AND PERMCODE IN "+funcInSql+"");
		MenuStaticizeCacheManager.clearAllCache();
		UserHomeCacheManager.clearAllCache();
		staticizeManager.clearFuncPermStatize(StringUtil.buildSplitString(funcCodeArrays,","));
	}
	private void buildFuncInfo(JSONTreeNode funcNode,Map<String,List<DynaBean>> funcBtns,Map<String,List<DynaBean>> funcChilds){
		List<DynaBean> btns=funcBtns.get(funcNode.getId());
		if(btns!=null && btns.size()>0){
			for(DynaBean btn:btns){
				String btnType="未知";
				if("GRID".equalsIgnoreCase(btn.getStr("RESOURCEBUTTON_TYPE"))){
					btnType="表格";
				}else if("FORM".equalsIgnoreCase(btn.getStr("RESOURCEBUTTON_TYPE"))){
					btnType="表单";
				}else if("ACTION".equalsIgnoreCase(btn.getStr("RESOURCEBUTTON_TYPE"))){
					btnType="ACTION";
				}
				JSONTreeNode btnNode=TreeUtil.buildTreeNode(btn.getStr("JE_CORE_RESOURCEBUTTON_ID"), btn.getStr("RESOURCEBUTTON_NAME")+"->"+btnType, btn.getStr("RESOURCEBUTTON_CODE"), "BUTTON", PermType.BUTTON, "jeicon jeicon-button", funcNode.getId());
				funcNode.getChildren().add(btnNode);
			}
		}
		List<DynaBean> childs=funcChilds.get(funcNode.getId());
		if(childs!=null && childs.size()>0){
			for(DynaBean child:childs){
				JSONTreeNode subNode=TreeUtil.buildTreeNode(child.getStr("JE_CORE_FUNCRELATION_ID"), child.getStr("FUNCRELATION_NAME")+"->"+"子功能", child.getStr("FUNCRELATION_CODE"), "SUB_FUNC", PermType.SUB_FUNC, "jeicon jeicon-set-up", funcNode.getId());
				if("func".equals(child.getStr("SY_STATUS")) || "tree".equals(child.getStr("SY_STATUS"))){
					buildFuncChild(subNode, child);
				}
				funcNode.getChildren().add(subNode);
			}
		}
	}
	private void buildFuncChild(JSONTreeNode subNode,DynaBean child){
		DynaBean funcInfo=serviceTemplate.selectOne("JE_CORE_FUNCINFO", " AND FUNCINFO_FUNCCODE='"+child.getStr("FUNCRELATION_CODE")+"'");
		if(funcInfo!=null){
			List<DynaBean> btns=serviceTemplate.selectList("JE_CORE_RESOURCEBUTTON", "  AND RESOURCEBUTTON_DISABLED!='1' AND RESOURCEBUTTON_FUNCINFO_ID='"+funcInfo.getStr("JE_CORE_FUNCINFO_ID")+"' ORDER BY SY_ORDERINDEX");
			for(DynaBean btn:btns){
				String btnType="未知";
				if("GRID".equalsIgnoreCase(btn.getStr("RESOURCEBUTTON_TYPE"))){
					btnType="表格";
				}else if("FORM".equalsIgnoreCase(btn.getStr("RESOURCEBUTTON_TYPE"))){
					btnType="表单";
				}else if("ACTION".equalsIgnoreCase(btn.getStr("RESOURCEBUTTON_TYPE"))){
					btnType="ACTION";
				}
				JSONTreeNode btnNode=TreeUtil.buildTreeNode(btn.getStr("JE_CORE_RESOURCEBUTTON_ID"), btn.getStr("RESOURCEBUTTON_NAME")+"->"+btnType, btn.getStr("RESOURCEBUTTON_CODE"), "BUTTON", PermType.BUTTON, "jeicon jeicon-button", subNode.getId());
				subNode.getChildren().add(btnNode);
			}
			List<DynaBean> childs=serviceTemplate.selectList("JE_CORE_FUNCRELATION", " AND FUNCRELATION_FUNCINFO_ID='"+funcInfo.getStr("JE_CORE_FUNCINFO_ID")+"'");
			for(DynaBean subChild:childs){
				JSONTreeNode node=TreeUtil.buildTreeNode(subChild.getStr("JE_CORE_FUNCRELATION_ID"), subChild.getStr("FUNCRELATION_NAME")+"->"+"子功能", subChild.getStr("FUNCRELATION_CODE"), "SUB_FUNC", PermType.SUB_FUNC, "jeicon jeicon-set-up", subNode.getId());
				if("func".equals(subChild.getStr("SY_STATUS")) || "tree".equals(subChild.getStr("SY_STATUS"))){
					buildFuncChild(node, subChild);
				}
				subNode.getChildren().add(node);
			}
		}
	}
	/**
	 * 构建子功能信息
	 * @param funcRelations
	 * @param funcIds
	 * @param childMaps
	 * @param funcMaps
	 */
	private void buildFuncChilds(List<DynaBean> funcRelations,List<String> funcIds,Map<String,List<DynaBean>> childMaps,Map<String,DynaBean> funcMaps){
		List<String> childIds=new ArrayList<String>();
		for(DynaBean funcRelation:funcRelations){
			if(ArrayUtils.contains(new String[]{"tree","func"}, funcRelation.getStr("SY_STATUS"))){
				childIds.add(funcRelation.getStr("FUNCRELATION_FUNCID"));
				funcIds.add(funcRelation.getStr("FUNCRELATION_FUNCID"));
				//将子功能加入功能项
				DynaBean funcInfo=new DynaBean("JE_CORE_FUNCINFO",false);
				funcInfo.set(BeanUtils.KEY_PK_CODE, "JE_CORE_FUNCINFO_ID");
				funcInfo.set("JE_CORE_FUNCINFO_ID", funcRelation.getStr("FUNCRELATION_FUNCID"));
				funcInfo.set("FUNCINFO_FUNCCODE", funcRelation.getStr("FUNCRELATION_CODE"));
				funcInfo.set("FUNCINFO_FUNCNAME", funcRelation.getStr("FUNCRELATION_NAME"));
				funcMaps.put(funcRelation.getStr("FUNCRELATION_CODE"), funcInfo);
			}
			List<DynaBean> childrens=childMaps.get(funcRelation.getStr("FUNCRELATION_FUNCINFO_ID"));
			if(childrens==null){
				childrens=new ArrayList<DynaBean>();
			}
			childrens.add(funcRelation);
			childMaps.put(funcRelation.getStr("FUNCRELATION_FUNCINFO_ID"), childrens);
		}
		if(childIds.size()>0){
			List<DynaBean> relations=serviceTemplate.selectList("JE_CORE_FUNCRELATION", "AND FUNCRELATION_FUNCINFO_ID IN ("+StringUtil.buildArrayToString(ArrayUtils.getArray(childIds))+") ORDER BY SY_ORDERINDEX","JE_CORE_FUNCRELATION_ID,FUNCRELATION_NAME,FUNCRELATION_CODE,FUNCRELATION_FUNCID,SY_STATUS,FUNCRELATION_FUNCINFO_ID,SY_ORDERINDEX");
			buildFuncChilds(relations, funcIds, childMaps, funcMaps);
		}
	}
	/**
	 * 构建功能树
	 */
	private void buildFuncTree(Map<String,DynaBean> permMap,Map<String,DynaBean> funcMaps,Map<String,List<DynaBean>> childMaps,Map<String,List<DynaBean>> funcBtnMaps,List<DynaBean> targers,String permType,JSONTreeNode node,Boolean haveDeny,Boolean haveButton,Boolean haveSubFunc,Set<String> includes,Set<String> nodeIds,boolean onlyOneChild){
		String funcCode=node.getNodeInfo();
		if(funcCode.indexOf(",")>=0){
			funcCode=funcCode.split(",")[0];
		}
		//如果子功能取编码作为功能编码
		if("SUB_FUNC".equals(node.getNodeInfoType())){
			funcCode=node.getCode();
		}
//		DynaBean funcInfo=serviceTemplate.selectOne("JE_CORE_FUNCINFO"," and FUNCINFO_FUNCCODE='"+funcCode+"'","JE_CORE_FUNCINFO_ID,FUNCINFO_FUNCCODE,SY_PATH");
		DynaBean funcInfo=funcMaps.get(funcCode);
		if(funcInfo!=null){
			if(haveButton){
//				List<DynaBean> buttons=serviceTemplate.selectList("JE_CORE_RESOURCEBUTTON", " AND RESOURCEBUTTON_FUNCINFO_ID='"+funcInfo.getStr("JE_CORE_FUNCINFO_ID")+"' order by SY_ORDERINDEX","JE_CORE_RESOURCEBUTTON_ID,RESOURCEBUTTON_TYPE,RESOURCEBUTTON_NAME,RESOURCEBUTTON_CODE,RESOURCEBUTTON_FUNCINFO_ID,SY_ORDERINDEX");
				List<DynaBean> buttons=funcBtnMaps.get(funcInfo.getStr("JE_CORE_FUNCINFO_ID"));
				if(buttons==null){
					buttons=new ArrayList<DynaBean>();
				}
				for(DynaBean button:buttons){
					JSONTreeNode btnNode=new JSONTreeNode();
					btnNode.setId(button.getStr("JE_CORE_RESOURCEBUTTON_ID"));
					if(nodeIds!=null){
						if(!nodeIds.contains(btnNode.getId())){
							continue;
						}
					}
					String btnType="未知";
					if("GRID".equalsIgnoreCase(button.getStr("RESOURCEBUTTON_TYPE"))){
						btnType="表格";
					}else if("FORM".equalsIgnoreCase(button.getStr("RESOURCEBUTTON_TYPE"))){
						btnType="表单";
					}else if("ACTION".equalsIgnoreCase(button.getStr("RESOURCEBUTTON_TYPE"))){
						btnType="ACTION";
					}
					btnNode.setText(button.getStr("RESOURCEBUTTON_NAME")+"->"+btnType);
					btnNode.setCode(button.getStr("RESOURCEBUTTON_CODE"));
					btnNode.setNodeInfoType("BUTTON");
					//				btnNode.setParent(funcInfo.getStr("JE_CORE_FUNCINFO_ID"));
					btnNode.setParent(node.getId());
					btnNode.setNodePath(funcInfo.getStr("SY_PATH")+"/"+button.getStr("JE_CORE_RESOURCEBUTTON_ID"));
					btnNode.setOrderIndex(button.getStr("SY_ORDERINDEX"));
					btnNode.setIconCls("jeicon jeicon-button");
					btnNode.setNodeInfo(button.getStr("RESOURCEBUTTON_FUNCINFO_ID"));
					DynaBean permission=checkPerm(permMap, targers, permType, btnNode);
					if(haveDeny){
						if(permission!=null){
							btnNode.setChecked(true);
						}else{
							btnNode.setChecked(false);
						}
						node.getChildren().add(btnNode);
					}else{
						if(permission!=null){
							btnNode.setChecked(true);
							node.getChildren().add(btnNode);
						}
					}
				}
			}
			if(haveSubFunc){
//				List<DynaBean> children=serviceTemplate.selectList("JE_CORE_FUNCRELATION", " and FUNCRELATION_FUNCINFO_ID='"+funcInfo.getStr("JE_CORE_FUNCINFO_ID")+"' order by SY_ORDERINDEX", "JE_CORE_FUNCRELATION_ID,FUNCRELATION_NAME,FUNCRELATION_CODE,SY_STATUS,SY_ORDERINDEX");
				List<DynaBean> children=childMaps.get(funcInfo.getStr("JE_CORE_FUNCINFO_ID"));
				if(children==null){
					children=new ArrayList<DynaBean>();
				}
				for(DynaBean subFunc:children){
					if(includes!=null){
						if(!includes.contains(subFunc.getStr("JE_CORE_FUNCRELATION_ID"))){
							continue;
						}
					}
					JSONTreeNode subNode=new JSONTreeNode();
					subNode.setId(subFunc.getStr("JE_CORE_FUNCRELATION_ID"));
					if(nodeIds!=null){
						if(!nodeIds.contains(subNode.getId())){
							continue;
						}
					}
					subNode.setText(subFunc.getStr("FUNCRELATION_NAME")+"->"+"子功能");
					subNode.setCode(subFunc.getStr("FUNCRELATION_CODE"));
					subNode.setNodeInfoType("SUB_FUNC");
					subNode.setNodeInfo(subFunc.getStr("FUNCRELATION_CODE"));
					subNode.setNodePath(funcInfo.getStr("SY_PATH")+"/"+subFunc.getStr("JE_CORE_FUNCRELATION_ID"));
					//				subNode.setParent(funcInfo.getStr("JE_CORE_FUNCINFO_ID"));
					subNode.setParent(node.getId());
					subNode.setOrderIndex(subFunc.getStr("SY_ORDERINDEX"));
					subNode.setNodeInfo(subFunc.getStr("FUNCRELATION_FUNCINFO_ID"));
					subNode.setIconCls("jeicon jeicon-set-up");
					DynaBean permission=checkPerm(permMap, targers, permType, subNode);
					if(haveDeny){
						if(permission!=null){
							subNode.setChecked(true);
						}else{
							subNode.setChecked(false);
						}
						node.getChildren().add(subNode);
					}else{
						if(permission!=null){
							subNode.setChecked(true);
							node.getChildren().add(subNode);
						}
					}
					if(ArrayUtils.contains(new String[]{"tree","func"}, subFunc.getStr("SY_STATUS")) && !onlyOneChild){
						buildFuncTree(permMap,funcMaps,childMaps,funcBtnMaps, targers, permType, subNode, haveDeny,haveButton,haveSubFunc,includes,nodeIds,onlyOneChild);
					}
				}
			}
		}else{
			logger.error("["+node.getText()+"]该功能不存在，请检查菜单配置!");
		}
	}
	/**
	 * 获取权限对象
	 * @param permMap
	 * @param targers
	 * @param permType
	 * @param node
	 * @return
	 */
	private DynaBean checkPerm(Map<String,DynaBean> permMap,List<DynaBean> targers,String permType,JSONTreeNode node){

//		for(DynaBean targer:targers){
//			String targerId="";
//			if(AuthorPermType.PERM_ROLE.equalsIgnoreCase(permType)){
//				targerId=targer.getStr("ROLEID");
//			}else if(AuthorPermType.PERM_ROLEGROUP.equalsIgnoreCase(permType)){
//				targerId=targer.getStr("JE_CORE_ROLEGROUP_ID");
//			}
//			String nodeType=node.getNodeInfoType();;
////			String nodeType="MENU";
////			if(PermType.MENU_TYPE.indexOf(node.getNodeInfoType())!=-1){
////				nodeType=node.getNodeInfoType();
////			}
//			String key=targerId+"_"+;
//			DynaBean permission=permMap.get(key);
//			if(permission!=null){
//				return permission;
//			}else{
//				permission=permMap.get("PUBLIC_"+permType+"_"+node.getId()+"_"+nodeType);
//				if(permission!=null){
//					return permission;
//				}
//			}
//		}
		String nodeType=node.getNodeInfoType();;
		String key=permType+"_"+node.getId()+"_"+nodeType;
		return permMap.get(key);
	}
	/**
	 * 构建菜单类型
	 * @param node
	 */
	public void buildMenuType(JSONTreeNode node){
		String menuType=node.getNodeInfoType();
		if(PermType.MENU.equalsIgnoreCase(menuType)){
			node.setText(node.getText()+"->"+"菜单");
		}else if(PermType.MT.equalsIgnoreCase(menuType)){
			node.setText(node.getText()+"->"+"功能");
		}else if(PermType.IDDT.equalsIgnoreCase(menuType)){
			node.setText(node.getText()+"->"+"个性化");
		}else if(PermType.IFRAME.equalsIgnoreCase(menuType)){
			node.setText(node.getText()+"->"+"IFRAME");
		}else if(PermType.URL.equalsIgnoreCase(menuType)){
			node.setText(node.getText()+"->"+"链接");
		}else if(PermType.PORTAL.equalsIgnoreCase(menuType)){
			node.setText(node.getText()+"->"+"PORTAL");
		}else if(PermType.DIC.equalsIgnoreCase(menuType)){
			node.setText(node.getText()+"->"+"字典功能");
		}
	}
	/**
	 * 得到授权树形
	 * @param rootId 根节点ID
	 * @param targers  目标   角色 /权限组
	 * @param permType 目标类型
	 * @param queryInfo 查询条件(主要用于菜单的过滤)
	 * @return
	 */
	@Override
	public JSONTreeNode getAuthorPermTree(String rootId,List<DynaBean> targers, String permType, QueryInfo queryInfo) {
		// TODO Auto-generated method stub
		return buildPermTree(rootId, targers, permType, true, true, true,false, queryInfo,null,null,"",false,false);
	}
	/**
	 * 查询用户所有权限   用户查看权限
	 * @param user 用户
	 * @return
	 */
	@Override
	public JSONTreeNode getUserPermTree(DynaBean user) {
		// TODO Auto-generated method stub
		List<DynaBean> roles=serviceTemplate.selectList("JE_CORE_ROLE", " and ROLEID IN (SELECT ROLEID FROM JE_CORE_ROLE_USER WHERE USERID='"+user.getStr("USERID")+"')");
		for(DynaBean role:roles){
			//权限组的ids
			String groupCode=role.getStr("GROUPCODE");
			String extendGroupCode=role.getStr("EXTENDGROUPCODE");
			String rejectGroupCode=role.getStr("REJECTGROUPCODE");
			Set<String> codes=new HashSet<String>();
			if(StringUtil.isNotEmpty(extendGroupCode)){
				for(String code:extendGroupCode.split(",")){
					codes.add(code);
				}
			}
			if(StringUtil.isNotEmpty(rejectGroupCode)){
				for(String code:rejectGroupCode.split(",")){
					codes.remove(code);
				}
			}
			if(StringUtil.isNotEmpty(groupCode)){
				for(String code:groupCode.split(",")){
					codes.add(code);
				}
			}
			StringBuffer gCodes=new StringBuffer();
			for(String code:codes){
				gCodes.append(","+code);
			}
			role.set("GROUPCODE", StringUtil.replaceSplit(gCodes.toString()));
		}
		return buildPermTree("ROOT", roles, AuthorPermType.PERM_ROLE, true, true, false,false, new QueryInfo(),null,null,"",true,false);
	}

	/**
	 * 得到角色权限
	 * @param rootId 根节点id
	 * @param roles 角色
	 * @param en TODO 暂不明确
	 * @param includeStr TODO 暂不明确
	 * @param jtgsId TODO 暂不明确
	 * @return
	 */
	@Override
	public JSONTreeNode getMenuTree(String rootId, List<DynaBean> roles,Boolean en,String includeStr,String jtgsId) {
		// TODO Auto-generated method stub
		QueryInfo queryInfo=new QueryInfo();
		queryInfo.setWhereSql(" and SY_STATUS='1'");
		Set<String> includes=null;
		if(StringUtil.isNotEmpty(includeStr)){
			includes=new HashSet<String>();
			for(String resId:includeStr.split(",")){
				includes.add(resId+"#"+AdminPermType.RES_MENU);
			}
		}
		return buildPermTree(rootId, roles, AuthorPermType.PERM_ROLE, false, false, false,en, queryInfo,includes,null,jtgsId,true,false);
	}

	/**
	 * 得到手机端菜单权限
	 * @param apkId TODO 暂不明确
	 * @param roles TODO 暂不明确
	 * @param permType 目标类型
	 * @param havePublic 是否公开
	 * @return
	 */
	@Override
	public List<String> getAppMenuPerm(String apkId,List<DynaBean> roles,String permType,boolean havePublic){
//		for(DynaBean targer:roles){
//			String whereSql="";
//			String targerId="";
//			if(AuthorPermType.PERM_ROLE.equalsIgnoreCase(permType)){
//				targerId=targer.getStr("ROLEID");
//				//处理聚合权限组
////				String groupCode=targer.getStr("GROUPCODE", "");
////				if(haveGroup && StringUtil.isNotEmpty(groupCode)){
////					whereSql=" AND (PERID IN (SELECT PERID FROM JE_CORE_ROLE_PERM WHERE ENABLED='1' AND ROLEID='"+targerId+"') OR PERID IN (SELECT PERID FROM JE_CORE_ROLEGROUP_PERM WHERE ENABLED='1' AND ROLEGROUPID in ("+StringUtil.buildArrayToString(groupCode.split(","))+")))";
////				}else{
//				whereSql=" AND PERID IN (SELECT PERID FROM JE_CORE_ROLE_PERM WHERE ENABLED='1' AND ROLEID='"+targerId+"')";
////				}
//			}else if(AuthorPermType.PERM_ROLEGROUP.equalsIgnoreCase(permType)){
//				targerId=targer.getStr("JE_CORE_ROLEGROUP_ID");
//				whereSql=" AND PERID IN (SELECT PERID FROM JE_CORE_ROLEGROUP_PERM WHERE ENABLED='1' AND ROLEGROUPID='"+targerId+"')";
//			}
//			whereSql+=" and PERMTYPE!='"+PermType.APPBUTTON+"'";
//			whereSql+=" and PERMTYPE!='"+PermType.APPCHILDFUNC+"'";
//			whereSql+=" and MODULE in ("+StringUtil.buildArrayToString(apkId.split(","))+")";
//			List<DynaBean> permissions=serviceTemplate.selectList("JE_CORE_PERMISSION", whereSql);
//			for(DynaBean perm:permissions){
////				String key=targerId+"_"+permType+"_"+perm.getStr("PERMCODE")+"_"+perm.getStr("PERMTYPE");
////				permMap.put(key, perm);
//				lists.add(perm.getStr("PERMCODE"));
//			}
//		}
//		if(havePublic){
//			String whereSql="";
//			whereSql+=" and PERMTYPE!='"+PermType.APPBUTTON+"'";
//			whereSql+=" and PERMTYPE!='"+PermType.APPCHILDFUNC+"'";
//			whereSql+=" and MODULE in ("+StringUtil.buildArrayToString(apkId.split(","))+")";
//			List<DynaBean> permissions=serviceTemplate.selectList("JE_CORE_PERMISSION", " AND PERID IN (SELECT PERMID FROM JE_CORE_PUBLICPERM) "+whereSql);
//			for(DynaBean perm:permissions){
//				lists.add(perm.getStr("PERMCODE"));
//			}
//		}
		List<String> lists=new ArrayList<String>();
		StringBuffer whereSql=new StringBuffer();
		String publicPermSql="";
		Set<String> targerIds=new HashSet<>();
		whereSql.append(" and PERMTYPE NOT IN ('"+PermType.APPBUTTON+"','"+PermType.APPCHILDFUNC+"')");
		whereSql.append(" and MODULE in ("+StringUtil.buildArrayToString(apkId.split(","))+")");
		if(havePublic){
			publicPermSql=" OR PERID IN (SELECT PERMID FROM JE_CORE_PUBLICPERM)";
		}
		for(DynaBean targer:roles){
			targerIds.add(targer.getStr("ROLEID"));
		}
		whereSql.append(" AND (PERID IN (SELECT PERID FROM JE_CORE_ROLE_PERM WHERE ENABLED='1' AND ROLEID IN ("+StringUtil.buildArrayToString(ArrayUtils.getArray(targerIds))+"))"+publicPermSql+")");
		List<DynaBean> permissions=serviceTemplate.selectList("JE_CORE_PERMISSION", whereSql.toString());
		for(DynaBean perm:permissions){
			lists.add(perm.getStr("PERMCODE"));
		}
		//处理聚合MAP
		return lists;
	}

	/**
	 * 得到手机端菜单权限
	 * @param funcIds 功能ids
	 * @param roles 角色
	 * @param permType 目标类型
	 * @param havePublic 是否公开
	 * @return
	 */
	@Override
	public List<String> getAppFuncPerm(String funcIds,List<DynaBean> roles,String permType,boolean havePublic){
//		List<String> lists=new ArrayList<String>();
//		for(DynaBean targer:roles){
//			String whereSql="";
//			String targerId="";
//			if(AuthorPermType.PERM_ROLE.equalsIgnoreCase(permType)){
//				targerId=targer.getStr("ROLEID");
//				//处理聚合权限组
////				String groupCode=targer.getStr("GROUPCODE", "");
////				if(haveGroup && StringUtil.isNotEmpty(groupCode)){
////					whereSql=" AND (PERID IN (SELECT PERID FROM JE_CORE_ROLE_PERM WHERE ENABLED='1' AND ROLEID='"+targerId+"') OR PERID IN (SELECT PERID FROM JE_CORE_ROLEGROUP_PERM WHERE ENABLED='1' AND ROLEGROUPID in ("+StringUtil.buildArrayToString(groupCode.split(","))+")))";
////				}else{
//				whereSql=" AND PERID IN (SELECT PERID FROM JE_CORE_ROLE_PERM WHERE ENABLED='1' AND ROLEID='"+targerId+"')";
////				}
//			}else if(AuthorPermType.PERM_ROLEGROUP.equalsIgnoreCase(permType)){
//				targerId=targer.getStr("JE_CORE_ROLEGROUP_ID");
//				whereSql=" AND PERID IN (SELECT PERID FROM JE_CORE_ROLEGROUP_PERM WHERE ENABLED='1' AND ROLEGROUPID='"+targerId+"')";
//			}
//			whereSql+=" and PERMTYPE in ('"+PermType.APPBUTTON+"','"+PermType.APPCHILDFUNC+"','"+PermType.APPFUNC+"')";
////			whereSql+=" and PERMTYPE!='"+PermType.APPCHILDFUNC+"'";
//			whereSql+=" and FUNCID in ("+StringUtil.buildArrayToString(funcIds.split(","))+")";
//			List<DynaBean> permissions=serviceTemplate.selectList("JE_CORE_PERMISSION", whereSql);
//			for(DynaBean perm:permissions){
////				String key=targerId+"_"+permType+"_"+perm.getStr("PERMCODE")+"_"+perm.getStr("PERMTYPE");
////				permMap.put(key, perm);
//				lists.add(perm.getStr("PERMCODE"));
//			}
//		}
//		if(havePublic){
//			String whereSql="";
//			whereSql+=" and PERMTYPE in ('"+PermType.APPBUTTON+"','"+PermType.APPCHILDFUNC+"','"+PermType.APPFUNC+"')";
////			whereSql+=" and PERMTYPE!='"+PermType.APPCHILDFUNC+"'";
//			whereSql+=" and FUNCID in ("+StringUtil.buildArrayToString(funcIds.split(","))+")";
//			List<DynaBean> permissions=serviceTemplate.selectList("JE_CORE_PERMISSION", " AND PERID IN (SELECT PERMID FROM JE_CORE_PUBLICPERM) "+whereSql);
//			for(DynaBean perm:permissions){
//				lists.add(perm.getStr("PERMCODE"));
//			}
//		}
		List<String> lists=new ArrayList<String>();
		StringBuffer whereSql=new StringBuffer();
		String publicPermSql="";
		Set<String> targerIds=new HashSet<>();
		whereSql.append(" and PERMTYPE in ('"+PermType.APPBUTTON+"','"+PermType.APPCHILDFUNC+"','"+PermType.APPFUNC+"')");
		whereSql.append(" and FUNCID in ("+StringUtil.buildArrayToString(funcIds.split(","))+")");
		if(havePublic){
			publicPermSql=" OR PERID IN (SELECT PERMID FROM JE_CORE_PUBLICPERM)";
		}
		for(DynaBean targer:roles){
			targerIds.add(targer.getStr("ROLEID"));
		}
		whereSql.append(" AND (PERID IN (SELECT PERID FROM JE_CORE_ROLE_PERM WHERE ENABLED='1' AND ROLEID IN ("+StringUtil.buildArrayToString(ArrayUtils.getArray(targerIds))+"))"+publicPermSql+")");
		List<DynaBean> permissions=serviceTemplate.selectList("JE_CORE_PERMISSION", whereSql.toString());
		for(DynaBean perm:permissions){
			lists.add(perm.getStr("PERMCODE"));
		}
		//处理聚合MAP
		return lists;
	}

	/**
	 * 得到权限组
	 * @param rootId 根节点id
	 * @param roleGroups 角色权限组
	 * @return
	 */
	@Override
	public JSONTreeNode getRoleGroupTree(String rootId,List<DynaBean> roleGroups) {
		// TODO Auto-generated method stub
		QueryInfo queryInfo=new QueryInfo();
		queryInfo.setWhereSql(" and SY_STATUS='1'");
		return buildPermTree(rootId, roleGroups, AuthorPermType.PERM_ROLEGROUP, true, true, true,false, queryInfo,null,null,"",false,false);
	}

	/**
	 * 得到所有权限树
	 * @param rootId 根节点id
	 * @param queryInfo 查询信息
	 * @param cpId TODO 暂不明确
	 * @return
	 */
	@Override
	public JSONTreeNode getPermTree(String rootId, QueryInfo queryInfo,String cpId) {
		// TODO Auto-generated method stub
		EndUser currentUser=SecurityUserHolder.getCurrentUser();
		Set<String> includes=null;
		Set<String> nodeIds=null;
		if("1".equals(WebUtils.getSysVar("JE_SYS_ADMINPERM"))){
			if(!WebUtils.getSysVar("JE_SYS_ADMIN").equals(currentUser.getBackUserCode())){
				includes=adminPermManager.getUserPerms(currentUser.getUserId(), " AND ADMINPERM_TYPE IN ('"+AdminPermType.RES_MENU+"','"+AdminPermType.RES_SUBFUNC+"')");
			}
		}
		if(WebUtils.isSaas()){
			if(currentUser.getSaas()){
				if(StringUtil.isNotEmpty(cpId)){
					nodeIds=new HashSet<String>();
					List<DynaBean> perms=serviceTemplate.selectList("JE_SAAS_CPYHZY", " AND CPYHZY_TYPE='CP' AND CPYHZY_CPID='"+cpId+"'");
					for(DynaBean perm:perms){
						nodeIds.add(perm.getStr("CPYHZY_ZYID"));
					}
				}
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
		return buildPermTree(rootId, new ArrayList<DynaBean>(), AuthorPermType.PERM_ROLE, true, true, true,false, queryInfo,includes,nodeIds,"",false,false);
	}
	/**
	 * 更新权限
	 * @param targer   目标
	 * @param permType 目标类型
	 * @param delPerm  删除的权限
	 * @param addPerm  添加的权限
	 */
	@Override
	public void doUpdatePermData(DynaBean targer, String permType,String delPerm, String addPerm,Boolean isHard) {
		// TODO Auto-generated method stub
		String[] permIds=addPerm.split(",");
		String cpId=targer.getStr("CPID","");
		List<DynaBean> permissions=new ArrayList<DynaBean>();
		Boolean haveDic=false;
		if(StringUtil.isNotEmpty(addPerm)){
			for(String permId:permIds){
				String[] permArray=permId.split("#");
				String moduleCode=permArray[0];
				String permsType=permArray[1];
				String permCode=permArray[2];
				String funcId="";
				if("DIC".equals(permsType)){
					haveDic=true;
				}
				//把功能主键也放入
				if(PermType.BUTTON.equalsIgnoreCase(permsType)){
					DynaBean button=serviceTemplate.selectOneByPk("JE_CORE_RESOURCEBUTTON",permCode,"JE_CORE_RESOURCEBUTTON_ID,RESOURCEBUTTON_FUNCINFO_ID");
					if(button!=null){
						funcId=button.getStr("RESOURCEBUTTON_FUNCINFO_ID");
					}
				}else if(PermType.SUB_FUNC.equalsIgnoreCase(permsType)){
					DynaBean funRelation=serviceTemplate.selectOneByPk("JE_CORE_FUNCRELATION", permCode,"JE_CORE_FUNCRELATION_ID,FUNCRELATION_FUNCINFO_ID");
					if(funRelation!=null){
						funcId=funRelation.getStr("FUNCRELATION_FUNCINFO_ID");
					}
				}else if(PermType.APPCHILDFUNC.equalsIgnoreCase(permsType)){
					funcId=permArray[3];
				}else if(PermType.APPBUTTON.equalsIgnoreCase(permsType)){
					funcId=permArray[3];
				}else{
//					permission.set("FUNCID", "");
				}
				DynaBean permission=serviceTemplate.selectOne("JE_CORE_PERMISSION", " and PERMTYPE='"+permsType+"' and PERMCODE='"+permCode+"'");
				if(permission==null){
					permission=new DynaBean("JE_CORE_PERMISSION",false);
					permission.set(BeanUtils.KEY_PK_CODE, "PERID");
					permission.set("PERMCODE", permCode);
					permission.set("PERMTYPE", permsType);
					permission.set("MODULE", moduleCode);
					permission.set("FUNCID",funcId);
					permission=serviceTemplate.insert(permission);
				}else{
					Boolean updateFlag=false;
					if(!permission.getStr("MODULE").equals(moduleCode)){
						permission.set("MODULE", moduleCode);
						updateFlag=true;

					}
					if(!permission.getStr("FUNCID").equals(funcId)){
						permission.set("FUNCID", funcId);
						updateFlag=true;
					}
					if(updateFlag){
						permission=serviceTemplate.update(permission);
					}
				}
				permissions.add(permission);
			}
		}
		if(haveDic){
			//查询到字典功能的按钮权限
			List<DynaBean> dicBtnPerms=serviceTemplate.selectList("JE_CORE_PERMISSION", " AND PERMCODE in (SELECT JE_CORE_RESOURCEBUTTON_ID FROM JE_CORE_RESOURCEBUTTON WHERE RESOURCEBUTTON_FUNCINFO_ID IN (SELECT JE_CORE_FUNCINFO_ID FROM JE_CORE_FUNCINFO WHERE FUNCINFO_FUNCCODE in ('JE_CORE_DICTIONARYITEM','JE_CORE_DICTIONARYITEM_ZWF')))");
			for(DynaBean dicBtnPerm:dicBtnPerms){
				permissions.add(dicBtnPerm);
			}
		}
		if(AuthorPermType.PERM_ROLE.equalsIgnoreCase(permType)){
			RolePermNode rootNode=buildRolePermTree(targer.getStr("ROLEID"));
			//如果是行业SAAS模式，则级联用户的权限信息
			if(WebUtils.isSaas()){
				EndUser currentUser=SecurityUserHolder.getCurrentUser();
				if(currentUser.getSaas()){
					targer=serviceTemplate.selectOneByPk("JE_CORE_ROLE", targer.getStr("ROLEID"));
					if("CP".equals(targer.getStr("ROLERANK"))){;
						String baseRoleId=targer.getStr("BASEROLEID");
						String cpSql=" AND ZHID IN (SELECT JE_SAAS_YH_ID FROM JE_SAAS_CPYH WHERE 1=1 "+WebUtils.getCpYhWhere()+" AND JE_SAAS_CP_ID='"+cpId+"')";
						if(StringUtil.isEmpty(cpId)){
							cpSql="";
						}
						List<DynaBean> roles=serviceTemplate.selectList("JE_CORE_ROLE", " AND ROLETYPE='ROLE' AND ROLERANK='YH' AND BASEROLEID='"+baseRoleId+"' "+cpSql);//找到安装该产品的
						for(DynaBean role:roles){
							RolePermNode saasNode=buildRolePermTree(role.getStr("ROLEID"));
							rootNode.getChildren().add(saasNode);
							//删除子项静态化
							String clearSql=" AND STATICIZE_TYPE IN ('MENU','FUNCPERM') ";
							staticizeManager.clearUserStatize(" AND USERID IN (SELECT USERID FROM JE_CORE_ROLE_USER WHERE ROLEID IN (SELECT ROLEID FROM JE_CORE_ROLE WHERE 1=1 AND PATH LIKE '%"+role.getStr("ROLEID")+"%' AND ROLERANK='YH'))",false);
						}
//						for(DynaBean r:roles){
//							RolePermNode node=new RolePermNode();
//							node.setId(r.getStr("ROLEID"));
//							node.setText(r.getStr("ROLENAME"));
//							node.setCode(r.getStr("ROLECODE"));
//							node.setGroupName(r.getStr("GROUPNAME"));
//							node.setGroupCode(r.getStr("GROUPCODE"));
//							node.setExtendGroupName(r.getStr("EXTENDGROUPNAME"));
//							node.setExtendGroupCode(r.getStr("EXTENDGROUPCODE"));
//							node.setRejectGroupName(r.getStr("REJECTGROUPNAME"));
//							node.setRejectGroupCode(r.getStr("REJECTGROUPCODE"));
//							node.setPath(targer.getStr("PATH")+"/"+r.getStr("ROLEID"));
//							node.setParent(targer.getStr("ROLEID"));
//							rootNode.getChildren().add(node);
//						}
					}
				}
			}
			rootNode.updatePerm(delPerm, permissions, isHard, PermExtendType.PERM_SELF);
		}else{
//			RoleGroupPermNode rootNode=buildRoleGroupTree(targer.getStr("JE_CORE_ROLEGROUP_ID"));
//			rootNode.updatePerm(delPerm, permissions, isHard, PermExtendType.PERM_SELF);
			doUpdateGroupPerm(targer.getStr("JE_CORE_ROLEGROUP_ID"), delPerm, permissions);
		}
		//影响过的人员和功能重新静态化
//		if("1".equals(WebUtils.sysVar.get("JE_SYS_STATICIZE"))){
		String sysMode=WebUtils.getSysVar("JE_CORE_MODE");
		if(JECoreMode.DEVELOP.equals(sysMode))return;
		if(PCDaoTemplateImpl.DBNAME.equals(ConstantVars.STR_ORACLE)){
			String userSql="";
			//修改，由于修改角色的静态化，  角色的人员一旦多，  内存会溢出，现修改成  直接删除静态化   下次用于首次进来重新静态化
			if(AuthorPermType.PERM_ROLE.equalsIgnoreCase(permType)){
				userSql=" AND USERID IN (SELECT USERID FROM JE_CORE_ROLE_USER WHERE ROLEID IN (SELECT ROLEID FROM JE_CORE_ROLE WHERE PATH LIKE '%"+targer.getStr("ROLEID")+"%'))";
			}else{
				userSql=" AND USERID IN (SELECT USERID FROM JE_CORE_ROLE_USER WHERE ROLEID IN (SELECT ROLEID FROM JE_CORE_ROLE WHERE 1=1 AND (GROUPCODE LIKE '%"+targer.getStr("JE_CORE_ROLEGROUP_ID")+"%' OR (EXTENDGROUPCODE LIKE '%"+targer.getStr("JE_CORE_ROLEGROUP_ID")+"%' AND REJECTGROUPCODE NOT LIKE '%"+targer.getStr("JE_CORE_ROLEGROUP_ID")+"%'))))";
			}
			staticizeManager.clearUserStatize(userSql,false);
		}else{
			//查询所有影响权限的用户
			String userSql="";
			if(AuthorPermType.PERM_ROLE.equalsIgnoreCase(permType)){
				userSql=" AND USERID IN (SELECT USERID FROM JE_CORE_ROLE_USER WHERE ROLEID IN (SELECT ROLEID FROM JE_CORE_ROLE WHERE PATH LIKE '%"+targer.getStr("ROLEID")+"%'))";
			}else{
				userSql=" AND USERID IN (SELECT USERID FROM JE_CORE_ROLE_USER WHERE ROLEID IN (SELECT ROLEID FROM JE_CORE_ROLE WHERE 1=1 AND (GROUPCODE LIKE '%"+targer.getStr("JE_CORE_ROLEGROUP_ID")+"%' OR (EXTENDGROUPCODE LIKE '%"+targer.getStr("JE_CORE_ROLEGROUP_ID")+"%' AND REJECTGROUPCODE NOT LIKE '%"+targer.getStr("JE_CORE_ROLEGROUP_ID")+"%'))))";
			}
			staticizeManager.clearUserStatize(userSql,false);
//				List<DynaBean> users=serviceTemplate.selectList("JE_CORE_ENDUSER", userSql,"USERID,USERNAME,USERCODE");
//				for(DynaBean user:users){
//					MenuStaticizeCacheManager.removeCache(user.getStr("USERID"));
//					Set<String> funcCodes=new HashSet<String>();
//					Set<String> funcIds=new HashSet<String>();
//					if(StringUtil.isNotEmpty(delPerm)){
//						 List<DynaBean> perms=serviceTemplate.selectList("JE_CORE_PERMISSION", " AND PERMCODE IN ("+StringUtil.buildArrayToString(delPerm.split(","))+")");
//						 for(DynaBean perm:perms){
//							 if(ArrayUtils.contains(new String[]{PermType.BUTTON,PermType.SUB_FUNC},perm.getStr("PERMTYPE"))){
//								 funcIds.add(perm.getStr("FUNCID"));
//							 }
//						 }
//					}
//					for(DynaBean perm:permissions){
//						if(ArrayUtils.contains(new String[]{PermType.BUTTON,PermType.SUB_FUNC},perm.getStr("PERMTYPE"))){
//							 funcIds.add(perm.getStr("FUNCID"));
//						}
//					}
//					if(funcIds.size()>0){
//						List<DynaBean> funcInfos=serviceTemplate.selectList("JE_CORE_FUNCINFO"," AND JE_CORE_FUNCINFO_ID IN ("+StringUtil.buildArrayToString(ArrayUtils.getArray(funcIds))+")","JE_CORE_FUNCINFO_ID,FUNCINFO_FUNCCODE");
//						for(DynaBean funcInfo:funcInfos){
//							funcCodes.add(funcInfo.getStr("FUNCINFO_FUNCCODE"));
//						}
//					}
//					for(String funcCode:funcCodes){
//						FuncPermStaticizeCacheManager.removeCache(funcCode+"_"+user.getStr("USERID"));
//					}
//				}
		}
//		}

	}
	/**
	 * 更新权限
	 * @param targer   目标
	 * @param permType 目标类型
	 * @param delPerm  删除的权限
	 * @param addPerm  添加的权限
	 */
	@Override
	public void doUpdatePublicPermData(DynaBean targer, String permType,String delPerm, String addPerm) {
		// TODO Auto-generated method stub
		String[] permIds=addPerm.split(",");
		List<DynaBean> permissions=new ArrayList<DynaBean>();
		Boolean haveDic=false;
		if(StringUtil.isNotEmpty(addPerm)){
			if(permType.equals("menu")){
				for(String permId:permIds){
					String[] permArray=permId.split("#");
					String moduleCode=permArray[0];
					String permsType=permArray[1];
					String permCode=permArray[2];
					if("DIC".equals(permsType)){
						haveDic=true;
					}
					DynaBean permission=serviceTemplate.selectOne("JE_CORE_PERMISSION", " and PERMTYPE='"+permsType+"' and PERMCODE='"+permCode+"'");
					if(permission==null){
						permission=new DynaBean("JE_CORE_PERMISSION",false);
						permission.set(BeanUtils.KEY_PK_CODE, "PERID");
						permission.set("PERMCODE", permCode);
						permission.set("PERMTYPE", permsType);
						permission.set("MODULE", moduleCode);
						//把功能主键也放入
						if(PermType.BUTTON.equalsIgnoreCase(permsType)){
							DynaBean button=serviceTemplate.selectOneByPk("JE_CORE_RESOURCEBUTTON",permCode);
							if(button!=null){
								permission.set("FUNCID", button.getStr("RESOURCEBUTTON_FUNCINFO_ID"));
							}
						}else if(PermType.SUB_FUNC.equalsIgnoreCase(permsType)){
							DynaBean funRelation=serviceTemplate.selectOneByPk("JE_CORE_FUNCRELATION", permCode);
							if(funRelation!=null){
								permission.set("FUNCID", funRelation.getStr("FUNCRELATION_FUNCINFO_ID"));
							}
						}else if(PermType.APPCHILDFUNC.equalsIgnoreCase(permsType)){
							permission.set("FUNCID", permArray[3]);
						}else if(PermType.APPBUTTON.equalsIgnoreCase(permsType)){
							permission.set("FUNCID", permArray[3]);
						}else{
							permission.set("FUNCID", "");
						}
						permission=serviceTemplate.insert(permission);
					}else{
						if(!permission.getStr("MODULE").equals(moduleCode)){
							permission.set("MODULE", moduleCode);
							permission=serviceTemplate.update(permission);
						}

					}
					permissions.add(permission);
				}
			}else if("func".equals(permType)){

			}else{

			}
		}
		if(haveDic){
			//查询到字典功能的按钮权限
			List<DynaBean> dicBtnPerms=serviceTemplate.selectList("JE_CORE_PERMISSION", " AND PERMCODE in (SELECT JE_CORE_RESOURCEBUTTON_ID FROM JE_CORE_RESOURCEBUTTON WHERE RESOURCEBUTTON_FUNCINFO_ID IN (SELECT JE_CORE_FUNCINFO_ID FROM JE_CORE_FUNCINFO WHERE FUNCINFO_FUNCCODE in ('JE_CORE_DICTIONARYITEM','JE_CORE_DICTIONARYITEM_ZWF')))");
			for(DynaBean dicBtnPerm:dicBtnPerms){
				permissions.add(dicBtnPerm);
			}
		}
		for(DynaBean perm:permissions){
			String permId=perm.getStr("PERID");
			long count=serviceTemplate.selectCount("JE_CORE_PUBLICPERM", " AND PERMID='"+permId+"'");
			if(count<=0){
				DynaBean permInfo=new DynaBean("JE_CORE_PUBLICPERM",true);
				permInfo.set("PERMID", permId);
				permInfo.set("TYPE", permType);
				serviceTemplate.insert(permInfo);
			}
		}
		if(StringUtil.isNotEmpty(delPerm)){
			pcServiceTemplate.executeSql(" DELETE FROM JE_CORE_PUBLICPERM WHERE PERMID IN (SELECT PERID FROM JE_CORE_PERMISSION WHERE PERMCODE IN ("+StringUtil.buildArrayToString(delPerm.split(","))+"))");
		}
		//影响过的人员和功能重新静态化
		staticizeManager.clearUserStatize("",false);
	}
	/**
	 * 更新权限
	 * @param targer   目标
	 * @param permType 目标类型
	 * @param delPerm  删除的权限
	 * @param addPerm  添加的权限
	 */
	@Override
	public void doUpdateFuncPermData(DynaBean targer, String permType,String delPerm, String addPerm,Boolean isHard) {
		// TODO Auto-generated method stub
		String[] permIds=addPerm.split(",");
		String cpId=targer.getStr("CPID","");
		List<DynaBean> permissions=new ArrayList<DynaBean>();
		if(StringUtil.isNotEmpty(addPerm)){
			for(String permId:permIds){
				String[] permArray=permId.split("#");
				String moduleCode=permArray[0];
				String permsType=permArray[1];
				String permCode=permArray[2];
				DynaBean permission=serviceTemplate.selectOne("JE_CORE_PERMISSION", " and PERMTYPE='"+permsType+"' and PERMCODE='"+permCode+"'");
				if(permission==null){
					permission=new DynaBean("JE_CORE_PERMISSION",false);
					permission.set(BeanUtils.KEY_PK_CODE, "PERID");
					permission.set("PERMCODE", permCode);
					permission.set("PERMTYPE", permsType);
					permission.set("MODULE", moduleCode);
					//把功能主键也放入
					if(PermType.BUTTON.equalsIgnoreCase(permsType)){
						DynaBean button=serviceTemplate.selectOneByPk("JE_CORE_RESOURCEBUTTON",permCode);
						if(button!=null){
							permission.set("FUNCID", button.getStr("RESOURCEBUTTON_FUNCINFO_ID"));
						}
					}else if(PermType.SUB_FUNC.equalsIgnoreCase(permsType)){
						DynaBean funRelation=serviceTemplate.selectOneByPk("JE_CORE_FUNCRELATION", permCode);
						if(funRelation!=null){
							permission.set("FUNCID", funRelation.getStr("FUNCRELATION_FUNCINFO_ID"));
						}
					}else{
						permission.set("FUNCID", "");
					}
					permission=serviceTemplate.insert(permission);
				}
				permissions.add(permission);
			}
		}
		if(AuthorPermType.PERM_ROLE.equalsIgnoreCase(permType)){
			RolePermNode rootNode=buildRolePermTree(targer.getStr("ROLEID"));
			rootNode.updatePerm(delPerm, permissions, isHard, PermExtendType.PERM_SELF);
			//如果是行业SAAS模式，则级联用户的权限信息
			if(WebUtils.isSaas()){
				EndUser currentUser=SecurityUserHolder.getCurrentUser();
				if(currentUser.getSaas()){
					targer=serviceTemplate.selectOneByPk("JE_CORE_ROLE", targer.getStr("ROLEID"));
					if("CP".equals(targer.getStr("ROLERANK"))){
						String baseRoleId=targer.getStr("BASEROLEID");
						String cpSql=" AND ZHID IN (SELECT JE_SAAS_YH_ID FROM JE_SAAS_CPYH WHERE 1=1 "+WebUtils.getCpYhWhere()+" AND JE_SAAS_CP_ID='"+cpId+"')";
						if(StringUtil.isEmpty(cpId)){
							cpSql="";
						}
						List<DynaBean> roles=serviceTemplate.selectList("JE_CORE_ROLE", " AND ROLERANK='YH' AND BASEROLEID='"+baseRoleId+"'"+cpSql);
						for(DynaBean role:roles){
							RolePermNode saasNode=buildRolePermTree(role.getStr("ROLEID"));
							rootNode.getChildren().add(saasNode);
							//删除子项静态化
							String clearSql=" AND STATICIZE_TYPE IN ('FUNCPERM') AND ";
//							staticizeManager.clearStaticize(clearSql);
							staticizeManager.clearUserStatize("AND USERID IN (SELECT USERID FROM JE_CORE_ROLE_USER WHERE ROLEID IN (SELECT ROLEID FROM JE_CORE_ROLE WHERE 1=1 AND PATH LIKE '%"+role.getStr("ROLEID")+"%' AND ROLERANK='YH'))",false);
						}
					}
				}
			}
		}else{
//			RoleGroupPermNode rootNode=buildRoleGroupTree(targer.getStr("JE_CORE_ROLEGROUP_ID"));
//			rootNode.updatePerm(delPerm, permissions, isHard, PermExtendType.PERM_SELF);
			doUpdateGroupPerm(targer.getStr("JE_CORE_ROLEGROUP_ID"), delPerm, permissions);
		}
		String userSql="";
		//修改，由于修改角色的静态化，  角色的人员一旦多，  内存会溢出，现修改成  直接删除静态化   下次用于首次进来重新静态化
		if(AuthorPermType.PERM_ROLE.equalsIgnoreCase(permType)){
			userSql=" AND USERID IN (SELECT USERID FROM JE_CORE_ROLE_USER WHERE ROLEID IN (SELECT ROLEID FROM JE_CORE_ROLE WHERE PATH LIKE '%"+targer.getStr("ROLEID")+"%'))";
		}else{
			userSql=" AND USERID IN (SELECT USERID FROM JE_CORE_ROLE_USER WHERE ROLEID IN (SELECT ROLEID FROM JE_CORE_ROLE WHERE 1=1 AND (GROUPCODE LIKE '%"+targer.getStr("JE_CORE_ROLEGROUP_ID")+"%' OR (EXTENDGROUPCODE LIKE '%"+targer.getStr("JE_CORE_ROLEGROUP_ID")+"%' AND REJECTGROUPCODE NOT LIKE '%"+targer.getStr("JE_CORE_ROLEGROUP_ID")+"%'))))";
		}
//		List<DynaBean> users=serviceTemplate.selectList("JE_CORE_ENDUSER", userSql,"USERID,USERNAME,USERCODE");
		staticizeManager.clearUserStatize(userSql,false);
//		for(DynaBean user:users){
//			MenuStaticizeCacheManager.removeCache(user.getStr("USERID"));
////			Set<String> funcCodes=new HashSet<String>();
////			Set<String> funcIds=new HashSet<String>();
////			if(StringUtil.isNotEmpty(delPerm)){
////				 List<DynaBean> perms=serviceTemplate.selectList("JE_CORE_PERMISSION", " AND PERMCODE IN ("+StringUtil.buildArrayToString(delPerm.split(","))+")");
////				 for(DynaBean perm:perms){
////					 if(ArrayUtils.contains(new String[]{PermType.BUTTON,PermType.SUB_FUNC},perm.getStr("PERMTYPE"))){
////						 funcIds.add(perm.getStr("FUNCID"));
////					 }
////				 }
////			}
////			for(DynaBean perm:permissions){
////				if(ArrayUtils.contains(new String[]{PermType.BUTTON,PermType.SUB_FUNC},perm.getStr("PERMTYPE"))){
////					 funcIds.add(perm.getStr("FUNCID"));
////				}
////			}
////			if(funcIds.size()>0){
////				List<DynaBean> funcInfos=serviceTemplate.selectList("JE_CORE_FUNCINFO"," AND JE_CORE_FUNCINFO_ID IN ("+StringUtil.buildArrayToString(ArrayUtils.getArray(funcIds))+")","JE_CORE_FUNCINFO_ID,FUNCINFO_FUNCCODE");
////				for(DynaBean funcInfo:funcInfos){
////					funcCodes.add(funcInfo.getStr("FUNCINFO_FUNCCODE"));
////				}
////			}
////			for(String funcCode:funcCodes){
////				FuncPermStaticizeCacheManager.removeCache(funcCode+"_"+user.getStr("USERID"));
////			}
//		}

	}
	/**
	 * 更新权限组中权限
	 */
	private void doUpdateGroupPerm(String roleGroupId,String delPerm,List<DynaBean> addPerms){
		//查询到所有聚合过该权限组的所有角色
		List<DynaBean> roles=serviceTemplate.selectList("JE_CORE_ROLE", " AND (GROUPCODE LIKE '%"+roleGroupId+"%' OR (EXTENDGROUPCODE LIKE '%"+roleGroupId+"%' AND REJECTGROUPCODE NOT LIKE '%"+roleGroupId+"%'))");
		//处理添加的权限
		for(DynaBean permission:addPerms){
			String perId=permission.getStr("PERID");
			DynaBean permRole=serviceTemplate.selectOne("JE_CORE_ROLEGROUP_PERM"," and PERID='"+perId+"' and ROLEGROUPID='"+roleGroupId+"'");
			if(permRole==null){
				permRole=new DynaBean("JE_CORE_ROLEGROUP_PERM",false);
				permRole.set(BeanUtils.KEY_PK_CODE, "PERID");
				permRole.set("PERID", permission.getStr("PERID"));
				permRole.set("ROLEGROUPID", roleGroupId);
				permRole.set("TYPE", "SELF");
				permRole.set("ENABLED", "1");
				serviceTemplate.insert(permRole);
			}else{
				if("0".equals(permRole.getStr("ENABLED"))){
					permRole.set("ENABLED", "1");
					permRole.set("TYPE", PermExtendType.PERM_SELF);
					pcServiceTemplate.executeSql("UPDATE JE_CORE_ROLEGROUP_PERM SET TYPE='"+permRole.getStr("TYPE")+"',ENABLED='"+permRole.getStr("ENABLED")+"' where PERID='"+permRole.getStr("PERID")+"' and ROLEGROUPID='"+permRole.getStr("ROLEGROUPID")+"'");
				}
			}
			for(DynaBean role:roles){
				DynaBean rolePerm=serviceTemplate.selectOne("JE_CORE_ROLE_PERM"," AND ROLEID='"+role.getStr("ROLEID")+"' AND PERID='"+perId+"'");
				if(rolePerm==null){
					permRole=new DynaBean("JE_CORE_ROLE_PERM",false);
					permRole.set(BeanUtils.KEY_PK_CODE, "PERID");
					permRole.set("PERID", permission.getStr("PERID"));
					permRole.set("ROLEID", role.getStr("ROLEID"));
					permRole.set("TYPE", PermExtendType.PERM_GROUP);
					permRole.set("ENABLED", "1");
					serviceTemplate.insert(permRole);
				}else if(PermExtendType.PERM_GROUP.equals(rolePerm.getStr("TYPE")) && "0".equals(rolePerm.getStr("ENABLED"))){
					pcServiceTemplate.executeSql("UPDATE JE_CORE_ROLE_PERM SET TYPE='"+rolePerm.getStr("TYPE")+"',ENABLED='1' where PERID='"+rolePerm.getStr("PERID")+"' and ROLEID='"+rolePerm.getStr("ROLEID")+"'");
				}
			}
		}
		//处理删除的权限
		if(StringUtil.isNotEmpty(delPerm)){
			String delPermIds= StringUtil.buildArrayToString(delPerm.split(","));
			//这里后续想该一下。因为我个人不敢确定按钮  子功能  功能  菜单中的UUID可能重复 修改思路：在权限打对勾的时候把权限的id放入，删除权限的时候取权限的ID
			pcServiceTemplate.executeSql(" update JE_CORE_ROLEGROUP_PERM SET ENABLED='0',TYPE='"+PermExtendType.PERM_SELF+"' WHERE  ROLEGROUPID='"+roleGroupId+"' and PERID in (SELECT PERID FROM JE_CORE_PERMISSION WHERE PERMCODE IN ("+delPermIds+"))");
			for(DynaBean role:roles){
				//构建该角色所有的聚合权限组
				String groupCodes=getRoleGroupCodes(role, roleGroupId);
				String whereSql="";
				if(StringUtil.isNotEmpty(groupCodes)){
					whereSql=" AND PERID NOT IN (SELECT PERID FROM JE_CORE_ROLEGROUP_PERM WHERE ROLEGROUPID in ("+groupCodes+") AND ENABLED='1')";
				}
				//如果该角色聚合两个权限组，一个取消，则另一个还存在，所以应该排除他
				pcServiceTemplate.executeSql(" update JE_CORE_ROLE_PERM SET ENABLED='0' WHERE  ROLEID='"+role.getStr("ROLEID")+"' and PERID in (SELECT PERID FROM JE_CORE_PERMISSION WHERE PERMCODE IN ("+delPermIds+")) AND TYPE='"+PermExtendType.PERM_GROUP+"'" +whereSql);
			}
		}
	}
	private String getRoleGroupCodes(DynaBean role,String roleGroupId){
		Set<String> codes=new HashSet<String>();
		String groupCode=role.getStr("GROUPCODE");
		String extendGroupCode=role.getStr("EXTENDGROUPCODE");
		String rejectGroupCode=role.getStr("REJECTGROUPCODE");
		if(StringUtil.isNotEmpty(extendGroupCode)){
			for(String code:extendGroupCode.split(",")){
				codes.add(code);
			}
		}
		if(StringUtil.isNotEmpty(rejectGroupCode)){
			for(String code:rejectGroupCode.split(",")){
				codes.remove(code);
			}
		}
		if(StringUtil.isNotEmpty(groupCode)){
			for(String code:groupCode.split(",")){
				codes.add(code);
			}
		}
		codes.remove(roleGroupId);
		if(codes.size()>0){
			String codeStr="";
			for(String c:codes){
				codeStr+=("'"+c+"',");
			}
			return StringUtil.replaceSplit(codeStr);
		}else{
			return "";
		}
	}

	/**
	 * 构建角色授权树
	 * @param rootId 根节点id
	 * @return
	 */
	@Override
	public RolePermNode buildRolePermTree(String rootId) {
		// TODO Auto-generated method stub
		List<RolePermNode> children = new ArrayList<RolePermNode>();
		String sql=DBSqlUtils.getPcDBMethodManager().getRolePermLikeSql(rootId);
		List<?> treeItems = pcServiceTemplate.queryBySql(sql.toString());
		if(null != treeItems && 0 != treeItems.size()) {
			for(int i=0; i<treeItems.size(); i++) {
				Object[] record = (Object[])treeItems.get(i);
				RolePermNode node=new RolePermNode();
				node.setId((String)record[0]);
				node.setText((String)record[1]);
				node.setCode((String)record[2]);
				node.setGroupName(StringUtil.getDefaultValue(record[3], ""));
				node.setGroupCode(StringUtil.getDefaultValue(record[4], ""));
				node.setExtendGroupName(StringUtil.getDefaultValue(record[5], ""));
				node.setExtendGroupCode(StringUtil.getDefaultValue(record[6], ""));
				node.setRejectGroupName(StringUtil.getDefaultValue(record[7], ""));
				node.setRejectGroupCode(StringUtil.getDefaultValue(record[8], ""));
				node.setPath((String)record[9]);
				node.setParent((String)record[10]);
				children.add(node);
			}
		}
		RolePermNode rootNode=buildRoleTreeNode(children, rootId);
		return rootNode;
	}
	private RolePermNode buildRoleTreeNode(List<RolePermNode> lists,String rootId){
		RolePermNode root = new RolePermNode();
		for(RolePermNode node:lists) { //当前循环这个集合每一个元素
			if(node.getParent()==null || node.getParent().equals("") || node.getId().equals(rootId)){
				root=node;
				lists.remove(node);
				break;
			}
		}
		createRoleTreeChildren(lists, root);
		return root;
	}
	/**
	 * 递归方法
	 * @param childrens
	 * @param root
	 */
	public void createRoleTreeChildren(List<RolePermNode> childrens,RolePermNode root){
		String parentId=root.getId();
		for(int i=0;i<childrens.size();i++){
			RolePermNode node=childrens.get(i);
			if(node.getParent()!=null){
				if(node.getParent().equalsIgnoreCase(parentId)){
					root.getChildren().add(node);
					//当前不能删除节点，因为孩子引用与它， 递归回来，坐标失效
					createRoleTreeChildren(childrens, node);
				}
			}
			if(i==childrens.size()-1){
				return;
			}
		}
	}

	/**
	 * 构建权限组授权树
	 * @param rootId 根节点id
	 * @return
	 */
	@Override
	public RoleGroupPermNode buildRoleGroupTree(String rootId) {
		// TODO Auto-generated method stub
		List<RoleGroupPermNode> children = new ArrayList<RoleGroupPermNode>();
		String sql=DBSqlUtils.getPcDBMethodManager().getRoleGroupPermLikeSql(rootId);
		List<?> treeItems = pcServiceTemplate.queryBySql(sql.toString());
		if(null != treeItems && 0 != treeItems.size()) {
			for(int i=0; i<treeItems.size(); i++) {
				Object[] record = (Object[])treeItems.get(i);
				RoleGroupPermNode node=new RoleGroupPermNode();
				node.setId((String)record[0]);
				node.setText((String)record[1]);
				node.setCode((String)record[2]);
				node.setPath((String)record[3]);
				node.setParent((String)record[4]);
				children.add(node);
			}
		}
		RoleGroupPermNode rootNode=buildRoleGroupTreeNode(children, rootId);
		return rootNode;
	}
	private RoleGroupPermNode buildRoleGroupTreeNode(List<RoleGroupPermNode> lists,String rootId){
		RoleGroupPermNode root = new RoleGroupPermNode();
		for(RoleGroupPermNode node:lists) { //当前循环这个集合每一个元素
			if(node.getParent()==null || node.getParent().equals("") || node.getId().equals(rootId)){
				root=node;
				lists.remove(node);
				break;
			}
		}
		createRoleGroupTreeChildren(lists, root);
		return root;
	}
	/**
	 * 递归方法
	 * @param childrens
	 * @param root
	 */
	public void createRoleGroupTreeChildren(List<RoleGroupPermNode> childrens,RoleGroupPermNode root){
		String parentId=root.getId();
		for(int i=0;i<childrens.size();i++){
			RoleGroupPermNode node=childrens.get(i);
			if(node.getParent()!=null){
				if(node.getParent().equalsIgnoreCase(parentId)){
					root.getChildren().add(node);
					//当前不能删除节点，因为孩子引用与它， 递归回来，坐标失效
					createRoleGroupTreeChildren(childrens, node);
				}
			}
			if(i==childrens.size()-1){
				return;
			}
		}
	}

	/**
	 * 得到当前权限角色
	 * @return
	 */
	@Override
	public List<DynaBean> getCurrentPermRoles() {
		List<DynaBean> targers=new ArrayList<DynaBean>();
		EndUser currentUser=SecurityUserHolder.getCurrentUser();
		for(Role role:currentUser.getRoles()){
			DynaBean targer=new DynaBean("JE_CORE_ROLE",false);
			targer.set(BeanUtils.KEY_PK_CODE, "ROLEID");
			targer.set("ROLEID", role.getRoleId());
			targer.set("ROLENAME", role.getRoleName());
			targer.set("ROLECODE", role.getRoleCode());
			targer.set("ROLETYPE", role.getRoleType());
			targer.set("BASEROLEID", role.getBaseRoleId());
			targer.set("JTGSID", role.getJtgsId());
			//已经将权限组的权限加入到了角色权限中
//			//权限组的ids
//			String groupCode=role.getGroupCode();
//			String extendGroupCode=role.getExtendGroupCode();
//			String rejectGroupCode=role.getRejectGroupCode();
//			Set<String> codes=new HashSet<String>();
//			if(StringUtil.isNotEmpty(extendGroupCode)){
//				for(String code:extendGroupCode.split(",")){
//					codes.add(code);
//				}
//			}
//			if(StringUtil.isNotEmpty(rejectGroupCode)){
//				for(String code:rejectGroupCode.split(",")){
//					codes.remove(code);
//				}
//			}
//			if(StringUtil.isNotEmpty(groupCode)){
//				for(String code:groupCode.split(",")){
//					codes.add(code);
//				}
//			}
//			StringBuffer gCodes=new StringBuffer();
//			for(String code:codes){
//				gCodes.append(","+code);
//			}
//			targer.set("GROUPCODE", StringUtil.replaceSplit(gCodes.toString()));
			targers.add(targer);
		}
		return targers;
	}

	/**
	 * 清空权限
	 * @param permType 类型
	 * @param ids 主键
	 * @param delMenu 是否清空按钮
	 */
	@Override
	public void clearPermData(String permType, String ids,Boolean delMenu) {
		// TODO Auto-generated method stub
		if(StringUtil.isNotEmpty(ids)){
			for(String permCode:ids.split(",")){
				if(PermType.MT.equals(permType)){
					String whereSql="and SY_PATH LIKE '%"+permCode+"%'";
					if(delMenu){
						whereSql="and JE_CORE_FUNCINFO_ID = '"+permCode+"'";
					}
					List<DynaBean> funcInfos=serviceTemplate.selectList("JE_CORE_FUNCINFO",whereSql);
					for(DynaBean funcInfo:funcInfos){
						pcServiceTemplate.executeSql("DELETE FROM JE_CORE_ROLE_PERM WHERE PERID IN (SELECT PERID FROM JE_CORE_PERMISSION WHERE PERMTYPE='"+PermType.BUTTON+"' and PERMCODE IN (SELECT JE_CORE_RESOURCEBUTTON_ID FROM JE_CORE_RESOURCEBUTTON WHERE RESOURCEBUTTON_FUNCINFO_ID='"+funcInfo.getStr("JE_CORE_FUNCINFO_ID")+"'))");
						pcServiceTemplate.executeSql("DELETE FROM JE_CORE_ROLEGROUP_PERM WHERE PERID IN (SELECT PERID FROM JE_CORE_PERMISSION WHERE PERMTYPE='"+PermType.BUTTON+"' and PERMCODE IN (SELECT JE_CORE_RESOURCEBUTTON_ID FROM JE_CORE_RESOURCEBUTTON WHERE RESOURCEBUTTON_FUNCINFO_ID='"+funcInfo.getStr("JE_CORE_FUNCINFO_ID")+"'))");
						pcServiceTemplate.executeSql("DELETE FROM JE_CORE_PERMISSION WHERE PERMTYPE='"+PermType.BUTTON+"' and PERMCODE IN (SELECT JE_CORE_RESOURCEBUTTON_ID FROM JE_CORE_RESOURCEBUTTON WHERE RESOURCEBUTTON_FUNCINFO_ID='"+funcInfo.getStr("JE_CORE_FUNCINFO_ID")+"')");
						pcServiceTemplate.executeSql("DELETE FROM JE_CORE_ROLEGROUP_PERM WHERE PERID IN (SELECT PERID FROM JE_CORE_PERMISSION WHERE PERMTYPE='"+PermType.SUB_FUNC+"' and PERMCODE IN (SELECT JE_CORE_FUNCRELATION_ID FROM JE_CORE_FUNCRELATION WHERE FUNCRELATION_FUNCINFO_ID='"+funcInfo.getStr("JE_CORE_FUNCINFO_ID")+"'))");
						pcServiceTemplate.executeSql("DELETE FROM JE_CORE_ROLE_PERM WHERE PERID IN (SELECT PERID FROM JE_CORE_PERMISSION WHERE PERMTYPE='"+PermType.SUB_FUNC+"' and PERMCODE IN (SELECT JE_CORE_FUNCRELATION_ID FROM JE_CORE_FUNCRELATION WHERE FUNCRELATION_FUNCINFO_ID='"+funcInfo.getStr("JE_CORE_FUNCINFO_ID")+"'))");
						pcServiceTemplate.executeSql("DELETE FROM JE_CORE_PERMISSION WHERE PERMTYPE='"+PermType.SUB_FUNC+"' and PERMCODE IN (SELECT JE_CORE_FUNCRELATION_ID FROM JE_CORE_FUNCRELATION WHERE FUNCRELATION_FUNCINFO_ID='"+funcInfo.getStr("JE_CORE_FUNCINFO_ID")+"')");
					}
				}else if(PermType.MENU.equals(permType) || PermType.PORTAL.equals(permType)){
					List<DynaBean> menus=serviceTemplate.selectList("JE_CORE_MENU"," and SY_PATH LIKE '%"+permCode+"%'");
					for(DynaBean menu:menus){
						String menuType=menu.getStr("MENU_NODEINFOTYPE");
						if(PermType.MT.equals(menuType)){
							if(StringUtil.isNotEmpty("MENU_NODEINFO")){
								DynaBean funcInfo=serviceTemplate.selectOne("JE_CORE_FUNCINFO","AND FUNCINFO_FUNCCODE='"+menu.getStr("MENU_NODEINFO")+"'");
								if(funcInfo!=null){
									clearPermData(PermType.MT, ids, true);
								}
							}
						}
						pcServiceTemplate.executeSql("DELETE FROM JE_CORE_ROLEGROUP_PERM WHERE PERID IN (SELECT PERID FROM JE_CORE_PERMISSION WHERE PERMTYPE='"+menuType+"' and PERMCODE='"+menu.getStr("JE_CORE_MENU_ID")+"')");
						pcServiceTemplate.executeSql("DELETE FROM JE_CORE_ROLE_PERM WHERE PERID IN (SELECT PERID FROM JE_CORE_PERMISSION WHERE PERMTYPE='"+menuType+"' and PERMCODE='"+menu.getStr("JE_CORE_MENU_ID")+"')");
						pcServiceTemplate.executeSql("DELETE FROM JE_CORE_PERMISSION WHERE PERMTYPE='"+menuType+"' and PERMCODE='"+menu.getStr("JE_CORE_MENU_ID")+"'");
					}
				}else{
					pcServiceTemplate.executeSql("DELETE FROM JE_CORE_ROLEGROUP_PERM WHERE PERID IN (SELECT PERID FROM JE_CORE_PERMISSION WHERE PERMTYPE='"+permType+"' and PERMCODE='"+permCode+"')");
					pcServiceTemplate.executeSql("DELETE FROM JE_CORE_ROLE_PERM WHERE PERID IN (SELECT PERID FROM JE_CORE_PERMISSION WHERE PERMTYPE='"+permType+"' and PERMCODE='"+permCode+"')");
					pcServiceTemplate.executeSql("DELETE FROM JE_CORE_PERMISSION WHERE PERMTYPE='"+permType+"' and PERMCODE='"+permCode+"'");
				}
			}
		}
	}

	/**
	 * 更新功能下权限的所属模块   在菜单拖动过程中需要更新该值
	 * @param funcInfo TODO 暂不明确
	 * @param mouldeId TODO 暂不明确
	 */
	@Override
	public void updatePermModule(DynaBean funcInfo, String mouldeId) {
		// TODO Auto-generated method stub
		pcServiceTemplate.executeSql("update JE_CORE_PERMISSION SET MODULE='"+mouldeId+"' WHERE PERMCODE in (select JE_CORE_RESOURCEBUTTON_ID FROM JE_CORE_RESOURCEBUTTON WHERE RESOURCEBUTTON_FUNCINFO_ID='"+funcInfo.getStr("JE_CORE_FUNCINFO_ID")+"') AND PERMTYPE='BUTTON'");
		pcServiceTemplate.executeSql("update JE_CORE_PERMISSION SET MODULE='"+mouldeId+"' WHERE PERMCODE in (select JE_CORE_FUNCRELATION_ID FROM JE_CORE_FUNCRELATION WHERE FUNCRELATION_FUNCINFO_ID='"+funcInfo.getStr("JE_CORE_FUNCINFO_ID")+"') AND PERMTYPE='SUB_FUNC'");
		List<DynaBean> childrens=serviceTemplate.selectList("JE_CORE_FUNCINFO", " AND JE_CORE_FUNCINFO_ID IN (SELECT FUNCRELATION_FUNCID FROM JE_CORE_FUNCRELATION WHERE FUNCRELATION_FUNCINFO_ID='"+funcInfo.getStr("JE_CORE_FUNCINFO_ID")+"')");
		for(DynaBean children:childrens){
			updatePermModule(children, mouldeId);
		}
	}

	/**
	 * 得到当前登录用户所有可操作功能的功能功能编码集合
	 * @return
	 */
	@Override
	public Set<String> getPermFunc() {
		// TODO Auto-generated method stub
		Set<String> funcCodes=new HashSet<String>();
		List<DynaBean> targers=getCurrentPermRoles();
		EndUser currentUser=SecurityUserHolder.getCurrentUser();
		String includeStr=currentUser.getExcludePerms();
		QueryInfo queryInfo=new QueryInfo();
		queryInfo.setWhereSql(" and SY_STATUS='1'");
		Set<String> includes=null;
		if(StringUtil.isNotEmpty(includeStr)){
			includes=new HashSet<String>();
			for(String resId:includeStr.split(",")){
				includes.add(resId+"#"+AdminPermType.RES_MENU);
			}
		}
		List<String> roleIds=new ArrayList<String>();
		for(DynaBean role:targers){
			roleIds.add(role.getStr("ROLEID"));
		}
		List<DynaBean> menus=serviceTemplate.selectList("JE_CORE_MENU", " AND JE_CORE_MENU_ID IN (SELECT PERMCODE FROM JE_CORE_PERMISSION WHERE PERMTYPE ='MT' AND PERID IN (SELECT PERID FROM JE_CORE_ROLE_PERM WHERE ENABLED='1' AND ROLEID IN ("+StringUtil.buildArrayToString(ArrayUtils.getArray(roleIds))+")))","JE_CORE_MENU_ID,MENU_NODEINFO");
		for(DynaBean menu:menus){
			if(includes!=null && !includes.contains(menu.getStr("JE_CORE_MENU_ID")+"#"+AdminPermType.RES_MENU)){
				continue;
			}
			funcCodes.add(menu.getStr("MENU_NODEINFO"));
		}
		List<DynaBean> subFuncs=serviceTemplate.selectList("JE_CORE_FUNCRELATION", "  AND JE_CORE_FUNCRELATION_ID IN (SELECT PERMCODE FROM JE_CORE_PERMISSION WHERE PERMTYPE ='SUB_FUNC' AND PERID IN (SELECT PERID FROM JE_CORE_ROLE_PERM WHERE ENABLED='1' AND ROLEID IN ("+StringUtil.buildArrayToString(ArrayUtils.getArray(roleIds))+")))","JE_CORE_FUNCRELATION_ID,FUNCRELATION_CODE");
		for(DynaBean func:subFuncs){
			funcCodes.add(func.getStr("FUNCRELATION_CODE"));
		}
		return funcCodes;
	}

	/**
	 * 授权按钮给开发人员
	 * @param btnId 按钮id
	 * @param funcId 功能id
	 * @param type 类型
	 * @param targer TODO 暂不明确
	 */
	@Override
	public void doDevelopFuncPerm(String btnId,String funcId,String type, DynaBean targer) {
		// TODO Auto-generated method stub
		//查询模块信息
		DynaBean funcInfo=serviceTemplate.selectOneByPk("JE_CORE_FUNCINFO", funcId);
		DynaBean menu=serviceTemplate.selectOne("JE_CORE_MENU"," AND MENU_NODEINFO='"+funcInfo.getStr("FUNCINFO_FUNCCODE")+"'");
		String module="";
		if(menu!=null){
			String path=menu.getStr("SY_PATH");
			module=menu.getStr("JE_CORE_MENU_ID");
			if(!NodeType.ROOT.equals(menu.getStr("SY_PARENT"))){
				module=path.substring(6, path.indexOf("/", 7));
			}

		}
		if("BUTTON".equals(type)){
			String id=module+"#"+PermType.BUTTON+"#"+btnId;
			doUpdatePermData(targer, AuthorPermType.PERM_ROLE, "", id, false);
		}else{
			List<String> ids=new ArrayList<String>();
			DynaBean subFunc=serviceTemplate.selectOneByPk("JE_CORE_FUNCRELATION",btnId);
			ids.add(module+"#"+PermType.SUB_FUNC+"#"+subFunc.getStr("JE_CORE_FUNCRELATION_ID"));
			buildFuncPermIds(subFunc.getStr("FUNCRELATION_CODE"), module, ids);
			doUpdatePermData(targer, AuthorPermType.PERM_ROLE, "", StringUtil.buildSplitString(ArrayUtils.getArray(ids), ","), false);
		}
	}

	/**
	 * 授权给开发人员
	 * @param menuId 菜单id
	 * @param targer TODO 暂不明确
	 */
	@Override
	public void doDevelopPerm(String menuId,DynaBean targer){
		DynaBean menu=serviceTemplate.selectOneByPk("JE_CORE_MENU", menuId);
		String path=menu.getStr("SY_PATH","");
		String module=menuId;
		if(!NodeType.ROOT.equals(menu.getStr("SY_PARENT"))){
			module=path.substring(6, path.indexOf("/", 7));
		}
		if(PermType.MT.equals(menu.getStr("MENU_NODEINFOTYPE"))){
			List<String> ids=new ArrayList<String>();
			ids.add(module+"#"+menu.getStr("MENU_NODEINFOTYPE")+"#"+menuId);
			buildFuncPermIds(menu.getStr("MENU_NODEINFO"), module, ids);
			doUpdatePermData(targer, AuthorPermType.PERM_ROLE, "", StringUtil.buildSplitString(ArrayUtils.getArray(ids), ","), false);
		}else{
			String id=module+"#"+menu.getStr("MENU_NODEINFOTYPE")+"#"+menuId;
			doUpdatePermData(targer, AuthorPermType.PERM_ROLE, "", id, false);
		}
	}

	private void buildFuncPermIds(String funcCode,String module,List<String> ids){
		DynaBean funcInfo=serviceTemplate.selectOne("JE_CORE_FUNCINFO", " AND FUNCINFO_FUNCCODE='"+funcCode+"'");
		if(funcInfo==null){
			System.out.println("功能【"+funcCode+"】未找到，程序将继续运行!");
		}else{
			List<DynaBean> btns=serviceTemplate.selectList("JE_CORE_RESOURCEBUTTON", " AND RESOURCEBUTTON_FUNCINFO_ID='"+funcInfo.getStr("JE_CORE_FUNCINFO_ID")+"'");
			for(DynaBean btn:btns){
				ids.add(module+"#"+PermType.BUTTON+"#"+btn.getStr("JE_CORE_RESOURCEBUTTON_ID"));
			}
			List<DynaBean> subFuncs=serviceTemplate.selectList("JE_CORE_FUNCRELATION", " AND FUNCRELATION_FUNCINFO_ID='"+funcInfo.getStr("JE_CORE_FUNCINFO_ID")+"'","JE_CORE_FUNCRELATION_ID,FUNCRELATION_CODE");
			for(DynaBean func:subFuncs){
				ids.add(module+"#"+PermType.SUB_FUNC+"#"+func.getStr("JE_CORE_FUNCRELATION_ID"));
				buildFuncPermIds(func.getStr("FUNCRELATION_CODE"), module, ids);
			}
		}
	}

	/**
	 * 获取用户申请权限
	 * @param dicInfoVo TODO 暂不明确
	 * @return
	 */
	@Override
	public JSONTreeNode getRoleDiyPerm(DicInfoVo dicInfoVo) {
		// TODO Auto-generated method stub
		Map<String,String> params=dicInfoVo.getParams();
		String roleIds=params.get("roleIds");
		List<DynaBean> permDiys=serviceTemplate.selectList("JE_SYS_ROLEPERMDIY", " AND ROLEID IN ("+StringUtil.buildArrayToString(roleIds.split(","))+")");
		Set<String> includIds=new HashSet<String>();
		List<DynaBean> perms=serviceTemplate.selectList("JE_CORE_PERMISSION"," AND PERID IN (SELECT PERID FROM JE_CORE_ROLE_PERM WHERE ROLEID IN ("+StringUtil.buildArrayToString(roleIds.split(","))+") AND ENABLED='1')");
		for(DynaBean perm:perms){
			includIds.add(perm.getStr("PERMCODE"));
		}
		for(DynaBean permDiy:permDiys){
			includIds.add(permDiy.getStr("PERMID"));
		}
		JSONTreeNode rootNode=buildPermTree("ROOT", new ArrayList<DynaBean>(), AuthorPermType.PERM_ROLE, true, true, true,false, new QueryInfo(),null,includIds,"",false,true);//是否加入该产品过滤
		return rootNode;
	}
	@Resource(name="wfServiceTemplate")
	public void setWfServiceTemplate(WfServiceTemplate wfServiceTemplate) {
		this.wfServiceTemplate = wfServiceTemplate;
	}
	@Resource(name="PCDynaServiceTemplate")
	public void setServiceTemplate(PCDynaServiceTemplate serviceTemplate) {
		this.serviceTemplate = serviceTemplate;
	}
	@Resource(name="PCServiceTemplateImpl")
	public void setPcServiceTemplate(PCServiceTemplate pcServiceTemplate) {
		this.pcServiceTemplate = pcServiceTemplate;
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
	@Resource(name="jbpmTaskService")
	public void setTaskService(TaskService taskService) {
		this.taskService = taskService;
	}
	@Resource(name="wfInfoManager")
	public void setWfInfoManager(WfInfoManager wfInfoManager) {
		this.wfInfoManager = wfInfoManager;
	}
	@Resource(name="userManager")
	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}
}