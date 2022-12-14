public class Model {
    /** Creates 2d array to hold boards */
    private Board[][] boards;

    /** Large board */
    private Board largeBoard;

    /** Represents the column index the player has to player next */
    private int nextValidColumn;

    /** Represents the row index the player has to player next */
    private int nextValidRow;

    /** True if first move of game */
    private boolean isFirstMove;

    /** Counter for number of moves */
    private int counter = 0;

    /** Constant for size of board */
    public static final int SIZE = 3;

    public Model() {
        boards = new Board[SIZE][SIZE];
        largeBoard = new Board();
        isFirstMove = true;

        initBoard();
    }

    private void initBoard() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                boards[i][j] = new Board();
            }
        }
    }

    /**
     * Sets position of board in boards to value and updates large board
     * 
     * @param boardRow    row of board to set
     * @param boardColumn column of board to set
     * @param row         row to set
     * @param column      column to set
     * @param value       value to set to
     */
    public void setBoardPosition(int boardRow, int boardColumn, int row, int column, int value) {
        isFirstMove = false;
        setNextValidMove(row, column);
        boards[boardRow][boardColumn].setBoardPosition(row, column, value);

        largeBoard.setBoardPosition(boardRow, boardColumn, boards[boardRow][boardColumn].getWinner());

        counter++;
    }

    private void setNextValidMove(int row, int column) {
        nextValidRow = row;
        nextValidColumn = column;
    }

    public int getNextValidColumn() {
        return nextValidColumn;
    }

    public int getNextValidRow() {
        return nextValidRow;
    }

    public int getCurrentPlayer() {
        return largeBoard.getCurrentPlayer();

    }

    /**
     * Returns winner of large board
     * 
     * @return winner, if any
     */
    public int getWinner() {
        return largeBoard.getWinner();
    }

    public Board getBoard(int row, int column) {
        return boards[row][column];
    }

    public Board[] getBoardsRow(int row) {
        return boards[row];
    }

    /**
     * Returns true if move is valid
     * 
     * @param boardRow
     * @param boardColumn
     * @param row
     * @param column
     * @return true if move is valid
     */
    public boolean isValidMove(int boardRow, int boardColumn, int row, int column) {
        Board board = getBoard(boardRow, boardColumn);

        // If board cannot be played on
        if (board.getWinner() != Board.EMPTY)
            return false;

        // If position cannot be played on
        if (board.getBoardPosition(row, column) != Board.EMPTY)
            return false;

        // If invalid board in gameplay
        if (!isFirstMove && (boardRow != getNextValidRow() || boardColumn != getNextValidColumn())
                && getBoard(getNextValidRow(), getNextValidColumn()).getWinner() == Board.EMPTY)
            return false;

        return true;
    }

    /**
     * Flips current player to other player
     */
    public void flipCurrentPlayer() {
        largeBoard.flipCurrentPlayer();
    }

    public int getBoardPosition(int boardRow, int boardColumn, int positionRow, int positionColumn) {
        return boards[boardRow][boardColumn].getBoardPosition(positionRow, positionColumn);
    }

    /**
     * Returns number of moves made
     * 
     * @return number of moves made
     */
    public int getNumberOfMoves() {
        return counter;
    }
}
