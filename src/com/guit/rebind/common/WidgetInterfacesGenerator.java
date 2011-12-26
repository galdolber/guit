package com.guit.rebind.common;

import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.JMethod;
import com.google.gwt.core.ext.typeinfo.JPackage;
import com.google.gwt.core.ext.typeinfo.JParameter;
import com.google.gwt.thirdparty.guava.common.io.Files;
import com.google.gwt.user.rebind.ClassSourceFileComposerFactory;
import com.google.gwt.user.rebind.SourceWriter;

import java.io.File;
import java.io.IOException;

/**
 * Hacky utility to generate interfaces for all gwt widgets.
 */
public class WidgetInterfacesGenerator extends AbstractGenerator {

  private static boolean hasExecuted = false;

  @Override
  protected void generate(SourceWriter writer) throws UnableToCompleteException {
    // WIGenerator g = new WIGenerator();
    // g.generate(logger, context, typeName);

    if (hasExecuted) {
      return;
    }
    hasExecuted = true;

    JPackage ui = typeOracle.findPackage("com.google.gwt.user.client.ui");
    JClassType[] types = ui.getTypes();
    for (JClassType t : types) {
      if (t.isAbstract()) {
        continue;
      }

      String className = t.getName();

      StringBuilder sb = new StringBuilder();

      sb.append("package com.guit.client.widget;\n");
      sb.append("\n");
      sb.append("public interface " + className + " {\n");

      JMethod[] methods = t.getMethods();
      for (JMethod m : methods) {
        if (m.isStatic() || !m.isPublic()) {
          continue;
        }
        StringBuilder parameters = new StringBuilder();;
        for (JParameter p : m.getParameters()) {
          if (parameters.length() > 0) {
            parameters.append(", ");
          }
          parameters.append(p.getType().getParameterizedQualifiedSourceName() + " " + p.getName());
        }
        sb.append("  " + m.getReturnType().getParameterizedQualifiedSourceName() + " "
            + m.getName() + "(" + parameters + ");\n\n");
      }
      sb.append("}");

      try {
        Files.write(sb.toString().getBytes(), new File(
            "/Users/admin/Documents/workspace/Guit/src/com/guit/client/widget/" + className
                + ".java"));
        System.out.println("Writing " + className);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
  }

  @Override
  protected void processComposer(ClassSourceFileComposerFactory composer) {

  }
}
