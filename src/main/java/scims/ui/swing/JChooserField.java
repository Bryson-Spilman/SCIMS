package scims.ui.swing;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.util.List;
import java.util.stream.Collectors;

class JChooserField<T> extends JPanel {

    private JTextField _textField;
    private JButton _button;

    private final JChooserDialog<T> _chooserDlg;

    public JChooserField(Window parent, List<T> objects) {
        _chooserDlg = new JChooserDialog<>(parent, objects, this::updateField);
        setLayout(new GridBagLayout());
        setBorder(BorderFactory.createLoweredBevelBorder());
        buildComponents();
        addListeners();
    }

    private void updateField(List<T> objects) {
        _textField.setText(objects.stream().map(Object::toString).collect(Collectors.joining(",")));
    }

    private void addListeners() {
        _button.addActionListener(e -> _chooserDlg.setVisible(true));
    }

    public void addActionListener(ActionListener e)
    {
        _button.addActionListener(e);
    }

    public void addKeyListener(KeyListener keyListener)
    {
        _textField.addKeyListener(keyListener);
    }

    private void buildComponents() {
        _textField = new JTextField();
        _textField.setBorder(BorderFactory.createEmptyBorder());
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
}
