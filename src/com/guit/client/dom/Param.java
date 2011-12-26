package com.guit.client.dom;

import com.guit.client.Implementation;
import com.guit.client.dom.impl.ParamImpl;
import com.guit.client.junit.Mock;
import com.guit.junit.dom.ParamMock;

@Implementation(ParamImpl.class)
@Mock(ParamMock.class)
public interface Param extends Element {

  String name();

  String value();

  void name(String name);

  void value(String value);
}