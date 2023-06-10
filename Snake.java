import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Snake extends Thread implements KeyListener {
//    private int direction;
    private int length;
    private final List<int[]> piecesList = new ArrayList<>();
    private final int[] movementBuffer;
    private int bufferContains;
    boolean bufferUtilized;
    private final SnakeMoveListener field;

    public Snake(SnakeMoveListener field) {
        this.field = field;

        // 3 initial elements for head, next to head and tail
        
        piecesList.add(new int[] {0, 0});
        piecesList.add(new int[] {0, 0});
        piecesList.add(new int[] {0, 0});

        // this.direction = 1; // 0 - UP, 1 - RIGHT, 2 - DOWN, 3 - LEFT
        this.bufferContains = 1;
        this.bufferUtilized = true;
        this.movementBuffer = new int[] {1, -1};

        this.length = 1;
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
                    foodFound = field.moveHead(new MoveEvent(this, movementBuffer[0], piecesList));
                    length++;
                    System.out.println(length);
                    foodEaten = true;
                } else {
                    foodFound = field.moveSnake(new MoveEvent(this, movementBuffer[0], piecesList));
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
            switch (movementBuffer[0]) { // change head position
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
            if(bufferUtilized) removeDirection();
            bufferUtilized = true;
        }
    }
    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_UP) {
            addDirection(0);
        } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            addDirection(2);
        } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            addDirection(3);
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            addDirection(1);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    public boolean invalidDirection(int direction, int index) {
        return (direction == movementBuffer[index] ||
                direction == 0 && movementBuffer[index] == 2 ||
                direction == 1 && movementBuffer[index] == 3 ||
                direction == 2 && movementBuffer[index] == 0 ||
                direction == 3 && movementBuffer[index] == 1);
    }

    synchronized public void addDirection(int direction) {
        if(bufferUtilized && bufferContains == 1) {

            if(invalidDirection(direction, 0)) return;

            movementBuffer[0] = direction;
            bufferUtilized = false;
        } else if(bufferContains == 1) {
            System.out.println("Entered 1");

            if(invalidDirection(direction, 0)) return;

            System.out.println("Direction added");
            movementBuffer[1] = direction;
            bufferContains++;
            bufferUtilized = false;
        } else {
            System.out.println("Entered 2");

            if(invalidDirection(direction, 1)) return;

            movementBuffer[0] = movementBuffer[1];
            movementBuffer[1] = direction;

            System.out.println("Direction replaced");
            bufferUtilized = false;
        }
    }

    synchronized public void removeDirection() {
        if(bufferContains == 2) {
            movementBuffer[0] = movementBuffer[1];
            movementBuffer[1] = -1;
            bufferContains--;
        }
    }
}
