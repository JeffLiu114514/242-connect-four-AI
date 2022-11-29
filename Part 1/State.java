import java.util.LinkedList;

public class State {
    public int[][] board;
    public final int playerX = 1;
    public final int playerO = -1;
    public final int init = 0;
    public int lastPlayer;
    public int winner;
    public enum Result { someoneWin, draw, notOverYet };

    public void switchPlayer(State state) {
        if (lastPlayer == playerO) {
            state.lastPlayer = playerX;
        } else if (lastPlayer == playerX) {
            state.lastPlayer = playerO;
        }
    }

    public State() {
        this.board = new int[3][3];
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                board[i][j] = init;

        this.lastPlayer = playerO;//player O get to play first
    }

    public int evaluation(int player, Result result) throws Exception {
        switch (result) {
            case someoneWin -> {
                if (player==Minimax.AiPlayer) {
                    return 100;
                } else {
                    return -100;
                }
            }
            case draw ->  {
                return 0;
            }
            default -> {
                throw new Exception("evaluation error: evaluating when the game is not over yet.");
            }
        }
    }

    public boolean checkColumn(int column){
        for (int i = 2; i >= 0; i--) {
            if (board[i][column] == init) return true;
        }
        return false;
    }

    public State action(int column) {
        State newState = this;
        switchPlayer(newState);
        for (int i = 2; i >= 0; i--) {
            if (newState.board[i][column] == init) { //That column is not filled and can be put in.
                newState.board[i][column] = lastPlayer;
                break;
            }
        }
        return newState;
    }

    //print board
    public void printBoard() {
        System.out.println("   a   b   c");
        System.out.println("  ---+---+---");
        for (int i = 0; i < 3; i++) {
            System.out.print(i + 1 + "|");
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == playerO) {
                    System.out.print(" " + "O" + " |");
                } else if (board[i][j] == playerX) {
                    System.out.print(" " + "X" + " |");
                } else {
                    System.out.print(" " + " " + " |");
                }
            }
            System.out.println();
            System.out.println("  ---+---+---");
        }
    }

    public boolean checkWin() {
        for (int i = 2; i >= 0; i--) { //3 connecting in row
            if (board[i][0] == board[i][1] && board[i][1] == board[i][2] && board[i][0] != init) {
                winner = board[i][0];//TODO: winner目前没有使用
                return true;
            }
        }

        for (int i = 2; i >= 0; i--) { // 3 connecting in column
            if (board[0][i] == board[1][i] && board[1][i] == board[2][i] && board[0][i] != init) {
                winner = board[0][i];
                return true;
            }

        }

        // 3 connecting diagonal
        if ((board[0][0] == board[1][1] && board[1][1] == board[2][2] && board[1][1] != init) | board[0][2] == board[1][1] && board[1][1] == board[2][0] && board[1][1] != init) {
            winner = board[1][1];
            return true;
        }

        return false;
    }

    public Result checkGameOver() {
        if(checkWin())
            return Result.someoneWin;

        for(int row=0; row<3; row++) {
            for(int col=0; col<3; col++) {
                if(board[row][col] == init) {
                    return Result.notOverYet;
                }
            }
        }
        return Result.draw;
    }

    public boolean checkDraw() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == 0) return false;
            }
        }
        return true;
    }

    public LinkedList<State> getFrontiers() {
        LinkedList<State> frontiers = new LinkedList<>();
        State child;
        for (int i = 0; i < 3; i++) {
            child = newBoard(this);
            if (checkColumn(i)) {
                child.action(i);
                //child.printBoard();
                frontiers.add(child);
            }
        }
        return frontiers;
    }

    public State newBoard(State state) {//deep clone
        State newBoard = new State();
        newBoard.lastPlayer = state.lastPlayer;
        newBoard.winner = state.winner;
        newBoard.board = new int[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                newBoard.board[i][j] = state.board[i][j];
            }
        }
        return newBoard;
    }
}