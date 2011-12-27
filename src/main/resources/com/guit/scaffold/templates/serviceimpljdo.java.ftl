package ${package}.server;

import com.google.gwt.view.client.Range;

import java.util.List;
import java.util.ArrayList;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import ${package}.client.QueryResponse;
import ${pojoType};

public class ${pojoName}ServiceImpl implements ${pojoName}Service {

  PersistenceManager pm = PMF.get().getPersistenceManager();

  @SuppressWarnings("unchecked")
  @Override
  public QueryResponse<${pojoName}> list${pojoName}(Range range) {
    Query query = pm.newQuery(${pojoName}.class);
    query.setRange(range.getStart(), range.getStart() + range.getLength());

    Query count = pm.newQuery(${pojoName}.class);
    count.setResult("count(id)");

    return new QueryResponse<${pojoName}>(new ArrayList<${pojoName}>((List<${pojoName}>) query.execute()),
        (Integer) count.execute());
  }

  @Override
  public void put${pojoName}(${pojoName} ${pojoNameLowerCase}) {
    pm.makePersistent(${pojoNameLowerCase});
  }

  @Override
  public void delete${pojoName}(Long id) {
    pm.deletePersistent(get${pojoName}(id));
  }

  @Override
  public ${pojoName} get${pojoName}(Long id) {
    return pm.detachCopy(pm.getObjectById(${pojoName}.class, id));
  }
}