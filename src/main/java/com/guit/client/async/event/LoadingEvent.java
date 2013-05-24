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
package com.guit.client.async.event;

import com.google.gwt.event.shared.GwtEvent;

public class LoadingEvent extends GwtEvent<LoadingHandler> {

  private static Type<LoadingHandler> type;

  public static Type<LoadingHandler> getType() {
    return type != null ? type : (type = new Type<LoadingHandler>());
  }

  private final boolean completed;

  public LoadingEvent(boolean completed) {
    this.completed = completed;
  }

  @Override
  protected void dispatch(LoadingHandler handler) {
    handler.onLoading(this);
  }

  @Override
  public Type<LoadingHandler> getAssociatedType() {
    return getType();
  }

  public boolean isCompleted() {
    return completed;
  }

  @Override
  public String toDebugString() {
    return super.toDebugString() + "[completed=" + completed + "]";
  }
}
