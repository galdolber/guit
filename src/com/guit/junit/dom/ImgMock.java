package com.guit.junit.dom;

import com.guit.client.dom.Img;

public class ImgMock extends ElementMock implements Img {

  public ImgMock() {
    super("img");
  }

  @Override
  public String alt() {
    return attr("alt");
  }

  @Override
  public int imageHeight() {
    return attrInt("height");
  }

  @Override
  public String src() {
    return attr("src");
  }

  @Override
  public int imageWidth() {
    return attrInt("width");
  }

  @Override
  public boolean map() {
    return propertyBoolean("isMap");
  }

  @Override
  public void alt(String alt) {
    attr("alt", alt);
  }

  @Override
  public void height(int height) {
    attr("height", String.valueOf(height));
  }

  @Override
  public void map(boolean isMap) {
    propertyBoolean("isMap", isMap);
  }

  @Override
  public final void src(String src) {
    attr("src", src);
  }

  @Override
  public void useMap(boolean useMap) {
    propertyBoolean("useMap", useMap);
  }

  @Override
  public void width(int width) {
    attr("width", String.valueOf(width));
  }

  @Override
  public boolean useMap() {
    return propertyBoolean("useMap");
  }
}
