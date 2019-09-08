package com.je.rbac.model;

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

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.je.core.entity.TreeBaseEntity;

/**
 * 资源描述实体
 * 资源不用有特定的维护界面,完全在前台APP的XML中定制,有安装功能自行插入.
 * 单向关系就可以了
 * @author 研发部:云凤程
 * 2012-1-18 下午04:50:05
 */
//@Entity
//@GenericGenerator(name="systemUUID",strategy="uuid")
//@Table(name="PC_CORE_APPRESOURCE")
public class AppResource extends TreeBaseEntity {
	public AppResource() {
		super();
	}
	//父节点
	private AppResource parent;
	//孩子节点
	private Set<AppResource> children =  new HashSet<AppResource>();	
	//资源项编码
	private String resCode ;
	//主键
	private String resId;
	//资源名称
	private String resName;
	//资源类型 菜单,操作,数据
	private String resType;
	@JsonIgnore
//	@OneToMany(fetch=FetchType.LAZY,mappedBy="parent",cascade={CascadeType.REMOVE})
//	@LazyCollection(LazyCollectionOption.TRUE)
	public Set<AppResource> getChildren() {
		return children;
	}
	public void setChildren(Set<AppResource> children) {
		this.children = children;
	}
	public String getResCode() {
		return resCode;
	}
	public void setResCode(String resCode) {
		this.resCode = resCode;
	}
//	@Id
//	@GeneratedValue(generator="systemUUID")
//	@Column(length=50)
	public String getResId() {
		return resId;
	}
	public void setResId(String resId) {
		this.resId = resId;
	}
	public String getResName() {
		return resName;
	}
	public void setResName(String resName) {
		this.resName = resName;
	}
	public String getResType() {
		return resType;
	}
	public void setResType(String resType) {
		this.resType = resType;
	}
	public void setParent(AppResource parent) {
		this.parent = parent;
	}
	@JsonIgnore
//	@ManyToOne(optional=false, fetch=FetchType.LAZY)
//	@JoinColumn(name="PARENT")
//	@Override
//	@LazyCollection(LazyCollectionOption.TRUE)
	public AppResource getParent() {
		return parent;
	}
	
	
	//他拥有的功能操作权限描述
//	private Set<Operation> operations = new HashSet<Operation>();
//	@OneToMany(fetch=FetchType.LAZY,cascade={CascadeType.PERSIST})
//	@LazyCollection(LazyCollectionOption.TRUE)
//	public Set<Operation> getOperations() {
//		return operations;
//	}
//	public void setOperations(Set<Operation> operations) {
//		this.operations = operations;
//	}
}
