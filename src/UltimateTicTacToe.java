import java.util.Random;
import java.util.Scanner;

public class UltimateTicTacToe {

    public static final String RESET = "\033[0m"; // Text Reset

    // Regular Colors
    public static final String BLACK = "\033[0;30m"; // BLACK
    public static final String RED = "\033[0;31m"; // RED
    public static final String GREEN = "\033[0;32m"; // GREEN
    public static final String YELLOW = "\033[0;33m"; // YELLOW
    public static final String BLUE = "\033[0;34m"; // BLUE
    public static final String PURPLE = "\033[0;35m"; // PURPLE
    public static final String CYAN = "\033[0;36m"; // CYAN
    public static final String WHITE = "\033[0;37m"; // WHITE

    /** Game model */
    private static Model game;

    enum Mode {
        DEFAULT, SHOW_BOARD_NUMBERS
    }

    public static void main(String[] args) {
        game = new Model();
        Scanner input = new Scanner(System.in);
        Random rand = new Random();

        boolean firstMove = true;
        while (game.getWinner() == Board.EMPTY) {
            System.out.println();
            printBoards(game, Mode.SHOW_BOARD_NUMBERS);
            System.out.println();

            System.out.println("Player " + getBoardString(game.getCurrentPlayer()) + " move.");

            int boardRow, boardColumn;
            int boardIndex = getPosFromRowAndColumn(game.getNextValidRow(), game.getNextValidColumn());

            if (firstMove ||
                    game.getBoard(getRowFromPosition(boardIndex), getColumnFromPosition(boardIndex))
                            .getWinner() != Board.EMPTY) {
                firstMove = false;

                System.out.print("Select a board 1-9: ");
                while (!input.hasNextInt()) {
                    input.next();
                    System.out.print("Select a board 1-9:");
                }
                boardIndex = input.nextInt();
            }

            if (boardIndex < 1 || boardIndex > 9) {
                continue;
            }

            System.out.println("Playing on board " + boardIndex);

            boardRow = getRowFromPosition(boardIndex);
            boardColumn = getColumnFromPosition(boardIndex);

            if (game.getBoard(boardRow, boardColumn).getWinner() != Board.EMPTY) {
                continue;
            }

            makeMove(input, boardRow, boardColumn);

            printBoards(game, Mode.DEFAULT);
            // If game is over, don't let other player make move
            if (game.getWinner() != Board.EMPTY) {
                break;
            }
            game.flipCurrentPlayer();

            System.out.println("Player " + getBoardString(game.getCurrentPlayer()) + " move.");

            boardIndex = getPosFromRowAndColumn(game.getNextValidRow(), game.getNextValidColumn());

            if (game.getBoard(getRowFromPosition(boardIndex), getColumnFromPosition(boardIndex))
                    .getWinner() != Board.EMPTY) {

                System.out.print("Select a board 1-9: ");
                while (!input.hasNextInt()) {
                    input.next();
                    System.out.print("Select a board 1-9:");
                }
                boardIndex = input.nextInt();
            }

            int nextBoardRow = game.getNextValidRow();
            int nextBoardColumn = game.getNextValidColumn();

            boardIndex = getPosFromRowAndColumn(nextBoardRow, nextBoardColumn);

            if (game.getBoard(getRowFromPosition(boardIndex), getColumnFromPosition(boardIndex))
                    .getWinner() != Board.EMPTY) {
                int newIndex = boardIndex;

                while (game.getBoard(getRowFromPosition(newIndex), getColumnFromPosition(newIndex))
                        .getWinner() != Board.EMPTY) {
                    newIndex = rand.nextInt(9);
                    if (newIndex < 1 || newIndex > 9) {
                        newIndex = boardIndex;
                    }
                }

                boardIndex = newIndex;
            }

            System.out.println("Playing on board " + boardIndex);

            makeMove(input, nextBoardRow, nextBoardColumn);
            game.flipCurrentPlayer();
        }

        printBoards(game, Mode.DEFAULT);

        System.out.print("Game over! ");
        if (game.getWinner() == Board.CIRCLE) {
            System.out.println(RED + "O wins!" + RESET);
        } else if (game.getWinner() == Board.CROSS) {
            System.out.println(BLUE + "X wins!" + RESET);
        } else {
            System.out.println("You tied!");
        }
        input.close();
    }

    private static void makeMove(Scanner input, int boardRow, int boardColumn) {
        boolean hasMoved = false;
        while (!hasMoved) {
            System.out.print("Select a position 1-9: ");
            while (!input.hasNextInt()) {
                input.next();
                System.out.print("Select a position 1-9:");
            }

            int positionRow, positionColumn;
            int positionIndex = input.nextInt();

            if (positionIndex < 1 || positionIndex > 9) {
                continue;
            }

            positionRow = getRowFromPosition(positionIndex);
            positionColumn = getColumnFromPosition(positionIndex);

            hasMoved = true;

            if (game.getBoardPosition(boardRow, boardColumn, positionRow, positionColumn) != Board.EMPTY) {
                hasMoved = false;
                continue;
            }
            try {
                game.setBoardPosition(boardRow, boardColumn, positionRow, positionColumn, game.getCurrentPlayer());
            } catch (Exception e) {
                hasMoved = false;
            }
        }
    }

    public static void printBoards(Model game, Mode mode) {

        for (int i = 0; i < Board.SIZE; i++) { // For each row of boards
            Board[] boards = game.getBoardsRow(i);
            // For each row in each board
            for (int j = 0; j < boards.length; j++) {
                for (int boardIndex = 0; boardIndex < boards.length; boardIndex++) {
                    for (int k = 0; k < boards.length; k++) {

                        System.out.print(getBoardString(boards[boardIndex].getBoardPosition(j, k)));
                        if (k < boards.length - 1)
                            System.out.print("|");
                    }
                    if (boardIndex < boards.length - 1)
                        System.out.print(" | ");
                }
                System.out.println();
            }

            if (i < boards.length - 1)
                for (int j = 0; j < 21; j++) {
                    System.out.print("-");
                }
            System.out.println();
        }
    }

    private static int getPosFromRowAndColumn(int row, int column) {
        return row * 3 + column + 1;
    }

    private static int getRowFromPosition(int position) {
        return (position - 1) / 3;
    }

    private static int getColumnFromPosition(int position) {
        return (position - 1) % 3;
    }

    private static String getBoardString(int piece) {
        if (piece == Board.CIRCLE)
            return RED + "O" + RESET;
        if (piece == Board.CROSS)
            return BLUE + "X" + RESET;
        return BLACK + "-" + RESET;
    }
}
