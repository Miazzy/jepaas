package com.je.core.entity;

import com.je.core.ann.entity.FieldChineseName;
import com.je.core.ann.entity.TreeNode;
import com.je.core.constants.tree.TreeNodeType;
import com.je.core.util.EntityUtils;
import com.je.core.util.StringUtil;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

/**
 * TODO 暂不明确
 */
@MappedSuperclass
public abstract class TreeBaseEntity extends BaseEntity {

	@FieldChineseName(value="层数")
	private Integer layer ;//所在的层数
	@TreeNode(type=TreeNodeType.NODETYPE)
	@FieldChineseName(value="结点类型")
	private String nodeType;//结点类型
	@FieldChineseName(value="父对象ID")
	private String parentId; // for ext-js show
	@TreeNode(type=TreeNodeType.NODEINFO)
	@FieldChineseName(value="功能信息")
	private String nodeInfo; // 功能信息，可能是URL，或一个相对路径
	@TreeNode(type=TreeNodeType.NODEINFOTYPE)
	@FieldChineseName(value="功能信息类型")
	private String nodeInfoType; //
	@TreeNode(type=TreeNodeType.NODEPATH)
	private String path;

	public Integer getLayer() {
		return layer;
	}

	public String getNodeType() {
		return nodeType;
	}

	public void setLayer(Integer layer) {
		this.layer = layer;
	}
	public void setNodeType(String nodeType) {
		this.nodeType = nodeType;
	}

	@Transient
	public abstract TreeBaseEntity getParent();

	@Transient
	public String getParentId() {
		TreeBaseEntity parent = this.getParent();
		if(null != parent) {
			String parentPkValue = EntityUtils.getInstance().getEntityIdValue(parent);
			if(StringUtil.isNotEmpty(parentPkValue)) {
				parentId = parentPkValue;
			}
		}
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	@Column(length=2000)
	public String getNodeInfo() {
		return nodeInfo;
	}
	public void setNodeInfo(String nodeInfo) {
		this.nodeInfo = nodeInfo;
	}
	public String getNodeInfoType() {
		return nodeInfoType;
	}
	public void setNodeInfoType(String nodeInfoType) {
		this.nodeInfoType = nodeInfoType;
	}
	@Column(length=4000)
	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}


}
