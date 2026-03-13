package uk.ac.bris.cs.scotlandyard.ui.ai;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import jakarta.annotation.Nonnull;
import io.atlassian.fugue.Pair;
import uk.ac.bris.cs.scotlandyard.model.Ai;
import uk.ac.bris.cs.scotlandyard.model.Board;
import uk.ac.bris.cs.scotlandyard.model.Move;
import uk.ac.bris.cs.scotlandyard.model.Player;

public class MyAi implements Ai {
	private final MiniMax finalmove = new MiniMax();
	//Q why cant make MiniMax a static class and call without need to instantiate
	@Nonnull @Override public String name() { return "Name me!"; }

	@Nonnull @Override public Move pickMove(@Nonnull Board board, Pair<Long, TimeUnit> timeoutPair) {
		// returns a random move, replace with your own implementation
		Move bestmove = null;
		Integer maxscore = Integer.MAX_VALUE;
		final int DEPTH = 3;
		final int ALPHA = Integer.MIN_VALUE;
		final int BETA = Integer.MAX_VALUE;
		Boolean isMrx = true;
		//var moves = board.getAvailableMoves().asList();
		//loop through each move to find out which one is the best
		for (Move move : board.getAvailableMoves()) { // TODO add timeoutPair check when calling minimax
			Integer score = finalmove.Minimax(board, DEPTH, ALPHA, BETA, isMrx);
			if (score >= maxscore) {
				maxscore = score;
				bestmove = move;
			}
		}
		return bestmove;
	}
}
