import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

public class Snake extends Thread implements KeyListener {
    private int direction;
    private int length;
    private final List<int[]> piecesList = new ArrayList<>();
    private final int[] movementBuffer;
    private int bufferContains;
    private final SnakeMoveListener field;

    public Snake(SnakeMoveListener field) {
        this.field = field;

        // 3 initial elements for head, next to head and tail
        
        piecesList.add(new int[] {0, 0});
        piecesList.add(new int[] {0, 0});
        piecesList.add(new int[] {0, 0});

        this.direction = 1; // 0 - UP, 1 - RIGHT, 2 - DOWN, 3 - LEFT
        this.length = 1;
        this.movementBuffer = new int[2];
    }



    @Override
    public void run() {
        boolean foodEaten;
        boolean foodFound = false;
        while(!isInterrupted()) {
            foodEaten = false;
            try {
                if(foodFound) {
                    field.spawnFood();
                    foodFound = field.moveHead(new MoveEvent(this, direction, piecesList));
                    length++;
                    System.out.println(length);
                    foodEaten = true;
                } else {
                    foodFound = field.moveSnake(new MoveEvent(this, direction, piecesList));
                }
            } catch (Exception e) {
                System.out.println("Game over!");
                System.exit(0);
                break;
            }
            if(length != 1) {
                if (length == 3) {
                    piecesList.get(2)[0] = piecesList.get(1)[0];
                    piecesList.get(2)[1] = piecesList.get(1)[1];
                    piecesList.get(1)[0] = piecesList.get(0)[0];
                    piecesList.get(1)[1] = piecesList.get(0)[1];
                } else if (length == 2) {
                    piecesList.get(1)[0] = piecesList.get(0)[0];
                    piecesList.get(1)[1] = piecesList.get(0)[1];
                    piecesList.get(2)[0] = piecesList.get(0)[0];
                    piecesList.get(2)[1] = piecesList.get(0)[1];
                } else if(!foodEaten) {
                    for (int i = piecesList.size() - 1; i > 0; i--) {
                        piecesList.get(i)[0] = piecesList.get(i - 1)[0];
                        piecesList.get(i)[1] = piecesList.get(i - 1)[1];
                    }
                }
            }
            if(length > 3 && foodEaten) piecesList.add(1,
                    new int[] {piecesList.get(0)[0], piecesList.get(0)[1]});
            switch (direction) { // change head position
                case 0 -> piecesList.get(0)[0]--;
                case 1 -> piecesList.get(0)[1]++;
                case 2 -> piecesList.get(0)[0]++;
                case 3 -> piecesList.get(0)[1]--;
            }
            if(length == 1) {
                piecesList.get(1)[0] = piecesList.get(0)[0];
                piecesList.get(1)[1] = piecesList.get(0)[1];
                piecesList.get(2)[0] = piecesList.get(1)[0];
                piecesList.get(2)[1] = piecesList.get(1)[1];
            }
            try {
                Thread.sleep(250 - length * 4L);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_UP) {
            if(direction != 2) direction = 0;
        } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            if(direction != 0) direction = 2;
        } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            if(direction != 1) direction = 3;
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            if(direction != 3) direction = 1;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }


}
