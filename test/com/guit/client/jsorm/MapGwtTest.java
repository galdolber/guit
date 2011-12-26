package com.guit.client.jsorm;

import com.google.gwt.core.client.GWT;
import com.google.gwt.junit.client.GWTTestCase;

import java.util.HashMap;

public class MapGwtTest extends GWTTestCase {

  @Override
  public String getModuleName() {
    return "com.guit.Guit";
  }

  public void test() {
    MySerializer s = GWT.create(MySerializer.class);

    HashMap<String, String> map = new HashMap<String, String>();
    map.put("1", "uno");
    map.put("2", "dos");
    map.put("3", "tres");
    map.put("4", "cuatro");
    map.put("5", "cinco");
    MapBean t = new MapBean(map);
    MapBean des = s.deserialize(s.serialize(t));

    assertEquals(map, des.getMap());
  }
}
