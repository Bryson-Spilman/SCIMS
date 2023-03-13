package scims.ui.fx;

import javafx.scene.control.TreeItem;
import scims.model.Competitor;
import scims.model.WeightClassGroup;

class WeightClassTreeItem extends TreeItem<WeightClassGroup> {

    WeightClassTreeItem(WeightClassGroup weightClassGroup)
    {
        super(weightClassGroup);
    }

    public void addCompetitor(Competitor competitor) {
        getValue().addCompetitor(competitor);
    }

    public void removeCompetitor(Competitor competitor) {
        getValue().removeCompetitor(competitor);
    }
}

