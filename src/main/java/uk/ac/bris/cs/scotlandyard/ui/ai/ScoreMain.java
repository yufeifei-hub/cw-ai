package uk.ac.bris.cs.scotlandyard.ui.ai;

import com.google.common.collect.ImmutableList;
import uk.ac.bris.cs.scotlandyard.model.Board;
import uk.ac.bris.cs.scotlandyard.model.GameSetup;
import uk.ac.bris.cs.scotlandyard.model.Piece;
import uk.ac.bris.cs.scotlandyard.model.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

public class ScoreMain implements ScoreInterface {
    int totalScore;
    public Integer score(Player MrX, ImmutableList<Player> detectives, Board.GameState state,DistanceMainCalculator distancematrix){

        final int MrxLocation = MrX.location();
        final List<Integer> detectiveLocations = detectives.stream().map(detl-> detl.location()).toList();
        final List<Integer> detdist= new ArrayList<>();
     ;


        for (int location:detectiveLocations){
            detdist.add(distancematrix.getdistance(MrxLocation,location));// calcs distance of each detective with respect to mrx's current location
        }

        //TODO: sum of distance to detectives, see if he is surrounded
        if (detectiveLocations.isEmpty()){return 0;} // check if no locations found due to some other error


        int sumOfDistance = detdist.stream().mapToInt(i -> i.intValue()).sum();
        int avarageDistance = sumOfDistance/detectiveLocations.size();

        if (avarageDistance < 2){
            totalScore += (avarageDistance * -100); // he is likely to be surrounded as the avarage suggest that many are around 2 nodes away.
            //high penalty
        }else{
            totalScore += 1;
        }

        //TODO: available exits, where detectives not blocking
        long count = detdist.stream()
                .filter(x -> x > 1)
                .count();

        if (count >1){
            totalScore+=(count * 20);
        }else{
            totalScore+=Integer.MIN_VALUE;
        }
        //TODO: nearest detective distance
        //TODO: has underground exit
        //TODO: secret ticked remaining
        //TODO: double moves remaining
        //TODO: is reveal round
        //TODO:check if detective can even make move to possible positions
        //TODO:availible number of transportaion for mrx


        return totalScore;



    };
}
