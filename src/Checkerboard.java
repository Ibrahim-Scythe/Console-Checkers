import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Checkerboard extends JComponent {
    private static final int ROWS = 8;
    private static final int COLS = 8;
    private static final int SQUARE_SIZE = 80;

    final private Board board;
    private Piece selectedPiece;

    final private BufferedImage redPiece;
    final private BufferedImage blackPiece;
    final private BufferedImage redKing;
    final private BufferedImage blackKing;
    final private BufferedImage previewCircle;


    public Checkerboard (Board board) throws IOException {
        super();
        this.board = board;
        this.selectedPiece = null;

        this.redPiece = ImageIO.read(new File("images/RedPiece.png"));
        this.blackPiece = ImageIO.read(new File("images/BlackPiece.png"));
        this.redKing = ImageIO.read(new File("images/RedKing.png"));
        this.blackKing = ImageIO.read(new File("images/BlackKing.png"));
        this.previewCircle = ImageIO.read(new File("images/PreviewCircle.png"));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int x = e.getX() / SQUARE_SIZE;
                int y = e.getY() / SQUARE_SIZE;
                Coordinate clickedCoordinate = Coordinate.newCoordinate(x, y);

                // If no piece selected
                if (selectedPiece == null) {
                    if (clickedCoordinate == null) return;
                    System.out.println("Clicked: " + clickedCoordinate.toString());

                    Piece p = board.getPiece(clickedCoordinate);
                    if (p == null || p.getState() != board.getCurrentTurn() || !p.hasValidMoves()) return;

                    selectedPiece = p;
                }
                // If piece selected
                else {
                    boolean successfulMove = board.movePiece(selectedPiece, clickedCoordinate);
                    selectedPiece = null;

                    if (!successfulMove) {
                        repaint();
                        return;
                    }

                    // Update Game State
                    board.updatePieces();

                    board.nextTurn();
                }
                repaint();
            }
        });

        setPreferredSize(new Dimension(COLS*SQUARE_SIZE, ROWS*SQUARE_SIZE));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawBoard(g);
        drawPieces(g);
        if (selectedPiece != null) { drawPossibleMoves(g);}
    }

    public void drawBoard(Graphics g) {
        for (int r = 0; r < ROWS; r++) {
            for(int c = 0; c < COLS; c++) {
                if ((r + c) % 2 == 1)
                    g.setColor(Color.decode("#644037"));
                else
                    g.setColor(Color.WHITE);

                g.fillRect(c * SQUARE_SIZE, r * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);
            }
        }
    }

    public void drawPieces(Graphics g) {
        for (int y = 0; y < ROWS; y++) {
            for(int x = 0; x < COLS; x++) {
                Piece p = board.getPiece(Coordinate.newCoordinate(x, y));
                if (p == null) continue;

                BufferedImage image;
                if (p.isKing() && p.getState()==1)
                    image = redKing;
                else if (p.isKing() && p.getState()==-1)
                    image = blackKing;
                else if (p.getState()==1)
                    image = redPiece;
                else
                    image = blackPiece;

                g.drawImage(image, x*SQUARE_SIZE, y*SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE, null);
            }
        }
    }

    public void drawPossibleMoves(Graphics g) {
        Coordinate c;
        for (int y = 0; y < ROWS; y++) {
            for(int x = 0; x < COLS; x++) {
                c = Coordinate.newCoordinate(x, y);
                Piece p = board.getPiece(c);
                if (p == null && selectedPiece.isValidMove(c)) {
                    g.drawImage(previewCircle, x*SQUARE_SIZE, y*SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE,null);
                }
            }
        }
    }

    public static void main(String[] args) throws IOException {
        JFrame frame = new JFrame("Checkers");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Board board = new Board();
        Checkerboard checkerboard = new Checkerboard(board);
        frame.add(checkerboard);

        frame.pack();
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
