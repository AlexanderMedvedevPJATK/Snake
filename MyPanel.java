import javax.swing.*;
import javax.swing.event.CellEditorListener;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.util.EventObject;

public class MyPanel extends JPanel implements TableCellRenderer, TableCellEditor {
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        switch (Integer.parseInt(value.toString())) {
            case 0 -> setBackground(Color.LIGHT_GRAY);
            case 1 -> setBackground(Color.GREEN);
            case 2 -> setBackground(new Color(67, 162, 53));
            case 3 -> setBackground(Color.RED);
            case 4 -> setBackground(Color.MAGENTA);
        }
        return this;
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        return null;
    }

    @Override
    public Object getCellEditorValue() {
        return this.getBackground();
    }

    @Override
    public boolean isCellEditable(EventObject anEvent) {
        return false;
    }

    @Override
    public boolean shouldSelectCell(EventObject anEvent) {
        return false;
    }

    @Override
    public boolean stopCellEditing() {
        return false;
    }

    @Override
    public void cancelCellEditing() {

    }

    @Override
    public void addCellEditorListener(CellEditorListener l) {

    }

    @Override
    public void removeCellEditorListener(CellEditorListener l) {

    }
}
