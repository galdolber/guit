package com.guit.client.place;

import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.junit.client.GWTTestCase;
import com.google.inject.Provider;

import com.guit.client.jsorm.IntegerSerializer;

public class SmartNewItemGwtTest extends GWTTestCase {

  @Override
  public String getModuleName() {
    return "com.guit.Guit";
  }

  public void test() {
    PlaceManagerImpl placeManager = new PlaceManagerImpl(new PlaceManagerInitializer() {
      @Override
      public void initialize(PlaceManagerImpl placeManager) {
      }
    }, null, new SimpleEventBus());

    String token = "my_place";
    placeManager.addPlace(MyPlace.class, token, "", new Provider<MyPlace>() {
      @Override
      public MyPlace get() {
        return new MyPlace();
      }
    }, IntegerSerializer.getSingleton(), false);

    placeManager.go(MyPlace.class);
    placeManager.newItem(MyPlace.class, null, null);
    assertEquals("!" + token, placeManager.getCurrentToken());

    placeManager.newItem(MyPlace.class, 3, 3);
    assertEquals("!" + token, placeManager.getCurrentToken());

    placeManager.setDefaultPlace(token);

    placeManager.newItem(MyPlace.class, 1, 1);
    assertEquals("", placeManager.getCurrentToken());

    placeManager.go(MyPlace.class, 3);
    assertEquals(placeManager.getToken(MyPlace.class, 3), placeManager.getCurrentToken());
    placeManager.newItem(MyPlace.class, 1, 1);
    assertEquals("", placeManager.getCurrentToken());
  }
}
