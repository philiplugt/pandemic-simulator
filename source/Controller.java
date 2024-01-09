
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;

import javax.swing.SwingUtilities;

public class Controller {

    private ViewMenu menu;
    private Loop loop;

    public Controller(ViewMenu menu, Loop loop) {
        this.menu = menu;
        this.loop = loop;
        addAllListeners();
    }

    private void addAllListeners() {
        menu.startButton.addActionListener(new startButtonListener());
        menu.resetButton.addActionListener(new resetButtonListener());
        menu.quitButton.addActionListener(new quitButtonListener());
        menu.socialDistancingCheckBox.addItemListener(
            new socialDistancingItemListener()
        );
    }

    class startButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if (loop.isRunning()) {
                loop.pause();
            } else {
                loop.start();
            }   
        }
    }

    class resetButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            loop.reset();
        }
    }

    class socialDistancingItemListener implements ItemListener {
        public void itemStateChanged(ItemEvent e) {
            if(e.getStateChange() == ItemEvent.SELECTED) {
                loop.toggleSocialDistancing(true);
            } else {
                loop.toggleSocialDistancing(false);
            }
        }
    }

    class quitButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            loop.quit();
        }
    }
}