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

import com.google.gwt.core.ext.BadPropertyValueException;
import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JArrayType;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.JField;
import com.google.gwt.core.ext.typeinfo.JGenericType;
import com.google.gwt.core.ext.typeinfo.JParameterizedType;
import com.google.gwt.core.ext.typeinfo.JPrimitiveType;
import com.google.gwt.core.ext.typeinfo.JType;
import com.google.gwt.core.ext.typeinfo.NotFoundException;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONBoolean;
import com.google.gwt.json.client.JSONNull;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.rebind.ClassSourceFileComposerFactory;
import com.google.gwt.user.rebind.SourceWriter;

import com.guit.client.jsorm.BooleanSerializer;
import com.guit.client.jsorm.DateSerializer;
import com.guit.client.jsorm.DoubleSerializer;
import com.guit.client.jsorm.FloatSerializer;
import com.guit.client.jsorm.IntegerSerializer;
import com.guit.client.jsorm.LongSerializer;
import com.guit.client.jsorm.StringSerializer;
import com.guit.client.jsorm.TypeJsonSerializer;
import com.guit.client.jsorm.VoidSerializer;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Json serializer generator. Support virtually any kind of serializable data
 * but java.lang.Object. For Iterable Interfaces like java.util.Set,
 * java.util.List or java.util.Map you need to add an exception to specify what
 * implementation to use. java.util.List, java.util.Set, java.util.Map are
 * already supported. See Framework.gwt.xml.
 */
public class JsonSerializerUtil {

  private static final JType[] emptyParameter = new JType[]{};
  private static final String jsonObject = JSONObject.class.getCanonicalName();
  private static TreeLogger logger;
  private static HashMap<String, String> exceptions;

  private static void error(String message, Object... params) throws UnableToCompleteException {
    logger.log(TreeLogger.ERROR, String.format(message, params));
    throw new UnableToCompleteException();
  }

