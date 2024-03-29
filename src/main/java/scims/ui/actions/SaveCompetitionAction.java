package scims.ui.actions;

import scims.controllers.CompetitionModelController;
import scims.main.SCIMS;
import scims.model.data.Competition;
import scims.model.data.CompetitionObjectMapper;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static scims.ui.swing.SCIMSFrame.COMPETITION_FILE_TYPE;

public class SaveCompetitionAction implements ActionListener {
    private static final Logger LOGGER = Logger.getLogger(SaveCompetitionAction.class.getName());
    private final CompetitionModelController _controller;

    public SaveCompetitionAction(CompetitionModelController controller) {
        _controller = controller;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        save();
    }

    private void save() {
        List<Competition> competitions = _controller.getCompetitions();
        if(!competitions.isEmpty())
        {
            try
            {
                _controller.saveScores();
                _controller.saveCompetition();
            }
            catch (IOException e)
            {
                LOGGER.log(Level.SEVERE, e, ()-> "Save failed: " + e.getMessage());
                JOptionPane.showMessageDialog(SwingUtilities.windowForComponent(JOptionPane.getRootFrame()), "Save failed! " + e.getMessage(), "Save Failure", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
