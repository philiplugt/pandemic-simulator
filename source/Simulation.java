
public class Simulation {

    State state = new State();
    Physics physics = new Physics();
    StopWatch chrono = new StopWatch();

    public Simulation(boolean socialDistancing) {
        if (socialDistancing) {
            createBarriers();
        }
        createBalls(Constants.BALLS);
    }

    public void start() {
        chrono.start();
        if (state.balls.get(0).health == Health.HEALTHY) {
            state.balls.get(0).makeCarrier(chrono.getElapsedTime());
            state.numHealthy--;
            state.numCarriers++;
        }
    }

    public void update() {
        for (Ball ball : state.balls) {
            ball.move();
        }

        for (Ball ball : state.balls) {
            // Determine how the disease is affecting the ball
            boolean healthChanged;
            healthChanged = ball.makeSick(chrono.getElapsedTime());
            if (healthChanged && ball.health == Health.INFECTED) {
                state.numCarriers--;
                state.numInfected++;
            }

            healthChanged = ball.makeHealedOrDead(chrono.getElapsedTime());
            if (healthChanged && ball.health == Health.CURED) {
                state.numInfected--;
                state.numCured++;
            }

            if (healthChanged && ball.health == Health.DEAD) {
                state.numInfected--;
                state.numDead++;
            }

            // Determine the physics of ball movement
            physics.bounceOffBoundary(ball);

            for (Barrier barrier : state.barriers) {
                physics.bounceOffBarrier(ball, barrier);
            }

            for (Ball otherBall : state.balls) {
                if (ball != otherBall && physics.ballCollision(ball, otherBall)) {
                    // Collision! Change color of ball
                    spreadSickness(ball, otherBall);
                    physics.bounceOffOtherBall(ball, otherBall);
                }
            }
        }
    }

    private void spreadSickness(Ball ball, Ball otherBall) {
        // Change healthy balls to carriers, if touched by carriers or infected
        if (ball.health == Health.HEALTHY && 
                (otherBall.health == Health.CARRIER || otherBall.health == Health.INFECTED)) {
            ball.makeCarrier(chrono.getElapsedTime());
            state.numHealthy--;
            state.numCarriers++;
        }
    }

    private void createBalls(int numBalls) {
        // Create an initial pleb that will be patient 0
        state.balls.add(new Ball(10, 10));

        // Create all other balls
        for (int i=0; i<numBalls; i++) {
            boolean validPosition = true;
            Ball newBall = new Ball();
            
            // Check if new balls are not placed inside rectangles or other balls
            for (Barrier barrier : state.barriers) { 
                if (physics.barrierCollision(newBall, barrier)) {
                    validPosition = false;
                }
            }

            for (Ball otherBall : state.balls) {
                if (physics.ballCollision(newBall, otherBall)) {
                    validPosition = false;
                }
            }

            if (validPosition) {
                state.balls.add(newBall);
            } else {
                numBalls++;
            }
        }
    }

    private void createBarriers() {
        //Note: Collision errors may occur if rectangles overlap
        state.barriers.add(new Barrier(0, 297, 220, 6));   // Left wall
        state.barriers.add(new Barrier(380, 297, 220, 6)); // Right wall
        state.barriers.add(new Barrier(297, 0, 6, 220));   // Top wall
        state.barriers.add(new Barrier(297, 380, 6, 220)); // Bottom wall
        
        // Center square
        state.barriers.add(new Barrier(214, 220, 6, 77)); // Left
        state.barriers.add(new Barrier(214, 343, 6, 37));
        state.barriers.add(new Barrier(380, 220, 6, 37)); // Right
        state.barriers.add(new Barrier(380, 303, 6, 77));
        state.barriers.add(new Barrier(214, 214, 43, 6)); // Top
        state.barriers.add(new Barrier(303, 214, 83, 6));
        state.barriers.add(new Barrier(214, 380, 83, 6)); // Bottom
        state.barriers.add(new Barrier(343, 380, 43, 6));
        
        // Quadrant dividers
        state.barriers.add(new Barrier(113, 107, 184, 6)); // Top left
        state.barriers.add(new Barrier(107, 107, 6, 150));
        state.barriers.add(new Barrier(107, 303, 6, 184)); // Bottom left
        state.barriers.add(new Barrier(107, 487, 150, 6));
        state.barriers.add(new Barrier(487, 113, 6, 184)); // Top right
        state.barriers.add(new Barrier(343, 107, 150, 6));
        state.barriers.add(new Barrier(303, 487, 184, 6)); // Bottom right
        state.barriers.add(new Barrier(487, 343, 6, 150));
    }
}