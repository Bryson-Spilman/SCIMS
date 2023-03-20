package scims.ui.swing;

import scims.model.data.*;
import scims.model.enums.StrongmanCorpWeightClasses;
import scims.model.enums.USSWeightClasses;
import scims.model.enums.UnitSystem;
import scims.ui.Modifiable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.function.Consumer;

public class NewCompetitionDialog extends JDialog implements Modifiable {
    private final Consumer<Competition> _createAction;
    private JRadioButton _prevSelectedUnitSystem;
    private JTextField _nameTextField;
    private JButton _createButton;
    private JButton _cancelButton;
    private boolean _isModified = false;
    private JRadioButton _strongmanCorpRadioButton;
    private JRadioButton _ussRadioButton;
    private JRadioButton _otherRadioButton;
    private ButtonGroup _sanctionRadioButtonGroup;
    private JRadioButton _prevRadioButton;
    private boolean _ignoreRadioChange;
    private EventsTable _eventsTable;
    private JCheckBox _sameEventsForAllWeightsClassesCheckbox;
    private WeightClassTable _weightClassTable;
    private OkCancelPanel _okCancelPanel;
    private JButton _createNewEventButton;
    private JButton _createNewWeightClassButton;
    private ButtonGroup _uniSystemButtonGroup;
    private JRadioButton _poundsRadioButton;
    private JRadioButton _kilosRadioButton;

    public NewCompetitionDialog(JFrame parentFrame, Consumer<Competition> createAction) {
        super(parentFrame, "New Competition", true);
        setLayout(new GridBagLayout());
        setSize(650,700);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        _createAction = createAction;
        // Initialize components
        buildComponents();
        _prevSelectedUnitSystem = _poundsRadioButton;
        addListeners();
    }

    private Competition buildCompetition()
    {
        //TODO
        return null;
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
        _strongmanCorpRadioButton.addActionListener(e -> sanctionRadioButtonChanged());
        _ussRadioButton.addActionListener(e -> sanctionRadioButtonChanged());
        _otherRadioButton.addActionListener(e -> sanctionRadioButtonChanged());
        _sameEventsForAllWeightsClassesCheckbox.addActionListener(e -> useSameEventsForAllCheckBoxClicked());
        _eventsTable.addNewEventSelectedAction(this::updatedEvents);
        _okCancelPanel.addOkActionListener(e -> createCompetitionClicked());
        _okCancelPanel.addCancelActionListener(e -> closeDialogClicked());
        _poundsRadioButton.addActionListener(e -> unitSystemChanged(e));
        _kilosRadioButton.addActionListener(e -> unitSystemChanged(e));
    }

