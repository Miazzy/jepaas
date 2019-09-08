package com.je.core.facade.extjs;

import com.je.core.constants.ConstantVars;
import com.je.core.constants.ExtFieldType;
import com.je.core.entity.extjs.Model;
import com.je.core.entity.extjs.ModelInfo;
import com.je.core.exception.PlatformException;
import com.je.core.exception.PlatformExceptionEnum;
import com.je.core.util.ArrayUtils;
import com.je.core.util.FormatDate4Json;
import com.je.core.util.StringUtil;
import com.je.core.util.bean.DynaBean;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.util.CycleDetectionStrategy;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 用于支持EXTJS的JSON辅助类
 * 
 * @author YUNFENGCHENG 2011-12-30 下午12:59:08
 */
public class JsonAssist {
	private static Logger logger = LoggerFactory.getLogger(JsonAssist.class);

	private JsonAssist() {

	}

	/**
	 * 实例化此类 研发部:云凤程 2011-8-30 下午03:26:33
	 * 
	 * @return
	 */
	public static JsonAssist getInstance() {
		return JsonAssistHolder.JSON_ASSIST;
	}

	/**
	 * 内部静态类用于实例化本类
	 * 
	 * @author YUNFENCGHENG 2011-8-30 下午03:48:07
	 */
	private static class JsonAssistHolder {
		private static final JsonAssist JSON_ASSIST = new JsonAssist();
		/**
		 * ModelJson 静态缓存
		 */
		private static Map<String, ModelInfo> modelJsons = new HashMap<String, ModelInfo>();
	}

