package scims.controllers;

import javafx.application.Platform;
import scims.model.data.Competition;
import scims.ui.fx.CompetitionTreeTable;
import scims.ui.swing.tree.CompetitionTree;

import java.util.ArrayList;
import java.util.List;

public class CompetitionModelController {
    private final CompetitionTree _competitionTree;
    private CompetitionTreeTable _treeTableInView;
    private final List<Competition> _competitionsList;

    public CompetitionModelController(CompetitionTree competitionTree, CompetitionTreeTable treeTableInView)
    {
        _competitionTree = competitionTree;
        _treeTableInView = treeTableInView;
        _competitionsList = new ArrayList<>();
    }

    public void updateTreeTableView(Competition competition)
    {
        _treeTableInView.refresh(competition);
    }

    public void newCompetitionCreated(Competition competition)
    {
        _competitionsList.add(competition);
        _competitionTree.addNewCompetition(competition);
        Platform.runLater(() -> updateTreeTableView(competition));
    }

    public void competitionRemoved(Competition competition)
    {
        _competitionsList.remove(competition);
        _competitionTree.removeCompetition(competition);
        if(_treeTableInView.isShowingCompetition(competition))
        {
            _treeTableInView.clear();
        }
    }
}
