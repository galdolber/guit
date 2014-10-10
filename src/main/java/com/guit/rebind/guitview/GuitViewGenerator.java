/*
 * Copyright 2010 Gal Dolber.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package com.guit.rebind.guitview;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.dom.client.Element;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.rebind.ClassSourceFileComposerFactory;
import com.google.gwt.user.rebind.SourceWriter;

import com.guit.client.View;
import com.guit.client.ViewProperties;
import com.guit.client.ViewType;
import com.guit.client.apt.GwtPresenter;
import com.guit.client.binder.GwtEditor;
import com.guit.rebind.common.AbstractGenerator;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Set;

public class GuitViewGenerator extends AbstractGenerator {

  protected ViewType viewType;
  protected Class<? extends com.google.gwt.dom.client.Element> widgetElementClass;

  public GuitViewGenerator() {
    implementationPostfix = "_GuitViewImpl";
  }

  @Override
  protected void generate(SourceWriter writer) throws UnableToCompleteException {
    JClassType pojo = null;
    HashMap<String, String> editorPaths = new HashMap<String, String>();
    boolean isEditor = baseClass.isAnnotationPresent(GwtEditor.class);
    GwtEditor editor = null;
    if (isEditor) {
      editor = baseClass.getAnnotation(GwtEditor.class);
      pojo = getType(editor.pojo().getCanonicalName());
      String[] paths = editor.paths();
      for (String p : paths) {
        String[] parts = p.split("=");
        if (parts.length != 2) {
          error(
              "Error processing paths, it should have the following format: {fieldName}={path}. i.e: address=data.address . Found: %s, path: %s",
              baseClass.getQualifiedSourceName(), p);
        }
        editorPaths.put(parts[0], parts[1]);
      }
    }

    // User template
    String template = GuitViewHelper.getDeclaredTemplateName(baseClass, context, logger);

    Set<GuitViewField> uiFields =
        GuitViewHelper.findUiFields(baseClass, logger, typeOracle, pojo, editorPaths, template);

    writer.println("@UiTemplate(\"" + template + "\")");
    String bindedType = "Widget";
    if (viewType.equals(ViewType.WIDGET)) {
      bindedType = widgetElementClass.getCanonicalName();
    }
    writer.println("public interface Binder extends UiBinder<" + bindedType + ", " + implName
        + ">{}");
    writer.println("private Binder binder = GWT.create(Binder.class);");

    writer.println("public void bind() {");
    switch (viewType) {
      case COMPOSITE:
        writer.println("initWidget(binder.createAndBindUi(this));");
        break;
      case WIDGET:
        writer.println("setElement(binder.createAndBindUi(this));");
        break;
      case ROOTLESS:
        writer.println("_guit$widget = binder.createAndBindUi(this);");
        break;
    }
    writer.println("}");

    if (baseClass.isAnnotationPresent(GwtPresenter.class)) {
      GwtPresenter gwtPresenter = baseClass.getAnnotation(GwtPresenter.class);

      // Autofocus
      String autofocus = gwtPresenter.autofocus();
      int autofocusDelay = gwtPresenter.autofocusDelay();
      JClassType autofocusType = null;
      if (!autofocus.isEmpty()) {
        for (GuitViewField f : uiFields) {
          if (f.getName().equals(autofocus)) {
            autofocusType = getType(f.getType());
            break;
          }
        }

        String scheduledCommand = ScheduledCommand.class.getCanonicalName();
        writer.println("private " + scheduledCommand + " focusCmd = new " + scheduledCommand
            + "() {");
        writer.println("	@Override");
        writer.println("	public void execute() {");
        if (autofocusDelay > 0) {
          writer.println("    new " + Timer.class.getCanonicalName() + "() {");
          writer.println("      @Override");
          writer.println("      public void run() {");
        }
        if (autofocusType.isAssignableTo(getType(Focusable.class.getCanonicalName()))) {
          writer.println("      " + autofocus + ".setFocus(true);");
        } else if (autofocusType.isAssignableTo(getType(Widget.class.getCanonicalName()))) {
          writer.println("      " + autofocus + ".getElement().focus();");
        } else {
          writer.println("      " + autofocus + ".focus();");
        }
        if (autofocusDelay > 0) {
          writer.println("      }");
          writer.println("    }.schedule(" + autofocusDelay + ");");
        }
        writer.println("	}");
        writer.println("};");

        writer.println("public void autofocus() {");
        writer.println("    " + Scheduler.class.getCanonicalName()
            + ".get().scheduleDeferred(focusCmd);");
        writer.println("}");

        writer.println("@Override");
        writer.println("protected void onLoad() {");
        writer.println("    autofocus();");
        writer.println("}");
      }
    }

    writer.println("private Widget _guit$widget;");

    writer.println("public Widget asWidget() {");
    switch (viewType) {
      case COMPOSITE:
      case WIDGET:
        writer.println("return this;");
        break;
      case ROOTLESS:
        writer.println("return _guit$widget;");
        break;
    }
    writer.println("}");

    for (GuitViewField f : uiFields) {
      writer.println(f.getDeclaration());
    }
  }

  @Override
  protected void processComposer(ClassSourceFileComposerFactory composer) {
    composer.addImport(UiField.class.getCanonicalName());
    composer.addImport(UiTemplate.class.getCanonicalName());
    composer.addImport(UiBinder.class.getCanonicalName());
    composer.addImport(Widget.class.getCanonicalName());
    composer.addImport(GWT.class.getCanonicalName());

    composer.addImplementedInterface(View.class.getCanonicalName());

    if (baseClass.isAnnotationPresent(GwtEditor.class)) {
      GwtEditor editor = baseClass.getAnnotation(GwtEditor.class);
      composer.addImplementedInterface(Editor.class.getCanonicalName() + "<"
          + editor.pojo().getCanonicalName() + ">");
    }

    // View type
    ViewProperties properties = null;
    if (baseClass.isAnnotationPresent(ViewProperties.class)) {
      properties = baseClass.getAnnotation(ViewProperties.class);
      viewType = properties.type();

      if (viewType.equals(ViewType.WIDGET)) {
        widgetElementClass = Element.class;
      }
    } else {
      viewType = ViewType.COMPOSITE;
    }

    switch (viewType) {
      case COMPOSITE:
        composer.setSuperclass(Composite.class.getCanonicalName());
        break;
      case WIDGET:
        composer.setSuperclass(Widget.class.getCanonicalName());
        break;
      default:
        break;
    }
  }

  @Override
  protected ClassSourceFileComposerFactory createComposer() throws UnableToCompleteException {
    return new ClassSourceFileComposerFactory(generatedPackage
        + (GuitViewHelper.isOnViewPackage(baseClass, context, logger) ? ".view" : ""), implName);
  }

  @Override
  protected PrintWriter createPrintWriter() throws UnableToCompleteException {
    return context.tryCreate(logger, generatedPackage
        + (GuitViewHelper.isOnViewPackage(baseClass, context, logger) ? ".view" : ""), implName);
  }
}
