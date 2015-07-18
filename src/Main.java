import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JToggleButton;
import javax.swing.SwingUtilities;

public class Main extends JFrame {
	private static final long serialVersionUID = -1683077438634744861L;
	
	private final String[] COLORS_ARRAY = {"Orange", "Blue", "Yellow", "Red"};
	private final String[] DAYS_ARRAY = {"Thursday", "Friday", "Saturday", "Sunday"};
	private final String[] SLOTS_ARRAY = {"Time Slot 1", "Time Slot 2", "Time Slot 3", "Time Slot 4"};
	
	private JToggleButton[] colorsButtons;
	private JToggleButton[] daysButtons;
	private JToggleButton[] slotsButtons;
	
	private ButtonGroup colors;
	private ButtonGroup days;
	private ButtonGroup slots;
	
	private JScrollPane nameScrollPane;
	private JTextArea nameTextArea;
	private JButton createButton;
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				JFrame frame = new Main();
				frame.setVisible(true);
			}
		});
	}
	
	public Main() {
		initUI();
	}
	
	private void initUI() {
		setTitle("Birthday Names");
		setPreferredSize(new Dimension(500, 200));
		pack();
		setLocationRelativeTo(null);
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		JPanel panel = new JPanel();
		
		panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		panel.setLayout(new GridLayout(4, 4, 5, 5));
		
		colorsButtons = new JToggleButton[4];
		daysButtons = new JToggleButton[4];
		slotsButtons = new JToggleButton[4];
		
		colors = new ButtonGroup();
		days = new ButtonGroup();
		slots = new ButtonGroup();
		
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
			if(i == 0 || i == 3) {
				panel.add(new JLabel(""));
			}
			else if(i == 2) {
				nameScrollPane = new JScrollPane();
				nameTextArea = new JTextArea();
				
				nameTextArea.setLineWrap(true);
				nameTextArea.setWrapStyleWord(true);
				nameTextArea.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
				
				nameScrollPane.getViewport().add(nameTextArea);
				panel.add(nameScrollPane);
			}
			else {
				createButton = new JButton("Create");
				createButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						create();
					}					
				});
				panel.add(createButton);
			}
		}
		
		add(panel);
	}
	
	private void create() {
		String color = null;
		String day = null;
		String slot = null;
		for(int i = 0; i < 4; i++) {
			if(colorsButtons[i].isSelected()) {
				color = colorsButtons[i].getText().toLowerCase();
			}
			if(daysButtons[i].isSelected()) {
				day = daysButtons[i].getText().toLowerCase();
			}
			if(slotsButtons[i].isSelected()) {
				slot = Integer.toString(i);
			}
		}
		System.out.println("color = " + color);
		System.out.println("day = " + day);
		System.out.println("slot = " + slot);
	}
}