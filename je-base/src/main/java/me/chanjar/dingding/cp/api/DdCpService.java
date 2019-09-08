package me.chanjar.dingding.cp.api;

import com.dingtalk.api.request.OapiMessageCorpconversationAsyncsendV2Request;
import com.taobao.api.FileItem;
import me.chanjar.dingding.common.bean.result.DdMediaUploadResult;
import me.chanjar.dingding.common.exception.DdErrorException;
import me.chanjar.weixin.common.session.WxSession;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.dingding.common.util.http.RequestExecutor;
import me.chanjar.weixin.cp.api.WxCpConfigStorage;
import me.chanjar.weixin.cp.bean.WxCpDepart;
import me.chanjar.weixin.cp.bean.WxCpUser;

import java.io.File;
import java.io.InputStream;
import java.util.List;

/**
 * 钉钉API的Service
 */
public interface DdCpService {

  /**
   * 获取access_token, 不强制刷新access_token
   *
   * @see #getAccessToken(boolean)
   */
  String getAccessToken() throws DdErrorException;

  /**
   * <pre>
   * 获取access_token，本方法线程安全
   * 且在多线程同时刷新时只刷新一次，避免超出2000次/日的调用次数上限
   * 另：本service的所有方法都会在access_token过期是调用此方法
   * 程序员在非必要情况下尽量不要主动调用此方法
   * </pre>
   *
   * @param forceRefresh 强制刷新
   */
  String getAccessToken(boolean forceRefresh);

  /**
   * <pre>
   * 上传多媒体文件
   * 上传的多媒体文件有格式和大小限制，如下：
   * 图片（image）:1MB，支持JPG格式
   * 语音（voice）：2MB，播放长度不超过60s，AMR格式
   * 普通文件（file）：10MB
   * </pre>
   *
   * @param mediaType   媒体类型, 请看
   * @param fileName    文件类型，请看
   * @param inputStream 输入流
   */
  DdMediaUploadResult mediaUpload(String mediaType, String fileName, InputStream inputStream);

  /**
   * @param mediaType 媒体类型
   * @param file      文件对象
   * @see #mediaUpload(String, String, InputStream)
   */
  DdMediaUploadResult mediaUpload(String mediaType, File file);

  /**
   *
   * @param mediaType 类型
   * @param file 钉钉文件
   * @return
   */
  DdMediaUploadResult mediaUpload(String mediaType, FileItem file);

  /**
   * <pre>
   * 发送消息
   * </pre>
   *
   * @param request 要发送的消息对象
   */
  void messageSend(OapiMessageCorpconversationAsyncsendV2Request request) throws DdErrorException;


  /**
   * <pre>
   * 部门管理接口 - 创建部门
   * </pre>
   *
   * @param depart 部门
   * @return 部门id
   */
  Integer departCreate(WxCpDepart depart) throws DdErrorException;

  /**
   * <pre>
   * 部门管理接口 - 查询所有部门
   * </pre>
   */
  List<WxCpDepart> departGet() throws DdErrorException;

  /**
   * <pre>
   * 部门管理接口 - 修改部门名
   * </pre>
   *
   * @param group 要更新的group，group的id,name必须设置
   */
  void departUpdate(WxCpDepart group) throws DdErrorException;

  /**
   * <pre>
   * 部门管理接口 - 删除部门
   * </pre>
   *
   * @param departId 部门id
   */
  void departDelete(Integer departId) throws DdErrorException;

  /**
   * <pre>
   * 获取部门成员(详情)
   * </pre>
   *
   * @param departId   必填。部门id
   */
  List<WxCpUser> userList(String departId) throws DdErrorException;

  /**
   * <pre>
   * 获取部门成员
   * </pre>
   *
   * @param departId   必填。部门id
   * @param fetchChild 非必填。1/0：是否递归获取子部门下面的成员
   * @param status     非必填。0获取全部员工，1获取已关注成员列表，2获取禁用成员列表，4获取未关注成员列表。status可叠加
   */
  List<WxCpUser> departGetUsers(Integer departId, Boolean fetchChild, Integer status) throws DdErrorException;

