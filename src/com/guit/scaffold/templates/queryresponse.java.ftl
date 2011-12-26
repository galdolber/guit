package ${package}.client;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.util.List;

public class QueryResponse<M> implements IsSerializable {
  private List<M> list;
  private int count;

  public QueryResponse() {
  }
  
  @Override
  public String toString() {
    return "QueryResponse [list=" + list + ", count=" + count + "]";
  }

  public QueryResponse(List<M> list, int count) {
    this.list = list;
    this.count = count;
  }
  
  public void setList(List<M> list) {
    this.list = list;
  }
  
  public List<M> getList() {
    return list;
  }

  public void setCount(int count) {
    this.count = count;
  }

  public int getCount() {
    return count;
  }
}