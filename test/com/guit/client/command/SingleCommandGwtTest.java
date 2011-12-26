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

import com.guit.client.async.AbstractAsyncCallback;
import com.guit.client.command.action.TestAction;
import com.guit.client.command.action.TestAction.TestResponse;

public class SingleCommandGwtTest extends BaseCommandGwtTest {

  public void testSingleRequest() {
    commandService.execute(new TestAction(), new AbstractAsyncCallback<TestResponse>() {
      @Override
      public void success(TestResponse result) {
        finishTest();
      }

      @Override
      public void failure(Throwable caught) {
        fail();
      }
    });
    delayTestFinish(1000);
  }
}
