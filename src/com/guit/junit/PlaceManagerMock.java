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
package com.guit.junit;

import com.google.gwt.user.client.History;

import com.guit.client.place.Place;
import com.guit.client.place.PlaceManager;

public class PlaceManagerMock implements PlaceManager {

  private Class<?> placeClass;
  private Object placeData;
  private String token = "";
  private Class<? extends Place<?>> defaultPlace;

  private boolean wentBack;

  @Override
  public String getCurrentToken() {
    return token;
  }

  @Override
  public <D> String getToken(Class<? extends Place<D>> placeClass) {
    return getToken(placeClass, null);
  }

  @Override
  public <D> String getToken(Class<? extends Place<D>> placeClass, D placeData) {
    return placeClass.getSimpleName() + String.valueOf(placeData);
  }

  @Override
  public <D> void go(Class<? extends Place<D>> placeClass) {
    this.placeClass = placeClass;
  }

  @Override
  public <D> void go(Class<? extends Place<D>> placeClass, D placeData) {
    this.placeClass = placeClass;
    this.placeData = placeData;
  }

  @Override
  public void go(String token) {
    this.token = token;
  }

  @Override
  public void goBack() {
    wentBack = true;
  }

  @Override
  public void goForward() {
  }

  @Override
  public void goDefault() {
  }

  @Override
  public <D> void newItem(Class<? extends Place<D>> placeClass) {
    go(placeClass);
  }

  @Override
  public <D> void newItem(Class<? extends Place<D>> placeClass, D placeData) {
    go(placeClass, placeData);
  }

  @Override
  public void newItem(String token) {
    go(token);
  }

  public Class<?> getPlaceClass() {
    return placeClass;
  }

  public Object getPlaceData() {
    return placeData;
  }

  public String getToken() {
    return token;
  }

  @Override
  public <D> void newItem(Class<? extends Place<D>> placeClass, D placeData, D defaultPlaceData) {
    String token = getToken(placeClass, placeData);
    String currentToken = getCurrentToken();

    // If there is any change
    if (!currentToken.equals(token)) {

      if (placeData == null) {
        // If default place
        if (defaultPlace != null && placeClass.equals(defaultPlace)) {
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
          if (defaultPlace != null && placeClass.equals(defaultPlace)) {

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

  public void setDefaultPlace(Class<? extends Place<?>> defaultPlace) {
    this.defaultPlace = defaultPlace;
  }

  public Class<? extends Place<?>> getDefaultPlace() {
    return defaultPlace;
  }

  public boolean isWentBack() {
    return wentBack;
  }

  public void setWentBack(boolean wentBack) {
    this.wentBack = wentBack;
  }
}
