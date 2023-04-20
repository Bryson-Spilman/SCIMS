package scims.ui.actions;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import scims.controllers.CompetitionModelController;
import scims.main.SCIMS;
import scims.ui.swing.dialogs.SelectCompetitionDialog;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Logger;

public class OpenCompetitionAction implements ActionListener {
    private final CompetitionModelController _controller;

    public OpenCompetitionAction(CompetitionModelController controller) {
        _controller = controller;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        SelectCompetitionDialog dlg = new SelectCompetitionDialog(_controller.getFrame(), _controller::setCompetition);
        dlg.setVisible(true);
    }
}
