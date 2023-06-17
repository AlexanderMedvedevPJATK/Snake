public interface FieldEventListener {
    void fireMoveSnake(MoveEvent evt);
    void fireMoveHead(MoveEvent evt);
    void fireSpawnFood();
}
