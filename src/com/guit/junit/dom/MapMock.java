package com.guit.junit.dom;

import com.guit.client.dom.Map;

public class MapMock extends ElementMock implements Map {

  public MapMock() {
    super("map");
  }

  // public NodeList<AreaElement> getAreas() {
  // return this.areas;
  // }

  @Override
  public String name() {
    return attr("name");
  }

  @Override
  public void name(String name) {
    attr("name", name);
  }
}
