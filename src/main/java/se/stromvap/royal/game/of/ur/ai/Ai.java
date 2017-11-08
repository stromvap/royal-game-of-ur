package se.stromvap.royal.game.of.ur.ai;

import se.stromvap.royal.game.of.ur.GameUtil;
import se.stromvap.royal.game.of.ur.model.Game;
import se.stromvap.royal.game.of.ur.model.GamePiece;

import java.util.Collections;
import java.util.List;

public interface Ai {
    /**
     * This method will trigger when it's your turn to move and should return an int of the GamePiece you want to move.
     *
     * @param game The current game state
     * @return the id of the GamePiece you want to move
     */
    default int myTurn(Game game) {
        List<GamePiece> gamePieces = game.getStatus().getCurrentTurnPlayer().getGamePieces();
        Collections.shuffle(gamePieces);
        for (GamePiece gamePiece : gamePieces) {
            if (GameUtil.canMove(game, gamePiece)) {
                return gamePiece.getId();
            }
        }

        throw new IllegalStateException("Bad game! Don't ask me to move when i can't!");
    }
}
