
package assoc;

import java.util.LinkedHashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author lajos
 */
public class AssocStringArray {
  // If we don't use get(int index) method, could use HashMap instead of LinkedHashMap

  private final LinkedHashMap<String, String> hashmap;

  public AssocStringArray() {
    hashmap = new LinkedHashMap<>();
  }

  public void set(String name, String data) {
    hashmap.put(name, data);
  }

  public void set(String name, Object data) {
    hashmap.put(name, data.toString());
  }

  public void unset(String name) {
    hashmap.remove(name);
  }

  public String get(String name) {
    return hashmap.get(name);
  }

  // If we dont use this method, could use HashMap instead of LinkedHashMap
  public String get(int index) {
    if (index < hashmap.size() && index >= 0) {
      return hashmap.values().toArray()[index].toString();
    }
    return null;
  }

  public int size() {
    return hashmap.size();
  }

  @Override
  public String toString() {
    // Get a set of the entries
    Set set = hashmap.entrySet();

    // Get an iterator
    Iterator i = set.iterator();

    StringBuilder ret = new StringBuilder("");

    // Display elements
    while (i.hasNext()) {
      Map.Entry me = (Map.Entry) i.next();
      ret.append(me.getKey()).append(" : ").append(me.getValue()).append("\n");
    }

    return ret.toString();
  }

}
