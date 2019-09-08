package me.chanjar.weixin.mp.api;

import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.mp.bean.kefu.WxMpKefuMessage;
import me.chanjar.weixin.mp.bean.kefu.request.WxMpKfAccountRequest;
import me.chanjar.weixin.mp.bean.kefu.result.*;

import java.io.File;
import java.util.Date;

/**
 * 客服接口 ，
 * 命名采用kefu拼音的原因是：
 * 其英文CustomerService如果再加上Service后缀显得有点啰嗦，
 * 如果不加又显得表意不完整
 *
 * @author Binary Wang
 */
public interface WxMpKefuService {

  /**
   * <pre>
   * 发送客服消息
   * 详情请见: <a href="http://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1421140547&token=&lang=zh_CN">发送客服消息</a>
   * 接口url格式：https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=ACCESS_TOKEN
   * </pre>
   */
  boolean sendKefuMessage(WxMpKefuMessage message) throws WxErrorException;

  //*******************客服管理接口***********************//

  /**
   * <pre>
   * 获取客服基本信息
   * 详情请见：<a href="http://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1458044813&token=&lang=zh_CN">客服管理</a>
   * 接口url格式：https://api.weixin.qq.com/cgi-bin/customservice/getkflist?access_token=ACCESS_TOKEN
   * </pre>
   */
  WxMpKfList kfList() throws WxErrorException;

  /**
   * <pre>
   * 获取在线客服接待信息
   * 详情请见：<a href="http://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1458044813&token=&lang=zh_CN">客服管理</a>
   * 接口url格式：https://api.weixin.qq.com/cgi-bin/customservice/getonlinekflist?access_token=ACCESS_TOKEN
   * </pre>
   */
  WxMpKfOnlineList kfOnlineList() throws WxErrorException;

  /**
   * <pre>
   * 添加客服账号
   * 详情请见：<a href="http://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1458044813&token=&lang=zh_CN">客服管理</a>
   * 接口url格式：https://api.weixin.qq.com/customservice/kfaccount/add?access_token=ACCESS_TOKEN
   * </pre>
   */
  boolean kfAccountAdd(WxMpKfAccountRequest request) throws WxErrorException;

  /**
   * <pre>
   * 设置客服信息（即更新客服信息）
   * 详情请见：<a href="http://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1458044813&token=&lang=zh_CN">客服管理</a>
   * 接口url格式：https://api.weixin.qq.com/customservice/kfaccount/update?access_token=ACCESS_TOKEN
   * </pre>
   */
  boolean kfAccountUpdate(WxMpKfAccountRequest request) throws WxErrorException;

  /**
   * <pre>
   * 设置客服信息（即更新客服信息）
   * 详情请见：<a href="http://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1458044813&token=&lang=zh_CN">客服管理</a>
   * 接口url格式：https://api.weixin.qq.com/customservice/kfaccount/inviteworker?access_token=ACCESS_TOKEN
   * </pre>
   */
  boolean kfAccountInviteWorker(WxMpKfAccountRequest request) throws WxErrorException;

  /**
   * <pre>
   * 上传客服头像
   * 详情请见：<a href="http://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1458044813&token=&lang=zh_CN">客服管理</a>
   * 接口url格式：http://api.weixin.qq.com/customservice/kfaccount/uploadheadimg?access_token=ACCESS_TOKEN&kf_account=KFACCOUNT
   * </pre>
   */
  boolean kfAccountUploadHeadImg(String kfAccount, File imgFile)
          throws WxErrorException;

  /**
   * <pre>
   * 删除客服账号
   * 详情请见：<a href="http://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1458044813&token=&lang=zh_CN">客服管理</a>
   * 接口url格式：https://api.weixin.qq.com/customservice/kfaccount/del?access_token=ACCESS_TOKEN&kf_account=KFACCOUNT
   * </pre>
   */
  boolean kfAccountDel(String kfAccount) throws WxErrorException;

  //*******************客服会话控制接口***********************//

