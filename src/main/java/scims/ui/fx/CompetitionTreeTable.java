package scims.ui.fx;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableView;
import scims.model.data.*;

import java.util.ArrayList;
import java.util.List;

public class CompetitionTreeTable extends TreeTableView<Object> {

    private Competition _competition;
    TreeItem<Object> _root;

    public CompetitionTreeTable() {
        setRoot(new TreeItem<>());
        _root = getRoot();
        _root.setExpanded(true);
        setShowRoot(false);
        setEditable(true);
    }

    public void refresh(Competition competition) {
        _competition = competition;
        clear();
        List<WeightClass> weightClasses = competition.getWeightClasses();
        //add columns
        getColumns().add(new CompetitorsColumn());
        List<EventColumn> eventColumns = new ArrayList<>();
        for(WeightClass weightClass : weightClasses)
        {
            for(Event event : weightClass.getEventsInOrder())
            {
                if(eventColumns.stream().noneMatch(ec -> ec.getEvent().getName().equalsIgnoreCase(event.getName())))
                {
                    EventColumn eventColumn = new EventColumn(event);
                    eventColumns.add(eventColumn);
                    getColumns().add(eventColumn);
                    eventColumn.setOnEditCommit(event1 -> {
                        TreeItem<Object> treeItem = getTreeItem(event1.getTreeTablePosition().getRow());
                        if(treeItem.getValue() instanceof CompetitorRow)
                        {
                            CompetitorRow competitorRow = (CompetitorRow) treeItem.getValue();
                            SimpleStringProperty obsValue = (SimpleStringProperty) competitorRow.getObservableValue((EventColumn) event1.getTableColumn());
                            obsValue.setValue(event1.getNewValue());
                        }
                    });
                }
            }
        }
        for(WeightClass weightClass : weightClasses)
        {
            TreeItem<Object> weightClassRow = new TreeItem<>(new WeightClassRow(weightClass));
            for(Competitor competitor : weightClass.getCompetitors())
            {
                weightClassRow.getChildren().add(new TreeItem<>(new CompetitorRow(competitor, eventColumns)));
            }
            getRoot().getChildren().add(weightClassRow);
        }

    }

    public boolean isShowingCompetition(Competition competition) {
        return _competition == competition;
    }

    public void clear() {
        if(_root != null)
        {
            _root.getChildren().clear();
        }
        getColumns().clear();
    }
}
