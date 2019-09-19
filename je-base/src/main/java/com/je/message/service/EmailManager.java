package com.je.message.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.Message;

import net.sf.json.JSONObject;

import com.je.core.entity.extjs.JSONTreeNode;
import com.je.core.util.bean.DynaBean;
import com.je.dd.vo.DicInfoVo;
import com.je.message.vo.EmailInfoVo;
import com.je.message.vo.EmailMsgVo;

/**
 * 邮箱消息  服务
 * @author zhangshuaipeng
 *
 */
public interface EmailManager {
	/**
	 * 发送消息
	 * @param msgVo 消息VO实例
	 */
	public void send(EmailMsgVo msgVo);
	/**
	 * 发送消息
	 * @param msgVo 消息VO实例
	 * @param jtgsId TODO未处理
	 */
	public void send(EmailMsgVo msgVo, String jtgsId);
	/**
	 * 发送邮件消息
	 * @param receiveEmail 接受邮箱
	 * @param subject 主题
	 * @param contextType 发送内容类型   文本：SendContextType.TEXT HTML:SendContextType.HTML
	 * @param context 发送内容
	 */
	public void send(String receiveEmail, String subject, String contextType, String context);
	/**
	 * 按照模版发送邮件
	 * @param receiveEmail 接受的邮箱
	 * @param code 编码
	 * @param params 传入信息
	 */
	public void send(String receiveEmail, String code, Map params);

	/**
	 * 发送带有附件的邮件
	 * @param receiveEmail 接受邮箱
	 * @param subject 主题
	 * @param contextType 内容类型   文本：SendContextType.TEXT HTML:SendContextType.HTML
	 * @param context 内容
	 * @param fileNames 文件名
	 * @param addresses 文件地址
	 */
	public void send(String receiveEmail, String subject, String contextType, String context, List<String> fileNames, List<String> addresses);
	/**
	 * 外部邮箱发送
	 * @param emailMsgVo 邮箱VO实例
	 * @param emailInfoVo 邮箱信息VO
	 * @param pkValue 父级值
	 */
	public void send(EmailMsgVo emailMsgVo, EmailInfoVo emailInfoVo, String pkValue);
	/**
	 * 接收指定单个邮箱索引的邮件
	 * @param emailInfoVo 邮箱VO实例
	 * @param returnObj TODO未处理
	 * @param dwrFlag TODO未处理
	 */
	public void receive(EmailInfoVo emailInfoVo, JSONObject returnObj, Boolean dwrFlag);
	/**
	 * 接收指定多个邮箱索引的邮件
	 * @param emailInfoVos TODO未处理
	 * @param returnObj TODO未处理
	 */
	public void receive(List<EmailInfoVo> emailInfoVos, JSONObject returnObj);
	/**
	 * 接收指定用户的所有邮箱的邮件
	 * @param endUserCode
	 * @return 共接收多少封邮件
	 */
	public int receive(String endUserCode);
	/**
	 * 接收在线用户的邮件,然后用Dwr技术提送到前台.
	 * @return
	 */
	public int receiveOnLineUser();
	/**
	 * 根据邮件的ID把邮件标记成删除,但是邮件先不删除.(假删除)
	 * @param ids
	 */
	public void deleteEmail(String[] ids);
	/**
	 * 得到收件信息的VO对象
	 * @param dynaBean  邮箱设置的表实例
	 * @return
	 */
	public EmailInfoVo getEmailInfoVo(DynaBean dynaBean);
	/**
	 * 构建发送消息对象
	 * @param dynaBean 传入信息
	 * @return
	 */
	public EmailMsgVo getEmailMsgVo(DynaBean dynaBean);
	/**
	 * 初始化系统通讯录
	 */
	public void initSysAddress();
	/**
	 * 初始化当前用户通讯录
	 */
	public void initUserAddress();
	/**
	 * 格式化地址
	 * @param address 地址
	 * @param userAddress 用户地址
	 * @return
	 */
	public String formatAddress(String address, HashMap<String, String> userAddress);
	/**
	 * 查询最近联系人
	 * @param dicInfoVo
	 * @return
	 */
	public JSONTreeNode loadAddress(DicInfoVo dicInfoVo);
}















