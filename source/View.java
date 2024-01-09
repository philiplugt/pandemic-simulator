
import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JFrame;

public class View extends JFrame implements ViewObserver {

    private ViewStats stats;
    private ViewGraph graph;
    private ViewCanvas canvas;
    private ViewMenu menu;

    public View(ViewMenu menu) {
        JPanel header = new JPanel(new GridBagLayout());
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Constants.BORDER));

        this.menu = menu;
        header.add(menu.startButton, getConstraints(0, 0, 1, 1, 0, new Insets(2, 2, 0, 2)));
        header.add(menu.resetButton, getConstraints(0, 1, 1, 1, 0, new Insets(0, 2, 0, 2)));
        header.add(menu.socialDistancingCheckBox, getConstraints(0, 2, 1, 1, 0, new Insets(0, 0, 0, 0)));
        header.add(menu.quitButton, getConstraints(0, 3, 1, 1, 0, new Insets(0, 2, 2, 2)));

        stats = new ViewStats();
        for (int i=0; i<stats.labels.length; i++) {
            header.add(stats.labels[i], getConstraints(1+i, 0, 1, 1, 1, new Insets(0, 0, 0, 0)));
            header.add(stats.counters[i], getConstraints(1+i, 1, 1, 1, 1, new Insets(0, 0, 0, 0)));
        }

        graph = new ViewGraph();
        header.add(graph, getConstraints(1, 2, 6, 2, 1, new Insets(2, 2, 5, 5)));

        canvas = new ViewCanvas();

        JPanel main = new JPanel(new BorderLayout());       
        main.add(header, BorderLayout.NORTH);
        main.add(canvas, BorderLayout.CENTER);
        this.add(main);
    }

    public void init() {
        setTitle(Constants.TITLE);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        setResizable(false);
    }

    private GridBagConstraints getConstraints(int gridx, int gridy, 
            int gridwidth, int gridheight,
            int weightx, Insets inset) {
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.gridx = gridx;
        c.gridy = gridy;
        c.gridwidth = gridwidth;
        c.gridheight = gridheight;
        c.weightx = weightx;
        c.insets = inset;
        return c;
    }

    public void onDrawNotification(State state) {
        canvas.updateState(state);
        canvas.repaint();
    }

    public void onCounterNotification(int[] newStats) {
        for (int i=0; i<stats.counters.length; i++) {
            stats.counters[i].setText(Integer.toString(newStats[i]));
        }
        graph.updateImage(newStats);
        graph.repaint();
    }

    public void onTextNotification(String message) {
        menu.startButton.setText(message);
    }

    public void onClearGraphNotification() {
        graph.reset();
    }

    public void onQuitNotification() {
        System.exit(0); // Do not use when running Java server
    }
}