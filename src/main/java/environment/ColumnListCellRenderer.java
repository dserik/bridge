package environment;

import java.awt.*;
import javax.swing.*;

public class ColumnListCellRenderer extends DefaultListCellRenderer {

    @Override
    public Component getListCellRendererComponent(JList list, Object data, int idx, boolean isSelected, boolean hasFocus) {

        if (data instanceof ColumnListElement) {
            ColumnListElement lie = (ColumnListElement) data;

            Icon icon = lie.icon;
            String text = lie.columnName;

            JLabel label = (JLabel) super.getListCellRendererComponent(list, text, idx, isSelected, hasFocus);
            label.setIcon(icon);
            return label;
        } else {
            return super.getListCellRendererComponent(list, data, idx, isSelected, hasFocus);
        }
    }
}
