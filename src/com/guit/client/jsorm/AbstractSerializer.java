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

import com.google.gwt.json.client.JSONNull;
import com.google.gwt.json.client.JSONValue;

public abstract class AbstractSerializer<T> implements TypeJsonSerializer<T> {

  @Override
  public final T deserialize(JSONValue data) {
    if (data.isNull() == null) {
      return deserializeJson(data);
    }
    return null;
  }

  protected abstract T deserializeJson(JSONValue o);

  @Override
  public final JSONValue serialize(T data) {
    if (data != null) {
      return serializeJson(data);
    }
    return JSONNull.getInstance();
  }

  protected abstract JSONValue serializeJson(T data);
}
