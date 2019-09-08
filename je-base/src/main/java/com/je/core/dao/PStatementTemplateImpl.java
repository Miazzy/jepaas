package com.je.core.dao;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.db.Entity;
import cn.hutool.db.Page;
import cn.hutool.db.PageResult;
import cn.hutool.db.SqlConnRunner;
import cn.hutool.db.handler.*;
import cn.hutool.db.sql.*;
import com.je.core.util.SpringContextHolder;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.annotation.AnnotationSessionFactoryBean;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

/**
 * 平台基于PrepareStatement的DAO层实现
 *
 * @ProjectName: je-saas-platform
 * @Package: com.je.core.dao
 * @ClassName: PStatementTemplateImpl
 * @Description: 平台基于Dao层的实现，防止SQL注入
 * @Author: LIULJ
 * @Version: 1.0
 * <p>Copyright: Copyright (c) 2019</p>
 */
@Component
public class PStatementTemplateImpl implements PStatementTemplate {

    /**
     * Hibernate模板类
     */
    private HibernateTemplate hibernateTemplate;

    /**
     * Session工厂
     */
    private SessionFactory sessionFactory;

    /**
     * SQL执行器
     */
    protected SqlConnRunner runner;

    /**
     * 获得链接
     * @return
     * @throws SQLException
     */
    @Override
    public Connection getConnection() throws SQLException{
        if(runner == null){
            synchronized (this) {
                if(runner == null){
                    AnnotationSessionFactoryBean propBean = SpringContextHolder.getBean(AnnotationSessionFactoryBean.class);
                    String dialect = propBean.getHibernateProperties().getProperty("hibernate.dialect");
                    Assert.notNull(dialect,"the driver class name can not be null!");
                    runner = new SqlConnRunner(dialect);
                }
            }
        }
        return getSession().connection();
    }

    /**
     * 查询
     * @param sql 查询语句
     * @param params 参数
     * @return
     * @throws SQLException
     */
    @Override
    public List<Entity> query(String sql, Object... params) throws SQLException {
        return query(sql, new EntityListHandler(), params);
    }

    /**
     * 查询
     * @param sql 查询语句
     * @param beanClass 元素Bean类型
     * @param params 参数
     * @param <T>
     * @return
     * @throws SQLException
     */
    @Override
    public <T> List<T> query(String sql, Class<T> beanClass, Object... params) throws SQLException {
        return query(sql, new BeanListHandler<T>(beanClass), params);
    }

    /**
     * 查询单条记录
     * @param sql 查询语句
     * @param params 参数
     * @return
     * @throws SQLException
     */
    @Override
    public Entity queryOne(String sql, Object... params) throws SQLException {
        return query(sql, new EntityHandler(), params);
    }

    /**
     *  查询单条单个字段记录,并将其转换为Number
     * @param sql 查询语句
     * @param params 参数
     * @return
     * @throws SQLException
     */
    @Override
    public Number queryNumber(String sql, Object... params) throws SQLException {
        return query(sql, new NumberHandler(), params);
    }

    /**
     * 查询单条单个字段记录,并将其转换为String
     * @param sql 查询语句
     * @param params 参数
     * @return
     * @throws SQLException
     */
    @Override
    public String queryString(String sql, Object... params) throws SQLException {
        return query(sql, new StringHandler(), params);
    }

    /**
     * 查询
     * @param sql 查询语句
     * @param rsh 结果集处理对象
     * @param params 参数
     * @param <T>
     * @return
     * @throws SQLException
     */
    @Override
    public <T> T query(String sql, RsHandler<T> rsh, Object... params) throws SQLException {
        Connection conn = this.getConnection();
        return SqlExecutor.query(conn, sql, rsh, params);
    }

    /**
     * 执行非查询语句<br>
     *  语句包括 插入、更新、删除
     * @param sql SQL
     * @param params 参数
     * @return
     * @throws SQLException
     */
    @Override
    public int execute(String sql, Object... params) throws SQLException {
        Connection conn = this.getConnection();
        return SqlExecutor.execute(conn, sql, params);
    }

    /**
     * 执行非查询语句<br>
     * 语句包括 插入、更新、删除
     * @param sql SQL
     * @param params 参数
     * @return
     * @throws SQLException
     */
    @Override
    public Long executeForGeneratedKey(String sql, Object... params) throws SQLException {
        Connection conn = this.getConnection();
        return SqlExecutor.executeForGeneratedKey(conn, sql, params);
    }

