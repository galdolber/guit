package com.guit.client.dom;

import com.guit.client.Implementation;
import com.guit.client.dom.impl.LinkImpl;
import com.guit.client.junit.Mock;
import com.guit.junit.dom.LinkMock;

@Implementation(LinkImpl.class)
@Mock(LinkMock.class)
public interface Li extends Element {
}