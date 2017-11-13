package se.stromvap.royal.game.of.ur.grpc;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class UrGrpcServer {
    private static final Logger log = LoggerFactory.getLogger(UrGrpcServer.class);

    public static void main(String[] args) throws InterruptedException, IOException {
        Server server = ServerBuilder.forPort(8081).addService(new UrService()).build();
        server.start();

        log.info("Lobby is running. Waiting for players...");

        server.awaitTermination();
    }
}