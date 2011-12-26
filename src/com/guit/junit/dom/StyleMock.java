package com.guit.junit.dom;

import com.guit.client.dom.Style;

public class StyleMock extends BaseMock implements Style {

  public StyleMock() {
    super("style");
  }

  @Override
  public String cssText() {
    return attr("cssText");
  }

  @Override
  public String media() {
    return attr("media");
  }

  @Override
  public void cssText(String cssText) {
    attr("cssText", cssText);
  }

  @Override
  public void media(String media) {
    attr("media", media);
  }
}
