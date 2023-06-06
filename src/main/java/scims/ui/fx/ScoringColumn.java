package scims.ui.fx;

import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;
import scims.model.data.Competitor;
import scims.model.data.scoring.EventScoring;

import java.util.*;

class ScoringColumn<T extends EventScoring<S>, S> extends LinkedTreeTableColumn {
    private final T _scoring;
    private final EventColumn<?, ?> _parent;

    public ScoringColumn(EventColumn<?,?> parent, T scoring) {
        super(scoring.toString());
        _parent = parent;
        setEditable(true);
        _scoring = scoring;
        setCellValueFactory(param -> {
            Object value = param.getValue().getValue();
            if (value instanceof CompetitorRow) {
                // Retrieve the score for the corresponding event
                CompetitorRow competitor = (CompetitorRow) value;
                return competitor.getObservableValue(this);
            }
            return new SimpleObjectProperty<>();
        });
        setCellFactory(col -> TableCellFactory.getTreeCell(col, scoring));
    }

    public SimpleObjectProperty<Object> getInitialProperty()
    {
        return new SimpleObjectProperty<>("");
    }

    public T getScoring()
    {
        return _scoring;
    }

    @SuppressWarnings("unchecked")
    public Map<Competitor, Double> sortCompetitorsByScore(ObservableList<TreeItem<Competitor>> competitorRows) {
        Map<Competitor, S> sortedCompetitors = new LinkedHashMap<>();
        for(TreeItem<Competitor> competitorTreeItem : competitorRows) {
            if(competitorTreeItem instanceof CompetitorRow) {
                CompetitorRow competitorRow = (CompetitorRow) competitorTreeItem;
                S changedVal = (S) competitorRow.getObservableValue(this).getValue();
                sortedCompetitors.put(competitorRow.getCompetitor(),changedVal);
            }
        }
        return _scoring.sortCompetitorScores(sortedCompetitors);
    }

    @Override
    void updateLinkedCells(TreeItem<Object> rowObject) {
        _parent.updateLinkedCells(rowObject);
    }
}
