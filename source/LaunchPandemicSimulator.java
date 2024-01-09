
import javax.swing.SwingUtilities;

public class LaunchPandemicSimulator {

    public static void main(String[] args) {

        ViewMenu menu = new ViewMenu();
        View view = new View(menu);
        
        Loop loop = new Loop();
        loop.registerObserver(view);

        Controller controller = new Controller(menu, loop);
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                view.init();
            }
        });
    }
}