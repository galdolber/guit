package com.guit.client.dom;

import com.guit.client.Implementation;
import com.guit.client.dom.impl.LabelImpl;
import com.guit.client.junit.Mock;
import com.guit.junit.dom.LabelMock;

@Implementation(LabelImpl.class)
@Mock(LabelMock.class)
public interface Label extends Element {

  String accessKey();

  String htmlFor();

  void accessKey(String accessKey);

  void htmlFor(String htmlFor);
}