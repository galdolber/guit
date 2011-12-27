package com.guit.rebind.guitview;

import com.google.gwt.dev.util.collect.Sets;
import com.google.gwt.uibinder.rebind.UiBinderGenerator;

import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Set;

/**
 * Does special handling of external entities encountered by sax xml parser,
 * e.g. the uri in
 * 
 * <pre>
 * &lt;!DOCTYPE gwt:UiBinder
  SYSTEM "http://google-web-toolkit.googlecode.com/files/xhtml.ent"></pre>
 * <p>
 * Specifically, if the requested uri starts with
 * <code>http://google-web-toolkit.googlecode.com/files</code>, provide the
 * contents from a built in resource rather than allowing sax to make a network
 * request.
 */
class GwtResourceEntityResolver implements EntityResolver {
  interface ResourceLoader {
    InputStream fetch(String name);
  }

  private static final Set<String> EXTERNAL_PREFIXES = Collections.unmodifiableSet(Sets
      .create(new String[]{
          "http://google-web-toolkit.googlecode.com/files/", "http://dl.google.com/gwt/DTD/",
          "https://dl-ssl.google.com/gwt/DTD/"}));

  private static final String RESOURCES = "com/google/gwt/uibinder/resources/";

  private final ResourceLoader resourceLoader;

  public GwtResourceEntityResolver() {
    this(new ResourceLoader() {
      public InputStream fetch(String name) {
        return UiBinderGenerator.class.getClassLoader().getResourceAsStream(name);
      }
    });
  }

  /**
   * For testing.
   */
  GwtResourceEntityResolver(ResourceLoader resourceLoader) {
    this.resourceLoader = resourceLoader;
  }

  private String findMatchingPrefix(String systemId) {
    for (String prefix : EXTERNAL_PREFIXES) {
      if (systemId.startsWith(prefix)) {
        return prefix;
      }
    }
    return null;
  }

  public InputSource resolveEntity(String publicId, String systemId) throws SAXException,
      IOException {
    String matchingPrefix = findMatchingPrefix(systemId);
    if (matchingPrefix == null) {
      // Not our problem, return null and sax will find it
      return null;
    }

    String name = RESOURCES + systemId.substring(matchingPrefix.length());
    InputStream resourceStream = resourceLoader.fetch(name);

    if (resourceStream == null) {
      /*
       * They're asking for another DTD resource that we don't short circuit,
       * Return null and let Sax find it on the interweb.
       */
      return null;
    }

    InputSource inputSource = new InputSource(resourceStream);
    inputSource.setPublicId(publicId);
    inputSource.setSystemId(systemId);
    return inputSource;
  }
}
