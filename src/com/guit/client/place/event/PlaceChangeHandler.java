package com.guit.client.place.event;

import com.google.gwt.event.shared.EventHandler;

public interface PlaceChangeHandler extends EventHandler {

  void onPlaceChange(PlaceChangeEvent event);
}
