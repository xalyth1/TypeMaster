import javax.swing.*;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.nio.charset.StandardCharsets;

public class TypingHandler {

    private int pointer = 0;
    private boolean error = false;
    private int errorIndex;

    private double WPM = 0;
    private int words = 0;


    private MyTimer myTimer;

    private long startMillis;

    private Thread timerThread;

    private TypeMaster typeMaster;

    public TypingHandler(TypeMaster typeMaster) {
        this.typeMaster = typeMaster;
        words = typeMaster.getJTextPane().getText().split(" ").length;
        myTimer = new MyTimer(typeMaster.getTimeLabel());
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
        //WPM = (double) wordsCompleted * 60 / timeInSeconds;
        WPM = (double) correctIndex / 5 * 60 / timeInSeconds;

        //newWPM = (double) correctIndex / 5 * 60 / timeInSeconds;
    }

    /**
     *
     * @return ActionListener, which is used in inputButton (input own text)
     */
    public ActionListener createActionListener() {
        return actionEvent -> SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                String inputText = (String) JOptionPane.showInputDialog(typeMaster/*JOptionPane.getRootFrame()*/,
                        "Please input your own text:",
                        "Input own text",
                        JOptionPane.PLAIN_MESSAGE,
                        null,
                        null,
                        "paste here your text");

                if (inputText != null) {
                    pointer = 0;
                    error = false;

                    changeColor(pointer, Color.BLACK);

                    myTimer.setFinished(true);
                    myTimer = new MyTimer(typeMaster.getTimeLabel());
                    timerThread = new Thread(myTimer);
                    timerThread.start();
                    System.out.println("Timer thread started id=" + timerThread.getId());

                    JTextPane jTextPane = typeMaster.getJTextPane();

                    Font font = new Font("SansSerif", Font.BOLD, 30);
                    jTextPane.setFont(font);


                    byte[] bytes = inputText.getBytes();
                    String utf8EncodedText = new String(bytes, StandardCharsets.UTF_8);

                    jTextPane.setText(utf8EncodedText);

                    Style style = jTextPane.addStyle("I'm a Style", null);
                    jTextPane.getStyledDocument().setCharacterAttributes(0, inputText.length(), style, true);

                    jTextPane.setEditable(false);
                    //jTextPane.setDisabledTextColor(Color.BLACK);//

                    typeMaster.getPointerLabel().setText(" Pointer: " + pointer);
                    //label.setHorizontalAlignment(SwingConstants.LEFT);

                    jTextPane.setCaretPosition(pointer);
                    jTextPane.setCaretColor(LayoutSettings.getDefaultFontColor());

                    System.out.println("caret color: " + jTextPane.getCaretColor());

                    jTextPane.getCaret().setVisible(true);

                    jTextPane.grabFocus();
                    jTextPane.requestFocusInWindow();

                    typeMaster.swingElements.updateBufferedImage(pointer);

                    startMillis = System.currentTimeMillis();
                }
            }
        });
    }

    public KeyAdapter createKeyAdapter() {
        return new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyPressed(e);
                JTextPane jTextPane = typeMaster.getJTextPane();
                String text = jTextPane.getText()
                        .replace('“', '"') // left-double-quotation mark  -> quotation mark
                        .replace('”', '"') // right-double-quotation mark  -> quotation mark
                        .replace("’", "'"); // right-single-quotation mark -> apostrophe

                calculateWPM(text);

                if (e.getKeyChar() == text.charAt(pointer)) {
                    changeColor(pointer, LayoutSettings.getGoodLetterColor());
                    pointer++;
                    jTextPane.setCaretPosition(pointer);///////////////////////
                } else if (e.getKeyChar() == KeyEvent.VK_BACK_SPACE) {
                    if (pointer >= text.length()) {
                        if (error) {
                            pointer = text.length() - 1;
                        }
                    }

                    if (pointer > 0)
                        pointer--;
                    changeColor(pointer, LayoutSettings.getDefaultFontColor());
                    if (pointer == errorIndex) {
                        error = false;
                    }
                } else if (e.getKeyChar() != text.charAt(pointer)) {
                    changeColor(pointer, LayoutSettings.getWrongLetterColor());
                    if (!error) {
                        error = true; // wrong = true means: occurence of any wrong (red) character in entire text
                        errorIndex = pointer;
                    }
                    pointer++;
                }

                typeMaster.getPointerLabel().setText(" Pointer: " + pointer + "    WPM = " + (int) WPM );

                if (!error && pointer == text.length()) {
                    myTimer.setFinished(true);
                    // if input own text mode
                    if (typeMaster.al == null) {
                        //typeMaster.pointerLabel.setText("Typing test ended. Results: WPM:" + (int) WPM + " Time: " + myTimer.getTimeElapsed());
                    } else if (typeMaster.currentTextIndex < typeMaster.al.size() - 1) {
                        typeMaster.currentTextIndex++;
                        setText(typeMaster.al.get(typeMaster.currentTextIndex));
                        pointer = 0;
                    } else {
                        typeMaster.getPointerLabel().setText("No more texts.");
                    }
                }

                jTextPane.setCaretPosition(pointer);
                jTextPane.getCaret().setVisible(pointer >= text.length() ? false : true);

                typeMaster.swingElements.updateBufferedImage(pointer);
            }
        };
    }

    void setText(String str) {
        pointer = 0;
        error = false;

        changeColor(pointer, LayoutSettings.getDefaultFontColor());

        myTimer.setFinished(true);
        myTimer = new MyTimer(typeMaster.getTimeLabel());
        timerThread = new Thread(myTimer);
        timerThread.start();
        //System.out.println("Timer thread started " + timerThread.getId());

        //Font font = new Font("SansSerif", Font.BOLD, 30);

        JTextPane jTextPane = typeMaster.getJTextPane();
        jTextPane.setFont(LayoutSettings.getFont());
        jTextPane.setText(str);

        Style style = jTextPane.addStyle("I'm a Style", null);
        jTextPane.getStyledDocument().setCharacterAttributes(0, str.length(), style, true);


        jTextPane.setEditable(false);
        //jTextPane.setDisabledTextColor(Color.BLACK);//

        typeMaster.getPointerLabel().setText(" Pointer: " + pointer);
        //label.setHorizontalAlignment(SwingConstants.LEFT);

        jTextPane.setCaretPosition(pointer);
        jTextPane.getCaret().setVisible(true);

        jTextPane.grabFocus();
        jTextPane.requestFocusInWindow();

        typeMaster.swingElements.updateBufferedImage(pointer);

        startMillis = System.currentTimeMillis();
    }

    public void changeColor(int charIndex, Color color) {
        Style style = typeMaster.getJTextPane().addStyle("I'm a Style", null);
        StyleConstants.setForeground(style, color);

        StyledDocument doc = typeMaster.getJTextPane().getStyledDocument();

        try {
            //doc.insertString(doc.getLength(), "BLAH ",style);
            doc.setCharacterAttributes(charIndex, 1, style, true);
        } catch (/*BadLocationException e*/ Exception e) {
            e.printStackTrace();
        }
    }

//    public void startTyping() {
//        startMillis = System.currentTimeMillis();
//        timerThread = new Thread(myTimer);
//        timerThread.start();
//        System.out.println("Timer thread started " + timerThread.getId());
//    }

    public double getTextCompletedPercentage() {
        return 0.0;
    }

    public int getPointer() {
        return pointer;
    }

    public boolean isError() {
        return error;
    }

    public int getErrorIndex() {
        return errorIndex;
    }
}
