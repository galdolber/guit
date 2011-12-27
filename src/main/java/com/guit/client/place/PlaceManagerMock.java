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

public class PlaceManagerMock implements PlaceManager {

  private static final String ERROR =
      "The place manager is not activated. Make sure you don't have this line in your gwt xml module: <set-configuration-property name=\"app.use.place\"value=\"false\" />";

  @Override
  public String getCurrentToken() {
    if (!GWT.isClient()) {
      throw new IllegalStateException(ERROR);
    } else {
      return null;
    }
  }

  @Override
  public <D> String getToken(Class<? extends Place<D>> placeClass) {
    if (!GWT.isScript()) {
      throw new IllegalStateException(ERROR);
    } else {
      return null;
    }
  }

  @Override
  public <D> String getToken(Class<? extends Place<D>> placeClass, D placeData) {
    if (!GWT.isScript()) {
      throw new IllegalStateException(ERROR);
    } else {
      return null;
    }
  }

  @Override
  public <D> void go(Class<? extends Place<D>> placeClass) {
    if (!GWT.isClient()) {
      throw new IllegalStateException(ERROR);
    }
  }

  @Override
  public <D> void go(Class<? extends Place<D>> placeClass, D placeData) {
    if (!GWT.isClient()) {
      throw new IllegalStateException(ERROR);
    }
  }

  @Override
  public void go(String token) {
    if (!GWT.isClient()) {
      throw new IllegalStateException(ERROR);
    }
  }

  @Override
  public void goBack() {
    if (!GWT.isClient()) {
      throw new IllegalStateException(ERROR);
    }
  }

  @Override
  public void goForward() {
    if (!GWT.isClient()) {
      throw new IllegalStateException(ERROR);
    }
  }

  @Override
  public void goDefault() {
    if (!GWT.isClient()) {
      throw new IllegalStateException(ERROR);
    }
  }

  @Override
  public <D> void newItem(Class<? extends Place<D>> placeClass) {
    if (!GWT.isClient()) {
      throw new IllegalStateException(ERROR);
    }
  }

  @Override
  public <D> void newItem(Class<? extends Place<D>> placeClass, D placeData) {
    if (!GWT.isClient()) {
      throw new IllegalStateException(ERROR);
    }
  }

  @Override
  public void newItem(String token) {
    if (!GWT.isClient()) {
      throw new IllegalStateException(ERROR);
    }
  }

  @Override
  public <D> void newItem(Class<? extends Place<D>> placeClass, D placeData, D defaultPlaceData) {
    if (!GWT.isClient()) {
      throw new IllegalStateException(ERROR);
    }
  }
}
