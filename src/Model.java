public class Model {
    /** Creates 2d array to hold boards */
    private Board[][] boards;

    /** Large board */
    private Board largeBoard;

    /** Represents the column index the player has to player next */
    private int nextValidColumn;

    /** Represents the row index the player has to player next */
    private int nextValidRow;

    /** Constant for size of board */
    public static final int SIZE = 3;

    public Model() {
        boards = new Board[SIZE][SIZE];
        largeBoard = new Board();

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
        setNextValidMove(row, column);
        boards[boardRow][boardColumn].setBoardPosition(row, column, value);

        largeBoard.setBoardPosition(boardRow, boardColumn, boards[boardRow][boardColumn].getWinner());
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
     * Flips current player to other player
     */
    public void flipCurrentPlayer() {
        largeBoard.flipCurrentPlayer();
    }

    public int getBoardPosition(int boardRow, int boardColumn, int positionRow, int positionColumn) {
        return boards[boardRow][boardColumn].getBoardPosition(positionRow, positionColumn);
    }
}
