package scims.controllers;

import javafx.application.Platform;
import scims.model.data.Competition;
import scims.model.data.Competitor;
import scims.model.data.WeightClass;
import scims.ui.actions.*;
import scims.ui.fx.CompetitionTreeTable;
import scims.ui.swing.SCIMSFrame;
import scims.ui.swing.tree.CompetitionTree;
import scims.ui.swing.tree.IconNode;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class CompetitionModelController {
    private final CompetitionTree _competitionTree;
    private final SCIMSFrame _parentFrame;
    private CompetitionTreeTable _treeTableInView;

    public CompetitionModelController(SCIMSFrame parentFrame, CompetitionTree competitionTree, CompetitionTreeTable treeTableInView)
    {
        _parentFrame = parentFrame;
        _competitionTree = competitionTree;
        _treeTableInView = treeTableInView;
        addListeners();
    }

    private void addListeners() {
        _competitionTree.addMouseListener(getTreeMouseListener());
    }

    private MouseListener getTreeMouseListener() {
        return new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1) {
                    TreePath path = _competitionTree.getPathForLocation(e.getX(), e.getY());
                    if (path != null) {
                        DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
                        Object obj = node.getUserObject();
                        if(obj instanceof IconNode) {
                            Object userObj = ((IconNode) obj).getUserObject();
                            if(userObj instanceof Competition) {
                                Competition competition = (Competition) userObj;
                                _parentFrame.updateFxPanelTitle(competition.getName());
                                updateTreeTableView(competition);
                            }
                        }
                    }
                }
            }
        };
    }

    public void updateTreeTableView(Competition competition)
    {
        Platform.runLater(() -> _treeTableInView.refresh(competition));
    }

    public void competitionRemoved(Competition competition)
    {
        _competitionTree.removeCompetition(competition);
        Platform.runLater(() -> {
            if(_treeTableInView.isShowingCompetition(competition)) {
                _treeTableInView.clear();
            }
        });
    }

    public void addNewCompetitionAction() {
        new NewCompetitionAction(_parentFrame, this::addCompetition).actionPerformed(null);
    }

    public void addNewCompetitorAction(Competition competition, WeightClass weightClass) {
        new NewCompetitorAction(_parentFrame, competitor -> addCompetitor(competitor, weightClass, competition)).actionPerformed(null);
    }

    public void addNewWeightClassAction(Competition competition) {
        new NewWeightClassAction(_parentFrame, wc -> addWeightClass(wc, competition)).actionPerformed(null);
    }

    private void addWeightClass(WeightClass wc, Competition competition) {
        _competitionTree.addNewWeightClass(competition, wc);
    }

    private void addCompetitor(Competitor competitor, WeightClass weightClass, Competition competition) {
        _competitionTree.addNewCompetitor(competition, weightClass, competitor);
    }

    private void addCompetition(Competition competition)
    {
        _competitionTree.addNewCompetition(competition);
        updateTreeTableView(competition);
    }

    public void editCompetitorAction(Competition competition, WeightClass weightClass, Competitor competitor) {
        new EditCompetitorAction(_parentFrame, competitor, updatedCompetitor -> updateCompetitor(updatedCompetitor, competitor, competition, weightClass)).actionPerformed(null);
    }

    public void editWeightClassAction(Competition competition, WeightClass weightClass) {
        new EditWeightClassAction(_parentFrame, weightClass, wc -> updateWeightClass(competition, wc, weightClass)).actionPerformed(null);
    }

    public void editCompetitionAction(Competition competition) {
        new EditCompetitionAction(_parentFrame, competition, updatedCompetition -> updateCompetition(updatedCompetition, competition)).actionPerformed(null);
    }

    private void updateCompetition(Competition updatedCompetition, Competition originalCompetition) {
        _competitionTree.updateCompetition(originalCompetition, updatedCompetition);
    }

    private void updateCompetitor(Competitor updatedCompetitor, Competitor oldCompetitor, Competition competition, WeightClass weightClass) {
        _competitionTree.updateCompetitor(oldCompetitor, updatedCompetitor, competition, weightClass);
    }

    private void updateWeightClass(Competition competition, WeightClass weightClass, WeightClass oldWeightClass) {
        _competitionTree.updateWeightClass(oldWeightClass, weightClass, competition);
    }
}
