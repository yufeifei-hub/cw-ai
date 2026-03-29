package uk.ac.bris.cs.scotlandyard.ui.ai;

import com.google.common.collect.ImmutableList;
import uk.ac.bris.cs.scotlandyard.model.*;

import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toList;

public class ScoreMain implements ScoreInterface {

    public Integer score(Player MrX, List<Player> detectives, ImmutableList<LogEntry> travelog, DistanceMainCalculator distancematrix) {
        final int MrxLocation = MrX.location();
        final List<Integer> detectiveLocations = detectives.stream().map(Player::location).toList();


        int totalScore = 0;


        int minDistance = detectiveLocations.stream()
                .mapToInt(loc -> distancematrix.getdistance(MrxLocation, loc))
                .min().orElse(10);


        if (minDistance <= 1){
            totalScore -= 5000;
        }

        else if (minDistance == 2) {
            totalScore -= 500;
        }
        else {
            totalScore += (minDistance * 20);
        }


        Set<ScotlandYard.Transport> modes = distancematrix.getAvailableTransportModes(MrxLocation);
        totalScore += (modes.size() * 50);


        int currentMove = travelog.size() + 1;
        boolean isRevealRound = ScotlandYard.REVEAL_MOVES.contains(currentMove);

        if (isRevealRound) {

            if (minDistance < 3) totalScore -= 2000;
        }


        int ticketBonus = 0;
        ticketBonus += MrX.tickets().getOrDefault(ScotlandYard.Ticket.SECRET, 0) * 40;
        ticketBonus += MrX.tickets().getOrDefault(ScotlandYard.Ticket.DOUBLE, 0) * 60;
        ticketBonus += MrX.tickets().getOrDefault(ScotlandYard.Ticket.UNDERGROUND, 0) * 10;
        totalScore += ticketBonus;


        int sumOfDistance = 0;
        for (int location:detectiveLocations){ // calcs distance of each detective with respect to mrx's current location
            sumOfDistance += distancematrix.getdistance(MrxLocation,location);
        }
        int avarageDistance = sumOfDistance/detectiveLocations.size();
        totalScore += (int)(avarageDistance * 15);


        return totalScore;
    }
}
