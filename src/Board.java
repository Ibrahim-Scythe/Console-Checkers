import java.util.ArrayList;

// Represents the Checkers board
// Holds a 8x8 grid of Pieces and a counter which marks whose turn it is
public class Board {
    private final Piece[][] board;
    private Colour currentPlayerColour;

    public static final int ROWS = 8;
    public static final int COLS = 8;

    // Constructor
    public Board() {
        board = new Piece[ROWS][COLS];
        currentPlayerColour = Colour.BLACK;

        for (int y = 0; y < ROWS; y++) {
            for (int x = 0; x < COLS; x++) {
                if ((x + y) % 2 == 1) {
                    if (y <= 2)
                        board[y][x] = new Piece(Colour.RED, x, y);
                    else if (y >= 5)
                        board[y][x] = new Piece(Colour.BLACK, x, y);
                }
                else board[y][x] = null;
            }
        }

        updatePieces();
    }

    // Returns the current players colour
    public Colour getCurrentPlayerColour() {
        return currentPlayerColour;
    }

    // Swaps the currentPlayerColour between Black and Red
    public void nextTurn() {
        if (currentPlayerColour == Colour.RED)
            currentPlayerColour = Colour.BLACK;
        else
            currentPlayerColour = Colour.RED;
    }

    // Promotes any pieces that can promote
    // Updates possible moves for all pieces
    public void updatePieces() {
        for (Piece[] row : board) {
            for (Piece piece : row) if (piece != null) {
                    if (piece.canPromote()) piece.promote();
                    getPossibleMoves(piece);
            }
        }
    }

    // Updates the list of all possible moves for a piece
    public void getPossibleMoves(Piece p) {
        ArrayList<Move> possibleMoves = new ArrayList<>();

        ArrayList<Coordinate> targetCoordinates = p.getAdjacentCoordinates(null);

        for (Coordinate targetPos : targetCoordinates) {
            Piece target = getPiece(targetPos);

            // If target is empty add as a possible move
            if (target == null) {
                possibleMoves.add(new Move(targetPos));
            }

            // If target is enemy piece checks if any pieces can be taken
            else if (target.getColour() != p.getColour()) {
                Coordinate newTargetPos = p.getPos().getCoordinateAfter(targetPos);

                ArrayList<Piece> capturedPieces = new ArrayList<>();
                capturedPieces.add(target);

                ArrayList<Move> followUpMoves = getFollowUpMoves(p, capturedPieces, newTargetPos);
                possibleMoves.addAll(followUpMoves);
            }
        }

        p.updateMoves(possibleMoves);
    }

    // Helper method
    // Checks if a piece can move to the followUpPos by taking a piece
    // Recursively checks if any more pieces can be taken after
    public ArrayList<Move> getFollowUpMoves(Piece p, ArrayList<Piece> capturedPieces, Coordinate followUpPos) {
        ArrayList<Move> followUpMoves = new ArrayList<>();

        // If the following position is invalid or there is already a piece at the following position no further moves can be made
        if (followUpPos == null || getPiece(followUpPos) != null) {
            return followUpMoves;
        }
        else {
            followUpMoves.add(new Move(followUpPos, capturedPieces));
        }

        ArrayList<Coordinate> targetCoordinates = p.getAdjacentCoordinates(followUpPos);

        for (Coordinate targetPos : targetCoordinates) {
            Piece target = getPiece(targetPos);

            // If target is enemy piece recursively checks if any more pieces can be taken
            if (target != null && target.getColour() != p.getColour() && !capturedPieces.contains(target)) {
                Coordinate newTargetPos = followUpPos.getCoordinateAfter(targetPos);

                ArrayList<Piece> furtherCapturedPieces = new ArrayList<>(capturedPieces);
                furtherCapturedPieces.add(target);

                ArrayList<Move> furtherMoves = getFollowUpMoves(p, furtherCapturedPieces, newTargetPos);
                followUpMoves.addAll(furtherMoves);
            }
        }

        return followUpMoves;
    }

    // Moves the piece to the given coordinate if it is a valid move
    // Takes all pieces on the way if it is a capture move
    public boolean movePiece(Piece selectedPiece, Coordinate dest) {
        if (selectedPiece.isValidMove(dest)) {
            Move move = selectedPiece.getMoveTo(dest);
            if (move.isCapture()) {
                for (Piece p : move.getCapturedPieces()) {
                    // Deletes captured pieces
                    Coordinate c = p.getPos();
                    board[c.getY()][c.getX()] = null;
                }
            }

            board[dest.getY()][dest.getX()] = selectedPiece;
            board[selectedPiece.getPos().getY()][selectedPiece.getPos().getX()] = null;
            selectedPiece.updatePos(dest);
            return true;
        }
        else {
            return false;
        }
    }

    // Checks if both sides have valid moves
    // Returns null if both sides have valid move
    // Returns the opposite colour (the winner) if either side has no valid moves
    public Colour hasWinner() {
        boolean blackHasValidMove = false;
        boolean redHasValidMove = false;

        for (Piece[] row : board) {
            for (Piece p : row) if (p != null) {
                if (p.getColour() == Colour.BLACK && p.hasValidMoves()) {
                    blackHasValidMove = true;
                }
                else if (p.getColour() == Colour.RED && p.hasValidMoves()) {
                    redHasValidMove = true;
                }

                // If both have valid moves then there is no winner
                if (blackHasValidMove && redHasValidMove) {
                    return null;
                }
            }
        }
        if (!blackHasValidMove) return Colour.RED;
        else return Colour.BLACK;
    }

    // Returns the piece at the given coordinate
    public Piece getPiece(Coordinate c) {
        if (c == null) return null;
        return board[c.getY()][c.getX()];
    }
}
