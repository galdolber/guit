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

/**
 * The interface to the browser history.
 */
public interface PlaceManager {

  /**
   * @return The current token.
   */
  String getCurrentToken();

  /**
   * Gets a token.
   * 
   * @param placeClass Place class
   * @return The token
   */
  <D> String getToken(Class<? extends Place<D>> placeClass);

  /**
   * Gets a token.
   * 
   * @param placeClass Place class
   * @param placeData Place data
   * @return The token
   */
  <D> String getToken(Class<? extends Place<D>> placeClass, D placeData);

  /**
   * Goes to a place. Fires a PlaceChange event.
   * 
   * @param placeClass The place class
   */
  <D> void go(Class<? extends Place<D>> placeClass);

  /**
   * Goes to a place. Fires a PlaceChange event.
   * 
   * @param placeClass The place class
   * @param placeData The place data
   */
  <D> void go(Class<? extends Place<D>> placeClass, D placeData);

  /**
   * Goes to a place. Fires a PlaceChange event.
   * 
   * @param token The place token, can have data
   */
  void go(String token);

  /**
   * Goes back. This is a simple wrapper to History.back(), we don't hold state.
   */
  void goBack();

  /**
   * Goes forward. This is a simple wrapper to History.forward(), we don't hold
   * state.
   */
  void goForward();

  /**
   * Goes to the default place.
   */
  void goDefault();

  /**
   * Makes a new history token.
   * 
   * @param placeClass Place class
   */
  <D> void newItem(Class<? extends Place<D>> placeClass);

  /**
   * Makes a new history token.
   * 
   * @param placeClass Place class
   * @param placeData Place data
   */
  <D> void newItem(Class<? extends Place<D>> placeClass, D placeData);

  /**
   * Makes a new history token, but its smart to know when no to create a new
   * token to avoid locks.
   * 
   * @param placeClass Place class
   * @param placeData Place data
   * @param defaultPlaceData The default data of the place
   */
  <D> void newItem(Class<? extends Place<D>> placeClass, D placeData, D defaultPlaceData);

  /**
   * Makes a new history token from a string.
   * 
   * @param token The token
   */
  void newItem(String token);
}
