package com.guit.scaffold;

import freemarker.cache.ClassTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;

public class Creator {

  Configuration cfg = new Configuration();

  public Creator() {
    ClassTemplateLoader ctl = new ClassTemplateLoader(getClass(), "templates/");
    cfg.setTemplateLoader(ctl);
    cfg.setObjectWrapper(new DefaultObjectWrapper());
  }
}
