import javax.swing.*;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TypeMaster extends JFrame implements Runnable {
    private final StyledDocument doc = new DefaultStyledDocument();
    private final JTextPane jTextPane = new JTextPane(doc);
    private final JScrollPane scrollPane = new JScrollPane(jTextPane);

    private final JPanel labelsPanel = new JPanel();

    private JLabel pointerLabel;
    private JLabel timeLabel;

    private TypingHandler typingHandler;
    public SwingElements swingElements;

    private JButton inputButton;
    private JButton databaseButton;

    ArrayList<String> al;
    int currentTextIndex;

    private JCheckBox completeCorrectnessModeCheckBox;
    private JButton createTextJButton;

    private Database database = new Database();

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new TypeMaster());
    }

    public void run() {
        createAndShowGUI();
    }

    public TypeMaster() { }

    public void createAndShowGUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(LayoutSettings.MAIN_FRAME_WIDTH, LayoutSettings.MAIN_FRAME_HEIGHT);
        setResizable(false);
        setLocationRelativeTo(null);
        setTitle("Type Master - Learn typing really fast!");

        typingHandler = new TypingHandler(this);
        swingElements = new SwingElements(this);

        initializeGUI();
        organizeLayout();

        System.out.println("created GUI");
        setVisible(true);
    }

    public void initializeGUI() {
        jTextPane.setText("Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.");
        jTextPane.setForeground(LayoutSettings.getDefaultFontColor());
        jTextPane.setFont(LayoutSettings.getFont());
        jTextPane.setBackground(LayoutSettings.getBackgroundColor());// grey

        jTextPane.setEditable(false);

        Action beep = jTextPane.getActionMap().get(DefaultEditorKit.deletePrevCharAction);
        beep.setEnabled(false);

        jTextPane.addKeyListener(typingHandler.createKeyAdapter());

        inputButton = new JButton("Input Own Text");
        inputButton.setBackground(LayoutSettings.BUTTON_COLOR);
        //inputButton.setFont(LayoutSettings.getFont());
        inputButton.setForeground(LayoutSettings.getDefaultFontColor());
        inputButton.setFocusPainted(true);
        inputButton.setRolloverEnabled(true);
        inputButton.setBorderPainted(false);
        inputButton.addMouseListener(swingElements.createMouseAdapter(inputButton));
        inputButton.addActionListener(typingHandler.createActionListener());
        inputButton.setFocusable(false); // important: allows to avoid problems with returning focus to new JTextPane after returning from JOptionPane.showInputDialog

        databaseButton = new JButton("Load Texts");
        databaseButton.setBackground(LayoutSettings.BUTTON_COLOR);
        databaseButton.setForeground(LayoutSettings.getDefaultFontColor());
        databaseButton.setFocusPainted(true);
        databaseButton.setRolloverEnabled(true);
        databaseButton.setBorderPainted(false);
        databaseButton.addMouseListener(swingElements.createMouseAdapter(databaseButton));
        databaseButton.addActionListener(e -> {
            List<String> al = database.loadDataFromDatabase();
            typingHandler.setText(al.get(0));
            currentTextIndex = 0;
        });

        createTextJButton = new JButton("Create Text");
        createTextJButton.setBackground(LayoutSettings.BUTTON_COLOR);
        createTextJButton.setForeground(LayoutSettings.getDefaultFontColor());
        createTextJButton.setFocusPainted(true);
        createTextJButton.setRolloverEnabled(true);
        createTextJButton.setBorderPainted(false);
        createTextJButton.addMouseListener(swingElements.createMouseAdapter(createTextJButton));
        createTextJButton.addActionListener(e -> {
            Optional<String[]> opt = database.getPossibilities();
            if (opt.isPresent()) {
                String[] possibilities = opt.get();
                InputText inputText = swingElements.getInputDataFromUser(possibilities);
                if (inputText.getId() == -1) {
                    JOptionPane.showMessageDialog(this, "Invalid subject category has been chosen.",
                            "User Input Error Message", JOptionPane.ERROR_MESSAGE);
                } else {
                    System.out.println("id: " + inputText.getId());
                    System.out.println("txt: " + inputText.getText());
                    boolean result = database.insertText(inputText.getId(), inputText.getText());
                    if (!result) {
                        JOptionPane.showMessageDialog(this, "Could not add text to database. Please try again.",
                                "Database Error Message", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "There is no category available. Please add category and texts or change database file.",
                        "Database Error Message", JOptionPane.ERROR_MESSAGE);
            }
        });

        completeCorrectnessModeCheckBox = new JCheckBox("complete correctness");
        completeCorrectnessModeCheckBox.setBackground(LayoutSettings.BUTTON_COLOR);
        completeCorrectnessModeCheckBox.setForeground(LayoutSettings.getDefaultFontColor());
        completeCorrectnessModeCheckBox.setFocusPainted(true);
        completeCorrectnessModeCheckBox.setRolloverEnabled(true);
        completeCorrectnessModeCheckBox.setBorderPainted(false);

        setJMenuBar(swingElements.createMenu());
    }

    public void organizeLayout() {
        BoxLayout boxLayout = new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS);
        setLayout(boxLayout);

        //labelsPanel.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
        pointerLabel = new JLabel();
        timeLabel = new JLabel();

        pointerLabel.setFont(LayoutSettings.LABELS_PANEL_FONT);
        timeLabel.setFont(LayoutSettings.LABELS_PANEL_FONT);

        labelsPanel.add(pointerLabel);
        labelsPanel.add(timeLabel);

        JPanel progressBarPanel = swingElements.createProgressBarPanel();
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

        JPanel inputPanel = new JPanel();
        inputPanel.setBackground(LayoutSettings.INPUT_PANEL_COLOR);
        inputPanel.add(inputButton);
        inputPanel.add(databaseButton);
        inputPanel.add(createTextJButton);
        inputPanel.add(completeCorrectnessModeCheckBox);
        add(inputPanel);
    }


    public boolean loadAnotherDatabase() {
        //TODO
        // show input dialog
        // ask user for database name and path  -> select file in chooser
        // create database object from user inputted file
        // setDatabase(newDatabase)
        // return operation is successful or not
        return false;
    }

//    private void selectSubject() {
//
//    }

    public TypingHandler getTypingHandler() {
        return this.typingHandler;
    }

    public JTextPane getJTextPane() {
        return jTextPane;
    }

    public JLabel getPointerLabel() {
        return pointerLabel;
    }

    public JLabel getTimeLabel() {
        return timeLabel;
    }

    public Database getDatabase() {
        return database;
    }

    public void setDatabase(Database database) {
        this.database = database;
    }
}