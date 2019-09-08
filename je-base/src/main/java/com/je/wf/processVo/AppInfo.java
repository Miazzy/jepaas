package com.je.wf.processVo;

import java.io.Serializable;

public class AppInfo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8750641114577893848L;
	/**手机应用ID*/
	private String apkId;
	/**手机应用KEY*/
	private String apkKey;
	/**手机应用名称*/
	private String apkName;
	/**功能ID*/
	private String appId;
	/**功能编码*/
	private String appCode;
	/**功能名称*/
	private String appName;
	
	public String getApkId() {
		return apkId;
	}
	public void setApkId(String apkId) {
		this.apkId = apkId;
	}
	public String getApkName() {
		return apkName;
	}
	public void setApkName(String apkName) {
		this.apkName = apkName;
	}
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	public String getAppName() {
		return appName;
	}
	public void setAppName(String appName) {
		this.appName = appName;
	}
	public String getApkKey() {
		return apkKey;
	}
	public void setApkKey(String apkKey) {
		this.apkKey = apkKey;
	}
	public String getAppCode() {
		return appCode;
	}
	public void setAppCode(String appCode) {
		this.appCode = appCode;
	}
	
}
