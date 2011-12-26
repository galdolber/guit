package com.guit.client.dom;

import com.guit.client.Implementation;
import com.guit.client.dom.impl.BodyImpl;
import com.guit.client.junit.Mock;
import com.guit.junit.dom.BodyMock;

@Implementation(BodyImpl.class)
@Mock(BodyMock.class)
public interface Body extends Element {
}