package com.je.core.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.SessionFactory;

import com.je.core.entity.QueryInfo;
import com.je.core.entity.extjs.JSONTreeNode;
import com.je.core.util.bean.DynaBean;

/**
 * 针对于动态BEAN的数据库操作
 * @author YUNFENGCHENG
 * 2012-5-02 下午10:21:05
 */
public interface PCDynaDaoTemplate {
    /**
     * 插入一条带指定数据的数据库记录，如果对应字段没有值，则系统将字符串类型字段置为'', 将数字类型字段置为0
     *
     * @param dynaBean          存放要插入的数据库记录信息
     * @return 插入是否成功：      1 是， 0 否
     */
    public DynaBean insert(DynaBean dynaBean);
    /**
     * 插入一条带指定数据的数据库记录，如果对应字段没有值，则系统将字符串类型字段置为'', 将数字类型字段置为0
     *
     * @param beans          存放要插入的数据库记录信息
     * @return 插入是否成功：      1 是， 0 否
     */
    public void insert(List<DynaBean> beans);
    /**
     * 基于主键删除一条数据库记录
     *
     * @param dynaBean      数据信息
     * @return              删除行数，正常为1
     */
    public DynaBean delete(DynaBean dynaBean);
    /**
     * 基于主键假删除一条数据库记录
     *
     * @param dynaBean      数据信息
     * @return              删除行数，正常为1
     */
    public DynaBean deleteFake(DynaBean dynaBean);
    /**
     * 基于主键启用一条数据库记录
     *
     * @param dynaBean      数据信息
     * @return              删除行数，正常为1
     */

