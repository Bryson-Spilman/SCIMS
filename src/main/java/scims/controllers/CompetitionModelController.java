package scims.controllers;

import scims.model.data.*;
import scims.ui.actions.*;
import scims.ui.fx.CompetitionTreeTable;
import scims.ui.swing.SCIMSFrame;
import scims.ui.swing.tree.CompetitionTree;
import scims.ui.swing.tree.IconNode;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

public class CompetitionModelController {
    private final CompetitionTree _competitionTree;
    private final SCIMSFrame _parentFrame;
    private final CompetitionTreeTable _treeTableInView;
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
                                if(!_treeTableInView.isShowingCompetition(competition)) {
                                    _treeTableInView.refresh(competition);
                                }
                                _parentFrame.updateFxPanelTitle(competition.getName());
                            } else if (userObj instanceof WeightClass) {
                                Competition competition = getParentCompetition((WeightClass) userObj);
                                if(!_treeTableInView.isShowingCompetition(competition)) {
                                    _treeTableInView.refresh(competition);
                                }
                                _parentFrame.updateFxPanelTitle(competition.getName());
                            } else if(userObj instanceof Competitor) {
                                Competition competition = getParentCompetition((Competitor) userObj);
                                if(!_treeTableInView.isShowingCompetition(competition)) {
                                    _treeTableInView.refresh(competition);
                                }
                                _parentFrame.updateFxPanelTitle(competition.getName());
                            }
                        }
                    }
                }
            }
        };
    }

    private Competition getParentCompetition(WeightClass weightClass) {
        Competition retVal = null;
        for(Competition competition : _competitions) {
            if(competition.getWeightClasses().contains(weightClass)) {
                retVal = competition;
                break;
            }
        }
        return retVal;
    }

    private Competition getParentCompetition(Competitor competitor) {
        Competition retVal = null;
        for(Competition competition : _competitions) {
            boolean found = false;
            for(WeightClass weightClass : competition.getWeightClasses()) {
                if(weightClass.getCompetitors().contains(competitor)) {
                    retVal = competition;
                    found = true;
                    break;
                }
            }
            if(found) {
                break;
            }
        }
        return retVal;
    }

    public void removeCompetition(Competition competition) {
        int opt = JOptionPane.showConfirmDialog(_parentFrame, "Are you sure you want to remove " + competition.getName() + "?",
                "Confirm Delete", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if(opt == JOptionPane.YES_OPTION) {
            _competitionTree.removeCompetition(competition);
            _treeTableInView.removeCompetition(competition);
            _competitions.remove(competition);
        }
    }

    public void removeWeightClass(WeightClass weightClass) {
        int opt = JOptionPane.showConfirmDialog(_parentFrame, "Are you sure you want to remove " + weightClass.getName() + "?",
                "Confirm Delete", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if(opt == JOptionPane.YES_OPTION) {
            Competition updatedCompetition = null;
            Competition oldCompetition = null;
            for(Competition competitionInList : _competitions) {
                if(competitionInList.getWeightClasses().contains((StrengthWeightClass)weightClass)) {
                    List<StrengthWeightClass> weightClasses = competitionInList.getWeightClasses();
                    _competitionTree.removeWeightClass(competitionInList, weightClass);
                    _treeTableInView.removeWeightClass(weightClass);
                    weightClasses.remove(weightClass);
                    updatedCompetition = new StrengthCompetitionBuilder()
                            .fromExistingCompetition(competitionInList)
                            .withUpdatedWeightClasses(weightClasses)
                            .build();
                    oldCompetition = competitionInList;
                }
            }
            if(updatedCompetition != null) {
                int index = _competitions.indexOf(oldCompetition);
                _competitions.add(index, updatedCompetition);
                _competitions.remove(oldCompetition);
            }
        }
    }

    public void removeCompetitor(Competitor competitor) {
        int opt = JOptionPane.showConfirmDialog(_parentFrame, "Are you sure you want to remove " + competitor.getName() + "?",
                "Confirm Delete", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if(opt == JOptionPane.YES_OPTION) {
            Competition updatedCompetition = null;
            Competition oldCompetition = null;
            for(Competition competitionInList : _competitions) {
                List<StrengthWeightClass> weightClasses = competitionInList.getWeightClasses();
                for(WeightClass weightClassInList : weightClasses) {
                    if(weightClassInList.getCompetitors().contains(competitor)) {
                        _competitionTree.removeCompetitor(competitionInList, weightClassInList, competitor);
                        _treeTableInView.removeCompetitor(weightClassInList, competitor);
                        weightClassInList.removeCompetitor((StrengthCompetitor) competitor);
                        updatedCompetition = new StrengthCompetitionBuilder()
                                .fromExistingCompetition(competitionInList)
                                .withUpdatedWeightClasses(weightClasses)
                                .build();
                        oldCompetition = competitionInList;
                    }
                }
            }
            if(oldCompetition != null) {
                int index = _competitions.indexOf(oldCompetition);
                _competitions.add(index, updatedCompetition);
                _competitions.remove(oldCompetition);
            }
        }
    }

    public void addNewCompetitionAction() {
        new NewCompetitionAction(_parentFrame, this::setCompetition).actionPerformed(null);
    }

    public void addNewCompetitorAction(WeightClass weightClass) {
        new NewCompetitorAction(_parentFrame, competitor -> addCompetitor(competitor, weightClass)).actionPerformed(null);
    }

    public void addNewWeightClassAction(Competition competition) {
        new NewWeightClassAction(_parentFrame, wc -> addWeightClass(wc, competition)).actionPerformed(null);
    }

    private void addWeightClass(WeightClass wc, Competition competition) {
        for(Competition competitionInList : _competitions) {
            if(competitionInList.equals(competition)) {
                List<StrengthWeightClass> weightClasses = competitionInList.getWeightClasses();
                weightClasses.add((StrengthWeightClass) wc);
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

    private void addCompetitor(Competitor competitor, WeightClass weightClass) {
        Competition oldCompetition = null;
        Competition updatedCompetition = null;
        for(Competition competition : _competitions) {
            List<StrengthWeightClass> weightClasses = competition.getWeightClasses();
            for(WeightClass wc : weightClasses) {
                if(wc.equals(weightClass)) {
                    wc.addCompetitor((StrengthCompetitor)competitor);
                    oldCompetition = competition;
                    updatedCompetition = new StrengthCompetitionBuilder().fromExistingCompetition(competition)
                            .withUpdatedWeightClasses(weightClasses)
                            .build();
                    _competitionTree.addNewCompetitor(competition, wc, competitor);
                    _treeTableInView.addNewCompetitor(competition, wc, competitor);
                    break;
                }
            }
            if(oldCompetition != null) {
                break;
            }
        }
        if(updatedCompetition != null) {
            int index = _competitions.indexOf(oldCompetition);
            _competitions.add(index, updatedCompetition);
            _competitions.remove(oldCompetition);
        }
    }

    public void setCompetition(Competition competition)
    {
        _competitions.clear();
        _competitions.add(competition);
        _competitionTree.removeAllNodes();
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
            List<StrengthWeightClass> weightClasses = competition.getWeightClasses();
            for(WeightClass weightClass : weightClasses) {
                List<Competitor> competitors = weightClass.getCompetitors();
                if(competitors.contains(oldCompetitor)) {
                    int index = competitors.indexOf(oldCompetitor);
                    weightClass.removeCompetitor((StrengthCompetitor)oldCompetitor);
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
            List<StrengthWeightClass> weightClasses = competition.getWeightClasses();
            if(weightClasses.contains((StrengthWeightClass)oldWeightClass)) {
                int index = weightClasses.indexOf(oldWeightClass);
                weightClasses.add(index, (StrengthWeightClass) weightClass);
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

    public List<Competition> getCompetitions() {
        return new ArrayList<>(_competitions);
    }

    public Window getFrame() {
        return _parentFrame;
    }
}