	/**
	 * 把所有字段(excludes中的除外)拼成json格式 ：{name:'',type:''}
	 * 把VO实体模型转换成ExtJs model使用的模型数据
	 * @param fields
	 * @return
	 */
	public String getMoldeJsonByFields4Extjs(String modelName, Field[] fields,	String excludes) {
		List<Model> list = new ArrayList<Model>();
		// 已传入excludes
		if (StringUtils.isNotEmpty(excludes)) {
			for (Field f : fields) {
				String name = f.getName();
				String[] exs = excludes.split(ArrayUtils.SPLIT);
				for (int i = 0; i < exs.length; i++) {
					if (!exs[i].equals(name)) {
						Class<?> type = f.getType();
						String fieldType = type.getSimpleName().toLowerCase()
								.toString();
						if ("double".equals(fieldType)) {
							fieldType = ExtFieldType.FLOAT;
						}
						if ("long".equals(fieldType)) {
							fieldType = ExtFieldType.INT;
						}
						if ("bigdecimal".equals(fieldType)) {
							fieldType = ExtFieldType.INT;
						}
						if ("timestamp".equals(fieldType)) {
							fieldType = ExtFieldType.STRING;
						}
						if ("date".equals(fieldType)) {
							fieldType = ExtFieldType.STRING;
						}
						if ("boolean".equalsIgnoreCase(fieldType)) {
							fieldType = "boolean";
						}
						if ("integer".equals(fieldType)) {
							fieldType = ExtFieldType.INT;
						}
						if ("map".equals(fieldType)) {
							fieldType = ExtFieldType.AUTO;
						}	
						Model model = new Model(name, fieldType);
						list.add(model);
					}
				}
			}
		} else { // 未传入excludes
			for (Field f : fields) {
				String name = f.getName();
				Class<?> type = f.getType();
				String fieldType = type.getSimpleName().toLowerCase().toString();

				if ("double".equals(fieldType)) {
					fieldType = ExtFieldType.FLOAT;
				}
				if ("long".equals(fieldType)) {
					fieldType = ExtFieldType.INT;
				}
				if ("bigdecimal".equals(fieldType)) {
					fieldType = ExtFieldType.INT;
				}
				if ("timestamp".equals(fieldType)) {
					fieldType = ExtFieldType.STRING;
				}
				if ("date".equals(fieldType)) {
					fieldType = ExtFieldType.STRING;
				}
				if ("integer".equals(fieldType)) {
					fieldType = ExtFieldType.INT;
				}				
				if ("map".equals(fieldType)) {
					fieldType = ExtFieldType.AUTO;
				}				
				Model model = new Model(name, fieldType);
				list.add(model);
			}
		}
		JSONArray ja = JSONArray.fromObject(list);
		ModelInfo modelInfo = new ModelInfo();
		modelInfo.setModels(ja.toString());
		modelInfo.setExcludes(excludes);
		JsonAssistHolder.modelJsons.put(modelName, modelInfo);
		return ja.toString();
	}
	/**
	 * 把所有字段(excludes中的除外)拼成json格式 ：{name:'',type:''}
	 * 
	 * @param fields
	 * @return
	 */
	public List<Model> getMoldeListByFields4Extjs(String modelName, Field[] fields,	String excludes) {
		List<Model> list = new ArrayList<Model>();
		// 已传入excludes
		if (StringUtils.isNotEmpty(excludes)) {
			for (Field f : fields) {
				String name = f.getName();
				String[] exs = excludes.split(ArrayUtils.SPLIT);
				for (int i = 0; i < exs.length; i++) {
					if (!exs[i].equals(name)) {
						Class<?> type = f.getType();
						String fieldType = type.getSimpleName().toLowerCase()
								.toString();
						if ("double".equals(fieldType)) {
							fieldType = ExtFieldType.FLOAT;
						}
						if ("long".equals(fieldType)) {
							fieldType = ExtFieldType.INT;
						}
						if ("bigdecimal".equals(fieldType)) {
							fieldType = ExtFieldType.INT;
						}
						if ("timestamp".equals(fieldType)) {
							fieldType = ExtFieldType.STRING;
						}
						if ("date".equals(fieldType)) {
							fieldType = ExtFieldType.STRING;
						}
						if ("boolean".equalsIgnoreCase(fieldType)) {
							fieldType = "boolean";
						}
						if ("integer".equals(fieldType)) {
							fieldType = ExtFieldType.INT;
						}
						if ("map".equals(fieldType)) {
							fieldType = ExtFieldType.AUTO;
						}	
						Model model = new Model(name, fieldType);
						list.add(model);
					}
				}
			}
		} else { // 未传入excludes
			for (Field f : fields) {
				String name = f.getName();
				Class<?> type = f.getType();
				String fieldType = type.getSimpleName().toLowerCase().toString();

				if ("double".equals(fieldType)) {
					fieldType = ExtFieldType.FLOAT;
				}
				if ("long".equals(fieldType)) {
					fieldType = ExtFieldType.INT;
				}
				if ("bigdecimal".equals(fieldType)) {
					fieldType = ExtFieldType.INT;
				}
				if ("timestamp".equals(fieldType)) {
					fieldType = ExtFieldType.STRING;
				}
				if ("date".equals(fieldType)) {
					fieldType = ExtFieldType.STRING;
				}
				if ("integer".equals(fieldType)) {
					fieldType = ExtFieldType.INT;
				}				
				if ("map".equals(fieldType)) {
					fieldType = ExtFieldType.AUTO;
				}				
				Model model = new Model(name, fieldType);
				list.add(model);
			}
		}
		return list;
	}
	public String getMoldeJsonByFields4Extjs(String modelName, List<DynaBean> fields,	String excludes) {
		List<Model> list = new ArrayList<Model>();
		// 已传入excludes
		if (StringUtils.isNotEmpty(excludes)) {
			for (DynaBean f : fields) {
				String name = f.getStr("TABLECOLUMN_NAME");
				String[] exs = excludes.split(ArrayUtils.SPLIT);
				for (int i = 0; i < exs.length; i++) {
					if (!exs[i].equals(name)) {
						String fieldType = f.getStr("TABLECOLUMN_TYPE").toLowerCase().toString();
						if(StringUtil.isNotEmpty(fieldType)) {
							if(fieldType.startsWith("varchar")) {
								fieldType = ExtFieldType.STRING;
							}else if(fieldType.equals("id")) {
								fieldType = ExtFieldType.STRING;
							}else if(fieldType.startsWith("number")) {
								fieldType = ExtFieldType.INT;
							} else if(fieldType.equalsIgnoreCase("float") || fieldType.equalsIgnoreCase("double")) {
								fieldType = ExtFieldType.FLOAT;
							}else if(fieldType.equals("boolean")) {
								fieldType = ExtFieldType.BOOLEAN;
							}else if(fieldType.startsWith("varchar")) {
								fieldType = ExtFieldType.STRING;
							}else if(fieldType.startsWith("date")) {
								fieldType = ExtFieldType.STRING;
							}else if(fieldType.equals("id")) {
								fieldType = ExtFieldType.STRING;
							}else if(fieldType.equals("yesorno")) {
								fieldType = ExtFieldType.STRING;
							}else if(fieldType.equals("foreignkey")) {
								fieldType = ExtFieldType.STRING;
							}else if(fieldType.equals("clob")) {
								fieldType = ExtFieldType.STRING;
							}else if(fieldType.equals("bigclob")) {
								fieldType = ExtFieldType.STRING;
							}else if(fieldType.startsWith("number")) {
								fieldType = ExtFieldType.INT;
							}else if(fieldType.equals("float")) {
								fieldType = ExtFieldType.FLOAT;
							}else if(fieldType.equals("float2")) {
								fieldType = ExtFieldType.FLOAT;
							}else if(fieldType.equals("boolean")) {
								fieldType = ExtFieldType.BOOLEAN;
							}
						}
						
						Model model = new Model(f.getStr("TABLECOLUMN_CODE"), fieldType);
						list.add(model);
					}
				}
			}
		} else { // 未传入excludes
			for (DynaBean f : fields) {
				String fieldType = f.getStr("TABLECOLUMN_TYPE").toLowerCase().toString();
				if(StringUtil.isNotEmpty(fieldType)) {
					if(fieldType.startsWith("varchar")) {
						fieldType = ExtFieldType.STRING;
					}else if(fieldType.equals("id")) {
						fieldType = ExtFieldType.STRING;
					}else if(fieldType.startsWith("number")) {
						fieldType = ExtFieldType.INT;
					} else if(fieldType.equalsIgnoreCase("float") || fieldType.equalsIgnoreCase("double")) {
						fieldType = ExtFieldType.FLOAT;
					}else if(fieldType.equals("boolean")) {
						fieldType = ExtFieldType.BOOLEAN;
					}else if(fieldType.startsWith("varchar")) {
						fieldType = ExtFieldType.STRING;
					}else if(fieldType.startsWith("date")) {
						fieldType = ExtFieldType.STRING;
					}else if(fieldType.equals("id")) {
						fieldType = ExtFieldType.STRING;
					}else if(fieldType.equals("yesorno")) {
						fieldType = ExtFieldType.STRING;
					}else if(fieldType.equals("foreignkey")) {
						fieldType = ExtFieldType.STRING;
					}else if(fieldType.equals("clob")) {
						fieldType = ExtFieldType.STRING;
					}else if(fieldType.equals("bigclob")) {
						fieldType = ExtFieldType.STRING;
					}else if(fieldType.startsWith("number")) {
						fieldType = ExtFieldType.INT;
					}else if(fieldType.equals("float")) {
						fieldType = ExtFieldType.FLOAT;
					}else if(fieldType.equals("float2")) {
						fieldType = ExtFieldType.FLOAT;
					}else if(fieldType.equals("boolean")) {
						fieldType = ExtFieldType.BOOLEAN;
					}
				}
				Model model = new Model(f.getStr("TABLECOLUMN_CODE"), fieldType);
				list.add(model);
			}
		}
		list.add(new Model("SY__MARK",ExtFieldType.STRING));
		JSONArray ja = JSONArray.fromObject(list);
//		ModelInfo modelInfo = new ModelInfo();
//		modelInfo.setModels(ja.toString());
//		modelInfo.setExcludes(excludes);
//		JsonAssistHolder.modelJsons.put(modelName, modelInfo);
		return ja.toString();
	}
	public List<Model> getMoldeListByFields4Extjs(String modelName, List<DynaBean> fields,	String excludes) {
		List<Model> list = new ArrayList<Model>();
		// 已传入excludes
		if (StringUtils.isNotEmpty(excludes)) {
			for (DynaBean f : fields) {
				String name = f.getStr("TABLECOLUMN_NAME");
				String[] exs = excludes.split(ArrayUtils.SPLIT);
				for (int i = 0; i < exs.length; i++) {
					if (!exs[i].equals(name)) {
						String fieldType = f.getStr("TABLECOLUMN_TYPE").toLowerCase().toString();
						if(StringUtil.isNotEmpty(fieldType)) {
							if(fieldType.startsWith("varchar")) {
								fieldType = ExtFieldType.STRING;
							}else if(fieldType.equals("id")) {
								fieldType = ExtFieldType.STRING;
							}else if(fieldType.startsWith("number")) {
								fieldType = ExtFieldType.INT;
							} else if(fieldType.equalsIgnoreCase("float") || fieldType.equalsIgnoreCase("double")) {
								fieldType = ExtFieldType.FLOAT;
							}else if(fieldType.equals("boolean")) {
								fieldType = ExtFieldType.BOOLEAN;
							}else if(fieldType.startsWith("varchar")) {
								fieldType = ExtFieldType.STRING;
							}else if(fieldType.startsWith("date")) {
								fieldType = ExtFieldType.STRING;
							}else if(fieldType.equals("id")) {
								fieldType = ExtFieldType.STRING;
							}else if(fieldType.equals("yesorno")) {
								fieldType = ExtFieldType.STRING;
							}else if(fieldType.equals("foreignkey")) {
								fieldType = ExtFieldType.STRING;
							}else if(fieldType.equals("clob")) {
								fieldType = ExtFieldType.STRING;
							}else if(fieldType.equals("bigclob")) {
								fieldType = ExtFieldType.STRING;
							}else if(fieldType.startsWith("number")) {
								fieldType = ExtFieldType.INT;
							}else if(fieldType.equals("float")) {
								fieldType = ExtFieldType.FLOAT;
							}else if(fieldType.equals("float2")) {
								fieldType = ExtFieldType.FLOAT;
							}else if(fieldType.equals("boolean")) {
								fieldType = ExtFieldType.BOOLEAN;
							}
						}
						
						Model model = new Model(f.getStr("TABLECOLUMN_CODE"), fieldType);
						list.add(model);
					}
				}
			}
		} else { // 未传入excludes
			for (DynaBean f : fields) {
				String fieldType = f.getStr("TABLECOLUMN_TYPE").toLowerCase().toString();
				if(StringUtil.isNotEmpty(fieldType)) {
					if(fieldType.startsWith("varchar")) {
						fieldType = ExtFieldType.STRING;
					}else if(fieldType.equals("id")) {
						fieldType = ExtFieldType.STRING;
					}else if(fieldType.startsWith("number")) {
						fieldType = ExtFieldType.INT;
					} else if(fieldType.equalsIgnoreCase("float") || fieldType.equalsIgnoreCase("double")) {
						fieldType = ExtFieldType.FLOAT;
					}else if(fieldType.equals("boolean")) {
						fieldType = ExtFieldType.BOOLEAN;
					}else if(fieldType.startsWith("varchar")) {
						fieldType = ExtFieldType.STRING;
					}else if(fieldType.startsWith("date")) {
						fieldType = ExtFieldType.STRING;
					}else if(fieldType.equals("id")) {
						fieldType = ExtFieldType.STRING;
					}else if(fieldType.equals("yesorno")) {
						fieldType = ExtFieldType.STRING;
					}else if(fieldType.equals("foreignkey")) {
						fieldType = ExtFieldType.STRING;
					}else if(fieldType.equals("clob")) {
						fieldType = ExtFieldType.STRING;
					}else if(fieldType.equals("bigclob")) {
						fieldType = ExtFieldType.STRING;
					}else if(fieldType.startsWith("number")) {
						fieldType = ExtFieldType.INT;
					}else if(fieldType.equals("float")) {
						fieldType = ExtFieldType.FLOAT;
					}else if(fieldType.equals("float2")) {
						fieldType = ExtFieldType.FLOAT;
					}else if(fieldType.equals("boolean")) {
						fieldType = ExtFieldType.BOOLEAN;
					}
				}
				
				Model model = new Model(f.getStr("TABLECOLUMN_CODE"), fieldType);
				list.add(model);
			}
		}
		list.add(new Model("SY__MARK",ExtFieldType.STRING));
		return list;
	}
	

