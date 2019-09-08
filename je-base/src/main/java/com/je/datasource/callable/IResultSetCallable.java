package com.je.datasource.callable;

import java.sql.ResultSet;
import java.sql.SQLException;

/**   
* Copyright: Copyright (c) 2018 jeplus.cn
* @Description: 结果集回调统一接口
* @version: v1.0.0
* @author: LIULJ
* @date: 2018年4月16日 下午5:31:16 
*
* Modification History:
* Date         Author          Version            Description
*---------------------------------------------------------*
* 2018年4月16日     LIULJ           v1.0.0               初始创建
*
*
*/
public interface IResultSetCallable<T> {
	
    public T invoke(ResultSet rs) throws SQLException;
 
}
