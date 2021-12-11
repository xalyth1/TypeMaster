import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

public class TypeMaster extends JFrame implements Runnable {

    StyledDocument doc = new DefaultStyledDocument();
    JTextPane jTextPane = new JTextPane(doc);
    JScrollPane scrollPane = new JScrollPane(jTextPane);

    JPanel labelsPanel = new JPanel();
    JPanel progressBarPanel;

    JLabel pointerLabel;
    JLabel timeLabel = new JLabel();

    MyTimer myTimer = new MyTimer(timeLabel);
    Thread timerThread;

    int pointer = 0;
    boolean error = false;
    int errorIndex;

    double WPM = 0;
    int words = 0;

    BufferedImage bi = prepareBufferedImage(new BufferedImage(700, 50, BufferedImage.TYPE_INT_ARGB), pointer);

    JButton inputButton;

    KeyAdapter keyAdapter;

    long startMillis = System.currentTimeMillis();

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new TypeMaster());
    }

    public void run() {
        createAndShowGUI();
    }

    public TypeMaster() {
    }

    public void createAndShowGUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 700);

        setResizable(false);
        setLocationRelativeTo(null);
        setTitle("Type Master");

        initializeGUIelements();

        words = jTextPane.getText().split(" ").length;

        //bi = prepareBufferedImage(new BufferedImage(700, 50, BufferedImage.TYPE_INT_ARGB));
        createProgressBarPanel();//
        organizeLayout();

        //SwingUtilities.invokeLater(myTimer);
        timerThread = new Thread(myTimer);
        timerThread.start();
        System.out.println("Timer thread started " + timerThread.getId());

        System.out.println("created GUI");
        setVisible(true);
    }

    public BufferedImage prepareBufferedImage(BufferedImage img, int pointer) {
        Graphics2D paintBrush = img.createGraphics();

        paintBrush.setColor(Color.RED);
        paintBrush.fillRect(0, 0, img.getWidth(), img.getHeight());

        paintBrush.setColor(Color.GREEN);

        //double percentage = error ? (double) pointer /  jTextPane.getText().length() : (double) errorIndex /  jTextPane.getText().length();
        double percentage;

        if (!error) {
            percentage = (double) pointer / jTextPane.getText().length();
        } else {
            percentage = (double) errorIndex / jTextPane.getText().length();
        }

        int progress = (int) (percentage * img.getWidth());
        paintBrush.fillRect(0, 0, progress, 50);

        paintBrush.dispose();
        repaint();
        return img;
    }

    public void createProgressBarPanel() {
        progressBarPanel = new JPanel() {

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D paintBrush = bi.createGraphics();

                g.drawImage(bi, 0, 0, this);
                paintBrush.dispose();
                repaint();
            }
        };

        System.out.println("content pane width: " + this.getContentPane().getWidth());
        progressBarPanel.setPreferredSize(new Dimension(this.getContentPane().getWidth(), 50));
    }

    public KeyAdapter createKeyAdapter() {
        //pointer = 0;
        return new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyPressed(e);

                String text = jTextPane.getText();

                calculateWPM(text);

                if (e.getKeyChar() == text.charAt(pointer)) {
                    changeColor(pointer, Color.GREEN);
                    pointer++;
                } else if (e.getKeyChar() == KeyEvent.VK_BACK_SPACE) {
                    if (pointer > 0)
                        pointer--;
                    changeColor(pointer, Color.BLACK);
                    if (pointer == errorIndex) {
                        error = false;
                    }
                } else if (e.getKeyChar() != text.charAt(pointer)) {
                    changeColor(pointer, Color.RED);
                    if (!error) {
                        error = true; // wrror = true means: occurence of any wrong (red) character in entire text
                        errorIndex = pointer;
                    }
                    pointer++;
                }

                pointerLabel.setText(" Pointer: " + pointer + "    WPM = " + (int) WPM);

                if (!error && pointer == text.length() - 1) {
                    myTimer.end = true;
                }

                jTextPane.setCaretPosition(pointer);
                jTextPane.getCaret().setVisible(true);

                bi = prepareBufferedImage(bi, pointer);
            }
        };
    }

    private void calculateWPM(String text) {
        int correctIndex;
        if (error)
            correctIndex = errorIndex;
        else
            correctIndex = pointer;

        String correctSubSting = text.substring(0, correctIndex);
        int wordsCompleted = correctSubSting.split(" ").length;
        long timeInMillis = System.currentTimeMillis() - startMillis;
        double timeInSeconds = (double) timeInMillis / 1000;
        WPM = (double) wordsCompleted * 60 / timeInSeconds;
    }

    public ActionListener createActionListener() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        String inputText = (String) JOptionPane.showInputDialog(TypeMaster.this/*JOptionPane.getRootFrame()*/,
                                "Please input your own text:",
                                "Customized Dialog",
                                JOptionPane.PLAIN_MESSAGE,
                                null,
                                null,
                                "paste here your text");

                        if (inputText != null) {
                            pointer = 0;
                            error = false;

                            changeColor(pointer, Color.BLACK);

                            myTimer.end = true;
                            myTimer = new MyTimer(timeLabel);
                            timerThread = new Thread(myTimer);
                            timerThread.start();
                            System.out.println("Timer thread started " + timerThread.getId());

                            Font font = new Font("SansSerif", Font.BOLD, 30);
                            jTextPane.setFont(font);

                            jTextPane.setText(inputText);

                            Style style = jTextPane.addStyle("I'm a Style", null);
                            jTextPane.getStyledDocument().setCharacterAttributes(0, inputText.length(), style, true);


                            jTextPane.setEditable(false);
                            //jTextPane.setDisabledTextColor(Color.BLACK);//

                            pointerLabel.setText(" Pointer: " + pointer);
                            //label.setHorizontalAlignment(SwingConstants.LEFT);

                            jTextPane.setCaretPosition(pointer);
                            jTextPane.getCaret().setVisible(true);

                            jTextPane.grabFocus();
                            jTextPane.requestFocusInWindow();

                            bi = prepareBufferedImage(bi, pointer);

                            startMillis = System.currentTimeMillis();
                        }
                    }
                });
            }
        };
    }

    public void initializeGUIelements() {
        //StyledDocument doc = new DefaultStyledDocument();
        //jTextPane = new JTextPane(doc);
        jTextPane.setText("Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.");

        Font font = new Font("SansSerif", Font.BOLD, 30);
        jTextPane.setFont(font);
        jTextPane.setEditable(false);

        pointerLabel = new JLabel();

        Action beep = jTextPane.getActionMap().get(DefaultEditorKit.deletePrevCharAction);
        beep.setEnabled(false);

        keyAdapter = createKeyAdapter();
        jTextPane.addKeyListener(keyAdapter);

        inputButton = new JButton("Input Own Text");
        inputButton.addActionListener(createActionListener());
        inputButton.setFocusable(false); // important: allows avoid problems with returning focus to new JTextPane after returning from JOptionPane.showInputDialog
    }

    public void changeColor(int charIndex, Color color) {
        Style style = jTextPane.addStyle("I'm a Style", null);
        StyleConstants.setForeground(style, color);

        StyledDocument doc = jTextPane.getStyledDocument();

        try {
            //doc.insertString(doc.getLength(), "BLAH ",style);
            doc.setCharacterAttributes(charIndex, 1, style, true);
        } catch (/*BadLocationException e*/ Exception e) {
            e.printStackTrace();
        }
    }

    public void organizeLayout() {
        BoxLayout boxLayout = new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS);
        setLayout(boxLayout);

        //labelsPanel.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
        labelsPanel.add(pointerLabel);
        labelsPanel.add(timeLabel);

        add(progressBarPanel);

        add(labelsPanel);

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BorderLayout());

        //add(jTextPane);
        textPanel.add(scrollPane, BorderLayout.CENTER);
//        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
//        scrollPane.setPreferredSize(new Dimension(20,200));
//        //scrollPane.setCorner(JScrollPane.UPPER_RIGHT_CORNER, new Corner());
//        scrollPane.getVerticalScrollBar().setUnitIncrement(10);

        //textPanel.add(scrollPane.getVerticalScrollBar(), BorderLayout.EAST);

        add(textPanel);

        //add(scrollPane);

        JPanel inputPanel = new JPanel();
        inputPanel.add(inputButton);
        add(inputPanel);
    }
}
