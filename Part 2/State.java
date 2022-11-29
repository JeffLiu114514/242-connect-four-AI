import java.util.LinkedList;
import java.util.Objects;

public class State {
    public int n_row = 6;
    public int n_column = 7;
    public int[][] board;
    public final int playerX = 1;
    public final int playerO = -1;
    public final int init = 0;
    public int lastPlayer;
    public int winner;
    public final int infinity = 1000000;

    public enum Result {someoneWin, draw, notOverYet}

    public void switchPlayer(State state) {
        if (lastPlayer == playerO) {
            state.lastPlayer = playerX;
        } else if (lastPlayer == playerX) {
            state.lastPlayer = playerO;
        }
    }

    public State() {
        this.board = new int[n_row][n_column];
        for (int i = 0; i < n_row; i++)
            for (int j = 0; j < n_column; j++)
                board[i][j] = init;

        this.lastPlayer = playerO;//player O get to play first
    }

    public boolean checkWin() {
        for (int i = 5; i >= 0; i--) { //4 connecting in row
            for (int j = 0; j < 4; j++) {
                if (board[i][j] == board[i][j + 1] && board[i][j] == board[i][j + 2] && board[i][j] == board[i][j + 3] && board[i][j] != init) {
                    winner = board[i][j];
                    return true;
                }
            }
        }

        for (int i = 5; i >= 3; i--) { //4 connecting in column
            for (int j = 0; j < 7; j++) {
                if (board[i][j] == board[i - 1][j] && board[i][j] == board[i - 2][j] && board[i][j] == board[i - 3][j] && board[i][j] != init) {
                    winner = board[i][j];
                    return true;
                }
            }
        }

        for (int i = 0; i < 3; i++) { //4 connecting in main diagonal direction
            for (int j = 0; j < 4; j++) {
                if (board[i][j] == board[i + 1][j + 1] && board[i][j] == board[i + 2][j + 2] && board[i][j] == board[i + 3][j + 3] && board[i][j] != init) {
                    winner = board[i][j];
                    return true;
                }
            }
        }

        for (int i = 0; i < 6; i++) { //4 connecting in counter diagonal direction
            for (int j = 0; j < 7; j++) {
                if (canMove(i - 3, j + 3)) {
                    if (board[i][j] == board[i - 1][j + 1] && board[i][j] == board[i - 2][j + 2] && board[i][j] == board[i - 3][j + 3] && board[i][j] != init) {
                        winner = board[i][j];
                        return true;
                    }
                }
            }
        }

        winner = 0;
        return false;
    }

    public int evaluation(int player, Result result) {//heuristic static evaluation of a board
        int Xlines = 0;
        int Olines = 0;
        Xlines = checkThree(1) *100 + checkTwo(1)*10; // three connecting counts for 100 points
        Olines = checkThree(-1)*100 + checkTwo(-1)*10; // two connecting counts for 10 points
        if (player == playerO) {//player is playerO
            if (result == Result.someoneWin) {
                if (winner == player) { // if an action leads to victory or defeat, heuristic value is set to infinity
                    Olines = Integer.MAX_VALUE;
                } else {
                    Xlines = Integer.MAX_VALUE;
                }
            }
            return Olines - Xlines;
        } else {
            if (result == Result.someoneWin) {
                if (winner == player) {
                    Xlines = Integer.MAX_VALUE;
                } else {
                    Olines = Integer.MAX_VALUE;
                }
            }
            return  Xlines - Olines;
        }
    }

    public boolean checkColumn(int column) {
        for (int i = n_row - 1; i >= 0; i--) {
            if (board[i][column] == init) return true;
        }
        return false;
    }

    public State action(int column) {
        State newState = this;
        switchPlayer(newState);
        for (int i = n_row - 1; i >= 0; i--) {
            if (newState.board[i][column] == init) { //That column is not filled and can be put in.
                newState.board[i][column] = lastPlayer;
                break;
            }
        }
        return newState;
    }

    //print board
    public void printBoard() {
        System.out.println("   a   b   c   d   e   f   g");
        System.out.println("  ---+---+---+---+---+---+---");
        for (int i = 0; i < n_row; i++) {
            System.out.print(i + 1 + "|");
            for (int j = 0; j < n_column; j++) {
                if (board[i][j] == playerO) {
                    System.out.print(" " + "O" + " |");
                } else if (board[i][j] == playerX) {
                    System.out.print(" " + "X" + " |");
                } else {
                    System.out.print(" " + " " + " |");
                }
            }
            System.out.println();
            System.out.println("  ---+---+---+---+---+---+---");
        }
    }



