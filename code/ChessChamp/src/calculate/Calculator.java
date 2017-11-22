
package calculate;

import chesschamp.Allas;
import constants.Constants;

/**
 *
 * @author lajos
 */
public class Calculator implements Constants {
  
  public Allas[] ALLAS;
  public boolean an_pass;
  public boolean hibaSanc;
  public int lepes, erosseg;
  public int[] kir = new int[2];
  public int[] ellKir = new int[2];
  public int[]  honnan = new int[2], hova = new int[2];
  public boolean kezd;

  public Calculator() {
    this.ALLAS = new Allas[420];
    for (int i = 0; i < ALLAS.length; i++) {
      ALLAS[i] = new Allas();
    }
//        this.segedTomb = new int[]{URES, K, V, B, H, F, GY};
    this.erosseg = GYENGE;
    this.lepes = 0;
    this.kezd = false;
  }
  
  private int ertek(int e) {
    if (e < 0) {
      e = e * -1;
    }
    return e;
  }
  
  private int bab_poz(int x, int y) {
    switch (ALLAS[lepes].TABLA[y][x].babu) {
      case F:
        return FUTO_POZ[x][y];
      case H:
        return HUSZAR_POZ[x][y];
      case GY:
        return GYALOG_POZ[x][y];
      default:
        return 0;
    }
  }
  
  private boolean futo(int honnanV, int honnanF, int hovaV, int hovaF) {
    int ev = 1, ef = 1, i;
    if (hovaV < honnanV) {
      ev = -1;
    }
    if (hovaF < honnanF) {
      ef = -1;
    }
    if ((hovaV - honnanV) * ev != (hovaF - honnanF) * ef) {
      return false;
    }
    for (i = 1; i < (hovaV - honnanV) * ev; i++) {
      if (ALLAS[lepes].TABLA[honnanV + i * ev][honnanF + i * ef].babu != 0) {
        return false;
      }
    }
    return true;
  }

  private boolean bastya(int honnanV, int honnanF, int hovaV, int hovaF) {

    if ((hovaV != honnanV) && (hovaF != honnanF)) {
      return false;
    }
    int e = 1, i;
    if (hovaV == honnanV) {
      if (hovaF - honnanF < 0) {
        e = -1;
      }
      for (i = 1; i < ertek(hovaF - honnanF); i++) {
        if (ALLAS[lepes].TABLA[honnanV][honnanF + i * e].babu != 0) {
          return false;
        }
      }
      return true;
    } else {
      if (hovaV - honnanV < 0) {
        e = -1;
      }
      for (i = 1; i < ertek(hovaV - honnanV); i++) {
        if (ALLAS[lepes].TABLA[honnanV + i * e][honnanF].babu != 0) {
          return false;
        }
      }
      return true;
    }
  }

  private boolean huszar(int honnanV, int honnanF, int hovaV, int hovaF) {
    return (ertek(hovaV - honnanV) == 2 && ertek(hovaF - honnanF) == 1) || ertek(hovaV - honnanV) == 1 && ertek(hovaF - honnanF) == 2;
  }

  private boolean kiraly(int honnanV, int honnanF, int hovaV, int hovaF) {
    return ertek(hovaV - honnanV) <= 1 && ertek(hovaF - honnanF) <= 1;
  }

//private boolean vezer(int honnanV, int honnanF, int hovaV, int hovaF){
//    return bastya(honnanV, honnanF,hovaV, hovaF) || futo(honnanV, honnanF,hovaV, hovaF);
//}
  private boolean gyalog(int honnanV, int honnanF, int hovaV, int hovaF) {
    int e = 1, HOVA = hovaF, HONNAN = honnanF;
    if (ALLAS[lepes].TABLA[honnanV][honnanF].szin == SOT) {
      e = -1;
      HOVA = 7 - hovaF;
      HONNAN = 7 - honnanF;
    }
    if (HOVA - HONNAN == 1 && hovaV == honnanV && ALLAS[lepes].TABLA[hovaV][hovaF].babu == URES) {
      return true;
    }
    if (HONNAN == 1 && HOVA - HONNAN == 2 && hovaV == honnanV && ALLAS[lepes].TABLA[honnanV][honnanF + e].babu == URES
            && ALLAS[lepes].TABLA[hovaV][hovaF].babu == URES) {
      return true;
    }
    return HOVA - HONNAN == 1 && ertek(hovaV - honnanV) == 1 && ALLAS[lepes].TABLA[hovaV][hovaF].szin != UR;
  }

  public boolean an_passant(int honnanV, int honnanF, int hovaV, int hovaF) {
    int HOVA = hovaF, HONNAN = honnanF;
    if (ALLAS[lepes].TABLA[honnanV][honnanF].szin == SOT) {
      HOVA = 7 - hovaF;
      HONNAN = 7 - honnanF;
    }
    if (ALLAS[lepes].babu == GY && ertek(ALLAS[lepes].hova[1] - ALLAS[lepes].honnan[1]) == 2
            && (HONNAN == 4 && HOVA == 5) && ertek(hovaV - honnanV) == 1
            && hovaV == ALLAS[lepes].honnan[0]) {
      an_pass = true;
      return true;
    }
    return false;
  }

  public boolean szabalyos(int honnanV, int honnanF, int hovaV, int hovaF) {
    switch (ALLAS[lepes].TABLA[honnanV][honnanF].babu) {
      case K:
        return kiraly(honnanV, honnanF, hovaV, hovaF);
      case V:
        return bastya(honnanV, honnanF, hovaV, hovaF) || futo(honnanV, honnanF, hovaV, hovaF);
      case F:
        return futo(honnanV, honnanF, hovaV, hovaF);
      case B:
        return bastya(honnanV, honnanF, hovaV, hovaF);
      case H:
        return huszar(honnanV, honnanF, hovaV, hovaF);
      case GY:
        return (gyalog(honnanV, honnanF, hovaV, hovaF)
                || an_passant(honnanV, honnanF, hovaV, hovaF));
      default:
        return false;
    }
  }

  public boolean sakk(int kiralyV, int kiralyF) {
    int i, j;
    for (i = 0; i < 8; i++) {
      for (j = 0; j < 8; j++) {
        if (ALLAS[lepes].TABLA[i][j].szin != UR && ALLAS[lepes].TABLA[i][j].szin != ALLAS[lepes].TABLA[kiralyV][kiralyF].szin
                && szabalyos(i, j, kiralyV, kiralyF)) {
          return true;
        }
      }

    }
    return false;
  }

  public void alapBeall() {
    lepes = 0;
    int i, j;
    for (i = 0; i < 8; i++) {
      for (j = 2; j < 6; j++) {
        ALLAS[lepes].TABLA[i][j].szin = UR;
        ALLAS[lepes].TABLA[i][j].babu = URES;
      }
    }

    for (i = 0; i < 8; i++) {
      for (j = 0; j < 2; j++) {
        ALLAS[lepes].TABLA[i][j].szin = VIL;
        if (j == 1) {
          ALLAS[lepes].TABLA[i][j].babu = GY;
        }
      }
    }

    for (i = 0; i < 8; i++) {
      for (j = 6; j < 8; j++) {
        ALLAS[lepes].TABLA[i][j].szin = SOT;
        if (j == 6) {
          ALLAS[lepes].TABLA[i][j].babu = GY;
        }
      }
    }
    ALLAS[lepes].TABLA[0][0].babu = B;
    ALLAS[lepes].TABLA[1][0].babu = H;
    ALLAS[lepes].TABLA[2][0].babu = F;
    ALLAS[lepes].TABLA[3][0].babu = V;
    ALLAS[lepes].TABLA[4][0].babu = K;
    ALLAS[lepes].TABLA[5][0].babu = F;
    ALLAS[lepes].TABLA[6][0].babu = H;
    ALLAS[lepes].TABLA[7][0].babu = B;
    ALLAS[lepes].TABLA[0][7].babu = B;
    ALLAS[lepes].TABLA[1][7].babu = H;
    ALLAS[lepes].TABLA[2][7].babu = F;
    ALLAS[lepes].TABLA[3][7].babu = V;
    ALLAS[lepes].TABLA[4][7].babu = K;
    ALLAS[lepes].TABLA[5][7].babu = F;
    ALLAS[lepes].TABLA[6][7].babu = H;
    ALLAS[lepes].TABLA[7][7].babu = B;
    ALLAS[lepes].kiraly_v[0] = 4;
    ALLAS[lepes].kiraly_v[1] = 0;
    ALLAS[lepes].kiraly_s[0] = 4;
    ALLAS[lepes].kiraly_s[1] = 7;

    ALLAS[lepes].szin = SOT;
    ALLAS[lepes].pontszam = 0;
    ALLAS[lepes].allasIsm = 0;
    kezd = true;
  }

  private boolean allasEgyez(int lepes1) {
    for (int i = 0; i < 8; i++) {
      for (int j = 0; j < 8; j++) {
        if (ALLAS[lepes1].TABLA[i][j].babu != ALLAS[lepes].TABLA[i][j].babu
                || ALLAS[lepes1].TABLA[i][j].szin != ALLAS[lepes].TABLA[i][j].szin) {
          return false;
        }
      }
    }
    return true;
  }

  public boolean allasEgyezes() {
    int kezdet = (lepes % 2 == 0) ? 0 : 1;
    for (int i = kezdet; i < lepes; i += 2) {
      if (ALLAS[i].allasIsm == 1 && allasEgyez(i)) {
        return true;
      }
    }
    return false;
  }

  public void setIsm() {
    ALLAS[lepes].allasIsm = 0;
    int kezdet = (lepes % 2 == 0) ? 0 : 1;
    for (int i = kezdet; i < lepes; i += 2) {
      if (allasEgyez(i)) {
        ALLAS[lepes].allasIsm = 1;
      }
    }
  }

