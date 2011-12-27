package com.guit.junit.dom;

import com.guit.client.dom.Frameset;

public class FramesetMock extends ElementMock implements Frameset {

  public FramesetMock() {
    super("frameset");
  }

  @Override
  public String cols() {
    return attr("cols");
  }

  @Override
  public String rows() {
    return attr("rows");
  }

  @Override
  public void cols(String cols) {
    attr("cols", cols);
  }

  @Override
  public void rows(String rows) {
    attr("rows", rows);
  }
}