    private void unitSystemChanged(ActionEvent e) {
        if(_prevSelectedUnitSystem != e.getSource() && _weightClassTable.hasMaxWeights()) {
            int opt = JOptionPane.showConfirmDialog(this, "Unit system changed. Would you like to apply a unit conversion to the max competitor weights?",
                    "Apply Unit Conversion", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if(opt == JOptionPane.YES_OPTION) {
                UnitSystem newUnitSystem = UnitSystem.POUNDS;
                UnitSystem oldUnitSystem = UnitSystem.KILOS;
                if(_kilosRadioButton.isSelected()) {
                    newUnitSystem = UnitSystem.KILOS;
                    oldUnitSystem = UnitSystem.POUNDS;
                }
                _weightClassTable.convertWeights(oldUnitSystem, newUnitSystem);
            }
            _prevSelectedUnitSystem = (JRadioButton) e.getSource();
        }
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
        updatedEvents();
    }

    private void updatedEvents() {
        _weightClassTable.setEvents(_eventsTable.getSelectedEvents());
        _weightClassTable.setUseSameEventsForAllWeightClasses(_sameEventsForAllWeightsClassesCheckbox.isSelected());
        _weightClassTable.commitEdits();
        _weightClassTable.requestFocus();
        _weightClassTable.repaint();
        requestFocus();
    }

    private void sanctionRadioButtonChanged() {
        if(!_ignoreRadioChange) {
            _isModified = true;
            String warningMsg = "Changing this will reset weight classes. Continue?";
            String warningTitle = "Confirm Weight Classes reset";
            if(_strongmanCorpRadioButton.isSelected()) {
                if(!emptyOrOnlyContainsStrongmanCorpWeightClasses()) {
                    int opt = JOptionPane.showConfirmDialog(this, warningMsg, warningTitle, JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                    if(opt == JOptionPane.YES_OPTION)
                    {
                        _weightClassTable.clear();
                        _weightClassTable.setWeightClasses(StrongmanCorpWeightClasses.getValues());
                        _prevRadioButton = _strongmanCorpRadioButton;
                    } else {
                        _ignoreRadioChange = true;
                        _sanctionRadioButtonGroup.setSelected(_prevRadioButton.getModel(), true);
                    }
                } else {
                    _weightClassTable.setWeightClasses(StrongmanCorpWeightClasses.getValues());
                }
            } else if (_ussRadioButton.isSelected()) {
                if(!emptyOrOnlyContainsUssWeightClasses()) {
                    int opt = JOptionPane.showConfirmDialog(this, warningMsg, warningTitle, JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                    if(opt == JOptionPane.YES_OPTION) {
                        _weightClassTable.clear();
                        _weightClassTable.setWeightClasses(USSWeightClasses.getValues());
                        _prevRadioButton = _ussRadioButton;
                    } else {
                        _ignoreRadioChange = true;
                        _sanctionRadioButtonGroup.setSelected(_prevRadioButton.getModel(), true);
                    }
                } else {
                    _weightClassTable.setWeightClasses(USSWeightClasses.getValues());
                }
            }
            else {
                _prevRadioButton = _otherRadioButton;
            }
            _ignoreRadioChange = false;
        }
    }

    private boolean emptyOrOnlyContainsStrongmanCorpWeightClasses() {
        boolean retVal = true;
        if(_weightClassTable.getModel().getRowCount() > 0)
        {
            List<WeightClass> weightClasses = _weightClassTable.getAllWeightClasses();
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
        if(_weightClassTable.getModel().getRowCount() > 0)
        {
            List<WeightClass> weightClasses = _weightClassTable.getAllWeightClasses();
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
        JLabel unitSystemLabel = new JLabel("Unit System:");
        _uniSystemButtonGroup = new ButtonGroup();
        _poundsRadioButton = new JRadioButton(UnitSystem.POUNDS.getDisplayName());
        _kilosRadioButton = new JRadioButton(UnitSystem.KILOS.getDisplayName());
        _uniSystemButtonGroup.add(_poundsRadioButton);
        _uniSystemButtonGroup.add(_kilosRadioButton);
        _uniSystemButtonGroup.setSelected(_poundsRadioButton.getModel(), true);
        JLabel typeLabel = new JLabel("Competiton Type:");
        _sanctionRadioButtonGroup = new ButtonGroup();
        _strongmanCorpRadioButton = new JRadioButton("Strongman Corporation");
        _ussRadioButton = new JRadioButton("USS");
        _otherRadioButton = new JRadioButton("Other");
        _sanctionRadioButtonGroup.add(_strongmanCorpRadioButton);
        _sanctionRadioButtonGroup.add(_ussRadioButton);
        _sanctionRadioButtonGroup.add(_otherRadioButton);
        _sanctionRadioButtonGroup.setSelected(_strongmanCorpRadioButton.getModel(), true);
        _sameEventsForAllWeightsClassesCheckbox = new JCheckBox("Use same events for all weight classes");
        _sameEventsForAllWeightsClassesCheckbox.setSelected(true);
        _createButton = new JButton("Create");
        _cancelButton = new JButton("Cancel");
        _eventsTable = new EventsTable();
        _weightClassTable = new WeightClassTable();
        _weightClassTable.setWeightClasses(StrongmanCorpWeightClasses.getValues());

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

        gbc = new GridBagConstraints();
        gbc.gridx     = GridBagConstraints.RELATIVE;
        gbc.gridy     = GridBagConstraints.RELATIVE;
        gbc.gridwidth = GridBagConstraints.RELATIVE;
        gbc.weightx   = 0.0;
        gbc.weighty   = 0.0;
        gbc.anchor    = GridBagConstraints.NORTHWEST;
        gbc.fill      = GridBagConstraints.NONE;
        gbc.insets    = new Insets(8,5,0,0);
        attributesPanel.add(typeLabel, gbc);

        JPanel sanctionRadioButtonPanel = new JPanel(new BorderLayout());
        sanctionRadioButtonPanel.add(_strongmanCorpRadioButton, BorderLayout.WEST);
        sanctionRadioButtonPanel.add(_ussRadioButton, BorderLayout.CENTER);
        sanctionRadioButtonPanel.add(_otherRadioButton, BorderLayout.EAST);
        gbc = new GridBagConstraints();
        gbc.gridx     = GridBagConstraints.RELATIVE;
        gbc.gridy     = GridBagConstraints.RELATIVE;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.weightx   = 0.001;
        gbc.weighty   = 0.0;
        gbc.anchor    = GridBagConstraints.NORTHWEST;
        gbc.fill      = GridBagConstraints.NONE;
        gbc.insets    = new Insets(5,-10,0,5);
        attributesPanel.add(sanctionRadioButtonPanel, gbc);

        JPanel unitSystemRadioButtonPanel = new JPanel(new BorderLayout());
        unitSystemRadioButtonPanel.add(unitSystemLabel, BorderLayout.WEST);
        unitSystemRadioButtonPanel.add(_poundsRadioButton, BorderLayout.CENTER);
        unitSystemRadioButtonPanel.add(_kilosRadioButton, BorderLayout.EAST);
        addComponent(attributesPanel, unitSystemRadioButtonPanel, GridBagConstraints.REMAINDER, GridBagConstraints.NONE);

        addComponent(attributesPanel, _sameEventsForAllWeightsClassesCheckbox, GridBagConstraints.REMAINDER, GridBagConstraints.NONE);

        JPanel titledEventsPanel = new JPanel(new BorderLayout());
        titledEventsPanel.setBorder(BorderFactory.createTitledBorder("Events"));
        JScrollPane eventsTableScrollPane = new JScrollPane(_eventsTable);
        eventsTableScrollPane.setMinimumSize(new Dimension(Integer.MAX_VALUE, 200)); // Set maximum height to 200 pixels
        eventsTableScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS); //

        _createNewEventButton = new JButton("Create New Event...");
        titledEventsPanel.add(_createNewEventButton, BorderLayout.WEST);
        JPanel newEventPanel = new JPanel(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx     = GridBagConstraints.RELATIVE;
        gbc.gridy     = GridBagConstraints.RELATIVE;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.weightx   = 0.001;
        gbc.weighty   = 0.001;
        gbc.anchor    = GridBagConstraints.NORTHWEST;
        gbc.fill      = GridBagConstraints.NONE;
        gbc.insets    = new Insets(5,0,0,5);
        newEventPanel.add(_createNewEventButton, gbc);

        titledEventsPanel.add(eventsTableScrollPane, BorderLayout.CENTER);
        titledEventsPanel.add(newEventPanel, BorderLayout.SOUTH);

        JPanel titledWeightClassPanel = new JPanel(new BorderLayout());
        titledWeightClassPanel.setBorder(BorderFactory.createTitledBorder("Weight Classes"));
        JScrollPane weightClassScrollPane = new JScrollPane(_weightClassTable);
        weightClassScrollPane.setMinimumSize(new Dimension(Integer.MAX_VALUE, 200)); // Set maximum height to 200 pixels
        weightClassScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS); //

        _createNewWeightClassButton = new JButton("Create New Weight Class...");
        JPanel newWeightClassPanel = new JPanel(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx     = GridBagConstraints.RELATIVE;
        gbc.gridy     = GridBagConstraints.RELATIVE;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.weightx   = 0.001;
        gbc.weighty   = 0.001;
        gbc.anchor    = GridBagConstraints.NORTHWEST;
        gbc.fill      = GridBagConstraints.NONE;
        gbc.insets    = new Insets(5,0,0,5);
        newWeightClassPanel.add(_createNewWeightClassButton, gbc);

        titledWeightClassPanel.add(weightClassScrollPane, BorderLayout.CENTER);
        titledWeightClassPanel.add(newWeightClassPanel, BorderLayout.SOUTH);

        gbc = new GridBagConstraints();
        gbc.gridx     = GridBagConstraints.RELATIVE;
        gbc.gridy     = GridBagConstraints.RELATIVE;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.weightx   = 0.001;
        gbc.weighty   = 0.5;
        gbc.anchor    = GridBagConstraints.NORTHWEST;
        gbc.fill      = GridBagConstraints.BOTH;
        gbc.insets    = new Insets(5,5,0,5);
        attributesPanel.add(titledEventsPanel, gbc);

        gbc = new GridBagConstraints();
        gbc.gridx     = GridBagConstraints.RELATIVE;
        gbc.gridy     = GridBagConstraints.RELATIVE;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.weightx   = 0.001;
        gbc.weighty   = 0.5;
        gbc.anchor    = GridBagConstraints.NORTHWEST;
        gbc.fill      = GridBagConstraints.BOTH;
        gbc.insets    = new Insets(5,5,0,5);
        attributesPanel.add(titledWeightClassPanel, gbc);
        _okCancelPanel = new OkCancelPanel("Create");

        gbc = new GridBagConstraints();
        gbc.gridx     = GridBagConstraints.RELATIVE;
        gbc.gridy     = GridBagConstraints.RELATIVE;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.weightx   = 0.001;
        gbc.weighty   = 1.0;
        gbc.anchor    = GridBagConstraints.NORTHWEST;
        gbc.fill      = GridBagConstraints.BOTH;
        gbc.insets    = new Insets(5,5,0,5);
        add(attributesPanel, gbc);

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

    private void addComponent(Container parentComponent, JComponent componentToAdd, int gridWidth, int fill)
    {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx     = GridBagConstraints.RELATIVE;
        gbc.gridy     = GridBagConstraints.RELATIVE;
        gbc.gridwidth = gridWidth;
        gbc.weightx   = 0.001;
        gbc.weighty   = 0.0;
        gbc.anchor    = GridBagConstraints.NORTHWEST;
        gbc.fill      = fill;
        gbc.insets    = new Insets(5,5,0,5);
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
