
package chesschamp;

import javax.swing.*;
import static javax.swing.JFrame.EXIT_ON_CLOSE;

/**
 *
 * @author lajos
 */
public class ChessChamp {

  public static void main(String[] args) {

    Runnable r = () -> {
      ChessGui cg = new ChessGui();
//      cg.setTitle("ChessChamp");
//      cg.add(cg.getGui());
//      // Ensures JVM closes after frame(s) closed and
//      // all non-daemon threads are finished
//      cg.setDefaultCloseOperation(EXIT_ON_CLOSE);
//      // See http://stackoverflow.com/a/7143398/418556 for demo.
//      cg.setLocationByPlatform(true);
//      // ensures the frame is the minimum size it needs to be
//      // in order display the components within it
//      cg.pack();
//      cg.setChessBoardSize();
//      // ensures the minimum size is enforced.
//      cg.setMinimumSize(cg.getSize());
//      cg.setLocationRelativeTo(cg);
////      cg.setResizable(false);
//      cg.setVisible(true);
//      cg.setupNewGame();
    };
    // Swing GUIs should be created and updated on the EDT
    // http://docs.oracle.com/javase/tutorial/uiswing/concurrency
    SwingUtilities.invokeLater(r);
  }

}
