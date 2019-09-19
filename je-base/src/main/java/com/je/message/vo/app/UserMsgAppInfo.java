package com.je.message.vo.app;

import net.sf.json.JSONObject;

/**
 * 手机APP通知
 */
public class UserMsgAppInfo {
    private String text;
    /**
     * 类型   FUNC  PLUGIN
     */
    private String type;
    /**
     * 功能编码
     */
    private String funcCode;
    /**
     * 动作
     */
    private String action;
    /**
     * 参数值
     */
    private JSONObject params=new JSONObject();
    public UserMsgAppInfo(String text,String type, String funcCode, String action, JSONObject params) {
        this.text=text;
        this.type = type;
        this.funcCode = funcCode;
        this.action = action;
        this.params = params;
    }
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFuncCode() {
        return funcCode;
    }

    public void setFuncCode(String funcCode) {
        this.funcCode = funcCode;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public JSONObject getParams() {
        return params;
    }

    public void setParams(JSONObject params) {
        this.params = params;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
