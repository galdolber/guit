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
package com.guit.rebind.binder;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.ext.CachedGeneratorResult;
import com.google.gwt.core.ext.RebindMode;
import com.google.gwt.core.ext.RebindResult;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.JField;
import com.google.gwt.core.ext.typeinfo.JMethod;
import com.google.gwt.core.ext.typeinfo.JPackage;
import com.google.gwt.core.ext.typeinfo.JParameter;
import com.google.gwt.core.ext.typeinfo.JParameterizedType;
import com.google.gwt.core.ext.typeinfo.JPrimitiveType;
import com.google.gwt.core.ext.typeinfo.JRealClassType;
import com.google.gwt.core.ext.typeinfo.JType;
import com.google.gwt.dev.javac.typemodel.JAbstractMethod;
import com.google.gwt.dev.javac.typemodel.JRawType;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.HasNativeEvent;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.logging.client.LogConfiguration;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.rebind.ClassSourceFileComposerFactory;
import com.google.gwt.user.rebind.SourceWriter;
import com.google.gwt.user.rebind.StringSourceWriter;
import com.google.web.bindery.requestfactory.gwt.client.RequestFactoryEditorDriver;
import com.google.web.bindery.requestfactory.shared.RequestFactory;

import com.guit.client.GuitEntryPoint;
import com.guit.client.Implementation;
import com.guit.client.ImplicitCast;
import com.guit.client.Presenter;
import com.guit.client.View;
import com.guit.client.ViewAccesor;
import com.guit.client.apt.GwtPresenter;
import com.guit.client.binder.Attribute;
import com.guit.client.binder.ContributorAnnotation;
import com.guit.client.binder.EventBusHandler;
import com.guit.client.binder.GwtEditor;
import com.guit.client.binder.Plugin;
import com.guit.client.binder.ViewField;
import com.guit.client.binder.ViewHandler;
import com.guit.client.binder.ViewInitializer;
import com.guit.client.binder.ViewPool;
import com.guit.client.dom.Event;
import com.guit.client.dom.impl.ElementImpl;
import com.guit.client.dom.impl.EventImpl;
import com.guit.rebind.common.AbstractGeneratorExt;
import com.guit.rebind.gin.GinOracle;
import com.guit.rebind.guitview.GuitViewField;
import com.guit.rebind.guitview.GuitViewGenerator;
import com.guit.rebind.guitview.GuitViewHelper;

import java.io.PrintWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

import elemental.events.EventListener;

public class GuitBinderGenerator extends AbstractGeneratorExt {

  private static final String GUIT_INFO = "guit-info";
  private static Method getAnnotationsMethod;
  private final GuitViewGenerator guitViewGenerator = new GuitViewGenerator();
  private static final String STRINGCANONICALNAME = String.class.getCanonicalName();

  protected JClassType hasNativeEventType;

  protected JPackage domEventsPackage;
  protected JPackage sharedEventsPackage;
  protected JClassType gwtEventType;
  protected JClassType gwtHandlerType;

