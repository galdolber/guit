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
package com.guit.rebind.binder.contributor;

import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.user.rebind.StringSourceWriter;

import com.guit.client.binder.contributor.UserAction;
import com.guit.client.binder.useraction.Category;
import com.guit.client.binder.useraction.HasUserActionValue;
import com.guit.client.binder.useraction.event.UserActionEvent;
import com.guit.rebind.binder.BinderContext;
import com.guit.rebind.binder.BinderContributor;

public class UserActionContributor implements BinderContributor {

  @Override
  public void contribute(BinderContext binderContext, TreeLogger logger, GeneratorContext context)
      throws UnableToCompleteException {
    String userAction = binderContext.getMethod().getAnnotation(UserAction.class).value();

    StringSourceWriter writer = new StringSourceWriter();
    String component;
    JClassType presenterType = binderContext.getPresenterType();
    if (presenterType.isAnnotationPresent(Category.class)) {
      component = presenterType.getAnnotation(Category.class).value();
    } else {
      component = presenterType.getQualifiedSourceName();
    }

    writer.print("eventBus.fireEvent(new " + UserActionEvent.class.getCanonicalName() + "(\""
        + component + "\", \"" + userAction + "\"");

    JClassType hasUserAction =
        context.getTypeOracle().findType(HasUserActionValue.class.getCanonicalName());
    if (!presenterType.isAssignableTo(hasUserAction)) {
      writer.println("));");
    } else {
      writer.println(", presenter.getUserActionText(), presenter.getUserActionNumber()));");
    }
    binderContext.addAfterHandler(writer);
  }

  @Override
  public String name() {
    return "UserAction";
  }
}