	/**
	 * 把数据字典中的0,1转换成boolean类型
	 * YUNFENGCHENG
	 * @param value
	 * @return
	 */
	public boolean yesOrNo2boolean(String value){
		return ("1".equals(value) || "true".equalsIgnoreCase(value))?true:false;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public String buildSetPageJson(Long count, Set<?> entities, String[] excludes) {
		List list = new ArrayList();
		Iterator iterator = entities.iterator();
		while(iterator.hasNext()) {
			list.add(iterator.next());
		}
		return buildListPageJson(count, list, excludes, false);
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
	public String buildListPageJson(Long count, List<?> entities, String[] excludes, boolean listJson) {
		StringBuffer pageJson = null;
		if (listJson) {
			pageJson = new StringBuffer("{\"totalCount\":\"" + count + "\",\""
					+ ConstantVars.DEFAULT_RESULT_NAME + "\":[");
		} else {
			pageJson = new StringBuffer("[");
		}

		if (entities.size() != 0) {

			for (Object entity : entities) {
				JsonConfig cfg = FormatDate4Json.getCfgByYYYYMMDD();
				cfg.setCycleDetectionStrategy(CycleDetectionStrategy.LENIENT);
				if (null != excludes) {
					cfg.setExcludes(excludes);
				}
				JSONObject jsonObject = JSONObject.fromObject(entity, cfg);
				// 用逗号分隔
				pageJson.append(jsonObject).append(ArrayUtils.SPLIT);
			}
			if (pageJson.toString().endsWith(",")) {
				pageJson.deleteCharAt(pageJson.length() - 1);
			}
		}
		if (listJson) {
			pageJson.append("]}");
		} else {
			pageJson.append("]");
		}

		return pageJson.toString();
	}
	/**
	 * 构建分页Json信息
	 * @param count 条数
	 * @param entities 数据集合
	 * @param excludes 排除字段
	 * @return
	 */
	public String buildListPageJson(Long count, List<?> entities,String[] excludes) {
		return buildListPageJson(count, entities, excludes, true);
	}
	/**
	 * 构建集合分页Json数据
	 * @param count
	 * @param entities
	 * @return
	 */
	public String buildListPageJson(Long count, List<?> entities) {
		return buildListPageJson(count, entities, null);
	}

	/**
	 * 返回一个model的json数据
	 * @param model 对象实例
	 * @param excludes 排除字段
	 * @return
	 */
	public String buildModelJson(Object model, String[] excludes) {
		JsonConfig cfg = FormatDate4Json.getCfgByYYYYMMDD();
		if (null != excludes && 0 != excludes.length
				&& StringUtils.isNotEmpty(excludes[0])) {
			cfg.setExcludes(excludes);
		} else {
			String modelName = model.getClass().getName();
			ModelInfo modelInfo = JsonAssistHolder.modelJsons.get(modelName);
			if (null != modelInfo) {
				String cacheExcludes = modelInfo.getExcludes();
				if (StringUtil.isNotEmpty(cacheExcludes)) {
					cfg.setExcludes(cacheExcludes.split(ArrayUtils.SPLIT));
				}
			}
		}

		cfg.setCycleDetectionStrategy(CycleDetectionStrategy.LENIENT);
		JSONObject jsonObject = JSONObject.fromObject(model, cfg);
		return jsonObject.toString();
	}

	/**
	 * 返回一个model的json chenmeng 2011-12-31 下午01:42:19
	 * 
	 * @param model
	 * @return
	 */
	public String buildModelJson(Object model) {
		return buildModelJson(model, null);
	}

	/**
	 * 为操作成功返回Json {success : true, obj : obj}
	 * 
	 * @return
	 */
	public String buildReturnSuccessJson4Obj(String strData) {
		StringBuffer returnJson = new StringBuffer("{ \"success\" : true, \"obj\" : " + strData + "}");
		logger.debug("buildReturnSuccessJson end, returnJson4Obj is : "	+ returnJson.toString());
		return returnJson.toString();
	}
	
	/**
	 * 为操作失败返回Json {success : true, obj : obj}
	 * 
	 * @return
	 */
	public String buildReturnFailureJson4Obj(String strData) {
		StringBuffer returnJson = new StringBuffer("{ \"success\" : false, \"obj\" : " + strData + "}");
		return returnJson.toString();
	}

	/**
	 * 将jsonSqlString转化成一个封装若干SQL的json数组
	 * 
	 * @return
	 */
	public String[] jsonSqlToString(String jsonSql) {
		Object[] os = JSONArray.fromObject(jsonSql).toArray();
		String[] sqls = new String[os.length];
		for (int i = 0; i < os.length; i++) {
			JSONObject k = (JSONObject) os[i];
			String kk = (String) k.get("sql");
			sqls[i] = kk;
		}
		return sqls;
	}
	/**
	 * 将jsonSqlString转化成一个封装若干id的json数组
	 * 
	 * @return
	 */
	public String[] jsonSqlToIdStr(String jsonSql) {
		Object[] os = JSONArray.fromObject(jsonSql).toArray();
		String[] sqls = new String[os.length];
		for (int i = 0; i < os.length; i++) {
			JSONObject k = (JSONObject) os[i];
			String kk = (String) k.get("id");
			sqls[i] = kk;
		}
		return sqls;
	}

	/**
	 * 将JsonString封装为JsonArray chenmeng 2012-1-18 下午05:21:15
	 * 
	 * @param jsonStr
	 * @return
	 */
	public JSONArray stringToJsonArray(String jsonStr) {
		JsonConfig cfg = FormatDate4Json.getCfgByYYYYMMDD();
		JSONArray array = null;
		try {
//			JSONObject jsonObject = JSONObject.fromObject(jsonStr, cfg);
//			array = JSONArray.fromObject(jsonObject);
            array = JSONArray.fromObject(jsonStr, cfg);
		} catch (JSONException ex) {
			//ex.printStackTrace();
			throw  new PlatformException("字符串信息转换成Json数组对象报错", PlatformExceptionEnum.JE_CORE_JSONSTR_ERROR,new Object[]{jsonStr},ex);
		}
		return array;
	}
}
