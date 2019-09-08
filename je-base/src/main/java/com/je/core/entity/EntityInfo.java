package com.je.core.entity;

import com.je.core.ann.entity.ViewEntity;
import com.je.core.util.ReflectionUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.Entity;

/**
 * 用于描述类结构信息
 * @author YUNFENGCHENG
 * 2011-8-30 下午04:08:34
 */
public final class EntityInfo {
	/**类ID的名字*/
	private String idName;
	/**类中的函数集合*/
	private Method[] methods;
	/**类中的属性集合*/
	private Field[] fields;
	/**类及父类中的基本属性集合*/
	private Field[] allBaseFields;
	/**类对应表名字*/
	private String tableName;
	/**实体名字*/
	private String entityName;
	/**类的注解集合*/
	private Annotation[] classAnns;
	/**类本身的类对象*/
	private Class<?> c;
	/**类中可以进行列表更新的字段*/
	private Field[] canListUpdateFields;
	/**判断实体是不是试图*/
	private Boolean isView;
	private static Logger logger = LoggerFactory.getLogger(EntityInfo.class);
	/**
	 * 验证类的基本信息是否正确
	 * YUNFENGCHENG
	 * 2011-8-31 上午10:47:36
	 */
	public Boolean checking(){
		if(this.isView == null){
			//1.如果是实体必须继承自BaseEntity或是TreeBaseEntity[试图实体除外]
			try {
				Object obj = c.newInstance();
				if(getEntityName() != null && !getIsView()){
					if(!(obj instanceof BaseEntity)){
						logger.error("类["+c.getSimpleName()+"]是已被ORM并且不是VIEW那面他必修继承自BaseEntity或是TreeBaseEntity");
						return false;
					}
				}
				//2.如果是实体是否字段必须用Boolean不能是原声类型
				if(getEntityName() != null){
					Field[] fields = getFields();
					List<Field> errorField = new ArrayList<Field>();
					for(Field field : fields){
						if((field.getType().getSimpleName()).equals(boolean.class.getSimpleName())){
							errorField.add(field);
						}
					}
					if(errorField.size() > 0){
						for(Field field : errorField){
							logger.error("类["+c.getSimpleName()+"]是已被ORM他的属性字段["+field.getName()+"]类型可以是Boolean绝对不能是boolean");	
						}
						return false;
					}
				}
				//4.如果是实体构建类的时候任何树形不能有默认值
				if(getEntityName() != null){
					List<String> defvalues = new ArrayList<String>();
					try {
						Field[] fields = getCanListUpdateFields();
						if(null != fields && 0 != fields.length)
						for (Field f : fields) {  
				            f.setAccessible(true);  
				            if(f.get(obj) != null){
				            	defvalues.add(f.getName()+" = "+obj);
				            }  
				        } 	
						if(defvalues.size() > 0){
							logger.error("类["+c.getSimpleName()+"]是已被ORM不能设置默认数值");
							for(String s : defvalues){
								logger.error(s);
							}
							return false;
						}
					} catch (Exception e) {
						logger.error("实例化类["+c.getSimpleName()+"]失败 "+e);
						return false;
					} 
				}
			
			
			
			} catch (InstantiationException e1) {
				e1.printStackTrace();
			} catch (IllegalAccessException e1) {
				e1.printStackTrace();
			}
			return true;
		}else{
			return this.isView;
		}
		
	}
	/**
	 * 类每部自己初始化相关信息
	 * 研发部: 云凤程
	 * 2011-8-31 下午12:26:26
	 */
	public void init(){
		getIsView();
		getCanListUpdateFields();
		getCanListUpdateFields();
		getEntityName();
		getTableName();
	}
	
	/**
	 * 判断实体是不是试图
	 * 研发部: 云凤程
	 * 2011-8-31 下午12:06:31
	 * @return
	 */
	public Boolean getIsView() {
		if(this.isView == null){
			this.isView = false;
			Annotation[] anns = getClassAnns();
			for(Annotation ann : anns){
				if(((String)ViewEntity.class.getSimpleName()).equals(ann.annotationType().getSimpleName())){
					return true;
				}
			}
			return this.isView;
		}else{
			return this.isView;
		}
	}

	public void setIsView(Boolean isView) {
		this.isView = isView;
	}

