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

public class CompetitionTree extends JTree {

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
        menuItem1.addActionListener(e -> _controller.newCompetitionAction());
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

    private JPopupMenu buildWeightClassPopUpMenu(WeightClass weightClass) {
        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem menuItem1 = new JMenuItem("Add New Competitor...");
        JMenuItem menuItem2 = new JMenuItem("Edit Weight Class...");
        popupMenu.add(menuItem1);
        popupMenu.addSeparator();
        popupMenu.add(menuItem2);
        menuItem1.addActionListener(e -> _controller.addNewCompetitorAction(weightClass));
        menuItem2.addActionListener(e -> _controller.editWeightClass(weightClass));
        return popupMenu;
    }

    private JPopupMenu buildCompetitorPopUpMenu() {
        JPopupMenu jPopupMenu = new JPopupMenu();
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
                            popUpMenu.show(tree, x, y);
                        }
                    }
                }
            }
        });
    }

    public void addNewCompetition(Competition competition)
    {
        IconMutableTreeNode competitionNode = new IconMutableTreeNode(competition, getClass().getResource("CompetitionIcon.png"));
        competitionNode.setPopUpMenu(buildCompetitionPopUpMenu(competition));
        _root.add(competitionNode);
        for(WeightClass weightClass : competition.getWeightClasses())
        {
            IconMutableTreeNode weightClassNode = new IconMutableTreeNode(weightClass, getClass().getResource("WeightClassIcon.png"));
            weightClassNode.setPopUpMenu(buildWeightClassPopUpMenu(weightClass));
            for(Competitor competitor : weightClass.getCompetitors())
            {
                IconMutableTreeNode competitorNode = new IconMutableTreeNode(competitor, getClass().getResource("CompetitorIcon.png"));
                weightClassNode.add(competitorNode);
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
