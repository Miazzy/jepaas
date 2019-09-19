/**
 * 
 */
package com.je.wf.vo;

import com.je.core.ann.entity.FieldChineseName;

/**
 * 流程文件基本信息VO
 * @author chenmeng
 * 2012-3-22 上午11:33:53
 */
public class DeploymentFile {

	@FieldChineseName("文件全名")
	private String fileFullName;
	@FieldChineseName("文件名称")
	private String simpleName;
	@FieldChineseName("流程名称")
	private String processName;
	@FieldChineseName("流程KEY")
	private String processKey;
	@FieldChineseName("流程描述")
	private String description;
	@FieldChineseName("业务对象类全名")
	private String boType;
	@FieldChineseName("业务对象主键名称")
	private String boIdName;
	@FieldChineseName("发布状态")
	private String deploymented;
	@FieldChineseName("当前版本号")
	private Long lastVersion;
	@FieldChineseName("是否任何人均可启动流程")
	private String anyoneStartable;
	@FieldChineseName("启动角色组")
	private String startRoles;
	@FieldChineseName("流程所属组")
	private String processGroup;
	@FieldChineseName("是否回执")
	private String receiptAble;
	@FieldChineseName("回执类型")
	private String receiptType;
	public String getFileFullName() {
		return fileFullName;
	}
	public void setFileFullName(String fileFullName) {
		this.fileFullName = fileFullName;
	}
	public String getProcessName() {
		return processName;
	}
	public void setProcessName(String processName) {
		this.processName = processName;
	}
	public String getProcessKey() {
		return processKey;
	}
	public void setProcessKey(String processKey) {
		this.processKey = processKey;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getBoType() {
		return boType;
	}
	public void setBoType(String boType) {
		this.boType = boType;
	}
	public String getBoIdName() {
		return boIdName;
	}
	public void setBoIdName(String boIdName) {
		this.boIdName = boIdName;
	}
	public String getDeploymented() {
		return deploymented;
	}
	public void setDeploymented(String deploymented) {
		this.deploymented = deploymented;
	}
	public String getSimpleName() {
		return simpleName;
	}
	public void setSimpleName(String simpleName) {
		this.simpleName = simpleName;
	}
	public Long getLastVersion() {
		return lastVersion;
	}
	public void setLastVersion(Long lastVersion) {
		this.lastVersion = lastVersion;
	}
	public String getAnyoneStartable() {
		return anyoneStartable;
	}
	public void setAnyoneStartable(String anyoneStartable) {
		this.anyoneStartable = anyoneStartable;
	}
	public String getStartRoles() {
		return startRoles;
	}
	public void setStartRoles(String startRoles) {
		this.startRoles = startRoles;
	}
	public String getProcessGroup() {
		return processGroup;
	}
	public void setProcessGroup(String processGroup) {
		this.processGroup = processGroup;
	}
	public String getReceiptAble() {
		return receiptAble;
	}
	public void setReceiptAble(String receiptAble) {
		this.receiptAble = receiptAble;
	}
	public String getReceiptType() {
		return receiptType;
	}
	public void setReceiptType(String receiptType) {
		this.receiptType = receiptType;
	}
	
}
