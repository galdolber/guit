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

import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONValue;

public class LongSerializer extends AbstractSerializer<Long> {

  public static LongSerializer singleton;

  public static LongSerializer getSingleton() {
    return singleton == null ? (singleton = new LongSerializer()) : singleton;
  }

  @Override
  protected Long deserializeJson(JSONValue o) {
    return (long) o.isNumber().doubleValue();
  }

  @Override
  protected JSONValue serializeJson(Long data) {
    return new JSONNumber(data);
  }
}
