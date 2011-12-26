package com.guit.junit.dom;

import com.guit.client.dom.Label;

public class LabelMock extends ElementMock implements Label {

  public LabelMock() {
    super("label");
  }

  @Override
  public String accessKey() {
    return attr("accessKey");
  }

  @Override
  public String htmlFor() {
    return attr("htmlFor");
  }

  @Override
  public void accessKey(String accessKey) {
    attr("accessKey", accessKey);
  }

  @Override
  public void htmlFor(String htmlFor) {
    attr("htmlFor", htmlFor);
  }
}
