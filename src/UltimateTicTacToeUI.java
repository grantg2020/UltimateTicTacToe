import javax.swing.JFrame;

import java.awt.Graphics;
import java.awt.Stroke;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.Color;
import java.awt.BasicStroke;
import java.awt.Graphics2D;

import javax.swing.SwingUtilities;

public class UltimateTicTacToeUI extends JFrame implements MouseMotionListener {

    /** Game to play with */
    private static Model game;

    /** Width on screen of box */
    public static final int BOX_WIDTH = 70;
    /** Padding height from top of screen */
    public static final int PADDING_HEIGHT = BOX_WIDTH / 2;
    /** Padding around pieces */
    public static final int PIECE_BORDER_PADDING = BOX_WIDTH / 10;
    /** Background shaded color */
    public static final Color OVERLAY_COLOR = new Color(0f, 0f, 0f,
            .3141592653589793238462643383279502884197169399375105f);
    /** Stroke size for between each game board */
    private static final float BOARDS_STROKE_SIZE = 2f;
    /** Default stroke size */
    public static final float DEFAULT_STROKE_SIZE = (float) ((3f / 50.0) * BOX_WIDTH);

    public UltimateTicTacToeUI() {
        super("Ultimate Tic Tac Toe");

        int displaySize = BOX_WIDTH * Model.SIZE * Model.SIZE;
        setSize(displaySize, displaySize + PADDING_HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    void drawLines(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        Stroke stroke = new BasicStroke(BOARDS_STROKE_SIZE);
        g2d.setStroke(stroke);

        // Draw major lines
        for (int i = 0; i < Model.SIZE; i++) { // Boards rows
            g2d.drawLine(BOX_WIDTH * Model.SIZE * i, PADDING_HEIGHT, BOX_WIDTH * Model.SIZE * i,
                    BOX_WIDTH * Model.SIZE * Model.SIZE + PADDING_HEIGHT);
        }
        for (int j = 0; j < Model.SIZE; j++) { // Boards columns
            g2d.drawLine(0, BOX_WIDTH * Model.SIZE * j + PADDING_HEIGHT, BOX_WIDTH * Model.SIZE * Model.SIZE,
                    BOX_WIDTH * Model.SIZE * j + PADDING_HEIGHT);
        }

        stroke = new BasicStroke(1f);
        g2d.setStroke(stroke);

        // Draw individual board lines
        for (int i = 0; i < Model.SIZE * Model.SIZE; i++) { // Board rows
            g2d.drawLine(BOX_WIDTH * (Model.SIZE / Board.SIZE) * i, PADDING_HEIGHT, BOX_WIDTH * (Model.SIZE / 3) * i,
                    BOX_WIDTH * Model.SIZE * Model.SIZE + PADDING_HEIGHT);
        }
        for (int j = 0; j < Model.SIZE * Model.SIZE; j++) { // Board columns
            g2d.drawLine(0, BOX_WIDTH * (Model.SIZE / Board.SIZE) * j + PADDING_HEIGHT,
                    BOX_WIDTH * Model.SIZE * Model.SIZE,
                    BOX_WIDTH * (Model.SIZE / Board.SIZE) * j + PADDING_HEIGHT);
        }

        // Draw X's and O's

        // Iterate through each game board
        for (int i = 0; i < Model.SIZE; i++) { // Rows
            for (int j = 0; j < Model.SIZE; j++) { // Columns
                Board board = game.getBoard(i, j);
                // Board
                for (int row = 0; row < Model.SIZE; row++) { // Row
                    for (int col = 0; col < Model.SIZE; col++) { // Column
                        int position = board.getBoardPosition(row, col);
                        int boardWidth = Model.SIZE * BOX_WIDTH;
                        // Padding for boards + padding for current board + border padding
                        int leftPadding = (boardWidth * j) + (BOX_WIDTH * col) + PIECE_BORDER_PADDING;
                        int topPadding = (boardWidth * i) + (BOX_WIDTH * row) + PIECE_BORDER_PADDING + PADDING_HEIGHT;
                        int size = BOX_WIDTH - (PIECE_BORDER_PADDING * 2);

                        stroke = new BasicStroke(3f);
                        g2d.setStroke(stroke);

                        switch (position) {

                            case Board.CIRCLE:
                                g2d.setColor(Color.RED);
                                g2d.drawOval(leftPadding, topPadding, size, size);
                                break;
                            case Board.CROSS:
                                g2d.setColor(Color.BLUE);
                                g2d.drawLine(leftPadding, topPadding, leftPadding + size, topPadding + size);
                                g2d.drawLine(leftPadding + size, topPadding, leftPadding, topPadding + size);
                                break;
                            default:
                                break;
                        }
                    }
                }
            }
        }

        // If a board has a winner, draw the winner piece over it
        for (int row = 0; row < Board.SIZE; row++) {
            for (int col = 0; col < Board.SIZE; col++) {
                Board board = game.getBoard(row, col);
                int winner = board.getWinner();
                if (winner == Board.EMPTY)
                    continue;

                g2d.setColor(OVERLAY_COLOR);
                g2d.setBackground(OVERLAY_COLOR);
                // Draw grey out box
                g2d.fillRect(col * Board.SIZE * BOX_WIDTH, (row * Board.SIZE * BOX_WIDTH) + PADDING_HEIGHT,
                        Board.SIZE * BOX_WIDTH,
                        Board.SIZE * BOX_WIDTH);

                if (winner == Board.TIE)
                    continue;

                stroke = new BasicStroke(DEFAULT_STROKE_SIZE * Board.SIZE);
                g2d.setStroke(stroke);

                g2d.setColor(Color.RED);

                int boardWidth = Model.SIZE * BOX_WIDTH;
                double paddingFactor = 1;
                int leftPadding = (int) ((boardWidth * col) + (BOX_WIDTH * col)
                        + (PIECE_BORDER_PADDING * Board.SIZE * paddingFactor));
                int topPadding = (int) ((boardWidth * row) + (BOX_WIDTH * row)
                        + (PIECE_BORDER_PADDING * Board.SIZE * paddingFactor)
                        + PADDING_HEIGHT);
                int size = (int) ((BOX_WIDTH - (PIECE_BORDER_PADDING * (paddingFactor * 2))) * Board.SIZE);

                if (winner == Board.CIRCLE) {
                    g2d.drawOval(leftPadding, topPadding, size, size);
                } else { // Board.CROSS
                    g2d.setColor(Color.BLUE);
                    topPadding -= BOX_WIDTH;
                    g2d.drawLine(leftPadding, topPadding, leftPadding + size, topPadding + size);
                    g2d.drawLine(leftPadding + size, topPadding, leftPadding, topPadding + size);
                }
            }
        }
    }

    public void paint(Graphics g) {
        super.paint(g);
        drawLines(g);
    }

    public static void main(String[] args) {
        game = new Model();

        game.setBoardPosition(0, 0, 1, 1, Board.CIRCLE);
        game.setBoardPosition(0, 0, 1, 0, Board.CIRCLE);
        game.setBoardPosition(0, 0, 2, 1, Board.CIRCLE);
        game.setBoardPosition(0, 0, 1, 2, Board.CIRCLE);

        game.setBoardPosition(1, 0, 1, 1, Board.CROSS);
        game.setBoardPosition(1, 0, 1, 0, Board.CROSS);
        game.setBoardPosition(1, 0, 2, 1, Board.CROSS);
        game.setBoardPosition(1, 0, 1, 2, Board.CROSS);

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                UltimateTicTacToeUI game = new UltimateTicTacToeUI();
                game.setVisible(true);

                game.addMouseMotionListener(game);
            }
        });
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        // TODO Auto-generated method stub
        System.out.println(e);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        getPositionFromLocation(e.getX(), e.getY());

    }

    int[] getPositionFromLocation(int x, int y) {
        int[] pos = new int[4]; // Board row, board col, row col

        y -= PADDING_HEIGHT;

        pos[0] = x / BOX_WIDTH / Board.SIZE;
        pos[1] = y / BOX_WIDTH / Board.SIZE;
        pos[2] = (x / BOX_WIDTH) % Board.SIZE;
        pos[3] = (y / BOX_WIDTH) % Board.SIZE;

        //

        int posval = (x / BOX_WIDTH) + (y / BOX_WIDTH) * 9;
        int boardc = (posval % 9) / 3;
        int boardr = posval / 27;
        int smallc = (posval % 3);
        int smallr = (posval % 9) % 3;

        System.out.println("posval: " + posval);
        System.out.println("br: " + boardr);
        System.out.println("bc: " + boardc);
        System.out.println("sr: " + smallr);
        System.out.println("sc: " + smallc);
        // x = 0, y = PADDING_HEIGHT

        return pos;
    }
}
