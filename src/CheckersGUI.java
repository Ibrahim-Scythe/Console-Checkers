import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class CheckersGUI extends JComponent {
    private static final int ROWS = 8;
    private static final int COLS = 8;
    private static final int SQUARE_SIZE = 80;

    private final Board board;
    private Piece selectedPiece;
    private Colour winningColour;

    final private BufferedImage redPiece;
    final private BufferedImage blackPiece;
    final private BufferedImage redKing;
    final private BufferedImage blackKing;
    final private BufferedImage previewCircle;
    final private BufferedImage pieceHighlight;


    public CheckersGUI(Board board) throws IOException {
        super();
        this.board = board;
        this.selectedPiece = null;
        this.winningColour = null;

        // Loads Images
        this.redPiece = ImageIO.read(new File("images/RedPiece.png"));
        this.blackPiece = ImageIO.read(new File("images/BlackPiece.png"));
        this.redKing = ImageIO.read(new File("images/RedKing.png"));
        this.blackKing = ImageIO.read(new File("images/BlackKing.png"));
        this.previewCircle = ImageIO.read(new File("images/PreviewCircle.png"));
        this.pieceHighlight = ImageIO.read(new File("images/PieceHighlight.png"));

        addMouseControls();

        setPreferredSize(new Dimension(COLS*SQUARE_SIZE, ROWS*SQUARE_SIZE));
    }

    // Converts mouse clicks to a Coordinate
    // Updates game depending on what coordinate is clicked
    public void addMouseControls() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (winningColour != null) return;

                int x = e.getX() / SQUARE_SIZE;
                int y = e.getY() / SQUARE_SIZE;
                Coordinate clickedCoordinate = Coordinate.newCoordinate(x, y);

                // If no piece selected
                if (selectedPiece == null) {
                    if (clickedCoordinate == null) return;

                    Piece p = board.getPiece(clickedCoordinate);
                    if (p == null || p.getColour() != board.getCurrentPlayerColour() || !p.hasValidMoves()) return;

                    selectedPiece = p;
                }
                // If piece selected
                else {
                    boolean successfulMove = board.movePiece(selectedPiece, clickedCoordinate);
                    selectedPiece = null;

                    // Update Game State
                    if (successfulMove) {
                        board.updatePieces();
                        winningColour = board.hasWinner();
                        board.nextTurn();
                    }
                }
                repaint();

                if (winningColour != null) makeWinDialog();
            }
        });
    }

    // Draws game
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawBoard(g);
        drawPieces(g);
        drawPossibleMoves(g);
    }

    // Draws checkerboard
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

    // Draws checker pieces
    public void drawPieces(Graphics g) {
        for (int y = 0; y < ROWS; y++) {
            for(int x = 0; x < COLS; x++) {
                Piece p = board.getPiece(Coordinate.newCoordinate(x, y));
                if (p == null) continue;

                BufferedImage image;
                if (p.isKing() && p.getColour()==Colour.WHITE)
                    image = redKing;
                else if (p.isKing() && p.getColour()==Colour.BLACK)
                    image = blackKing;
                else if (p.getColour()==Colour.WHITE)
                    image = redPiece;
                else
                    image = blackPiece;

                drawImage(image, x, y, g);

                if (selectedPiece == null && p.hasValidMoves() && p.getColour() == board.getCurrentPlayerColour()) {
                    drawImage(pieceHighlight, x, y, g);
                }
            }
        }
    }

    // Highlights either which pieces can move this turn or where the selected piece can move
    public void drawPossibleMoves(Graphics g) {
        // Highlights pieces that can move this turn
        if (selectedPiece == null) {
            for (int y = 0; y < ROWS; y++) {
                for (int x = 0; x < COLS; x++) {
                    Piece p = board.getPiece(Coordinate.newCoordinate(x, y));
                    if (p == null) continue;

                    if (p.hasValidMoves() && p.getColour() == board.getCurrentPlayerColour()) {
                        drawImage(pieceHighlight, x, y, g);
                    }
                }
            }
        }

        // Highlights selected piece and shows its possible moves
        else {
            drawImage(pieceHighlight, selectedPiece.getPos().getX(), selectedPiece.getPos().getY(), g);

            Coordinate c;
            for (int y = 0; y < ROWS; y++) {
                for (int x = 0; x < COLS; x++) {
                    c = Coordinate.newCoordinate(x, y);
                    Piece p = board.getPiece(c);
                    if (p == null && selectedPiece.isValidMove(c)) {
                        drawImage(previewCircle, x, y, g);
                    }
                }
            }
        }
    }

    // Draws the given image at the given x and y coordinates
    // 0 <= x,y <= 7
    public void drawImage(BufferedImage image, int x, int y, Graphics g) {
        g.drawImage(image, x*SQUARE_SIZE, y*SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE, null);
    }

    // Creates and displays a dialog that shows who won
    // Has buttons to play again or close dialog
    public void makeWinDialog() {
        JFrame frame = getParentFrame();
        JDialog winDialog = new JDialog(frame, true);

        winDialog.setSize(400, 200);
        winDialog.setUndecorated(true);
        winDialog.setBackground(new Color(0,0,0,0));

        // Grey Rounded Body of Dialog
        JPanel body = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(Color.decode("#202020"));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 50, 50);
                g2d.dispose();
            }
        };
        body.setLayout(new BorderLayout());
        body.setOpaque(false);
        body.setBorder(new EmptyBorder(15, 10, 15, 10));
        winDialog.add(body);

        // Label for which colour won
        String winnerMessage = switch(winningColour) {
            case WHITE -> "Red Wins!";
            case BLACK -> "Black Wins!";
        };
        JLabel winnerLabel = new JLabel(winnerMessage, SwingConstants.CENTER);
        winnerLabel.setForeground(Color.WHITE);
        winnerLabel.setFont(new Font("Segoe UI Black", Font.PLAIN, 52));
        body.add(winnerLabel, BorderLayout.NORTH);

        // Buttons to play again or close dialog
        JPanel buttonWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 30));
        buttonWrapper.setOpaque(false);

        RoundedButton playAgainBtn = new RoundedButton("Play Again");
        playAgainBtn.addActionListener(e-> {
            frame.dispose();
            try {
                CheckersGUI.main(null);
            }
            catch (IOException e1) {
                throw new RuntimeException(e1);
            }
        });
        buttonWrapper.add(playAgainBtn);

        RoundedButton closeBtn = new RoundedButton("Close");
        closeBtn.addActionListener(e-> {
            winDialog.dispose();
        });
        buttonWrapper.add(closeBtn);

        body.add(buttonWrapper, BorderLayout.SOUTH);

        winDialog.setLocationRelativeTo(frame);
        winDialog.setVisible(true);
    }

    // Returns the JFrame which holds the GUI
    public JFrame getParentFrame() {
        Component parent = getParent();
        while (!(parent instanceof JFrame)) {
            parent = parent.getParent();
        }
        return (JFrame) parent;
    }

    public static void main(String[] args) throws IOException {
        JFrame frame = new JFrame("Checkers");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Board board = new Board();
        CheckersGUI checkerboard = new CheckersGUI(board);
        frame.add(checkerboard);

        frame.pack();
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
