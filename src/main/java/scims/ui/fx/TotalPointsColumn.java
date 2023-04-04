package scims.ui.fx;

import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableRow;

import java.util.ArrayList;
import java.util.List;

class TotalPointsColumn extends TreeTableColumn<Object, Object> {
    TotalPointsColumn() {
        super("Total Points");
        setEditable(true);
        setCellValueFactory(param -> {
            Object value = param.getValue().getValue();
            if (value instanceof CompetitorRow) {
                // Retrieve the score for the corresponding event
                CompetitorRow competitor = (CompetitorRow) value;
                List<EventPointsColumn> eventPointsColumns = getEventPointColumns();
                double totalPoints = 0;
                for(EventPointsColumn pointsColumn : eventPointsColumns) {
                    Object val = competitor.getObservableValue(pointsColumn).getValue();
                    double pointsToAdd = 0;
                    if(val != null && !val.toString().trim().isEmpty()) {
                        pointsToAdd = Double.parseDouble(val.toString());
                    }
                    totalPoints += pointsToAdd;
                }
                return new SimpleObjectProperty<>(totalPoints);
            }
            return new SimpleObjectProperty<>();
        });
        setCellFactory(col -> new DoubleCellEditor(this) {
            @Override
            boolean isCellEditable(TreeTableRow row) {
                return TableCellFactory.cellEditable(row);
            }
        });
    }

    private List<EventPointsColumn> getEventPointColumns() {
        List<EventPointsColumn> pointsColumns = new ArrayList<>();
        ObservableList<TreeTableColumn<Object, ?>> cols = getTreeTableView().getColumns();
        for(TreeTableColumn<Object, ?> col : cols) {
            if(col instanceof EventColumn) {
                pointsColumns.add(((EventColumn<?,?>)col).getPointsColumn());
            }
        }
        return pointsColumns;
    }
}
