package scims.ui.swing;

import scims.model.data.*;
import scims.model.data.Event;
import scims.model.enums.EventScoreType;
import scims.model.enums.UnitSystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

public class NewCompetitionDialog extends JDialog {
    private final Consumer<Competition> _createAction;
    private JTextField _nameTextField;
    private JButton _createButton;
    private JButton _cancelButton;
    private boolean _isModified = false;
    private JChooserField _weightClassChooserField;

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
        List<Event> eventsInOrder = new ArrayList<>();
        eventsInOrder.add(new StrengthEventBuilder()
                .withName("Event 1")
                .withScoreType(EventScoreType.REPS)
                .build());
        WeightClass testWeightClass = new StrengthWeightClassBuilder()
                .withName("LightWeight")
                .withMaxCompetitorWeight(200.4)
                .withEventsInOrder(eventsInOrder)
                .build();
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
        testWeightClass.addCompetitor(competitor);
        testWeightClass.addCompetitor(competitor2);
        return new StrengthCompetitionBuilder()
                .withName(_nameTextField.getText())
                .withDateTime(ZonedDateTime.now())
                .withIsSameNumberOfEventsForAllWeightClasses(true)
                .withWeightClasses(Collections.singletonList(testWeightClass))
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
    }

    private void addWeightClasses(List<WeightClass> weightClasses) {
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
        JLabel selectWeightClassesLabel = new JLabel("Select Weight Classes:");
        List<WeightClass> weightClasses = new ArrayList<>();
        weightClasses.add(new StrengthWeightClassBuilder().withName("Light Weight Men").withMaxCompetitorWeight(200.4).withEventsInOrder(new ArrayList<>()).build());
        weightClasses.add(new StrengthWeightClassBuilder().withName("Middle Weight Men").withMaxCompetitorWeight(264.4).withEventsInOrder(new ArrayList<>()).build());;
        weightClasses.add(new StrengthWeightClassBuilder().withName("Heavy Weight Men").withMaxCompetitorWeight(Integer.MAX_VALUE).withEventsInOrder(new ArrayList<>()).build());
        _weightClassChooserField = new JChooserField<>(this, weightClasses);
        _createButton = new JButton("Create");
        _cancelButton = new JButton("Cancel");

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
        gbc.insets    = new Insets(5,5,0,5);
        attributesPanel.add(selectWeightClassesLabel, gbc);

        gbc = new GridBagConstraints();
        gbc.gridx     = GridBagConstraints.RELATIVE;
        gbc.gridy     = GridBagConstraints.RELATIVE;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.weightx   = 0.001;
        gbc.weighty   = 0.001;
        gbc.anchor    = GridBagConstraints.NORTHWEST;
        gbc.fill      = GridBagConstraints.HORIZONTAL;
        gbc.insets    = new Insets(5,5,0,5);
        attributesPanel.add(_weightClassChooserField, gbc);

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
}
