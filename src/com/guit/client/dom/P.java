package com.guit.client.dom;

import com.guit.client.Implementation;
import com.guit.client.dom.impl.PImpl;
import com.guit.client.junit.Mock;
import com.guit.junit.dom.PMock;

@Implementation(PImpl.class)
@Mock(PMock.class)
public interface P extends Element {
}