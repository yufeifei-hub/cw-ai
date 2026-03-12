package uk.ac.bris.cs.scotlandyard.ui.ai;

import uk.ac.bris.cs.scotlandyard.model.Board;
import uk.ac.bris.cs.scotlandyard.model.GameSetup;

import javax.annotation.Nonnull;

public interface MiniMaxInterface {
    Integer Minimax(@Nonnull Board board, Integer depth, Integer alpha, Integer beta, boolean isMrx);
}
