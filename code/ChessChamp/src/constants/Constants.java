package constants;

/**
 *
 * @author lajos
 */
public interface Constants {

  final String HIBA_SAJAT_BABU_E = "HEEEEJ! DO NOT PUNCH ME BRO' !",
          HIBA_SAKK_E = "WOULD YOU LEAVE YOUR KING, YUO DOG ?!",
          HIBA_SAKKBA_LEP_E = "ATTENTION, YOUR MAJESTY, MINE !",
          HIBA_SANC_E = "YOU CAN NOT CASTLE !",
          HIBA_NINCS_ILYEN_E = "IT CAN NOT BE SUCH MOVE NOW !",
          HIBA_PATT_E = "STALEMATE!    IT IS DRAW",
          HIBA_MATTV_E = "CHECKMATE!    BLACK HAS WON",
          HIBA_MATTS_E = "CHECKMATE!    WHITE HAS WON",
          HIBA_3X_E = "3x the same position! IT IS DRAW",
          NOT_VALID_E = "NOT VALID PARAMETERS!",
          HIBA_SAJAT_BABU_M = " HEEEEJ! NE ENGEM ÜSSÉL MÁR TESA! ",
          HIBA_SAKK_M = "CSERBENHAGYNÁD KIRÁLYODAT, KUTYA?!",
          HIBA_SAKKBA_LEP_M = "     VIGYÁZZON, FELSÉG, AKNA!     ",
          HIBA_SANC_M = "NEM LEHET SÁNCOLNI!",
          HIBA_NINCS_ILYEN_M = "ILYEN LÉPÉS MOST NEM LEHETSÉGES!",
          HIBA_PATT_M = "  PATT!   DÖNTETLEN  ",
          HIBA_MATTV_M = " MATT!   SÖTÉT NYERT ",
          HIBA_MATTS_M = "MATT!   VILÁGOS NYERT",
          HIBA_3X_M = "3-szori állásismétlés! Döntetlen",
          NOT_VALID_M = "NEM ÉRVÉNYES ÉRTÉKEK!";

  final int URES = 0, K = 1, GY = 2, V = 3, B = 4, H = 5, F = 6, MAX_LEP = 401;
  final int SOT = -1, UR = 0, VIL = 1, MAX_PARTI_SZAM = 15;
  final String NINCS = "";
  int GYENGE = 2;
  int EROS = 3;

  final int a = 'a';
// final int egy = '1';
  final int[][] FUTO_POZ = {
    {-8, -8, -4, -2, -2, -4, -8, -8},
    {-8, 2, -2, 2, 2, -2, 2, -8},
    {-2, 0, 0, -2, -2, 0, 0, -2},
    {0, 2, 8, -4, -4, 8, 2, 0},
    {0, 2, 8, -4, -4, 8, 2, 0},
    {-2, 0, 0, -4, -4, 0, 0, -2},
    {-8, 2, -2, 2, 2, -2, 2, -8},
    {-8, -8, -4, -2, -2, -4, -8, -8}
  };

  final int[][] HUSZAR_POZ = {
    {-8, -8, -8, -8, -8, -8, -8, -8},
    {-15, 0, 0, 0, 0, 0, 0, -15},
    {-8, 0, 7, 7, 7, 7, 0, -8},
    {-8, 0, 4, 7, 7, 4, 0, -8},
    {-8, 0, 4, 7, 7, 4, 0, -8},
    {-8, 0, 7, 7, 7, 7, 0, -8},
    {-15, 0, 0, 0, 0, 0, 0, -15},
    {-8, -8, -8, -8, -8, -8, -8, -8}
  };

  final int[][] GYALOG_POZ = {
    {0, 0, 0, 0, 0, 0, 0, 0},
    {-15, -3, 0, 0, 0, 0, -3, -15},
    {-10, -3, 6, 14, 14, 2, -3, -10},
    {-15, -3, 20, 40, 40, 2, -3, -15},
    {-15, -3, 20, 40, 40, 2, -3, -15},
    {-10, -3, 6, 14, 14, 2, -3, -10},
    {-15, -3, 0, 0, 0, 0, -3, -15},
    {0, 0, 0, 0, 0, 0, 0, 0}
  };
}
