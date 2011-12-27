package com.guit.client.dom;

import com.guit.client.Implementation;
import com.guit.client.dom.impl.DivImpl;
import com.guit.client.junit.Mock;
import com.guit.junit.dom.DivMock;

@Implementation(DivImpl.class)
@Mock(DivMock.class)
public interface Div extends Element {
}