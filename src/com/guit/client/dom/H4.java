package com.guit.client.dom;

import com.guit.client.Implementation;
import com.guit.client.dom.impl.H4Impl;
import com.guit.client.junit.Mock;
import com.guit.junit.dom.H4Mock;

@Implementation(H4Impl.class)
@Mock(H4Mock.class)
public interface H4 extends Element {
}