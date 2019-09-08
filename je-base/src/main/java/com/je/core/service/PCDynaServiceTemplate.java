package com.je.core.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.hibernate.SessionFactory;

import com.je.core.entity.QueryInfo;
import com.je.core.util.bean.DynaBean;
import com.je.rbac.model.EndUser;

/**
 * 针对于动态BEAN的服务
 * @author YUNFENGCHENG
 * 2012-5-02 下午10:21:05
 */
public interface PCDynaServiceTemplate {
    /**
     * 插入一条带指定数据的数据库记录，如果对应字段没有值，则系统将字符串类型字段置为'', 将数字类型字段置为0
     *
     * @param dynaBean          存放要插入的数据库记录信息
     * @return DynaBean  保存后的数据
     */
    public DynaBean insert(DynaBean dynaBean);
    /**
     * 批量插入数据
     * @param beans
     */
    public void insert(List<DynaBean> beans);
    /**
     * 基于主键删除一条数据库记录
     *
     * @param dynaBean      数据信息
     * @return              本类
     */
    public DynaBean delete(DynaBean dynaBean);
    /**
     * 基于主键假删除一条数据库记录
     *
     * @param dynaBean      数据信息
     * @return              本类
     */
    public DynaBean deleteFake(DynaBean dynaBean);
    /**
     * 基于主键假删除一条数据库记录
     *
     * @param tableCode     表名
     * @param ids           主键数组
     * @return              本类
     */
    public Integer deleteFake(String tableCode, String[] ids);
    /**
     * 基于主键假删除一条数据库记录
     *
     * @param tableCode     表名
     * @param ids           主键数组
     * @return              本类
     */
    public Integer enableFake(String tableCode, String[] ids);
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
    public int deleteByWhereSql(DynaBean dynaBean);
    /**
     * 根据动态类中$WHERE$项设定的条件删除多条数据库记录
     *
     * @param dynaBean          数据信息
     * @return params
     */
    public int deleteByWhereSql(DynaBean dynaBean,Object[] params);
    /**
     * 根据动态类中$WHERE$项设定的条件删除多条数据库记录
     *
     * @param dynaBean          数据信息
     * @return params
     */
    public int deleteByWhereSql(DynaBean dynaBean,Map<String,Object> params);
    /**
     * 根据SQL删除指定表数据
     * @param tableCode
     * @param whereSql*如果想增加排序条件直接在写好的where后面增加order by即可
     * @return
     */
    public int deleteByWehreSql(String tableCode, String whereSql);
    /**
     * 根据SQL删除指定表数据
     * @param tableCode
     * @param params
     * @return
     */
    public int deleteByWehreSql(String tableCode,String whereSql, Object[] params);
    /**
     * 根据SQL删除指定表数据
     * @param tableCode
     * @param params
     * @return
     */
    public int deleteByWehreSql(String tableCode, String whereSql,Map<String,Object> params);
    /**
     * 根据用逗号分开的主键id字符串批量删除表中的数据
     * @param ids 主键字符串
     * @param tableName 表名字
     * @param idName 主键名称
     * @return 删除的行数
     */
    public int deleteByIds(String ids, String tableName, String idName);
    /**
     * 根据逗号分开的主键id字符批量删除树形的数据
     * @param ids
     * @param tableName
     */
    public int deleteTreeByIds(String ids, String tableName);
    /**
     * 根据主键查询一条指定的记录。
     *
     * @param dynaBean      数据信息
     * @return              数据库记录相信信息，不存在数据时返回空
     */
    public DynaBean selectByPk(DynaBean dynaBean);
    /**
     * 根据主键查询一条指定的记录。
     *
     * @param dynaBean      数据信息
     * @param queryFields   查询字段   多个按逗号隔开
     * @return              数据库记录相信信息，不存在数据时返回空
     */
    public DynaBean selectByPk(DynaBean dynaBean, String queryFields);
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
//    /**
//     * 根据主键或者过滤条件查询一条指定的记录：
//     * 如果是则使用主键字段，则自动根据动态类中主键的信息进行过滤
//     * 如果不是使用主键字段，则使用$WHERE$中的信息进行过滤
//     *
//     * @param dynaBean      数据信息
//     * @param queryFields   查询字段   多个按逗号隔开
//     * @param bKeySelect    是否基于主键查找一条指定的记录
//     * @return              数据库记录相信信息，不存在数据时返回空
//     */
//    public DynaBean select(DynaBean dynaBean, String queryFields, boolean bKeySelect);
    /**
     * 根据$WEHRE$条件查询符合条件的记录数量
     *
     * @param dynaBean      数据信息
     * @return              符合条件的纪录数
     */
    public long selectCount(DynaBean dynaBean);
    /**
     * 根据$WEHRE$条件查询符合条件的记录数量
     *
     * @param dynaBean      数据信息
     * @return              符合条件的纪录数
     */
    public long selectCount(DynaBean dynaBean,Object[] params);
    /**
     * 根据$WEHRE$条件查询符合条件的记录数量
     *
     * @param dynaBean      数据信息
     * @return              符合条件的纪录数
     */
    public long selectCount(DynaBean dynaBean,Map<String,Object> params);
    /**
     * 根据$WEHRE$条件查询符合条件的纪录数量
     *
     * @param tableCode      表名
     * @param whereSql       查询条件*如果想增加排序条件直接在写好的where后面增加order by即可
     * @return              符合条件的纪录数
     */
    public long selectCount(String tableCode, String whereSql);
    /**
     * 根据$WEHRE$条件查询符合条件的纪录数量
     *
     * @param tableCode      表名
     * @param whereSql       查询条件*如果想增加排序条件直接在写好的where后面增加order by即可
     * @return              符合条件的纪录数
     */
    public long selectCount(String tableCode, String whereSql,Object[] params);
    /**
     * 根据$WEHRE$条件查询符合条件的纪录数量
     *
     * @param tableCode      表名
     * @param whereSql       查询条件*如果想增加排序条件直接在写好的where后面增加order by即可
     * @return              符合条件的纪录数
     */
    public long selectCount(String tableCode, String whereSql,Map<String,Object> params);
    /**
     * 根据条件查询数据库中的一条记录，如果查询出0条或多条记录返回null
     * @param dynaBean  查询条件bean
     * @return  结果bean
     */
    public DynaBean selectOne(DynaBean dynaBean);
    /**
     * 根据条件查询数据库中的一条记录，如果查询出0条或多条记录返回null
     * @param dynaBean  查询条件bean
     * @return  结果bean
     */
    public DynaBean selectOne(DynaBean dynaBean,Object[] params);
    /**
     * 根据条件查询数据库中的一条记录，如果查询出0条或多条记录返回null
     * @param dynaBean  查询条件bean
     * @return  结果bean
     */
    public DynaBean selectOne(DynaBean dynaBean,Map<String,Object> params);
    /**
     * 根据条件查询数据库中的一条记录，如果查询出0条或多条记录返回null
     * @param dynaBean  查询条件bean
     * @param queryFields 查询字段，多个按逗号隔开
     * @return  结果bean
     */
    public DynaBean selectOne(DynaBean dynaBean, String queryFields);
    /**
     * 根据条件查询数据库中的一条记录，如果查询出0条或多条记录返回null
     * @param dynaBean  查询条件bean
     * @param queryFields 查询字段，多个按逗号隔开
     * @return  结果bean
     */
    public DynaBean selectOne(DynaBean dynaBean,Object[] params, String queryFields);
    /**
     * 根据条件查询数据库中的一条记录，如果查询出0条或多条记录返回null
     * @param dynaBean  查询条件bean
     * @param queryFields 查询字段，多个按逗号隔开
     * @return  结果bean
     */
    public DynaBean selectOne(DynaBean dynaBean,Map<String,Object> params, String queryFields);
    /**
     * 根据条件查询数据库中的一条记录，如果查询出0条或多条记录返回null
     * @param tableCode 表名
     * @param whereSql 查询条件*如果想增加排序条件直接在写好的where后面增加order by即可
     * @return
     */
    public DynaBean selectOne(String tableCode, String whereSql);
    /**
     * 根据条件查询数据库中的一条记录，如果查询出0条或多条记录返回null
     * @param tableCode 表名
     * @param whereSql 查询条件*如果想增加排序条件直接在写好的where后面增加order by即可
     * @return
     */
    public DynaBean selectOne(String tableCode, String whereSql,Object[] params);
    /**
     * 根据条件查询数据库中的一条记录，如果查询出0条或多条记录返回null
     * @param tableCode 表名
     * @param whereSql 查询条件*如果想增加排序条件直接在写好的where后面增加order by即可
     * @return
     */
    public DynaBean selectOne(String tableCode, String whereSql,Map<String,Object> params);
    /**
     * 根据条件查询数据库中的一条记录，如果查询出0条或多条记录返回null
     * @param tableCode 表名
     * @param whereSql 查询条件*如果想增加排序条件直接在写好的where后面增加order by即可
     * @param queryFields 查询字段，多个按逗号隔开
     * @return
     */
    public DynaBean selectOne(String tableCode, String whereSql, String queryFields);
    /**
     * 根据条件查询数据库中的一条记录，如果查询出0条或多条记录返回null
     * @param tableCode 表名
     * @param whereSql 查询条件*如果想增加排序条件直接在写好的where后面增加order by即可
     * @param queryFields 查询字段，多个按逗号隔开
     * @return
     */
    public DynaBean selectOne(String tableCode, String whereSql,Object[] params, String queryFields);
    /**
     * 根据条件查询数据库中的一条记录，如果查询出0条或多条记录返回null
     * @param tableCode 表名
     * @param whereSql 查询条件*如果想增加排序条件直接在写好的where后面增加order by即可
     * @param queryFields 查询字段，多个按逗号隔开
     * @return
     */
    public DynaBean selectOne(String tableCode, String whereSql,Map<String,Object> params, String queryFields);
    /**
     * 传入tableName和主键值，返回一个DynaBean，如果未找到则返回null
     * @param tableName 表名
     * @param pk 主键值
     * @return
     */
    public DynaBean selectOneByPk(String tableName, String pk);
    /**
     * 传入tableName和主键值，返回一个DynaBean，如果未找到则返回null
     * @param tableName 表名
     * @param pk 主键值
     * @param queryFields 查询字段，多个按逗号隔开
     * @return
     */
    public DynaBean selectOneByPk(String tableName, String pk, String queryFields);
    /**
     * 查询动态Bean的集合
     * @param tableCode 表名
     * @param whereSql 查询条件*如果想增加排序条件直接在写好的where后面增加order by即可
     * @return
     */
    public List<DynaBean> selectList(String tableCode, String whereSql);
    /**
     * 查询动态Bean的集合
     * @param tableCode 表名
     * @param whereSql 查询条件*如果想增加排序条件直接在写好的where后面增加order by即可
     * @return
     */
    public List<DynaBean> selectList(String tableCode, String whereSql,Object[] params);
    /**
     * 查询动态Bean的集合
     * @param tableCode 表名
     * @param whereSql 查询条件*如果想增加排序条件直接在写好的where后面增加order by即可
     * @return
     */
    public List<DynaBean> selectList(String tableCode, String whereSql,Map<String,Object> params);
    /**
     * 查询动态Bean的集合
     * @param tableCode 表名
     * @param whereSql 查询条件*如果想增加排序条件直接在写好的where后面增加order by即可
     * @param start
     * @param limit
     * @return
     */
    public List<DynaBean> selectList(String tableCode, String whereSql, int start, int limit);
    /**
     * 查询动态Bean的集合
     * @param tableCode 表名
     * @param whereSql 查询条件*如果想增加排序条件直接在写好的where后面增加order by即可
     * @param start
     * @param limit
     * @return
     */
    public List<DynaBean> selectList(String tableCode, String whereSql,Object[] params, int start, int limit);
    /**
     * 查询动态Bean的集合
     * @param tableCode 表名
     * @param whereSql 查询条件*如果想增加排序条件直接在写好的where后面增加order by即可
     * @param start
     * @param limit
     * @return
     */
    public List<DynaBean> selectList(String tableCode, String whereSql,Map<String,Object> params, int start, int limit);
    /**
     * 查询动态Bean的集合
     * @param tableCode 表名
     * @param whereSql 查询条件*如果想增加排序条件直接在写好的where后面增加order by即可
     * @param queryFields 查询字段，多个按逗号隔开
     * @return
     */
    public List<DynaBean> selectList(String tableCode, String whereSql, String queryFields);
    /**
     * 查询动态Bean的集合
     * @param tableCode 表名
     * @param whereSql 查询条件*如果想增加排序条件直接在写好的where后面增加order by即可
     * @param queryFields 查询字段，多个按逗号隔开
     * @return
     */
    public List<DynaBean> selectList(String tableCode, String whereSql,Object[] params, String queryFields);
    /**
     * 查询动态Bean的集合
     * @param tableCode 表名
     * @param whereSql 查询条件*如果想增加排序条件直接在写好的where后面增加order by即可
     * @param queryFields 查询字段，多个按逗号隔开
     * @return
     */
    public List<DynaBean> selectList(String tableCode, String whereSql,Map<String,Object> params, String queryFields);
    /**
     * 查询动态Bean的集合
     * @param tableCode 表名
     * @param whereSql 查询条件*如果想增加排序条件直接在写好的where后面增加order by即可
     * @param queryFields 查询字段，多个按逗号隔开
     * @return
     */
    public List<DynaBean> selectList(String tableCode, String whereSql, String queryFields, int start, int limit);
    /**
     * 查询动态Bean的集合
     * @param tableCode 表名
     * @param whereSql 查询条件*如果想增加排序条件直接在写好的where后面增加order by即可
     * @param queryFields 查询字段，多个按逗号隔开
     * @return
     */
    public List<DynaBean> selectList(String tableCode, String whereSql,Object[] params, String queryFields, int start, int limit);
    /**
     * 查询动态Bean的集合
     * @param tableCode 表名
     * @param whereSql 查询条件*如果想增加排序条件直接在写好的where后面增加order by即可
     * @param queryFields 查询字段，多个按逗号隔开
     * @return
     */
    public List<DynaBean> selectList(String tableCode, String whereSql,Map<String,Object> params, String queryFields, int start, int limit);
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
     * @return                  数据库记录列表信息
     */
    public ArrayList<DynaBean> selectList(DynaBean dynaBean,Object[] params);
    /**
     * 根据条件查询数据库记录列表，不分页，取全部信息
     *
     * @param dynaBean          数据信息
     * @return                  数据库记录列表信息
     */
    public ArrayList<DynaBean> selectList(DynaBean dynaBean,Map<String,Object> params);
    /**
     * 根据条件查询数据库记录列表，不分页，取全部信息
     * @param dynaBean          数据信息
     * @param queryFields 查询字段，多个按逗号隔开
     * @return                  数据库记录列表信息
     */
    public ArrayList<DynaBean> selectList(DynaBean dynaBean, String queryFields);
    /**
     * 根据条件查询数据库记录列表，不分页，取全部信息
     * @param dynaBean          数据信息
     * @param queryFields 查询字段，多个按逗号隔开
     * @return                  数据库记录列表信息
     */
    public ArrayList<DynaBean> selectList(DynaBean dynaBean,Object[] params, String queryFields);
    /**
     * 根据条件查询数据库记录列表，不分页，取全部信息
     * @param dynaBean          数据信息
     * @param queryFields 查询字段，多个按逗号隔开
     * @return                  数据库记录列表信息
     */
    public ArrayList<DynaBean> selectList(DynaBean dynaBean,Map<String,Object> params, String queryFields);
    /**
     * 根据条件查询数据库记录列表
     *
     * @param dynaBean      查询信息
     * @param limit         返回纪录数
     * @param start         开始的纪录条数
     * @return              数据库记录以及结果信息
     */
    public ArrayList<DynaBean> selectList(DynaBean dynaBean, int start, int limit);
    /**
     * 根据条件查询数据库记录列表
     *
     * @param dynaBean      查询信息
     * @param limit         返回纪录数
     * @param start         开始的纪录条数
     * @return              数据库记录以及结果信息
     */
    public ArrayList<DynaBean> selectList(DynaBean dynaBean,Object[] params, int start, int limit);
    /**
     * 根据条件查询数据库记录列表
     *
     * @param dynaBean      查询信息
     * @param limit         返回纪录数
     * @param start         开始的纪录条数
     * @return              数据库记录以及结果信息
     */
    public ArrayList<DynaBean> selectList(DynaBean dynaBean,Map<String,Object> params, int start, int limit);
    /**
     * 根据条件查询数据库记录列表
     *
     * @param dynaBean      查询信息
     * @param limit         返回纪录数
     * @param start         开始的纪录条数
     * @param queryFields 	查询字段，多个按逗号隔开
     * @return              数据库记录以及结果信息
     */
    public ArrayList<DynaBean> selectList(DynaBean dynaBean, int start, int limit, String queryFields);
    /**
     * 根据条件查询数据库记录列表
     *
     * @param dynaBean      查询信息
     * @param limit         返回纪录数
     * @param start         开始的纪录条数
     * @param queryFields 	查询字段，多个按逗号隔开
     * @return              数据库记录以及结果信息
     */
    public ArrayList<DynaBean> selectList(DynaBean dynaBean,Object[] params, int start, int limit, String queryFields);
    /**
     * 根据条件查询数据库记录列表
     *
     * @param dynaBean      查询信息
     * @param limit         返回纪录数
     * @param start         开始的纪录条数
     * @param queryFields 	查询字段，多个按逗号隔开
     * @return              数据库记录以及结果信息
     */
    public ArrayList<DynaBean> selectList(DynaBean dynaBean,Map<String,Object> params, int start, int limit, String queryFields);
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
     * @return
     */
    public int listUpdate(DynaBean dynaBean,Object[] params);
    /**
     * 基于whereSql修改数据库的一条记录信息
     * @param dynaBean
     * @return
     */
    public int listUpdate(DynaBean dynaBean,Map<String,Object> params);
    /**
     * 传入一个SQL数组，批量执行更新操作（列表更新保存操作）
     * @param sqls
     * @return
     */
    public Integer listUpdate(String[] sqls);
    /**
     * 加载dynaBean的动态表格树
     * @param rootId 根节点ID
     * @param tableCode 表名
     * @ excludes 排除树形  按逗号隔开  一般排除的是checked 去掉得树节点前无多选框
     * @param checked 是否选中
     * @param queryInfo 查询对象
     * @return
     */
    public List<HashMap> loadTree(String rootId, String tableCode, String excludes, Boolean checked, QueryInfo queryInfo);
    /**
     * 构建创建信息
     * @param model
     */
    public void buildModelCreateInfo(DynaBean model);
    /**
     * 构建创建信息
     * @param model
     */
    public void buildModelCreateInfo(DynaBean model, EndUser currentUser);
    /**
     * 仅当特殊情下(更改人信息但是流程继续走)才用次方法
     * 构建创建信息,但是不处理工作流信息
     * @param model
     */
    public void buildModelCreateInfoNoWf(DynaBean model);
    /**
     * 构建修改信息
     * @param model
     */
    public void buildModelModifyInfo(DynaBean model);
    /**
     * 构建修改信息
     * @param model
     */
    public void buildModelModifyInfo(DynaBean model, EndUser currentUser);
    /**
     * 构建DynaBean指定功能上的默认值
     * @param funcCode
     * @param dynaBean
     * @return
     */
    public void buildFuncDefaultValues(String funcCode, DynaBean dynaBean);
    /**
     * 构建编号
     * @param fieldCode 字段编码
     * @param funcCode 功能编码
     * @param dynaBean 数据对象
     * @return 编号值
     */
    public String buildCode(String fieldCode, String funcCode, DynaBean dynaBean);
    /**
     * 构建编号
     * @param fieldCode 字段编码
     * @param funcCode 功能编码
     * @param dynaBean 数据对象
     * @return 编号值
     */
    public String buildCode(String fieldCode, String funcCode, DynaBean dynaBean, String jtgsId);
    /**
     * 构建编号
     * @param fieldCode 字段编码
     * @param funcCode 功能编码
     * @param dynaBean 数据对象
     * @return 编号值
     */
    public String buildCode(String fieldCode, String funcCode, DynaBean dynaBean, String zhId, Boolean disableRepeat, String tableCode);
    /**
     * 构建编号
     * @param codePatterns
     * @param entity
     * @param fieldName
     * @param infos
     * @return
     */
    public String codeGenerator(JSONArray codePatterns, DynaBean entity, String fieldName, JSONObject infos);
    /**
     * 更新父节点的节点类型         叶子
     * @param tableCode
     * @param parentId
     */
    public void updateTreePanent4NodeType(String tableCode, String parentId);
    /**
     * 节点保存级联更新父节点类型
     */
    public void saveTreeParentInfo(String tableCode, String pkCode, String parentId);
    /**
     * 级联删除树形附件
     * @param tableCode 表名
     * @param ids 主键值  多个按逗号隔开
     */
    public void removeTreeDocument(String tableCode, String ids);
    /**
     * 级联删除附件
     * @param tableCode 表名
     * @param ids 主键值  多个按逗号隔开
     */
    public void removeDocument(String tableCode, String ids);
//    /**
//     * 业务常用方法
//     * 把原始表中的数据全部导入到目标表,条件是目标表的字段要大于等于原始表字段
//     * 当然你可以增加目标表的特殊字段处理,例如他独有的外键
//     * @param source 原始表
//     * @param goal 目标表
//     * @param add 例外比如原有表总没有字段A但是目标表中有那你就要想上[Map<String,Object>]
//     * @param replace 需要在目标中替换的字符串比如目标ID为A_ID本表ID为B_ID就可以使用该参数
//     * @param whereSql 条件
//     * @param newId 拷贝时候ID生成粗略,传输""代表使用源中的id作为新的id,sqlserver2005 可以使用Newid()函数
//     * @return 是否成功
//     */
//    public boolean dynaFill(DynaBean source,DynaBean goal,Map<String,Object> add , Set<String> replace ,String whereSql,String newId);
    /**
     * 获取当前sessionFactory
     * @return
     */
    public SessionFactory getSessionFactory();

