package uk.ac.bris.cs.scotlandyard.ui.ai;

import com.google.common.collect.ImmutableList;
import uk.ac.bris.cs.scotlandyard.model.*;

import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toList;

public class ScoreMain implements ScoreInterface {
    
    public Integer score(Player MrX, List<Player> detectives,ImmutableList<LogEntry> travelog,DistanceMainCalculator distancematrix){
        
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
            totalScore += Integer.MIN_VALUE / 2; // he is likely to be surrounded as the avarage suggest that many are around 2 nodes away.
            //high penalty
        } else if (avarageDistance > 2 && isRvealRound) {
            totalScore += avarageDistance * -100;
        } else if (avarageDistance > 2 && !isRvealRound) {
            totalScore += avarageDistance * 10;
        } else {
            totalScore += -200;
        }

        //TODO: nearest detective distance
        //TODO: available exits, where detectives not blocking

        long count = detectiveLocations.stream()
                .filter(x -> distancematrix.getdistance(MrxLocation, x) < 2)
                .count();
        if(count == MrxNextPosRoutes){
            totalScore += Integer.MIN_VALUE;
        } else if (count < MrxNextPosRoutes && isRvealRound) {
            totalScore += (MrxNextPosRoutes * 10);
        } else if (count < MrxNextPosRoutes && !isRvealRound) {
            totalScore += (MrxNextPosRoutes * 20);
        }

        //TODO: has underground exit
        //TODO: secret ticked remaining
        //TODO: double moves remaining
        //TODO: is reveal round
        //TODO:check if detective can even make move to possible positions
        //TODO:availible number of transportaion for mrx



        return totalScore;
    };
}
