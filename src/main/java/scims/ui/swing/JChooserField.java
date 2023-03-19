package scims.ui.swing;

import scims.ui.Modifiable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class JChooserField<T> extends JPanel {

    private List<T> _objects;
    private final Window _parent;
    private JTextField _textField;
    private JButton _button;

    private final JChooserDialog<T> _chooserDlg;

    public JChooserField(Window parent, List<T> objects) {
        _parent = parent;
        _chooserDlg = new JChooserDialog<>(parent, objects, this::updateField);
        _objects = objects;
        setLayout(new GridBagLayout());
        setBorder(BorderFactory.createLoweredBevelBorder());
        buildComponents();
        addListeners();
    }

    private void updateField(List<T> objects) {
        _textField.setText(objects.stream().map(Object::toString).collect(Collectors.joining("; ")));
        if(_parent instanceof Modifiable)
        {
            ((Modifiable)_parent).setModified(true);
        }
    }

    private void addListeners() {
        _button.addActionListener(e -> {
            _chooserDlg.setVisible(true);
        });
    }

    public void addKeyListener(KeyListener keyListener)
    {
        _textField.addKeyListener(keyListener);
    }

    private void buildComponents() {
        _textField = new JTextField();
        _textField.setBorder(BorderFactory.createEmptyBorder());
        _chooserDlg.setTextField(_textField);
        _button = new JButton("...");
        _button.setPreferredSize(new Dimension(20, _textField.getPreferredSize().height));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx     = GridBagConstraints.RELATIVE;
        gbc.gridy     = GridBagConstraints.RELATIVE;
        gbc.gridwidth = GridBagConstraints.RELATIVE;
        gbc.weightx   = 0.001;
        gbc.weighty   = 0.0;
        gbc.anchor    = GridBagConstraints.NORTHWEST;
        gbc.fill      = GridBagConstraints.HORIZONTAL;
        gbc.insets    = new Insets(0,0,0,0);
        add(_textField, gbc);

        gbc.gridx     = GridBagConstraints.RELATIVE;
        gbc.gridy     = GridBagConstraints.RELATIVE;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.weightx   = 0.0;
        gbc.weighty   = 0.0;
        gbc.anchor    = GridBagConstraints.EAST;
        gbc.fill      = GridBagConstraints.NONE;
        gbc.insets    = new Insets(0,0,0,0);
        add(_button, gbc);

    }

    public String getText() {
        return _textField.getText();
    }

    public void setText(String text) {
        _textField.setText(text);
    }

    public void setObjects(List<T> objects) {
        _chooserDlg.setObjects(objects);
    }

    public void reset() {
        _textField.setText("");
        _chooserDlg.setObjects(new ArrayList<>());
    }

    public List<T> getSelectedObjects() {
        return _chooserDlg.getSelectedObjects();
    }

    public List<T> getObjects() {
        return _chooserDlg.getObjects();
    }

    public void addOnSelectionEvent(Runnable event) {
        _chooserDlg.addOnSelectionEvent(event);
    }

    public void setSelectedObjects(List<T> selectedObjects) {
        _chooserDlg.setSelectedObjects(selectedObjects);
    }

    public void setEditable(boolean editable) {
        _textField.setEditable(editable);
    }

    @Override
    public void setEnabled(boolean enabled) {
        _textField.setEnabled(enabled);
        _button.setEnabled(enabled);
    }

}