    /**
     * 保存功能更新的内容，内容格式为自定义的字符串
     * @param funcCode
     * @param pkValue
     * @param updateContext
     */
    public void savePageDiyNicked(String funcCode, String pkValue, String updateContext);
    /**
     * 保存功能修改记录
     * @param funcCode
     * @param pkValue
     * @param updateContext   内容格式: [text:"修改字段中文名",oldVal:"原值",newVal:"新值"]
     */
    public void savePageNicked(String funcCode, String pkValue, String updateContext);

    /**
     * 保存功能修改记录
     * @param funcCode 功能编码
     * @param dynaBean 业务实体
     * @param updateFields 修改字段
     */
    public void savePageNicked(String funcCode, DynaBean dynaBean, String updateFields, String updateFieldNames);
    /**
     * 保存编辑标记信息
     * @param funcCode 功能编码
     * @param tableCode 业务实体
     * @param pkValue  主键
     * @param userId  标记的用户ID
     * @param isNew   1位标记  0为不标记   2未读
     */
    public void doDataFuncEdit(String funcCode, String tableCode, String pkValue, String userId, String isNew);
    /**
     * 根据SQL语句查询结果(一般查询多个字段返回List<Map>)
     * @param sql
     * @return
     */
    public List<Map> queryMapBySql(String sql);
    /**
     * 根据SQL语句查询结果(一般查询多个字段返回List<Map>)
     * @param sql
     * @return
     */
    public List<Map> queryMapBySql(String sql,Object[] params);
    /**
     * 根据SQL语句查询结果(一般查询多个字段返回List<Map>)
     * @param sql
     * @return
     */
    public List<Map> queryMapBySql(String sql,Map<String,Object> params);
    /**
     * 执行SQL
     * @param sql
     * @return
     */
    public Long executeSql(String sql);
    /**
     * 执行SQL
     * @param sql
     * @return
     */
    public Long executeSql(String sql,Object[] params);
    /**
     * 执行SQL
     * @param sql
     * @return
     */
    public Long executeSql(String sql,Map<String,Object> params);

    /**
     *为Model保存相关的上传文档信息
     * @param documentInfo
     * @param entityPo
     * @param isQuery
     * @return
     */
    public DynaBean doSaveDocuments(List<DynaBean> documentInfo, DynaBean entityPo, Boolean isQuery);

    /**
     * 设定表主键值
     * @param dynaBean
     * @return
     */
    public String buildPkValue(DynaBean dynaBean);
}





