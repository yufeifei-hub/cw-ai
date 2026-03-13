package uk.ac.bris.cs.scotlandyard.ui.ai;

import uk.ac.bris.cs.scotlandyard.model.Board;
import uk.ac.bris.cs.scotlandyard.model.Move;
import javax.annotation.Nonnull;
import java.util.Set;

public class MiniMax implements MiniMaxInterface {

    private final MrXScore score;
    private final DistanceCalculator calc;

    public MiniMax(MrXScore score, DistanceCalculator calc) {
        this.score = score;
        this.calc = calc;
    }

    @Override
    public Integer Minimax(@Nonnull Board board, Integer depth, Integer alpha, Integer beta, boolean isMrx) {
        if (depth == 0 || !board.getWinner().isEmpty()) {
            return score.score(board, calc);
        }

        Board.GameState currentState = (Board.GameState) board;
        Set<Move> availableMoves = board.getAvailableMoves();

        if (isMrx) {
            int maxScore = Integer.MIN_VALUE;
            for (Move move : availableMoves) {
                Board.GameState nextState = currentState.advance(move);
                int childScore = Minimax(nextState, depth - 1, alpha, beta, false);
                maxScore = Math.max(maxScore, childScore);
                alpha = Math.max(alpha, childScore);
                if (beta <= alpha) {
                    break;
                }
            }
            return maxScore;
        } else {
            int minScore = Integer.MAX_VALUE;
            for (Move move : availableMoves) {
                Board.GameState nextState = currentState.advance(move);
                int childScore = Minimax(nextState, depth - 1, alpha, beta, true);
                minScore = Math.min(minScore, childScore);
                beta = Math.min(beta, childScore);
                if (beta <= alpha) {
                    break;
                }
            }
            return minScore;
        }
    }
}
