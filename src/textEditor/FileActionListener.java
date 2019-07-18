package textEditor;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.*;
import java.io.*;

public class FileActionListener implements ActionListener {
    private TextEditor editor;

    public FileActionListener(TextEditor editor) {
        this.editor = editor;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int userSelection = editor.fileChooser.showOpenDialog(null);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File selectedFile = editor.fileChooser.getSelectedFile();
            String path = selectedFile.getAbsolutePath();
            editor.currentPath = path;
            editor.setTitle(selectedFile.getName());
            try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
                editor.textArea.setText(null);
                String line = reader.readLine();
                while (line != null) {
                    editor.textArea.append(line);
                    line = reader.readLine();
                    if (line != null) {
                        editor.textArea.append("\n");
                    }
                }
            } catch (IOException ioe) {
                System.out.println(ioe.getMessage());
            }
        }
    }
}
