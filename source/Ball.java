
import java.awt.Color;
import java.awt.Graphics;

import java.util.Random;

enum Health { HEALTHY, CARRIER, INFECTED, CURED, DEAD }

public class Ball {

    int x, y;
    double vx, vy;
    double speed = Constants.BALL_SPEED;
    int size = Constants.BALL_DIAMETER;
    double radius = size / 2.0;
    long sickTime;
    Health health = Health.HEALTHY;

    public Ball() {
        Random r = new Random();
        this.x = r.nextInt(Constants.SCREEN.width - size);
        this.y = r.nextInt(Constants.SCREEN.height - size);
 
        double angle = 2 * Math.PI * Math.random();
        this.vx = Math.cos(angle);
        this.vy = Math.sin(angle);
    }

    public Ball(int x, int y) {
        this.x = x;
        this.y = y;
 
        double angle = 2 * Math.PI * Math.random();
        this.vx = Math.cos(angle);
        this.vy = Math.sin(angle);
    }

    public void paint(Graphics g){
        g.setColor(Constants.BLACK);
        g.fillOval(x, y, size, size);

        g.setColor(getColor());
        g.fillOval(x+1, y+1, size-2, size-2);
    }

    private Color getColor() {
        Color color = Constants.BLACK;
        switch(health){
            case HEALTHY -> color = Constants.BLUE;
            case CARRIER -> color = Constants.YELLOW;
            case INFECTED -> color = Constants.RED;
            case CURED -> color = Constants.GREEN;
            case DEAD -> color = Constants.GRAY;
        }
        return color;
    }

    public double centerX() {
        return x + radius;
    }

    public double centerY() {
        return y + radius;
    }

    public void move() {
        this.x = this.x + Math.round((float) this.vx * (float) this.speed);
        this.y = this.y + Math.round((float) this.vy * (float) this.speed);
    }


    public void makeCarrier(long time) {
        health = Health.CARRIER;
        this.sickTime = time;
    }

    public boolean makeSick(long time) {
        if (health == Health.CARRIER && time - sickTime > Constants.INCUBATION_TIME) {
            health = Health.INFECTED;
            speed = 0; // Sick people don't travel much
            return true;
        }
        return false;
    }

    public boolean makeHealedOrDead(long time) {
        if (health == Health.INFECTED 
                && time - sickTime > (Constants.INCUBATION_TIME + Constants.SICK_TIME)) {
            if (Math.random() > Constants.DEATH_RATE) {
                health = Health.CURED;
                speed = Constants.BALL_SPEED;
            } else {
                health = Health.DEAD;
            }
            return true;
        }
        return false;
    }
}