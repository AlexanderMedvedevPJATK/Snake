import javax.swing.*;
import java.awt.*;
import java.util.List;

public class TopScorePanel extends JPanel implements TopScoreListener {
    private List<String> scores;
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int x = this.getWidth() / 8;
        int y = 100;
        g.setColor(Color.WHITE);
        g.setFont(new Font("Comic Sans MS", Font.BOLD, 25));
        FontMetrics fontMetrics = getFontMetrics(g.getFont());
        g.drawString("Top Scores", (this.getWidth() - fontMetrics.stringWidth("Top Scores")) / 2, 50);
        for (int i = 0; i < scores.size(); i++) {
            if (i == 5) {
                x = this.getWidth() * 3 / 5;
                y = 100;
            }
            g.drawString(i + 1 + ". " + scores.get(i), x, y);
            y += 80;
            System.out.println(scores.get(i));
        }
        System.out.println("drawn");
    }


    @Override
    public void showTopScore(TopScoreEvent topScoreEvent) {
        this.scores = topScoreEvent.getScores();
        repaint();
        this.setVisible(true);
    }
}
