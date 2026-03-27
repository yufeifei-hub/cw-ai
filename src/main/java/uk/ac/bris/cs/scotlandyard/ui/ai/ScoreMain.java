package uk.ac.bris.cs.scotlandyard.ui.ai;

import com.google.common.collect.ImmutableList;
import uk.ac.bris.cs.scotlandyard.model.*;

import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toList;

public class ScoreMain implements ScoreInterface {
    
    public Integer score(Player MrX, List<Player> detectives,ImmutableList<LogEntry> travelog,DistanceMainCalculator distancematrix){
        System.out.println("getting score");
//        final int MrxLocation = MrX.location();
//        final List<Integer> detectiveLocations = detectives.stream().map(detl-> detl.location()).toList();
        final int MrxLocation = MrX.location();
        final List<Integer> detectiveLocations = detectives.stream().map(p->p.location()).toList();
        long MrxNextPosRoutes = distancematrix.graphAcesss.adjacentNodes(MrxLocation).size();
        //TODO: sum of distance to detectives, see if he is surrounded


        if (detectiveLocations.isEmpty()){return 0;} // check if no locations found due to some other error

        int sumOfDistance = 0;
        for (int location:detectiveLocations){ // calcs distance of each detective with respect to mrx's current location
            sumOfDistance += distancematrix.getdistance(MrxLocation,location);
        }
        int avarageDistance = sumOfDistance/detectiveLocations.size();

        ImmutableList<LogEntry> log = travelog;
        int currentMove = log.size() + 1;
        boolean isRvealRound = ScotlandYard.REVEAL_MOVES.contains(currentMove);
        int totalScore = 0;

        if (avarageDistance < 2 && isRvealRound){
            totalScore += -100; // he is likely to be surrounded as the avarage suggest that many are around 2 nodes away.
            //high penalty
        } else if (avarageDistance > 2 && isRvealRound) {
            totalScore += avarageDistance * -1;
        } else if (avarageDistance > 2 && !isRvealRound) {
            totalScore += avarageDistance * 4;
        } else {
            totalScore += -2;
        }


        //TODO: nearest detective distance
        //TODO: available exits, where detectives not blocking

        long count = detectiveLocations.stream()
                .filter(x -> distancematrix.getdistance(MrxLocation, x) < 2)
                .count();
        if(count == MrxNextPosRoutes){
            totalScore += -100;
        } else if (count < MrxNextPosRoutes && isRvealRound) {
            totalScore += (MrxNextPosRoutes * 2);
        } else if (count < MrxNextPosRoutes && !isRvealRound) {
            totalScore += (MrxNextPosRoutes * 3);
        }

        //changed scalar from 20 to 5
        int ticketBonus = 0;
        ticketBonus += MrX.tickets().getOrDefault(ScotlandYard.Ticket.SECRET, 0) * 2;
        ticketBonus += MrX.tickets().getOrDefault(ScotlandYard.Ticket.DOUBLE, 0) * 2;
        ticketBonus += MrX.tickets().getOrDefault(ScotlandYard.Ticket.UNDERGROUND, 0) * 2;
        totalScore += ticketBonus;


        int roundBonus = currentMove * 5;
        totalScore += roundBonus;


        int minDistance = detectiveLocations.stream()
                .mapToInt(x -> distancematrix.getdistance(MrxLocation, x))
                .min()
                .getAsInt();

        System.out.println("min: " + minDistance);
        if (minDistance < 3) {
            totalScore -= (3 - minDistance) * 3;
        }

        //TODO: has underground exit
        //TODO: secret ticked remaining
        //TODO: double moves remaining
        //TODO: is reveal round
        //TODO:check if detective can even make move to possible positions
        //TODO:availible number of transportaion for mrx


        // one move to a detective no way!!!!
        // take into account the num of tickekts the detectives has
        //
        return totalScore;
    };
}
