package com.guit.client.dom;

import com.guit.client.Implementation;
import com.guit.client.dom.impl.OlImpl;
import com.guit.client.junit.Mock;
import com.guit.junit.dom.OlMock;

@Implementation(OlImpl.class)
@Mock(OlMock.class)
public interface Ol extends Element {
}