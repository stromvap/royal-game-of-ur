package se.stromvap.royal.game.of.ur;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.stromvap.royal.game.of.ur.model.FlowerTile;
import se.stromvap.royal.game.of.ur.model.Game;
import se.stromvap.royal.game.of.ur.model.GamePiece;
import se.stromvap.royal.game.of.ur.model.Player;
import se.stromvap.royal.game.of.ur.model.Tile;

import java.util.List;

public class GameUtil {
    private static final Logger log = LoggerFactory.getLogger(GameUtil.class);

    public static boolean canMove(Game game, GamePiece gamePiece) {
        List<Tile> tiles = game.getBoard().getTiles().get(game.getStatus().getCurrentTurnPlayer());
        for (int i = 0; i < tiles.size(); i++) {
            Tile tile = tiles.get(i);

            if (gamePiece == tile.getGamePiece()) {
                // We found the game piece on the board!

                // Check if the game piece will be moved out of bounds
                if (i + game.getStatus().getLatestRoll() >= tiles.size()) {
                    return false;
                }

                // Get the new tile
                Tile newTile = tiles.get(i + game.getStatus().getLatestRoll());

                // Check if we can move to the new tile
                return canMoveTo(game, newTile);
            }
        }

        // If we get to here, the game piece we are checking to move is not on the board
        Tile newTile = tiles.get(game.getStatus().getLatestRoll() - 1);
        return canMoveTo(game, newTile);
    }

    public static boolean canMoveTo(Game game, Tile newTile) {
        if (newTile.getGamePiece() != null && newTile.getGamePiece().getPlayer() == game.getStatus().getCurrentTurnPlayer()) {
            // Can't move to a tile you already have a game piece at
            return false;
        }

        if (newTile instanceof FlowerTile && newTile.getGamePiece() != null && newTile.getGamePiece().getPlayer() != game.getStatus().getCurrentTurnPlayer()) {
            // Can't move to a flower tile with an opponents game piece
            return false;
        }

        return true;
    }

    public static Player isAnyPlayerAWinner(Game game) {
        if (game.getPlayer1().getGamePieces().isEmpty()) {
            log.info("{} won!", game.getPlayer1().getName());
            return game.getPlayer1();
        }

        if (game.getPlayer2().getGamePieces().isEmpty()) {
            log.info("{} won!", game.getPlayer2().getName());
            return game.getPlayer2();
        }

        return null;
    }
}
