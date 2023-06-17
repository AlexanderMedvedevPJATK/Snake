import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class MyPanel extends JPanel implements TableCellRenderer {
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        switch (Integer.parseInt(value.toString())) {
            case 0 -> setBackground(Color.DARK_GRAY);
            case 1 -> setBackground(Color.GREEN);
            case 2 -> setBackground(new Color(67, 162, 53));
            case 3 -> setBackground(Color.RED);
            case 4 -> setBackground(Color.MAGENTA);
        }
        return this;
    }
}
