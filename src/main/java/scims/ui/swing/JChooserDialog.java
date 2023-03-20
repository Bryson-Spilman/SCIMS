package scims.ui.swing;



import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JChooserDialog<T> extends JDialog {

    private static final Logger LOGGER = Logger.getLogger(JChooserDialog.class.getName());
    private final List<T> _objects = new ArrayList<>();
    private final Consumer<List<T>> _chosenAction;
    private JTextField _textField;
    private JList<T> _leftList;
    private JList<T> _rightList;
    private JButton _addButton;
    private JButton _removeButton;
    private OkCancelPanel _okCancelPanel;
    private boolean _isModified;
    private DefaultListModel<T> _leftModel;
    private DefaultListModel<T> _rightModel;
    private final List<Runnable> _additionalOkActionEvents = new ArrayList<Runnable>();
    private String _delimeter = ";";

    public JChooserDialog(Window parentFrame, List<T> objects, Consumer<List<T>> chosenAction) {
        super(parentFrame, "Choose Weight Classes", ModalityType.APPLICATION_MODAL);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.X_AXIS));
        _chosenAction = chosenAction;
        setSize(500,500);
        setLocationRelativeTo(parentFrame);
        // Initialize components
        buildComponents();
        addListeners();
        setObjects(objects);
    }

    void setTextField(JTextField textField) {
        _textField = textField;
    }

    private void addListeners() {
        _addButton.addActionListener(e -> addButtonClicked());
        _removeButton.addActionListener(e -> removeButtonClicked());
        _okCancelPanel.addOkActionListener(e -> okAction());
        _okCancelPanel.addCancelActionListener(e -> closedAction());
    }

    private void okAction() {
        _chosenAction.accept(getSelectedObjects());
        setVisible(false);
        for(Runnable event : _additionalOkActionEvents) {
            event.run();
        }
    }

    private void closedAction() {
        int opt = JOptionPane.YES_OPTION;
        if(_isModified)
        {
            opt = JOptionPane.showConfirmDialog(this, "Cancel selection of weight classes?", "Confirm Cancel",
                    JOptionPane.YES_NO_OPTION);
        }
        if(opt == JOptionPane.YES_OPTION)
        {
            resetLists();
            dispose();
        }
    }

    private void resetLists() {
        _leftModel.clear();
        _rightModel.clear();
        for(T object : _objects) {
            _leftModel.addElement(object);
        }
        setSelectedFromTextField();
    }

    List<T> getSelectedObjects() {
        List<T> retVal = new ArrayList<>();
        for(int i=0; i < _rightModel.getSize(); i++)
        {
            retVal.add(_rightModel.getElementAt(i));
        }
        return retVal;
    }

    @Override
    public void setVisible(boolean visible) {
        if(visible)
        {
            _isModified = false;
            resetLists();
        }
        super.setVisible(visible);
    }

    private void setSelectedFromTextField() {
        String text = _textField.getText();
        if(text != null) {
            String[] split = text.split(_delimeter);
            for(String name : split) {
                name = name.trim();
                for(T object : _objects) {
                    if(object != null && object.toString().equalsIgnoreCase(name)) {
                        _rightModel.addElement(object);
                        if(_leftModel.contains(object)) {
                            _leftModel.removeElement(object);
                        }
                    }
                }
            }
        }
    }

    private void removeButtonClicked() {
        _isModified = true;
        List<T> selected = _rightList.getSelectedValuesList();
        DefaultListModel<Object> leftModel = (DefaultListModel<Object>) _leftList.getModel();
        DefaultListModel<Object> rightModel = (DefaultListModel<Object>) _rightList.getModel();
        for(Object item : selected) {
            leftModel.addElement(item);
        }
        int[] selectedIndices = _rightList.getSelectedIndices();
        for (int i = selectedIndices.length - 1; i >= 0; i--) {
            rightModel.removeElementAt(selectedIndices[i]);
        }
    }

    private void addButtonClicked() {
        _isModified = true;
        List<T> selected = _leftList.getSelectedValuesList();
        DefaultListModel<Object> rightModel = (DefaultListModel<Object>) _rightList.getModel();
        DefaultListModel<Object> leftModel = (DefaultListModel<Object>) _leftList.getModel();
        for(Object item : selected) {
            rightModel.addElement(item);
        }
        int[] selectedIndices = _leftList.getSelectedIndices();
        for (int i = selectedIndices.length - 1; i >= 0; i--) {
            leftModel.removeElementAt(selectedIndices[i]);
        }
    }

    private void buildComponents() {
        _leftModel = new DefaultListModel<>();
        _rightModel = new DefaultListModel<>();

        for (T object : _objects) {
            _leftModel.addElement(object);
        }

        _leftList = new JList<>(_leftModel);
        _rightList = new JList<>(_rightModel);

        _addButton = new JButton("<html>Add \u25BA</html>");
        _removeButton = new JButton("<html>\u25C4Remove</html>");
        _okCancelPanel = new OkCancelPanel();

        JPanel buttonPanel = new JPanel();
        JPanel addButtonPanel = new JPanel(new BorderLayout());
        addButtonPanel.add(_addButton, BorderLayout.SOUTH);
        JPanel removeButtonPanel = new JPanel(new BorderLayout());
        removeButtonPanel.add(_removeButton, BorderLayout.NORTH);
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.add(addButtonPanel);
        buttonPanel.add(removeButtonPanel);
        buttonPanel.setMaximumSize(new Dimension(50, Integer.MAX_VALUE));

        JPanel leftPanel = new JPanel(new BorderLayout());
        JPanel rightPanel = new JPanel(new BorderLayout());

        JScrollPane leftScrollPane = new JScrollPane(_leftList);
        leftScrollPane.setMinimumSize(new Dimension(100, Integer.MIN_VALUE));
        leftScrollPane.setBorder(BorderFactory.createEmptyBorder(5,5, 5 + _okCancelPanel.getPreferredSize().height,5));
        leftPanel.add(leftScrollPane, BorderLayout.CENTER);
        getContentPane().add(leftPanel);
        getContentPane().add(buttonPanel);
        JScrollPane rightScrollPane = new JScrollPane(_rightList);
        rightScrollPane.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        rightScrollPane.setPreferredSize(leftScrollPane.getPreferredSize());
        rightScrollPane.setMinimumSize(leftScrollPane.getMinimumSize());
        rightPanel.add(rightScrollPane, BorderLayout.CENTER);
        rightPanel.add(_okCancelPanel, BorderLayout.SOUTH);
        getContentPane().add(rightPanel);

        _rightList.setDragEnabled(true);
        _rightList.setTransferHandler(new ChooserTransferHandler<>(_rightList, _rightModel));

        _rightList.setDropMode(DropMode.ON);
    }

    public void setObjects(List<T> objects) {
        _objects.clear();
        _objects.addAll(objects);
        _leftModel.clear();
        _rightModel.clear();
        for(T object : objects) {
            _leftModel.addElement(object);
        }
    }

    public void addOnSelectionEvent(Runnable event) {
        _additionalOkActionEvents.add(event);
    }

    public void setSelectedObjects(List<T> selectedObjects) {
        resetLists();
        for(T object : selectedObjects) {
            _rightModel.addElement(object);
            if(_leftModel.contains(object)) {
                _leftModel.removeElement(object);
            }
        }
    }

    public List<T> getObjects() {
        return _objects;
    }

    public void setDelimeter(String delimeter) {
        _delimeter = delimeter;
    }

    private static class ChooserTransferHandler<T> extends TransferHandler {
        private final JList<T> _list;
        private final DefaultListModel<T> _model;
        int _index;

        ChooserTransferHandler(JList<T> list, DefaultListModel<T> model) {
            _list = list;
            _model = model;
        }
        @Override
        public int getSourceActions(JComponent comp) {
            return COPY_OR_MOVE;
        }

        @Override
        public Transferable createTransferable(JComponent comp) {
            _index = _list.getSelectedIndex();
            return new StringSelection(_list.getSelectedValue().toString());
        }


        @Override
        public void exportDone( JComponent comp, Transferable trans, int action ) {
        }

        @Override
        public boolean canImport(TransferHandler.TransferSupport support) {
            // data of type string?
            return support.isDataFlavorSupported(DataFlavor.stringFlavor);
        }

        @Override
        public boolean importData(TransferHandler.TransferSupport support) {
            try {
                // convert data to string
                String s = (String)support.getTransferable().getTransferData(DataFlavor.stringFlavor);
                JList.DropLocation dl = (JList.DropLocation)support.getDropLocation();
                T data = _list.getSelectedValuesList().stream().filter(item -> item.toString().equalsIgnoreCase(s))
                        .findFirst().orElse(null);
                int indexFrom = -1;
                for(int i=0; i<_model.getSize(); i++) {
                    if(_model.getElementAt(i) == data) {
                        indexFrom = i;
                        break;
                    }
                }
                int moveToIndex = dl.getIndex();
                if(indexFrom >= 0) {
                    T temp = _model.get(moveToIndex);
                    _model.set(moveToIndex, _model.get(indexFrom));
                    _model.set(indexFrom, temp);
                }
                return true;
            }
            catch (UnsupportedFlavorException | IOException e) {
                LOGGER.log(Level.CONFIG, e.getMessage(), e);
            }
            return false;
        }
    }
}
