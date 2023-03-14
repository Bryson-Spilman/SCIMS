package scims.ui.fx;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;
import scims.model.data.Competitor;
import scims.model.data.WeightClass;

public class WeightClassRow extends TreeItem<WeightClass> {
    private final WeightClass _weightClass;
    private final SimpleStringProperty _nameProperty;
    private final ObservableList<CompetitorRow> _childRows = FXCollections.observableArrayList();

    public WeightClassRow(WeightClass weightClass) {
        _weightClass = weightClass;
        setExpanded(true);
        setValue(weightClass);
        _nameProperty = new SimpleStringProperty(_weightClass.getName());
    }

    public WeightClass getWeightClass() {
        return _weightClass;
    }

    public ObservableValue<String> getNameProperty() {
        return _nameProperty;
    }

    public void addCompetitorRow(CompetitorRow competitorRow)
    {
        _childRows.add(competitorRow);
    }

    public void removeCompetitorRow(CompetitorRow competitorRow)
    {
        _childRows.remove(competitorRow);
    }

    public ObservableList<CompetitorRow> getChildRows() {
        return _childRows;
    }
}




