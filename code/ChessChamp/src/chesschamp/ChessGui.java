package chesschamp;

import assoc.AssocStringArray;
import calculate.Calculator;
import constants.Constants;
import database.DBConnect;
import form.Settings;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.*;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author lajos
 */
class ChessGui extends JFrame implements Constants {

  private String HIBA_SAJAT_BABU = HIBA_SAJAT_BABU_M,
          HIBA_SAKK = HIBA_SAKK_M,
          HIBA_SAKKBA_LEP = HIBA_SAKKBA_LEP_M,
          HIBA_SANC = HIBA_SANC_M,
          HIBA_NINCS_ILYEN = HIBA_NINCS_ILYEN_M,
          HIBA_PATT = HIBA_PATT_M,
          HIBA_MATTV = HIBA_MATTV_M,
          HIBA_MATTS = HIBA_MATTS_M,
          HIBA_3X = HIBA_3X_M,
          NOT_VALID = NOT_VALID_M;
  
  private final Calculator CALCULATOR;
  private String hiba;
  private boolean an_pass_pr;
  private final int NULLA;
  private int akt_lepes;
  private int f, vilagos, sotet, pontszam, magyar, nyelv, angol;
  private JButton button1, button2, button3, button6, newGameAction, buttonFile;
  private final JPanel GUI;
  private JButton[][] sakktabla;
  private final JLabel MESSAGE;
  private JPanel chessBoard;
  private final String COLS;
  private Image[][] chessPieceImages;
  private String[][] figures;

  private final int BLACK, WHITE;
  private int xAdjustment, yAdjustment;
  private Image img;
  private ImageIcon icon;
  private ImageIcon[][] chessPieceIcons;
  private int klikk, Xhonnan, Yhonnan, xElozo, yElozo, gepjatszik;
//private int Xhova, Yhova;
  private JButton button;
  private Color elozoColor;
  private JLabel[] lent, fent;
  private JLabel[] oldalt, oldalt2;
  private final Color FEKETE;
  private final Color FEHER;
  private final SimpleDateFormat DATE_FORMAT;
  private boolean ujTiszt;
  private JMenu menu, menuFile;
  private JMenuItem itemSettings, itemMent, itemBetolt, itemElejere, itemAktualis;
  WindowAdapter windowAdapter = new WindowAdapter() {
    @Override
    public void windowClosing(WindowEvent e) {
      exit();
    }
  };

  ChessGui() {
    setDesign();
    CALCULATOR = new Calculator();
    this.hiba = "";
    this.NULLA = '0';
    this.akt_lepes = 0;
    this.angol = 0;
    this.nyelv = 1;
    this.magyar = 1;
    this.pontszam = 0;
    this.sotet = 0;
    this.vilagos = 1;
    this.WHITE = 1;
    this.BLACK = 0;
    this.ujTiszt = false;
    this.DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        this.feher = new Color(0xFF, 0xCE, 0x9E);
//        this.fekete = new Color(0xD1, 0x89, 0x47);
    this.FEHER = new Color(0xFF, 0xFF, 0xFF);
    this.FEKETE = new Color(0x00, 0x00, 0x00);
    this.oldalt = new JLabel[8];
    this.lent = new JLabel[8];
    this.oldalt2 = new JLabel[8];
    this.fent = new JLabel[8];
    this.klikk = 0;
    this.chessPieceIcons = new ImageIcon[2][7];
    // empty img and icon
    this.img = new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB);
    this.icon = new ImageIcon(img);
    this.chessPieceImages = new Image[2][7];
    this.figures = new String[][]{
      {"BKing", "BPawn", "BQueen", "BRook", "BKnight", "BBishop"},
      {"WKing", "WPawn", "WQueen", "WRook", "WKnight", "WBishop"}
    };
    this.COLS = "abcdefgh";
    this.MESSAGE = new JLabel(
            "          ***         A Chess Champ készen áll a játékra!         ***             ");
    this.sakktabla = new JButton[8][8];
    this.GUI = new JPanel(new BorderLayout(3, 3));
    this.menu = new JMenu(" Nyelv  ");
    this.menuFile = new JMenu(" File ");

    UIManager.put("OptionPane.cancelButtonText", "Mégse");
    UIManager.put("OptionPane.okButtonText", "OK");

// create the images for the chess pieces
    createImages();

    // set up the main GUI
    GUI.setBorder(new EmptyBorder(5, 5, 5, 5));
    JToolBar tools = new JToolBar();
    tools.setFloatable(false);
    GUI.add(tools, BorderLayout.PAGE_START);

    tools.addSeparator();
    buttonFile = new JButton(" File ");
    tools.add(buttonFile);
    JMenuBar menuBarFile = new JMenuBar();
    menuBarFile.setLayout(new GridLayout());
    menuBarFile.add(menuFile);
    itemBetolt = new JMenuItem("Betölt");
    itemMent = new JMenuItem("Mentés");
    itemElejere = new JMenuItem("Ugrás a parti elejére");
    itemAktualis = new JMenuItem("Ugrás az aktuális álláshoz");
    itemSettings = new JMenuItem("Beállítások");
    menuFile.add(itemMent);
    menuFile.add(itemBetolt);
    menuFile.add(itemElejere);
    menuFile.add(itemAktualis);
    menuFile.addSeparator();
    menuFile.add(itemSettings);
//    itemMent.addActionListener(new ActionListener() {
//      @Override
//      public void actionPerformed(ActionEvent e) {
//        elment();
//      }
//    });
    itemMent.addActionListener((ActionEvent e) -> elment());
//    itemBetolt.addActionListener(new ActionListener() {
//      @Override
//      public void actionPerformed(ActionEvent e) {
//        betolt();
//      }
//    });
    itemBetolt.addActionListener((ActionEvent e) -> betolt());
//    itemElejere.addActionListener(new ActionListener() {
//      @Override
//      public void actionPerformed(ActionEvent e) {
//        if (CALCULATOR.lepes != 0) {
//          klikkTorol();
//          CALCULATOR.lepes = (gepjatszik == WHITE) ? 1 : 0;
//          kiir();
//        }
//      }
//    });
    itemElejere.addActionListener((ActionEvent e) -> {
      if (CALCULATOR.lepes != 0){
        klikkTorol();
        CALCULATOR.lepes = (gepjatszik == WHITE) ? 1 : 0;
        kiir();
      }
    });
//    itemAktualis.addActionListener(new ActionListener() {
//      @Override
//      public void actionPerformed(ActionEvent e) {
//        if (akt_lepes != 0) {
//          klikkTorol();
//          CALCULATOR.lepes = akt_lepes;
//          kiir();
//        }
//      }
//    });
    itemAktualis.addActionListener((ActionEvent e) -> {
      if (akt_lepes != 0){
        klikkTorol();
        CALCULATOR.lepes = akt_lepes;
        kiir();
      }
    });
//    itemSettings.addActionListener(new ActionListener() {
//      @Override
//      public void actionPerformed(ActionEvent e) {
//        int gepjatszikMost = gepjatszik;
//        setSettings();
//        if (gepjatszikMost != gepjatszik && !CALCULATOR.ALLAS[CALCULATOR.lepes].patt && CALCULATOR.ALLAS[CALCULATOR.lepes].allasIsm != 2 && CALCULATOR.lepes % 2 == vilagos && CALCULATOR.lepes < MAX_LEP + sotet) {
//          GUI.update(GUI.getGraphics());
//          geplep();
//        }
//      }
//    });
    itemSettings.addActionListener((ActionEvent e) -> {
        int gepjatszikMost = gepjatszik;
        setSettings();
      if (gepjatszikMost != gepjatszik && !CALCULATOR.ALLAS[CALCULATOR.lepes].patt && CALCULATOR.ALLAS[CALCULATOR.lepes].allasIsm != 2 && CALCULATOR.lepes % 2 == vilagos && CALCULATOR.lepes < MAX_LEP + sotet) {
        GUI.update(GUI.getGraphics());
        geplep();
      }
    });
    buttonFile.add(menuBarFile);
    tools.addSeparator();
    button6 = new JButton();
    tools.add(button6);
    JMenuBar menuBar = new JMenuBar();
    menuBar.setLayout(new GridLayout());
    menuBar.add(menu);
    JMenuItem itemMagyar = new JMenuItem("Magyar");
    JMenuItem itemEnglish = new JMenuItem("English");
    menu.add(itemMagyar);
    menu.add(itemEnglish);
    button6.add(menuBar);
//    itemMagyar.addActionListener(new ActionListener() {
//      @Override
//      public void actionPerformed(ActionEvent e) {
//        setNyelvMagyar();
//      }
//    });
    itemMagyar.addActionListener((ActionEvent e) -> setNyelvMagyar());
//    itemEnglish.addActionListener(new ActionListener() {
//      @Override
//      public void actionPerformed(ActionEvent e) {
//        setNyelvEnglish();
//      }
//    });
    itemEnglish.addActionListener((ActionEvent e) -> setNyelvEnglish());
    String str;
    str = (nyelv == magyar) ? "Új játék" : "New Game";
    newGameAction = new JButton(str);
    tools.addSeparator();
    tools.add(newGameAction);
    newGameAction.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        setupNewGame();
      }
    });

    tools.addSeparator();
    str = (nyelv == magyar) ? "Vissza" : "Undo";
    button1 = new JButton(str);
    tools.add(button1);
    button1.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        if (CALCULATOR.lepes >= 2) {
          klikkTorol();
          if (CALCULATOR.ALLAS[CALCULATOR.lepes].patt || CALCULATOR.ALLAS[CALCULATOR.lepes].allasIsm == 2) {
            CALCULATOR.lepes = (CALCULATOR.lepes % 2 == vilagos) ? CALCULATOR.lepes - 1 : CALCULATOR.lepes - 2;
          } else {
            CALCULATOR.lepes -= 2;
          }
          kiir();
        }
      }
    });
    tools.addSeparator();
    str = (nyelv == magyar) ? "Előre" : " Redo ";
    button2 = new JButton(str);
    tools.add(button2);
    button2.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        if (CALCULATOR.lepes < akt_lepes) {
          klikkTorol();
          CALCULATOR.lepes++;
          if (CALCULATOR.lepes != akt_lepes) {
            CALCULATOR.lepes++;
          }
          kiir();
        }
      }
    });

    tools.addSeparator();
    str = (nyelv == magyar) ? "Kilépés" : "Quit";
    button3 = new JButton(str);
    tools.add(button3); // TODO - add functionality!
    button3.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        exit();
      }
    });
