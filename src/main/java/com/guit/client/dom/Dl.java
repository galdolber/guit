package com.guit.client.dom;

import com.guit.client.Implementation;
import com.guit.client.dom.impl.DlImpl;
import com.guit.client.junit.Mock;
import com.guit.junit.dom.DlMock;

@Implementation(DlImpl.class)
@Mock(DlMock.class)
public interface Dl extends Element {
}