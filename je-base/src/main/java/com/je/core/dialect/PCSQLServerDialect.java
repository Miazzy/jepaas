package com.je.core.dialect;

import java.sql.Types;

import org.hibernate.Hibernate;
import org.hibernate.dialect.SQLServerDialect;
/**
 * 扩展hibernate的方言，将text类型加入到方言中
 * @author zhangshuaipeng
 *
 */
public class PCSQLServerDialect extends SQLServerDialect {
	public  PCSQLServerDialect() {
		  super();
		  registerHibernateType(Types.NVARCHAR,Hibernate.TEXT.getName());
		  registerHibernateType(Types.SMALLINT, Hibernate.BIG_INTEGER.getName());
		  registerHibernateType(Types.LONGVARCHAR, Hibernate.TEXT.getName());
   }
}
