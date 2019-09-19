package com.je.wf.processVo;

import java.io.Serializable;

/**
 * 会签信息
 * @author zhangshuaipeng
 *
 */
public class CountersignInfo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3552369263425739369L;
	/**会签类型*/
	private String countersignType;
	/**会签比例*/
	private Double countersignProportion=0.0;
	/**会签负责人*/
	private String countersignAssgineName;
	/**会签负责人ID*/
	private String countersignAssgineId;
	/**会签人员*/
	private String[] countersignUserNames;
	/**会签人员*/
	private String[] countersignUserIds;
	/**是否按照顺序审批*/
	private Boolean countersignOrder;
	public String getCountersignType() {
		return countersignType;
	}
	public void setCountersignType(String countersignType) {
		this.countersignType = countersignType;
	}
	public Double getCountersignProportion() {
		return countersignProportion;
	}
	public void setCountersignProportion(Double countersignProportion) {
		this.countersignProportion = countersignProportion;
	}
	public String getCountersignAssgineName() {
		return countersignAssgineName;
	}
	public void setCountersignAssgineName(String countersignAssgineName) {
		this.countersignAssgineName = countersignAssgineName;
	}
	public String getCountersignAssgineId() {
		return countersignAssgineId;
	}
	public void setCountersignAssgineId(String countersignAssgineId) {
		this.countersignAssgineId = countersignAssgineId;
	}
	public String[] getCountersignUserNames() {
		return countersignUserNames;
	}
	public void setCountersignUserNames(String[] countersignUserNames) {
		this.countersignUserNames = countersignUserNames;
	}
	public String[] getCountersignUserIds() {
		return countersignUserIds;
	}
	public void setCountersignUserIds(String[] countersignUserIds) {
		this.countersignUserIds = countersignUserIds;
	}
	public Boolean getCountersignOrder() {
		return countersignOrder;
	}
	public void setCountersignOrder(Boolean countersignOrder) {
		this.countersignOrder = countersignOrder;
	}
	
}
