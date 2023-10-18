package scims.ui.swing.dialogs;

import jdk.nashorn.internal.scripts.JO;
import scims.main.CustomEventClassRegistry;
import scims.main.SCIMS;
import scims.model.data.CompetitionObjectMapper;
import scims.model.data.Event;
import scims.model.data.StrengthEvent;
import scims.model.data.StrengthEventBuilder;
import scims.model.data.scoring.CustomEventScoring;
import scims.model.data.scoring.CustomScore;
import scims.model.data.scoring.EventScoring;
import scims.model.data.scoring.ScoringFactory;
import scims.ui.Modifiable;
import scims.ui.swing.MissingRequiredValueException;
import scims.ui.swing.OkCancelPanel;
import scims.ui.swing.tablecells.DoubleDocumentFilter;
import scims.ui.swing.tables.EventsRowData;

import javax.swing.*;
import javax.swing.text.AbstractDocument;
import java.awt.*;
import java.awt.event.*;
import java.time.Duration;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class EventDialog extends JDialog implements Modifiable {

    private static final String NO_TIE_BREAKER = "NO_TIE_BREAKER";
    private static final String HAS_TIE_BREAKER = "HAS_TIE_BREAKER";
    private static final String NO_SECOND_TIE_BREAKER = "NO_SECOND_TIE_BREAKER";
    private static final String HAS_SECOND_TIE_BREAKER = "HAS_SECOND_TIE_BREAKER";
    private final Consumer<Event> _createAction;
    private final List<StrengthEvent> _existingEvents;
    private boolean _isModified;
    private JTextField _nameTextField;
    private JTextField _timeLimitTextField;
    private  JComboBox<EventScoring<?>> _primaryScoringComboBox;
    private JComboBox<EventScoring<?>> _secondaryScoringComboBox;
    private JComboBox<EventScoring<?>> _thirdScoringComboBox;
    private JCheckBox _hasTieBreakerScoring;
    private OkCancelPanel _okCancelPanel;
    private JPanel _secondaryScoringCardPanel;
    private JCheckBox _hasSecondTieBreakerScoring;
    private JPanel _thirdScoringComboPanel;
    private String _cancelType;

    public EventDialog(Window parent, Consumer<Event> createAction) {
        super(parent, "New Event");
        _cancelType = "creation";
        setModal(true);
        setLayout(new GridBagLayout());
        setSize(400,300);
        setMinimumSize(new Dimension(Integer.MIN_VALUE, 175));
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(parent);
        _createAction = createAction;
        _existingEvents = CompetitionObjectMapper.deserializeIntoList(SCIMS.getEventsFile(), StrengthEvent.class);
        // Initialize components
        buildComponents();
        addListeners();
    }

    private void addListeners() {
        _nameTextField.addKeyListener(getModifiedKeyListener());
        _primaryScoringComboBox.addActionListener(e -> setModified(true));
        _secondaryScoringComboBox.addActionListener(e -> setModified(true));
        _timeLimitTextField.addKeyListener(getModifiedKeyListener());
        _hasTieBreakerScoring.addActionListener(e -> tieBreakerCheckBoxClicked());
        _hasSecondTieBreakerScoring.addActionListener(e -> secondTieBreakerCheckBoxClicked());
        _okCancelPanel.addOkActionListener(e -> createClicked());
        _okCancelPanel.addCancelActionListener(e -> closeDialogClicked());
        addWindowListener( new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                closeDialogClicked();
            }
        });
    }

    private KeyListener getModifiedKeyListener() {
        return new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                setModified(true);
            }
        };
    }

    private void tieBreakerCheckBoxClicked() {
        CardLayout cardLayout = (CardLayout) _secondaryScoringCardPanel.getLayout();
        if(_hasTieBreakerScoring.isSelected()) {
            cardLayout.show(_secondaryScoringCardPanel, HAS_TIE_BREAKER);
            _hasSecondTieBreakerScoring.setEnabled(true);
        } else {
            cardLayout.show(_secondaryScoringCardPanel, NO_TIE_BREAKER);
            _hasSecondTieBreakerScoring.setEnabled(false);
        }
        secondTieBreakerCheckBoxClicked();
        setModified(true);
    }

    private void secondTieBreakerCheckBoxClicked() {
        CardLayout cardLayout = (CardLayout) _thirdScoringComboPanel.getLayout();
        if(_hasSecondTieBreakerScoring.isSelected()) {
            cardLayout.show(_thirdScoringComboPanel, HAS_SECOND_TIE_BREAKER);
        } else {
            cardLayout.show(_thirdScoringComboPanel, NO_SECOND_TIE_BREAKER);
        }
        if(!_hasSecondTieBreakerScoring.isEnabled())
        {
            cardLayout.show(_thirdScoringComboPanel, NO_SECOND_TIE_BREAKER);
        }
        setModified(true);
    }

    private void closeDialogClicked() {
        int opt = JOptionPane.YES_OPTION;
        if(_isModified)
        {
            opt = JOptionPane.showConfirmDialog(this, "Cancel " + _cancelType + " of event?", "Confirm Cancel",
                    JOptionPane.YES_NO_OPTION);
        }
        if(opt == JOptionPane.YES_OPTION)
        {
            dispose();
        }
    }

    private void createClicked() {
        try {
            Event event = buildEvent();
            List<String> existingNames = _existingEvents.stream().map(StrengthEvent::getName)
                    .collect(Collectors.toList());
            if(existingNames.contains(event.getName()))
            {
                JOptionPane.showMessageDialog(this, event.getName() + " is already a name of an existing event! Please choose another name,",
                        "Invalid Event Name", JOptionPane.ERROR_MESSAGE);
            }
            else
            {
                CustomEventClassRegistry.getInstance().registerEvent(event);
                _createAction.accept(event);
                dispose();
            }
        } catch (MissingRequiredValueException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Invalid Event",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private Event buildEvent() throws MissingRequiredValueException {
        String name = _nameTextField.getText();
        if(name == null || name.trim().isEmpty()) {
            throw new MissingRequiredValueException("Name");
        }
        if(_primaryScoringComboBox.getSelectedIndex() < 0 || _primaryScoringComboBox.getSelectedItem() == null) {
            throw new MissingRequiredValueException("Score Type");
        }
        if(_hasTieBreakerScoring.isSelected() && (_secondaryScoringComboBox.getSelectedItem() == null || _secondaryScoringComboBox.getSelectedIndex() < 0)) {
            throw new MissingRequiredValueException("Secondary Score Type", "Has Tie Breaker");
        }
        if(_hasSecondTieBreakerScoring.isSelected() && (_thirdScoringComboBox.getSelectedItem() == null || _thirdScoringComboBox.getSelectedIndex() < 0)) {
            throw new MissingRequiredValueException("Third Score Type", "Has Second Tie Breaker");
        }
        EventScoring<?> scoring = (EventScoring<?>) _primaryScoringComboBox.getSelectedItem();
        if(_hasTieBreakerScoring.isSelected()) {
            EventScoring<CustomScore> customScoring = new CustomEventScoring();
            CustomScore customScore = new CustomScore(scoring, (EventScoring<?>) _secondaryScoringComboBox.getSelectedItem(),
                ((EventScoring<?>) _thirdScoringComboBox.getSelectedItem()));
            customScoring.setScore(customScore);
            scoring = customScoring;
        }
        String timeLimitText = _timeLimitTextField.getText();
        Duration timeLimit = null;
        if(timeLimitText != null && !timeLimitText.isEmpty()) {
            timeLimit = Duration.ofSeconds(Double.valueOf(timeLimitText).longValue());
        }
        return new StrengthEventBuilder()
                .withName(name)
                .withScoring(scoring)
                .withTimeLimit(timeLimit)
                .build();
    }

    private void buildComponents() {
        JLabel nameLabel = new JLabel("Event Name:");
        _nameTextField = new JTextField();
        JLabel timeLimitLabel = new JLabel("Time Limit (seconds):");
        _timeLimitTextField = new JTextField();
        ((AbstractDocument)_timeLimitTextField.getDocument()).setDocumentFilter(new DoubleDocumentFilter());
        JLabel scoreTypeLabel = new JLabel("Score Type:");
        _primaryScoringComboBox = new JComboBox<>();
        _hasTieBreakerScoring = new JCheckBox("Has tie-breaker");
        JLabel secondaryScoreTypeLabel = new JLabel("Secondary Score Type:");
        _secondaryScoringComboBox = new JComboBox<>();
        _hasSecondTieBreakerScoring = new JCheckBox("Has second tie-breaker");
        JLabel thirdScoreTypeLabel = new JLabel("Third Score Type:");
        _thirdScoringComboBox = new JComboBox<>();
        DefaultComboBoxModel<EventScoring<?>> primaryModel = (DefaultComboBoxModel<EventScoring<?>>) _primaryScoringComboBox.getModel();
        for(EventScoring<?> scoring : ScoringFactory.createAllScorings()) {
            if(!(scoring instanceof CustomEventScoring)) {
                primaryModel.addElement(scoring);
            }
        }
        DefaultComboBoxModel<EventScoring<?>> secondaryModel = (DefaultComboBoxModel<EventScoring<?>>) _secondaryScoringComboBox.getModel();
        for(EventScoring<?> scoring : ScoringFactory.createAllScorings()) {
            if(!(scoring instanceof CustomEventScoring)) {
                secondaryModel.addElement(scoring);
            }
        }
        DefaultComboBoxModel<EventScoring<?>> thirdScoringModel = (DefaultComboBoxModel<EventScoring<?>>) _thirdScoringComboBox.getModel();
        for(EventScoring<?> scoring : ScoringFactory.createAllScorings()) {
            if(!(scoring instanceof CustomEventScoring)) {
                thirdScoringModel.addElement(scoring);
            }
        }
        _okCancelPanel = new OkCancelPanel("Create");

        addLabeledComponentOnOwnLine(this, nameLabel, _nameTextField, 0.0);
        addLabeledComponentOnOwnLine(this, timeLimitLabel, _timeLimitTextField, 0.0);
        addLabeledComponentOnOwnLine(this, scoreTypeLabel, _primaryScoringComboBox, 0.0);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx     = GridBagConstraints.RELATIVE;
        gbc.gridy     = GridBagConstraints.RELATIVE;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.weightx   = 0.0;
        gbc.weighty   = 0.0;
        gbc.anchor    = GridBagConstraints.NORTHWEST;
        gbc.fill      = GridBagConstraints.NONE;
        gbc.insets    = new Insets(5,2,0,5);
        add(_hasTieBreakerScoring, gbc);

        JPanel secondaryComboPanel = new JPanel(new GridBagLayout());
        addLabeledComponentOnOwnLine(secondaryComboPanel, secondaryScoreTypeLabel, _secondaryScoringComboBox, 0.001);
        _secondaryScoringCardPanel = new JPanel(new CardLayout());
        _secondaryScoringCardPanel.add(NO_TIE_BREAKER, new JPanel());
        _secondaryScoringCardPanel.add(HAS_TIE_BREAKER, secondaryComboPanel);
        gbc = new GridBagConstraints();
        gbc.gridx     = GridBagConstraints.RELATIVE;
        gbc.gridy     = GridBagConstraints.RELATIVE;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.weightx   = 0.0;
        gbc.weighty   = 0.001;
        gbc.anchor    = GridBagConstraints.NORTHWEST;
        gbc.fill      = GridBagConstraints.HORIZONTAL;
        gbc.insets    = new Insets(0,0,0,5);
        add(_secondaryScoringCardPanel, gbc);

        gbc = new GridBagConstraints();
        gbc.gridx     = GridBagConstraints.RELATIVE;
        gbc.gridy     = GridBagConstraints.RELATIVE;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.weightx   = 0.0;
        gbc.weighty   = 0.0;
        gbc.anchor    = GridBagConstraints.NORTHWEST;
        gbc.fill      = GridBagConstraints.NONE;
        gbc.insets    = new Insets(5,2,0,5);
        add(_hasSecondTieBreakerScoring, gbc);

        JPanel thirdComboPanel = new JPanel(new GridBagLayout());
        addLabeledComponentOnOwnLine(thirdComboPanel, thirdScoreTypeLabel, _thirdScoringComboBox, 0.001);
        _thirdScoringComboPanel = new JPanel(new CardLayout());
        _thirdScoringComboPanel.add(NO_SECOND_TIE_BREAKER, new JPanel());
        _thirdScoringComboPanel.add(HAS_SECOND_TIE_BREAKER, thirdComboPanel);
        gbc = new GridBagConstraints();
        gbc.gridx     = GridBagConstraints.RELATIVE;
        gbc.gridy     = GridBagConstraints.RELATIVE;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.weightx   = 0.0;
        gbc.weighty   = 0.001;
        gbc.anchor    = GridBagConstraints.NORTHWEST;
        gbc.fill      = GridBagConstraints.HORIZONTAL;
        gbc.insets    = new Insets(0,0,0,5);
        add(_thirdScoringComboPanel, gbc);

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

    private void addLabeledComponentOnOwnLine(Container parent, JLabel label, JComponent component, double weightY) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx     = GridBagConstraints.RELATIVE;
        gbc.gridy     = GridBagConstraints.RELATIVE;
        gbc.gridwidth = GridBagConstraints.RELATIVE;
        gbc.weightx   = 0.0;
        gbc.weighty   = 0.0;
        gbc.anchor    = GridBagConstraints.NORTHWEST;
        gbc.fill      = GridBagConstraints.NONE;
        gbc.insets    = new Insets(5,5,0,5);
        parent.add(label, gbc);

        gbc = new GridBagConstraints();
        gbc.gridx     = GridBagConstraints.RELATIVE;
        gbc.gridy     = GridBagConstraints.RELATIVE;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.weightx   = 0.001;
        gbc.weighty   = weightY;
        gbc.anchor    = GridBagConstraints.NORTHWEST;
        gbc.fill      = GridBagConstraints.HORIZONTAL;
        gbc.insets    = new Insets(5,5,0,5);
        parent.add(component, gbc);
    }

    @Override
    public void setModified(boolean modified) {
        _isModified = modified;
    }

    @Override
    public boolean isModified() {
        return _isModified;
    }

    public void fill(EventsRowData rowData) {
        _nameTextField.setText(rowData.getName());
        _timeLimitTextField.setText(rowData.getTimeLimit() == null ? "" : rowData.getTimeLimit().toString());
        String eventScoringString = rowData.getEventScoring().toString();
        String[] split = eventScoringString.split(">");
        if(split.length > 1)
        {
            _hasTieBreakerScoring.setSelected(true);
            tieBreakerCheckBoxClicked();
            for(int i=0; i < _secondaryScoringComboBox.getModel().getSize(); i++)
            {
                Object scoring = ((DefaultComboBoxModel<?>) _secondaryScoringComboBox.getModel()).getElementAt(i);
                if(scoring != null && scoring.toString().equalsIgnoreCase(split[1].trim()))
                {
                    _secondaryScoringComboBox.setSelectedItem(scoring);
                    break;
                }
            }
            if(split.length > 2)
            {
                _hasSecondTieBreakerScoring.setSelected(true);
                secondTieBreakerCheckBoxClicked();
                for(int i=0; i < _thirdScoringComboBox.getModel().getSize(); i++)
                {
                    Object scoring = ((DefaultComboBoxModel<?>) _thirdScoringComboBox.getModel()).getElementAt(i);
                    if(scoring != null && scoring.toString().equalsIgnoreCase(split[2].trim()))
                    {
                        _thirdScoringComboBox.setSelectedItem(scoring);
                        break;
                    }
                }
            }
        }
    }

    public void setToUpdateMode() {
        _okCancelPanel.setOkText("Update");
        _cancelType = "update";
        setTitle("Edit Event");
    }
}
