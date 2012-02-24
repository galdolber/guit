/*
 * Copyright 2010 Gal Dolber.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.guit.client.place;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.inject.client.AsyncProvider;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.logging.client.LogConfiguration;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.Window.ClosingEvent;
import com.google.gwt.user.client.Window.ClosingHandler;
import com.google.inject.Inject;
import com.google.inject.Provider;

import com.guit.client.async.AbstractAsyncCallback;
import com.guit.client.jsorm.TypeJsonSerializer;
import com.guit.client.place.event.PlaceChangeEvent;

import java.util.HashMap;
import java.util.logging.Logger;

public class PlaceManagerImpl implements PlaceManager, ValueChangeHandler<String>, ClosingHandler {

  private static interface PlaceCallback<D> {
    void onSuccess(Place<D> place);
  }

  private static final String PLACENAMESEPARATOR = "/";

  private final String defaultTitle;

  protected Place<?> currentPlace;
  protected String currentPlaceName;
  protected Object currentData;

  protected String defaultPlace;

  private final Crypter crypter;

  protected final HashMap<Class<?>, String> placesNames = new HashMap<Class<?>, String>();
  protected final HashMap<String, String> placesTitles = new HashMap<String, String>();
  protected final HashMap<String, Place<?>> places = new HashMap<String, Place<?>>();
  protected final HashMap<String, Provider<? extends Place<?>>> providedPlaces =
      new HashMap<String, Provider<? extends Place<?>>>();
  protected final HashMap<String, AsyncProvider<? extends Place<?>>> asyncProvidedPlaces =
      new HashMap<String, AsyncProvider<? extends Place<?>>>();

  protected final HashMap<String, TypeJsonSerializer<?>> serializers =
      new HashMap<String, TypeJsonSerializer<?>>();
  protected final HashMap<String, Boolean> serializersEncrypted = new HashMap<String, Boolean>();

  private final Logger logger = Logger.getLogger("Place Manager");

  private final EventBus eventBus;

  private int placeChangeCount = 0;

  @Inject
  public PlaceManagerImpl(final PlaceManagerInitializer initializer, Crypter crypter,
      EventBus eventBus) {
    this.eventBus = eventBus;
    this.crypter = crypter;
    this.defaultTitle = Window.getTitle();

    initializer.initialize(this);
  }

  public <P extends Place<D>, D> void addPlace(Class<P> clazz, String token, String title,
      AsyncProvider<P> placeProvider, TypeJsonSerializer<D> dataSerializer, boolean encrypted) {
    placesNames.put(clazz, token);
    serializersEncrypted.put(token, encrypted);
    serializers.put(token, dataSerializer);
    asyncProvidedPlaces.put(token, placeProvider);
    placesTitles.put(token, title);
  }

  public <P extends Place<D>, D> void addPlace(Class<P> clazz, String token, String title,
      Provider<P> placeProvider, TypeJsonSerializer<D> dataSerializer, boolean encrypted) {
    placesNames.put(clazz, token);
    serializersEncrypted.put(token, encrypted);
    serializers.put(token, dataSerializer);
    providedPlaces.put(token, placeProvider);
    placesTitles.put(token, title);
  }

  @Override
  public String getCurrentToken() {
    return History.getToken();
  }

  @SuppressWarnings("unchecked")
  private <D> void getPlace(final String placeName, final PlaceCallback<D> callback) {
    if (places.containsKey(placeName)) {
      callback.onSuccess((Place<D>) places.get(placeName));
    } else if (providedPlaces.containsKey(placeName)) {
      Provider<? extends Place<?>> placeProvider = providedPlaces.get(placeName);
      Place<?> place = placeProvider.get();
      places.put(placeName, place);
      callback.onSuccess((Place<D>) place);
    } else if (asyncProvidedPlaces.containsKey(placeName)) {
      AsyncProvider<Place<D>> asyncProvider =
          (AsyncProvider<Place<D>>) asyncProvidedPlaces.get(placeName);
      asyncProvider.get(new AbstractAsyncCallback<Place<D>>() {
        @Override
        public void success(Place<D> place) {
          places.put(placeName, place);
          callback.onSuccess(place);
        }
      });
    } else {
      // The exception is only for development mode
      assert false : "Error on history manager. The place " + placeName
          + " is not registered. It should be binded as Singleton.";

      // In production we just go to the default place
      if (defaultPlace != null) {
        getPlace(defaultPlace, callback);
      }
    }
  }

  @Override
  public <D> String getToken(Class<? extends Place<D>> placeClass) {
    return getToken(placeClass, null);
  }

  @Override
  public <D> String getToken(Class<? extends Place<D>> placeClass, D placeData) {
    String placeName = placesNames.get(placeClass);
    assert placeName != null : "Error on history manager. The place " + placeClass.getName()
        + " is not registered. It should be binded as Singleton.";
    return getToken(placeName, placeData);
  }

  @SuppressWarnings("unchecked")
  protected <D> String getToken(String placeName, D placeData) {
    TypeJsonSerializer<D> s = (TypeJsonSerializer<D>) serializers.get(placeName);
    boolean encrypted = serializersEncrypted.get(placeName);
    if (s != null) {
      if (placeData != null) {
        String json = s.serialize(placeData).toString();
        return "!" + placeName + PLACENAMESEPARATOR + (encrypted ? crypter.encode(json) : json);
      }
    } else {
      assert false : "Error on history manager. The place " + placeName
          + " is not registered. It should be binded as Singleton.";
    }
    return "!" + placeName;
  }

  @Override
  public <D> void go(Class<? extends Place<D>> placeClass) {
    go(placeClass, null);
  }

  @Override
  public <D> void go(Class<? extends Place<D>> placeClass, final D placeData) {
    if (LogConfiguration.loggingIsEnabled()) {
      logger.info("PlaceGo=[" + getToken(placeClass, placeData) + "]");
    }
    History.newItem(getToken(placeClass, placeData), false);
    goToPlace(placesNames.get(placeClass), placeData);
  }

  @Override
  public void go(String token) {
    History.newItem(token, false);
    onTokenChange(token);
  }

  @Override
  public void goBack() {
    if (placeChangeCount == 1 && defaultPlace != null) {
      History.newItem(getToken(defaultPlace, null));
      goToDefaultPlace();
      if (LogConfiguration.loggingIsEnabled()) {
        logger.info("goBack to default place");
      }
    } else {
      History.back();
      if (LogConfiguration.loggingIsEnabled()) {
        logger.info("goBack");
      }
    }
  }

  @Override
  public void goForward() {
    History.forward();
  }

  @Override
  public void goDefault() {
    if (defaultPlace != null) {
      History.newItem(getToken(defaultPlace, null), false);
      goToDefaultPlace();
    }
  }

  private void goToDefaultPlace() {
    if (defaultPlace != null) {
      goToPlace(defaultPlace, null);
    }
  }

  private <D> void goToPlace(final String placeName, final D placeData) {
    if (currentPlace != null && currentPlace instanceof StayPlace) {
      StayPlace<?> stayPlace = (StayPlace<?>) currentPlace;
      String warning = stayPlace.mayLeave();
      if (warning != null) {
        if (!Window.confirm(warning)) {
          // Restore the token
          History.newItem(getToken(currentPlaceName, currentData), false);

          if (currentPlace instanceof StayPlaceWithCallback) {
            ((StayPlaceWithCallback<?>) stayPlace).stay();
          }
          return;
        } else if (currentPlace instanceof StayPlaceWithCallback) {
          ((StayPlaceWithCallback<?>) stayPlace).leave();
        }
      }
    } else if (currentPlace != null && currentPlace instanceof LeavePlace) {
      ((LeavePlace<?>) currentPlace).leave();
    }

    placeChangeCount++;

    getPlace(placeName, new PlaceCallback<D>() {
      @Override
      public void onSuccess(Place<D> place) {
        eventBus.fireEvent(new PlaceChangeEvent(place.getClass(), placeData));

        String title = placesTitles.get(placeName);
        if (!title.isEmpty()) {
          Window.setTitle(defaultTitle + " - " + title);
        } else {
          Window.setTitle(defaultTitle);
        }

        currentPlace = place;
        currentPlaceName = placeName;
        currentData = placeData;
        place.go(placeData);
      }
    });
  }

  @Override
  public <D> void newItem(Class<? extends Place<D>> placeClass) {
    newItem(placeClass, null);
  }

  @Override
  public <D> void newItem(Class<? extends Place<D>> placeClass, D placeData) {
    assert placesNames.containsKey(placeClass) : "The place " + placeClass.getName()
        + " is not registered. It should be binded as Singleton.";
    assert placesNames.get(placeClass).equals(currentPlaceName) : "You only can call newItem() for the current place. Otherwise call go().";
    if (LogConfiguration.loggingIsEnabled()) {
      logger.info("Place NewItem=[" + getToken(placeClass, placeData));
    }
    History.newItem(getToken(placeClass, placeData), false);
    eventBus.fireEvent(new PlaceChangeEvent(placeClass, placeData));
  }

  @Override
  public void newItem(String token) {
    // TODO Fire change event
    History.newItem(token, false);
  }

  /**
   * Simulate browser token change event (public for testing).
   */
  public void onTokenChange(String token) {
    if (LogConfiguration.loggingIsEnabled()) {
      logger.info("Place TokenChange=[" + token + "]");
    }

    if (token.isEmpty()) {
      goToDefaultPlace();
      return;
    }

    if (!token.startsWith("!")) {
      assert false : "Error on place manager. The token doesn't start with '!'. Found: " + token;

      goToDefaultPlace();
      return;
    }

    int nameSeparator = token.indexOf(PLACENAMESEPARATOR);
    if (nameSeparator != -1) {
      String placeName = token.substring(1, nameSeparator);
      String json = token.substring(nameSeparator + 1);

      Object data = null;
      if (!json.isEmpty()) {
        TypeJsonSerializer<?> typeJsonSerializer = serializers.get(placeName);
        boolean encrypted = serializersEncrypted.get(placeName);
        if (typeJsonSerializer == null) {
          // The exception is only for development mode
          assert false : "Error on history manager. The place " + placeName
              + " is not registered. It must be binded as Singleton.";

          goToDefaultPlace();
        }
        try {
          data =
              typeJsonSerializer.deserialize(JSONParser.parseStrict((encrypted ? crypter
                  .decode(json) : json)));
        } catch (Exception e) {
          if (GWT.isScript()) {
            goToDefaultPlace();
            return;
          } else {
            throw new IllegalStateException("Malformed place data", e);
          }
        }
      }
      goToPlace(placeName, data);
    } else {
      goToPlace(token.substring(1), null);
    }
  }

  /**
   * Token changed handler.
   */
  @Override
  public void onValueChange(ValueChangeEvent<String> event) {
    onTokenChange(event.getValue());
  }

  @Override
  public void onWindowClosing(ClosingEvent event) {
    if (currentPlace != null && currentPlace instanceof StayPlace) {
      String warning = ((StayPlace<?>) currentPlace).mayLeave();
      if (warning != null) {
        event.setMessage(warning);
      }
    }
  }

  /**
   * Set default place.
   */
  protected void setDefaultPlace(String defaultPlace) {
    assert providedPlaces.containsKey(defaultPlace)
        || asyncProvidedPlaces.containsKey(defaultPlace) : "The default place must implement "
        + Place.class.getName() + ". Found: " + defaultPlace;
    this.defaultPlace = defaultPlace;
  }

  @Override
  public String toString() {
    return placesNames.toString();
  }

  @Override
  public <D> void newItem(Class<? extends Place<D>> placeClass, D placeData, D defaultPlaceData) {
    String token = getToken(placeClass, placeData);
    String currentToken = getCurrentToken();

    // If there is any change
    if (!currentToken.equals(token)) {

      if (placeData == null) {
        // If default place
        if (defaultPlace != null && placesNames.get(placeClass).equals(defaultPlace)) {
          if (!currentToken.isEmpty() && !currentToken.equals(getToken(placeClass))) {
            History.newItem("");
          }
        } else if (!currentToken.equals(getToken(placeClass))) {
          newItem(placeClass);
        }
      } else {
        // If the place have his default data
        if (placeData.equals(defaultPlaceData)) {

          // If the place is the default place
          if (defaultPlace != null && placesNames.get(placeClass).equals(defaultPlace)) {

            // And we are in the default place we use empty place data
            if (!currentToken.isEmpty() && !currentToken.equals(getToken(placeClass))) {
              History.newItem("");
            }
          } else {
            newItem(placeClass, null);
          }
        } else {
          newItem(placeClass, placeData);
        }
      }
    }
  }
}
