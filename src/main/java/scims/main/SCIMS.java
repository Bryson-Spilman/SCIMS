package scims.main;

import scims.ui.swing.SCIMSFrame;

import javax.swing.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.prefs.Preferences;

public class SCIMS {
    private static final String PREF_KEY_DIRECTORY = "scims_directory";
    private static final String DEFAULT_DIRECTORY = "";
    private static final AtomicReference<Path> COMPETITIONS_DIRECTORY = new AtomicReference<>();
    private static final AtomicReference<Path> WEIGHT_CLASSES_FILE = new AtomicReference<>();
    private static final AtomicReference<Path> EVENTS_FILE = new AtomicReference<>();
    public static final String WEIGHT_CLASSES_XML = "Weight-Classes.xml";
    private static final String EVENTS_FILE_XML = "Events.xml";
    private static SCIMSFrame FRAME;

    public static void main(String[] args) {
        Preferences prefs = Preferences.userNodeForPackage(SCIMS.class);
        String selectedDir = prefs.get(PREF_KEY_DIRECTORY, DEFAULT_DIRECTORY);
        Path compDirectory = Paths.get(selectedDir).resolve("scims/competitions");
        Handler fileHandler= null;
        try {
            fileHandler = new FileHandler(Paths.get(selectedDir).resolve("scims\\output.log").toString());
            fileHandler.setFormatter(new SimpleFormatter());
            Logger.getLogger("").addHandler(fileHandler);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(fileHandler != null) {
                fileHandler.close();
            }
        }
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
        WEIGHT_CLASSES_FILE.set(compDirectory.resolve(WEIGHT_CLASSES_XML));
        if(!Files.exists(WEIGHT_CLASSES_FILE.get()))
        {
            try
            {
                Files.createFile(WEIGHT_CLASSES_FILE.get());
            }
            catch (IOException e)
            {
                throw new RuntimeException(e);
            }
        }
        EVENTS_FILE.set(compDirectory.resolve(EVENTS_FILE_XML));
        if(!Files.exists(EVENTS_FILE.get()))
        {
            try
            {
                Files.createFile(EVENTS_FILE.get());
            }
            catch (IOException e)
            {
                throw new RuntimeException(e);
            }
        }
        SwingUtilities.invokeLater(() -> FRAME = new SCIMSFrame());
    }

    public static Path getCompetitionsDirectory()
    {
        return COMPETITIONS_DIRECTORY.get();
    }

    public static Path getWeightClassesFile() {
        return WEIGHT_CLASSES_FILE.get();
    }

    public static Path getEventsFile() {
        return EVENTS_FILE.get();
    }

    public static SCIMSFrame getFrame() {
        return FRAME;
    }
}
