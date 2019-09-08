package com.je.core.util;

import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
/**
 * 格式化日期对象成JSON
 * @author thinkpad
 *
 */
public class FormatDate4Json {
	private static Logger logger = LoggerFactory.getLogger(FormatDate4Json.class);
	public static JsonConfig getCfgByYYYYMMDD(){
		JsonConfig cfg = new JsonConfig();
		cfg.registerJsonValueProcessor(Date.class,new JsonValueProcessor() {
			private final String format="yyyy-MM-dd";
			private final String timestampFormat = "yyyy-MM-dd HH:mm:ss";
			public Object processObjectValue(String key, Object value,JsonConfig jsonValue){
				if(value==null){
					return "";
				}
				if(value instanceof Date) {
					// 如果需要展示时间，则实体bean应定义为时间戳－陈盟 20110831
					if(value instanceof Timestamp) {
						String str = new SimpleDateFormat(timestampFormat).format((Timestamp) value);
						return str;
					}
					String str = new SimpleDateFormat(format).format((Date) value);
					return str;
				}

				return value.toString();
			}
			public Object processArrayValue(Object value, JsonConfig arg1){
				return value;
			}
		});
		return cfg;
	}
	public static String getStringByYYYYMMDD4EnglishDate(String data){
		if(data != null && data.length() == 28 && data.indexOf("CST") == 20){
			String year = data.substring(24, 28);
			String date = data.substring(8, 10);
			String temp = data.substring(4,7);
			if(temp.equals("Jan")){
				data = year + "-" + "01" + "-" + date;
			}
			if(temp.equals("Feb")){
				data = year + "-" + "02" + "-" + date;
			}
			if(temp.equals("Mar")){
				data = year + "-" + "03" + "-" + date;
			}
			if(temp.equals("Apr")){
				data = year + "-" + "04" + "-" + date;
			}
			if(temp.equals("May")){
				data = year + "-" + "05" + "-" + date;
			}
			if(temp.equals("Jun")){
				data = year + "-" + "06" + "-" + date;
			}
			if(temp.equals("Jul")){
				data = year + "-" + "07" + "-" + date;
			}
			if(temp.equals("Aug")){
				data = year + "-" + "08" + "-" + date;
			}
			if(temp.equals("Sep")){
				data = year + "-" + "09" + "-" + date;
			}
			if(temp.equals("Oct")){
				data = year + "-" + "10" + "-" + date;
			}
			if(temp.equals("Nov")){
				data = year + "-" + "11" + "-" + date;
			}
			if(temp.equals("Dec")){
				data = year + "-" + "12" + "-" + date;
			}
		}
		return data;
	}
	public static Date getDataByYYYYMMDD4EnglishDate(String data){
		SimpleDateFormat st = new SimpleDateFormat("yyyy-MM-dd");
		data = getStringByYYYYMMDD4EnglishDate(data);
		try {
			return st.parse(data);
		} catch (ParseException e) {
			logger.error("解析日期出错"+data+"出错",e);
			return null;
		}
	}
}






