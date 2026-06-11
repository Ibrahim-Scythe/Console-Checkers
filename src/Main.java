/*  Console Checkers
    Author: Ibrahim Saif

    A 2 Player Checkers game to be played on the command line
    Written entirely in Java
 */

import java.util.Scanner;

class Main {
    // Run parameters: -noflip board will not flip between turns
    public static void main(String[] args) {
        playCheckersCLI(args);
    }

    public static void playCheckersCLI(String[] args) {
        // If ran with the argument 'noflip' the board will not flip between turns
        boolean flipBoard = true;
        if (args.length > 0 && args[0].toLowerCase().contains("-noflip")) {
            flipBoard = false;
        }

        Scanner scanner = new Scanner(System.in);

        Board board = new Board();
        Colour winningColour = null;

        while (winningColour == null) {
            // Prints the board
            board.printBoard(null, flipBoard);

            // Choose Piece
            Piece selectedPiece = board.getPiece(askForCoordinate("Choose a piece: ", scanner));
            if (selectedPiece == null || selectedPiece.getColour() != board.getCurrentPlayerColour()) {
                System.out.println("Not your piece. Try again.");
                continue;
            } else if (!selectedPiece.hasValidMoves()) {
                System.out.println("No valid moves. Try again.");
                continue;
            }

            // Choose Move for Piece
            board.printBoard(selectedPiece, flipBoard);

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

        if (winningColour == Colour.WHITE) {
            System.out.println("White Wins!");
        } else {
            System.out.println("Black Wins!");
        }

        System.out.println("Play Again? (Y/N)");
        String answer = scanner.nextLine();
        if (answer.equalsIgnoreCase("Y")) main(args);
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