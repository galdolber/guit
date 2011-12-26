package ${package}.server;

import com.google.gwt.view.client.Range;
import com.google.inject.ImplementedBy;
import com.guit.client.apt.GuitService;
import ${package}.client.QueryResponse;
import ${pojoType};

@GuitService
@ImplementedBy(${pojoName}ServiceImpl.class)
public interface ${pojoName}Service {

  QueryResponse<${pojoName}> list${pojoName}(Range range);

  void put${pojoName}(${pojoName} ${pojoNameLowerCase});
	
  void delete${pojoName}(${keyType} id);
	
  ${pojoName} get${pojoName}(${keyType} id);
}