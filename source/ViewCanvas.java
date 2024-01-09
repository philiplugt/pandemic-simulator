
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JPanel;

public class ViewCanvas extends JPanel {

    private State state;

    public ViewCanvas() {
        this.setBackground(Constants.BACKGROUND);
        this.setPreferredSize(Constants.SCREEN);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        
        for (Ball ball : state.balls) {
            ball.paint(g);
        }

        for (Barrier barrier : state.barriers) {
            barrier.paint(g);
        }
    }

    public void updateState(State state) {
        this.state = state;
    }
}