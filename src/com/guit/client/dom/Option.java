package com.guit.client.dom;

import com.guit.client.Implementation;
import com.guit.client.dom.impl.OptionImpl;
import com.guit.client.junit.Mock;
import com.guit.junit.dom.OptionMock;

@Implementation(OptionImpl.class)
@Mock(OptionMock.class)
public interface Option extends Element {

  int index();

  String label();

  String value();

  boolean isDefaultSelected();

  boolean disabled();

  boolean selected();

  void defaultSelected(boolean selected);

  void disabled(boolean disabled);

  void label(String label);

  void selected(boolean selected);

  void value(String value);
}