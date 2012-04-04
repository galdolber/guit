package com.guit.server.command;

import com.guit.client.command.action.Action;
import com.guit.client.command.action.CommandException;
import com.guit.client.command.action.Response;

public interface CommandHook {

  void success(Action<Response> action, Response response);

  void exception(Action<Response> action, CommandException e);

  void unexpectedException(Throwable throwable);
}
