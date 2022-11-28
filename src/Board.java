import java.util.Random;

public class Board {
    /** Holds each position on board */
    private int[][] board;

    /** Constant for size of board */
    public static final int SIZE = 3;

    /** X Player value */
    public static final int CROSS = 2;
    /** O Player value */
    public static final int CIRCLE = 1;
    /** Empty value */
    public static final int EMPTY = 0;
    /** Tie value */
    public static final int TIE = -1;

    /** Current player turn */
    private int currentPlayer;

    /**
     * Default constructor
     */
    public Board() {
        Random r = new Random();
        board = new int[SIZE][SIZE];
        resetBoard();

        currentPlayer = r.nextInt(1) + 1; // 1,2
    }

    /**
     * Gets winner, if any
     * 
     * @return winner of game
     */
    public int getWinner() {
        // All three columns in a row are the same
        for (int[] row : board) {
            int value = row[0];
            boolean isSame = true;

            for (int elem : row) {
                if (elem != value)
                    isSame = false;
            }

            if (isSame && value != EMPTY)
                return value;
        }

        // All three rows in a column are the same
        for (int i = 0; i < board.length; i++) {
            int value = board[0][i];
            boolean isSame = true;

            for (int[] row : board) {
                if (row[i] != value)
                    isSame = false;
            }

            if (isSame && value != EMPTY)
                return value;
        }

        // Diagonals are the same
        int value = board[1][1];
        boolean isSame = true;

        for (int i = 0; i < board.length; i++) {
            if (board[i][i] != value)
                isSame = false;
        }

        if (isSame && value != EMPTY)
            return value;
        else {
            isSame = true;
            for (int i = 0; i < board.length; i++) {
                if (board[i][2 - i] != value)
                    isSame = false;
            }
            if (isSame && value != EMPTY)
                return value;
        }

        // Check for tie
        boolean isTie = true;

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                if (board[i][j] == EMPTY)
                    isTie = false;
            }
        }
        if (isTie)
            return TIE;

        return EMPTY;
    }

    public int getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(int currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public int getBoardPosition(int row, int column) {
        if (row < 0 || row > board.length - 1)
            throw new IllegalArgumentException();
        if (column < 0 || column > board.length - 1)
            throw new IllegalArgumentException();

        return board[row][column];
    }

    public void setBoardPosition(int row, int column, int value) {
        if (row < 0 || row > board.length - 1)
            throw new IllegalArgumentException();
        if (column < 0 || column > board.length - 1)
            throw new IllegalArgumentException();

        board[row][column] = value;
    }

    /**
     * Resets board to empty
     */
    public void resetBoard() {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                board[i][j] = EMPTY;
            }
        }
    }

    /**
     * Flips current player to other player
     */
    public void flipCurrentPlayer() {
        currentPlayer = 3 - currentPlayer;
    }
}