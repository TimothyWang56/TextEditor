package textEditor;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.LinkedList;
import java.util.regex.*;

public class SearchActionListener implements ActionListener {
    private TextEditor editor;
    boolean forwards;

    public SearchActionListener(TextEditor editor, boolean forwards) {
        this.editor = editor;
        this.forwards = forwards;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!this.editor.alreadySearching) {
            firstSearch();
        } else {
            int increment;
            int boundary;
            int firstIndex;
            if (forwards) {
                increment = 1;
                boundary = this.editor.indexes.length - 1;
                firstIndex = 0;
            } else {
                increment = -1;
                boundary = 0;
                firstIndex = this.editor.indexes.length - 1;
            }
            next(increment, boundary, firstIndex);
        }
    }

    private String makeRegexFromString(String textInput) {
        //TODO: Double check that these are the right characters to replace
        String[] specialCharacters = new String[]{
                "\\", "^", "$", ".", "|", "?", "*", "+", "(", ")", "[", "{"};
        for (String character: specialCharacters) {
            textInput = textInput.replace(character, "\\" + character);
        }
        return textInput;
    }

    private void firstSearch() {
        String textInput = this.editor.searchField.getText();
        String text = this.editor.textArea.getText();
        if (textInput != null && textInput != "") {
            if (!this.editor.regexCheckBox.isSelected()) {
                textInput = makeRegexFromString(textInput);
            }
            Pattern regex;
            if (this.editor.caseSensitiveBox.isSelected()) {
                regex = Pattern.compile(textInput);
            } else {
                regex = Pattern.compile(textInput, Pattern.CASE_INSENSITIVE);
            }
            Matcher matcher = regex.matcher(text);
            LinkedList<Integer[]> indexesList = new LinkedList<>();
            while (matcher.find()) {
                indexesList.add(new Integer[]{matcher.start(), matcher.group().length()});
            }
            Integer[][] indexesArray = indexesList.toArray(new Integer[indexesList.size()][]);
            this.editor.indexes = indexesArray;
            if (indexesArray.length > 0) {
                this.editor.currentIndex = 0;
                this.editor.textArea.setCaretPosition(indexesArray[0][0]);
                this.editor.textArea.select(indexesArray[0][0],
                        indexesArray [0][0] + indexesArray[0][1]);
                this.editor.textArea.grabFocus();
                this.editor.alreadySearching = true;
            }
        }
    }

    private void next(int increment, int boundary, int firstIndex) {
        if (this.editor.currentIndex == boundary) {
            this.editor.currentIndex = firstIndex;
        } else {
            this.editor.currentIndex += increment;
        }
        int index = this.editor.currentIndex;
        this.editor.textArea.setCaretPosition(this.editor.indexes[index][0]);
        this.editor.textArea.select(this.editor.indexes[index][0],
                this.editor.indexes[index][0] + this.editor.indexes[index][1]);
        this.editor.textArea.grabFocus();
    }
}
