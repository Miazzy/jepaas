package com.je.core.dao;

import java.util.List;

import com.je.core.util.mongo.MBean;

/**
 * PCAT平台对NOSQL数据库统一操作接口
 * @author 云凤程
 *
 */
public interface PCNoSqlTemplateImpl {
    /**
     * 添加数据
     * @param mBean
     * @return
     */
    public MBean insert(MBean mBean);
    /**
     * 批量增加
     * @param mBeans
     * @param simple 集合提是否针对单一数据库链表
     * @return
     */
    public int insertBatch(List<MBean> mBeans, boolean simple);
    /**
     * 根据_id删除数据
     * @param _id 可以是_id的字符串,也可以是ObjectId对象实例
     * @param listName 链表名字
     * @param mongoAutoId 是否是mongo自动管理的_id
     * @return
     */
    public int deleteById(Object _id, String listName, boolean mongoAutoId);
    /**
     * 根据组合条件删除数据
     * @param mBean
     * @return
     */
    public int deleteByMBean(MBean mBean);
    /**
     * 根据条件查出一条记录
     * @param ref
     * @param keys
     * @return
     */
    public MBean findOne(MBean ref, MBean keys);
}
