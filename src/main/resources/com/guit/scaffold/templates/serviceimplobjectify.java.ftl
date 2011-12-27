package ${package}.server;

import com.google.gwt.view.client.Range;

import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.Query;

import ${package}.client.QueryResponse;
import ${pojoType};

public class ${pojoName}ServiceImpl implements ${pojoName}Service {

  static {
    ObjectifyService.register(${pojoName}.class);
  }

  @Override
  public QueryResponse<${pojoName}> list${pojoName}(Range range) {
    Query<${pojoName}> query =
        begin().query(${pojoName}.class).offset(range.getStart()).limit(range.getLength());
    return new QueryResponse<${pojoName}>(query.list(), query.count());
  }

  @Override
  public void put${pojoName}(${pojoName} ${pojoNameLowerCase}) {
    Objectify ofy = begin();
    ofy.put(${pojoNameLowerCase});
  }

  @Override
  public void delete${pojoName}(Long id) {
    Objectify ofy = begin();
    ofy.delete(${pojoName}.class, id);
  }

  @Override
  public ${pojoName} get${pojoName}(Long id) {
    return begin().find(${pojoName}.class, id);
  }

  public Objectify begin() {
    return ObjectifyService.begin();
  }
}