
package database;

import assoc.AssocStringArray;
import java.io.File;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author lajos
 */
public class DBConnect {

  private Connection conn;
  private PreparedStatement pst;
  private ResultSet rs;
  public boolean errno;
  public String error;
  public String errorEn;

  public DBConnect() {
    conn = null;
    pst = null;
    errno = false;
    error = "";
    errorEn = "";
    try {
      Class.forName("org.sqlite.JDBC");
      File fajl = new File("media/data/sakk.db").getAbsoluteFile();
      fajl.setReadable(true, false);
      fajl.setWritable(true, false);
      conn = DriverManager.getConnection("jdbc:sqlite:media/data/sakk.db");
      String sql = "CREATE TABLE IF NOT EXISTS game(\n"
              + "  nev VARCHAR(50) NOT NULL,\n"
              + "  date DATETIME DEFAULT NULL,\n"
              + "  lepes TEXT DEFAULT NULL,\n"
              + "  pont TEXT DEFAULT NULL,\n"
              + "  PRIMARY KEY (nev)"
              + ")";
      pst = conn.prepareStatement(sql);
      pst.execute();
      sql = "CREATE TABLE IF NOT EXISTS setting(\n"
              + "  id INT NOT NULL,\n"
              + "  nyelv INT NOT NULL,\n"
              + "  szin INT NOT NULL,\n"
              + "  erosseg INT NOT NULL,\n"
              + "  PRIMARY KEY (id)"
              + ")";
      pst = conn.prepareStatement(sql);
      pst.execute();
      sql = "PRAGMA encoding = 'UTF-8'";
      pst = conn.prepareStatement(sql);
      pst.execute();
    } catch (ClassNotFoundException e) {
      error = "Az adatbázishoz való csatlakozás közben hiba történt:\nValószínűleg hiányzik \"lib\" mappából a \"sqlite-jdbc-3.8.11.2.jar\" file.\n" + e.toString();
      errorEn = "Connection to database was not successful:\nMost probable the file \"sqlite-jdbc-3.8.11.2.jar\" is missing from the \"lib\" directory.\n" + e.toString();
      errno = true;
    } catch (SQLException e) {
      error = "Az adatbázishoz való csatlakozás közben hiba történt:\n" + e.toString();
      errorEn = "Connection to database was not successful:\n" + e.toString();
      errno = true;
    } finally {
      try {
        if (pst != null) {
          pst.close();
        }
      } catch (SQLException ex) {
        Logger.getLogger(DBConnect.class.getName()).log(Level.SEVERE, null, ex);
      }
    }
  }

  public String getOne(String sql, String[] values) {
    AssocStringArray[] names = getAll(sql, values);
    return (names == null) ? null : (names.length == 0) ? "" : names[0].get(0);
    // Hiba esetén null a visszatérő érték,
    // ha nincs a lekérdezésnek megfelelő adat, akkor üres String
  }

  public AssocStringArray getRow(String sql, String[] values) {
    AssocStringArray[] names = getAll(sql, values);
    return (names == null) ? null : (names.length == 0) ? new AssocStringArray() : names[0];
    // Hiba esetén null a visszatérő érték,
    // ha nincs a lekérdezésnek megfelelő adat, akkor üres AssocStringArray
  }

  public AssocStringArray[] getAll(String sql, String[] values) {
    int tombSzam;
    String strnames[];
    rs = null;
    pst = null;
    error = "";
    errorEn = "";
    AssocStringArray[] ret = null;
    try {
      pst = conn.prepareStatement(sql);
      if (values != null) {
        for (int i = 0; i < values.length; i++) {
          pst.setString(i + 1, values[i]);
        }
      }
      rs = pst.executeQuery();
      ResultSetMetaData rsmd = rs.getMetaData();
      tombSzam = rsmd.getColumnCount();
      strnames = new String[tombSzam];
      // The column count starts from 1
      for (int i = 1; i <= tombSzam; i++) {
        strnames[i - 1] = rsmd.getColumnLabel(i);
        //  getColumnLabel:
//  Gets the designated column's suggested title for use in printouts and displays.
//  The suggested title is usually specified by the SQL AS clause.
//  If a SQL AS is not specified, the value returned from getColumnLabel will be the same
//  as the value returned by the getColumnName method.
      }
      int j = 0;
      while (rs.next()) {
        j++;
      }
      ret = new AssocStringArray[j];// ha nincs visszatérő érték, akkor 0 elemű tömb!
      rs = pst.executeQuery();
      j = 0;
      while (rs.next()) {
        ret[j] = new AssocStringArray();
        for (int i = 0; i < tombSzam; i++) {
          ret[j].set(strnames[i], rs.getString(strnames[i]));
        }
        j++;
      }
    } catch (SQLException e) {
      error = "Az adatok beolvasása nem sikerült:\n" + e.toString();
      errorEn = "Reading the data was not successful:\n" + e.toString();
      return null; //ha az adatbáziskapcsolatban hiba van
    } finally {
      try {
        if (rs != null) {
          rs.close();
        }
        if (pst != null) {
          pst.close();
        }
      } catch (SQLException ex) {
        Logger.getLogger(DBConnect.class.getName()).log(Level.SEVERE, null, ex);
      }
    }
    return ret; //ha az adatbáziskapcsolatban hiba van, akkor ez null
    // ha nincs visszatérő érték, akkor 0 elemű tömb!
  }

