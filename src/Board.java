import java.util.ArrayList;

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
            System.out.print("Not implemented yet");
        }
    }

    // Gets a list of possible moves for a Piece at the currentPos, with the properties of the piece at originalPos
    // currentPos and originalPos should be the same when called by another method
    public ArrayList<Coordinate> getPossibleMoves(Coordinate currentPos, Coordinate originalPos) {
        ArrayList<Coordinate> possibleMoves = new ArrayList<>();

        Piece selectedPiece = getPiece(originalPos);

        int x = currentPos.getX();
        int y = currentPos.getY();
        int state = selectedPiece.state;

        ArrayList<Coordinate> targetCoordinates = new ArrayList<>();

        // Forward Moves
        targetCoordinates.add(Coordinate.newCoordinate(x+1, y+state));
        targetCoordinates.add(Coordinate.newCoordinate(x-1, y+state));

        // Backwards Moves
        if (selectedPiece.isKing) {
            targetCoordinates.add(Coordinate.newCoordinate(x+1, y-state));
            targetCoordinates.add(Coordinate.newCoordinate(x-1, y-state));
        }

        // Removes the originalPos from targetCoordinates
        if (originalPos != currentPos) {
            targetCoordinates.remove(originalPos);
        }

        int i = 0;
        Coordinate targetPos;
        while (i < targetCoordinates.size()) {
            targetPos = targetCoordinates.get(i);

            // If invalid coordinate goes to next coordinate
            if (targetPos == null) {
                i++;
                continue;
            }

            Piece target = getPiece(targetPos);
            // If target is empty add as a possible move and goes to next coordinate
            if (target == null) {
                possibleMoves.add(targetPos);
                i++;
            }

            // If target is ally piece goes to next coordinate
            else if (state == target.state) {
                i++;
            }

            // If target is enemy piece recursively checks next piece
            else {
                Coordinate newPos = selectedPiece.currentPos.getCoordinateAfter(targetPos);
                ArrayList<Coordinate> newPossibleMoves = getPossibleMoves(newPos, originalPos);
                possibleMoves.addAll(newPossibleMoves);
                i++;
            }
        }

        return possibleMoves;
    }

    // Returns the piece at the given coordinate or null if c is null
    public Piece getPiece(Coordinate c) {
        if (c == null) return null;

        int x = c.getX();
        int y = c.getY();
        return board[y][x];
    }
}
