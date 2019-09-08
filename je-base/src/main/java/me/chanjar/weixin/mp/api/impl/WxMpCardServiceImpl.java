package me.chanjar.weixin.mp.api.impl;

import java.util.Arrays;
import java.util.concurrent.locks.Lock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.google.gson.reflect.TypeToken;

import me.chanjar.weixin.common.bean.WxCardApiSignature;
import me.chanjar.weixin.common.bean.result.WxError;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.common.util.RandomUtils;
import me.chanjar.weixin.common.util.crypto.SHA1;
import me.chanjar.weixin.common.util.http.SimpleGetRequestExecutor;
import me.chanjar.weixin.mp.api.WxMpCardService;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpCardResult;
import me.chanjar.weixin.mp.util.json.WxMpGsonBuilder;

/**
 * Created by Binary Wang on 2016/7/27.
 */
public class WxMpCardServiceImpl implements WxMpCardService {

  private final Logger log = LoggerFactory.getLogger(WxMpCardServiceImpl.class);

  private WxMpService wxMpService;

  public WxMpCardServiceImpl(WxMpService wxMpService) {
    this.wxMpService = wxMpService;
  }

  /**
   * 获得卡券api_ticket，不强制刷新卡券api_ticket
   *
   * @return 卡券api_ticket
   * @see #getCardApiTicket(boolean)
   */
  @Override
  public String getCardApiTicket() throws WxErrorException {
    return getCardApiTicket(false);
  }

  /**
   * <pre>
   * 获得卡券api_ticket
   * 获得时会检查卡券apiToken是否过期，如果过期了，那么就刷新一下，否则就什么都不干
   *
   * 详情请见：http://mp.weixin.qq.com/wiki/7/aaa137b55fb2e0456bf8dd9148dd613f.html#.E9.99.84.E5.BD
   * .954-.E5.8D.A1.E5.88.B8.E6.89.A9.E5.B1.95.E5.AD.97.E6.AE.B5.E5.8F.8A.E7.AD.BE.E5.90.8D.E7.94
   * .9F.E6.88.90.E7.AE.97.E6.B3.95
   * </pre>
   *
   * @param forceRefresh 强制刷新
   * @return 卡券api_ticket
   */
  @Override
  public String getCardApiTicket(boolean forceRefresh) throws WxErrorException {
    Lock lock = wxMpService.getWxMpConfigStorage().getCardApiTicketLock();
    try {
      lock.lock();

      if (forceRefresh) {
        this.wxMpService.getWxMpConfigStorage().expireCardApiTicket();
      }

      if (this.wxMpService.getWxMpConfigStorage().isCardApiTicketExpired()) {
        String url = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?type=wx_card";
        String responseContent = this.wxMpService.execute(new SimpleGetRequestExecutor(), url, null);
        JsonElement tmpJsonElement = new JsonParser().parse(responseContent);
        JsonObject tmpJsonObject = tmpJsonElement.getAsJsonObject();
        String cardApiTicket = tmpJsonObject.get("ticket").getAsString();
        int expiresInSeconds = tmpJsonObject.get("expires_in").getAsInt();
        this.wxMpService.getWxMpConfigStorage().updateCardApiTicket(cardApiTicket, expiresInSeconds);
      }
    } finally {
      lock.unlock();
    }
    return this.wxMpService.getWxMpConfigStorage().getCardApiTicket();
  }

  /**
   * <pre>
   * 创建调用卡券api时所需要的签名
   *
   * 详情请见：http://mp.weixin.qq.com/wiki/7/aaa137b55fb2e0456bf8dd9148dd613f.html#.E9.99.84.E5.BD
   * .954-.E5.8D.A1.E5.88.B8.E6.89.A9.E5.B1.95.E5.AD.97.E6.AE.B5.E5.8F.8A.E7.AD.BE.E5.90.8D.E7.94
   * .9F.E6.88.90.E7.AE.97.E6.B3.95
   * </pre>
   *
   * @param optionalSignParam 参与签名的参数数组。
   *                  可以为下列字段：app_id, card_id, card_type, code, openid, location_id
   *                  </br>注意：当做wx.chooseCard调用时，必须传入app_id参与签名，否则会造成签名失败导致拉取卡券列表为空
   * @return 卡券Api签名对象
   */
  @Override
  public WxCardApiSignature createCardApiSignature(String... optionalSignParam) throws
          WxErrorException {
    long timestamp = System.currentTimeMillis() / 1000;
    String nonceStr = RandomUtils.getRandomStr();
    String cardApiTicket = getCardApiTicket(false);

    String[] signParam = Arrays.copyOf(optionalSignParam, optionalSignParam.length + 3);
    signParam[optionalSignParam.length] = String.valueOf(timestamp);
    signParam[optionalSignParam.length + 1] = nonceStr;
    signParam[optionalSignParam.length + 2] = cardApiTicket;
    String signature = SHA1.gen(signParam);
    WxCardApiSignature cardApiSignature = new WxCardApiSignature();
    cardApiSignature.setTimestamp(timestamp);
    cardApiSignature.setNonceStr(nonceStr);
    cardApiSignature.setSignature(signature);
    return cardApiSignature;
  }

