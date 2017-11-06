package se.stromvap.royal.game.of.ur.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.stromvap.royal.game.of.ur.Board;
import se.stromvap.royal.game.of.ur.Game;
import se.stromvap.royal.game.of.ur.GameEngine;
import se.stromvap.royal.game.of.ur.GamePiece;
import se.stromvap.royal.game.of.ur.Player;
import se.stromvap.royal.game.of.ur.rest.model.MoveResponse;
import se.stromvap.royal.game.of.ur.rest.model.RollResponse;

import java.util.stream.Collectors;

@RestController
public class UrRest {
    private static final Logger log = LoggerFactory.getLogger(UrRest.class);

    private static GameEngine gameEngine = new GameEngine();

    @RequestMapping("/new-game")
    public Game newGame() {
        return gameEngine.newGame();
    }

    @RequestMapping("/game")
    public Game getGame() {
        return gameEngine.getGame();
    }

    @RequestMapping("/board")
    public Board getBoard() {
        return gameEngine.getGame().getBoard();
    }

    @RequestMapping("/roll")
    public RollResponse roll() {
        int roll = gameEngine.roll();

        RollResponse rollResponse = new RollResponse();
        rollResponse.setRoll(roll);
        rollResponse.setPlayer(gameEngine.getCurrentTurnPlayer().getName());

        if (!gameEngine.getStatus().hasMoved()) {
            rollResponse.setMovableGamePieces(gameEngine.getCurrentTurnPlayer().getGamePieces().stream().filter(gameEngine::canMove).collect(Collectors.toList()));
        }

        return rollResponse;
    }

    @RequestMapping("/move")
    public MoveResponse move() {
        moveRandom();
        return new MoveResponse(gameEngine.getCurrentTurnPlayer().getName());
    }

    @PostMapping("/move/{gamePieceId}")
    public MoveResponse move(@PathVariable("gamePieceId") int gamePieceId) {
        gameEngine.move(gamePieceId);
        return new MoveResponse(gameEngine.getCurrentTurnPlayer().getName());
    }

    @RequestMapping("/play")
    public String play() {
        Player winningPlayer;
        while ((winningPlayer = gameEngine.isAnyPlayerAWinner()) == null) {
            gameEngine.roll();
            moveRandom();
        }

        return winningPlayer.getName() + " won!";
    }

    private void moveRandom() {
        if (gameEngine.getStatus().hasMoved()) {
            return;
        }

        for (GamePiece gamePiece : gameEngine.getCurrentTurnPlayer().getGamePieces()) {
            if (gameEngine.canMove(gamePiece)) {
                gameEngine.move(gamePiece);
                return;
            }
        }
    }
}
