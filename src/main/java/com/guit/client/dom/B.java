package com.guit.client.dom;

import com.guit.client.Implementation;
import com.guit.client.dom.impl.BImpl;
import com.guit.client.junit.Mock;
import com.guit.junit.dom.BMock;

@Implementation(BImpl.class)
@Mock(BMock.class)
public interface B extends Element {
}
