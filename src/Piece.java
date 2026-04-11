import java.util.ArrayList;

class Piece {
    int state; // White: 1, Black: -1
    boolean isKing;
    Coordinate currentPos;
    ArrayList<Coordinate> possibleMoves;

    public Piece(int state, int y, int x) {
        this.state = state;
        isKing = false;
        currentPos = Coordinate.newCoordinate(y, x);
        possibleMoves = null;
    }

    public void promote() { isKing = true; }
}