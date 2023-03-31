package scims.ui.fx;

import javafx.collections.ObservableList;
import javafx.scene.control.*;
import scims.model.data.Competitor;
import scims.model.data.Event;
import scims.model.data.scoring.CustomEventScoring;
import scims.model.data.scoring.CustomScore;
import scims.model.data.scoring.EventScoring;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public class EventColumn<T extends EventScoring<S>, S> extends TreeTableColumn<Object, Object> {

    private final Event _event;
    private final List<ScoringColumn<?,?>> _scoringColumns = new ArrayList<>();
    private final EventPointsColumn _pointsColumns;

    public EventColumn(Event event) {
        super(event.getName());
        if(!(event.getScoring() instanceof CustomEventScoring)) {
            ScoringColumn<? extends EventScoring<?>, ?> scoreCol = ScoringColumnFactory.buildScoringColumn(this, event.getScoring());
            _scoringColumns.add(scoreCol);
            getColumns().add(scoreCol);
        } else {
            CustomScore<?,?> customScore = ((CustomEventScoring<?,?>) event.getScoring()).getScore();
            ScoringColumn<? extends EventScoring<?>, ?> primaryScoreCol = ScoringColumnFactory.buildScoringColumn(this, customScore.getPrimaryScoring());
            ScoringColumn<? extends EventScoring<?>, ?> secondaryScoreCol = ScoringColumnFactory.buildScoringColumn(this, customScore.getPrimaryScoring());
            _scoringColumns.add(primaryScoreCol);
            _scoringColumns.add(secondaryScoreCol);
            getColumns().add(primaryScoreCol);
            getColumns().add(secondaryScoreCol);
        }
        _pointsColumns = new EventPointsColumn(_scoringColumns);
        getColumns().add(_pointsColumns);
        setEditable(true);
        _event = event;
    }

    void updateEventPoints(CompetitorRow row) {
        T scoring = (T) _event.getScoring();
        Map<Competitor, S> sortedCompetitors = new LinkedHashMap<>();
        //TODO figure out why this parent is null and fix it
        //Try just setting the parent when created, or set custom parent method on competitor row to get weight class
        ObservableList<TreeItem<Competitor>> competitorRows = row.getParent().getChildren();
        for(TreeItem<Competitor> competitorTreeItem : competitorRows) {
            if(competitorTreeItem instanceof CompetitorRow) {
                CompetitorRow competitorRow = (CompetitorRow) competitorTreeItem;
                TreeTableColumn<Object, ?> primaryScoreCol = getColumns().get(0);
                if(scoring instanceof CustomEventScoring) {
                    TreeTableColumn<Object, ?> secondaryScoreCol = getColumns().get(1);
                    CustomEventScoring<?,?> customScoring = (CustomEventScoring<?,?>) scoring;
                    CustomScore customScore = customScoring.getScore();
                    customScore.setPrimaryScore(row.getObservableValue(primaryScoreCol).getValue());
                    customScore.setSecondaryScore(row.getObservableValue(secondaryScoreCol).getValue());
                    sortedCompetitors.put(competitorRow.getCompetitor(),(S) customScore);
                } else {
                    S changedVal = (S) competitorRow.getObservableValue(primaryScoreCol).getValue();
                    sortedCompetitors.put(competitorRow.getCompetitor(),changedVal);
                }
            }
        }
    }

    public Event getEvent()
    {
        return _event;
    }
}
