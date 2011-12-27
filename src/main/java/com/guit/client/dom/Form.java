package com.guit.client.dom;

import com.guit.client.Implementation;
import com.guit.client.dom.impl.FormImpl;
import com.guit.client.junit.Mock;
import com.guit.junit.dom.FormMock;

import java.util.List;

@Implementation(FormImpl.class)
@Mock(FormMock.class)
public interface Form extends Element {

  Element getField(String name);

  void setFieldValue(String name, String value);

  String getFieldValue(String name);

  String acceptCharset();

  String action();

  List<Element> elements();

  String enctype();

  String method();

  String name();

  String target();

  void reset();

  void acceptCharset(String acceptCharset);

  void action(String action);

  void enctype(String enctype);

  void method(String method);

  void name(String name);

  void target(String target);

  void submit();
}