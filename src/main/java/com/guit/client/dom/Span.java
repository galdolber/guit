package com.guit.client.dom;

import com.guit.client.Implementation;
import com.guit.client.dom.impl.SpanImpl;
import com.guit.client.junit.Mock;
import com.guit.junit.dom.SpanMock;

@Implementation(SpanImpl.class)
@Mock(SpanMock.class)
public interface Span extends Element {
}