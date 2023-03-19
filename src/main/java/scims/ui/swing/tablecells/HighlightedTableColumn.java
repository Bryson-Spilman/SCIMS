package scims.ui.swing.tablecells;

import javax.swing.table.TableColumn;

public abstract class HighlightedTableColumn extends TableColumn implements HighlightedComponent {

    public HighlightedTableColumn(int modelIndex) {
        super(modelIndex);
    }
}
