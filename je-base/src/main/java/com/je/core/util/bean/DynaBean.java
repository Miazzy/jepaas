package com.je.core.util.bean;

import com.je.core.util.ArrayUtils;
import com.je.core.util.StringUtil;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

/**
 * 自定义动态类
 *
 * 手工设定属性的内容，系统保留以下属性：
 * $TABLE_CODE$ :   表的名称
 * $ROWS$       :   结果记录集
 * $P_COUNT$      :   当前返回记录数
 * $A_COUNT$  :   总的查询记录数
 * $SQL$        :   查询的SQL语句
 * .....  (所有参数信息见@see BeanUtils.java)
 */
public class DynaBean implements Serializable {
    /** 序列号  */
    private static final long serialVersionUID = -2563215753689331432L;
    @SuppressWarnings("unused")
    private static Logger logger = LoggerFactory.getLogger(DynaBean.class);
    /** 存放属性的值集 */
    @SuppressWarnings("rawtypes")
    private HashMap values = new HashMap();

    /**
     * 初始化空的动态类
     */
    public DynaBean() {
        this.set(BeanUtils.KEY_WHERE, "");
    }

    /**
     * 初始化带表编码信息的动态类
     * 不被推荐的方法
     * @param tableCode 表的编码（即数据库表名）
     */
    @SuppressWarnings("unchecked")
    @Deprecated
    public DynaBean(String tableCode) {
        values.put(BeanUtils.KEY_TABLE_CODE, tableCode);
        //初始化主键字段
        String pkName =  BeanUtils.getInstance().getPKeyFieldNames(tableCode);
        if(pkName != null){
            this.set(BeanUtils.KEY_PK_CODE , pkName);
        }
        this.set(BeanUtils.KEY_WHERE, "");
    }
    /**
     * 初始化带表编码信息的动态类(是否装载组建)
     * @param tableCode 表的编码（即数据库表名）
     * @param isHavePK 是否自动生成主键的名称
     */
    @SuppressWarnings("unchecked")
    public DynaBean(String tableCode,Boolean isHavePK){
        values.put(BeanUtils.KEY_TABLE_CODE, tableCode);
        if(isHavePK){
            String pkName =  BeanUtils.getInstance().getPKeyFieldNames(tableCode);
            if(pkName != null){
                this.set(BeanUtils.KEY_PK_CODE , pkName);
            }
        }
        this.set(BeanUtils.KEY_WHERE, "");
    }
    /**
     * 研发部:云凤程
     * 初始化带表编码信息的动态类
     * @param tableCode 表的编码（即数据库表名）
     * @param isHavePK 是否自动生成主键的名称
     * @param all 是否装载字段数据
     */
    @SuppressWarnings("unchecked")
    public DynaBean(String tableCode,Boolean isHavePK,Boolean all){
        values.put(BeanUtils.KEY_TABLE_CODE, tableCode);
        if(isHavePK){
            String pkName =  BeanUtils.getInstance().getPKeyFieldNames(tableCode);
            if(pkName != null){
                this.set(BeanUtils.KEY_PK_CODE , pkName);
            }
        }
        if(all){
            List<DynaBean> columns = (List<DynaBean>) BeanUtils.getInstance().getResourceTable(tableCode).get(BeanUtils.KEY_TABLE_COLUMNS);
            for(DynaBean c : columns){
                this.set(c.getStr("TABLECOLUMN_CODE") , "");
            }
        }
        this.set(BeanUtils.KEY_WHERE, "");
    }
    /**
     * 设置属性值，如果有则直接覆盖，如果没有则添加一个
     *
     * @param key 属性名称
     * @param value 属性值
     */
    @SuppressWarnings("unchecked")
    public void set(String key, Object value) {
        if(StringUtil.isNotEmpty(key)) {
            if(BeanUtils.KEY_TABLE_CODE.equals(key)){
                values.put(key, value);
                //如果传入的是表CODE继续进行主键操作
                //如果已经有PK_CODE则不去查询
                if(!(this.get(BeanUtils.KEY_PK_CODE)!=null && !this.get(BeanUtils.KEY_PK_CODE).equals(""))){
                    String pkName =  BeanUtils.getInstance().getPKeyFieldNames(new DynaBean((String)value));
                    if(pkName != null){
                        this.set(BeanUtils.KEY_PK_CODE , pkName);
                    }
                }
            }else if(BeanUtils.KEY_WHERE.equals(key)){
                //对whereSql进行预处理
//                value=BeanUtils.getInstance().parseWhereSql((String)value);
                values.put(key, value);
            }else{
                values.put(key, value);
            }
        } else {
//    		logger.error("Null key for a Map not allowed in JSON!!!");
        }

    }

