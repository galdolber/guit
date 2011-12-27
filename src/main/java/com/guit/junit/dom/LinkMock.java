package com.guit.junit.dom;

import com.guit.client.dom.Link;

public class LinkMock extends BaseMock implements Link {

  public LinkMock() {
    super("link");
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
  public String media() {
    return attr("media");
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
  public void href(String href) {
    attr("href", href);
  }

  @Override
  public void hreflang(String hreflang) {
    attr("hreflang", hreflang);
  }

  @Override
  public void media(String media) {
    attr("media", media);
  }

  @Override
  public void rel(String rel) {
    attr("rel", rel);
  }

  @Override
  public void target(String target) {
    attr("target", target);
  }
}
