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
package com.guit.client.ga;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.ScriptElement;
import com.google.gwt.user.client.Window;

/**
 * Google analytics helper.
 */
public class GoogleAnalyticsImpl implements GoogleAnalytics {

  public GoogleAnalyticsImpl() {
  }

  @Override
  public void setAccount(String ua) {
    // Inject the configuration code
    ScriptElement config =
        Document.get().createScriptElement(
            "var _gaq = _gaq || [];_gaq.push(['_setAccount', '" + ua
                + "']);_gaq.push(['_trackPageview']);");
    Element s = Document.get().getElementsByTagName("script").getItem(0);
    s.getParentNode().insertBefore(config, s);

    // Inject ga code
    ScriptElement script = Document.get().createScriptElement();
    script.setSrc(("https:".equals(Window.Location.getProtocol()) ? "https://ssl" : "http://www")
        + ".google-analytics.com/ga.js");
    script.setType("text/javascript");
    script.setAttribute("async", "");
    s.getParentNode().insertBefore(script, s);
  }

  @Override
  public native void trackEvent(String category, String action) /*-{
		$wnd._gaq.push([ '_trackEvent', category, action ]);
  }-*/;

  @Override
  public native void trackEvent(String category, String action, String optLabel, int optValue) /*-{
		$wnd._gaq.push([ '_trackEvent', category, action, optLabel, optValue ]);
  }-*/;

  @Override
  public native void trackPageview() /*-{
		$wnd._gaq.push([ '_trackPageview' ]);
  }-*/;

  @Override
  public native void trackPageview(String pageName) /*-{
		if (!pageName.match("^/") == "/") {
			pageName = "/" + pageName;
		}
		$wnd._gaq.push([ '_trackPageview', pageName ]);
  }-*/;
}