  static {
    try {
      getAnnotationsMethod = JAbstractMethod.class.getDeclaredMethod("getAnnotations");
      getAnnotationsMethod.setAccessible(true);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  protected static final String handlerRegistrationName = HandlerRegistration.class
      .getCanonicalName();

  protected static final String bindingsDeclaration = ArrayList.class.getCanonicalName() + "<"
      + handlerRegistrationName + "> bindings = new " + ArrayList.class.getCanonicalName() + "<"
      + handlerRegistrationName + ">();";

  protected static final String eventBusbindingsDeclaration = ArrayList.class.getCanonicalName()
      + "<" + handlerRegistrationName + "> eventBusBindings = new "
      + ArrayList.class.getCanonicalName() + "<" + handlerRegistrationName + ">();";

  protected String capitalize(String part) {
    return part.substring(0, 1).toUpperCase() + part.substring(1);
  }

  private boolean checkIsPresenter(JClassType clazz) throws UnableToCompleteException {
    if (clazz.isAssignableTo(getType(Presenter.class.getCanonicalName()))) {
      return true;
    }
    return false;
  }

  protected String eventClassNameToEventName(String simpleName) {
    String eventName;
    eventName = simpleName.substring(0, simpleName.length() - 5);
    eventName = eventName.substring(0, 1).toLowerCase() + eventName.substring(1);
    return eventName;
  }

  protected String eventNameToEventClassName(String eventName) {
    eventName = eventName.substring(0, 1).toUpperCase() + eventName.substring(1) + "Event";
    return eventName;
  }

  private void findAllMethods(JClassType type, ArrayList<JMethod> methods) {
    JMethod[] typeMethods = type.getMethods();
    methods.addAll(Arrays.asList(typeMethods));
    JClassType[] interfaces = type.getImplementedInterfaces();
    for (JClassType i : interfaces) {
      findAllMethods(i, methods);
    }
    JClassType superclass = type.getSuperclass();
    if (superclass != null) {
      findAllMethods(superclass, methods);
    }
  }

  @SuppressWarnings("unchecked")
  @Override
  protected RebindResult generate() throws UnableToCompleteException {
    if (checkAlreadyGenerated(createdClassName)) {
      return new RebindResult(RebindMode.USE_ALL_CACHED, createdClassName);
    }

    // Do we need to check this?
    JClassType presenterType =
        baseClass.getImplementedInterfaces()[0].isParameterized().getTypeArgs()[0];

    // Presenter or controller
    boolean isPresenter = checkIsPresenter(presenterType);

    CachedGeneratorResult cachedGeneratorResult = context.getCachedGeneratorResult();
    if (cachedGeneratorResult != null) {
      Object clientData = cachedGeneratorResult.getClientData(GUIT_INFO);
      if (clientData != null) {
        HashMap<String, Object> lastBindingData = (HashMap<String, Object>) clientData;
        if (lastBindingData.equals(makeUnitData(presenterType, isPresenter))) {
          return new RebindResult(RebindMode.USE_ALL_CACHED, createdClassName);
        }
      }
    }

    hasNativeEventType =
        hasNativeEventType == null ? getType(HasNativeEvent.class.getCanonicalName())
            : hasNativeEventType;

    // If it is a parameterized type we need to find the base type to get
    // the right method names
    while (presenterType.isParameterized() != null || presenterType.isRawType() != null) {
      if (presenterType.isParameterized() != null) {
        presenterType = presenterType.isParameterized().getBaseType();
      } else {
        presenterType = presenterType.isRawType().getBaseType();
      }
    }

    checkForRepetition(presenterType);

    ClassSourceFileComposerFactory composer = createComposer();
    processComposer(composer);
    composer.getCreatedClassName();
    PrintWriter printWriter = createPrintWriter();
    if (printWriter == null) {
      return new RebindResult(RebindMode.USE_ALL_CACHED, createdClassName);
    }

    SourceWriter writer = composer.createSourceWriter(context, printWriter);
    writer.println(bindingsDeclaration);
    writer.println(eventBusbindingsDeclaration);

    String viewTypeName = null;
    HashMap<String, JType> validBindingFieldsTypes = null;
    if (isPresenter) {
      viewTypeName =
          guitViewGenerator.generate(logger, context, presenterType.getQualifiedSourceName());
      writer.println(viewTypeName + " view;");

      // Field -> Type (for validation purpose)
      validBindingFieldsTypes = getValidGuitViewBindingFields(presenterType);

      // View pool
      writer.println("private static " + ViewPool.class.getCanonicalName() + " pool = new "
          + ViewPool.class.getCanonicalName() + "();");
    }

    // If is an editor generate the driver
    boolean isGwtEditor = presenterType.isAnnotationPresent(GwtEditor.class);
    GwtEditor editor = null;
    Class<?> editorDriver = null;
    if (isGwtEditor) {
      editor = presenterType.getAnnotation(GwtEditor.class);
      editorDriver = editor.base();
      writer.println("public static interface Driver extends " + editorDriver.getCanonicalName()
          + "<" + editor.pojo().getCanonicalName() + "," + viewTypeName + "> {}");
    }

    writer.println(presenterType.getQualifiedSourceName() + " presenter;");

    writer.println(EventBus.class.getCanonicalName() + " eventBus;");

    writer.println("boolean isBinded = false;");

    /*
     * Event bus binder.
     */
    writer.println("public void bind(final " + presenterType.getQualifiedSourceName()
        + " presenter, final " + EventBus.class.getCanonicalName() + " eventBus) {");
    writer.indent();

    writer.println("this.presenter = presenter;");
    writer.println("this.eventBus = eventBus != null ? eventBus : "
        + GuitEntryPoint.class.getCanonicalName() + ".getEventBus();");
    printEventBusBindingMethods(writer, presenterType);

    writer.outdent();
    writer.println("}");

    /*
     * View binder.
     */
    writer.println("public " + View.class.getCanonicalName() + " getView() {");
    writer.indent();

    if (isPresenter) {
      // If already binded return the view
      writer.println("if (isBinded) {");
      writer.println("    return view;");
      writer.println("}");

      // Find a view from the pool or create a new one
      writer.println("if (pool.isEmpty()) {");
      writer.println("view = (" + viewTypeName + ") new " + viewTypeName + "();");

      ArrayList<JField> fields = new ArrayList<JField>();
      collectAllFields(presenterType, fields);

      // Provided fields
      printProvidedFields(presenterType, validBindingFieldsTypes, writer, false, fields);

      writer.println("view.bind();");
      writer.println("} else {");
      writer.println("view = (" + viewTypeName + ") pool.pop();");

      // Provided fields
      printProvidedFields(presenterType, validBindingFieldsTypes, writer, true, fields);

      writer.println("}");

      printViewFieldBindings(writer, presenterType, viewTypeName, validBindingFieldsTypes, fields);
      printViewBindingMethods(writer, presenterType, viewTypeName, validBindingFieldsTypes);

      // Initializers
      printPresentersInitilizersCalls(writer, presenterType);

      writer.println("isBinded = true;");

      // GwtEditor
      if (isGwtEditor) {
        writer.println("Driver driver = GWT.create(Driver.class);");

        if (editorDriver.equals(RequestFactoryEditorDriver.class)) {
          JField factoryField = presenterType.getField("factory");
          if (factoryField == null
              || !factoryField.getType().isClassOrInterface().isAssignableTo(
                  getType(RequestFactory.class.getCanonicalName())) || factoryField.isPrivate()) {
            error("The presenter does not have a non-private factory field of the type RequestFactory. Found: "
                + presenterType.getQualifiedSourceName() + ".factory");
          }
          writer.println("driver.initialize(eventBus, presenter.factory, view);");
        } else {
          writer.println("driver.initialize(view);");
        }

        writer.println("presenter.driver = driver;");
      }

      // Return the view or null
      writer.println("return view;");
    } else {
      writer.println("return null;");
    }

    writer.outdent();
    writer.println("}");

    /*
     * Unbind
     */
    writer.println("public void releaseView() {");
    writer.indent();

    if (isPresenter) {
      writer.println("for (" + handlerRegistrationName + " b : bindings) {b.removeHandler();}");
      writer.println("bindings.clear();");
      if (isPresenter) {
        // Recycle the view
        writer.println("if (view != null) {");
        writer.indent();
        writer.println("pool.push(view);");
        writer.println("view = null;");
        writer.outdent();
        writer.println("}");
      }
      writer.println("isBinded = false;");
    }
    writer.outdent();
    writer.println("}");

    /*
     * Destroy.
     */
    writer.println("public void destroy() {");
    writer.indent();
    writer.println("for (" + handlerRegistrationName
        + " b : eventBusBindings) {b.removeHandler();}");
    if (isPresenter) {
      writer.println("releaseView();");
    }
    writer.println("bindings = null;");
    writer.println("eventBusBindings = null;");
    writer.println("presenter = null;");
    writer.println("eventBus = null;");
    writer.outdent();
    writer.println("}");
    writer.commit(logger);

    if (context.isGeneratorResultCachingEnabled()) {
      RebindResult result = new RebindResult(RebindMode.USE_ALL_NEW, createdClassName);
      result.putClientData(GUIT_INFO, makeUnitData(presenterType, isPresenter));
      return result;
    } else {
      return new RebindResult(RebindMode.USE_ALL_NEW_WITH_NO_CACHING, createdClassName);
    }
  }

  private HashMap<String, Object> makeUnitData(JClassType presenterType, boolean isPresenter)
      throws UnableToCompleteException {
    HashMap<String, Object> data = new HashMap<String, Object>();
    data.put("presenter", getModificationTime(presenterType));
    if (isPresenter) {
      data.put("view.ui.xml", GuitViewHelper.lastMofified(presenterType, "view/"
          + GuitViewHelper.getDeclaredTemplateName(presenterType, context, logger)));
    }
    return data;
  }

  private Object getModificationTime(JClassType type) {
    if (type instanceof JRealClassType) {
      JRealClassType sourceRealType = (JRealClassType) type;
      return sourceRealType.getLastModifiedTime();
    } else if (type instanceof JRawType) {
      JRawType r = (JRawType) type;
      return r.getBaseType().getLastModifiedTime();
    } else {
      throw new RuntimeException(type.getQualifiedSourceName() + " "
          + type.getClass().getCanonicalName());
    }
  }

  private void printProvidedFields(JClassType baseClass,
      HashMap<String, JType> validBindingFieldsTypes, SourceWriter writer, boolean fromPool,
      ArrayList<JField> fields) throws UnableToCompleteException {
    for (JField f : fields) {
      if (f.isAnnotationPresent(ViewField.class)) {
        String name = f.getAnnotation(ViewField.class).name();
        if (name.isEmpty()) {
          name = f.getName();
        }

        if (f.getAnnotation(ViewField.class).provided()) {
          if (!fromPool) {
            // Sanity assert
            writer.println("assert (" + validBindingFieldsTypes.get(name).getQualifiedSourceName()
                + ")presenter." + f.getName()
                + " != null: \"You forgot to provide the field, make sure it is not null. Found: "
                + baseClass + "." + name + "\";");

            writer.println("view." + name + " = ("
                + validBindingFieldsTypes.get(name).getQualifiedSourceName() + ")presenter."
                + f.getName() + ";");
          } else {
            writer.println("presenter." + f.getName() + " = ("
                + validBindingFieldsTypes.get(name).getQualifiedSourceName() + ")view." + name
                + ";");
          }
        }
      }
    }
  }

  // TODO cache
  public static void collectAllFields(JClassType clazz, ArrayList<JField> list) {
    for (JField jField : clazz.getFields()) {
      list.add(jField);
    }
    JClassType superclass = clazz.getSuperclass();
    if (superclass != null) {
      collectAllFields(superclass, list);
    }
  }

  /**
   * Find event in dom, shared or context locations. Context = '{currentPackage}/event'. Context
   * events have priority over shared and dom ones.
   */
  protected JClassType getEventByName(String eventName, JPackage contextEventsPackage) {
    // Get event class name
    eventName = eventNameToEventClassName(eventName);

    // Find in context
    if (contextEventsPackage != null) {
      JClassType contextEventType = contextEventsPackage.findType(eventName);
      if (contextEventType != null) {
        return contextEventType;
      }
    }

    // Find in dom events
    JClassType domEventType = domEventsPackage.findType(eventName);
    if (domEventType != null) {
      return domEventType;
    }

    // Find in shared events
    JClassType sharedEventType = sharedEventsPackage.findType(eventName);
    if (sharedEventType != null) {
      return sharedEventType;
    }

    return null;
  }

  /**
   * Retrieves the handler associated with the event.
   * 
   * @throws UnableToCompleteException
   */
  protected JClassType getHandlerForEvent(JClassType eventType) throws UnableToCompleteException {
    return eventType.findMethod("getAssociatedType", new JType[0]).getReturnType()
        .isParameterized().getTypeArgs()[0];
  }

  private HashMap<String, JType> getValidGuitViewBindingFields(JClassType presenterType)
      throws UnableToCompleteException {
    Set<GuitViewField> findUiFields =
        GuitViewHelper.findUiFields(presenterType, logger, typeOracle, null, null, "view/"
            + GuitViewHelper.getDeclaredTemplateName(presenterType, context, logger));

    HashMap<String, JType> fields = new HashMap<String, JType>();
    for (GuitViewField f : findUiFields) {
      JClassType type = getType(f.getType());
      if (type == null) {
        error("The type '%s' of the field '%s' doesn't exists. Found: %s.ui.xml", type,
            f.getName(), presenterType.getQualifiedSourceName());
      }
      fields.put(f.getName(), type);
    }
    return fields;
  }

  /**
   * Finds the valid methods and check all the conventions.
   */
  protected void printEventBusBindingMethods(SourceWriter writer, JClassType presenterType)
      throws UnableToCompleteException {
    JPackage contextEventsPackage = getPackage(presenterType.getPackage().getName() + ".event");
    for (JMethod m : presenterType.getMethods()) {
      if (m.isAnnotationPresent(EventBusHandler.class)) {

        EventBusHandler eventBusHandler = m.getAnnotation(EventBusHandler.class);

        String name = m.getName();
        String presenterName = presenterType.getQualifiedSourceName();

        validateHandler(m, name, presenterName);

        // Find the event type
        JParameter[] parameters = m.getParameters();

        if (!name.startsWith("$") && !name.startsWith("eventBus$")) {
          error(
              "Bad method name: %s on class: %s, the method should start with '$' or 'eventBus$'",
              name, presenterName);
        }

        // Clean the name
        if (name.startsWith("$")) {
          name = name.substring(1); // Cut off the $
        } else {
          name = name.substring(9); // Cut off the eventBus$
        }

        JClassType eventType = getType(eventBusHandler.value().getCanonicalName());
        if (eventType.equals(gwtEventType)) {
          eventType = getEventByName(name, contextEventsPackage);
          if (eventType == null) {
            error(
                "There is no context, dom or shared event with the name '%s'. Binding method: '%s' in class: '%s'",
                name, m.getName(), presenterType.getQualifiedSourceName());
          }
        }

        StringBuilder bindingParameters = new StringBuilder();
        ArrayList<String> parameterStrings = new ArrayList<String>();
        for (JParameter p : parameters) {
          String parameter = p.getName();

          if (bindingParameters.length() > 0) {
            bindingParameters.append(", ");
          }

          int initlenght = bindingParameters.length();

          // Implicit cast
          bindingParameters.append("("
              + p.getType().getErasedType().getParameterizedQualifiedSourceName() + ")");

          String getter = "get";

          // Check if it is a boolean then the getter is 'is' not
          // 'get'
          JPrimitiveType parameterTypeIsPrimitive = p.getType().isPrimitive();
          if (parameterTypeIsPrimitive != null
              && parameterTypeIsPrimitive.equals(JPrimitiveType.BOOLEAN)) {
            getter = "is";
          }

          // Event getter binding
          if (parameter.indexOf("$") == -1) {
            // Event binding
            bindingParameters.append("event.");
            bindingParameters.append(getter);
            bindingParameters.append(capitalize(parameter));
            bindingParameters.append("()");
          } else {
            // Event binding nested binding
            String[] parameterParts = parameter.split("[$]");

            bindingParameters.append("event");
            for (int n = 0; n < parameterParts.length - 1; n++) {
              bindingParameters.append(".get");
              bindingParameters.append(capitalize(parameterParts[n]));
              bindingParameters.append("()");
            }

            bindingParameters.append(".");
            bindingParameters.append(getter);
            bindingParameters.append(capitalize(parameterParts[parameterParts.length - 1]));
            bindingParameters.append("()");
          }

          parameterStrings.add(bindingParameters.substring(initlenght, bindingParameters.length()));
        }

        // Find the event name
        String simpleName = eventType.getSimpleSourceName();
        if (!simpleName.endsWith("Event")) {
          error("The event %s does not use the event convention. It should end with 'Event'",
              eventType.getQualifiedSourceName());
        }
        String eventName = eventClassNameToEventName(simpleName);

        // Check that the name of the event correspond to the method
        // name convention
        if (!eventName.equals(name)) {
          error(
              "The method %s on class %s does not use the event bus handler method convention. "
                  + "It should start with '$' or 'eventBus$' "
                  + "and end with the event name. i.e ValueChangeEvent -> $valueChange. Solve it renaming it to '$%s'",
              m.getName(), presenterName, eventName);
        }

        // Get event handler name
        JClassType handlerType = getHandlerForEvent(eventType);
        if (handlerType == null) {
          error("Parameter '%s' is not an event (subclass of GwtEvent).", eventType.getName());
        }

        // Retrieves the single method (usually 'onSomething') related
        // to all
        // handlers. Ex: onClick in ClickHandler, onBlur in BlurHandler
        // ...
        JMethod[] methods = handlerType.getMethods();
        if (methods.length != 1) {
          error("'%s' has more than one method defined.", handlerType.getName());
        }

        // 'onSomething' method
        JMethod handlerOnMethod = methods[0];

        // Checks if the method has an Event as parameter. Ex:
        // ClickEvent in onClick, BlurEvent in onBlur ...
        parameters = handlerOnMethod.getParameters();
        if (parameters.length != 1) {
          error("Method '%s' needs '%s' as parameter", handlerOnMethod.getName(), eventType
              .getName());
        }

        writer.println("eventBusBindings.add(eventBus.addHandler(");

        writer.println(eventType.getQualifiedSourceName() + ".");

        // getType or TYPE ?
        JField typeField = eventType.getField("TYPE");
        if (typeField != null && typeField.isStatic() && typeField.isPublic()) {
          writer.println("TYPE");
        } else {
          writer.println("getType()");
        }
        writer.println(", ");

        writer.println("new " + handlerType.getQualifiedSourceName() + "() {");
        writer.indent();
        writer.println("public void " + handlerOnMethod.getName() + "(final "
            + eventType.getQualifiedSourceName() + " event) {");
        writer.indent();

        // Process contributors
        String bindingParametersString = bindingParameters.toString();
        BinderContextImpl binderContext =
            processMethodContributors(presenterType, null, null, null, m, eventType,
                parameterStrings.toArray(new String[parameterStrings.size()]));

        StringSourceWriter handlerWriter = new StringSourceWriter();

        handlerWriter.println("if (" + LogConfiguration.class.getCanonicalName()
            + ".loggingIsEnabled()) {");
        handlerWriter.println(Logger.class.getCanonicalName() + ".getLogger(\"Binder\").info(\""
            + binderContext.getLog() + "\");");
        handlerWriter.println("}");

        handlerWriter.println("presenter." + m.getName() + "(" + bindingParametersString + ");");

        writer.println(binderContext.build(handlerWriter));

        writer.outdent();
        writer.println("}");
        writer.outdent();
        writer.println("}));");
      }
    }
  }

  /**
   * Print calls to @PresenterInitializer methods.
   */
  protected void printPresentersInitilizersCalls(SourceWriter writer, JClassType presenterType)
      throws UnableToCompleteException {
    for (JMethod m : presenterType.getMethods()) {
      if (m.isAnnotationPresent(ViewInitializer.class)) {
        if (m.getParameters().length > 0) {
          error("All @PresenterInitializer must have zero parameters. Found: %s.%s", presenterType
              .getQualifiedSourceName(), m.getName());
        }

        writer.println("presenter." + m.getName() + "();");
      }
    }

    JClassType superclass = presenterType.getSuperclass();
    if (superclass != null
        && !superclass.getQualifiedSourceName().equals(Object.class.getCanonicalName())) {
      printPresentersInitilizersCalls(writer, superclass);
    }
  }

  private void printViewBindingMethods(SourceWriter writer, JClassType presenterType,
      String viewTypeName, HashMap<String, JType> validBindingFieldsTypes)
      throws UnableToCompleteException {

    Set<String> validBindingFields = validBindingFieldsTypes.keySet();

    JPackage contextEventsPackage = getPackage(presenterType.getPackage().getName() + ".event");

    ArrayList<JMethod> methods = new ArrayList<JMethod>();
    findAllMethods(presenterType, methods);

    for (JMethod m : methods) {
      String name = m.getName();
      if (m.isAnnotationPresent(ViewHandler.class)) {
        validateHandler(m, name, presenterType.getQualifiedSourceName());

        String eventName;
        ViewHandler viewHandlerAnnotation = m.getAnnotation(ViewHandler.class);
        JClassType eventType = getType(viewHandlerAnnotation.event().getCanonicalName());

        boolean fieldsAreElements = false;
        Set<String> bindingFields = null;
        boolean addHandlerToView = false;
        if (viewHandlerAnnotation.fields().length == 0) {
          if (name.startsWith("$")) {
            // Direct view binding
            eventName = name.substring(1);

            addHandlerToView = true;
          } else {
            // View fields binding
            String[] nameParts = name.split("[$]");

            // Check the name format
            if (nameParts.length < 2) {
              error(
                  "The method %s on the class %s have a bad binding format. It should be: "
                      + "'{viewField}${eventName}' or for binding multiple fields: '{viewField1}${viewField2}${eventName}'",
                  name, presenterType.getQualifiedSourceName());
            }

            // Check that the binding fields are valid
            bindingFields = new HashSet<String>();
            for (int n = 0; n < nameParts.length - 1; n++) {
              if (!validBindingFields.contains(nameParts[n])) {
                error(
                    "The field %s on the class %s is not a valid binding field. It must be public or protected and not static",
                    nameParts[n], presenterType);
              }
              bindingFields.add(nameParts[n]);
            }

            eventName = nameParts[nameParts.length - 1]; // last
            // token
          }

          // Check the event type and name convention
          if (eventType.equals(gwtEventType)) {
            eventType = getEventByName(eventName, contextEventsPackage);
            if (eventType == null) {
              error(
                  "There is no context, dom or shared event with the name '%s'. Binding method: '%s' in class: '%s'",
                  eventName, name, presenterType.getQualifiedSourceName());
            }
          } else {
            // Check that the method name correspond to the event
            // type
            String eventNameToEventClassName = eventNameToEventClassName(eventName);
            if (!eventNameToEventClassName.equals(eventType.getSimpleSourceName())) {
              error(
                  "The method '%s' in the class '%s' have a typo in the name. The last token should be : ..$%s() {.. ",
                  name, presenterType.getQualifiedSourceName(), eventName);
            }
          }
        } else {
          String[] fields = viewHandlerAnnotation.fields();
          bindingFields = new HashSet<String>();
          for (String f : fields) {
            if (f.isEmpty()) {
              addHandlerToView = true;
            } else {
              if (!validBindingFields.contains(f)) {
                error(
                    "The field %s on the class %s is not a valid binding field. It must be public or protected and not static",
                    f, presenterType);
              }
              bindingFields.add(f);
            }
          }

          if (eventType.equals(gwtEventType)) {
            error(
                "When using ViewFields you must specify the event class in the Handler annotation. Found: %s.%s",
                presenterType.getQualifiedSourceName(), name);
          }

          eventName = eventClassNameToEventName(eventType.getSimpleSourceName());
        }

        // If any field is an element all of them should be otherwise none
        // of them
        int widgetCount = 0;
        JClassType widgetJType = getType(Widget.class.getCanonicalName());
        JClassType isWidgetJType = getType(IsWidget.class.getCanonicalName());
        for (String f : bindingFields) {
          JClassType classOrInterface = validBindingFieldsTypes.get(f).isClassOrInterface();
          if (classOrInterface.isAssignableTo(widgetJType)
              || classOrInterface.isAssignableTo(isWidgetJType)) {
            widgetCount++;
          }
        }

        if (widgetCount != bindingFields.size() && widgetCount != 0) {
          error(
              "Not all fields on the class %s.%s are either all elements or all widgets. You cannot bind elements and widgets on the same handler",
              presenterType, name);
        }
        fieldsAreElements = widgetCount == 0;

        /**
         * Find parameters bindings. The binding can be with the event(cannot have anidation of
         * getters):'getter'->'getGetter()' or with the view:'$getter'->'view.getGetter()' or with a
         * view field '{viewField$getter}'->'view.viewField.getGetter();', this last two ones will
         * support anidation: '{viewField$getter$another}'->'view.viewField.getGetter().getAnother (
         * ) ; '
         **/
        StringBuilder bindingParameters = new StringBuilder();
        JParameter[] parameters = m.getParameters();
        ArrayList<String> parameterStrings = new ArrayList<String>();
        for (JParameter p : parameters) {
          String parameter = p.getName();
          JType parameterType = p.getType();
          if (bindingParameters.length() > 0) {
            bindingParameters.append(", ");
          }

          int initlenght = bindingParameters.length();

          // Implicit cast
          bindingParameters.append("("
              + parameterType.getErasedType().getParameterizedQualifiedSourceName() + ")");

          String getter = "get";

          // Check if it is a boolean then the getter is 'is' not
          // 'get'
          JPrimitiveType parameterTypeIsPrimitive = parameterType.isPrimitive();
          if (parameterTypeIsPrimitive != null
              && parameterTypeIsPrimitive.equals(JPrimitiveType.BOOLEAN)) {
            getter = "is";
          }

          if (p.getName().equals("event")) {
            bindingParameters.append("event");
          } else if (p.isAnnotationPresent(Attribute.class)) {
            // Only valid for domEvents
            if (!eventType.isAssignableTo(hasNativeEventType)) {
              error(
                  "Attributes binding are only valid for DomEvents. Found: %s.%s in parameter: %s",
                  presenterType.getQualifiedSourceName(), name, parameter);
            }

            String parameterTypeQualifiedSourceName = parameterType.getQualifiedSourceName();
            boolean isString = parameterTypeQualifiedSourceName.equals(STRINGCANONICALNAME);
            if (!isString) {
              bindingParameters.append(parameterTypeQualifiedSourceName + ".valueOf(");
            }
            bindingParameters.append("((" + Element.class.getCanonicalName()
                + ")event.getNativeEvent().getEventTarget().cast()).getAttribute(\"" + parameter
                + "\")");
            if (!isString) {
              bindingParameters.append(")");
            }
          } else if (parameter.indexOf("$") == -1) {
            // Event binding
            bindingParameters.append("event.");
            bindingParameters.append(getter);
            bindingParameters.append(capitalize(parameter));
            bindingParameters.append("()");
          } else {
            // Event binding nested binding
            String[] parameterParts = parameter.split("[$]");

            bindingParameters.append("event");
            for (int n = 0; n < parameterParts.length - 1; n++) {
              bindingParameters.append(".get");
              bindingParameters.append(capitalize(parameterParts[n]));
              bindingParameters.append("()");
            }

            bindingParameters.append(".");
            bindingParameters.append(getter);
            bindingParameters.append(capitalize(parameterParts[parameterParts.length - 1]));
            bindingParameters.append("()");
          }

          parameterStrings.add(bindingParameters.substring(initlenght, bindingParameters.length()));
        }

        // Get event handler name
        JClassType handlerType = getHandlerForEvent(eventType);
        if (handlerType == null) {
          error("Parameter '%s' is not an event (subclass of GwtEvent).", eventType.getName());
        }

        // Retrieves the single method (usually 'onSomething') related
        // to all
        // handlers. Ex: onClick in ClickHandler, onBlur in BlurHandler
        // ...
        JMethod[] handlerMethods = handlerType.getMethods();
        if (handlerMethods.length != 1) {
          error("'%s' has more than one method defined.", handlerType.getName());
        }

        // 'onSomething' method
        JMethod handlerOnMethod = handlerMethods[0];

        String methodName = name;
        String handlerTypeName = handlerType.getQualifiedSourceName();

        GwtPresenter presenterAnnotation = presenterType.getAnnotation(GwtPresenter.class);
        boolean isElemental = presenterAnnotation != null && presenterAnnotation.elemental();

        // Write handler
        SourceWriter eventHandlerWriter = new StringSourceWriter();
        if (!fieldsAreElements) {
          eventHandlerWriter.println("new " + handlerTypeName + "() {");
          eventHandlerWriter.indent();
          eventHandlerWriter.println("public void " + handlerOnMethod.getName() + "(final "
              + eventType.getQualifiedSourceName() + " event) {");
          eventHandlerWriter.indent();
        } else if (isElemental) {
          eventHandlerWriter.println("new elemental.events.EventListener() {");
          eventHandlerWriter.println("  @Override");
          eventHandlerWriter.println("  public void handleEvent(elemental.events.Event event) {");
        } else {
          eventHandlerWriter.println("new "
              + com.guit.client.dom.EventHandler.class.getCanonicalName() + "() {");
          eventHandlerWriter.indent();
          eventHandlerWriter.println("public void onEvent(" + Event.class.getCanonicalName()
              + " event_) {");
          eventHandlerWriter.println("  " + EventImpl.class.getCanonicalName() + " event = ("
              + EventImpl.class.getCanonicalName() + ") event_;");
          eventHandlerWriter.indent();
        }

        String bindingParametersString = bindingParameters.toString();

        // Process contributors
        BinderContextImpl binderContext =
            processMethodContributors(presenterType, null, null, viewTypeName, m, eventType,
                parameterStrings.toArray(new String[parameterStrings.size()]));

        StringSourceWriter handlerWriter = new StringSourceWriter();

        handlerWriter.println("if (" + LogConfiguration.class.getCanonicalName()
            + ".loggingIsEnabled()) {");
        handlerWriter.println(Logger.class.getCanonicalName() + ".getLogger(\"Binder\").info(\""
            + binderContext.getLog() + "\");");
        handlerWriter.println("}");

        handlerWriter.print("presenter." + methodName + "(");
        handlerWriter.print(bindingParametersString);
        handlerWriter.println(");");

        eventHandlerWriter.println(binderContext.build(handlerWriter));

        eventHandlerWriter.outdent();
        eventHandlerWriter.println("}");
        eventHandlerWriter.outdent();
        eventHandlerWriter.print("}");

        if (fieldsAreElements) {
          if (bindingFields != null) {
            writer.print("final "
                + (isElemental ? EventListener.class.getCanonicalName()
                    : com.guit.client.dom.EventHandler.class.getCanonicalName()) + " " + methodName
                + "$" + eventName + " =");
            writer.print(eventHandlerWriter.toString());
            writer.println(";");

            for (String f : bindingFields) {
              String eventNameLower = eventName.toLowerCase();
              boolean isTouchStart = eventNameLower.equals("touchstart");
              boolean isTouchEnd = eventNameLower.equals("touchend");
              if (isTouchStart || isTouchEnd) {
                writer.println("if (com.google.gwt.event.dom.client.TouchEvent.isSupported()) {");
              }
              if (isElemental) {
                writer.println("presenter." + f + ".setOn" + eventNameLower + "(" + methodName
                    + "$" + eventName + ");");
              } else {
                writer.println("bindings.add(new " + ElementImpl.class.getCanonicalName()
                    + "(view." + f + ")." + eventNameLower + "(" + methodName + "$" + eventName
                    + "));");
              }
              if (isTouchStart || isTouchEnd) {
                writer.println("} else {");

                if (isElemental) {
                  writer.println("presenter." + f + ".setOnmouse" + (isTouchStart ? "down" : "up")
                      + "(" + methodName + "$" + eventName + ");");
                } else {
                  writer.println("bindings.add(new " + ElementImpl.class.getCanonicalName()
                      + "(view." + f + ")." + (isTouchStart ? "mousedown" : "mouseup") + "("
                      + methodName + "$" + eventName + "));");
                }

                writer.print("}");
              }
            }
          }
        } else if (viewHandlerAnnotation.force()) {
          String addMethodName = "addDomHandler";
          String eventTypeGetter = eventType.getQualifiedSourceName() + ".";
          JField typeField = eventType.getField("TYPE");
          if (typeField != null && typeField.isStatic() && typeField.isPublic()) {
            eventTypeGetter += "TYPE";
          } else {
            eventTypeGetter += "getType()";
          }
          if (bindingFields != null) {
            writer.print("final " + handlerTypeName + " " + methodName + " =");
            writer.print(eventHandlerWriter.toString());
            writer.println(";");

            for (String f : bindingFields) {
              writer.println("bindings.add(view." + f + "." + addMethodName + "(" + methodName
                  + ", " + eventTypeGetter + "));");
            }
          }

          if (addHandlerToView) {
            writer.print("bindings.add(view." + addMethodName + "(" + eventHandlerWriter.toString()
                + ", " + eventTypeGetter + "));");
          }
        } else {
          String addMethodName =
              "add" + eventName.substring(0, 1).toUpperCase() + eventName.substring(1) + "Handler";
          if (bindingFields != null) {
            writer.print("final " + handlerTypeName + " " + methodName + " =");
            writer.print(eventHandlerWriter.toString());
            writer.println(";");

            for (String f : bindingFields) {

              // Small patch for touch events
              if (addMethodName.equals("addTouchStartHandler") && parameters.length == 0) {
                writer.println("if (!com.google.gwt.event.dom.client.TouchEvent.isSupported()) {");
                writer.println("bindings.add(view." + f + ".addMouseDownHandler(new "
                    + MouseDownHandler.class.getCanonicalName() + "(){public void onMouseDown("
                    + MouseDownEvent.class.getCanonicalName() + " event){presenter." + methodName
                    + "();} }" + "));");
                writer.println("}");
              }

              if (addMethodName.equals("addTouchEndHandler") && parameters.length == 0) {
                writer.println("if (!com.google.gwt.event.dom.client.TouchEvent.isSupported()) {");
                writer.println("bindings.add(view." + f + ".addMouseUpHandler(new "
                    + MouseUpHandler.class.getCanonicalName() + "(){public void onMouseUp("
                    + MouseUpEvent.class.getCanonicalName() + " event){presenter." + methodName
                    + "();} }" + "));");
                writer.println("}");
              }
              writer.println("bindings.add(view." + f + "." + addMethodName + "(" + methodName
                  + "));");
            }
          }

          if (addHandlerToView) {
            writer.print("bindings.add(view." + addMethodName + "(" + eventHandlerWriter.toString()
                + "));");
          }
        }
      } else {
        for (Annotation a : m.getAnnotations()) {
          Class<? extends Annotation> annotationType = a.annotationType();
          if (annotationType.isAnnotationPresent(Plugin.class)) {
            String[] nameParts = name.split("[$]");

            // Check that the binding fields are valid
            StringBuilder fields = new StringBuilder();
            for (int n = 0; n < nameParts.length - 1; n++) {
              if (!validBindingFields.contains(nameParts[n])) {
                error(
                    "The field %s on the class %s is not a valid binding field. It must be public or protected and not static",
                    nameParts[n], presenterType);
              }
              if (fields.length() > 0) {
                fields.append(",");
              }
              fields.append("view." + nameParts[n]);
            }

            Class<?> handler = annotationType.getAnnotation(Plugin.class).value();
            writer.println("new " + handler.getCanonicalName()
                + "().install(new com.google.gwt.user.client.Command() {");
            writer.println("@Override");
            writer.println("public void execute() {");
            writer.println("  presenter." + m.getName() + "();");
            writer.println("}");
            writer.println("}, new Object[]{");
            writer.println(fields.toString() + "});");
          }
        }
      }
    }
  }

  private void checkForRepetition(JClassType presenterType) throws UnableToCompleteException {

    ArrayList<JField> superFields = new ArrayList<JField>();
    collectAllFields(presenterType.getSuperclass(), superFields);
    ArrayList<String> superFieldsNames = new ArrayList<String>();
    for (JField jField : superFields) {
      superFieldsNames.add(jField.getName());
    }

    for (JField f : presenterType.getFields()) {
      if (f.isAnnotationPresent(ViewField.class) || f.getName().equals("driver")) {
        if (superFieldsNames.contains(f.getName())) {
          error("The field '" + f.getName()
              + "' is already declared on a superclass, to fix delete the field. Found: "
              + presenterType.getQualifiedSourceName());
        }
      }
    }
  }

  private void printViewFieldBindings(SourceWriter writer, JClassType presenterType,
      String viewTypeName, HashMap<String, JType> validBindingFieldsType, ArrayList<JField> fields)
      throws UnableToCompleteException {

    Set<String> validBindingFields = validBindingFieldsType.keySet();

    JClassType viewAccesorType = getType(ViewAccesor.class.getCanonicalName());

    ArrayList<JField> superFields = new ArrayList<JField>();
    collectAllFields(presenterType.getSuperclass(), superFields);

    JClassType elementDomType = getType(com.guit.client.dom.Element.class.getCanonicalName());

    for (JField f : fields) {
      if (f.isAnnotationPresent(ViewField.class)) {
        // Check for repetided fields
        if (f.getType().isClassOrInterface().isAssignableTo(elementDomType)) {
          if (superFields.contains(f)) {

          }
        }

        String name = f.getName();
        ViewField viewField = f.getAnnotation(ViewField.class);
        String viewName = viewField.name();
        if (viewName.isEmpty()) {
          viewName = name;
        }
        if (!validBindingFields.contains(viewName)) {
          error("The field '%s' do not exists. Found: %s.%s", viewName, presenterType
              .getQualifiedSourceName(), name);
        }

        JClassType type = f.getType().isClassOrInterface();
        // if (type.isInterface() == null && !viewField.provided()) {
        // error("A ViewField must be an interface. Found: %s.%s", presenterType
        // .getQualifiedSourceName(), name);
        // }

        if (type.isAssignableTo(viewAccesorType)) {
          writer.println("{");
          writer.indent();
          if (!type.isAnnotationPresent(Implementation.class)) {
            writer.println(type.getQualifiedSourceName() + " accesor = "
                + GinOracle.getProvidedInstance(type.getQualifiedSourceName()) + ".get();");
          } else {

            JClassType implementation =
                getType(type.getAnnotation(Implementation.class).value().getCanonicalName());

            // If they are parameterized look for the base type
            JParameterizedType implementationParameterized = implementation.isParameterized();
            if (implementationParameterized != null) {
              implementation = implementationParameterized.getBaseType();
            }
            JParameterizedType typeParameterized = type.isParameterized();
            if (typeParameterized != null) {
              type = typeParameterized.getBaseType();
            }

            // Verify that they are assignable
            if (!implementation.isAssignableTo(type)) {
              error(
                  "An implementation of a ViewAccesor must implement the ViewAccesor interface. Found: %s",
                  implementation.getQualifiedSourceName());
            }
            writer.println(type.getQualifiedSourceName() + " accesor = new "
                + implementation.getQualifiedSourceName() + "();");
          }

          writer.println("accesor.setTarget(view." + viewName + ");");
          writer.println("presenter." + name + " = accesor;");
          writer.outdent();
          writer.print("}");
          writer.println();
        } else if (type == null
            || type.isAssignableFrom(validBindingFieldsType.get(viewName).isClassOrInterface())
            || type.getQualifiedSourceName().startsWith("elemental.")) {
          String qualifiedSourceName = f.getType().getQualifiedSourceName();
          writer.println("presenter." + name + " = (" + qualifiedSourceName + ") view." + viewName
              + ";");
          writer.println();
        } else {
          // Interface emulation (without exceptions)
          writer.println("presenter." + name + " = new "
              + type.getParameterizedQualifiedSourceName() + "() {");
          writer.indent();

          ArrayList<JMethod> methods = new ArrayList<JMethod>();
          findAllMethods(type, methods);
          for (JMethod m : methods) {
            writer.print(m.getReadableDeclaration(false, true, true, true, true));
            writer.println("{");
            writer.indent();

            // Find the parameters
            StringBuilder callParameters = new StringBuilder();
            for (JParameter p : m.getParameters()) {
              if (callParameters.length() > 0) {
                callParameters.append(", ");
              }
              if (p.isAnnotationPresent(ImplicitCast.class)) {
                callParameters.append("(");
                callParameters.append(p.getAnnotation(ImplicitCast.class).value()
                    .getCanonicalName());
                callParameters.append(") ");
              }
              callParameters.append(p.getName());
            }

            JType returnType = m.getReturnType();
            if (!returnType.equals(JPrimitiveType.VOID)) {
              writer.print("return ");

              // Implicit cast
              writer.print("(" + returnType.getParameterizedQualifiedSourceName() + ")");
            }

            writer.indent();
            writer.println(createdClassName + ".this.view." + viewName + "." + m.getName() + "("
                + callParameters.toString() + ");");
            writer.outdent();

            writer.outdent();
            writer.println("}");
            writer.println();
          }

          // Get .equals working on emulated interfaces for
          // event.getSource() comparations
          writer.println("@Override");
          writer.println("public boolean equals(Object obj) {");
          writer.indent();
          writer.println("return view." + viewName + ".equals(obj);");
          writer.outdent();
          writer.println("}");
          writer.println();

          writer.outdent();
          writer.println("};");
        }
      }
    }
  }

  @Override
  protected void processComposer(ClassSourceFileComposerFactory composer) {
    gwtEventType = getType(GwtEvent.class.getCanonicalName());
    gwtHandlerType = getType(EventHandler.class.getName());
    domEventsPackage = getPackage("com.google.gwt.event.dom.client");
    sharedEventsPackage = getPackage("com.google.gwt.event.logical.shared");
    composer.addImport(GWT.class.getCanonicalName());
    composer.addImplementedInterface(typeName);
  }

  protected BinderContextImpl processMethodContributors(JClassType presenterType,
      JClassType viewType, JClassType viewInterfaceType, String viewTypeName, JMethod method,
      JClassType eventType, String[] parameterGetters) throws UnableToCompleteException {

    Annotation[] annotations;
    try {
      annotations = (Annotation[]) getAnnotationsMethod.invoke(method);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }

    BinderContextImpl binderContext =
        new BinderContextImpl(method, eventType, presenterType, viewTypeName, parameterGetters);
    for (Annotation annotation : annotations) {
      Class<? extends Annotation> annotationType = annotation.annotationType();
      if (annotationType.isAnnotationPresent(ContributorAnnotation.class)) {
        BinderContributor contributor = getContributor(annotationType);
        binderContext.log(contributor.name());
        contributor.contribute(binderContext, logger, context);
      }
    }
    return binderContext;
  }

  /**
   * Finds the contributor by convention. It have to be on the same package than the annotation but
   * instead of client -> rebind and it have to end with Contributor.
   */
  public BinderContributor getContributor(Class<? extends Annotation> annotationType) {
    try {
      return (BinderContributor) Class.forName(
          annotationType.getCanonicalName().replace(".client.", ".rebind.") + "Contributor")
          .newInstance();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  protected void validateHandler(JMethod m, String name, String presenterName)
      throws UnableToCompleteException {
    if (m.isStatic()) {
      error("All event handlers must not be static. Found: %s.%s", presenterName, name);
    }

    if (m.isPrivate()) {
      error("All event handlers must not be private. Found: %s.%s", presenterName, name);
    }

    JPrimitiveType returnTypePrimitive = m.getReturnType().isPrimitive();
    if (returnTypePrimitive == null || !returnTypePrimitive.equals(JPrimitiveType.VOID)) {
      error("All event handlers should return void. Found: %s.%s", presenterName, name);
    }
  }

  @Override
  public long getVersionId() {
    return 1L;
  }
}
