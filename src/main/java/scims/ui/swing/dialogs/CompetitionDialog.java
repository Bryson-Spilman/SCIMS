package scims.ui.swing.dialogs;

import scims.main.CustomEventClassRegistry;
import scims.main.CustomWeightClassRegistry;
import scims.model.data.*;
import scims.model.data.Event;
import scims.model.enums.DistanceUnitSystem;
import scims.model.enums.StrongmanCorpWeightClasses;
import scims.model.enums.USSWeightClasses;
import scims.model.enums.WeightUnitSystem;
import scims.ui.Modifiable;
import scims.ui.swing.DateTimeTextField;
import scims.ui.swing.MissingRequiredValueException;
import scims.ui.swing.OkCancelPanel;
import scims.ui.swing.tables.EventsTable;
import scims.ui.swing.tables.WeightClassTable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.ZonedDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.function.Consumer;

public class CompetitionDialog extends JDialog implements Modifiable {
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
    private JRadioButton _poundsRadioButton;
    private JRadioButton _kilosRadioButton;
    private JRadioButton _feetRadioButton;
    private JRadioButton _metersRadioButton;
    private DateTimeTextField _dateTimeTextField;
    private boolean _ignoreSameEventsCheckBoxAction = false;
    private boolean _ignoreEventsChanged = false;
    private JScrollPane _eventsTableScrollPane;
    private JScrollPane _weightClassScrollPane;

    public CompetitionDialog(JFrame parentFrame, Consumer<Competition> createAction) {
        super(parentFrame, "New Competition", true);
        setLayout(new GridBagLayout());
        setSize(650,700);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(parentFrame);
        _createAction = createAction;
        // Initialize components
        buildComponents();
        _createNewWeightClassButton.setEnabled(false);
        initializeTables();
        _prevSelectedUnitSystem = _poundsRadioButton;
        addListeners();
    }

    private void initializeTables() {
        for(Event event : CustomEventClassRegistry.getInstance().getEvents()) {
            _eventsTable.addEvent(event);
            _eventsTable.deselectAll();
        }
    }

    private Competition buildCompetition() throws MissingRequiredValueException, DateTimeParseException {
        String name = _nameTextField.getText();
        if(name == null || name.trim().isEmpty()) {
            throw new MissingRequiredValueException("Name");
        }
        ZonedDateTime dateTime = null;
        if(_dateTimeTextField.getText() != null &&  !_dateTimeTextField.getText().trim().isEmpty()) {
            dateTime = _dateTimeTextField.getZonedDateTime();
        }
        return new StrengthCompetitionBuilder()
                .withName(name)
                .withDateTime(dateTime)
                .withIsSameNumberOfEventsForAllWeightClasses(_sameEventsForAllWeightsClassesCheckbox.isSelected())
                .withWeightClasses(_weightClassTable.getSelectedWeightClasses())
                .withWeightUnitSystem(_poundsRadioButton.isSelected() ? WeightUnitSystem.POUNDS : WeightUnitSystem.KILOS)
                .withDistanceUnitSystem(_feetRadioButton.isSelected() ? DistanceUnitSystem.FEET : DistanceUnitSystem.METERS)
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
        _strongmanCorpRadioButton.addActionListener(e -> sanctionRadioButtonChanged());
        _ussRadioButton.addActionListener(e -> sanctionRadioButtonChanged());
        _otherRadioButton.addActionListener(e -> sanctionRadioButtonChanged());
        _sameEventsForAllWeightsClassesCheckbox.addActionListener(e -> useSameEventsForAllCheckBoxClicked());
        _eventsTable.addNewEventSelectedAction(this::updatedEvents);
        _eventsTable.addOrderChangedAction(this::orderChanged);
        _okCancelPanel.addOkActionListener(e -> createCompetitionClicked());
        _okCancelPanel.addCancelActionListener(e -> closeDialogClicked());
        _poundsRadioButton.addActionListener(this::weightUnitSystemChanged);
        _kilosRadioButton.addActionListener(this::weightUnitSystemChanged);
        _feetRadioButton.addActionListener(this::distanceUnitSystemChanged);
        _metersRadioButton.addActionListener(this::distanceUnitSystemChanged);
        _createNewEventButton.addActionListener(e -> newEventButtonAction());
        _createNewWeightClassButton.addActionListener(e -> newWeightClassButtonAction());
    }

