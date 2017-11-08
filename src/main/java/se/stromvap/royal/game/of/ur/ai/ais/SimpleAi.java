package se.stromvap.royal.game.of.ur.ai.ais;

import se.stromvap.royal.game.of.ur.GameUtil;
import se.stromvap.royal.game.of.ur.ai.Ai;
import se.stromvap.royal.game.of.ur.ai.AiUtil;
import se.stromvap.royal.game.of.ur.model.EndTile;
import se.stromvap.royal.game.of.ur.model.FlowerTile;
import se.stromvap.royal.game.of.ur.model.Game;
import se.stromvap.royal.game.of.ur.model.GamePiece;
import se.stromvap.royal.game.of.ur.model.SafeTile;

import java.util.List;

public class SimpleAi implements Ai {

    @Override
    public int myTurn(Game game) {
        GamePiece gamePiece;

        gamePiece = AiUtil.canKnockOutOpponent(game);
        if (gamePiece != null) {
            return gamePiece.getId();
        }

        gamePiece = AiUtil.canMoveAnyGamePieceTo(game, FlowerTile.class);
        if (gamePiece != null) {
            return gamePiece.getId();
        }

        gamePiece = AiUtil.canMoveAnyGamePieceTo(game, SafeTile.class);
        if (gamePiece != null) {
            return gamePiece.getId();
        }

        gamePiece = AiUtil.canMoveAnyGamePieceTo(game, EndTile.class);
        if (gamePiece != null) {
            return gamePiece.getId();
        }

        List<GamePiece> gamePieces = game.getStatus().getCurrentTurnPlayer().getGamePieces();
        for (GamePiece gp : gamePieces) {
            if (GameUtil.canMove(game, gp)) {
                return gp.getId();
            }
        }

        throw new IllegalStateException("Bad game! Don't ask me to move when i can't!");
    }
}
