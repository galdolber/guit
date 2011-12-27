package com.guit.client.dom;

import com.guit.client.Implementation;
import com.guit.client.dom.impl.SourceImpl;
import com.guit.client.junit.Mock;
import com.guit.junit.dom.SourceMock;

@Implementation(SourceImpl.class)
@Mock(SourceMock.class)
public interface Source extends Element {

  String src();

  String type();

  void src(String url);

  void type(String type);
}