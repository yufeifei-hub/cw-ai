package uk.ac.bris.cs.scotlandyard.ui.ai;

import com.google.common.collect.ImmutableSet;
import com.google.common.graph.ImmutableValueGraph;
import uk.ac.bris.cs.scotlandyard.model.ScotlandYard;

import java.util.Optional;
import java.util.Set;

public interface DistanceCalculator {
    public void Dijkstras(Integer source, ImmutableValueGraph<Integer, ImmutableSet<ScotlandYard.Transport>> graph);
    public Integer getdistance(Integer source, Integer destination);
    public Set<ScotlandYard.Transport> getAvailableTransportModes(int location);
}
