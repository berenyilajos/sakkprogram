
package assoc;

import java.util.ArrayList;

/**
 *
 * @author lajos
 */
public class AssocStringArray_old {

  private final ArrayList<AssocString> arraylist;

  public AssocStringArray_old() {
    arraylist = new ArrayList<AssocString>();
  }

  public void set(String name, String data) {
    for (AssocString as : arraylist) {
      if (as.get().equals(name)) {
        as.set(data);
        return;
      }
    }
    arraylist.add(new AssocString(name, data));
  }

  public void set(String name, Object data) {
    for (AssocString as : arraylist) {
      if (as.get().equals(name)) {
        as.set(data.toString());
        return;
      }
    }
    arraylist.add(new AssocString(name, data.toString()));
  }

  public void unset(String name) {
    for (AssocString as : arraylist) {
      if (as.get().equals(name)) {
        arraylist.remove(as);
        return;
      }
    }
  }

  public String get(String name) {
    for (AssocString as : arraylist) {
      if (as.get().equals(name)) {
        return as.toString(); // return data
      }
    }
    return null;
  }

  public String get(int index) {
    if (index < arraylist.size() && index >= 0) {
      return arraylist.get(index).toString();
    }
    return null;
  }

  public int size() {
    return arraylist.size();
  }

  @Override
  public String toString() {
    String ret = "";
    for (AssocString as : arraylist) {
      ret += as.get() + " : " + as.toString() + "\n";
    }
    return ret;
  }

}
