import javax.swing.JFrame;

import java.awt.Graphics;
import java.awt.Stroke;
import java.awt.Color;
import java.awt.BasicStroke;
import java.awt.Graphics2D;

import javax.swing.SwingUtilities;

public class UltimateTicTacToeUI extends JFrame {
    /** Game to play with */
    private static Model game;

    /** Width on screen of box */
    public static final int BOX_WIDTH = 50;
    /** Padding height from top of screen */
    public static final int PADDING_HEIGHT = 25;
    /** Padding around pieces */
    public static final int PIECE_BORDER_PADDING = 5;

    public UltimateTicTacToeUI() {
        super("Ultimate Tic Tac Toe");

        int displaySize = BOX_WIDTH * Model.SIZE * Model.SIZE;
        setSize(displaySize, displaySize + PADDING_HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    void drawLines(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        Stroke stroke = new BasicStroke(2f);
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
            g2d.drawLine(BOX_WIDTH * (Model.SIZE / 3) * i, PADDING_HEIGHT, BOX_WIDTH * (Model.SIZE / 3) * i,
                    BOX_WIDTH * Model.SIZE * Model.SIZE + PADDING_HEIGHT);
        }
        for (int j = 0; j < Model.SIZE * Model.SIZE; j++) { // Board columns
            g2d.drawLine(0, BOX_WIDTH * (Model.SIZE / 3) * j + PADDING_HEIGHT, BOX_WIDTH * Model.SIZE * Model.SIZE,
                    BOX_WIDTH * (Model.SIZE / 3) * j + PADDING_HEIGHT);
        }

        // Draw X's and O's
        // g2d.drawOval(EXIT_ON_CLOSE, ABORT, WIDTH, HEIGHT);
        // g2d.drawLine(FRAMEBITS, ERROR, ALLBITS, ABORT);

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

        game.setBoardPosition(1, 0, 1, 1, Board.CROSS);
        game.setBoardPosition(1, 0, 1, 0, Board.CROSS);
        game.setBoardPosition(1, 0, 2, 1, Board.CROSS);

        /*
         * -|-|-
         * o|o|-
         * -|o|-
         * 
         * -|-|-|x|o
         * x|x|-|x|o
         * -|x|-|x|o
         * -|-|-|-|-
         *
         * 
         * 
         */
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new UltimateTicTacToeUI().setVisible(true);
            }
        });
    }
}