  public void kov_allas(int honnanV, int honnanF, int hovaV, int hovaF) {
    int i, j;
    for (i = 0; i < 8; i++) {
      for (j = 0; j < 8; j++) {
        ALLAS[lepes + 1].TABLA[i][j].szin = ALLAS[lepes].TABLA[i][j].szin;
        ALLAS[lepes + 1].TABLA[i][j].babu = ALLAS[lepes].TABLA[i][j].babu;
      }
    }
    ALLAS[lepes + 1].kiraly_v[0] = ALLAS[lepes].kiraly_v[0];
    ALLAS[lepes + 1].kiraly_v[1] = ALLAS[lepes].kiraly_v[1];
    ALLAS[lepes + 1].kiraly_s[0] = ALLAS[lepes].kiraly_s[0];
    ALLAS[lepes + 1].kiraly_s[1] = ALLAS[lepes].kiraly_s[1];
    ALLAS[lepes + 1].vkir_lepett = ALLAS[lepes].vkir_lepett;
    ALLAS[lepes + 1].skir_lepett = ALLAS[lepes].skir_lepett;
    ALLAS[lepes + 1].Bvj_lepett = ALLAS[lepes].Bvj_lepett;
    ALLAS[lepes + 1].Bvb_lepett = ALLAS[lepes].Bvb_lepett;
    ALLAS[lepes + 1].Bsj_lepett = ALLAS[lepes].Bsj_lepett;
    ALLAS[lepes + 1].Bsb_lepett = ALLAS[lepes].Bsb_lepett;
    ALLAS[lepes + 1].v_sancolt = ALLAS[lepes].v_sancolt;
    ALLAS[lepes + 1].s_sancolt = ALLAS[lepes].s_sancolt;

    ALLAS[lepes + 1].TABLA[hovaV][hovaF].szin = ALLAS[lepes].TABLA[honnanV][honnanF].szin;
    ALLAS[lepes + 1].TABLA[hovaV][hovaF].babu = ALLAS[lepes].TABLA[honnanV][honnanF].babu;
    ALLAS[lepes + 1].TABLA[honnanV][honnanF].szin = UR;
    ALLAS[lepes + 1].TABLA[honnanV][honnanF].babu = URES;
    if (an_pass) {
      ALLAS[lepes + 1].TABLA[ALLAS[lepes].hova[0]][ALLAS[lepes].hova[1]].szin = UR;
      ALLAS[lepes + 1].TABLA[ALLAS[lepes].hova[0]][ALLAS[lepes].hova[1]].babu = URES;
    }
    ALLAS[lepes + 1].honnan[0] = honnanV;
    ALLAS[lepes + 1].honnan[1] = honnanF;
    ALLAS[lepes + 1].hova[0] = hovaV;
    ALLAS[lepes + 1].hova[1] = hovaF;
    ALLAS[lepes + 1].babu = ALLAS[lepes].TABLA[honnanV][honnanF].babu;
    ALLAS[lepes + 1].szin = ALLAS[lepes].TABLA[honnanV][honnanF].szin;

    if (ALLAS[lepes + 1].babu == K && ALLAS[lepes + 1].szin == VIL) {
      if (ertek(hovaV - honnanV) == 2) {
        ALLAS[lepes + 1].v_sancolt = true;
        if (hovaV == 6) {
          ALLAS[lepes + 1].TABLA[5][0].babu = B;
          ALLAS[lepes + 1].TABLA[5][0].szin = VIL;
          ALLAS[lepes + 1].TABLA[7][0].szin = UR;
          ALLAS[lepes + 1].TABLA[7][0].babu = URES;
        } else {
          ALLAS[lepes + 1].TABLA[3][0].babu = B;
          ALLAS[lepes + 1].TABLA[3][0].szin = VIL;
          ALLAS[lepes + 1].TABLA[0][0].szin = UR;
          ALLAS[lepes + 1].TABLA[0][0].babu = URES;
        }
      }
      ALLAS[lepes + 1].kiraly_v[0] = hovaV;
      ALLAS[lepes + 1].kiraly_v[1] = hovaF;
      ALLAS[lepes + 1].vkir_lepett = true;
    }
    if (ALLAS[lepes + 1].babu == K && ALLAS[lepes + 1].szin == SOT) {
      if (ertek(hovaV - honnanV) == 2) {
        ALLAS[lepes + 1].s_sancolt = true;
        if (hovaV == 6) {
          ALLAS[lepes + 1].TABLA[5][7].babu = B;
          ALLAS[lepes + 1].TABLA[5][7].szin = SOT;
          ALLAS[lepes + 1].TABLA[7][7].szin = UR;
          ALLAS[lepes + 1].TABLA[7][7].babu = URES;
        } else {
          ALLAS[lepes + 1].TABLA[3][7].babu = B;
          ALLAS[lepes + 1].TABLA[3][7].szin = SOT;
          ALLAS[lepes + 1].TABLA[0][7].szin = UR;
          ALLAS[lepes + 1].TABLA[0][7].babu = URES;
        }
      }
      ALLAS[lepes + 1].kiraly_s[0] = hovaV;
      ALLAS[lepes + 1].kiraly_s[1] = hovaF;
      ALLAS[lepes + 1].skir_lepett = true;
    }
    if ((honnanV == 0 && honnanF == 0) || (hovaV == 0 && hovaF == 0)) {
      ALLAS[lepes + 1].Bvb_lepett = true;
    }
    if ((honnanV == 7 && honnanF == 0) || (hovaV == 7 && hovaF == 0)) {
      ALLAS[lepes + 1].Bvj_lepett = true;
    }
    if ((honnanV == 0 && honnanF == 7) || (hovaV == 0 && hovaF == 7)) {
      ALLAS[lepes + 1].Bsb_lepett = true;
    }
    if ((honnanV == 7 && honnanF == 7) || (hovaV == 7 && hovaF == 7)) {
      ALLAS[lepes + 1].Bsj_lepett = true;
    }
    ALLAS[lepes + 1].v_sakk = false;
    ALLAS[lepes + 1].s_sakk = false;
    ALLAS[lepes + 1].patt = false;
    lepes++;
  }

  

  public void atvaltozik(int x, int y) {
    ALLAS[lepes].TABLA[x][y].babu = V;
  }

  public void sancol() {
    if ((lepes % 2 == 0 && (ALLAS[lepes].vkir_lepett || ALLAS[lepes].Bvj_lepett)) || (lepes % 2 == 1 && (ALLAS[lepes].skir_lepett || ALLAS[lepes].Bsj_lepett))
            || ALLAS[lepes].v_sakk || ALLAS[lepes].s_sakk) {
      hibaSanc = true;
    } else if (lepes % 2 == 0) {
      if (ALLAS[lepes].TABLA[6][0].babu == UR && ALLAS[lepes].TABLA[5][0].babu == URES) {
        ALLAS[lepes].TABLA[5][0].szin = VIL;
        if (sakk(5, 0)) {
          hibaSanc = true;
          ALLAS[lepes].TABLA[5][0].szin = UR;
        } else {
          ALLAS[lepes].TABLA[5][0].szin = UR;
          honnan[0] = 4;
          honnan[1] = 0;
          hova[0] = 6;
          hova[1] = 0;
        }
      } else {
        hibaSanc = true;
      }
    } //    else if (lepes%2==1){
    else {
      if (ALLAS[lepes].TABLA[6][7].babu == URES && ALLAS[lepes].TABLA[5][7].babu == URES) {
        ALLAS[lepes].TABLA[5][7].szin = SOT;
        if (sakk(5, 7)) {
          hibaSanc = true;
          ALLAS[lepes].TABLA[5][7].szin = UR;
        } else {
          ALLAS[lepes].TABLA[5][7].szin = UR;
          honnan[0] = 4;
          honnan[1] = 7;
          hova[0] = 6;
          hova[1] = 7;
        }
      } else {
        hibaSanc = true;
      }
    }
  }

  public void hosszusancol() {
    if ((lepes % 2 == 0 && (ALLAS[lepes].vkir_lepett || ALLAS[lepes].Bvb_lepett)) || (lepes % 2 == 1 && (ALLAS[lepes].skir_lepett || ALLAS[lepes].Bsb_lepett))
            || ALLAS[lepes].v_sakk || ALLAS[lepes].s_sakk) {
      hibaSanc = true;
    } else if (lepes % 2 == 0) {
      if (ALLAS[lepes].TABLA[3][0].babu == URES && ALLAS[lepes].TABLA[2][0].babu == URES
              && ALLAS[lepes].TABLA[1][0].babu == URES) {
        ALLAS[lepes].TABLA[3][0].szin = VIL;
        if (sakk(3, 0)) {
          hibaSanc = true;
          ALLAS[lepes].TABLA[3][0].szin = UR;
        } else {/*ALLAS[lepes].TABLA[4][1].babu=B;
                ALLAS[lepes].TABLA[1][1].szin=URES; ALLAS[lepes].TABLA[1][1].babu=URES;*/
          ALLAS[lepes].TABLA[3][0].szin = UR;
          honnan[0] = 4;
          honnan[1] = 0;
          hova[0] = 2;
          hova[1] = 0;
        }
      } else {
        hibaSanc = true;
      }
    } //    else if (lepes%2==1){
    else {
      if (ALLAS[lepes].TABLA[1][7].babu == URES && ALLAS[lepes].TABLA[2][7].babu == URES
              && ALLAS[lepes].TABLA[3][7].babu == URES) {
        ALLAS[lepes].TABLA[3][7].szin = SOT;
        if (sakk(3, 7)) {
          hibaSanc = true;
          ALLAS[lepes].TABLA[3][7].szin = UR;
        } else {/*ALLAS[lepes].TABLA[4][8].babu=B;
                ALLAS[lepes].TABLA[1][8].szin=URES; ALLAS[lepes].TABLA[1][8].babu=URES;*/
          ALLAS[lepes].TABLA[3][7].szin = UR;
          honnan[0] = 4;
          honnan[1] = 7;
          hova[0] = 2;
          hova[1] = 7;
        }
      } else {
        hibaSanc = true;
      }
    }
  }

