import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Arrays;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
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
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class Main extends JFrame {
	private static final long serialVersionUID = -1683077438634744861L;
	
	private static final String DIRECTORY = "/";
	
	private static final int BUTTONS_PANEL_WIDTH = 253;
	private static final int FRAME_HEIGHT = 250;
	private static final int IMAGE_HEIGHT = FRAME_HEIGHT;
	private static final int IMAGE_WIDTH = (int)(IMAGE_HEIGHT / 126d * 225);
	private static final int FRAME_WIDTH = BUTTONS_PANEL_WIDTH + IMAGE_WIDTH;
	
	private static final Image BG = new ImageIcon(Main.class.getResource(DIRECTORY + "img/bg.png")).getImage();
	final static int BG_WIDTH = BG.getWidth(null);
	final static int BG_HEIGHT = BG.getHeight(null);
	
	private final String[] COLORS_ARRAY = {"Orange", "Blue", "Yellow", "Red"};
	
	private JToggleButton orangeButton;
	
	private JToggleButton[] colorsButtons;
	
	private ButtonGroup colors;
	
	private JScrollPane nameScrollPane;
	private JTextArea nameTextArea;
	private JButton createButton;
	
	private JPanel panel;
	private JPanel preview;
	private JPanel container;
	
	private static JFrame frame;
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				frame = new Main();
				((Main)frame).orangeButton.doClick();
				frame.setVisible(true);
			}
		});
	}
	
	public Main() {
		initUI();
	}
	
	private void initUI() {
		setTitle("Birthday Names");
		setPreferredSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT));
		pack();
		setLocationRelativeTo(null);
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		container = new JPanel();
		container.setLayout(new BoxLayout(container, BoxLayout.X_AXIS));
		
		panel = new JPanel();
		preview = new JPanel() {
			private static final long serialVersionUID = 1L;
			@Override public void paintComponent(Graphics g) {
				String color = null;
				if(colorsButtons == null) {
					return;
				}
				for(int i = 0; i < 4; i++) {
					if(colorsButtons[i] == null) {
						return;
					}
					if(colorsButtons[i].isSelected()) {
						color = colorsButtons[i].getText().toLowerCase();
					}
				}
				String name = nameTextArea.getText();
				
				// shouldn't run
				if(color == null) {
					name = "Please select a color";
					color = "white";
				}
				
				BufferedImage image = Main.createImage(color, name);
				
				Graphics2D g2d = (Graphics2D)g;
				g2d.setColor(Color.BLACK);
				g2d.fillRect(0, 0, IMAGE_WIDTH, IMAGE_HEIGHT);
				g2d.drawImage(image, 0, 0, IMAGE_WIDTH, IMAGE_HEIGHT, null);
			}
		};
		
		panel.setMaximumSize(new Dimension(BUTTONS_PANEL_WIDTH, FRAME_HEIGHT));
		preview.setMinimumSize(new Dimension(IMAGE_WIDTH, IMAGE_HEIGHT));
		
		panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		panel.setLayout(new GridLayout(4, 2, 5, 5));
		
		colorsButtons = new JToggleButton[4];
		
		colors = new ButtonGroup();
		
		for(int i = 0; i < 4; i++) {
			JToggleButton colorButton = new JToggleButton(COLORS_ARRAY[i]);
			if(i == 0) {
				orangeButton = colorButton;
			}
			
			colorButton.addActionListener(new ActionListener() {
				@Override public void actionPerformed(ActionEvent e) {
					updatePreview();
					nameTextArea.grabFocus();
				}
			});
			
			colorsButtons[i] = colorButton;
			
			colors.add(colorButton);

			panel.add(colorButton);
			if(i == 0 || i == 3) {
				panel.add(new JLabel(""));
			}
			else if(i == 2) {
				nameScrollPane = new JScrollPane();
				nameTextArea = new JTextArea();
				
				nameTextArea.setLineWrap(true);
				nameTextArea.setWrapStyleWord(true);
				nameTextArea.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
				
				nameTextArea.getDocument().addDocumentListener(new DocumentListener() {
					@Override public void changedUpdate(DocumentEvent e) {}
					@Override
					public void insertUpdate(DocumentEvent e) {
						updatePreview();
					}
					@Override
					public void removeUpdate(DocumentEvent e) {
						updatePreview();
					}
				});
				
				nameTextArea.grabFocus();
				
				nameScrollPane.getViewport().add(nameTextArea);
				panel.add(nameScrollPane);
			}
			else {
				createButton = new JButton("Create");
				createButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						create();
						nameTextArea.grabFocus();
					}					
				});
				panel.add(createButton);
			}
		}
		
		container.add(panel);
		container.add(preview);
		
		add(container);
	}

	public void updatePreview() {
		preview.repaint();
	}
	
	private void create() {
		String color = null;
		for(int i = 0; i < 4; i++) {
			if(colorsButtons[i].isSelected()) {
				color = colorsButtons[i].getText().toLowerCase();
			}
		}
		if(color == null) {
			JOptionPane.showMessageDialog(new JFrame(), "Please select a color");
			return;
		}
		
		String name = nameTextArea.getText();
		
		BufferedImage image = createImage(color, name);

		String colorDir = color.substring(0, 1).toUpperCase() + color.substring(1);
		
		String htmlString = createHtmlString(colorDir);
		
		File resFile = new File("C:/chrome/");
		if(!resFile.exists()) {
			new File("C:/chrome/").mkdir();
		}
		
		File outputImage = new File("C:/chrome/" + colorDir + ".png");
		File outputHtml = new File("C:/chrome/" + colorDir + ".html");
		try {
			ImageIO.write(image, "png", outputImage);
			PrintWriter writer = new PrintWriter(outputHtml);
			writer.println(htmlString);
			writer.close();
			
		}
		catch(IOException e) {
			e.printStackTrace();
		}
		
		try {			
			String slot = null;
			switch(colorDir) {
			case "Orange":
				slot = "1";
				break;
			case "Blue":
				slot = "2";
				break;
			case "Yellow":
				slot = "3";
				break;
			case "Red":
				slot = "4";
				break;
			}
			
			// based on a stackoverflow snippet
//	        ProcessBuilder builder = new ProcessBuilder(
//	                "cmd.exe", "/c", "start \"\" chrome --user-data-dir=\"C:\\chrome\\tag" + slot + "\" --new-window \"C:\\chrome\\" + colorDir + ".html\"");
			ProcessBuilder builder = new ProcessBuilder(
	                "cmd.exe", "/c", "start \"\" \"C:\\chrome\\application\\chrome.lnk\" --user-data-dir=\"C:\\chrome\\tag" + slot + "\" --new-window \"C:\\chrome\\" + colorDir + ".html\"");
	            builder.redirectErrorStream(true);
	            Process p = builder.start();
	            BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
	            String line;
	            while (true) {
	                line = r.readLine();
	                if (line == null) { break; }
	                System.out.println(line);
	            }
		}
		catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	private static BufferedImage createImage(String color, String name) {
		BufferedImage image = new BufferedImage(2250, 1265, BufferedImage.TYPE_INT_ARGB);
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
		case "white":
			g2d.setColor(Color.WHITE);
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
		if(!b1) {
			font1 = new Font(Font.SANS_SERIF, Font.BOLD, 144);
			System.out.println("Font \"" + font1String + "\" does not exist\nList of available fonts: " + Arrays.toString(fonts));
		}
		else {
			font1 = new Font(font1String, Font.BOLD, 144);
		}
		if(!b2) {
			font2 = new Font(Font.SANS_SERIF, Font.BOLD, 144);
			System.out.println("Font \"" + font2String + "\" does not exist\nList of available fonts: " + Arrays.toString(fonts));
		}
		else {
			font2 = new Font(font2String, Font.PLAIN, 144);
		}
		
		g2d.setFont(font1);
		g2d.drawString("HAPPY BIRTHDAY", (BG_WIDTH - g2d.getFontMetrics().stringWidth("HAPPY BIRTHDAY")) / 2, (BG_HEIGHT + 144) / 2);
		
		if(!name.contains("\n")) {
			g2d.setFont(font2);
			g2d.drawString(name, (BG_WIDTH - g2d.getFontMetrics().stringWidth(name)) / 2, (BG_HEIGHT + 144) / 2 + 200);
		}
		else {
			g2d.setFont(font2);
			int startY = 200;
			String line = name.substring(0, name.indexOf("\n"));
			g2d.drawString(line, (BG_WIDTH - g2d.getFontMetrics().stringWidth(line)) / 2, (BG_HEIGHT + 144) / 2 + startY);
			name = name.substring(name.indexOf("\n") + 1);
			while(name.contains("\n")) {
				line = name.substring(0, name.indexOf("\n"));
				startY += 160;
				g2d.drawString(line, (BG_WIDTH - g2d.getFontMetrics().stringWidth(line)) / 2, (BG_HEIGHT + 144) / 2 + startY);
				name = name.substring(name.indexOf("\n") + 1);
			}
			line = name.substring(name.indexOf("\n") + 1);
			startY += 160;
			g2d.drawString(line, (BG_WIDTH - g2d.getFontMetrics().stringWidth(line)) / 2, (BG_HEIGHT + 144) / 2 + startY);
		}
		return image;
	}
	
	private String createHtmlString(String color) {
		String videoTag = "";
		if(new File("C:\\chrome\\video\\fireworks.mp4").exists()) {
			videoTag = "<video width=\"100%\" autoplay loop muted><source src=\"C:\\chrome\\video\\fireworks.mp4\" type=\"video/mp4\"></video>";
		}
		else if(new File("C:\\chrome\\video\\fireworks.webm").exists()) {
			videoTag = "<video width=\"100%\" autoplay loop muted><source src=\"C:\\chrome\\video\\fireworks.webm\" type=\"video/webm\"></video>";
		}
		return "<!DOCTYPE html><head><title>Happy birthday</title><style type=\"text/css\">body { background-color:#000;}.main {height: 100vh;}.player {position: absolute;padding-bottom: 56.25%;padding-top: 25px;height: 0;}.player video {position: absolute;top: 0;left: 0;width: 100%;height: 100%;}</style></head><body style=\"overflow:hidden\"> <div id=\"player\">" + videoTag + "</div><img src=\"file:///C:/chrome/"
				+ color + ".png\" style=\"position:absolute;z-index:100;top:0;left:0;width:100%;height:auto\"></img></body></html>";
		
	}
}