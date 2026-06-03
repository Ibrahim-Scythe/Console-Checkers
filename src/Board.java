import java.util.ArrayList;
import java.util.Objects;

public class Board {
    Piece[][] board;

    // Initialises board
    public Board() {
        board = new Piece[8][8];

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

    // Prints the board state
    // If a selectedPiece is given also highlights the possible moves
    public void printBoard(Piece selectedPiece) {
        if (selectedPiece == null) {
            for (int row = 0; row < 8; row++) {
                System.out.print( (8 - row) + " |");
                for (Piece piece : board[row]) {
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

            System.out.println("   A B C D E F G H");
        }
        else {
            Coordinate c;

            for (int row = 0; row < 8; row++) {
                System.out.print( (8 - row) + " |");
                for (int column = 0; column < 8; column++) {
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

            System.out.println("   A B C D E F G H");
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

    // Gets a list of possible moves for a piece
    public void getPossibleMoves(Piece p) {
        ArrayList<Move> possibleMoves = new ArrayList<>();

        ArrayList<Coordinate> targetCoordinates = getTargetCoordinates(p.getPos(), p.getState(), p.isKing());

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

        ArrayList<Coordinate> targetCoordinates = getTargetCoordinates(followUpPos, p.getState(), p.isKing());

        for (Coordinate targetPos : targetCoordinates) {
            Piece target = getPiece(targetPos);

            // If target is enemy piece recursively checks if any more pieces can be taken
            if (target != null && target.getState() != p.getState()) {
                Coordinate newTargetPos = followUpPos.getCoordinateAfter(targetPos);

                ArrayList<Piece> furtherCapturedPieces = new ArrayList<>(capturedPieces);
                furtherCapturedPieces.add(target);

                ArrayList<Move> furtherMoves = getFollowUpMoves(p, furtherCapturedPieces, newTargetPos);
                followUpMoves.addAll(furtherMoves);
            }
        }

        return followUpMoves;
    }

    // Returns a list of the coordinates diagonally adjacent to the given coordinate on the board.
    // Gives only forward coordinates if not king.
    public ArrayList<Coordinate> getTargetCoordinates(Coordinate c, int state, boolean isKing) {
        ArrayList<Coordinate> targetCoordinates = new ArrayList<>();

        int x = c.getX();
        int y = c.getY();

        // Forward Moves
        targetCoordinates.add(Coordinate.newCoordinate(x+1, y+state));
        targetCoordinates.add(Coordinate.newCoordinate(x-1, y+state));

        // Backwards Moves
        if (isKing) {
            targetCoordinates.add(Coordinate.newCoordinate(x+1, y-state));
            targetCoordinates.add(Coordinate.newCoordinate(x-1, y-state));
        }

        // Remove nulls
        targetCoordinates.removeIf(Objects::isNull);

        return targetCoordinates;
    }

    // Returns the piece at the given coordinate
    public Piece getPiece(Coordinate c) {
        return board[c.getY()][c.getX()];
    }
}
