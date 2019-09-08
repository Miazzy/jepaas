package com.je.table.service;

import java.util.List;

import net.sf.json.JSONObject;

import com.je.core.util.bean.DynaBean;


/**
 * 数据库DDL统一操作
 * @author YUNFENGCHENG
 *
 */
public interface TableManager {
	/**
	 * 数据库创建操作
	 * @param resourceTableId TODO 暂不明确
	 * @return
	 */
	public boolean createTable(String resourceTableId);
	/**
	 * 删除表,和表在标管理功能下的数据
	 * @param ids
	 * @param isPhy 是否删除表结构
	 * @return
	 */
	public boolean removeTable(String ids, Boolean isPhy);

	/**
	 * 通过表名,把数据量中的表导入到资源表中管理
	 * @param table TODO 暂不明确
	 * @return
	 */
	public DynaBean impTable(DynaBean table);
	/**
	 * 同步表结构
	 * @param table TODO 暂不明确
	 * @return
	 */
	public DynaBean syncTable(DynaBean table);
	/**
	 * 通过视图名,把数据量中的表导入到资源表中管理
	 * @param table TODO 暂不明确
	 * @return
	 */
	public DynaBean impView(DynaBean table);
	/**
	 * 创建视图
	 * @param table TODO 暂不明确
	 * @return
	 */
	public DynaBean createView(DynaBean table);
	/**
	 * 持久视图信息
	 * @param table 表信息
	 * @param fields 字段
	 * @return
	 */
	public DynaBean saveViewInfo(DynaBean table, String fields);
	/**
	 * 修改视图
	 * @param table TODO 暂不明确
	 * @return
	 */
	public DynaBean updateView(DynaBean table);
	/**
	 * 获取视图的创建语句
	 * @param tableCode TODO 暂不明确
	 * @return
	 */
	public String getViewCreateSql(String tableCode);
	/**
	 * 持久视图信息
	 * @param table TODO 暂不明确
	 * @param fields TODO 暂不明确
	 * @param syncView 是否同步视图，是则不修改功能信息和键信息   只同步列信息
	 * @return
	 */
	public DynaBean updateViewInfo(DynaBean table, String fields, Boolean syncView);

	/**
	 * 更新表结构
	 * 1.表被创建后表的CODE不能被修改了
	 * 2.字段类型不能修改
	 * 3.字段删除操作会事前强硬的吧数据清空
	 * 4.字段的长度只能变大不能变小
	 * 5.字段和表的注解是可以改的
	 * 6.字段的编码是不可以该的
	 * 7.主外键关系只能添加不能删除和更新
	 * 如果有特殊的修改的话请自己到数据库中修改,但是一定要保证数据库与平台保
	 * @param resourceTableId TODO 暂不明确
	 * @param isFuncs TODO 暂不明确
	 * @return
	 */
	public boolean updateTable(String resourceTableId, Boolean isFuncs);

	/**
	 * 物理删除指定表的字段
	 * @param tableCode 暂不明确
	 * @param columns 暂不明确
	 * @param isDdl 暂不明确
	 */
	public void deleteColumn(String tableCode, List<DynaBean> columns, Boolean isDdl);

