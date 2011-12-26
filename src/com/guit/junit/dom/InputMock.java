package com.guit.junit.dom;

import com.guit.client.dom.Input;

public class InputMock extends BaseMock implements Input {

  public InputMock() {
    super("input");
  }

  @Override
  public void click() {
  }

  @Override
  public String accept() {
    return attr("accept");
  }

  @Override
  public String alt() {
    return attr("alt");
  }

  @Override
  public String defaultValue() {
    return attr("defaultValue");
  }

  @Override
  public int maxLength() {
    return attrInt("maxLength");
  }

  @Override
  public int size() {
    return attrInt("size");
  }

  @Override
  public String src() {
    return attr("src");
  }

  @Override
  public boolean checked() {
    return propertyBoolean("checked");
  }

  @Override
  public boolean defaultChecked() {
    return propertyBoolean("defaultChecked");
  }

  @Override
  public boolean readOnly() {
    return propertyBoolean("readOnly");
  }

  @Override
  public void select() {
  }

  @Override
  public void accept(String accept) {
    attr("accept", accept);
  }

  @Override
  public void alt(String alt) {
    attr("alt", alt);
  }

  @Override
  public void checked(boolean checked) {
    propertyBoolean("checked", checked);
  }

  @Override
  public void defaultChecked(boolean defaultChecked) {
    propertyBoolean("defaultChecked", defaultChecked);
  }

  @Override
  public void defaultValue(String defaultValue) {
    propertyString("defaultValue", defaultValue);
  }

  @Override
  public void maxLength(int maxLength) {
    attr("maxLength", String.valueOf(maxLength));
  }

  @Override
  public void readOnly(boolean readOnly) {
    propertyBoolean("readOnly", readOnly);
  }

  @Override
  public void size(int size) {
    attr("size", String.valueOf(size));
  }

  @Override
  public void src(String src) {
    attr("src", src);
  }

  @Override
  public void useMap(boolean useMap) {
    propertyBoolean("useMap", useMap);
  }

  @Override
  public boolean useMap() {
    return propertyBoolean("useMap");
  }
}