    /**
     * 设置String类型的属性值，如果有则直接覆盖，如果没有则添加一个
     *
     * @param key 属性名称
     * @param value 属性值
     */
    @SuppressWarnings("unchecked")
    public void setStr(String key, String value) {
        if(StringUtil.isNotEmpty(key)) {
            if(BeanUtils.KEY_TABLE_CODE.equals(key)){
                values.put(key, value);
                //如果传入的是表CODE继续进行主键操作
                //如果已经有PK_CODE则不去查询
                if(!(this.get(BeanUtils.KEY_PK_CODE)!=null && !this.get(BeanUtils.KEY_PK_CODE).equals(""))){
                    String pkName =  BeanUtils.getInstance().getPKeyFieldNames(new DynaBean((String)value));
                    if(pkName != null){
                        this.set(BeanUtils.KEY_PK_CODE , pkName);
                    }
                }
            }else if(BeanUtils.KEY_WHERE.equals(key)){
                //对whereSql进行预处理
//                value=BeanUtils.getInstance().parseWhereSql(value);
                values.put(key, value);
            }else{
                values.put(key, value);
            }
        } else {
//    		logger.error("Null key for a Map not allowed in JSON!!!");
        }
    }
    /**
     * 适用于枚举
     * 设置String类型的属性值，如果有则直接覆盖，如果没有则添加一个
     *
     * @param key 属性名称
     * @param value 属性值
     */
    public void setStr(Enum<?> key, String value) {
        setStr(key.toString(), value);
    }

    /**
     * 设置int类型的属性值，如果有则直接覆盖，如果没有则添加一个
     *
     * @param key 属性名称
     * @param value 属性值
     */
    @SuppressWarnings("unchecked")
    public void setInt(String key, int value) {
        if(StringUtil.isNotEmpty(key)) {
            values.put(key, String.valueOf(value));
        } else {
//    		logger.error("Null key for a Map not allowed in JSON!!!");
        }

    }

    /**
     * 设置long类型的属性值，如果有则直接覆盖，如果没有则添加一个
     *
     * @param key 属性名称
     * @param value 属性值
     */
    @SuppressWarnings("unchecked")
    public void setLong(String key, long value) {
        if(StringUtil.isNotEmpty(key)) {
            values.put(key, String.valueOf(value));
        } else {
//    		logger.error("Null key for a Map not allowed in JSON!!!");
        }

    }

    /**
     * 设置float类型的属性值，如果有则直接覆盖，如果没有则添加一个
     *
     * @param key 属性名称
     * @param value 属性值
     */
    @SuppressWarnings("unchecked")
    public void setFloat(String key, float value) {
        if(StringUtil.isNotEmpty(key)) {
            values.put(key, String.valueOf(value));
        } else {
//    		logger.error("Null key for a Map not allowed in JSON!!!");
        }

    }

    /**
     * 设置double类型的属性值，如果有则直接覆盖，如果没有则添加一个
     *
     * @param key 属性名称
     * @param value 属性值
     */
    @SuppressWarnings("unchecked")
    public void setDouble(String key, double value) {
        if(StringUtil.isNotEmpty(key)) {
            values.put(key, String.valueOf(value));
        } else {
//    		logger.error("Null key for a Map not allowed in JSON!!!");
        }

    }

    /**
     * 得到属性对应的值
     *
     * @param key 属性名称
     *
     * @return 对应的值
     */
    public Object get(String key) {
        return values.get(key);
    }

