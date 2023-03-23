package scims.ui.swing;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.StackPane;
import scims.controllers.CompetitionModelController;
import scims.model.data.Competition;
import scims.model.data.StrengthCompetition;
import scims.ui.actions.NewCompetitionAction;
import scims.ui.fx.CompetitionTreeTable;
import scims.ui.swing.tree.CompetitionTree;
import scims.ui.swing.tree.IconNode;
import scims.ui.swing.tree.IconTreeCellRenderer;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;

public class SCIMSFrame extends JFrame {
    private CompetitionTree _competitionTree;
    private JMenuItem _newCompetitionMenuItem;
    private final CompetitionModelController _controller;
    private CompetitionTreeTable _competitionTreeTable;
    private TitledPane _titledPane;

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
        addListeners();
        setVisible(true);
        setLocationRelativeTo(null);
    }

    private void addListeners()
    {
        _newCompetitionMenuItem.addActionListener(new NewCompetitionAction(this, _controller::newCompetitionCreated));
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
