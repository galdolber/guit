package com.guit.client.binder;

import com.google.gwt.user.client.Element;

import com.guit.client.Implementation;

@Implementation(IElementImpl.class)
public interface IElement extends ViewAccesor {
  Element getElement();
}
