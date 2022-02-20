import org.sqlite.SQLiteDataSource;

import javax.swing.*;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.io.File;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class TypeMaster extends JFrame implements Runnable {
    StyledDocument doc = new DefaultStyledDocument();
    JTextPane jTextPane = new JTextPane(doc);
    JScrollPane scrollPane = new JScrollPane(jTextPane);

    JPanel labelsPanel = new JPanel();

    JLabel pointerLabel;
    JLabel timeLabel;

    private TypingHandler typingHandler;
    public SwingElements swingElements;

    JButton inputButton;
    JButton databaseButton;
    //int databaseTextIndex;

    KeyAdapter keyAdapter;

    ArrayList<String> al;
    int currentTextIndex;

    private JCheckBox completeCorrectnessModeCheckBox;
    private JButton createTextJButton;

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
        setSize(1000, 700);
        setResizable(false);
        setLocationRelativeTo(null);
        setTitle("Type Master");

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

        keyAdapter = typingHandler.createKeyAdapter();
        jTextPane.addKeyListener(keyAdapter);

        inputButton = new JButton("Input Own Text");
        inputButton.setBackground(LayoutSettings.BUTTON_COLOR);
        //inputButton.setFont(LayoutSettings.getFont());
        inputButton.setForeground(LayoutSettings.getDefaultFontColor());
        inputButton.setFocusPainted(true);
        inputButton.setRolloverEnabled(true);
        inputButton.setBorderPainted(false);
        inputButton.addMouseListener(swingElements.createMouseAdapter(inputButton));
        inputButton.addActionListener(typingHandler.createActionListener());
        inputButton.setFocusable(false); // important: allows avoid problems with returning focus to new JTextPane after returning from JOptionPane.showInputDialog


        databaseButton = new JButton("Load Texts");
        databaseButton.setBackground(LayoutSettings.BUTTON_COLOR);
        databaseButton.setForeground(LayoutSettings.getDefaultFontColor());
        databaseButton.setFocusPainted(true);
        databaseButton.setRolloverEnabled(true);
        databaseButton.setBorderPainted(false);
        databaseButton.addMouseListener(swingElements.createMouseAdapter(databaseButton));
        databaseButton.addActionListener(e -> {
            Database database = new Database();
            al = database.loadDataFromDatabase();
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
            String dbName = "database.db";
            String path = "jdbc:sqlite:" + dbName;
            System.out.println("Working Directory = " + System.getProperty("user.dir"));
            File file = new File(System.getProperty("user.dir") + "\\" + dbName);
            System.out.println(file.exists());

            if (!file.exists()) {
                System.out.println("database file does not exist");
            }

            HashMap<String, Integer> subjects = new HashMap<>();

            SQLiteDataSource dataSource = new SQLiteDataSource();
            dataSource.setUrl(path);
            try (Connection con = dataSource.getConnection()) {
                if (con.isValid(5)) {
                    System.out.println("Connection is valid.");
                    DatabaseMetaData metaData = con.getMetaData();
                    //ResultSet rs = metaData.getTables(null, null, "%", null);

                    String tableName = "SUBJECTS";
                    ResultSet subjectsRS = con.createStatement().executeQuery(
                            "SELECT * FROM " + tableName);
                    while (subjectsRS.next()) {
                        subjects.put(subjectsRS.getString(2), Integer.parseInt(subjectsRS.getString(1)));
                    }

//                    System.out.println("wyc. subj.");
//                    System.out.println(subjects.toString());
                    Object[] possibilities = subjects.keySet().toArray(new String[0]);
//                    JOptionPane.showInputDialog(
//                            this,
//                            "Select text category:",
//                            "Select text category",
//                            JOptionPane.PLAIN_MESSAGE,
//                            null,
//                            possibilities,
//                            possibilities[0]);


                    //JTextField xField = new JTextField(5);
                    JComboBox selectCategoryJComboBox = new JComboBox(possibilities);
                    JTextArea jTextArea = new JTextArea();
                    jTextArea.setPreferredSize(new Dimension(100,100));

                    JPanel myPanel = new JPanel();
                    myPanel.setLayout(new BoxLayout(myPanel, BoxLayout.Y_AXIS));
                    myPanel.add(new JLabel("Select category:"));
                    myPanel.add(selectCategoryJComboBox);
                    myPanel.add(Box.createVerticalStrut(15)); // a spacer
                    myPanel.add(new JLabel("Enter Text:"));
                    myPanel.add(jTextArea);

                    int result = JOptionPane.showConfirmDialog(null, myPanel,
                            "Please Enter X and Y Values", JOptionPane.OK_CANCEL_OPTION);

                    if (result == JOptionPane.OK_OPTION) {
                        // find category id in hash map
                        int index = selectCategoryJComboBox.getSelectedIndex();
                        Integer subject_id = subjects.get(possibilities[index]);


                        con.createStatement().executeQuery("INSERT INTO Texts(text, subject_id) VALUES ('" + jTextArea.getText() +
                                "'," + subject_id + ");");
                    }


                    //ResultSetMetaData rsmd = rs.getMetaData();
                    //int numberOfColumns = rsmd.getColumnCount();
//                    int rowCounted = data_texts.getInt(1);
//                    while (data_texts.next()) {
//                        //System.out.println(data_texts.getString(2));
//                        al.add(data_texts.getString(2));
//                    }
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

        });

        completeCorrectnessModeCheckBox = new JCheckBox("complete corectness");
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

    private void selectSubject() {

    }
    TypingHandler getTypingHandler() {
        return this.typingHandler;
    }
}
