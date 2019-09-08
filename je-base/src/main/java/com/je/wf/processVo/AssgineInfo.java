package com.je.wf.processVo;

import java.io.Serializable;

/**
 * 处理目标信息
 * @author zhangshuaipeng
 */
public class AssgineInfo implements Serializable{
	/**
	 *
	 */
	private static final long serialVersionUID = 1991696555306148026L;
	private String type;
	private String id;
	private String code;
	private String name;
	private String source;
	private String allIds;
	private String showType;
	private String permType;
	private String jtgsMc;
	private String jtgsId;
	private String userWhere;
	private String userPerm;
	private String userWhereSql;
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getAllIds() {
		return allIds;
	}
	public void setAllIds(String allIds) {
		this.allIds = allIds;
	}
	public String getShowType() {
		return showType;
	}
	public void setShowType(String showType) {
		this.showType = showType;
	}
	public String getPermType() {
		return permType;
	}
	public void setPermType(String permType) {
		this.permType = permType;
	}
	public String getJtgsMc() {
		return jtgsMc;
	}
	public void setJtgsMc(String jtgsMc) {
		this.jtgsMc = jtgsMc;
	}
	public String getJtgsId() {
		return jtgsId;
	}
	public void setJtgsId(String jtgsId) {
		this.jtgsId = jtgsId;
	}
	public String getUserWhere() {
		return userWhere;
	}
	public void setUserWhere(String userWhere) {
		this.userWhere = userWhere;
	}
	public String getUserPerm() {
		return userPerm;
	}
	public void setUserPerm(String userPerm) {
		this.userPerm = userPerm;
	}
	public String getUserWhereSql() {
		return userWhereSql;
	}
	public void setUserWhereSql(String userWhereSql) {
		this.userWhereSql = userWhereSql;
	}

}
