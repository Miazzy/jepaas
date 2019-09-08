package com.je.message.vo;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

import com.je.core.util.StringUtil;

/**
 * 消息推送机制
 * @author zhangshuaipeng
 */
public class DwrMsgVo implements Serializable {

	private static final long serialVersionUID = -2892092243806133838L;
	/**用户ID*/
	private String userId;
	/**推送组ID*/
	private String groupId;
	/**标题*/
	private String title;
	/**内容*/
	private String context;
	/**内容展示模版*/
	private String showConfig;
	/**功能编码*/
	private String funcCode="";
	/**客户端提醒消息*/
	private JSONObject wsConfig;
	/**自定义函数*/
	private String callFunction="JE.showMsg";
	/**批量处理自定义函数*/
	private String batchCallFunction="JE.showBatchMsg";
	/**是否没有登录，下次登录调用*/
	private Boolean loginHistory=false;
	//排除浏览器sessionId  推送该用户时，不推送指定的sessionId
	private String excludeSessionIds="";
	/**业务数据*/
	private Map<String,Object> bean=new HashMap<String,Object>();
	/**验证用户是否登录操作   用于是否发送html5提醒*/
	private Boolean login=false;

	@SuppressWarnings("unused")
	private DwrMsgVo(){}

	/**
	 * 简易发送消息
	 * @param userId
	 * @param title
	 * @param context
	 */
	public DwrMsgVo(String userId, String title, String context) {
		super();
		this.userId = userId;
		this.title = title;
		this.context = context;
	}
	/**
	 * 带业务数据发送消息
	 * @param userId
	 * @param userName
	 * @param userCode
	 * @param title
	 * @param context
	 * @param bean
	 */
	public DwrMsgVo(String userId,
					String title, String context, Map<String, Object> bean) {
		super();
		this.userId = userId;
		this.title = title;
		this.context = context;
		this.bean = bean;
	}
	/**
	 * 使用模版展示数据
	 * @param userId
	 * @param userName
	 * @param userCode
	 * @param title
	 * @param context
	 * @param showConfig 模版   {字段值}
	 * @param bean
	 */
	public DwrMsgVo(String userId,String title, String context, String showConfig,Map<String, Object> bean) {
		super();
		this.userId = userId;
		this.title = title;
		this.context = context;
		this.showConfig = showConfig;
		this.bean = bean;
	}
	/**
	 * 调用前台指定方法  传入参数为    DwrMsgVo
	 * @param userId
	 * @param userName
	 * @param userCode
	 * @param title
	 * @param context
	 * @param callFunction
	 * @param bean
	 */
	public DwrMsgVo(String userId,String title, String context,String showConfig, String callFunction, String batchCallFunction,
					Map<String, Object> bean) {
		super();
		this.userId = userId;
		this.title = title;
		this.context = context;
		this.showConfig=showConfig;
		if(StringUtil.isNotEmpty(callFunction)){
			this.callFunction = callFunction;
		}
		if(StringUtil.isNotEmpty(batchCallFunction)){
			this.batchCallFunction=batchCallFunction;
		}
		this.bean = bean;
	}
	/**
	 *  调用前台指定方法  传入参数为    DwrMsgVo
	 * @param userId
	 * @param userName
	 * @param userCode
	 * @param title
	 * @param context
	 * @param showConfig
	 * @param callFunction
	 * @param loginHistory
	 * @param bean
	 */
	public DwrMsgVo(String userId,String title, String context, String showConfig,
					String callFunction,String batchCallFunction, Boolean loginHistory, Map<String, Object> bean) {
		super();
		this.userId = userId;
		this.title = title;
		this.context = context;
		this.showConfig = showConfig;
		if(StringUtil.isNotEmpty(callFunction)){
			this.callFunction = callFunction;
		}
		if(StringUtil.isNotEmpty(batchCallFunction)){
			this.batchCallFunction=batchCallFunction;
		}
		this.loginHistory = loginHistory;
		this.bean = bean;
	}
	public Map<String, Object> getBean() {
		return bean;
	}
	public void setBean(Map<String, Object> bean) {
		this.bean = bean;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContext() {
		return context;
	}
	public void setContext(String context) {
		this.context = context;
	}
	public String getShowConfig() {
		return showConfig;
	}
	public void setShowConfig(String showConfig) {
		this.showConfig = showConfig;
	}
	public String getCallFunction() {
		return callFunction;
	}
	public void setCallFunction(String callFunction) {
		this.callFunction = callFunction;
	}
	public Boolean getLoginHistory() {
		return loginHistory;
	}
	public void setLoginHistory(Boolean loginHistory) {
		this.loginHistory = loginHistory;
	}
	public String getBatchCallFunction() {
		return batchCallFunction;
	}
	public void setBatchCallFunction(String batchCallFunction) {
		this.batchCallFunction = batchCallFunction;
	}
	public String getFuncCode() {
		return funcCode;
	}
	public void setFuncCode(String funcCode) {
		this.funcCode = funcCode;
	}
	public Boolean getLogin() {
		return login;
	}
	public void setLogin(Boolean login) {
		this.login = login;
	}
	public String getExcludeSessionIds() {
		return excludeSessionIds;
	}
	public void setExcludeSessionIds(String excludeSessionIds) {
		this.excludeSessionIds = excludeSessionIds;
	}

	public JSONObject getWsConfig() {
		return wsConfig;
	}

	public void setWsConfig(JSONObject wsConfig) {
		this.wsConfig = wsConfig;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
}
