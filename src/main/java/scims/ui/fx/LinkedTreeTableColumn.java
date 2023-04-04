package scims.ui.fx;

import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;

abstract class LinkedTreeTableColumn extends TreeTableColumn<Object, Object> {

    public LinkedTreeTableColumn(String name) {
        super(name);
    }

    abstract void updateLinkedCells(TreeItem<Object> rowObject);
}
