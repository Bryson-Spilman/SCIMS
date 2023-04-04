package scims.ui.swing.tablecells;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.util.Arrays;
import java.util.function.Function;

interface HighlightedComponent {
    void setHighlightCondition(Function<Integer, Boolean> highlightRow);

    void setHighlightColor(Color highlightColor);

    default void applySelectedHighlighting(JComponent c, JTable table, int col, int row)
    {
        if (Arrays.stream(table.getSelectedRows()).anyMatch(r -> r == row)) {
            int leftMatteBorder = 0;
            int rightMatteBorder = 0;
            if(col == 0)
            {
                leftMatteBorder = 1;
            }
            else if(col == table.getColumnCount() -1)
            {
                rightMatteBorder = 1;
            }
            Border border = BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(1, leftMatteBorder, 1, rightMatteBorder, Color.BLACK),
                    BorderFactory.createEmptyBorder(0, 5, 0, 5));
            c.setBorder(border);
        } else {
            c.setBorder(null);
        }
    }
}
