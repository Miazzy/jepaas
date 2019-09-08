package com.je.core.dao;

import cn.hutool.db.Entity;
import cn.hutool.db.Page;
import cn.hutool.db.PageResult;
import cn.hutool.db.handler.RsHandler;
import cn.hutool.db.sql.Condition;
import cn.hutool.db.sql.Query;
import org.hibernate.SessionFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

/**
 * 平台基于连接PrepareStatement的DAO层定义
 *
 * @ProjectName: je-saas-platform
 * @Package: com.je.core.dao
 * @ClassName: PStatementTemplate
 * @Description: 主要目的用于阶段性防止SQL注入，待后期优化平台SQL注入问题后，才实现要去除
 * @Author: LIULJ
 * @Version: 1.0
 * <p>Copyright: Copyright (c) 2019</p>
 */
public interface PStatementTemplate {

    /**
     * 查询
     *
     * @param sql 查询语句
     * @param params 参数
     * @return 结果对象
     * @throws SQLException SQL执行异常
     */
    List<Entity> query(String sql, Object... params) throws SQLException;

    /**
     * 查询
     * @param <T> 结果集需要处理的对象类型
     * @param sql 查询语句
     * @param beanClass 元素Bean类型
     * @param params 参数
     * @return 结果对象
     * @throws SQLException SQL执行异常
     */
    <T> List<T> query(String sql, Class<T> beanClass, Object... params) throws SQLException;

    /**
     * 查询单条记录
     *
     * @param sql 查询语句
     * @param params 参数
     * @return 结果对象
     * @throws SQLException SQL执行异常
     */
    Entity queryOne(String sql, Object... params) throws SQLException;

    /**
     * 查询单条单个字段记录,并将其转换为Number
     *
     * @param sql 查询语句
     * @param params 参数
     * @return 结果对象
     * @throws SQLException SQL执行异常
     */
    Number queryNumber(String sql, Object... params) throws SQLException;

    /**
     * 查询单条单个字段记录,并将其转换为String
     *
     * @param sql 查询语句
     * @param params 参数
     * @return 结果对象
     * @throws SQLException SQL执行异常
     */
    String queryString(String sql, Object... params) throws SQLException;

    /**
     * 查询
     *
     * @param <T> 结果集需要处理的对象类型
     * @param sql 查询语句
     * @param rsh 结果集处理对象
     * @param params 参数
     * @return 结果对象
     * @throws SQLException SQL执行异常
     */
    <T> T query(String sql, RsHandler<T> rsh, Object... params) throws SQLException;

    /**
     * 执行非查询语句<br>
     * 语句包括 插入、更新、删除
     *
     * @param sql SQL
     * @param params 参数
     * @return 影响行数
     * @throws SQLException SQL执行异常
     */
    int execute(String sql, Object... params) throws SQLException;

    /**
     * 执行非查询语句<br>
     * 语句包括 插入、更新、删除
     *
     * @param sql SQL
     * @param params 参数
     * @return 主键
     * @throws SQLException SQL执行异常
     */
    Long executeForGeneratedKey(String sql, Object... params) throws SQLException;

    /**
     * 批量执行非查询语句
     *
     * @param sql SQL
     * @param paramsBatch 批量的参数
     * @return 每个SQL执行影响的行数
     * @throws SQLException SQL执行异常
     */
    int[] executeBatch(String sql, Object[]... paramsBatch) throws SQLException;

    /**
     * 批量执行非查询语句
     *
     * @param sqls SQL列表
     * @return 每个SQL执行影响的行数
     * @throws SQLException SQL执行异常
     */
    int[] executeBatch(String... sqls) throws SQLException;

    /**
     * 插入数据
     *
     * @param record 记录
     * @return 插入行数
     * @throws SQLException SQL执行异常
     */
    int insert(Entity record) throws SQLException;

    /**
     * 批量插入数据<br>
     * 需要注意的是，批量插入每一条数据结构必须一致。批量插入数据时会获取第一条数据的字段结构，之后的数据会按照这个格式插入。<br>
     * 也就是说假如第一条数据只有2个字段，后边数据多于这两个字段的部分将被抛弃。
     *
     * @param records 记录列表
     * @return 插入行数
     * @throws SQLException SQL执行异常
     */
    int[] insert(Collection<Entity> records) throws SQLException;

