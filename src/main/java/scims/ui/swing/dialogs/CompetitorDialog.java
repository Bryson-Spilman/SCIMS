package scims.ui.swing.dialogs;

import scims.main.CustomEventClassRegistry;
import scims.model.data.Competitor;
import scims.model.data.StrengthCompetitorBuilder;
import scims.ui.Modifiable;
import scims.ui.swing.MissingRequiredValueException;
import scims.ui.swing.OkCancelPanel;
import scims.ui.swing.tablecells.DoubleDocumentFilter;
import scims.ui.swing.tablecells.IntegerDocumentFilter;

import javax.swing.*;
import javax.swing.text.AbstractDocument;
import java.awt.*;
import java.awt.event.*;
import java.util.function.Consumer;

public class CompetitorDialog extends JDialog implements Modifiable {
    private final Consumer<Competitor> _createAction;
    private JTextField _nameTextField;
    private JTextField _ageField;
    private JTextField _weightField;
    private OkCancelPanel _okCancelPanel;
    private boolean _isModified;

    public CompetitorDialog(Window parent, Consumer<Competitor> createAction) {
        super(parent, "New Competitor");
        setModal(true);
        setLayout(new GridBagLayout());
        setSize(400,250);
        setMinimumSize(new Dimension(Integer.MIN_VALUE, 175));
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(parent);
        _createAction = createAction;
        buildComponents();
        addListeners();
    }

    private void addListeners() {
        _nameTextField.addKeyListener(getModifiedKeyListener());
        _ageField.addKeyListener(getModifiedKeyListener());
        _weightField.addKeyListener(getModifiedKeyListener());
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
            public void keyPressed(KeyEvent e) {
                setModified(true);
            }
        };
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
            Competitor competitor = buildCompetitor();
            _createAction.accept(competitor);
            dispose();
        } catch (MissingRequiredValueException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Invalid Competitor",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private Competitor buildCompetitor() throws MissingRequiredValueException {
        String name = _nameTextField.getText();
        if(name == null || name.trim().isEmpty()) {
            throw new MissingRequiredValueException("Name");
        }
        String ageTxt = _ageField.getText();
        Integer age = null;
        if(ageTxt != null && !ageTxt.isEmpty()) {
            age = Integer.parseInt(ageTxt);
        }
        String weightTxt = _weightField.getText();
        Double weight = null;
        if(weightTxt != null && !weightTxt.isEmpty()) {
            weight = Double.parseDouble(weightTxt);
        }
        return new StrengthCompetitorBuilder()
                .withCompetitorName(name)
                .withCompetitorAge(age)
                .withCompetitorWeight(weight)
                .build();
    }
    private void buildComponents() {
        JLabel nameLabel = new JLabel("Name:");
        _nameTextField = new JTextField();
        JLabel ageLabel = new JLabel("Age <Optional>:");
        _ageField = new JTextField();
        ((AbstractDocument)_ageField.getDocument()).setDocumentFilter(new IntegerDocumentFilter());
        JLabel weightLabel = new JLabel("Weight <Optional>:");
        _weightField = new JTextField();
        ((AbstractDocument)_weightField.getDocument()).setDocumentFilter(new DoubleDocumentFilter());
        _okCancelPanel = new OkCancelPanel("Create");

        addLabeledComponentOnOwnLine(this, nameLabel, _nameTextField, 0.0);
        addLabeledComponentOnOwnLine(this, ageLabel, _ageField, 0.0);
        addLabeledComponentOnOwnLine(this, weightLabel, _weightField, 0.001);

        GridBagConstraints gbc = new GridBagConstraints();
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

    public void fillPanel(Competitor competitor) {
        _nameTextField.setText(competitor.getName());
        Integer age = competitor.getAge();
        Double weight = competitor.getWeight();
        if(age != null) {
            _ageField.setText(age.toString());
        }
        if(weight != null) {
            _weightField.setText(weight.toString());
        }
        _okCancelPanel.setOkText("Update");
    }
}
