package com.je.rbac.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class AdminPermInfo implements Serializable{
	private static final long serialVersionUID = 548477451914337874L;
	/**可授权角色*/
	private Set<String> rolePerms=new HashSet<String>();
	/**可见角色*/
	private Set<String> roleSees=new HashSet<String>();
	/**角色修改*/
	private Set<String> roleUpdates=new HashSet<String>();
	/**部门授权*/
	private Set<String> deptPerms=new HashSet<String>();
	/**部门查看**/
	private Set<String> deptSees=new HashSet<String>();
	/**部门修改*/
	private Set<String> deptUpdates=new HashSet<String>();
	/**岗位权限*/
	private Set<String> sentryPerms=new HashSet<String>();
	/**岗位查看*/
	private Set<String> sentrySees=new HashSet<String>();
	/**岗位修改*/
	private Set<String> sentryUpdates=new HashSet<String>();
	public Set<String> getRolePerms() {
		return rolePerms;
	}
	public void setRolePerms(Set<String> rolePerms) {
		this.rolePerms = rolePerms;
	}
	public Set<String> getRoleSees() {
		return roleSees;
	}
	public void setRoleSees(Set<String> roleSees) {
		this.roleSees = roleSees;
	}
	public Set<String> getRoleUpdates() {
		return roleUpdates;
	}
	public void setRoleUpdates(Set<String> roleUpdates) {
		this.roleUpdates = roleUpdates;
	}
	public Set<String> getDeptPerms() {
		return deptPerms;
	}
	public void setDeptPerms(Set<String> deptPerms) {
		this.deptPerms = deptPerms;
	}
	public Set<String> getDeptSees() {
		return deptSees;
	}
	public void setDeptSees(Set<String> deptSees) {
		this.deptSees = deptSees;
	}
	public Set<String> getDeptUpdates() {
		return deptUpdates;
	}
	public void setDeptUpdates(Set<String> deptUpdates) {
		this.deptUpdates = deptUpdates;
	}
	public Set<String> getSentryPerms() {
		return sentryPerms;
	}
	public void setSentryPerms(Set<String> sentryPerms) {
		this.sentryPerms = sentryPerms;
	}
	public Set<String> getSentrySees() {
		return sentrySees;
	}
	public void setSentrySees(Set<String> sentrySees) {
		this.sentrySees = sentrySees;
	}
	public Set<String> getSentryUpdates() {
		return sentryUpdates;
	}
	public void setSentryUpdates(Set<String> sentryUpdates) {
		this.sentryUpdates = sentryUpdates;
	}
	
	
}