    public void fillPanel(Competition competition) {
        _nameTextField.setText(competition.getName());
        ZonedDateTime zonedDateTime = competition.getDateTime();
        if(zonedDateTime != null) {
            String dateTime = zonedDateTime.format(DateTimeTextField.DATE_FORMATTER);
            if(competition.getDateTime().toString().contains(":"))
            {
                dateTime = competition.getDateTime().format(DateTimeTextField.DATE_TIME_FORMATTER);
            }
            _dateTimeTextField.setText(dateTime);
        }
        _feetRadioButton.setSelected(competition.getDistanceUnitSystem() == DistanceUnitSystem.FEET);
        _metersRadioButton.setSelected(competition.getDistanceUnitSystem() == DistanceUnitSystem.METERS);
        _poundsRadioButton.setSelected(competition.getWeightUnitSystem() == WeightUnitSystem.POUNDS);
        _poundsRadioButton.setSelected(competition.getWeightUnitSystem() == WeightUnitSystem.KILOS);
        _okCancelPanel.setOkText("Update");
        _otherRadioButton.setSelected(true);
        _sameEventsForAllWeightsClassesCheckbox.setSelected(competition.isSameNumberOfEventsForAllWeightClasses());
        List<StrengthWeightClass> weightClasses = competition.getWeightClasses();
        for(WeightClass wc : weightClasses) {
            List<StrengthEvent> events = wc.getEventsInOrder();
            _weightClassTable.setSelectedEvents(events);
            _eventsTable.setSelectedEvents(events);
        }
        _weightClassTable.setSelectedWeightClasses(weightClasses);
        if(competition.isSameNumberOfEventsForAllWeightClasses() && !weightClasses.isEmpty()) {
            _eventsTable.setEventOrderColumnEnabled(true);
            _eventsTable.setOrdersByListOrder(weightClasses.get(0).getEventsInOrder());
        }
        _strongmanCorpRadioButton.setSelected(containsOnlyStrongmanCorpWeightClasses(weightClasses));
        _ussRadioButton.setSelected(containsOnlyUSSWeightClasses(weightClasses));
        sanctionRadioButtonChanged();
    }

    private void orderChanged() {
        if(!_ignoreEventsChanged) {
            _ignoreEventsChanged = true;
            _eventsTable.commitEdits();
        }
        _ignoreEventsChanged = false;
        updatedEvents();

    }

    private void distanceUnitSystemChanged(ActionEvent actionEvent) {
        DistanceUnitSystem selected = DistanceUnitSystem.FEET;
        if(_metersRadioButton.isSelected()) {
            selected = DistanceUnitSystem.METERS;
        }
        _eventsTable.applyDistanceUnitsChange(selected);
    }

    private void newEventButtonAction() {
        EventDialog dlg = new EventDialog(this, this::addEvent);
        dlg.setVisible(true);
    }

    private void addEvent(Event event) {
        _eventsTable.addEvent(event);
        updatedEvents();
        SwingUtilities.invokeLater(() -> {
            JViewport scrollViewPort = _eventsTableScrollPane.getViewport();
            scrollViewPort.setViewPosition(new Point(0, scrollViewPort.getViewSize().height));
        });
    }

    private void newWeightClassButtonAction() {
        WeightClassDialog dlg = new WeightClassDialog(this, this::addWeightClass);
        dlg.setVisible(true);
    }

    private void addWeightClass(WeightClass weightClass) {
        if(_sameEventsForAllWeightsClassesCheckbox.isSelected()) {
            weightClass = new StrengthWeightClassBuilder()
                    .fromExistingWeightClass(weightClass)
                    .withUpdatedEvents(_eventsTable.getSelectedEvents())
                    .build();
        }
        _weightClassTable.addWeightClass(weightClass);
        SwingUtilities.invokeLater(() -> {
            JViewport scrollViewPort = _weightClassScrollPane.getViewport();
            scrollViewPort.setViewPosition(new Point(0, scrollViewPort.getViewSize().height));
        });

    }

