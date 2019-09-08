package me.chanjar.weixin.common.session;

import java.util.Enumeration;

public interface WxSession {

  Object getAttribute(String name);

  Enumeration<String> getAttributeNames();

  void setAttribute(String name, Object value);

  void removeAttribute(String name);

  void invalidate();

}
