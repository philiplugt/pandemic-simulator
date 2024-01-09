
import java.awt.Color;
import java.awt.Dimension;

public class Constants {

    static final String TITLE = "Pandemic Simulator - A simulator modeling virus spread";
    static final Dimension SCREEN = new Dimension(600, 600);
    static final int DELAY = 20;
    
    static final int BALLS = 250;
    static final int BALL_DIAMETER = 8;
    static final double BALL_SPEED = 1;

    static final int INCUBATION_TIME = 4000;
    static final int SICK_TIME = 8000;
    static final double DEATH_RATE = 0.2;

    // Set colors
    static final Color BLUE = new Color(30, 144, 255);
    static final Color YELLOW = new Color(255, 185, 0);
    static final Color RED = new Color(250, 20, 65);
    static final Color GREEN = new Color(50, 205, 50);
    static final Color GRAY = new Color(107, 122, 144);

    static final Color BARRIER = Color.DARK_GRAY; // Inner walls
    static final Color BLACK = Color.BLACK;
    
    static final Color BACKGROUND = new Color(199, 208, 221);
    static final Color BORDER = new Color(157, 168, 183); //(211, 211, 211);

    private Constants() { }
}