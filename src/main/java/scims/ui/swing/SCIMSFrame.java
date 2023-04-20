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
import scims.model.data.scoring.*;
import scims.ui.actions.OpenCompetitionAction;
import scims.ui.actions.SaveCompetitionAction;
import scims.ui.fx.CompetitionTreeTable;
import scims.ui.swing.tree.CompetitionTree;
import scims.ui.swing.tree.IconTreeCellRenderer;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.util.*;
import java.util.List;

public class SCIMSFrame extends JFrame {
    private CompetitionTree _competitionTree;
    private JMenuItem _newCompetitionMenuItem;
    private final CompetitionModelController _controller;
    private CompetitionTreeTable _competitionTreeTable;
    private TitledPane _titledPane;
    private JMenuItem _saveProjectMenuItem;
    private JMenuItem _openCompetitionMenuItem;

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
        for(Event event : buildTestEvents()) {
            CustomEventClassRegistry.getInstance().registerEvent(event);
        }
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
        JPanel leftPanel = new JPanel();
        leftPanel.add(_competitionTree);
        JPanel bottomPanel = new JPanel();

        // Create the menu bar
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenu editMenu = new JMenu("Edit");
        _newCompetitionMenuItem = new JMenuItem("New Competition...");
        editMenu.add(_newCompetitionMenuItem);
        _openCompetitionMenuItem = new JMenuItem("Open Competition...");
        _saveProjectMenuItem = new JMenuItem("Save Competition");
        fileMenu.add(_saveProjectMenuItem);
        fileMenu.add(_openCompetitionMenuItem);
        menuBar.add(fileMenu);
        menuBar.add(editMenu);

        // Add components to the content pane
        getContentPane().add(menuBar, BorderLayout.NORTH);
        getContentPane().add(leftPanel, BorderLayout.WEST);
        getContentPane().add(fxPanel, BorderLayout.CENTER);
        getContentPane().add(bottomPanel, BorderLayout.SOUTH);
    }

    public void updateFxPanelTitle(String name) {
        Platform.runLater(() -> {
            _titledPane.setText(name);
            _titledPane.requestLayout();
        });

    }
}
