package ${package}.server;

import com.google.appengine.api.datastore.Key;
import com.google.gwt.view.client.Range;

import org.slim3.datastore.Datastore;
import org.slim3.datastore.ModelQuery;

import ${package}.client.QueryResponse;
import ${pojoType};

public class ${pojoName}ServiceImpl implements ${pojoName}Service {

  @Override
  public QueryResponse<${pojoName}> list${pojoName}(Range range) {
    ModelQuery<${pojoName}> query =
        Datastore.query(${pojoName}.class).offset(range.getStart()).limit(range.getLength());
    return new QueryResponse<${pojoName}>(query.asList(), range.getStart() + query.limit(1000).count());
  }

  @Override
  public void put${pojoName}(${pojoName} ${pojoNameLowerCase}) {
    Datastore.put(${pojoNameLowerCase});
  }

  @Override
  public void delete${pojoName}(Long id) {
    Datastore.delete(getKey(id));
  }

  @Override
  public ${pojoName} get${pojoName}(Long id) {
    return Datastore.get(${pojoName}.class, getKey(id));
  }

  public Key getKey(Long id) {
    return Datastore.createKey(${pojoName}.class, id);
  }
}