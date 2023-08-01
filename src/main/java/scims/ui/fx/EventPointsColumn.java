package scims.ui.fx;

import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableRow;
import scims.model.data.scoring.EventScoring;

import java.util.List;

class EventPointsColumn extends TreeTableColumn<Object, Object> {
    private final List<ScoringColumn<?,?>> _scoringColumns;

    public EventPointsColumn(List<ScoringColumn<?,?>> scoringColumns) {
        super("Points");
        _scoringColumns = scoringColumns;
        setEditable(true);
        setCellValueFactory(param -> {
            Object value = param.getValue().getValue();
            if (value instanceof CompetitorRow) {
                // Retrieve the score for the corresponding event
                CompetitorRow competitor = (CompetitorRow) value;
                return competitor.getObservableValue(this);
            }
            return new SimpleObjectProperty<>();
        });
        setCellFactory(col -> new DoubleCellEditor(this) {
            @Override
            boolean isCellEditable(TreeTableRow row) {
                return false;
            }
        });
    }

    public SimpleObjectProperty<Object> getInitialProperty()
    {
        return new SimpleObjectProperty<>("");
    }

}
