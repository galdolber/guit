package com.guit.junit.dom;

import com.guit.client.dom.Area;

public class AreaMock extends ElementMock implements Area {

  public AreaMock() {
    super("area");
  }

  @Override
  public String accessKey() {
    return attr("accessKey");
  }

  @Override
  public String alt() {
    return attr("alt");
  }

  @Override
  public String coords() {
    return attr("coords");
  }

  @Override
  public String href() {
    return attr("href");
  }

  @Override
  public String shape() {
    return attr("shape");
  }

  @Override
  public String target() {
    return attr("target");
  }

  @Override
  public void accessKey(String accessKey) {
    attr("accessKey", accessKey);
  }

  @Override
  public void alt(String alt) {
    attr("alt", alt);
  }

  @Override
  public void coords(String coords) {
    attr("coords", coords);
  }

  @Override
  public void href(String href) {
    attr("href", href);
  }

  @Override
  public void shape(String shape) {
    attr("shape", shape);
  }

  @Override
  public void target(String target) {
    attr("target", target);
  }
}
