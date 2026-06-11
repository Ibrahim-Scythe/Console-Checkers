/*  Console Checkers
    Author: Ibrahim Saif

    A 2 Player Checkers game to be played on the command line
    Written entirely in Java
 */

import java.util.Scanner;

class CheckersCLI {
    private final Board board;
    private Piece selectedPiece;

    // Starts the checkers game in the command line
    public static void playCheckersCLI() {
        Board board = new Board();
        CheckersCLI checkersGame = new CheckersCLI(board);
        checkersGame.play();
    }

    // Constructor
    public CheckersCLI(Board board) {
        this.board = board;
        this.selectedPiece = null;
    }

    // Main game loop
    // Prints board, asks for piece, prints possible moves, asks for move, moves piece if everything is correct
    public void play() {
        Scanner scanner = new Scanner(System.in);
        Colour winningColour = null;

        while (winningColour == null) {
            selectedPiece = null;
            // Prints the board
            printBoard();

            // Choose Piece
            selectedPiece = board.getPiece(askForCoordinate("Choose a piece: ", scanner));
            if (selectedPiece == null || selectedPiece.getColour() != board.getCurrentPlayerColour()) {
                System.out.println("Not your piece. Try again.");
                continue;
            } else if (!selectedPiece.hasValidMoves()) {
                System.out.println("No valid moves. Try again.");
                continue;
            }

            // Choose Move for Piece
            printBoard();

            Coordinate moveTarget = askForCoordinate("Move to: ", scanner);
            boolean successfulMove = board.movePiece(selectedPiece, moveTarget);

            if (!successfulMove) {
                System.out.println("Invalid move. Try again.");
                continue;
            }

            // Update Game State
            board.updatePieces();
            winningColour = board.hasWinner();

            board.nextTurn();
        }

        selectedPiece = null;
        printBoard();
        if (winningColour == Colour.RED) {
            System.out.println("Red Wins!");
        } else {
            System.out.println("Black Wins!");
        }

        System.out.println("Play Again? (Y/N)");
        String answer = scanner.nextLine();
        if (answer.equalsIgnoreCase("Y")) playCheckersCLI();
    }

    // Prints the board
    // If selectedPiece is not null highlights the possible moves for the piece
    public void printBoard() {
        String turn = switch(board.getCurrentPlayerColour()) {
            case RED -> "Red's Turn";
            case BLACK -> "Black's Turn";
        };

        System.out.println("   " + turn);
        if (selectedPiece == null) {
            for (int row = 0; row < Board.ROWS; row++) {
                System.out.print( (Board.ROWS - row) + " |");
                for (int col = 0;col < Board.COLS; col++) {
                    Piece piece = board.getPiece(Coordinate.newCoordinate(col, row));
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

            for (int row = 0; row < Board.ROWS; row++) {
                System.out.print( (Board.ROWS - row) + " |");
                for (int col = 0;col < Board.COLS; col++) {
                    c = Coordinate.newCoordinate(col, row);
                    Piece p = board.getPiece(c);
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

    // Asks the user for a coordinate and returns that coordinate
    public static Coordinate askForCoordinate(String message, Scanner scanner) {
        System.out.print(message);
        Coordinate c = Coordinate.newCoordinate(scanner.nextLine().toUpperCase());

        while (c == null) {
            System.out.println("Please enter a valid coordinate");
            System.out.print(message);
            c = Coordinate.newCoordinate(scanner.nextLine().toUpperCase());
        }

        return c;
    }
}