package com.guit.junit.dom;

import com.guit.client.dom.Legend;

public class LegendMock extends ElementMock implements Legend {

  public LegendMock() {
    super("legend");
  }

  @Override
  public String accessKey() {
    return attr("accessKey");
  }

  @Override
  public void accessKey(String accessKey) {
    attr("accessKey", accessKey);
  }
}
