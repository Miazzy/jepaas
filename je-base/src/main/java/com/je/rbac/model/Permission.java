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
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 权限实体
 * @author 研发部:云凤程
 * 2012-1-18 下午04:27:31
 */
//@Entity
//@GenericGenerator(name="systemUUID",strategy="uuid")
//@Table(name="JE_CORE_PERMISSION")
public class Permission implements Serializable{
	
	private static final long serialVersionUID = 2731904962553009885L;
	//主键
	private String perId;
	//拥有改权限的角色
	private Set<Role> roles = new HashSet<Role>();
	//拥有改权限的用户
	private Set<EndUser> endUsers = new HashSet<EndUser>();
	// 权限代码
	private String permCode;
	// 权限类型（菜单、按钮等）
	private String permType;
	// 追加类型（排除、追加）
	private String appendType;
	// 权限路径 permPath
	private String permPath;
	// 模块编码
	private String module;
	// 功能主键
	private String funcId;
//	@Id
//	@GeneratedValue(generator="systemUUID")
//	@Column(length=50)
	public String getPerId() {
		return perId;
	}
	public void setPerId(String perId) {
		this.perId = perId;
	}
	@JsonIgnore
//	@ManyToMany(fetch=FetchType.LAZY,cascade={CascadeType.MERGE})
//	@JoinTable(name = "JE_CORE_ROLE_PERM", joinColumns = {
//			@JoinColumn(name = "perId") },
//			inverseJoinColumns = { @JoinColumn(name = "roleId")
//	})
//	@LazyCollection(LazyCollectionOption.TRUE)
	public Set<Role> getRoles() {
		return roles;
	}
	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}
	@JsonIgnore
//	@ManyToMany(fetch=FetchType.LAZY,cascade={CascadeType.MERGE})
//	@JoinTable(name = "JE_CORE_USER_PERM", joinColumns = {
//			@JoinColumn(name = "perId") },
//			inverseJoinColumns = { @JoinColumn(name = "userId")
//	})
//	@LazyCollection(LazyCollectionOption.TRUE)
//	@Transient
	public Set<EndUser> getEndUsers() {
		return endUsers;
	}
	public void setEndUsers(Set<EndUser> endUsers) {
		this.endUsers = endUsers;
	}
	public String getPermCode() {
		return permCode;
	}
	public void setPermCode(String permCode) {
		this.permCode = permCode;
	}
	public String getPermType() {
		return permType;
	}
	public void setPermType(String permType) {
		this.permType = permType;
	}
	public String getAppendType() {
		return appendType;
	}
	public void setAppendType(String appendType) {
		this.appendType = appendType;
	}
//	@Column(length=3000)
	public String getPermPath() {
		return permPath;
	}
	public void setPermPath(String permPath) {
		this.permPath = permPath;
	}
	public String getModule() {
		return module;
	}
	public void setModule(String module) {
		this.module = module;
	}
	public String getFuncId() {
		return funcId;
	}
	public void setFuncId(String funcId) {
		this.funcId = funcId;
	}
	
	//所在权限组
//	private Set<PermissionGroup> permissionGroups = new HashSet<PermissionGroup>();
	//拥有改权限的部门
//	private Set<Department> departments = new HashSet<Department>();
	//拥有的资源
//	private AppResource resource = new AppResource();
	//拥有资源的操作权限描述
//	private Operation operation = new Operation();
	
//	@OneToOne(optional=false,fetch=FetchType.LAZY,cascade = CascadeType.MERGE)
//	@JoinColumn(name = "resId", referencedColumnName = "resId")
//	@LazyCollection(LazyCollectionOption.TRUE)
//	public AppResource getResource() {
//		return resource;
//	}
//	public void setResource(AppResource resource) {
//		this.resource = resource;
//	}
//	@OneToOne(optional=false,fetch=FetchType.LAZY,cascade = CascadeType.MERGE)
//	@JoinColumn(name = "operId", referencedColumnName = "operId")
//	@LazyCollection(LazyCollectionOption.TRUE)
//	public Operation getOperation() {
//		return operation;
//	}
//	public void setOperation(Operation operation) {
//		this.operation = operation;
//	}
	
//	@ManyToMany(fetch=FetchType.LAZY,cascade={CascadeType.MERGE})
//	@JoinTable(name = "DEPT_PERM", joinColumns = {
//			@JoinColumn(name = "perId") },
//			inverseJoinColumns = { @JoinColumn(name = "deptId")
//	})
//	@LazyCollection(LazyCollectionOption.TRUE)
//	public Set<Department> getDepartments() {
//		return departments;
//	}
//	public void setDepartments(Set<Department> departments) {
//		this.departments = departments;
//	}
	
//	@ManyToMany(fetch=FetchType.LAZY,cascade={CascadeType.MERGE})
//	@LazyCollection(LazyCollectionOption.TRUE)
//	public Set<PermissionGroup> getPermissionGroups() {
//		return permissionGroups;
//	}
//	public void setPermissionGroups(Set<PermissionGroup> permissionGroups) {
//		this.permissionGroups = permissionGroups;
//	}
	
	
}
