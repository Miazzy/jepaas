package com.je.core.dialect;

import org.hibernate.dialect.Oracle10gDialect;

public class PCOracleDialect extends Oracle10gDialect {
	public  PCOracleDialect() {
	  super();
//	  registerHibernateType(Types.CLOB,Hibernate.TEXT.getName());
//	  registerHibernateType(Types.BLOB,Hibernate.TEXT.getName());
 }
}
