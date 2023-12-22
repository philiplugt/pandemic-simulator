import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * DrawingArea is the class that graphically displays and handles the
 * world and physics of the plebs
 * 
 * @author PvdL
 *
 */

public class DrawingArea extends JPanel implements Runnable {

	private static final int DRAW_SIZE = 600;
	private static final int BALLS = 250;
	private static final int SPEED = 1; // Note: Diagonal speed is higher than up-down 
	                                    // or left-right speed due to rounding of pixel position
	private static final int INCUBATION_TIME = 4000;
	private static final int SICK_TIME = 8000;
	private static final double DEATH_RATE = 0.2;
	
	private ArrayList<Pleb> plebs = new ArrayList<Pleb>();
	private ArrayList<Rectangle> walls = new ArrayList<Rectangle>();
	private final AtomicBoolean running = new AtomicBoolean(false);
	private ArrayList<JLabel> jl;
	private long pauseTotal = 0;
	private long pauseStart = 0;
	private long pauseEnd = 0;
	
	int numTotal = BALLS + 1;
	int numHealthy = BALLS + 1;
	int numAsymptomatic = 0; //Equal to the number of asymptomates at the start
	int numInfected = 0;
	int numCured = 0;
	int numDead = 0;
	
	public DrawingArea(ArrayList<JLabel> jl) {
		this.jl = jl;
		createPlebs(BALLS);
		this.jl.get(0).setText(Integer.toString(numTotal));
		this.jl.get(1).setText(Integer.toString(numHealthy));
		this.jl.get(2).setText(Integer.toString(numAsymptomatic));
		this.jl.get(3).setText(Integer.toString(numInfected));
		this.jl.get(4).setText(Integer.toString(numCured));
		this.jl.get(5).setText(Integer.toString(numDead));
		setBackground(new Color(255, 250, 250)); //Snow white color
		setPreferredSize(new Dimension(DRAW_SIZE, DRAW_SIZE));
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		
		for (Rectangle wl : walls) {
			g.setColor(Color.DARK_GRAY);
			g.fillRect(wl.x, wl.y, wl.width, wl.height);
		}
		
		for (Pleb pl : plebs) {
			g.setColor(pl.getColor());
			g.fillOval(pl.x, pl.y, Pleb.diameter, Pleb.diameter);
		}
	}
	
	private void createPlebs(int amount) {
		Random rd = new Random();
		plebs.add(new Pleb(10, 10, SPEED, Vitality.HEALTHY));
		for( int i = 0; i < amount; i++) {
			Pleb np = new Pleb(
					rd.nextInt(DRAW_SIZE-Pleb.diameter+1), 
					rd.nextInt(DRAW_SIZE-Pleb.diameter+1), 
					SPEED,
					Vitality.HEALTHY);
			boolean collided = false;
			for (Rectangle wl : walls) { // Check if new plebs are not placed inside rectangles
				if (collideRectangle(np, wl) > 0) {
					collided = true;
				}
			}
			
			if (!collided) {
				plebs.add(np);
			} else {
				amount++;
			}
		}
	}

