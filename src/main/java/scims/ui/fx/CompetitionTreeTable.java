package scims.ui.fx;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableView;
import scims.controllers.CompetitionModelController;
import scims.model.data.*;

import java.util.ArrayList;
import java.util.List;

public class CompetitionTreeTable extends TreeTableView<Object> {

    private Competition _competition;
    TreeItem<Object> _root;
    private CompetitionModelController _controller;
    private final List<EventColumn> _eventsColumns = new ArrayList<>();

    public CompetitionTreeTable() {
        setRoot(new TreeItem<>());
        _root = getRoot();
        _root.setExpanded(true);
        setShowRoot(false);
        setEditable(true);
    }

    public void refresh(Competition competition) {
        Platform.runLater(() -> {
            _eventsColumns.clear();
            _competition = competition;
            clear();
            List<WeightClass> weightClasses = competition.getWeightClasses();
            //add columns
            getColumns().add(new CompetitorsColumn());
            List<EventColumn> eventColumns = new ArrayList<>();
            for(WeightClass weightClass : weightClasses)
            {
                eventColumns.addAll(addNewEventsFromWeightClass(weightClass));
            }
            for(WeightClass weightClass : weightClasses)
            {
                addWeightClassRow(weightClass, eventColumns);
            }
            _eventsColumns.addAll(eventColumns);
        });
    }

    private void addWeightClassRow(WeightClass weightClass, List<EventColumn> eventColumns) {
        TreeItem<Object> weightClassRow = new TreeItem<>(new WeightClassRow(weightClass));
        for(Competitor competitor : weightClass.getCompetitors())
        {
            weightClassRow.getChildren().add(new TreeItem<>(new CompetitorRow(competitor, eventColumns)));
        }
        getRoot().getChildren().add(weightClassRow);
    }

    private void addWeightClassRow(WeightClass weightClass, List<EventColumn> eventColumns, int index) {
        TreeItem<Object> weightClassRow = new TreeItem<>(new WeightClassRow(weightClass));
        for(Competitor competitor : weightClass.getCompetitors())
        {
            weightClassRow.getChildren().add(new TreeItem<>(new CompetitorRow(competitor, eventColumns)));
        }
        getRoot().getChildren().add(index, weightClassRow);
    }

    private List<EventColumn> addNewEventsFromWeightClass(WeightClass weightClass) {
        List<EventColumn> eventColumns = new ArrayList<>();
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
        return eventColumns;
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

    public void setController(CompetitionModelController controller) {
        _controller = controller;
    }

    public void addNewWeightClass(Competition competition, WeightClass wc) {
        if(competition.equals(_competition)) {
            Platform.runLater(() -> {
                List<EventColumn> newEventColumns = addNewEventsFromWeightClass(wc);
                _eventsColumns.addAll(newEventColumns);
                addWeightClassRow(wc, newEventColumns);
            });
        }
    }

    public void addNewCompetitor(Competition competition, WeightClass weightClass, Competitor competitor) {
        if(competition.equals(_competition)) {
            Platform.runLater(() -> {
                TreeItem<Object> weightClassRow = getWeightClassRow(weightClass);
                if(weightClassRow != null) {
                    weightClassRow.getChildren().add(new TreeItem<>(new CompetitorRow(competitor, _eventsColumns)));
                }
            });
        }
    }

    private TreeItem<Object> getWeightClassRow(WeightClass weightClass) {
        TreeItem<Object> retVal = null;
        for(int i=0; i < _root.getChildren().size(); i++) {
            TreeItem<Object> child = _root.getChildren().get(i);
            Object value = child.getValue();
            if(value instanceof WeightClassRow) {
                WeightClass weightClassFromRow = ((WeightClassRow) value).getWeightClass();
                if(weightClassFromRow.equals(weightClass)) {
                    retVal = child;
                    break;
                }
            }
        }
        return retVal;
    }

    public void updateWeightClass(WeightClass oldWeightClass, WeightClass weightClass, Competition competition) {
        if(_competition.equals(competition)) {
            Platform.runLater(() -> {
                TreeItem<Object> weightClassRow = getWeightClassRow(oldWeightClass);
                int index = _root.getChildren().indexOf(weightClassRow);
                _eventsColumns.addAll(addNewEventsFromWeightClass(weightClass));
                addWeightClassRow(weightClass, _eventsColumns, index);
                _root.getChildren().remove(weightClassRow);
            });
        }
    }

    public void updateCompetitor(Competitor oldCompetitor, Competitor updatedCompetitor, Competition competition, WeightClass weightClass) {
        if(_competition.equals(competition)) {
            Platform.runLater(() -> {
                TreeItem<Object> competitorRow = getCompetitorRow(oldCompetitor, weightClass);
                ObservableList<TreeItem<Object>> competitorRows = competitorRow.getParent().getChildren();
                int index = competitorRows.indexOf(competitorRow);
                competitorRows.add(index, new TreeItem<>(new CompetitorRow(updatedCompetitor, _eventsColumns)));
                competitorRows.remove(competitorRow);
            });
        }
    }

    private TreeItem<Object> getCompetitorRow(Competitor competitor, WeightClass weightClass) {
        TreeItem<Object> retVal = null;
        TreeItem<Object> weightClassRow = getWeightClassRow(weightClass);
        ObservableList<TreeItem<Object>> children = weightClassRow.getChildren();
        for(TreeItem<Object> child : children) {
            if(child.getValue() instanceof CompetitorRow) {
                Competitor competitorFromRow = ((CompetitorRow) child.getValue()).getCompetitor();
                if(competitorFromRow.equals(competitor)) {
                    retVal = child;
                }
            }
        }
        return retVal;
    }
}
