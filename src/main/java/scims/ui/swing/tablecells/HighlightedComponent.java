package scims.ui.swing.tablecells;

import java.awt.*;
import java.util.function.Function;

interface HighlightedComponent {
    void setHighlightCondition(Function<Integer, Boolean> highlightRow);

    void setHighlightColor(Color highlightColor);
}