    /**
     * 批量执行非查询语句
     * @param sql SQL
     * @param paramsBatch 批量的参数
     * @return
     * @throws SQLException
     */
    @Override
    public int[] executeBatch(String sql, Object[]... paramsBatch) throws SQLException {
        Connection conn = this.getConnection();
        return SqlExecutor.executeBatch(conn, sql, paramsBatch);
    }

    /**
     * 批量执行非查询语句
     * @param sqls SQL列表
     * @return
     * @throws SQLException
     */
    @Override
    public int[] executeBatch(String... sqls) throws SQLException {
        Connection conn = this.getConnection();
        return SqlExecutor.executeBatch(conn, sqls);
    }

    /**
     * 插入数据
     * @param record 记录
     * @return
     * @throws SQLException
     */
    @Override
    public int insert(Entity record) throws SQLException {
        Connection conn = this.getConnection();
        return runner.insert(conn, record);
    }

    /**
     * 插入或更新数据<br>
     * 根据给定的字段名查询数据，如果存在则更新这些数据，否则执行插入
     * @param record 记录
     * @param keys 需要检查唯一性的字段
     * @return
     * @throws SQLException
     */
    @Override
    public int insertOrUpdate(Entity record, String... keys) throws SQLException {
        Connection conn = this.getConnection();
        return runner.insertOrUpdate(conn, record, keys);
    }

    /**
     * 批量插入数据<br>
     *  需要注意的是，批量插入每一条数据结构必须一致。批量插入数据时会获取第一条数据的字段结构，之后的数据会按照这个格式插入。<br>
     *  也就是说假如第一条数据只有2个字段，后边数据多于这两个字段的部分将被抛弃。
     * @param records 记录列表
     * @return
     * @throws SQLException
     */
    @Override
    public int[] insert(Collection<Entity> records) throws SQLException {
        Connection conn = this.getConnection();
        return runner.insert(conn, records);
    }

    /**
     * 插入数据
     * @param record 记录
     * @return
     * @throws SQLException
     */
    @Override
    public List<Object> insertForGeneratedKeys(Entity record) throws SQLException {
        Connection conn = this.getConnection();
        return runner.insertForGeneratedKeys(conn, record);
    }

    /**
     * 插入数据
     * @param record 记录
     * @return
     * @throws SQLException
     */
    @Override
    public Long insertForGeneratedKey(Entity record) throws SQLException {
        Connection conn = this.getConnection();
        return runner.insertForGeneratedKey(conn, record);
    }

    /**
     * 删除数据
     * @param tableName 表名
     * @param field 字段名，最好是主键
     * @param value 值，值可以是列表或数组，被当作IN查询处理
     * @return
     * @throws SQLException
     */
    @Override
    public int del(String tableName, String field, Object value) throws SQLException {
        return del(Entity.create(tableName).set(field, value));
    }

    /**
     * 删除数据
     * @param where 条件
     * @return
     * @throws SQLException
     */
    @Override
    public int del(Entity where) throws SQLException {
        Connection conn = this.getConnection();
        return runner.del(conn, where);
    }

    /**
     * 更新数据<br>
     *  更新条件为多个key value对表示，默认key = value，如果使用其它条件可以使用：where.put("key", " &gt; 1")，value也可以传Condition对象，key被忽略
     * @param record 记录
     * @param where 条件
     * @return
     * @throws SQLException
     */
    @Override
    public int update(Entity record, Entity where) throws SQLException {
        Connection conn = this.getConnection();
        return runner.update(conn, record, where);
    }

    /**
     * 根据某个字段（最好是唯一字段）查询单个记录<br>
     * 当有多条返回时，只显示查询到的第一条
     * @param tableName 表名
     * @param field 字段名
     * @param value 字段值
     * @param <T>
     * @return
     * @throws SQLException
     */
    @Override
    public <T> Entity get(String tableName, String field, T value) throws SQLException {
        return this.get(Entity.create(tableName).set(field, value));
    }

    /**
     * 根据条件实体查询单个记录，当有多条返回时，只显示查询到的第一条
     * @param where 条件
     * @return
     * @throws SQLException
     */
    @Override
    public Entity get(Entity where) throws SQLException {
        return find(where.getFieldNames(), where, new EntityHandler());
    }

