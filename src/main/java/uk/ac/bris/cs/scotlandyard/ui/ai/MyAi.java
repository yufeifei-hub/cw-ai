package uk.ac.bris.cs.scotlandyard.ui.ai;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import jakarta.annotation.Nonnull;
import io.atlassian.fugue.Pair;
import uk.ac.bris.cs.scotlandyard.model.Ai;
import uk.ac.bris.cs.scotlandyard.model.Board;
import uk.ac.bris.cs.scotlandyard.model.Move;

public class MyAi implements Ai {

	private final MrXScore score = new MrXScore();

	@Nonnull @Override public String name() { return "Name me!"; }

	@Nonnull @Override public Move pickMove(@Nonnull Board board, Pair<Long, TimeUnit> timeoutPair) {

		final DistanceCalculator calc = null; //need to have the real class
		final MiniMax finalmove = new MiniMax(score, calc);

		Move bestmove = null;
		Integer maxscore = Integer.MIN_VALUE;
		final int DEPTH = 3;
		final int ALPHA = Integer.MIN_VALUE;
		final int BETA = Integer.MAX_VALUE;
		boolean isMrx = true;

		for (Move move : board.getAvailableMoves()) {
			Board.GameState currentState = (Board.GameState) board;
			Board nextState = currentState.advance(move);

			Integer score = finalmove.Minimax(board, DEPTH, ALPHA, BETA, false);

			if (score > maxscore) {
				maxscore = score;
				bestmove = move;
			}
		}

		if (bestmove == null) {
			bestmove = board.getAvailableMoves().iterator().next();
		}
		return bestmove;
	}
}
