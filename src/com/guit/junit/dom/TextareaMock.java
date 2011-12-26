package com.guit.junit.dom;

import com.guit.client.dom.Textarea;

public class TextareaMock extends BaseMock implements Textarea {

  public TextareaMock() {
    super("textarea");
  }

  @Override
  public int cols() {
    return propertyInt("cols");
  }

  @Override
  public String defaultValue() {
    return propertyString("defaultValue");
  }

  @Override
  public int rows() {
    return propertyInt("rows");
  }

  @Override
  public boolean readOnly() {
    return Boolean.TRUE.equals(propertyBoolean("readOnly"));
  }

  @Override
  public void select() {
  }

  @Override
  public void cols(int cols) {
    propertyInt("cols", cols);
  }

  @Override
  public void defaultValue(String defaultValue) {
    propertyString("defaultValue", defaultValue);
  }

  @Override
  public void readOnly(boolean readOnly) {
    propertyBoolean("readOnly", readOnly);
  }

  @Override
  public void rows(int rows) {
    propertyInt("rows", rows);
  }
}
