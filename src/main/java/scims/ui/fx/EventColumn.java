package scims.ui.fx;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTreeTableCell;
import javafx.util.converter.IntegerStringConverter;
import scims.model.data.Event;

public class EventColumn extends TreeTableColumn<Object, String> {

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
            return new SimpleStringProperty("");
        });

        setCellFactory(TextFieldTreeTableCell.forTreeTableColumn());
    }

    public ObservableValue<String> getInitialProperty()
    {
        return new SimpleStringProperty("");
    }

    public Event getEvent()
    {
        return _event;
    }
}
