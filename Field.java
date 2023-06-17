import javax.swing.table.AbstractTableModel;
import java.util.Arrays;

public class Field extends AbstractTableModel implements FieldEventListener {
    int[][] field;
    private final int WIDTH = 25;
    private final int HEIGHT = 16;
    private SnakeEventListener snake;

    public Field() {
        // 0 = Empty, 1 = Snake Body and Border, 2 = Snake Head, 3 = Food
        // + 2 is needed for not visible border, used for collision check
        field = new int[HEIGHT + 2][WIDTH + 2];

        for (int i = 0; i < HEIGHT + 2; i++) {
            for (int j = 0; j < WIDTH + 2; j++) {
                if (j == 0 || i == 0 || j == WIDTH + 1 || i == HEIGHT + 1) {
                    field[i][j] = 1;
                } else {
                    field[i][j] = 0;
                }
            }
        }
        field[1][1] = 2;
        for (int i = 0; i < HEIGHT + 2; i++) {
            System.out.println(Arrays.toString(field[i]));
        }
        fireSpawnFood();
    }

    public void setSnake(SnakeEventListener snake) {
        this.snake = snake;
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
        return field[rowIndex + 1][columnIndex + 1];
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
        this.field[rowIndex + 1][columnIndex + 1] = Integer.parseInt(aValue.toString());
    }

    // <---------- SNAKE MOVEMENT AND FOOD ---------->

    public void fireSpawnFood() {
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

    public void checkFood(Object valueAt) {
        if(Integer.parseInt(valueAt.toString()) == 3) snake.fireFoodFound();
    }

    @Override
    public void fireMoveSnake(MoveEvent evt) {

        // place next to head
        setValueAt(1, evt.getPieces().get(0)[0], evt.getPieces().get(0)[1]);
        fireTableCellUpdated(evt.getPieces().get(0)[0], evt.getPieces().get(0)[1]);

        fireMoveHead(evt);

        // clean tail cell
        setValueAt(0, evt.getPieces().get(
                        evt.getPieces().size() - 1)[0],
                        evt.getPieces().get(evt.getPieces().size() - 1)[1]);

        fireTableCellUpdated(evt.getPieces().get(
                        evt.getPieces().size() - 1)[0],
                        evt.getPieces().get(evt.getPieces().size() - 1)[1]);
    }
    @Override
    public void fireMoveHead(MoveEvent evt){
        int x = evt.getPieces().get(0)[1];
        int y = evt.getPieces().get(0)[0];
        switch (evt.getDirection()) {
            case 0 -> {
                checkFood(getValueAt(y - 1, x));

                if(Integer.parseInt(getValueAt(y - 1, x).toString()) == 1)
                    snake.fireCollision();

                setValueAt(2, y - 1, x);
                fireTableCellUpdated(y - 1, x);
            }
            case 1 -> {
                 checkFood(getValueAt(y, x + 1));

                if(Integer.parseInt(getValueAt(y, x + 1).toString()) == 1)
                    snake.fireCollision();

                setValueAt(2, y, x + 1);
                fireTableCellUpdated(y, x + 1);
            }
            case 2 -> {
                checkFood(getValueAt(y + 1, x));

                if(Integer.parseInt(getValueAt(y + 1, x).toString()) == 1)
                    snake.fireCollision();

                setValueAt(2, y + 1, x);
                fireTableCellUpdated(y + 1, x);
            }
            case 3 -> {
                checkFood(getValueAt(y, x - 1));

                if(Integer.parseInt(getValueAt(y, x - 1).toString()) == 1)
                    snake.fireCollision();

                setValueAt(2, y, x - 1);
                fireTableCellUpdated(y, x - 1);
            }
        }
        setValueAt(1, y, x);
        fireTableCellUpdated(y, x);
    }
}
