package com.je.portal.service;

import com.je.portal.vo.PushActVo;
import com.je.rbac.model.EndUser;

import net.sf.json.JSONObject;

import java.util.List;

public interface PortalManager {

	/**
	 * 加载指定的数字信息
	 * @param userId 用户id
	 * @param pushTypes 推送消息类型
	 * @param pushActs TODO 暂不明确
	 * @return
	 */
	public List<PushActVo> getPushInfo(String userId, String[] pushTypes, String[] pushActs);
	/**
	 * 推送指定消息
	 * @param userId 用户id
	 * @param pushTypes 推送消息类型
	 * @param pushActs TODO 暂不明确
	 * @return
	 */
	public void push(String userId, String[] pushTypes, String[] pushActs);

	/**
	 * 获取流程数字
	 * @param userId 用户id
	 * @return
	 */
	public JSONObject getWfNum(String userId);
	/**
	 * 获取消息数字
	 * @param userId 用户id
	 * @return
	 */
	public JSONObject getMsgNum(String userId);

	/**
	 * 获取脚本事务数字
	 * @param userId 用户id
	 * @return
	 */
	public JSONObject getTransactionNum(String userId);
	/**
	 * 获取日历数字
	 * @param userId 用户id
	 * @return
	 */
	public JSONObject getCalendarNum(String userId);

	/**
	 * 获取冒泡数字
	 * @param userId 用户id
	 * @return
	 */
	public JSONObject getBubbleNum(String userId);

	/**
	 * 获取批注数字
	 * @param userId 用户id
	 * @return
	 */
	public JSONObject getPostilNum(String userId);
	/**
	 * 获取IM数字
	 * @param userId 用户id
	 * @return
	 */
	public JSONObject getImNum(String userId);
	/**
	 * 获取新闻插件的badge
	 * @param userId
	 * @return
	 */
	public JSONObject getNoticeNum(String userId);
	/**
	 * 获取日志的badge
	 * @param userId
	 * @return
	 */
	public JSONObject getRzNum(String userId);
	/**
	 * 获取流程数字
	 * @param userId 用户id
	 * @return
	 */
	public JSONObject getWfBadge(String userId);
	/**
	 * 获取消息数字
	 * @param userId 用户id
	 * @return
	 */
	public JSONObject getMsgBadge(String userId);

	/**
	 * 获取脚本事务数字
	 * @param userId 用户id
	 * @return
	 */
	public JSONObject getTransactionBadge(String userId);
	/**
	 * 获取日历数字
	 * @param userId 用户id
	 * @return
	 */
	public JSONObject getCalendarBadge(String userId);

	/**
	 * 获取冒泡数字
	 * @param userId 用户id
	 * @return
	 */
	public JSONObject getBubbleBadge(String userId);

	/**
	 * 获取批注数字
	 * @param userId 用户id
	 * @return
	 */
	public JSONObject getPostilBadge(String userId);
	/**
	 * 获取IM数字
	 * @param userId 用户id
	 * @return
	 */
	public JSONObject getImBadge(String userId);

	/**
	 * 获取新闻插件的badge
	 * @param userId
	 * @return
	 */
	public JSONObject getNoticeBadge(String userId);
	/**
	 * 获取日志的badge
	 * @param userId
	 * @return
	 */
	public JSONObject getRzBadge(String userId);
	/**
	 * 得到登录信息
	 * @param currentUser 挡墙登陆人
	 * @param loadTypes 加载类型
	 * @param login 是否登陆
	 * @return
	 */
	public JSONObject getCurrentInfo(EndUser currentUser, String[] loadTypes, Boolean login);

}
