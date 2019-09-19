/**
 * 
 */
package com.je.core.constants.tree;

/**
 * 树形节点类型
 * @author zhangshuaipeng
 * 2012-2-23 上午09:24:53
 */
public enum TreeNodeType {
	
	ID,
	TEXT,
	CODE,
	PARENT,
	PARENTTEXT,
	NODEINFO,
	NODEINFOTYPE,
	NODETYPE,
	LAYER,
	ICON,
	ICONCLS,
	DISABLED,
	NODEPATH,
	PARENTPATH,
	HREF,
	HREFTARGET,
	ORDERINDEX,
	DESCRIPTION,
	ENFIELD,
	TREEORDERINDEX,
	NONE;
	public boolean equals(TreeNodeType other) {
		int i = this.compareTo(other);
		if(0!=i) {
			return false;
		}
		return true;
	}
}
