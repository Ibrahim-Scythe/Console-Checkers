import java.util.ArrayList;
import java.util.Objects;

// Represents a Checkers piece
// Has a colour, can be king and hold a coordinate of its current position and moves that can be made from that position
class Piece {
    private final Colour pieceColour;
    private boolean isKing;
    private Coordinate currentPos;
    private ArrayList<Move> possibleMoves;

    // Constructor
    public Piece(Colour colour, int x, int y) {
        this.pieceColour = colour;
        isKing = false;
        currentPos = Coordinate.newCoordinate(x, y);
        possibleMoves = null;
    }

    // Returns the state of the piece
    public Colour getColour() {
        return pieceColour;
    }

    // Returns true if the piece is a king, false otherwise
    public boolean isKing() {
        return isKing;
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
        if (pieceColour == Colour.WHITE && isKing) return '♚';
        if (pieceColour == Colour.BLACK && isKing) return '♔';

        if (pieceColour == Colour.WHITE) return '◉';
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

        int forward;
        if (pieceColour == Colour.WHITE) forward = 1;
        else forward = -1;

        int x = c.getX();
        int y = c.getY();

        // Forward Moves
        adjacentCoordinates.add(Coordinate.newCoordinate(x+1, y+forward));
        adjacentCoordinates.add(Coordinate.newCoordinate(x-1, y+forward));

        // Backwards Moves
        if (isKing) {
            adjacentCoordinates.add(Coordinate.newCoordinate(x+1, y-forward));
            adjacentCoordinates.add(Coordinate.newCoordinate(x-1, y-forward));
        }

        // Remove nulls
        adjacentCoordinates.removeIf(Objects::isNull);

        return adjacentCoordinates;
    }

    // Checks if a non-king piece is at the end of the board for its respective side
    public boolean canPromote() {
        if (isKing) return false;
        return (pieceColour == Colour.WHITE && currentPos.getY() == 7) || (pieceColour == Colour.BLACK && currentPos.getY() == 0);
    }
}