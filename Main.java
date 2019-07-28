import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
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

import com.sun.net.httpserver.*;

public class Main extends JFrame {
    private static final long serialVersionUID = -1683077438634744861L;

    private static final String DIRECTORY = "";

    private static final int BUTTONS_PANEL_WIDTH = 253;
    private static final int FRAME_HEIGHT = 250;
    private static final int IMAGE_HEIGHT = FRAME_HEIGHT;
    private static final int IMAGE_WIDTH = (int)(IMAGE_HEIGHT / 126d * 225);
    private static final int FRAME_WIDTH = BUTTONS_PANEL_WIDTH + IMAGE_WIDTH;

    private static final Image BG = new ImageIcon(Main.class.getResource(DIRECTORY + "img/bg.png")).getImage();
    private static final Image ICON = new ImageIcon(Main.class.getResource(DIRECTORY + "img/cake-512.png")).getImage();
    final static int BG_WIDTH = BG.getWidth(null);
    final static int BG_HEIGHT = BG.getHeight(null);

    private final String[] COLORS_ARRAY = { "Orange", "Blue", "Yellow", "Red" };

    private JToggleButton orangeButton;

    private JToggleButton[] colorsButtons;

    private JCheckBox happyBirthdayCheckBox;

    private ButtonGroup colors;

    private JScrollPane nameScrollPane;
    private JTextArea nameTextArea;
    private JButton createButton;

    private JPanel panel;
    private JPanel preview;
    private JPanel container;

    private static JFrame frame;

    private boolean upKeyDown, downKeyDown, enterKeyDown;

