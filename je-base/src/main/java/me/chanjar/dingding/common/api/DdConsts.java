package me.chanjar.dingding.common.api;

import java.util.HashMap;
import java.util.Map;

public class DdConsts {

  ///////////////////////
  // 钉钉推送过来的消息的类型，和发送给钉钉xml格式消息的消息类型
  ///////////////////////
  public static final String XML_MSG_TEXT = "text";
  public static final String XML_MSG_IMAGE = "image";
  public static final String XML_MSG_VOICE = "voice";
  public static final String XML_MSG_SHORTVIDEO = "shortvideo";
  public static final String XML_MSG_VIDEO = "video";
  public static final String XML_MSG_NEWS = "news";
  public static final String XML_MSG_MUSIC = "music";
  public static final String XML_MSG_LOCATION = "location";
  public static final String XML_MSG_LINK = "link";
  public static final String XML_MSG_EVENT = "event";
  public static final String XML_MSG_DEVICE_TEXT = "device_text";
  public static final String XML_MSG_DEVICE_EVENT = "device_event";
  public static final String XML_MSG_DEVICE_STATUS = "device_status";
  public static final String XML_MSG_HARDWARE = "hardware";
  public static final String XML_TRANSFER_CUSTOMER_SERVICE = "transfer_customer_service";


  ///////////////////////
  // 主动发送消息(即客服消息)的消息类型
  ///////////////////////
  public static final String CUSTOM_MSG_TEXT = "text";//文本消息
  public static final String CUSTOM_MSG_IMAGE = "image";//图片消息
  public static final String CUSTOM_MSG_VOICE = "voice";//语音消息
  public static final String CUSTOM_MSG_VIDEO = "video";//视频消息
  public static final String CUSTOM_MSG_MUSIC = "music";//音乐消息
  public static final String CUSTOM_MSG_NEWS = "news";//图文消息（点击跳转到外链）
  public static final String CUSTOM_MSG_MPNEWS = "mpnews";//图文消息（点击跳转到图文消息页面）
  public static final String CUSTOM_MSG_FILE = "file";//发送文件（CP专用）
  public static final String CUSTOM_MSG_WXCARD = "wxcard";//卡券消息
  public static final String CUSTOM_MSG_TRANSFER_CUSTOMER_SERVICE = "transfer_customer_service";
  public static final String CUSTOM_MSG_SAFE_NO = "0";
  public static final String CUSTOM_MSG_SAFE_YES = "1";

  ///////////////////////
  // 群发消息的消息类型
  ///////////////////////
  public static final String MASS_MSG_NEWS = "mpnews";
  public static final String MASS_MSG_TEXT = "text";
  public static final String MASS_MSG_VOICE = "voice";
  public static final String MASS_MSG_IMAGE = "image";
  public static final String MASS_MSG_VIDEO = "mpvideo";

  ///////////////////////
  // 群发消息后钉钉端推送给服务器的反馈消息
  ///////////////////////
  public static final String MASS_ST_SUCCESS = "send success";
  public static final String MASS_ST_FAIL = "send fail";
  public static final String MASS_ST_10001 = "err(10001)";
  public static final String MASS_ST_20001 = "err(20001)";
  public static final String MASS_ST_20004 = "err(20004)";
  public static final String MASS_ST_20002 = "err(20002)";
  public static final String MASS_ST_20006 = "err(20006)";
  public static final String MASS_ST_20008 = "err(20008)";
  public static final String MASS_ST_20013 = "err(20013)";
  public static final String MASS_ST_22000 = "err(22000)";
  public static final String MASS_ST_21000 = "err(21000)";

  /**
   * 群发反馈消息代码所对应的文字描述
   */
  public static final Map<String, String> MASS_ST_2_DESC = new HashMap<String, String>();

  //以下为钉钉认证事件
  /**
   * 资质认证成功
   */
  public static final String EVT_QUALIFICATION_VERIFY_SUCCESS = "qualification_verify_success";
  /**
   * 资质认证失败
   */
  public static final String EVT_QUALIFICATION_VERIFY_FAIL = "qualification_verify_fail";
  /**
   * 名称认证成功
   */
  public static final String EVT_NAMING_VERIFY_SUCCESS = "naming_verify_success";
  /**
   * 名称认证失败
   */
  public static final String EVT_NAMING_VERIFY_FAIL = "naming_verify_fail";
  /**
   * 年审通知
   */
  public static final String EVT_ANNUAL_RENEW = "annual_renew";
  /**
   * 认证过期失效通知
   */
  public static final String EVT_VERIFY_EXPIRED = "verify_expired";

  ///////////////////////
  // 上传多媒体文件的类型
  ///////////////////////
  public static final String MEDIA_IMAGE = "image";
  public static final String MEDIA_VOICE = "voice";
  public static final String MEDIA_LINK = "link";
  public static final String MEDIA_THUMB = "thumb";
  public static final String MEDIA_FILE = "file";
  public static final String OA = "oa";
  public static final String MARKDOWN = "markdown";
  public static final String ACTIONCARD = "action_card";

  ///////////////////////
  // 永久素材类型
  ///////////////////////
  public static final String MATERIAL_NEWS = "news";
  public static final String MATERIAL_VOICE = "voice";
  public static final String MATERIAL_IMAGE = "image";
  public static final String MATERIAL_VIDEO = "video";

  static {
    MASS_ST_2_DESC.put(MASS_ST_SUCCESS, "发送成功");
    MASS_ST_2_DESC.put(MASS_ST_FAIL, "发送失败");
    MASS_ST_2_DESC.put(MASS_ST_10001, "涉嫌广告");
    MASS_ST_2_DESC.put(MASS_ST_20001, "涉嫌政治");
    MASS_ST_2_DESC.put(MASS_ST_20004, "涉嫌社会");
    MASS_ST_2_DESC.put(MASS_ST_20002, "涉嫌色情");
    MASS_ST_2_DESC.put(MASS_ST_20006, "涉嫌违法犯罪");
    MASS_ST_2_DESC.put(MASS_ST_20008, "涉嫌欺诈");
    MASS_ST_2_DESC.put(MASS_ST_20013, "涉嫌版权");
    MASS_ST_2_DESC.put(MASS_ST_22000, "涉嫌互推_互相宣传");
    MASS_ST_2_DESC.put(MASS_ST_21000, "涉嫌其他");
  }
}
