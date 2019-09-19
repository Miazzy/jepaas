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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.je.rbac.model.EndUser;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.je.core.ann.entity.TreeNode;
import com.je.core.constants.tree.TreeNodeType;
import com.je.core.entity.TreeBaseEntity;
//@Entity
//@GenericGenerator(name="systemUUID",strategy="uuid")
//@Table(name="JE_CORE_SENTRY")
public class Sentry extends TreeBaseEntity implements Serializable {
	
	private static final long serialVersionUID = -4625985676117937470L;
//	@TreeNode(type=TreeNodeType.ID)
	private String sentryId;
//	@TreeNode(type=TreeNodeType.TEXT)
	private String sentryName;
//	@TreeNode(type=TreeNodeType.CODE)
	private String sentryCode;
	private String mainDuty;
	//对应的岗位参数值的表名
	private String tableCode;
	//集团监管公司名称
	private String jtjggsName;
	//集团监管公司编码
	private String jtjggsCode;
	//集团监管公司主键
	private String jtjggsId;
	//集团公司名称
	private String jtgsMc;
	//集团公司代码
	private String jtgsDm;
	//集团公司主键
	private String jtgsId;
	private Set<Sentry> children =  new HashSet<Sentry>();
//	@TreeNode(type=TreeNodeType.PARENT)
	private Sentry parent;
	private Set<EndUser> users = new HashSet<EndUser>();
//	@Id
//	@GeneratedValue(generator="systemUUID")
//	@Column(length=50)
	public String getSentryId() {
		return sentryId;
	}
	public void setSentryId(String sentryId) {
		this.sentryId = sentryId;
	}
	public String getSentryName() {
		return sentryName;
	}
	public void setSentryName(String sentryName) {
		this.sentryName = sentryName;
	}
	public String getSentryCode() {
		return sentryCode;
	}
	public void setSentryCode(String sentryCode) {
		this.sentryCode = sentryCode;
	}
//	@Column(length=4000)
	public String getMainDuty() {
		return mainDuty;
	}
	public void setMainDuty(String mainDuty) {
		this.mainDuty = mainDuty;
	}
	@JsonIgnore
//	@OneToMany(fetch=FetchType.LAZY,mappedBy="parent",cascade={CascadeType.REMOVE})
//	@LazyCollection(LazyCollectionOption.TRUE)
	public Set<Sentry> getChildren() {
		return children;
	}
	public void setChildren(Set<Sentry> children) {
		this.children = children;
	}
	@JsonIgnore
//	@ManyToOne(optional=false,fetch=FetchType.LAZY)
//	@JoinColumn(name="PARENT")
//	@Override
	public Sentry getParent() {
		return parent;
	}
	public void setParent(Sentry parent) {
		this.parent = parent;
	}
	@JsonIgnore
//	@ManyToMany(fetch=FetchType.LAZY,cascade={CascadeType.MERGE})
//	@JoinTable(name = "JE_CORE_SENTRY_USER", joinColumns = {
//			@JoinColumn(name = "sentryId") },
//			inverseJoinColumns = { @JoinColumn(name = "userId")
//	})
//	@LazyCollection(LazyCollectionOption.TRUE)
	public Set<EndUser> getUsers() {
		return users;
	}
	public void setUsers(Set<EndUser> users) {
		this.users = users;
	}
	public String getTableCode() {
		return tableCode;
	}
	public void setTableCode(String tableCode) {
		this.tableCode = tableCode;
	}
	public String getJtjggsName() {
		return jtjggsName;
	}
	public void setJtjggsName(String jtjggsName) {
		this.jtjggsName = jtjggsName;
	}
	public String getJtjggsCode() {
		return jtjggsCode;
	}
	public void setJtjggsCode(String jtjggsCode) {
		this.jtjggsCode = jtjggsCode;
	}
	public String getJtjggsId() {
		return jtjggsId;
	}
	public void setJtjggsId(String jtjggsId) {
		this.jtjggsId = jtjggsId;
	}
	public String getJtgsMc() {
		return jtgsMc;
	}
	public void setJtgsMc(String jtgsMc) {
		this.jtgsMc = jtgsMc;
	}
	public String getJtgsDm() {
		return jtgsDm;
	}
	public void setJtgsDm(String jtgsDm) {
		this.jtgsDm = jtgsDm;
	}
	public String getJtgsId() {
		return jtgsId;
	}
	public void setJtgsId(String jtgsId) {
		this.jtgsId = jtgsId;
	}
	
	
}
