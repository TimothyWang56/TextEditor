package textEditor;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class TextListener implements DocumentListener {
    TextEditor editor;

    public TextListener(TextEditor editor) {
        this.editor = editor;
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        this.editor.resetRegex();
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        this.editor.resetRegex();
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        this.editor.resetRegex();
    }
}
