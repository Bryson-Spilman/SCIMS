package scims.ui.swing.tree;

import scims.model.data.Competition;
import scims.model.data.Competitor;
import scims.model.data.WeightClass;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

public class CompetitionTree extends JTree {

    private final DefaultMutableTreeNode _root;

    public CompetitionTree()
    {
        super(new IconMutableTreeNode("Competitions", null));
        _root = (DefaultMutableTreeNode) getModel().getRoot();
    }

    public void addNewCompetition(Competition competition)
    {
        DefaultMutableTreeNode competitionNode = new IconMutableTreeNode(competition, getClass().getResource("CompetitionIcon.png"));
        _root.add(competitionNode);

        for(WeightClass weightClass : competition.getWeightClasses())
        {
            DefaultMutableTreeNode weightClassNode = new IconMutableTreeNode(weightClass, getClass().getResource("WeightClassIcon.png"));
            for(Competitor competitor : weightClass.getCompetitors())
            {
                weightClassNode.add(new IconMutableTreeNode(competitor, getClass().getResource("CompetitorIcon.png")));
            }
            competitionNode.add(weightClassNode);
        }
        expandPath(new TreePath(competitionNode.getPath()));

        ((DefaultTreeModel) getModel()).reload();
    }

    public void removeCompetition(Competition competition)
    {
        int removeIndex = -1;
        for(int i=0; i < _root.getChildCount(); i++)
        {
            if(((DefaultMutableTreeNode)_root.getChildAt(i)).getUserObject() == competition)
            {
                removeIndex = 0;
            }
        }
        _root.remove(removeIndex);
    }
}
