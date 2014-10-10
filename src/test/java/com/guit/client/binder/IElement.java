package com.guit.client.binder;

import com.google.gwt.dom.client.Element;
import com.guit.client.Implementation;
import com.guit.client.ViewAccesor;

@Implementation(IElementImpl.class)
public interface IElement extends ViewAccesor {
  Element getElement();
}
