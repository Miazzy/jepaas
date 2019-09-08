package com.je.core.util;
/**
 * 超链接工具类
 * @author zhangshuaipeng
 *
 */
public class LinkUtils {
	/**
	 * 得到访问地址
	 * @return
	 */
	public static String getLocalAddress(){
		return WebUtils.getSysVar("JE_SYS_LOCALADDRESS");
	}
	/**
	 * 得到打开功能表单超链接
	 * @return
	 */
	public static String getOpenFuncForm(String funcCode,String pkValue,String linkName){
		return "<a href=\"javascript:void(0)\" onclick=\"JE.showFuncForm('"+funcCode+"','"+pkValue+"')\">"+linkName+"</a>";
	}
	/**
	 * 得到打开功能列表超链接
	 * @return
	 */
	public static String getOpenFuncGrid(String funcCode,String whereSql,String linkName){
		return "<a href=\"javascript:void(0)\" onclick=\"JE.showFunc('"+funcCode+"',{whereSql:'"+whereSql+"'});\">"+linkName+"</a>";
	}
}