    /**
     * 插入或更新数据<br>
     * 根据给定的字段名查询数据，如果存在则更新这些数据，否则执行插入
     *
     * @param record 记录
     * @param keys 需要检查唯一性的字段
     * @return 插入行数
     * @throws SQLException SQL执行异常
     */
    int insertOrUpdate(Entity record, String... keys) throws SQLException;

    /**
     * 插入数据
     *
     * @param record 记录
     * @return 主键列表
     * @throws SQLException SQL执行异常
     */
    List<Object> insertForGeneratedKeys(Entity record) throws SQLException;

    /**
     * 插入数据
     *
     * @param record 记录
     * @return 主键
     * @throws SQLException SQL执行异常
     */
    Long insertForGeneratedKey(Entity record) throws SQLException;

    /**
     * 删除数据
     *
     * @param tableName 表名
     * @param field 字段名，最好是主键
     * @param value 值，值可以是列表或数组，被当作IN查询处理
     * @return 删除行数
     * @throws SQLException SQL执行异常
     */
    int del(String tableName, String field, Object value) throws SQLException;

    /**
     * 删除数据
     *
     * @param where 条件
     * @return 影响行数
     * @throws SQLException SQL执行异常
     */
    int del(Entity where) throws SQLException;

    /**
     * 更新数据<br>
     * 更新条件为多个key value对表示，默认key = value，如果使用其它条件可以使用：where.put("key", " &gt; 1")，value也可以传Condition对象，key被忽略
     *
     * @param record 记录
     * @param where 条件
     * @return 影响行数
     * @throws SQLException SQL执行异常
     */
    int update(Entity record, Entity where) throws SQLException;

    /**
     * 根据某个字段（最好是唯一字段）查询单个记录<br>
     * 当有多条返回时，只显示查询到的第一条
     *
     * @param <T> 字段值类型
     * @param tableName 表名
     * @param field 字段名
     * @param value 字段值
     * @return 记录
     * @throws SQLException SQL执行异常
     */
    <T> Entity get(String tableName, String field, T value) throws SQLException;

    /**
     * 根据条件实体查询单个记录，当有多条返回时，只显示查询到的第一条
     *
     * @param where 条件
     * @return 记录
     * @throws SQLException SQL执行异常
     */
    Entity get(Entity where) throws SQLException;

    /**
     * 查询<br>
     * 查询条件为多个key value对表示，默认key = value，如果使用其它条件可以使用：where.put("key", " &gt; 1")，value也可以传Condition对象，key被忽略
     *
     * @param <T> 需要处理成的结果对象类型
     * @param fields 返回的字段列表，null则返回所有字段
     * @param where 条件实体类（包含表名）
     * @param rsh 结果集处理对象
     * @return 结果对象
     * @throws SQLException SQL执行异常
     */
    <T> T find(Collection<String> fields, Entity where, RsHandler<T> rsh) throws SQLException;

    /**
     * 查询<br>
     * Query为查询所需数据的一个实体类，此对象中可以定义返回字段、查询条件，查询的表、分页等信息
     *
     * @param <T> 需要处理成的结果对象类型
     * @param query {@link Query}对象，此对象中可以定义返回字段、查询条件，查询的表、分页等信息
     * @param rsh 结果集处理对象
     * @return 结果对象
     * @throws SQLException SQL执行异常
     */
    <T> T find(Query query, RsHandler<T> rsh) throws SQLException;

    /**
     * 查询，返回所有字段<br>
     * 查询条件为多个key value对表示，默认key = value，如果使用其它条件可以使用：where.put("key", " &gt; 1")，value也可以传Condition对象，key被忽略
     *
     * @param <T> 需要处理成的结果对象类型
     * @param where 条件实体类（包含表名）
     * @param rsh 结果集处理对象
     * @param fields 字段列表，可变长参数如果无值表示查询全部字段
     * @return 结果对象
     * @throws SQLException SQL执行异常
     */
    <T> T find(Entity where, RsHandler<T> rsh, String... fields) throws SQLException;

    /**
     * 查询数据列表，返回字段由where参数指定<br>
     * 查询条件为多个key value对表示，默认key = value，如果使用其它条件可以使用：where.put("key", " &gt; 1")，value也可以传Condition对象，key被忽略
     *
     * @param where 条件实体类（包含表名）
     * @return 数据对象列表
     * @throws SQLException SQL执行异常
     */
    List<Entity> find(Entity where) throws SQLException;

