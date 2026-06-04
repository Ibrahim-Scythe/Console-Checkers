import java.util.ArrayList;
import java.util.Objects;

// Represents a Checkers piece
// Has a color, can be king and hold a coordinate of its current position and moves that can be made from that position
class Piece {
    private final int state; // White: 1, Black: -1
    private boolean isKing;
    private Coordinate currentPos;
    private ArrayList<Move> possibleMoves;

    // Constructor
    public Piece(int state, int x, int y) {
        this.state = state;
        isKing = false;
        currentPos = Coordinate.newCoordinate(x, y);
        possibleMoves = null;
    }

    // Returns the state of the piece
    public int getState() {
        return state;
    }

    // Promotes the piece to a King piece
    public void promote() {
        isKing = true;
    }

    // Returns the current position
    public Coordinate getPos() {
        return currentPos;
    }

    // Updates the current position to the given coordinate
    public void updatePos(Coordinate newPos) {
        this.currentPos = newPos;
    }

    // Updates the list of possible moves to the given list
    public void updateMoves(ArrayList<Move> moves) {
        this.possibleMoves = moves;
    }

    // Returns the symbol representing the piece;
    public char getSymbol() {
        if (state == 1 && isKing) return '♚';
        if (state == -1 && isKing) return '♔';

        if (state == 1) return '◉';
        else return '◎';
    }

    // Returns true if the piece has possible moves and false if not
    public boolean hasValidMoves() {
        return possibleMoves != null && !possibleMoves.isEmpty();
    }

    // Returns true if there is a possible move that leads to the destination coordinate
    // Returns false otherwise
    public boolean isValidMove(Coordinate dest) {
        if (dest == null) return false;
        for (Move m : possibleMoves) {
            if (dest.equals(m.getDestination())) return true;
        }
        return false;
    }

    // Returns the move that leads to a given destination coordinate
    // Returns null if move not found
    public Move getMoveTo(Coordinate dest) {
        for (Move m : possibleMoves) {
            if (dest.equals(m.getDestination())) return m;
        }
        return null;
    }

    // Returns a list of the coordinates diagonally adjacent to the given coordinate on the board.
    // Uses current position if not given a coordinate
    // Gives only forward coordinates if not king.
    public ArrayList<Coordinate> getAdjacentCoordinates(Coordinate c) {
        ArrayList<Coordinate> adjacentCoordinates = new ArrayList<>();

        if (c == null) c = currentPos;

        int x = c.getX();
        int y = c.getY();

        // Forward Moves
        adjacentCoordinates.add(Coordinate.newCoordinate(x+1, y+state));
        adjacentCoordinates.add(Coordinate.newCoordinate(x-1, y+state));

        // Backwards Moves
        if (isKing) {
            adjacentCoordinates.add(Coordinate.newCoordinate(x+1, y-state));
            adjacentCoordinates.add(Coordinate.newCoordinate(x-1, y-state));
        }

        // Remove nulls
        adjacentCoordinates.removeIf(Objects::isNull);

        return adjacentCoordinates;
    }

    // Checks if a non-king piece is at the end of the board for its respective side
    public boolean canPromote() {
        if (isKing) return false;
        return (state == 1 && currentPos.getY() == 7) || (state == -1 && currentPos.getY() == 0);
    }
}