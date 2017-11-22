
package chesschamp;

/**
 *
 * @author lajos
 */
public class Allas {

  public Mezo[][] TABLA = new Mezo[8][8];
  public int[] honnan = new int[2];
  public int[] hova = new int[2];
  public int babu;
  public int szin;
  public int[] kiraly_v = new int[2];
  public int[] kiraly_s = new int[2];
  public boolean v_sakk;
  public boolean s_sakk;
  public boolean skir_lepett;
  public boolean vkir_lepett;
  public boolean Bvj_lepett;
  public boolean Bvb_lepett;
  public boolean Bsj_lepett;
  public boolean Bsb_lepett;
  public boolean patt;
  public boolean v_sancolt;
  public boolean s_sancolt;
  public int pontszam, allasIsm;

  public Allas() {
    for (int i = 0; i < 8; i++) {
      for (int j = 0; j < 8; j++) {
        TABLA[i][j] = new Mezo();
      }
    }
    allasIsm = 0;
  }

}
