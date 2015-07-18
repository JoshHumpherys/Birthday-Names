import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.SwingUtilities;

public class Main extends JFrame {
	private final String[] COLORS_ARRAY = {"Orange", "Blue", "Yellow", "Red"};
	private final String[] DAYS_ARRAY = {"Thursday", "Friday", "Saturday", "Sunday"};
	private final String[] SLOTS_ARRAY = {"Time Slot 1", "Time Slot 2", "Time Slot 3", "Time Slot 4"};
	
	private JToggleButton[] colorsButtons = new JToggleButton[4];
	private JToggleButton[] daysButtons = new JToggleButton[4];
	private JToggleButton[] slotsButtons = new JToggleButton[4];
	
	private ButtonGroup colors = new ButtonGroup();
	private ButtonGroup days = new ButtonGroup();
	private ButtonGroup slots = new ButtonGroup();
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new Main();
			}
		});
	}
	
	public Main() {
		setTitle("Birthday Names");
		setPreferredSize(new Dimension(500, 200));
		pack();
		setLocationRelativeTo(null);
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
		
		JPanel panel = new JPanel();
		
		panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		panel.setLayout(new GridLayout(4, 4, 5, 5));
		
		for(int i = 0; i < 4; i++) {
			JToggleButton colorButton = new JToggleButton(COLORS_ARRAY[i]);
			JToggleButton dayButton = new JToggleButton(DAYS_ARRAY[i]);
			JToggleButton slotButton = new JToggleButton(SLOTS_ARRAY[i]);
			
			colorsButtons[i] = colorButton;
			daysButtons[i] = dayButton;
			slotsButtons[i] = slotButton;
			
			colors.add(colorButton);
			days.add(dayButton);
			slots.add(slotButton);

			panel.add(colorButton);
			panel.add(dayButton);
			panel.add(slotButton);
		}
		
		add(panel);
	}
}