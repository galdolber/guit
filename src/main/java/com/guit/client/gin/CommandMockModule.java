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
package com.guit.client.gin;

import com.google.gwt.inject.client.AbstractGinModule;
import com.google.inject.Singleton;

import com.guit.client.command.CommandService;
import com.guit.client.command.CommandServiceMock;

public class CommandMockModule extends AbstractGinModule {

  @Override
  protected void configure() {
    bind(CommandService.class).to(CommandServiceMock.class).in(Singleton.class);
  }
}
