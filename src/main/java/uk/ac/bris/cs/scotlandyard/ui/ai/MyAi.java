package uk.ac.bris.cs.scotlandyard.ui.ai;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Stream;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import io.atlassian.fugue.Option;
import jakarta.annotation.Nonnull;
import io.atlassian.fugue.Pair;
import uk.ac.bris.cs.scotlandyard.model.*;

public class MyAi implements Ai {

	private final ScoreMain score = new ScoreMain();

	@Nonnull @Override public String name() { return "Name me!"; }

	@Nonnull @Override public Move pickMove(@Nonnull Board board, Pair<Long, TimeUnit> timeoutPair) {
//---------------------------------------------------------------------------------------------------------------------------------

		ImmutableMap<Piece, ImmutableMap<ScotlandYard.Ticket, Integer>> tickets = Objects.requireNonNull(
				board.getPlayers().stream().collect(ImmutableMap.toImmutableMap(
						Function.identity(), x -> {
							Board.TicketBoard b = board.getPlayerTickets(x).orElseThrow();
							return Stream.of(ScotlandYard.Ticket.values()).collect(ImmutableMap.toImmutableMap(
									Function.identity(), b::getCount));
						})));

		ImmutableMap<Piece.Detective, Integer> detectiveLocations = Objects.requireNonNull(board.getPlayers().stream()
				.filter(Piece::isDetective)
				.map(Piece.Detective.class::cast)
				.collect(ImmutableMap.toImmutableMap(Function.identity(),
						x1 -> board.getDetectiveLocation(x1).orElseThrow())));
		ImmutableSet<Move> movesToLoop=board.getAvailableMoves();
		List<Player> DetectivePlayer = detectiveLocations.keySet().stream().map(d->new Player(d,tickets.get(d),detectiveLocations.get(d))).toList();
//		System.out.println("hola"+DetectivePlayer.size());
		int MrXLocation;
		if(board.getMrXTravelLog().isEmpty()){
			Move first_move = movesToLoop.iterator().next();
			MrXLocation=first_move.source();

		}else{
			Optional<Integer> MrXLocationtoUpdate =
					NewGameState.revealedtravelLog.get(NewGameState.revealedtravelLog.size() - 1).location();
			MrXLocation = MrXLocationtoUpdate.get();
			System.out.println("Mrxlocation: "+MrXLocation);
		}


		Player mrX = board.getPlayers().stream()
				.filter(Piece::isMrX)
				.map(p -> new Player(p, tickets.get(p), MrXLocation))
				.findAny()
				.orElseThrow();


		ImmutableList<LogEntry> travalLog = board.getMrXTravelLog();
		GameSetup setup = board.getSetup();
;//---------------------------------------------------------------------------------------------------------------------------------
		final DistanceMainCalculator calc = new DistanceMainCalculator(board.getSetup()); //need to have the real class
		final MiniMax finalmove = new MiniMax(score, calc);
		/// update current move



		Move bestmove = null;
		Integer maxscore = Integer.MIN_VALUE;
		final int DEPTH =setup.moves.size()-NewGameState.roundNum>= 3?3:setup.moves.size()-NewGameState.roundNum;
		final int ALPHA = Integer.MIN_VALUE;
		final int BETA = Integer.MAX_VALUE;


		for (Move move :movesToLoop) {

			NewGameState currentState = new NewGameState(setup,DetectivePlayer,mrX,travalLog,movesToLoop,true,ImmutableSet.of(),NewGameState.roundNum,ImmutableSet.of());
			NewGameState nextState = currentState.advanceState(move);

			Integer score = finalmove.Minimax(nextState, DEPTH, ALPHA, BETA, false);
			for (Integer i:nextState.detectiveLocations){
				if(calc.getdistance(nextState.mrX.location(),i)<=1){

					score+=Integer.MIN_VALUE/10;
					break;
				}
			}
			if (score > maxscore) {
				maxscore = score;
				bestmove = move;
			}
		}
		//need to check if this null check is still valid and only happens if all moves wehre equally horrible or is there some other edge case that causes it as well
		if (bestmove == null) {
			bestmove = board.getAvailableMoves().iterator().next();
			System.out.print("Hello ");
		}
		System.out.println(maxscore+"roundNum: "+NewGameState.roundNum);
		NewGameState.UpdateRevealedTravelLog(bestmove);
		if (bestmove instanceof Move.DoubleMove){
			NewGameState.roundNum+=2;
		}else{
			NewGameState.roundNum+=1;
		}
	 return bestmove;
	}
}
