package com.je.core.entity.extjs;

import java.util.ArrayList;
import java.util.List;


/**
 * @version 1.0
 * @author  YUNFENGCHENG
 * @用途     Node辅助类
 */
public class AsyncJSONTreeNode {
	private static String NULL_FIELD = "STATUS";
	private String id = NULL_FIELD;            //ID
    private String text = NULL_FIELD;          //节点显示
    private String cls = NULL_FIELD;           //图标
    private boolean leaf = false;         //是否叶子
    private String href;          //链接
    private String hrefTarget = NULL_FIELD;    //链接指向
    private boolean expandable = true;   //是否展开
    private String description = NULL_FIELD;   //描述信息
    private String code = NULL_FIELD; //所在字典的名称
    private String icon;//图片
    private String bigIcon;
    private String iconCls;//图片
    private String bigIconCls;
    private boolean checked = false;
    private String parent = NULL_FIELD; // 父节点ID
    private String nodeInfo = NULL_FIELD; //节点信息
    private String nodeInfoType = NULL_FIELD; //节点实体类型
    private String disabled="FALSE";
    private String path;
    private Integer orderIndex;
    
    
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
	public String getCls() {
		return cls;
	}
	public void setCls(String cls) {
		this.cls = cls;
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
	public Integer getOrderIndex() {
		return orderIndex;
	}
	public void setOrderIndex(Integer orderIndex) {
		this.orderIndex = orderIndex;
	}
	public String getDisabled() {
		return disabled;
	}
	public void setDisabled(String disabled) {
		this.disabled = disabled;
	}
	public String getBigIcon() {
		return bigIcon;
	}
	public void setBigIcon(String bigIcon) {
		this.bigIcon = bigIcon;
	}
	public String getIconCls() {
		return iconCls;
	}
	public void setIconCls(String iconCls) {
		this.iconCls = iconCls;
	}
	public String getBigIconCls() {
		return bigIconCls;
	}
	public void setBigIconCls(String bigIconCls) {
		this.bigIconCls = bigIconCls;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	
	
}
