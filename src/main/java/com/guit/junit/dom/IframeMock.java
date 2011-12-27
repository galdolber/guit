package com.guit.junit.dom;

import com.guit.client.dom.Iframe;

public class IframeMock extends ElementMock implements Iframe {

  public IframeMock() {
    super("iframe");
  }

  // public Document getContentDocument() {
  // return this.contentWindow.document;
  // }

  @Override
  public int frameBorder() {
    return attrInt("frameBorder");
  }

  @Override
  public int marginHeight() {
    return attrInt("marginHeight");
  }

  @Override
  public int marginWidth() {
    return attrInt("marginWidth");
  }

  @Override
  public String name() {
    return attr("name");
  }

  @Override
  public String scrolling() {
    return attr("scrolling");
  }

  @Override
  public String src() {
    return attr("src");
  }

  @Override
  public boolean noResize() {
    return propertyBoolean("noResize");
  }

  @Override
  public void frameBorder(int frameBorder) {
    attr("frameBorder", String.valueOf(frameBorder));
  }

  @Override
  public void marginHeight(int marginHeight) {
    attr("marginHeight", String.valueOf(marginHeight));
  }

  @Override
  public void marginWidth(int marginWidth) {
    attr("marginWidth", String.valueOf(marginWidth));
  }

  @Override
  public void name(String name) {
    attr("name", name);
  }

  @Override
  public void noResize(boolean noResize) {
    propertyBoolean("noResize", noResize);
  }

  @Override
  public void scrolling(String scrolling) {
    attr("scrolling", scrolling);
  }

  @Override
  public void src(String src) {
    attr("src", src);
  }
}
