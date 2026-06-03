import java.util.ArrayList;

class Piece {
    private final int state; // White: 1, Black: -1
    private boolean isKing;
    private Coordinate currentPos;
    private ArrayList<Move> possibleMoves;

    public Piece(int state, int x, int y) {
        this.state = state;
        isKing = false;
        currentPos = Coordinate.newCoordinate(x, y);
        possibleMoves = null;
    }

    public int getState() {
        return state;
    }

    public boolean isKing() {
        return isKing;
    }

    public void promote() {
        isKing = true;
    }

    public Coordinate getPos() {
        return currentPos;
    }

    public void updatePos(Coordinate c) {
        this.currentPos = c;
    }

    public void updateMoves(ArrayList<Move> moves) {
        this.possibleMoves = moves;
    }

    public char getSymbol() {
        if (state == 1 && isKing) return '♚';
        if (state == -1 && isKing) return '♔';

        if (state == 1) return '◉';
        else return '◎';
    }

    public boolean isValidMove(Coordinate c) {
        if (c == null) return false;
        for (Move m : possibleMoves) {
            if (c.equals(m.getDestination())) return true;
        }
        return false;
    }

    public Move getMoveTo(Coordinate c) {
        for (Move m : possibleMoves) {
            if (c.equals(m.getDestination())) return m;
        }
        return null;
    }

    // Checks if a non-king piece is at the end of the board for its respective side
    public boolean canPromote() {
        if (isKing) return false;
        return (state == 1 && currentPos.getY() == 7) || (state == -1 && currentPos.getY() == 0);
    }
}