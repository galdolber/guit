package com.guit.client.dom;

import com.guit.client.Implementation;
import com.guit.client.dom.impl.H2Impl;
import com.guit.client.junit.Mock;
import com.guit.junit.dom.H2Mock;

@Implementation(H2Impl.class)
@Mock(H2Mock.class)
public interface H2 extends Element {
}