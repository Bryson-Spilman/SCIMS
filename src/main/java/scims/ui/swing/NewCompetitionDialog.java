package scims.ui.swing;

import scims.model.data.*;
import scims.model.enums.StrongmanCorpWeightClasses;
import scims.model.enums.USSWeightClasses;
import scims.model.enums.UnitSystem;
import scims.ui.Modifiable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.function.Consumer;

public class NewCompetitionDialog extends JDialog implements Modifiable {
    private final Consumer<Competition> _createAction;
    private JTextField _nameTextField;
    private JButton _createButton;
    private JButton _cancelButton;
    private boolean _isModified = false;
    private JChooserField<WeightClass> _weightClassChooserField;
    private JRadioButton _strongmanCorpRadioButton;
    private JRadioButton _ussRadioButton;
    private JRadioButton _otherRadioButton;
    private ButtonGroup _radioButtonGroup;
    private JRadioButton _prevRadioButton;
    private boolean _ignoreRadioChange;
    private EventsTable _eventsTable;
    private JCheckBox _sameEventsForAllWeightsClassesCheckbox;

    public NewCompetitionDialog(JFrame parentFrame, Consumer<Competition> createAction) {
        super(parentFrame, "New Competition", true);
        setLayout(new BorderLayout());
        setSize(500,500);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        _createAction = createAction;
        // Initialize components
        buildComponents();
        addListeners();
    }

    private Competition buildCompetition()
    {
        Competitor competitor = new StrengthCompetitorBuilder()
                .withCompetitorName("Bryson Spilman")
                .withCompetitorAge(27)
                .withCompetitorWeight(199.8)
                .build();
        Competitor competitor2 = new StrengthCompetitorBuilder()
                .withCompetitorName("Amanda Reynolds")
                .withCompetitorAge(25)
                .withCompetitorWeight(115)
                .build();
        List<WeightClass> weightClasses = _weightClassChooserField.getObjects();
        for(WeightClass weightClass : weightClasses) {
            weightClass.addCompetitor(competitor);
            weightClass.addCompetitor(competitor2);
        }
        return new StrengthCompetitionBuilder()
                .withName(_nameTextField.getText())
                .withDateTime(ZonedDateTime.now())
                .withIsSameNumberOfEventsForAllWeightClasses(true)
                .withWeightClasses(weightClasses)
                .withUnitSystem(UnitSystem.POUNDS)
                .build();
    }

