package com.guit.rebind.binder;

import com.guit.client.dom.Element;

import java.util.HashMap;

public class Widget2Element {

  static HashMap<String, String> map = new HashMap<String, String>();

  static {
    // com.google.gwt.user.client.ui
    map.put("Anchor", "a");
    map.put("Button", "button");
    map.put("CheckBox", "input");
    map.put("CustomButton", "div");
    map.put("HTML", "div");
    map.put("Hyperlink", "div");
    map.put("Image", "img");
    map.put("InlineHTML", "span");
    map.put("InlineHyperlink", "a");
    map.put("InlineLabel", "span");
    map.put("Label", "div");
    map.put("ListBox", "select");
    map.put("DateBox", "input");
    map.put("DatePicker", "input");
    map.put("TextBox", "input");
  }

  public static String getElement(Element e) {
    String el = map.get(e.tag().substring(e.tag().indexOf(":") + 1));
    if (el == null) {
      return "div";
    } else {
      return el;
    }
  }
}
