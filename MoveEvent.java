import java.util.EventObject;
import java.util.List;

public class MoveEvent extends EventObject {
    private final int direction;
    private final List<int[]> pieces;
    public MoveEvent(Object source, int direction, List<int[]> pieces) {
        super(source);
        this.direction = direction;
        this.pieces = pieces;
    }

    public int getDirection() {
        return direction;
    }

    public List<int[]> getPieces() {
        return pieces;
    }
}
