package scims.ui.swing.tree;

import scims.controllers.CompetitionModelController;
import scims.model.data.Competition;
import scims.model.data.Competitor;
import scims.model.data.WeightClass;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.util.Enumeration;

public class CompetitionTree extends JTree {

    private static final URL COMPETITOR_IMG_URL = CompetitionTree.class.getResource("CompetitorIcon.png");
    private static final URL COMPETITION_IMG_URL = CompetitionTree.class.getResource("CompetitionIcon.png");
    private static final URL WEIGHT_CLASS_IMG_URL = CompetitionTree.class.getResource("WeightClassIcon.png");
    private final IconMutableTreeNode _root;
    private CompetitionModelController _controller;

    public CompetitionTree()
    {
        super(new IconMutableTreeNode("Competitions", null));
        _root = (IconMutableTreeNode) getModel().getRoot();
        _root.setPopUpMenu(buildRootPopUpMenu());
        addListeners();
    }

    public void setController(CompetitionModelController controller) {
        _controller = controller;
    }

    private JPopupMenu buildRootPopUpMenu() {
        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem menuItem1 = new JMenuItem("Create New Competition...");
        popupMenu.add(menuItem1);
        menuItem1.addActionListener(e -> _controller.addNewCompetitionAction());
        return popupMenu;
    }

    private JPopupMenu buildCompetitionPopUpMenu(Competition competition) {
        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem menuItem1 = new JMenuItem("Add New Weight Class...");
        JMenuItem menuItem2 = new JMenuItem("Edit Competition...");
        popupMenu.add(menuItem1);
        popupMenu.addSeparator();
        popupMenu.add(menuItem2);
        menuItem1.addActionListener(e -> _controller.addNewWeightClassAction(competition));
        menuItem2.addActionListener(e -> _controller.editCompetitionAction(competition));
        return popupMenu;
    }

    private JPopupMenu buildWeightClassPopUpMenu(Competition competition, WeightClass weightClass) {
        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem menuItem1 = new JMenuItem("Add New Competitor...");
        JMenuItem menuItem2 = new JMenuItem("Edit Weight Class...");
        popupMenu.add(menuItem1);
        popupMenu.addSeparator();
        popupMenu.add(menuItem2);
        menuItem1.addActionListener(e -> _controller.addNewCompetitorAction(competition, weightClass));
        menuItem2.addActionListener(e -> _controller.editWeightClassAction(competition, weightClass));
        return popupMenu;
    }

    private JPopupMenu buildCompetitorPopUpMenu(Competition competition, WeightClass weightClass, Competitor competitor) {
        JPopupMenu jPopupMenu = new JPopupMenu();
        JMenuItem menuItem1 = new JMenuItem("Edit Competitor...");
        jPopupMenu.add(menuItem1);
        menuItem1.addActionListener(e -> _controller.editCompetitorAction(competition, weightClass, competitor));
        return jPopupMenu;
    }

