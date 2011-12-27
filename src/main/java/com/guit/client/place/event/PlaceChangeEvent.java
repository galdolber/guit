package com.guit.client.place.event;

import com.google.gwt.event.shared.GwtEvent;

public class PlaceChangeEvent extends GwtEvent<PlaceChangeHandler> {

  private static Type<PlaceChangeHandler> type;

  public static Type<PlaceChangeHandler> getType() {
    return type != null ? type : (type = new Type<PlaceChangeHandler>());
  }

  private final Class<?> placeClass;

  private final Object placeData;

  public PlaceChangeEvent(Class<?> placeClass, Object placeData) {
    this.placeClass = placeClass;
    this.placeData = placeData;
  }

  @Override
  protected void dispatch(PlaceChangeHandler handler) {
    handler.onPlaceChange(this);
  }

  @Override
  public Type<PlaceChangeHandler> getAssociatedType() {
    return getType();
  }

  public Class<?> getPlaceClass() {
    return placeClass;
  }

  public Object getPlaceData() {
    return placeData;
  }

  @Override
  public String toDebugString() {
    return super.toDebugString() + toString();
  }

  @Override
  public String toString() {
    return "[placeClass=" + placeClass + ", placeData=" + placeData + "]";
  }
}
