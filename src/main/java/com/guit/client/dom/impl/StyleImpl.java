package com.guit.client.dom.impl;

import com.guit.client.dom.Style;

public class StyleImpl extends ElementImpl implements Style {

  public StyleImpl() {
    super("style");
  }

  private com.google.gwt.dom.client.StyleElement el() {
    return e.cast();
  }

  @Override
  public java.lang.String media() {
    return el().getMedia();
  }

  @Override
  public void media(java.lang.String arg0) {
    el().setMedia(arg0);
  }

  @Override
  public java.lang.String cssText() {
    return el().getCssText();
  }

  @Override
  public void cssText(java.lang.String arg0) {
    el().setCssText(arg0);
  }
}
