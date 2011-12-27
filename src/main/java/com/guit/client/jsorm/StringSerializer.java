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
package com.guit.client.jsorm;

import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;

public class StringSerializer extends AbstractSerializer<String> {

  public static StringSerializer singleton;

  public static StringSerializer getSingleton() {
    return singleton == null ? (singleton = new StringSerializer()) : singleton;
  }

  @Override
  protected String deserializeJson(JSONValue o) {
    return o.isString().stringValue();
  }

  @Override
  protected JSONValue serializeJson(String data) {
    return new JSONString(data);
  }
}