    public DynaBean enableFake(DynaBean dynaBean);
    /**
     * 根据动态类中$WHERE$项设定的条件删除多条数据库记录
     *
     * @param dynaBean          数据信息
     * @return                  删除行数
     */
    public int deleteByCondition(DynaBean dynaBean);
    /**
     * 根据动态类中$WHERE$项设定的条件删除多条数据库记录
     *
     * @param dynaBean          数据信息
     * @param params            占位参数值
     * @return                  删除行数
     */
    public int deleteByCondition(DynaBean dynaBean,Object[] params);
    /**
     * 根据动态类中$WHERE$项设定的条件删除多条数据库记录
     *
     * @param dynaBean          数据信息
     * @param params            占位参数值
     * @return                  删除行数
     */
    public int deleteByCondition(DynaBean dynaBean,Map<String,Object> params);
    /**
     * 根据用逗号分开的主键id字符串批量删除表中的数据
     * @param ids 主键字符串
     * @param tableName 表名字
     * @param idName 主键名称
     * @return 删除的行数
     */
    public int deleteByIds(String ids, String tableName, String idName);
    /**
     * 根据主键查询一条指定的记录。
     *
     * @param dynaBean      数据信息
     * @return              数据库记录相信信息，不存在数据时返回空
     */
    public DynaBean selectByPk(DynaBean dynaBean);
//    /**
//     * 根据主键或者过滤条件查询一条指定的记录：
//     * 如果是则使用主键字段，则自动根据动态类中主键的信息进行过滤
//     * 如果不是使用主键字段，则使用$WHERE$中的信息进行过滤
//     *
//     * @param dynaBean      数据信息
//     * @param bKeySelect    是否基于主键查找一条指定的记录
//     * @return              数据库记录相信信息，不存在数据时返回空
//     */
//    public DynaBean select(DynaBean dynaBean, boolean bKeySelect);
    /**
     * 根据$WEHRE$条件查询符合条件的纪录数量
     *
     * @param dynaBean      数据信息
     * @return              符合条件的纪录数
     */
    public long selectCount(DynaBean dynaBean);
    /**
     * 根据$WEHRE$条件查询符合条件的纪录数量
     *
     * @param dynaBean      数据信息
     * @param params        占位参数值
     * @return              符合条件的纪录数
     */
    public long selectCount(DynaBean dynaBean,Object[] params);
    /**
     * 根据$WEHRE$条件查询符合条件的纪录数量
     *
     * @param dynaBean      数据信息
     * @param params        占位参数值
     * @return              符合条件的纪录数
     */
    public long selectCount(DynaBean dynaBean, Map<String,Object> params);
    /**
     * 根据条件查询数据库中的一条记录，如果查询出多条记录，那么只取第一条记录
     * @param dynaBean  查询条件bean
     * @return  结果bean
     */
    public DynaBean selectOne(DynaBean dynaBean);
    /**
     * 根据条件查询数据库中的一条记录，如果查询出多条记录，那么只取第一条记录
     * @param dynaBean  查询条件bean
     * @param params        占位参数值
     * @return  结果bean
     */
    public DynaBean selectOne(DynaBean dynaBean,Object[] params);
    /**
     * 根据条件查询数据库中的一条记录，如果查询出多条记录，那么只取第一条记录
     * @param dynaBean  查询条件bean
     * @param params        占位参数值
     * @return  结果bean
     */
    public DynaBean selectOne(DynaBean dynaBean, Map<String,Object> params);
    /**
     * 根据条件查询数据库记录列表，不分页，取全部信息
     *
     * @param dynaBean          数据信息
     * @return                  数据库记录列表信息
     */
    public ArrayList<DynaBean> selectList(DynaBean dynaBean);
    /**
     * 根据条件查询数据库记录列表，不分页，取全部信息
     *
     * @param dynaBean          数据信息
     * @param params           占位参数值
     * @return                  数据库记录列表信息
     */
    public ArrayList<DynaBean> selectList(DynaBean dynaBean,Object[] params);
    /**
     * 根据条件查询数据库记录列表，不分页，取全部信息
     *
     * @param dynaBean          数据信息
     * @param params           占位参数值
     * @return                  数据库记录列表信息
     */
    public ArrayList<DynaBean> selectList(DynaBean dynaBean,Map<String,Object> params);
//    /**
//     * 根据条件查询数据库记录列表
//     *
//     * @param dynaBean      查询信息
//     * @param page          当前所在页数，第一页应该为1
//     * @param limit         返回纪录数
//     * @param start         开始的纪录条数
//     * @return              数据库记录以及结果信息
//     */
//    public ArrayList<DynaBean> selectList(DynaBean dynaBean, int start, int limit, int page);
//    /**
//     * 根据条件查询数据库记录列表
//     *
//     * @param dynaBean      查询信息
//     * @param params        占位参数值
//     * @param page          当前所在页数，第一页应该为1
//     * @param limit         返回纪录数
//     * @param start         开始的纪录条数
//     * @return              数据库记录以及结果信息
//     */
//    public ArrayList<DynaBean> selectList(DynaBean dynaBean,Object[] params, int start, int limit, int page);
    /**
     * 根据条件查询数据库记录列表
     *
     * @param dynaBean      查询信息
     * @param limit         返回纪录数
     * @param start         开始的纪录条数
     * @return              数据库记录以及结果信息
     */
    public ArrayList<DynaBean> selectList(DynaBean dynaBean, int start, int limit, boolean noCount);
    /**
     * 根据条件查询数据库记录列表
     *
     * @param dynaBean      查询信息
     * @param params        占位参数值
     * @param limit         返回纪录数
     * @param start         开始的纪录条数
     * @return              数据库记录以及结果信息
     */
    public ArrayList<DynaBean> selectList(DynaBean dynaBean,Object[] params, int start, int limit, boolean noCount);
    /**
     * 根据条件查询数据库记录列表
     *
     * @param dynaBean      查询信息
     * @param params        占位参数值
     * @param limit         返回纪录数
     * @param start         开始的纪录条数
     * @return              数据库记录以及结果信息
     */
    public ArrayList<DynaBean> selectList(DynaBean dynaBean,Map<String,Object> params, int start, int limit, boolean noCount);
    /**
     * 基于主键修改数据库记录信息
     * @param dynaBean                要更新的数据库记录信息
     * @return                        更新好的数据BEAN
     */
    public DynaBean update(DynaBean dynaBean);
    /**
     * 基于whereSql修改数据库的一条记录信息
     * @param dynaBean
     * @return
     */
    public int listUpdate(DynaBean dynaBean);
    /**
     * 基于whereSql修改数据库的一条记录信息
     * @param dynaBean
     * @param params
     * @return
     */
    public int listUpdate(DynaBean dynaBean,Object[] params);
    /**
     * 基于whereSql修改数据库的一条记录信息
     * @param dynaBean
     * @param params
     * @return
     */
    public int listUpdate(DynaBean dynaBean,Map<String,Object> params);
    /**
     * 获取当前sessionFactory
     * YUNFENGCHENG
     * 2012-1-4 上午11:10:18
     * @return
     */
    public SessionFactory getSessionFactory();
    /**
     * 执行sql
     * @param sql
     * @return
     */
    public abstract int listUpdate(String sql);
    /**
     * 加载dyanBean树形数据
     * @param rootId
     * @param template
     * @param dynaBean
     * @return
     */
    public ArrayList<HashMap> loadTree(String rootId, JSONTreeNode template, DynaBean dynaBean, QueryInfo queryInfo);
    /**
     * 加载表格树
     * @param rootId
     * @param template
     * @param table
     * @return
     */
    public ArrayList<HashMap> loadTree(String rootId, JSONTreeNode template, DynaBean table, String excludes, Boolean checked, QueryInfo queryInfo);
}





