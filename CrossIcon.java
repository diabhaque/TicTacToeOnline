import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.Icon;
/**
 * This class generates the Cross Icon to show when player one presses a box
 * @author Diabul Haque
 * @version 1.0
 */
public class CrossIcon implements Icon{

	private int width = 100;
    private int height = 100;

    private BasicStroke stroke = new BasicStroke(4);
    
    /**
     * This method paints the icon
     */
    public void paintIcon(Component c, Graphics g, int x, int y) {
        Graphics2D g2d = (Graphics2D) g.create();

        g2d.setColor(Color.WHITE);
        g2d.fillRect(x +1 ,y + 1,width -2 ,height -2);

        g2d.setColor(Color.BLACK);
        g2d.drawRect(x +1 ,y + 1,width -2 ,height -2);

        g2d.setColor(Color.RED);

        g2d.setStroke(stroke);
        g2d.drawLine(x +10, y + 10, x + width -10, y + height -10);
        g2d.drawLine(x +10, y + height -10, x + width -10, y + 10);

        g2d.dispose();
    }
    
    /**
     * This method returns the width of the icon
     */
    public int getIconWidth() {
        return width;
    }
    /**
     * This method returns the height of the icon
     */
    public int getIconHeight() {
        return height;
    }

}
