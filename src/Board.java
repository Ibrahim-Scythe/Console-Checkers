public class Board {
    Piece[][] board;


    public Board() {
        board = new Piece[8][8];

        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                if ((y == 0 || y == 2) && x % 2 == 1) {
                    board[y][x] = new Piece (1, y, x);
                }
                else if (y == 1 && x % 2 == 0) {
                    board[y][x] = new Piece (1, y, x);
                }
                else if ((y == 5 || y == 7) && x % 2 == 0) {
                    board[y][x] = new Piece (-1, y, x);
                }
                else if (y == 6 && x % 2 == 1) {
                    board[y][x] = new Piece (-1, y, x);
                    board[y][x].promote();
                }
                else {
                    board[y][x] = null;
                }
            }
        }
    }

    public void printBoard(Piece selectedPiece) {
        if (selectedPiece == null) {
            for (int row = 0; row < 8; row++) {
                System.out.print( (8 - row) + " |");
                for (Piece piece : board[row]) {
                    if (piece == null) {
                        System.out.print(" |");
                    }
                    else if (piece.state == -1 && piece.isKing) {
                        System.out.print("♔|");
                    }
                    else if (piece.state == 1 && piece.isKing) {
                        System.out.print("♚|");
                    }
                    else if (piece.state == -1) {
                        System.out.print("○|");
                    }
                    else if (piece.state == 1) {
                        System.out.print("●|");
                    }
                    else {
                        System.out.print(" |");
                    }
                }
                System.out.println();
            }

            System.out.println("   A B C D E F G H");
        }
        else {
            System.out.print("Not implemented yet");
        }
    }
}
