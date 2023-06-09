import javax.swing.table.AbstractTableModel;
import java.util.Arrays;

public class Field extends AbstractTableModel implements SnakeMoveListener {
    int[][] field;
    private final int WIDTH = 25;
    private final int HEIGHT = 16;

    public Field() {
        // 0 = Empty, 1 = Snake Body, 2 = Snake Head, 3 = Food
        field = new int[HEIGHT][WIDTH];

        for (int i = 0; i < HEIGHT; i++)
            for (int j = 0; j < WIDTH; j++)
                field[i][j] = 0;
        field[0][0] = 2;
        field[3][4] = 3;
        field[5][8] = 3;
        field[15][7] = 3;
    }

    @Override
    public int getRowCount() {
        return HEIGHT;
    }

    @Override
    public int getColumnCount() {
        return WIDTH;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return field[rowIndex][columnIndex];
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return Integer.class;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        this.field[rowIndex][columnIndex] = Integer.parseInt(aValue.toString());
    }

    // <---------- SNAKE MOVEMENT ---------->

    public boolean checkFood(Object valueAt) {
        return Integer.parseInt(valueAt.toString()) == 3;
    }

    @Override
    public boolean moveSnake(MoveEvent evt) {
        boolean food = false;

        // place next to head
        setValueAt(1, evt.getPieces()[0][0], evt.getPieces()[0][1]);
        fireTableCellUpdated(evt.getPieces()[0][0], evt.getPieces()[0][1]);

        switch (evt.getDirection()) {
            case 0 -> {
                food = checkFood(getValueAt(evt.getPieces()[0][0] - 1, evt.getPieces()[0][1]));
                // move head
                setValueAt(2, evt.getPieces()[0][0] - 1, evt.getPieces()[0][1]);
                fireTableCellUpdated(evt.getPieces()[0][0] - 1, evt.getPieces()[0][1]);
            }
            case 1 -> {
                // move head
                food = checkFood(getValueAt(evt.getPieces()[0][0], evt.getPieces()[0][1] + 1));
                setValueAt(2, evt.getPieces()[0][0], evt.getPieces()[0][1] + 1);
                fireTableCellUpdated(evt.getPieces()[0][0], evt.getPieces()[0][1] + 1);

//                if(evt.getPieces()[1][0] != -1) {
//                    setValueAt(1, evt.getPieces()[0][0], evt.getPieces()[0][1]);
//                    fireTableCellUpdated(evt.getPieces()[0][0], evt.getPieces()[0][1]);
//                }
            }
            case 2 -> {
                // move head
                food = checkFood(getValueAt(evt.getPieces()[0][0] + 1, evt.getPieces()[0][1]));
                setValueAt(2, evt.getPieces()[0][0] + 1, evt.getPieces()[0][1]);
                fireTableCellUpdated(evt.getPieces()[0][0] + 1, evt.getPieces()[0][1]);
            }
            case 3 -> {
                // move head
                food = checkFood(getValueAt(evt.getPieces()[0][0], evt.getPieces()[0][1] - 1));
                setValueAt(2, evt.getPieces()[0][0], evt.getPieces()[0][1] - 1);
                fireTableCellUpdated(evt.getPieces()[0][0], evt.getPieces()[0][1] - 1);
            }
        }
        if(true) { // if food, then we don't remove the tail, but move the head
            setValueAt(0, evt.getPieces()[2][0], evt.getPieces()[2][1]);
            fireTableCellUpdated(evt.getPieces()[2][0], evt.getPieces()[2][1]);
        }
        for (int[] pieces: evt.getPieces()) {
            System.out.print(Arrays.toString(pieces) + " ");
        }
        System.out.println();
        return food;
    }
    @Override
    public boolean moveHead(MoveEvent evt) {
        boolean food = false;
        switch (evt.getDirection()) {
            case 0 -> {
                food = checkFood(getValueAt(evt.getPieces()[0][0] - 1, evt.getPieces()[0][1]));

                setValueAt(2, evt.getPieces()[0][0] - 1, evt.getPieces()[0][1]);
                fireTableCellUpdated(evt.getPieces()[0][0] - 1, evt.getPieces()[0][1]);
            }
            case 1 -> {
                food = checkFood(getValueAt(evt.getPieces()[0][0], evt.getPieces()[0][1] + 1));
                setValueAt(2, evt.getPieces()[0][0], evt.getPieces()[0][1] + 1);
                fireTableCellUpdated(evt.getPieces()[0][0], evt.getPieces()[0][1] + 1);
            }
            case 2 -> {
                food = checkFood(getValueAt(evt.getPieces()[0][0] + 1, evt.getPieces()[0][1]));
                setValueAt(2, evt.getPieces()[0][0] + 1, evt.getPieces()[0][1]);
                fireTableCellUpdated(evt.getPieces()[0][0] + 1, evt.getPieces()[0][1]);
            }
            case 3 -> {
                food = checkFood(getValueAt(evt.getPieces()[0][0], evt.getPieces()[0][1] - 1));
                setValueAt(2, evt.getPieces()[0][0], evt.getPieces()[0][1] - 1);
                fireTableCellUpdated(evt.getPieces()[0][0], evt.getPieces()[0][1] - 1);
            }
        }
        setValueAt(1, evt.getPieces()[0][0], evt.getPieces()[0][1]);
        fireTableCellUpdated(evt.getPieces()[0][0], evt.getPieces()[0][1]);
        return food;
    }
}
