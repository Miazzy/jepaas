package com.je.message.vo.app;

import net.sf.json.JSONObject;

/**
 * 设置推送自定义参数VO
 * 主要用于用户点开消息激活应用后触发的动作， 如  打开功能
 * @author zhangshuaipeng
 *
 */
public class PayloadInfo {
	
	/**类型    ExecuteType  FUNC || NO*/
	private String type="NO";
	/**手机功能编码*/
	private String app;
	/**参数*/
	private JSONObject params;
	/**配置参数*/
	private JSONObject _cfg;
	
	public PayloadInfo() {
		super();

	}
	public PayloadInfo(String type, String app, JSONObject params) {
		super();
		this.type = type;
		this.app = app;
		this.params = params;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getApp() {
		return app;
	}
	public void setApp(String app) {
		this.app = app;
	}
	public JSONObject getParams() {
		return params;
	}
	public void setParams(JSONObject params) {
		this.params = params;
	}
	public JSONObject get_cfg() {
		return _cfg;
	}
	public void set_cfg(JSONObject _cfg) {
		this._cfg = _cfg;
	}
	
}
