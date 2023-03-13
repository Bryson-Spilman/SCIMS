package scims.ui.fx;

import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.scene.layout.Pane;
import scims.model.WeightClass;

class CompetitionTreeView extends Pane {

    private TreeTableView<WeightClass> _treeTableView;

    public CompetitionTreeView() {
        _treeTableView = new TreeTableView<>();
        setupTableColumns();
        getChildren().add(_treeTableView);
    }

    private void setupTableColumns() {
        TreeTableColumn<WeightClass, String> weightClassColumn = new TreeTableColumn<>("Weight Class");
        weightClassColumn.setCellValueFactory(new TreeItemPropertyValueFactory<>("name"));

        // Create columns for each event
        TreeTableColumn<WeightClass, Integer> deadliftColumn = new TreeTableColumn<>("Deadlift");
        deadliftColumn.setCellValueFactory(new TreeItemPropertyValueFactory<>("deadliftScore"));

        TreeTableColumn<WeightClass, Integer> overheadPressColumn = new TreeTableColumn<>("Overhead Press");
        overheadPressColumn.setCellValueFactory(new TreeItemPropertyValueFactory<>("overheadPressScore"));

        // Add columns to the table
        _treeTableView.getColumns().addAll(weightClassColumn, deadliftColumn, overheadPressColumn);
    }

    public void setRoot(WeightClassTreeItem weightClassTreeItem) {
        _treeTableView.setRoot(weightClassTreeItem);
    }
}
