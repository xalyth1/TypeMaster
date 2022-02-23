import javax.swing.*;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TypeMaster extends JFrame implements Runnable {

    private final StyledDocument doc = new DefaultStyledDocument();
    private final JTextPane jTextPane = new JTextPane(doc);
    private final JScrollPane scrollPane = new JScrollPane(jTextPane);

    private final JPanel labelsPanel = new JPanel();
    private JPanel settingsPanel;

    private JLabel pointerLabel;
    private JLabel timeLabel;

    private TypingHandler typingHandler;
    public SwingElements swingElements;

    private JButton settingsButton;
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


        settingsButton = new JButton("Settings");
        settingsButton.setBackground(LayoutSettings.BUTTON_COLOR);
        settingsButton.setForeground(LayoutSettings.getDefaultFontColor());
        settingsButton.setFocusPainted(true);
        settingsButton.setRolloverEnabled(true);
        settingsButton.setBorderPainted(false);
        settingsButton.addMouseListener(swingElements.createMouseAdapter(settingsButton));
        settingsButton.addActionListener(e -> settingsPanel.setVisible(!settingsPanel.isVisible()));


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
            try {
                List<String> al = database.loadDataFromDatabase();
                typingHandler.setText(al.get(0));
                currentTextIndex = 0;

            } catch (SQLException exception) {
                exception.printStackTrace();
                JOptionPane.showMessageDialog(this, "Database error. Could not load data from database.",
                        "Database error", JOptionPane.ERROR_MESSAGE);
            }
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
        progressBarPanel.add(labelsPanel);
        labelsPanel.setOpaque(false);
        add(progressBarPanel);

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BorderLayout());

        textPanel.add(scrollPane, BorderLayout.CENTER);

        settingsPanel = swingElements.createSettingsPanel();
        textPanel.add(settingsPanel, BorderLayout.WEST);

        add(textPanel);

        JPanel inputPanel = new JPanel();
        inputPanel.setBackground(LayoutSettings.INPUT_PANEL_COLOR);
        inputPanel.add(settingsButton);
        inputPanel.add(inputButton);
        inputPanel.add(databaseButton);
        inputPanel.add(createTextJButton);
        inputPanel.add(completeCorrectnessModeCheckBox);
        add(inputPanel);
    }


    public boolean loadAnotherDatabase() {
        JFileChooser jFileChooser = new JFileChooser(System.getProperty("user.dir"));

        int returnValue = jFileChooser.showOpenDialog(null);

        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = jFileChooser.getSelectedFile();

            //System.out.println("   Selelected file: " + selectedFile.toString());

            String path = selectedFile.getAbsolutePath();

            System.out.println("Path: " + path);
            System.out.println("file name: " + selectedFile.getName());


            //System.out.println("File to load path: " + path);
            if (selectedFile.exists()) {
                System.out.println("Selected file exists");
                try {
                    //String str = Files.readString(Paths.get(path));
                    //byte[] bytes = Files.readAllBytes(Paths.get(path));
                    this.database = new Database(selectedFile.getName());

                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        }


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