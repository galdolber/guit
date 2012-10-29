package com.guit.rebind.rpc;

import com.google.gwt.core.ext.ConfigurationProperty;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.user.rebind.ClassSourceFileComposerFactory;
import com.google.gwt.user.rebind.SourceWriter;

import com.guit.rebind.common.AbstractGenerator;
import com.guit.rpc.RemoteServiceUrl;

public class RemoteServiceUrlGenerator extends AbstractGenerator {

  @Override
  protected void generate(SourceWriter writer) throws UnableToCompleteException {
    try {
      ConfigurationProperty property = propertiesOracle.getConfigurationProperty("remote.url");
      String remoteUrl;
      if (property.getValues().size() == 0) {
        remoteUrl = "null";
      } else {
        remoteUrl = "\"" + property.getValues().get(0) + "\"";
      }
      writer.println("public String getRemoteUrl(){");
      writer.indent();
      writer.println("return " + remoteUrl + ";");
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
