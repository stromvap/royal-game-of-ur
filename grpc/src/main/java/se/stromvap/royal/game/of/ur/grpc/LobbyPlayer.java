package se.stromvap.royal.game.of.ur.grpc;

import io.grpc.stub.StreamObserver;
import se.stromvap.royal.game.of.ur.model.Player;

public class LobbyPlayer {
    private se.stromvap.royal.game.of.ur.model.Player player;
    private StreamObserver<Game> gameObserver;

    public LobbyPlayer(Player player) {
        this.player = player;
    }

    public se.stromvap.royal.game.of.ur.model.Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public StreamObserver<Game> getGameObserver() {
        return gameObserver;
    }

    public void setGameObserver(StreamObserver<Game> gameObserver) {
        this.gameObserver = gameObserver;
    }
}
