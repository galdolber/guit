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

import com.google.gwt.user.client.ui.InlineHyperlink;

import com.guit.client.GuitEntryPoint;

public class PlaceInlineHyperlink extends InlineHyperlink {

  private static final PlaceManager placeManager = GuitEntryPoint.getPlaceManager();

  public PlaceInlineHyperlink() {
  }

  public <P extends Place<D>, D> PlaceInlineHyperlink(Class<P> placeClass) {
    setPlace(placeClass);
  }

  public <P extends Place<D>, D> PlaceInlineHyperlink(Class<P> placeClass, D data) {
    setPlace(placeClass, data);
  }

  public <P extends Place<D>, D> void setPlace(Class<P> placeClass) {
    setTargetHistoryToken(placeManager.getToken(placeClass, null));
  }

  public <P extends Place<D>, D> void setPlace(Class<P> placeClass, D data) {
    setTargetHistoryToken(placeManager.getToken(placeClass, data));
  }
}
