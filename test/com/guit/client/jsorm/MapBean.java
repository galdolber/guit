package com.guit.client.jsorm;

import java.util.HashMap;

public class MapBean {
  private HashMap<String, String> map;

  public void setMap(HashMap<String, String> map) {
    this.map = map;
  }

  public HashMap<String, String> getMap() {
    return map;
  }

  public MapBean() {
  }

  public MapBean(HashMap<String, String> map) {
    setMap(map);
  }
}