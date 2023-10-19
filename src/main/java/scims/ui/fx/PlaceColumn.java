package scims.ui.fx;

import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableRow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class PlaceColumn extends TreeTableColumn<Object, Object> {
    private Map<CompetitorRow, Double> _totalPointsMap = new HashMap<>();
    private Map<CompetitorRow, SimpleObjectProperty<Object>> _placeMap = new HashMap<>();
    PlaceColumn() {
        super("Place");
        setEditable(true);
        setCellValueFactory(param -> {
            Object value = param.getValue().getValue();
            if (value instanceof CompetitorRow) {
                // Retrieve the score for the corresponding event
                CompetitorRow competitor = (CompetitorRow) value;
                List<EventPointsColumn> eventPointsColumns = getEventPointColumns();
                double totalPoints = 0;
                for(EventPointsColumn pointsColumn : eventPointsColumns) {
                    Object val = competitor.getObservableValue(pointsColumn).getValue();
                    double pointsToAdd = 0;
                    if(val != null && !val.toString().trim().isEmpty()) {
                        pointsToAdd = Double.parseDouble(val.toString());
                    }
                    totalPoints += pointsToAdd;
                }
                _totalPointsMap.put(competitor, totalPoints);
                Integer place = getPlaceOfCompetitor(competitor);
                SimpleObjectProperty<Object> prop = _placeMap.get(competitor);
                if(prop != null) {
                    prop.setValue(place);
                }
                else
                {
                    _placeMap.put(competitor, new SimpleObjectProperty<>(place));
                }
                return _placeMap.get(competitor);
            }
            return new SimpleObjectProperty<>();
        });
        setCellFactory(col -> new DoubleCellEditor(this) {
            @Override
            boolean isCellEditable(TreeTableRow row) {
                return TableCellFactory.cellEditable(row);
            }
        });
    }

    private List<EventPointsColumn> getEventPointColumns() {
        List<EventPointsColumn> pointsColumns = new ArrayList<>();
        ObservableList<TreeTableColumn<Object, ?>> cols = getTreeTableView().getColumns();
        for(TreeTableColumn<Object, ?> col : cols) {
            if(col instanceof EventColumn) {
                pointsColumns.add(((EventColumn<?,?>)col).getPointsColumn());
            }
        }
        return pointsColumns;
    }

    Integer getPlaceOfCompetitor(CompetitorRow competitor) {
        if (_totalPointsMap.containsKey(competitor)) {
            // Create a list of entries (CompetitorRow -> Total Points)
            List<Map.Entry<CompetitorRow, Double>> entries = new ArrayList<>(_totalPointsMap.entrySet());

            // Sort the entries by total points in descending order
            entries.sort((entry1, entry2) -> Double.compare(entry2.getValue(), entry1.getValue()));

            // Initialize variables for handling tied scores
            double previousPoints = Double.POSITIVE_INFINITY;
            int currentPlace = 0;
            int competitorPlace = -1;

            for (Map.Entry<CompetitorRow, Double> entry : entries) {
                double currentPoints = entry.getValue();

                if (currentPoints != previousPoints) {
                    // If the current points are not equal to the previous points, assign a new place
                    currentPlace++;
                }

                if (entry.getKey() == competitor) {
                    competitorPlace = currentPlace;
                    break; // No need to iterate further
                }

                previousPoints = currentPoints;
            }

            return competitorPlace;
        }

        return null; // Competitor not found in the map
    }

    public void updateValues() {
        for(Map.Entry<CompetitorRow, SimpleObjectProperty<Object>> entry : _placeMap.entrySet()) {
            Integer place = getPlaceOfCompetitor(entry.getKey());
            entry.getValue().setValue(place);
        }
    }
}
