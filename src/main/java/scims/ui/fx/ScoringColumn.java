package scims.ui.fx;

import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import scims.model.data.Competitor;
import scims.model.data.Event;
import scims.model.data.scoring.CustomScore;
import scims.model.data.scoring.EventScoring;

import java.util.*;

class ScoringColumn<T extends EventScoring<S>, S> extends TreeTableColumn<Object, Object> {
    private final T _scoring;

    public ScoringColumn(EventColumn<?,?> parent, T scoring) {
        super(scoring.toString());
        setEditable(true);
        _scoring = scoring;
        setCellValueFactory(param -> {
            Object value = param.getValue().getValue();
            if (value instanceof CompetitorRow) {
                // Retrieve the score for the corresponding event
                CompetitorRow competitor = (CompetitorRow) value;
                parent.updateEventPoints(competitor);
                return competitor.getObservableValue(this);
            }
            return new SimpleObjectProperty<>();
        });
        setCellFactory(col -> TableCellFactory.getTreeCell(scoring));
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
    public List<Competitor> sortCompetitorsByScore(ObservableList<TreeItem<Competitor>> competitorRows) {
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
}