    /**
     * 得到属性对应的值
     *
     * @param key 属性名称
     * @param defValue  缺省值，如果没有就返回缺省值
     *
     * @return 对应的值
     */
    public Object get(String key, Object defValue) {
        Object value = get(key);
        if (value == null) {
            return defValue;
        } else {
            return value;
        }
    }

    /**
     * 直接得到字符串类型的返回值
     * @param key   属性名称
     * @return      字符串类型的返回值
     */
    public String getStr(String key) {
        return getStr(key, null);
    }
    /**
     * 直接得到字符串类型的返回值(参数key为枚举类型)
     * @param key   属性名称
     * @return      字符串类型的返回值
     */
    public String getStr(Enum<?> key) {
        return getStr(key.toString(), null);
    }
    /**
     * 直接得到字符串类型的返回值
     * @param key   属性名称
     * @param defValue  如果取不到就返回缺省值
     * @return      字符串类型的返回值
     */
    public String getStr(String key, String defValue) {
        Object value = values.get(key);
        if (value == null) {
            return defValue;
        } else {
            return value.toString();
        }
    }

    /**
     * 得到整型返回值
     * @param key   属性名称
     * @return      整型返回值
     */
    public int getInt(String key) {
        return getInt(key, 0);
    }

    /**
     * 得到整型返回值
     * @param key       属性名称
     * @param defValue  如果为空则返回缺省值
     * @return          整型返回值
     */
    public int getInt(String key, int defValue) {
        Object value = values.get(key);
        if (value == null || (String.valueOf(value)).length() == 0) {
            return defValue;
        } else {
            return Integer.parseInt((String.valueOf(value)));
        }
    }

    /**
     * 得到长整型返回值
     * @param key   属性名称
     * @return      整型返回值
     */
    public long getLong(String key) {
        return getLong(key , 0);
    }

    /**
     * 得到长整型返回值
     * @param key       属性名称
     * @param defValue  如果为空则返回缺省值
     * @return          整型返回值
     */
    public long getLong(String key, long defValue) {
        Object value = values.get(key);
        if (value == null || ((String) value).length() == 0) {
            return defValue;
        } else {
            return Long.parseLong((String) value);
        }
    }

    /**
     * 得到double型返回值
     * @param key   属性名称
     * @return      整型返回值
     */
    public double getDouble(String key) {
        return getDouble(key, 0);
    }

    /**
     * 得到double型返回值
     * @param key       属性名称
     * @param defValue  如果为空则返回缺省值
     * @return          整型返回值
     */
    public double getDouble(String key, double defValue) {
        Object value = values.get(key);
        if (value == null || String.valueOf(value).length() == 0) {
            return defValue;
        } else {
            return Double.parseDouble(String.valueOf(value));
        }
    }

    /**
     * 得到float型返回值
     * @param key   属性名称
     * @return      float返回值
     */
    public float getFloat(String key) {
        return getFloat(key, 0);
    }

    /**
     * float
     * @param key       属性名称
     * @param defValue  如果为空则返回缺省值
     * @return          float返回值
     */
    public float getFloat(String key, float defValue) {
        Object value = values.get(key);
        if (value == null || ((String) value).length() == 0) {
            return defValue;
        } else {
            return Float.parseFloat((String) value);
        }
    }

