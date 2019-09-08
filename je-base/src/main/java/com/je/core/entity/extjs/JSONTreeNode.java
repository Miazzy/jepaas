package com.je.core.entity.extjs;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnore;


/**
 * @version 1.0
 * @author  YUNFENGCHENG
 * @用途     Node辅助类
 */
@JsonFilter("withoutChecked")
public class JSONTreeNode implements Serializable{
	/**
	 *
	 */
	private static final long serialVersionUID = -5445464065128298330L;
	private static String NULL_FIELD = "STATUS";
	private String id = NULL_FIELD;            //ID
	private String text = NULL_FIELD;          //节点显示
	private String enField="";          //英文字段
	private String code = NULL_FIELD; //所在字典的名称
	private String parent = NULL_FIELD; // 父节点ID
	private String icon;//图片
	private String iconCls;//图片样式
	private String href;          //链接
	private String hrefTarget = "";    //链接指向
	private String nodeType;
	private String layer;
	private String description = "";   //描述信息
	private boolean leaf = false;         //是否叶子
	private boolean expandable = true;   //是否可展开
	private boolean expanded=false;    //默认是否展开
	private boolean checked = false;
	private boolean async=false;//是否异步
	private String nodeInfo = ""; //节点信息
	private String nodeInfoType = ""; //节点实体类型
	private String parentText="";//父节点路径
	private String nodePath; //路径
	private String parentPath;//父节点路径
	private String disabled="";
	private String orderIndex="";
	private String treeOrderIndex="";
	private String fieldCodes=""; //查询字段集合     不会构建成json
	//原业务实体需构建的业务字段
	private Map<String,Object> bean=new HashMap<String,Object>();

	List<JSONTreeNode> children = new ArrayList<JSONTreeNode>();

	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}

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
	public boolean isLeaf() {
		return leaf;
	}
	public void setLeaf(boolean leaf) {
		this.leaf = leaf;
	}
	public String getHref() {
		return href;
	}
	public void setHref(String href) {
		this.href = href;
	}
	public String getHrefTarget() {
		return hrefTarget;
	}
	public void setHrefTarget(String hrefTarget) {
		this.hrefTarget = hrefTarget;
	}
	public boolean isExpandable() {
		return expandable;
	}
	public void setExpandable(boolean expandable) {
		this.expandable = expandable;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public boolean getChecked() {
		return checked;
	}
	public void setChecked(boolean checked) {
		this.checked = checked;
	}
	public List<JSONTreeNode> getChildren() {
		return children;
	}
	public void setChildren(List<JSONTreeNode> children) {
		this.children = children;
	}
	public String getParent() {
		return parent;
	}
	public void setParent(String parent) {
		this.parent = parent;
	}
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
	@JsonIgnore
	public String getFieldCodes() {
		return fieldCodes;
	}
	public void setFieldCodes(String fieldCodes) {
		this.fieldCodes = fieldCodes;
	}
	public String getDisabled() {
		return disabled;
	}
	public void setDisabled(String disabled) {
		this.disabled = disabled;
	}
	public String getIconCls() {
		return iconCls;
	}
	public void setIconCls(String iconCls) {
		this.iconCls = iconCls;
	}
	public String getNodePath() {
		return nodePath;
	}
	public void setNodePath(String nodePath) {
		this.nodePath = nodePath;
	}
	public String getNodeType() {
		return nodeType;
	}
	public void setNodeType(String nodeType) {
		this.nodeType = nodeType;
	}
	public Map<String, Object> getBean() {
		return bean;
	}
	public void setBean(Map<String, Object> bean) {
		this.bean = bean;
	}
	public String getOrderIndex() {
		return orderIndex;
	}
	public void setOrderIndex(String orderIndex) {
		this.orderIndex = orderIndex;
	}
	public String getParentPath() {
		return parentPath;
	}
	public void setParentPath(String parentPath) {
		this.parentPath = parentPath;
	}
	public String getLayer() {
		return layer;
	}
	public void setLayer(String layer) {
		this.layer = layer;
	}
	public String getTreeOrderIndex() {
		return treeOrderIndex;
	}
	public void setTreeOrderIndex(String treeOrderIndex) {
		this.treeOrderIndex = treeOrderIndex;
	}
	public String getEnField() {
		return enField;
	}
	public void setEnField(String enField) {
		this.enField = enField;
	}
	public boolean getExpanded() {
		return expanded;
	}
	public void setExpanded(boolean expanded) {
		this.expanded = expanded;
	}
	public boolean isAsync() {
		return async;
	}
	public void setAsync(boolean async) {
		this.async = async;
	}

	public String getParentText() {
		return parentText;
	}

	public void setParentText(String parentText) {
		this.parentText = parentText;
	}
}
