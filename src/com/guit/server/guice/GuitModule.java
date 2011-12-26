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
package com.guit.server.guice;

import com.google.inject.servlet.ServletModule;

import com.guit.client.command.CommandRpc;
import com.guit.server.command.CommandRpcImpl;

public class GuitModule extends ServletModule {

  @Override
  protected void configureServlets() {
    bind(CommandRpc.class).to(CommandRpcImpl.class);
  }
}
