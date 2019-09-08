package com.je.portal.service;

import com.je.core.util.bean.DynaBean;

import java.util.List;
import java.util.Map;

public interface WorkLogService {

    /**
     * 查询一条日志的所有信息
     *
     * @param pkValue
     * @return
     */
    public DynaBean queryReportInfo(String pkValue);

    /**
     * 增加日志阅读记录
     *
     * @param pkValue
     * @return
     */
    public void insertReportReadLog(String pkValue);


    /**
     * 增加日志点评记录
     *
     * @param pkValue
     * @return
     */
    public DynaBean insertReportCommentLog(String pkValue);

    /**
     * 查询日志列表 全部 单表查询 再拼接数据
     * @param whereSql
     * @return
     */
    //public List<DynaBean> queryReportList(String whereSql,String orderSql,String limitSql);

    /**
     * 查询日志列表 全部 多表联查SQL直接查出需要数据 再用whereSql过滤
     *
     * @param whereSql
     * @return
     */
    public List<Map> queryReportList(String whereSql, String orderSql, String limitSql);

    /**
     * 查询日志列表数量 全部 多表联查SQL直接查出需要数据 再用whereSql过滤
     *
     * @param whereSql
     * @return
     */
    public Long queryReportListCount(String whereSql, String orderSql);

    /**
     * 查询日志列表 已点评与已评价
     *
     * @param whereSql
     * @return
     */
    public List<DynaBean> queryReportList(String whereSql, String orderSql, String isComment, String isRead);

    /**
     * 获取日志主键ID的集合
     *
     * @param dynaBeans 含有日志ID的表
     * @return
     */
    public List<String> getReportIds(List<DynaBean> dynaBeans);

//    /**
//     * 判断创建人是否是当前登录人
//     * @param pkValue
//     * @return true 是当前登录人，false为不是当前登录人
//     */
//    public void isCreatUser(String pkValue);
}