  private int futo_hany(int x, int y) {
    int i, j, z = 0;
    i = x + 1;
    j = y + 1;
    while (i < 8 && j < 8 && ALLAS[lepes].TABLA[i][j].szin == UR) {
      z++;
      j++;
      i++;
    }
    if (i < 8 && j < 8 && ALLAS[lepes].TABLA[i][j].szin != ALLAS[lepes].TABLA[x][y].szin) {
      z++;
    }
    i = x + 1;
    j = y - 1;
    while (i < 8 && j >= 0 && ALLAS[lepes].TABLA[i][j].szin == UR) {
      z++;
      j--;
      i++;
    }
    if (i < 8 && j >= 0 && ALLAS[lepes].TABLA[i][j].szin != ALLAS[lepes].TABLA[x][y].szin) {
      z++;
    }
    i = x - 1;
    j = y + 1;
    while (i >= 0 && j < 8 && ALLAS[lepes].TABLA[i][j].szin == UR) {
      z++;
      j++;
      i--;
    }
    if (i >= 0 && j < 8 && ALLAS[lepes].TABLA[i][j].szin != ALLAS[lepes].TABLA[x][y].szin) {
      z++;
    }
    i = x - 1;
    j = y - 1;
    while (i >= 0 && j >= 0 && ALLAS[lepes].TABLA[i][j].szin == UR) {
      z++;
      j--;
      i--;
    }
    if (i >= 0 && j >= 0 && ALLAS[lepes].TABLA[i][j].szin != ALLAS[lepes].TABLA[x][y].szin) {
      z++;
    }
    return z;
  }

  private int[] futo_lep(int x, int y) {
    int szaml = 0;
    int[] b = new int[60];
    int i, j;
    i = x + 1;
    j = y + 1;
    while (i < 8 && j < 8 && ALLAS[lepes].TABLA[i][j].szin == UR) {
      b[szaml] = i;
      b[szaml + 30] = j;
      szaml++;
      j++;
      i++;
    }
    if (i < 8 && j < 8 && ALLAS[lepes].TABLA[i][j].szin != ALLAS[lepes].TABLA[x][y].szin) {
      b[szaml] = i;
      b[szaml + 30] = j;
      szaml++;
    }
    i = x + 1;
    j = y - 1;
    while (i < 8 && j >= 0 && ALLAS[lepes].TABLA[i][j].szin == UR) {
      b[szaml] = i;
      b[szaml + 30] = j;
      szaml++;
      j--;
      i++;
    }
    if (i < 8 && j >= 0 && ALLAS[lepes].TABLA[i][j].szin != ALLAS[lepes].TABLA[x][y].szin) {
      b[szaml] = i;
      b[szaml + 30] = j;
      szaml++;
    }
    i = x - 1;
    j = y + 1;
    while (i >= 0 && j < 8 && ALLAS[lepes].TABLA[i][j].szin == UR) {
      b[szaml] = i;
      b[szaml + 30] = j;
      szaml++;
      j++;
      i--;
    }
    if (i >= 0 && j < 8 && ALLAS[lepes].TABLA[i][j].szin != ALLAS[lepes].TABLA[x][y].szin) {
      b[szaml] = i;
      b[szaml + 30] = j;
      szaml++;
    }
    i = x - 1;
    j = y - 1;
    while (i >= 0 && j >= 0 && ALLAS[lepes].TABLA[i][j].szin == UR) {
      b[szaml] = i;
      b[szaml + 30] = j;
      szaml++;
      j--;
      i--;
    }
    if (i >= 0 && j >= 0 && ALLAS[lepes].TABLA[i][j].szin != ALLAS[lepes].TABLA[x][y].szin) {
      b[szaml] = i;
      b[szaml + 30] = j;
      szaml++;
    }
    b[szaml] = 9;
    return b;
  }

  private int bastya_hany(int x, int y) {
    int i, z = 0;
    i = x + 1;
    while (i < 8 && ALLAS[lepes].TABLA[i][y].szin == UR) {
      z++;
      i++;
    }
    if (i < 8 && ALLAS[lepes].TABLA[i][y].szin != ALLAS[lepes].TABLA[x][y].szin) {
      z++;
    }
    i = x - 1;
    while (i >= 0 && ALLAS[lepes].TABLA[i][y].szin == UR) {
      z++;
      i--;
    }
    if (i >= 0 && ALLAS[lepes].TABLA[i][y].szin != ALLAS[lepes].TABLA[x][y].szin) {
      z++;
    }
    i = y + 1;
    while (i < 8 && ALLAS[lepes].TABLA[x][i].szin == UR) {
      z++;
      i++;
    }
    if (i < 8 && ALLAS[lepes].TABLA[x][i].szin != ALLAS[lepes].TABLA[x][y].szin) {
      z++;
    }
    i = y - 1;
    while (i >= 0 && ALLAS[lepes].TABLA[x][i].szin == UR) {
      z++;
      i--;
    }
    if (i >= 0 && ALLAS[lepes].TABLA[x][i].szin != ALLAS[lepes].TABLA[x][y].szin) {
      z++;
    }
    return z;
  }

  private int[] bastya_lep(int x, int y) {
    int i;
    int szaml = 0;
    int[] b = new int[60];
    i = x + 1;
    while (i < 8 && ALLAS[lepes].TABLA[i][y].szin == UR) {
      b[szaml] = i;
      b[szaml + 30] = y;
      szaml++;
      i++;
    }
    if (i < 8 && ALLAS[lepes].TABLA[i][y].szin != ALLAS[lepes].TABLA[x][y].szin) {
      b[szaml] = i;
      b[szaml + 30] = y;
      szaml++;
    }
    i = x - 1;
    while (i >= 0 && ALLAS[lepes].TABLA[i][y].szin == UR) {
      b[szaml] = i;
      b[szaml + 30] = y;
      szaml++;
      i--;
    }
    if (i >= 0 && ALLAS[lepes].TABLA[i][y].szin != ALLAS[lepes].TABLA[x][y].szin) {
      b[szaml] = i;
      b[szaml + 30] = y;
      szaml++;
    }
    i = y + 1;
    while (i < 8 && ALLAS[lepes].TABLA[x][i].szin == UR) {
      b[szaml] = x;
      b[szaml + 30] = i;
      szaml++;
      i++;
    }
    if (i < 8 && ALLAS[lepes].TABLA[x][i].szin != ALLAS[lepes].TABLA[x][y].szin) {
      b[szaml] = x;
      b[szaml + 30] = i;
      szaml++;
    }
    i = y - 1;
    while (i >= 0 && ALLAS[lepes].TABLA[x][i].szin == UR) {
      b[szaml] = x;
      b[szaml + 30] = i;
      szaml++;
      i--;
    }
    if (i >= 0 && ALLAS[lepes].TABLA[x][i].szin != ALLAS[lepes].TABLA[x][y].szin) {
      b[szaml] = x;
      b[szaml + 30] = i;
      szaml++;
    }
    b[szaml] = 9;
    return b;
  }

  private int[] vezer_lep(int x, int y) {
    int szaml = 0;
    int[] b = new int[60];
    int i, j;
    i = x + 1;
    j = y + 1;
    while (i < 8 && j < 8 && ALLAS[lepes].TABLA[i][j].szin == UR) {
      b[szaml] = i;
      b[szaml + 30] = j;
      szaml++;
      j++;
      i++;
    }
    if (i < 8 && j < 8 && ALLAS[lepes].TABLA[i][j].szin != ALLAS[lepes].TABLA[x][y].szin) {
      b[szaml] = i;
      b[szaml + 30] = j;
      szaml++;
    }
    i = x + 1;
    j = y - 1;
    while (i < 8 && j >= 0 && ALLAS[lepes].TABLA[i][j].szin == UR) {
      b[szaml] = i;
      b[szaml + 30] = j;
      szaml++;
      j--;
      i++;
    }
    if (i < 8 && j >= 0 && ALLAS[lepes].TABLA[i][j].szin != ALLAS[lepes].TABLA[x][y].szin) {
      b[szaml] = i;
      b[szaml + 30] = j;
      szaml++;
    }
    i = x - 1;
    j = y + 1;
    while (i >= 0 && j < 8 && ALLAS[lepes].TABLA[i][j].szin == UR) {
      b[szaml] = i;
      b[szaml + 30] = j;
      szaml++;
      j++;
      i--;
    }
    if (i >= 0 && j < 8 && ALLAS[lepes].TABLA[i][j].szin != ALLAS[lepes].TABLA[x][y].szin) {
      b[szaml] = i;
      b[szaml + 30] = j;
      szaml++;
    }
    i = x - 1;
    j = y - 1;
    while (i >= 0 && j >= 0 && ALLAS[lepes].TABLA[i][j].szin == UR) {
      b[szaml] = i;
      b[szaml + 30] = j;
      szaml++;
      j--;
      i--;
    }
    if (i >= 0 && j >= 0 && ALLAS[lepes].TABLA[i][j].szin != ALLAS[lepes].TABLA[x][y].szin) {
      b[szaml] = i;
      b[szaml + 30] = j;
      szaml++;
    }

    i = x + 1;
    while (i < 8 && ALLAS[lepes].TABLA[i][y].szin == UR) {
      b[szaml] = i;
      b[szaml + 30] = y;
      szaml++;
      i++;
    }
    if (i < 8 && ALLAS[lepes].TABLA[i][y].szin != ALLAS[lepes].TABLA[x][y].szin) {
      b[szaml] = i;
      b[szaml + 30] = y;
      szaml++;
    }
    i = x - 1;
    while (i >= 0 && ALLAS[lepes].TABLA[i][y].szin == UR) {
      b[szaml] = i;
      b[szaml + 30] = y;
      szaml++;
      i--;
    }
    if (i >= 0 && ALLAS[lepes].TABLA[i][y].szin != ALLAS[lepes].TABLA[x][y].szin) {
      b[szaml] = i;
      b[szaml + 30] = y;
      szaml++;
    }
    i = y + 1;
    while (i < 8 && ALLAS[lepes].TABLA[x][i].szin == UR) {
      b[szaml] = x;
      b[szaml + 30] = i;
      szaml++;
      i++;
    }
    if (i < 8 && ALLAS[lepes].TABLA[x][i].szin != ALLAS[lepes].TABLA[x][y].szin) {
      b[szaml] = x;
      b[szaml + 30] = i;
      szaml++;
    }
    i = y - 1;
    while (i >= 0 && ALLAS[lepes].TABLA[x][i].szin == UR) {
      b[szaml] = x;
      b[szaml + 30] = i;
      szaml++;
      i--;
    }
    if (i >= 0 && ALLAS[lepes].TABLA[x][i].szin != ALLAS[lepes].TABLA[x][y].szin) {
      b[szaml] = x;
      b[szaml + 30] = i;
      szaml++;
    }
    b[szaml] = 9;
    return b;
  }

