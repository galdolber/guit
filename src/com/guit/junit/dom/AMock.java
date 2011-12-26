package com.guit.junit.dom;

import com.guit.client.dom.A;

public class AMock extends ElementMock implements A {

  public AMock() {
    super("a");
  }

  @Override
  public String accessKey() {
    return attr("accesskey");
  }

  @Override
  public String href() {
    return attr("href");
  }

  @Override
  public String hreflang() {
    return attr("hreflang");
  }

  @Override
  public String name() {
    return attr("name");
  }

  @Override
  public String rel() {
    return attr("rel");
  }

  @Override
  public String target() {
    return attr("target");
  }

  @Override
  public String type() {
    return attr("type");
  }

  @Override
  public void accessKey(String accessKey) {
    attr("accessKey", accessKey);
  }

  @Override
  public void href(String href) {
    attr("href", href);
  }

  @Override
  public void hreflang(String hreflang) {
    attr("hreflang", hreflang);
  }

  @Override
  public void name(String name) {
    attr("name", name);
  }

  @Override
  public void rel(String rel) {
    attr("rel", rel);
  }

  @Override
  public void target(String target) {
    attr("target", target);
  }

  @Override
  public void type(String type) {
    attr("type", type);
  }
}
