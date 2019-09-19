package com.je.wf.processVo;

import java.io.Serializable;

/**
 * 任务事件VO
 * @author zhangshuaipeng
 *
 */
public class EventInfo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3388368453735001248L;
	/**事件类型*/
	private String eventType;
	/**事件执行策略*/
	private String executeStratey;
	/**赋值字段配置*/
	private String fieldsConfig;
	/**执行业务Bean*/
	private String serviceBean;
	/**执行业务方法*/
	private String serviceMethod;
	/**执行业务方法参数编码*/
	private String methodParam;
	public String getEventType() {
		return eventType;
	}
	public void setEventType(String eventType) {
		this.eventType = eventType;
	}
	public String getExecuteStratey() {
		return executeStratey;
	}
	public void setExecuteStratey(String executeStratey) {
		this.executeStratey = executeStratey;
	}
	public String getFieldsConfig() {
		return fieldsConfig;
	}
	public void setFieldsConfig(String fieldsConfig) {
		this.fieldsConfig = fieldsConfig;
	}
	public String getServiceBean() {
		return serviceBean;
	}
	public void setServiceBean(String serviceBean) {
		this.serviceBean = serviceBean;
	}
	public String getServiceMethod() {
		return serviceMethod;
	}
	public void setServiceMethod(String serviceMethod) {
		this.serviceMethod = serviceMethod;
	}
	public String getMethodParam() {
		return methodParam;
	}
	public void setMethodParam(String methodParam) {
		this.methodParam = methodParam;
	}
}
