package uk.ac.bris.cs.scotlandyard.ui.ai;

import com.google.common.collect.ImmutableList;
import uk.ac.bris.cs.scotlandyard.model.Board;
import uk.ac.bris.cs.scotlandyard.model.GameSetup;
import uk.ac.bris.cs.scotlandyard.model.Player;

public interface ScoreInterface {
    Integer score(Player MrX, ImmutableList<Player> detectives,  Board.GameState state,DistanceMainCalculator distancematrix);
}
