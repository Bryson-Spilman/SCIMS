package scims.ui.fx;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.cell.TextFieldTreeTableCell;

public class CompetitorsColumn extends TreeTableColumn<Object, String> {

    public CompetitorsColumn() {
        super("Competitors");
        setCellFactory(TextFieldTreeTableCell.forTreeTableColumn());

        setCellValueFactory(param -> {
            Object value = param.getValue().getValue();
            if (value instanceof WeightClassRow) {
                WeightClassRow weightClass = (WeightClassRow) value;
                return weightClass.getNameProperty();
            } else if (value instanceof CompetitorRow) {
                CompetitorRow competitor = (CompetitorRow) value;
                return competitor.getNameProperty();
            }
            return new SimpleStringProperty("");
        });
    }
}
