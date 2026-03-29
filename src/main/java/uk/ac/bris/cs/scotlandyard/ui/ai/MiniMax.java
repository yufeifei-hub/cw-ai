package uk.ac.bris.cs.scotlandyard.ui.ai;


import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import uk.ac.bris.cs.scotlandyard.model.*;

import javax.annotation.Nonnull;
import java.util.ArrayList;
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

        List<Player> detectives =currentState.detectives;

        Player mrX = currentState.mrX;
        GameSetup setup = currentState.setup;
        ImmutableList<LogEntry> travelLog = currentState.travelLog;


        //logic to consider who is playing
        //mrx max
        //detectives min

        //look at detectives all of them
        if (depth == 0 || !currentState.winner().isEmpty()) {
            ImmutableSet<Piece> winners = currentState.winner();
            if (!winners.isEmpty()) {
                System.out.println("yes");
                if (winners.contains(currentState.mrX.piece())) {
                    System.out.println("yes");
                    return Integer.MAX_VALUE / 2;
                } else {
                    return Integer.MIN_VALUE / 2;
                }
            }

            return score.score(mrX, detectives, currentState.travelLog, calc);
        }



        //List<Move> availableMoves = sortMoves(currentState.availableMoves(), currentState);
        Set<Move> availableMoves = currentState.availableMoves();
//        System.out.println("depth"+availableMoves.size());
        if (ismrx) {
            int maxScore = Integer.MIN_VALUE;
            for (Move move : availableMoves) {

                NewGameState nextstate = currentState.advanceState(move);
                int childScore = Minimax(nextstate,depth - 1, alpha, beta, false);
                //will need to first see if the particular detective has  the tickets to make the journey!!! need to fix
                for (Integer i:nextstate.detectiveLocations){
                    if(calc.getdistance(nextstate.mrX.location(),i)<=1){
                        childScore+=Integer.MIN_VALUE/15;
                        break;
                    }
                }
                //is alpha beta pruning actually working here shouldn't it be if beta<=alpha first then make updates to maxscore, i think the order of the following 3 statements need to be changed
                maxScore = Math.max(maxScore, childScore);
                alpha = Math.max(alpha, childScore);
                if (beta <= alpha) {
                    break;
                }
                currentState.moves=null;
            }
            return maxScore;
        } else {
            int minScore = Integer.MAX_VALUE;
            for (Move move : availableMoves) {
                NewGameState nextstate = currentState.advanceState(move);
                int childScore;
                if (nextstate.mrxTurn){
                     childScore = Minimax(nextstate, depth - 1, alpha, beta, true);
                }else{
                     childScore = Minimax(nextstate, depth - 1, alpha, beta, false);
                }

                currentState.moves=null;
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
