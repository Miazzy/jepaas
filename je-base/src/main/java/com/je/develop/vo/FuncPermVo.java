package com.je.develop.vo;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class FuncPermVo{
	/**
	 * 是否全部
	 */
	private Boolean all=true;
	/**
	 * 是否取反
	 */
	private Boolean versa=false;
	/**
	 * 查询SQL
	 */
	private String querySql="";
	/**
	 * 是否覆盖
	 */
	private Boolean sqlOverwrite=false;
	/**
	 * 字段权限
	 */
	private Set<String> fieldIds=new HashSet<String>();
	private Set<String> fieldReadOnlyIds=new HashSet<String>();
	private Set<String> buttonIds=new HashSet<String>();
	private Set<String> childrenIds=new HashSet<String>();
	private Set<String> dicCodes=new HashSet<String>();
	/**
	 * 使用关系ID
	 */
	private Set<String> useCjglIds=new HashSet<>();
	/**
	 * 可见人ID
	 */
	private Set<String> seeUserIds=new HashSet<>();
	/**
	 * 可见部门ID
	 */
	private Set<String> seeDeptIds=new HashSet<>();
	/**
	 * 可见角色ID
	 */
	private Set<String> seeRoleIds=new HashSet<>();
	/**
	 * 可见岗位ID
	 */
	private Set<String> seeSentryIds=new HashSet<>();
	/**
	 * 权限类型
	 */
	private Set<String> permConfig=new HashSet<>();
	/**权限SQL*/
	private String permSql="";
	/**
	 * 权限脚本
	 */
	private String permJs="";
	/**
	 * 用户控制字段  为空使用SY_CREATEUSERID
	 */
	private String userField="";
	/***
	 * 部门控制字段 为空使用SY_CREATEORGID
	 */
	private String deptField="";
	/**
	 * 字典权限信息
	 */
	private Map<String,FuncDicPermVo> dicInfos=new HashMap<String,FuncDicPermVo>();

	public Boolean getAll() {
		return all;
	}
	public void setAll(Boolean all) {
		this.all = all;
	}
	public Boolean getVersa() {
		return versa;
	}
	public void setVersa(Boolean versa) {
		this.versa = versa;
	}
	public String getQuerySql() {
		return querySql;
	}
	public void setQuerySql(String querySql) {
		this.querySql = querySql;
	}
	public Boolean getSqlOverwrite() {
		return sqlOverwrite;
	}
	public void setSqlOverwrite(Boolean sqlOverwrite) {
		this.sqlOverwrite = sqlOverwrite;
	}
	public Set<String> getFieldIds() {
		return fieldIds;
	}
	public void setFieldIds(Set<String> fieldIds) {
		this.fieldIds = fieldIds;
	}
	public Map<String, FuncDicPermVo> getDicInfos() {
		return dicInfos;
	}
	public void setDicInfos(Map<String, FuncDicPermVo> dicInfos) {
		this.dicInfos = dicInfos;
	}
	public Set<String> getButtonIds() {
		return buttonIds;
	}
	public void setButtonIds(Set<String> buttonIds) {
		this.buttonIds = buttonIds;
	}
	public Set<String> getChildrenIds() {
		return childrenIds;
	}
	public void setChildrenIds(Set<String> childrenIds) {
		this.childrenIds = childrenIds;
	}
	public Set<String> getDicCodes() {
		return dicCodes;
	}
	public void setDicCodes(Set<String> dicCodes) {
		this.dicCodes = dicCodes;
	}
	public Set<String> getFieldReadOnlyIds() {
		return fieldReadOnlyIds;
	}
	public void setFieldReadOnlyIds(Set<String> fieldReadOnlyIds) {
		this.fieldReadOnlyIds = fieldReadOnlyIds;
	}

	public Set<String> getUseCjglIds() {
		return useCjglIds;
	}

	public void setUseCjglIds(Set<String> useCjglIds) {
		this.useCjglIds = useCjglIds;
	}

	public Set<String> getSeeUserIds() {
		return seeUserIds;
	}

	public void setSeeUserIds(Set<String> seeUserIds) {
		this.seeUserIds = seeUserIds;
	}

	public Set<String> getSeeDeptIds() {
		return seeDeptIds;
	}

	public void setSeeDeptIds(Set<String> seeDeptIds) {
		this.seeDeptIds = seeDeptIds;
	}

	public Set<String> getSeeRoleIds() {
		return seeRoleIds;
	}

	public void setSeeRoleIds(Set<String> seeRoleIds) {
		this.seeRoleIds = seeRoleIds;
	}

	public Set<String> getSeeSentryIds() {
		return seeSentryIds;
	}

	public void setSeeSentryIds(Set<String> seeSentryIds) {
		this.seeSentryIds = seeSentryIds;
	}

	public Set<String> getPermConfig() {
		return permConfig;
	}

	public void setPermConfig(Set<String> permConfig) {
		this.permConfig = permConfig;
	}

	public String getPermSql() {
		return permSql;
	}

	public void setPermSql(String permSql) {
		this.permSql = permSql;
	}

	public String getPermJs() {
		return permJs;
	}

	public void setPermJs(String permJs) {
		this.permJs = permJs;
	}

	public String getUserField() {
		return userField;
	}

	public void setUserField(String userField) {
		this.userField = userField;
	}

	public String getDeptField() {
		return deptField;
	}

	public void setDeptField(String deptField) {
		this.deptField = deptField;
	}
}
