package scims.main;

import scims.ui.swing.SCIMSFrame;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.atomic.AtomicReference;
import java.util.prefs.Preferences;

public class SCIMS {
    private static final String PREF_KEY_DIRECTORY = "scims_directory";
    private static final String DEFAULT_DIRECTORY = "";
    private static final AtomicReference<Path> COMPETITIONS_DIRECTORY = new AtomicReference<>();
    public static void main(String[] args) {
        Preferences prefs = Preferences.userNodeForPackage(SCIMS.class);
        String selectedDir = prefs.get(PREF_KEY_DIRECTORY, DEFAULT_DIRECTORY);
        Path compDirectory = Paths.get(selectedDir).resolve("scims/competitions");
        // Check if the stored directory is valid
        boolean isValidDirectory = !selectedDir.isEmpty() && new File(selectedDir).isDirectory()
                && compDirectory.toFile().isDirectory();

        if (!isValidDirectory) {
            // Show the directory chooser if the stored directory is not valid
            JFrame frame = new JFrame();
            frame.setTitle("Select Working Directory");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

            int result = fileChooser.showOpenDialog(frame);
            if (result == JFileChooser.APPROVE_OPTION) {
                selectedDir = fileChooser.getSelectedFile().getAbsolutePath();
                try
                {
                    Files.createDirectories(compDirectory);
                }
                catch (IOException e)
                {
                    throw new RuntimeException(e);
                }
                prefs.put(PREF_KEY_DIRECTORY, selectedDir);
            }
        }
        COMPETITIONS_DIRECTORY.set(compDirectory);
        SwingUtilities.invokeLater(SCIMSFrame::new);
    }

    public static Path getCompetitionsDirectory()
    {
        return COMPETITIONS_DIRECTORY.get();
    }
}
