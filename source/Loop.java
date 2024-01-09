
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

public class Loop {

    private ViewObserver observer;
    private Timer timer;
    private Simulation simulation;
    private boolean socialDistancing = false;

    public Loop() {
        simulation = new Simulation(socialDistancing);
        timer = new Timer(Constants.DELAY, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                simulation.update();
                render();
            }
        });
    }

    public boolean isRunning() {
        return timer.isRunning();
    }

    public void start() {
        timer.start();
        simulation.start();
        observer.onTextNotification("Pause");
    }

    public void pause() {
        timer.stop();
        observer.onTextNotification("Resume");
        simulation.chrono.stop();
    }

    public void reset() {
        timer.stop();
        simulation = new Simulation(socialDistancing);
        observer.onTextNotification("Start");
        observer.onClearGraphNotification();
        render();
    }

    public void quit() {
        timer.stop();
        observer.onQuitNotification();
    }

    public void toggleSocialDistancing(boolean socialDistancing) {
        this.socialDistancing = socialDistancing;
        reset();
    }

    public void registerObserver(ViewObserver observer) {
        this.observer = observer;
        render();
    }

    private void render() {
        observer.onDrawNotification(simulation.state);
        observer.onCounterNotification(simulation.state.getCounters());
    }
}