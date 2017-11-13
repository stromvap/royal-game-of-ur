package se.stromvap.royal.game.of.ur.grpc;

import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.stromvap.royal.game.of.ur.GameEngine;
import se.stromvap.royal.game.of.ur.model.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class LobbyGame {
    private static final Logger log = LoggerFactory.getLogger(LobbyGame.class);

    private GameEngine gameEngine = new GameEngine();
    private Map<String, LobbyPlayer> players = new HashMap<>();

    public GameEngine getGameEngine() {
        return gameEngine;
    }

    public boolean isFull() {
        return players.size() == 2;
    }

    public void addPlayer(Player player, StreamObserver<Game> responseObserver) {
        String playerId = UUID.randomUUID().toString();
        player.setId(playerId);
        LobbyPlayer lobbyPlayer = new LobbyPlayer(player);
        lobbyPlayer.setGameObserver(responseObserver);
        players.put(playerId, lobbyPlayer);
    }

    public void startGame() {
        for (LobbyPlayer lobbyPlayer : players.values()) {
            if (lobbyPlayer.getGameObserver() == null) {
                throw new IllegalStateException(lobbyPlayer.getPlayer().toString() + " did not show up for game in time...");
            }
        }

        List<Player> players = this.players.values().stream().map(LobbyPlayer::getPlayer).collect(Collectors.toList());
        gameEngine.newGame(players.get(0), players.get(1));

        do {
            gameEngine.roll();
        }
        while (gameEngine.getStatus().hasMoved());

        triggerTurn();
    }

    public void triggerTurn() {
        LobbyPlayer currentPlayer = players.get(gameEngine.getCurrentTurnPlayer().getId());
        currentPlayer.getGameObserver().onNext(GameMapper.map(gameEngine.getGame()));
    }

    public Map<String, LobbyPlayer> getPlayers() {
        return players;
    }
}
