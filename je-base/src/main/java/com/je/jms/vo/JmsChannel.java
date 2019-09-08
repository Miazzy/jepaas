package com.je.jms.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.je.core.util.DateUtils;
import com.je.core.util.bean.DynaBean;
import com.je.jms.util.JmsUtil;
import com.je.rbac.model.EndUser;

/**
 * 聊天通道
 * @author zhangshuaipeng
 *
 */
public class JmsChannel implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 5629775649227179572L;
	/**通道ID*/
	private String id;
	/**通道名称  (用户讨论组)*/
	private String name;
	/**创建人  (用户讨论组)*/
	private String ownerName;
	/**创建人ID  (用户讨论组)*/
	private String ownerId;
	/**通道类型   GROUP || USER*/   
	private String type;
	/**通道参与人*/
	private List<DynaBean> users=new ArrayList<DynaBean>();
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getOwnerName() {
		return ownerName;
	}
	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}
	public String getOwnerId() {
		return ownerId;
	}
	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	@JsonIgnore
	public List<DynaBean> getUsers() {
		return users;
	}
	public void setUsers(List<DynaBean> users) {
		this.users = users;
	}
}
