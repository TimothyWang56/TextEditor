package textEditor;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.*;
import java.io.*;

public class SaveActionListener implements ActionListener {
    private TextEditor editor;
    private boolean saveAs;

    public SaveActionListener(TextEditor editor, boolean saveAs) {
        this.editor = editor;
        this.saveAs = saveAs;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (editor.currentPath != null && !this.saveAs) {
            writeToFile(editor.currentPath);
        } else {
            int userSelection = editor.saveFileChooser.showSaveDialog(null);
            if (userSelection == JFileChooser.APPROVE_OPTION) {
                File selectedFile = editor.saveFileChooser.getSelectedFile();
                String path = selectedFile.getAbsolutePath();
                String fileName = selectedFile.getName();
                if (fileName == "") {
                    fileName = "untitled.txt";
                }
                if (!fileName.endsWith(".txt")) {
                    fileName += ".txt";
                    path += ".txt";
                }
                editor.currentPath = path;
                editor.setTitle(fileName);
                writeToFile(path);
            }
        }
    }

    private void writeToFile(String path) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(new File(path)))) {
            String text = editor.textArea.getText();
            writer.write(text);

        } catch (IOException ioe) {
            System.out.println(ioe.getMessage());
        }
    }
}
