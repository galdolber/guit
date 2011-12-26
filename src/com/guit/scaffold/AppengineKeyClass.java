package com.guit.scaffold;

public class AppengineKeyClass {

  public static boolean exists() {
    return get() == null ? false : true;
  }

  public static Class<?> get() {
    try {
      return Class.forName("com.google.appengine.api.datastore.Key");
    } catch (ClassNotFoundException e) {
      return null;
    }
  }
}
