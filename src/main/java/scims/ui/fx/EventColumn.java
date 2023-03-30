package scims.ui.fx;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.*;
import scims.model.data.Event;

public class EventColumn extends TreeTableColumn<Object, Object> {

    private final Event _event;

    public EventColumn(Event event) {
        super(event.getName());
        setEditable(true);
        _event = event;
        setCellValueFactory(param -> {
            Object value = param.getValue().getValue();
            if (value instanceof CompetitorRow) {
                // Retrieve the score for the corresponding event
                CompetitorRow competitor = (CompetitorRow) value;
                return competitor.getObservableValue(this);
            }
            return new SimpleObjectProperty<>();
        });

        setCellFactory(col -> TableCellFactory.getTreeCell(event.getScoring()));
    }

    public SimpleObjectProperty<Object> getInitialProperty()
    {
        return new SimpleObjectProperty<>("");
    }

    public Event getEvent()
    {
        return _event;
    }
}
