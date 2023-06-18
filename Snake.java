import javax.swing.JOptionPane;
import javax.xml.crypto.Data;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Snake extends Thread implements KeyListener, SnakeEventListener {
//    private int direction;
    private boolean foodFound;
    private int length;
    private int score;
    private final List<int[]> piecesList = new ArrayList<>();
    private final int[] movementBuffer;
    private int bufferContains;
    private boolean bufferUtilized;
    private FieldEventListener field;
    private ScoreEventListener scorePanel;
    private TopScoreListener topScoreListener;

    public Snake() {
        // 3 initial elements for head, next to head and tail
        
        piecesList.add(new int[] {0, 0});

        // this.direction = 1; // 0 - UP, 1 - RIGHT, 2 - DOWN, 3 - LEFT
        this.bufferContains = 1;
        this.bufferUtilized = true;
        this.movementBuffer = new int[] {1, -1};

        this.length = 1;
    }

    public void setField(FieldEventListener field) {
        this.field = field;
    }
    public void setScoreListener(ScoreEventListener scorePanel) {
        this.scorePanel = scorePanel;
    }
    public void setTopScoreListener(TopScoreListener topScoreListener) {
        this.topScoreListener = topScoreListener;
    }

    @Override
    public void run() {
        boolean foodEaten;
        while(!isInterrupted()) {
            foodEaten = false;
            // moving snake
            if(foodFound) {
                foodFound = false;
                field.fireSpawnFood();
                field.fireMoveHead(new MoveEvent(this, movementBuffer[0], piecesList));
                score += ((length++ - 1) / 10 + 1);
                scorePanel.fireScoreChange(new ScoreChangeEvent(this, score));
                foodEaten = true;
            } else {
                field.fireMoveSnake(new MoveEvent(this, movementBuffer[0], piecesList));
            }

            // add new segment
            if(foodEaten)
                piecesList.add(1, new int[] {piecesList.get(0)[0], piecesList.get(0)[1]});

            // changing snake's body coordinates after the move
            if(!foodEaten) {
                for (int i = length - 1; i > 0; i--) {
                    piecesList.get(i)[0] = piecesList.get(i - 1)[0];
                    piecesList.get(i)[1] = piecesList.get(i - 1)[1];
                }
            }

            // change head position
            switch (movementBuffer[0]) {
                case 0 -> piecesList.get(0)[0]--;
                case 1 -> piecesList.get(0)[1]++;
                case 2 -> piecesList.get(0)[0]++;
                case 3 -> piecesList.get(0)[1]--;
            }

            // game tick
            try {
                Thread.sleep(200 - length * 3L);
            } catch (InterruptedException e) {
                interrupt();
            }

            if(bufferUtilized) removeDirection();
            bufferUtilized = true;
        }
    }

    @Override
    public void fireFoodFound() {
        foodFound = true;
    }

    @Override
    public void fireCollision() {
        System.out.println("Game over!");
        readScores();
        interrupt();
    }

    private String getCurrentScore() {
        String nickname = "";
        try(FileOutputStream fos = new FileOutputStream("scores.bin", true)) {
            while (nickname == null || nickname.trim().equals("")) {
                nickname = JOptionPane.showInputDialog(
                        null,
                        "New high score! Enter nickname",
                        "New high score",
                        JOptionPane.PLAIN_MESSAGE);
                if (nickname == null) {
                    int result = JOptionPane.showConfirmDialog(null,
                            "The result will be lost!", "Confirmation", JOptionPane.YES_NO_OPTION);
                    if(result == JOptionPane.YES_OPTION) {
                        break;
                    }
                } else if (nickname.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(null,
                            "Nickname should not be empty",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                } else if (nickname.length() > 15) {
                    JOptionPane.showMessageDialog(null,
                            "Nickname cannot contain more than 15 characters",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                    nickname = "";
                } else {
//                    char[] nicknameArr = nickname.trim().toCharArray();
//                    fos.write(nicknameArr.length);
//                    for(char ch : nicknameArr) {
//                        fos.write(ch >> 8);
//                        fos.write(ch);
//                    }
//                    for (int i = Integer.BYTES - 1; i >= 0; i--) {
//                        fos.write(score >> (8 * i));
//                        System.out.println(score >> (8 * i));
//                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return nickname + " " + score;
    // topScoreListener.showTopScore(new TopScoreEvent(this, scores));
    }

    public void readScores() {
        boolean added = false;
        List<String> scores = new ArrayList<>();
        try(FileInputStream fos = new FileInputStream("scores.bin")) {
            int data;
            int recordScore = 0;
            int bytesCounter = 3;
            StringBuilder scoreRecord = new StringBuilder();
            while((data = fos.read()) != -1) {
                scoreRecord.setLength(0);
                for (int i = 0; i < data; i++) {
                    char ch = (char)(fos.read() << 8 | fos.read());
                    scoreRecord.append(ch);
                }
                for(int i = Integer.BYTES - 1; i >= 0; i--) {
                    recordScore = fos.read() << 8 * bytesCounter--;
                }
                scoreRecord.append(" ").append(recordScore);

                if (recordScore < score && !added) {
                    scores.add(getCurrentScore());
                    added = true;
                }

                scores.add(String.valueOf(scoreRecord));
//                boolean added = false;
//                for (int i = 0; i < scores.size(); i++) {
//                    String[] recordArr = scores.get(i).split(" ");
//                    if (Integer.parseInt(recordArr[recordArr.length - 1]) < score) {
//
//                        scores.add(i, String.valueOf(scoreRecord));
//                        added = true;
//                        if (scores.size() > 10) scores.remove(10);
//                        break;
//                    }
//                }
//                if (!added && scores.size() < 10) {
//                    scores.add(String.valueOf(scoreRecord));
//                }
            }
            saveScores(scores);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(scores.isEmpty() || scores.size() < 10 && !added) {
            scores.add(getCurrentScore());
            added = true;
        }
        if(added) saveScores(scores);
        topScoreListener.showTopScore(new TopScoreEvent(this, scores));
    }

    public void saveScores(List<String> scores) {
        System.out.println("called");
        try(FileOutputStream fos = new FileOutputStream("scores.bin")) {
            for (String record : scores) {
                String[] recordArr = record.split(" ");
                StringBuilder nickname = new StringBuilder();
                int recordScore = Integer.parseInt(recordArr[recordArr.length - 1]);

                for (int i = 0; i < recordArr.length - 1; i++) {
                    nickname.append(recordArr[i]).append(" ");
                }

                char[] nicknameArr = String.valueOf(nickname).trim().toCharArray();
                fos.write(nicknameArr.length);
                for(char ch : nicknameArr) {
                    fos.write(ch >> 8);
                    fos.write(ch);
                }
                for (int i = Integer.BYTES - 1; i >= 0; i--) {
                    fos.write(recordScore >> (8 * i));
                    System.out.println(recordScore >> (8 * i));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
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

            if(invalidDirection(direction, 0)) return;

            movementBuffer[1] = direction;
            bufferContains++;
            bufferUtilized = false;
        } else {

            if(invalidDirection(direction, 1)) return;

            movementBuffer[0] = movementBuffer[1];
            movementBuffer[1] = direction;

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