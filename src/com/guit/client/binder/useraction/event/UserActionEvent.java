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
package com.guit.client.binder.useraction.event;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.GwtEvent;

public class UserActionEvent extends GwtEvent<UserActionHandler> {

  public static Type<UserActionHandler> getType() {
    return type != null ? type : (type = new Type<UserActionHandler>());
  }

  private final String userAction;

  private final String category;

  private String text;

  private Integer number;

  private static Type<UserActionHandler> type;

  public UserActionEvent(String category, String userAction) {
    this.category = category;
    this.userAction = userAction;
  }

  public UserActionEvent(String component, String userAction, String text, int number) {
    this(component, userAction);
    this.text = text;
    this.number = number;
  }

  @Override
  protected void dispatch(UserActionHandler handler) {
    handler.onUserAction(this);
  }

  @Override
  public Type<UserActionHandler> getAssociatedType() {
    return getType();
  }

  public String getCategory() {
    return category;
  }

  public Integer getNumber() {
    return number;
  }

  public String getText() {
    return text;
  }

  public String getUserAction() {
    return userAction;
  }

  @Override
  public String toDebugString() {
    if (!GWT.isScript()) {
      return super.toDebugString() + " Component: " + category + ", Action: " + userAction
          + ", Text: " + text + ", Number: " + number;
    } else {
      return super.toDebugString();
    }
  }
}