    private void weightUnitSystemChanged(ActionEvent e) {
        if(_prevSelectedUnitSystem != e.getSource() && _weightClassTable.hasMaxWeights()) {
            int opt = JOptionPane.showConfirmDialog(this, "Unit system changed. Would you like to apply a unit conversion to the max competitor weights?",
                    "Apply Unit Conversion", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if(opt == JOptionPane.YES_OPTION) {
                WeightUnitSystem newUnitSystem = WeightUnitSystem.POUNDS;
                WeightUnitSystem oldUnitSystem = WeightUnitSystem.KILOS;
                if(_kilosRadioButton.isSelected()) {
                    newUnitSystem = WeightUnitSystem.KILOS;
                    oldUnitSystem = WeightUnitSystem.POUNDS;
                }
                _weightClassTable.convertWeights(oldUnitSystem, newUnitSystem);
            }
            _prevSelectedUnitSystem = (JRadioButton) e.getSource();
        }
        WeightUnitSystem selected = WeightUnitSystem.POUNDS;
        if(_kilosRadioButton.isSelected()) {
            selected = WeightUnitSystem.KILOS;
        }
        _eventsTable.applyWeightUnitsChange(selected);
    }

    private void useSameEventsForAllCheckBoxClicked() {
        _isModified = true;
        if(!_ignoreSameEventsCheckBoxAction) {
            if(_eventsTable.hasOrdersSet()) {
                int opt = JOptionPane.showConfirmDialog(this, "Selecting this will reset any event orders set in the events table. Continue?",
                        "Confirm Reset Events Orders", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                if (opt == JOptionPane.YES_OPTION) {
                    _eventsTable.resetEventOrders();
                } else {
                    _ignoreSameEventsCheckBoxAction = true;
                    _sameEventsForAllWeightsClassesCheckbox.setSelected(!_sameEventsForAllWeightsClassesCheckbox.isSelected());
                    _ignoreSameEventsCheckBoxAction = false;
                }
            }
            _eventsTable.setEventOrderColumnEnabled(_sameEventsForAllWeightsClassesCheckbox.isSelected());
            updatedEvents();
        }
    }

    private void updatedEvents() {
        if(!_ignoreEventsChanged) {
            _weightClassTable.setAvailableEvents(_eventsTable.getSelectedEvents());
            if(_sameEventsForAllWeightsClassesCheckbox.isSelected()) {
                _weightClassTable.setSelectedEvents(_eventsTable.getSelectedEvents());
            }
            _weightClassTable.setUseSameEventsForAllWeightClasses(_sameEventsForAllWeightsClassesCheckbox.isSelected());
            _weightClassTable.commitEdits();
            _weightClassTable.requestFocus();
            _weightClassTable.repaint();
        }
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
                        if(_kilosRadioButton.isSelected()) {
                            _weightClassTable.convertWeights(WeightUnitSystem.POUNDS, WeightUnitSystem.KILOS);
                        }
                        _prevRadioButton = _strongmanCorpRadioButton;
                    } else {
                        _ignoreRadioChange = true;
                        _sanctionRadioButtonGroup.setSelected(_prevRadioButton.getModel(), true);
                    }
                } else {
                    _weightClassTable.setWeightClasses(StrongmanCorpWeightClasses.getValues());
                    if(_kilosRadioButton.isSelected()) {
                        _weightClassTable.convertWeights(WeightUnitSystem.POUNDS, WeightUnitSystem.KILOS);
                    }
                }
            } else if (_ussRadioButton.isSelected()) {
                if(!emptyOrOnlyContainsUssWeightClasses()) {
                    int opt = JOptionPane.showConfirmDialog(this, warningMsg, warningTitle, JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                    if(opt == JOptionPane.YES_OPTION) {
                        _weightClassTable.clear();
                        _weightClassTable.setWeightClasses(USSWeightClasses.getValues());
                        if(_kilosRadioButton.isSelected()) {
                            _weightClassTable.convertWeights(WeightUnitSystem.POUNDS, WeightUnitSystem.KILOS);
                        }
                        _prevRadioButton = _ussRadioButton;
                    } else {
                        _ignoreRadioChange = true;
                        _sanctionRadioButtonGroup.setSelected(_prevRadioButton.getModel(), true);
                    }
                } else {
                    _weightClassTable.setWeightClasses(USSWeightClasses.getValues());
                    if(_kilosRadioButton.isSelected()) {
                        _weightClassTable.convertWeights(WeightUnitSystem.POUNDS, WeightUnitSystem.KILOS);
                    }
                }
            }
            else {
                _prevRadioButton = _otherRadioButton;
                for(WeightClass wc : CustomWeightClassRegistry.getInstance().getWeightClasses()) {
                    if(!_weightClassTable.containsWeightClass(wc)) {
                        _weightClassTable.addWeightClass(wc);
                    }
                }
            }
            _ignoreRadioChange = false;
        }
        _createNewWeightClassButton.setEnabled(_otherRadioButton.isSelected());
    }

    private boolean emptyOrOnlyContainsStrongmanCorpWeightClasses() {
        boolean retVal = true;
        if(_weightClassTable.getModel().getRowCount() > 0)
        {
           retVal = containsOnlyStrongmanCorpWeightClasses(_weightClassTable.getAllWeightClasses());
        }
        return retVal;
    }

    private boolean containsOnlyStrongmanCorpWeightClasses(List<StrengthWeightClass> weightClasses) {
        boolean retVal = true;
        for(WeightClass wc : weightClasses)
        {
            if(!StrongmanCorpWeightClasses.getValues().contains(wc))
            {
                retVal = false;
                break;
            }
        }
        return retVal;
    }

    private boolean emptyOrOnlyContainsUssWeightClasses() {
        boolean retVal = true;
        if(_weightClassTable.getModel().getRowCount() > 0)
        {
            List<StrengthWeightClass> weightClasses = _weightClassTable.getAllWeightClasses();
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

    private boolean containsOnlyUSSWeightClasses(List<StrengthWeightClass> weightClasses) {
        boolean retVal = true;
        for(WeightClass wc : weightClasses)
        {
            if(!USSWeightClasses.getValues().contains(wc))
            {
                retVal = false;
                break;
            }
        }
        return retVal;
    }

    private void createCompetitionClicked() {
        try {
            _createAction.accept(buildCompetition());
            dispose();
        } catch (MissingRequiredValueException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Invalid Competition",
                    JOptionPane.ERROR_MESSAGE);
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this, "<html>Invalid Competition Date! Date should be of form MM-dd-yyyy,<br>" +
                            "and Time should be of form HH:mm (AM/PM)<html>", "Invalid Competition", JOptionPane.ERROR_MESSAGE);
        }

    }

    private void closeDialogClicked()
    {
        int opt = JOptionPane.YES_OPTION;
        if(_isModified)
        {
            opt = JOptionPane.showConfirmDialog(this, "Cancel "  + _okCancelPanel.getOkText() + " of competition?", "Confirm Cancel",
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
        JLabel dateTimeLabel = new JLabel("Date & Time:");
        _dateTimeTextField = new DateTimeTextField();
        JLabel weightUnitSystemLabel = new JLabel("Weight Unit System:");
        ButtonGroup weightUnitSystemButtonGroup = new ButtonGroup();
        _poundsRadioButton = new JRadioButton(WeightUnitSystem.POUNDS.getDisplayName());
        _kilosRadioButton = new JRadioButton(WeightUnitSystem.KILOS.getDisplayName());
        weightUnitSystemButtonGroup.add(_poundsRadioButton);
        weightUnitSystemButtonGroup.add(_kilosRadioButton);
        weightUnitSystemButtonGroup.setSelected(_poundsRadioButton.getModel(), true);
        JLabel distanceUnitSystemLabel = new JLabel("Distance Unit System:");
        ButtonGroup distanceUnitSystemButtonGroup = new ButtonGroup();
        _feetRadioButton = new JRadioButton(DistanceUnitSystem.FEET.getDisplayName());
        _metersRadioButton = new JRadioButton(DistanceUnitSystem.METERS.getDisplayName());
        distanceUnitSystemButtonGroup.add(_feetRadioButton);
        distanceUnitSystemButtonGroup.add(_metersRadioButton);
        distanceUnitSystemButtonGroup.setSelected(_feetRadioButton.getModel(), true);
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
        gbc.insets    = new Insets(5,5,0,5);
        attributesPanel.add(dateTimeLabel, gbc);

        gbc = new GridBagConstraints();
        gbc.gridx     = GridBagConstraints.RELATIVE;
        gbc.gridy     = GridBagConstraints.RELATIVE;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.weightx   = 0.0;
        gbc.weighty   = 0.0;
        gbc.anchor    = GridBagConstraints.NORTHWEST;
        gbc.fill      = GridBagConstraints.HORIZONTAL;
        gbc.insets    = new Insets(5,5,0,5);
        _dateTimeTextField.setColumns(10);
        attributesPanel.add(_dateTimeTextField, gbc);

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

        JPanel weightUnitSystemRadioButtonPanel = new JPanel(new BorderLayout());
        weightUnitSystemRadioButtonPanel.add(weightUnitSystemLabel, BorderLayout.WEST);
        weightUnitSystemRadioButtonPanel.add(_poundsRadioButton, BorderLayout.CENTER);
        weightUnitSystemRadioButtonPanel.add(_kilosRadioButton, BorderLayout.EAST);
        addComponent(attributesPanel, weightUnitSystemRadioButtonPanel);

        JPanel distanceUnitSystemRadioButtonPanel = new JPanel(new BorderLayout());
        distanceUnitSystemRadioButtonPanel.add(distanceUnitSystemLabel, BorderLayout.WEST);
        distanceUnitSystemRadioButtonPanel.add(_feetRadioButton, BorderLayout.CENTER);
        distanceUnitSystemRadioButtonPanel.add(_metersRadioButton, BorderLayout.EAST);
        addComponent(attributesPanel, distanceUnitSystemRadioButtonPanel);

        addComponent(attributesPanel, _sameEventsForAllWeightsClassesCheckbox);

        JPanel titledEventsPanel = new JPanel(new BorderLayout());
        titledEventsPanel.setBorder(BorderFactory.createTitledBorder("Events"));
        _eventsTableScrollPane = new JScrollPane(_eventsTable);
        _eventsTableScrollPane.setMinimumSize(new Dimension(Integer.MAX_VALUE, 200)); // Set maximum height to 200 pixels
        _eventsTableScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS); //

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

        titledEventsPanel.add(_eventsTableScrollPane, BorderLayout.CENTER);
        titledEventsPanel.add(newEventPanel, BorderLayout.SOUTH);

        JPanel titledWeightClassPanel = new JPanel(new BorderLayout());
        titledWeightClassPanel.setBorder(BorderFactory.createTitledBorder("Weight Classes"));
        _weightClassScrollPane = new JScrollPane(_weightClassTable);
        _weightClassScrollPane.setMinimumSize(new Dimension(Integer.MAX_VALUE, 200)); // Set maximum height to 200 pixels
        _weightClassScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS); //

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

        titledWeightClassPanel.add(_weightClassScrollPane, BorderLayout.CENTER);
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

    private void addComponent(Container parentComponent, JComponent componentToAdd)
    {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx     = GridBagConstraints.RELATIVE;
        gbc.gridy     = GridBagConstraints.RELATIVE;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.weightx   = 0.001;
        gbc.weighty   = 0.0;
        gbc.anchor    = GridBagConstraints.NORTHWEST;
        gbc.fill      = GridBagConstraints.NONE;
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
