package com.guit.junit.dom;

import com.guit.client.dom.Param;

public class ParamMock extends ElementMock implements Param {

  public ParamMock() {
    super("param");
  }

  @Override
  public String name() {
    return attr("name");
  }

  @Override
  public String value() {
    return attr("value");
  }

  @Override
  public void name(String name) {
    attr("name", name);
  }

  @Override
  public void value(String value) {
    attr("value", value);
  }
}
