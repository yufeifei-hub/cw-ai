package uk.ac.bris.cs.scotlandyard.ui.ai;

import java.util.Optional;

public interface DistanceCalculator {
    public void Dijkstras(Optional<Integer> souce, Optional<Integer> destination);
    Integer getdistance(Optional<Integer> source, Optional<Integer> destination);
}
