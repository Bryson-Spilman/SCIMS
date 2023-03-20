package scims.ui.swing;

import scims.ui.Modifiable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class JChooserField<T> extends JPanel {

    private Window _parent;
    private JTextField _textField;
    private JButton _button;

    private final JChooserDialog<T> _chooserDlg;

    private String _delimeter = ";";

    public JChooserField(Window parent, List<T> objects) {
        _parent = parent;
        _chooserDlg = new JChooserDialog<>(parent, objects, this::updateField);
        _textField = new JTextField();
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

    public void setDelimeter(String delimeter) {
        _delimeter = delimeter;
        _chooserDlg.setDelimeter(delimeter);
    }

    public String getDelimeter() {
        return _delimeter;
    }

    private void addListeners() {
        _button.addActionListener(e -> {
            if(_parent == null) {
                _parent = SwingUtilities.getWindowAncestor(this);
                if(_parent instanceof JFrame) {
                    _chooserDlg.setIconImage(((JFrame)_parent).getIconImage());
                }
                else {
                    JFrame frame = getTopLevelFrame(this);
                    _chooserDlg.setIconImage(frame.getIconImage());
                }
                boolean modal = _chooserDlg.isModal();
                _chooserDlg.setModal(false);
                _chooserDlg.setAlwaysOnTop(true);
                _chooserDlg.setLocationRelativeTo(_parent);
                _chooserDlg.setModal(modal);
            }
            _chooserDlg.setVisible(true);
        });
    }

    public JFrame getTopLevelFrame(Component c) {
        if (c.getParent() == null) {
            return null;
        }
        if (c.getParent() instanceof JFrame) {
            return (JFrame) c.getParent();
        }
        return getTopLevelFrame(c.getParent());
    }

    public void addKeyListener(KeyListener keyListener)
    {
        _textField.addKeyListener(keyListener);
    }

    private void buildComponents() {
        _textField = new JTextField();
        _textField.setBorder(BorderFactory.createEmptyBorder());
        _textField.setPreferredSize(new Dimension(Integer.MAX_VALUE, 25));
        _chooserDlg.setTextField(_textField);
        _button = new JButton("...");
        _button.setPreferredSize(new Dimension(20, 20));
        _button.setMaximumSize(_button.getPreferredSize());
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.add(_textField, new Integer(1));
        layeredPane.add(_button, new Integer(2));
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

        gbc.gridx     = 1;
        gbc.gridy     = 0;
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
        String text = _textField.getText();
        if(text != null) {
            String[] split = text.split(_delimeter);
            List<String> objStrings = objects.stream().map(Object::toString).collect(Collectors.toList());
            objStrings.retainAll(Arrays.asList(split));
            boolean isEnabled = _textField.isEnabled();
            _textField.setEnabled(true);
            _textField.setText(String.join(_delimeter,objStrings));
            _textField.setEnabled(isEnabled);
        }
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
        boolean isEnabled = _textField.isEnabled();
        _textField.setEnabled(true);
        _textField.setText(selectedObjects.stream().map(Object::toString).collect(Collectors.joining(_delimeter)));
        _textField.setEnabled(isEnabled);
    }

    public void setEditable(boolean editable) {
        _textField.setEditable(editable);
    }

    @Override
    public void setEnabled(boolean enabled) {
        _textField.setEnabled(enabled);
        _textField.setEditable(enabled);
        _button.setEnabled(enabled);
    }

    @Override
    public void setBackground(Color bg) {
        super.setBackground(bg);
        if(_textField != null) {
            _textField.setBackground(bg);
        }
    }

    @Override
    public void setForeground(Color bg) {
        super.setForeground(bg);
        if(_textField != null) {
            _textField.setForeground(bg);
        }
    }
}