  public static String generate(TreeLogger logger, GeneratorContext context, JClassType pojoType)
      throws UnableToCompleteException {
    JsonSerializerUtil.logger = logger;

    // We cannot serialize java.lang.Object
    String pojoQualifiedName = pojoType.getQualifiedSourceName();
    if (pojoQualifiedName.equals(Object.class.getCanonicalName())) {
      error("You cannot serialize Object... we either");
    }

    if (exceptions == null) {
      exceptions = new HashMap<String, String>();
      try {
        List<String> ormExceptions =
            context.getPropertyOracle().getConfigurationProperty("json.orm.exception").getValues();
        for (String e : ormExceptions) {
          String[] parts = e.split(" ");
          if (parts.length != 2) {
            error(
                "Bad json orm exception format. i.e 'java.util.List java.util.ArrayList<%s>. Found: %s'",
                e);
          }
          exceptions.put(parts[0], parts[1]);
        }
      } catch (BadPropertyValueException e) {
        throw new IllegalStateException(e);
      }
    }

    String parameterizedQualifiedSourceName = pojoType.getParameterizedQualifiedSourceName();
    String typeName = parameterizedQualifiedSourceName;

    // Basic types
    if (typeName.equals(Void.class.getCanonicalName())) {
      return VoidSerializer.class.getCanonicalName();
    } else if (typeName.equals(String.class.getCanonicalName())) {
      return StringSerializer.class.getCanonicalName();
    } else if (typeName.equals(Integer.class.getCanonicalName())) {
      return IntegerSerializer.class.getCanonicalName();
    } else if (typeName.equals(Long.class.getCanonicalName())) {
      return LongSerializer.class.getCanonicalName();
    } else if (typeName.equals(Double.class.getCanonicalName())) {
      return DoubleSerializer.class.getCanonicalName();
    } else if (typeName.equals(Float.class.getCanonicalName())) {
      return FloatSerializer.class.getCanonicalName();
    } else if (typeName.equals(Date.class.getCanonicalName())) {
      return DateSerializer.class.getCanonicalName();
    } else if (typeName.equals(Boolean.class.getCanonicalName())) {
      return BooleanSerializer.class.getCanonicalName();
    }

    // Build name avoiding generics collitions
    StringBuilder implName = new StringBuilder();
    makeImplName(pojoType, implName);
    implName.append("_GuitJsonSerializer");

    String packageName = pojoType.getPackage().getName();
    String implNameString = implName.toString();

    if (getClass(packageName, implNameString)) {
      return packageName + "." + implNameString;
    }

    ClassSourceFileComposerFactory composer =
        new ClassSourceFileComposerFactory(packageName, implNameString);
    composer.addImplementedInterface(TypeJsonSerializer.class.getCanonicalName() + "<" + typeName
        + ">");
    PrintWriter printWriter = context.tryCreate(logger, packageName, implNameString);
    String createdName = composer.getCreatedClassName();
    if (printWriter != null) {
      SourceWriter writer = composer.createSourceWriter(context, printWriter);

      JType iterableParameterType = null;
      JPrimitiveType iterableParameterPrimitiveType = null;

      // Iterable
      JGenericType iterableType =
          context.getTypeOracle().findType(Iterable.class.getCanonicalName()).isGenericType();
      boolean isIterable = false;
      if (iterableType.isAssignableFrom(pojoType)) {
        isIterable = true;
        iterableParameterType = pojoType.asParameterizationOf(iterableType).getTypeArgs()[0];
        iterableParameterPrimitiveType = iterableParameterType.isPrimitive();

        // Find if theres any exception
        String qualifiedSourceName = pojoQualifiedName;
        if (exceptions.containsKey(qualifiedSourceName)) {
          parameterizedQualifiedSourceName =
              exceptions.get(qualifiedSourceName) + "<"
                  + iterableParameterType.getParameterizedQualifiedSourceName() + ">";
        }
      }

      // Map
      JGenericType mapType =
          context.getTypeOracle().findType(Map.class.getCanonicalName()).isGenericType();
      boolean isMap = false;
      JClassType mapKeyType = null;
      JClassType mapValueType = null;
      if (mapType.isAssignableFrom(pojoType)) {
        isMap = true;
        JParameterizedType pojoMap = pojoType.asParameterizationOf(mapType);
        JClassType[] args = pojoMap.getTypeArgs();
        mapKeyType = args[0];
        mapValueType = args[1];

        // Find if theres any exception
        String qualifiedSourceName = pojoQualifiedName;
        if (exceptions.containsKey(qualifiedSourceName)) {
          parameterizedQualifiedSourceName =
              exceptions.get(qualifiedSourceName) + "<"
                  + mapKeyType.getParameterizedQualifiedSourceName() + ","
                  + mapValueType.getParameterizedQualifiedSourceName() + ">";
        }
      }

      // Array
      boolean isArray = false;
      JArrayType pojoArray = pojoType.isArray();
      if (pojoArray != null) {
        isArray = true;
        iterableParameterType = pojoArray.getComponentType();
        iterableParameterPrimitiveType = iterableParameterType.isPrimitive();
      }

      // For pojos
      ArrayList<JField> fields = null;

      writer.println("public static " + createdName + " singleton;");

      writer.println("public static " + createdName + " getSingleton() {");
      writer.indent();
      writer.println("return singleton == null ? (singleton = new " + createdName
          + "()) : singleton;");
      writer.outdent();
      writer.println("}");

      writer.println("@Override");
      writer.println("public " + JSONValue.class.getCanonicalName() + " serialize(" + typeName
          + " data) {");
      writer.indent();

      if (isMap) {
        writer.println("if (data != null) {");
        writer.indent();
        writer.println(JSONArray.class.getCanonicalName() + " array = new "
            + JSONArray.class.getCanonicalName() + "();");
        writer.println("int n = 0;");
        writer.println("for (" + Entry.class.getCanonicalName() + "<"
            + mapKeyType.getParameterizedQualifiedSourceName() + ", "
            + mapValueType.getParameterizedQualifiedSourceName() + ">"
            + " entry : data.entrySet()) {");
        writer.indent();

        writer.print("array.set(n, ");
        JPrimitiveType mapKeyPrimitive = mapKeyType.isPrimitive();
        if (mapKeyPrimitive == null) {
          printValueSerialized(logger, context, writer, "entry.getKey()", mapKeyType, pojoType);
        } else {
          printPrimitiveSerialized(typeName, writer, "entry.getKey()", mapKeyPrimitive);
        }
        writer.println(");");
        writer.println("n++;");

        writer.print("array.set(n, ");
        JPrimitiveType mapValuePrimitive = mapValueType.isPrimitive();
        if (mapValuePrimitive == null) {
          printValueSerialized(logger, context, writer, "entry.getValue()", mapValueType, pojoType);
        } else {
          printPrimitiveSerialized(typeName, writer, "entry.getValue()", mapValuePrimitive);
        }
        writer.println(");");
        writer.println("n++;");

        writer.outdent();
        writer.println("}");
        writer.println("return array;");
        writer.outdent();
        writer.println("}");
        writer.println("return " + JSONNull.class.getCanonicalName() + ".getInstance();");
      } else if (isIterable || isArray) {
        writer.println("if (data != null) {");
        writer.indent();
        writer.println(JSONArray.class.getCanonicalName() + " array = new "
            + JSONArray.class.getCanonicalName() + "();");
        writer.println("int n = 0;");
        writer.println("for (" + iterableParameterType.getParameterizedQualifiedSourceName()
            + " item : data) {");
        writer.indent();

        writer.print("array.set(n, ");
        if (iterableParameterPrimitiveType == null) {
          printValueSerialized(logger, context, writer, "item", iterableParameterType, pojoType);
        } else {
          printPrimitiveSerialized(typeName, writer, "item", iterableParameterPrimitiveType);
        }
        writer.println(");");
        writer.println("n++;");

        writer.outdent();
        writer.println("}");
        writer.println("return array;");
        writer.outdent();
        writer.println("}");
        writer.println("return " + JSONNull.class.getCanonicalName() + ".getInstance();");
      } else if (pojoType.isEnum() != null) {
        writer.println("if (data != null) {");
        writer.indent();
        writer.println("return new " + JSONString.class.getCanonicalName() + "(data.name());");
        writer.outdent();
        writer.println("}");
        writer.println("return " + JSONNull.class.getCanonicalName() + ".getInstance();");
      } else {
        // Assert the type have an empty constructor
        try {
          pojoType.getConstructor(emptyParameter);
        } catch (NotFoundException e) {
          error("The data type of the place does not have an empty constructor. Found %s", typeName);
        }

        writer.println(jsonObject + " json = new " + jsonObject + "();");

        fields = new ArrayList<JField>();
        getFields(fields, pojoType);
        for (JField f : fields) {
          String fieldName = f.getName();
          String getterName = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
          JType fieldType = f.getType();
          JPrimitiveType primitive = fieldType.isPrimitive();
          String fieldTypeQualifiedType = fieldType.getQualifiedSourceName();

          if (primitive != null) {
            writer.print("json.put(\"" + fieldName + "\",");
            printPrimitiveSerialized(typeName, writer, "get" + getterName + "(data)", primitive);
            writer.println(");");
          } else {
            writer.println(fieldTypeQualifiedType + " " + fieldName + " = get" + getterName
                + "(data);");
            writer.println("if (" + fieldName + " != null) {");
            writer.indent();

            writer.print("json.put(\"" + fieldName + "\",");
            printValueSerialized(logger, context, writer, fieldName, fieldType, pojoType);
            writer.println(");");

            writer.outdent();
            writer.println("}");
          }
        }

        writer.println("return json;");
      }

      writer.outdent();
      writer.println("}");

      // Getters and setters
      printJsniGettersAndSetters(writer, pojoType);

      writer.println("@Override");
      writer.println("public " + typeName + " deserialize(" + JSONValue.class.getCanonicalName()
          + " jsonValue) {");
      writer.indent();

      if (isMap) {
        writer.println("if (jsonValue.isNull() == null) {");
        writer.indent();

        writer.println(JSONArray.class.getCanonicalName() + " jsonArray = jsonValue.isArray();");
        writer.println("int jsonArraySize = jsonArray.size();");

        writer.println(parameterizedQualifiedSourceName + " map = new "
            + parameterizedQualifiedSourceName + "();");

        writer.println("for (int n = 0; n < jsonArraySize; n+=2) {");
        writer.indent();
        writer.println(JSONValue.class.getCanonicalName() + " key = jsonArray.get(n);");
        writer.println(JSONValue.class.getCanonicalName() + " value = jsonArray.get(n + 1);");

        writer.print("map.put(");
        JPrimitiveType mapKeyPrimitive = mapKeyType.isPrimitive();
        if (mapKeyPrimitive == null) {
          printValueDeserialized(logger, context, writer, "key", mapKeyType);
        } else {
          printPrimitiveDeserialized(typeName, writer, "key", mapKeyPrimitive);
        }
        writer.print(",");
        JPrimitiveType mapValuePrimitive = mapValueType.isPrimitive();
        if (mapValuePrimitive == null) {
          printValueDeserialized(logger, context, writer, "value", mapValueType);
        } else {
          printPrimitiveDeserialized(typeName, writer, "value", mapValuePrimitive);
        }
        writer.println(");");

        writer.outdent();
        writer.println("}");
        writer.println("return map;");
        writer.outdent();
        writer.println("} else { return null; }");
      } else if (isIterable || isArray) {
        writer.println("if (jsonValue.isNull() == null) {");
        writer.indent();

        writer.println(JSONArray.class.getCanonicalName() + " jsonArray = jsonValue.isArray();");
        writer.println("int jsonArraySize = jsonArray.size();");

        if (isIterable) {
          writer.println(parameterizedQualifiedSourceName + " array = new "
              + parameterizedQualifiedSourceName + "();");
        } else {
          JArrayType array = iterableParameterType.isArray();
          if (array != null) {
            String arrayName = array.getQualifiedSourceName() + "[]";
            int index = arrayName.indexOf("[");
            String arrayDeclaration =
                arrayName.substring(0, index + 1) + "jsonArraySize"
                    + arrayName.substring(index + 1);
            writer.println(arrayName + " array = new " + arrayDeclaration + ";");
          } else {
            String parameterQualifiedName = iterableParameterType.getQualifiedSourceName();
            writer.println(parameterQualifiedName + "[] array = new " + parameterQualifiedName
                + "[jsonArraySize];");
          }
        }

        writer.println("for (int n = 0; n < jsonArraySize; n++) {");
        writer.indent();
        writer.println(JSONValue.class.getCanonicalName() + " item = jsonArray.get(n);");

        if (isIterable) {
          writer.print("array.add(");
        } else {
          writer.print("array[n] = ");
        }

        if (iterableParameterPrimitiveType == null) {
          printValueDeserialized(logger, context, writer, "item", iterableParameterType);
        } else {
          printPrimitiveDeserialized(typeName, writer, "item", iterableParameterPrimitiveType);
        }

        if (isIterable) {
          writer.println(");");
        } else {
          writer.println(";");
        }

        writer.outdent();
        writer.println("}");
        writer.println("return array;");
        writer.outdent();
        writer.println("} else { return null; }");
      } else if (pojoType.isEnum() != null) {
        writer.println("if (jsonValue.isNull() == null) {");
        writer.indent();
        writer.println("return " + typeName + ".valueOf(jsonValue.isString().stringValue());");
        writer.outdent();
        writer.println("} else { return null; }");

      } else {
        // Assert the type have an empty constructor
        try {
          pojoType.getConstructor(emptyParameter);
        } catch (NotFoundException e) {
          error("The data type of the place does not have an empty constructor. Found %s", typeName);
        }

        writer.println(JSONObject.class.getCanonicalName() + " json = jsonValue.isObject();");
        writer.println(typeName + " instance = new " + typeName + "();");

        for (JField f : fields) {
          String fieldName = f.getName();
          String setterName = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
          JType fieldType = f.getType();
          JPrimitiveType primitive = fieldType.isPrimitive();
          if (primitive != null) {
            writer.print("set" + setterName + "(instance,");
            printPrimitiveDeserialized(typeName, writer, "json.get(\"" + fieldName + "\")",
                primitive);
            writer.println(");");
          } else {
            writer.println("if (json.containsKey(\"" + fieldName + "\")) {");
            writer.indent();

            writer.print("set" + setterName + "(instance,");
            printValueDeserialized(logger, context, writer, "json.get(\"" + fieldName + "\")",
                fieldType);
            writer.println(");");

            writer.outdent();
            writer.println("}");
          }
        }

        writer.println("return instance;");
      }

      writer.outdent();
      writer.println("}");

      writer.commit(logger);
    }

    return createdName;
  }

