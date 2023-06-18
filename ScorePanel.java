import javax.swing.*;
import java.awt.*;

public class ScorePanel extends JPanel implements ScoreEventListener {
    int score;
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.BLACK);
        g.setFont(new Font(null, Font.BOLD, 20));
        FontMetrics fontMetrics = getFontMetrics(g.getFont());
        g.drawString("Score: " + score,
                (getWidth() - fontMetrics.stringWidth("Score: " + score)) / 2,
                22);
    }

    @Override
    public void fireScoreChange(ScoreChangeEvent sce) {
        score = sce.getScore();
        repaint();
    }
}
