package scims.ui.actions;

import scims.model.data.WeightClass;
import scims.ui.swing.dialogs.WeightClassDialog;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.function.Consumer;

public class EditWeightClassAction implements ActionListener {
    private final JFrame _parentFrame;
    private final Consumer<WeightClass> _consumerOfCreate;
    private final WeightClass _weightClass;

    public EditWeightClassAction(JFrame parentFrame, WeightClass weightClass, Consumer<WeightClass> consumerOfCreate)
    {
        _parentFrame = parentFrame;
        _consumerOfCreate = consumerOfCreate;
        _weightClass = weightClass;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        WeightClassDialog dlg = new WeightClassDialog(_parentFrame, _consumerOfCreate);
        dlg.setTitle("Update Weight Class");
        dlg.fillPanel(_weightClass);
        dlg.setVisible(true);
    }
}
