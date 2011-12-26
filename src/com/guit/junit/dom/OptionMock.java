package com.guit.junit.dom;

import com.guit.client.dom.Option;

public class OptionMock extends ElementMock implements Option {

  public OptionMock() {
    super("option");
  }

  @Override
  public int index() {
    return attrInt("index");
  }

  @Override
  public String label() {
    return attr("label");
  }

  @Override
  public String value() {
    return attr("value");
  }

  @Override
  public boolean isDefaultSelected() {
    return propertyBoolean("defaultSelected");
  }

  @Override
  public boolean disabled() {
    return propertyBoolean("disabled");
  }

  @Override
  public boolean selected() {
    return propertyBoolean("selected");
  }

  @Override
  public void defaultSelected(boolean selected) {
    propertyBoolean("defaultSelected", selected);
  }

  @Override
  public void disabled(boolean disabled) {
    propertyBoolean("disabled", disabled);
  }

  @Override
  public void label(String label) {
    attr("label", label);
  }

  @Override
  public void selected(boolean selected) {
    propertyBoolean("selected", selected);
  }

  @Override
  public void value(String value) {
    attr("value", value);
  }
}
