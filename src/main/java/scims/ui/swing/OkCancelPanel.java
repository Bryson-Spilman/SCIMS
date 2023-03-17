package scims.ui.swing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class OkCancelPanel extends JPanel {

    private JButton _okButton;
    private JButton _cancelButton;
    public OkCancelPanel() {
       this(null);
    }

    public OkCancelPanel(String okButtonNameOverride) {
        _okButton = new JButton(okButtonNameOverride == null ? "OK" : okButtonNameOverride);
        _cancelButton = new JButton("Cancel");
        setLayout(new BorderLayout());
        buildOkCancelButtons();
    }

     void addOkActionListener(ActionListener actionListener) {
        _okButton.addActionListener(actionListener);
    }
    public void addCancelActionListener(ActionListener actionListener) {
        _cancelButton.addActionListener(actionListener);
    }

    private void buildOkCancelButtons() {
        JPanel okCancelPanel = new JPanel();

        setLayout(new BorderLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx     = GridBagConstraints.RELATIVE;
        gbc.gridy     = GridBagConstraints.RELATIVE;
        gbc.gridwidth = GridBagConstraints.RELATIVE;
        gbc.weightx   = 0.001;
        gbc.weighty   = 0.001;
        gbc.anchor    = GridBagConstraints.SOUTHEAST;
        gbc.fill      = GridBagConstraints.NONE;
        gbc.insets    = new Insets(5,5,5,5);
        okCancelPanel.add(_okButton, gbc);

        gbc = new GridBagConstraints();
        gbc.gridx     = GridBagConstraints.RELATIVE;
        gbc.gridy     = GridBagConstraints.RELATIVE;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.weightx   = 0.0;
        gbc.weighty   = 0.0;
        gbc.anchor    = GridBagConstraints.SOUTHEAST;
        gbc.fill      = GridBagConstraints.NONE;
        gbc.insets    = new Insets(5,5,5,0);
        okCancelPanel.add(_cancelButton, gbc);

        add(okCancelPanel, BorderLayout.EAST);
    }

}
