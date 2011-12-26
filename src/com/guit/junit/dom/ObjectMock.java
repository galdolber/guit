package com.guit.junit.dom;

import com.guit.client.dom.Object;

public class ObjectMock extends ElementMock implements Object {

  public ObjectMock() {
    super("object");
  }

  @Override
  public String code() {
    return attr("code");
  }

  // public Document getContentDocument() {
  // return attr("contentDocument");
  // }

  @Override
  public String data() {
    return attr("data");
  }

  @Override
  public String objectHeight() {
    return attr("height");
  }

  @Override
  public String name() {
    return attr("name");
  }

  @Override
  public String type() {
    return attr("type");
  }

  @Override
  public String objectWidth() {
    return attr("width");
  }

  @Override
  public void code(String code) {
    attr("code", code);
  }

  @Override
  public void data(String data) {
    attr("data", data);
  }

  @Override
  public void height(String height) {
    attr("height", height);
  }

  @Override
  public void name(String name) {
    attr("name", name);
  }

  @Override
  public void type(String type) {
    attr("type", type);
  }

  @Override
  public void useMap(boolean useMap) {
    propertyBoolean("useMap", useMap);
  }

  @Override
  public void width(String width) {
    attr("width", width);
  }

  @Override
  public boolean useMap() {
    return propertyBoolean("useMap");
  }
}
