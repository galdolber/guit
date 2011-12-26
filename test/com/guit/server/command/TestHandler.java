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
package com.guit.server.command;

import com.guit.client.command.action.CommandException;
import com.guit.client.command.action.Handler;
import com.guit.client.command.action.TestAction;
import com.guit.client.command.action.TestAction.TestResponse;

public class TestHandler implements Handler<TestAction, TestResponse> {

  @Override
  public TestResponse handle(TestAction action) throws CommandException {
    if (action.isFail()) {
      throw new CommandException("Some message");
    }
    return new TestResponse();
  }
}
