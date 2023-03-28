package scims.controllers;

import javafx.application.Platform;
import scims.model.data.*;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CompetitionModelController {
    private final CompetitionTree _competitionTree;
    private final SCIMSFrame _parentFrame;
    private CompetitionTreeTable _treeTableInView;
    private final List<Competition> _competitions = new ArrayList<>();

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
                                _treeTableInView.refresh(competition);
                            }
                        }
                    }
                }
            }
        };
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
        for(Competition competitionInList : _competitions) {
            if(competitionInList.equals(competition)) {
                List<WeightClass> weightClasses = competitionInList.getWeightClasses();
                weightClasses.add(wc);
                StrengthCompetition updatedCompetition = new StrengthCompetitionBuilder().fromExistingCompetition(competition)
                        .withUpdatedWeightClasses(weightClasses)
                        .build();
                _competitionTree.addNewWeightClass(competition, wc);
                _treeTableInView.addNewWeightClass(competition, wc);
                int index = _competitions.indexOf(competition);
                _competitions.add(index, updatedCompetition);
                _competitions.remove(competition);
            }
        }

    }

    private void addCompetitor(Competitor competitor, WeightClass weightClass, Competition competition) {
        List<WeightClass> weightClasses = competition.getWeightClasses();
        for(WeightClass wc : weightClasses) {
            if(wc.equals(weightClass)) {
                wc.addCompetitor(competitor);
                StrengthCompetition updatedCompetition = new StrengthCompetitionBuilder().fromExistingCompetition(competition)
                        .withUpdatedWeightClasses(weightClasses)
                        .build();
                _competitionTree.addNewCompetitor(competition, wc, competitor);
                _treeTableInView.addNewCompetitor(competition, wc, competitor);
                int index = _competitions.indexOf(competition);
                _competitions.add(index, updatedCompetition);
                _competitions.remove(competition);
                break;
            }
        }
    }

    private void addCompetition(Competition competition)
    {
        _competitions.add(competition);
        _competitionTree.addNewCompetition(competition);
        _treeTableInView.refresh(competition);
    }

    public void editCompetitorAction(Competitor competitor) {
        new EditCompetitorAction(_parentFrame, competitor, updatedCompetitor -> updateCompetitor(updatedCompetitor, competitor)).actionPerformed(null);
    }

    public void editWeightClassAction(WeightClass weightClass) {
        new EditWeightClassAction(_parentFrame, weightClass, wc -> updateWeightClass(wc, weightClass)).actionPerformed(null);
    }

    public void editCompetitionAction(Competition competition) {
        new EditCompetitionAction(_parentFrame, competition, updatedCompetition -> updateCompetition(updatedCompetition, competition)).actionPerformed(null);
    }

    private void updateCompetition(Competition updatedCompetition, Competition originalCompetition) {
        _competitionTree.updateCompetition(originalCompetition, updatedCompetition);
        _treeTableInView.refresh(updatedCompetition);
        int index = _competitions.indexOf(originalCompetition);
        _competitions.add(index, updatedCompetition);
        _competitions.remove(originalCompetition);
    }

    private void updateCompetitor(Competitor updatedCompetitor, Competitor oldCompetitor) {
        Competition updatedCompetition = null;
        Competition oldCompetition = null;
        WeightClass weightClassForCompetitor = null;
        for(Competition competition : _competitions) {
            List<WeightClass> weightClasses = competition.getWeightClasses();
            for(WeightClass weightClass : weightClasses) {
                List<Competitor> competitors = weightClass.getCompetitors();
                if(competitors.contains(oldCompetitor)) {
                    int index = competitors.indexOf(oldCompetitor);
                    weightClass.removeCompetitor(oldCompetitor);
                    weightClass.addCompetitor(index, updatedCompetitor);
                    weightClassForCompetitor = weightClass;
                    oldCompetition = competition;
                    break;
                }
            }
            if(oldCompetition != null) {
                updatedCompetition = new StrengthCompetitionBuilder().fromExistingCompetition(competition)
                        .withUpdatedWeightClasses(weightClasses)
                        .build();
                break;
            }
        }
        if(oldCompetition != null) {
            _competitionTree.updateCompetitor(oldCompetitor, updatedCompetitor, oldCompetition, weightClassForCompetitor);
            _treeTableInView.updateCompetitor(oldCompetitor, updatedCompetitor, oldCompetition, weightClassForCompetitor);
            int index = _competitions.indexOf(oldCompetition);
            _competitions.add(index, updatedCompetition);
            _competitions.remove(oldCompetition);
        }
    }

    private void updateWeightClass(WeightClass weightClass, WeightClass oldWeightClass) {
        Competition updatedCompetition = null;
        Competition oldCompetition = null;
        for(Competition competition : _competitions) {
            List<WeightClass> weightClasses = competition.getWeightClasses();
            if(weightClasses.contains(oldWeightClass)) {
                int index = weightClasses.indexOf(oldWeightClass);
                weightClasses.add(index, weightClass);
                weightClasses.remove(oldWeightClass);
                oldCompetition = competition;
                updatedCompetition = new StrengthCompetitionBuilder().fromExistingCompetition(competition)
                        .withUpdatedWeightClasses(weightClasses)
                        .build();
                break;
            }
        }
        if(oldCompetition != null) {
            _competitionTree.updateWeightClass(oldWeightClass, weightClass, oldCompetition);
            _treeTableInView.updateWeightClass(oldWeightClass, weightClass, oldCompetition);
            int index = _competitions.indexOf(oldCompetition);
            _competitions.add(index, updatedCompetition);
            _competitions.remove(oldCompetition);
        }
    }
}
