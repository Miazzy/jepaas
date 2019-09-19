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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.je.core.ann.entity.TreeNode;
import com.je.core.constants.tree.TreeNodeType;
import com.je.core.entity.TreeBaseEntity;
/**
 * 部门实体
 * @author 研发部:云凤程
 * 2012-1-18 下午02:21:50
 */
//@Entity
//@GenericGenerator(name="systemUUID",strategy="uuid")
//@Table(name="JE_CORE_DEPARTMENT",uniqueConstraints={@UniqueConstraint(columnNames="DEPTCODE")})
public class Department extends TreeBaseEntity implements Serializable {
	private static final long serialVersionUID = 5668212356811214701L;
	
	public Department() {
		super();
	}
	
	//主键
//	@TreeNode(type=TreeNodeType.ID)
	private String deptId;
	//父部门节点
//	@TreeNode(type=TreeNodeType.PARENT)
	private Department parent;
	//孩子节点
	private Set<Department> children =  new HashSet<Department>();
	//部门项编码
//	@TreeNode(type=TreeNodeType.CODE)
	private String deptCode ;
	//部门名称
//	@TreeNode(type=TreeNodeType.TEXT)
	private String deptName;
	//图标样式
//	@TreeNode(type=TreeNodeType.ICONCLS)
	private String iconCls;
	//部门级别编码
	private String rankCode;
	//部门级别名称
	private String rankName;
	//自编号
	private String deptNum;
	//上级部门名称
	private String parentName;
	//上级部门编码
	private String parentCode;
	//地址
	private String address;
	//电话
	private String phone;
	//负责人编码
	private String chargeUser;
	//负责人
	private String chargeUserName;
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
	//公司部门ID
	private String gsbmId;
	//职能描述
	private String znms;
	//部门简称
	private String easyName;
	//部门内用户
	private Set<EndUser> users = new HashSet<EndUser>();
	@JsonIgnore
//	@OneToMany(fetch=FetchType.LAZY,mappedBy="dept",cascade={CascadeType.MERGE})
//	@LazyCollection(LazyCollectionOption.TRUE)
	public Set<EndUser> getUsers() {
		return users;
	}
	public void setUsers(Set<EndUser> users) {
		this.users = users;
	}
	@JsonIgnore
//	@ManyToOne(optional=false,fetch=FetchType.LAZY)
//	@JoinColumn(name="PARENT")
//	@Override
	public Department getParent() {
		return parent;
	}
	public void setParent(Department parent) {
		this.parent = parent;
	}
	@JsonIgnore
//	@OneToMany(fetch=FetchType.LAZY,mappedBy="parent",cascade={CascadeType.REMOVE})
//	@LazyCollection(LazyCollectionOption.TRUE)
	public Set<Department> getChildren() {
		return children;
	}
	public void setChildren(Set<Department> children) {
		this.children = children;
	}
//	@Column(unique=true)
	public String getDeptCode() {
		return deptCode;
	}
	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}
//	@Id
//	@GeneratedValue(generator="systemUUID")
//	@Column(length=50)
	public String getDeptId() {
		return deptId;
	}
	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public String getRankCode() {
		return rankCode;
	}
	public void setRankCode(String rankCode) {
		this.rankCode = rankCode;
	}
	public String getRankName() {
		return rankName;
	}
	public void setRankName(String rankName) {
		this.rankName = rankName;
	}
	public String getDeptNum() {
		return deptNum;
	}
	public void setDeptNum(String deptNum) {
		this.deptNum = deptNum;
	}
	public String getParentName() {
		return parentName;
	}
	public void setParentName(String parentName) {
		this.parentName = parentName;
	}
	public String getParentCode() {
		return parentCode;
	}
	public void setParentCode(String parentCode) {
		this.parentCode = parentCode;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getChargeUser() {
		return chargeUser;
	}
	public void setChargeUser(String chargeUser) {
		this.chargeUser = chargeUser;
	}
	public String getChargeUserName() {
		return chargeUserName;
	}
	public void setChargeUserName(String chargeUserName) {
		this.chargeUserName = chargeUserName;
	}
	public String getIconCls() {
		return iconCls;
	}
	public void setIconCls(String iconCls) {
		this.iconCls = iconCls;
	}
	public String getJtjggsName() {
		return jtjggsName;
	}
	public void setJtjggsName(String jtjggsName) {
		this.jtjggsName = jtjggsName;
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
	public String getJtjggsId() {
		return jtjggsId;
	}
	public void setJtjggsId(String jtjggsId) {
		this.jtjggsId = jtjggsId;
	}
	public String getJtgsId() {
		return jtgsId;
	}
	public void setJtgsId(String jtgsId) {
		this.jtgsId = jtgsId;
	}
	public String getJtjggsCode() {
		return jtjggsCode;
	}
	public void setJtjggsCode(String jtjggsCode) {
		this.jtjggsCode = jtjggsCode;
	}
	public String getGsbmId() {
		return gsbmId;
	}
	public void setGsbmId(String gsbmId) {
		this.gsbmId = gsbmId;
	}
	public String getZnms() {
		return znms;
	}
	public void setZnms(String znms) {
		this.znms = znms;
	}
	
}
