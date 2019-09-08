/**
 * 
 */
package com.je.core.facade.extjs;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.je.core.constants.ConstantVars;
import com.je.core.exception.PlatformException;
import com.je.core.exception.PlatformExceptionEnum;
import com.je.core.sql.BuildingSql;
import com.je.core.util.StringUtil;
import com.je.core.util.bean.BeanUtils;
import com.je.core.util.bean.DynaBean;
import com.je.rbac.model.EndUser;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.StringWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * @author chenmeng
 * 2012-5-21 上午11:20:24
 */
public class JsonBuilder {
	private static Logger logger = LoggerFactory.getLogger(JsonBuilder.class);
	private static String datePattern = "yyyy-MM-dd HH:mm:ss"; 

	static {
		DateFormat df = new SimpleDateFormat(datePattern);

		JsonHolder.mapper.setDateFormat(df);
		JsonHolder.mapper.configure(SerializationFeature.WRITE_NULL_MAP_VALUES, true);
		JsonHolder.mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		JsonHolder.mapper.configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, false);
		JsonHolder.mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		JsonHolder.mapper.configure(com.fasterxml.jackson.core.JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
		JsonHolder.mapper.configure(com.fasterxml.jackson.core.JsonGenerator.Feature.QUOTE_FIELD_NAMES, true);
	}
	
	public static JsonBuilder getInstance() {
		return JsonHolder.JSON_BUILDER;
	}

	/**
	 * 内部静态类用于实例化本类
	 * 
	 * @author YUNFENCGHENG 2011-8-30 下午03:48:07
	 */
	private static class JsonHolder {
		private static final JsonBuilder JSON_BUILDER = new JsonBuilder();
		private static ObjectMapper mapper = new ObjectMapper();
		private static String EMPTY_JSON = "{}";
	}
	
