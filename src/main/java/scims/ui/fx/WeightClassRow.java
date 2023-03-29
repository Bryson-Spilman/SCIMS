package scims.ui.fx;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TreeItem;
import javafx.scene.input.MouseEvent;
import scims.controllers.CompetitionModelController;
import scims.model.data.WeightClass;

public class WeightClassRow extends TreeItem<WeightClass> implements ContextMenuRow {
    private final WeightClass _weightClass;
    private final SimpleStringProperty _nameProperty;
    private final ObservableList<CompetitorRow> _childRows = FXCollections.observableArrayList();
    private final CompetitionModelController _controller;

    public WeightClassRow(WeightClass weightClass, CompetitionModelController controller) {
        _weightClass = weightClass;
        _controller = controller;
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

    @Override
    public ContextMenu getContextMenu() {
        MenuItem addCompetitorItem = new MenuItem("Add New Competitor...");
        MenuItem menuItem1 = new MenuItem("Edit Weight Class...");
        MenuItem menuItem2 = new MenuItem("Remove Weight Class");
        addCompetitorItem.setOnAction(e -> {
            _controller.addNewCompetitorAction(_weightClass);
        });
        menuItem1.setOnAction(e -> {
            _controller.editWeightClassAction(_weightClass);
        });
        menuItem2.setOnAction(e -> {
            _controller.removeWeightClass(_weightClass);
        });
        return new ContextMenu(addCompetitorItem, new SeparatorMenuItem(), menuItem1, menuItem2);
    }
}




