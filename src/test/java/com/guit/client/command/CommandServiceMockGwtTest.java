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
package com.guit.client.command;

import com.google.gwt.junit.client.GWTTestCase;

import com.guit.client.async.AbstractAsyncCallback;
import com.guit.client.command.action.Action;
import com.guit.client.command.action.Response;

public class CommandServiceMockGwtTest extends GWTTestCase {

  @Override
  public String getModuleName() {
    return "com.guit.CommandModule";
  }

  CommandServiceMock mock = new CommandServiceMock();

  public void testExecute() {
    try {
      AbstractAsyncCallback<Response> b = null;
      Action<Response> a = null;
      mock.execute(a, b);
    } catch (IllegalStateException e) {
      return;
    }
    fail();
  }

  public void testExecuteLater() {
    try {
      AbstractAsyncCallback<Response> b = null;
      Action<Response> a = null;
      mock.executeLater(a, b);
    } catch (IllegalStateException e) {
      return;
    }
    fail();
  }
}
