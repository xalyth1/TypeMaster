import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class TypeMaster extends JFrame {
    JTextArea jTextArea;
    JTextField jTextField;
    JTextPane tPane;

    public static void main(String[] args) {
        new TypeMaster();
    }

    public TypeMaster() {
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

    private void initializeGUIelements() {
//        jTextArea = new JTextArea("Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.");
//        jTextArea.setLineWrap(true);
//        jTextArea.setFont(new Font("SansSerif", Font.BOLD, 17));
        StyledDocument doc = new DefaultStyledDocument();

        tPane = new JTextPane(doc);
        tPane.setText("iLorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.");

        Font font = new Font("SansSerif", Font.BOLD, 30);
        tPane.setFont(font);


        jTextField = new JTextField();
        jTextField.setBackground(Color.GRAY);
        jTextField.setForeground(Color.GREEN);

        jTextField.setFont(font);
        //jTextField.setHorizontalAlignment(SwingConstants.TOP);

//        jTextField.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent actionEvent) {
//
//
//                StyledDocument doc = tPane.getStyledDocument();
//                try {
//                    String inputText = doc.getText(0, 100);
//                    String text = jTextField.getText();
//
//                    for (int i = 0; i < 100; i++) {
//                        if (inputText.charAt(i) == text.charAt(i)) {
//                            changeColorTest(i);
//                        }
//                    }
//
//                } catch (BadLocationException e) {
//                    System.out.println(e);
//                }
//            }
//        });

        jTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyPressed(e);

                System.out.println();
                System.out.println("performed");



                try {
                    StyledDocument doc = tPane.getStyledDocument();
                    String inputText = doc.getText(0, doc.getLength());
                    System.out.println("input text lenght "  + inputText.length());
                    String text = jTextField.getText();

                    System.out.println("text length " + text.length());

                    //Math.min(inputText.length(), text.length())

                    for (int i = 0; i < Math.min(inputText.length(), text.length() + 1); i++) {
                        if (inputText.charAt(i) == text.charAt(i)) {
                            changeColorTest(i, Color.GREEN);
                        } else {
                            changeColorTest(i, Color.RED);
                        }

                    }

                } catch (Exception e1) {
                   e1.printStackTrace();
                }

            }
        });



    }

    void changeColorTest(int charIndex, Color color) {
        Style style = tPane.addStyle("I'm a Style", null);
        StyleConstants.setForeground(style, color);

        StyledDocument doc = tPane.getStyledDocument();


        try {
            //doc.insertString(doc.getLength(), "BLAH ",style);
            doc.setCharacterAttributes(charIndex, 1, style, true);
        }
        catch (/*BadLocationException e*/ Exception e){
            e.printStackTrace();
        }
    }

    private void organizeLayout() {
        BoxLayout boxLayout = new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS);
        setLayout(boxLayout);


        add(tPane);
        add(jTextField);

    }
}