  private int huszar_hany(int x, int y) {
    int i = 0;
    if (x + 2 < 8 && y + 1 < 8 && ALLAS[lepes].TABLA[x + 2][y + 1].szin != ALLAS[lepes].TABLA[x][y].szin) {
      i++;
    }
    if (x + 2 < 8 && y - 1 >= 0 && ALLAS[lepes].TABLA[x + 2][y - 1].szin != ALLAS[lepes].TABLA[x][y].szin) {
      i++;
    }
    if (x - 2 >= 0 && y + 1 < 8 && ALLAS[lepes].TABLA[x - 2][y + 1].szin != ALLAS[lepes].TABLA[x][y].szin) {
      i++;
    }
    if (x - 2 >= 0 && y - 1 >= 0 && ALLAS[lepes].TABLA[x - 2][y - 1].szin != ALLAS[lepes].TABLA[x][y].szin) {
      i++;
    }
    if (x + 1 < 8 && y + 2 < 8 && ALLAS[lepes].TABLA[x + 1][y + 2].szin != ALLAS[lepes].TABLA[x][y].szin) {
      i++;
    }
    if (x + 1 < 8 && y - 2 >= 0 && ALLAS[lepes].TABLA[x + 1][y - 2].szin != ALLAS[lepes].TABLA[x][y].szin) {
      i++;
    }
    if (x - 1 >= 0 && y + 2 < 8 && ALLAS[lepes].TABLA[x - 1][y + 2].szin != ALLAS[lepes].TABLA[x][y].szin) {
      i++;
    }
    if (x - 1 >= 0 && y - 2 >= 0 && ALLAS[lepes].TABLA[x - 1][y - 2].szin != ALLAS[lepes].TABLA[x][y].szin) {
      i++;
    }
    return i;
  }

  private int[] huszar_lep(int x, int y) {
    int szaml = 0;
    int[] b = new int[60];
    if (x + 2 < 8 && y + 1 < 8 && ALLAS[lepes].TABLA[x + 2][y + 1].szin != ALLAS[lepes].TABLA[x][y].szin) {
      b[szaml] = x + 2;
      b[szaml + 30] = y + 1;
      szaml++;
    }
    if (x + 2 < 8 && y - 1 >= 0 && ALLAS[lepes].TABLA[x + 2][y - 1].szin != ALLAS[lepes].TABLA[x][y].szin) {
      b[szaml] = x + 2;
      b[szaml + 30] = y - 1;
      szaml++;
    }
    if (x - 2 >= 0 && y + 1 < 8 && ALLAS[lepes].TABLA[x - 2][y + 1].szin != ALLAS[lepes].TABLA[x][y].szin) {
      b[szaml] = x - 2;
      b[szaml + 30] = y + 1;
      szaml++;
    }
    if (x - 2 >= 0 && y - 1 >= 0 && ALLAS[lepes].TABLA[x - 2][y - 1].szin != ALLAS[lepes].TABLA[x][y].szin) {
      b[szaml] = x - 2;
      b[szaml + 30] = y - 1;
      szaml++;
    }
    if (x + 1 < 8 && y + 2 < 8 && ALLAS[lepes].TABLA[x + 1][y + 2].szin != ALLAS[lepes].TABLA[x][y].szin) {
      b[szaml] = x + 1;
      b[szaml + 30] = y + 2;
      szaml++;
    }
    if (x + 1 < 8 && y - 2 >= 0 && ALLAS[lepes].TABLA[x + 1][y - 2].szin != ALLAS[lepes].TABLA[x][y].szin) {
      b[szaml] = x + 1;
      b[szaml + 30] = y - 2;
      szaml++;
    }
    if (x - 1 >= 0 && y + 2 < 8 && ALLAS[lepes].TABLA[x - 1][y + 2].szin != ALLAS[lepes].TABLA[x][y].szin) {
      b[szaml] = x - 1;
      b[szaml + 30] = y + 2;
      szaml++;
    }
    if (x - 1 >= 0 && y - 2 >= 0 && ALLAS[lepes].TABLA[x - 1][y - 2].szin != ALLAS[lepes].TABLA[x][y].szin) {
      b[szaml] = x - 1;
      b[szaml + 30] = y - 2;
      szaml++;
    }
    b[szaml] = 9;
    return b;
  }

  private int kiraly_hany(int x, int y) {
    int i = 0;
    if (x + 1 < 8 && y + 1 < 8 && ALLAS[lepes].TABLA[x + 1][y + 1].szin != ALLAS[lepes].TABLA[x][y].szin) {
      i++;
    }
    if (x + 1 < 8 && y - 1 >= 0 && ALLAS[lepes].TABLA[x + 1][y - 1].szin != ALLAS[lepes].TABLA[x][y].szin) {
      i++;
    }
    if (x - 1 >= 0 && y + 1 < 8 && ALLAS[lepes].TABLA[x - 1][y + 1].szin != ALLAS[lepes].TABLA[x][y].szin) {
      i++;
    }
    if (x - 1 >= 0 && y - 1 >= 0 && ALLAS[lepes].TABLA[x - 1][y - 1].szin != ALLAS[lepes].TABLA[x][y].szin) {
      i++;
    }
    if (x + 1 < 8 && ALLAS[lepes].TABLA[x + 1][y].szin != ALLAS[lepes].TABLA[x][y].szin) {
      i++;
    }
    if (y - 1 >= 0 && ALLAS[lepes].TABLA[x][y - 1].szin != ALLAS[lepes].TABLA[x][y].szin) {
      i++;
    }
    if (x - 1 >= 0 && ALLAS[lepes].TABLA[x - 1][y].szin != ALLAS[lepes].TABLA[x][y].szin) {
      i++;
    }
    if (y + 1 < 8 && ALLAS[lepes].TABLA[x][y + 1].szin != ALLAS[lepes].TABLA[x][y].szin) {
      i++;
    }
    return i;
  }

  private int[] kiraly_lep(int x, int y) {
    int szaml = 0;
    int[] b = new int[60];
    if (x + 1 < 8 && y + 1 < 8 && ALLAS[lepes].TABLA[x + 1][y + 1].szin != ALLAS[lepes].TABLA[x][y].szin) {
      b[szaml] = x + 1;
      b[szaml + 30] = y + 1;
      szaml++;
    }
    if (x + 1 < 8 && y - 1 >= 0 && ALLAS[lepes].TABLA[x + 1][y - 1].szin != ALLAS[lepes].TABLA[x][y].szin) {
      b[szaml] = x + 1;
      b[szaml + 30] = y - 1;
      szaml++;
    }
    if (x - 1 >= 0 && y + 1 < 8 && ALLAS[lepes].TABLA[x - 1][y + 1].szin != ALLAS[lepes].TABLA[x][y].szin) {
      b[szaml] = x - 1;
      b[szaml + 30] = y + 1;
      szaml++;
    }
    if (x - 1 >= 0 && y - 1 >= 0 && ALLAS[lepes].TABLA[x - 1][y - 1].szin != ALLAS[lepes].TABLA[x][y].szin) {
      b[szaml] = x - 1;
      b[szaml + 30] = y - 1;
      szaml++;
    }
    if (x + 1 < 8 && ALLAS[lepes].TABLA[x + 1][y].szin != ALLAS[lepes].TABLA[x][y].szin) {
      b[szaml] = x + 1;
      b[szaml + 30] = y;
      szaml++;
    }
    if (y - 1 >= 0 && ALLAS[lepes].TABLA[x][y - 1].szin != ALLAS[lepes].TABLA[x][y].szin) {
      b[szaml] = x;
      b[szaml + 30] = y - 1;
      szaml++;
    }
    if (x - 1 >= 0 && ALLAS[lepes].TABLA[x - 1][y].szin != ALLAS[lepes].TABLA[x][y].szin) {
      b[szaml] = x - 1;
      b[szaml + 30] = y;
      szaml++;
    }
    if (y + 1 < 8 && ALLAS[lepes].TABLA[x][y + 1].szin != ALLAS[lepes].TABLA[x][y].szin) {
      b[szaml] = x;
      b[szaml + 30] = y + 1;
      szaml++;
    }
    b[szaml] = 9;
    return b;
  }

