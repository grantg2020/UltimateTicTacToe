import javax.swing.JFrame;

import java.awt.Graphics;
import java.awt.Stroke;
import java.awt.event.MouseEvent;
import java.awt.Color;
import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Canvas;
import javax.swing.SwingUtilities;
import java.awt.Font;
import javax.swing.event.MouseInputListener;
// Imports

public class UltimateTicTacToeUI extends JFrame implements MouseInputListener {

    /** Game to play with */
    private static Model game;

    /** Array that stores last hovered position */
    private int[] lastHoveredPosition;

    /** Bool for only drawing when hover position changes */
    private boolean isNewHoverPosition = false;

    /** Canvas for drawing */
    private Canvas view;

    /** Width on screen of box */
    public static final int BOX_WIDTH = 70;
    /** Padding height from top of screen */
    public static final int PADDING_HEIGHT = BOX_WIDTH / 2;
    /** Padding around pieces */
    public static final int PIECE_BORDER_PADDING = BOX_WIDTH / 10;
    /** Background shaded color */
    public static final Color OVERLAY_COLOR = new Color(0f, 0f, 0f,
            .3141592653589793238462643383279502884197169399375105f);
    /** Background shaded color */
    public static final Color HOVER_COLOR = new Color(0f, 0f, 0f,
            .805f);
    /** Stroke size for between each game board */
    private static final float BOARDS_STROKE_SIZE = 2f;
    /** Default stroke size */
    public static final float DEFAULT_STROKE_SIZE = (float) ((3f / 50.0) * BOX_WIDTH);

