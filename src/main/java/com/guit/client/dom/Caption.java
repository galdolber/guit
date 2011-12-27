package com.guit.client.dom;

import com.guit.client.Implementation;
import com.guit.client.dom.impl.CaptionImpl;
import com.guit.client.junit.Mock;
import com.guit.junit.dom.CaptionMock;

@Implementation(CaptionImpl.class)
@Mock(CaptionMock.class)
public interface Caption extends Element {
}