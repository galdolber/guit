package com.guit.junit.dom;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;

public class ElementFactoryGenerator {

  public static void main(String[] args) {
    HashSet<Class<?>> classes = getClasses("com.guit.client.dom");
    for (Class<?> c : classes) {
      String name = c.getSimpleName().toLowerCase();
      if (!name.endsWith("impl") && !name.endsWith("test")) {
        System.out.println("public " + c.getSimpleName() + " " + name + "() {");
        System.out.println("  return new " + c.getSimpleName() + "Mock();");
        System.out.println("}");
      }
    }
  }

  /**
   * Scans all classes accessible from the context class loader which belong to
   * the given package and subpackages.
   * 
   * @param packageName The base package
   * @return The classes
   * @throws ClassNotFoundException
   * @throws IOException
   */
  private static HashSet<Class<?>> getClasses(String packageName) {
    try {
      ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
      assert classLoader != null;
      String path = packageName.replace('.', '/');
      Enumeration<URL> resources = classLoader.getResources(path);
      List<File> dirs = new ArrayList<File>();
      while (resources.hasMoreElements()) {
        URL resource = resources.nextElement();
        dirs.add(new File(resource.getFile()));
      }
      HashSet<Class<?>> classes = new HashSet<Class<?>>();
      for (File directory : dirs) {
        classes.addAll(findClasses(directory, packageName));
      }
      return classes;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Recursive method used to find all classes in a given directory and subdirs.
   * 
   * @param directory The base directory
   * @param packageName The package name for classes found inside the base
   *          directory
   * @return The classes
   * @throws ClassNotFoundException
   */
  private static List<Class<?>> findClasses(File directory, String packageName)
      throws ClassNotFoundException {
    List<Class<?>> classes = new ArrayList<Class<?>>();
    if (!directory.exists()) {
      return classes;
    }
    File[] files = directory.listFiles();
    for (File file : files) {
      if (file.isDirectory()) {
        assert !file.getName().contains(".");
        classes.addAll(findClasses(file, packageName + "." + file.getName()));
      } else if (file.getName().endsWith(".class")) {
        classes.add(Class.forName(packageName + '.'
            + file.getName().substring(0, file.getName().length() - 6)));
      }
    }
    return classes;
  }
}
