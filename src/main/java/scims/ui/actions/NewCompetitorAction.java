package scims.ui.actions;

import scims.model.data.Competitor;
import scims.ui.swing.dialogs.CompetitionDialog;
import scims.ui.swing.dialogs.CompetitorDialog;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.function.Consumer;

public class NewCompetitorAction implements ActionListener {
    private final JFrame _parentFrame;
    private final Consumer<Competitor> _consumerOfCreate;

    public NewCompetitorAction(JFrame parentFrame, Consumer<Competitor> consumerOfCreate) {
        _parentFrame = parentFrame;
        _consumerOfCreate = consumerOfCreate;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        CompetitorDialog competitorDialog = new CompetitorDialog(_parentFrame, _consumerOfCreate);
        competitorDialog.setVisible(true);
    }
}
