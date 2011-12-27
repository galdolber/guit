package com.guit.client.dom;

import com.guit.client.Implementation;
import com.guit.client.dom.impl.TitleImpl;
import com.guit.client.junit.Mock;
import com.guit.junit.dom.TitleMock;

@Implementation(TitleImpl.class)
@Mock(TitleMock.class)
public interface Title extends Element {
}