package se.stromvap.royal.game.of.ur.ai;

import org.apache.commons.lang3.SerializationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.stromvap.royal.game.of.ur.GameEngine;
import se.stromvap.royal.game.of.ur.ai.ais.RandomAi;
import se.stromvap.royal.game.of.ur.ai.ais.SimpleAi;
import se.stromvap.royal.game.of.ur.model.Player;

import java.util.HashMap;
import java.util.Map;

public class AiArena {
    private static final Logger log = LoggerFactory.getLogger(AiArena.class);

    public static void main(String[] args) {
        Battle battle = new Battle(1000, new SimpleAi(), new RandomAi());
        battle(battle);
    }

    private static void battle(Battle battle) {
        Map<Player, Ai> players = new HashMap<>();

        Player player1 = new Player("Player 1 - " + battle.getPlayer1().getClass().getSimpleName());
        Player player2 = new Player("Player 2 - " + battle.getPlayer2().getClass().getSimpleName());

        players.put(player1, battle.getPlayer1());
        players.put(player2, battle.getPlayer2());

        for (int i = 0; i < battle.getNumberOfRounds(); i++) {
            GameEngine gameEngine = new GameEngine();
            gameEngine.newGame(player1, player2);

            Player winner;
            while ((winner = gameEngine.isAnyPlayerAWinner()) == null) {
                gameEngine.roll();

                if (gameEngine.getStatus().hasMoved()) {
                    continue;
                }

                int gamePieceToMove = players.get(gameEngine.getCurrentTurnPlayer()).myTurn(SerializationUtils.clone(gameEngine.getGame()));
                gameEngine.move(gamePieceToMove);
            }

            battle.reportWin(players.get(winner));
        }

        battle.getResult().forEach((key, value) -> log.info(key.getClass().getSimpleName() + " won " + value + " times out of " + battle.getNumberOfRounds() + " (" + ((value.doubleValue() / battle.getNumberOfRounds()) * 100d) + "% win rate)"));
    }
}