    public static void main(String[] args) {
        // Launch the application window
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                frame = new Main();
                ((Main)frame).orangeButton.doClick();
                frame.setVisible(true);
            }
        });

        // Launch an HTTP server to serve up the html files
        launchHttpServer();
    }

    public Main() {
        initUI();
    }

    private void initUI() {
        setTitle("Birthday Names");
        setIconImage(ICON);
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

            @Override
            public void paintComponent(Graphics g) {
                String color = null;
                if (colorsButtons == null) {
                    return;
                }
                for (var i = 0; i < colorsButtons.length; i++) {
                    if (colorsButtons[i] == null) {
                        return;
                    }
                    if (colorsButtons[i].isSelected()) {
                        color = colorsButtons[i].getText().toLowerCase();
                    }
                }
                var name = nameTextArea.getText();
                var happyBirthday = happyBirthdayCheckBox.isSelected();

                // shouldn't run
                if (color == null) {
                    color = "white";
                    name = "Please select a color";
                    happyBirthday = false;
                }

                var image = Main.createImage(color, happyBirthday, name);

                var g2d = (Graphics2D)g;
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

        for (var i = 0; i < 4; i++) {
            var colorButton = new JToggleButton(COLORS_ARRAY[i]);
            if (i == 0) {
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
            if (i == 0) {
                happyBirthdayCheckBox = new JCheckBox("Birthday");
                happyBirthdayCheckBox.setSelected(true);
                happyBirthdayCheckBox.addActionListener(new ActionListener() {
                    @Override public void actionPerformed(ActionEvent e) {
                        updatePreview();
                        nameTextArea.grabFocus();
                    }
                });
                panel.add(happyBirthdayCheckBox);
            } else if (i == 3) {
                panel.add(new JLabel(""));
            } else if (i == 2) {
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
            } else {
                createButton = new JButton("Create");
                createButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        create();
                        nameTextArea.grabFocus();
                        nameTextArea.setText("");
                        moveDown();
                    }
                });
                panel.add(createButton);
            }
        }

        container.add(panel);
        container.add(preview);

        add(container);

        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher() {
            @Override
            public boolean dispatchKeyEvent(KeyEvent e) {
                switch (e.getID()) {
                case KeyEvent.KEY_PRESSED:
                    switch (e.getKeyCode()) {
                    case KeyEvent.VK_DOWN:
                        if (!downKeyDown) {
                            downKeyDown = true;
                            moveDown();
                        }
                        return true;
                    case KeyEvent.VK_UP:
                        if (!upKeyDown) {
                            upKeyDown = true;
                            moveUp();
                        }
                        return true;
                    case KeyEvent.VK_ENTER:
                        if (!enterKeyDown && e.isShiftDown()) {
                            enterKeyDown = true;
                            create();
                            nameTextArea.grabFocus();
                            nameTextArea.setText("");
                            moveDown();
                        }
                    };
                    return false;
                case KeyEvent.KEY_RELEASED:
                    switch (e.getKeyCode()) {
                    case KeyEvent.VK_DOWN:
                        downKeyDown = false;
                        return true;
                    case KeyEvent.VK_UP:
                        upKeyDown = false;
                        return true;
                    case KeyEvent.VK_ENTER:
                        enterKeyDown = false;
                        return true;
                    };
                    return false;
                }
                return false;
            }
        });
    }

    private void moveDown() {
        for (var i = 0; i < 4; i++) {
            if (colorsButtons[i].isSelected()) {
                colorsButtons[i == 3 ? 0 : i + 1].doClick();
                break;
            }
        }
    }

    private void moveUp() {
        for (var i = 0; i < 4; i++) {
            if (colorsButtons[i].isSelected()) {
                colorsButtons[i == 0 ? 3 : i - 1].doClick();
                break;
            }
        }
    }

    public void updatePreview() {
        preview.repaint();
    }

    private void create() {
        String color = null;
        for (var i = 0; i < 4; i++) {
            if (colorsButtons[i].isSelected()) {
                color = colorsButtons[i].getText().toLowerCase();
            }
        }
        if (color == null) {
            JOptionPane.showMessageDialog(new JFrame(), "Please select a color");
            return;
        }

        var name = nameTextArea.getText();
        var happyBirthday = happyBirthdayCheckBox.isSelected();

        var image = createImage(color, happyBirthday, name);

        var colorDir = color.substring(0, 1).toUpperCase() + color.substring(1);

        var htmlString = createHtmlString(colorDir);

        var resFile = new File("C:/chrome/");
        if (!resFile.exists()) {
            new File("C:/chrome/").mkdir();
        }

        var outputImage = new File("C:/chrome/" + colorDir + ".png");
        var outputHtml = new File("C:/chrome/" + colorDir + ".html");
        try {
            ImageIO.write(image, "png", outputImage);
            var writer = new PrintWriter(outputHtml);
            writer.println(htmlString);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            String slot = null;
            switch (colorDir) {
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
            var screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            var screenWidth = screenSize.width;
            var screenHeight = screenSize.height;
            var windowWidth = 25 * 30;
            var windowHeight = 14 * 30 + 89;
            var x = this.getLocation().x + BUTTONS_PANEL_WIDTH;
            if (x + windowWidth > screenWidth) {
                x = (screenWidth - windowWidth) / 2;
            }
            var y = (screenHeight - windowHeight) / 2;
            var chromeDir = "\"C:\\chrome\\application\\chrome" + slot + ".lnk\"";
            var kill = new ProcessBuilder("cmd.exe", "/c", "taskkill /IM chrome" + slot + ".exe /T /F > nul");
            var create = new ProcessBuilder("cmd.exe", "/c", "start \"\" " + chromeDir + " --user-data-dir=\"C:\\chrome\\tag"
                    + slot + "\" --new-window \"localhost:8000/" + colorDir + " --window-size=" + windowWidth + "," + windowHeight + " --window-position="
                    + x + "," + y + " --disable-infobars --allow-file-access-from-files");
            var killProcess = kill.start();
            killProcess.waitFor();
            kill.redirectErrorStream(true);
            create.start();
            var killReader = new BufferedReader(new InputStreamReader(killProcess.getInputStream()));
            String line;
            while (true) {
                line = killReader.readLine();
                if (line == null) { break; }
                System.out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static BufferedImage createImage(String color, boolean happyBirthday, String name) {
        var image = new BufferedImage(2250, 1265, BufferedImage.TYPE_INT_ARGB);
        var g2d = image.createGraphics();
        switch (color) {
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

        var fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();

        var font1String = "Times New Roman";
        var font2String = "Lucida Calligraphy";
        var b1 = false;
        var b2 = false;
        Font font1, font2;
        for (var font : fonts) {
            if (font.equals(font1String)) {
                b1 = true;
            }
            if (font.equals(font2String)) {
                b2 = true;
            }
        }

        if (!b1) {
            font1 = new Font(Font.SANS_SERIF, Font.BOLD, 144);
            System.out.println("Font \"" + font1String + "\" does not exist");
        } else {
            font1 = new Font(font1String, Font.BOLD, 144);
        }

        if (!b2) {
            font2 = new Font(Font.SANS_SERIF, Font.BOLD, 144);
            System.out.println("Font \"" + font2String + "\" does not exist");
        } else {
            font2 = new Font(font2String, Font.PLAIN, 144);
        }

        var shift = 0;
        g2d.setFont(font1);
        if (happyBirthday) {
            final String happyBirthdayString = "HAPPY BIRTHDAY";
            g2d.drawString(happyBirthdayString, (BG_WIDTH - g2d.getFontMetrics().stringWidth(happyBirthdayString)) / 2, (BG_HEIGHT + 144) / 2);
        } else {
            shift = -200;
        }

        if (!name.contains("\n")) {
            g2d.setFont(font2);
            g2d.drawString(name, (BG_WIDTH - g2d.getFontMetrics().stringWidth(name)) / 2, (BG_HEIGHT + 144) / 2 + 200 + shift);
        } else {
            g2d.setFont(font2);
            var startY = 200;
            var line = name.substring(0, name.indexOf("\n"));
            g2d.drawString(line, (BG_WIDTH - g2d.getFontMetrics().stringWidth(line)) / 2, (BG_HEIGHT + 144) / 2 + startY + shift);
            name = name.substring(name.indexOf("\n") + 1);
            while (name.contains("\n")) {
                line = name.substring(0, name.indexOf("\n"));
                startY += 160;
                g2d.drawString(line, (BG_WIDTH - g2d.getFontMetrics().stringWidth(line)) / 2, (BG_HEIGHT + 144) / 2 + startY + shift);
                name = name.substring(name.indexOf("\n") + 1);
            }
            line = name.substring(name.indexOf("\n") + 1);
            startY += 160;
            g2d.drawString(line, (BG_WIDTH - g2d.getFontMetrics().stringWidth(line)) / 2, (BG_HEIGHT + 144) / 2 + startY + shift);
        }
        return image;
    }

    private String createHtmlString(String color) {
        var videoTag = "";
        if (new File("C:\\chrome\\video\\fireworks.mp4").exists()) {
            videoTag = "<video width=\"100%\" autoplay loop muted><source src=\"fireworks.mp4\" type=\"video/mp4\"></video>";
        } else if (new File("C:\\chrome\\video\\fireworks.webm").exists()) {
            videoTag = "<video width=\"100%\" autoplay loop muted><source src=\"fireworks.webm\" type=\"video/webm\"></video>";
        }

        var chromecastSDKScript = "<script type=\"text/javascript\" src=\"https://www.gstatic.com/cv/js/sender/v1/cast_sender.js\"></script>"
                + "<script type=\"text/javascript\">"
                + "        var session = null;"
                + "        document.addEventListener(\"DOMContentLoaded\", function(e) {"
                + "            var loadCastInterval = setInterval(function() {"
                + "                if (chrome.cast.isAvailable) {"
                + "                    clearInterval(loadCastInterval);"
                + "                    initializeCastApi();"
                + "                    chrome.cast.requestSession(onRequestSessionSuccess, onLaunchError);"
                + "                }"
                + "            }, 1000);"
                + "        });"
                + "        function initializeCastApi() {"
                + "            var applicationID = chrome.cast.media.DEFAULT_MEDIA_RECEIVER_APP_ID;"
                + "            var sessionRequest = new chrome.cast.SessionRequest(applicationID);"
                + "            var apiConfig = new chrome.cast.ApiConfig(sessionRequest,sessionListener,receiverListener);"
                + "            chrome.cast.initialize(apiConfig, onInitSuccess, onInitError);"
                + "        }"
                + "        function sessionListener(e) {"
                + "            session = e;"
                + "            console.log(\"new session. found \" + session.media.length + \" sessions\");"
                + "        }"
                + "        function receiverListener(e) {"
                + "            if (e == \"available\") {"
                + "                console.log(\"Chromecast was found on the network.\");"
                + "            }"
                + "            else {"
                + "                console.log(\"There are no Chromecasts available.\");"
                + "            }"
                + "        }"
                + "        function onInitSuccess() {"
                + "            console.log(\"Initialization succeeded\");"
                + "        }"
                + "        function onInitError() {"
                + "            console.log(\"Initialization failed\");"
                + "        }"
                + "        function onRequestSessionSuccess(e) {"
                + "            session = e;"
                + "            console.log(\"Successfully created session: \" + e.sessionId);"
                + "        }"
                + "        function onLaunchError() {"
                + "            console.log(\"Error connecting to the Chromecast.\");"
                + "        }"
                + "</script>";

        if (new File("C:\\chrome\\params\\DO_NOT_USE_CHROMECAST_SDK_SCRIPT.txt").exists()) {
            chromecastSDKScript = "";
        }

        return "<!DOCTYPE html>"
                + "<head>"
                + "<title>Invasion Laser Tag</title>"
                + chromecastSDKScript
                + "<style type=\"text/css\">"
                + "        body { background-color:#000;}"
                + "        .main {height: 100vh;}"
                + "        .player {position: absolute;padding-bottom: 56.25%;padding-top: 25px;height: 0;}"
                + "        .player video {position: absolute;top: 0;left: 0;width: 100%;height: 100%;}"
                + "</style>"
                + "</head>"
                + "<body style=\"overflow:hidden;margin:0;\">"
                + "<div id=\"player\">" + videoTag + "</div>"
                + "<img src=\"" + color + ".png\" style=\"position:absolute;z-index:100;top:0;left:0;width:100%;height:auto\"></img>"
                + "</body>"
                + "</html>";
    }

    private static void launchHttpServer() {
        HttpServer server;
        try {
            server = HttpServer.create(new InetSocketAddress(InetAddress.getLoopbackAddress(), 8000), 0);
        } catch (IOException e) {
            System.out.println("Error creating http server");
            e.printStackTrace();
            return;
        }

        server.createContext("/", new BirthdayNamesHttpHandler());

        server.start();
    }

    private static class BirthdayNamesHttpHandler implements HttpHandler {
        public void handle(HttpExchange httpExchange) throws IOException {
            Headers headers = httpExchange.getResponseHeaders();
            headers.set("Content-Type","text/html");

            var uri = httpExchange.getRequestURI();
            var path = uri.getPath();

            if (path.length() == 0) {
                handleTextResponse(httpExchange, "Bad request", 400);
            }

            var pathWithSlashRemoved = path.substring(1);

            var htmlFilePath = "C:\\chrome\\" + pathWithSlashRemoved + ".html";

            if (pathWithSlashRemoved.equals("fireworks.mp4") ||
                pathWithSlashRemoved.equals("fireworks.webm") ||
                pathWithSlashRemoved.equals("bg.png")) {
                handleFileResponse(httpExchange, "C:\\chrome\\" + pathWithSlashRemoved);
            } else if (
                pathWithSlashRemoved.equals("Orange.png") ||
                pathWithSlashRemoved.equals("Blue.png") ||
                pathWithSlashRemoved.equals("Yellow.png") ||
                pathWithSlashRemoved.equals("Red.png")) {
                handleFileResponse(httpExchange, "C:\\chrome\\" + pathWithSlashRemoved);
            } else if (new File(htmlFilePath).exists()) {
                handleFileResponse(httpExchange, htmlFilePath);
            } else {
                handleTextResponse(httpExchange, "404\n", 404);
            }
        }

        private void handleTextResponse(HttpExchange httpExchange, String text, int responseCode) throws IOException {
            httpExchange.sendResponseHeaders(responseCode, text.length());

            var outputStream = httpExchange.getResponseBody();
            outputStream.write(text.getBytes());
            outputStream.close();
        }

        private void handleFileResponse(HttpExchange httpExchange, String path) throws IOException {
            httpExchange.sendResponseHeaders(200, 0);
            var outputStream = httpExchange.getResponseBody();
            var fileInputStream = new FileInputStream(path);
            final byte[] buffer = new byte[0x10000];
            int count = 0;
            while ((count = fileInputStream.read(buffer)) >= 0) {
                outputStream.write(buffer, 0, count);
            }
            fileInputStream.close();
            outputStream.close();
        }
    }
}