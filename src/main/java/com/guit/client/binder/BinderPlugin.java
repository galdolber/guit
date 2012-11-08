package com.guit.client.binder;

import com.google.gwt.user.client.Command;

public interface BinderPlugin {

  public void install(Command cmd, Object... o);
}
