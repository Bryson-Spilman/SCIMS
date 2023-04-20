package scims.ui.swing.dialogs;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import scims.main.SCIMS;
import scims.model.data.Competition;
import scims.model.data.CompetitionObjectMapper;
import scims.model.data.StrengthCompetition;
import scims.model.data.StrengthCompetitions;
import scims.ui.Modifiable;
import scims.ui.swing.OkCancelPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SelectCompetitionDialog extends JDialog implements Modifiable {

    private static final Logger LOGGER = Logger.getLogger(SelectCompetitionDialog.class.getName());
    private final Consumer<Competition> _selectAction;
    private JComboBox<String> _competitionComboBox;
    private OkCancelPanel _okCancelPanel;
    private boolean _isModified;

    public SelectCompetitionDialog(Window parent, Consumer<Competition> selectAction)
    {
        super(parent, "Select Competition");
        setModal(true);
        setLayout(new GridBagLayout());
        setSize(400,250);
        setMinimumSize(new Dimension(Integer.MIN_VALUE, 175));
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(parent);
        _selectAction = selectAction;
        buildComponents();
        addListeners();
    }

    private void addListeners() {
        _competitionComboBox.addActionListener(e -> setModified(true));
        _okCancelPanel.addOkActionListener(e -> okAction());
        _okCancelPanel.addCancelActionListener(e -> closeDialogClicked());
        addWindowListener( new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                closeDialogClicked();
            }
        });
    }

    private void okAction() {
        _selectAction.accept(loadCompetition());
        dispose();
    }

    private void closeDialogClicked() {
        int opt = JOptionPane.YES_OPTION;
        if(_isModified)
        {
            opt = JOptionPane.showConfirmDialog(this, "Cancel selection?", "Confirm Cancel",
                    JOptionPane.YES_NO_OPTION);
        }
        if(opt == JOptionPane.YES_OPTION)
        {
            dispose();
        }
    }

    private Competition loadCompetition() {
        Competition competition = null;
        Object selected = _competitionComboBox.getSelectedItem();
        if(selected != null) {
            Path competitionXml = SCIMS.getCompetitionsDirectory().resolve(selected + ".xml");
            try {
                competition = CompetitionObjectMapper.deSerializeCompetition(competitionXml);
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, e, () -> "Failed to parse competition");
                JOptionPane.showMessageDialog(this, "Failed to open " + selected, "Failed To Open",JOptionPane.ERROR_MESSAGE);
            }
        }
        return competition;
    }

    private void buildComponents() {
        JLabel selectLabel = new JLabel("Select Competition:");
        _competitionComboBox = new JComboBox<>();
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
        File[] compFiles = SCIMS.getCompetitionsDirectory().toFile().listFiles();
        if(compFiles != null)
        {
            for(File competitionFile : compFiles)
            {
                model.addElement(competitionFile.getName().replace(".xml", ""));
            }
        }
        _competitionComboBox.setModel(model);

        GridBagConstraints  gbc = new GridBagConstraints();
        gbc.gridx     = GridBagConstraints.RELATIVE;
        gbc.gridy     = GridBagConstraints.RELATIVE;
        gbc.gridwidth = GridBagConstraints.RELATIVE;
        gbc.weightx   = 0.0;
        gbc.weighty   = 0.0;
        gbc.anchor    = GridBagConstraints.NORTHWEST;
        gbc.fill      = GridBagConstraints.NONE;
        gbc.insets    = new Insets(5,5,0,5);
        add(selectLabel, gbc);

        gbc = new GridBagConstraints();
        gbc.gridx     = GridBagConstraints.RELATIVE;
        gbc.gridy     = GridBagConstraints.RELATIVE;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.weightx   = 1.0;
        gbc.weighty   = 0.001;
        gbc.anchor    = GridBagConstraints.NORTHWEST;
        gbc.fill      = GridBagConstraints.HORIZONTAL;
        gbc.insets    = new Insets(5,5,0,5);
        add(_competitionComboBox, gbc);

        _okCancelPanel = new OkCancelPanel("Create");
        gbc = new GridBagConstraints();
        gbc.gridx     = GridBagConstraints.RELATIVE;
        gbc.gridy     = GridBagConstraints.RELATIVE;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.weightx   = 1.0;
        gbc.weighty   = 0.0;
        gbc.anchor    = GridBagConstraints.SOUTHEAST;
        gbc.fill      = GridBagConstraints.BOTH;
        gbc.insets    = new Insets(5,0,0,5);
        add(_okCancelPanel, gbc);
    }

    @Override
    public void setModified(boolean modified) {
        _isModified = modified;
    }

    @Override
    public boolean isModified() {
        return _isModified;
    }
}
