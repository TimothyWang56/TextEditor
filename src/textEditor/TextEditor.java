package textEditor;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

public class TextEditor extends JFrame {
    String currentPath = null;
    JTextArea textArea = new JTextArea();
    JFileChooser fileChooser =  new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
    JFileChooser saveFileChooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
    JTextField searchField = new JTextField(12);
    JCheckBox regexCheckBox = new JCheckBox("Regex");
    Integer[][] indexes = null;
    Boolean alreadySearching = false;
    Integer currentIndex = null;
    JCheckBox caseSensitiveBox = new JCheckBox("Case Sensitive");

    public TextEditor() {
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });
        setSize(650, 400);
        setLocationRelativeTo(null);
        setTitle("Untitled");

        setFileChoosers();
        createTopBar();
        createFilesMenu();
        createTextArea();
        setVisible(true);
    }

    private void setFileChoosers() {
        FileNameExtensionFilter txtFilter = new FileNameExtensionFilter("txt files", "txt");
        this.fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        this.fileChooser.setFileFilter(txtFilter);
        this.fileChooser.setAcceptAllFileFilterUsed(false);
        this.saveFileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        this.saveFileChooser.setSelectedFile(new File("untitled"));
    }

    private void createTextArea() {
        this.textArea.setLineWrap(true);
        this.textArea.getDocument().addDocumentListener(new TextListener(this));
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BorderLayout());
        textPanel.add(this.textArea);
        JScrollPane scrollPane = new JScrollPane(textPanel,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);
    }


    private void createFilesMenu() {
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");

        JMenuItem newItem = new JMenuItem("New");
        newItem.addActionListener(e -> new TextEditor());

        JMenuItem saveAsItem = new JMenuItem("Save As");
        saveAsItem.addActionListener(new SaveActionListener(this, true));

        JMenuItem saveItem = new JMenuItem("Save");
        saveItem.addActionListener(new SaveActionListener(this, false));

        JMenuItem loadItem = new JMenuItem("Load");
        loadItem.addActionListener(new FileActionListener(this));

        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(e -> dispose());

        fileMenu.add(newItem);
        fileMenu.addSeparator();
        fileMenu.add(saveAsItem);
        fileMenu.add(saveItem);
        fileMenu.add(loadItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);

        menuBar.add(fileMenu);
        setJMenuBar(menuBar);
    }

    private void createTopBar() {
        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(new SaveActionListener(this, false));

        JButton loadButton = new JButton("Load");
        loadButton.addActionListener(new FileActionListener(this));

        JButton backwardButton = new JButton();
        backwardButton.addActionListener(new SearchActionListener(this, false));

        JButton forwardButton = new JButton();
        forwardButton.addActionListener(new SearchActionListener(this, true));

        ImageIcon saveIcon = new ImageIcon(getClass().getResource("/resources/save.png"));
        Image bigSave = saveIcon.getImage();
        Image littleSave = bigSave.getScaledInstance(18, 18, Image.SCALE_SMOOTH);
        saveIcon = new ImageIcon(littleSave);
        saveButton.setIcon(saveIcon);
        ImageIcon loadIcon = new ImageIcon(getClass().getResource("/resources/load.png"));
        Image bigLoad = loadIcon.getImage();
        Image littleLoad = bigLoad.getScaledInstance(18, 18, Image.SCALE_SMOOTH);
        loadIcon = new ImageIcon(littleLoad);
        loadButton.setIcon(loadIcon);
        ImageIcon leftIcon = new ImageIcon(getClass().getResource("/resources/left_arrow.png"));
        Image bigLeft = leftIcon.getImage();
        Image littleLeft = bigLeft.getScaledInstance(18, 18, Image.SCALE_SMOOTH);
        leftIcon = new ImageIcon(littleLeft);
        backwardButton.setIcon(leftIcon);
        ImageIcon rightIcon = new ImageIcon(getClass().getResource("/resources/right_arrow.png"));
        Image bigRight = rightIcon.getImage();
        Image littleRight = bigRight.getScaledInstance(18, 18, Image.SCALE_SMOOTH);
        rightIcon = new ImageIcon(littleRight);
        forwardButton.setIcon(rightIcon);

        this.searchField.getDocument().addDocumentListener(new TextListener(this));
        this.caseSensitiveBox.addActionListener(e -> resetRegex());
        this.regexCheckBox.addActionListener(e -> resetRegex());

        JLabel findLabel = new JLabel("Find:");
        findLabel.setLabelFor(this.searchField);

        JPanel filesBar = new JPanel();
        filesBar.add(saveButton);
        filesBar.add(loadButton);
        filesBar.add(findLabel);
        filesBar.add(this.searchField);
        filesBar.add(backwardButton);
        filesBar.add(forwardButton);
        filesBar.add(this.regexCheckBox);
        filesBar.add(this.caseSensitiveBox);
        if (System.getProperty("os.name").toLowerCase().indexOf("mac") >= 0) {
            System.setProperty("apple.laf.useScreenMenuBar", "true");
        }
        add(filesBar, BorderLayout.NORTH);
    }

    protected void resetRegex() {
        this.currentIndex = null;
        this.indexes = null;
        this.alreadySearching = false;
    }

    public static void main(String[] args) {
        new TextEditor();
    }
}
