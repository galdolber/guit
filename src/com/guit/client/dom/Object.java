package com.guit.client.dom;

import com.guit.client.Implementation;
import com.guit.client.dom.impl.ObjectImpl;
import com.guit.client.junit.Mock;
import com.guit.junit.dom.ObjectMock;

@Implementation(ObjectImpl.class)
@Mock(ObjectMock.class)
public interface Object extends Element {

  String code();

  String data();

  String objectHeight();

  String name();

  String type();

  String objectWidth();

  void code(String code);

  void data(String data);

  void height(String height);

  void name(String name);

  void type(String type);

  void useMap(boolean useMap);

  void width(String width);

  boolean useMap();
}