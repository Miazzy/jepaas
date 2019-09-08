package me.chanjar.weixin.mp.api;

import me.chanjar.weixin.common.bean.result.WxMediaUploadResult;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.mp.bean.material.WxMpMaterial;
import me.chanjar.weixin.mp.bean.material.WxMpMaterialArticleUpdate;
import me.chanjar.weixin.mp.bean.material.WxMpMaterialNews;
import me.chanjar.weixin.mp.bean.material.*;

import java.io.File;
import java.io.InputStream;

/**
 * <pre>
 * Created by Binary Wang on 2016/7/21.
 * 素材管理的相关接口，包括媒体管理的接口，
 * 即以https://api.weixin.qq.com/cgi-bin/material
 * 和 https://api.weixin.qq.com/cgi-bin/media开头的接口
 * </pre>
 */
public interface WxMpMaterialService {

  /**
   * <pre>
   * 新增临时素材
   * 公众号经常有需要用到一些临时性的多媒体素材的场景，例如在使用接口特别是发送消息时，对多媒体文件、多媒体消息的获取和调用等操作，是通过media_id来进行的。
   * 素材管理接口对所有认证的订阅号和服务号开放。通过本接口，公众号可以新增临时素材（即上传临时多媒体文件）。
   * 请注意：
   *  1、对于临时素材，每个素材（media_id）会在开发者上传或粉丝发送到微信服务器3天后自动删除（所以用户发送给开发者的素材，若开发者需要，应尽快下载到本地），以节省服务器资源。
   *  2、media_id是可复用的。
   *  3、素材的格式大小等要求与公众平台官网一致。具体是，图片大小不超过2M，支持png/jpeg/jpg/gif格式，语音大小不超过5M，长度不超过60秒，支持mp3/amr格式
   *  4、需使用https调用本接口。
   *  本接口即为原“上传多媒体文件”接口。
   *  注意事项：
   *    上传的临时多媒体文件有格式和大小限制，如下：
   *    图片（image）: 2M，支持PNG\JPEG\JPG\GIF格式
   *    语音（voice）：2M，播放长度不超过60s，支持AMR\MP3格式
   *    视频（video）：10MB，支持MP4格式
   *    缩略图（thumb）：64KB，支持JPG格式
   *媒体文件在后台保存时间为3天，即3天后media_id失效。
   * 详情请见: <a href="http://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1444738726&token=&lang=zh_CN">新增临时素材</a>
   * 接口url格式：https://api.weixin.qq.com/cgi-bin/media/upload?access_token=ACCESS_TOKEN&type=TYPE
   * </pre>
   * @param mediaType 媒体类型, 请看{@link me.chanjar.weixin.common.api.WxConsts}
   * @param file 文件对象
   * @throws WxErrorException
   * @see #mediaUpload(String, String, InputStream)
   */
  WxMediaUploadResult mediaUpload(String mediaType, File file) throws WxErrorException;

  /**
   * <pre>
   * 新增临时素材
   * 本接口即为原“上传多媒体文件”接口。
   *
   * 详情请见: <a href="http://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1444738726&token=&lang=zh_CN">新增临时素材</a>
   * 接口url格式：https://api.weixin.qq.com/cgi-bin/media/upload?access_token=ACCESS_TOKEN&type=TYPE
   * </pre>
   *
   * @param mediaType   媒体类型, 请看{@link me.chanjar.weixin.common.api.WxConsts}
   * @param fileType    文件类型，请看{@link me.chanjar.weixin.common.api.WxConsts}
   * @param inputStream 输入流
   * @throws WxErrorException
   * @see #mediaUpload(String, File)
   */
  WxMediaUploadResult mediaUpload(String mediaType, String fileType, InputStream inputStream) throws WxErrorException;

  /**
   * <pre>
   * 获取临时素材
   * 公众号可以使用本接口获取临时素材（即下载临时的多媒体文件）。请注意，视频文件不支持https下载，调用该接口需http协议。
   * 本接口即为原“下载多媒体文件”接口。
   * 根据微信文档，视频文件下载不了，会返回null
   * 详情请见: <a href="http://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1444738727&token=&lang=zh_CN">获取临时素材</a>
   * 接口url格式：https://api.weixin.qq.com/cgi-bin/media/get?access_token=ACCESS_TOKEN&media_id=MEDIA_ID
   * </pre>
   *
   * @param media_id
   * @return 保存到本地的临时文件
   * @throws WxErrorException
   */
  File mediaDownload(String media_id) throws WxErrorException;

