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
import com.guit.client.command.action.SometAction;
import com.guit.client.command.action.SometAction.TestResponse;
import com.guit.client.common.HasState;

public class BatchCommandGwtTest extends BaseCommandGwtTest {

  public void testSingleRequest() {
    final HasState<Integer> state = new HasState<Integer>();
    state.value = 0;
    commandService.execute(new SometAction(), new AbstractAsyncCallback<TestResponse>() {
      @Override
      public void success(TestResponse result) {
        state.value++;
      }

      @Override
      public void failure(Throwable caught) {
        fail();
      }
    });
    commandService.execute(new SometAction(), new AbstractAsyncCallback<TestResponse>() {
      @Override
      public void success(TestResponse result) {
        state.value++;
      }

      @Override
      public void failure(Throwable caught) {
        fail();
      }
    });
    commandService.execute(new SometAction(), new AbstractAsyncCallback<TestResponse>() {
      @Override
      public void success(TestResponse result) {
        state.value++;
      }

      @Override
      public void failure(Throwable caught) {
        fail();
      }
    });
    commandService.execute(new SometAction(), new AbstractAsyncCallback<TestResponse>() {
      @Override
      public void success(TestResponse result) {
        assertEquals(3, (int) state.value);
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
