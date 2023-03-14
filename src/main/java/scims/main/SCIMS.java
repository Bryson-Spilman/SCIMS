package scims.main;

import scims.ui.swing.SCIMSFrame;

import javax.swing.*;

public class SCIMS {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(SCIMSFrame::new);
    }
}
