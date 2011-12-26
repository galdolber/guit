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

import com.google.gwt.core.client.RunAsyncCallback;

public abstract class AbstractRunAsyncCallback implements RunAsyncCallback {

  public AbstractRunAsyncCallback() {
  }

  public void failure(Throwable caught) {
  }

  @Override
  public final void onFailure(Throwable caught) {
    failure(caught);
  }

  @Override
  public final void onSuccess() {
    success();
  }

  public abstract void success();
}