    /**
     * 设置值集
     *
     * @param valueMap 值集
     */
    @SuppressWarnings("rawtypes")
    public void setValues(HashMap valueMap) {
        this.values = valueMap;
    }
    /**
     * 根据表字段设置值
     * @param valueMap
     */
    public void setBeanValues(HashMap valueMap){
        //1.得到表结构
        String tabelCode = (String)values.get(BeanUtils.KEY_TABLE_CODE);
        DynaBean table = BeanUtils.getInstance().getResourceTable(tabelCode);
        //2.得到列模式
        List<DynaBean> columns = (List<DynaBean>) table.get(BeanUtils.KEY_TABLE_COLUMNS);
        for(DynaBean column : columns){
            String fieldCode = column.getStr("TABLECOLUMN_CODE");
            Object fieldValue = valueMap.get(fieldCode);
            if(fieldValue!=null){
                values.put(fieldCode, StringUtil.getDefaultValue(fieldValue,""));
            }
        }
    }
    /**
     * 根据表字段设置值
     * @param valueMap 值
     * @param excludes 排除字段
     */
    public void setBeanValues(HashMap valueMap,String[] excludes){
        //1.得到表结构
        String tabelCode = (String)values.get(BeanUtils.KEY_TABLE_CODE);
        DynaBean table = BeanUtils.getInstance().getResourceTable(tabelCode);
        //2.得到列模式
        List<DynaBean> columns = (List<DynaBean>) table.get(BeanUtils.KEY_TABLE_COLUMNS);
        for(DynaBean column : columns){
            String fieldCode = column.getStr("TABLECOLUMN_CODE");
            if(ArrayUtils.contains(excludes,fieldCode)){
                continue;
            }
            Object fieldValue = valueMap.get(fieldCode);
            if(fieldValue!=null){
                values.put(fieldCode, StringUtil.getDefaultValue(fieldValue,""));
            }
        }
    }
    /**
     * 根据表字段设置值
     * @param jsonObj
     */
    public void setJsonValues(JSONObject jsonObj){
        //1.得到表结构
        String tabelCode = (String)values.get(BeanUtils.KEY_TABLE_CODE);
        DynaBean table = BeanUtils.getInstance().getResourceTable(tabelCode);
        //2.得到列模式
        List<DynaBean> columns = (List<DynaBean>) table.get(BeanUtils.KEY_TABLE_COLUMNS);
        for(DynaBean column : columns){
            String fieldCode = column.getStr("TABLECOLUMN_CODE");
            Object fieldValue = jsonObj.get(fieldCode);
            if(fieldValue!=null){
                values.put(fieldCode, StringUtil.getDefaultValue(fieldValue,""));
            }
        }
    }
    /**
     * 得到值集
     * @return 值集
     */
    @SuppressWarnings("rawtypes")
    public HashMap getValues() {
        values.remove(BeanUtils.KEY_TABLE_CODE);
        values.remove(BeanUtils.KEY_PK_CODE);
        values.remove(BeanUtils.KEY_WHERE);
        values.remove(BeanUtils.DEF_ALL_FIELDS);
        values.remove(BeanUtils.KEY_QUERY_FIELDS);
        values.remove(BeanUtils.KEY_ORDER);
        return values;
    }
    /**
     * 得到值集
     * @return 值集
     */
    @SuppressWarnings("rawtypes")
    public HashMap fetchFilterValues() {
        HashMap hashmap = (HashMap) values.clone();
        hashmap.remove(BeanUtils.KEY_TABLE_CODE);
        hashmap.remove(BeanUtils.KEY_PK_CODE);
        hashmap.remove(BeanUtils.KEY_WHERE);
        hashmap.remove(BeanUtils.DEF_ALL_FIELDS);
        hashmap.remove(BeanUtils.KEY_QUERY_FIELDS);
        hashmap.remove(BeanUtils.KEY_ORDER);
        return hashmap;
    }
    /**
     *
     * @return
     */
    public HashMap fetchAllValues(){
        return values;
    }
    /**
     * 清除数据
     */
    public void clear() {
        this.values.clear();
    }
    /**
     * 覆盖父对象的clone方法，复制出一份内容完全一样的新对象。
     * @return  内容完全一样的新对象
     */
    @SuppressWarnings("rawtypes")
    public DynaBean clone() {
        DynaBean dynaBean = new DynaBean();
        dynaBean.setValues((HashMap) values.clone());
        return dynaBean;
    }

    /**
     * 快捷方法获取dynaBean的主键值，如无则返回null
     * @return
     */
    public String getPkValue() {
        String pkCode = (String)values.get(BeanUtils.KEY_PK_CODE);
        if(values.containsKey(pkCode)) {
            return (String)values.get(pkCode);
        } else {
            return null;
        }
    }
    /**
     * 删除指定属性
     * @param code
     */
    public void remove(String code){
        if(values.containsKey(code)) {
            values.remove(code);
        }
    }
    /**
     * 是否包含键
     * @param code
     * @return
     */
    public Boolean containsKey(String code){
        if(values.containsKey(code)) {
            return true;
        }else{
            return false;
        }
    }
}
