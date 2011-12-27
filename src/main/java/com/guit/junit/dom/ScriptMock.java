package com.guit.junit.dom;

import com.guit.client.dom.Script;

public class ScriptMock extends ElementMock implements Script {

  public ScriptMock() {
    super("script");
  }

  @Override
  public String defer() {
    return attr("defer");
  }

  @Override
  public String src() {
    return attr("src");
  }

  @Override
  public String type() {
    return attr("type");
  }

  @Override
  public void defer(String defer) {
    attr("defer", defer);
  }

  @Override
  public void src(String src) {
    attr("src", src);
  }

  @Override
  public void type(String type) {
    attr("type", type);
  }
}
