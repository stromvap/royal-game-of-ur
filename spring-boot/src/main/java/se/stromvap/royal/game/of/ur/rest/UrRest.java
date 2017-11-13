package se.stromvap.royal.game.of.ur.rest;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.stromvap.royal.game.of.ur.GameEngine;
import se.stromvap.royal.game.of.ur.GameUtil;
import se.stromvap.royal.game.of.ur.model.Board;
import se.stromvap.royal.game.of.ur.model.Game;
import se.stromvap.royal.game.of.ur.model.GamePiece;
import se.stromvap.royal.game.of.ur.model.Player;
import se.stromvap.royal.game.of.ur.rest.model.MoveResponse;
import se.stromvap.royal.game.of.ur.rest.model.RollResponse;

import java.util.stream.Collectors;

@RestController
public class UrRest {
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
            rollResponse.setMovableGamePieces(gameEngine.getCurrentTurnPlayer().getGamePieces().stream().filter(gp -> GameUtil.canMove(getGame(), gp)).collect(Collectors.toList()));
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
        while ((winningPlayer = GameUtil.isAnyPlayerAWinner(gameEngine.getGame())) == null) {
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
            if (GameUtil.canMove(gameEngine.getGame(), gamePiece)) {
                gameEngine.move(gamePiece);
                return;
            }
        }
    }
}
