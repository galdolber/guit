package com.guit.client.dom;

import com.guit.client.Implementation;
import com.guit.client.dom.impl.ButtonImpl;
import com.guit.client.junit.Mock;
import com.guit.junit.dom.ButtonMock;

@Implementation(ButtonImpl.class)
@Mock(ButtonMock.class)
public interface Button extends Element {

  void click();
}