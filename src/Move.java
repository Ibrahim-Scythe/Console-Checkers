import java.util.ArrayList;

public class Move {
    private final Coordinate destination;
    private final ArrayList<Piece> capturedPieces;

    // Constructor for no captures
    public Move(Coordinate destination) {
        this.destination = destination;
        this.capturedPieces = new ArrayList<>();
    }

    // Constructor with captures
    public Move(Coordinate destination, ArrayList<Piece> capturedPieces) {
        this.destination = destination;
        this.capturedPieces = capturedPieces;
    }

    // Returns destination coordinates
    public Coordinate getDestination() {
        return destination;
    }

    // Returns list of pieces that will be captured
    public ArrayList<Piece> getCapturedPieces() {
        return capturedPieces;
    }

    // Returns if the move is a capture move
    public boolean isCapture() {
        return !capturedPieces.isEmpty();
    }
}
