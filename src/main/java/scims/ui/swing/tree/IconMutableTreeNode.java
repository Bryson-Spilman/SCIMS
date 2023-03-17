package scims.ui.swing.tree;

import javax.swing.tree.DefaultMutableTreeNode;
import java.net.URL;

public class IconMutableTreeNode extends DefaultMutableTreeNode {
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
}
