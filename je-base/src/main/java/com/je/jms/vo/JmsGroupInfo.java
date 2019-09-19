package com.je.jms.vo;

import java.util.HashMap;
import java.util.Map;

public class JmsGroupInfo {
	/**讨论组ID*/
	private String id;
	/**操作       refresh:刷新用户  kickOut：踢出讨论组  join：加入讨论组  update：更新讨论组名称  remove：删除讨论组*/
	private String oper;
	/**用户ID*/
	private String userIds;
	/**用户名称*/
	private String userNames;
	private Map<String,Object> groupValues=new HashMap<String,Object>();
	/**修改后讨论组名称*/
	private String name;
	
	public JmsGroupInfo(String id, String oper,String userNames, String userIds, String name) {
		super();
		this.id = id;
		this.oper = oper;
		this.userIds = userIds;
		this.userNames=userNames;
		this.name = name;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getOper() {
		return oper;
	}
	public void setOper(String oper) {
		this.oper = oper;
	}
	public String getUserIds() {
		return userIds;
	}
	public void setUserIds(String userIds) {
		this.userIds = userIds;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUserNames() {
		return userNames;
	}
	public void setUserNames(String userNames) {
		this.userNames = userNames;
	}
	public Map<String, Object> getGroupValues() {
		return groupValues;
	}
	public void setGroupValues(Map<String, Object> groupValues) {
		this.groupValues = groupValues;
	}
	
}
