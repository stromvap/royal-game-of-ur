package se.stromvap.royal.game.of.ur.grpc;

import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.stromvap.royal.game.of.ur.GameEngine;
import se.stromvap.royal.game.of.ur.GameUtil;

public class UrService extends UrServiceGrpc.UrServiceImplBase {
    private static final Logger log = LoggerFactory.getLogger(UrService.class);

    private Lobby lobby = new Lobby();

    @Override
    public void move(GamePiece gamePiece, StreamObserver<Empty> responseObserver) {
        LobbyGame lobbyGame = lobby.getLobbyGame(GrpcGameMapper.map(gamePiece.getPlayer()));
        GameEngine gameEngine = lobbyGame.getGameEngine();
        gameEngine.move(gamePiece.getId());

        if (GameUtil.isAnyPlayerAWinner(gameEngine.getGame()) != null) {
            lobbyGame.getPlayers().values().stream().map(LobbyPlayer::getGameObserver).forEach(o -> o.onNext(GameMapper.map(gameEngine.getGame())));
            lobbyGame.getPlayers().values().stream().map(LobbyPlayer::getGameObserver).forEach(StreamObserver::onCompleted);
            responseObserver.onCompleted();
            return;
        }

        do {
            gameEngine.roll();
        } while (gameEngine.getStatus().hasMoved());

        lobbyGame.triggerTurn();
        responseObserver.onCompleted();
    }

    @Override
    public void join(Player request, StreamObserver<Game> responseObserver) {
        se.stromvap.royal.game.of.ur.model.Player player = GrpcGameMapper.map(request);
        LobbyGame lobbyGame = lobby.findLobbyGame(player, responseObserver);

        log.info("{} joined lobby", player);

        if (lobbyGame.isFull()) {
            log.info("Lobby full, starting game");
            new Thread(lobbyGame::startGame).start();
        }
    }
}
