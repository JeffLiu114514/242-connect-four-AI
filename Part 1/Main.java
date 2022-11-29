import java.util.LinkedList;
import java.util.Scanner;
import java.util.Random;

public class Main {
    public static void main(String[] args) throws Exception {
        Random rand = new Random();
        Scanner sc = new Scanner(System.in);
        State state = new State();
        Minimax minimax;
        int max;
        int value;
        LinkedList<State> frontier;
        System.out.println("Please choose your figure: X or O. X acts first.");
        String input = sc.next();

        //Let the player choose side.
        while (!(input.equals("X") || input.equals("O"))) {
            if (input.isEmpty()) System.exit(0);
            System.out.println("Invalid input! Choose again. Leave it blank to exit.");
            input = sc.next();
        }

        if (input.equals("X")) {
            minimax = new Minimax(-1); //AI player initialized to O
        } else {
            minimax = new Minimax(1); //AI player initialized to X
        }
        state.lastPlayer = state.playerO; //Previous move initialized to player0. First move will be playerX

        System.out.println("Board initialized.");
        state.printBoard();

        //in game
        while (state.checkGameOver() == State.Result.notOverYet) { // while game is not over
            System.out.println();
            if (state.lastPlayer == minimax.AiPlayer) { //now it is human player's turn
                System.out.println("Your move (choose column).");
                input = sc.next();
                while (!(input.equals("a") || input.equals("b") || input.equals("c"))) {
                    System.out.println("Invalid input! Choose column again.");
                    input = sc.next();
                }
                while (((input.equals("a")) && !state.checkColumn(0)) || ((input.equals("b")) && !state.checkColumn(1))
                        || ((input.equals("c")) && !state.checkColumn(2))) {
                    System.out.println("Column full! Choose a valid column.");
                    input = sc.next();
                }
                switch (input) {
                    case "a" -> {
                        state = state.action(0);
                    }
                    case "b" -> {
                        state = state.action(1);
                    }
                    default -> {
                        state = state.action(2);
                    }
                }
            }
            else { // AI's turn
                System.out.println("AI playing");
                frontier = state.getFrontiers();
                if (frontier.size() == 0)
                    throw new Exception("Error: No new frontiers.");

                max=-100;
                for (int i = 0; i < frontier.size(); i++) {
                    value = minimax.minimaxEvaluation(frontier.get(i), minimax.AiPlayer);
                    if (max < value) {
                        state = frontier.get(i);
                        max = value;
                    }
                }
                if (max == -100) {
                    int idx = rand.nextInt(frontier.size());
                    state = frontier.get(idx);
                }//the best choice is to lose
            }
            state.printBoard();
        }

        //game finished
        if (state.checkWin()) {
            if (state.winner == minimax.humanPlayer) {
                System.out.println("Player win!");
            } else {
                System.out.println("AI rules!");
            }
        } else if (state.checkDraw()) {
            System.out.println("Nobody wins.");
        }

    }
}