  private int[] gyalog_lep(int x, int y) {
    int szaml = 0;
    int[] b = new int[60];
    int e = 1, i = y;
    if (ALLAS[lepes].TABLA[x][y].szin == SOT) {
      e = -1;
      i = 7 - y;
    }
    if (ALLAS[lepes].TABLA[x][y + e].szin == UR) {
      b[szaml] = x;
      b[szaml + 30] = y + e;
      szaml++;
      if (i == 1 && ALLAS[lepes].TABLA[x][y + 2 * e].szin == UR) {
        b[szaml] = x;
        b[szaml + 30] = y + 2 * e;
        szaml++;
      }
    }
    if (x > 0 && ALLAS[lepes].TABLA[x - 1][y + e].szin == -e) {
      b[szaml] = x - 1;
      b[szaml + 30] = y + e;
      szaml++;
    }
    if (x < 7 && ALLAS[lepes].TABLA[x + 1][y + e].szin == -e) {
      b[szaml] = x + 1;
      b[szaml + 30] = y + e;
      szaml++;
    }
    b[szaml] = 9;
    return b;
  }

  private int[] an_pass_lep(int x, int y) {
    int e = 1;
    int i = y;
    int[] b = new int[2];
    if (ALLAS[lepes].TABLA[x][y].szin == SOT) {
      e = -1;
      i = 7 - y;
    }
    if (i == 4 && ALLAS[lepes].babu == GY && ertek(ALLAS[lepes].hova[1] - ALLAS[lepes].honnan[1]) == 2) {
      if (x > 0 && ALLAS[lepes].honnan[0] == x - 1) {
        b[0] = x - 1;
        b[1] = y + e;
        return b;
      }
      if (x < 7 && ALLAS[lepes].honnan[0] == x + 1) {
        b[0] = x + 1;
        b[1] = y + e;
        return b;
      }
    }
    b[0] = 9;
    return b;
  }

  private int futo_hanyfh(int x, int y) {
    int i, j, z = 0;
    i = x + 1;
    j = y + 1;
    while (i < 8 && j < 6 && ALLAS[lepes].TABLA[i][j].szin == UR) {
      z++;
      j++;
      i++;
    }
    if (i < 8 && j < 6 && ALLAS[lepes].TABLA[i][j].szin != ALLAS[lepes].TABLA[x][y].szin) {
      z++;
    }
    i = x + 1;
    j = y - 1;
    while (i < 8 && j > 1 && ALLAS[lepes].TABLA[i][j].szin == UR) {
      z++;
      j--;
      i++;
    }
    if (i < 8 && j > 1 && ALLAS[lepes].TABLA[i][j].szin != ALLAS[lepes].TABLA[x][y].szin) {
      z++;
    }
    i = x - 1;
    j = y + 1;
    while (i > 0 && j < 6 && ALLAS[lepes].TABLA[i][j].szin == UR) {
      z++;
      j++;
      i--;
    }
    if (i >= 0 && j < 6 && ALLAS[lepes].TABLA[i][j].szin != ALLAS[lepes].TABLA[x][y].szin) {
      z++;
    }
    i = x - 1;
    j = y - 1;
    while (i >= 0 && j > 1 && ALLAS[lepes].TABLA[i][j].szin == UR) {
      z++;
      j++;
      i++;
    }
    if (i >= 0 && j > 1 && ALLAS[lepes].TABLA[i][j].szin != ALLAS[lepes].TABLA[x][y].szin) {
      z++;
    }
    return z;
  }

  private int huszar_hanyfh(int x, int y) {
    if (y != 3 && y != 4) {
      int i = 0;
      if (x < 6 && y < 6 && ALLAS[lepes].TABLA[x + 2][y + 1].szin != ALLAS[lepes].TABLA[x][y].szin) {
        i++;
      }
      if (x < 6 && y > 1 && ALLAS[lepes].TABLA[x + 2][y - 1].szin != ALLAS[lepes].TABLA[x][y].szin) {
        i++;
      }
      if (x > 1 && y < 6 && ALLAS[lepes].TABLA[x - 2][y + 1].szin != ALLAS[lepes].TABLA[x][y].szin) {
        i++;
      }
      if (x > 1 && y > 1 && ALLAS[lepes].TABLA[x - 2][y - 1].szin != ALLAS[lepes].TABLA[x][y].szin) {
        i++;
      }
      if (x < 7 && y < 5 && ALLAS[lepes].TABLA[x + 1][y + 2].szin != ALLAS[lepes].TABLA[x][y].szin) {
        i++;
      }
      if (x < 7 && y > 2 && ALLAS[lepes].TABLA[x + 1][y - 2].szin != ALLAS[lepes].TABLA[x][y].szin) {
        i++;
      }
      if (x > 0 && y < 5 && ALLAS[lepes].TABLA[x - 1][y + 2].szin != ALLAS[lepes].TABLA[x][y].szin) {
        i++;
      }
      if (x > 0 && y > 2 && ALLAS[lepes].TABLA[x - 1][y - 2].szin != ALLAS[lepes].TABLA[x][y].szin) {
        i++;
      }
      return i;
    } else {
      return 0;
    }
  }

  private int hanyszabalyosfh(int x, int y) {
    switch (ALLAS[lepes].TABLA[x][y].babu) {
      case F:
        return 10 * futo_hanyfh(x, y);
      case H:
        return 10 * huszar_hanyfh(x, y);
      default:
        return 0;
    }
  }

  private int hanyszabalyos(int x, int y) {
    switch (ALLAS[lepes].TABLA[x][y].babu) {
      case V:
        return 10 * (futo_hany(x, y) + bastya_hany(x, y));
      case B:
        return 10 * bastya_hany(x, y);
      case F:
        return 10 * futo_hany(x, y);
      case H:
        return 10 * huszar_hany(x, y);
      case K:
        return 10 * kiraly_hany(x, y);
      default:
        return 0;
    }
  }

  private int babertek(int c) {
    switch (c) {
      case V:
        return 1400;
      case B:
        return 750;
      case F:
        return 500;
      case H:
        return 480;
      case GY:
        return 150;
      default:
        return 0;
    }
  }

  public int hanybab_f() {
// nohanyszab=false;
    int i, j, z, x = 0, y = 0, s = 0;
    for (i = 0; i < 8; i++) {
      for (j = 0; j < 8; j++) {
        if (ALLAS[lepes].TABLA[i][j].babu != URES) {
          x++;
          y += babertek(ALLAS[lepes].TABLA[i][j].babu);
          if (ALLAS[lepes].TABLA[i][j].babu > 2) {
            s++;
          }
        }
      }
    }
    z = erosseg;
    if (x < 6 || y < 2510) {
      z++;
//            nohanyszab=true;
    }
    if (x < 4 || y < 1110) {
      z++;
    }
    if (s == 0) {
      z++;
    }

    return z;
  }

  private int[] allas_lep(int x, int y) {

    switch (ALLAS[lepes].TABLA[x][y].babu) {
      case V:
        return vezer_lep(x, y);
      case B:
        return bastya_lep(x, y);
      case F:
        return futo_lep(x, y);
      case H:
        return huszar_lep(x, y);
      case K:
        return kiraly_lep(x, y);
      //case GY: return gyalog_lep(x,y);//***************************
      default:
        return gyalog_lep(x, y);
    }

  }

  public boolean patt_e() {
    int i, j;
    for (i = 0; i < 8; i++) {
      for (j = 0; j < 8; j++) {
        if (ALLAS[lepes].szin * -1 == ALLAS[lepes].TABLA[i][j].szin) {
          int l;
          int[] hovab;//int hovac[60];
          hovab = allas_lep(i, j);
          l = 0;
          while (hovab[l] != 9) {
            an_pass = false;

            kov_allas(i, j, hovab[l], hovab[l + 30]);
            if (lepes % 2 == 0) {
              kir[0] = ALLAS[lepes].kiraly_s[0];
              kir[1] = ALLAS[lepes].kiraly_s[1];
            } else {
              kir[0] = ALLAS[lepes].kiraly_v[0];
              kir[1] = ALLAS[lepes].kiraly_v[1];
            }
            if (!sakk(kir[0], kir[1])) {
              lepes--;
              return false;
            } else {
              lepes--;
            }
            l++;
          }
          if (ALLAS[lepes].TABLA[i][j].babu == GY) {
            hovab = an_pass_lep(i, j);
            if (hovab[0] != 9) {
              an_pass = true;
              kov_allas(i, j, hovab[0], hovab[1]);
              if (lepes % 2 == 0) {
                kir[0] = ALLAS[lepes].kiraly_s[0];
                kir[1] = ALLAS[lepes].kiraly_s[1];
              } else {
                kir[0] = ALLAS[lepes].kiraly_v[0];
                kir[1] = ALLAS[lepes].kiraly_v[1];
              }
              if (!sakk(kir[0], kir[1])) {
                lepes--;
                return false;
              } else {
                lepes--;
              }
            }
          }
        }
      }
    }
    return true;
  }

