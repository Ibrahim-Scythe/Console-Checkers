import java.awt.Font;
import javax.swing.JTextArea;

class Main {
    public static void main(String[] args) {
        JTextArea textArea = new JTextArea();
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 14));

        Board board = new Board();
        board.printBoard(null);
    }

}