import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.JPanel;

public class Graph extends JPanel implements Runnable {
	
	private final AtomicBoolean running = new AtomicBoolean(false);
	private BufferedImage image;
	private DrawingArea da;
	private int x = 0;
	
	public Graph(DrawingArea da) {
		this.da = da;
		image = new BufferedImage(413, 51, BufferedImage.TYPE_INT_ARGB);
	}

	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(image, 0, 0, null); // see javadoc for more info on the parameters            
	}
	
	@Override
	public void run() {
		
		running.set(true);
		while(running.get()) {
			
			int cRed = (int) Math.round((double) da.numInfected / (double) da.numTotal * image.getHeight());
			int cBlue = (int) Math.round((double) da.numHealthy / (double) da.numTotal * image.getHeight());
			int cYell = (int) Math.round((double) da.numAsymptomatic / (double) da.numTotal * image.getHeight());
			int cGreen = (int) Math.round((double) da.numCured / (double) da.numTotal * image.getHeight());
			int cBlack = (int) Math.round((double) da.numDead / (double) da.numTotal * image.getHeight());
			
			assert(cBlue + cYell + cRed + cGreen + cBlack < image.getHeight());
			if (x >= image.getWidth()) {	
				BufferedImage subimage = image.getSubimage(1, 0, image.getWidth()-1, image.getHeight());
		        image = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
		        Graphics2D g2d = image.createGraphics();
		        g2d.drawImage(subimage, 0, 0, null);
		        g2d.dispose();
				x = image.getWidth()-1;
			}

			for (int i = image.getHeight()-1; i >= 0; i--) {
				
				if(cYell > 0) {
					image.setRGB(x, i, new Color(255, 193, 0).getRGB());
					cYell--;
				} else {
					if(cRed > 0) {
						image.setRGB(x, i, new Color(250, 20, 65).getRGB());
						cRed--;
					} else {
						if(cBlack > 0) {
							image.setRGB(x, i, Color.BLACK.getRGB());
							cBlack--;
						} else {
							if(cGreen > 0) {
								image.setRGB(x, i, new Color(50, 205, 50).getRGB());
								cGreen--;
							} else {
								image.setRGB(x, i, new Color(30, 144, 255).getRGB());
							}
						}
					}
				}
			}
			
			repaint();
			
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			x++;
		}
	}
	
	//Pauses the thread from running
	public void end() {
		running.set(false);
	}
	
	//Resets the graph
	public void reset() {
		image = new BufferedImage(413, 51, BufferedImage.TYPE_INT_ARGB);
		x = 0;
		repaint();
	}
}
