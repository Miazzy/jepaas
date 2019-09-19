package com.je.rbac.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.je.core.ann.entity.TreeNode;
import com.je.core.constants.ConstantVars;
import com.je.core.constants.tree.TreeNodeType;
import com.je.core.entity.TreeBaseEntity;
import org.jbpm.api.identity.Group;

/**
 * 角色管理
 * 在实体结构上,角色对人员是N:1但是角色对部门没有表现关系
 * 我们可以在实际操作选人员界面的时候,可以用到部门的数来来跟好的为业务服务
 * @author 研发部:云凤程
 * 2012-1-18 下午02:46:55
 */
//@Entity
//@GenericGenerator(name="systemUUID",strategy="uuid")
//@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
//@Table(name="JE_CORE_ROLE")
public class Role extends TreeBaseEntity implements Group,Serializable {
	
	private static final long serialVersionUID = 1984796136964287842L;
	public Role() {
		super();
	}
	//父节点
//	@TreeNode(type=TreeNodeType.PARENT)
	private Role parent;
	//孩子节点
	private Set<Role> children =  new HashSet<Role>();	
	//角色项编码
//	@TreeNode(type=TreeNodeType.CODE)
	private String roleCode ;
	//主键
//	@TreeNode(type=TreeNodeType.ID)
	private String roleId;
	//角色名称
//	@TreeNode(type=TreeNodeType.TEXT)
	private String roleName;
//	@TreeNode(type=TreeNodeType.ICON)
	private String icon; //图标
//	@TreeNode(type=TreeNodeType.ICONCLS)
	private String iconCls;
	//是否是超级管理员
	private String isSuperAdmin = "0";
	private String groupCode;
	private String groupName;
	private String extendGroupCode;
	private String extendGroupName;
	private String rejectGroupCode;
	private String rejectGroupName;
	private String deptId;
	private String sentryId;
	private String roleType;
	private String manager;
	private String jtgsId;
	private String jtgsMc;
	private String develop;
	//角色级别       基础角色SYS    产品角色CP   用户角色YH
	private String roleRank;
	//产品名称
	private String cpName;
	//产品ID
	private String cpId;
	//所属基础角色
	private String baseRoleName;
	//所属基础角色ID
	private String baseRoleId;
	//saas权
	private String saas;
	//角色中的成员
	private Set<EndUser> users = new HashSet<EndUser>();
	//角色拥有的权限
	private Set<Permission> permissions = new HashSet<Permission>();
	@JsonIgnore
//	@ManyToMany(mappedBy="roles",fetch=FetchType.LAZY,cascade={CascadeType.MERGE})
//	@JoinTable(name = "JE_CORE_ROLE_PERM", joinColumns = {
//			@JoinColumn(name = "roleId") },
//			inverseJoinColumns = { @JoinColumn(name = "perId")
//	})
//	@LazyCollection(LazyCollectionOption.TRUE)
	public Set<Permission> getPermissions() {
		return permissions;
	}
	public void setPermissions(Set<Permission> permissions) {
		this.permissions = permissions;
	}
	@JsonIgnore
//	@ManyToMany(fetch=FetchType.LAZY,cascade={CascadeType.MERGE})
//	@JoinTable(name = "JE_CORE_ROLE_USER", joinColumns = {
//			@JoinColumn(name = "roleId") },
//			inverseJoinColumns = { @JoinColumn(name = "userId")
//	})
//	@LazyCollection(LazyCollectionOption.TRUE)
	public Set<EndUser> getUsers() {
		return users;
	}
	public void setUsers(Set<EndUser> users) {
		this.users = users;
	}
	@JsonIgnore
//	@ManyToOne(optional=false, fetch=FetchType.LAZY)//(cascade={CascadeType.REFRESH})
//	@JoinColumn(name="PARENT")
//	@Override
	public Role getParent() {
		return parent;
	}
	@JsonIgnore
//	@OneToMany(mappedBy="parent",cascade={CascadeType.ALL})
//	@LazyCollection(LazyCollectionOption.TRUE)
	public Set<Role> getChildren() {
		return children;
	}
	public void setChildren(Set<Role> children) {
		this.children = children;
	}
	public void setParent(Role parent) {
		this.parent = parent;
	}
	public String getRoleCode() {
		return roleCode;
	}
	public void setRoleCode(String roleCode) {
		this.roleCode = roleCode;
	}
	public String getIsSuperAdmin() {
		return isSuperAdmin;
	}
	public void setIsSuperAdmin(String isSuperAdmin) {
		this.isSuperAdmin = isSuperAdmin;
	}
//	@Id
//	@GeneratedValue(generator="systemUUID")
//	@Column(length=50)
	public String getRoleId() {
		return roleId;
	}
	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	/**
	 * jBPM使用的获取roleId方法
	 */
//	@Transient
	public String getId() {
		return this.roleCode;
	}
	/**
	 * jBPM使用的获取roleName方法
	 */
//	@Transient
	public String getName() {
		return this.roleName;
	}
	