  private int allasertek() {
    //folyamatosan alakítható tesztjátszmák alapján:
    int i, j, bab = 0;
    /*    if (nohanyszab){
       for (i=1; i<=8; i++){
        for (j=1; j<=8;j++){
                if (ALLAS[lepes].TABLA[i][j].babu>2)
            bab+=(babertek(ALLAS[lepes].TABLA[i][j].babu)*ALLAS[lepes].TABLA[i][j].szin);
            else if (ALLAS[lepes].TABLA[i][j].babu==GY)
            bab+=(15*ALLAS[lepes].TABLA[i][j].szin+2*j-9);
        }
    }
    if (ALLAS[lepes].v_sakk) bab-=3;
    else if (ALLAS[lepes].s_sakk) bab+=3;
    return bab;
    }

    else*/ if (lepes < 60) {
      if (lepes < 19) {  //*** megnyitás ***
        for (i = 0; i < 8; i++) {
          int sgy = 0, vgy = 0;
          for (j = 0; j < 8; j++) {
            if (ALLAS[lepes].TABLA[i][j].babu != UR) {
              bab += (babertek(ALLAS[lepes].TABLA[i][j].babu) + bab_poz(j, i)) * ALLAS[lepes].TABLA[i][j].szin;

              if (ALLAS[lepes].TABLA[i][j].babu > 2) {
                bab += (hanyszabalyos(i, j) * ALLAS[lepes].TABLA[i][j].szin);
              }
//            if (j>0 && j<7 && ALLAS[lepes].TABLA[i][j].babu>4) bab+=(hanyszabalyosfh(i, j)*ALLAS[lepes].TABLA[i][j].szin);
              if (ALLAS[lepes].TABLA[i][j].babu == GY) {
                if (ALLAS[lepes].TABLA[i][j].szin == VIL) {
                  vgy++;
                } else {
                  sgy++;
                }
//            if (i>2 && i<5 && j>1 && j<6){
//                bab+=2*ALLAS[lepes].TABLA[i][j].szin;
//                if (j>2 && j<5 && i>2 && i<5)bab+=80*ALLAS[lepes].TABLA[i][j].szin;
//            }
              }
            }
          }
          if (vgy > 1) {
            bab -= 20;
            if (vgy > 2) {
              bab -= 60;
            }
          }
          if (sgy > 1) {
            bab += 20;
            if (vgy > 2) {
              bab += 60;
            }
          }
        }
        if (ALLAS[lepes].v_sancolt) {
          bab += 50;
        }
        if (ALLAS[lepes].s_sancolt) {
          bab -= 50;
        }
      } else {//lepes>=19  *** középjáték ***
        for (i = 0; i < 8; i++) {
          int sgy = 0, vgy = 0;
          for (j = 0; j < 8; j++) {
            if (ALLAS[lepes].TABLA[i][j].babu != URES) {
              bab += (babertek(ALLAS[lepes].TABLA[i][j].babu) * ALLAS[lepes].TABLA[i][j].szin);
            }
            if (ALLAS[lepes].TABLA[i][j].babu > 2) {
              bab += (hanyszabalyos(i, j) * ALLAS[lepes].TABLA[i][j].szin);
            }
            if (ALLAS[lepes].TABLA[i][j].babu == GY) {
              if (ALLAS[lepes].TABLA[i][j].szin == VIL) {
                vgy++;
              } else {
                sgy++;
              }
            }
          }
          if (vgy > 1) {
            bab -= 20;
            if (vgy > 2) {
              bab -= 60;
            }
          }
          if (sgy > 1) {
            bab += 20;
            if (vgy > 2) {
              bab += 60;
            }
          }
        }
        if (ALLAS[lepes].v_sancolt) {
          bab += 30;
        }
        if (ALLAS[lepes].s_sancolt) {
          bab -= 30;
        }
        if (ALLAS[lepes].v_sakk) {
          bab -= 50;
        }
        if (ALLAS[lepes].s_sakk) {
          bab += 50;
        }
      }
      //      return bab;
    } else {
      for (i = 0; i < 8; i++) {
        for (j = 0; j < 8; j++) {
          if (ALLAS[lepes].TABLA[i][j].babu > 2) {
            bab += (babertek(ALLAS[lepes].TABLA[i][j].babu) * ALLAS[lepes].TABLA[i][j].szin);
            bab += (hanyszabalyos(i, j) * ALLAS[lepes].TABLA[i][j].szin);
          } else if (ALLAS[lepes].TABLA[i][j].babu == GY) {
            bab += (150 * ALLAS[lepes].TABLA[i][j].szin + 20 * j - 70);
          } else if (ALLAS[lepes].TABLA[i][j].babu == K) {
            bab += (hanyszabalyos(i, j) * ALLAS[lepes].TABLA[i][j].szin);
          }
        }
      }
      if (ALLAS[lepes].v_sakk) {
        bab -= 30;
      } else if (ALLAS[lepes].s_sakk) {
        bab += 30;
      }
//    return bab;
    }
    int x = ALLAS[lepes].hova[0];
    int y = ALLAS[lepes].hova[1];
    if (ALLAS[lepes - 1].TABLA[x][y].szin == -ALLAS[lepes].szin) {
      if (sakk(x, y)) {
        bab -= babertek(ALLAS[lepes].babu) * ALLAS[lepes].szin;
      }
    }
    return bab;
  }

  private int vegallas() {
    int e = 1;
    if (lepes % 2 == 1) {
      e = -1;
    }
    int veg = e * -10010, csere;

    hibaSanc = false;
    sancol();
    if (!hibaSanc) {
      kov_allas(honnan[0], honnan[1], hova[0], hova[1]);
      if (lepes % 2 == 0) {
        kir[0] = ALLAS[lepes].kiraly_s[0];
        ellKir[0] = ALLAS[lepes].kiraly_v[0];
        kir[1] = ALLAS[lepes].kiraly_s[1];
        ellKir[1] = ALLAS[lepes].kiraly_v[1];
      } else {
        kir[0] = ALLAS[lepes].kiraly_v[0];
        ellKir[0] = ALLAS[lepes].kiraly_s[0];
        kir[1] = ALLAS[lepes].kiraly_v[1];
        ellKir[1] = ALLAS[lepes].kiraly_s[1];
      }
      if (!sakk(kir[0], kir[1])) {
        if (sakk(ellKir[0], ellKir[1])) {
          if (ALLAS[lepes].szin == VIL) {
            ALLAS[lepes].s_sakk = true;
          } else {
            ALLAS[lepes].v_sakk = true;
          }
        }
        if (patt_e()) {
          if (ALLAS[lepes].s_sakk) {
            lepes--;
            return 10000;
          } else if (ALLAS[lepes].v_sakk) {
            lepes--;
            return -10000;
          } else {
            csere = 0;
          }
        } else {
          csere = allasertek();
        }

        if (csere * e > veg * e) {
          veg = csere;
          lepes--;
//            if (veg*e==1000){
//                return veg;
//            }//@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
        } else {
          lepes--;
        }
      } else {
        lepes--;
      }
    }

    hibaSanc = false;
    hosszusancol();
    if (!hibaSanc) {
      kov_allas(honnan[0], honnan[1], hova[0], hova[1]);
      if (lepes % 2 == 0) {
        kir[0] = ALLAS[lepes].kiraly_s[0];
        ellKir[0] = ALLAS[lepes].kiraly_v[0];
        kir[1] = ALLAS[lepes].kiraly_s[1];
        ellKir[1] = ALLAS[lepes].kiraly_v[1];
      } else {
        kir[0] = ALLAS[lepes].kiraly_v[0];
        ellKir[0] = ALLAS[lepes].kiraly_s[0];
        kir[1] = ALLAS[lepes].kiraly_v[1];
        ellKir[1] = ALLAS[lepes].kiraly_s[1];
      }
      if (!sakk(kir[0], kir[1])) {
        if (sakk(ellKir[0], ellKir[1])) {
          if (ALLAS[lepes].szin == VIL) {
            ALLAS[lepes].s_sakk = true;
          } else {
            ALLAS[lepes].v_sakk = true;
          }
        }
        if (patt_e()) {
          if (ALLAS[lepes].s_sakk) {
            lepes--;
            return 10000;
          } else if (ALLAS[lepes].v_sakk) {
            lepes--;
            return -10000;
          } else {
            csere = 0;
          }
        } else {
          csere = allasertek();
        }

        if (csere * e > veg * e) {
          veg = csere;
          lepes--;
//            if (veg*e==10000){
//                return veg;
//            }
        } else {
          lepes--;
        }
      } else {
        lepes--;
      }
    }

    int i, j;
    for (i = 0; i < 8; i++) {
      for (j = 0; j < 8; j++) {
        if (ALLAS[lepes].TABLA[i][j].szin == e) {
          //allas_lep(i,j);
          int l;
          int[] hovab;//int hovac[60];
          hovab = allas_lep(i, j);
          l = 0;
          while (hovab[l] != 9) {//hovac[l]=hovab[l];hovac[l+30]=hovab[l+30];l++;}
//
//            for (k=0;k<l;k++){
            an_pass = false;
            kov_allas(i, j, hovab[l], hovab[l + 30]);
            if (lepes % 2 == 0) {
              kir[0] = ALLAS[lepes].kiraly_s[0];
              ellKir[0] = ALLAS[lepes].kiraly_v[0];
              kir[1] = ALLAS[lepes].kiraly_s[1];
              ellKir[1] = ALLAS[lepes].kiraly_v[1];
            } else {
              kir[0] = ALLAS[lepes].kiraly_v[0];
              ellKir[0] = ALLAS[lepes].kiraly_s[0];
              kir[1] = ALLAS[lepes].kiraly_v[1];
              ellKir[1] = ALLAS[lepes].kiraly_s[1];
            }
            if (!sakk(kir[0], kir[1])) {
              if (ALLAS[lepes].babu == GY && (ALLAS[lepes].hova[1] == 0 || ALLAS[lepes].hova[1] == 7)) {
                atvaltozik(ALLAS[lepes].hova[0], ALLAS[lepes].hova[1]);
              }

              if (sakk(ellKir[0], ellKir[1])) {
                if (ALLAS[lepes].szin == VIL) {
                  ALLAS[lepes].s_sakk = true;
                } else {
                  ALLAS[lepes].v_sakk = true;
                }
              }
              if (patt_e()) {
                if (ALLAS[lepes].s_sakk) {
                  lepes--;
                  return 10000;
                } else if (ALLAS[lepes].v_sakk) {
                  lepes--;
                  return -10000;
                } else {
                  csere = 0;
                }
              } else if (allasEgyezes()) {
                csere = 0;
              } else {
                csere = allasertek();
              }
              if (csere * e > veg * e) {
                veg = csere;
                lepes--;
//            if (veg*e==1000) return veg;
              } else {
                lepes--;
              }
            } else {
              lepes--;
            }
            l++;
          }

          if (ALLAS[lepes].TABLA[i][j].babu == GY) {
            hovab = an_pass_lep(i, j);
            //hovac[0]=hovab[0];hovac[1]=hovab[1];
            if (hovab[0] != 9) {
              an_pass = true;
              kov_allas(i, j, hovab[0], hovab[1]);
              if (lepes % 2 == 0) {
                kir[0] = ALLAS[lepes].kiraly_s[0];
                ellKir[0] = ALLAS[lepes].kiraly_v[0];
                kir[1] = ALLAS[lepes].kiraly_s[1];
                ellKir[1] = ALLAS[lepes].kiraly_v[1];
              } else {
                kir[0] = ALLAS[lepes].kiraly_v[0];
                ellKir[0] = ALLAS[lepes].kiraly_s[0];
                kir[1] = ALLAS[lepes].kiraly_v[1];
                ellKir[1] = ALLAS[lepes].kiraly_s[1];
              }
              if (!sakk(kir[0], kir[1])) {
                //if (ALLAS[lepes].babu==GY && (ALLAS[lepes].hova[1]==0 || ALLAS[lepes].hova[1]==7)) {atvaltozik(ALLAS[lepes].hova[0], ALLAS[lepes].hova[1]);}
                if (ALLAS[lepes].hova[1] == 0 || ALLAS[lepes].hova[1] == 7) {
                  atvaltozik(ALLAS[lepes].hova[0], ALLAS[lepes].hova[1]);
                }

                if (sakk(ellKir[0], ellKir[1])) {
                  if (ALLAS[lepes].szin == VIL) {
                    ALLAS[lepes].s_sakk = true;
                  } else {
                    ALLAS[lepes].v_sakk = true;
                  }
                }
                if (patt_e()) {
                  if (ALLAS[lepes].s_sakk) {
                    lepes--;
                    return 10000;
                  } else if (ALLAS[lepes].v_sakk) {
                    lepes--;
                    return -10000;
                  } else {
                    csere = 0;
                  }
                } else {
                  csere = allasertek();
                }
                if (csere * e > veg * e) {
                  veg = csere;
                  lepes--;
//            if (veg*e==1000) return veg;
                } else {
                  lepes--;
                }
              } else {
                lepes--;
              }
            }
          }
        }
      }
    }
    return veg;
  }

