import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        Board board = new Board();
        int state = 1;

        while (true) {
            board.updatePieces();
            board.printBoard(null);

            Piece selectedPiece = board.getPiece(askForCoordinate("Choose a piece: "));
            if (selectedPiece == null || selectedPiece.getState() != state) {
                System.out.println("Not your piece. Try again.");
                continue;
            }

            board.printBoard(selectedPiece);

            // TODO: MAKE THIS INTO PROPER METHOD
            Coordinate moveTarget = askForCoordinate("Move to: ");
            if (selectedPiece.isValidMove(moveTarget)) {
                Move move = selectedPiece.getMoveTo(moveTarget);
                if (move.isCapture()) {
                    for (Piece p : move.getCapturedPieces()) {
                        Coordinate c = p.getPos();
                        board.board[c.getY()][c.getX()] = null;
                    }
                }

                board.board[moveTarget.getY()][moveTarget.getX()] = selectedPiece;
                board.board[selectedPiece.getPos().getY()][selectedPiece.getPos().getX()] = null;
                selectedPiece.updatePos(moveTarget);
            }
            else {
                System.out.println("Invalid move. Try again.");
                continue;
            }

            state = changeState(state);
        }
    }

    public static Coordinate askForCoordinate(String message) {
        Scanner scanner = new Scanner(System.in);

        System.out.print(message);
        Coordinate c = Coordinate.newCoordinate(scanner.nextLine().toUpperCase());

        while (c == null) {
            System.out.println("Please enter a valid coordinate");
            System.out.print(message);
            c = Coordinate.newCoordinate(scanner.nextLine().toUpperCase());
        }

        return c;
    }

    public static int changeState(int state) {
        if (state == 1) {
            state = -1;
        }
        else if (state == -1) {
            state = 1;
        }
        return state;
    }
}