  /**
   * <pre>
   * 上传图文消息内的图片获取URL
   * 请注意，本接口所上传的图片不占用公众号的素材库中图片数量的5000个的限制。图片仅支持jpg/png格式，大小必须在1MB以下。
   * 详情请见: <a href="http://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1444738729&token=&lang=zh_CN">新增永久素材</a>
   * 接口url格式：https://api.weixin.qq.com/cgi-bin/media/uploadimg?access_token=ACCESS_TOKEN
   * </pre>
   *
   * @param file 上传的文件对象
   * @return WxMediaImgUploadResult 返回图片url
   * @throws WxErrorException
   */
  WxMediaImgUploadResult mediaImgUpload(File file) throws WxErrorException;

  /**
   * <pre>
   * 新增非图文永久素材
   * 通过POST表单来调用接口，表单id为media，包含需要上传的素材内容，有filename、filelength、content-type等信息。请注意：图片素材将进入公众平台官网素材管理模块中的默认分组。
   * 新增永久视频素材需特别注意：
   * 在上传视频素材时需要POST另一个表单，id为description，包含素材的描述信息，内容格式为JSON，格式如下：
   * {   "title":VIDEO_TITLE,   "introduction":INTRODUCTION   }
   * 详情请见: <a href="http://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1444738729&token=&lang=zh_CN">新增永久素材</a>
   * 接口url格式：https://api.weixin.qq.com/cgi-bin/material/add_material?access_token=ACCESS_TOKEN&type=TYPE
   *
   * 除了3天就会失效的临时素材外，开发者有时需要永久保存一些素材，届时就可以通过本接口新增永久素材。
   永久图片素材新增后，将带有URL返回给开发者，开发者可以在腾讯系域名内使用（腾讯系域名外使用，图片将被屏蔽）。
   请注意：
   1、新增的永久素材也可以在公众平台官网素材管理模块中看到
   2、永久素材的数量是有上限的，请谨慎新增。图文消息素材和图片素材的上限为5000，其他类型为1000
   3、素材的格式大小等要求与公众平台官网一致。具体是，图片大小不超过2M，支持bmp/png/jpeg/jpg/gif格式，语音大小不超过5M，长度不超过60秒，支持mp3/wma/wav/amr格式
   4、调用该接口需https协议
   * </pre>
   *
   * @param mediaType 媒体类型, 请看{@link me.chanjar.weixin.common.api.WxConsts}
   * @param material  上传的素材, 请看{@link WxMpMaterial}
   */
  WxMpMaterialUploadResult materialFileUpload(String mediaType, WxMpMaterial material) throws WxErrorException;

  /**
   * <pre>
   * 新增永久图文素材
   *
   * 详情请见: <a href="http://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1444738729&token=&lang=zh_CN">新增永久素材</a>
   * 接口url格式：https://api.weixin.qq.com/cgi-bin/material/add_news?access_token=ACCESS_TOKEN
   *
   * 除了3天就会失效的临时素材外，开发者有时需要永久保存一些素材，届时就可以通过本接口新增永久素材。
   永久图片素材新增后，将带有URL返回给开发者，开发者可以在腾讯系域名内使用（腾讯系域名外使用，图片将被屏蔽）。
   请注意：
   1、新增的永久素材也可以在公众平台官网素材管理模块中看到
   2、永久素材的数量是有上限的，请谨慎新增。图文消息素材和图片素材的上限为5000，其他类型为1000
   3、素材的格式大小等要求与公众平台官网一致。具体是，图片大小不超过2M，支持bmp/png/jpeg/jpg/gif格式，语音大小不超过5M，长度不超过60秒，支持mp3/wma/wav/amr格式
   4、调用该接口需https协议
   * </pre>
   *
   * @param news 上传的图文消息, 请看{@link WxMpMaterialNews}
   */
  WxMpMaterialUploadResult materialNewsUpload(WxMpMaterialNews news) throws WxErrorException;

  /**
   * <pre>
   * 获取声音或者图片永久素材
   *
   * 详情请见: <a href="http://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1444738729&token=&lang=zh_CN">获取永久素材</a>
   * 接口url格式：https://api.weixin.qq.com/cgi-bin/material/get_material?access_token=ACCESS_TOKEN
   * </pre>
   *
   * @param mediaId 永久素材的id
   */
  InputStream materialImageOrVoiceDownload(String mediaId) throws WxErrorException;

