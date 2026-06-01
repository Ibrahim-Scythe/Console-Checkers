import java.util.ArrayList;

class Piece {
    int state; // White: 1, Black: -1
    boolean isKing;
    Coordinate currentPos;
    ArrayList<Coordinate> possibleMoves;

    public Piece(int state, int x, int y) {
        this.state = state;
        isKing = false;
        currentPos = Coordinate.newCoordinate(x, y);
        possibleMoves = null;
    }

    public char getSymbol() {
        if (state == 1 && isKing) return '♚';
        if (state == -1 && isKing) return '♔';

        if (state == 1) return '●';
        else return '○';
    }

    public void promote() { isKing = true; }
}