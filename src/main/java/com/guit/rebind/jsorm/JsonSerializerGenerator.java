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
package com.guit.rebind.jsorm;

import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.user.rebind.ClassSourceFileComposerFactory;
import com.google.gwt.user.rebind.SourceWriter;

import com.guit.client.jsorm.JsonSerializer;
import com.guit.client.jsorm.TypeJsonSerializer;
import com.guit.rebind.common.AbstractGenerator;

public class JsonSerializerGenerator extends AbstractGenerator {

  @Override
  protected void generate(SourceWriter writer) throws UnableToCompleteException {
    JClassType arg =
        baseClass.asParameterizationOf(
            getType(JsonSerializer.class.getCanonicalName()).isGenericType()).getTypeArgs()[0];

    String parameterType = arg.getParameterizedQualifiedSourceName();
    writer.println("private " + TypeJsonSerializer.class.getCanonicalName() + "<" + parameterType
        + "> serializer = " + JsonSerializerUtil.generate(logger, context, arg)
        + ".getSingleton();");

    writer.println("public String serialize(" + parameterType + " t) {");
    writer.println("return serializer.serialize(t).toString();");
    writer.println("}");

    writer.println("public " + parameterType + " deserialize(String json) {");
    writer.println("return serializer.deserialize(" + JSONParser.class.getCanonicalName()
        + ".parse(json));");
    writer.println("}");
  }

  @Override
  protected void processComposer(ClassSourceFileComposerFactory composer) {
    composer.addImplementedInterface(typeName);
  }
}
