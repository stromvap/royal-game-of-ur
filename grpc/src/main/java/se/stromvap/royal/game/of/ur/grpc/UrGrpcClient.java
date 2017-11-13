package se.stromvap.royal.game.of.ur.grpc;

import com.google.protobuf.Empty;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.stromvap.royal.game.of.ur.GameUtil;
import se.stromvap.royal.game.of.ur.model.GamePiece;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class UrGrpcClient {
    private static final Logger log = LoggerFactory.getLogger(UrGrpcClient.class);

    public static void main(String[] args) {
        UrGrpcClient.playRoyalGameOfUr("localhost", 8081, new RemoteAi() {
            @Override
            public String getName() {
                return "Patrik";
            }

            @Override
            public GamePiece yourTurn(se.stromvap.royal.game.of.ur.model.Game game) {
                List<se.stromvap.royal.game.of.ur.model.GamePiece> gamePieces = game.getStatus().getCurrentTurnPlayer().getGamePieces();
                Collections.shuffle(gamePieces);
                for (se.stromvap.royal.game.of.ur.model.GamePiece gamePiece : gamePieces) {
                    if (GameUtil.canMove(game, gamePiece)) {
                        return gamePiece;
                    }
                }

                throw new IllegalStateException("Should not get to here");
            }

            @Override
            public void gameOver(se.stromvap.royal.game.of.ur.model.Player winner) {

            }
        });
    }

    public static void playRoyalGameOfUr(String host, int port, RemoteAi remoteAi) {
        ManagedChannel channel = ManagedChannelBuilder.
                forAddress(host, port).
                usePlaintext(true).
                build();

        UrServiceGrpc.UrServiceStub stub = UrServiceGrpc.newStub(channel);

        log.info("Joining a lobby!");
        stub.join(Player.newBuilder().setName(remoteAi.getName()).build(), new StreamObserver<Game>() {
            @Override
            public void onNext(Game game) {
                se.stromvap.royal.game.of.ur.model.Game gameModel = GrpcGameMapper.map(game);

                se.stromvap.royal.game.of.ur.model.Player winner = GameUtil.isAnyPlayerAWinner(gameModel);
                if (winner != null) {
                    remoteAi.gameOver(winner);
                    return;
                }

                se.stromvap.royal.game.of.ur.model.GamePiece gamePiece = remoteAi.yourTurn(gameModel);
                stub.move(GameMapper.map(game.getStatus().getCurrentTurnPlayer(), gamePiece), ignore());
            }

            @Override
            public void onError(Throwable throwable) {
                log.error("Got error from server", throwable);
            }

            @Override
            public void onCompleted() {
                log.info("Done, someone won...");
                channel.shutdown();
            }
        });

        log.info("Waiting for another player...");

        try {
            channel.awaitTermination(120, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            throw new IllegalStateException("Can't wait for termination", e);
        }
        channel.shutdown();
    }

    private static StreamObserver<Empty> ignore() {
        return new StreamObserver<Empty>() {
            @Override
            public void onNext(Empty empty) {

            }

            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onCompleted() {

            }
        };
    }
}