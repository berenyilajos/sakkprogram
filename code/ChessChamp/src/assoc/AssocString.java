
package assoc;

/**
 *
 * @author lajos
 */
class AssocString {

  private final String name;
  private String data;

  AssocString(String name, String data) {
    this.name = name;
    this.data = data;
  }

  void set(String data) {
    this.data = data;
  }

  String get() {
    return this.name;
  }

  @Override
  public String toString() {
    return this.data;
  }

}
