package com.guit.client.dom;

import com.guit.client.Implementation;
import com.guit.client.dom.impl.SelectImpl;
import com.guit.client.junit.Mock;
import com.guit.junit.dom.SelectMock;

@Implementation(SelectImpl.class)
@Mock(SelectMock.class)
public interface Select extends Element {

  void clear();

  int length();

  String getMultiple();

  int selectedIndex();

  int size();

  boolean multiple();

  void removeItem(int index);

  void multiple(boolean multiple);

  void selectedIndex(int index);

  void size(int size);
}