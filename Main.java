import javax.swing.*;

public class Main extends JFrame {

    public Main() {
        setSize(750, 480);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::new);
    }
}