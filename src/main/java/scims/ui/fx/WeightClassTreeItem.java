package scims.ui.fx;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;
import scims.model.Competitor;
import scims.model.WeightClass;

class WeightClassTreeItem extends TreeItem<WeightClass> {

    WeightClassTreeItem(WeightClass weightClass)
    {
        super(weightClass);
    }

    public void addCompetitor(Competitor competitor) {
        getValue().addCompetitor(competitor);
    }

    public void removeCompetitor(Competitor competitor) {
        getValue().removeCompetitor(competitor);
    }
}

