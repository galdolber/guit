package com.guit.logging;

import java.util.logging.Level;

/**
 * A Handler that prints logs to window.console which is used by Firebug. Note
 * we are consciously using 'window' rather than '$wnd' to avoid issues similar
 * to http://code.google.com/p/fbug/issues/detail?id=2914
 */
public class FirebugLogHandler extends com.google.gwt.logging.client.FirebugLogHandler {

  public FirebugLogHandler() {
    setFormatter(new GuitLogFormatter(true));
    setLevel(Level.ALL);
  }
}
