package com.guit.scaffold;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class Utils {

  public static String WAR = "war";
  public static String WAR_FOLDER = "./war/";
  public static String SOURCE_FOLDER = "./src/";
  public static String RESOURCES_FOLDER = "./src/";

  static {
    // Detect maven
    if (isMaven()) {
      WAR = "src/main/webapp";
      WAR_FOLDER = "./src/main/webapp/";
      SOURCE_FOLDER = "./src/main/java/";
      RESOURCES_FOLDER = "./src/main/resources/";
    }
  }

  static boolean isMaven() {
    return new File("./pom.xml").exists();
  }

  public static void copyfile(File src, File dst) {
    try {
      InputStream in = new FileInputStream(src);
      OutputStream out = new FileOutputStream(dst);

      byte[] buf = new byte[1024];
      int len;
      while ((len = in.read(buf)) > 0) {
        out.write(buf, 0, len);
      }
      in.close();
      out.close();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public static void downloadFile(File file, String url) {
    if (!file.exists()) {
      try {
        java.io.BufferedInputStream in =
            new java.io.BufferedInputStream(new java.net.URL(url).openStream());
        java.io.FileOutputStream fos = new java.io.FileOutputStream(file);
        java.io.BufferedOutputStream bout = new BufferedOutputStream(fos, 1024);
        byte[] data = new byte[1024];
        int x = 0;
        while ((x = in.read(data, 0, 1024)) >= 0) {
          bout.write(data, 0, x);
        }
        bout.close();
        in.close();
      } catch (Exception e) {
        throw new RuntimeException("Error downloading file", e);
      }
    }
  }

  public static String findBasePackage(String parent) {
    File src = new File(parent);
    String[] childs = src.list();
    for (String c : childs) {
      String path = parent + "/" + c;
      File child = new File(path);
      if (child.isDirectory()) {
        String basePackage = findBasePackage(path);
        if (basePackage != null) {
          return basePackage;
        }
      } else if (c.endsWith("gwt.xml")) {
        String replace = parent.substring(RESOURCES_FOLDER.length() + 1).replace("/", ".");
        return replace;
      }
    }
    return null;
  }

  public static void writeTextFile(File file, String contents) {
    Writer output = null;
    try {
      output = new BufferedWriter(new FileWriter(file));
      output.write(contents);
    } catch (IOException e) {
      throw new RuntimeException("Error writing file", e);
    } finally {
      if (output != null) {
        try {
          output.close();
        } catch (IOException e) {
          throw new RuntimeException("Error closing stream", e);
        }
      }
    }
  }

  public static void deleteDirectoryFiles(File path) {
    if (path.exists()) {
      File[] files = path.listFiles();
      for (int i = 0; i < files.length; i++) {
        if (files[i].isDirectory()) {
          deleteDirectoryFiles(files[i]);
        } else {
          files[i].delete();
        }
      }
    }
  }

  public static ArrayList<String> getZipFiles(File file, String destination) {
    ArrayList<String> files = new ArrayList<String>();
    try {
      byte[] buf = new byte[1024];
      ZipInputStream zipinputstream = null;
      ZipEntry zipentry;
      zipinputstream = new ZipInputStream(new FileInputStream(file));

      zipentry = zipinputstream.getNextEntry();
      while (zipentry != null) {
        String entryName = zipentry.getName();
        FileOutputStream fileoutputstream;
        File newFile = new File(entryName);
        String directory = newFile.getParent();

        if (directory == null) {
          if (newFile.isDirectory()) {
            break;
          }
        }

        String dependency = destination + "/" + entryName;
        files.add(dependency);
        fileoutputstream = new FileOutputStream(dependency);

        int n;
        while ((n = zipinputstream.read(buf, 0, 1024)) > -1) {
          fileoutputstream.write(buf, 0, n);
        }

        fileoutputstream.close();
        zipinputstream.closeEntry();
        zipentry = zipinputstream.getNextEntry();
      }

      zipinputstream.close();
    } catch (Exception e) {
      throw new RuntimeException("Error unzipping file", e);
    }
    return files;
  }
}
