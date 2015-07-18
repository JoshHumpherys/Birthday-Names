import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
//import java.awt.FontFormatException;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
//import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
//import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JToggleButton;
import javax.swing.SwingUtilities;

public class Main extends JFrame {
	private static final long serialVersionUID = -1683077438634744861L;
	
	private static final String DIRECTORY = "";
	
	private static final Image BG = new ImageIcon(Main.class.getResource(DIRECTORY + "img/bg.png")).getImage();
	final int BG_WIDTH = BG.getWidth(null);
	final int BG_HEIGHT = BG.getHeight(null);
	
//	private static final InputStream LUCIDA_INPUT_STREAM = Main.class.getResourceAsStream(DIRECTORY + "fonts/22799_LCALLIG.ttf");
//	private static Font lucida;
	
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
		if(color == null || day == null || slot == null) {
			JOptionPane.showMessageDialog(new JFrame(), "Please select a value from each of the 3 columns");
			return;
		}
		
		String name = nameTextArea.getText();
		
		BufferedImage image = createImage(color, name);
		
		try {
			File output = new File("name.png");
			ImageIO.write(image, "png", output);
		}
		catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	private BufferedImage createImage(String color, String name) {
		BufferedImage image = new BufferedImage(2250, 1265, BufferedImage.TYPE_INT_RGB);
		Graphics2D g2d = image.createGraphics();
		switch(color) {
		case "orange":
			g2d.setColor(new Color(255, 26, 26));
			break;
		case "blue":
			g2d.setColor(new Color(0, 162, 232));
			break;
		case "yellow":
			g2d.setColor(new Color(255, 242, 0));
			break;
		case "red":
			g2d.setColor(new Color(165, 2, 2));
			break;
		}
		
		g2d.drawImage(BG, 0, 0, BG_WIDTH, BG_HEIGHT, null);
		
		String[] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
		
		String font1String = "Times New Roman";
		String font2String = "Lucida Calligraphy";
		boolean b1 = false;
		boolean b2 = false;
		Font font1, font2;
		for(String s : fonts) {
			if(s.equals(font1String)) {
				b1 = true;
			}
			if(s.equals(font2String)) {
				b2 = true;
			}
		}
		if(!b1) font1 = new Font(Font.SANS_SERIF, Font.BOLD, 144);
		else font1 = new Font(font1String, Font.BOLD, 144);
		if(!b2) font2 = new Font(Font.SANS_SERIF, Font.BOLD, 144);
		else font2 = new Font(font2String, Font.PLAIN, 144);
		
		g2d.setFont(font1);
		g2d.drawString("HAPPY BIRTHDAY", (BG_WIDTH - g2d.getFontMetrics().stringWidth("HAPPY BIRTHDAY")) / 2, (BG_HEIGHT + 144) / 2);
		
//		try {
//			lucida = Font.createFont(Font.TRUETYPE_FONT, new File("fonts/22799_LCALLIG.ttf"));
//		}
//		catch(IOException e) {
//			e.printStackTrace();
//		}
//		catch(FontFormatException e) {
//			e.printStackTrace();
//		}
//		
//		g2d.setFont(new Font(lucida.toString(), Font.PLAIN, 144));
		
		g2d.setFont(font2);
		g2d.drawString(name, (BG_WIDTH - g2d.getFontMetrics().stringWidth(name)) / 2, (BG_HEIGHT + 144) / 2 + 200);
		
		return image;
	}
}