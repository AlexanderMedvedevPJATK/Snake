import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Main extends JFrame {
    public Main() {
        TopScorePanel topScorePanel = new TopScorePanel();
        topScorePanel.setSize(750, 510);
        topScorePanel.setBackground(Color.DARK_GRAY);
        topScorePanel.setVisible(false);
        topScorePanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }
        });

        ScorePanel scorePanel = new ScorePanel();
        scorePanel.setPreferredSize(new Dimension(750, 30));

        JTable fieldTable = new JTable();
        fieldTable.setRowHeight(450 / 16);
//        fieldTable.setEnabled(false);
//        fieldTable.setRowSelectionAllowed(false);
//        fieldTable.setColumnSelectionAllowed(false);
//        fieldTable.setCellSelectionEnabled(false);

        Field field = new Field();
        fieldTable.setModel(field);

        MyPanel panel = new MyPanel();
        panel.setFocusable(true);

        Snake snake = new Snake();

        fieldTable.addKeyListener(snake);

        field.setSnake(snake);
        snake.setField(field);
        snake.setScoreListener(scorePanel);
        snake.setTopScoreListener(topScorePanel);

        fieldTable.setDefaultRenderer(Integer.class, panel);

        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        add(scorePanel);
        add(fieldTable);
        getLayeredPane().add(topScorePanel);
        setSize(750, 510);
        setVisible(true);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                snake.start();
            }
        });
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::new);
    }
}