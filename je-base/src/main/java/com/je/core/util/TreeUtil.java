package com.je.core.util;

import java.util.ArrayList;
import java.util.HashMap;

import com.je.core.constants.tree.NodeType;
import com.je.core.entity.extjs.JSONTreeNode;
/**
 * 树形节点工具类
 * @author zhangshuaipeng
 *
 */
public class TreeUtil {
	/**
	 * 构建根节点
	 * @return
	 */
	public static JSONTreeNode buildRootNode(){
		return buildTreeNode(NodeType.ROOT, NodeType.ROOT, NodeType.ROOT, "", "", "", null);
	}
	/**
	 * 构建结构性返回的树形VO
	 * @param id 主键
	 * @param text 展示名称 就是要展示出来的内容
	 * @param code 编码 没有空
	 * @param nodeInfo 节点信息 没有空
	 * @param nodeInfoType 节点信息类型 没有空
	 * @param iconCls 图标
	 * @return
	 */
	public static JSONTreeNode buildTreeNode(String id,String text,String code,String nodeInfo,String nodeInfoType,String iconCls,String parent){
		JSONTreeNode node=new JSONTreeNode();
		node.setId(id);
		node.setText(text);
		node.setCode(code);
		node.setNodeInfo(nodeInfo);
		node.setNodeInfoType(nodeInfoType);
		node.setDisabled("1");
		node.setIconCls(iconCls);
		node.setParent(parent);
		node.setLeaf(false);
		return node;
	}
	public static JSONTreeNode copyNewTreeNode(JSONTreeNode node){
		JSONTreeNode n=new JSONTreeNode();
		n.setBean(new HashMap<String,Object>(node.getBean()));
		n.setChecked(node.getChecked());
		n.setChildren(new ArrayList<JSONTreeNode>());
		n.setCode(node.getCode());
		n.setDescription(node.getDescription());
		n.setDisabled(node.getDisabled());
		n.setEnField(node.getEnField());
		n.setExpandable(node.isExpandable());
		n.setExpanded(node.getExpanded());
		n.setFieldCodes(node.getFieldCodes());
		n.setHref(node.getHref());
		n.setHrefTarget(node.getHrefTarget());
		n.setIcon(node.getIcon());
		n.setIconCls(node.getIconCls());
		n.setId(node.getId());
		n.setLayer(node.getLayer());
		n.setLeaf(node.isLeaf());
		n.setNodeInfo(node.getNodeInfo());
		n.setNodeInfoType(node.getNodeInfoType());
		n.setNodePath(node.getNodePath());
		n.setNodeType(node.getNodeType());
		n.setOrderIndex(node.getOrderIndex());
		n.setParent(node.getParent());
		n.setParentPath(node.getParentPath());
		n.setText(node.getText());
		n.setTreeOrderIndex(node.getTreeOrderIndex());
		return n;
	}
	public static boolean verify(JSONTreeNode template) {
		if(StringUtil.isNotEmpty(template.getId())) {
			if(StringUtil.isNotEmpty(template.getCode())) {
				if(StringUtil.isNotEmpty(template.getText())) {
					if(StringUtil.isNotEmpty(template.getParent())) {
						return true;
					}
				}
			}
		}
		return false;
	}
}
