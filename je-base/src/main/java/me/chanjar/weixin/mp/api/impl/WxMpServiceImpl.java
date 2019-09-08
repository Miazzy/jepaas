package me.chanjar.weixin.mp.api.impl;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.locks.Lock;

import me.chanjar.weixin.common.bean.WxAccessToken;
import me.chanjar.weixin.common.bean.WxJsapiSignature;
import me.chanjar.weixin.common.bean.result.WxError;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.common.session.StandardSessionManager;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.common.util.RandomUtils;
import me.chanjar.weixin.common.util.crypto.SHA1;
import me.chanjar.weixin.common.util.http.ApacheHttpClientBuilder;
import me.chanjar.weixin.common.util.http.DefaultApacheHttpClientBuilder;
import me.chanjar.weixin.common.util.http.RequestExecutor;
import me.chanjar.weixin.common.util.http.SimpleGetRequestExecutor;
import me.chanjar.weixin.common.util.http.SimplePostRequestExecutor;
import me.chanjar.weixin.common.util.http.URIUtil;
import me.chanjar.weixin.mp.api.WxMpCardService;
import me.chanjar.weixin.mp.api.WxMpConfigStorage;
import me.chanjar.weixin.mp.api.WxMpDataCubeService;
import me.chanjar.weixin.mp.api.WxMpDeviceService;
import me.chanjar.weixin.mp.api.WxMpKefuService;
import me.chanjar.weixin.mp.api.WxMpMaterialService;
import me.chanjar.weixin.mp.api.WxMpMenuService;
import me.chanjar.weixin.mp.api.WxMpQrcodeService;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.WxMpStoreService;
import me.chanjar.weixin.mp.api.WxMpTemplateMsgService;
import me.chanjar.weixin.mp.api.WxMpUserBlacklistService;
import me.chanjar.weixin.mp.api.WxMpUserService;
import me.chanjar.weixin.mp.api.WxMpUserTagService;
import me.chanjar.weixin.mp.bean.WxMpMassNews;
import me.chanjar.weixin.mp.bean.WxMpMassOpenIdsMessage;
import me.chanjar.weixin.mp.bean.WxMpMassPreviewMessage;
import me.chanjar.weixin.mp.bean.WxMpMassTagMessage;
import me.chanjar.weixin.mp.bean.WxMpMassVideo;
import me.chanjar.weixin.mp.bean.WxMpSemanticQuery;
import me.chanjar.weixin.mp.bean.result.WxMpMassSendResult;
import me.chanjar.weixin.mp.bean.result.WxMpMassUploadResult;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;
import me.chanjar.weixin.mp.bean.result.WxMpSemanticQueryResult;
import me.chanjar.weixin.mp.bean.result.WxMpUser;

public class WxMpServiceImpl implements WxMpService {

  private static final JsonParser JSON_PARSER = new JsonParser();

  protected final Logger log = LoggerFactory.getLogger(this.getClass());
  protected WxSessionManager sessionManager = new StandardSessionManager();
  //微信客户端配置存储
  private WxMpConfigStorage wxMpConfigStorage;
  //客服接口
  private WxMpKefuService kefuService = new WxMpKefuServiceImpl(this);
  // 素材管理的相关接口，包括媒体管理的接口，
  private WxMpMaterialService materialService = new WxMpMaterialServiceImpl(this);
  //菜单操接口
  private WxMpMenuService menuService = new WxMpMenuServiceImpl(this);
  //用户管理相关操作接口
  private WxMpUserService userService = new WxMpUserServiceImpl(this);
  //用户标签管理接口
  private WxMpUserTagService tagService = new WxMpUserTagServiceImpl(this);
  //二维码操作接口
  private WxMpQrcodeService qrCodeService = new WxMpQrcodeServiceImpl(this);
  //卡券相关接口
  private WxMpCardService cardService = new WxMpCardServiceImpl(this);
  //门店管理的相关接口代码
  private WxMpStoreService storeService = new WxMpStoreServiceImpl(this);
  //统计分析相关接口
  private WxMpDataCubeService dataCubeService = new WxMpDataCubeServiceImpl(this);
  //公众号的黑名单服务接口
  private WxMpUserBlacklistService blackListService = new WxMpUserBlacklistServiceImpl(this);
  //模板消息接口
  private WxMpTemplateMsgService templateMsgService = new WxMpTemplateMsgServiceImpl(this);
  private WxMpDeviceService deviceService = new WxMpDeviceServiceImpl(this);
  private CloseableHttpClient httpClient;
  private HttpHost httpProxy;
  private int retrySleepMillis = 1000;
  private int maxRetryTimes = 5;

