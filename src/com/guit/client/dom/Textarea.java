package com.guit.client.dom;

import com.guit.client.Implementation;
import com.guit.client.dom.impl.TextareaImpl;
import com.guit.client.junit.Mock;
import com.guit.junit.dom.TextareaMock;

@Implementation(TextareaImpl.class)
@Mock(TextareaMock.class)
public interface Textarea extends Element {

  int cols();

  String defaultValue();

  int rows();

  String value();

  boolean readOnly();

  void select();

  void cols(int cols);

  void defaultValue(String defaultValue);

  void readOnly(boolean readOnly);

  void rows(int rows);

  Element value(String value);
}