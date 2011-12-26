package com.guit.client.dom;

import com.guit.client.Implementation;
import com.guit.client.dom.impl.TableImpl;
import com.guit.client.junit.Mock;
import com.guit.junit.dom.TableMock;

@Implementation(TableImpl.class)
@Mock(TableMock.class)
public interface Table extends Element {

  int border();

  int cellPadding();

  int cellSpacing();

  String frame();

  String rules();

  String tableWidth();

  void border(int border);

  void cellPadding(int cellPadding);

  void cellSpacing(int cellSpacing);

  void frame(String frame);

  void rules(String rules);

  void width(String width);
}