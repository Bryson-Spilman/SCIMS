package scims.ui.actions;

import scims.model.data.Competition;
import scims.ui.swing.CompetitionDialog;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.function.Consumer;

public class EditCompetitionAction implements ActionListener {
    private final JFrame _parentFrame;
    private final Consumer<Competition> _consumerOfCreate;
    private final Competition _competition;

    public EditCompetitionAction(JFrame parentFrame, Competition competition, Consumer<Competition> consumerOfCreate) {
        _parentFrame = parentFrame;
        _consumerOfCreate = consumerOfCreate;
        _competition = competition;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        CompetitionDialog competitionDialog = new CompetitionDialog(_parentFrame, _consumerOfCreate);
        competitionDialog.fillPanel(_competition);
        competitionDialog.setVisible(true);
    }
}
