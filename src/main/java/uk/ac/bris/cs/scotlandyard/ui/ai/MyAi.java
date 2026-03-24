package uk.ac.bris.cs.scotlandyard.ui.ai;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Stream;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
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

		List<Player> DetectivePlayer = detectiveLocations.keySet().stream().map(d->new Player(d,tickets.get(d),detectiveLocations.get(d))).toList();

		Player mrX = board.getPlayers().stream()
				.filter(Piece::isMrX)
				.map(p -> new Player(p, tickets.get(p),42))
				.findAny()
				.orElseThrow();

		ImmutableList<LogEntry> travalLog = board.getMrXTravelLog();
		GameSetup setup = board.getSetup();
;//---------------------------------------------------------------------------------------------------------------------------------
		final DistanceMainCalculator calc = new DistanceMainCalculator(board.getSetup()); //need to have the real class
		final MiniMax finalmove = new MiniMax(score, calc);



		Move bestmove = null;
		Integer maxscore = Integer.MIN_VALUE;
		final int DEPTH = 3;
		final int ALPHA = Integer.MIN_VALUE;
		final int BETA = Integer.MAX_VALUE;


		for (Move move : board.getAvailableMoves()) {
			NewGameState currentState = new NewGameState(setup,DetectivePlayer,mrX,travalLog,board.getAvailableMoves(),true,board.getPlayers());
			NewGameState nextState = currentState.advanceState(move);

			Integer score = finalmove.Minimax(nextState, DEPTH, ALPHA, BETA, true);

			if (score > maxscore) {
				maxscore = score;
				bestmove = move;
			}
		}

		if (bestmove == null) {
			bestmove = board.getAvailableMoves().iterator().next();
		}
	 return bestmove;
	}
}
