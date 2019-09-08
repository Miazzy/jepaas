package com.je.core.ann.entity;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 实体类中  属性为实体时标识此注解  可将此属性导入
 * value ： 属性映射到数据库中的ID,原则上应该等同于@JoinColumn(name="PARENT")中的name值
 * descr ： 中文描述
 * @author sunwanxiang
 * @date 2012-3-27 下午02:09:58
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface JoinColumnName {
	public String value() default "";   //外键     映射到数据库中的ID
	public String descr() default "";   //中文描述
}