  private int kozepallas(int f) {
    f--;
    if (f == 0) {
      return vegallas();
    }

    int e = 1;
    if (lepes % 2 == 1) {
      e = -1;
    }
    int veg = e * -10010, csere;
    hibaSanc = false;
    sancol();
    if (!hibaSanc) {
      kov_allas(honnan[0], honnan[1], hova[0], hova[1]);
      if (lepes % 2 == 0) {
        kir[0] = ALLAS[lepes].kiraly_s[0];
        ellKir[0] = ALLAS[lepes].kiraly_v[0];
        kir[1] = ALLAS[lepes].kiraly_s[1];
        ellKir[1] = ALLAS[lepes].kiraly_v[1];
      } else {
        kir[0] = ALLAS[lepes].kiraly_v[0];
        ellKir[0] = ALLAS[lepes].kiraly_s[0];
        kir[1] = ALLAS[lepes].kiraly_v[1];
        ellKir[1] = ALLAS[lepes].kiraly_s[1];
      }
      if (!sakk(kir[0], kir[1])) {
        if (sakk(ellKir[0], ellKir[1])) {
          if (ALLAS[lepes].szin == VIL) {
            ALLAS[lepes].s_sakk = true;
          } else {
            ALLAS[lepes].v_sakk = true;
          }
        }
        if (patt_e()) {
          if (ALLAS[lepes].s_sakk) {
            lepes--;
            return 10000 + f;
          } else if (ALLAS[lepes].v_sakk) {
            lepes--;
            return -10000 - f;
          } else {
            csere = 0;
          }
        } else {
          csere = kozepallas(f);
        }

        if (csere * e > veg * e) {
          veg = csere;
          lepes--;
//            if (veg*e==1000){
//                return veg;
//            }//@@@@@@@@@@@@@@@@@@@@@@
        } else {
          lepes--;
        }
      } else {
        lepes--;
      }
    }

    hibaSanc = false;
    hosszusancol();
    if (!hibaSanc) {
      kov_allas(honnan[0], honnan[1], hova[0], hova[1]);
      if (lepes % 2 == 0) {
        kir[0] = ALLAS[lepes].kiraly_s[0];
        ellKir[0] = ALLAS[lepes].kiraly_v[0];
        kir[1] = ALLAS[lepes].kiraly_s[1];
        ellKir[1] = ALLAS[lepes].kiraly_v[1];
      } else {
        kir[0] = ALLAS[lepes].kiraly_v[0];
        ellKir[0] = ALLAS[lepes].kiraly_s[0];
        kir[1] = ALLAS[lepes].kiraly_v[1];
        ellKir[1] = ALLAS[lepes].kiraly_s[1];
      }
      if (!sakk(kir[0], kir[1])) {
        if (sakk(ellKir[0], ellKir[1])) {
          if (ALLAS[lepes].szin == VIL) {
            ALLAS[lepes].s_sakk = true;
          } else {
            ALLAS[lepes].v_sakk = true;
          }
        }
        if (patt_e()) {
          if (ALLAS[lepes].s_sakk) {
            lepes--;
            return 10000 + f;
          } else if (ALLAS[lepes].v_sakk) {
            lepes--;
            return -10000 - f;
          } else {
            csere = 0;
          }
        } else {
          csere = kozepallas(f);
        }

        if (csere * e > veg * e) {
          veg = csere;
          lepes--;
//            if (veg*e==1000){
//                return veg;
//            }//@@@@@@@@@@@@@@@@@@@@@@@@
        } else {
          lepes--;
        }
      } else {
        lepes--;
      }
    }

    int i, j;
    for (i = 0; i < 8; i++) {
      for (j = 0; j < 8; j++) {
        if (ALLAS[lepes].TABLA[i][j].szin == e) {
          int l;
          int[] hovab;//int hovac[60];
          hovab = allas_lep(i, j);
          l = 0;
          while (hovab[l] != 9) {//hovac[l]=hovab[l];hovac[l+30]=hovab[l+30];l++;}
//
//            for (k=0;k<l;k++){
            an_pass = false;
            kov_allas(i, j, hovab[l], hovab[l + 30]);
            if (lepes % 2 == 0) {
              kir[0] = ALLAS[lepes].kiraly_s[0];
              ellKir[0] = ALLAS[lepes].kiraly_v[0];
              kir[1] = ALLAS[lepes].kiraly_s[1];
              ellKir[1] = ALLAS[lepes].kiraly_v[1];
            } else {
              kir[0] = ALLAS[lepes].kiraly_v[0];
              ellKir[0] = ALLAS[lepes].kiraly_s[0];
              kir[1] = ALLAS[lepes].kiraly_v[1];
              ellKir[1] = ALLAS[lepes].kiraly_s[1];
            }
            if (!sakk(kir[0], kir[1])) {
              if (ALLAS[lepes].babu == GY && (ALLAS[lepes].hova[1] == 0 || ALLAS[lepes].hova[1] == 7)) {
                atvaltozik(ALLAS[lepes].hova[0], ALLAS[lepes].hova[1]);
              }
              if (sakk(ellKir[0], ellKir[1])) {
                if (ALLAS[lepes].szin == VIL) {
                  ALLAS[lepes].s_sakk = true;
                } else {
                  ALLAS[lepes].v_sakk = true;
                }
              }
              if (patt_e()) {
                if (ALLAS[lepes].s_sakk) {
                  lepes--;
                  return 10000 + f;
                } else if (ALLAS[lepes].v_sakk) {
                  lepes--;
                  return -10000 - f;
                } else {
                  csere = 0;
                }
              } else if (allasEgyezes()) {
                csere = 0;
              } else {
                csere = kozepallas(f);
              }
              if (csere * e > veg * e) {
                veg = csere;
                lepes--;
//            if (veg*e==1000) return veg;//@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
              } else {
                lepes--;
              }
            } else {
              lepes--;
            }
            l++;
          }

          if (ALLAS[lepes].TABLA[i][j].babu == GY) {
            hovab = an_pass_lep(i, j);
            //hovac[0]=hovab[0];hovac[1]=hovab[1];
            if (hovab[0] != 9) {
              an_pass = true;
              kov_allas(i, j, hovab[0], hovab[1]);
              if (lepes % 2 == 0) {
                kir[0] = ALLAS[lepes].kiraly_s[0];
                ellKir[0] = ALLAS[lepes].kiraly_v[0];
                kir[1] = ALLAS[lepes].kiraly_s[1];
                ellKir[1] = ALLAS[lepes].kiraly_v[1];
              } else {
                kir[0] = ALLAS[lepes].kiraly_v[0];
                ellKir[0] = ALLAS[lepes].kiraly_s[0];
                kir[1] = ALLAS[lepes].kiraly_v[1];
                ellKir[1] = ALLAS[lepes].kiraly_s[1];
              }
              if (!sakk(kir[0], kir[1])) {
                //if (ALLAS[lepes].babu==GY && (ALLAS[lepes].hova[1]==0 || ALLAS[lepes].hova[1]==7)) {atvaltozik(ALLAS[lepes].hova[0], ALLAS[lepes].hova[1]);}
                if (ALLAS[lepes].hova[1] == 0 || ALLAS[lepes].hova[1] == 7) {
                  atvaltozik(ALLAS[lepes].hova[0], ALLAS[lepes].hova[1]);
                }
                if (sakk(ellKir[0], ellKir[1])) {
                  if (ALLAS[lepes].szin == VIL) {
                    ALLAS[lepes].s_sakk = true;
                  } else {
                    ALLAS[lepes].v_sakk = true;
                  }
                }
                if (patt_e()) {
                  if (ALLAS[lepes].s_sakk) {
                    lepes--;
                    return 10000 + f;
                  } else if (ALLAS[lepes].v_sakk) {
                    lepes--;
                    return -10000 - f;
                  } else {
                    csere = 0;
                  }
                } else {
                  csere = kozepallas(f);
                }
                if (csere * e > veg * e) {
                  veg = csere;
                  lepes--;
//            if (veg*e==1000) return veg;//@@@@@@@@@@@@@@@@@@@@@@@
                } else {
                  lepes--;
                }
              } else {
                lepes--;
              }
            }
          }
        }
      }
    }
    return veg;

  }

