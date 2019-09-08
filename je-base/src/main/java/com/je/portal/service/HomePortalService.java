package com.je.portal.service;

import com.je.core.util.bean.DynaBean;
import com.je.portal.vo.Bubble;
import com.je.portal.vo.Comment;
import com.je.portal.vo.WorkLog;
import com.je.rbac.model.EndUser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface HomePortalService {

    /**
     * 保存冒泡信息到数据库
     * @param bubble TODO 暂不明确
     * @return
     */
    public DynaBean saveDataBaseBubble(Bubble bubble);

    /**
     * 保存冒泡信息到缓存
     * @param bubbleBean
     * @return
     */
//    public HashMap saveCacheBubble(DynaBean bubbleBean);

    /**
     * 数据库查询冒泡
     * @param type 最热或者最新
     * @param start 起始数据
     * @param limit 每页多少条数据
     * @return
     */
    public HashMap findDataBaseBubble(String type, int start, int limit);

    /**
     * 保存日志信息
     * @param workLog TODO 暂不明确
     * @return
     */
    public Map<String,Object> saveWorkLog(WorkLog workLog);

    /**
     * 修改日志信息
     * @param workLog TODO 暂不明确
     * @return
     */
    public Map<String,Object> updateWorkLog(WorkLog workLog);

    /**
     * 查询工作日志
     * @param workLog TODO 暂不明确
     * @param currentUser 当前登陆用户信息
     * @return
     */
    public List<Map> findWorkLog(WorkLog workLog, EndUser currentUser);

    /**
     * 点评日志接口
     * @param workLog  TODO 暂不明确
     * @param currentUser 当前登陆用户信息
     * @return
     */
    public Map<String,Object> appraiseWorkLog(WorkLog workLog, EndUser currentUser);

    /**
     * 保存消息
     * @param type 消息类型
     * @param dynaBean
     */
    public void sendMsg(String type, String source, DynaBean dynaBean, String sourceMpNr);


    /**
     * 更新点赞数评论数
     */
    public void updateBubbleNum(String bubbleId, String type, String oper);
    /**
     * 点赞
     */
    public void thumbsUp(String source, String id, DynaBean mpxxBean);
    /**
     * 取消点赞
     */
    public void cancelThumbsUp(String source, String id);
    public List<Map> isThumbsUp(List<Map> list);
    /**
     * 保存评论
     */
    public DynaBean saveComment(Comment comment);
    public List<HashMap> findComment(Comment comment);
    public List thumbsUpAndComment(List<Map> list);
}
