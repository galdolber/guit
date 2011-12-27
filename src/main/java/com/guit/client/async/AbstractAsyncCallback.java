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
package com.guit.client.async;

import com.google.gwt.user.client.rpc.AsyncCallback;

import com.guit.client.command.action.CommandSerializable;

public abstract class AbstractAsyncCallback<T> implements AsyncCallback<T>, CommandSerializable {

  public AbstractAsyncCallback() {
  }

  public void failure(Throwable caught) {
  }

  @Override
  public final void onFailure(Throwable caught) {
    failure(caught);
  }

  @Override
  public final void onSuccess(T response) {
    success(response);
  }

  public abstract void success(T response);
}