//        tools.addSeparator();
    tools.add(MESSAGE);
    tools.setLayout(new FlowLayout(FlowLayout.LEFT));

//        gui.add(new JLabel("?"), BorderLayout.LINE_START);
//        chessBoard = new JPanel(new GridLayout(0, 9));
    chessBoard = new JPanel() {

      /**
       * Override the preferred size to return the largest it can, in a square
       * shape. Must (must, must) be added to a GridBagLayout as the only
       * component (it uses the parent as a guide to size) with no
       * GridBagConstaint (so it is centered).
       */
      @Override
      public final Dimension getPreferredSize() {
        Dimension d = super.getPreferredSize();
        Dimension prefSize;
        Component c = getParent();
        if (c == null) {
          prefSize = new Dimension(
                  (int) d.getWidth(), (int) d.getHeight());
        } else if (c.getWidth() > d.getWidth()
                && c.getHeight() > d.getHeight()) {
          prefSize = c.getSize();
        } else {
          prefSize = d;
        }
        int w = (int) prefSize.getWidth();
        int h = (int) prefSize.getHeight();
        // the smaller of the two sizes
        int s = (w > h ? h : w);
        return new Dimension(s, s);
      }
    };

    RelativeLayout rl = new RelativeLayout(RelativeLayout.Y_AXIS);
    rl.setRoundingPolicy(RelativeLayout.FIRST);
    rl.setFill(true);
    chessBoard.setLayout(rl);

    chessBoard.setBorder(new CompoundBorder(
            new EmptyBorder(8, 8, 8, 8),
            new LineBorder(Color.DARK_GRAY, 2)
    ));
    // Set the BG to be ochre
    Color ochre = new Color(204, 119, 34);
    chessBoard.setBackground(ochre);
    JPanel boardConstrain = new JPanel(new GridBagLayout());
// Essentially, GridBagLayout places components in rectangles (cells) in a grid,
// and then uses the components' preferred sizes to determine how big the cells should be.
    boardConstrain.setBackground(ochre);
    boardConstrain.add(chessBoard);
    GUI.add(boardConstrain);
    // create the chess board squares
    Insets buttonMargin = new Insets(0, 0, 0, 0);

    for (int i = 0; i < 8; i++) {
      for (int j = 0; j < 8; j++) {

        sakktabla[i][j] = new JButton();
        sakktabla[i][j].setMargin(buttonMargin);//**********************************************
        sakktabla[i][j].setUI(new ModifButtonUI());
        sakktabla[i][j].setName("" + i + j);
        //    System.out.println(sakktabla[i][j].getName());//ellenőrzés

        //    ImageIcon icon = new ImageIcon(new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB));
        sakktabla[i][j].setIcon(icon);
        if ((i + j) % 2 == 0) {
          sakktabla[i][j].setBackground(FEHER);
        } else {
          sakktabla[i][j].setBackground(FEKETE);
        }

        sakktabla[i][j].addMouseListener(new MouseAdapter() {

          @Override
          public void mousePressed(MouseEvent e) {
            button = (JButton) e.getSource();
            //    xAdjustment=(button.getX()-xAdd)/wAdd;
            //    yAdjustment=(button.getY()-yAdd)/hAdd;
            String name = button.getName();
            //        int nulla = (int)'0';
            xAdjustment = (int) (name.charAt(0)) - NULLA;
            yAdjustment = (int) (name.charAt(1)) - NULLA;

            ujTiszt = false;
            klikkreag();
            if (ujTiszt) {
              button.doClick();
            }
          }

          @Override
          public void mouseReleased(MouseEvent e) {
            if (!CALCULATOR.ALLAS[CALCULATOR.lepes].patt && CALCULATOR.ALLAS[CALCULATOR.lepes].allasIsm != 2 && CALCULATOR.lepes % 2 == vilagos && CALCULATOR.lepes < MAX_LEP + sotet) {
              geplep();
            }
          }

        });
//        sakktabla[i][j].addActionListener(new ActionListener() {
//          @Override
//          public void actionPerformed(ActionEvent e) {
//            if (ujTiszt && !CALCULATOR.ALLAS[CALCULATOR.lepes].patt && CALCULATOR.ALLAS[CALCULATOR.lepes].allasIsm != 2 && CALCULATOR.lepes % 2 == vilagos && CALCULATOR.lepes < MAX_LEP + sotet) {
//              akt_lepes = CALCULATOR.lepes;
//              geplep();
//            }
//            ujTiszt = false;
//          }
//        });
        sakktabla[i][j].addActionListener((ActionEvent e) -> {
          if (ujTiszt && !CALCULATOR.ALLAS[CALCULATOR.lepes].patt && CALCULATOR.ALLAS[CALCULATOR.lepes].allasIsm != 2 && CALCULATOR.lepes % 2 == vilagos && CALCULATOR.lepes < MAX_LEP + sotet) {
            akt_lepes = CALCULATOR.lepes;
            geplep();
          }
          ujTiszt = false;
        });

        //Játékoslép és gép lép egyben változat:
//            sakktabla[i][j].addActionListener((ActionEvent e) -> {
//                    button=(JButton)e.getSource();
//                //    xAdjustment=(button.getX()-xAdd)/wAdd;
//                //    yAdjustment=(button.getY()-yAdd)/hAdd;
//                    String name = button.getName();
//                    xAdjustment = (int)(name.charAt(0)) - nulla;
//                    yAdjustment = (int)(name.charAt(1)) - nulla;
//
//                    ujTiszt = false;
//                    klikkreag();
//                    gui.update(gui.getGraphics());
//                    if (!ALLAS[CALCULATOR.lepes].patt && ALLAS[CALCULATOR.lepes].allasIsm != 2 && CALCULATOR.lepes%2==vilagos && CALCULATOR.lepes<maxLep+sotet)
//                        geplep();
//            });
      }
    }

    RelativeLayout topRL = new RelativeLayout(RelativeLayout.X_AXIS);
    topRL.setRoundingPolicy(RelativeLayout.FIRST);
    topRL.setFill(true);
    RelativeLayout bottomRL = new RelativeLayout(RelativeLayout.X_AXIS);
    bottomRL.setRoundingPolicy(RelativeLayout.LAST);
    bottomRL.setFill(true);
    JPanel top = new JPanel(topRL);
    top.setOpaque(false);
    JPanel bottom = new JPanel(bottomRL);
    bottom.setOpaque(false);

    chessBoard.add(top, (float) 0.6);
    top.add(new JLabel(""), (float) 0.6);
    // fill the bottom row

    for (int ii = 0; ii < 8; ii++) {
      JLabel label = new JLabel(COLS.substring(ii, ii + 1), SwingConstants.CENTER);
//            JLabel label=new JLabel(""+(char)(ii+'A'), SwingConstants.CENTER);
      fent[ii] = label;
      top.add(fent[ii], new Float(1));
    }
    top.add(new JLabel(""), (float) 0.6);

    for (int ii = 0; ii < 8; ii++) {
      RelativeLayout rowRL = new RelativeLayout(RelativeLayout.X_AXIS);
      rowRL.setRoundingPolicy(RelativeLayout.FIRST);
      rowRL.setFill(true);
      JPanel row = new JPanel(rowRL);
      row.setOpaque(false);
      chessBoard.add(row, new Float(1));

      for (int jj = 0; jj < 8; jj++) {
        switch (jj) {
          case 7: {
            row.add(sakktabla[jj][ii], new Float(1));
            JLabel label = new JLabel("" + (8 - (ii)),
                    SwingConstants.CENTER);
            oldalt2[7 - ii] = label;
            row.add(oldalt2[7 - ii], (float) 0.6);
            break;
          }
          case 0: {
            JLabel label = new JLabel("" + (8 - (ii)),
                    SwingConstants.CENTER);
            oldalt[7 - ii] = label;
            row.add(oldalt[7 - ii], (float) 0.6);//nincs break, tehát default-tal folytatódik !
          }
          default:
            row.add(sakktabla[jj][ii], new Float(1));
        }
      }
    }

    chessBoard.add(bottom, (float) 0.6);
    bottom.add(new JLabel(""), (float) 0.6);
    // fill the bottom row

    for (int ii = 0; ii < 8; ii++) {
      JLabel label = new JLabel(COLS.substring(ii, ii + 1), SwingConstants.CENTER);
//            JLabel label=new JLabel(""+(char)(ii+'A'), SwingConstants.CENTER);
      lent[ii] = label;
      bottom.add(lent[ii], new Float(1));
    }
    bottom.add(new JLabel(""), (float) 0.6);

    getSettings();

    setTitle("ChessChamp");
    add(GUI);
    setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    // See http://stackoverflow.com/a/7143398/418556 for demo.
    setLocationByPlatform(true);
    // ensures the frame is the minimum size it needs to be
    // in order display the components within it
    pack();
    setChessBoardSize();
    // ensures the minimum size is enforced.
    setMinimumSize(getSize());
    setLocationRelativeTo(this);
