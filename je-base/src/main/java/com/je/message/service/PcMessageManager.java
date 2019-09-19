package com.je.message.service;

import java.util.List;
import java.util.Map;

import com.je.core.constants.message.SendContextType;
import com.je.core.util.bean.DynaBean;
import com.je.message.vo.DwrMsgVo;
import com.je.message.vo.EmailMsgVo;
import com.je.message.vo.NoteMsgVo;
import com.je.message.vo.RTXMsgVo;
import com.je.message.vo.Remind;
import com.je.message.vo.WxMsgVo;
import com.je.message.vo.app.UserMsgAppInfo;

public interface PcMessageManager {
	/**
	 * 发送短信
	 * @param msgVo 消息VO实例
	 */
	public void sendNote(NoteMsgVo msgVo);
	/**
	 * 同步发送短信
	 * @param msgVo
	 */
	public DynaBean sendNoteSync(NoteMsgVo msgVo);
	/**
	 * 发送短信
	 * @param msgVo 消息VO实例
	 * @param jtgsId TODO未处理
	 */
	public void sendNote(NoteMsgVo msgVo, String jtgsId);
	/**
	 * 发送短信
	 * @param phoneNumber 手机号
	 * @param context 内容
	 */
	public void sendNote(String phoneNumber, String context);
	/**
	 * 发送短信
	 * @param phoneNumber 手机号
	 * @param toUserName 接收人
	 * @param toUserId	接收人主键
	 * @param context 内容
	 */
	public void sendNote(String phoneNumber, String toUserName, String toUserId, String context);
	/**
	 * 发送短信
	 * @param phoneNumber 手机号
	 * @param fromUserName 发送人
	 * @param fromUserId 发送人主键
	 * @param toUserName 接收人
	 * @param toUserId 接收人主键
	 * @param context 内容
	 */
	public void sendNote(String phoneNumber, String fromUserName, String fromUserId, String toUserName, String toUserId, String context);
	
	/**
	 * 发送消息
	 * @param msgVo 消息
	 */
	public void sendRtx(RTXMsgVo msgVo);
	/**
	 * 发送消息
	 * @param msgVo 消息
	 * @param jtgsId TODO未处理
	 */
	public void sendRtx(RTXMsgVo msgVo, String jtgsId);
	/**
	 * 发送RTX消息
	 * @param receiveUser RTX帐号
	 * @param title 标题
	 * @param context 内容
	 */
	public void sendRtx(String receiveUser, String title, String context);
	/**
	 * 发送RTX消息
	 * @param receiveUser RTX帐号
	 * @param title 标题
	 * @param context 内容
	 * @param fastType 是否紧急    是 1  否 0
	 * @param fromUserName 发送人
	 * @param fromDeptName 发送部门
	 * @param delayTime 停留时间
	 */
	public void sendRtx(String receiveUser, String title, String context, String fastType, String fromUserName, String fromDeptName, Long delayTime);
	/**
	 * 发送消息
	 * @param msgVo 消息VO实例
	 */
	public void sendEmail(EmailMsgVo msgVo);
	/**
	 * 发送消息
	 * @param msgVo 消息VO实例
	 * @param jtgsId TODO未处理
	 */
	public void sendEmail(EmailMsgVo msgVo, String jtgsId);
	/**
	 * 发送邮件
	 * @param receiveEmail 邮箱地址
	 * @param subject 主题 
	 * @param context 内容
	 */
	public void sendEmail(String receiveEmail, String subject, String context);
	/**
	 * 发送邮件
	 * @param receiveEmail 邮箱地址
	 * @param subject 主题
	 * @param contextType 内容类型: 普通文本 SendContextType.TEXT  HTML：SendContextType.HTML   
	 * @param context  内容
	 */
	public void sendEmail(String receiveEmail, String subject, String contextType, String context);
	/**
	 * 发送邮件
	 * @param receiveEmail 邮箱地址
	 * @param subject 主题
	 * @param contextType 内容类型: 普通文本 SendContextType.TEXT  HTML：SendContextType.HTML   
	 * @param context  内容
	 * @param fileNames   文件名称集合
	 * @param addresses   文件地址集合
	 */
	public void sendEmail(String receiveEmail, String subject, String contextType, String context, List<String> fileNames, List<String> addresses);
	/**
	 * 发送消息
	 * @param msgVo 消息VO实例
	 */
	public void sendDwr(DwrMsgVo msgVo);
	/**
	 * 推送消息
	 * @param userId 人员ID
	 * @param title 标题
	 * @param context 内容
	 */
	public void sendDwr(String userId, String title, String context);
	/**
	 * 推送消息，带有打开功能的超链接
	 * @param userId 人员ID
	 * @param title 标题
	 * @param context 内容
	 * @param funcCode 功能编码
	 * @param showType LinkFuncType.FORM(打开表单  需传pkValue) || LinkFuncType.GRID(打开表单  需传whereSql)  
	 * @param whereSql 查询条件
	 * @param pkValue 主键值
	 */
	public void sendDwr(String userId, String title, String context, String funcCode, String showType, String whereSql, String pkValue);
	/**
	 * 推送消息
	 * @param userId 人员ID
	 * @param title 标题
	 * @param showConfig 内容
	 * @param bean 对象
	 * 例子：  sendDwr("111","提醒","您有一条任务，内容是：{CONTEXT},请尽快审批!",values)    values为Map里面为键-值 替换模版中引用的变量
	 */
	public void sendDwr(String userId, String title, String showConfig, Map<String, Object> bean);
	/**
	 * 发送调用自定义的js方法的消息
	 * @param userId 用户ID
	 * @param title 主题
	 * @param context 内容
	 * @param callFunction TODO未处理
	 * @param batchCallFunction TODO未处理
	 */
	public void sendDwr(String userId, String title, String context, String callFunction, String batchCallFunction);
	/**
	 * 发送调用自定义的js方法的消息
	 * @param userId 用户ID
	 * @param title 主题
	 * @param showConfig TODO未处理
	 * @param callFunction TODO未处理
	 * @param batchCallFunction TODO未处理
	 * @param bean 传入数据
	 */
	public void sendDwr(String userId, String title, String showConfig, String callFunction, String batchCallFunction, Map<String, Object> bean);
	
