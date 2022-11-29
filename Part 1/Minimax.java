import java.util.LinkedList;

public class Minimax {
    int maxValue;
    int minValue;
    static int AiPlayer;
    static int humanPlayer;


    public Minimax(int player) {
        this.AiPlayer = player;
        if (AiPlayer == 1) {
            humanPlayer = -1;
        } else {
            humanPlayer = 1;
        }
    }

    public int minimaxEvaluation(State state, int player) throws Exception {

        State.Result result = state.checkGameOver();
        if (result != State.Result.notOverYet) {//TODO: 这里有可能出问题
            return state.evaluation(player, result);//static evaluation of the state
        }
        if (AiPlayer == player) { //maximizing player
            LinkedList<State> temp = state.getFrontiers();

            maxValue = -100;
            for (int i = 0; i < temp.size(); i++) { // for each child

                maxValue = Math.max(maxValue, minimaxEvaluation(temp.get(i), humanPlayer));

            }

            return maxValue;
        } else { // minimizing player
            LinkedList<State> temp = state.getFrontiers();

            minValue = 100;
            for (int i = 0; i < temp.size(); i++) { // for each child

                minValue = Math.min(minValue, minimaxEvaluation(temp.get(i), AiPlayer));

            }
            return minValue;
        }
    }
}