import java.util.EventObject;

public class ScoreChangeEvent extends EventObject {
    int score;
    public ScoreChangeEvent(Object source, int score) {
        super(source);
        this.score = score;
    }

    public int getScore() {
        return score;
    }
}
