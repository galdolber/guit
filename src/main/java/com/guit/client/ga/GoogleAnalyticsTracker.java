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
package com.guit.client.ga;

import com.google.gwt.core.client.GWT;
import com.google.inject.Inject;

import com.guit.client.GuitController;
import com.guit.client.binder.EventBusHandler;
import com.guit.client.binder.GuitBinder;
import com.guit.client.binder.useraction.event.UserActionEvent;
import com.guit.client.ga.GoogleAnalyticsTracker.GoogleAnalyticsUserActionTrackerBinder;
import com.guit.client.place.Place;
import com.guit.client.place.event.PlaceChangeEvent;

public class GoogleAnalyticsTracker extends GuitController<GoogleAnalyticsUserActionTrackerBinder> {

  public static interface GoogleAnalyticsUserActionTrackerBinder extends
      GuitBinder<GoogleAnalyticsTracker> {
  }

  private final GoogleAnalytics util;

  @Inject
  public GoogleAnalyticsTracker(GoogleAnalytics util) {
    this.util = util;
  }

  /**
   * Listen for place changes.
   */
  @EventBusHandler(PlaceChangeEvent.class)
  protected <D> void $placeChange(Class<? extends Place<D>> placeClass, D placeData) {
    trackPageView(placeClass, placeData);
  }

  /**
   * Listen for user actions.
   */
  @EventBusHandler(UserActionEvent.class)
  protected void $userAction(String category, String userAction, String text, Integer number) {
    if (GWT.isScript()) {
      if (text == null && number == null) {
        util.trackEvent(category, userAction);
      } else {
        util.trackEvent(category, userAction, text, number);
      }
    }
  }

  /**
   * Set account.
   */
  public void setAccount(String ua) {
    if (GWT.isScript()) {
      util.setAccount(ua);
    }
  }

  private <D> void trackPageView(Class<? extends Place<D>> placeClass, D placeData) {
    if (GWT.isScript()) {
      util.trackPageview(placeManager.getToken(placeClass, placeData));
    }
  }
}
