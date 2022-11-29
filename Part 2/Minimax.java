import java.util.LinkedList;

public class Minimax {
    int maxValue;
    int minValue;
    static int AiPlayer;
    static int humanPlayer;
    static final int infinity = 1000000;

    public Minimax(int player) {
        this.AiPlayer = player;
        if (AiPlayer == 1) {
            humanPlayer = -1;
        } else {
            humanPlayer = 1;
        }
    }

    public int minimaxEvaluation(State state, int depth, int alpha, int beta, int player) throws Exception {//TODO:alpha-beta pruning + depth
        State.Result result = state.checkGameOver();
        if (depth == 0 || result != State.Result.notOverYet) {//TODO: 这里有可能出问题
            return state.evaluation(player, result);//static evaluation of the state
        }
        if (AiPlayer == player) { //maximizing player: AI
            LinkedList<State> temp = state.getFrontiers();
            maxValue = -infinity;
            for (int i = 0; i < temp.size(); i++) { // for each child
                int eval = minimaxEvaluation(temp.get(i), depth - 1, alpha, beta, humanPlayer);
                maxValue = Math.max(maxValue, eval);
                alpha = Math.max(alpha, maxValue);
                if (beta <= alpha)
                    break;
            }
            return maxValue;
        } else { // minimizing player: Human
            LinkedList<State> temp = state.getFrontiers();
            minValue = infinity;
            for (int i = 0; i < temp.size(); i++) { // for each child
                int eval = minimaxEvaluation(temp.get(i), depth - 1, alpha, beta, AiPlayer);
                minValue = Math.min(minValue, eval);
                beta = Math.min(beta, minValue);
                if (beta <= alpha)
                    break;
            }
            return minValue;
        }
    }
}


