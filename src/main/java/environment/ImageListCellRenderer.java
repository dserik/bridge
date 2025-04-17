
package environment;

import javax.swing.*;
import java.awt.*;

public class ImageListCellRenderer extends DefaultListCellRenderer {

    @Override
    public Component getListCellRendererComponent(
            JList list, Object data, int idx, boolean isSelected, boolean hasFocus) {
        if (data instanceof ImageListElement) {
            ImageListElement lie = (ImageListElement) data;
            Icon icon = lie.icon;
            String text = lie.text;
            JLabel label = (JLabel)
                    super.getListCellRendererComponent(list, text, idx, isSelected, hasFocus);
            label.setIcon(icon);
            return label;
        } else
            return super.getListCellRendererComponent(list, data, idx, isSelected, hasFocus);
    }
} 