    /**
     * 查询数据列表，返回字段由where参数指定<br>
     * 查询条件为多个key value对表示，默认key = value，如果使用其它条件可以使用：where.put("key", " &gt; 1")，value也可以传Condition对象，key被忽略
     *
     * @param <T> Bean类型
     * @param where 条件实体类（包含表名）
     * @return 数据对象列表
     * @throws SQLException SQL执行异常
     */
    <T> List<T> find(Entity where, Class<T> beanClass) throws SQLException;

    /**
     * 查询数据列表，返回所有字段<br>
     * 查询条件为多个key value对表示，默认key = value，如果使用其它条件可以使用：where.put("key", " &gt; 1")，value也可以传Condition对象，key被忽略
     *
     * @param where 条件实体类（包含表名）
     * @return 数据对象列表
     * @throws SQLException SQL执行异常
     */
    List<Entity> findAll(Entity where) throws SQLException;

    /**
     * 查询数据列表，返回所有字段<br>
     * 查询条件为多个key value对表示，默认key = value，如果使用其它条件可以使用：where.put("key", " &gt; 1")，value也可以传Condition对象，key被忽略
     *
     * @param <T> Bean类型
     * @param where 条件实体类（包含表名）
     * @return 数据对象列表
     * @throws SQLException SQL执行异常
     */
    <T> List<T> findAll(Entity where, Class<T> beanClass) throws SQLException;

    /**
     * 查询数据列表，返回所有字段
     *
     * @param tableName 表名
     * @return 数据对象列表
     * @throws SQLException SQL执行异常
     */
    List<Entity> findAll(String tableName) throws SQLException;

    /**
     * 根据某个字段名条件查询数据列表，返回所有字段
     *
     * @param tableName 表名
     * @param field 字段名
     * @param value 字段值
     * @return 数据对象列表
     * @throws SQLException SQL执行异常
     */
    List<Entity> findBy(String tableName, String field, Object value) throws SQLException;

    /**
     * 根据某个字段名条件查询数据列表，返回所有字段
     *
     * @param tableName 表名
     * @param wheres 字段名
     * @return 数据对象列表
     * @throws SQLException SQL执行异常
     */
    List<Entity> findBy(String tableName, Condition... wheres) throws SQLException;

    /**
     * 根据某个字段名条件查询数据列表，返回所有字段
     *
     * @param tableName 表名
     * @param field 字段名
     * @param value 字段值
     * @param likeType {@link Condition.LikeType}
     * @return 数据对象列表
     * @throws SQLException SQL执行异常
     */
    List<Entity> findLike(String tableName, String field, String value, Condition.LikeType likeType) throws SQLException;

    /**
     * 结果的条目数
     *
     * @param where 查询条件
     * @return 复合条件的结果数
     * @throws SQLException SQL执行异常
     */
    int count(Entity where) throws SQLException;

    /**
     * 分页查询<br>
     * 查询条件为多个key value对表示，默认key = value，如果使用其它条件可以使用：where.put("key", " &gt; 1")，value也可以传Condition对象，key被忽略
     *
     * @param <T> 结果对象类型
     * @param fields 返回的字段列表，null则返回所有字段
     * @param where 条件实体类（包含表名）
     * @param page 页码
     * @param numPerPage 每页条目数
     * @param rsh 结果集处理对象
     * @return 结果对象
     * @throws SQLException SQL执行异常
     */
    <T> T page(Collection<String> fields, Entity where, int page, int numPerPage, RsHandler<T> rsh) throws SQLException;

    /**
     * 分页查询<br>
     * 查询条件为多个key value对表示，默认key = value，如果使用其它条件可以使用：where.put("key", " &gt; 1")，value也可以传Condition对象，key被忽略
     *
     * @param <T> 结果对象类型
     * @param where 条件实体类（包含表名）
     * @param page 页码
     * @param numPerPage 每页条目数
     * @param rsh 结果集处理对象
     * @return 结果对象
     * @throws SQLException SQL执行异常
     */
    <T> T page(Entity where, int page, int numPerPage, RsHandler<T> rsh) throws SQLException;

