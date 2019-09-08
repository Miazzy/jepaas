package com.je.core.ann.entity;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标识实体类字段的中文名
 * 标有此标识的字段在子系统管理功能的列表中 按导入按钮时可导入
 * 注意：实体和集合 不要加此注解
 * @author sunwanxiang
 * 2012-2-20 下午06:11:15
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface FieldChineseName {
	public String value() default "";   //字段中文名
	public String width() default "";   //列宽
	public String xtype() default "";   //字段类型
	public String descr() default "";   //描述
	public String hidden() default "";   //是否隐藏  1为隐藏 0 为显示

}
