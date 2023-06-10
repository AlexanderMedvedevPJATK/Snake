import javax.swing.table.AbstractTableModel;

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
        spawnFood();
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

    // <---------- SNAKE MOVEMENT AND FOOD ---------->

    public void spawnFood() {
        int x = (int)(Math.random() * WIDTH);
        int y = (int)(Math.random() * HEIGHT);
        while(Integer.parseInt(getValueAt(y,x).toString()) == 3 ||
              Integer.parseInt(getValueAt(y,x).toString()) == 2 ||
              Integer.parseInt(getValueAt(y,x).toString()) == 1) {
            x = (int)(Math.random() * WIDTH);
            y = (int)(Math.random() * HEIGHT);
        }
        setValueAt(3, y, x);
        fireTableCellUpdated(y, x);
    }

    public boolean checkFood(Object valueAt) {
        return Integer.parseInt(valueAt.toString()) == 3;
    }

    @Override
    public boolean moveSnake(MoveEvent evt) throws Exception {

        // place next to head
        setValueAt(1, evt.getPieces().get(0)[0], evt.getPieces().get(0)[1]);
        fireTableCellUpdated(evt.getPieces().get(0)[0], evt.getPieces().get(0)[1]);

        boolean food = moveHead(evt);

        // clean tail cell
        setValueAt(0, evt.getPieces().get(
                        evt.getPieces().size() - 1)[0],
                        evt.getPieces().get(evt.getPieces().size() - 1)[1]);

        fireTableCellUpdated(evt.getPieces().get(
                        evt.getPieces().size() - 1)[0],
                        evt.getPieces().get(evt.getPieces().size() - 1)[1]);

        return food;
    }
    @Override
    public boolean moveHead(MoveEvent evt) throws Exception {
        boolean food = false;
        int x = evt.getPieces().get(0)[1];
        int y = evt.getPieces().get(0)[0];
        switch (evt.getDirection()) {
            case 0 -> {
                food = checkFood(getValueAt(y - 1, x));

                if(Integer.parseInt(getValueAt(y - 1, x).toString()) == 1)
                    throw new Exception();

                setValueAt(2, y - 1, x);
                fireTableCellUpdated(y - 1, x);
            }
            case 1 -> {
                food = checkFood(getValueAt(y, x + 1));

                if(Integer.parseInt(getValueAt(y, x + 1).toString()) == 1)
                    throw new Exception();

                setValueAt(2, y, x + 1);
                fireTableCellUpdated(y, x + 1);
            }
            case 2 -> {
                food = checkFood(getValueAt(y + 1, x));

                if(Integer.parseInt(getValueAt(y + 1, x).toString()) == 1)
                    throw new Exception();

                setValueAt(2, y + 1, x);
                fireTableCellUpdated(y + 1, x);
            }
            case 3 -> {
                food = checkFood(getValueAt(y, x - 1));

                if(Integer.parseInt(getValueAt(y, x - 1).toString()) == 1)
                    throw new Exception();

                setValueAt(2, y, x - 1);
                fireTableCellUpdated(y, x - 1);
            }
        }
        setValueAt(1, y, x);
        fireTableCellUpdated(y, x);

        return food;
    }
}
