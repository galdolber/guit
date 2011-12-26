package com.guit.client.dom;

import com.guit.client.Implementation;
import com.guit.client.dom.impl.H6Impl;
import com.guit.client.junit.Mock;
import com.guit.junit.dom.H6Mock;

@Implementation(H6Impl.class)
@Mock(H6Mock.class)
public interface H6 extends Element {
}