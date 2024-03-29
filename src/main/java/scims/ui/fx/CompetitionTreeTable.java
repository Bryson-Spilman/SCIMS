package scims.ui.fx;

import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import scims.controllers.CompetitionModelController;
import scims.main.SCIMS;
import scims.model.data.*;
import scims.model.data.Event;
import scims.ui.swing.SCIMSFrame;
import scims.ui.swing.tables.Coloring;

import java.awt.*;
import java.awt.event.InputEvent;
import java.util.ArrayList;
import java.util.List;

public class CompetitionTreeTable extends TreeTableView<Object> {

    private Competition _competition;
    TreeItem<Object> _root;
    private CompetitionModelController _controller;
    private final List<EventColumn<?,?>> _eventsColumns = new ArrayList<>();
    private TotalPointsColumn _totalPointsColumn;
    private PlaceColumn _placeColumn;

    public CompetitionTreeTable() {
        setRoot(new TreeItem<>());
        _root = getRoot();
        _root.setExpanded(true);
        setShowRoot(false);
        setEditable(true);
        addListeners();
        setRowFactory(tv -> {
            TreeTableRow<Object> row = new TreeTableRow<Object>()
            {
                @Override
                protected void updateItem(Object item, boolean empty) {
                    super.updateItem(item, empty);
                    if (!empty && item != null) {
                        // Set the background color of the row based on some condition
                        if (item instanceof CompetitorRow) {
                            CompetitorRow cr = (CompetitorRow) item;
                            WeightClassRow wcr = (WeightClassRow) cr.getParentRow().getValue();
                            int wcIndex = _competition.getWeightClasses().indexOf((StrengthWeightClass) wcr.getWeightClass());
                            StrengthWeightClass wc = _competition.getWeightClasses().get(wcIndex);
                            int competitorIndex = wc.getCompetitors().indexOf(cr.getCompetitor());
                            int red = Coloring.FX_COMPETITOR_ROW_COLOR_ODD.getRed();
                            int green = Coloring.FX_COMPETITOR_ROW_COLOR_ODD.getGreen();
                            int blue = Coloring.FX_COMPETITOR_ROW_COLOR_ODD.getBlue();
                            if(competitorIndex % 2 == 0)
                            {
                                red = Coloring.FX_COMPETITOR_ROW_COLOR_EVEN.getRed();
                                green = Coloring.FX_COMPETITOR_ROW_COLOR_EVEN.getGreen();
                                blue = Coloring.FX_COMPETITOR_ROW_COLOR_EVEN.getBlue();
                            }
                            if(isSelected())
                            {
                                setStyle("-fx-background-color: -fx-selection-bar;");
                            } else {
                                setStyle("-fx-background-color: rgb(" + red + "," + green + "," + blue + ");");
                            }
                        } else if (item instanceof WeightClassRow) {
                            int red = Coloring.FX_WEIGHT_CLASS_ROW_COLOR.getRed();
                            int green = Coloring.FX_WEIGHT_CLASS_ROW_COLOR.getGreen();
                            int blue = Coloring.FX_WEIGHT_CLASS_ROW_COLOR.getBlue();
                            if(isSelected())
                            {
                                setStyle("-fx-background-color: -fx-selection-bar;");
                            } else {
                                setStyle("-fx-background-color: rgb(" + red + "," + green + "," + blue + ");");
                            }
                        } else {
                            setStyle(null);
                        }
                    } else {
                        setStyle(null);
                    }
                }
            };
            row.selectedProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue && !row.isEmpty()) {
                    row.setStyle("-fx-background-color: -fx-selection-bar;");
                } else {
                    row.setStyle("");
                }
            });
            return row;
        });
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
        boolean parentModified = SCIMS.getFrame().isModified();
        Platform.runLater(() -> {
            _eventsColumns.clear();
            _competition = competition;
            clear();
            List<StrengthWeightClass> weightClasses = competition.getWeightClasses();
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
            _totalPointsColumn = new TotalPointsColumn();
            getColumns().add(_totalPointsColumn);
            _placeColumn = new PlaceColumn();
            getColumns().add(_placeColumn);
        });
        SCIMS.getFrame().setModified(parentModified);

    }

    public TotalPointsColumn getTotalPointsColumn()
    {
        return _totalPointsColumn;
    }

    public PlaceColumn getPlaceColumn()
    {
        return _placeColumn;
    }

    private void addWeightClassRow(WeightClass weightClass, List<EventColumn<?,?>> eventColumns) {
        TreeItem<Object> weightClassRow = new TreeItem<>(new WeightClassRow(weightClass, _controller));
        for(Competitor competitor : weightClass.getCompetitors())
        {
            weightClassRow.getChildren().add(new TreeItem<>(new CompetitorRow(weightClassRow, competitor, eventColumns, _controller)));
        }
        getRoot().getChildren().add(weightClassRow);
    }

    private void addWeightClassRow(WeightClass weightClass, List<EventColumn<?,?>> eventColumns, int index) {
        TreeItem<Object> weightClassRow = new TreeItem<>(new WeightClassRow(weightClass, _controller));
        for(Competitor competitor : weightClass.getCompetitors())
        {
            weightClassRow.getChildren().add(new TreeItem<>(new CompetitorRow(weightClassRow, competitor, eventColumns, _controller)));
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
                        TreeTablePosition<Object, ?> pos = score.getTreeTablePosition();
                        if(pos != null)
                        {
                            TreeItem<Object> treeItem = getTreeItem(pos.getRow());
                            if(treeItem != null && treeItem.getValue() instanceof CompetitorRow)
                            {
                                CompetitorRow competitorRow = (CompetitorRow) treeItem.getValue();
                                SimpleObjectProperty<Object> obsValue = competitorRow.getObservableValue(score.getTableColumn());
                                obsValue.setValue(score.getNewValue());
                            }
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
                    weightClassRow.getChildren().add(new TreeItem<>(new CompetitorRow(weightClassRow, competitor, _eventsColumns, _controller)));
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
                competitorRows.add(index, new TreeItem<>(new CompetitorRow(competitorRow.getParent(), updatedCompetitor, _eventsColumns, _controller)));
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

    public void save() {
        ObservableList<TreeItem<Object>> weightClasses = getRoot().getChildren();
        ObservableList<TreeTableColumn<Object, ?>> columns = getColumns();
        for(TreeItem<Object> wcRow : weightClasses)
        {
            for(TreeItem<Object> competitorRowWrapper : wcRow.getChildren())
            {
                CompetitorRow competitorRow = (CompetitorRow) competitorRowWrapper.getValue();
                for(TreeTableColumn<Object, ?> col : columns)
                {
                    if(col instanceof EventColumn)
                    {
                        Event event = ((EventColumn<?,?>) col).getEvent();
                        ObservableList<TreeTableColumn<Object, ?>> scoreCols = col.getColumns();
                        for(TreeTableColumn<Object, ? > scoreCol : scoreCols)
                        {
                            if(scoreCol instanceof ScoringColumn)
                            {
                                SimpleObjectProperty<Object> value = competitorRow.getObservableValue(scoreCol);
                                competitorRow.getCompetitor().setEventScore(event, ((ScoringColumn<?,?>)scoreCol).getScoring(), value.getValue());
                            }

                        }
                    }
                }
            }
        }
    }

    public void fireUpdated() {
        _controller.fireUpdated();
    }

    public void commitEdit() {
        ObservableList<TreeTableColumn<Object, ?>> columns = getColumns();
        for (TreeTableColumn<Object, ?> column : columns) {
            TreeTableColumn<Object, Object> col = (TreeTableColumn<Object, Object>) column;
            for (TreeItem<Object> item : getRoot().getChildren()) {
                TreeTableCell<Object, Object> cell = col.getCellFactory().call(col);
                int row = getRow(item);
                cell.updateIndex(row);
                cell.commitEdit(cell.getItem());
            }
        }

    }






}
