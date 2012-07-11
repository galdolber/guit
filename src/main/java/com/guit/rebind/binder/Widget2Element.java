package com.guit.rebind.binder;

import com.google.gwt.dom.client.Node;

import com.guit.client.dom.Element;

import org.reflections.Reflections;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
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

  public static void main(String[] args) throws SecurityException, NoSuchFieldException,
      IllegalArgumentException, IllegalAccessException {
    Reflections reflections = new Reflections("com.google.gwt.dom.client");
    for (Class<? extends Node> c : reflections.getSubTypesOf(Node.class)) {
      for (Field f : c.getDeclaredFields()) {
        if (f.getType().equals(String.class) && f.getName().startsWith("TAG")
            && Modifier.isStatic(f.getModifiers())) {
          f.setAccessible(true);
          System.out.println("element2dom.put(\"" + f.get(null) + "\", \"" + c.getCanonicalName()
              + "\");");
        }
      }
    }
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
