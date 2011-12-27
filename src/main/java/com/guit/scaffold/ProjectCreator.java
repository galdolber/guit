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
package com.guit.scaffold;

import static com.guit.scaffold.Utils.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Create an empty guit project.
 */
public class ProjectCreator {

  static final String VERSION = "1.6";

  public static void main(String[] args) {
    // Confirm
    Scanner in = new Scanner(System.in);
    System.out
        .println("This script will delete your project files and create a scaffold project. Continue? (Y/N)");
    if (!in.next().toUpperCase().equals("Y")) {
      return;
    }

    String path = new File(".").getAbsolutePath();

    // Project name
    String projectName = path.substring(0, path.lastIndexOf(File.separator));
    projectName = projectName.substring(projectName.lastIndexOf(File.separator) + 1);

    // Module name
    String moduleName = projectName.toLowerCase();

    // Delete old host
    File oldHost = new File(WAR_FOLDER + projectName + ".html");
    if (oldHost.exists()) {
      oldHost.delete();
    }

    // Create host file
    writeTextFile(
        new File(WAR_FOLDER + projectName + ".html"),
        "<!doctype html>\n"
            + "<html>\n"
            + "  <head>\n"
            + "    <meta http-equiv=\"content-type\" content=\"text/html; charset=UTF-8\">\n"
            + "    <title>"
            + moduleName
            + "</title>\n"
            + "    <script type=\"text/javascript\" language=\"javascript\" src=\""
            + moduleName
            + "/"
            + moduleName
            + ".nocache.js\"></script>\n"
            + "  </head>\n"
            + "  <body>\n"
            + "    <iframe src=\"javascript:''\" id=\"__gwt_historyFrame\" tabIndex='-1' style=\"position:absolute;width:0;height:0;border:0\"></iframe>\n"
            + "  </body>\n" + "</html>\n");

    // Delete css file
    File cssFile = new File(WAR_FOLDER + projectName + ".css");
    if (cssFile.exists()) {
      cssFile.delete();
    }

    // Locate the base pakage
    String pack = findBasePackage(RESOURCES_FOLDER);
    String packPath = pack.replaceAll("[.]", "/");

    // Remove all the default files
    File client = new File(SOURCE_FOLDER + packPath + "/client/");
    if (client.exists()) {
      deleteDirectoryFiles(client);
    }
    File server = new File(SOURCE_FOLDER + packPath + "/server/");
    if (server.exists()) {
      deleteDirectoryFiles(server);
    }
    File shared = new File(SOURCE_FOLDER + packPath + "/shared/");
    if (shared.exists()) {
      deleteDirectoryFiles(shared);
    }

    // Create gwt module
    writeTextFile(
        new File(RESOURCES_FOLDER + packPath + "/" + projectName + ".gwt.xml"),
        "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
            + "<!DOCTYPE module PUBLIC \"-//Google Inc.//DTD Google Web Toolkit 2.1.0//EN\" \"http://google-web-toolkit.googlecode.com/svn/tags/2.1.0/distro-source/core/src/gwt-module.dtd\">\n"
            + "<module rename-to='" + moduleName + "'>\n" + "	<inherits name='com.guit.Guit' />\n"
            + "	\n" + "	<extend-configuration-property name=\"app.gin.module\" value=\"" + pack
            + ".client." + projectName + "GinModule\"/>\n"
            + "	<set-configuration-property name=\"app.default.place\" value=\"" + pack
            + ".client." + projectName + "\"/>\n" + "	\n" + "	<source path='client' />\n"
            + "	<source path='shared' />\n" + "</module>");

    // Create gin module
    writeTextFile(new File(SOURCE_FOLDER + packPath + "/client/" + projectName + "GinModule.java"),
        "package " + pack + ".client;\n" + "\n"
            + "import com.google.gwt.inject.client.AbstractGinModule;\n"
            + "import com.google.inject.Singleton;\n" + "\n" + "public class " + projectName
            + "GinModule extends AbstractGinModule {\n" + "\n" + "	@Override\n"
            + "	protected void configure() {\n" + "		bind(" + projectName
            + ".class).in(Singleton.class);\n" + "	}\n" + "}\n" + "");

    // Create default presenter
    writeTextFile(new File(SOURCE_FOLDER + packPath + "/client/" + projectName + ".java"),
        "package " + pack + ".client;\n" + "\n" + "import com.google.gwt.user.client.Window;\n"
            + "import com.google.gwt.user.client.ui.HasText;\n"
            + "import com.google.gwt.user.client.ui.AcceptsOneWidget;\n" + "\n"
            + "import com.google.inject.Inject;\n" + "\n"
            + "import com.guit.client.apt.GwtPresenter;\n"
            + "import com.guit.client.command.Async;\n"
            + "import com.guit.client.binder.ViewField;\n"
            + "import com.guit.client.binder.ViewHandler;\n"
            + "import com.guit.client.place.Place;\n"
            + "import com.guit.client.display.RootPanelDisplay;\n" + "\n" + "@GwtPresenter\n"
            + "public class " + projectName + " extends " + projectName
            + "Presenter implements Place<Void> {\n" + "\n" + "  @Inject\n"
            + "  @RootPanelDisplay\n" + "  AcceptsOneWidget display;\n" + "\n" + "  @Inject\n"
            + "  " + projectName + "ServiceAsync service;\n" + "\n" + "  @ViewField\n"
            + "  HasText name;\n" + "\n" + "  @Override\n" + "  public void go(Void data) {\n"
            + "    display.setWidget(getView());\n" + "  }\n" + "	\n" + "  @ViewHandler\n"
            + "  void greet$click() {\n"
            + "    service.greet(name.getText()).fire(new Async<String>() {\n"
            + "      @Override\n" + "      public void success(String response) {\n"
            + "        Window.alert(response);\n" + "      }\n" + "    });" + "  }\n" + "}\n" + "");

    writeTextFile(new File(RESOURCES_FOLDER + packPath + "/client/" + projectName + ".ui.xml"),
        "<!DOCTYPE ui:UiBinder SYSTEM \"http://dl.google.com/gwt/DTD/xhtml.ent\">\n"
            + "<ui:UiBinder xmlns:ui=\"urn:ui:com.google.gwt.uibinder\"\n"
            + "	xmlns:g=\"urn:import:com.google.gwt.user.client.ui\">\n" + "	<ui:style>\n" + "		\n"
            + "	</ui:style>\n" + "	<g:HTMLPanel>\n" + "		<label>Name</label>\n"
            + "		<g:TextBox ui:field=\"name\"></g:TextBox>\n"
            + "		<g:Button ui:field=\"greet\">Greet</g:Button>\n" + "	</g:HTMLPanel>\n"
            + "</ui:UiBinder> ");

    // Write web.xml
    writeTextFile(new File(WAR_FOLDER + "WEB-INF/web.xml"),
        "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + "<!DOCTYPE web-app\n"
            + "    PUBLIC \"-//Sun Microsystems, Inc.//DTD Web Application 2.3//EN\"\n"
            + "    \"http://java.sun.com/dtd/web-app_2_3.dtd\">\n" + "<web-app>\n" + "	<filter>\n"
            + "		<filter-name>guiceFilter</filter-name>\n"
            + "		<filter-class>com.google.inject.servlet.GuiceFilter</filter-class>\n"
            + "	</filter>\n" + "	<filter-mapping>\n" + "		<filter-name>guiceFilter</filter-name>\n"
            + "		<url-pattern>/*</url-pattern>\n" + "	</filter-mapping>\n" + "	<listener>\n"
            + "		<listener-class>" + pack + ".server." + projectName
            + "GuiceListener</listener-class>\n" + "	</listener>\n" + "\n"
            + "	<welcome-file-list>\n" + "		<welcome-file>" + projectName + ".jsp</welcome-file>\n"
            + "	</welcome-file-list>\n" + "</web-app>\n" + "");

    // Context listener
    writeTextFile(new File(SOURCE_FOLDER + packPath + "/server/" + projectName
        + "GuiceListener.java"), "package " + pack + ".server;\n" + "\n"
        + "import com.google.inject.Guice;\n" + "import com.google.inject.Injector;\n"
        + "import com.google.inject.servlet.GuiceServletContextListener;\n" + "\n"
        + "public class " + projectName + "GuiceListener extends GuiceServletContextListener {\n"
        + "\n" + "	public static Injector injector;\n" + "\n" + "	@Override\n"
        + "	protected Injector getInjector() {\n" + "		return injector = Guice.createInjector(new "
        + projectName + "Module());\n" + "	}\n" + "}\n" + "");

    // Guice module
    writeTextFile(new File(SOURCE_FOLDER + packPath + "/server/" + projectName + "Module.java"),
        "package " + pack + ".server;\n" + "\n"
            + "import com.google.inject.servlet.ServletModule;\n" + "\n"
            + "import com.guit.server.guice.GuiceGwtServlet;\n"
            + "import com.google.inject.Singleton;\n"
            + "import com.guit.server.guice.GuitModule;\n" + "\n" + "public class " + projectName
            + "Module extends ServletModule {\n" + "\n" + "	@Override\n"
            + "	protected void configureServlets() {\n" + "		serve(\"/" + moduleName
            + "/service\").with(GuiceGwtServlet.class);\n" + "		bind(" + projectName
            + "Service.class).to(" + projectName + "ServiceImpl.class).in(Singleton.class);" + "\n"
            + "		install(new GuitModule());\n" + "	}\n" + "}\n");

    // Guit service
    writeTextFile(new File(SOURCE_FOLDER + packPath + "/server/" + projectName + "Service.java"),
        "package " + pack + ".server;\n" + "\n" + "import com.guit.client.apt.GuitService;\n"
            + "\n" + "@GuitService\n" + "public interface " + projectName + "Service {\n" + "	\n"
            + "	String greet(String name);\n" + "}\n" + "");

    writeTextFile(
        new File(SOURCE_FOLDER + packPath + "/server/" + projectName + "ServiceImpl.java"),
        "package " + pack + ".server;\n" + "\n" + "public class " + projectName
            + "ServiceImpl implements " + projectName + "Service {\n" + "\n" + "	@Override\n"
            + "	public String greet(String name) {\n" + "		return \"Hello \" + name;\n" + "	}\n"
            + "}\n" + "");

    // Download dependencies
    ArrayList<String> files = new ArrayList<String>();
    if (!isMaven()) {
      new File("../.guit/").mkdir();
      File dependencies = new File("../.guit/dependencies-" + VERSION + ".zip");
      String url = "http://guit.googlecode.com/files/guice-gin-" + VERSION + ".zip";
      downloadFile(dependencies, url);

      // Unzip
      files = getZipFiles(dependencies, new File(WAR_FOLDER + "WEB-INF/lib/").getAbsolutePath());
    }

    StringBuilder sb = new StringBuilder();
    sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
    sb.append("<classpath>\n");
    if (!isMaven()) {
      if (isMaven()) {
        sb.append(" <classpathentry kind=\"src\" path=\"src/main/java\"/>");
        sb.append(" <classpathentry kind=\"src\" path=\"src/main/resources\"/>");
      } else {
        sb.append("	<classpathentry kind=\"src\" path=\"src\"/>\n");
      }
      sb.append("	<classpathentry kind=\"src\" output=\"test-classes\" path=\"test\"/>\n"
          + "	<classpathentry kind=\"con\" path=\"com.google.appengine.eclipse.core.GAE_CONTAINER\"/>\n"
          + "	<classpathentry kind=\"con\" path=\"com.google.gwt.eclipse.core.GWT_CONTAINER\"/>\n"
          + "	<classpathentry kind=\"con\" path=\"org.eclipse.jdt.launching.JRE_CONTAINER\"/>\n");
      for (String file : files) {
        sb.append("" + "	<classpathentry kind=\"lib\" path=\"" + file.substring(file.indexOf(WAR))
            + "\"/>\n");
      }
      sb.append("	<classpathentry kind=\"lib\" path=\"" + WAR + "/WEB-INF/lib/guit-" + VERSION
          + ".jar\"/>\n" + "	<classpathentry kind=\"src\" path=\".apt_generated\">\n"
          + "		<attributes>\n" + "			<attribute name=\"optional\" value=\"true\"/>\n"
          + "		</attributes>\n" + "	</classpathentry>\n"
          + "	<classpathentry kind=\"output\" path=\"" + WAR + "/WEB-INF/classes\"/>\n");
    } else {
      sb.append("<classpathentry kind=\"src\" output=\"src/main/webapp/WEB-INF/classes\" path=\"src/main/java\" />");
      sb.append("<classpathentry kind=\"src\" output=\"src/main/webapp/WEB-INF/classes\" path=\"src/main/resources\" />");
      sb.append("<classpathentry kind=\"src\" output=\"target/test-classes\" path=\"src/test/java\" />");
      sb.append("<classpathentry kind=\"src\" output=\"target/test-classes\" path=\"src/test/resources\" />");
      sb.append("<classpathentry kind=\"con\" path=\"org.eclipse.jdt.launching.JRE_CONTAINER/org.eclipse.jdt.internal.debug.ui.launcher.StandardVMType/J2SE-1.5\" />");
      sb.append("<classpathentry kind=\"con\" path=\"com.google.gwt.eclipse.core.GWT_CONTAINER\" />");
      sb.append("<classpathentry kind=\"con\" path=\"org.maven.ide.eclipse.MAVEN2_CLASSPATH_CONTAINER\">");
      sb.append("<attributes>");
      sb.append("<attribute name=\"org.eclipse.jst.component.dependency\" value=\"/WEB-INF/lib\" />");
      sb.append("</attributes>");
      sb.append("</classpathentry>");
      sb.append("<classpathentry kind=\"con\" path=\"org.eclipse.jst.j2ee.internal.web.container\" />");
      sb.append("<classpathentry kind=\"con\" path=\"org.eclipse.jst.j2ee.internal.module.container\" />");
      sb.append("<classpathentry kind=\"output\" path=\"src/main/webapp/WEB-INF/classes\" />");
    }
    sb.append("</classpath>\n");
    writeTextFile(new File("./.classpath"), sb.toString());

    // Factory path
    writeTextFile(new File("./.factorypath"), "<factorypath>\n"
        + "    <factorypathentry kind=\"WKSPJAR\" id=\"/" + projectName
        + "/gen/guit-gen.jar\" enabled=\"true\" runInBatchMode=\"false\"/>\n" + "</factorypath>\n"
        + "");

    // Download guit-gen
    File guitgen = new File("../.guit/guit-gen-" + VERSION + ".jar");
    downloadFile(guitgen, "http://guit.googlecode.com/files/guit-gen-" + VERSION + ".jar");
    new File("./gen/").mkdir();
    copyfile(guitgen, new File("./gen/guit-gen.jar"));

    // Enable annotation processing
    new File("./.settings").mkdir();
    writeTextFile(new File("./.settings/org.eclipse.jdt.apt.core.prefs"),
        "eclipse.preferences.version=1\n" + "org.eclipse.jdt.apt.aptEnabled=true\n"
            + "org.eclipse.jdt.apt.genSrcDir=.apt_generated\n"
            + "org.eclipse.jdt.apt.reconcileEnabled=true\n" + "");

    System.out.println("Project created!");
  }
}
