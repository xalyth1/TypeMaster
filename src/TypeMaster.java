import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.SQLOutput;

public class TypeMaster extends JFrame {
    JTextPane tPane;

    JLabel label;
    int pointer;
    boolean error;

    public static void main(String[] args) {
        new TypeMaster();
    }

    public TypeMaster() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 700);

        setResizable(false);
        setLocationRelativeTo(null);
        setTitle("Type Master");

        pointer = 0;
        error = false;

        initializeGUIelements();
        organizeLayout();

        System.out.println("d");
        setVisible(true);
    }

    private void initializeGUIelements() {
        StyledDocument doc = new DefaultStyledDocument();

        tPane = new JTextPane(doc);
        tPane.setText("Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.");

        Font font = new Font("SansSerif", Font.BOLD, 30);
        tPane.setFont(font);
        tPane.setEditable(false);

        label = new JLabel();

        String text = tPane.getText();
        System.out.println("text: " + text);


        Action beep = tPane.getActionMap().get(DefaultEditorKit.deletePrevCharAction);
        beep.setEnabled(false);

        tPane.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyPressed(e);

                System.out.println();
                System.out.println("performed " + e.getKeyChar());

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

                label.setText(" Pointer: " + pointer + "      Color: " + currentColor);

                //tPane.setEditable(true);
                tPane.setCaretPosition(pointer);
                tPane.getCaret().setVisible(true);
                //tPane.setEditable(false);



            }
        });


    }

    void changeColor(int charIndex, Color color) {
        Style style = tPane.addStyle("I'm a Style", null);
        StyleConstants.setForeground(style, color);

        StyledDocument doc = tPane.getStyledDocument();


        try {
            //doc.insertString(doc.getLength(), "BLAH ",style);
            doc.setCharacterAttributes(charIndex, 1, style, true);
        } catch (/*BadLocationException e*/ Exception e) {
            e.printStackTrace();
        }
    }

    private void organizeLayout() {
        BoxLayout boxLayout = new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS);
        setLayout(boxLayout);

        add(label);
        add(tPane);

//        tPane.setFocusable(true);
//        tPane.grabFocus();
//        tPane.requestFocusInWindow();


    }
}
