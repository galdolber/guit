package com.guit.junit.dom;

import com.guit.client.dom.Frame;

public class FrameMock extends ElementMock implements Frame {

  public FrameMock() {
    super("frame");
  }

  // public Document getContentDocument() {
  // // This is known to work on all modern browsers.
  // return this.contentWindow.document;
  // }

  @Override
  public int frameBorder() {
    return propertyInt("frameBorder");
  }

  @Override
  public String longDesc() {
    return propertyString("longDesc");
  }

  @Override
  public int marginHeight() {
    return propertyInt("marginHeight");
  }

  @Override
  public int marginWidth() {
    return propertyInt("marginWidth");
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
    propertyInt("frameBorder", frameBorder);;
  }

  @Override
  public void longDesc(String longDesc) {
    propertyString("longDesc", longDesc);
  }

  @Override
  public void marginHeight(int marginHeight) {
    propertyInt("marginHeight", marginHeight);
  }

  @Override
  public void marginWidth(int marginWidth) {
    propertyInt("marginWidth", marginWidth);
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
    propertyString("scrolling", scrolling);
  }

  @Override
  public void src(String src) {
    attr("src", src);
  }
}