	/**
	 * 为分页列表提供Json封装
	 * 
	 * @param count
	 *            记录总数
	 * @param entities
	 *            实体列表
	 * @param excludes
	 *            在json生成时需要排除的属性名称
	 * @listJson  true: {totalCount:总条数,rows:[数据]}  false: [数据]
	 * @return
	 */
	public String buildListPageJson(Long count, List<DynaBean> records, boolean listJson) {
		try {
			// 序列化配置项
			// MAP中的key如果对应的value为null则不参与json输出
			logger.debug("into buildPageJson...");
			StringBuffer pageJson = null;
			if (listJson) {
				pageJson = new StringBuffer("{\"totalCount\":" + count + ",\""
						+ ConstantVars.DEFAULT_RESULT_NAME + "\":");
			} else {
				pageJson = new StringBuffer("");
			}
			List<HashMap> dataList = new ArrayList<HashMap>(records.size());
			for(DynaBean bean : records) {
				HashMap values = bean.getValues();
				dataList.add(values);
			}
			StringWriter w = new StringWriter();
			JsonHolder.mapper.writeValue(w, dataList);
			pageJson.append(w);
			w.close();
			
			if (listJson) {
				pageJson.append("}");
			} else {
				pageJson.append("");
			}

			return pageJson.toString();
		} catch (Exception ex) {
			ex.printStackTrace();
			throw  new PlatformException("将查询对象构建分页信息报错", PlatformExceptionEnum.JE_CORE_JSONSTR_ERROR,new Object[]{count,records,listJson},ex);
		}
	}
	/**
	 * 构建dynaBean的values
	 * @param records
	 * @return
	 */
	public List<HashMap> buildDynaValues(List<DynaBean> records){
		List<HashMap> dataList = new ArrayList<HashMap>(records.size());
		for(DynaBean record:records){
			dataList.add(record.getValues());
		}
		return dataList;
	}
	/**
	 * 为分页列表提供Json封装
	 * 
	 * @param count
	 *            记录总数
	 * @param entities
	 *            实体列表
	 * @param excludes
	 *            在json生成时需要排除的属性名称
	 * @param listJson true: {totalCount:总条数,rows:[数据]}  false: [数据]
	 * @return
	 */
	public String buildTreePageJson(Long count, List<HashMap> records, boolean listJson) {
		try {
			// 序列化配置项
			// MAP中的key如果对应的value为null则不参与json输出
			logger.debug("into buildPageJson...");
			StringBuffer pageJson = null;
			if (listJson) {
				pageJson = new StringBuffer("{\"totalCount\":" + count + ",\""
						+ ConstantVars.TREE_RESULT_NAME + "\":");
			} else {
				pageJson = new StringBuffer("");
			}
			List<HashMap> dataList = new ArrayList<HashMap>(records.size());
			for(HashMap bean : records) {
				dataList.add(bean);
			}
			StringWriter w = new StringWriter();
			JsonHolder.mapper.writeValue(w, dataList);
			pageJson.append(w);
			w.close();
			
			if (listJson) {
				pageJson.append("}");
			} else {
				pageJson.append("");
			}
			return pageJson.toString();
		} catch (Exception ex) {
			ex.printStackTrace();
			throw  new PlatformException("将查询树形对象构建分页信息报错", PlatformExceptionEnum.JE_CORE_JSONSTR_ERROR,new Object[]{count,records,listJson},ex);
		}
	}
	public List<Map> jsonSqlToString(String jsonSql) {
		if(StringUtil.isNotEmpty(jsonSql)) {
			return (List<Map>) fromJson(jsonSql, ArrayList.class);
		} else {
			return null;
		}
	}
	/**
	 * 为树提供Json封装
	 * 
	 * @param count
	 *            记录总数
	 * @param entities
	 *            实体列表
	 * @param excludes
	 *            在json生成时需要排除的属性名称
	 * @return
	 */
	public String buildTreeListToJson(Long count, List<DynaBean> records, boolean listJson) {
		try {
			logger.debug("into buildPageJson...");
			StringBuffer pageJson = null;
			if (listJson) {
				pageJson = new StringBuffer("{\"totalCount\":" + count + ",\"children\":");
			} else {
				pageJson = new StringBuffer("");
			}
			List<HashMap> dataList = new ArrayList<HashMap>(records.size());
			for(DynaBean bean : records) {
				HashMap values = bean.getValues();
				dataList.add(values);
			}
			// 序列化配置项
			// MAP中的key如果对应的value为null则不参与json输出
//			logger.debug("into buildPageJson...");
			
			StringWriter w = new StringWriter();
			JsonHolder.mapper.writeValue(w, dataList);
			pageJson.append(w);
			w.close();
			
			if (listJson) {
				pageJson.append("}");
			} else {
				pageJson.append("");
			}

//			logger.debug("buildPageJson end, pageJson is : " + pageJson.toString());
			return pageJson.toString();
		} catch (Exception ex) {
			ex.printStackTrace();
			throw  new PlatformException("将查询树形对象构建分页信息报错", PlatformExceptionEnum.JE_CORE_JSONSTR_ERROR,new Object[]{count,records,listJson},ex);
		}
	}
	/**
	 * 为分页列表提供Json封装
	 * 
	 * @param count
	 *            记录总数
	 * @param entities
	 *            实体列表
	 * @param excludes
	 *            在json生成时需要排除的属性名称
	 * @return
	 */
	public String buildObjListToJson(Long count, Collection<? extends Object> records, boolean listJson) {
		try {
			logger.debug("into buildPageJson...");
			StringBuffer pageJson = null;
			if (listJson) {
				pageJson = new StringBuffer("{\"totalCount\":" + count + ",\""
						+ ConstantVars.DEFAULT_RESULT_NAME + "\":");
			} else {
				pageJson = new StringBuffer("");
			}
			// 序列化配置项
			// MAP中的key如果对应的value为null则不参与json输出
//			logger.debug("into buildPageJson...");
			
			StringWriter w = new StringWriter();
			JsonHolder.mapper.writeValue(w, records);
			pageJson.append(w);
			w.close();
			
			if (listJson) {
				pageJson.append("}");
			} else {
				pageJson.append("");
			}

//			logger.debug("buildPageJson end, pageJson is : " + pageJson.toString());
			return pageJson.toString();
		} catch (Exception ex) {
			ex.printStackTrace();
			throw  new PlatformException("将查询集合对象构建分页信息报错", PlatformExceptionEnum.JE_CORE_JSONSTR_ERROR,new Object[]{count,records,listJson},ex);
		}
	}
	
