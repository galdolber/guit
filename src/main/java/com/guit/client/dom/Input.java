package com.guit.client.dom;

import com.guit.client.Implementation;
import com.guit.client.dom.impl.InputImpl;
import com.guit.client.junit.Mock;
import com.guit.junit.dom.InputMock;

@Implementation(InputImpl.class)
@Mock(InputMock.class)
public interface Input extends Element {

  void click();

  String accept();

  String alt();

  String defaultValue();

  int maxLength();

  int size();

  String src();

  boolean checked();

  boolean defaultChecked();

  boolean readOnly();

  void select();

  void accept(String accept);

  void alt(String alt);

  void checked(boolean checked);

  void defaultChecked(boolean defaultChecked);

  void defaultValue(String defaultValue);

  void maxLength(int maxLength);

  void readOnly(boolean readOnly);

  void size(int size);

  void src(String src);

  void useMap(boolean useMap);

  boolean useMap();

  String value();

  Element value(String value);
}