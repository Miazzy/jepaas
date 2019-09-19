package com.je.rbac.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * 权限资源的功能描述实体[单向关联]
 * @author 研发部:云凤程
 * 2012-1-18 下午05:04:55
 */
//@Entity
//@GenericGenerator(name="systemUUID",strategy="uuid")
//@Table(name="PC_CORE_OPERATION")
public class Operation implements Serializable{
	private static final long serialVersionUID = -8859897552002195070L;
	//ID
	private String operId;
	//功能描述的CODE
	private String operCode;
	//功能描述的Name
	private String operName;
//	@Id
//	@GeneratedValue(generator="systemUUID")
//	@Column(length=50)
	public String getOperId() {
		return operId;
	}
	public void setOperId(String operId) {
		this.operId = operId;
	}
	public String getOperCode() {
		return operCode;
	}
	public void setOperCode(String operCode) {
		this.operCode = operCode;
	}
	public String getOperName() {
		return operName;
	}
	public void setOperName(String operName) {
		this.operName = operName;
	}
	
}
