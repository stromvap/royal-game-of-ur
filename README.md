# royal-game-of-ur

A very simple implementation of the Royal Game of Ur (https://en.wikipedia.org/wiki/Royal_Game_of_Ur)

Can be run in three different modes:
1. REST server
2. gRPC server
3. Standalone

## How to run the REST server

`mvn clean package && java -jar spring-boot/target/spring-boot-0.0.1-SNAPSHOT.jar`

To start a new game:</br>
`GET http://localhost:8080/new-game`

To get the game-state:</br>
`GET http://localhost:8080/game`

To get the board-state:</br>
`GET http://localhost:8080/board`

To roll the 'dice':</br>
`GET http://localhost:8080/roll`

To move a specific game piece:</br>
`POST http://localhost:8080/move/{gamePieceId}`

To move a random game piece:</br>
`GET http://localhost:8080/move`

To make the game play itself out by rolling and moving random game pieces until one player has won:</br>
`GET http://localhost:8080/play`

## How to run the gRPC server and client

### Server

Find `UrGrpcServer` and run it. Now the server has started on server `localhost:8081`

### Client

Download the code and run `mvn clean install`

Add this Maven dependency:
```
<dependency>
    <groupId>se.stromvap.royal.game.of.ur</groupId>
    <artifactId>grpc</artifactId>
    <version>0.0.1-SNAPSHOT</version>
</dependency>
```

Implement your AI by using the interface `RemoteAi`. Start the client with the code below.
(_Note that you need two players before the game will start_)

```
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
```

## How to run AIs in Standalone mode

Create a class that implements `se.stromvap.royal.game.of.ur.ai.Ai`.

Go to `se.stromvap.royal.game.of.ur.ai.AiArenaMain` and use your new AI against another AI.

Currently the `SimpleAi` has a win rate of 95% vs `RandomAi`.

## TODO
TODO: Create UI