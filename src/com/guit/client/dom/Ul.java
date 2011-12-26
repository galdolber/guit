package com.guit.client.dom;

import com.guit.client.Implementation;
import com.guit.client.dom.impl.UlImpl;
import com.guit.client.junit.Mock;
import com.guit.junit.dom.UlMock;

@Implementation(UlImpl.class)
@Mock(UlMock.class)
public interface Ul extends Element {
}