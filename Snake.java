import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Snake extends Thread implements KeyListener {
    private int direction;
    private int length;
    boolean draw;
    private int[][] pieces;
    private final SnakeMoveListener field;

    public Snake(SnakeMoveListener field) {
        this.field = field;

        // 4 arrays for storing info about X and Y coordinate of
        // head, next to head, second next to tail, next to tail, tail
        pieces = new int[][]{
                {0, 0}, {0, 0}, {0, 0}
        };



        this.direction = 1; // 0 - UP, 1 - RIGHT, 2 - DOWN, 3 - LEFT
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
                    foodFound = field.moveHead(new MoveEvent(this, direction, pieces));
                    length++;
                    foodEaten = true;
                } else {
                    foodFound = field.moveSnake(new MoveEvent(this, direction, pieces, draw));
                }
            } catch (ArrayIndexOutOfBoundsException ignored) {
                System.out.println("Game over!");
                System.exit(0);
                break;
            }
            System.out.println(length);
            if(length != 1) {
                if(pieces.length == 3) {
                    pieces[2][0] = pieces[1][0];
                    pieces[2][1] = pieces[1][1];
                }
                pieces[1][0] = pieces[0][0];
                pieces[1][1] = pieces[0][1];
                if (length < 3) {
                    pieces[2][0] = pieces[1][0];
                    pieces[2][1] = pieces[1][1];
                }
            }
            switch (direction) {
                case 0 -> {
                    pieces[0][0]--;

                }
                case 1 -> {
                    pieces[0][1]++;
                }
                case 2 -> {
                    pieces[0][0]++;
                }
                case 3 -> {
                    pieces[0][1]--;
                }
            }
            if(length == 1) {
                pieces[1][0] = pieces[0][0];
                pieces[1][1] = pieces[0][1];
                pieces[2][0] = pieces[1][0];
                pieces[2][1] = pieces[1][1];
            }
            try {
                Thread.sleep(300);
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
            System.out.println("Up arrow key pressed");
            if(direction != 2) direction = 0;
        } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            System.out.println("Down arrow key pressed");
            if(direction != 0) direction = 2;
        } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            System.out.println("Left arrow key pressed");
            if(direction != 1) direction = 3;
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            System.out.println("Right arrow key pressed");
            if(direction != 3) direction = 1;
        } else if (e.getKeyCode() == KeyEvent.VK_V) {
            draw = !draw;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