  @Override
  public boolean checkSignature(String timestamp, String nonce, String signature) {
    try {
      return SHA1.gen(this.getWxMpConfigStorage().getToken(), timestamp, nonce)
        .equals(signature);
    } catch (Exception e) {
      return false;
    }
  }

  @Override
  public String getAccessToken() throws WxErrorException {
    return getAccessToken(false);
  }

  @Override
  public String getAccessToken(boolean forceRefresh) throws WxErrorException {
    Lock lock = this.getWxMpConfigStorage().getAccessTokenLock();
    try {
      lock.lock();

      if (forceRefresh) {
        this.getWxMpConfigStorage().expireAccessToken();
      }

      if (this.getWxMpConfigStorage().isAccessTokenExpired()) {
        String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential" +
          "&appid=" + this.getWxMpConfigStorage().getAppId() + "&secret="
          + this.getWxMpConfigStorage().getSecret();
        try {
          HttpGet httpGet = new HttpGet(url);
          if (this.httpProxy != null) {
            RequestConfig config = RequestConfig.custom().setProxy(this.httpProxy).build();
            httpGet.setConfig(config);
          }
          CloseableHttpResponse response = getHttpclient().execute(httpGet);
          try  {
            String resultContent = new BasicResponseHandler().handleResponse(response);
            WxError error = WxError.fromJson(resultContent);
            if (error.getErrorCode() != 0) {
              throw new WxErrorException(error);
            }
            WxAccessToken accessToken = WxAccessToken.fromJson(resultContent);
            this.getWxMpConfigStorage().updateAccessToken(accessToken.getAccessToken(),
              accessToken.getExpiresIn());
          } finally {
            httpGet.releaseConnection();
          }
        } catch (IOException e) {
          throw new RuntimeException(e);
        }
      }
    } finally {
      lock.unlock();
    }
    return this.getWxMpConfigStorage().getAccessToken();
  }

  @Override
  public String getJsapiTicket() throws WxErrorException {
    return getJsapiTicket(false);
  }

  @Override
  public String getJsapiTicket(boolean forceRefresh) throws WxErrorException {
    Lock lock = this.getWxMpConfigStorage().getJsapiTicketLock();
    try {
      lock.lock();

      if (forceRefresh) {
        this.getWxMpConfigStorage().expireJsapiTicket();
      }

      if (this.getWxMpConfigStorage().isJsapiTicketExpired()) {
        String url = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?type=jsapi";
        String responseContent = execute(new SimpleGetRequestExecutor(), url, null);
        JsonElement tmpJsonElement = JSON_PARSER.parse(responseContent);
        JsonObject tmpJsonObject = tmpJsonElement.getAsJsonObject();
        String jsapiTicket = tmpJsonObject.get("ticket").getAsString();
        int expiresInSeconds = tmpJsonObject.get("expires_in").getAsInt();
        this.getWxMpConfigStorage().updateJsapiTicket(jsapiTicket, expiresInSeconds);
      }
    } finally {
      lock.unlock();
    }
    return this.getWxMpConfigStorage().getJsapiTicket();
  }

  @Override
  public WxJsapiSignature createJsapiSignature(String url) throws WxErrorException {
    long timestamp = System.currentTimeMillis() / 1000;
    String noncestr = RandomUtils.getRandomStr();
    String jsapiTicket = getJsapiTicket(false);
    String signature = SHA1.genWithAmple("jsapi_ticket=" + jsapiTicket,
      "noncestr=" + noncestr, "timestamp=" + timestamp, "url=" + url);
    WxJsapiSignature jsapiSignature = new WxJsapiSignature();
    jsapiSignature.setAppId(this.getWxMpConfigStorage().getAppId());
    jsapiSignature.setTimestamp(timestamp);
    jsapiSignature.setNonceStr(noncestr);
    jsapiSignature.setUrl(url);
    jsapiSignature.setSignature(signature);
    return jsapiSignature;
  }

  @Override
  public WxMpMassUploadResult massNewsUpload(WxMpMassNews news) throws WxErrorException {
    String url = "https://api.weixin.qq.com/cgi-bin/media/uploadnews";
    String responseContent = this.post(url, news.toJson());
    return WxMpMassUploadResult.fromJson(responseContent);
  }