    /**
     * 查询<br>
     * 查询条件为多个key value对表示，默认key = value，如果使用其它条件可以使用：where.put("key", " &gt; 1")，value也可以传Condition对象，key被忽略
     * @param fields 返回的字段列表，null则返回所有字段
     * @param where 条件实体类（包含表名）
     * @param rsh 结果集处理对象
     * @param <T>
     * @return
     * @throws SQLException
     */
    @Override
    public <T> T find(Collection<String> fields, Entity where, RsHandler<T> rsh) throws SQLException {
        Connection conn = this.getConnection();
        return runner.find(conn, fields, where, rsh);
    }

    /**
     * 查询<br>
     *   Query为查询所需数据的一个实体类，此对象中可以定义返回字段、查询条件，查询的表、分页等信息
     * @param query {@link Query}对象，此对象中可以定义返回字段、查询条件，查询的表、分页等信息
     * @param rsh 结果集处理对象
     * @param <T>
     * @return
     * @throws SQLException
     */
    @Override
    public <T> T find(Query query, RsHandler<T> rsh) throws SQLException {
        Connection conn = this.getConnection();
        return runner.find(conn, query, rsh);
    }

    /**
     * 查询，返回所有字段<br>
     *  查询条件为多个key value对表示，默认key = value，如果使用其它条件可以使用：where.put("key", " &gt; 1")，value也可以传Condition对象，key被忽略
     * @param where 条件实体类（包含表名）
     * @param rsh 结果集处理对象
     * @param fields 字段列表，可变长参数如果无值表示查询全部字段
     * @param <T>
     * @return
     * @throws SQLException
     */
    @Override
    public <T> T find(Entity where, RsHandler<T> rsh, String... fields) throws SQLException {
        return find(CollectionUtil.newArrayList(fields), where, rsh);
    }

    /**
     * 查询数据列表，返回字段由where参数指定<br>
     * 查询条件为多个key value对表示，默认key = value，如果使用其它条件可以使用：where.put("key", " &gt; 1")，value也可以传Condition对象，key被忽略
     * @param where 条件实体类（包含表名）
     * @return
     * @throws SQLException
     */
    @Override
    public List<Entity> find(Entity where) throws SQLException {
        return find(where.getFieldNames(), where, EntityListHandler.create());
    }

    /**
     * 查询数据列表，返回字段由where参数指定<br>
     * 查询条件为多个key value对表示，默认key = value，如果使用其它条件可以使用：where.put("key", " &gt; 1")，value也可以传Condition对象，key被忽略
     * @param where 条件实体类（包含表名）
     * @param beanClass
     * @param <T>
     * @return
     * @throws SQLException
     */
    @Override
    public <T> List<T> find(Entity where, Class<T> beanClass) throws SQLException {
        return find(where.getFieldNames(), where, BeanListHandler.create(beanClass));
    }

    /**
     * 查询数据列表，返回所有字段<br>
     * 查询条件为多个key value对表示，默认key = value，如果使用其它条件可以使用：where.put("key", " &gt; 1")，value也可以传Condition对象，key被忽略
     * @param where 条件实体类（包含表名）
     * @return
     * @throws SQLException
     */
    @Override
    public List<Entity> findAll(Entity where) throws SQLException {
        return find(where, EntityListHandler.create());
    }

    /**
     * 查询数据列表，返回所有字段<br>
     * 查询条件为多个key value对表示，默认key = value，如果使用其它条件可以使用：where.put("key", " &gt; 1")，value也可以传Condition对象，key被忽略
     * @param where 条件实体类（包含表名）
     * @param beanClass
     * @param <T>
     * @return
     * @throws SQLException
     */
    @Override
    public <T> List<T> findAll(Entity where, Class<T> beanClass) throws SQLException {
        return find(where, BeanListHandler.create(beanClass));
    }

    /**
     * 查询数据列表，返回所有字段
     * @param tableName 表名
     * @return
     * @throws SQLException
     */
    @Override
    public List<Entity> findAll(String tableName) throws SQLException {
        return findAll(Entity.create(tableName));
    }

    /**
     * 根据某个字段名条件查询数据列表，返回所有字段
     * @param tableName 表名
     * @param field 字段名
     * @param value 字段值
     * @return
     * @throws SQLException
     */
    @Override
    public List<Entity> findBy(String tableName, String field, Object value) throws SQLException {
        return findAll(Entity.create(tableName).set(field, value));
    }

