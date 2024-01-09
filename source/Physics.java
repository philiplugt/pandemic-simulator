
public class Physics {

    public void Physics() { }

    public void bounceOffBoundary(Ball ball) {
        int topBound = 0;
        int leftBound = 0;
        int bottomBound = Constants.SCREEN.height - ball.size;
        int rightBound = Constants.SCREEN.width - ball.size;

        if (ball.y < topBound) {
            ball.y = topBound;
            ball.vy *= -1;
        }
        
        if (ball.y > bottomBound) {
            ball.y = bottomBound;
            ball.vy *= -1;
        }

        if (ball.x < leftBound) {
            ball.x = leftBound;
            ball.vx *= -1;
        }

        if (ball.x > rightBound) {
            ball.x = rightBound;
            ball.vx *= -1;
        }
    }

    public boolean ballCollision(Ball ball, Ball otherBall) {
        int deltaX = otherBall.x - ball.x;
        int deltaY = otherBall.y - ball.y;
        double distance = getDistance(deltaX, deltaY);

        if (distance <= (ball.radius + otherBall.radius)) {
            return true;
        }
        return false;
    }

    public void bounceOffOtherBall(Ball ball, Ball otherBall) {
        int deltaX = otherBall.x - ball.x;
        int deltaY = otherBall.y - ball.y; 
        double distance = getDistance(deltaX, deltaY);

        // Calculate unit vector of the norm
        double normalizedX = (double) deltaX / distance;
        double normalizedY = (double) deltaY / distance;
        
        // Calculate new vector with normal vector and ball vector
        ball.vx = ball.vx - normalizedX;
        ball.vy = ball.vy - normalizedY;
        
        // Normalize new vector;
        double normalDistance = getDistance(ball.vx, ball.vy);
        ball.vx = ball.vx / normalDistance;
        ball.vy = ball.vy / normalDistance;
    }

    public boolean barrierCollision(Ball ball, Barrier barrier) {
        int collideSide = barrierCollisionSide(ball, barrier);
        if (collideSide > 0) {
            return true;
        }
        return false;
    }

    public void bounceOffBarrier(Ball ball, Barrier barrier) {
        int collideSide = barrierCollisionSide(ball, barrier);
        if (collideSide > 0) {

            //Collision! Bounce of edge
            if (collideSide == 1) {
                ball.x = barrier.x - ball.size;
                ball.y = barrier.y - ball.size;
                ball.vx *= -1;
                ball.vy *= -1;
            } else if (collideSide == 2) {
                ball.x = barrier.x - ball.size;
                ball.vx *= -1;
            } else if (collideSide == 3) {
                ball.x = barrier.x - ball.size;
                ball.y = barrier.y + barrier.height;
                ball.vx *= -1;
                ball.vy *= -1;
            } else if (collideSide == 4) {
                ball.y = barrier.y - ball.size;
                ball.vy *= -1;
            } else if (collideSide == 6) {
                ball.y = barrier.y + barrier.height;
                ball.vy *= -1;
            } else if (collideSide == 7) {
                ball.x = barrier.x + barrier.width;
                ball.y = barrier.y - ball.size;
                ball.vx *= -1;
                ball.vy *= -1;
            } else if (collideSide == 8) {
                ball.x = barrier.x + barrier.width;
                ball.vx *= -1;
            } else if (collideSide == 9) {
                ball.x = barrier.x + barrier.width;
                ball.y = barrier.y + barrier.height;
                ball.vx *= -1;
                ball.vy *= -1;
            }
        }
    }

    private double getDistance(double deltaX, double deltaY) {
        return Math.sqrt(deltaX * deltaX + deltaY * deltaY);
    }

    private int barrierCollisionSide(Ball ball, Barrier barrier) {
        int collisionID = 5;
        double nearestX = ball.centerX();
        double nearestY = ball.centerY();

        int rectTop = barrier.y;
        int rectLeft = barrier.x;
        int rectBottom = barrier.y + barrier.height;
        int rectRight = barrier.x + barrier.width;
        
        if (ball.centerX() < rectLeft) {
            nearestX = rectLeft;
            collisionID = 2;
        } else if (ball.centerX() > rectRight) {
            nearestX = rectRight;
            collisionID = 8;
        }
        
        if (ball.centerY() < rectTop) {
            nearestY = rectTop;
            collisionID = collisionID - 1;
        } else if (ball.centerY() > rectBottom) {
            nearestY = rectBottom;
            collisionID = collisionID + 1;
        }
        
        // Distance to rectangle corner
        double deltaX = ball.centerX() - nearestX;
        double deltaY = ball.centerY() - nearestY;
        double distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY);
        
        // If distance is less than radius there is a collision
        if (distance <= ball.radius) {
            return collisionID;
        } else {
            return 0;
        }
    }
}