	/**
	 * 发送调用自定义的js方法的消息
	 * @param userId 用户ID
	 * @param title  标题
	 * @param context 内容
	 * @param callFunction 单次调用JS方法
	 * @param batchCallFunction 批量调用JS方法
	 * @param loginHistory 是否留痕   如果传true的话  当用户未登录状态，则用户下次登录可以收到    默认为false
	 */
	public void sendDwr(String userId, String title, String context, String callFunction, String batchCallFunction, Boolean loginHistory);
	/**
	 * 发送调用自定义的js方法的消息
	 * @param userId 用户ID
	 * @param title 标题
	 * @param showConfig 展示模版
	 * @param callFunction 调用方法
	 * @param batchCallFunction TODO未处理
	 * @param loginHistory 是否留痕   如果传true的话  当用户未登录状态，则用户下次登录可以收到    默认为false
	 * @param bean 业务数据
	 */
	public void sendDwr(String userId, String title, String showConfig, String callFunction, String batchCallFunction, Boolean loginHistory, Map<String, Object> bean);
	/**
	 * 发送站内信给一个人
	 * @param title  标题
	 * @param context  内容
	 * @param userId  要发送的人
	 */
	public void sendParcel(String title, String context, String userId);
	/**
	 * 发送站内信给一组人
	 * @param title  标题
	 * @param context  内容
	 * @param userIds  要发送的人
	 */	
	public void sendParcel(String title, String context, String[] userIds);
	/**
	 * 发送快递给一个人
	 * @param title  标题
	 * @param context  内容
	 * @param userId  要发送的人
	 * @param receipt  是否回执
	 */
	public void sendExpress(String title, String context, String userId, Boolean receipt);
	/**
	 * 发送快递给一组人
	 * @param title  标题
	 * @param context  内容
	 * @param userIds  要发送的人
	 * @param receipt  是否回执
	 */	
	public void sendExpress(String title, String context, String[] userIds, Boolean receipt);
	/**
	 * 以系统身份发送快递给一个人
	 * @param title  标题
	 * @param context  内容
	 * @param userId  要发送的人
	 */
	public void sendSystemExpress(String title, String context, String userId);
	/**
	 * 以系统身份发送快递给一组人
	 * @param title  标题
	 * @param context  内容
	 * @param userIds  要发送的人
	 */	
	public void sendSystemExpress(String title, String context, String[] userIds);
	/**
	 * 发送用户消息，在首页右上角的消息队列中
	 * @param userId 用户ID
	 * @param title 主题
	 * @param context 内容
	 * @param type 类型
	 */
	public void sendUserMsg(String userId, String title, String context, String type,List<UserMsgAppInfo> userMsgAppInfos);
	/**
	 * 发送消息
	 *  title 标题
	 *  counText 内容
	 *  userIds 用户Id数据
	 */
	public void sendUserMsg(String title, String countext, String[] userIds,List<UserMsgAppInfo> userMsgAppInfos);
	/**
	 * 发送消息 (该方法节省性能，不需要查询各个信息)
	 * @param username 用户名称
	 * @param userId 用户ID
	 * @param title 主题
	 * @param content 内容
	 * @param type 类型
	 * @param typeName 类型名称
	 * @param acceptTime TODO未处理
	 */
	public void sendUserMsg(String username, String userId, String title, String content, String type, String typeName, String pkValue,List<UserMsgAppInfo> userMsgAppInfos);
	/**
	 * 发送消息
	*  title 标题
	*  counText 内容
	*  whereSql 查询条件 (查询表为用户表， 内有 DEPTID,DEPTCODE,DEPTNAME部门信息，如果传空则为发送全部)
	*/
	public void sendUserMsg4Sql(String title, String countext, String whereSql,List<UserMsgAppInfo> userMsgAppInfos);
	/**
	 * 发送消息
	 * @param title 主题
	 * @param context 内容
	 * @param whereSql 条件
	 * @param type 类型
	 */
	public void sendUserMsg4Sql(String title, String context, String whereSql, String type,List<UserMsgAppInfo> userMsgAppInfos);
	/**
	 * 发送给用户指定的消息
	 * @param title 主题
	 * @param context 内容
	 * @param userSql 用户条件
	 * @param types TODO未处理
	 */
	public void send2User(String title, String context, String userSql, String[] types);
	