	/**
	 * 将一个dynaBean中的数据传为Json数据格式
	 * @param dynaBean
	 * @return
	 */
	public String toJson(DynaBean dynaBean) {
		try {
			if(null != dynaBean && null != dynaBean.getValues()) {
				return JsonHolder.mapper.writeValueAsString(dynaBean.getValues());
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			throw  new PlatformException("将DynaBean对象构建字符串信息报错", PlatformExceptionEnum.JE_CORE_JSONSTR_ERROR,new Object[]{dynaBean},ex);
		}
		return JsonHolder.EMPTY_JSON;
	}
	
	/**
	 * 将一个数据实体传为Json数据格式
	 * @param obj
	 * @return
	 */
	public String toJson(Object obj) {
		try {
			if(obj instanceof JSONObject || obj instanceof JSONArray){
				return obj.toString();
			}else{
				return JsonHolder.mapper.writeValueAsString(obj);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw  new PlatformException("将DynaBean对象构建字符串信息报错", PlatformExceptionEnum.JE_CORE_JSONSTR_ERROR,new Object[]{obj},e);
//			return ConstantVars.BLANK_STR;
		}
		
	}
	
	/**
	 * 将一个数据实体传为Json数据格式
	 * @param obj
	 * @return
	 */
	public String toNameQuoteJson(Object obj) {
		try {
			JsonHolder.mapper.configure(com.fasterxml.jackson.core.JsonGenerator.Feature.QUOTE_FIELD_NAMES, true);
			String json = JsonHolder.mapper.writeValueAsString(obj);
			JsonHolder.mapper.configure(com.fasterxml.jackson.core.JsonGenerator.Feature.QUOTE_FIELD_NAMES, false);
			return json;
		} catch (Exception e) {
			JsonHolder.mapper.configure(com.fasterxml.jackson.core.JsonGenerator.Feature.QUOTE_FIELD_NAMES, false);
			throw  new PlatformException("将一个数据实体传为Json数据格式报错", PlatformExceptionEnum.JE_CORE_JSONSTR_ERROR,new Object[]{obj},e);
//			return ConstantVars.BLANK_STR;
		}
		
	}
	
	/**
	 * 将一个Json字符串封装为指定类型对象
	 * @param json
	 * @param c
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Object fromJson(String json, Class c) {
		json = cleanJson(json);
		try {
			Object obj = JsonHolder.mapper.readValue(json, c);
			return obj;
		} catch (Exception e) {
			e.printStackTrace();
			throw  new PlatformException("字符串信息转换成实体对象报错", PlatformExceptionEnum.JE_CORE_JSONSTR_ERROR,new Object[]{json,c},e);
		}
	}
	
	/**
	 * 将一个JsonArray
	 * @param json
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<Map> fromJsonArray(String json) {
		json = cleanJson(json);
		List<Map> dataList = (List<Map>) fromJson(json, ArrayList.class);
		
		return dataList;
	}
	/**
	 * 将json字符串数组构建成指定的实体类的List集合
	 * @param json
	 * @param c
	 * @return
	 */
	public List<?> fromJsonArray(String json,Class<?> c){
		JSONArray array=JSONArray.fromObject(json);
		List<?> res=(List<?>) JSONArray.toCollection(array, c);
		return res;
	}
	/**
	 * 将json字符串转换成HashMap集合
	 * @param json
	 * @return
	 */
	public List<HashMap> toJsonArray(JSONArray array){
		List<HashMap> lists=new ArrayList<HashMap>();
		for(Integer i=0;i<array.size();i++){
			JSONObject objs=array.getJSONObject(i);
			HashMap values=new HashMap(objs);
			lists.add(values);
		}
		return lists;
	}
	/**
	 * 将json字符串转换成HashMap集合
	 * @param json
	 * @return
	 */
	public List<HashMap> toJsonArray(String json){
		return toJsonArray(JSONArray.fromObject(json));
	}
	/**
	 * @param srcList
	 * @param c
	 * @returnjd
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<Object> fromJsonArray(List<Map> srcList, Class c) {
		List desList = new ArrayList();
		for(Map map : srcList) {
			String string = toJson(map);
			Object obj = fromJson(string, c);
			desList.add(obj);
		}
		return desList;
	}
	
	/**
	 * 为操作成功返回Json {success : true, obj : obj}
	 * 
	 * @param obj
	 * @return
	 */
	public String returnSuccessJson(String strData) {
		StringBuffer returnJson = new StringBuffer("{\"success\" : true, \"obj\" : ");
		returnJson.append(strData);
		returnJson.append("}");
		return returnJson.toString();
	}
	
	/**
	 * 为操作失败返回Json {success : true, obj : obj}
	 * 
	 * @param obj
	 * @return
	 */
	public String returnFailureJson(String strData) {
		StringBuffer returnJson = new StringBuffer("{\"success\" : false,\"obj\" : ");
		returnJson.append(strData);
		returnJson.append("}");
		return returnJson.toString();
	}
	
	private String cleanJson(String json) {
		if(StringUtil.isNotEmpty(json)) {
			return json.replaceAll("\n", ConstantVars.BLANK_STR).trim();
		}
		return ConstantVars.BLANK_STR;
		
	}
	/**
	 * 构建列表修改的批量update SQL语句
	 * @param dynaBean
	 * @param strData
	 * @return
	 */
	public String[] jsonSqlToString(DynaBean dynaBean,String strData){
		String tableCode=dynaBean.getStr(BeanUtils.KEY_TABLE_CODE);
		String pkName=dynaBean.getStr(BeanUtils.KEY_PK_CODE);
		BuildingSql buildingSql=BuildingSql.getInstance();
		if(StringUtil.isNotEmpty(strData) && StringUtil.isNotEmpty(tableCode)) {
			List<Map> sqlMapList = JsonBuilder.getInstance().fromJsonArray(strData);
			String[] updateSqls = new String[sqlMapList.size()];
			for(int i=0; i<sqlMapList.size(); i++) {
				Map sqlMap = sqlMapList.get(i);
				String pkValue=(String) sqlMap.get(pkName);
				if(StringUtil.isNotEmpty(pkValue)){
					String sql = buildingSql.getUpdateSql(tableCode, pkName, sqlMap);
					updateSqls[i] = sql;
				}else{
					updateSqls[i]="";
				}
			}
			return updateSqls;
		}else{
			return new String[]{};
		}
		
	}
	/**
	 * 构建列表更新的主键集合
	 * @param dynaBean
	 * @param strData
	 * @return
	 */
	public String[] jsonSqlToIdsStr(DynaBean dynaBean,String strData){
		String tableCode=dynaBean.getStr(BeanUtils.KEY_TABLE_CODE);
		String pkName=dynaBean.getStr(BeanUtils.KEY_PK_CODE);
		if(StringUtil.isNotEmpty(strData) && StringUtil.isNotEmpty(tableCode)) {
			List<Map> sqlMapList = JsonBuilder.getInstance().fromJsonArray(strData);
			String[] idsAarray = new String[sqlMapList.size()];
			for(int i=0; i<sqlMapList.size(); i++) {
				Map sqlMap = sqlMapList.get(i);
				idsAarray[i] = StringUtil.getDefaultValue(sqlMap.get(pkName), "");
			}
			return idsAarray;
		}else{
			return new String[]{};
		}
	}
}
