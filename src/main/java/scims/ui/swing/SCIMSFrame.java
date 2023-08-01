package scims.ui.swing;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.StackPane;
import scims.controllers.CompetitionModelController;
import scims.main.CustomEventClassRegistry;
import scims.model.data.Event;
import scims.model.data.StrengthEventBuilder;
import scims.model.data.scoring.DistanceScoring;
import scims.model.data.scoring.EventScoring;
import scims.model.data.scoring.LastManStandingEliminationScoring;
import scims.model.data.scoring.LastManStandingWithPointsScoring;
import scims.model.data.scoring.RepsScoring;
import scims.model.data.scoring.TimeScoring;
import scims.ui.actions.OpenCompetitionAction;
import scims.ui.actions.SaveCompetitionAction;
import scims.ui.fx.CompetitionTreeTable;
import scims.ui.swing.tree.CompetitionTree;
import scims.ui.swing.tree.IconMutableTreeNode;
import scims.ui.swing.tree.IconTreeCellRenderer;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.event.TreeModelEvent;
import java.awt.BorderLayout;
import java.awt.Image;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class SCIMSFrame extends JFrame {

    public static final String COMPETITION_FILE_TYPE = "comp";
    private CompetitionTree _competitionTree;
    private JMenuItem _newCompetitionMenuItem;
    private final CompetitionModelController _controller;
    private CompetitionTreeTable _competitionTreeTable;
    private TitledPane _titledPane;
    private JMenuItem _saveProjectMenuItem;
    private JMenuItem _openCompetitionMenuItem;
    private JMenuItem _editCompetitionMenuItem;
    private boolean _modified;

    public SCIMSFrame() {
        super("SCIMS");
        super.setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        // Set the size of the JFrame and make it visible
        setSize(800, 600);
        URL scimsFrameImageUrl = getClass().getResource("SCIMS.png");
        if(scimsFrameImageUrl != null) {
            ImageIcon icon = new ImageIcon(scimsFrameImageUrl);
            Image scaledImage = icon.getImage().getScaledInstance(32, 32, java.awt.Image.SCALE_SMOOTH);
            setIconImage(scaledImage);
        }
        buildComponents();
        _controller = new CompetitionModelController(this, _competitionTree, _competitionTreeTable);
        _competitionTree.setController(_controller);
        _competitionTreeTable.setController(_controller);
        addListeners();
        setVisible(true);
        setLocationRelativeTo(null);
    }

    private List<Event> buildTestEvents() {
        List<Event> retVal = new ArrayList<>();
        Map<Integer, EventScoring<?>> randomScoring = new HashMap<>();
        randomScoring.put(1, new RepsScoring());
        randomScoring.put(2, new TimeScoring());
        randomScoring.put(3, new DistanceScoring());
        randomScoring.put(4, new TimeScoring());
        randomScoring.put(5, new LastManStandingEliminationScoring());
        randomScoring.put(6, new LastManStandingWithPointsScoring());
        Random rand = new Random();
        for(int i=1; i <=5; i++) {
            retVal.add(new StrengthEventBuilder().withName("Event" + i)
                    .withScoring(randomScoring.get(rand.nextInt(randomScoring.size()) + 1))
                    .withTimeLimit(null)
                    .build());
        }
        return retVal;
    }

    private void addListeners()
    {
        _newCompetitionMenuItem.addActionListener(e -> _controller.addNewCompetitionAction());
        _saveProjectMenuItem.addActionListener(new SaveCompetitionAction(_controller));
        _openCompetitionMenuItem.addActionListener(new OpenCompetitionAction(_controller));
        _editCompetitionMenuItem.addActionListener(e -> _controller.editCompetitionAction());
        _competitionTree.getModel().addTreeModelListener(new TreeModelAdapter() {
            @Override
            public void treeStructureChanged(TreeModelEvent e) {
                treeUpdated();
            }
        });
    }

    private void treeUpdated() {
        IconMutableTreeNode rootNode = (IconMutableTreeNode) _competitionTree.getModel().getRoot();
        _editCompetitionMenuItem.setEnabled(rootNode != null && rootNode.getChildCount() > 0);
    }

    private void buildComponents() {
        // Create a new JFXPanel to host the JavaFX TreeTableView
        JFXPanel fxPanel = new JFXPanel();
        _competitionTreeTable = new CompetitionTreeTable();
        _titledPane = new TitledPane("No Competition Selected", _competitionTreeTable);
        _titledPane.setCollapsible(false);
        // Create the JavaFX scene containing the TreeTableView
        StackPane pane = new StackPane();
        //pane.getChildren().add(_competitionTreeTable);
        Scene scene = new Scene(_titledPane);
        // Add the scene to the JFXPanel
        fxPanel.setScene(scene);

        _competitionTree = new CompetitionTree();
        _competitionTree.setController(_controller);
        IconTreeCellRenderer renderer = new IconTreeCellRenderer();
        _competitionTree.setCellRenderer(renderer);
        // Create the panels
        JSplitPane mainContentPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.add(_competitionTree, BorderLayout.CENTER);
        JPanel bottomPanel = new JPanel(new BorderLayout());

        // Create the menu bar
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenu editMenu = new JMenu("Edit");
        _editCompetitionMenuItem = new JMenuItem("Edit Competition...");
        _editCompetitionMenuItem.setEnabled(false);
        editMenu.add(_editCompetitionMenuItem);
        _newCompetitionMenuItem = new JMenuItem("New Competition...");
        fileMenu.add(_newCompetitionMenuItem);
        _openCompetitionMenuItem = new JMenuItem("Open Competition...");
        _saveProjectMenuItem = new JMenuItem("Save Competition");
        _saveProjectMenuItem.setEnabled(false);
        fileMenu.add(_saveProjectMenuItem);
        fileMenu.add(_openCompetitionMenuItem);
        menuBar.add(fileMenu);
        menuBar.add(editMenu);

        mainContentPane.setLeftComponent(leftPanel);
        mainContentPane.setRightComponent(fxPanel);
        mainContentPane.setDividerLocation(250);

        // Add components to the content pane
        getContentPane().add(menuBar, BorderLayout.NORTH);
        getContentPane().add(mainContentPane, BorderLayout.CENTER);
        getContentPane().add(bottomPanel, BorderLayout.SOUTH);
    }

    public void updateFxPanelTitle(String name) {
        Platform.runLater(() -> {
            _titledPane.setText(name);
            _titledPane.requestLayout();
        });

    }

    public void setModified(boolean modified) {
        _modified = modified;
        _saveProjectMenuItem.setEnabled(modified);
    }

    public boolean isModified() {
        return _modified;
    }
}