  /**
   * <pre>
   * 创建会话
   * 此接口在客服和用户之间创建一个会话，如果该客服和用户会话已存在，则直接返回0。指定的客服帐号必须已经绑定微信号且在线。
   * 详情请见：<a href="http://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1458044820&token=&lang=zh_CN">客服会话控制接口</a>
   * 接口url格式： https://api.weixin.qq.com/customservice/kfsession/create?access_token=ACCESS_TOKEN
   * </pre>
   */
  boolean kfSessionCreate(String openid, String kfAccount) throws WxErrorException;

  /**
   * <pre>
   * 关闭会话
   * 开发者可以使用本接口，关闭一个会话。
   * 详情请见：<a href="http://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1458044820&token=&lang=zh_CN">客服会话控制接口</a>
   * 接口url格式： https://api.weixin.qq.com/customservice/kfsession/close?access_token=ACCESS_TOKEN
   * </pre>
   */
  boolean kfSessionClose(String openid, String kfAccount) throws WxErrorException;

  /**
   * <pre>
   * 获取客户的会话状态
   * 此接口获取一个客户的会话，如果不存在，则kf_account为空。
   * 详情请见：<a href="http://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1458044820&token=&lang=zh_CN">客服会话控制接口</a>
   * 接口url格式： https://api.weixin.qq.com/customservice/kfsession/getsession?access_token=ACCESS_TOKEN&openid=OPENID
   * </pre>
   */
  WxMpKfSessionGetResult kfSessionGet(String openid) throws WxErrorException;

  /**
   * <pre>
   * 获取客服的会话列表
   * 开发者可以通过本接口获取某个客服正在接待的会话列表。
   * 详情请见：<a href="http://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1458044820&token=&lang=zh_CN">客服会话控制</a>
   * 接口url格式： https://api.weixin.qq.com/customservice/kfsession/getsessionlist?access_token=ACCESS_TOKEN&kf_account=KFACCOUNT
   * </pre>
   */
  WxMpKfSessionList kfSessionList(String kfAccount) throws WxErrorException;

  /**
   * <pre>
   * 获取未接入会话列表
   * 开发者可以通过本接口获取当前正在等待队列中的会话列表，此接口最多返回最早进入队列的100个未接入会话。
   * 详情请见：<a href="http://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1458044820&token=&lang=zh_CN">客服会话控制</a>
   * 接口url格式： https://api.weixin.qq.com/customservice/kfsession/getwaitcase?access_token=ACCESS_TOKEN
   * </pre>
   */
  WxMpKfSessionWaitCaseList kfSessionGetWaitCase() throws WxErrorException;

  //*******************获取聊天记录的接口***********************//

  /**
   * <pre>
   * 获取聊天记录（原始接口）
   * 此接口返回的聊天记录中，对于图片、语音、视频，分别展示成文本格式的[image]、[voice]、[video]
   * 详情请见：<a href="http://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1464937269_mUtmK&token=&lang=zh_CN">获取聊天记录</a>
   * 接口url格式： https://api.weixin.qq.com/customservice/msgrecord/getmsglist?access_token=ACCESS_TOKEN
   * </pre>
   *
   * @param startTime 起始时间
   * @param endTime   结束时间
   * @param msgId     消息id顺序从小到大，从1开始
   * @param number    每次获取条数，最多10000条
   * @return 聊天记录对象
   * @throws WxErrorException
   */
  WxMpKfMsgList kfMsgList(Date startTime, Date endTime, Long msgId, Integer number) throws WxErrorException;

  /**
   * <pre>
   * 获取聊天记录（优化接口，返回指定时间段内所有的聊天记录）
   * 此接口返回的聊天记录中，对于图片、语音、视频，分别展示成文本格式的[image]、[voice]、[video]
   * 详情请见：<a href="http://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1464937269_mUtmK&token=&lang=zh_CN">获取聊天记录</a>
   * 接口url格式： https://api.weixin.qq.com/customservice/msgrecord/getmsglist?access_token=ACCESS_TOKEN
   * </pre>
   *
   * @param startTime 起始时间
   * @param endTime   结束时间
   * @return 聊天记录对象
   * @throws WxErrorException
   */
  WxMpKfMsgList kfMsgList(Date startTime, Date endTime) throws WxErrorException;

}
