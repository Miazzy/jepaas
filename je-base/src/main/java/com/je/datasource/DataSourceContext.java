package com.je.datasource;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import com.je.core.util.SpringContextHolder;

/**
* Copyright: Copyright (c) 2018 jeplus.cn
* @Description: 数据源上下文对象
* @version: v1.0.0
* @author: LIULJ
* @date: 2018年4月16日 下午5:24:43 
*
* Modification History:
* Date         Author          Version            Description
*---------------------------------------------------------*
* 2018年4月16日     LIULJ           v1.0.0               初始创建
*
*
*/
public class DataSourceContext implements Serializable {
	
	private static final long serialVersionUID = -4689604766212153357L;
	private String dataSourceName;
	private DataSource dataSource;
	private Connection connection;
	
	public DataSourceContext(String dataSourceName) {
		this.dataSourceName = dataSourceName;
	}

	public Connection getConnection() throws SQLException {
		if(dataSource == null) {
			dataSource = SpringContextHolder.getBean(dataSourceName);
		}
		if(dataSource == null) {
			throw new RuntimeException("---------------------根据名称" + dataSourceName + "获取数据源失败---------------------");
		}
		if(connection == null || connection.isClosed()) {
			connection = dataSource.getConnection();
		}
		return connection;
	}
	
	public void close() throws SQLException {
		if(connection != null) {
			connection.close();
		}
	}
}
