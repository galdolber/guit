package com.guit.client.dom;

import com.guit.client.Implementation;
import com.guit.client.dom.impl.ScriptImpl;
import com.guit.client.junit.Mock;
import com.guit.junit.dom.ScriptMock;

@Implementation(ScriptImpl.class)
@Mock(ScriptMock.class)
public interface Script extends Element {

  String defer();

  String src();

  String type();

  void defer(String defer);

  void src(String src);

  void type(String type);
}