package se.stromvap.royal.game.of.ur.grpc;

import io.grpc.stub.StreamObserver;
import se.stromvap.royal.game.of.ur.model.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Lobby {
    private List<LobbyGame> games = new ArrayList<>();

    public LobbyGame getLobbyGame(Player player) {
        Optional<LobbyGame> thisPlayersGame = games.stream().filter(g -> g.getPlayers().keySet().contains(player.getId())).findFirst();
        if (thisPlayersGame.isPresent()) {
            return thisPlayersGame.get();
        }

        throw new IllegalStateException("Should not be here");
    }

    public LobbyGame findLobbyGame(Player player, StreamObserver<Game> responseObserver) {
        Optional<LobbyGame> firstAvilableGame = games.stream().filter(g -> !g.isFull()).findFirst();
        if (firstAvilableGame.isPresent()) {
            LobbyGame game = firstAvilableGame.get();
            game.addPlayer(player, responseObserver);
            return game;
        }

        return createLobbyGame(player, responseObserver);
    }

    private LobbyGame createLobbyGame(Player player, StreamObserver<Game> responseObserver) {
        LobbyGame lobbyGame = new LobbyGame();
        lobbyGame.addPlayer(player, responseObserver);
        games.add(lobbyGame);
        return lobbyGame;
    }
}
