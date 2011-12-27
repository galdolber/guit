package com.guit.junit.dom;

import com.guit.client.dom.Select;

public class SelectMock extends BaseMock implements Select {

  public SelectMock() {
    super("select");
  }

  // public final void add(OptionElement option, OptionElement before) {
  // DOMImpl.impl.selectAdd(this, option, before);
  // }

  @Override
  public final void clear() {
    html("");
  }

  @Override
  public final int length() {
    return children().size();
  }

  @Override
  public String getMultiple() {
    return attr("multiple");
  }

  @Override
  public int selectedIndex() {
    return propertyInt("selectedIndex");
  }

  @Override
  public int size() {
    return attrInt("size");
  }

  @Override
  public boolean multiple() {
    return getMultiple().equals("multiple");
  }

  @Override
  public void removeItem(int index) {
    // TODO
  }

  @Override
  public void multiple(boolean multiple) {
    attr("multiple", "multiple");
  }

  @Override
  public void selectedIndex(int index) {
    propertyInt("selectedIndex", index);
  }

  @Override
  public void size(int size) {
    attr("size", String.valueOf(size));
  }
}
