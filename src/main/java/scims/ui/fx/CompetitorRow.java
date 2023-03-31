package scims.ui.fx;

import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import scims.controllers.CompetitionModelController;
import scims.model.data.Competitor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CompetitorRow extends TreeItem<Competitor> implements ContextMenuRow {

    private final Competitor _competitor;
    private final Map<TreeTableColumn<?,?>, SimpleObjectProperty<Object>> _columnToValueMap = new HashMap<>();
    private final SimpleStringProperty _nameProperty;
    private final CompetitionModelController _controller;

    public CompetitorRow(Competitor competitor, List<EventColumn<?,?>> eventColumns, CompetitionModelController controller) {
        _competitor = competitor;
        _controller = controller;
        _nameProperty = new SimpleStringProperty(competitor.getName());
        setValue(competitor);
        initializeValues(eventColumns);
    }

    private void initializeValues(List<EventColumn<?,?>> eventColumns)
    {
        for(EventColumn<?,?> eventColumn : eventColumns) {
            ObservableList<TreeTableColumn<Object, ?>> columns = eventColumn.getColumns();
            for(TreeTableColumn<Object,?> column : columns) {
                if(column instanceof ScoringColumn) {
                    ScoringColumn<?,?> scoringColumn = (ScoringColumn<?,?>) column;
                    _columnToValueMap.put(scoringColumn, scoringColumn.getInitialProperty());
                } else if(column instanceof EventPointsColumn) {
                    EventPointsColumn pointsColumn = (EventPointsColumn) column;
                    _columnToValueMap.put(pointsColumn, pointsColumn.getInitialProperty());
                }
            }
        }
    }

    public Competitor getCompetitor() {
        return _competitor;
    }

    public SimpleObjectProperty<Object> getObservableValue(TreeTableColumn<?,?> scoringColumn) {
        SimpleObjectProperty<Object> retVal = _columnToValueMap.get(scoringColumn);
        if(retVal == null)
        {
            retVal = new SimpleObjectProperty<>();
        }
        return retVal;
    }

    public ObservableValue<String> getNameProperty() {
        return _nameProperty;
    }

    @Override
    public ContextMenu getContextMenu() {
        MenuItem menuItem1 = new MenuItem("Edit Competitor...");
        MenuItem menuItem2 = new MenuItem("Remove Competitor");
        menuItem1.setOnAction(e -> {
            Platform.runLater(() -> menuItem1.getParentPopup().hide());
            _controller.editCompetitorAction(_competitor);
        });
        menuItem2.setOnAction(e -> {
            Platform.runLater(() -> menuItem2.getParentPopup().hide());
            _controller.removeCompetitor(_competitor);
        });
        return new ContextMenu(menuItem1, menuItem2);
    }
}