	public String getIconCls() {
		return iconCls;
	}
	public void setIconCls(String iconCls) {
		this.iconCls = iconCls;
	}
	/**
	 * jBPM使用的获取角色类型的方法
	 */
//	@Transient
	public String getType() {
		return ConstantVars.BLANK_STR;
	}
	public String getGroupCode() {
		return groupCode;
	}
	public void setGroupCode(String groupCode) {
		this.groupCode = groupCode;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public String getExtendGroupCode() {
		return extendGroupCode;
	}
	public void setExtendGroupCode(String extendGroupCode) {
		this.extendGroupCode = extendGroupCode;
	}
	public String getExtendGroupName() {
		return extendGroupName;
	}
	public void setExtendGroupName(String extendGroupName) {
		this.extendGroupName = extendGroupName;
	}
	public String getRejectGroupCode() {
		return rejectGroupCode;
	}
	public void setRejectGroupCode(String rejectGroupCode) {
		this.rejectGroupCode = rejectGroupCode;
	}
	public String getRejectGroupName() {
		return rejectGroupName;
	}
	public void setRejectGroupName(String rejectGroupName) {
		this.rejectGroupName = rejectGroupName;
	}
	public String getDeptId() {
		return deptId;
	}
	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}
	public String getSentryId() {
		return sentryId;
	}
	public void setSentryId(String sentryId) {
		this.sentryId = sentryId;
	}
	public String getRoleType() {
		return roleType;
	}
	public void setRoleType(String roleType) {
		this.roleType = roleType;
	}
	public String getManager() {
		return manager;
	}
	public void setManager(String manager) {
		this.manager = manager;
	}
	public String getDevelop() {
		return develop;
	}
	public void setDevelop(String develop) {
		this.develop = develop;
	}
	public String getJtgsId() {
		return jtgsId;
	}
	public void setJtgsId(String jtgsId) {
		this.jtgsId = jtgsId;
	}
	public String getJtgsMc() {
		return jtgsMc;
	}
	public void setJtgsMc(String jtgsMc) {
		this.jtgsMc = jtgsMc;
	}
	public String getRoleRank() {
		return roleRank;
	}
	public void setRoleRank(String roleRank) {
		this.roleRank = roleRank;
	}
	public String getCpName() {
		return cpName;
	}
	public void setCpName(String cpName) {
		this.cpName = cpName;
	}
	public String getCpId() {
		return cpId;
	}
	public void setCpId(String cpId) {
		this.cpId = cpId;
	}
	public String getBaseRoleName() {
		return baseRoleName;
	}
	public void setBaseRoleName(String baseRoleName) {
		this.baseRoleName = baseRoleName;
	}
	public String getBaseRoleId() {
		return baseRoleId;
	}
	public void setBaseRoleId(String baseRoleId) {
		this.baseRoleId = baseRoleId;
	}
	public String getSaas() {
		return saas;
	}
	public void setSaas(String saas) {
		this.saas = saas;
	}
}
