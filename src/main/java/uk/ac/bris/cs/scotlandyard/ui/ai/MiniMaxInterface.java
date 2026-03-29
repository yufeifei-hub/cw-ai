package uk.ac.bris.cs.scotlandyard.ui.ai;

import com.google.common.collect.ImmutableList;
import uk.ac.bris.cs.scotlandyard.model.*;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Set;

public interface MiniMaxInterface {
    Integer Minimax(NewGameState currentState, Integer depth, Integer alpha, Integer beta, boolean isMrx);


}
