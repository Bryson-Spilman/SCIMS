package scims.ui.swing.tablecells;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

public class IntegerDocumentFilter extends DocumentFilter{
    @Override
    public void insertString(DocumentFilter.FilterBypass fb, int offset, String text, AttributeSet attrs) throws BadLocationException {
        StringBuilder sb = new StringBuilder();
        sb.append(fb.getDocument().getText(0, fb.getDocument().getLength()));
        sb.insert(offset, text);

        if (isValid(sb.toString())) { // check if the input is valid
            super.insertString(fb, offset, text, attrs);
        } else {
            // handle invalid input if wanted
        }
    }

    @Override
    public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
        StringBuilder sb = new StringBuilder();
        sb.append(fb.getDocument().getText(0, fb.getDocument().getLength()));
        sb.replace(offset, offset + length, text);

        if (isValid(sb.toString())) { // check if the input is valid
            super.replace(fb, offset, length, text, attrs);
        } else {
            // handle invalid input if wanted
        }
    }

    private boolean isValid(String input) {
        try {
            int i = Integer.parseInt(input);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
