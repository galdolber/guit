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

public class AsyncExceptionEvent extends GwtEvent<AsyncExceptionHandler> {

  private static Type<AsyncExceptionHandler> type;

  public static Type<AsyncExceptionHandler> getType() {
    return type != null ? type : (type = new Type<AsyncExceptionHandler>());
  }

  private final Throwable caught;

  public AsyncExceptionEvent(Throwable caught) {
    this.caught = caught;
  }

  @Override
  protected void dispatch(AsyncExceptionHandler handler) {
    handler.onRunAsyncException(this);
  }

  @Override
  public Type<AsyncExceptionHandler> getAssociatedType() {
    return getType();
  }

  public Throwable getCaught() {
    return caught;
  }

  @Override
  public String toDebugString() {
    return super.toDebugString() + "[caught=" + caught.toString() + "]";
  }
}
