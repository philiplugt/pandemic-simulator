
import java.awt.Graphics;
import java.awt.Rectangle;

public class Barrier extends Rectangle {

    public Barrier(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    public void paint(Graphics g) {
        g.setColor(Constants.BARRIER);
        g.fillRect(x, y, width, height);
    }
}