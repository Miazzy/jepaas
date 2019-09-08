/**
 * 
 */
package com.je.core.util;

import java.util.List;

import com.je.core.constants.table.ColumnType;
import com.je.core.util.bean.BeanUtils;
import com.je.core.util.bean.DynaBean;

/**
 * @author chenmeng
 * 2012-5-17 下午2:01:38
 */
public class SqlUtil {
	public static final String IN = "in";
	public static final String NOT_IN = "not in";
	public static final String LIKE ="like";
	
	public static String escape(String fieldValue) {
		return fieldValue.replaceAll("'", "''");
	}
	/**
	 * 获取字符串过长的字段code
	 * @return
	 */
	public static String getStringLong(DynaBean dynaBean){
		DynaBean resourceTable=BeanUtils.getInstance().getResourceTable(dynaBean.getStr(BeanUtils.KEY_TABLE_CODE));
		List<DynaBean> columns=(List<DynaBean>) resourceTable.get(BeanUtils.KEY_TABLE_COLUMNS);
		String errors="";
		for(DynaBean column:columns){
			String type=column.getStr("TABLECOLUMN_TYPE");
			String code=column.getStr("TABLECOLUMN_CODE");
			//排除字段
			if(ColumnType.NUMBER.equals(type) || ColumnType.CLOB.equals(type)|| ColumnType.BIGCLOB.equals(type) || ColumnType.FLOAT.equals(type) || ColumnType.CUSTOM.equals(type)){
				continue;
			}
			Integer length=255;
			if(ColumnType.ID.equals(type) || ColumnType.FOREIGNKEY.equals(type) || ColumnType.DATETIME.equals(type)){
				length=50;
			}else if(ColumnType.VARCHAR100.equals(type)){
				length=100;
			}else if(ColumnType.VARCHAR50.equals(type)){
				length=50;
			}else if(ColumnType.VARCHAR30.equals(type)){
				length=30;
			}else if(ColumnType.VARCHAR1000.equals(type)){
				length=1000;
			}else if(ColumnType.VARCHAR4000.equals(type)){
				length=4000;
			}else if(ColumnType.VARCHAR.equals(type)){
				length=Integer.parseInt(column.getStr("TABLECOLUMN_LENGTH","0"));
			}else if(ColumnType.VARCHAR4000.equals(type)){
				length=4000;
			}else if(ColumnType.YESORNO.equals(type)){
				length=4;
			}
			if(dynaBean.containsKey(code)){
				String value=dynaBean.getStr(code);
				if(value.length()>length){
					errors=column.getStr("TABLECOLUMN_NAME")+"【"+column.getStr("TABLECOLUMN_CODE")+"】字段过长，请修改字段类型!";
					break;
				}
			}
		}
		return errors;
	}
}
