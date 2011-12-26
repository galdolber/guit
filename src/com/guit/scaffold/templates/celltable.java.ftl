package ${package}.client.view;

<#list imports as import>
import ${import};
</#list>
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import ${package}.client.${pojoName}List;

import java.util.List;

public class ${pojoName}CellTable extends CellTable<${pojoName}> {
  
  private final ScheduledCommand focusCommand = new ScheduledCommand() {
    @Override
    public void execute() {
      setFocus(true);
    }
  };
  
  public ${pojoName}CellTable() {   
    super(${pojoName}List.LIMIT);
    <#list columns as column>
    
    addColumn(new Column<${pojoName}, ${column.columnType}>(new ${column.cellType}()) {
      @Override
      public ${column.columnType} getValue(${pojoName} object) {
      	<#if column.getToString()>
      	return String.valueOf(object.${column.getter}());
      	<#else>
        return object.${column.getter}();
        </#if>
      }
    }, "${column.fieldName}");
	</#list>
	
    setSelectionModel(new SingleSelectionModel<${pojoName}>());
  }
  
  @Override
  public void setRowData(int start, List<? extends ${pojoName}> values) {
    super.setRowData(start, values);
    Scheduler.get().scheduleDeferred(focusCommand);
  }
}