//    setResizable(false);
    addWindowListener(windowAdapter);
    setVisible(true);

    setupNewGame();

  }

  private void exit() {
    int result;
    Object[] valasztas = {"Igen", "Nem"};
    Object[] valasztasEn = {"Yes", "No"};
    result = JOptionPane.showOptionDialog(
            rootPane,
            (nyelv == magyar) ? "Tényleg ki akarsz lépni"
                    + "\n a programból?" : "Would you like to really quit?",
            (nyelv == magyar) ? "Kilépés?" : "Quit?",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            (nyelv == magyar) ? valasztas : valasztasEn,
            (nyelv == magyar) ? valasztas[1] : valasztasEn[1]
    );
    if (result == JOptionPane.YES_OPTION) {
      saveSettings();
      System.exit(0);
    }
  }

  void setChessBoardSize() {
    chessBoard.setPreferredSize(chessBoard.getPreferredSize());
  }

  private void saveSettings() {
    DBConnect dbconn = new DBConnect();
    if (dbconn.errno) {
      JOptionPane.showMessageDialog(null, (nyelv == magyar) ? "A beállítások mentése az adatbázisba nem sikerült!\n" + dbconn.error
              : "Saving the settings to the database was not successful" + dbconn.errorEn, (nyelv == magyar) ? "Hiba!" : "Error!", JOptionPane.ERROR_MESSAGE);
      dbconn.connClose();
      return;
    }
    AssocStringArray setting = dbconn.getRow("SELECT * FROM setting WHERE id = 1", null);
    if (setting.size() == 0) {
      dbconn.insertData("INSERT INTO setting (id, nyelv, erosseg, szin) VALUES('1', ?, ?, ?)",
              new String[]{"" + nyelv, "" + CALCULATOR.erosseg, "" + ((gepjatszik + 1) % 2)});
    } else {
      dbconn.updateData("UPDATE setting SET nyelv=?, erosseg=?, szin=? WHERE id=1",
              new String[]{"" + nyelv, "" + CALCULATOR.erosseg, "" + ((gepjatszik + 1) % 2)});
    }
    if (dbconn.errno) {
      JOptionPane.showMessageDialog(null, (nyelv == magyar) ? "A beállítások mentése az adatbázisba nem sikerült!\n" + dbconn.error
              : "Saving the settings to the database was not successful" + dbconn.errorEn,
              (nyelv == magyar) ? "Hiba!" : "Error!",
              JOptionPane.ERROR_MESSAGE);
    }
//    else{
//        JOptionPane.showMessageDialog(null, (nyelv == magyar) ? "A beállítások mentése az adatbázisba sikerült!\n":
//                "Saving the settings to the database was successful",
//                (nyelv == magyar) ? "Siker!" : "Success!",
//                JOptionPane.INFORMATION_MESSAGE);
//    } // ellenőrzés
    dbconn.connClose();
  }

  private void getSettings() {
    DBConnect dbconn = new DBConnect();
    if (dbconn.errno) {
      JOptionPane.showMessageDialog(null, (nyelv == magyar) ? dbconn.error : dbconn.errorEn, (nyelv == magyar) ? "Hiba!" : "Error!", JOptionPane.ERROR_MESSAGE);
      dbconn.connClose();
      System.exit(0);
    }
    AssocStringArray setting = dbconn.getRow("SELECT * FROM setting WHERE id=1", null);
    if (setting == null) {
      JOptionPane.showMessageDialog(null, (nyelv == magyar) ? dbconn.error : dbconn.errorEn, (nyelv == magyar) ? "Hiba!" : "Error!", JOptionPane.ERROR_MESSAGE);
      dbconn.connClose();
      return;
    }
    if (setting.size() != 0) {
      try {
        int ny = Integer.parseInt(setting.get("nyelv"));
        int sz = Integer.parseInt(setting.get("szin"));
        CALCULATOR.erosseg = Integer.parseInt(setting.get("erosseg"));
        if (!(ny == magyar || ny == angol)) {
          throw new Exception("Nem megfelelő számok! Nyelv");
        }
        if (!(CALCULATOR.erosseg == GYENGE || CALCULATOR.erosseg == EROS)) {
          CALCULATOR.erosseg = GYENGE;
        }
//            System.out.println(WHITE);
//            System.out.println(BLACK);
//            System.out.println(sz); //ellenőrzések
        if (!(sz == WHITE || sz == BLACK)) {
          throw new Exception("Nem megfelelő számok! Szin");
        }
        setColorSettings(sz);
        if (ny == angol) {
          setNyelvEnglish();
          MESSAGE.setText("           ***           Chess Champ is ready to play!           ***              ");
        } else {
          setNyelvMagyar();
          MESSAGE.setText("          ***         A Chess Champ készen áll a játékra!         ***             ");
        }

//            System.out.println(setting.get("nyelv"));
//            System.out.println(setting.get("szin"));
//            System.out.println(setting.get("erosseg")); //ellenőrzések
      } catch (Exception e) {
        JOptionPane.showMessageDialog(
                null,
                (nyelv == magyar) ? "Sérült az adatbázis, nem sikerült a beállítások betöltése!\n" + e.getMessage()
                        : "The database is corrupt, loading the settings was not successful!\n" + e.getMessage(),
                (nyelv == magyar) ? "Hiba!" : "Error!",
                JOptionPane.WARNING_MESSAGE
        );
        dbconn.connClose();
        System.exit(0);
      }
    }
    dbconn.connClose();
  }

  private void setDesign() {
    /* Set the Nimbus look and feel */
    //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
    /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
     */
    try {
      UIManager.setLookAndFeel(
              "com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
//            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
//                if ("Nimbus".equals(info.getName())) {
//                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
//                    break;
//                }
//            }
    } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
      java.util.logging.Logger.getLogger(ChessChamp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    }
    //</editor-fold>
  }

  final JComponent getGui() {
    return GUI;
  }

//private final void createImages() {
//        try {
//            URL url = new URL("http://i.stack.imgur.com/memI0.png");
//            BufferedImage bi = ImageIO.read(url);
//            for (int ii = 0; ii < 2; ii++) {
//                for (int jj = 0; jj < 6; jj++) {
//                    chessPieceImages[ii][jj] = bi.getSubimage(
//                            jj * 64, ii * 64, 64, 64);
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            System.exit(1);
//        }
//    }
  private void createImages() {
    try {
//            File fajl = new File("media/chess-pieces.png");
////            File fajl = new File("media/chess-pieces-solid.png");
//
//            BufferedImage bi = ImageIO.read(fajl);
//            chessPieceImages[0][0]=img;
//            chessPieceImages[1][0]=img;
//            for (int ii = 0; ii < 2; ii++) {
//                for (int jj = 0; jj < 6; jj++) {
//                    chessPieceImages[ii][segedTomb[jj+1]] = bi.getSubimage(
//                            jj * 64, ii * 64, 64, 64);
//                }
//            }
      chessPieceImages[0][0] = img;
      chessPieceImages[1][0] = img;
      for (int i = 0; i < 2; i++) {
        for (int j = 1; j < 7; j++) {
          File fajl = new File("media/Resources/" + figures[i][j - 1] + ".gif").getAbsoluteFile();
          BufferedImage bi = ImageIO.read(fajl);
          chessPieceImages[i][j] = bi.getSubimage(0, 0, 57, 57);
        }

      }
    } catch (IOException e) {
      JOptionPane.showMessageDialog(
              rootPane,
              (nyelv == magyar) ? "A sakkfigura képek beolvasása a media/Resources mappából nem sikerült!"
                      : "The reading of the chess-pieces images from the media/Resources folder was not succesful!",
              (nyelv == magyar) ? "Hiba!" : "Error!",
              JOptionPane.ERROR_MESSAGE
      );
      System.exit(0);
    } catch (Exception ex) {
      JOptionPane.showMessageDialog(
              rootPane,
              (nyelv == magyar) ? "A sakkfigura képek image-é formálása a media/Resources mappából nem sikerült!"
                      : "The transforming of the chess-pieces images from the media/Resources folder was not succesful!",
              (nyelv == magyar) ? "Hiba!" : "Error!",
              JOptionPane.ERROR_MESSAGE
      );
      System.exit(0);
    }
    chessPieceIcons[0][0] = icon;
    chessPieceIcons[1][0] = icon;
    for (int i = 0; i < 2; i++) {
      for (int j = 1; j < 7; j++) {
        chessPieceIcons[i][j] = new ImageIcon(chessPieceImages[i][j]);
      }
    }

//        for (int i = 0; i < 2; i++) {
//            for (int j = 1; j < 7; j++) {
//                chessPiecesLabels[i][j]=new JLabel(chessPieceIcons[i][j]);
//            }
//        }//*******************************************************************
  }

  private boolean setSettings() {
    klikkTorol();
    Settings settings = new Settings(nyelv, (gepjatszik == WHITE) ? BLACK : WHITE, CALCULATOR.erosseg);
    if (CALCULATOR.kezd) {
      int result = JOptionPane.showOptionDialog(rootPane,
              settings.getGui(),
              null,
              JOptionPane.OK_CANCEL_OPTION,
              JOptionPane.QUESTION_MESSAGE,
              null,
              null,
              JOptionPane.OK_OPTION);
      if (result != JOptionPane.OK_OPTION) {
        return false;
      }
    } else {
      JOptionPane.showMessageDialog(rootPane,
              settings.getGui(),
              null,
              JOptionPane.QUESTION_MESSAGE,
              null);
    }
    CALCULATOR.erosseg = settings.getErosseg();
    setColorSettings(settings.getSzin());
    int ny = nyelv;
    if (settings.getNyelv() == magyar) {
      setNyelvMagyar();
    } else {
      setNyelvEnglish();
    }
    settings.dispose();
    if (ny == nyelv) {
      kiir();
    }
    return true;
  }

  private void setColorSettings(int szin) {
    if (szin == WHITE) {
      gepjatszik = BLACK;
      {
        vilagos = 1;
        sotet = 0;
      }
      for (int ii = 0; ii < 8; ii++) {
        lent[ii].setText("" + (char) (ii + ((int) 'a')));
        fent[ii].setText("" + (char) (ii + ((int) 'a')));
      }
      for (int ii = 0; ii < 8; ii++) {
        oldalt[ii].setText("" + (1 + ii));
        oldalt2[ii].setText("" + (1 + ii));
      }
    } else {
      gepjatszik = WHITE;
      {
        vilagos = 0;
        sotet = 1;
      }
      for (int ii = 0; ii < 8; ii++) {
        lent[7 - ii].setText("" + (char) (((int) 'a') + ii));
        fent[7 - ii].setText("" + (char) (((int) 'a') + ii));
      }
      for (int ii = 0; ii < 8; ii++) {
        oldalt[7 - ii].setText("" + (char) (((int) '1') + ii));
        oldalt2[7 - ii].setText("" + (char) (((int) '1') + ii));
      }
    }
  }

  void setupNewGame() {
    if (!setSettings()) {
      return;
    }
    CALCULATOR.lepes = 0;
    akt_lepes = 0;
    CALCULATOR.alapBeall();
    if (CALCULATOR.lepes % 2 == vilagos) {
      int x, y, i, j;
      int z = (int) (Math.random() * 6);
      switch (z) {
        case 0:
          x = 4;
          y = 1;
          i = 4;
          j = 3;
          break;
        case 1:
          x = 3;
          y = 1;
          i = 3;
          j = 3;
          break;
        case 2:
          x = 2;
          y = 1;
          i = 2;
          j = 3;
          break;
        case 3:
          x = 6;
          y = 0;
          i = 5;
          j = 2;
          break;
        case 4:
          x = 4;
          y = 1;
          i = 4;
          j = 3;
          break;
        default:
          x = 3;
          y = 1;
          i = 3;
          j = 3;
          break;
      }
      CALCULATOR.kov_allas(x, y, i, j);
      akt_lepes = CALCULATOR.lepes;
      CALCULATOR.ALLAS[CALCULATOR.lepes].pontszam = 0;
    }
    kiir();

  }

  private void klikkreag() {
    if (CALCULATOR.lepes % 2 == vilagos || (klikk == 0 && button.getIcon() == icon) || CALCULATOR.ALLAS[CALCULATOR.lepes].patt || CALCULATOR.ALLAS[CALCULATOR.lepes].allasIsm == 2) {
      return;
    }
    int x, y;
    if (gepjatszik == BLACK) {
      x = xAdjustment;
      y = 7 - yAdjustment;
    } else {
      x = 7 - xAdjustment;
      y = yAdjustment;
    }
    if (klikk == 0 && (CALCULATOR.ALLAS[CALCULATOR.lepes].TABLA[x][y].szin + 1) / 2 == gepjatszik) {
      return;
    }
    klikk++;
    klikk = klikk % 2;
    if (klikk == 1) {
      Xhonnan = xAdjustment;
      Yhonnan = yAdjustment;
      xElozo = x;
      yElozo = y;
      elozoColor = button.getBackground();
      button.setBackground(Color.lightGray);
    } else if (Xhonnan == xAdjustment && Yhonnan == yAdjustment) {
      button.setBackground(elozoColor);
    } else {
//        Xhova=xAdjustment; Yhova=yAdjustment;
      sakktabla[Xhonnan][Yhonnan].setBackground(elozoColor);

      lep_meghat(xElozo, yElozo, x, y, true, URES);

    }
  }

  private char utes_e() {
    if (CALCULATOR.ALLAS[CALCULATOR.lepes - 1].TABLA[CALCULATOR.ALLAS[CALCULATOR.lepes].hova[0]][CALCULATOR.ALLAS[CALCULATOR.lepes].hova[1]].babu != URES || an_pass_pr) {
      return 'x';
    } else {
      return '-';
    }
  }

  private char utes_e_ment(int lepes) {
    if (CALCULATOR.ALLAS[lepes - 1].TABLA[CALCULATOR.ALLAS[lepes].hova[0]][CALCULATOR.ALLAS[lepes].hova[1]].babu != URES || an_pass_pr) {
      return 'x';
    } else {
      return '-';
    }
  }

  private void kiir() {
//    System.out.println(CALCULATOR.lepes); //ellenőrzéshez
    int x, y;
    for (int i = 0; i < 8; i++) {
      for (int j = 0; j < 8; j++) {
        if (gepjatszik == BLACK) {
          x = i;
          y = 7 - j;
        } else {
          x = 7 - i;
          y = j;
        }
        sakktabla[i][j].setIcon(
                chessPieceIcons[(CALCULATOR.ALLAS[CALCULATOR.lepes].TABLA[x][y].szin + 1) / 2][CALCULATOR.ALLAS[CALCULATOR.lepes].TABLA[x][y].babu]);
      }
    }
    if (CALCULATOR.lepes == 0) {
      MESSAGE.setText((nyelv == magyar) ? "          ***        A Chess Champ készen áll a játékra!        ***"
              : "           ***         Chess Champ is ready to play!         ***");
      return;
    }
    CALCULATOR.setIsm();
    if (CALCULATOR.allasEgyezes()) {
      CALCULATOR.ALLAS[CALCULATOR.lepes].allasIsm = 2;
      //hiba = HIBA_3x;
    }
    String text1, text2, text3 = "", text4 = "";
    if (CALCULATOR.ALLAS[CALCULATOR.lepes].v_sakk || CALCULATOR.ALLAS[CALCULATOR.lepes].s_sakk) {
      if (CALCULATOR.ALLAS[CALCULATOR.lepes].patt) {
        text3 = (nyelv == magyar) ? " sakk-matt" : " checkmate";
      } else {
        text3 = (nyelv == magyar) ? " sakk" : " check";
      }
    } else if (CALCULATOR.ALLAS[CALCULATOR.lepes].patt) {
      text3 = (nyelv == magyar) ? " patt" : " stalemate";
    }
    if (CALCULATOR.ALLAS[CALCULATOR.lepes].babu == GY && (CALCULATOR.ALLAS[CALCULATOR.lepes].hova[1] == 0 || CALCULATOR.ALLAS[CALCULATOR.lepes].hova[1] == 7)) {
      text4 = "" + babkod(CALCULATOR.ALLAS[CALCULATOR.lepes].TABLA[CALCULATOR.ALLAS[CALCULATOR.lepes].hova[0]][CALCULATOR.ALLAS[CALCULATOR.lepes].hova[1]].babu);
    }
    if (CALCULATOR.ALLAS[CALCULATOR.lepes].babu == K && CALCULATOR.ALLAS[CALCULATOR.lepes].honnan[0] - CALCULATOR.ALLAS[CALCULATOR.lepes].hova[0] == 2) {
      text1 = "    ** 0-0-0";
    } else if (CALCULATOR.ALLAS[CALCULATOR.lepes].babu == K && CALCULATOR.ALLAS[CALCULATOR.lepes].hova[0] - CALCULATOR.ALLAS[CALCULATOR.lepes].honnan[0] == 2) {
      text1 = "    **  0-0 ";
    } else {
      text1 = "    ** " + babkod(CALCULATOR.ALLAS[CALCULATOR.lepes].babu)
              + betukod(CALCULATOR.ALLAS[CALCULATOR.lepes].honnan[0]) + szamkod(CALCULATOR.ALLAS[CALCULATOR.lepes].honnan[1])
              + utes_e() + betukod(CALCULATOR.ALLAS[CALCULATOR.lepes].hova[0]) + szamkod(CALCULATOR.ALLAS[CALCULATOR.lepes].hova[1]);
    }
//    if (ALLAS[CALCULATOR.lepes].patt)text2="   ***";
//    else if (CALCULATOR.lepes%2==sotet) text2="   ***          Te következel!";
//    else text2="   ***          Gondolkodom...";
//    message.setText(text1+text4+text3+text2);
//
//    int pont=(ALLAS[CALCULATOR.lepes].pontszam < 10000) ? ALLAS[CALCULATOR.lepes].pontszam : ALLAS[CALCULATOR.lepes].pontszam - f;
//    pont = (pont > -10000) ? pont : pont + f;
//    int pattPont=(ALLAS[CALCULATOR.lepes].s_sakk) ? 10000 : (ALLAS[CALCULATOR.lepes].v_sakk) ? -10000 : 0;
    if (CALCULATOR.ALLAS[CALCULATOR.lepes].patt) {
      CALCULATOR.ALLAS[CALCULATOR.lepes].pontszam = (CALCULATOR.ALLAS[CALCULATOR.lepes].s_sakk) ? 10000 : (CALCULATOR.ALLAS[CALCULATOR.lepes].v_sakk) ? -10000 : 0;
      text2 = (nyelv == magyar) ? " **   Állásérték: " + CALCULATOR.ALLAS[CALCULATOR.lepes].pontszam
              : " **  Position rate: " + CALCULATOR.ALLAS[CALCULATOR.lepes].pontszam;
    } else if (CALCULATOR.ALLAS[CALCULATOR.lepes].allasIsm == 2) {
      CALCULATOR.ALLAS[CALCULATOR.lepes].pontszam = 0;
      text2 = (nyelv == magyar) ? " **   Állásérték: 0  ** 3x állás"
              : " **  Position rate: 0  ** 3x position";
    } else if (CALCULATOR.lepes % 2 == sotet) {
      text2 = (nyelv == magyar) ? " **   Állásérték: " + CALCULATOR.ALLAS[CALCULATOR.lepes].pontszam + " **  Te következel!"
              : " **  Position rate: " + CALCULATOR.ALLAS[CALCULATOR.lepes].pontszam + " **  Make your move!";
    } else {
      text2 = (nyelv == magyar) ? " **   Állásérték: " + CALCULATOR.ALLAS[CALCULATOR.lepes].pontszam + " **  Gondolkodom..."
              : " **  Position rate: " + CALCULATOR.ALLAS[CALCULATOR.lepes].pontszam + " **  I am thinking...";
    }
//    int pont=(pontszam < 1000) ? pontszam : pontszam-f;
//    int pattPont=(ALLAS[CALCULATOR.lepes].s_sakk) ? 1000 : (ALLAS[CALCULATOR.lepes].v_sakk) ? -1000 : 0;
//    if (ALLAS[CALCULATOR.lepes].patt)text2=" **";
//    else if (CALCULATOR.lepes%2==sotet) text2=(nyelv==magyar) ? " **    Te következel!" :
//            " **    Make your move!";
//    else text2=(nyelv==magyar) ? "  **    Gondolkodom..." :
//            " **    I am thinking...";
    MESSAGE.setText(text1 + text4 + text3 + text2);
  }

  private void atvaltoz(int x, int y, int tiszt) {
    if (tiszt != URES) {
      CALCULATOR.ALLAS[CALCULATOR.lepes].TABLA[x][y].babu = tiszt;
      return;
    }
    kiir();
//    ALLAS[CALCULATOR.lepes].TABLA[x][y].babu=V;

    int atv;
    Object[] tisztek = {chessPieceIcons[(gepjatszik + 1) % 2][V], chessPieceIcons[(gepjatszik + 1) % 2][B],
      chessPieceIcons[(gepjatszik + 1) % 2][F], chessPieceIcons[(gepjatszik + 1) % 2][H]};
    atv = JOptionPane.showOptionDialog(
            rootPane,
            (nyelv == magyar) ? "\nOSZT' MI LESZEL, HA NAGY LESZEL?\n\n"
                    : "\nWHAT YOU'LL BE WHEN YOU GROW UP?\n\n",
            (nyelv == magyar) ? "Új tiszt" : "New officer",
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            tisztek,
            tisztek[0]
    );
    ujTiszt = true;

    switch (atv) {
      case 1:
        CALCULATOR.ALLAS[CALCULATOR.lepes].TABLA[x][y].babu = B;
        break;
      case 2:
        CALCULATOR.ALLAS[CALCULATOR.lepes].TABLA[x][y].babu = F;
        break;
      case 3:
        CALCULATOR.ALLAS[CALCULATOR.lepes].TABLA[x][y].babu = H;
        break;
      default:
        CALCULATOR.ALLAS[CALCULATOR.lepes].TABLA[x][y].babu = V;
    }

  }

  private String babkod(int z) {
    if (nyelv == magyar) {
      switch (z) {
        case V:
          return "V";
        case K:
          return "K";
        case F:
          return "F";
        case B:
          return "B";
        case H:
          return "H";
        default:
          return "";
      }
    } else {
      switch (z) {
        case V:
          return "Q";
        case K:
          return "K";
        case F:
          return "B";
        case B:
          return "R";
        case H:
          return "N";
        default:
          return "";
      }
    }
  }

  private char betukod(int z) {
    return (char) (z + a);
  }

  private int szamkod(int z) {
    return z + 1;
  }

  

  private void setNyelvEnglish() {
    if (nyelv != angol) {
      UIManager.put("OptionPane.cancelButtonText", "Cancel");
      nyelv = angol;
//    message.setText("           ***         Chess Champ is ready to play!         ***");
      HIBA_SAJAT_BABU = HIBA_SAJAT_BABU_E;
      HIBA_SAKK = HIBA_SAKK_E;
      HIBA_SAKKBA_LEP = HIBA_SAKKBA_LEP_E;
      HIBA_SANC = HIBA_SANC_E;
      HIBA_MATTS = HIBA_MATTS_E;
      HIBA_MATTV = HIBA_MATTV_E;
      HIBA_PATT = HIBA_PATT_E;
      HIBA_NINCS_ILYEN = HIBA_NINCS_ILYEN_E;
      HIBA_3X = HIBA_3X_E;
      button1.setText("Undo");
      button2.setText("Redo");
      itemMent.setText("Save");
      itemBetolt.setText("Load");
      itemElejere.setText("Jump to the beginning of the game");
      itemAktualis.setText("Jump to the current position");
      itemSettings.setText("Settings");
      menu.setText("Language");
      button3.setText("Quit");
      newGameAction.setText("New Game");
    }
    kiir();
    saveSettings();
  }

  private void setNyelvMagyar() {
    if (nyelv != magyar) {
      UIManager.put("OptionPane.cancelButtonText", "Mégse");
      nyelv = magyar;
//    message.setText("          ***            A Chess Champ készen áll a játékra!             ***    ");
      HIBA_SAJAT_BABU = HIBA_SAJAT_BABU_M;
      HIBA_SAKK = HIBA_SAKK_M;
      HIBA_SAKKBA_LEP = HIBA_SAKKBA_LEP_M;
      HIBA_SANC = HIBA_SANC_M;
      HIBA_MATTS = HIBA_MATTS_M;
      HIBA_MATTV = HIBA_MATTV_M;
      HIBA_PATT = HIBA_PATT_M;
      HIBA_NINCS_ILYEN = HIBA_NINCS_ILYEN_M;
      HIBA_3X = HIBA_3X_M;
      button1.setText("Vissza");
      button2.setText("Előre");
      itemMent.setText("Mentés");
      itemBetolt.setText("Betölt");
      itemElejere.setText("Ugrás a parti elejére");
      itemAktualis.setText("Ugrás az aktuális álláshoz");
      itemSettings.setText("Beállítások");
      menu.setText(" Nyelv  ");
      button3.setText("Kilépés");
      newGameAction.setText("Új játék");
    }
    kiir();
    saveSettings();
  }

  private boolean ment_e() {
    int result;
    Object[] valasztas = {"Mentés", "Mégse"};
    Object[] valasztasEn = {"Save", "Cancel"};
    result = JOptionPane.showOptionDialog(
            rootPane,
            (nyelv == magyar) ? "Szeretnéd menteni a partit?\n\n"
                    : "Would you like to save the game?\n\n",
            (nyelv == magyar) ? "Mentés" : "Save",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            (nyelv == magyar) ? valasztas : valasztasEn,
            (nyelv == magyar) ? valasztas[0] : valasztasEn[0]
    );
    return (result == JOptionPane.YES_OPTION);
  }

  private boolean rosszChar(String str) {
    return str.contains("/") || str.contains("\\") || str.contains("\"") || str.contains(":")
            || str.contains("*") || str.contains("?") || str.contains("|") || str.contains("<") || str.contains(">");
  }

  private void elment() {
    if (akt_lepes == 0) {
      return;
    }
    klikkTorol();
    DBConnect dbconn = new DBConnect();
    if (dbconn.errno) {
      JOptionPane.showMessageDialog(null, (nyelv == magyar) ? dbconn.error : dbconn.errorEn, (nyelv == magyar) ? "Hiba!" : "Error!", JOptionPane.ERROR_MESSAGE);
      dbconn.connClose();
      return;
    }

    AssocStringArray[] datas = dbconn.getAll("SELECT nev FROM game ORDER BY date DESC", null);
    if (datas == null) {
      JOptionPane.showMessageDialog(null, (nyelv == magyar) ? dbconn.error : dbconn.errorEn, (nyelv == magyar) ? "Hiba!" : "Error!", JOptionPane.ERROR_MESSAGE);
      dbconn.connClose();
      return;
    }
    String text = "";
    int i;
    for (AssocStringArray nev : datas) {
      text += nev.get("nev") + ",\n";
    }
    String strbe;
    boolean save;
    boolean vanmar;
    int result;
    Object[] valasztas = {"Mentés", "Mégse"};
    Object[] valasztasEn = {"Save", "Cancel"};
    do {
      save = false;
      vanmar = false;
      strbe = JOptionPane.showInputDialog(rootPane,
              (nyelv == magyar) ? "Milyen néven szeretnéd az állást/partit elmenteni?\n\n"
                      + "Nem tartalmazhat \\/:*?\"<>| karaktert!\n\nA létező mentett partik:\n\n"
                      + text + "\n-Max. " + MAX_PARTI_SZAM + " lehetséges, " + (MAX_PARTI_SZAM + 1) + ". mentésénél a legrégebbi,\nvagyis a legalsó törlődik.\n"
                      + "-A létező nevű file átíródik.\n\n"
                      : "What file to would you like to save the position/game?\n\n"
                      + "Must not contain \\/:*?\"<>| character!\n\nThe existing games are:\n\n" + text + "\n-Max. " + MAX_PARTI_SZAM + " are possible, at the "
                      + (MAX_PARTI_SZAM + 1) + ". save the oldest file,\nhere at the bottom will be deleted.\n"
                      + "-The file with existing name will be overwritten.\n\n",
              (nyelv == magyar) ? "sakkparti" : "chessgame"
      );
      if (strbe != null && strbe.length() > 50) {
        JOptionPane.showMessageDialog(null,
                (nyelv == magyar) ? "Túl hosszú név!"
                        : "Too long name!"
        );
      }
      else if (strbe != null && rosszChar(strbe)) {
        JOptionPane.showMessageDialog(null,
                (nyelv == magyar) ? "Nem tartalmazhat \\/:*?\"<>| karaktert!"
                        : "Must not contain \\/:*?\"<>| character!"
        );
      }
      else if (strbe != null) {
        for (AssocStringArray nev : datas) {
          if (nev.get("nev").equals(strbe)) {
            vanmar = true;
            break;
          }
        }
        if (vanmar) {
          result = JOptionPane.showOptionDialog(
                  rootPane,
                  (nyelv == magyar) ? "Az adatbázisban \"" + strbe + "\" néven már létezik egy parti.\n\n"
                          + "Szeretnéd felülírni?\n\n"
                          : "In the name \"" + strbe + "\" in the database already exists a game.\n\n"
                          + "Would you like to owerwrite?\n\n",
                  (nyelv == magyar) ? "Mentés" : "Save",
                  JOptionPane.YES_NO_OPTION,
                  JOptionPane.QUESTION_MESSAGE,
                  null,
                  (nyelv == magyar) ? valasztas : valasztasEn,
                  (nyelv == magyar) ? valasztas[1] : valasztasEn[1]
          );
          save = result == JOptionPane.YES_OPTION;
        } else {
          save = true;
        }
      }
    } while (strbe != null && ((strbe.length() > 50 || strbe.length() == 0) || rosszChar(strbe) || !save));
    if (strbe == null || strbe.length() == 0) {
      dbconn.connClose();
      return;
    }

    if (!vanmar) {
      if (datas.length == MAX_PARTI_SZAM) {
        dbconn.deleteData("DELETE FROM game WHERE nev=?", new String[]{datas[MAX_PARTI_SZAM - 1].get("nev")});
        if (dbconn.errno) {
          JOptionPane.showMessageDialog(null, (nyelv == magyar) ? dbconn.error : dbconn.errorEn, (nyelv == magyar) ? "Hiba!" : "Error!", JOptionPane.WARNING_MESSAGE);
          dbconn.connClose();
          return;
        }
      }
    }
//    Date date = new Date(); // deprecated
    Calendar currentDate = Calendar.getInstance();
//    String dateTime = DATE_FORMAT.format(date);
    String dateTime = DATE_FORMAT.format(currentDate.getTime());
    String lepesek = "";
    String pontok = "";
    for (i = 1; i <= akt_lepes; i++) {
      if (i != 1) {
        lepesek += ",";
        pontok += ",";
      }
      int tiszt = (CALCULATOR.ALLAS[i].babu == GY && CALCULATOR.ALLAS[i].hova[1] % 7 == 0) ? CALCULATOR.ALLAS[i].TABLA[CALCULATOR.ALLAS[i].hova[0]][CALCULATOR.ALLAS[i].hova[1]].babu : URES;
      lepesek += "" + CALCULATOR.ALLAS[i].honnan[0] + CALCULATOR.ALLAS[i].honnan[1] + CALCULATOR.ALLAS[i].hova[0] + CALCULATOR.ALLAS[i].hova[1] + tiszt;
      pontok += CALCULATOR.ALLAS[i].pontszam;
    }
    if (vanmar) {
      dbconn.updateData("UPDATE game SET date=?, lepes=?, pont=? WHERE nev=?",
              new String[]{dateTime, lepesek, pontok, strbe});
      if (dbconn.errno) {
        JOptionPane.showMessageDialog(null, (nyelv == magyar) ? dbconn.error : dbconn.errorEn, (nyelv == magyar) ? "Hiba!" : "Error!", JOptionPane.ERROR_MESSAGE);
        dbconn.connClose();
        return;
      }
    } else {
      dbconn.insertData(new String[]{"nev", "date", "lepes", "pont"}, new String[]{strbe, dateTime, lepesek, pontok}, "game");
      if (dbconn.errno) {
        JOptionPane.showMessageDialog(null, (nyelv == magyar) ? dbconn.error : dbconn.errorEn, (nyelv == magyar) ? "Hiba!" : "Error!", JOptionPane.ERROR_MESSAGE);
        dbconn.connClose();
        return;
      }
    }
    dbconn.connClose();

    result = JOptionPane.showOptionDialog(
            rootPane,
            (nyelv == magyar) ? "Sikerült elmenteni az állástaz adatbázisba.\n\n"
                    + "Szeretnéd menteni a partit txt formában is?\n\n"
                    + "Így a lépéseket akár ki is tudod nyomtatni\nés igazi sakktáblán is visszajátszhatod a partit.\n\n"
                    : "Would you like to save the game in txt format?\n\n"
                    + "So you can print it as well\nand replay the game on the real chessboard.",
            (nyelv == magyar) ? "Mentés" : "Save",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            (nyelv == magyar) ? valasztas : valasztasEn,
            (nyelv == magyar) ? valasztas[0] : valasztasEn[0]
    );
    if (result != JOptionPane.YES_OPTION) {
      return;
    }

    JFileChooser fileChooser = new JFileChooser();
    fileChooser.setFileFilter(new FileNameExtensionFilter("txt", "txt"));
//    fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
    fileChooser.setDialogTitle((nyelv == magyar) ? "Válassz ki egy mappát, amibe mentheted a " + strbe + ".txt-t!"
            : "Specify a directory to save the " + strbe + ".txt!");

    String fileName;
    File fajl = new File("");
    String fileAbsolutPath = "";
    boolean rendben = true;
    FileWriter fw = null;
    try {
      boolean rosszChar;
      do {
        save = false;
        fileChooser.setSelectedFile(new File(strbe + ".txt"));
        int userSelection = fileChooser.showSaveDialog(this);
        if (userSelection == JFileChooser.CANCEL_OPTION) {
          return;
        } else if (userSelection != JFileChooser.APPROVE_OPTION) {
//    else if (userSelection == JFileChooser.ABORT || userSelection == JFileChooser.ERROR){
          JOptionPane.showMessageDialog(
                  rootPane,
                  (nyelv == magyar) ? "Nem sikerült írni a file-t"
                          : "It was not successful to write the file",
                  (nyelv == magyar) ? "Hiba!"
                          : "Error!",
                  JOptionPane.ERROR_MESSAGE
          );
          return;
        }
        fileName = fileChooser.getSelectedFile().getName();
        if ((rosszChar =rosszChar(fileName))) {
          JOptionPane.showMessageDialog(null,
                  (nyelv == magyar) ? "Nem tartalmazhat \\/:*?\"<>| karaktert!"
                          : "Must not contain \\/:*?\"<>| character!"
          );
        } else {
          fileAbsolutPath = fileChooser.getSelectedFile().getCanonicalPath();
          if (!fileAbsolutPath.substring(fileAbsolutPath.length() - 4).equals(".txt")) {
            fileAbsolutPath += ".txt";
          }
          fajl = new File(fileAbsolutPath);
          if (fajl.exists()) {
            result = JOptionPane.showOptionDialog(
                    rootPane,
                    (nyelv == magyar) ? "A txt file ezen a néven már létezik.\n\n"
                            + "Szeretnéd felülírni?\n\n"
                            : "The txt file in this name already exists.\n\n"
                            + "Would you like to owerwrite?\n\n",
                    (nyelv == magyar) ? "Mentés" : "Save",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    (nyelv == magyar) ? valasztas : valasztasEn,
                    (nyelv == magyar) ? valasztas[1] : valasztasEn[1]
            );
            save = result == JOptionPane.YES_OPTION;
          } else {
            save = true;
          }
        }
      } while (rosszChar || !save);
    
      fajl.setReadable(true, false);
      fajl.setWritable(true, false);
      String str = "";
      for (i = 1; i <= akt_lepes; i++) {
        if (i % 2 == 1) {
          str += (i + 1) / 2 + ". ";
        } else {
          str += ",  ";
        }
        String text1;
        if (CALCULATOR.ALLAS[i].babu == K && CALCULATOR.ALLAS[i].honnan[0] - CALCULATOR.ALLAS[i].hova[0] == 2) {
          text1 = "0-0-0";
        } else if (CALCULATOR.ALLAS[i].babu == K && CALCULATOR.ALLAS[i].hova[0] - CALCULATOR.ALLAS[i].honnan[0] == 2) {
          text1 = " 0-0 ";
        } else {
          text1 = babkod(CALCULATOR.ALLAS[i].babu)
                  + betukod(CALCULATOR.ALLAS[i].honnan[0]) + szamkod(CALCULATOR.ALLAS[i].honnan[1])
                  + utes_e_ment(i) + betukod(CALCULATOR.ALLAS[i].hova[0]) + szamkod(CALCULATOR.ALLAS[i].hova[1]);
        }
        if (CALCULATOR.ALLAS[i].babu == GY && (CALCULATOR.ALLAS[i].hova[1] == 0 || CALCULATOR.ALLAS[i].hova[1] == 7)) {
          text1 += babkod(CALCULATOR.ALLAS[i].TABLA[CALCULATOR.ALLAS[i].hova[0]][CALCULATOR.ALLAS[i].hova[1]].babu);
        }
        if (CALCULATOR.ALLAS[i].v_sakk || CALCULATOR.ALLAS[i].s_sakk) {
          if (CALCULATOR.ALLAS[i].patt) {
            if (nyelv == magyar) {
              text1 += " sakk-matt";
            } else {
              text1 += " checkmate";
            }
          } else {
            text1 += "+";
          }
        } else if (CALCULATOR.ALLAS[i].patt) {
          if (nyelv == magyar) {
            text1 += " patt";
          } else {
            text1 += " stalemate";
          }
        }
        if (CALCULATOR.ALLAS[i].allasIsm == 2) {
          text1 += "\r\n1/2-1/2";
        }

        str += text1;
        if (i % 2 == 0) {
          str += "\r\n";//""+(char)13+(char)10;
        }
      }
      fw = new FileWriter(fajl);
      fw.write(str);

    } catch (IOException e) {
      JOptionPane.showMessageDialog(
              null,
              (nyelv == magyar) ? "Nem sikerült írni a file-t"
                      : "It was not successful to write the file",
              (nyelv == magyar) ? "Hiba!"
                      : "Error!",
              JOptionPane.ERROR_MESSAGE);
      rendben = false;
    } finally {
      try {
        if (fw != null) {
          fw.close();
        }
      } catch (IOException e) {
        JOptionPane.showMessageDialog(null,
                (nyelv == magyar) ? "Hiba! Nem sikerült bezárni a file-t."
                        : "Error! Closing the file was not succeessful."
        );
        rendben = false;
      }
    }
    if (rendben) {

      JOptionPane.showMessageDialog(null,
              (nyelv == magyar) ? "Sikerült a Parti mentése a\n" + fileAbsolutPath + "\nfile-ba!\n"
                      : "Saving the Game to the\n" + fileAbsolutPath + "\nfile was successful!\n",
              NINCS,
              JOptionPane.INFORMATION_MESSAGE);
    } else {
      JOptionPane.showMessageDialog(
              null,
              (nyelv == magyar) ? "Nem sikerült a Parti mentése a media mappában levő\n" + strbe + ".txt file-ba!"
                      : "Saving the Game to the " + strbe + ".txt file\n"
                      + "in the media folder was not successful!",
              (nyelv == magyar) ? "Hiba!" : "Error!",
              JOptionPane.ERROR_MESSAGE);
    }

  }

  private void betolt() {
    klikkTorol();
    DBConnect dbconn = new DBConnect();
    if (dbconn.errno) {
      JOptionPane.showMessageDialog(null, (nyelv == magyar) ? dbconn.error : dbconn.errorEn, (nyelv == magyar) ? "Hiba!" : "Error!", JOptionPane.ERROR_MESSAGE);
      dbconn.connClose();
      return;
    }
    AssocStringArray[] datas = dbconn.getAll("SELECT nev FROM game ORDER BY date DESC", null);
    if (datas == null) {
      JOptionPane.showMessageDialog(null, (nyelv == magyar) ? dbconn.error : dbconn.errorEn, (nyelv == magyar) ? "Hiba!" : "Error!", JOptionPane.ERROR_MESSAGE);
      dbconn.connClose();
      return;
    }

    if (datas.length != 0) {
      Object games[] = new Object[datas.length];
      for (int i = 0; i < games.length; i++) {
        games[i] = (Object) datas[i].get("nev");
      }
      String str;
      boolean van = false;
      do {
        str = (String) JOptionPane.showInputDialog(rootPane,
                (nyelv == magyar) ? "Melyik játszmát szeretnéd betölteni?\n\nA létező mentett partik:\n\n"
                        : "What game would you like to load?\n\n"
                        + "The existing games are:\n\n",
                (nyelv == magyar) ? "Játszmák betöltése" : "Loading a game",
                JOptionPane.QUESTION_MESSAGE,
                null,
                games,
                datas[0].get(0)
        );
        if (str == null) {
          dbconn.connClose();
          return;
        }
        for (AssocStringArray nev : datas) {
          if (nev.get("nev").equals(str)) {
            van = true;
            break;
          }
        }
        if (!van) {
          JOptionPane.showMessageDialog(null,
                  (nyelv == magyar) ? "Nincs ilyen nevű mentett parti!"
                          : "There is no saved game with this name!"
          );
        }
      } while (!van);

      AssocStringArray allas = dbconn.getRow(
              "SELECT lepes, pont"
              + " FROM game"
              + " WHERE nev=?",
              new String[]{str});
      if (allas == null) {
        JOptionPane.showMessageDialog(null, (nyelv == magyar) ? dbconn.error : dbconn.errorEn, (nyelv == magyar) ? "Hiba!" : "Error!", JOptionPane.ERROR_MESSAGE);
        dbconn.connClose();
        return;
      } else if (allas.size() == 0) {
        JOptionPane.showMessageDialog(null, (nyelv == magyar) ? "Nincs ilyen nevű mentett parti!" : "There is no saved game with this name!", (nyelv == magyar) ? "Hiba!" : "Error!", JOptionPane.ERROR_MESSAGE);
        // itt már hiba, ha NINCS ilyen nevű az adatbázisban, hiszen korábban már ellenőriztük, hogy van!!!
        dbconn.connClose();
        return;
      }
      CALCULATOR.lepes = 0;
      gepjatszik = BLACK;
      {
        vilagos = 1;
        sotet = 0;
      }

      for (int ii = 0; ii < 8; ii++) {
        lent[ii].setText("" + (char) (ii + ((int) 'a')));
        fent[ii].setText("" + (char) (ii + ((int) 'a')));
      }

      for (int ii = 0; ii < 8; ii++) {
        oldalt[ii].setText("" + (1 + ii));
        oldalt2[ii].setText("" + (1 + ii));
      }
      CALCULATOR.alapBeall();
      String[] lepesek = allas.get("lepes").split(",");
      String[] pontok = allas.get("pont").split(",");
      if (pontok.length != lepesek.length) {
        hiba = NOT_VALID;
        JOptionPane.showMessageDialog(
                null,
                (nyelv == magyar) ? "Sérült az adatbázis, nem sikerült a parti betöltése!"
                        : "The database is corrupt, loading the game was not successful!",
                (nyelv == magyar) ? "Hiba!" : "Error!",
                JOptionPane.WARNING_MESSAGE
        );
        dbconn.connClose();
        return;
      }
      for (int i = 0; i < lepesek.length; i++) {
        if (lepesek[i].length() == 5) {
          lep_meghat((int) lepesek[i].charAt(0) - NULLA, (int) lepesek[i].charAt(1) - NULLA, (int) lepesek[i].charAt(2) - NULLA, (int) lepesek[i].charAt(3) - NULLA, false, (int) lepesek[i].charAt(4) - NULLA);
        } else {
          hiba = NOT_VALID;
        }
        if (!hiba.equals(NINCS)) {
          JOptionPane.showMessageDialog(
                  null,
                  (nyelv == magyar) ? "Sérült az adatbázis, nem sikerült a parti betöltése!"
                          : "The database is corrupt, loading the game was not successful!",
                  (nyelv == magyar) ? "Hiba!" : "Error!",
                  JOptionPane.WARNING_MESSAGE
          );
          dbconn.connClose();
          return;
        }
        try {
          CALCULATOR.ALLAS[CALCULATOR.lepes].pontszam = Integer.parseInt(pontok[i]);
        } catch (NumberFormatException e) {
          JOptionPane.showMessageDialog(
                  null,
                  (nyelv == magyar) ? "Sérült az adatbázis, nem sikerült a parti betöltése!"
                          : "The database is corrupt, loading the game was not successful!",
                  (nyelv == magyar) ? "Hiba!" : "Error!",
                  JOptionPane.WARNING_MESSAGE
          );
          dbconn.connClose();
          return;
        }
      }
    } else {
      JOptionPane.showMessageDialog(null,
              (nyelv == magyar) ? "Nincs mentett parti!"
                      : "There is no any saved game!"
      );
      dbconn.connClose();
      return;
    }
    dbconn.connClose();
    szinvalasztas();
  }

  private void klikkTorol() {
    if (klikk == 1) {
      klikk = 0;
      sakktabla[Xhonnan][Yhonnan].setBackground(elozoColor);
    }
  }

  private void szinvalasztas() {
    klikkTorol();
    akt_lepes = CALCULATOR.lepes;
    kiir();
    String szinitt;
    if (CALCULATOR.lepes % 2 == 0) {
      szinitt = (nyelv == magyar) ? "VILÁGOS" : "WHITE";
    } else {
      szinitt = (nyelv == magyar) ? "SÖTÉT" : "BLACK";
    }
    int result;
    Object[] valasztas = {"VILÁGOS", "SÖTÉT"};
    Object[] valasztasEn = {"WHITE", "BLACK"};
    result = JOptionPane.showOptionDialog(
            rootPane,
            (nyelv == magyar) ? "Melyik színnel szeretnél játszani?\n\n"
                    + "***Az állásban " + szinitt + " következik.***\n\n"
                    : "What color would you like to play?\n\n"
                    + "***In the position it is " + szinitt + "'s turn now.***\n\n",
            (nyelv == magyar) ? "Színválasztás" : "Choose color!",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            (nyelv == magyar) ? valasztas : valasztasEn,
            (nyelv == magyar) ? valasztas[0] : valasztasEn[0]
    );
    if (result != JOptionPane.NO_OPTION) {

      gepjatszik = BLACK;
      vilagos = 1;
      sotet = 0;

      for (int ii = 0; ii < 8; ii++) {
        lent[ii].setText("" + (char) (ii + ((int) 'a')));
        fent[ii].setText("" + (char) (ii + ((int) 'a')));
      }

      for (int ii = 0; ii < 8; ii++) {
        oldalt[ii].setText("" + (1 + ii));
        oldalt2[ii].setText("" + (1 + ii));
      }

    } else {

      gepjatszik = WHITE;
      vilagos = 0;
      sotet = 1;

      for (int ii = 0; ii < 8; ii++) {
        lent[7 - ii].setText("" + (char) (((int) 'a') + ii));
        fent[7 - ii].setText("" + (char) (((int) 'a') + ii));
      }

      for (int ii = 0; ii < 8; ii++) {
        oldalt[7 - ii].setText("" + (char) (((int) '1') + ii));
        oldalt2[7 - ii].setText("" + (char) (((int) '1') + ii));
      }

    }

    kiir();
    valasztas = new Object[]{"Erős", "Gyenge"};
    valasztasEn = new Object[]{"Advanced", "Beginner"};
    result = JOptionPane.showOptionDialog(
            rootPane,
            (nyelv == magyar) ? "Erős vagy Gyenge fokozat?\n\n"
                    : "Advanced or beginner?\n\n",
            (nyelv == magyar) ? "Fokozat-választás" : "Choose degree!",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            (nyelv == magyar) ? valasztas : valasztasEn,
            (nyelv == magyar) ? valasztas[1] : valasztasEn[1]
    );
    if (result == JOptionPane.YES_OPTION) {
      CALCULATOR.erosseg = EROS;
    } else {
      CALCULATOR.erosseg = GYENGE;
    }
    if (CALCULATOR.lepes == 0 && CALCULATOR.lepes % 2 == vilagos) {
      int x, y, i, j;
      int z = (int) (Math.random() * 6);
      switch (z) {
        case 0:
          x = 4;
          y = 1;
          i = 4;
          j = 3;
          break;
        case 1:
          x = 3;
          y = 1;
          i = 3;
          j = 3;
          break;
        case 2:
          x = 2;
          y = 1;
          i = 2;
          j = 3;
          break;
        case 3:
          x = 6;
          y = 0;
          i = 5;
          j = 2;
          break;
        case 4:
          x = 4;
          y = 1;
          i = 4;
          j = 3;
          break;
        default:
          x = 3;
          y = 1;
          i = 3;
          j = 3;
          break;
      }
      CALCULATOR.kov_allas(x, y, i, j);
//     akt_lepes=CALCULATOR.lepes;
    }
    akt_lepes = CALCULATOR.lepes;
    kiir();
    if (CALCULATOR.lepes % 2 == vilagos && !CALCULATOR.ALLAS[CALCULATOR.lepes].patt && CALCULATOR.ALLAS[CALCULATOR.lepes].allasIsm != 2) {
      geplep();
    }
  }

  private boolean valid(int data) {
    return data >= 0 && data <= 7;
  }

  private void lep_meghat(int xElozo, int yElozo, int x, int y, boolean kiirMent, int tiszt) {
    f = CALCULATOR.hanybab_f();
    hiba = NINCS;
    CALCULATOR.hibaSanc = false;
    CALCULATOR.an_pass = false;
    if (!valid(xElozo) || !valid(yElozo) || !valid(x) || !valid(y) || (tiszt > F) || (tiszt < V && tiszt != URES)) {
      hiba = NOT_VALID;
      return;
    }

    if (CALCULATOR.ALLAS[CALCULATOR.lepes].TABLA[xElozo][yElozo].babu == K && xElozo == 4
            && ((yElozo == 0 && y == 0 && CALCULATOR.ALLAS[CALCULATOR.lepes].TABLA[xElozo][yElozo].szin == vilagos)
            || (yElozo == 7 && y == 7 && CALCULATOR.ALLAS[CALCULATOR.lepes].TABLA[xElozo][yElozo].szin == SOT)) && x == 6) {
      CALCULATOR.sancol();
    } else if (CALCULATOR.ALLAS[CALCULATOR.lepes].TABLA[xElozo][yElozo].babu == K && xElozo == 4
            && ((yElozo == 0 && y == 0 && CALCULATOR.ALLAS[CALCULATOR.lepes].TABLA[xElozo][yElozo].szin == vilagos)
            || (yElozo == 7 && y == 7 && CALCULATOR.ALLAS[CALCULATOR.lepes].TABLA[xElozo][yElozo].szin == SOT)) && x == 2) {
      CALCULATOR.hosszusancol();
    } else {
      if (CALCULATOR.ALLAS[CALCULATOR.lepes].TABLA[xElozo][yElozo].szin
              == CALCULATOR.ALLAS[CALCULATOR.lepes].TABLA[x][y].szin) {
        hiba = HIBA_SAJAT_BABU;
      } else if (CALCULATOR.szabalyos(xElozo, yElozo, x, y)) {
        CALCULATOR.honnan[0] = xElozo;
        CALCULATOR.honnan[1] = yElozo;
        CALCULATOR.hova[0] = x;
        CALCULATOR.hova[1] = y;
      } else {
        hiba = HIBA_NINCS_ILYEN;
      }
//an_pass_pr=an_pass=an_passant(honnan[0],honnan[1],hova[0],hova[1]);***********
      an_pass_pr = CALCULATOR.an_pass;
    }
    if (CALCULATOR.hibaSanc) {
      hiba = HIBA_SANC;
    }
    if (hiba.equals(NINCS)) {
      CALCULATOR.kov_allas(CALCULATOR.honnan[0], CALCULATOR.honnan[1], CALCULATOR.hova[0], CALCULATOR.hova[1]);
      //      akt_lepes=CALCULATOR.lepes;***************************

      if (CALCULATOR.lepes % 2 == 0) {
        CALCULATOR.kir[0] = CALCULATOR.ALLAS[CALCULATOR.lepes].kiraly_s[0];
        CALCULATOR.ellKir[0] = CALCULATOR.ALLAS[CALCULATOR.lepes].kiraly_v[0];
        CALCULATOR.kir[1] = CALCULATOR.ALLAS[CALCULATOR.lepes].kiraly_s[1];
        CALCULATOR.ellKir[1] = CALCULATOR.ALLAS[CALCULATOR.lepes].kiraly_v[1];
      } else {
        CALCULATOR.kir[0] = CALCULATOR.ALLAS[CALCULATOR.lepes].kiraly_v[0];
        CALCULATOR.ellKir[0] = CALCULATOR.ALLAS[CALCULATOR.lepes].kiraly_s[0];
        CALCULATOR.kir[1] = CALCULATOR.ALLAS[CALCULATOR.lepes].kiraly_v[1];
        CALCULATOR.ellKir[1] = CALCULATOR.ALLAS[CALCULATOR.lepes].kiraly_s[1];
      }
      if (CALCULATOR.sakk(CALCULATOR.kir[0], CALCULATOR.kir[1])) {
        hiba = HIBA_SAKK;
        if (CALCULATOR.ALLAS[CALCULATOR.lepes].babu == K) {
          hiba = HIBA_SAKKBA_LEP;
        }
        CALCULATOR.lepes--;
        akt_lepes = CALCULATOR.lepes;
      }
      if (hiba.equals(NINCS)) {
        if (CALCULATOR.ALLAS[CALCULATOR.lepes].babu == GY && (CALCULATOR.hova[1] == 0 || CALCULATOR.hova[1] == 7)) {
          atvaltoz(CALCULATOR.hova[0], CALCULATOR.hova[1], tiszt);
        }
        if (CALCULATOR.sakk(CALCULATOR.ellKir[0], CALCULATOR.ellKir[1])) {
          if (CALCULATOR.ALLAS[CALCULATOR.lepes].szin == VIL) {
            CALCULATOR.ALLAS[CALCULATOR.lepes].s_sakk = true;
          } else {
            CALCULATOR.ALLAS[CALCULATOR.lepes].v_sakk = true;
          }
        }
        CALCULATOR.ALLAS[CALCULATOR.lepes].patt = CALCULATOR.patt_e();

        akt_lepes = CALCULATOR.lepes;//*************************
        CALCULATOR.ALLAS[CALCULATOR.lepes].pontszam = CALCULATOR.ALLAS[CALCULATOR.lepes - 1].pontszam;
        kiir();

        if ((CALCULATOR.ALLAS[CALCULATOR.lepes].patt || CALCULATOR.ALLAS[CALCULATOR.lepes].allasIsm == 2) && kiirMent) {
          String text;
          String grat = (nyelv == magyar) ? "\n\n        GRATULÁLOK!\n\n"
                  : "\n\n          CONGRATULATIONS!\n\n";
          if (CALCULATOR.ALLAS[CALCULATOR.lepes].allasIsm == 2) {
            text = HIBA_3X;
          } else if (CALCULATOR.ALLAS[CALCULATOR.lepes].s_sakk) {
            text = HIBA_MATTS + grat;
          } else if (CALCULATOR.ALLAS[CALCULATOR.lepes].v_sakk) {
            text = HIBA_MATTV + grat;
          } else {
            text = HIBA_PATT;
          }
          JOptionPane.showMessageDialog(rootPane, text, NINCS, JOptionPane.INFORMATION_MESSAGE);
          if (ment_e()) {
            elment();
          }
        }
//        else message.setText("I am thinking...");//****************************
      }
    }
    if (!hiba.equals(NINCS)) {
      JOptionPane.showMessageDialog(null, hiba, NINCS, JOptionPane.WARNING_MESSAGE);
    }
    if (CALCULATOR.lepes >= MAX_LEP + sotet) {
      int result;
      Object[] valasztas = {"Új játék", "Visszalépés"};
      Object[] valasztasEn = {"New Game", "Undo"};
      result = JOptionPane.showOptionDialog(
              rootPane,
              (nyelv == magyar) ? "A programban a maximális lépésszám = " + MAX_LEP / 2 + ".\n"
                      + "Most túllépted ezt.\n\n"
                      : "In the program the maximum possible number of moves = " + MAX_LEP / 2 + ".\n"
                      + "You have exceeded that now.\n\n",
              (nyelv == magyar) ? "Maximális lépésszám" : "Maximum number of moves",
              JOptionPane.YES_NO_OPTION,
              JOptionPane.QUESTION_MESSAGE,
              null,
              (nyelv == magyar) ? valasztas : valasztasEn,
              (nyelv == magyar) ? valasztas[1] : valasztasEn[1]
      );
      CALCULATOR.lepes--;
      akt_lepes = CALCULATOR.lepes;
      if (result == JOptionPane.YES_OPTION) {
        if (ment_e()) {
          elment();
        }
        setupNewGame();
      }
      kiir();
    }

  }

  private void geplep() {
    if (!CALCULATOR.kezd) {
      return;
    }
// message.setText("I am thinking...");
    f = CALCULATOR.hanybab_f();
    hiba = NINCS;
    CALCULATOR.hibaSanc = false;
    CALCULATOR.an_pass = false;

    pontszam = CALCULATOR.kezdoallas(f);
    an_pass_pr = CALCULATOR.an_pass = CALCULATOR.an_passant(CALCULATOR.honnan[0], CALCULATOR.honnan[1], CALCULATOR.hova[0], CALCULATOR.hova[1]);
    CALCULATOR.kov_allas(CALCULATOR.honnan[0], CALCULATOR.honnan[1], CALCULATOR.hova[0], CALCULATOR.hova[1]);
    akt_lepes = CALCULATOR.lepes;

    if (CALCULATOR.lepes % 2 == 0) {
      CALCULATOR.ellKir[0] = CALCULATOR.ALLAS[CALCULATOR.lepes].kiraly_v[0];
      CALCULATOR.ellKir[1] = CALCULATOR.ALLAS[CALCULATOR.lepes].kiraly_v[1];
    } else {
      CALCULATOR.ellKir[0] = CALCULATOR.ALLAS[CALCULATOR.lepes].kiraly_s[0];
      CALCULATOR.ellKir[1] = CALCULATOR.ALLAS[CALCULATOR.lepes].kiraly_s[1];
    }
    if (CALCULATOR.ALLAS[CALCULATOR.lepes].babu == GY && (CALCULATOR.ALLAS[CALCULATOR.lepes].hova[1] == 0 || CALCULATOR.ALLAS[CALCULATOR.lepes].hova[1] == 7)) {
      CALCULATOR.atvaltozik(CALCULATOR.ALLAS[CALCULATOR.lepes].hova[0], CALCULATOR.ALLAS[CALCULATOR.lepes].hova[1]);
    }
    if (CALCULATOR.sakk(CALCULATOR.ellKir[0], CALCULATOR.ellKir[1])) {
      if (CALCULATOR.ALLAS[CALCULATOR.lepes].szin == VIL) {
        CALCULATOR.ALLAS[CALCULATOR.lepes].s_sakk = true;
      } else {
        CALCULATOR.ALLAS[CALCULATOR.lepes].v_sakk = true;
      }
    }
    CALCULATOR.ALLAS[CALCULATOR.lepes].patt = CALCULATOR.patt_e();

    int pont = (pontszam < 10000) ? pontszam : pontszam - f;
    pont = (pont > -10000) ? pont : pont + f;
    CALCULATOR.ALLAS[CALCULATOR.lepes].pontszam = pont;
    kiir();

    if (CALCULATOR.ALLAS[CALCULATOR.lepes].patt || CALCULATOR.ALLAS[CALCULATOR.lepes].allasIsm == 2) {
      String text;
      if (CALCULATOR.ALLAS[CALCULATOR.lepes].allasIsm == 2) {
        text = HIBA_3X;
      } else if (CALCULATOR.ALLAS[CALCULATOR.lepes].s_sakk) {
        text = HIBA_MATTS;
      } else if (CALCULATOR.ALLAS[CALCULATOR.lepes].v_sakk) {
        text = HIBA_MATTV;
      } else {
        text = HIBA_PATT;
      }

      JOptionPane.showMessageDialog(null, text, NINCS, JOptionPane.INFORMATION_MESSAGE);
      if (ment_e()) {
        elment();
      }
    }

  }

}
