package uk.ac.bris.cs.scotlandyard.ui.ai;


import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import uk.ac.bris.cs.scotlandyard.model.*;

import javax.annotation.Nonnull;
import java.awt.*;
import java.util.List;
import java.util.Set;


public class MiniMax implements MiniMaxInterface {

    private final ScoreMain score;
    private final DistanceMainCalculator calc;

    public MiniMax(ScoreMain score, DistanceMainCalculator calc) {
        this.score = score;
        this.calc = calc;
    }

    @Override
    public Integer Minimax( NewGameState currentState, Integer depth, Integer alpha, Integer beta, boolean ismrx) {
        System.out.println(ismrx);
        List<Player> detectives =currentState.detectives;

        Player mrX = currentState.mrX;
        GameSetup setup = currentState.setup;
        ImmutableList<LogEntry> travelLog = currentState.travelLog;


        //logic to consider who is playing
        //mrx max
        //detectives min

        //look at detectives all of them
//        if (depth == 0 || !currentState.winner().isEmpty()) {
//            ImmutableSet<Piece> winners = currentState.winner();
//            if (!winners.isEmpty()) {
//
//                if (winners.contains(currentState.mrX.piece())) {
//
//                    return 100;
//                } else {
//                    return 100;
//                }
//            }
//            return score.score(mrX, detectives, currentState.travelLog, calc);
//        }

        if (depth == 0) {


            return score.score(mrX, detectives, currentState.travelLog, calc);
        }

        Set<Move> availableMoves = currentState.availableMoves();
        System.out.println(availableMoves);

        if (ismrx) {
            int maxScore = Integer.MIN_VALUE;
            for (Move move : availableMoves) {
                System.out.println("inside mrx");
                NewGameState nextstate = currentState.advanceState(move);
                int childScore = Minimax(nextstate,depth - 1, alpha, beta, false);

                //will need to first see if the particular detective has  the tickets to make the journey!!! need to fix
//                for (Integer i:nextstate.detectiveLocations){ //WHY CHECKING DETECTIVE LOCATION HERE
//                    if(calc.getdistance(nextstate.mrX.location(),i)<=1){
//                        childScore+=Integer.MIN_VALUE/15;
//                        break;
//                    }
//                }
                //is alpha beta pruning actually working here shouldn't it be if beta<=alpha first then make updates to maxscore, i think the order of the following 3 statements need to be changed
                maxScore = Math.max(maxScore, childScore);

                alpha = Math.max(alpha, childScore);
                if (beta <= alpha) {
                    break;
                }
                //currentState.moves=null; // WHY ADD THIS
            }
            return maxScore;
        } else {
            System.out.println("inside detective");
            int minScore = Integer.MAX_VALUE;
            for (Move move : availableMoves) {
                NewGameState nextstate = currentState.advanceState(move);
                int childScore;
//                if (nextstate.played.isEmpty()){ // WHAT IS THE PURPOSE OF THIS
                     childScore = Minimax(nextstate, depth - 1, alpha, beta, true);
                System.out.println("this is child score"+childScore);
//                }else{
//                     childScore = Minimax(nextstate, depth - 1, alpha, beta, false);
//                }

                //currentState.moves=null;// WHY
                minScore = Math.min(minScore, childScore);
                beta = Math.min(beta, childScore);
                if (beta <= alpha) {
                    break;
                }
            }
            return minScore;
        }
    }
}