    /**
     * 根据某个字段名条件查询数据列表，返回所有字段
     * @param tableName 表名
     * @param wheres 字段名
     * @return
     * @throws SQLException
     */
    @Override
    public List<Entity> findBy(String tableName, Condition... wheres) throws SQLException {
        final Query query = new Query(wheres, tableName);
        return find(query, EntityListHandler.create());
    }

    /**
     * 根据某个字段名条件查询数据列表，返回所有字段
     * @param tableName 表名
     * @param field 字段名
     * @param value 字段值
     * @param likeType {@link Condition.LikeType}
     * @return
     * @throws SQLException
     */
    @Override
    public List<Entity> findLike(String tableName, String field, String value, Condition.LikeType likeType) throws SQLException {
        return findAll(Entity.create(tableName).set(field, SqlUtil.buildLikeValue(value, likeType, true)));
    }

    /**
     * 结果的条目数
     * @param where 查询条件
     * @return
     * @throws SQLException
     */
    @Override
    public int count(Entity where) throws SQLException {
        Connection conn = this.getConnection();
        return runner.count(conn, where);
    }

    /**
     * 分页查询<br>
     * 查询条件为多个key value对表示，默认key = value，如果使用其它条件可以使用：where.put("key", " &gt; 1")，value也可以传Condition对象，key被忽略
     * @param fields 返回的字段列表，null则返回所有字段
     * @param where 条件实体类（包含表名）
     * @param page 页码
     * @param numPerPage 每页条目数
     * @param rsh 结果集处理对象
     * @param <T>
     * @return
     * @throws SQLException
     */
    @Override
    public <T> T page(Collection<String> fields, Entity where, int page, int numPerPage, RsHandler<T> rsh) throws SQLException {
        Connection conn = this.getConnection();
        return runner.page(conn, fields, where, page, numPerPage, rsh);
    }

    /**
     * 分页查询<br>
     * 查询条件为多个key value对表示，默认key = value，如果使用其它条件可以使用：where.put("key", " &gt; 1")，value也可以传Condition对象，key被忽略
     * @param where 条件实体类（包含表名）
     * @param page 页码
     * @param numPerPage 每页条目数
     * @param rsh 结果集处理对象
     * @param <T>
     * @return
     * @throws SQLException
     */
    @Override
    public <T> T page(Entity where, int page, int numPerPage, RsHandler<T> rsh) throws SQLException {
        return page(where, new Page(page, numPerPage), rsh);
    }

    /**
     * 分页查询，结果为Entity列表，不计算总数<br>
     * 查询条件为多个key value对表示，默认key = value，如果使用其它条件可以使用：where.put("key", " &gt; 1")，value也可以传Condition对象，key被忽略
     * @param where 条件实体类（包含表名）
     * @param page 页码
     * @param numPerPage 每页条目数
     * @return
     * @throws SQLException
     */
    @Override
    public List<Entity> pageForEntityList(Entity where, int page, int numPerPage) throws SQLException {
        return pageForEntityList(where, new Page(page, numPerPage));
    }

    /**
     * 分页查询，结果为Entity列表，不计算总数<br>
     * 查询条件为多个key value对表示，默认key = value，如果使用其它条件可以使用：where.put("key", " &gt; 1")，value也可以传Condition对象，key被忽略
     * @param where 条件实体类（包含表名）
     * @param page 分页对象
     * @return
     * @throws SQLException
     */
    @Override
    public List<Entity> pageForEntityList(Entity where, Page page) throws SQLException {
        return page(where, page, EntityListHandler.create());
    }

    /**
     * 分页查询<br>
     * 查询条件为多个key value对表示，默认key = value，如果使用其它条件可以使用：where.put("key", " &gt; 1")，value也可以传Condition对象，key被忽略
     * @param where 条件实体类（包含表名）
     * @param page 分页对象
     * @param rsh 结果集处理对象
     * @param <T>
     * @return
     * @throws SQLException
     */
    @Override
    public <T> T page(Entity where, Page page, RsHandler<T> rsh) throws SQLException {
        return page(where.getFieldNames(), where, page, rsh);
    }