  @Override
  public WxMpMassUploadResult massVideoUpload(WxMpMassVideo video) throws WxErrorException {
    String url = "https://api.weixin.qq.com/cgi-bin/media/uploadvideo";
    String responseContent = this.post(url, video.toJson());
    return WxMpMassUploadResult.fromJson(responseContent);
  }

  @Override
  public WxMpMassSendResult massGroupMessageSend(WxMpMassTagMessage message) throws WxErrorException {
    String url = "https://api.weixin.qq.com/cgi-bin/message/mass/sendall";
    String responseContent = this.post(url, message.toJson());
    return WxMpMassSendResult.fromJson(responseContent);
  }

  @Override
  public WxMpMassSendResult massOpenIdsMessageSend(WxMpMassOpenIdsMessage message) throws WxErrorException {
    String url = "https://api.weixin.qq.com/cgi-bin/message/mass/send";
    String responseContent = this.post(url, message.toJson());
    return WxMpMassSendResult.fromJson(responseContent);
  }

  @Override
  public WxMpMassSendResult massMessagePreview(WxMpMassPreviewMessage wxMpMassPreviewMessage) throws Exception {
    String url = "https://api.weixin.qq.com/cgi-bin/message/mass/preview";
    String responseContent = this.post(url, wxMpMassPreviewMessage.toJson());
    return WxMpMassSendResult.fromJson(responseContent);
  }

  @Override
  public String shortUrl(String long_url) throws WxErrorException {
    String url = "https://api.weixin.qq.com/cgi-bin/shorturl";
    JsonObject o = new JsonObject();
    o.addProperty("action", "long2short");
    o.addProperty("long_url", long_url);
    String responseContent = this.post(url, o.toString());
    JsonElement tmpJsonElement = JSON_PARSER.parse(responseContent);
    return tmpJsonElement.getAsJsonObject().get("short_url").getAsString();
  }

  @Override
  public WxMpSemanticQueryResult semanticQuery(WxMpSemanticQuery semanticQuery) throws WxErrorException {
    String url = "https://api.weixin.qq.com/semantic/semproxy/search";
    String responseContent = this.post(url, semanticQuery.toJson());
    return WxMpSemanticQueryResult.fromJson(responseContent);
  }

  @Override
  public String oauth2buildAuthorizationUrl(String redirectURI, String scope, String state) {
    StringBuilder url = new StringBuilder();
    url.append("https://open.weixin.qq.com/connect/oauth2/authorize?");
    url.append("appid=").append(this.getWxMpConfigStorage().getAppId());
    url.append("&redirect_uri=").append(URIUtil.encodeURIComponent(redirectURI));
    url.append("&response_type=code");
    url.append("&scope=").append(scope);
    if (state != null) {
      url.append("&state=").append(state);
    }
    url.append("#wechat_redirect");
    return url.toString();
  }

  @Override
  public String buildQrConnectUrl(String redirectURI, String scope,
                                  String state) {
    StringBuilder url = new StringBuilder();
    url.append("https://open.weixin.qq.com/connect/qrconnect?");
    url.append("appid=").append(this.getWxMpConfigStorage().getAppId());
    url.append("&redirect_uri=").append(URIUtil.encodeURIComponent(redirectURI));
    url.append("&response_type=code");
    url.append("&scope=").append(scope);
    if (state != null) {
      url.append("&state=").append(state);
    }

    url.append("#wechat_redirect");
    return url.toString();
  }

