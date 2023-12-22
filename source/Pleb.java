import java.awt.Color;

//Defines the state of being or vitality of the pleb
enum Vitality {HEALTHY, ASYMPTOMATIC, INFECTED, DEAD, CURED}

/**
 * Class for a pleb, a pleb simulates a living being that moves
 * around (lives) in the world and it susceptible to catching 
 * a virus
 * 
 * @author PvdL
 *
 */
public class Pleb {
	
	//Pleb object dimensions
	static final int diameter = 7;
	static final double radius = diameter / 2.0;
	
	//Defined the object variables
	int x;
	int y;
	double vx;
	double vy;
	int speed;
	long statusTime;
	Vitality status;
	
	//Pleb constructor
	public Pleb(int x, int y, int speed, Vitality status) {
		this.x = x;
		this.y = y;
		double angle = 2 * Math.PI * Math.random(); // Random angle [0..360]
		this.vx = Math.cos(angle); // Use angle to get x vector
		this.vy = Math.sin(angle); // Use angle to get y vector
		this.speed = speed;
		this.statusTime = System.currentTimeMillis();
		this.status = status;
	}
	
	//Returns the color based on status
	public Color getColor() {
		Color color = Color.WHITE;
		switch(this.status){
			case HEALTHY:
				color = new Color(30, 144, 255); //Dodger blue
				break;
			case ASYMPTOMATIC:
				color = new Color(255, 193, 0); //Orange
				break;
			case INFECTED:
				color = new Color(250, 20, 65); //Crimson red
				break;
			case DEAD:
				color = Color.BLACK; //Black
				break;
			case CURED:
				color = new Color(50, 205, 50); //Lime green
				break;
		}
		return color;
	}
}
