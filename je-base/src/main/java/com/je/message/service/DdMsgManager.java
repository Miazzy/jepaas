package com.je.message.service;

import com.dingtalk.api.request.OapiMessageCorpconversationAsyncsendV2Request;
import com.je.message.vo.DdMsgVo;
import com.je.message.vo.WxMsgVo;

import java.util.List;

/**
 * 发送文字、图片、语音等等这种接口
 */
public interface DdMsgManager {
	/**
	 * 发送消息
	 * @param msgVo 消息
	 */
	 String send(DdMsgVo msgVo);
	/**
	 * 发送消息
	 * @param msgVo 消息
	 */
	 String sendSync(DdMsgVo msgVo);
	/**
	 * 发送消息
	 * @param msgVo 消息
	 */
	 String send(DdMsgVo msgVo, String jtgsId);
	/**
	 * 初始化该人员的缓存记录
	 * @param userIds
	 */
	 void init(String userIds);

	/**
	 * 发送文字消息
	 * @param userIds 用户ID
	 * @param deptIds 部门ID
	 * @param context 内容
	 * @return
	 */
	 String sendWz(String userIds, String deptIds, String context);

	/**
	 * 发送图片消息
	 * @param userIds 用户ID
	 * @param deptIds 部门ID
	 * @param filePath 文件路径
	 * @param fileName 文件名称
	 * @return
	 */
	 String sendTp(String userIds, String deptIds, String filePath, String fileName);

	/**
	 * 发送语音消息
	 * @param userIds 用户ID
	 * @param deptIds 部门ID
	 * @param filePath 文件路径
	 * @param fileName 文件名称
	 * @return
	 */
	 String sendYy(String userIds, String deptIds, String filePath, String fileName);

	/**
	 * 发送链接消息
	 * @param userIds 用户ID
	 * @param deptIds 部门ID
	 * @param filePath 文件路径
	 * @param fileName 文件名称
	 * @param title 主题
	 * @param text 内容
	 * @param url 路径
	 * @return
	 */
	 String sendLj(String userIds, String deptIds, String filePath, String fileName, String title, String text, String url);

	/**
	 *
	 * @param userIds 用户ID集合 多个按逗号隔开，如果是全部人员请传@all
	 * @param filePath 语音文件地址
	 */
	/**
	 * 发送OA消息
	 * @param userIds 用户ID
	 * @param deptIds 部门ID
	 * @param filePath 文件路径
	 * @param fileName 文件名称
	 * @param url 路径
	 * @param pcUrl TODO未处理
	 * @param headText 报头内容
	 * @param headColor 报头颜色
	 * @param listFrom 集合From
	 * @param richNum TODO未处理
	 * @param richUnit TODO未处理
	 * @param bodyContent 内容
	 * @param bodyTitle 主题
	 * @param fileCount 文件内容
	 * @param author TODO未处理
	 * @return
	 */
	 String sendOa(String userIds, String deptIds, String filePath, String fileName, String url, String pcUrl, String headText, String headColor,
                   List<OapiMessageCorpconversationAsyncsendV2Request.Form> listFrom, String richNum, String richUnit, String bodyContent, String bodyTitle, String fileCount, String author);


	/**
	 * 发送MD消息
	 * @param userIds 用户ID
	 * @param deptIds 部门ID
	 * @param mdText 内容
	 * @param title 主题
	 * @return
	 */
	 String sendMd(String userIds, String deptIds, String mdText, String title);

	/**
	 * 发送卡片消息
	 * @param userIds 用户ID集合 多个按逗号隔开，如果是全部人员请传@all
	 */
	/**
	 * 发送卡片消息
	 * @param userIds 用户ID
	 * @param deptIds 部门ID
	 * @param cardTitle 卡片主题
	 * @param markDownText TODO未处理
	 * @param btnOrientation TODO未处理
	 * @param btnJsonList TODO未处理
	 * @return
	 */
	 String sendKp(String userIds, String deptIds, String cardTitle, String markDownText, String btnOrientation,
                   List<OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList> btnJsonList);


	/**
	 * 发送文件消息
	 * @param userIds 用户ID
	 * @param deptIds 部门ID
	 * @param filePath 文件路径
	 * @param fileName 文件名称
	 * @return
	 */
	 String sendWj(String userIds, String deptIds, String filePath, String fileName);
}
