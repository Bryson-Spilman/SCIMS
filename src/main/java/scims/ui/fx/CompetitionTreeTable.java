package scims.ui.fx;

import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import scims.controllers.CompetitionModelController;
import scims.model.data.*;
import scims.model.data.Event;

import java.awt.*;
import java.awt.event.InputEvent;
import java.util.ArrayList;
import java.util.List;

public class CompetitionTreeTable extends TreeTableView<Object> {

    private Competition _competition;
    TreeItem<Object> _root;
    private CompetitionModelController _controller;
    private final List<EventColumn<?,?>> _eventsColumns = new ArrayList<>();
    public CompetitionTreeTable() {
        setRoot(new TreeItem<>());
        _root = getRoot();
        _root.setExpanded(true);
        setShowRoot(false);
        setEditable(true);
        addListeners();
    }

    private void addListeners() {
        getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            TreeTableColumn<Object, ?> tableColumn = getFocusModel().getFocusedCell().getTableColumn();
            if(!(tableColumn instanceof EventColumn)) {
                try {
                    Robot bot = new Robot();
                    bot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
                    bot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
                } catch (AWTException e) {
                    throw new RuntimeException(e);
                }
            }
            if (newValue != null) {
                Object row = newValue.getValue();
                if(row instanceof ContextMenuRow) {
                    ContextMenu tableViewContextMenu = ((ContextMenuRow) row).getContextMenu();
                    setContextMenu(tableViewContextMenu);
                    tableViewContextMenu.requestFocus();
                }
            }
        });
    }

    public void refresh(Competition competition) {
        Platform.runLater(() -> {
            _eventsColumns.clear();
            _competition = competition;
            clear();
            List<WeightClass> weightClasses = competition.getWeightClasses();
            //add columns
            CompetitorsColumn competitorColumns = new CompetitorsColumn();
            getColumns().add(competitorColumns);
            competitorColumns.setEditable(false);
            for(WeightClass weightClass : weightClasses)
            {
                _eventsColumns.addAll(addNewEventsFromWeightClass(weightClass));
            }
            for(WeightClass weightClass : weightClasses)
            {
                addWeightClassRow(weightClass, _eventsColumns);
            }
        });
    }

    private void addWeightClassRow(WeightClass weightClass, List<EventColumn<?,?>> eventColumns) {
        TreeItem<Object> weightClassRow = new TreeItem<>(new WeightClassRow(weightClass, _controller));
        for(Competitor competitor : weightClass.getCompetitors())
        {
            weightClassRow.getChildren().add(new TreeItem<>(new CompetitorRow(competitor, eventColumns, _controller)));
        }
        getRoot().getChildren().add(weightClassRow);
    }

    private void addWeightClassRow(WeightClass weightClass, List<EventColumn<?,?>> eventColumns, int index) {
        TreeItem<Object> weightClassRow = new TreeItem<>(new WeightClassRow(weightClass, _controller));
        for(Competitor competitor : weightClass.getCompetitors())
        {
            weightClassRow.getChildren().add(new TreeItem<>(new CompetitorRow(competitor, eventColumns, _controller)));
        }
        getRoot().getChildren().add(index, weightClassRow);
    }

    private List<EventColumn<?,?>> addNewEventsFromWeightClass(WeightClass weightClass) {
        List<EventColumn<?,?>> eventColumns = new ArrayList<>();
        for(Event event : weightClass.getEventsInOrder())
        {
            if(_eventsColumns.stream().noneMatch(ec -> ec.getEvent().getName().equalsIgnoreCase(event.getName())))
            {
                EventColumn<?,?> eventColumn = new EventColumn<>(event);
                eventColumns.add(eventColumn);
                getColumns().add(eventColumn);
                ObservableList<TreeTableColumn<Object, ?>> scoreColumns = eventColumn.getColumns();
                for(TreeTableColumn<Object, ?> scoreColumn : scoreColumns) {
                    scoreColumn.setOnEditCommit(score -> {
                        TreeItem<Object> treeItem = getTreeItem(score.getTreeTablePosition().getRow());
                        if(treeItem.getValue() instanceof CompetitorRow)
                        {
                            CompetitorRow competitorRow = (CompetitorRow) treeItem.getValue();
                            SimpleObjectProperty<Object> obsValue = competitorRow.getObservableValue((ScoringColumn<?,?>) score.getTableColumn());
                            obsValue.setValue(score.getNewValue());
                        }
                    });
                }
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
                List<EventColumn<?,?>> newEventColumns = addNewEventsFromWeightClass(wc);
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
                    weightClassRow.getChildren().add(new TreeItem<>(new CompetitorRow(competitor, _eventsColumns, _controller)));
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
                competitorRows.add(index, new TreeItem<>(new CompetitorRow(updatedCompetitor, _eventsColumns, _controller)));
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

    public void removeCompetition(Competition competition) {
        Platform.runLater(() -> {
            if(isShowingCompetition(competition)) {
                clear();
            }
        });
    }

    public void removeWeightClass(WeightClass weightClass) {
        Platform.runLater(() -> {
            TreeItem<Object> weightClassRow = getWeightClassRow(weightClass);
            _root.getChildren().remove(weightClassRow);
        });
    }

    public void removeCompetitor(WeightClass weightClass, Competitor competitor) {
        Platform.runLater(() -> {
            TreeItem<Object> competitorRow = getCompetitorRow(competitor, weightClass);
            ObservableList<TreeItem<Object>> competitorRows = competitorRow.getParent().getChildren();
            competitorRows.remove(competitorRow);
        });
    }
}
