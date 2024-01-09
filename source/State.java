
import java.util.ArrayList;

public class State {

    ArrayList<Ball> balls = new ArrayList<Ball>();
    ArrayList<Barrier> barriers = new ArrayList<Barrier>();

    int numTotal = 1 + Constants.BALLS;
    int numHealthy = 1 + Constants.BALLS;
    int numCarriers = 0; //Equal to the number of asymptomates at the start
    int numInfected = 0;
    int numCured = 0;
    int numDead = 0;

    public State() { }

    public int[] getCounters() {
        int[] counters = { numTotal, numHealthy, numCarriers, numInfected, numCured, numDead, };
        return counters;
    }
}