  private WxMpOAuth2AccessToken getOAuth2AccessToken(StringBuilder url) throws WxErrorException {
    try {
      RequestExecutor<String, String> executor = new SimpleGetRequestExecutor();
      String responseText = executor.execute(this.getHttpclient(), this.httpProxy, url.toString(), null);
      return WxMpOAuth2AccessToken.fromJson(responseText);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public WxMpOAuth2AccessToken oauth2getAccessToken(String code) throws WxErrorException {
    StringBuilder url = new StringBuilder();
    url.append("https://api.weixin.qq.com/sns/oauth2/access_token?");
    url.append("appid=").append(this.getWxMpConfigStorage().getAppId());
    url.append("&secret=").append(this.getWxMpConfigStorage().getSecret());
    url.append("&code=").append(code);
    url.append("&grant_type=authorization_code");

    return this.getOAuth2AccessToken(url);
  }

  @Override
  public WxMpOAuth2AccessToken oauth2refreshAccessToken(String refreshToken) throws WxErrorException {
    StringBuilder url = new StringBuilder();
    url.append("https://api.weixin.qq.com/sns/oauth2/refresh_token?");
    url.append("appid=").append(this.getWxMpConfigStorage().getAppId());
    url.append("&grant_type=refresh_token");
    url.append("&refresh_token=").append(refreshToken);

    return this.getOAuth2AccessToken(url);
  }

  @Override
  public WxMpUser oauth2getUserInfo(WxMpOAuth2AccessToken oAuth2AccessToken, String lang) throws WxErrorException {
    StringBuilder url = new StringBuilder();
    url.append("https://api.weixin.qq.com/sns/userinfo?");
    url.append("access_token=").append(oAuth2AccessToken.getAccessToken());
    url.append("&openid=").append(oAuth2AccessToken.getOpenId());
    if (lang == null) {
      url.append("&lang=zh_CN");
    } else {
      url.append("&lang=").append(lang);
    }

    try {
      RequestExecutor<String, String> executor = new SimpleGetRequestExecutor();
      String responseText = executor.execute(getHttpclient(), this.httpProxy, url.toString(), null);
      return WxMpUser.fromJson(responseText);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public boolean oauth2validateAccessToken(WxMpOAuth2AccessToken oAuth2AccessToken) {
    StringBuilder url = new StringBuilder();
    url.append("https://api.weixin.qq.com/sns/auth?");
    url.append("access_token=").append(oAuth2AccessToken.getAccessToken());
    url.append("&openid=").append(oAuth2AccessToken.getOpenId());

    try {
      RequestExecutor<String, String> executor = new SimpleGetRequestExecutor();
      executor.execute(getHttpclient(), this.httpProxy, url.toString(), null);
    } catch (IOException e) {
      throw new RuntimeException(e);
    } catch (WxErrorException e) {
      return false;
    }
    return true;
  }

  @Override
  public String[] getCallbackIP() throws WxErrorException {
    String url = "https://api.weixin.qq.com/cgi-bin/getcallbackip";
    String responseContent = get(url, null);
    JsonElement tmpJsonElement = JSON_PARSER.parse(responseContent);
    JsonArray ipList = tmpJsonElement.getAsJsonObject().get("ip_list").getAsJsonArray();
    String[] ipArray = new String[ipList.size()];
    for (int i = 0; i < ipList.size(); i++) {
      ipArray[i] = ipList.get(i).getAsString();
    }
    return ipArray;
  }

  @Override
  public String get(String url, String queryParam) throws WxErrorException {
    return execute(new SimpleGetRequestExecutor(), url, queryParam);
  }

  @Override
  public String post(String url, String postData) throws WxErrorException {
    return execute(new SimplePostRequestExecutor(), url, postData);
  }

  /**
   * 向微信端发送请求，在这里执行的策略是当发生access_token过期时才去刷新，然后重新执行请求，而不是全局定时请求
   */
  @Override
  public <T, E> T execute(RequestExecutor<T, E> executor, String uri, E data) throws WxErrorException {
    int retryTimes = 0;
    do {
      try {
        T result = executeInternal(executor, uri, data);
        this.log.debug("\n[URL]:  {}\n[PARAMS]: {}\n[RESPONSE]: {}", new Object[]{uri, data, result});
        return result;
      } catch (WxErrorException e) {
        if (retryTimes + 1 > this.maxRetryTimes) {
          this.log.warn("重试达到最大次数【{}】", maxRetryTimes);
          //最后一次重试失败后，直接抛出异常，不再等待
          throw new RuntimeException("微信服务端异常，超出重试次数");
        }

        WxError error = e.getError();
        // -1 系统繁忙, 1000ms后重试
        if (error.getErrorCode() == -1) {
          int sleepMillis = this.retrySleepMillis * (1 << retryTimes);
          try {
            this.log.warn("微信系统繁忙，{} ms 后重试(第{}次)", sleepMillis, retryTimes + 1);
            Thread.sleep(sleepMillis);
          } catch (InterruptedException e1) {
            throw new RuntimeException(e1);
          }
        } else {
          throw e;
        }
      }
    } while (retryTimes++ < this.maxRetryTimes);

    this.log.warn("重试达到最大次数【{}】", this.maxRetryTimes);
    throw new RuntimeException("微信服务端异常，超出重试次数");
  }

  protected synchronized <T, E> T executeInternal(RequestExecutor<T, E> executor, String uri, E data) throws WxErrorException {
    if (uri.indexOf("access_token=") != -1) {
      throw new IllegalArgumentException("uri参数中不允许有access_token: " + uri);
    }
    String accessToken = getAccessToken(false);

    String uriWithAccessToken = uri;
    uriWithAccessToken += uri.indexOf('?') == -1 ? "?access_token=" + accessToken : "&access_token=" + accessToken;

    try {
      return executor.execute(getHttpclient(), this.httpProxy, uriWithAccessToken, data);
    } catch (WxErrorException e) {
      WxError error = e.getError();
      /*
       * 发生以下情况时尝试刷新access_token
       * 40001 获取access_token时AppSecret错误，或者access_token无效
       * 42001 access_token超时
       */
      if (error.getErrorCode() == 42001 || error.getErrorCode() == 40001) {
        // 强制设置wxMpConfigStorage它的access token过期了，这样在下一次请求里就会刷新access token
        this.getWxMpConfigStorage().expireAccessToken();
        if (this.getWxMpConfigStorage().autoRefreshToken()) {
          return this.execute(executor, uri, data);
        }
      }

      if (error.getErrorCode() != 0) {
        this.log.error("\n[URL]:  {}\n[PARAMS]: {}\n[RESPONSE]: {}", new Object[]{uri, data, error});
        throw new WxErrorException(error);
      }
      return null;
    } catch (IOException e) {
      this.log.error("\n[URL]:  {}\n[PARAMS]: {}\n[EXCEPTION]: {}", new Object[]{uri, data, e.getMessage()});
      throw new RuntimeException(e);
    }
  }

  @Override
  public HttpHost getHttpProxy() {
    return this.httpProxy;
  }

  public CloseableHttpClient getHttpclient() {
    return this.httpClient;
  }

  private void initHttpClient() {
    WxMpConfigStorage configStorage = this.getWxMpConfigStorage();
    ApacheHttpClientBuilder apacheHttpClientBuilder = configStorage.getApacheHttpClientBuilder();
    if (null == apacheHttpClientBuilder) {
      apacheHttpClientBuilder = DefaultApacheHttpClientBuilder.get();
    }

    apacheHttpClientBuilder.httpProxyHost(configStorage.getHttpProxyHost())
      .httpProxyPort(configStorage.getHttpProxyPort())
      .httpProxyUsername(configStorage.getHttpProxyUsername())
      .httpProxyPassword(configStorage.getHttpProxyPassword());

    if (configStorage.getHttpProxyHost() != null && configStorage.getHttpProxyPort() > 0) {
      this.httpProxy = new HttpHost(configStorage.getHttpProxyHost(), configStorage.getHttpProxyPort());
    }

    this.httpClient = apacheHttpClientBuilder.build();
  }

  @Override
  public WxMpConfigStorage getWxMpConfigStorage() {
    return this.wxMpConfigStorage;
  }

  @Override
  public void setWxMpConfigStorage(WxMpConfigStorage wxConfigProvider) {
    this.wxMpConfigStorage = wxConfigProvider;
    this.initHttpClient();
  }

  @Override
  public void setRetrySleepMillis(int retrySleepMillis) {
    this.retrySleepMillis = retrySleepMillis;
  }

  @Override
  public void setMaxRetryTimes(int maxRetryTimes) {
    this.maxRetryTimes = maxRetryTimes;
  }

  @Override
  public WxMpKefuService getKefuService() {
    return this.kefuService;
  }

  @Override
  public WxMpMaterialService getMaterialService() {
    return this.materialService;
  }

  @Override
  public WxMpMenuService getMenuService() {
    return this.menuService;
  }

  @Override
  public WxMpUserService getUserService() {
    return this.userService;
  }

  @Override
  public WxMpUserTagService getUserTagService() {
    return this.tagService;
  }

  @Override
  public WxMpQrcodeService getQrcodeService() {
    return this.qrCodeService;
  }

  @Override
  public WxMpCardService getCardService() {
    return this.cardService;
  }

  @Override
  public WxMpDataCubeService getDataCubeService() {
    return this.dataCubeService;
  }

  @Override
  public WxMpUserBlacklistService getBlackListService() {
    return this.blackListService;
  }

  @Override
  public WxMpStoreService getStoreService() {
    return this.storeService;
  }

  @Override
  public WxMpTemplateMsgService getTemplateMsgService() {
    return this.templateMsgService;
  }

  @Override
  public WxMpDeviceService getDeviceService() {
    return this.deviceService;
  }
}
