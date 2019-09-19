/**
 * 
 */
package com.je.rbac.model.view;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import com.je.core.ann.entity.TreeNode;
import com.je.core.ann.entity.TreeSelectNode;
import com.je.core.constants.tree.TreeNodeType;
import com.je.core.entity.TreeBaseEntity;

/**
 * @author chenmeng
 * 2012-3-6 上午11:06:00
 */
//@Entity
//@Table(name="JE_CORE_VDEPTUSER")
public class VDeptUser extends TreeBaseEntity {
	@TreeNode(type=TreeNodeType.ID)
	private String id;
	@TreeNode(type=TreeNodeType.TEXT)
	private String text;
	@TreeNode(type=TreeNodeType.CODE)
	private String code;
	@TreeNode(type=TreeNodeType.PARENT)
	private VDeptUser parent;
	@TreeNode(type=TreeNodeType.DISABLED)
	private String disabled;
	@TreeSelectNode
	private String backUserCode;
	@TreeNode(type=TreeNodeType.ICONCLS)
	private String iconCls;
	
	private List<VDeptUser> children = new ArrayList<VDeptUser>();
	
	
	@Id
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
	@ManyToOne(fetch=FetchType.LAZY,optional=false)
	@JoinColumn(name="parent")
	public VDeptUser getParent() {
		return parent;
	}
	public void setParent(VDeptUser parent) {
		this.parent = parent;
	}
	@OneToMany(fetch=FetchType.LAZY,mappedBy="parent",cascade={CascadeType.ALL})
	@LazyCollection(LazyCollectionOption.TRUE)
	public List<VDeptUser> getChildren() {
		return children;
	}
	public void setChildren(List<VDeptUser> children) {
		this.children = children;
	}
	public String getDisabled() {
		return disabled;
	}
	public void setDisabled(String disabled) {
		this.disabled = disabled;
	}
	public String getBackUserCode() {
		return backUserCode;
	}
	public void setBackUserCode(String backUserCode) {
		this.backUserCode = backUserCode;
	}
	public String getIconCls() {
		return iconCls;
	}
	public void setIconCls(String iconCls) {
		this.iconCls = iconCls;
	}
	
	
}
