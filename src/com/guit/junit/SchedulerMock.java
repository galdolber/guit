/*
 * Copyright 2010 Google Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.guit.junit;

import com.google.gwt.core.client.impl.SchedulerImpl;
import com.google.gwt.user.client.Timer;

import java.util.ArrayList;

/**
 * Mock for SchedulerImpl.
 * 
 * SchedulerImpl's scheduling functions are implemented with the timer, which
 * fakes time
 */
public class SchedulerMock extends SchedulerImpl {
  // These functions are implemented using the timer class so controlling time
  // with JavaTimer.advanceTime* may also trigger these scheduled commands.
  @Override
  public void scheduleFixedDelay(final RepeatingCommand cmd, final int delayMs) {
    new Timer() {
      @Override
      public void run() {
        boolean repeat = cmd.execute();
        if (repeat) {
          this.schedule(delayMs);
        }
      }
    }.schedule(delayMs);
  }

  @Override
  public void scheduleFixedPeriod(final RepeatingCommand cmd, int delayMs) {
    new Timer() {
      @Override
      public void run() {
        boolean repeat = cmd.execute();
        if (!repeat) {
          cancel();
        }
      }
    }.scheduleRepeating(delayMs);
  }

  ArrayList<ScheduledCommand> deferredCmds = new ArrayList<ScheduledCommand>();
  ArrayList<ScheduledCommand> finallyCmds = new ArrayList<ScheduledCommand>();

  @Override
  public void scheduleDeferred(ScheduledCommand cmd) {
    deferredCmds.add(cmd);
  }

  @Override
  public void scheduleFinally(ScheduledCommand cmd) {
    finallyCmds.add(cmd);
  }

  @Override
  public void scheduleIncremental(RepeatingCommand cmd) {
    throw new AssertionError("Not implemented");
  }

  public void runDeferredCmds() {
    ArrayList<ScheduledCommand> oldCommands = deferredCmds;
    deferredCmds = new ArrayList<ScheduledCommand>();

    for (ScheduledCommand c : oldCommands) {
      c.execute();
    }
  }

  public void runFinallyCmds() {
    // All the commands should be run regardless of any exceptions thrown.
    // We will throw the first one.
    RuntimeException exception = null;
    ArrayList<ScheduledCommand> oldFinally = finallyCmds;
    finallyCmds = new ArrayList<ScheduledCommand>();

    for (ScheduledCommand c : oldFinally) {
      try {
        c.execute();
      } catch (RuntimeException e) {
        if (exception == null) {
          exception = e;
        } else {
          System.err.println("Multiple exceptions detected in runFinallyCmds."
              + " Subsequent exceptions ignored.");
        }
      }
    }

    if (exception != null) {
      throw exception;
    }
  }

  /**
   * Run the specified command in a try/catch block. Deferred commands are run
   * after the command if the command does not throw an exception and finally
   * commands are run afterwards regardless.
   * 
   * @param cmd
   */
  public void runAndFlushScheduler(ScheduledCommand cmd) {
    try {
      cmd.execute();
      runDeferredCmds();
    } catch (RuntimeException e) {
      throw e;
    } finally {
      runFinallyCmds();
    }
  }
}