    /**
     * 分页查询<br>
     * 查询条件为多个key value对表示，默认key = value，如果使用其它条件可以使用：where.put("key", " &gt; 1")，value也可以传Condition对象，key被忽略
     * @param fields 返回的字段列表，null则返回所有字段
     * @param where 条件实体类（包含表名）
     * @param page 分页对象
     * @param rsh 结果集处理对象
     * @param <T>
     * @return
     * @throws SQLException
     */
    @Override
    public <T> T page(Collection<String> fields, Entity where, Page page, RsHandler<T> rsh) throws SQLException {
        Connection conn = this.getConnection();
        return runner.page(conn, fields, where, page, rsh);
    }

    /**
     * 分页查询<br>
     * 查询条件为多个key value对表示，默认key = value，如果使用其它条件可以使用：where.put("key", " &gt; 1")，value也可以传Condition对象，key被忽略
     * @param fields 返回的字段列表，null则返回所有字段
     * @param where 条件实体类（包含表名）
     * @param page 分页对象
     * @param numPerPage 每页条目数
     * @return
     * @throws SQLException
     */
    @Override
    public PageResult<Entity> page(Collection<String> fields, Entity where, int page, int numPerPage) throws SQLException {
        Connection conn = this.getConnection();
        return runner.page(conn, fields, where, page, numPerPage);
    }

    /**
     * 分页查询<br>
     * 查询条件为多个key value对表示，默认key = value，如果使用其它条件可以使用：where.put("key", " &gt; 1")，value也可以传Condition对象，key被忽略
     * @param fields 返回的字段列表，null则返回所有字段
     * @param where 条件实体类（包含表名）
     * @param page 分页对象
     * @return
     * @throws SQLException
     */
    @Override
    public PageResult<Entity> page(Collection<String> fields, Entity where, Page page) throws SQLException {
        Connection conn = this.getConnection();
        return runner.page(conn, fields, where, page);
    }

    /**
     * 分页查询<br>
     * 查询条件为多个key value对表示，默认key = value，如果使用其它条件可以使用：where.put("key", " &gt; 1")，value也可以传Condition对象，key被忽略
     * @param where 条件实体类（包含表名）
     * @param page 页码
     * @param numPerPage 每页条目数
     * @return
     * @throws SQLException
     */
    @Override
    public PageResult<Entity> page(Entity where, int page, int numPerPage) throws SQLException {
        return this.page(where, new Page(page, numPerPage));
    }

    /**
     * 分页查询<br>
     * 查询条件为多个key value对表示，默认key = value，如果使用其它条件可以使用：where.put("key", " &gt; 1")，value也可以传Condition对象，key被忽略
     * @param where 条件实体类（包含表名）
     * @param page 分页对象
     * @return
     * @throws SQLException
     */
    @Override
    public PageResult<Entity> page(Entity where, Page page) throws SQLException {
        return this.page(where.getFieldNames(), where, page);
    }

    /**
     * 设置包装器，包装器用于对表名、字段名进行符号包装（例如双引号），防止关键字与这些表名或字段冲突
     * @param wrapperChar 包装字符，字符会在SQL生成时位于表名和字段名两边，null时表示取消包装
     */
    public void setWrapper(Character wrapperChar) {
        setWrapper(new Wrapper(wrapperChar));
    }

    /**
     * 设置包装器，包装器用于对表名、字段名进行符号包装（例如双引号），防止关键字与这些表名或字段冲突
     * @param wrapper 包装器，null表示取消包装
     */
    public void setWrapper(Wrapper wrapper) {
        this.runner.setWrapper(wrapper);
    }

    /**
     * 取消包装器<br>
     * 取消自动添加到字段名、表名上的包装符（例如双引号）
     *
     * @return this
     * @since 4.5.7
     */
    public void disableWrapper() {
        setWrapper((Wrapper) null);
    }

    /**
     * 获取{@link SqlConnRunner}
     * @return {@link SqlConnRunner}
     */
    public SqlConnRunner getRunner() {
        return runner;
    }
    /**
     * 设置 {@link SqlConnRunner}
     * @param runner {@link SqlConnRunner}
     */
    public void setRunner(SqlConnRunner runner) {
        this.runner = runner;
    }

    /**
     * 得到Session
     * @return
     */
    private Session getSession(){
        return hibernateTemplate.getSessionFactory().getCurrentSession();
    }
    public HibernateTemplate getHibernateTemplate() {
        return hibernateTemplate;
    }
    @Resource(name="hibernateTemplate")
    public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
        this.hibernateTemplate = hibernateTemplate;
    }

    /**
     * 获取Session工厂
     * @return
     */
    @Override
    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }
    @Resource(name="sf")
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
}