	//Run thread which moves all plebs in a direction with a speed
	@Override
	public void run() {
		if (plebs.get(0).status == Vitality.HEALTHY) {
			plebs.get(0).status = Vitality.ASYMPTOMATIC;
			plebs.get(0).statusTime = System.currentTimeMillis();
			numAsymptomatic++;
			numHealthy--;
		}
		
		running.set(true);
		while (running.get()) {
			for (Pleb pl : plebs) {
				//The pleb is on the move
				pl.x = pl.x + Math.round((float) pl.vx * pl.speed);
				pl.y = pl.y + Math.round((float) pl.vy * pl.speed);
			}
			
			for (Pleb pl : plebs) {
				//Check and change the vitality status of the pleb
				infectPleb(pl);
				finishPleb(pl);
				
				//Bounce off of the edge if collided, also set location to be inside bounds
				if (pl.x < 0 && pl.y < 0) {
					pl.x = 0;
					pl.y = 0;
					pl.vx = -pl.vx;
					pl.vy = -pl.vy;
				} else if (pl.x < 0 && pl.y > DRAW_SIZE - Pleb.diameter) {
					pl.x = 0;
					pl.y = DRAW_SIZE - Pleb.diameter;
					pl.vx = -pl.vx;
					pl.vy = -pl.vy;
				} else if (pl.x > DRAW_SIZE - Pleb.diameter && pl.y < 0) {
					pl.x = DRAW_SIZE - Pleb.diameter;
					pl.y = 0;
					pl.vx = -pl.vx;
					pl.vy = -pl.vy;
				} else if (pl.x > DRAW_SIZE - Pleb.diameter && pl.y > DRAW_SIZE - Pleb.diameter) {
					pl.x = DRAW_SIZE - Pleb.diameter;
					pl.y = DRAW_SIZE - Pleb.diameter;
					pl.vx = -pl.vx;
					pl.vy = -pl.vy;
				} else if (pl.x < 0) {	
					pl.x = 0;
					pl.vx = -pl.vx;
				} else if (pl.y < 0) {
					pl.y = 0;
					pl.vy = -pl.vy;
				} else if (pl.x > DRAW_SIZE - Pleb.diameter) {
					pl.x = DRAW_SIZE - Pleb.diameter;
					pl.vx = -pl.vx;
				} else if (pl.y > DRAW_SIZE - Pleb.diameter) {
					pl.y = DRAW_SIZE - Pleb.diameter;
					pl.vy = -pl.vy;
				}
				
				//Check collision with internal walls
				for (Rectangle wl : walls) {
				
					int collideSide = collideRectangle(pl, wl);
					//System.out.println(collideSide);
					if (collideSide > 0) {
						
						//Collision! Bounce of edge
						if (collideSide == 1) {
							pl.x = wl.x - Pleb.diameter;
							pl.y = wl.y - Pleb.diameter;
							pl.vx = -pl.vx;
							pl.vy = -pl.vy;
						} else if (collideSide == 2) {
							pl.x = wl.x - Pleb.diameter;
							pl.vx = -pl.vx;
						} else if (collideSide == 3) {
							pl.x = wl.x - Pleb.diameter;
							pl.y = wl.y + wl.height;
							pl.vx = -pl.vx;
							pl.vy = -pl.vy;
						} else if (collideSide == 4) {
							pl.y = wl.y - Pleb.diameter;
							pl.vy = -pl.vy;
						} else if (collideSide == 6) {
							pl.y = wl.y + wl.height;
							pl.vy = -pl.vy;
						} else if (collideSide == 7) {
							pl.x = wl.x + wl.width;
							pl.y = wl.y - Pleb.diameter;
							pl.vx = -pl.vx;
							pl.vy = -pl.vy;
						} else if (collideSide == 8) {
							pl.x = wl.x + wl.width;
							pl.vx = -pl.vx;
						} else if (collideSide == 9) {
							pl.x = wl.x + wl.width;
							pl.y = wl.y + wl.height;
							pl.vx = -pl.vx;
							pl.vy = -pl.vy;
						} else {
							System.out.println("Invalid object position detected");
						}
					}
				}
				
				//Check for collision with other plebs
				for (Pleb apl : plebs) { //Loop for detecting adjacent plebs
					if (!(pl == apl))	{ // Check if same object referenced
						int cx = apl.x - pl.x;
						int cy = apl.y - pl.y;
						double distance = Math.sqrt(cx * cx + cy * cy);
						
						if (distance <= Pleb.diameter) {
							//Collision! Change color
							changeColor(pl, apl);
							
							//Calculate unit vector of the norm
							double nx = (double) cx / distance;
							double ny = (double) cy / distance;
							
							//Calculate new vector with normal vector and pleb vector
							pl.vx = pl.vx - nx;
							pl.vy = pl.vy - ny;
							
							//Normalize new vector;
							double dist = Math.sqrt(pl.vx * pl.vx + pl.vy * pl.vy);
							pl.vx = pl.vx / dist;
							pl.vy = pl.vy / dist;
						}
					}
				}				
			}
			
			
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			repaint();
			
			jl.get(1).setText(Integer.toString(numHealthy));
			jl.get(2).setText(Integer.toString(numAsymptomatic));
			jl.get(3).setText(Integer.toString(numInfected));
			jl.get(4).setText(Integer.toString(numCured));
			jl.get(5).setText(Integer.toString(numDead));
			
		}
	}
	
	//Pauses the thread from running
	public void end() {
		running.set(false);
	}
	
	//Initiates pause timer to synch pleb statustimes
	public void pauseStart() {
		pauseStart = System.currentTimeMillis();
	}
	
	//Initiates pause timer to synch pleb statustimes
	public void pauseEnd() {
		pauseEnd = System.currentTimeMillis();
		pauseTotal = pauseEnd - pauseStart;
		for (Pleb pl : plebs) {
			if(pl.status == Vitality.ASYMPTOMATIC || pl.status == Vitality.INFECTED) {
				pl.statusTime = pl.statusTime + pauseTotal;
				System.out.println(pl.statusTime + "     " + pauseTotal);
				
			}
		}
	}
	
	//Reset game state
	public void reset(boolean walls) {
		numTotal = BALLS + 1;
		numHealthy = BALLS + 1;
		numAsymptomatic = 0; //Equal to the number of asymptomates at the start
		numInfected = 0;
		numCured = 0;
		numDead = 0;
		this.jl.get(0).setText(Integer.toString(numTotal));
		this.jl.get(1).setText(Integer.toString(numHealthy));
		this.jl.get(2).setText(Integer.toString(numAsymptomatic));
		this.jl.get(3).setText(Integer.toString(numInfected));
		this.jl.get(4).setText(Integer.toString(numCured));
		this.jl.get(5).setText(Integer.toString(numDead));
		addWalls(walls);
	}
	
