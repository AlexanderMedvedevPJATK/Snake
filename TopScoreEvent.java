import java.util.EventObject;
import java.util.List;

public class TopScoreEvent extends EventObject {
    private List<String> scores;
    public TopScoreEvent(Object source, List<String> scores) {
        super(source);
        this.scores = scores;
    }

    public List<String> getScores() {
        return scores;
    }
}