	/**
	 * 得到类中可以进行列表更新的字段
	 * 研发部:云凤程
	 * 2011-8-31 上午10:23:50
	 * @return
	 */
	public Field[] getCanListUpdateFields() {
		if(this.canListUpdateFields == null){
			List<Field> list = new ArrayList<Field>();
			Annotation[] anns = null;
			for(Field field : fields){
				anns = field.getAnnotations();
				for(Annotation ann : anns){
				}
			}
			if(list.size()>0){
				this.canListUpdateFields =  (Field[])list.toArray();
			}
			return this.canListUpdateFields;
		}else{
			return this.canListUpdateFields;
		}
	}
	public void setCanListUpdateFields(Field[] canListUpdateFields) {
		this.canListUpdateFields = canListUpdateFields;
	}
	/**
	 * 如果类是做了ORM的那返回类的className否则范虎NULL
	 * 研发部:云凤程
	 * 2011-8-31 上午10:12:51
	 * @return
	 */
	public String getEntityName() {
		if(this.entityName == null){
			Annotation[] annotations = getClassAnns();
			for(Annotation ann : annotations){
				if(((String)Entity.class.getSimpleName()).equals(ann.annotationType().getSimpleName())){
					String m = ((Entity)ann).name();
					if(!"".equals(m) && m != null){
						this.entityName = m;
					}
					break;
				}
			}
			return this.entityName;
		}else{
			return this.entityName;
		}
	}
	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}
	public Class<?> getC() {
		return c;
	}
	public void setC(Class<?> c) {
		this.c = c;
	}
	public Annotation[] getClassAnns() {
		return classAnns;
	}
	public void setClassAnns(Annotation[] classAnns) {
		this.classAnns = classAnns;
	}
	public String getIdName() {
		return idName;
	}
	public void setIdName(String idName) {
		this.idName = idName;
	}
	public Method[] getMethods() {
		return methods;
	}
	public void setMethods(Method[] methods) {
		this.methods = methods;
	}
	public Field[] getFields() {
		return fields;
	}
	public void setFields(Field[] fields) {
		this.fields = fields;
	}
	/**
	 * 如果类的注解Entity(name="tableName")那么返回tableName否则返回ClassName
	 * 研发部:云凤程
	 * 2011-8-31 上午09:55:42
	 * @return
	 */
	public String getTableName() {
		if(this.tableName == null){
			Annotation[] annotations = getClassAnns();
			if(getEntityName() != null){//首先判断是不是做了ORM到的实体如果是进一步操作如果不是则返回NULL
				this.tableName = c.getSimpleName();
				for(Annotation ann : annotations){
					if("Entity".equals(ann.annotationType().getSimpleName())){
						String m = ((Entity)ann).name();
						if(!"".equals(m) && m != null){
							this.tableName = m;
						}
						break;
					}
				}
				return this.tableName;
			}else{
				return null;
			}
		}else{
			return this.tableName;
		}
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	/**
	 * 获得某一实体对象及其父类的全部合法字段的"名称/类型"(for ext-JS)
	 * 1.枚举类型
	 * 2.多维信息
	 * 3.double/float问题
	 * chenmeng
	 * 2011-12-28 下午03:30:28
	 * @return
	 */
	public Field[] getAllBaseFields() {
		ReflectionUtils r = ReflectionUtils.getInstance();
		Field[] fieldArray = r.getClassFields(c, false);
		List<Field> fieldList = new ArrayList<Field>();
		
		for(Field f : fieldArray) {
			if(isValidField(f)) {
				fieldList.add(f);
			}
		}
		allBaseFields = new Field[fieldList.size()];
		fieldList.toArray(allBaseFields);
		return allBaseFields;
	}
	public void setAllBaseFields(Field[] allBaseFields) {
		this.allBaseFields = allBaseFields;
	}
	/*
	 * 校验是否为合法字段
	 */
	private boolean isValidField(Field f) {
		if(f.getType().equals(String.class)) {
			return true;
		}
		if(f.getType().equals(Integer.class)) {
			return true;
		}
		if(f.getType().equals(int.class)) {
			return true;
		}
		if(f.getType().equals(Long.class)) {
			return true;
		}
		if(f.getType().equals(long.class)) {
			return true;
		}
		if(f.getType().equals(BigDecimal.class)) {
			return true;
		}
		if(f.getType().equals(Float.class)) {
			return true;
		}
		if(f.getType().equals(float.class)) {
			return true;
		}
		if(f.getType().equals(Double.class)) {
			return true;
		}
		if(f.getType().equals(double.class)) {
			return true;
		}
		if(f.getType().equals(Boolean.class)) {
			return true;
		}
		if(f.getType().equals(boolean.class)) {
			return true;
		}
		if(f.getType().equals(Date.class)) {
			return true;
		}
		if(f.getType().equals(Timestamp.class)) {
			return true;
		}
		if(f.getType().equals(Map.class)) {
			return true;
		}
		return false;
	}
}
