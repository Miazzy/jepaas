package com.je.core.util;

import java.util.*;
import java.util.Map.Entry;

import com.je.core.util.bean.DynaBean;
import net.sf.json.JSONArray;

/**
 * 数组工具类
 */
public class ArrayUtils {
	
	/**
	 * 通用的字符串分隔符
	 */
	public static final String SPLIT = ",";
	
	public static final String DOT = ".";
	
	/**
	 * 拼接两个字数串数组为一个新数组
	 * 如果两个参数数组均为null，则返回null
	 * @param array1
	 * @param array2
	 * @return
	 */
	public static String[] concat(String[] array1, String[] array2) {
		if(null != array1 && null != array2) {
			int limit = array1.length + array2.length;
			String[] newArray = new String[limit];
			System.arraycopy(array1, 0, newArray, 0, array1.length);
			System.arraycopy(array2, 0, newArray, array1.length, array2.length);
			return newArray;
		} else if(null != array1 && null == array2) {
			return array1;
		} else if(null == array1 && null != array2) {
			return array2;
		} else {
			return null;
		}
	}
	
	/**
	 * 将一个形如：aaa,bbb,ccc,ddd的数组，转为形如'aaa','bbb','ccc','ddd'的字符串
	 * 如果ids为null或长度为0，则返回一个'NULL'
	 * @param ids
	 * @return
	 */
	public static String buildStringInSqlByArray(String[] ids) {
		StringBuffer idSql = new StringBuffer();
		if(null != ids && 0 != ids.length) {
			int i = 0;
			while(i<ids.length) {
				idSql.append("'");
				idSql.append(ids[i]);
				idSql.append("'");
				i++;
				if(i != ids.length) {
					idSql.append(ArrayUtils.SPLIT);
				}
			}
		} else {
			idSql.append("'NULL'");
		}
		return idSql.toString();
	}
	/**
	 * 查找数组是否包含
	 * @param array
	 * @param value
	 * @return
	 */
	public static Boolean contains(String[] array,String value){
		Boolean flag=false;
		if(StringUtil.isEmpty(value))return flag;
		for(String v:array){
			if(value.equalsIgnoreCase(v)){
				flag=true;
				break;
			}
		}
		return flag;
	}
	/**
	 * 查找value在数组的索引坐标
	 * @param array
	 * @param value
	 * @return
	 */
	public static Integer indexOf(String[] array,String value){
		Integer index=-1;
		for(Integer i =0;i<array.length;i++){
			if(value.equalsIgnoreCase(array[i])){
				index=i;
				break;
			}
		}
		return index;
	}
	/**
	 * 把集合转换成一个数组
	 * @param lists
	 * @return
	 */
	public static String[] getArray(Collection<String> lists){
		String[] arrays=new String[lists.size()];
		Integer i=0;
		for(String value:lists){
			arrays[i]=value;
			i++;
		}
		return arrays;
	}
	/**
	 * 把集合转换成一个数组
	 * @param lists
	 * @return
	 */
	public static String[] getArray4Json(JSONArray lists){
		String[] arrays=new String[lists.size()];
		Integer i=0;
		for(i=0;i<lists.size();i++){
			arrays[i]=lists.getString(i);
		}
		return arrays;
	}
	/**
	 * 把DynaBean集合中某字段值转换成数组
	 * @param lists
	 * @param fieldCode
	 * @return
	 */
	public static String[] getBeanFieldArray(Collection<DynaBean> lists,String fieldCode){
		String[] arrays=new String[lists.size()];
		Integer i=0;
		for(DynaBean bean:lists){
			arrays[i]=bean.getStr(fieldCode,"");
			i++;
		}
		return arrays;
	}
	/**
	 *
	 * 描述:array或list拼接
	 *
	 * @auther: wangmm@ketr.com.cn
	 * @date: 2019/3/6 15:26
	 */
	public static String join(String[] strs) {
		return join(strs, ArrayUtils.SPLIT);
	}
	public static String join(String[] strs,String split) {
		if(strs == null || strs.length == 0) return "";
		else return join(Arrays.asList(strs), split);
	}
	public static String join(List<String> strs) {
		return join(strs, ArrayUtils.SPLIT);
	}
	public static String join(List<String> strs, String split) {
		if(strs == null || strs.size() == 0) return "";
		StringBuffer idSql = new StringBuffer();
		for (String str : strs ) {
			idSql.append(str);
			idSql.append(split);
		}
		idSql.setLength(idSql.length()-1);
		return idSql.toString();
	}
}
