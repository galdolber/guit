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
import com.guit.client.command.action.NonSerializableAction;
import com.guit.client.command.action.NonSerializableAction.TestNonSerializableResponse;

public class NonSerializableCommandGwtTest extends BaseCommandGwtTest {

  // This test is expected to fail with a RuntimeException
  // TODO A better way to test this
  public void testSingleSerializationExceptionThatMustFail() {
    commandService.execute(new NonSerializableAction(false),
        new AbstractAsyncCallback<TestNonSerializableResponse>() {
          @Override
          public void success(TestNonSerializableResponse result) {
            fail();
          }

          @Override
          public void failure(Throwable caught) {
            fail();
          }
        });
    delayTestFinish(1000);
  }

  // This test is expected to fail with a RuntimeException
  // TODO A better way to test this
  public void testBatchSerializationExceptionThatMustFail() {
    commandService.execute(new NonSerializableAction(false),
        new AbstractAsyncCallback<TestNonSerializableResponse>() {
          @Override
          public void success(TestNonSerializableResponse result) {
            fail();
          }

          @Override
          public void failure(Throwable caught) {
            fail();
          }
        });

    commandService.execute(new NonSerializableAction(false),
        new AbstractAsyncCallback<TestNonSerializableResponse>() {
          @Override
          public void success(TestNonSerializableResponse result) {
            fail();
          }

          @Override
          public void failure(Throwable caught) {
            fail();
          }
        });
    delayTestFinish(1000);
  }
}
