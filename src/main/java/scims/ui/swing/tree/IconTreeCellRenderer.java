package scims.ui.swing.tree;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.*;
import java.net.URL;

public class IconTreeCellRenderer extends DefaultTreeCellRenderer {

    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {

        super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);

        if (value instanceof DefaultMutableTreeNode) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;

            Image originalImage = null;
            // check if the node should have an icon
            if (node.getUserObject() instanceof IconNode) {
                IconNode iconNode = (IconNode) node.getUserObject();
                originalImage = iconNode.getIcon().getImage();
            }
            else {
                URL imgUrl = getClass().getResource("CompetitionsIcon.png");
                if(imgUrl != null) {
                    originalImage = new ImageIcon(imgUrl).getImage();
                }
            }
            if(originalImage != null)
            {
                Image scaledImage = originalImage.getScaledInstance(16, 16, java.awt.Image.SCALE_SMOOTH);
                ImageIcon scaledIcon = new ImageIcon(scaledImage);
                setIcon(scaledIcon);
            }

        }

        return this;
    }

}
