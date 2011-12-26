package com.guit.logging;

import java.util.logging.Level;

/**
 * A Handler that prints logs to GWT.log, causing the messages to show up in the
 * Development Mode tab in Eclipse when running in Development mode.
 */
public class DevelopmentModeLogHandler extends
    com.google.gwt.logging.client.DevelopmentModeLogHandler {

  public DevelopmentModeLogHandler() {
    setFormatter(new GuitLogFormatter(false));
    setLevel(Level.ALL);
  }
}
