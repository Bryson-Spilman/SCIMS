package scims.ui.actions;

import scims.model.data.Competition;
import scims.ui.swing.NewCompetitionDialog;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.function.Consumer;

public class NewCompetitionAction implements ActionListener {
    private final JFrame _parentFrame;
    private final Consumer<Competition> _consumerOfCreate;

    public NewCompetitionAction(JFrame parentFrame, Consumer<Competition> consumerOfCreate)
    {
        _parentFrame = parentFrame;
        _consumerOfCreate = consumerOfCreate;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        NewCompetitionDialog newCompetitionDialog = new NewCompetitionDialog(_parentFrame, _consumerOfCreate);
        newCompetitionDialog.setVisible(true);
    }
}
