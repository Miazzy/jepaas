package me.chanjar.dingding.common.result;

import me.chanjar.weixin.common.util.json.WxGsonBuilder;

import java.io.Serializable;

public class DdAccessToken implements Serializable {
  private static final long serialVersionUID = 8709719312922168909L;

  private String accessToken;

  private int expiresIn = -1;

  public static DdAccessToken fromJson(String json) {
    return WxGsonBuilder.create().fromJson(json, DdAccessToken.class);
  }

  public String getAccessToken() {
    return this.accessToken;
  }

  public void setAccessToken(String accessToken) {
    this.accessToken = accessToken;
  }

  public int getExpiresIn() {
    return this.expiresIn;
  }

  public void setExpiresIn(int expiresIn) {
    this.expiresIn = expiresIn;
  }

}
