package ${package}.server;

import com.google.gwt.view.client.Range;
import ${package}.client.QueryResponse;
import ${pojoType};

public class ${pojoName}ServiceImpl implements ${pojoName}Service {

  public QueryResponse<${pojoName}> list${pojoName}(Range range) {
    // TODO
    return null;
  }

  public void put${pojoName}(${pojoName} ${pojoNameLowerCase}) {
    // TODO
  }
	
  public void delete${pojoName}(${keyType} id) {
    // TODO
  }
	
  public ${pojoName} get${pojoName}(${keyType} id) {
    // TODO
    return null;
  }
}