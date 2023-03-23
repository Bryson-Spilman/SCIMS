package scims.ui.swing;


import scims.model.data.StrengthWeightClassBuilder;
import scims.model.data.WeightClass;
import scims.ui.Modifiable;
import scims.ui.swing.tablecells.DoubleDocumentFilter;
import scims.ui.swing.tablecells.IntegerDocumentFilter;

import javax.swing.*;
import javax.swing.text.AbstractDocument;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.function.Consumer;

class NewWeightClassDialog extends JDialog implements Modifiable {

    private final Consumer<WeightClass> _createAction;
    private JTextField _nameTextField;
    private JTextField _maxNumberOfCompetitorsField;
    private JTextField _maxCompetitorWeight;
    private OkCancelPanel _okCancelPanel;
    private boolean _isModified;

    NewWeightClassDialog(Window parent, Consumer<WeightClass> createAction) {
        super(parent, "New Weight Class");
        setModal(true);
        setLayout(new GridBagLayout());
        setSize(400,175);
        setMinimumSize(new Dimension(Integer.MIN_VALUE, 175));
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(parent);
        _createAction = createAction;
        // Initialize components
        buildComponents();
        addListeners();
    }

    private void addListeners() {
        _nameTextField.addActionListener(e -> setModified(true));
        _maxNumberOfCompetitorsField.addActionListener(e -> setModified(true));
        _maxCompetitorWeight.addActionListener(e -> setModified(true));
        _okCancelPanel.addOkActionListener(e -> createClicked());
        _okCancelPanel.addCancelActionListener(e -> closeDialogClicked());
        addWindowListener( new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                closeDialogClicked();
            }
        });
    }

    private void closeDialogClicked() {
        int opt = JOptionPane.YES_OPTION;
        if(_isModified)
        {
            opt = JOptionPane.showConfirmDialog(this, "Cancel creation of weight class?", "Confirm Cancel",
                    JOptionPane.YES_NO_OPTION);
        }
        if(opt == JOptionPane.YES_OPTION)
        {
            dispose();
        }
    }

    private void createClicked() {
        try {
            _createAction.accept(buildWeightClass());
            dispose();
        } catch (MissingRequiredValueException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Invalid Weight Class",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private WeightClass buildWeightClass() throws MissingRequiredValueException {
        String name = _nameTextField.getText();
        String maxCompetitorWeight = _maxCompetitorWeight.getText();
        String maxNumberOfCompetitorsStr = _maxNumberOfCompetitorsField.getText();
        Integer maxNumberOfCompetitors = null;
        if(!maxNumberOfCompetitorsStr.trim().isEmpty()) {
            maxNumberOfCompetitors = Integer.parseInt(maxNumberOfCompetitorsStr);
        }
        if(name == null || name.trim().isEmpty()) {
            throw new MissingRequiredValueException("Name");
        }
        if(maxCompetitorWeight == null || maxCompetitorWeight.trim().isEmpty()) {
            throw new MissingRequiredValueException("Max Weight");
        }
        return new StrengthWeightClassBuilder()
                .withName(name)
                .withMaxCompetitorWeight(Double.parseDouble(maxCompetitorWeight))
                .withEventsInOrder(new ArrayList<>())
                .withMaxNumberOfCompetitors(maxNumberOfCompetitors)
                .build();
    }

    private void buildComponents() {
        JLabel nameLabel = new JLabel("Weight Class Name:");
        _nameTextField = new JTextField();
        JLabel maxNumberCompetitorLabel = new JLabel("Max Number of Competitors:");
        _maxNumberOfCompetitorsField = new JTextField();
        ((AbstractDocument) _maxNumberOfCompetitorsField.getDocument()).setDocumentFilter(new IntegerDocumentFilter());
        JLabel maxCompetitorWeightLabel = new JLabel("Max Competitor Weight:");
        _maxCompetitorWeight = new JTextField();
        ((AbstractDocument) _maxCompetitorWeight.getDocument()).setDocumentFilter(new DoubleDocumentFilter());

        _okCancelPanel = new OkCancelPanel("Create");
        addLabeledTextField(this, nameLabel, _nameTextField, 0.0);
        addLabeledTextField(this, maxCompetitorWeightLabel, _maxCompetitorWeight, 0.0);
        addLabeledTextField(this, maxNumberCompetitorLabel, _maxNumberOfCompetitorsField, 0.001);

        GridBagConstraints  gbc = new GridBagConstraints();
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

    private void addLabeledTextField(Container parent, JLabel label, JTextField textField, double weightY) {
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
        parent.add(textField, gbc);
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
