package com.guit.junit.dom;

import com.guit.client.dom.Element;

public class BaseMock extends ElementMock {

  public BaseMock(String tag) {
    super(tag);
  }

  public String getAccessKey() {
    return attr("accessKey");
  }

  public String getName() {
    return attr("name");
  }

  public String getType() {
    return attr("type");
  }

  @Override
  public String text() {
    return value();
  }

  @Override
  public Element text(String html) {
    return value(html);
  }

  public String value() {
    return attr("value");
  }

  public boolean isDisabled() {
    return hasAttribute("disabled");
  }

  public void setAccessKey(String accessKey) {
    attr("accessKey", accessKey);
  }

  public void setDisabled(boolean disabled) {
    if (disabled) {
      attr("disabled", "");
    } else {
      removeAttr("disabled");
    }
  }

  public void setName(String name) {
    attr("name", name);
  }

  public Element value(String value) {
    attr("value", value);
    return this;
  }
}
