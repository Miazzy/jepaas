package com.je.rbac.node;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.je.core.constants.rbac.PermExtendType;
import com.je.core.service.PCDynaServiceTemplate;
import com.je.core.service.PCServiceTemplate;
import com.je.core.service.PCServiceTemplateImpl;
import com.je.core.util.SpringContextHolder;
import com.je.core.util.StringUtil;
import com.je.core.util.bean.BeanUtils;
import com.je.core.util.bean.DynaBean;

public class RolePermNode {
	private String id;
	private String text;
	private String code;
	private String groupName;
	private String groupCode;
	private String extendGroupName;
	private String extendGroupCode;
	private String rejectGroupName;
	private String rejectGroupCode;
	private String path;
	private String parent;
	private List<RolePermNode> children=new ArrayList<RolePermNode>();
	/**
	 * 更新聚合
	 */
	public void updateGroup(String parentGroupCode,String parentGroupName,String type,Boolean isHard){
		PCServiceTemplate pcServiceTemplate=SpringContextHolder.getBean("PCServiceTemplateImpl");
		if(PermExtendType.PERM_SELF.equals(type)){
			String[] groupCodes=groupCode.split(",");
			String[] groupNames=groupName.split(",");
			for(int i=0;i<groupCodes.length;i++){
				if(this.extendGroupCode.indexOf(groupCodes[i])>=0){
					//如果该权限组被继承
					if(this.rejectGroupCode.indexOf(groupCodes[i])==-1){
						this.rejectGroupCode+=","+groupCodes[i];
						this.rejectGroupName+=","+groupNames[i];
					}
				}
			}
		}else{
			String[] parentGroupCodes=parentGroupCode.split(",");
			String[] parentGroupNames=parentGroupName.split(",");
			this.rejectGroupCode="";
			this.rejectGroupName="";
			this.extendGroupCode="";
			this.extendGroupName="";
			for(int i=0;i<parentGroupCodes.length;i++){
				if(groupCode.indexOf(parentGroupCodes[i])>=0){
					if(this.rejectGroupCode.indexOf(parentGroupCodes[i])==-1){
						this.rejectGroupCode+=","+parentGroupCodes[i];
						this.rejectGroupName+=","+parentGroupNames[i];
					}
				}
				if(extendGroupCode.indexOf(parentGroupCodes[i])==-1){
					this.extendGroupCode+=","+parentGroupCodes[i];
					this.extendGroupName+=","+parentGroupNames[i];
				}
			}
		}
		String[] rejectGroupCodes=rejectGroupCode.split(",");
		String[] rejectGroupNames=rejectGroupName.split(",");
		for(int i=0;i<rejectGroupCodes.length;i++){
			if(this.extendGroupCode.indexOf(rejectGroupCodes[i])==-1 && this.groupCode.indexOf(rejectGroupCodes[i])==-1){
				rejectGroupCode=rejectGroupCode.replace(rejectGroupCodes[i], "");
				rejectGroupName=rejectGroupName.replace(rejectGroupNames[i], "");
			}
		}
//		if(isHard){//如果是硬性保存
//			String[] extendGroupCodes=extendGroupCode.split(",");
//			String[] extendGroupNames=extendGroupName.split(",");
//			for(int i=0;i<extendGroupCodes.length;i++){
//				if(groupCode.indexOf(extendGroupCodes[i])>=0){
//					groupCode=groupCode.replace(extendGroupCodes[i], "");
//					groupName=groupName.replace(extendGroupNames[i], "");
//				}
//				if(rejectGroupCode.indexOf(extendGroupCodes[i])>=0){
//					rejectGroupCode=rejectGroupCode.replace(extendGroupCodes[i], "");
//					rejectGroupName=rejectGroupName.replace(extendGroupNames[i], "");
//				}
//			}
//		}
		this.groupCode=StringUtil.replaceSplit(groupCode);
		this.groupName=StringUtil.replaceSplit(groupName);
		this.rejectGroupCode=StringUtil.replaceSplit(rejectGroupCode);
		this.rejectGroupName=StringUtil.replaceSplit(rejectGroupName);
		this.extendGroupCode=StringUtil.replaceSplit(extendGroupCode);
		this.extendGroupName=StringUtil.replaceSplit(extendGroupName);
		//构建有效的权限组id
		Set<String> roleGroupIds=new HashSet<String>();
		for(String gc:this.groupCode.split(",")){
			roleGroupIds.add(gc);
		}
		for(String egc:this.extendGroupCode.split(",")){
			if(this.rejectGroupCode.indexOf(egc)<0){
				roleGroupIds.add(egc);
			}
		}
		pcServiceTemplate.executeSql("UPDATE JE_CORE_ROLE SET GROUPCODE='"+this.groupCode+"',GROUPNAME='"+this.groupName+"',EXTENDGROUPCODE='"+this.extendGroupCode+"',EXTENDGROUPNAME='"+this.extendGroupName+"',REJECTGROUPCODE='"+this.rejectGroupCode+"',REJECTGROUPNAME='"+this.rejectGroupName+"' where ROLEID='"+this.id+"'");
		//删除该角色下聚合的所有权限
		pcServiceTemplate.executeSql("DELETE FROM JE_CORE_ROLE_PERM WHERE TYPE='"+PermExtendType.PERM_GROUP+"' AND ROLEID='"+this.id+"'");
		for(String roleGroupId:roleGroupIds){
			if(!StringUtil.isNotEmpty(roleGroupId)){
				continue;
			}
			//直接为角色添加聚合权限，， 排除如果角色表里有权限则代表是 继承或者自身， 排除已有的权限， 这样也防止违反复合主键的约束
			pcServiceTemplate.executeSql(" INSERT INTO JE_CORE_ROLE_PERM(ROLEID,PERID,TYPE,ENABLED) SELECT '"+this.id+"',PERID,'"+PermExtendType.PERM_GROUP+"','1' FROM JE_CORE_ROLEGROUP_PERM WHERE ROLEGROUPID='"+roleGroupId+"' AND ENABLED='1' AND PERID NOT IN (SELECT PERID FROM JE_CORE_ROLEGROUP_PERM WHERE ROLEGROUPID='"+roleGroupId+"' AND ENABLED='1' AND PERID IN(SELECT PERID FROM JE_CORE_ROLE_PERM WHERE ROLEID='"+this.id+"'))");
		}
		for(RolePermNode child:children){
			if(PermExtendType.PERM_SELF.equalsIgnoreCase(type)){
				type=PermExtendType.PERM_EXTEND;
			}
			child.updateGroup(groupCode+","+extendGroupCode, groupName+","+extendGroupName, type, false);
		}
	}
	/**
	 * 为角色授权
	 * @param delPermIds
	 * @param addPermIds
	 */
	public void updatePerm(String delPermIds,List<DynaBean> addPerms,Boolean isHard,String type){
		PCDynaServiceTemplate serviceTemplate=SpringContextHolder.getBean("PCDynaServiceTemplate");
		PCServiceTemplate pcServiceTemplate=SpringContextHolder.getBean("PCServiceTemplateImpl");
		//1.找到权限对象
		for(DynaBean permission:addPerms){
				String perId=permission.getStr("PERID");
				DynaBean permRole=serviceTemplate.selectOne("JE_CORE_ROLE_PERM"," and PERID='"+perId+"' and ROLEID='"+this.id+"'");
				//查询父角色是否有孩子
				Long count = 0L;
				if(isHard && !PermExtendType.PERM_EXTEND.equalsIgnoreCase(type)){
					count=serviceTemplate.selectCount("JE_CORE_ROLE_PERM", " AND ROLEID='"+this.parent+"' AND ENABLED='1' AND PERID='"+permission.getStr("PERID")+"'");
				}
				if(permRole==null){
					permRole=new DynaBean("JE_CORE_ROLE_PERM",false);
					permRole.set(BeanUtils.KEY_PK_CODE, "PERID");
					permRole.set("PERID", permission.getStr("PERID"));
					permRole.set("ROLEID", this.id);
					permRole.set("TYPE", type);
					permRole.set("ENABLED", "1");
					if(isHard && count>0 && !PermExtendType.PERM_EXTEND.equalsIgnoreCase(type)){
						permRole.set("TYPE", PermExtendType.PERM_EXTEND);
					}
					serviceTemplate.insert(permRole);
				}else{
					Boolean flag=true;
					if(PermExtendType.PERM_EXTEND.equalsIgnoreCase(type)){
//						if(isHard){
//							permRole.set("ENABLED", "1");
//							permRole.set("TYPE", type);
//						}else 
						if(PermExtendType.PERM_EXTEND.equalsIgnoreCase(permRole.getStr("TYPE")) && "0".equals(permRole.getStr("ENABLED"))){
							permRole.set("ENABLED", "1");
						//如果是聚合则直接被强化成继承或者自身的权限
						}else if(PermExtendType.PERM_GROUP.equalsIgnoreCase(permRole.getStr("TYPE"))){
							permRole.set("TYPE", type);
							permRole.set("ENABLED", "1");
						}else{
							flag=false;
						}
					}else{
						if(isHard && count>0){
							permRole.set("ENABLED", "1");
							permRole.set("TYPE", PermExtendType.PERM_EXTEND);
						}else{
							permRole.set("ENABLED", "1");
							permRole.set("TYPE", PermExtendType.PERM_SELF);
						}
					}
					if(flag){
						pcServiceTemplate.executeSql("UPDATE JE_CORE_ROLE_PERM SET TYPE='"+permRole.getStr("TYPE")+"',ENABLED='"+permRole.getStr("ENABLED")+"' where PERID='"+permRole.getStr("PERID")+"' and ROLEID='"+permRole.getStr("ROLEID")+"'");
					}
				}
		}
		//4.删除指定权限
		if(StringUtil.isNotEmpty(delPermIds)){
			String delPerm= StringUtil.buildArrayToString(delPermIds.split(","));
			if(PermExtendType.PERM_EXTEND.equalsIgnoreCase(type)){
				pcServiceTemplate.executeSql(" update JE_CORE_ROLE_PERM SET ENABLED='0',TYPE='"+type+"' WHERE 1=1 AND (TYPE='"+type+"' OR TYPE='"+PermExtendType.PERM_GROUP+"') and ROLEID='"+this.id+"' and PERID in (SELECT PERID FROM JE_CORE_PERMISSION WHERE PERMCODE IN ("+delPerm+"))");
			}else{
				pcServiceTemplate.executeSql(" update JE_CORE_ROLE_PERM SET ENABLED='0',TYPE='"+PermExtendType.PERM_SELF+"' WHERE ROLEID='"+this.id+"' and PERID in (SELECT PERID FROM JE_CORE_PERMISSION WHERE PERMCODE IN ("+delPerm+"))");
				if(isHard){
					pcServiceTemplate.executeSql(" update JE_CORE_ROLE_PERM SET TYPE='"+PermExtendType.PERM_EXTEND+"' WHERE ROLEID='"+this.id+"' " + //and PERID in (SELECT PERID FROM JE_CORE_PERMISSION WHERE PERMCODE IN ("+delPerm+")) 
							" AND PERID IN (SELECT PERID FROM JE_CORE_ROLE_PERM WHERE ROLEID='"+this.parent+"' AND ENABLED='0' AND PERID IN (SELECT PERID FROM JE_CORE_PERMISSION WHERE PERMCODE IN ("+delPerm+")))");
				}
			}
		}
		for(RolePermNode child:children){
			if(PermExtendType.PERM_SELF.equalsIgnoreCase(type)){
				type=PermExtendType.PERM_EXTEND;
			}
			child.updatePerm(delPermIds, addPerms, false, type);
		}
	}
	public void deleteExtendPerms(String delPermIds){
		PCServiceTemplate pcServiceTemplate=SpringContextHolder.getBean("PCServiceTemplateImpl");
		pcServiceTemplate.executeSql("DELETE FROM JE_CORE_ROLE_PERM WHERE ROLEID='"+this.id+"' AND PERID IN ("+delPermIds+") AND TYPE='EXTEND'");
		for(RolePermNode child:children){
			child.deleteExtendPerms(delPermIds);
		}
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public String getGroupCode() {
		return groupCode;
	}
	public void setGroupCode(String groupCode) {
		this.groupCode = groupCode;
	}
	public String getExtendGroupName() {
		return extendGroupName;
	}
	public void setExtendGroupName(String extendGroupName) {
		this.extendGroupName = extendGroupName;
	}
	public String getExtendGroupCode() {
		return extendGroupCode;
	}
	public void setExtendGroupCode(String extendGroupCode) {
		this.extendGroupCode = extendGroupCode;
	}
	public String getRejectGroupName() {
		return rejectGroupName;
	}
	public void setRejectGroupName(String rejectGroupName) {
		this.rejectGroupName = rejectGroupName;
	}
	public String getRejectGroupCode() {
		return rejectGroupCode;
	}
	public void setRejectGroupCode(String rejectGroupCode) {
		this.rejectGroupCode = rejectGroupCode;
	}
	public String getParent() {
		return parent;
	}
	public void setParent(String parent) {
		this.parent = parent;
	}
	public List<RolePermNode> getChildren() {
		return children;
	}
	public void setChildren(List<RolePermNode> children) {
		this.children = children;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	
}
