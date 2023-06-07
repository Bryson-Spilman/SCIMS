package scims.ui.fx;

import javafx.collections.ObservableList;
import javafx.scene.control.*;
import scims.model.data.Competitor;
import scims.model.data.Event;
import scims.model.data.scoring.CustomEventScoring;
import scims.model.data.scoring.CustomScore;
import scims.model.data.scoring.EventScoring;

import java.util.*;


public class EventColumn<T extends EventScoring<S>, S> extends LinkedTreeTableColumn {

    private final Event _event;
    private final List<ScoringColumn<?,?>> _scoringColumns = new ArrayList<>();
    private final EventPointsColumn _pointsColumn;

    @SuppressWarnings("unchecked")
    public EventColumn(Event event) {
        super(event.getName());
        if(!(event.getScoring() instanceof CustomEventScoring)) {
            ScoringColumn<? extends EventScoring<?>, ?> scoreCol = ScoringColumnFactory.buildScoringColumn(this, (T) event.getScoring());
            _scoringColumns.add(scoreCol);
            getColumns().add(scoreCol);
        } else {
            CustomScore<?,?> customScore = ((CustomEventScoring<?,?>) event.getScoring()).getScore();
            ScoringColumn<? extends EventScoring<?>, ?> primaryScoreCol = ScoringColumnFactory.buildScoringColumn(this, customScore.getPrimaryScoring());
            ScoringColumn<? extends EventScoring<?>, ?> secondaryScoreCol = ScoringColumnFactory.buildScoringColumn(this, customScore.getSecondaryScoring());
            _scoringColumns.add(primaryScoreCol);
            _scoringColumns.add(secondaryScoreCol);
            getColumns().add(primaryScoreCol);
            getColumns().add(secondaryScoreCol);
        }
        _pointsColumn = new EventPointsColumn(_scoringColumns);
        getColumns().add(_pointsColumn);
        setEditable(true);
        _event = event;
    }

    void updateEventPoints(CompetitorRow row) {
        T scoring = (T) _event.getScoring();
        Map<Competitor, S> competitorScoreMap = new LinkedHashMap<>();
        ObservableList<TreeItem<Object>> competitorRows = row.getParentRow().getChildren();
        Map<Competitor, CompetitorRow> competitorRowMap = new HashMap<>();
        for(TreeItem<Object> competitorTreeItem : competitorRows) {
            if(competitorTreeItem.getValue() instanceof CompetitorRow) {
                CompetitorRow competitorRow = (CompetitorRow) competitorTreeItem.getValue();
                competitorRowMap.put(competitorRow.getCompetitor(), competitorRow);
                TreeTableColumn<Object, ?> primaryScoreCol = getColumns().get(0);
                if(scoring instanceof CustomEventScoring) {
                    TreeTableColumn<Object, ?> secondaryScoreCol = getColumns().get(1);
                    CustomEventScoring<?,?> customScoring = (CustomEventScoring<?,?>) scoring;
                    CustomScore customScore = customScoring.getScore();
                    customScore.setPrimaryScore(row.getObservableValue(primaryScoreCol).getValue());
                    customScore.setSecondaryScore(row.getObservableValue(secondaryScoreCol).getValue());
                    competitorScoreMap.put(competitorRow.getCompetitor(),(S) customScore);
                } else {
                    S changedVal = (S) competitorRow.getObservableValue(primaryScoreCol).getValue();
                    competitorScoreMap.put(competitorRow.getCompetitor(),changedVal);
                }
            }
        }
        if(competitorScoreMap.values().stream().anyMatch(v -> v != null && !isEmptyCustom(v) && !v.toString().trim().isEmpty())) {
            Map<Competitor, Double> competitorScores = scoring.sortCompetitorScores(competitorScoreMap);
            applyPointsForCompetitors(competitorScores, competitorRowMap);
        } else {
            resetPointsForCompetitors(competitorRowMap);
        }
        getTreeTableView().refresh();
    }

    private boolean isEmptyCustom(Object v) {
        boolean isEmptyCustom = false;
        if(v instanceof CustomScore)
        {
            CustomScore<?,?> custom = ((CustomScore<?,?>) v);
            isEmptyCustom = (custom.getPrimaryScoring() == null && (custom.getSecondaryScoring() == null || custom.getSecondaryScoring().toString().isEmpty()))
                || (custom.getPrimaryScoring().getScore().toString().isEmpty() && (custom.getSecondaryScoring() == null || custom.getSecondaryScoring().toString().isEmpty()));
        }
        return isEmptyCustom;
    }

    private void resetPointsForCompetitors(Map<Competitor, CompetitorRow> competitorRowMap) {
        for(CompetitorRow row : competitorRowMap.values()) {
            row.setObservableValue(_pointsColumn, "");
        }
    }

    private void applyPointsForCompetitors(Map<Competitor, Double> sortedCompetitorsList, Map<Competitor, CompetitorRow> competitorRowsMap) {
        //1st competitor did best
        for(Map.Entry<Competitor, Double> entry : sortedCompetitorsList.entrySet()) {
            CompetitorRow row = competitorRowsMap.get(entry.getKey());
            row.setObservableValue(_pointsColumn, entry.getValue());
        }
    }

    public Event getEvent()
    {
        return _event;
    }

    public EventPointsColumn getPointsColumn() {
        return _pointsColumn;
    }

    @Override
    void updateLinkedCells(TreeItem<Object> rowObject) {
        if(rowObject != null && rowObject.getValue() instanceof CompetitorRow) {
            updateEventPoints((CompetitorRow) rowObject.getValue());
        }
    }
}
