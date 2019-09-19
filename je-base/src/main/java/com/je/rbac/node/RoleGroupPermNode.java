package com.je.rbac.node;

import java.util.ArrayList;
import java.util.List;

import com.je.core.constants.rbac.PermExtendType;
import com.je.core.service.PCDynaServiceTemplate;
import com.je.core.service.PCServiceTemplate;
import com.je.core.util.SpringContextHolder;
import com.je.core.util.StringUtil;
import com.je.core.util.bean.BeanUtils;
import com.je.core.util.bean.DynaBean;

public class RoleGroupPermNode {
	private String id;
	private String text;
	private String code;
	private String parent;
	private String path;
	private List<RoleGroupPermNode> children=new ArrayList<RoleGroupPermNode>();
	public void updatePerm(String delPermIds,List<DynaBean> addPerms,Boolean isHard,String type){
		PCDynaServiceTemplate serviceTemplate=SpringContextHolder.getBean("PCDynaServiceTemplate");
		PCServiceTemplate pcServiceTemplate=SpringContextHolder.getBean("PCServiceTemplateImpl");
		//1.找到权限对象
		for(DynaBean permission:addPerms){
				String perId=permission.getStr("PERID");
				DynaBean permRole=serviceTemplate.selectOne("JE_CORE_ROLEGROUP_PERM"," and PERID='"+perId+"' and ROLEGROUPID='"+this.id+"'");
				if(permRole==null){
					permRole=new DynaBean("JE_CORE_ROLEGROUP_PERM",false);
					permRole.set(BeanUtils.KEY_PK_CODE, "PERID");
					permRole.set("PERID", permission.getStr("PERID"));
					permRole.set("ROLEGROUPID", this.id);
					permRole.set("TYPE", type);
					permRole.set("ENABLED", "1");
					serviceTemplate.insert(permRole);
				}else{
					Boolean flag=true;
					if(PermExtendType.PERM_EXTEND.equalsIgnoreCase(type)){
						if(isHard){
							permRole.set("ENABLED", "1");
							permRole.set("TYPE", type);
						}else if(PermExtendType.PERM_EXTEND.equalsIgnoreCase(permRole.getStr("TYPE")) && "0".equals(permRole.getStr("ENABLED"))){
							permRole.set("ENABLED", "1");
						}else{
							flag=false;
						}
					}else{
						permRole.set("ENABLED", "1");
						permRole.set("TYPE", PermExtendType.PERM_SELF);
					}
					if(flag){
						pcServiceTemplate.executeSql("UPDATE JE_CORE_ROLEGROUP_PERM SET TYPE='"+permRole.getStr("TYPE")+"',ENABLED='"+permRole.getStr("ENABLED")+"' where PERID='"+permRole.getStr("PERID")+"' and ROLEGROUPID='"+permRole.getStr("ROLEGROUPID")+"'");
					}
				}
		}
		//4.删除指定权限
		if(StringUtil.isNotEmpty(delPermIds)){
			String delPerm= StringUtil.buildArrayToString(delPermIds.split(","));
			if(PermExtendType.PERM_EXTEND.equalsIgnoreCase(type)){
				pcServiceTemplate.executeSql(" update JE_CORE_ROLEGROUP_PERM SET ENABLED='0' WHERE TYPE='"+type+"' and ROLEGROUPID='"+this.id+"' and PERID in (SELECT PERID FROM JE_CORE_PERMISSION WHERE PERMCODE IN ("+delPerm+"))");
				if(isHard){
					pcServiceTemplate.executeSql(" update JE_CORE_ROLEGROUP_PERM SET ENABLED='0',TYPE='"+type+"' WHERE TYPE='"+PermExtendType.PERM_SELF+"'  ROLEGROUPID='"+this.id+"' and PERID in (SELECT PERID FROM JE_CORE_PERMISSION WHERE PERMCODE IN ("+delPerm+"))");
				}
			}else{
				pcServiceTemplate.executeSql(" update JE_CORE_ROLEGROUP_PERM SET ENABLED='0',TYPE='"+PermExtendType.PERM_SELF+"' WHERE  ROLEGROUPID='"+this.id+"' and PERID in (SELECT PERID FROM JE_CORE_PERMISSION WHERE PERMCODE IN ("+delPerm+"))");
			}
		}
		for(RoleGroupPermNode child:children){
			if(PermExtendType.PERM_SELF.equalsIgnoreCase(type)){
				type=PermExtendType.PERM_EXTEND;
			}
			child.updatePerm(delPermIds, addPerms, isHard, type);
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
	public String getParent() {
		return parent;
	}
	public void setParent(String parent) {
		this.parent = parent;
	}
	public List<RoleGroupPermNode> getChildren() {
		return children;
	}
	public void setChildren(List<RoleGroupPermNode> children) {
		this.children = children;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	
}
