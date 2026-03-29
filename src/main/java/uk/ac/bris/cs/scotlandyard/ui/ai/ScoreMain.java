package uk.ac.bris.cs.scotlandyard.ui.ai;

import com.google.common.collect.ImmutableList;
import uk.ac.bris.cs.scotlandyard.model.*;
import java.util.List;
import java.util.Set;

public class ScoreMain implements ScoreInterface {

    @Override
    public Integer score(Player mrX, List<Player> detectives, ImmutableList<LogEntry> travelLog, DistanceMainCalculator distanceMatrix) {
        int MrxLocation = mrX.location();
        List<Integer> detectiveLocations = detectives.stream().map(Player::location).toList();
        int currentRound = travelLog.size() + 1;
        boolean isRevealRound = ScotlandYard.REVEAL_MOVES.contains(currentRound);
        boolean isNextReveal = ScotlandYard.REVEAL_MOVES.contains(currentRound + 1);
        int sumOfDistance = 0;
        for (int location:detectiveLocations){ // calcs distance of each detective with respect to mrx's current location
            sumOfDistance += distanceMatrix.getdistance(MrxLocation,location);
        }
        int avarageDistance = sumOfDistance/detectiveLocations.size();
        int edges = distanceMatrix.graphAcesss.adjacentNodes(MrxLocation).size();

        int secretTickets = mrX.tickets().getOrDefault(ScotlandYard.Ticket.SECRET, 0);
        int doubleTickets = mrX.tickets().getOrDefault(ScotlandYard.Ticket.DOUBLE, 0);
        int undergroundTickets = mrX.tickets().getOrDefault(ScotlandYard.Ticket.UNDERGROUND, 0);

        int totalScore = 0;


        int minDistance = detectiveLocations.stream()
                .mapToInt(loc -> distanceMatrix.getdistance(MrxLocation, loc))
                .min().orElse(10);


        if (minDistance <= 1) totalScore -= 50000;
        else if (minDistance == 2) totalScore -= 10000;
        else if (minDistance == 3) totalScore -= 1000;
        else totalScore += (minDistance * 100);


        totalScore += (avarageDistance * 50);

        totalScore += (edges * 200);


        if (isRevealRound && minDistance < 4) {
            totalScore -= 5000;
        }
        if (isNextReveal) {
            totalScore += 1000;
        }


        totalScore += (secretTickets * 300);
        totalScore += (doubleTickets * 500);
        totalScore += (undergroundTickets * 600);



        return totalScore;
    }
}