package me.chanjar.weixin.mp.bean.datacube;

import com.google.gson.JsonParser;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import me.chanjar.weixin.mp.util.json.WxMpGsonBuilder;

import java.util.List;

/**
 * 图文分析数据接口返回结果对象
 * @author <a href="https://github.com/binarywang">binarywang(Binary Wang)</a>
 *         Created by Binary Wang on 2016/8/24.
 */
public class WxDataCubeArticleResult extends WxDataCubeBaseResult {

  private static final JsonParser JSON_PARSER = new JsonParser();

  /**
   * ref_hour
   * 数据的小时，包括从000到2300，分别代表的是[000,100)到[2300,2400)，即每日的第1小时和最后1小时
   */
  @SerializedName("ref_hour")
  private Integer refHour;

  /**
   * msgid
   * 请注意：这里的msgid实际上是由msgid（图文消息id，这也就是群发接口调用后返回的msg_data_id）
   * 和index（消息次序索引）组成， 例如12003_3， 其中12003是msgid，即一次群发的消息的id； 3为index，
   * 假设该次群发的图文消息共5个文章（因为可能为多图文），3表示5个中的第3个
   */
  @SerializedName("msgid")
  private String msgId;

  /**
   * title
   * 图文消息的标题
   */
  @SerializedName("title")
  private String title;

  /**
   * int_page_read_user
   * 图文页（点击群发图文卡片进入的页面）的阅读人数
   */
  @SerializedName("int_page_read_user")
  private Integer intPageReadUser;

  /**
   * int_page_read_count
   * 图文页的阅读次数
   */
  @SerializedName("int_page_read_count")
  private Integer intPageReadCount;

  /**
   * ori_page_read_user
   * 原文页（点击图文页“阅读原文”进入的页面）的阅读人数，无原文页时此处数据为0
   */
  @SerializedName("ori_page_read_user")
  private Integer oriPageReadUser;

  /**
   * ori_page_read_count
   * 原文页的阅读次数
   */
  @SerializedName("ori_page_read_count")
  private Integer oriPageReadCount;

  /**
   * share_scene
   * 分享的场景 1代表好友转发 2代表朋友圈 3代表腾讯微博 255代表其他
   */
  @SerializedName("share_scene")
  private Integer shareScene;

  /**
   * share_user
   * 分享的人数
   */
  @SerializedName("share_user")
  private Integer shareUser;

  /**
   * share_count
   * 分享的次数
   */
  @SerializedName("share_count")
  private Integer shareCount;

  /**
   * add_to_fav_user
   * 收藏的人数
   */
  @SerializedName("add_to_fav_user")
  private Integer addToFavUser;

  /**
   * add_to_fav_count
   * 收藏的次数
   */
  @SerializedName("add_to_fav_count")
  private Integer addToFavCount;

  /**
   * user_source
   * 在获取图文阅读分时数据时才有该字段，代表用户从哪里进入来阅读该图文。0:会话;1.好友;2.朋友圈;3.腾讯微博;4.历史消息页;5.其他
   */
  @SerializedName("user_source")
  private Integer userSource;

  public Integer getRefHour() {
    return this.refHour;
  }

  public void setRefHour(Integer refHour) {
    this.refHour = refHour;
  }

  public String getMsgId() {
    return this.msgId;
  }

  public void setMsgId(String msgId) {
    this.msgId = msgId;
  }

  public String getTitle() {
    return this.title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public Integer getIntPageReadUser() {
    return this.intPageReadUser;
  }

  public void setIntPageReadUser(Integer intPageReadUser) {
    this.intPageReadUser = intPageReadUser;
  }

  public Integer getIntPageReadCount() {
    return this.intPageReadCount;
  }

  public void setIntPageReadCount(Integer intPageReadCount) {
    this.intPageReadCount = intPageReadCount;
  }

  public Integer getOriPageReadUser() {
    return this.oriPageReadUser;
  }

  public void setOriPageReadUser(Integer oriPageReadUser) {
    this.oriPageReadUser = oriPageReadUser;
  }

  public Integer getOriPageReadCount() {
    return this.oriPageReadCount;
  }

  public void setOriPageReadCount(Integer oriPageReadCount) {
    this.oriPageReadCount = oriPageReadCount;
  }

  public Integer getShareScene() {
    return this.shareScene;
  }

  public void setShareScene(Integer shareScene) {
    this.shareScene = shareScene;
  }

  public Integer getShareUser() {
    return this.shareUser;
  }

  public void setShareUser(Integer shareUser) {
    this.shareUser = shareUser;
  }

  public Integer getShareCount() {
    return this.shareCount;
  }

  public void setShareCount(Integer shareCount) {
    this.shareCount = shareCount;
  }

  public Integer getAddToFavUser() {
    return this.addToFavUser;
  }

  public void setAddToFavUser(Integer addToFavUser) {
    this.addToFavUser = addToFavUser;
  }

  public Integer getAddToFavCount() {
    return this.addToFavCount;
  }

  public void setAddToFavCount(Integer addToFavCount) {
    this.addToFavCount = addToFavCount;
  }

  public Integer getUserSource() {
    return this.userSource;
  }

  public void setUserSource(Integer userSource) {
    this.userSource = userSource;
  }

  public static List<WxDataCubeArticleResult> fromJson(String json) {
    return WxMpGsonBuilder.INSTANCE.create().fromJson(
        JSON_PARSER.parse(json).getAsJsonObject().get("list"),
        new TypeToken<List<WxDataCubeArticleResult>>() {
        }.getType());
  }
}
