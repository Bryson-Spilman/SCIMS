package scims.ui.actions;

import scims.model.data.WeightClass;
import scims.ui.swing.WeightClassDialog;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.function.Consumer;

public class NewWeightClassAction implements ActionListener {
    private final JFrame _parentFrame;
    private final Consumer<WeightClass> _consumerOfCreate;

    public NewWeightClassAction(JFrame parentFrame, Consumer<WeightClass> consumerOfCreate) {
        _parentFrame = parentFrame;
        _consumerOfCreate = consumerOfCreate;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        WeightClassDialog dlg = new WeightClassDialog(_parentFrame, _consumerOfCreate);
        dlg.setVisible(true);
    }
}