    private void addListeners() {

        _createButton.addActionListener(e -> createCompetitionClicked());
        _cancelButton.addActionListener(e -> closeDialogClicked());
        _nameTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                _isModified = true;
            }
        });
        addWindowListener( new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                closeDialogClicked();
            }
        });
        _strongmanCorpRadioButton.addActionListener(e -> radioButtonChanged());
        _ussRadioButton.addActionListener(e -> radioButtonChanged());
        _otherRadioButton.addActionListener(e -> radioButtonChanged());
        _weightClassChooserField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                _isModified = true;
            }
        });
        _sameEventsForAllWeightsClassesCheckbox.addActionListener(e -> useSameEventsForAllCheckBoxClicked());
    }

    private void useSameEventsForAllCheckBoxClicked() {
        _isModified = true;
        if(_eventsTable.hasOrdersSet()) {
            int opt = JOptionPane.showConfirmDialog(this, "Selecting this will reset any event orders set in the events table. Continue?",
                    "Confirm Reset Events Orders", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (opt == JOptionPane.YES_OPTION)
            {
                _eventsTable.resetEventOrders();
            }
        }
        ((EventsTableModel)_eventsTable.getModel()).setEventOrderColumnEnabled(_sameEventsForAllWeightsClassesCheckbox.isSelected());
    }

    private void radioButtonChanged() {
        if(!_ignoreRadioChange) {
            _isModified = true;
            String warningMsg = "Changing this will reset weight classes. Continue?";
            String warningTitle = "Confirm Weight Classes reset";
            if(_strongmanCorpRadioButton.isSelected()) {
                if(!emptyOrOnlyContainsStrongmanCorpWeightClasses()) {
                    int opt = JOptionPane.showConfirmDialog(this, warningMsg, warningTitle, JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                    if(opt == JOptionPane.YES_OPTION)
                    {
                        _weightClassChooserField.reset();
                        _weightClassChooserField.setObjects(StrongmanCorpWeightClasses.getValues());
                        _prevRadioButton = _strongmanCorpRadioButton;
                    } else {
                        _ignoreRadioChange = true;
                        _radioButtonGroup.setSelected(_prevRadioButton.getModel(), true);
                    }
                } else {
                    _weightClassChooserField.setObjects(StrongmanCorpWeightClasses.getValues());
                }
            } else if (_ussRadioButton.isSelected()) {
                if(!emptyOrOnlyContainsUssWeightClasses()) {
                    int opt = JOptionPane.showConfirmDialog(this, warningMsg, warningTitle, JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                    if(opt == JOptionPane.YES_OPTION) {
                        _weightClassChooserField.reset();
                        _weightClassChooserField.setObjects(USSWeightClasses.getValues());
                        _prevRadioButton = _ussRadioButton;
                    } else {
                        _ignoreRadioChange = true;
                        _radioButtonGroup.setSelected(_prevRadioButton.getModel(), true);
                    }
                } else {
                    _weightClassChooserField.setObjects(USSWeightClasses.getValues());
                }
            }
            else {
                if(_weightClassChooserField.getText().isEmpty()) {
                    _weightClassChooserField.reset();
                }
                _prevRadioButton = _otherRadioButton;
            }
            _ignoreRadioChange = false;
        }
    }

    private boolean emptyOrOnlyContainsStrongmanCorpWeightClasses() {
        boolean retVal = true;
        if(!_weightClassChooserField.getText().isEmpty())
        {
            List<WeightClass> weightClasses = _weightClassChooserField.getObjects();
            for(WeightClass wc : weightClasses)
            {
                if(!StrongmanCorpWeightClasses.getValues().contains(wc))
                {
                    retVal = false;
                    break;
                }
            }
        }
        return retVal;
    }

    private boolean emptyOrOnlyContainsUssWeightClasses() {
        boolean retVal = true;
        if(!_weightClassChooserField.getText().isEmpty())
        {
            List<WeightClass> weightClasses = _weightClassChooserField.getObjects();
            for(WeightClass wc : weightClasses)
            {
                if(!USSWeightClasses.getValues().contains(wc))
                {
                    retVal = false;
                    break;
                }
            }
        }
        return retVal;
    }

    private void createCompetitionClicked() {
        _createAction.accept(buildCompetition());
        dispose();
    }

    private void closeDialogClicked()
    {
        int opt = JOptionPane.YES_OPTION;
        if(_isModified)
        {
            opt = JOptionPane.showConfirmDialog(this, "Cancel creation of competition?", "Confirm Cancel",
                    JOptionPane.YES_NO_OPTION);
        }
        if(opt == JOptionPane.YES_OPTION)
        {
            dispose();
        }
    }

    private void buildComponents()
    {
        JLabel nameLabel = new JLabel("Competition Name:");
        _nameTextField = new JTextField();
        _radioButtonGroup = new ButtonGroup();
        _strongmanCorpRadioButton = new JRadioButton("Strongman Corporation");
        _ussRadioButton = new JRadioButton("USS");
        _otherRadioButton = new JRadioButton("Other");
        _radioButtonGroup.add(_strongmanCorpRadioButton);
        _radioButtonGroup.add(_ussRadioButton);
        _radioButtonGroup.add(_otherRadioButton);
        _radioButtonGroup.setSelected(_strongmanCorpRadioButton.getModel(), true);
        _sameEventsForAllWeightsClassesCheckbox = new JCheckBox("Use same events for all weight classes");
        JLabel selectWeightClassesLabel = new JLabel("Select Weight Classes:");
        _weightClassChooserField = new JChooserField<>(this, StrongmanCorpWeightClasses.getValues());
        _createButton = new JButton("Create");
        _cancelButton = new JButton("Cancel");
        _eventsTable = new EventsTable();

        JPanel attributesPanel = new JPanel(new GridBagLayout());
        JPanel createCancelPanel = new JPanel(new GridBagLayout());
        // Set GridBagConstraints for the label and text field
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx     = GridBagConstraints.RELATIVE;
        gbc.gridy     = GridBagConstraints.RELATIVE;
        gbc.gridwidth = GridBagConstraints.RELATIVE;
        gbc.weightx   = 0.0;
        gbc.weighty   = 0.0;
        gbc.anchor    = GridBagConstraints.NORTHWEST;
        gbc.fill      = GridBagConstraints.NONE;
        gbc.insets    = new Insets(5,5,0,5);
        attributesPanel.add(nameLabel, gbc);

        gbc = new GridBagConstraints();
        gbc.gridx     = GridBagConstraints.RELATIVE;
        gbc.gridy     = GridBagConstraints.RELATIVE;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.weightx   = 0.001;
        gbc.weighty   = 0.001;
        gbc.anchor    = GridBagConstraints.NORTHWEST;
        gbc.fill      = GridBagConstraints.HORIZONTAL;
        gbc.insets    = new Insets(5,5,0,5);
        attributesPanel.add(_nameTextField, gbc);

        JPanel radioButtonPanel = new JPanel(new BorderLayout());
        radioButtonPanel.add(_strongmanCorpRadioButton, BorderLayout.WEST);
        radioButtonPanel.add(_ussRadioButton, BorderLayout.CENTER);
        radioButtonPanel.add(_otherRadioButton, BorderLayout.EAST);
        addComponent(attributesPanel, radioButtonPanel, GridBagConstraints.REMAINDER, GridBagConstraints.NONE);

        addComponent(attributesPanel, _sameEventsForAllWeightsClassesCheckbox, GridBagConstraints.REMAINDER, GridBagConstraints.NONE);

        JPanel titledEventsPanel = new JPanel(new BorderLayout());
        titledEventsPanel.setBorder(BorderFactory.createTitledBorder("Events"));
        JScrollPane scrollPane = new JScrollPane(_eventsTable);
        titledEventsPanel.add(scrollPane, BorderLayout.CENTER);

        gbc = new GridBagConstraints();
        gbc.gridx     = GridBagConstraints.RELATIVE;
        gbc.gridy     = GridBagConstraints.RELATIVE;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.weightx   = 0.001;
        gbc.weighty   = 0.001;
        gbc.anchor    = GridBagConstraints.NORTHWEST;
        gbc.fill      = GridBagConstraints.BOTH;
        gbc.insets    = new Insets(5,5,0,5);
        attributesPanel.add(titledEventsPanel, gbc);

        gbc = new GridBagConstraints();
        gbc.gridx     = GridBagConstraints.RELATIVE;
        gbc.gridy     = GridBagConstraints.RELATIVE;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.weightx   = 0.001;
        gbc.weighty   = 0.001;
        gbc.anchor    = GridBagConstraints.NORTHWEST;
        gbc.fill      = GridBagConstraints.HORIZONTAL;
        gbc.insets    = new Insets(5,5,0,5);
        //attributesPanel.add(_weightClassChooserField, gbc);

        gbc = new GridBagConstraints();
        gbc.gridx     = GridBagConstraints.RELATIVE;
        gbc.gridy     = GridBagConstraints.RELATIVE;
        gbc.gridwidth = GridBagConstraints.RELATIVE;
        gbc.weightx   = 0.001;
        gbc.weighty   = 0.001;
        gbc.anchor    = GridBagConstraints.SOUTHEAST;
        gbc.fill      = GridBagConstraints.NONE;
        gbc.insets    = new Insets(5,5,5,5);
        createCancelPanel.add(_createButton, gbc);

        gbc = new GridBagConstraints();
        gbc.gridx     = GridBagConstraints.RELATIVE;
        gbc.gridy     = GridBagConstraints.RELATIVE;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.weightx   = 0.0;
        gbc.weighty   = 0.0;
        gbc.anchor    = GridBagConstraints.SOUTHEAST;
        gbc.fill      = GridBagConstraints.NONE;
        gbc.insets    = new Insets(5,5,5,5);
        createCancelPanel.add(_cancelButton, gbc);

        add(attributesPanel, BorderLayout.NORTH);
        add(createCancelPanel, BorderLayout.SOUTH);
    }

    private void addComponent(JComponent parentComponent, JComponent componentToAdd, int gridWidth, int fill)
    {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx     = GridBagConstraints.RELATIVE;
        gbc.gridy     = GridBagConstraints.RELATIVE;
        gbc.gridwidth = gridWidth;
        gbc.weightx   = 0.001;
        gbc.weighty   = 0.0;
        gbc.anchor    = GridBagConstraints.NORTHWEST;
        gbc.fill      = fill;
        gbc.insets    = new Insets(5,0,0,5);
        parentComponent.add(componentToAdd, gbc);
    }

    @Override
    public void setModified(boolean modified) {
        _isModified = true;
    }

    @Override
    public boolean isModified() {
        return _isModified;
    }
}
