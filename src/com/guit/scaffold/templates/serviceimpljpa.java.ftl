package ${package}.server;

import com.google.gwt.view.client.Range;
import com.google.inject.Inject;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import ${package}.client.QueryResponse;
import ${pojoType};

public class ${pojoName}ServiceImpl implements ${pojoName}Service {

  @Inject
  EntityManager em;

  @SuppressWarnings("unchecked")
  @Override
  public QueryResponse<${pojoName}> list${pojoName}(Range range) {
    Query q = em.createQuery("SELECT e FROM " + ${pojoName}.class.getSimpleName() + " e");
    q.setFirstResult(range.getStart());
    q.setMaxResults(range.getLength());

    Query count = em.createQuery("SELECT COUNT(e) FROM " + ${pojoName}.class.getSimpleName() + " e");

    return new QueryResponse<${pojoName}>(q.getResultList(), (Integer) count.getSingleResult());
  }

  @Override
  public void put${pojoName}(${pojoName} ${pojoNameLowerCase}) {
    em.getTransaction().begin();
    em.merge(${pojoNameLowerCase});
    em.getTransaction().commit();
  }

  @Override
  public void delete${pojoName}(Long id) {
    em.getTransaction().begin();
    em.remove(get${pojoName}(id));
    em.getTransaction().commit();
  }

  @Override
  public ${pojoName} get${pojoName}(Long id) {
    return em.find(${pojoName}.class, id);
  }
}