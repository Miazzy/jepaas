package com.je.message.vo.app;

import java.io.Serializable;

/**
 * app应用推送方式配置
 * @author zhangshuaipeng
 *
 */
public class MsgConfigVo implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 5629775649227179572L;
	/**应用apkKey，appId(平台创建apk生成的唯一码)*/
	private String apkKey;
	/**AppId*/
	private String gtAppId;
	/**AppKey*/
	private String gtAppKey;
	/**推送主机地址*/
	private String host="http://sdk.open.api.igexin.com/apiex.htm";
	/**应用master信息*/
	private String master;
	/**推送类型   单体用户  标签推送  别名推送*/
	private String targerType;//推送类型
	/**推送值*/
	private Object targerValue;
	public MsgConfigVo() {
		super();
	}
	
	public MsgConfigVo(String apkKey, String gtAppId, String gtAppKey, String master) {
		super();
		this.apkKey = apkKey;
		this.gtAppId = gtAppId;
		this.gtAppKey = gtAppKey;
		this.master = master;
	}
	
	public MsgConfigVo(String apkKey, String gtAppId, String gtAppKey, String master, String targerType, Object targerValue) {
		super();
		this.apkKey = apkKey;
		this.gtAppId = gtAppId;
		this.gtAppKey = gtAppKey;
		this.master = master;
		this.targerType = targerType;
		this.targerValue = targerValue;
	}

	public String getGtAppId() {
		return gtAppId;
	}
	public void setGtAppId(String gtAppId) {
		this.gtAppId = gtAppId;
	}
	public String getGtAppKey() {
		return gtAppKey;
	}
	public void setGtAppKey(String gtAppKey) {
		this.gtAppKey = gtAppKey;
	}
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public String getMaster() {
		return master;
	}
	public void setMaster(String master) {
		this.master = master;
	}
	public String getTargerType() {
		return targerType;
	}
	public void setTargerType(String targerType) {
		this.targerType = targerType;
	}
	public Object getTargerValue() {
		return targerValue;
	}
	public void setTargerValue(Object targerValue) {
		this.targerValue = targerValue;
	}
	public String getApkKey() {
		return apkKey;
	}
	public void setApkKey(String apkKey) {
		this.apkKey = apkKey;
	}
}
