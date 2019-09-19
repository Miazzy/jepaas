package com.je.wf.processVo;

import java.io.Serializable;

/**
 * 会签结果信息
 * @author zhangshuaipeng
 *
 */
public class CountersignTranInfo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -519931806952300938L;
	//结果
	private String result;
	//流转路径
	private String transition;
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public String getTransition() {
		return transition;
	}
	public void setTransition(String transition) {
		this.transition = transition;
	}
	
}
