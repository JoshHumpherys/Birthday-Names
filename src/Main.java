import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
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
	
//	private static final int FRAME_WIDTH = 820;
//	private static final int FRAME_HEIGHT = 200;
	private static final int BUTTONS_PANEL_WIDTH = 500;
	private static final int FRAME_HEIGHT = 250;
	private static final int IMAGE_HEIGHT = FRAME_HEIGHT;
	private static final int IMAGE_WIDTH = (int)(IMAGE_HEIGHT / 126d * 225);
	private static final int FRAME_WIDTH = BUTTONS_PANEL_WIDTH + IMAGE_WIDTH;
//	private static final int IMAGE_WIDTH;
//	private static final int IMAGE_HEIGHT;
//	static {
//		int width = FRAME_HEIGHT / 126 * 225;
//		int height = FRAME_WIDTH / 225 * 126;
//		if(width / FRAME_HEIGHT > 225 / 126) { // width is too big so keep height the same
//			IMAGE_WIDTH = width;
//			IMAGE_HEIGHT = FRAME_HEIGHT;
//		}
//		else { // width is too small so keep width the same
//			IMAGE_WIDTH = FRAME_WIDTH;
//			IMAGE_HEIGHT = height;
//		}
//	}
	
	private static final Image BG = new ImageIcon(Main.class.getResource(DIRECTORY + "img/bg.png")).getImage();
//	private static final File html = new File(Main.class.getResource(DIRECTORY + "fireworks.html").getFile());
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
	
	private JPanel panel;
	private JPanel preview;
	private JPanel container;
	
	private static JFrame frame;
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				frame = new Main();
				frame.setVisible(true);
//				frame.addComponentListener(new ComponentAdapter() {
//					@Override
//					public void componentShown(ComponentEvent e) {
//						((Main)frame).updatePreview();
//					}
//				});
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
		preview = new JPanel();
		
		panel.setMaximumSize(new Dimension(BUTTONS_PANEL_WIDTH, FRAME_HEIGHT));
		preview.setMinimumSize(new Dimension(IMAGE_WIDTH, IMAGE_HEIGHT));
		
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
				
//				nameTextArea.addKeyListener(new KeyListener() {
//					@Override public void keyPressed(KeyEvent e) {}
//					@Override public void keyReleased(KeyEvent e) {}
//					@Override public void keyTyped(KeyEvent e) {
//						updatePreview();
//					}
//				});
				
//				InputMap inputMap = nameTextArea.getInputMap(JTextArea.WHEN_FOCUSED);
//				ActionMap actionMap = nameTextArea.getActionMap();
//				
//				inputMap.put(actionMap.get(KeyStroke.getKeyStroke(arg0)))
				
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
		
		container.add(panel);
		container.add(preview);
		
		add(container);
	}

	public void updatePreview() {
		String color = null;
		for(int i = 0; i < 4; i++) {
			if(colorsButtons[i].isSelected()) {
				color = colorsButtons[i].getText().toLowerCase();
			}
		}
		String name = nameTextArea.getText();
		
		if(color == null) {
			name = "Please select a value from each of the 3 columns";
			color = "white";
		}
		
		BufferedImage image = createImage(color, name);
		
		Graphics2D g2d = (Graphics2D)preview.getGraphics();
		g2d.setColor(Color.BLACK);
		g2d.fillRect(0, 0, IMAGE_WIDTH, IMAGE_HEIGHT);
		g2d.drawImage(image, 0, 0, IMAGE_WIDTH, IMAGE_HEIGHT, null);
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
				slot = Integer.toString(i + 1);
			}
		}
		if(color == null || day == null || slot == null) {
			JOptionPane.showMessageDialog(new JFrame(), "Please select a value from each of the 3 columns");
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
//			FileUtils.writeStringToFile(outputHtml, htmlString);
			PrintWriter writer = new PrintWriter(outputHtml);
			writer.println(htmlString);
			writer.close();
			
		}
		catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	private BufferedImage createImage(String color, String name) {
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
//		try {
//			String htmlString = FileUtils.readFileToString(html);
//			String name = "Time Slot " + slot + ".png";
//			htmlString = htmlString.replace("$img", name);
//			return htmlString;
//		}
//		catch (IOException e) {
//			e.printStackTrace();
//			System.out.println(html.canRead());
//			System.out.println(html.exists());
//			System.out.println(html.getAbsoluteFile().exists());
//			return null;
//		}
		
//		the following code does not resize a top-level window:
//		<script type=\"text/javascript\">GlobalEventHandlers.onload=resizeWindow;function resizeWindow(){alert(\"hello world\");window.resizeTo(683, 469);}</script>
//		onload=\"resizeWindow();\" // attribute for body tag
		
		return "<!DOCTYPE html><head><title>Happy birthday</title><style type=\"text/css\">body { background-color:#000;}.main {height: 100vh;}.player {position: absolute;padding-bottom: 56.25%;padding-top: 25px;height: 0;}.player iframe {position: absolute;top: 0;left: 0;width: 100%;height: 100%;}</style></head><body> <div id=\"player\"></div><img src=\"file:///C:/chrome/"
				+ color + ".png\" style=\"position:absolute;z-index:100;top:0;left:0;width:100%;height:auto\"></img><div id=\"nologo\" style=\"background-color:#000;position:absolute;right:0;bottom:0;z-index=200;width:100px;height:100px;display:block\"></div><script> var tag = document.createElement('script'); tag.src = \"http://www.youtube.com/player_api\"; var firstScriptTag = document.getElementsByTagName('script')[0]; firstScriptTag.parentNode.insertBefore(tag, firstScriptTag); var player; function onYouTubePlayerAPIReady() { player = new YT.Player('player', { width: '100%', playerVars: { 'autoplay': 1, 'loop': 1, 'playlist': 'Du6n6wrEcxg', 'showinfo': 0, 'controls': 0, 'modestbranding': 1, nologo: 1}, videoId: 'Du6n6wrEcxg', events: { 'onReady': onPlayerReady,'onStateChange': onStateChange} }); } function onPlayerReady(event) { event.target.mute(); } function onStateChange(event) {switch(event.data) {case 1:setTimeout(function(){document.getElementById(\"nologo\").style.cssText=\"display:none\";setTimeout(function(){document.getElementById(\"nologo\").style.cssText=\"background-color:#000;position:absolute;right:0;bottom:0;z-index=200;width:100px;height:100px;display:block\";},47000);}, 3000);break;default:document.getElementById(\"nologo\").style.cssText=\"background-color:#000;position:absolute;right:0;bottom:0;z-index=200;width:100px;height:100px;display:block\";break;} }</script></body></html>";
		
	}
}