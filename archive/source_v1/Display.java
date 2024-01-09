import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;

/**
 * Pandemulator - A simulator modeling virus spread
 * 
 * @author Philip
 *
 */
@SuppressWarnings("serial")
public class Display extends JPanel {

	private static JFrame frame;
	private Thread plebmove;
	private Thread grapher;
	private boolean walls = false;
	
	public Display() {
		super(new BorderLayout());
		
		//Font font = new Font("Courier", Font.BOLD, 12);
		
		ArrayList<JLabel> stats = new ArrayList<JLabel>();
		stats.add(new JLabel("-", JLabel.CENTER));
		stats.get(0).setForeground(new Color(128, 128, 128));
		Font f = stats.get(0).getFont();
		stats.get(0).setFont(f.deriveFont(f.getStyle() | Font.BOLD));
		
		stats.add(new JLabel("-", JLabel.CENTER));
		stats.get(1).setForeground(new Color(30, 144, 255));
		f = stats.get(1).getFont();
		stats.get(1).setFont(f.deriveFont(f.getStyle() | Font.BOLD));
		
		stats.add(new JLabel("-", JLabel.CENTER));
		stats.get(2).setForeground(new Color(255, 177, 0));
		f = stats.get(2).getFont();
		stats.get(2).setFont(f.deriveFont(f.getStyle() | Font.BOLD));
		
		stats.add(new JLabel("-", JLabel.CENTER));
		stats.get(3).setForeground(new Color(250, 20, 65));
		f = stats.get(3).getFont();
		stats.get(3).setFont(f.deriveFont(f.getStyle() | Font.BOLD));
		
		stats.add(new JLabel("-", JLabel.CENTER));
		stats.get(4).setForeground(new Color(50, 205, 50));
		f = stats.get(4).getFont();
		stats.get(4).setFont(f.deriveFont(f.getStyle() | Font.BOLD));
		
		stats.add(new JLabel("-", JLabel.CENTER));
		stats.get(5).setForeground(Color.BLACK);
		f = stats.get(5).getFont();
		stats.get(5).setFont(f.deriveFont(f.getStyle() | Font.BOLD));
		
		DrawingArea da = new DrawingArea(stats);
		plebmove = new Thread(da);
		
		Graph graph = new Graph(da);
		graph.setBackground(new Color(255, 250, 250));
		Border bd = BorderFactory.createMatteBorder(1, 1, 1, 1, new Color(211, 211, 211));
		graph.setBorder(bd);
		grapher = new Thread(graph);
		
		JLabel lab1 = new JLabel("Total", JLabel.CENTER);
		JLabel lab2 = new JLabel("Unaffected", JLabel.CENTER);
		JLabel lab3 = new JLabel("Carriers", JLabel.CENTER);
		JLabel lab4 = new JLabel("Infected", JLabel.CENTER);
		JLabel lab5 = new JLabel("Recovered", JLabel.CENTER);
		JLabel lab6 = new JLabel("Deceased", JLabel.CENTER);
		
		JButton start = new JButton("Start");
		start.setFocusPainted(false);
		start.setPreferredSize(new Dimension(90, 33));
		start.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				if (!plebmove.isAlive()) {
					plebmove = new Thread(da);
					grapher = new Thread(graph);
					if (!(start.getText() == "Start")) {
						da.pauseEnd();
					}
					plebmove.start();
					grapher.start();
					start.setText("Pause");
				} else {
					da.end();
					graph.end();
					da.pauseStart(); //initiate timer
					start.setText("Resume");
				}
			}
		});
		
		JButton stop = new JButton("Reset");
		stop.setFocusPainted(false);
		stop.setPreferredSize(new Dimension(90, 33));
		stop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				start.setText("Start");
				da.end();
				graph.end();
				da.reset(walls);
				graph.reset();
			}
		});

		JButton quit = new JButton("Quit");
		quit.setFocusPainted(false);
		quit.setPreferredSize(new Dimension(90, 33));
		quit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				da.end();
				plebmove = null;
				graph.end();
				grapher = null;
				frame.dispose();
			}
		});
		
		JCheckBox sdist = new JCheckBox("Social distancing mode");
		sdist.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent ie) {
				if(ie.getStateChange() == ItemEvent.SELECTED) {//Checkbox has been selected
					start.setText("Start");
					da.end();
					graph.end();
					walls = true;
					da.reset(walls);
					graph.reset();
		        } else { //Checkbox has been deselected
		        	start.setText("Start");
		            da.end();
		            graph.end();
		            walls = false;
		            da.reset(walls);
		            graph.reset();
		        };
			}	
		});

		
		JPanel menu = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		
		gbc.insets = new Insets(2, 2, 0, 2);
	    gbc.gridx = 0;
	    gbc.gridy = 0;
	    menu.add(start, gbc);

	    gbc.insets = new Insets(0, 2, 0, 2);
	    gbc.gridx = 0;
	    gbc.gridy = 1;
	    menu.add(stop, gbc);
	    
	    gbc.insets = new Insets(0, 0, 0, 0);
	    gbc.gridx = 0;
	    gbc.gridy = 2;
	    menu.add(sdist, gbc);
		
	    gbc.insets = new Insets(0, 2, 2, 2);
	    gbc.gridx = 0;
	    gbc.gridy = 3;
	    menu.add(quit, gbc);
	    
	    
	    //Add label elements
	    gbc.weightx = 1;
	    gbc.gridx = 1;
	    gbc.gridy = 0;
	    menu.add(lab1, gbc);
	    
	    gbc.gridx = 2;
	    menu.add(lab2, gbc);
	    
	    gbc.gridx = 3;
	    menu.add(lab3, gbc);
	    
	    gbc.gridx = 4;
	    menu.add(lab4, gbc);
	    
	    gbc.gridx = 5;
	    menu.add(lab5, gbc);
	    
	    gbc.gridx = 6;
	    menu.add(lab6, gbc);
	    
	    //Add labels with stats
	    for (int i = 0; i < stats.size(); i++) {
	    	gbc.gridx = i + 1;
		    gbc.gridy = 1;
		    menu.add(stats.get(i), gbc);
	    }
	    
	    //Add graph
	    gbc.insets = new Insets(2, 2, 5, 5);
	    gbc.fill = GridBagConstraints.BOTH;
	    gbc.weightx = 1;
	    gbc.gridheight = 2;
	    gbc.gridwidth = 6;
	    gbc.gridx = 1;
	    gbc.gridy = 2;
	    menu.add(graph, gbc);
	    
	    bd = BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(211, 211, 211));
		menu.setBorder(bd);
		
		add(menu, BorderLayout.NORTH);
		add(da, BorderLayout.CENTER);
	}
	
	static void createDisplayFrame() {
		//Create the frame
		frame = new JFrame();
		frame.setTitle("Pandemulation - A simulator modeling virus spread");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//Create frame components
		JComponent contentPane = new Display();
		contentPane.setOpaque(true);
		frame.setContentPane(contentPane);
		
		//Finalize frame
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		frame.setResizable(false);
		//System.out.println(graph.getSize().height + " " + graph.getSize().width);
	}
}
