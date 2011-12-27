package com.guit.client.dom;

import com.guit.client.Implementation;
import com.guit.client.dom.impl.H1Impl;
import com.guit.client.junit.Mock;
import com.guit.junit.dom.H1Mock;

@Implementation(H1Impl.class)
@Mock(H1Mock.class)
public interface H1 extends Element {
}