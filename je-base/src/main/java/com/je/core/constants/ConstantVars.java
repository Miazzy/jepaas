package com.je.core.constants;


/**
 * 常用静态变量
 * @author chenmeng
 *
 */
public class ConstantVars {
	/** 空白字符串 */
	public static final String BLANK_STR = "";
	/** action load方法中数据列表的默认key */
	public static final String DEFAULT_RESULT_NAME = "rows";
	public static final String TREE_RESULT_NAME = "children";
	/** 树型结点封装类 */
	public static final String TREE_NODE_WRAPPER = "com.je.core.entity.extjs.JSONTreeNode";
	// 顶层单位
	public static final String TOP_ORG = "ROOT";
	public static final String TREE_ROOT = "ROOT";
	//数据字典缓存KEY值前缀
	public static final String CACHE_DDNAME_PREFIX = "DD_NAME_";
	public static final String CACHE_DDCODE_PREFIX = "DD_CODE_";
	
	public static final String CACHE_DDCODE_TREE_PREFIX = "DD_CODE_TREE_";
	//数据字典项缓存KEY值前缀
	public static final String CACHE_DD_ITEM_PREFIX = "DDITEM_";
	//树型数据字典缓存KEY值前缀
	public static final String CACHE_DD_ITEM_TREE_PREFIX = "DDITEMTREE_";
	// 树型展示的类型
	public static final String TREE_SHOW_TYPE_QUERY = "query";
	public static final String TREE_SHOW_TYPE_FUNC = "func";
	
	public static final String STR_TRUE = "TRUE";
	public static final String STR_FALSE = "FALSE";

	
	/** 工作流部署路径 */
	// public static final String WF_JPDL_PATHs = ConfigCacheManager.getCacheValue("jbpm.store.jpdl.dir");
	// public static final String WF_JPDL_ARCH_PATHs = WF_JPDL_PATHs + ConfigCacheManager.getCacheValue("jbpm.jpdl.deployed");
	/** 工作流启动中文描述 */
	public static final String WF_START = "启动";
	
	
	/**
	 * 在构造JSON串时如果要排除BaseEntity的字段，则要用到这个数组
	 */
	public static final String[] BASE_ENTITY_EXCLUDES = new String[] {
		"audFlag",
		"createOrg",
		"createOrgName",
		"createTime",
		"createUser",
		"createUserName",
		"flag",
		"modifyOrg",
		"modifyOrgName",
		"modifyTime",
		"modifyUser",
		"modifyUserName",
		"status"
	};
	/**
	 * 在构造JSON串时如果要排除TreeBaseEntity的字段，则要用到这个数组
	 */
	public static final String[] TREE_BASE_ENTITY_EXCLUDES = new String[] {
		"layer",
		"nodeType",
		"orderIndex"
	};
	
	public static final Long DD_FETCH_LIMITs = 50L;
	public static final String STR_ORACLE="ORACLE";
	public static final String STR_SQLSERVER="SQLSERVER";
	public static final String STR_MYSQL="MYSQL";
}
