import java.util.LinkedList;
import java.util.Scanner;
import java.util.Random;

public class Main {

    public static void main(String[] args) throws Exception {
        int DEPTH;
        Random rand = new Random();
        Scanner sc = new Scanner(System.in);
        State state = new State();
        Minimax minimax;
        int max;
        int value;
        LinkedList<State> frontier;

        //let player choose side.
        System.out.println("Please choose your figure: X or O. X acts first.");
        String input = sc.next();
        while (!(input.equals("X") || input.equals("O"))) {
            if (input.isEmpty()) System.exit(0);
            System.out.println("Invalid input! Choose again. Leave it blank to exit.");
            input = sc.next();
        }

        //let player choose the depth of minimax algorithm
        System.out.println("Choose the depth of minimax algorithm: (less than 15 if you want good performance)");
        DEPTH = sc.nextInt();


        if (input.equals("X")) {
            minimax = new Minimax(-1); //AI player initialized to O
        } else {
            minimax = new Minimax(1); //AI player initialized to X
        }
        state.lastPlayer = state.playerO; //Previous move initialized to player0. First move will be playerX

        System.out.println("Board initialized.");
        state.printBoard();

        //in game
        State.Result result; // the result of the game
        while ((result = state.checkGameOver()) == State.Result.notOverYet) { // while game is not over
            System.out.println();
            if (state.lastPlayer == minimax.AiPlayer) { //now it's human player's turn
                System.out.println("Your move (choose column).");
                input = sc.next();
                while (!(input.equals("a") || input.equals("b") || input.equals("c") || input.equals("d")
                        || input.equals("e") || input.equals("f") || input.equals("g"))) {
                    System.out.println("Invalid input! Choose column again.");
                    input = sc.next();
                }
                while (((input.equals("a")) && !state.checkColumn(0)) ||
                        ((input.equals("b")) && !state.checkColumn(1)) ||
                        ((input.equals("c")) && !state.checkColumn(2)) ||
                        ((input.equals("d")) && !state.checkColumn(3)) ||
                        ((input.equals("e")) && !state.checkColumn(4)) ||
                        ((input.equals("f")) && !state.checkColumn(5)) ||
                        ((input.equals("g")) && !state.checkColumn(6))) {
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
                    case "c" -> {
                        state = state.action(2);
                    }
                    case "d" -> {
                        state = state.action(3);
                    }
                    case "e" -> {
                        state = state.action(4);
                    }
                    case "f" -> {
                        state = state.action(5);
                    }
                    default -> {
                        state = state.action(6);
                    }
                }
            } else { // AI's turn
                System.out.println("AI playing");
                frontier = state.getFrontiers();
                if (frontier.size() == 0)
                    throw new Exception("Error: No new frontiers.");
                max = -100;
                for (int i = 0; i < frontier.size(); i++) {
                    value = minimax.minimaxEvaluation(frontier.get(i), DEPTH, -state.infinity, state.infinity, minimax.AiPlayer);
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
        switch (result) {
            case someoneWin -> {
                if (state.winner == minimax.humanPlayer) {
                    System.out.println("Player win!");
                } else {
                    System.out.println("AI rules!");
                }
            }
            case draw -> {
                System.out.println("Nobody wins.");
            }
        }

    }
}