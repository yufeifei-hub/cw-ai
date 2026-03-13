package uk.ac.bris.cs.scotlandyard.ui.ai;

import com.google.common.collect.ImmutableList;
import uk.ac.bris.cs.scotlandyard.model.Board;
import uk.ac.bris.cs.scotlandyard.model.Move;
import uk.ac.bris.cs.scotlandyard.model.Piece;
import uk.ac.bris.cs.scotlandyard.model.Player;

import javax.annotation.Nonnull;
import java.util.Set;


public class MiniMax implements MiniMaxInterface {

    private final ScoreMain score;
    private final DistanceMainCalculator calc;

    public MiniMax(ScoreMain score, DistanceMainCalculator calc) {
        this.score = score;
        this.calc = calc;
    }

    @Override
    public Integer Minimax(@Nonnull Board board, Integer depth, Integer alpha, Integer beta, boolean ismrx) {
        Piece Mrx = board.getPlayers().stream()
                .filter(Piece::isMrX)
                .findFirst()
                .orElse(null);
        ImmutableList<Piece> detectives = ImmutableList.copyOf(
                board.getPlayers().stream()
                .filter(Piece::isDetective)
                .toList()
        );

        Board.GameState state = (Board.GameState) board;

        if (depth == 0 || !board.getWinner().isEmpty()) {
            return score.score(Mrx, detectives, state, calc);
        }

        Board.GameState currentState = (Board.GameState) board;
        Set<Move> availableMoves = board.getAvailableMoves();

        if (ismrx) {
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
