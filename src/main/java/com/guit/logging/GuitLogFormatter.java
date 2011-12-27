package com.guit.logging;

import com.google.gwt.logging.client.TextLogFormatter;

import java.util.logging.LogRecord;

public class GuitLogFormatter extends TextLogFormatter {

  public GuitLogFormatter(boolean showStackTraces) {
    super(showStackTraces);
  }

  @Override
  protected String getRecordInfo(LogRecord event, String newline) {
    return event.getLevel().getName() + "(" + event.getLoggerName() + "): ";
  }
}
