package com.guit.scaffold;

import static com.guit.scaffold.Utils.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Scanner;

import freemarker.template.TemplateException;

public class Guit {

  private static Scanner in = new Scanner(System.in);

  public static void main(String[] args) throws IOException, TemplateException {
    System.out.println("Command: " + print(Command.values()));
    Command cmd = Command.valueOf(in.nextLine());
    if (cmd.equals(Command.quit)) {
      return;
    }

    if (!cmd.equals(Command.enumeditor)) {
      System.out.println("Model: ");
    } else {
      System.out.println("Enum: ");
    }
    Class<?> model = ClassForName(in.nextLine());

    create(cmd, model);

    System.out.println("Creators finished ok!");
    main(args);
  }

  public static void create(Command cmd, Class<?> model) throws IOException, TemplateException,
      FileNotFoundException {

    // We asume the model is on .client.model, .service.model or .shared.model
    String pack = model.getPackage().getName();

    if (pack.endsWith(".client.model") || pack.endsWith(".server.model")
        || pack.endsWith(".shared.model")) {

      // Remove the .model
      pack = pack.substring(0, pack.lastIndexOf("."));

      // Remove the .[client|server|shared]
      pack = pack.substring(0, pack.lastIndexOf("."));
    } else {
      // We support external enums
      if (model.isEnum()) {
        pack = Utils.findBasePackage(RESOURCES_FOLDER);
      }
    }

    // Pack path
    String packPath = pack.replaceAll("[.]", "/");

    // Model name
    String modelName = model.getSimpleName();

    File file = null;
    switch (cmd) {
      case enumeditor:
        System.out.println("Creating enum editor for " + modelName + "...");
        new File(SOURCE_FOLDER + packPath + "/client/view").mkdirs();

        EnumEditorCreator enumEditorCreator = new EnumEditorCreator();

        file = new File(SOURCE_FOLDER + packPath + "/client/view/EnumEditor.java");
        if (!file.exists()) {
          file.createNewFile();
          enumEditorCreator.createAbstractEnumEditor(pack, new FileOutputStream(file));
        }

        file = new File(SOURCE_FOLDER + packPath + "/client/view/" + modelName + "Editor.java");
        if (!file.exists()) {
          file.createNewFile();
          enumEditorCreator.createEnumEditor(model, pack, new FileOutputStream(file));
        }
        break;
      case celltable:
        System.out.println("Creating celltable for " + modelName + "...");
        new File(SOURCE_FOLDER + packPath + "/client/view").mkdirs();

        file = new File(SOURCE_FOLDER + packPath + "/client/view/" + modelName + "CellTable.java");
        if (!file.exists()) {
          file.createNewFile();
          new CellTableCreator().create(model, pack, new FileOutputStream(file));
        }
        break;
      case crud:
        System.out.println("Creating crud for " + modelName + "...");
        create(Command.form, model);
        create(Command.list, model);
        break;
      case form:
        System.out.println("Creating form for " + modelName + "...");
        new File(SOURCE_FOLDER + packPath + "/client/view").mkdirs();

        FormCreator formCreator = new FormCreator();
        file = new File(SOURCE_FOLDER + packPath + "/client/" + modelName + "Form.java");
        if (!file.exists()) {
          file.createNewFile();
          formCreator.createJava(model, pack, new FileOutputStream(file));
        }
        file = new File(RESOURCES_FOLDER + packPath + "/client/view/" + modelName + "Form.ui.xml");
        if (!file.exists()) {
          file.createNewFile();
          formCreator.createXml(model, pack, new FileOutputStream(file));
        }
        create(Command.service, model);
        break;
      case list:
        System.out.println("Creating list for " + modelName + "...");
        new File(SOURCE_FOLDER + packPath + "/client/view").mkdirs();

        ListCreator listCreator = new ListCreator();
        file = new File(SOURCE_FOLDER + packPath + "/client/" + modelName + "List.java");
        if (!file.exists()) {
          file.createNewFile();
          listCreator.createJava(model, pack, new FileOutputStream(file));
        }
        file = new File(RESOURCES_FOLDER + packPath + "/client/view/" + modelName + "List.ui.xml");
        if (!file.exists()) {
          file.createNewFile();
          listCreator.createXml(model, pack, new FileOutputStream(file));
        }
        create(Command.service, model);
        create(Command.celltable, model);
        break;
      case service:
        System.out.println("Creating service for " + modelName + "...");

        ServiceCreator serviceCreator = new ServiceCreator();
        file = new File(SOURCE_FOLDER + packPath + "/server/" + modelName + "Service.java");
        if (!file.exists()) {
          file.createNewFile();
          serviceCreator.createService(model, pack, new FileOutputStream(file));
        }

        file = new File(SOURCE_FOLDER + packPath + "/server/" + modelName + "ServiceImpl.java");
        if (!file.exists()) {
          System.out.println("Service type: " + print(ServiceType.values()));
          ServiceType type = ServiceType.valueOf(in.nextLine());
          file.createNewFile();
          switch (type) {
            case empty:
              serviceCreator.createServiceImpl(model, pack, new FileOutputStream(file));
              break;
            case objectify:
              serviceCreator.createServiceImplObjectify(model, pack, new FileOutputStream(file));
              break;
            case slim3:
              serviceCreator.createServiceImplSlim3(model, pack, new FileOutputStream(file));
              break;
            case jpa:
              serviceCreator.createServiceImplJpa(model, pack, new FileOutputStream(file));
              break;
            case jdo:
              File pmfFile = new File(SOURCE_FOLDER + packPath + "/server/PMF.java");
              if (!pmfFile.exists()) {
                pmfFile.createNewFile();
                serviceCreator.createPMF(pack, new FileOutputStream(pmfFile));
              }
              serviceCreator.createServiceImplJdo(model, pack, new FileOutputStream(file));
              break;
          }
        }
        file = new File(SOURCE_FOLDER + packPath + "/client/QueryResponse.java");
        if (!file.exists()) {
          file.createNewFile();
          serviceCreator.createQueryResponse(model, pack, new FileOutputStream(file));
        }
        break;
    }
  }

  private static String print(Enum<?>[] values) {
    StringBuilder sb = new StringBuilder();
    for (Enum<?> e : values) {
      if (sb.length() > 0) {
        sb.append(", ");
      }
      sb.append(e.name());
    }
    return sb.toString();
  }

  private static Class<?> ClassForName(String className) {
    try {
      return Class.forName(className);
    } catch (ClassNotFoundException e) {
      throw new RuntimeException(e);
    }
  }
}
