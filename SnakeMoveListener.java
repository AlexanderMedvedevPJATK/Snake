public interface SnakeMoveListener {
    boolean moveSnake(MoveEvent evt) throws Exception;
    boolean moveHead(MoveEvent evt) throws Exception;
    void spawnFood();
}
