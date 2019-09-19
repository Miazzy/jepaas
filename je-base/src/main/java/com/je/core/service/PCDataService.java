package com.je.core.service;

import java.util.List;

import com.je.core.ann.entity.DbFieldVo;
import com.je.core.entity.extjs.DbIndex;
import com.je.core.entity.extjs.DbModel;
import com.je.core.entity.extjs.Model;
/**
 * 加载表跟SQL模型及结构
 */
public interface PCDataService {
	/**
	 * 加载存储过程字段模型
	 * @param dataSource 数据来源
	 * @param procedureName 程序名称
	 * @param fieldVos TODO 暂不明确
	 * @return
	 */
	public List<Model> loadProcedure(String dataSource, String procedureName, List<DbFieldVo> fieldVos);

	/**
	 * 加载SQL字段模型
	 * @param dataSource 数据来源
	 * @param sql sql语句
	 * @param fieldVos TODO 暂不明确
	 * @return
	 */
	public List<Model> loadSql(String dataSource, String sql, List<DbFieldVo> fieldVos);
	/**
	 * 加载指定表名数据库字段模型
	 * @param tableCode 表code
	 * @return
	 */
	public List<DbModel> loadTableColumn(String tableCode);
	/**
	 * 加载指定表名数据库字段模型
	 * @param tableCode 表code
	 * @return
	 */
	public List<DbModel> loadTableColumnBySql(String tableCode);
	/**
	 * 查询出表中的索引
	 * @param tableCode 表code
	 * @return
	 */
	public List<DbIndex> loadTableIndex(String tableCode);
	/**
	 * 加载数据库所有索引名称(不支持MySQL)
	 * @return
	 */
	public List<String> loadDbIndex();
	/**
	 *TODO 暂不明确
	 * @param tableCode
	 * @return
	 */
	public Boolean existsTable(String tableCode);
	/**
	 * 构建存储过程占位参数
	 * @param params
	 * @return
	 */
	public String getCallParams(Object[] params);

	/**
	 * 构建前端使用的模型
	 * @param tableName 表明
	 * @param modelName TODO 暂不明确
	 * @param doTree TODO 暂不明确
	 * @param excludes TODO 暂不明确
	 * @return
	 */
	public String buildModel(String tableName, String modelName, Boolean doTree, String excludes);
}
