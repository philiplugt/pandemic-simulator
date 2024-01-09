
enum Status { START, STOP }

public class StopWatch {

    private long startTime = 0;
    private long stopTime = 0;
    private long pausedTime = 0;
    private Status status = Status.START;

    public StopWatch() { }

    public void start() {
        if (status == Status.START) {
            startTime = System.currentTimeMillis();
        } else {
            status = Status.START;
            startTime += getPausedTime();
        }
    }

    public void stop() {
        status = Status.STOP;
        stopTime = System.currentTimeMillis();
    }

    public long getPausedTime() {
        return System.currentTimeMillis() - stopTime;
    }

    public long getElapsedTime() {
        if (status == Status.START) {
            return System.currentTimeMillis() - startTime;
        } else {
            return stopTime - startTime;
        }
    }

    public double getElapsedTimeInSeconds() {
        if (status == Status.START) {
            return (double) (System.currentTimeMillis() - startTime) / 1_000;
        } else {
            return (double) (stopTime - startTime) / 1_000;
        }
    }
}