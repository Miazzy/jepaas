package com.je.message.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.je.message.vo.DwrMsgVo;

/**
 * DWR推送消息  服务
 * @author zhangshuaipeng
 *
 */
public interface DwrManager {
	/**
	 * 发送消息
	 * @param msgVo 消息VO实例
	 */
	public void sendMsg(DwrMsgVo msgVo);
	
	/**
	 * 指明不通过集群发送消息
	 * @param msgVo
	 */
	public void sendMsgWithoutCluster(DwrMsgVo msgVo);
	/**
	 * 发送消息
	 * @param userId 用户ID
	 * @param title 标题
	 * @param context 内容
	 */
	public void sendMsg(String userId, String title, String context);

	/**
	 * 发送按照模版展示的消息
	 * @param userId 用户ID
	 * @param title 标题
	 * @param showConfig 模版
	 * @param bean 业务数据
	 */
	public void sendMsg(String userId, String title, String showConfig, Map<String, Object> bean);

	/**
	 * 发送调用自定义的js方法的消息
	 * @param userId 用户ID
	 * @param title 标题
	 * @param context 内容
	 * @param callFunction 自定义函数
	 * @param batchCallFunction 自定义批量函数
	 */
	public void sendMsg(String userId, String title, String context, String callFunction, String batchCallFunction);

	/**
	 * 发送调用自定义的js方法的消息
	 * @param userId 用户ID
	 * @param title 标题
	 * @param showConfig 展示模版
	 * @param callFunction 自定义函数
	 * @param batchCallFunction 自定义批量函数
	 * @param bean 业务数据
	 */
	public void sendMsg(String userId, String title, String showConfig, String callFunction, String batchCallFunction, Map<String, Object> bean);

	/**
	 * 发送调用自定义的js方法的消息
	 * @param userId 用户ID
	 * @param title 标题
	 * @param context 内容
	 * @param callFunction 自定义函数
	 * @param batchCallFunction 自定义批量函数
	 * @param loginHistory 是否没有登录，下次登录调用
	 */
	public void sendMsg(String userId, String title, String context, String callFunction, String batchCallFunction, Boolean loginHistory);

	/**
	 * 指定不通过集群发送调用自定义的js方法的消息
	 * @param userId 用户ID
	 * @param title 标题
	 * @param context 内容
	 * @param callFunction 自定义函数
	 * @param batchCallFunction 自定义批量函数
	 * @param loginHistory 是否没有登录，下次登录调用
	 */
	public void sendMsgWithoutCluster(String userId, String title, String context, String callFunction, String batchCallFunction, Boolean loginHistory);

	/**
	 * 发送调用自定义的js方法的消息
	 * @param userId 用户ID
	 * @param title 标题
	 * @param context 内容
	 * @param callFunction 自定义函数
	 * @param batchCallFunction 自定义批量函数
	 * @param loginHistory 是否没有登录，下次登录调用
	 * @param excludeSessionIds 排除浏览器sessionId  推送该用户时，不推送指定的sessionId
	 */
	public void sendMsg(String userId, String title, String context, String callFunction, String batchCallFunction, Boolean loginHistory, String excludeSessionIds);
	/**
	 * 发送调用自定义的js方法的消息
	 * @param userId 用户ID
	 * @param title 标题
	 * @param showConfig 模版
	 * @param callFunction 自定义函数
	 * @param batchCallFunction 自定义批量函数
	 * @param loginHistory 是否没有登录，下次登录调用
	 * @param bean 业务数据
	 */
	public void sendMsg(String userId, String title, String showConfig, String callFunction, String batchCallFunction, Boolean loginHistory, Map<String, Object> bean);

	/**
	 * 获取在线用户的用户ID集合
	 * @param zhId
	 * @return
	 */
	public Set<String> getOnlineUser(String zhId);
	
}
