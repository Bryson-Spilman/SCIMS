package scims.ui.actions;

import scims.model.data.Competitor;
import scims.ui.swing.dialogs.CompetitorDialog;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.function.Consumer;

public class EditCompetitorAction implements ActionListener {
    private final JFrame _parentFrame;
    private final Consumer<Competitor> _consumerOfCreate;
    private final Competitor _competitor;

    public EditCompetitorAction(JFrame parentFrame, Competitor competitor, Consumer<Competitor> consumerOfCreate) {
        _parentFrame = parentFrame;
        _consumerOfCreate = consumerOfCreate;
        _competitor = competitor;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        CompetitorDialog competitorDialog = new CompetitorDialog(_parentFrame, _consumerOfCreate);
        competitorDialog.setTitle("Update Competitor");
        competitorDialog.fillPanel(_competitor);
        competitorDialog.setVisible(true);
    }
}
