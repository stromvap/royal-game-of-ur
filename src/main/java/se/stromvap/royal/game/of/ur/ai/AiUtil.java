package se.stromvap.royal.game.of.ur.ai;

import se.stromvap.royal.game.of.ur.GameUtil;
import se.stromvap.royal.game.of.ur.model.Game;
import se.stromvap.royal.game.of.ur.model.GamePiece;
import se.stromvap.royal.game.of.ur.model.Tile;

import java.util.List;
import java.util.function.Function;

public class AiUtil {

    public static GamePiece canMoveAnyGamePieceTo(Game game, Class<?> tileClass) {
        return canMoveAnyGamePieceTo(game, tileClass::isInstance);
    }

    public static GamePiece canKnockOutOpponent(Game game) {
        return canMoveAnyGamePieceTo(game, t -> t.getGamePiece() != null && t.getGamePiece().getPlayer() != game.getStatus().getCurrentTurnPlayer());
    }

    public static GamePiece canMoveAnyGamePieceTo(Game game, Function<Tile, Boolean> acceptableTile) {
        for (GamePiece gamePiece : game.getStatus().getCurrentTurnPlayer().getGamePieces()) {
            if (!GameUtil.canMove(game, gamePiece)) {
                continue;
            }

            List<Tile> tiles = game.getBoard().getTiles().get(game.getStatus().getCurrentTurnPlayer());
            for (int i = 0; i < tiles.size(); i++) {
                Tile tile = tiles.get(i);

                if (gamePiece == tile.getGamePiece()) {
                    // Check if the game piece will be moved out of bounds
                    if (i + game.getStatus().getLatestRoll() >= tiles.size()) {
                        continue;
                    }

                    // Get the new tile
                    Tile newTile = tiles.get(i + game.getStatus().getLatestRoll());

                    // Check if we can move to the new tile
                    if (!GameUtil.canMoveTo(game, newTile)) {
                        continue;
                    }

                    if (acceptableTile.apply(newTile)) {
                        return gamePiece;
                    }
                }
            }

            // If we get to here, the game piece we are checking to move is not on the board
            Tile newTile = tiles.get(game.getStatus().getLatestRoll() - 1);

            // Check if we can move to the new tile
            if (!GameUtil.canMoveTo(game, newTile)) {
                continue;
            }

            if (acceptableTile.apply(newTile)) {
                return gamePiece;
            }
        }

        return null;
    }
}
