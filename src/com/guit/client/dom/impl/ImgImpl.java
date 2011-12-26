package com.guit.client.dom.impl;

public class ImgImpl extends ElementImpl implements com.guit.client.dom.Img {

  public ImgImpl() {
    super("img");
  }

  private com.google.gwt.dom.client.ImageElement el() {
    return e.cast();
  }

  @Override
  public java.lang.String alt() {
    return el().getAlt();
  }

  @Override
  public void alt(java.lang.String arg0) {
    el().setAlt(arg0);
  }

  @Override
  public java.lang.String src() {
    return el().getSrc();
  }

  @Override
  public void src(java.lang.String arg0) {
    el().setSrc(arg0);
  }

  @Override
  public int imageHeight() {
    return el().getHeight();
  }

  @Override
  public int imageWidth() {
    return el().getWidth();
  }

  @Override
  public boolean map() {
    return el().isMap();
  }

  @Override
  public void height(int arg0) {
    el().setHeight(arg0);
  }

  @Override
  public void map(boolean arg0) {
    el().setIsMap(arg0);
  }

  @Override
  public void useMap(boolean arg0) {
    el().setUseMap(arg0);
  }

  @Override
  public void width(int arg0) {
    el().setWidth(arg0);
  }

  @Override
  public boolean useMap() {
    return el().useMap();
  }
}
