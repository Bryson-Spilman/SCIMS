package scims.ui.swing.tree;

import javax.swing.*;
import java.net.URL;

public class IconNode {
    private final Object _userObject;
    private ImageIcon _icon;

    public IconNode(Object userObject, URL iconImageFile) {
        _userObject = userObject;
        if(iconImageFile != null) {
            _icon = new ImageIcon(iconImageFile);
        }

    }

    public Object getUserObject() {
        return _userObject;
    }

    public ImageIcon getIcon() {
        return _icon;
    }

    @Override
    public String toString() {
        return _userObject.toString();
    }
}