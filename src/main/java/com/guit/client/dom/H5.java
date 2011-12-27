package com.guit.client.dom;

import com.guit.client.Implementation;
import com.guit.client.dom.impl.H5Impl;
import com.guit.client.junit.Mock;
import com.guit.junit.dom.H5Mock;

@Implementation(H5Impl.class)
@Mock(H5Mock.class)
public interface H5 extends Element {
}