    /**
     * 分页查询，结果为Entity列表，不计算总数<br>
     * 查询条件为多个key value对表示，默认key = value，如果使用其它条件可以使用：where.put("key", " &gt; 1")，value也可以传Condition对象，key被忽略
     *
     * @param where 条件实体类（包含表名）
     * @param page 页码
     * @param numPerPage 每页条目数
     * @return 结果对象
     * @throws SQLException SQL执行异常
     */
    List<Entity> pageForEntityList(Entity where, int page, int numPerPage) throws SQLException;

    /**
     * 分页查询，结果为Entity列表，不计算总数<br>
     * 查询条件为多个key value对表示，默认key = value，如果使用其它条件可以使用：where.put("key", " &gt; 1")，value也可以传Condition对象，key被忽略
     *
     * @param where 条件实体类（包含表名）
     * @param page 分页对象
     * @return 结果对象
     * @throws SQLException SQL执行异常
     */
    List<Entity> pageForEntityList(Entity where, Page page) throws SQLException;

    /**
     * 分页查询<br>
     * 查询条件为多个key value对表示，默认key = value，如果使用其它条件可以使用：where.put("key", " &gt; 1")，value也可以传Condition对象，key被忽略
     *
     * @param <T> 结果对象类型
     * @param where 条件实体类（包含表名）
     * @param page 分页对象
     * @param rsh 结果集处理对象
     * @return 结果对象
     * @throws SQLException SQL执行异常
     */
    <T> T page(Entity where, Page page, RsHandler<T> rsh) throws SQLException;

    /**
     * 分页查询<br>
     * 查询条件为多个key value对表示，默认key = value，如果使用其它条件可以使用：where.put("key", " &gt; 1")，value也可以传Condition对象，key被忽略
     *
     * @param <T> 结果对象类型
     * @param fields 返回的字段列表，null则返回所有字段
     * @param where 条件实体类（包含表名）
     * @param page 分页对象
     * @param rsh 结果集处理对象
     * @return 结果对象
     * @throws SQLException SQL执行异常
     */
    <T> T page(Collection<String> fields, Entity where, Page page, RsHandler<T> rsh) throws SQLException;

    /**
     * 分页查询<br>
     * 查询条件为多个key value对表示，默认key = value，如果使用其它条件可以使用：where.put("key", " &gt; 1")，value也可以传Condition对象，key被忽略
     *
     * @param fields 返回的字段列表，null则返回所有字段
     * @param where 条件实体类（包含表名）
     * @param page 分页对象
     * @param numPerPage 每页条目数
     * @return 结果对象
     * @throws SQLException SQL执行异常
     */
    PageResult<Entity> page(Collection<String> fields, Entity where, int page, int numPerPage) throws SQLException;

    /**
     * 分页查询<br>
     * 查询条件为多个key value对表示，默认key = value，如果使用其它条件可以使用：where.put("key", " &gt; 1")，value也可以传Condition对象，key被忽略
     *
     * @param fields 返回的字段列表，null则返回所有字段
     * @param where 条件实体类（包含表名）
     * @param page 分页对象
     * @return 结果对象
     * @throws SQLException SQL执行异常
     */
    PageResult<Entity> page(Collection<String> fields, Entity where, Page page) throws SQLException;

    /**
     * 分页查询<br>
     * 查询条件为多个key value对表示，默认key = value，如果使用其它条件可以使用：where.put("key", " &gt; 1")，value也可以传Condition对象，key被忽略
     *
     * @param where 条件实体类（包含表名）
     * @param page 页码
     * @param numPerPage 每页条目数
     * @return 分页结果集
     * @throws SQLException SQL执行异常
     */
    PageResult<Entity> page(Entity where, int page, int numPerPage) throws SQLException;

    /**
     * 分页查询<br>
     * 查询条件为多个key value对表示，默认key = value，如果使用其它条件可以使用：where.put("key", " &gt; 1")，value也可以传Condition对象，key被忽略
     *
     * @param where 条件实体类（包含表名）
     * @param page 分页对象
     * @return 分页结果集
     * @throws SQLException SQL执行异常
     */
    PageResult<Entity> page(Entity where, Page page) throws SQLException;


    /**
     * 获取Session工厂
     * @return
     */
    SessionFactory getSessionFactory();

    /**
     * 获得链接
     * @return {@link Connection}
     * @throws SQLException 连接获取异常
     */
    Connection getConnection() throws SQLException;
}
