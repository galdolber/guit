package ${package}.client;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.Range;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.inject.Inject;

import com.guit.client.apt.GwtPresenter;
import com.guit.client.binder.ViewField;
import com.guit.client.binder.ViewHandler;
import com.guit.client.binder.ViewInitializer;
import com.guit.client.command.Async;
import com.guit.client.place.Place;
import com.guit.client.display.RootLayoutPanelDisplay;

import ${pojoType};

@GwtPresenter
public class ${pojoName}List extends ${pojoName}ListPresenter implements Place<Range> {

  @Inject
  @RootLayoutPanelDisplay
  AcceptsOneWidget display;
  
  @Inject
  ${pojoName}ServiceAsync ${pojoNameLowerCase}Service;

  @ViewField
  HasData<${pojoName}> table;
  
  public static final int LIMIT = 30; 
  private static final Range defaultRange = new Range(0, LIMIT);
	
  @ViewInitializer
  public void initView() {
    AsyncDataProvider<${pojoName}> provider = new AsyncDataProvider<${pojoName}>() {
      @Override
      protected void onRangeChanged(HasData<${pojoName}> display) {
        refresh();
      }
    };
    provider.addDataDisplay(table);
  }

  @Override
  public void go(Range data) {
    display.setWidget(getView());
    if (data != null) {
      table.setVisibleRange(data);
    } else {
      table.setVisibleRange(defaultRange);
    }
    refresh();
  }

  void refresh() {
    final Range range = table.getVisibleRange();
    ${pojoNameLowerCase}Service.list${pojoName}(range).fire(new Async<QueryResponse<${pojoName}>>() {
      @Override
      public void success(QueryResponse<${pojoName}> response) {
        table.setRowCount(response.getCount());
        table.setRowData(range.getStart(), response.getList());
      }
    });

    placeManager.newItem(${pojoName}List.class, range, defaultRange);
  }

  @ViewHandler
  void create$click() {
    placeManager.go(${pojoName}Form.class);
  }

  @ViewHandler
  void edit$click() {
    ${pojoName} selected = getSelected();
    if (selected != null) {
      placeManager.go(${pojoName}Form.class, selected.${keyPath});
    }
  }

  @ViewHandler
  void delete$click() {
    ${pojoName} selected = getSelected();
    if (selected != null) {
      ${pojoNameLowerCase}Service.delete${pojoName}(selected.getId()).fire(new Async<Void>() {
        @Override
        public void success(Void response) {
          refresh();
        }
      });
    }
  }

  ${pojoName} getSelected() {
    return (${pojoName}) ((SingleSelectionModel<? super ${pojoName}>) table.getSelectionModel()).getSelectedObject();
  }
}