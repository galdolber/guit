package com.guit.junit.dom;

import com.guit.client.dom.Source;

public class SourceMock extends ElementMock implements Source {

  public SourceMock() {
    super("source");
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
  public void src(String url) {
    attr("src", url);
  }

  @Override
  public void type(String type) {
    attr("type", type);
  }
}
