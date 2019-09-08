package com.je.message.service;

import com.je.message.vo.WxMsgVo;

public interface WxGzhMsgManager {
	/**
	 * 发送消息
	 * @param msgVo 消息
	 */
	public void send(WxMsgVo msgVo);
	/**
	 * 发送消息
	 * @param msgVo 消息
	 */
	public void sendSync(WxMsgVo msgVo);
	/**
	 * 发送消息
	 * @param msgVo 消息
	 * @param jtgsId TODO未处理
	 */
	public void send(WxMsgVo msgVo, String jtgsId);
	/**
	 * 发送文字消息
	 * @param userIds 用户ID集合 多个按逗号隔开，如果是全部人员请传@all
	 * @param context 文字内容
	 */
	public void sendWz(String userIds, String context);
	/**
	 * 发送图文消息
	 * @param userIds 用户ID集合 多个按逗号隔开，如果是全部人员请传@all
	 * @param twType 图文内容类型    UEditor 编辑器内容 || HTML || URL 链接 
	 * @param context 图文内容
	 * @param picPath 图片地址
	 * @param author 作者
	 * @param description 摘要
	 */
	public void sendTw(String userIds, String twType, String title, String context, String picPath, String author, String description, String sourceUrl);
	/**
	 * 发送图片消息
	 * @param userIds 用户ID集合 多个按逗号隔开，如果是全部人员请传@all
	 * @param filePath 图片文件地址
	 */
	public void sendTp(String userIds, String filePath);
	/**
	 * 发送语音消息
	 * @param userIds 用户ID集合 多个按逗号隔开，如果是全部人员请传@all
	 * @param filePath 语音文件地址
	 */
	public void sendYy(String userIds, String filePath);
	/**
	 * 发送视频消息
	 * @param userIds 用户ID集合 多个按逗号隔开，如果是全部人员请传@all
	 * @param filePath 视频文件地址
	 * @param title 标题
	 * @param description 摘要
	 */
	public void sendSp(String userIds, String filePath, String title, String description);
	/**
	 * 发送音乐消息消息
	 * @param userIds 用户ID集合 多个按逗号隔开，如果是全部人员请传@all
	 * @param filePath 文件地址
	 */
	public void sendYiny(String userIds, String title, String filePath, String url, String sourceUrl);
}
