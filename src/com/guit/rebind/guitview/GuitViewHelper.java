package com.guit.rebind.guitview;

import com.google.gwt.core.ext.BadPropertyValueException;
import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.PropertyOracle;
import com.google.gwt.core.ext.SelectionProperty;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.TreeLogger.Type;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.JField;
import com.google.gwt.core.ext.typeinfo.JMethod;
import com.google.gwt.core.ext.typeinfo.JType;
import com.google.gwt.core.ext.typeinfo.NotFoundException;
import com.google.gwt.core.ext.typeinfo.TypeOracle;
import com.google.gwt.dom.client.TagName;
import com.google.gwt.editor.client.Editor.Ignore;
import com.google.gwt.editor.client.Editor.Path;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.DataResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.uibinder.rebind.UiBinderGenerator;

import com.guit.client.ViewProperties;
import com.guit.client.binder.ViewField;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXParseException;

import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class GuitViewHelper {
  static final String BINDER_URI = "urn:ui:com.google.gwt.uibinder";

  private static void findUiBundleFields(Set<GuitViewField> list, NodeList childNodes,
      String binderPrefix, TreeLogger logger, Set<String> providedFields)
      throws UnableToCompleteException {
    for (int n = 0; n < childNodes.getLength(); n++) {
      Node item = childNodes.item(n);
      if (item.getNodeType() == Node.ELEMENT_NODE) {
        Element element = (Element) item;

        String field = element.getAttribute("field");
        String namespace = element.getAttribute("type");

        String name = item.getNodeName();
        if (name.startsWith(binderPrefix)) {
          name = name.substring(binderPrefix.length() + 1);
          if (name.equals("style")) {
            if (field.isEmpty()) {
              field = "style";
            }

            if (namespace.isEmpty()) {
              namespace = CssResource.class.getCanonicalName();
            }
          } else if (name.equals("image")) {
            namespace = ImageResource.class.getCanonicalName();
          } else if (name.equals("data")) {
            namespace = DataResource.class.getCanonicalName();
          }
          list.add(new GuitViewField(field, namespace, "@UiField"
              + (providedFields.contains(field) ? "(provided=true) " : " ") + " public "
              + namespace + " " + field + ";"));
        }
      }
    }
  }

  public static Set<GuitViewField> findUiFields(JClassType presenterClass, TreeLogger logger,
      TypeOracle typeOracle, JClassType editorPojo, HashMap<String, String> editorPaths,
      String template) throws UnableToCompleteException {
    Set<GuitViewField> list = new HashSet<GuitViewField>();
    Element documentElement = getW3cDoc(presenterClass, template, logger).getDocumentElement();

    // Finds all provided fields
    HashSet<String> providedFields = getProvidedFields(presenterClass, logger);

    String binderPrefix = documentElement.lookupPrefix(BINDER_URI);
    String uiFieldAttribute = binderPrefix + ":field";
    findUiFields(list, documentElement, uiFieldAttribute, logger, typeOracle, providedFields,
        editorPojo, editorPaths);
    findUiBundleFields(list, documentElement.getChildNodes(), binderPrefix, logger, providedFields);
    return list;
  }

  public static HashSet<String> getProvidedFields(JClassType baseClass, TreeLogger logger)
      throws UnableToCompleteException {
    HashSet<String> providedFields = new HashSet<String>();
    JField[] fields = baseClass.getFields();
    for (JField f : fields) {
      if (f.isAnnotationPresent(ViewField.class)) {
        String name = f.getAnnotation(ViewField.class).name();
        if (name.isEmpty()) {
          name = f.getName();
        }

        if (f.getAnnotation(ViewField.class).provided()) {
          if (providedFields.contains(name)) {
            logger.log(Type.ERROR, "There can be only one provided @ViewField for a field. Found: "
                + baseClass.getQualifiedSourceName() + "." + f.getName());
            throw new UnableToCompleteException();
          }

          providedFields.add(name);
        }
      }
    }
    return providedFields;
  }

  private static void findUiFields(Set<GuitViewField> list, Element node, String uiFieldAttribute,
      TreeLogger logger, TypeOracle typeOracle, Set<String> providedFields, JClassType editorPojo,
      HashMap<String, String> editorPaths) throws UnableToCompleteException {
    String field = node.getAttribute(uiFieldAttribute);
    if (node.hasAttribute(uiFieldAttribute)) {
      String prefix = node.getPrefix();
      String namespace = node.lookupNamespaceURI(prefix);
      String name;
      if (namespace != null) {
        // Widgets
        if (namespace.startsWith("urn:import:")) {
          namespace = namespace.substring(11);
        } else {
          logger.log(TreeLogger.ERROR, String.format("Bad namespace. Found: %s", node.toString()));
          throw new UnableToCompleteException();
        }
        name = node.getNodeName().substring(prefix.length() + 1);
      } else {
        // Html elements
        name = findGwtDomElementTypeForTag(node.getNodeName(), typeOracle);
        namespace = "com.google.gwt.dom.client";
      }

      // GwtEditor
      String path =
          editorPojo != null && editorPaths.containsKey(field) ? "@"
              + Path.class.getCanonicalName() + "(\"" + editorPaths.get(field) + "\") " : "";

      JMethod method = null;
      if (editorPojo != null) {
        try {
          method =
              editorPojo.getMethod(
                  "get" + field.substring(0, 1).toUpperCase() + field.substring(1), new JType[0]);
        } catch (NotFoundException e) {
          // Do nothing
        }
      }
      String ignore =
          editorPojo != null && path.isEmpty()
              && (editorPojo.getField(field) == null && method == null) ? "@"
              + Ignore.class.getCanonicalName() + " " : "";

      String type = namespace + "." + name;
      list.add(new GuitViewField(field, type, path + ignore + "@UiField"
          + (providedFields.contains(field) ? "(provided=true) " : " ") + " public " + type + " "
          + field + ";"));
    }

    NodeList children = node.getChildNodes();
    for (int n = 0; n < children.getLength(); n++) {
      Node item = children.item(n);
      if (item.getNodeType() == Node.ELEMENT_NODE) {
        findUiFields(list, (Element) item, uiFieldAttribute, logger, typeOracle, providedFields,
            editorPojo, editorPaths);
      }
    }
  }

  public static boolean isOnViewPackage(JClassType baseClass, GeneratorContext context,
      TreeLogger logger) throws UnableToCompleteException {
    URL url = getDefaultUiTemplateURL(baseClass, context, logger);
    String path = url.getPath();
    path = (path.endsWith("ui.xml")) ? path.substring(0, path.lastIndexOf("/")) : path;

    return path.endsWith("/view");
  }

  public static URL getDefaultUiTemplateURL(JClassType presenterClass, GeneratorContext context,
      TreeLogger logger) throws UnableToCompleteException {
    return getUiTemplateURL(presenterClass,
        getDeclaredTemplateName(presenterClass, context, logger), logger);
  }

  public static URL getUiTemplateURL(JClassType presenterClass, String templateFile,
      TreeLogger logger) throws UnableToCompleteException {
    URL url = getResource(presenterClass, templateFile);
    if (url == null) {
      url = getResource(presenterClass, "view/" + templateFile);
    }
    if (null == url) {
      logger.log(TreeLogger.ERROR, "Unable to find resource: " + templateFile);
      throw new UnableToCompleteException();
    }

    return url;
  }

  public static boolean uiTemplateExists(JClassType presenterClass, String templateFile,
      TreeLogger logger) {
    URL url = getResource(presenterClass, templateFile);
    if (url == null) {
      url = getResource(presenterClass, "view/" + templateFile);
    }
    if (null == url) {
      return false;
    }

    return true;
  }

  private static Document getW3cDoc(JClassType presenterClass, String templateFile,
      TreeLogger logger) throws UnableToCompleteException {
    // String templateFile = getDeclaredTemplateName(baseClass);
    URL url = getUiTemplateURL(presenterClass, templateFile, logger);
    return getDocument(url, logger);
  }

  private static Document getDocument(URL url, TreeLogger logger) throws UnableToCompleteException {
    Document doc = null;
    try {
      doc = new W3cDomHelper(logger).documentFor(url);
    } catch (SAXParseException e) {
      logger.log(TreeLogger.ERROR, String.format("Error parsing XML (line " + e.getLineNumber()
          + "): " + e.getMessage(), e));
      throw new UnableToCompleteException();
    }
    return doc;
  }

  public static URL getResource(JClassType baseClass, String resourceName) {
    URL url = null;

    String packagePath = slashify(baseClass.getPackage().getName());
    String templatePath = packagePath + "/" + resourceName;
    url = UiBinderGenerator.class.getClassLoader().getResource(templatePath);
    return url;
  }

  private static String findGwtDomElementTypeForTag(String tag, TypeOracle oracle) {
    JClassType elementClass = oracle.findType("com.google.gwt.dom.client.Element");
    JClassType[] types = elementClass.getSubtypes();
    for (JClassType type : types) {
      TagName annotation = type.getAnnotation(TagName.class);
      if (annotation != null) {
        for (String annotationTag : annotation.value()) {
          if (annotationTag.equals(tag)) {
            return type.getName();
          }
        }
      }
    }
    return elementClass.getName();
  }

  public static String getDeclaredTemplateName(JClassType baseClass, GeneratorContext context,
      TreeLogger logger) throws UnableToCompleteException {
    String templateFile = baseClass.getSimpleSourceName() + ".ui.xml";
    if (baseClass.isAnnotationPresent(ViewProperties.class)) {
      String template = baseClass.getAnnotation(ViewProperties.class).template();
      if (!template.isEmpty()) {
        templateFile = template;
      }
    }

    SelectionProperty uixmlprefix;
    try {
      PropertyOracle propertyOracle = context.getPropertyOracle();
      uixmlprefix = propertyOracle.getSelectionProperty(logger, "ui.xml.prefix");
    } catch (BadPropertyValueException e) {
      throw new RuntimeException(e);
    }
    String currentValue = uixmlprefix.getCurrentValue();
    currentValue = "default".equals(currentValue) ? "" : currentValue;
    String fallbackValue = uixmlprefix.getFallbackValue();
    fallbackValue = "default".equals(fallbackValue) ? "" : fallbackValue;
    if (uiTemplateExists(baseClass, currentValue + templateFile, logger)) {
      return currentValue + templateFile;
    } else if (uiTemplateExists(baseClass, fallbackValue + templateFile, logger)) {
      return fallbackValue + templateFile;
    } else {
      logger.log(Type.ERROR, "Cannot find " + currentValue + templateFile + " or " + fallbackValue
          + templateFile);
      throw new UnableToCompleteException();
    }
  }

  static String slashify(String s) {
    return s.replace(".", "/");
  }

  static String deslashify(String s) {
    return s.replace("/", ".");
  }
}
