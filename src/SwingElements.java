import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class SwingElements {
    private BufferedImage bi;
    private TypeMaster typeMaster;
    TypingHandler typingHandler;


    public SwingElements(TypeMaster typeMaster) {
        this.typeMaster = typeMaster;
        this.typingHandler = this.typeMaster.getTypingHandler();
        this.bi = new BufferedImage(typeMaster.getWidth(), 50, BufferedImage.TYPE_INT_ARGB);
        updateBufferedImage(typingHandler.pointer);
    }

    JMenuBar createMenu() {
        JMenuBar jMenuBar = new JMenuBar();

        JMenu fileJMenu = new JMenu("File");
        JMenuItem loadJMenuItem = new JMenuItem("Load new database file");
        loadJMenuItem.addActionListener( e -> {
            this.typeMaster.loadAnotherDatabase();
        });
        JMenuItem saveJMenuItem = new JMenuItem("Save");
        JMenuItem exitJMenuItem = new JMenuItem("Exit");

        jMenuBar.add(fileJMenu);
        fileJMenu.add(loadJMenuItem);
        fileJMenu.add(saveJMenuItem);
        fileJMenu.add(exitJMenuItem);

        JMenu viewMenu = new JMenu("View");
        jMenuBar.add(viewMenu);

        JMenuItem largerFontMenuItem = new JMenuItem("Increase font size");
        viewMenu.add(largerFontMenuItem);
        largerFontMenuItem.addActionListener(e -> {
            Font f = typeMaster.getJTextPane().getFont();
            typeMaster.getJTextPane().setFont(new Font(f.getName(), f.getStyle(), f.getSize() + 2));
        });

        JMenuItem smallerFontMenuItem = new JMenuItem("Decrease font size");
        viewMenu.add(smallerFontMenuItem);
        smallerFontMenuItem.addActionListener(e -> {
            Font f = typeMaster.getJTextPane().getFont();
            typeMaster.getJTextPane().setFont(new Font(f.getName(), f.getStyle(), f.getSize() - 2));
        });

        JMenuItem changeFontColorMenuItem = new JMenuItem("Change font color");
        viewMenu.add(changeFontColorMenuItem);
        changeFontColorMenuItem.addActionListener(e ->
                typeMaster.getJTextPane().setForeground(JColorChooser.showDialog(null, "Choose Font Color", Color.BLACK)));

        JMenuItem changeBackgroundColorMenuItem = new JMenuItem("Change text background color");
        viewMenu.add(changeBackgroundColorMenuItem);
        changeBackgroundColorMenuItem.addActionListener(e ->
                typeMaster.getJTextPane().setBackground(JColorChooser.showDialog(null, "Choose Font Color", Color.BLACK)));

        exitJMenuItem.addActionListener(e -> System.exit(0));

        return jMenuBar;
    }

    JPanel createProgressBarPanel() {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D paintBrush = bi.createGraphics();

                g.drawImage(bi, 0, 0, this);
                paintBrush.dispose();
                repaint();
            }
        };

        System.out.println("content pane width: " + typeMaster.getContentPane().getWidth());
        panel.setPreferredSize(new Dimension(typeMaster.getContentPane().getWidth(), 50));
        return panel;
    }

    void updateBufferedImage(int pointer) {
        BufferedImage img = this.bi;
        Graphics2D paintBrush = img.createGraphics();

        paintBrush.setColor(LayoutSettings.getWrongLetterColor());
        paintBrush.fillRect(0, 0, img.getWidth(), img.getHeight());

        paintBrush.setColor(LayoutSettings.getGoodLetterColor());

        //double percentage = error ? (double) pointer /  jTextPane.getText().length() : (double) errorIndex /  jTextPane.getText().length();
        double percentage;

        if (!typingHandler.error) {
            percentage = (double) pointer / typeMaster.getJTextPane().getText().length();
        } else {
            percentage = (double) typingHandler.errorIndex / typeMaster.getJTextPane().getText().length();
        }

        int progress = (int) (percentage * img.getWidth());
        paintBrush.fillRect(0, 0, progress, 50);

        paintBrush.dispose();
        typeMaster.repaint();
    }

    MouseAdapter createMouseAdapter(JButton button) {
        return new java.awt.event.MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                button.setBackground(LayoutSettings.BUTTON_FOCUS_COLOR);
            }
            public void mouseExited(MouseEvent evt) {
                button.setBackground(LayoutSettings.BUTTON_COLOR);
            }
        };
    }


    InputText getInputDataFromUser(String[] possibilities) {
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
                "Please Select Category and Enter Text", JOptionPane.OK_CANCEL_OPTION);

        int index = -1;
        if (result == JOptionPane.OK_OPTION) {
            // find category id in hash map
            index = selectCategoryJComboBox.getSelectedIndex();
            //Integer subject_id = subjects.get(possibilities[index]);
        }
        return new InputText(index, jTextArea.getText());
    }
}
