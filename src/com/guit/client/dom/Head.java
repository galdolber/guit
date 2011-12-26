package com.guit.client.dom;

import com.guit.client.Implementation;
import com.guit.client.dom.impl.HeadImpl;
import com.guit.client.junit.Mock;
import com.guit.junit.dom.HeadMock;

@Implementation(HeadImpl.class)
@Mock(HeadMock.class)
public interface Head extends Element {
}