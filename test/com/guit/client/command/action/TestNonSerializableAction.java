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
package com.guit.client.command.action;

import com.guit.client.command.action.TestNonSerializableAction.TestNonSerializableResponse;

@ActionHandler("com.guit.server.command.TestHandler")
public class TestNonSerializableAction implements Action<TestNonSerializableResponse> {

  public static class TestNonSerializableResponse implements Response {

    // This action is not longer serializable
    // public NonSerializableResponse1() {
    // }
  }

  private boolean fail;

  // This action is not longer serializable
  // public TestNonSerializableAction() {
  // }

  public TestNonSerializableAction(boolean fail) {
    this.setFail(fail);
  }

  public void setFail(boolean fail) {
    this.fail = fail;
  }

  public boolean isFail() {
    return fail;
  }
}
