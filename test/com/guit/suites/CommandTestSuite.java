/*
 * Copyright 2010 Gal Dolber.
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
package com.guit.suites;

import com.google.gwt.junit.tools.GWTTestSuite;

import com.guit.client.command.BatchCommandGwtTest;
import com.guit.client.command.BatchFailureCommandGwtTest;
import com.guit.client.command.CommandServiceMockGwtTest;
import com.guit.client.command.NonSerializableCommandGwtTest;
import com.guit.client.command.NotAnnotatedActionGwtTest;
import com.guit.client.command.SingleCommandGwtTest;
import com.guit.client.command.SingleFailureCommandGwtTest;

import junit.framework.Test;
import junit.framework.TestSuite;

public class CommandTestSuite extends GWTTestSuite {

  public static Test suite() {
    TestSuite suite = new TestSuite("Test command service");
    suite.addTestSuite(SingleCommandGwtTest.class);
    suite.addTestSuite(BatchCommandGwtTest.class);
    suite.addTestSuite(CommandServiceMockGwtTest.class);
    suite.addTestSuite(NotAnnotatedActionGwtTest.class);
    suite.addTestSuite(BatchFailureCommandGwtTest.class);
    suite.addTestSuite(SingleFailureCommandGwtTest.class);
    suite.addTestSuite(NonSerializableCommandGwtTest.class);
    return suite;
  }
}
