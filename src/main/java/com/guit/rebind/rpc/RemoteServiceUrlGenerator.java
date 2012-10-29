package com.guit.rebind.rpc;

import com.google.gwt.core.ext.ConfigurationProperty;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.user.rebind.ClassSourceFileComposerFactory;
import com.google.gwt.user.rebind.SourceWriter;

import com.guit.rebind.common.AbstractGenerator;
import com.guit.rpc.RemoteServiceUrl;

import java.util.ArrayList;

public class RemoteServiceUrlGenerator extends AbstractGenerator {

  @Override
  protected void generate(SourceWriter writer) throws UnableToCompleteException {
    try {
      ConfigurationProperty property = propertiesOracle.getConfigurationProperty("remote.url");
      writer.println("public " + ArrayList.class.getCanonicalName() + "<String> getRemoteUrl(){");
      if (property.getValues().size() == 0) {
        writer.println(ArrayList.class.getCanonicalName() + "<String> list = null;");
      } else {
        writer.println(ArrayList.class.getCanonicalName() + "<String> list = new "
            + ArrayList.class.getCanonicalName() + "<String>();");
        for (String u : property.getValues()) {
          writer.println("list.add(\"" + u + "\");");
        }
      }
      writer.indent();
      writer.println("return list;");
      writer.outdent();
      writer.println("}");
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  protected String processImplName(String implName) {
    return implName;
  }

  @Override
  protected void processComposer(ClassSourceFileComposerFactory composer) {
    composer.addImplementedInterface(RemoteServiceUrl.class.getName());
  }
}
