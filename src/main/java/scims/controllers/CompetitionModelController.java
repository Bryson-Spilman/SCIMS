package scims.controllers;

import javafx.application.Platform;
import scims.model.data.Competition;
import scims.model.data.WeightClass;
import scims.ui.actions.EditCompetitionAction;
import scims.ui.actions.EditWeightClassAction;
import scims.ui.actions.NewCompetitionAction;
import scims.ui.actions.NewWeightClassAction;
import scims.ui.fx.CompetitionTreeTable;
import scims.ui.swing.SCIMSFrame;
import scims.ui.swing.tree.CompetitionTree;
import scims.ui.swing.tree.IconNode;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

public class CompetitionModelController {
    private final CompetitionTree _competitionTree;
    private final SCIMSFrame _parentFrame;
    private CompetitionTreeTable _treeTableInView;
    private final List<Competition> _competitionsList;

    public CompetitionModelController(SCIMSFrame parentFrame, CompetitionTree competitionTree, CompetitionTreeTable treeTableInView)
    {
        _parentFrame = parentFrame;
        _competitionTree = competitionTree;
        _treeTableInView = treeTableInView;
        _competitionsList = new ArrayList<>();
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

    public void newCompetitionCreated(Competition competition)
    {
        _competitionsList.add(competition);
        _competitionTree.addNewCompetition(competition);
        updateTreeTableView(competition);
    }

    public void competitionRemoved(Competition competition)
    {
        _competitionsList.remove(competition);
        _competitionTree.removeCompetition(competition);
        Platform.runLater(() -> {
            if(_treeTableInView.isShowingCompetition(competition)) {
                _treeTableInView.clear();
            }
        });
    }

    public void newCompetitionAction() {
        new NewCompetitionAction(_parentFrame, this::newCompetitionCreated).actionPerformed(null);
    }

    public void editWeightClass(WeightClass weightClass) {
        new EditWeightClassAction(_parentFrame, weightClass, this::weightClassUpdated);
    }

    private void weightClassUpdated(WeightClass weightClass) {
        //TODO
    }

    public void addNewCompetitorAction(WeightClass weightClass) {
        //TODO
    }

    public void addNewWeightClassAction(Competition competition) {
        new NewWeightClassAction(_parentFrame, wc -> addWeightClass(wc, competition)).actionPerformed(null);
    }

    private void addWeightClass(WeightClass wc, Competition competition) {
        //TODO
    }

    public void editCompetitionAction(Competition competition) {
        new EditCompetitionAction(_parentFrame, competition, updatedCompetition -> updateCompetition(updatedCompetition, competition)).actionPerformed(null);
    }

    private void updateCompetition(Competition updatedCompetition, Competition originalCompetition) {
        System.out.println();

    }
}
