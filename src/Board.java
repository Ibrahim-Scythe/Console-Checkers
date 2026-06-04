import java.util.ArrayList;

// Represents the Checkers board
// Holds a 8x8 grid of Pieces and a counter which marks whose turn it is
public class Board {
    private Piece[][] board;
    private int currentTurn; // White: 1, Black: -1

    // Constructor
    public Board() {
        board = new Piece[8][8];
        currentTurn = -1;

        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                if ((y == 0 || y == 2) && x % 2 == 1) {
                    board[y][x] = new Piece (1, x, y);
                }
                else if (y == 1 && x % 2 == 0) {
                    board[y][x] = new Piece (1, x, y);
                }
                else if ((y == 5 || y == 7) && x % 2 == 0) {
                    board[y][x] = new Piece (-1, x, y);
                }
                else if (y == 6 && x % 2 == 1) {
                    board[y][x] = new Piece (-1, x, y);
                }
                else {
                    board[y][x] = null;
                }
            }
        }
    }

    // Returns the current turn;
    public int getCurrentTurn() {
        return currentTurn;
    }

    // Swaps the turn between 1 and -1
    // 1: White and -1: Black
    public void nextTurn() {
        currentTurn *= -1;
    }

    // Prints the board
    // Board will flip between turns if flipBoard is true
    // If a selectedPiece is given highlights the possible moves
    public void printBoard(Piece selectedPiece, boolean flipBoard) {
        String turn = "Black's Turns";
        int[] order = {0, 1, 2, 3, 4, 5, 6, 7};
        String lettersRow = "   A B C D E F G H";

        if (currentTurn == 1) {
            turn = "White's Turns";
            if (flipBoard) {
                order = new int[]{7, 6, 5, 4, 3, 2, 1, 0};
                lettersRow = "   H G F E D C B A";
            }
        }

        System.out.println("   " + turn);
        if (selectedPiece == null) {
            for (int row : order) {
                System.out.print( (8 - row) + " |");
                for (int column : order) {
                    Piece piece = board[row][column];
                    if (piece == null) {
                        System.out.print(' ');
                    }
                    else {
                        System.out.print(piece.getSymbol());
                    }
                    System.out.print('|');
                }
                System.out.println();
            }

            System.out.println(lettersRow);
        }
        else {
            Coordinate c;

            for (int row : order) {
                System.out.print( (8 - row) + " |");
                for (int column : order) {
                    c = Coordinate.newCoordinate(column, row);
                    Piece p = getPiece(c);
                    if (p == null && selectedPiece.isValidMove(c)) {
                        System.out.print("\u001B[32m" + '●' + "\u001B[0m");
                    }
                    else if (p == null) {
                        System.out.print(' ');
                    }
                    else {
                        System.out.print(p.getSymbol());
                    }
                    System.out.print('|');
                }
                System.out.println();
            }

            System.out.println(lettersRow);
        }
    }

    // Promotes any pieces that can promote
    // Updates possible moves for all pieces
    public void updatePieces() {
        for (int row = 0; row < 8; row++) {
            for (Piece piece : board[row]) if (piece != null) {
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
            else if (target.getState() != p.getState()) {
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
            if (target != null && target.getState() != p.getState() && !capturedPieces.contains(target)) {
                Coordinate newTargetPos = followUpPos.getCoordinateAfter(targetPos);

                ArrayList<Piece> furtherCapturedPieces = new ArrayList<>(capturedPieces);
                furtherCapturedPieces.add(target);

                ArrayList<Move> furtherMoves = getFollowUpMoves(p, furtherCapturedPieces, newTargetPos);
                followUpMoves.addAll(furtherMoves);
            }
        }

        return followUpMoves;
    }

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
            System.out.println("Invalid move. Try again.");
            return false;
        }
    }

    // Checks if both sides have valid moves
    // Returns an int corresponding to win state
    // 1: White wins, 0: No Winner, -1: Black Wins
    public int hasWinner() {
        boolean blackHasValidMove = false;
        boolean whiteHasValidMove = false;

        for (Piece[] row : board) {
            for (Piece p : row) if (p != null) {
                if (p.getState() == -1) {
                    blackHasValidMove = p.hasValidMoves();
                }
                else {
                    whiteHasValidMove = p.hasValidMoves();
                }

                // If both have valid moves then there is no winner
                if (blackHasValidMove && whiteHasValidMove) {
                    return 0;
                }
            }
        }
        if (!blackHasValidMove) return 1;
        else return -1;
    }

    // Returns the piece at the given coordinate
    public Piece getPiece(Coordinate c) {
        return board[c.getY()][c.getX()];
    }
}
