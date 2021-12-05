import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

public class TypeMaster extends JFrame {
    JTextPane jTextPane;


    JPanel labelsPanel = new JPanel();
    JPanel progressBarPanel;

    JLabel pointerLabel;



    int pointer = 0;
    boolean error = false;

    BufferedImage bi;


    JButton inputButton;
    KeyAdapter keyAdapter;
    StyledDocument doc = new DefaultStyledDocument();

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });

        //new TypeMaster();
    }

    public TypeMaster() {

    }

    public TypeMaster(boolean x) {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 700);

        setResizable(false);
        setLocationRelativeTo(null);
        setTitle("Type Master");


        initializeGUIelements();
        organizeLayout();

        System.out.println("d");
        setVisible(true);
    }

    public static void createAndShowGUI() {
        TypeMaster frame = new TypeMaster();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(700, 700);

        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setTitle("Type Master");



        frame.initializeGUIelements();

        frame.bi = frame.prepareBufferedImage(new BufferedImage(700, 50, BufferedImage.TYPE_INT_ARGB));
        frame.createProgressBarPanel();//
        frame.organizeLayout();

        System.out.println("d");
        frame.setVisible(true);
    }

    public BufferedImage prepareBufferedImage(BufferedImage img) {
        Graphics2D paintBrush = img.createGraphics();

        paintBrush.setColor(Color.RED);
        paintBrush.fillRect(0,0, img.getWidth(), img.getHeight());

        paintBrush.setColor(Color.GREEN);

        System.out.println("AAA " + jTextPane);
        int progress = pointer / jTextPane.getText().length();
        paintBrush.fillRect(0,0, progress, 50);

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

                paintBrush.setColor(Color.GREEN);
                int progress = pointer / jTextPane.getText().length();
                paintBrush.fillRect(0,0, progress, 50);

                g.drawImage(bi, 0, 0, this);
                paintBrush.dispose();
                repaint();


            }
        };
        //progressBarPanel.setBackground(Color.WHITE);
        progressBarPanel.setMaximumSize(new Dimension(700, 50));
        progressBarPanel.setMinimumSize(new Dimension(700, 50));
        progressBarPanel.setPreferredSize(new Dimension(700, 50));
        progressBarPanel.setAlignmentX(0);

    }

    public void updateProgressBarPanel() {

    }


    public KeyAdapter createKeyAdapter() {
        //pointer = 0;
        return new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyPressed(e);

                System.out.println();
                System.out.println("performed " + e.getKeyChar());

                updateProgressBarPanel();
                bi = prepareBufferedImage(bi);
                progressBarPanel.repaint();



                String text = jTextPane.getText();
                System.out.println("text: " + text);

                String currentColor = null;

                if (e.getKeyChar() == text.charAt(pointer)) {
                    changeColor(pointer, Color.GREEN);
                    currentColor = "GREEN";
                    pointer++;
                } else if (e.getKeyChar() == KeyEvent.VK_BACK_SPACE) {


                    if (pointer > 0)
                        pointer--;
                    changeColor(pointer, Color.BLACK);
                    currentColor = "BLACK";
                } else if (e.getKeyChar() != text.charAt(pointer)) {
                    changeColor(pointer, Color.RED);
                    currentColor = "RED";
                    pointer++;
                }

                System.out.println("set label text " + pointer);
                pointerLabel.setText(" Pointer: " + pointer + "      Color: " + currentColor);




                jTextPane.setCaretPosition(pointer);
                jTextPane.getCaret().setVisible(true);



            }
        };
    }

    public ActionListener createActionListener() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {

                        //
                        String inputText = (String) JOptionPane.showInputDialog(TypeMaster.this/*JOptionPane.getRootFrame()*/,
                                "Please input your own text:",
                                "Customized Dialog",
                                JOptionPane.PLAIN_MESSAGE,
                                null,
                                null,
                                "paste here your text");

                        if (inputText != null) {
                            pointer = 0;
                            //tPane = new JTextPane(doc);

                            changeColor(pointer, Color.BLACK);
                            jTextPane.setText(inputText);


                            //tPane.addKeyListener(keyAdapter);

                            Font font = new Font("SansSerif", Font.BOLD, 30);
                            jTextPane.setFont(font);

                            jTextPane.setEditable(false);


                            pointerLabel.setText(" Pointer: " + pointer);
                            //label.setHorizontalAlignment(SwingConstants.LEFT);

                            jTextPane.setCaretPosition(pointer);
                            jTextPane.getCaret().setVisible(true);

                            jTextPane.grabFocus();
                            jTextPane.requestFocusInWindow();
                        }
                    }
                });
            }
        };
    }

    public void initializeGUIelements() {
        //StyledDocument doc = new DefaultStyledDocument();
        jTextPane = new JTextPane(doc);
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

        add(progressBarPanel);

        add(labelsPanel);

        add(jTextPane);

        add(inputButton);
    }
}
