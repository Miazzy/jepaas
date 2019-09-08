package me.chanjar.dingding.common.bean.result;

import me.chanjar.weixin.common.util.json.WxGsonBuilder;

import java.io.Serializable;

/**
 * 钉钉错误码说明，请阅读： <a href="https://open-doc.dingtalk.com/microapp/faquestions/rftpfg">全局返回码说明</a>
 *
 * @author Daniel Qian
 */
public class DdError implements Serializable {

  private static final long serialVersionUID = 7869786563361406291L;

  private int errorCode;

  private String errorMsg;

  private String json;

  public static DdError fromJson(String json) {
    return WxGsonBuilder.create().fromJson(json, DdError.class);
  }

  public static Builder newBuilder() {
    return new Builder();
  }

  public int getErrorCode() {
    return this.errorCode;
  }

  public void setErrorCode(int errorCode) {
    this.errorCode = errorCode;
  }

  public String getErrorMsg() {
    return this.errorMsg;
  }

  public void setErrorMsg(String errorMsg) {
    this.errorMsg = errorMsg;
  }

  public String getJson() {
    return this.json;
  }

  public void setJson(String json) {
    this.json = json;
  }

  @Override
  public String toString() {
    if (this.json != null) {
      return this.json;
    }
    return "错误: Code=" + this.errorCode + ", Msg=" + this.errorMsg;
  }

  public static class Builder {
    private int errorCode;
    private String errorMsg;

    public Builder setErrorCode(int errorCode) {
      this.errorCode = errorCode;
      return this;
    }

    public Builder setErrorMsg(String errorMsg) {
      this.errorMsg = errorMsg;
      return this;
    }

    public DdError build() {
      DdError wxError = new DdError();
      wxError.setErrorCode(this.errorCode);
      wxError.setErrorMsg(this.errorMsg);
      return wxError;
    }

  }
}
