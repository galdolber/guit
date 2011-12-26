package com.guit.client.binder.prefix.client;

import junit.framework.Test;
import junit.framework.TestSuite;

import com.google.gwt.junit.tools.GWTTestSuite;

public class PrefixTestSuite extends GWTTestSuite {

    public static Test suite() {
      TestSuite suite = new TestSuite("Test binder prefixes");
      suite.addTestSuite(PrefixAGwtTest.class);
      suite.addTestSuite(PrefixBGwtTest.class);
      suite.addTestSuite(PrefixCGwtTest.class);
      suite.addTestSuite(PrefixINVGwtTest.class);
      return suite;
    }
}
