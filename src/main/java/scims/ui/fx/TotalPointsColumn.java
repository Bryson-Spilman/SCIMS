package scims.ui.fx;

import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableRow;

import java.util.*;

class TotalPointsColumn extends TreeTableColumn<Object, Object> {

    private boolean _skipUpdate;
    private int _weightClassUpdateTracker = 0;
    TotalPointsColumn() {
        super("Total Points");
        setEditable(true);
        setCellValueFactory(param -> {
            Object value = param.getValue().getValue();
            if (value instanceof CompetitorRow) {
                _weightClassUpdateTracker++;
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
                if(!_skipUpdate && _weightClassUpdateTracker >= competitor.getParentRow().getChildren().size())
                {
                    _skipUpdate = true;
                    Platform.runLater(() ->
                    {
                        ((CompetitionTreeTable)getTreeTableView()).getPlaceColumn().updateValues();
                        _weightClassUpdateTracker = 0;
                        Platform.runLater(() -> _skipUpdate = false);
                    });
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

    boolean getWeightClassFinishedUpdating()
    {
        return _weightClassUpdateTracker == 0;
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
