package com.je.rbac.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * 权限组实体
 * 为了降低程序复杂程度权限组不参与与角色的多与多关系,单单完成快速授权的任务.
 * @author 研发部:云凤程
 * 2012-1-18 下午04:27:31
 */
//@Entity
//@GenericGenerator(name="systemUUID",strategy="uuid")
//@Table(name="PC_CORE_PERMISSIONGROUP")
public class PermissionGroup implements Serializable{
	
	private static final long serialVersionUID = 5475555082371852405L;
	//主键
	private String perGroupId;
	
	
//	@Id
//	@GeneratedValue(generator="systemUUID")
//	@Column(length=50)
	public String getPerGroupId() {
		return perGroupId;
	}
	public void setPerGroupId(String perGroupId) {
		this.perGroupId = perGroupId;
	}
	
	//权限组中的权限
//	private Set<Permission> permissions = new HashSet<Permission>();
//	@ManyToMany(fetch=FetchType.LAZY,cascade={CascadeType.PERSIST})
//	@LazyCollection(LazyCollectionOption.TRUE)
//	public Set<Permission> getPermissions() {
//		return permissions;
//	}
//	public void setPermissions(Set<Permission> permissions) {
//		this.permissions = permissions;
//	}
	
}