	//Start the sickness if they come into contact
	public void changeColor(Pleb pl, Pleb apl) {
		
		//Change healthy plebs to asymptomatic if touched by asymptomatic
		if (pl.status == Vitality.HEALTHY && apl.status == Vitality.ASYMPTOMATIC) {
			pl.status = Vitality.ASYMPTOMATIC;
			pl.statusTime = System.currentTimeMillis();
			numHealthy--;
			numAsymptomatic++;
		}
		
		//Change healthy plebs to asymptomatic if touched by asymptomatic
		if (pl.status == Vitality.ASYMPTOMATIC && apl.status == Vitality.HEALTHY) {
			apl.status = Vitality.ASYMPTOMATIC;
			apl.statusTime = System.currentTimeMillis();
			numHealthy--;
			numAsymptomatic++;
		} 
		
		//Change healthy plebs to asymptomatic if touched by infected
		if (pl.status == Vitality.INFECTED && apl.status == Vitality.HEALTHY) {
			apl.status = Vitality.ASYMPTOMATIC;
			apl.statusTime = System.currentTimeMillis();
			numHealthy--;
			numAsymptomatic++;
		}
		
		if (pl.status == Vitality.HEALTHY && apl.status == Vitality.INFECTED) {
			pl.status = Vitality.ASYMPTOMATIC;
			pl.statusTime = System.currentTimeMillis();
			numHealthy--;
			numAsymptomatic++;
		} 
	}
	
	//Pleb goes from asymptomatic state to infected state
	public void infectPleb(Pleb pl) {
		if (pl.status == Vitality.ASYMPTOMATIC && 
				(System.currentTimeMillis() - pl.statusTime) > INCUBATION_TIME) {
			pl.status = Vitality.INFECTED;
			pl.speed = 0; //Sick people don't travel much
			numAsymptomatic--;
			numInfected++;
		} 
	}
	
	//Pleb dies from virus or is cured and immune
	public void finishPleb(Pleb pl) {
		if (pl.status == Vitality.INFECTED && 
				(System.currentTimeMillis() - pl.statusTime) > (INCUBATION_TIME + SICK_TIME)) {
			if(Math.random() > DEATH_RATE) {
				pl.status = Vitality.CURED;
				pl.speed = 1;
				numInfected--;
				numCured++;
			} else {
				pl.status = Vitality.DEAD;
				numInfected--;
				numDead++;
			}
		}
	}

	public void addWalls(boolean wallsOn) {
		if (wallsOn) {
			plebs.clear();
			//Note: Collision errors may occur if rectangles overlap
			walls.add(new Rectangle(0, 297, 220, 6)); // Left wall
			walls.add(new Rectangle(380, 297, 220, 6)); // Right wall
			walls.add(new Rectangle(297, 0, 6, 220)); // Top wall
			walls.add(new Rectangle(297, 380, 6, 220)); // Bottom wall
			
			// Center square
			walls.add(new Rectangle(214, 220, 6, 77)); // Left
			walls.add(new Rectangle(214, 343, 6, 37));
			walls.add(new Rectangle(380, 220, 6, 37)); // Right
			walls.add(new Rectangle(380, 303, 6, 77));
			walls.add(new Rectangle(214, 214, 43, 6)); // Top
			walls.add(new Rectangle(303, 214, 83, 6));
			walls.add(new Rectangle(214, 380, 83, 6)); // Bottom
			walls.add(new Rectangle(343, 380, 43, 6));
			
			// Quadrant dividers
			walls.add(new Rectangle(113, 107, 184, 6)); // Top left
			walls.add(new Rectangle(107, 107, 6, 150));
			walls.add(new Rectangle(107, 303, 6, 184)); // Bottom left
			walls.add(new Rectangle(107, 487, 150, 6));
			walls.add(new Rectangle(487, 113, 6, 184)); // Top right
			walls.add(new Rectangle(343, 107, 150, 6));
			walls.add(new Rectangle(303, 487, 184, 6)); // Bottom right
			walls.add(new Rectangle(487, 343, 6, 150));
		
			createPlebs(BALLS);
			repaint();
		} else {
			plebs.clear();
			walls.clear();
			createPlebs(BALLS);
			repaint();
		}
	}
	
	public int collideRectangle(Pleb pl, Rectangle wl) {
		
		int collisionID = 5;
		double testx = pl.x + Pleb.radius;
		double testy = pl.y + Pleb.radius;
		
		//Shape is left of rectangle
		if (pl.x + Pleb.radius < wl.x) {
			testx = wl.x;
			collisionID = 2;
		} else if (pl.x + Pleb.radius > wl.x + wl.width) { //Shape is right of rectangle
			testx = wl.x+wl.width;
			collisionID = 8;
		}
		
		//Shape is top of rectangle
		if (pl.y + Pleb.radius < wl.y) {
			testy = wl.y;
			collisionID = collisionID - 1;
		} else if (pl.y + Pleb.radius > wl.y + wl.height) { //Shape is bottom of rectangle
			testy = wl.y+wl.height;
			collisionID = collisionID + 1;
		}
		
		//Distance to rectangle corner
		double distx = pl.x + Pleb.radius - testx;
		double disty = pl.y + Pleb.radius - testy;
		double distance = Math.sqrt(distx * distx + disty * disty);
		
		//If distance is less than radius there is a collision
		if (distance <= Pleb.radius) {
			return collisionID;
		} else {
			return 0;
		}
	}
}