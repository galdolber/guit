package com.guit.client.dom;

import com.guit.client.Implementation;
import com.guit.client.dom.impl.H3Impl;
import com.guit.client.junit.Mock;
import com.guit.junit.dom.H3Mock;

@Implementation(H3Impl.class)
@Mock(H3Mock.class)
public interface H3 extends Element {
}