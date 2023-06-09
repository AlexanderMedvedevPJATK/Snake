import java.util.EventObject;

public class MoveEvent extends EventObject {
    private int direction;
    private int[][] pieces;
    private boolean draw;
    public MoveEvent(Object source, int direction, int[][] pieces, boolean draw) {
        super(source);
        this.direction = direction;
        this.pieces = pieces;
        this.draw = draw;
    }

    public MoveEvent(Object source, int direction, int[][] pieces) {
        super(source);
        this.direction = direction;
        this.pieces = pieces;
    }

    public int getDirection() {
        return direction;
    }

    public int[][] getPieces() {
        return pieces;
    }

    public boolean isDraw() {
        return draw;
    }
}