    //Find if there are three consequent pieces of the same player
    public int checkThree(int player) {
        int count = 0;

        for (int i = 5; i >= 0; i--) { //3 connecting in row
            for (int j = 0; j < 7; j++) {
                if (canMove(i, j + 2)) {
                    if (board[i][j] == board[i][j + 1] && board[i][j] == board[i][j + 2] && board[i][j] == player) {
                        count++;
                    }
                }
            }
        }

        for (int i = 5; i >= 0; i--) { //3 connecting in column
            for (int j = 0; j < 7; j++) {
                if (canMove(i - 2, j)) {
                    if (board[i][j] == board[i - 1][j] && board[i][j] == board[i - 2][j] && board[i][j] == player) {
                        count++;
                    }
                }
            }
        }

        for (int i = 0; i < 6; i++) { //3 connecting in main diagonal direction
            for (int j = 0; j < 7; j++) {
                if (canMove(i + 2, j + 2)) {
                    if (board[i][j] == board[i + 1][j + 1] && board[i][j] == board[i + 2][j + 2] && board[i][j] == player) {
                        count++;
                    }
                }
            }
        }

        for (int i = 0; i < 6; i++) { //3 connecting in counter diagonal direction
            for (int j = 0; j < 7; j++) {
                if (canMove(i - 2, j + 2)) {
                    if (board[i][j] == board[i - 1][j + 1] && board[i][j] == board[i - 2][j + 2] && board[i][j] == player) {
                        count++;
                    }
                }
            }
        }
        return count;
    }

    //Find if there are two consequent pieces of the same player
    public int checkTwo(int player) {
        int count = 0;

        for (int i = 5; i >= 0; i--) { //2 connecting in row
            for (int j = 0; j < 7; j++) {
                if (canMove(i, j + 1)) {
                    if (board[i][j] == board[i][j + 1] && board[i][j] == player) {
                        count++;
                    }
                }
            }
        }

        for (int i = 5; i >= 0; i--) { //2 connecting in column
            for (int j = 0; j < 7; j++) {
                if (canMove(i - 1, j)) {
                    if (board[i][j] == board[i - 1][j] && board[i][j] == player) {
                        count++;
                    }
                }
            }
        }

        for (int i = 0; i < 6; i++) {  //2 connecting in main diagonal direction
            for (int j = 0; j < 7; j++) {
                if (canMove(i + 1, j + 1)) {
                    if (board[i][j] == board[i + 1][j + 1] && board[i][j] == player) {
                        count++;
                    }
                }
            }
        }

        for (int i = 0; i < 6; i++) { //2 connecting in counter diagonal direction
            for (int j = 0; j < 7; j++) {
                if (canMove(i - 1, j + 1)) {
                    if (board[i][j] == board[i - 1][j + 1] && board[i][j] == player) {
                        count++;
                    }
                }
            }
        }
        return count;
    }

    public Result checkGameOver() {
        if (checkWin())
            return Result.someoneWin;

        for (int row = 0; row < n_row; row++) {
            for (int col = 0; col < n_column; col++) {
                if (board[row][col] == init) {
                    return Result.notOverYet;
                }
            }
        }
        return Result.draw;
    }

    public LinkedList<State> getFrontiers() {
        LinkedList<State> frontiers = new LinkedList<>();
        State child;
        for (int i = 0; i < n_column; i++) {
            child = newBoard(this);
            if (checkColumn(i)) {
                child.action(i);
                //child.printBoard();
                frontiers.add(child);
            }
        }
        return frontiers;
    }

    public boolean canMove(int row, int col) { //check if it's inside the board
        if ((row <= -1) || (col <= -1) || (row > 5) || (col > 6)) {
            return false;
        }
        return true;
    }

    public State newBoard(State state) {//deep clone
        State newBoard = new State();
        newBoard.lastPlayer = state.lastPlayer;
        newBoard.winner = state.winner;
        newBoard.board = new int[n_row][n_column];
        for (int i = 0; i < n_row; i++) {
            for (int j = 0; j < n_column; j++) {
                newBoard.board[i][j] = state.board[i][j];
            }
        }
        return newBoard;
    }
}
