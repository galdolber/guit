package ${package}.client.view;
 
import com.google.gwt.editor.client.LeafValueEditor; 
import com.google.gwt.user.client.ui.ListBox;

import java.util.HashMap; 

public abstract class EnumEditor<T extends Enum<T>> extends ListBox implements LeafValueEditor<T> { 
        
  private Class<T> clazz; 
  private HashMap<T, Integer> index = new HashMap<T, Integer>(); 

  public EnumEditor(Class<T> e) { 
    super(); 
    this.clazz = e; 
    int idx = 0; 
    for (T t : e.getEnumConstants()) { 
      this.addItem(t.toString(), t.name()); 
      index.put(t, idx); 
      idx++; 
    } 
  } 

  @Override 
  public void setValue(T value) { 
    if (value == null) { 
      setSelectedIndex(-1); 
    } else { 
      setSelectedIndex(index.get(value)); 
    } 
  } 

  @Override 
  public T getValue() { 
    int idx = getSelectedIndex(); 
    if (idx == -1) {
      return null; 
    } else { 
      return Enum.valueOf(clazz, getValue(idx)); 
    } 
  } 
} 