  /**
   * <pre>
   * 获取视频永久素材的信息和下载地址
   *
   * 详情请见: <a href="http://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1444738729&token=&lang=zh_CN">获取永久素材</a>
   * 接口url格式：https://api.weixin.qq.com/cgi-bin/material/get_material?access_token=ACCESS_TOKEN
   * </pre>
   *
   * @param mediaId 永久素材的id
   */
  WxMpMaterialVideoInfoResult materialVideoInfo(String mediaId) throws WxErrorException;

  /**
   * <pre>
   * 获取图文永久素材的信息
   *
   * 详情请见: <a href="http://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1444738729&token=&lang=zh_CN">获取永久素材</a>
   * 接口url格式：https://api.weixin.qq.com/cgi-bin/material/get_material?access_token=ACCESS_TOKEN
   * </pre>
   *
   * @param mediaId 永久素材的id
   */
  WxMpMaterialNews materialNewsInfo(String mediaId) throws WxErrorException;

  /**
   * <pre>
   * 修改永久图文素材
   *
   * 详情请见: <a href="http://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1444738732&token=&lang=zh_CN">修改永久图文素材</a>
   * 接口url格式：https://api.weixin.qq.com/cgi-bin/material/update_news?access_token=ACCESS_TOKEN
   * </pre>
   *
   * @param wxMpMaterialArticleUpdate 用来更新图文素材的bean, 请看{@link WxMpMaterialArticleUpdate}
   */
  boolean materialNewsUpdate(WxMpMaterialArticleUpdate wxMpMaterialArticleUpdate) throws WxErrorException;

  /**
   * <pre>
   * 删除永久素材
   * 在新增了永久素材后，开发者可以根据本接口来删除不再需要的永久素材，节省空间。
   * 请注意：
   *  1、请谨慎操作本接口，因为它可以删除公众号在公众平台官网素材管理模块中新建的图文消息、语音、视频等素材（但需要先通过获取素材列表来获知素材的media_id）
   *  2、临时素材无法通过本接口删除
   *  3、调用该接口需https协议
   * 详情请见: <a href="http://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1444738731&token=&lang=zh_CN">删除永久素材</a>
   * 接口url格式：https://api.weixin.qq.com/cgi-bin/material/del_material?access_token=ACCESS_TOKEN
   * </pre>
   *
   * @param mediaId 永久素材的id
   */
  boolean materialDelete(String mediaId) throws WxErrorException;

  /**
   * <pre>
   * 获取各类素材总数
   * 开发者可以根据本接口来获取永久素材的列表，需要时也可保存到本地。
   * 请注意：
   *  1.永久素材的总数，也会计算公众平台官网素材管理中的素材
   *  2.图片和图文消息素材（包括单图文和多图文）的总数上限为5000，其他素材的总数上限为1000
   *  3.调用该接口需https协议
   *
   * 详情请见: <a href="http://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1444738733&token=&lang=zh_CN">获取素材总数</a>
   * 接口url格式：https://api.weixin.qq.com/cgi-bin/material/get_materialcount?access_token=ACCESS_TOKEN
   * </pre>
   */
  WxMpMaterialCountResult materialCount() throws WxErrorException;

  /**
   * <pre>
   * 分页获取图文素材列表
   *
   * 详情请见: <a href="http://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1444738734&token=&lang=zh_CN">获取素材列表</a>
   * 接口url格式：https://api.weixin.qq.com/cgi-bin/material/batchget_material?access_token=ACCESS_TOKEN
   * </pre>
   *
   * @param offset 从全部素材的该偏移位置开始返回，0表示从第一个素材 返回
   * @param count  返回素材的数量，取值在1到20之间
   */
  WxMpMaterialNewsBatchGetResult materialNewsBatchGet(int offset, int count) throws WxErrorException;

  /**
   * <pre>
   * 分页获取其他媒体素材列表
   *
   * 详情请见: <a href="http://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1444738734&token=&lang=zh_CN">获取素材列表</a>
   * 接口url格式：https://api.weixin.qq.com/cgi-bin/material/batchget_material?access_token=ACCESS_TOKEN
   * </pre>
   *
   * @param type   媒体类型, 请看{@link me.chanjar.weixin.common.api.WxConsts}
   * @param offset 从全部素材的该偏移位置开始返回，0表示从第一个素材 返回
   * @param count  返回素材的数量，取值在1到20之间
   */
  WxMpMaterialFileBatchGetResult materialFileBatchGet(String type, int offset, int count) throws WxErrorException;

}
