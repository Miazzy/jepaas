/**
 * 
 */
package com.je.develop.service;

import net.sf.json.JSONObject;

/**
 * @author chenmeng
 * 2012-5-3 下午3:12:10
 */
public interface CodeGenManager {
	/**
	 * 为编号执行周期归0操作
	 */
	public abstract void cleanCodeGenerator();
	/**
	 * 根据编号类型归0操作
	 * @param emptyType
	 */
	public abstract void cleanCodeValueByType(String emptyType);
	/**
	 *
	 * @param funcId
	 * @param fieldName
	 * @param codeBase
	 * @param step
	 * @param cycle
	 * @return
	 */
	/**
	 * 获取流水号
	 * @param infos TODO未处理
	 * @param fieldName TODO未处理
	 * @param codeBase TODO未处理
	 * @param step TODO未处理
	 * @param cycle TODO未处理
	 * @param length 长度
	 * @return
	 */
	public String getSeq(JSONObject infos, String fieldName, String codeBase, String step, String cycle, Integer length);
	/**
	 *
	 * @param funcId
	 * @param fieldName
	 * @param codeBase
	 * @param step
	 * @param cycle
	 * @param length
	 * @param jtgsId
	 * @return
	 */
	/**
	 * 获取流水号
	 * @param infos TODO未处理
	 * @param fieldName 文件名称
	 * @param codeBase TODO未处理
	 * @param step TODO未处理
	 * @param cycle TODO未处理
	 * @param length 长度
	 * @param jtgsId TODO未处理
	 * @return
	 */
	public String getSeq(JSONObject infos, String fieldName, String codeBase, String step, String cycle, Integer length, String jtgsId);

}