package uk.ac.bris.cs.scotlandyard.ui.ai;


import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import uk.ac.bris.cs.scotlandyard.model.*;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Set;


public class MiniMax implements MiniMaxInterface {

    private final ScoreMain score;
    private final DistanceMainCalculator calc;

    public MiniMax(ScoreMain score, DistanceMainCalculator calc) {
        this.score = score;
        this.calc = calc;
    }

    @Override
    public Integer Minimax( NewGameState currentState, Integer depth, Integer alpha, Integer beta, boolean ismrx) {

        List<Player> detectives =currentState.detectives;

        Player mrX = currentState.mrX;
        GameSetup setup = currentState.setup;
        ImmutableList<LogEntry> travelLog = currentState.travelLog;



        if (depth == 0 || !currentState.winner().isEmpty()) {
            ImmutableSet<Piece> winners = currentState.winner();
            if (!winners.isEmpty()) {
                if (winners.contains(currentState.mrX.piece())) {
                    return Integer.MAX_VALUE / 2;
                } else {
                    return Integer.MIN_VALUE / 2;
                }
            }
            return score.score(mrX, detectives, currentState.travelLog, calc);
        }


        Set<Move> availableMoves = currentState.availableMoves();

        if (ismrx) {
            int maxScore = Integer.MIN_VALUE;
            for (Move move : availableMoves) {

                NewGameState nextstate = currentState.advanceState(move);
                int childScore = Minimax(nextstate,depth - 1, alpha, beta, false);
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
                NewGameState nextstate = currentState.advanceState(move);
                int childScore = Minimax(nextstate, depth - 1, alpha, beta, true);
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
