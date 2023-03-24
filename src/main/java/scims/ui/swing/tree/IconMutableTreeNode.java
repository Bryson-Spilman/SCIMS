package scims.ui.swing.tree;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;

public class IconMutableTreeNode extends DefaultMutableTreeNode {
    private JPopupMenu _popupMenu;

    public IconMutableTreeNode(Object object, URL iconImageFile) {
        super(buildIconNode(object, iconImageFile));
    }

    private static Object buildIconNode(Object object, URL iconImageFile) {
        Object retVal = object;
        if(iconImageFile != null) {
            retVal = new IconNode(object, iconImageFile);
        }
        return retVal;
    }


    public void setPopUpMenu(JPopupMenu popupMenu) {
       _popupMenu = popupMenu;
    }

    public JPopupMenu getPopupMenu() {
        return _popupMenu;
    }
}