	/**
	 * 发送消息
	 * @param msgVo 消息
	 */
	public void sendWx(WxMsgVo msgVo);
	/**
	 * 发送消息
	 * @param msgVo 消息
	 */
	public void sendWxSync(WxMsgVo msgVo);
	/**
	 * 发送消息
	 * @param msgVo 消息
	 */
	public void sendWx(WxMsgVo msgVo, String jtgsId);
	/**
	 * 发送文字消息
	 * @param userIds 用户ID集合 多个按逗号隔开，如果是全部人员请传@all
	 * @param context 文字内容
	 */
	public void sendWxWz(String userIds, String context);
	/**
	 * 发送图文消息
	 * @param userIds 用户ID集合 多个按逗号隔开，如果是全部人员请传@all
	 * @param twType 图文内容类型    UEditor 编辑器内容 || HTML || URL 链接 
	 * @param context 图文内容
	 * @param picPath 图片地址
	 * @param author 作者
	 * @param description 摘要
	 */
	public void sendWxTw(String userIds, String twType, String title, String context, String picPath, String author, String description, String sourceUrl);
	/**
	 * 发送图片消息
	 * @param userIds 用户ID集合 多个按逗号隔开，如果是全部人员请传@all
	 * @param filePath 图片文件地址
	 */
	public void sendWxTp(String userIds, String filePath);
	/**
	 * 发送语音消息
	 * @param userIds 用户ID集合 多个按逗号隔开，如果是全部人员请传@all
	 * @param filePath 语音文件地址
	 */
	public void sendWxYy(String userIds, String filePath);
	/**
	 * 发送视频消息
	 * @param userIds 用户ID集合 多个按逗号隔开，如果是全部人员请传@all
	 * @param filePath 视频文件地址
	 * @param title 标题
	 * @param description 摘要
	 */
	public void sendWxSp(String userIds, String filePath, String title, String description);
	/**
	 * 发送文件消息
	 * @param userIds 用户ID集合 多个按逗号隔开，如果是全部人员请传@all
	 * @param filePath 文件地址
	 */
	public void sendWxWj(String userIds, String filePath);
	
}
