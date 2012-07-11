package com.guit.client.dom;

import com.guit.client.Implementation;
import com.guit.client.dom.impl.IImpl;
import com.guit.client.junit.Mock;
import com.guit.junit.dom.IMock;

@Implementation(IImpl.class)
@Mock(IMock.class)
public interface I extends Element {
}
