
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

public class ViewGraph extends JPanel {

    private BufferedImage image;
    private int xLine = 0;
    private int debounce = 0; // Counter to slow down repaint attempts
    private Color[] colors = { 
        Constants.BLUE, Constants.GREEN, Constants.GRAY, 
        Constants.RED, Constants.YELLOW, 
    };

    public ViewGraph() {
        this.setBackground(Constants.BACKGROUND);
        this.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Constants.BORDER));
        image = new BufferedImage(413, 51, BufferedImage.TYPE_INT_ARGB);
    }

    public void reset() {
        image = new BufferedImage(413, 51, BufferedImage.TYPE_INT_ARGB);
        xLine = 0;
        debounce = 0;
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, null); // See Javadoc for info on the parameters            
    }

    public int[] statsAsPercentage(int[] counters) {
        int[] percents = new int[counters.length-1];
        for (int i=1; i<counters.length; i++) {
            percents[i-1] = (int) Math.round((double) counters[i] / (double) counters[0] * image.getHeight());
        }
        return percents;
    }

    public void updateImage(int[] counters) {
        // Slow down rendering (loop speed is too fast for graph)
        if (debounce < 3) {
            debounce += 1;
            return;
        }

        debounce = 0;
        xLine++;
        
        // Get stats and rearrange else the graph will not color logically
        int[] percents = statsAsPercentage(counters);
        int[] colorPixels = { percents[0], percents[3], percents[4], percents[2], percents[1] };

        // Replace image with new one to extend if we are at the end
        if (xLine >= image.getWidth()) {    
            BufferedImage subimage = image.getSubimage(1, 0, image.getWidth()-1, image.getHeight());
            image = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = image.createGraphics();
            g2d.drawImage(subimage, 0, 0, null);
            g2d.dispose();
            xLine = image.getWidth()-1;
        }

        // Draw a new line of colors
        for (int i=0; i<image.getHeight(); i++) {
            for (int j=0; j<colorPixels.length; j++) {
                if (colorPixels[j] > 0) {
                    image.setRGB(xLine, i, colors[j].getRGB());
                    colorPixels[j]--;
                    break;
                }
            }
        }
    }
}