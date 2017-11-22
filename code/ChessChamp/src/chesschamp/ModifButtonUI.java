
package chesschamp;

import java.awt.Graphics;
import javax.swing.AbstractButton;
import javax.swing.plaf.metal.MetalButtonUI;

/**
 *
 * @author lajos
 */
class ModifButtonUI extends MetalButtonUI {

  ModifButtonUI() {
    super();
  }

  @Override
  protected void paintButtonPressed(Graphics g, AbstractButton b) {
    paintText(g, b, b.getBounds(), b.getText());
    g.setColor(b.getBackground());//Az eredeti háttérszín marad a gomb színe!
    g.fillRect(0, 0, b.getSize().width, b.getSize().height);
  }

}
