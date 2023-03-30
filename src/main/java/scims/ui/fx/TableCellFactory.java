package scims.ui.fx;

import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeTableCell;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.cell.TextFieldTreeTableCell;
import javafx.util.Callback;
import javafx.util.converter.IntegerStringConverter;
import scims.model.data.scoring.EventScoring;
import scims.model.data.scoring.RepsScoring;

class TableCellFactory {

    private TableCellFactory() {
        throw new AssertionError("Factory class");
    }

    static TreeTableCell<Object, Object> getTreeCell(EventScoring scoring) {
        TreeTableCell<Object, Object> retVal = new TextCellEditor();
        if(scoring instanceof RepsScoring) {
            retVal = new IntegerCellEditor(new IntegerStringConverter());
        }
        return retVal;
    }
}
