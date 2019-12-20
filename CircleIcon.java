import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;

import javax.swing.Icon;

/**
 * This class generates the Circle Icon to show when player two presses a box
 * @author Diabul Haque
 * @version 1.0
 */
public class CircleIcon implements Icon{

	private int width = 100;
    private int height = 100;

    private BasicStroke stroke = new BasicStroke(4);

    public void paintIcon(Component c, Graphics g, int x, int y) {
        Graphics2D g2d = (Graphics2D) g.create();

        g2d.setColor(Color.WHITE);
        g2d.fillRect(x +1 ,y + 1,width -2 ,height -2);

        g2d.setColor(Color.BLACK);
        g2d.drawRect(x +1 ,y + 1,width -2 ,height -2);

        g2d.setColor(Color.RED);

        g2d.setStroke(stroke);
        
        Shape theCircle = new Ellipse2D.Double(50 - 40, 50 - 40, 2.0 * 40, 2.0 * 40);
        g2d.draw(theCircle);
        
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

