package com.guit.client.dom;

import com.guit.client.Implementation;
import com.guit.client.dom.impl.BrImpl;
import com.guit.client.junit.Mock;
import com.guit.junit.dom.BrMock;

@Implementation(BrImpl.class)
@Mock(BrMock.class)
public interface Br extends Element {
}