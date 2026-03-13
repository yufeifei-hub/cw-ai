package uk.ac.bris.cs.scotlandyard.ui.ai;

import com.google.common.collect.ImmutableList;
import uk.ac.bris.cs.scotlandyard.model.*;

import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toList;

public class ScoreMain implements ScoreInterface {
    int totalScore;
    public Integer score(Piece MrX, ImmutableList<Piece> detectives, Board.GameState state,DistanceMainCalculator distancematrix){

//        final int MrxLocation = MrX.location();
//        final List<Integer> detectiveLocations = detectives.stream().map(detl-> detl.location()).toList();
        final int MrxLocation = state.getDetectiveLocation((Piece.Detective) MrX).orElse(0);
        final List<Integer> detectiveLocations = detectives.stream()
                .map(det -> state.getDetectiveLocation((Piece.Detective) det).orElse(0))
                .toList();
        long MrxNextPosRoutes = (distancematrix.adjNodesMrx).stream().count();
        //TODO: sum of distance to detectives, see if he is surrounded


        if (detectiveLocations.isEmpty()){return 0;} // check if no locations found due to some other error

        int sumOfDistance = 0;
        for (int location:detectiveLocations){ // calcs distance of each detective with respect to mrx's current location
            sumOfDistance += distancematrix.getdistance(MrxLocation,location);
        }
        int avarageDistance = sumOfDistance/detectiveLocations.size();

        if (avarageDistance < 2){
            totalScore += (avarageDistance * -100); // he is likely to be surrounded as the avarage suggest that many are around 2 nodes away.
            //high penalty
        }else{
            totalScore += 1;
        }

        //TODO: nearest detective distance
        //TODO: available exits, where detectives not blocking

        int count = (int) detectiveLocations.stream()
                .filter(x -> x < 2)
                .count();
        if(count == MrxNextPosRoutes){
            totalScore+=Integer.MIN_VALUE;
        }else if (count <MrxNextPosRoutes){
            totalScore+=(MrxNextPosRoutes*10);
        }


        ImmutableList<LogEntry> log = state.getMrXTravelLog();
        int currentMove = log.size() + 1;
        boolean isRvealRound = ScotlandYard.REVEAL_MOVES.contains(currentMove);
        int minDistance = detectiveLocations.stream()
                .mapToInt(loc -> distancematrix.getdistance(MrxLocation, loc))
                .min()
                .orElse(999);
        if (isRvealRound) {
            if (minDistance <= 2) {
                totalScore -= 300;
            }
        } else {
            if (minDistance <= 1) {
                totalScore -= 100;
            }
        }
        //TODO: has underground exit
        //TODO: secret ticked remaining
        //TODO: double moves remaining
        //TODO: is reveal round
        //TODO:check if detective can even make move to possible positions
        //TODO:availible number of transportaion for mrx
§1


        return totalScore;
    };
}