  private static boolean getClass(String packageName, String implNameString) {
    try {
      Class.forName(packageName + "." + implNameString);
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  private static void printJsniGettersAndSetters(SourceWriter writer, JClassType pojoType) {

    for (JField f : pojoType.getFields()) {
      // Non static, final or transient
      if (f.isStatic() || f.isFinal() || f.isTransient()) {
        continue;
      }

      String fieldName = f.getName();
      String getterName = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
      JType fieldType = f.getType();

      // Print getters and setters
      String fieldTypeQualifiedType = fieldType.getQualifiedSourceName();
      String pojoQualifiedName = pojoType.getQualifiedSourceName();
      writer.println("private static native " + fieldTypeQualifiedType + " get" + getterName + "("
          + pojoQualifiedName + " instance) /*-{\n" + "    return instance.@" + pojoQualifiedName
          + "::" + fieldName + ";\n" + "  }-*/;\n" + "  \n" + "  private static native void  set"
          + getterName + "(" + pojoQualifiedName + " instance, " + fieldTypeQualifiedType
          + " value) /*-{\n" + "    instance.@" + pojoQualifiedName + "::" + fieldName
          + " = value;\n" + "  }-*/;");
    }

    JClassType superclass = pojoType.getSuperclass();
    if (superclass != null
        && !superclass.getQualifiedSourceName().equals(Object.class.getCanonicalName())) {
      printJsniGettersAndSetters(writer, superclass);
    }
  }

  private static void getFields(List<JField> fields, JClassType pojoType) {
    for (JField f : pojoType.getFields()) {
      // Non static, final or transient
      if (f.isStatic() || f.isFinal() || f.isTransient()) {
        continue;
      }

      fields.add(f);
    }

    JClassType superclass = pojoType.getSuperclass();
    if (superclass != null
        && !superclass.getQualifiedSourceName().equals(Object.class.getCanonicalName())) {
      getFields(fields, superclass);
    }
  }

  private static void makeImplName(JType pojoType, StringBuilder implName) {
    JArrayType array = pojoType.isArray();
    if (pojoType.isPrimitive() != null) {
      implName.append(pojoType.getSimpleSourceName());
    } else if (array != null) {
      implName.append("Array");
      makeImplName(array.getComponentType(), implName);
    } else {
      JParameterizedType parameterized = pojoType.isParameterized();
      implName.append(pojoType.getSimpleSourceName());
      if (parameterized != null) {
        JClassType[] args = parameterized.getTypeArgs();
        for (JClassType a : args) {
          makeImplName(a, implName);
        }
      }
    }
  }

  private static void printPrimitiveDeserialized(String typeName, SourceWriter writer,
      String fieldName, JPrimitiveType primitive) throws UnableToCompleteException {
    if (primitive.equals(JPrimitiveType.BOOLEAN)) {
      writer.println(fieldName + ".isBoolean().booleanValue()");
    } else if (primitive.equals(JPrimitiveType.DOUBLE)) {
      writer.println(fieldName + ".isNumber().doubleValue()");
    } else if (primitive.equals(JPrimitiveType.FLOAT)) {
      writer.println("(float)" + fieldName + ".isNumber().doubleValue()");
    } else if (primitive.equals(JPrimitiveType.LONG)) {
      writer.println("(long)" + fieldName + ".isNumber().doubleValue()");
    } else if (primitive.equals(JPrimitiveType.INT)) {
      writer.println("(int)" + fieldName + ".isNumber().doubleValue()");
    } else {
      error("The type %s is not a valid type for the place data. Found %s", primitive
          .getSimpleSourceName(), typeName);
    }
  }

  private static void printPrimitiveSerialized(String typeName, SourceWriter writer,
      String fieldName, JPrimitiveType primitive) throws UnableToCompleteException {
    if (primitive.equals(JPrimitiveType.BOOLEAN)) {
      writer.print(JSONBoolean.class.getCanonicalName() + ".getInstance(" + fieldName + ")");
    } else if (primitive.equals(JPrimitiveType.DOUBLE)) {
      writer.print("new " + JSONNumber.class.getCanonicalName() + "(" + fieldName + ")");
    } else if (primitive.equals(JPrimitiveType.FLOAT)) {
      writer.print("new " + JSONNumber.class.getCanonicalName() + "(" + fieldName + ")");
    } else if (primitive.equals(JPrimitiveType.LONG)) {
      writer.print("new " + JSONNumber.class.getCanonicalName() + "(" + fieldName + ")");
    } else if (primitive.equals(JPrimitiveType.INT)) {
      writer.print("new " + JSONNumber.class.getCanonicalName() + "(" + fieldName + ")");
    } else {
      error("The type %s is not a valid type for the place data. Found %s", primitive
          .getSimpleSourceName(), typeName);
    }
  }

  private static void printValueDeserialized(TreeLogger logger, GeneratorContext context,
      SourceWriter writer, String fieldName, JType fieldType) throws UnableToCompleteException {
    String fieldTypeName = fieldType.getQualifiedSourceName();
    if (fieldTypeName.equals(String.class.getCanonicalName())) {
      writer.print(fieldName + ".isString().stringValue()");
    } else if (fieldTypeName.equals(Integer.class.getCanonicalName())) {
      writer.print("(int)" + fieldName + ".isNumber().doubleValue()");
    } else if (fieldTypeName.equals(Long.class.getCanonicalName())) {
      writer.print("(long)" + fieldName + ".isNumber().doubleValue()");
    } else if (fieldTypeName.equals(Double.class.getCanonicalName())) {
      writer.print(fieldName + ".isNumber().doubleValue()");
    } else if (fieldTypeName.equals(Float.class.getCanonicalName())) {
      writer.print("(float)" + fieldName + ".isNumber().doubleValue()");
    } else if (fieldTypeName.equals(Date.class.getCanonicalName())) {
      writer.print("new " + Date.class.getCanonicalName() + "((long)" + fieldName
          + ".isNumber().doubleValue())");
    } else if (fieldTypeName.equals(Boolean.class.getCanonicalName())) {
      writer.print(fieldName + ".isBoolean().booleanValue()");
    } else {
      JClassType classOrInterface = fieldType.isClassOrInterface();
      writer.print(generate(logger, context, (classOrInterface != null ? classOrInterface
          : fieldType.isArray()))
          + ".getSingleton().deserialize(" + fieldName + ")");
    }
  }

  private static void printValueSerialized(TreeLogger logger, GeneratorContext context,
      SourceWriter writer, String fieldName, JType fieldType, JClassType pojoType)
      throws UnableToCompleteException {

    String fieldTypeName = fieldType.getQualifiedSourceName();
    if (fieldTypeName.equals(String.class.getCanonicalName())) {
      writer.print("new " + JSONString.class.getCanonicalName() + "(" + fieldName + ")");
    } else if (fieldTypeName.equals(Integer.class.getCanonicalName())) {
      writer.print("new " + JSONNumber.class.getCanonicalName() + "(" + fieldName + ")");
    } else if (fieldTypeName.equals(Long.class.getCanonicalName())) {
      writer.print("new " + JSONNumber.class.getCanonicalName() + "(" + fieldName + ")");
    } else if (fieldTypeName.equals(Double.class.getCanonicalName())) {
      writer.print("new " + JSONNumber.class.getCanonicalName() + "(" + fieldName + ")");
    } else if (fieldTypeName.equals(Float.class.getCanonicalName())) {
      writer.print("new " + JSONNumber.class.getCanonicalName() + "(" + fieldName + ")");
    } else if (fieldTypeName.equals(Date.class.getCanonicalName())) {
      writer.print("new " + JSONNumber.class.getCanonicalName() + "(" + fieldName + ".getTime())");
    } else if (fieldTypeName.equals(Boolean.class.getCanonicalName())) {
      writer.print(JSONBoolean.class.getCanonicalName() + ".getInstance(" + fieldName + ")");
    } else {
      // We cannot serialize java.lang.Object
      if (fieldType.getQualifiedSourceName().equals(Object.class.getCanonicalName())) {
        error("You cannot serialize Object... we either. Found: %s.%s", pojoType
            .getQualifiedSourceName(), fieldName);
      }

      JClassType classOrInterface = fieldType.isClassOrInterface();
      writer.print(generate(logger, context, (classOrInterface != null ? classOrInterface
          : fieldType.isArray()))
          + ".getSingleton().serialize(" + fieldName + ")");
    }
  }
}
