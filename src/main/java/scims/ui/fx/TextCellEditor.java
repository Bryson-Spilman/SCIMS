package scims.ui.fx;

import javafx.application.Platform;
import javafx.event.Event;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import scims.main.SCIMS;
import scims.ui.swing.SCIMSFrame;

class TextCellEditor<T, S> extends TreeTableCell<T, S> {


    protected TextField _textField;
    protected TreeTableColumn<T, ?> _cancelledColumn;
    protected TreeTableRow<T> _cancelledRow;
    private String _previousVal = "";

    @SuppressWarnings("unchecked")
    public TextCellEditor(TreeTableColumn<Object, Object> col) {
        itemProperty().addListener((observable, oldValue, newValue) -> {
           if(col instanceof LinkedTreeTableColumn) {
               TreeTableRow<T> row = getTreeTableRow();
               if(row.getTreeItem() != null)
               {
                   ((LinkedTreeTableColumn)col).updateLinkedCells((TreeItem<Object>) row.getTreeItem());
               }
           }
        });

    }

    @Override
    @SuppressWarnings("unchecked")
    public void startEdit() {
        if(!isCellEditable(getTreeTableRow()))
        {
            return;
        }
        super.startEdit();
        if(_textField == null && isCellEditable(getTreeTableRow()))
        {
            createTextField();
        }
        _previousVal = _textField.getText();
        setText(null);
        setGraphic(_textField);
        if(_textField != null)
        {
            _textField.requestFocus();
            _textField.selectAll();
            _textField.setEditable(isCellEditable(getTreeTableRow()));
            _textField.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
                if (!isNowFocused) {
                    TreeTableColumn<T, S> col = getTableColumn();
                    if(_cancelledRow != null && _cancelledColumn != null && col == _cancelledColumn) {
                        @SuppressWarnings("unchecked")
                        TreeTablePosition<T,S> editingCell = (TreeTablePosition<T, S>) new TreeTablePosition<>(getTreeTableView(), _cancelledRow.getIndex(), _cancelledColumn);
                        manualCommitEdit((S)_textField.getText(), editingCell, col);
                    }
                }
                else
                {
                    Platform.runLater(() -> {
                        _cancelledRow = null;
                        _textField.requestFocus();
                        _textField.selectAll();
                    });
                }
            });
        }
    }

    @Override
    public void cancelEdit() {
        _cancelledColumn = getTreeTableView().getFocusModel().getFocusedCell().getTableColumn();
        _cancelledRow = getTreeTableRow();
        if(getItem() != null && _textField != null && getItem().toString().equalsIgnoreCase(_textField.getText()))
        {
            _cancelledRow = null;
            _cancelledColumn = null;
        }
        super.cancelEdit();
        if(_textField != null)
        {
            String text = _textField.getText();
            setText(text);
            SCIMS.getFrame().setModified(SCIMS.getFrame().isModified() || !_previousVal.equals(_textField.getText()));
            _previousVal = _textField.getText();
        }
        setGraphic(null);
    }

    public void manualCommitEdit(S newValue, TreeTablePosition<T,S> editingCell, TreeTableColumn<T, S> col)
    {
        final TreeTableView<T> table = getTreeTableView();
        if (table != null) {
            // Inform the TableView of the edit being ready to be committed.
            TreeTableColumn.CellEditEvent<T,S> editEvent = new TreeTableColumn.CellEditEvent<>(
                    table,
                    editingCell,
                    TreeTableColumn.editCommitEvent(),
                    newValue
            );

            Event.fireEvent(col, editEvent);
        }

        // inform parent classes of the commit, so that they can switch us
        // out of the editing state.
        // This MUST come before the updateItem call below, otherwise it will
        // call cancelEdit(), resulting in both commit and cancel events being
        // fired (as identified in RT-29650)
        super.commitEdit(newValue);

        // update the item within this cell, so that it represents the new value
        updateItem(newValue, false);
        if (table != null) {
            // reset the editing cell on the TableView
            table.edit(-1, null);

            requestFocusOnControlOnlyIfCurrentFocusOwnerIsChild(table);
        }
    }

    private static void requestFocusOnControlOnlyIfCurrentFocusOwnerIsChild(Control c) {
        Scene scene = c.getScene();
        final Node focusOwner = scene == null ? null : scene.getFocusOwner();
        if (focusOwner == null) {
            c.requestFocus();
        } else if (! c.equals(focusOwner)) {
            Parent p = focusOwner.getParent();
            while (p != null) {
                if (c.equals(p)) {
                    c.requestFocus();
                    break;
                }
                p = p.getParent();
            }
        }
    }

    @Override
    public void updateItem(S item, boolean empty) {
        super.updateItem(item, empty);
        if (empty || item == null) {
            setText(null);
            setGraphic(null);
        } else {
            setText(item.toString());
            if(_previousVal != null && _textField != null && !_previousVal.equals(_textField.getText()))
            {
                SCIMS.getFrame().setModified(true);
            }
            setGraphic(null);
        }
    }


    @SuppressWarnings("unchecked")
    void createTextField() {
        _textField = new TextField(getItem() == null ? null : getItem().toString());
        _textField.setMinWidth(this.getWidth() - this.getGraphicTextGap() * 2 - 5);
        _textField.setOnAction(evt -> commitEdit((S)_textField.getText()));
        _textField.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) {
                commitEdit((S)_textField.getText());
            }
        });
        setGraphic(_textField);
        _textField.selectAll();
        _textField.requestFocus();
        _textField.setEditable(isCellEditable(getTreeTableRow()));
    }

    boolean isCellEditable(TreeTableRow<?> row) {
       return true;
    }

}