  public String[][] getData(String[] strnames, String sql) {
    int tombSzam = strnames.length;
    rs = null;
    pst = null;
    error = "";
    errorEn = "";
    String[][] ret = null;
    try {
      pst = conn.prepareStatement(sql);
      rs = pst.executeQuery();
      int j = 0;
      while (rs.next()) {
        j++;
      }
      ret = new String[j][tombSzam];
      rs = pst.executeQuery();
      j = 0;
      while (rs.next()) {
        for (int i = 0; i < tombSzam; i++) {
          ret[j][i] = rs.getString(strnames[i]);
        }
        j++;
      }
    } catch (SQLException e) {
      error = "Az adatok beolvasása nem sikerült:\n" + e.toString();
      errorEn = "Reading the data was not successful:\n" + e.toString();
      return null;
    } finally {
      try {
        if (rs != null) {
          rs.close();
        }
        if (pst != null) {
          pst.close();
        }
      } catch (SQLException ex) {
        Logger.getLogger(DBConnect.class.getName()).log(Level.SEVERE, null, ex);
      }
    }
    return ret; //ha az adatbáziskapcsolatban hiba van, akkor ez null
    // ha nincs visszatérő érték, akkor 0 elemű tömb!
  }

  public void insertData(String[] coloumnNames, String[] values, String table) {
    if (coloumnNames == null || values == null || coloumnNames.length != values.length) {
      errno = true;
      error = "Az adatok írása nem sikerült!";
      errorEn = "Writing the data was not successful!";
      return;
    }
    errno = false;
    error = "";
    errorEn = "";
    pst = null;
    String names = "";
    String datas = "";
    for (int i = 0; i < coloumnNames.length; i++) {
      if (i != 0) {
        names += ", ";
        datas += ", ";
      }
      names += coloumnNames[i];
      datas += "?";
    }
    try {
      String sql = "INSERT INTO " + table + " (" + names + ") VALUES (" + datas + ")";
      pst = conn.prepareStatement(sql);
      for (int i = 0; i < values.length; i++) {
        pst.setString(i + 1, values[i]);
      }
      pst.executeUpdate();
    } catch (SQLException e) {
      error = "Az adatok írása nem sikerült:\n" + e.toString();
      errorEn = "Writing the data was not successful:\n" + e.toString();
      errno = true;
    } finally {
      try {
        if (pst != null) {
          pst.close();
        }
      } catch (SQLException ex) {
        Logger.getLogger(DBConnect.class.getName()).log(Level.SEVERE, null, ex);
      }
    }
  }

  public void insertData(String sql, String[] values) {
    errno = false;
    error = "";
    errorEn = "";
    pst = null;
    try {
      pst = conn.prepareStatement(sql);
      if (values != null) {
        for (int i = 0; i < values.length; i++) {
          pst.setString(i + 1, values[i]);
        }
      }
      pst.execute();
    } catch (SQLException e) {
      error = "Az adatok írása nem sikerült:\n" + e.toString();
      errorEn = "Writing the data was not successful:\n" + e.toString();
      errno = true;
    } finally {
      try {
        if (pst != null) {
          pst.close();
        }
      } catch (SQLException ex) {
        Logger.getLogger(DBConnect.class.getName()).log(Level.SEVERE, null, ex);
      }
    }
  }

  public void deleteData(String sql, String[] values) {
    errno = false;
    error = "";
    errorEn = "";
    pst = null;
    try {
      pst = conn.prepareStatement(sql);
      if (values != null) {
        for (int i = 0; i < values.length; i++) {
          pst.setString(i + 1, values[i]);
        }
      }
      pst.executeUpdate();
    } catch (SQLException e) {
      error = "A már nem szükséges adatok törlése nem sikerült:\n" + e.toString();
      errorEn = "Deleting of the data was not successful:\n" + e.toString();
      errno = true;
    } finally {
      try {
        if (pst != null) {
          pst.close();
        }
      } catch (SQLException ex) {
        Logger.getLogger(DBConnect.class.getName()).log(Level.SEVERE, null, ex);
      }
    }
  }

  public void updateData(String sql, String[] values) {
    errno = false;
    error = "";
    errorEn = "";
    pst = null;
    try {
      pst = conn.prepareStatement(sql);
      if (values != null) {
        for (int i = 0; i < values.length; i++) {
          pst.setString(i + 1, values[i]);
        }
      }
      pst.executeUpdate();
    } catch (SQLException e) {
      error = "Az adatok frissítése nem sikerült:\n" + e.toString();
      errorEn = "Updating of the data was not successful:\n" + e.toString();
      errno = true;
    } finally {
      try {
        if (pst != null) {
          pst.close();
        }
      } catch (SQLException ex) {
        Logger.getLogger(DBConnect.class.getName()).log(Level.SEVERE, null, ex);
      }
    }
  }

  public void connClose() {
    try {
      if (conn != null) {
        conn.close();
      }
    } catch (SQLException ex) {
      Logger.getLogger(DBConnect.class.getName()).log(Level.SEVERE, null, ex);
    }

  }

}