  /**
   * 卡券Code解码
   *
   * @param encryptCode 加密Code，通过JSSDK的chooseCard接口获得
   * @return 解密后的Code
   */
  @Override
  public String decryptCardCode(String encryptCode) throws WxErrorException {
    String url = "https://api.weixin.qq.com/card/code/decrypt";
    JsonObject param = new JsonObject();
    param.addProperty("encrypt_code", encryptCode);
    String responseContent = this.wxMpService.post(url, param.toString());
    JsonElement tmpJsonElement = new JsonParser().parse(responseContent);
    JsonObject tmpJsonObject = tmpJsonElement.getAsJsonObject();
    JsonPrimitive jsonPrimitive = tmpJsonObject.getAsJsonPrimitive("code");
    return jsonPrimitive.getAsString();
  }

  /**
   * 卡券Code查询
   *
   * @param cardId       卡券ID代表一类卡券
   * @param code         单张卡券的唯一标准
   * @param checkConsume 是否校验code核销状态，填入true和false时的code异常状态返回数据不同
   * @return WxMpCardResult对象
   */
  @Override
  public WxMpCardResult queryCardCode(String cardId, String code, boolean checkConsume) throws WxErrorException {
    String url = "https://api.weixin.qq.com/card/code/get";
    JsonObject param = new JsonObject();
    param.addProperty("card_id", cardId);
    param.addProperty("code", code);
    param.addProperty("check_consume", checkConsume);
    String responseContent = this.wxMpService.post(url, param.toString());
    JsonElement tmpJsonElement = new JsonParser().parse(responseContent);
    return WxMpGsonBuilder.INSTANCE.create().fromJson(tmpJsonElement,
            new TypeToken<WxMpCardResult>() {
            }.getType());
  }

  /**
   * 卡券Code核销。核销失败会抛出异常
   *
   * @param code 单张卡券的唯一标准
   * @return 调用返回的JSON字符串。
   * <br>可用 com.google.gson.JsonParser#parse 等方法直接取JSON串中的errcode等信息。
   */
  @Override
  public String consumeCardCode(String code) throws WxErrorException {
    return consumeCardCode(code, null);
  }

  /**
   * 卡券Code核销。核销失败会抛出异常
   *
   * @param code   单张卡券的唯一标准
   * @param cardId 当自定义Code卡券时需要传入card_id
   * @return 调用返回的JSON字符串。
   * <br>可用 com.google.gson.JsonParser#parse 等方法直接取JSON串中的errcode等信息。
   */
  @Override
  public String consumeCardCode(String code, String cardId) throws WxErrorException {
    String url = "https://api.weixin.qq.com/card/code/consume";
    JsonObject param = new JsonObject();
    param.addProperty("code", code);

    if (cardId != null && !"".equals(cardId)) {
      param.addProperty("card_id", cardId);
    }

    return this.wxMpService.post(url, param.toString());
  }

  /**
   * 卡券Mark接口。
   * 开发者在帮助消费者核销卡券之前，必须帮助先将此code（卡券串码）与一个openid绑定（即mark住），
   * 才能进一步调用核销接口，否则报错。
   *
   * @param code   卡券的code码
   * @param cardId 卡券的ID
   * @param openId 用券用户的openid
   * @param isMark 是否要mark（占用）这个code，填写true或者false，表示占用或解除占用
   */
  @Override
  public void markCardCode(String code, String cardId, String openId, boolean isMark) throws
          WxErrorException {
    String url = "https://api.weixin.qq.com/card/code/mark";
    JsonObject param = new JsonObject();
    param.addProperty("code", code);
    param.addProperty("card_id", cardId);
    param.addProperty("openid", openId);
    param.addProperty("is_mark", isMark);
    String responseContent = this.wxMpService.post(url, param.toString());
    JsonElement tmpJsonElement = new JsonParser().parse(responseContent);
    WxMpCardResult cardResult = WxMpGsonBuilder.INSTANCE.create().fromJson(tmpJsonElement,
            new TypeToken<WxMpCardResult>() { }.getType());
    if (!cardResult.getErrorCode().equals("0")) {
      this.log.warn("朋友的券mark失败：{}", cardResult.getErrorMsg());
    }
  }

  @Override
  public String getCardDetail(String cardId) throws WxErrorException {
    String url = "https://api.weixin.qq.com/card/get";
    JsonObject param = new JsonObject();
    param.addProperty("card_id", cardId);
    String responseContent = this.wxMpService.post(url, param.toString());

    // 判断返回值
    JsonObject json = (new JsonParser()).parse(responseContent).getAsJsonObject();
    String errcode = json.get("errcode").getAsString();
    if (!"0".equals(errcode)) {
      String errmsg = json.get("errmsg").getAsString();
      WxError error = new WxError();
      error.setErrorCode(Integer.parseInt(errcode));
      error.setErrorMsg(errmsg);
      throw new WxErrorException(error);
    }

    return responseContent;
  }
}
