package ${package}.client;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;

import com.guit.client.apt.GwtPresenter;
import com.guit.client.binder.GwtEditor;
import com.guit.client.binder.ViewHandler;
import com.guit.client.command.Async;
import com.guit.client.place.Place;
import com.guit.client.display.RootLayoutPanelDisplay;

import ${pojoType};

@GwtPresenter(autofocus = "${autofocus}")
@GwtEditor(pojo = ${pojoName}.class)
public class ${pojoName}Form extends ${pojoName}FormPresenter implements Place<${keyType}> {

  @Inject
  @RootLayoutPanelDisplay
  AcceptsOneWidget display;
  
  @Inject
  ${pojoName}ServiceAsync ${pojoNameLowerCase}Service;
  
  @Override
  public void go(${keyType} data) {
    display.setWidget(getView());
    if (data == null) {
      driver.edit(new ${pojoName}());
    } else {
      ${pojoNameLowerCase}Service.get${pojoName}(data).fire(new Async<${pojoName}>() {
        @Override
        public void success(${pojoName} ${pojoNameLowerCase}) {
          driver.edit(${pojoNameLowerCase});
        }
      });
    }
  }
	
  @ViewHandler
  void save$click() {
    ${pojoNameLowerCase}Service.put${pojoName}(driver.flush()).fire(new Async<Void>() {
      @Override
      public void success(Void response) {
        placeManager.goBack();
      }
    });
  }
	
  @ViewHandler
  void cancel$click() {
    placeManager.goBack();
  }
}