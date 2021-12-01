import javax.swing.*;
import java.awt.*;

public class TypeMaster extends JFrame {
    JTextArea jTextArea;
    JTextField jTextField;

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


        setVisible(true);
    }

    private void initializeGUIelements() {
        jTextArea = new JTextArea("Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.");
        jTextArea.setLineWrap(true);

        jTextField = new JTextField();
        jTextField.setBackground(Color.GRAY);
        jTextField.setForeground(Color.GREEN);
        Font font = new Font("SansSerif", Font.BOLD, 20);
        jTextField.setFont(font);


    }

    private void organizeLayout() {
        BoxLayout boxLayout = new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS);
        setLayout(boxLayout);


        add(jTextArea);
        add(jTextField);

    }
}
