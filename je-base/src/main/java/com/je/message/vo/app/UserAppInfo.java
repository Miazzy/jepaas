package com.je.message.vo.app;
/**
 * 用户登录注册推送信息
 * @author zhangshuaipeng
 *
 */
public class UserAppInfo {
	/**用户ID*/
	private String userId;
	/**APK名称*/
	private String apkName;
	/**APKID*/
	private String apkId;
	/**APK键*/
	private String apkKey;
	/**CID*/
	private String cId;
	/**TOKEN*/
	private String token;
	/**手机类型*/
	private String type;
	
	public UserAppInfo(String userId, String apkName, String apkId,
			String apkKey, String cId, String token, String type) {
		super();
		this.userId = userId;
		this.apkName = apkName;
		this.apkId = apkId;
		this.apkKey = apkKey;
		this.cId = cId;
		this.token = token;
		this.type = type;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getApkName() {
		return apkName;
	}
	public void setApkName(String apkName) {
		this.apkName = apkName;
	}
	public String getApkId() {
		return apkId;
	}
	public void setApkId(String apkId) {
		this.apkId = apkId;
	}
	public String getApkKey() {
		return apkKey;
	}
	public void setApkKey(String apkKey) {
		this.apkKey = apkKey;
	}
	public String getcId() {
		return cId;
	}
	public void setcId(String cId) {
		this.cId = cId;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
}
