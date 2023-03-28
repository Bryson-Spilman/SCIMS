package scims.ui.swing.dialogs;

import scims.main.CustomEventClassRegistry;
import scims.model.data.Event;
import scims.model.data.StrengthEventBuilder;
import scims.model.data.scoring.CustomEventScoring;
import scims.model.data.scoring.CustomScore;
import scims.model.data.scoring.EventScoring;
import scims.model.data.scoring.ScoringFactory;
import scims.ui.Modifiable;
import scims.ui.swing.MissingRequiredValueException;
import scims.ui.swing.OkCancelPanel;
import scims.ui.swing.tablecells.DoubleDocumentFilter;

import javax.swing.*;
import javax.swing.text.AbstractDocument;
import java.awt.*;
import java.awt.event.*;
import java.time.Duration;
import java.util.function.Consumer;

public class EventDialog extends JDialog implements Modifiable {

    private static final String NO_TIE_BREAKER = "NO_TIE_BREAKER";
    private static final String HAS_TIE_BREAKER = "HAS_TIE_BREAKER";
    private final Consumer<Event> _createAction;
    private boolean _isModified;
    private JTextField _nameTextField;
    private JTextField _timeLimitTextField;
    private  JComboBox<EventScoring<?>> _primaryScoringComboBox;
    private JComboBox<EventScoring<?>> _secondaryScoringComboBox;
    private JCheckBox _hasTieBreakerScoring;
    private OkCancelPanel _okCancelPanel;
    private JPanel _secondaryScoringCardPanel;

    public EventDialog(Window parent, Consumer<Event> createAction) {
        super(parent, "New Event");
        setModal(true);
        setLayout(new GridBagLayout());
        setSize(400,250);
        setMinimumSize(new Dimension(Integer.MIN_VALUE, 175));
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(parent);
        _createAction = createAction;
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
        } else {
            cardLayout.show(_secondaryScoringCardPanel, NO_TIE_BREAKER);
        }
        setModified(true);
    }

    private void closeDialogClicked() {
        int opt = JOptionPane.YES_OPTION;
        if(_isModified)
        {
            opt = JOptionPane.showConfirmDialog(this, "Cancel creation of event?", "Confirm Cancel",
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
            CustomEventClassRegistry.getInstance().registerEvent(event);
            _createAction.accept(event);
            dispose();
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
        EventScoring<?> scoring = (EventScoring<?>) _primaryScoringComboBox.getSelectedItem();
        if(_hasTieBreakerScoring.isSelected()) {
            EventScoring<CustomScore> customScoring = new CustomEventScoring();
            CustomScore customScore = new CustomScore(scoring, (EventScoring<?>) _secondaryScoringComboBox.getSelectedItem());
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
        _hasTieBreakerScoring = new JCheckBox("Has tie-breaker scoring");
        JLabel secondaryScoreTypeLabel = new JLabel("Secondary Score Type:");
        _secondaryScoringComboBox = new JComboBox<>();
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
}