    public UltimateTicTacToeUI() {
        super("Ultimate Tic Tac Toe");

        int displaySize = BOX_WIDTH * Model.SIZE * Model.SIZE;
        setSize(displaySize, displaySize + PADDING_HEIGHT);

        view = new Canvas();
        view.setSize(displaySize, displaySize + PADDING_HEIGHT);
        view.setPreferredSize(view.getSize());

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
                                drawX(g2d, size, leftPadding, topPadding);
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
                int leftPadding = (int) ((boardWidth * col)
                        + (PIECE_BORDER_PADDING * Board.SIZE * paddingFactor));
                int topPadding = (int) ((boardWidth * row)
                        + (PIECE_BORDER_PADDING * Board.SIZE * paddingFactor)
                        + PADDING_HEIGHT);
                int size = (int) ((BOX_WIDTH - (PIECE_BORDER_PADDING * (paddingFactor * 2))) * Board.SIZE);

                if (winner == Board.CIRCLE) {
                    g2d.drawOval(leftPadding, topPadding, size, size);
                } else { // Board.CROSS
                    g2d.setColor(Color.BLUE);
                    topPadding -= BOX_WIDTH;
                    drawX(g2d, size, leftPadding, topPadding + BOX_WIDTH);
                }
            }
        }

        // If there is a last hovered position and no winner
        if (lastHoveredPosition != null && game.getWinner() == Board.EMPTY) {
            int col = lastHoveredPosition[3];
            int row = lastHoveredPosition[2];
            int boardRow = lastHoveredPosition[0];
            int boardColumn = lastHoveredPosition[1];
            if (game.getBoardPosition(boardRow, boardColumn, row, col) == Board.EMPTY
                    && game.getBoard(boardRow, boardColumn).getWinner() == Board.EMPTY
                    && game.isValidMove(boardRow, boardColumn, row, col)) {
                stroke = new BasicStroke(3f);
                g2d.setStroke(stroke);
                g2d.setColor(HOVER_COLOR);
                int boardWidth = Model.SIZE * BOX_WIDTH;
                // Padding for boards + padding for current board + border padding
                int leftPadding = (boardWidth * boardColumn) + (BOX_WIDTH * col) + PIECE_BORDER_PADDING;
                int topPadding = (boardWidth * boardRow) + (BOX_WIDTH * row) + PIECE_BORDER_PADDING
                        + PADDING_HEIGHT;
                int size = BOX_WIDTH - (PIECE_BORDER_PADDING * 2);

                switch (game.getCurrentPlayer()) {
                    case Board.CROSS:
                        drawX(g2d, size, leftPadding, topPadding);
                        break;
                    case Board.CIRCLE:
                        g2d.drawOval(leftPadding, topPadding, size, size);
                        break;
                    default:
                        break;
                }
            }
        }

        if (game.getWinner() == Board.EMPTY) {
            g2d.setColor(Color.BLACK);
            g2d.setFont(new Font("Helvetica", Font.PLAIN, 20));
            g2d.drawString("Total Moves: " + game.getNumberOfMoves(), (int) (BOX_WIDTH * 3.5), BOX_WIDTH * 5);
        }
    }

    /**
     * Draw an X with parameters
     * 
     * @param g    graphics to draw on
     * @param size size of X
     * @param x    start X position
     * @param y    start Y position
     */
    private void drawX(Graphics g, int size, int x, int y) {
        g.drawLine(x, y, x + size, y + size);
        g.drawLine(x + size, y, x, y + size);
    }

    public void paint(Graphics g) {
        view.paint(g);
        drawLines(g);
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        // Intentionally left empty
    }

    private void makeMove(int[] pos) {
        if (game.getWinner() != Board.EMPTY)
            return;

        int boardRow = pos[0];
        int boardColumn = pos[1];
        int row = pos[2];
        int col = pos[3];

        if (game.isValidMove(boardRow, boardColumn, row, col)) {
            game.setBoardPosition(boardRow, boardColumn, row, col, game.getCurrentPlayer());
            game.flipCurrentPlayer();
        }

        lastHoveredPosition = null;
        repaint();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        int[] pos = getPositionFromLocation(e.getX(), e.getY());
        setLastHoveredPosition(pos);

        if (isNewHoverPosition)
            this.repaint();
    }

    int[] getPositionFromLocation(int x, int y) {
        int[] pos = new int[4]; // Board row, board col, row, col

        y -= PADDING_HEIGHT;

        pos[1] = x / BOX_WIDTH / Board.SIZE;
        pos[0] = y / BOX_WIDTH / Board.SIZE;
        pos[3] = (x / BOX_WIDTH) % Board.SIZE;
        pos[2] = (y / BOX_WIDTH) % Board.SIZE;

        for (int i = 0; i < pos.length; i++) {
            pos[i] = pos[i] > 2 ? 2 : pos[i];
        }

        return pos;
    }

    public int[] getLastHoveredPosition() {
        return lastHoveredPosition;
    }

    public void setLastHoveredPosition(int[] lastHoveredPosition) {
        if (this.lastHoveredPosition == null)
            this.lastHoveredPosition = new int[4];
        boolean newHoverPosition = false;
        for (int i = 0; i < lastHoveredPosition.length; i++) {
            if (lastHoveredPosition[i] != this.lastHoveredPosition[i])
                newHoverPosition = true;

            this.lastHoveredPosition[i] = lastHoveredPosition[i];

        }
        isNewHoverPosition = newHoverPosition;

        if (game.getWinner() != Board.EMPTY)
            isNewHoverPosition = false;
    }

    @Override
    public void mouseClicked(MouseEvent e) {

        if (game.getWinner() != Board.EMPTY) {
            reset();
            return;
        }

        int[] pos = getPositionFromLocation(e.getX(), e.getY());
        makeMove(pos);
    }

    private void reset() {
        game = new Model();
        repaint();
    }

    @Override
    public void mousePressed(MouseEvent e) {
        // Intentionally left empty
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // Intentionally left empty

    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // Intentionally left empty
    }

    @Override
    public void mouseExited(MouseEvent e) {
        // Intentionally left empty
    }

    public static void main(String[] args) {
        game = new Model();

        // int player = Board.CIRCLE;

        // for (int j = 0; j < 9; j++) {
        // for (int i = 0; i < 3; i++) {
        // game.setBoardPosition(j / 3, j % 3, i, i, player);
        // game.setBoardPosition(j / 3, j % 3, i, i, player);
        // game.setBoardPosition(j / 3, j % 3, i, i, player);

        // }
        // }

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                UltimateTicTacToeUI game = new UltimateTicTacToeUI();
                game.setVisible(true);

                // Adds mouse listeners
                game.addMouseMotionListener(game);
                game.addMouseListener(game);
            }
        });
    }

}