  /**
   * 新建用户
   *
   * @param user 用户对象
   */
  String userCreate(WxCpUser user) throws DdErrorException;

  /**
   * 更新用户
   *
   * @param user 用户对象
   */
  String userUpdate(WxCpUser user) throws DdErrorException;

  /**
   * <pre>
   * 批量删除成员
   * </pre>
   *
   * @param userids 员工UserID列表。对应管理端的帐号
   */
  void userDelete(String[] userids) throws DdErrorException;

  /**
   * 获取用户
   *
   * @param userid 用户id
   */
  WxCpUser userGet(String userid) throws DdErrorException;

  /**
   * 当本Service没有实现某个API的时候，可以用这个，针对所有钉钉API中的GET请求
   *
   * @param url        接口地址
   * @param queryParam 请求参数
   */
  String get(String url, String queryParam) throws DdErrorException;

  /**
   * 当本Service没有实现某个API的时候，可以用这个，针对所有钉钉API中的POST请求
   *
   * @param url      接口地址
   * @param postData 请求body字符串
   */
  String post(String url, String postData) throws DdErrorException;

  String execute(OapiMessageCorpconversationAsyncsendV2Request request) throws DdErrorException;
  /**
   * 发送钉钉消息
   * @param request 请求消息体
   * @return
   */
  String msgPost(OapiMessageCorpconversationAsyncsendV2Request request) throws DdErrorException;

  /**
   * <pre>
   * Service没有实现某个API的时候，可以用这个，
   * 比{@link #get}和{@link #post}方法更灵活，可以自己构造RequestExecutor用来处理不同的参数和不同的返回类型。
   * 可以参考，{@link me.chanjar.weixin.common.util.http.MediaUploadRequestExecutor}的实现方法
   * </pre>
   *
   * @param executor 执行器
   * @param uri      请求地址
   * @param data     参数
   * @param <T>      请求值类型
   * @param <E>      返回值类型
   */
  <T, E> T execute(RequestExecutor<T, E> executor, String uri, E data) throws DdErrorException;

  /**
   * 注入 {@link WxCpConfigStorage} 的实现
   *
   * @param ddConfigProvider 配置对象
   */
  void setWxCpConfigStorage(DdCpConfigStorage ddConfigProvider);

  /**
   * <pre>
   * 设置当钉钉系统响应系统繁忙时，要等待多少 retrySleepMillis(ms) * 2^(重试次数 - 1) 再发起重试
   * 默认：1000ms
   * </pre>
   *
   * @param retrySleepMillis 重试休息时间
   */
  void setRetrySleepMillis(int retrySleepMillis);

  /**
   * <pre>
   * 设置当钉钉系统响应系统繁忙时，最大重试次数
   * 默认：5次
   * </pre>
   *
   * @param maxRetryTimes 最大重试次数
   */
  void setMaxRetryTimes(int maxRetryTimes);

  /**
   * 获取某个sessionId对应的session,如果sessionId没有对应的session，则新建一个并返回。
   *
   * @param id id可以为任意字符串，建议使用FromUserName作为id
   */
  WxSession getSession(String id);

  /**
   * 获取某个sessionId对应的session,如果sessionId没有对应的session，若create为true则新建一个，否则返回null。
   *
   * @param id     id可以为任意字符串，建议使用FromUserName作为id
   * @param create 是否新建
   */
  WxSession getSession(String id, boolean create);

  /**
   * <pre>
   * 设置WxSessionManager，只有当需要使用个性化的WxSessionManager的时候才需要调用此方法，
   * WxCpService默认使用的是{@link me.chanjar.weixin.common.session.StandardSessionManager}
   * </pre>
   *
   * @param sessionManager 会话管理器
   */
  void setSessionManager(WxSessionManager sessionManager);

}
