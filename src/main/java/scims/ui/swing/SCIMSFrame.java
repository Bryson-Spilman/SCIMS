package scims.ui.swing;

import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import scims.ui.fx.CompetitionTreeTable;

import javax.swing.*;
import java.awt.*;

public class SCIMSFrame extends JFrame {
    public SCIMSFrame() {
        // Create a new JFXPanel to host the JavaFX TreeTableView
        JFXPanel fxPanel = new JFXPanel();

        // Create the JavaFX scene containing the TreeTableView
        StackPane pane = new StackPane();
        pane.getChildren().add(new CompetitionTreeTable());
        Scene scene = new Scene(pane);

        // Add the scene to the JFXPanel
        fxPanel.setScene(scene);

        // Add the JFXPanel to the JFrame
        getContentPane().add(fxPanel, BorderLayout.CENTER);

        // Set the size of the JFrame and make it visible
        setSize(800, 600);
        setVisible(true);
    }

}
