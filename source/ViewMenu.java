
import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JCheckBox;

public class ViewMenu{

    JButton startButton;
    JButton resetButton;
    JButton quitButton;
    JCheckBox socialDistancingCheckBox;

    public ViewMenu() {
        startButton = new JButton("Start");
        startButton.setFocusPainted(false);
        startButton.setPreferredSize(
            new Dimension(startButton.getPreferredSize().width, 33));

        resetButton = new JButton("Reset");
        resetButton.setFocusPainted(false);
        resetButton.setPreferredSize(
            new Dimension(resetButton.getPreferredSize().width, 33));

        socialDistancingCheckBox = new JCheckBox("Social distancing mode");
        socialDistancingCheckBox.setFocusPainted(false);

        quitButton = new JButton("Quit");
        quitButton.setFocusPainted(false);
        quitButton.setPreferredSize(
            new Dimension(quitButton.getPreferredSize().width, 33));
    }
}