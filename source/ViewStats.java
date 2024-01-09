
import java.awt.Color;
import java.awt.Font;

import javax.swing.JLabel;

public class ViewStats {

    private Color[] colors = { 
        Constants.BLACK, Constants.BLUE, Constants.YELLOW,
        Constants.RED, Constants.GREEN, Constants.GRAY,
    };

    JLabel[] labels = {
        new JLabel("Total", JLabel.CENTER), new JLabel("Unaffected", JLabel.CENTER),
        new JLabel("Carriers", JLabel.CENTER), new JLabel("Infected", JLabel.CENTER),
        new JLabel("Recovered", JLabel.CENTER), new JLabel("Deceased", JLabel.CENTER),
    };

    JLabel[] counters;

    public ViewStats() {
        counters = new JLabel[labels.length];
        for (int i=0; i<counters.length; i++) {
            counters[i] = new JLabel("0", JLabel.CENTER);
            Font f = counters[i].getFont();
            counters[i].setFont(f.deriveFont(f.getStyle() | Font.BOLD));
            counters[i].setForeground(colors[i]);
        }
    }
}