    private void addListeners() {
        CompetitionTree tree = this;
        addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    int x = e.getX();
                    int y = e.getY();
                    TreePath path = getPathForLocation(x, y);
                    if (path != null) {
                        setSelectionPath(path);
                        Object comp = path.getLastPathComponent();
                        if(comp instanceof IconMutableTreeNode) {
                            JPopupMenu popUpMenu = ((IconMutableTreeNode) comp).getPopupMenu();
                            if(popUpMenu != null) {
                                popUpMenu.show(tree, x, y);
                            }
                        }
                    }
                }
            }
        });
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
        updateTree();
    }

    public IconMutableTreeNode getCompetitorNode(Competition competition, WeightClass weightClass, Competitor competitor) {
        IconMutableTreeNode retVal = null;
        IconMutableTreeNode weightClassNode = getWeightClassNode(competition, weightClass);
        if(weightClassNode != null) {
            for (int i = 0; i < weightClassNode.getChildCount(); i++) {
                IconMutableTreeNode competitorNode = (IconMutableTreeNode) weightClassNode.getChildAt(i);
                IconNode iconNode = (IconNode) competitorNode.getUserObject();
                Competitor competitorFromNode = (Competitor) iconNode.getUserObject();
                if (competitorFromNode.equals(competitor)) {
                    retVal = competitorNode;
                    break;
                }
            }
        }
        return retVal;
    }

    public IconMutableTreeNode getWeightClassNode(Competition competition, WeightClass weightClass) {
        IconMutableTreeNode retVal = null;
        IconMutableTreeNode competitionNode = getCompetitionNode(competition);
        if(competitionNode != null) {
            for (int i = 0; i < competitionNode.getChildCount(); i++) {
                IconMutableTreeNode weightClassNode = (IconMutableTreeNode) competitionNode.getChildAt(i);
                IconNode iconNode = (IconNode) weightClassNode.getUserObject();
                WeightClass weightClassFromNode = (WeightClass) iconNode.getUserObject();
                if (weightClassFromNode.equals(weightClass)) {
                    retVal = weightClassNode;
                    break;
                }
            }
        }
        return retVal;
    }

    public IconMutableTreeNode getCompetitionNode(Competition competition) {
        IconMutableTreeNode retVal = null;
        for(int i=0; i < _root.getChildCount(); i++) {
            IconMutableTreeNode competitionNode = (IconMutableTreeNode) _root.getChildAt(i);
            IconNode iconNode = (IconNode) competitionNode.getUserObject();
            Competition competitionFromNode = (Competition) iconNode.getUserObject();
            if(competitionFromNode.equals(competition)) {
                retVal = competitionNode;
                break;
            }
        }
        return retVal;
    }

    public void addNewCompetitor(Competition competition, WeightClass weightClass, Competitor competitor) {
        IconMutableTreeNode weightClassNode = getWeightClassNode(competition, weightClass);
        if(weightClassNode != null) {
            IconMutableTreeNode competitorNode = new IconMutableTreeNode(competitor, COMPETITOR_IMG_URL);
            competitorNode.setPopUpMenu(buildCompetitorPopUpMenu(competition, weightClass, competitor));
            weightClassNode.add(competitorNode);
            expandPath(new TreePath(competitorNode.getPath()));
        }
        updateTree();
    }

    public void addNewWeightClass(Competition competition, WeightClass wc) {
        IconMutableTreeNode competitionNode = getCompetitionNode(competition);
        if(competitionNode != null) {
            IconMutableTreeNode weightClassNode = new IconMutableTreeNode(wc, WEIGHT_CLASS_IMG_URL);
            weightClassNode.setPopUpMenu(buildWeightClassPopUpMenu(competition, wc));
            competitionNode.add(weightClassNode);
            expandPath(new TreePath(weightClassNode.getPath()));
        }
        updateTree();
    }

    public void addNewCompetition(Competition competition)
    {
        IconMutableTreeNode competitionNode = new IconMutableTreeNode(competition, COMPETITION_IMG_URL);
        competitionNode.setPopUpMenu(buildCompetitionPopUpMenu(competition));
        _root.add(competitionNode);
        for(WeightClass weightClass : competition.getWeightClasses())
        {
            IconMutableTreeNode weightClassNode = new IconMutableTreeNode(weightClass, WEIGHT_CLASS_IMG_URL);
            weightClassNode.setPopUpMenu(buildWeightClassPopUpMenu(competition, weightClass));
            for(Competitor competitor : weightClass.getCompetitors())
            {
                IconMutableTreeNode competitorNode = new IconMutableTreeNode(competitor, COMPETITOR_IMG_URL);
                weightClassNode.add(competitorNode);
            }
            competitionNode.add(weightClassNode);
        }
        expandPath(new TreePath(competitionNode.getPath()));
        updateTree();
    }

    public void updateCompetitor(Competitor oldCompetitor, Competitor updatedCompetitor, Competition competition, WeightClass weightClass) {
        IconMutableTreeNode oldCompetitorNode = getCompetitorNode(competition, weightClass, oldCompetitor);
        if(oldCompetitorNode != null) {
            oldCompetitorNode.setUserObject(new IconNode(updatedCompetitor, COMPETITOR_IMG_URL));
            oldCompetitorNode.setPopUpMenu(buildCompetitorPopUpMenu(competition, weightClass, updatedCompetitor));
            updateTree();
        }
    }

    public void updateCompetition(Competition originalCompetition, Competition updatedCompetition) {
        IconMutableTreeNode oldCompetitionNode = getCompetitionNode(originalCompetition);
        if(oldCompetitionNode != null) {
            oldCompetitionNode.setUserObject(new IconNode(updatedCompetition, COMPETITION_IMG_URL));
            oldCompetitionNode.setPopUpMenu(buildCompetitionPopUpMenu(updatedCompetition));
            updateTree();
        }
    }

    public void updateWeightClass(WeightClass oldWeightClass, WeightClass updatedWeightClass, Competition competition) {
        IconMutableTreeNode oldWeightClassNode = getWeightClassNode(competition, oldWeightClass);
        if (oldWeightClassNode != null) {
            oldWeightClassNode.setUserObject(new IconNode(updatedWeightClass, WEIGHT_CLASS_IMG_URL));
            oldWeightClassNode.setPopUpMenu(buildWeightClassPopUpMenu(competition, updatedWeightClass));
            updateTree();
        }
    }

    private void updateTree() {
        TreePath selectedPath = getSelectionPath();
        Enumeration<TreePath> expandedPaths = getExpandedDescendants(getPathForRow(0));
        ((DefaultTreeModel)getModel()).reload();
        if (selectedPath != null) {
            setSelectionPath(selectedPath);
        }
        while (expandedPaths.hasMoreElements()) {
            TreePath path = expandedPaths.nextElement();
            expandPath(path);
        }
    }

}