	/**
	 * 物理删除指定表的键
	 * @param tableCode 表名字
	 * @param keys TODO 暂不明确
	 * @param ddl 是否删除
	 */
	public void deleteKey(String tableCode, List<DynaBean> keys, String ddl);
	/**
	 * 物理删除指定表的索引
	 * @param tableCode 表名
	 * @param indexs 索引
	 */
	public void deleteIndex(String tableCode, List<DynaBean> indexs);
	/**
	 * 初始化修改信息
	 * @param resourceTable 修改的表信息
	 */
	public void initUpdateColumns(DynaBean resourceTable);
	/**
	 * 初始化审核信息
	 * @param resourceTable 修改表的信息
	 */
	public void initShColumns(DynaBean resourceTable);
	/**
	 * 初始化工作流信息
	 * @param resourceTable 修改表的信息
	 */
	public void initProcessColumns(DynaBean resourceTable);
	/**
	 * 初始化租户信息
	 * @param resourceTable 修改表的信息
	 */
	public void initSaasColumns(DynaBean resourceTable);
	/**
	 * 初始化拓展字段
	 * @param resourceTable 修改表的信息
	 */
	public void initExtendColumns(DynaBean resourceTable);
	/**
	 * 初始化表格列信息
	 * @param resourceTable 修改表的信息
	 * @param isTree 是否根节点
	 */
	public void initColumns(DynaBean resourceTable, Boolean isTree);
	/**
	 * 初始化表格键信息
	 * @param resourceTable 修改表的地方
	 * @param isTree 是否根节点
	 */
	public void initKeys(DynaBean resourceTable, Boolean isTree);
	/**
	 * 初始化表格键信息
	 * @param resourceTable 修改表的地方
	 * @param isTree 是否根节点
	 */
	public void initIndexs(DynaBean resourceTable, Boolean isTree);

	/**
	 * 生成创建语句SQL和数据插入SQL
	 * @param table 表信息
	 * @param type 表类型
	 * @return
	 */
	public String generateSql(DynaBean table, String type);
	/**
	 * 构建表数据的数据留痕
	 * @param tableCode  操作的表编码
	 * @param oldBean	 原实体
	 * @param newBean   新实体
	 * @param oper   	 操作
	 */
	public void saveTableTrace(String tableCode, DynaBean oldBean, DynaBean newBean, String oper, String tableId);

	/**
	 * 粘贴表操作
	 * @param newTableCode 被操作表
	 * @param dynaBean TODO 暂不明确
	 * @param useNewName 用户名字 (黏贴)
	 * @return
	 */
	public DynaBean pasteTable(String newTableCode, DynaBean dynaBean, String useNewName);

	/**
	 * 创建索引
	 * @param funcInfo 功能
	 * @param columnCode TODO 暂不明确
	 * @param columnId TODO 暂不明确
	 * @return
	 */
	public DynaBean createIndexByColumn(DynaBean funcInfo,DynaBean resourceTable, String columnCode, String columnId);

	/**
	 * 删除索引
	 * @param funcId 功能id
	 * @param columnCode TODO 暂不明确
	 * @param columnId TODO 暂不明确
	 * @return
	 */
	public String removeIndexByColumn(String funcId, String columnCode, String columnId);

	/**
	 * 对列进行检测。。 主要检测字段为空和字段重复
	 * @param columns TODO 暂不明确
	 * @param jeCore TODO 暂不明确
	 * @return
	 */
	public String checkColumns(List<DynaBean> columns, Boolean jeCore);
	/**
	 * 对键进行检测  主要检测键为空和键重复
	 * @param keys TODO 暂不明确
	 * @return
	 */
	public String checkKeys(List<DynaBean> keys);
	/**
	 * 对索引进行检测，主要检测索引重复和索引为空
	 * @param indexs 检索的索引
	 * @return
	 */
	public String checkIndexs(List<DynaBean> indexs);

	/**
	 * 表保存
	 * @param dynaBean TODO 暂不明确
	 * @return
	 */
	public DynaBean doSave(DynaBean dynaBean);

	/**
	 * 表修改
	 * @param dynaBean TODO 暂不明确
	 * @return
	 */
	public DynaBean doUpdate(DynaBean dynaBean);

	/**
	 * 复制表
	 * @param dynaBean TODO 暂不明确
	 * @return
	 */
	public DynaBean copyTable(DynaBean dynaBean);

	/**
	 * 表移动
	 * @param dynaBean TODO 暂不明确
	 * @param oldParentId 移动前表的id
	 * @return
	 */
	public DynaBean tableMove(DynaBean dynaBean, String oldParentId);
}