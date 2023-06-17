import javax.swing.*;

public class Main extends JFrame {
    public Main() {

        JTable fieldTable = new JTable();
        fieldTable.setRowHeight(450 / 16);

        Field field = new Field();
        fieldTable.setModel(field);

        MyPanel panel = new MyPanel();
        panel.setFocusable(true);

        Snake snake = new Snake();
        fieldTable.addKeyListener(snake);
        snake.start();

        field.setSnake(snake);
        snake.setField(field);

        fieldTable.setDefaultRenderer(Integer.class, panel);

        add(fieldTable);
        setSize(750, 480);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::new);
    }
}