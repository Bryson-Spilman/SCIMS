package scims.ui.fx;

import javafx.scene.control.*;
import scims.model.data.scoring.*;

class TableCellFactory {

    private TableCellFactory() {
        throw new AssertionError("Factory class");
    }

    static TreeTableCell<Object, Object> getTreeCell(TreeTableColumn<Object, Object> col, EventScoring<?> scoring) {
        TreeTableCell<Object, Object> retVal = new TextCellEditor<>(col);
        if(scoring instanceof RepsScoring || scoring instanceof LastManStandingWithPointsScoring) {
            retVal = new IntegerCellEditor(col) {
                @Override
                boolean isCellEditable(TreeTableRow row) {
                    return cellEditable(row);
                }
            };
        } else if(scoring instanceof DistanceScoring || scoring instanceof TimeScoring || scoring instanceof WeightScoring) {
            retVal = new DoubleCellEditor(col) {
                @Override
                boolean isCellEditable(TreeTableRow row) {
                    return cellEditable(row);
                }
            };
        }
        return retVal;
    }

    static boolean cellEditable(TreeTableRow row) {
        TreeItem<?> item = row.getTreeItem();
        boolean retVal = false;
        if(item != null) {
            Object val = item.getValue();
            retVal = val instanceof CompetitorRow;
        }
        return retVal;
    }
}