  public int kezdoallas(int f) {
    int e = 1;
    if (lepes % 2 == 1) {
      e = -1;
    }
    int veg = e * -10010, csere, m, n, o, p;

    hibaSanc = false;
    sancol();
    m = honnan[0];
    n = honnan[1];
    o = hova[0];
    p = hova[1];
    if (!hibaSanc) {
      kov_allas(honnan[0], honnan[1], hova[0], hova[1]);
      if (lepes % 2 == 0) {
        kir[0] = ALLAS[lepes].kiraly_s[0];
        ellKir[0] = ALLAS[lepes].kiraly_v[0];
        kir[1] = ALLAS[lepes].kiraly_s[1];
        ellKir[1] = ALLAS[lepes].kiraly_v[1];
      } else {
        kir[0] = ALLAS[lepes].kiraly_v[0];
        ellKir[0] = ALLAS[lepes].kiraly_s[0];
        kir[1] = ALLAS[lepes].kiraly_v[1];
        ellKir[1] = ALLAS[lepes].kiraly_s[1];
      }
      if (!sakk(kir[0], kir[1])) {
        if (sakk(ellKir[0], ellKir[1])) {
          if (ALLAS[lepes].szin == VIL) {
            ALLAS[lepes].s_sakk = true;
          } else {
            ALLAS[lepes].v_sakk = true;
          }
        }
        if (patt_e()) {
          if (ALLAS[lepes].s_sakk) {
            lepes--;
            return 10000 + f;
          } else if (ALLAS[lepes].v_sakk) {
            lepes--;
            return -10000 + f;
          } else {
            csere = 0;
          }
        } else {
          csere = kozepallas(f);
        }

        if (csere * e > veg * e) {
          veg = csere;
          lepes--;
//            if (veg*e==1000){
//                honnan[0]=m; honnan[1]=n; hova[0]=o; hova[1]=p;
//                return veg;
//            }//@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
        } else {
          lepes--;
        }
      } else {
        lepes--;
      }
    }

    hibaSanc = false;
    int s, q, r, t;

    hosszusancol();
    s = honnan[0];
    q = honnan[1];
    r = hova[0];
    t = hova[1];
    if (!hibaSanc) {
      kov_allas(honnan[0], honnan[1], hova[0], hova[1]);
      if (lepes % 2 == 0) {
        kir[0] = ALLAS[lepes].kiraly_s[0];
        ellKir[0] = ALLAS[lepes].kiraly_v[0];
        kir[1] = ALLAS[lepes].kiraly_s[1];
        ellKir[1] = ALLAS[lepes].kiraly_v[1];
      } else {
        kir[0] = ALLAS[lepes].kiraly_v[0];
        ellKir[0] = ALLAS[lepes].kiraly_s[0];
        kir[1] = ALLAS[lepes].kiraly_v[1];
        ellKir[1] = ALLAS[lepes].kiraly_s[1];
      }
      if (!sakk(kir[0], kir[1])) {
        if (sakk(ellKir[0], ellKir[1])) {
          if (ALLAS[lepes].szin == VIL) {
            ALLAS[lepes].s_sakk = true;
          } else {
            ALLAS[lepes].v_sakk = true;
          }
        }
        if (patt_e()) {
          if (ALLAS[lepes].s_sakk) {
            lepes--;
            return 10000 + f;
          } else if (ALLAS[lepes].v_sakk) {
            lepes--;
            return -10000 - f;
          } else {
            csere = 0;
          }
        } else {
          csere = kozepallas(f);
        }

        if (csere * e > veg * e) {
          veg = csere;
          lepes--;
          m = s;
          n = q;
          o = r;
          p = t;
//            if (veg*e==1000){
//                honnan[0]=m; honnan[1]=n; hova[0]=o; hova[1]=p;
//                return veg;
//            }//@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
        } else {
          lepes--;
        }
      } else {
        lepes--;
      }
    }

    int i, j;
    for (i = 0; i < 8; i++) {
      for (j = 0; j < 8; j++) {
        if (ALLAS[lepes].TABLA[i][j].szin == e) {
          int l;
          int[] hovab;//int hovac[60];
          hovab = allas_lep(i, j);
          l = 0;
          while (hovab[l] != 9) {//hovac[l]=hovab[l];hovac[l+30]=hovab[l+30];l++;}

//            for (k=0;k<l;k++){
            an_pass = false;
            kov_allas(i, j, hovab[l], hovab[l + 30]);
            if (lepes % 2 == 0) {
              kir[0] = ALLAS[lepes].kiraly_s[0];
              ellKir[0] = ALLAS[lepes].kiraly_v[0];
              kir[1] = ALLAS[lepes].kiraly_s[1];
              ellKir[1] = ALLAS[lepes].kiraly_v[1];
            } else {
              kir[0] = ALLAS[lepes].kiraly_v[0];
              ellKir[0] = ALLAS[lepes].kiraly_s[0];
              kir[1] = ALLAS[lepes].kiraly_v[1];
              ellKir[1] = ALLAS[lepes].kiraly_s[1];
            }
            if (!sakk(kir[0], kir[1])) {
              if (ALLAS[lepes].babu == GY && (ALLAS[lepes].hova[1] == 0 || ALLAS[lepes].hova[1] == 7)) {
                atvaltozik(ALLAS[lepes].hova[0], ALLAS[lepes].hova[1]);
              }
              if (sakk(ellKir[0], ellKir[1])) {
                if (ALLAS[lepes].szin == VIL) {
                  ALLAS[lepes].s_sakk = true;
                } else {
                  ALLAS[lepes].v_sakk = true;
                }
              }
              if (patt_e()) {
                if (ALLAS[lepes].s_sakk) {
                  honnan[0] = i;
                  honnan[1] = j;
                  hova[0] = hovab[l];
                  hova[1] = hovab[l + 30];
                  lepes--;
                  return 10000 + f;
                } else if (ALLAS[lepes].v_sakk) {
                  honnan[0] = i;
                  honnan[1] = j;
                  hova[0] = hovab[l];
                  hova[1] = hovab[l + 30];
                  lepes--;
                  return -10000 - f;
                } else {
                  csere = 0;
                }
              } else if (allasEgyezes()) {
                csere = 0;
              } else {
                csere = kozepallas(f);
              }

              if (csere * e > veg * e) {
                m = i;
                n = j;
                o = hovab[l];
                p = hovab[l + 30];
                veg = csere;
                lepes--;
                //            if (veg*e==1000){
                //                honnan[0]=m; honnan[1]=n; hova[0]=o; hova[1]=p;
                //                return veg;
                //            }//@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
              } else {
                lepes--;
              }
            } else {
              lepes--;
            }
            l++;
          }

          if (ALLAS[lepes].TABLA[i][j].babu == GY) {
            hovab = an_pass_lep(i, j);
            //hovac[0]=hovab[0];hovac[1]=hovab[1];
            if (hovab[0] != 9) {
              an_pass = true;
              kov_allas(i, j, hovab[0], hovab[1]);
              if (lepes % 2 == 0) {
                kir[0] = ALLAS[lepes].kiraly_s[0];
                ellKir[0] = ALLAS[lepes].kiraly_v[0];
                kir[1] = ALLAS[lepes].kiraly_s[1];
                ellKir[1] = ALLAS[lepes].kiraly_v[1];
              } else {
                kir[0] = ALLAS[lepes].kiraly_v[0];
                ellKir[0] = ALLAS[lepes].kiraly_s[0];
                kir[1] = ALLAS[lepes].kiraly_v[1];
                ellKir[1] = ALLAS[lepes].kiraly_s[1];
              }
              if (!sakk(kir[0], kir[1])) {
                //if (ALLAS[lepes].babu==GY && (ALLAS[lepes].hova[1]==0 || ALLAS[lepes].hova[1]==7)) {atvaltozik(ALLAS[lepes].hova[0], ALLAS[lepes].hova[1]);}
                if (ALLAS[lepes].hova[1] == 0 || ALLAS[lepes].hova[1] == 7) {
                  atvaltozik(ALLAS[lepes].hova[0], ALLAS[lepes].hova[1]);
                }
                if (sakk(ellKir[0], ellKir[1])) {
                  if (ALLAS[lepes].szin == VIL) {
                    ALLAS[lepes].s_sakk = true;
                  } else {
                    ALLAS[lepes].v_sakk = true;
                  }
                }
                if (patt_e()) {
                  if (ALLAS[lepes].s_sakk) {
                    honnan[0] = i;
                    honnan[1] = j;
                    hova[0] = hovab[0];
                    hova[1] = hovab[1];
                    lepes--;
                    return 10000 + f;
                  } else if (ALLAS[lepes].v_sakk) {
                    honnan[0] = i;
                    honnan[1] = j;
                    hova[0] = hovab[0];
                    hova[1] = hovab[1];
                    lepes--;
                    return -10000 - f;
                  } else {
                    csere = 0;
                  }
                } else {
                  csere = kozepallas(f);
                }

                if (csere * e > veg * e) {
                  m = i;
                  n = j;
                  o = hovab[0];
                  p = hovab[1];
                  veg = csere;
                  lepes--;
                  //            if (veg*e==1000){
                  //                honnan[0]=m; honnan[1]=n; hova[0]=o; hova[1]=p;
                  //                return veg;
                  //            }//@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
                } else {
                  lepes--;
                }
              } else {
                lepes--;
              }
            }
          }
        }
      }
    }
    honnan[0] = m;
    honnan[1] = n;
    hova[0] = o;
    hova[1] = p;
    return veg;
  }
  
}
