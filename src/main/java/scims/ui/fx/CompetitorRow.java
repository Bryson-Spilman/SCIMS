package scims.ui.fx;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TreeItem;
import scims.model.data.Competitor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CompetitorRow extends TreeItem<Competitor> {

    private final Competitor _competitor;
    private final Map<EventColumn, ObservableValue<String>> _columnToValueMap = new HashMap<>();
    private final SimpleStringProperty _nameProperty;

    public CompetitorRow(Competitor competitor, List<EventColumn> eventColumns) {
        _competitor = competitor;
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
}
