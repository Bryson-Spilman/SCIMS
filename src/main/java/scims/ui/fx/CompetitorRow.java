package scims.ui.fx;

import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TreeItem;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import scims.controllers.CompetitionModelController;
import scims.model.data.Competitor;
import scims.ui.actions.EditCompetitorAction;

import javax.swing.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CompetitorRow extends TreeItem<Competitor> implements ContextMenuRow {

    private final Competitor _competitor;
    private final Map<EventColumn, ObservableValue<String>> _columnToValueMap = new HashMap<>();
    private final SimpleStringProperty _nameProperty;
    private final CompetitionModelController _controller;

    public CompetitorRow(Competitor competitor, List<EventColumn> eventColumns, CompetitionModelController controller) {
        _competitor = competitor;
        _controller = controller;
        _nameProperty = new SimpleStringProperty(competitor.getName());
        setValue(competitor);
        initializeValues(eventColumns);
    }

    private void initializeValues(List<EventColumn> eventColumns)
    {
        for(EventColumn eventColumn : eventColumns)
        {
            _columnToValueMap.put(eventColumn, eventColumn.getInitialProperty());
        }
    }

    public Competitor getCompetitor() {
        return _competitor;
    }

    public ObservableValue<String> getObservableValue(EventColumn eventColumn) {
        ObservableValue<String> retVal = _columnToValueMap.get(eventColumn);
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
        ContextMenu retVal = new ContextMenu(menuItem1, menuItem2);
        return retVal;
    }
}
